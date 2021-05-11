package com.muchen.pdfreader;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.muchen.pdfreader.utils.MyTTSUtils;

import java.util.Locale;

public class FileInfoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView title;//文件名
    private TextView info;//文件内容
    private Button spbtn;//阅读按钮
    private TextToSpeech tts;
    private String msg;
    MyTTSUtils mttsu;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_info);
        tts=new TextToSpeech(this, this);
        mttsu=new MyTTSUtils();
        title=(TextView)findViewById(R.id.pdf_file_title) ;
        info=(TextView)findViewById(R.id.pdf_file_info) ;
        spbtn=(Button) findViewById(R.id.speakbtn);
        Intent i = getIntent();
        Log.e("文件内容",i.getStringExtra("info"));
        msg=i.getStringExtra("info");
        title.setText(i.getStringExtra("title"));
        info.setText(i.getStringExtra("info"));
        spbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mttsu.play(msg,tts);
        }});
    }
    @Override
    public void onInit(int status) {
        //判断tts回调是否成功
        if (status == TextToSpeech.SUCCESS) {
            int result1 = tts.setLanguage(Locale.US);
            int result2 = tts.setLanguage(Locale.CHINESE);
            if (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED
                    || result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
