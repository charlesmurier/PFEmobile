package com.example.charl.audiorecorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        ShowPartition.this);
                alert.setTitle("Renommer");

                final EditText input = new EditText(ShowPartition.this);
                alert.setView(input);


                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String srt1 = input.getEditableText().toString();
                        File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/partitions/"+srt1+".png");
                        imgFile.renameTo(newFile);

                        Intent intent = new Intent(ShowPartition.this, ListPartitions.class);
                        startActivity(intent);

                    }
                });

                alert.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });
    }

}
