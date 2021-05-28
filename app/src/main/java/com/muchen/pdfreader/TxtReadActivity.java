package com.muchen.pdfreader;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.muchen.pdfreader.utils.MyTTSUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TxtReadActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText et_text;
    private TextToSpeech tts;
    private ImageButton btn;
    MyTTSUtils mttsu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txt_read);
        mttsu=new MyTTSUtils();
        tts = new TextToSpeech(this, this);
        et_text=(EditText) findViewById(R.id.ex_text);
        btn=(ImageButton)findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mttsu.play(et_text.getText().toString(),tts);
            }
        });
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
    //    系统返回键添加监听事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent inte = new Intent(TxtReadActivity.this,MainActivity.class);
            startActivity(inte);
            finish();
        } else {
        }
        return true;
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
