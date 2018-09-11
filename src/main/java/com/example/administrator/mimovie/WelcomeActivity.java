package com.example.administrator.mimovie;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    /*倒计时开始*/
    private int recLen = 3;
    private TextView textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initListener();
        initProgress();
    }

    private void initView() {
        textCount = findViewById(R.id.textView_welcome_count);
    }

    private void initListener() {

    }
    private void initProgress() {
        handler.postDelayed(runnable, 1000);
    }


    /*倒计时*//*线程*/
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            textCount.setText(" " + recLen + " ");
            handler.postDelayed(this, 1000);
            /*判定*/
            if (recLen==0)
            {
                finish();
                Intent intent_login = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent_login);
            }
        }
    };
}

