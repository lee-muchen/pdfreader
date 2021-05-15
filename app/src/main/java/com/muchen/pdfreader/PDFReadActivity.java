package com.muchen.pdfreader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.muchen.pdfreader.adapter.MyAdapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PDFReadActivity extends AppCompatActivity {
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
//        init();
        listView = (ListView) findViewById(R.id.pdf_item_list);
//        imageView = (ImageView) findViewById(R.id.pdf_item_seq);//文件图标
        textView = (TextView) findViewById(R.id.pdf_item_name);//文件名
        pdfView=(TextView) findViewById(R.id.pdf_path);//路径
        imageButton=(ImageButton) findViewById(R.id.pdf_turn);//返回上一级按钮
        list = new ArrayList<String>();
        File path =Environment.getExternalStorageDirectory();
//        File path=new File("/storage/emulated/0/Android");
        Log.e("cwj", "手机内存根目录路径  = " + path);
        pdfView.setText(path.toString());
        getAllFile(path);
        adapter = new MyAdapter(PDFReadActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
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
                    //读取pdf文件
                    Intent inte = new Intent(PDFReadActivity.this,FileInfoActivity.class);
                    inte.putExtra("info",readPdfToTxt(file.getAbsolutePath()));
                    inte.putExtra("title",file.getName());
                    startActivity(inte);
                }
                }
        });
        //返回上一级点击事件
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //getExternalStorageState()\getRootDirectory()
                if (pdfView.getText().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    Toast.makeText(PDFReadActivity.this, "已经是根目录",Toast.LENGTH_LONG).show();
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
// //系统返回键添加监听事件
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (pdfView.getText().equals(Environment.getExternalStorageState().toString())) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(PDFReadActivity.this);
//                builder.setMessage("确认退出吗?");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //点击确认后退出程序
//                        finish();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            } else {
//                imageButton.setClickable(true);
//                String str = textView.getText().toString();
//                File file = new File(str);
//                File fileParentPath = file.getParentFile();
//                pdfView.setText(fileParentPath.toString());
//                getAllFile(fileParentPath);
//                adapter.setList(list);
//                listView.setAdapter(adapter);
//            }
//            return true;
//        }
//        return false;
//    }

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
}