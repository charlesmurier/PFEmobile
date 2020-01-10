package com.example.charl.audiorecorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ShowPartition extends AppCompatActivity {

    private Button Delete;
    private Button Rename;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_partition);
        Delete = (Button) findViewById(R.id.delete);
        Rename = (Button) findViewById(R.id.rename);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        imgFile = new  File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/partitions/"+name+".png");
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.partition);

            myImage.setImageBitmap(myBitmap);

        };

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFile.delete();

                Intent intent = new Intent(ShowPartition.this, ListPartitions.class);
                startActivity(intent);
            }
        });
        Rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFile.delete();

                Intent intent = new Intent(ShowPartition.this, ListPartitions.class);
                startActivity(intent);
            }
        });
    }

}
