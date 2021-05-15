package com.muchen.pdfreader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.muchen.pdfreader.adapter.MyAdapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PDFToTxtActivity extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton;
    //    private ImageView imageView;
    private TextView textView;
    private TextView pdfView;
    private File path;
    ArrayList<String> list;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_list);
        listView = (ListView) findViewById(R.id.pdf_item_list);
        textView = (TextView) findViewById(R.id.pdf_item_name);//文件名
        pdfView=(TextView) findViewById(R.id.pdf_path);//路径
        imageButton=(ImageButton) findViewById(R.id.pdf_turn);//返回上一级按钮
        list = new ArrayList<String>();
        File path = Environment.getExternalStorageDirectory();
        Log.e("cwj", "手机内存根目录路径  = " + path);
        pdfView.setText(path.toString());
        getAllFile(path);
        adapter = new MyAdapter(PDFToTxtActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = list.get(position);
                pdfView.setText(path);
                File file = new File(path);
//                Log.e()
                if(file.isDirectory()){//文件夹
                    getAllFile(file);
                    adapter.setList(list);
                    listView.setAdapter(adapter);
                }else {
                    //pdf转换TXT并保存
                    showDialog(file);
//                    writeData(file.getAbsolutePath(),file.getName(),readPdfToTxt(file.getAbsolutePath()));

                }
            }
        });
        //返回上一级点击事件
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getExternalStorageState()\getRootDirectory()
                if (pdfView.getText().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    Toast.makeText(PDFToTxtActivity.this, "已经是根目录",Toast.LENGTH_LONG).show();
                } else {
                    imageButton.setClickable(true);
                    String str = pdfView.getText().toString();
                    File file = new File(str);
                    File fileParentPath = file.getParentFile();
                    pdfView.setText(fileParentPath.toString());
                    getAllFile(fileParentPath);
                    adapter.setList(list);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    //遍历文件夹
    public void getAllFile(File dir) {
//        getPermission();
        Log.e("cwj", "文件ming= " + dir);
        File[] file = dir.listFiles();
        Log.e("cwj", "文件  = " + file);
        if (file==null || file.length<1) {
            return;
        }
        list.clear();
        for (int i = 0; i < file.length; i++) {
            list.add(file[i].getAbsolutePath());
        }
    }
    //读取pdf文件内容
    private String readPdfToTxt(String pdfPath) {
        PdfReader reader = null;
        StringBuffer buff = new StringBuffer();
        try {
            reader = new PdfReader(pdfPath);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            int num = reader.getNumberOfPages();// 获得页数
            TextExtractionStrategy strategy;
            for (int i = 1; i <= num; i++) {
                strategy = parser.processContent(i,new SimpleTextExtractionStrategy());
                buff.append(strategy.getResultantText());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return buff.toString();
    }
    //获取SD卡路径
    public String getSDPath(){
        String sdcard=Environment.getExternalStorageState();
        if(sdcard.equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            return null;
        }
    }
    //获取基本路径
    public String getBasePath(){
        String basePath=getSDPath();
        if(basePath==null){
            return Environment.getDataDirectory().getAbsolutePath();
        }else{
            return basePath;
        }
    }
    void getPermission()
    {
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 124) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                Log.d("heihei","获取到权限了！");
//            path = new File(path.toString());//初始化File对象
                File [] files = path.listFiles();//噩梦结束了吗？
            }        else        {            Log.d("heihei","搞不定啊！");        }    }}

    public static void writeData(String url, String name, String content) {
        String filePath = url;
        int dot = name.lastIndexOf('.');
        if ((dot >-1) && (dot < (name.length()))) {
            name=name.substring(0, dot);
        }
        String fileName = name + ".txt";
        writeTxtToFile(content, filePath, fileName);
    }

    // 将字符串写入到文本文件中
    private static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //生成文件
    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            //不存在就新建
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
    public void showDialog(final File file){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否保存为txt格式");
        builder.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filepath=file.getAbsolutePath();
                        String name=file.getName();
                        writeData(filepath,name,readPdfToTxt(filepath));
                    }
                });
        builder.setNegativeButton("否",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

}
