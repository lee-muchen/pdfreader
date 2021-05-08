package com.muchen.pdfreader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.muchen.pdfreader.adapter.MyAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PDFtoTXTActivity extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton;
    private ImageView imageView;
    private TextView textView;
    private TextView pdfView;
    List<String> list;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_list);
//        init();
        listView = (ListView) findViewById(R.id.pdf_item_list);
        imageView = (ImageView) findViewById(R.id.pdf_item_seq);//文件图标
        textView = (TextView) findViewById(R.id.pdf_item_name);//文件名
        pdfView=(TextView) findViewById(R.id.pdf_path);//路径
        imageButton=(ImageButton) findViewById(R.id.pdf_turn);//返回上一级按钮
        list = new ArrayList<String>();
        File path = Environment.getRootDirectory();
//        Log.e("cwj", "手机内存根目录路径  = " + path);
        pdfView.setText(path.toString());
        getAllFile(path);
        adapter = new MyAdapter(PDFtoTXTActivity.this, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                String path = list.get(position);
                pdfView.setText(path);
                File file = new File(path);
                getAllFile(file);
                adapter.setList(list);
                listView.setAdapter(adapter);
                }
        });
        //返回上一级点击事件
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfView.getText().equals(Environment.getRootDirectory().toString())) {
                    Toast.makeText(PDFtoTXTActivity.this, "已经是根目录",Toast.LENGTH_LONG).show();
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
 //系统返回键添加监听事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (textView.getText().equals(Environment.getRootDirectory().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PDFtoTXTActivity.this);
                builder.setMessage("确认退出吗?");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确认后退出程序
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                imageButton.setClickable(true);
                String str = textView.getText().toString();
                File file = new File(str);
                File fileParentPath = file.getParentFile();
                textView.setText(fileParentPath.toString());
                getAllFile(fileParentPath);
                adapter.setList(list);
                listView.setAdapter(adapter);
            }
            return true;
        }
        return false;
    }

 //遍历文件夹
    public void getAllFile(File dir) {
        File[] file = dir.listFiles();
        Log.e("cwj", "长度  = " + file.length);
        if (file.length < 1) {
            return;
        }
        list.clear();
        for (int i = 0; i < file.length; i++) {
            list.add(file[i].getAbsolutePath());
        }
    }
}