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

public class PDFtoTXTActivity extends Activity {
    private ListView listView;
    private ImageButton imageButton;
    private ImageView imageView;
    private TextView textView;
    List<String> list;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_list);
        init();
        list = new ArrayList<String>();
        File path = Environment.getRootDirectory();
        textView.setText(path.toString());
        getAllFile(path);
        adapter = new MyAdapter(PDFtoTXTActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                String path = list.get(position);
                textView.setText(path);
                File file = new File(path);
                getAllFile(file);
                adapter.setList(list);
                listView.setAdapter(adapter);
                }
        });
//        imageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (textView.getText().equals(Environment.getRootDirectory().toString())) {
//                    Toast.makeText(PDFtoTXTActivity.this, "已经是根目录",Toast.LENGTH_LONG).show();
//            } else {
//                    imageView.setClickable(true);
//                    String str = textView.getText().toString();
//                    File file = new File(str);
//                    File fileParentPath = file.getParentFile();
//                    textView.setText(fileParentPath.toString());
//                    getAllFile(fileParentPath);
//                    adapter.setList(list);
//                    listView.setAdapter(adapter);
//                }
//            }
//        });
    }
 //系统返回键添加监听事件
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (textView.getText().equals(Environment.getRootDirectory().toString())) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(PDFtoTXTActivity.this);
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
//                textView.setText(fileParentPath.toString());
//                getAllFile(fileParentPath);
//                adapter.setList(list);
//                listView.setAdapter(adapter);
//            }
//            return true;
//        }
//        return false;
//    }

    private void init() {
        listView = (ListView) findViewById(R.id.pdf_item_list);
        imageView = (ImageView) findViewById(R.id.pdf_item_seq);
        textView = (TextView) findViewById(R.id.pdf_item_name);
    }
 //遍历文件夹
    public void getAllFile(File dir) {
        File[] file = dir.listFiles();
        if (file.length < 1) {
            return;
        }
        list.clear();
        for (int i = 0; i < file.length; i++) {
            list.add(file[i].getAbsolutePath());
        }
    }
}