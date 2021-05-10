package com.muchen.pdfreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FileInfoActivity extends AppCompatActivity {
    private TextView title;//文件名
    private TextView info;//文件内容
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_info);
        title=(TextView)findViewById(R.id.pdf_file_title) ;
        info=(TextView)findViewById(R.id.pdf_file_info) ;
        Intent i = getIntent();
        Log.e("文件内容",i.getStringExtra("info"));
        title.setText(i.getStringExtra("title"));
        info.setText(i.getStringExtra("info"));
    }
}
