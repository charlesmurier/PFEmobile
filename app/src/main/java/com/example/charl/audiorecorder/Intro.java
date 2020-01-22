package com.example.charl.audiorecorder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        this.getSupportActionBar().hide();

        Handler h =new Handler() ;
        h.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Intro.this, MainActivity.class);
                startActivity(intent);

            }

        }, 2300);
    }

}
