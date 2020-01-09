package com.example.charl.audiorecorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ShowPartition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_partition);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        File imgFile = new  File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/partitions/"+name+".png");
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.partition);

            myImage.setImageBitmap(myBitmap);

        };
    }

}
