package com.example.charl.audiorecorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ShowPartition extends AppCompatActivity {

    private Button Delete;
    private Button Rename;
    private String outputFile;
    private File imgFile;
    private File audioFile;
    private ImageView play;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private TextView time;
    private boolean playing = false;
    private Handler handler;
    private Runnable runnable;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_partition);
        Delete = (Button) findViewById(R.id.delete);
        Rename = (Button) findViewById(R.id.rename);
        play = (ImageView) findViewById(R.id.play);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        time= (TextView) findViewById(R.id.time);
        mediaPlayer = new MediaPlayer();
        handler= new Handler();



        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording/"+name+".m4a";
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            time.setText("00:00/"+getTimeString(mediaPlayer.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioFile = new File(outputFile);
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
                audioFile.delete();
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
                        File newAudioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording/"+srt1+".m4a");

                        imgFile.renameTo(newFile);
                        audioFile.renameTo(newAudioFile);


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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing==false)
                {
                    try {
                        mediaPlayer.start();
                        playing = true;
                        seekBar.setMax(mediaPlayer.getDuration());
                        changeSeekBar();
                        Toast.makeText(getApplicationContext(), "Joue audio", Toast.LENGTH_LONG).show();
                        play.setImageResource(R.drawable.pause);
                    } catch (Exception e) {
                        // make something
                    }
                }
                else{
                    mediaPlayer.pause();
                    playing=false;
                    play.setImageResource(R.drawable.play);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void changeSeekBar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        time.setText(getTimeString(mediaPlayer.getCurrentPosition())+"/"+getTimeString(mediaPlayer.getDuration()));
        if(mediaPlayer.isPlaying()){
            runnable= new Runnable(){
                @Override
                public void run() {
                    changeSeekBar();
                }
            };
            handler.postDelayed(runnable, 10);
        }

        if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()){
            seekBar.setProgress(0);
            time.setText("00:00/"+getTimeString(mediaPlayer.getDuration()));
            play.setImageResource(R.drawable.play);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playing=false;
        }
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000*60*60);
        long minutes = ( millis % (1000*60*60) ) / (1000*60);
        long seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }




}
