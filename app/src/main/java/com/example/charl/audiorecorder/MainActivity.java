package com.example.charl.audiorecorder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Chronometer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private ImageView logo;
    private ImageView play;
    private ProgressBar loading;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private Chronometer chronometer;
    private Button Send;

    private boolean recording = false;
    private boolean playing = false;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        Send = (Button) findViewById(R.id.send);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        logo = (ImageView) findViewById(R.id.logo);
        play = (ImageView) findViewById(R.id.play);
        time= (TextView) findViewById(R.id.time);
        loading = (ProgressBar) findViewById(R.id.loading);
        mediaPlayer = new MediaPlayer();
        loading.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send sendcode = new send();
                sendcode.execute();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording == false){
                    try {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.m4a";
                        myAudioRecorder = new MediaRecorder();
                        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        myAudioRecorder.setOutputFile(outputFile);
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        // make something ...
                    } catch (IOException ioe) {
                        // make something
                    }
                    loading.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Enregistrement débuté", Toast.LENGTH_LONG).show();
                    recording = true;
                }
                else{
                    chronometer.stop();
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    play.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    recording = false;
                    Toast.makeText(getApplicationContext(), "Audio bien enregistré", Toast.LENGTH_LONG).show();
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        time.setText("00:00/"+getTimeString(mediaPlayer.getDuration()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }






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

    class send extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void...params){
            Socket sock = null;
            File myFile = new File(outputFile);
            try {
                sock = new Socket("192.168.43.210",8000);

                byte[] mybytearray = new byte[(int) myFile.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                bis.read(mybytearray, 0, mybytearray.length);
                OutputStream os = sock.getOutputStream();
                os.write(mybytearray, 0, mybytearray.length);
                System.out.println(mybytearray);
                os.flush();

/*
                System.out.println("111111111111111111");

                DataInputStream dis = new DataInputStream(sock.getInputStream());
                System.out.println("222222222222222");

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fileName.jpg";
                File f = new File(filePath);

                FileOutputStream fos = new FileOutputStream(f, false);
                System.out.println("3333333333333");

                byte[] buffer = new byte[4096];
                System.out.println("4444444444444444");

                int filesize = 200123000; // Send file size in separate msg
                System.out.println("555555555555555555");

                int read = 0;
                System.out.println("66666666666666");

                int totalRead = 0;
                System.out.println("777777777777");

                int remaining = filesize;

                System.out.println("8888888888");
                while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }
                System.out.println("99999999999999");


                fos.close();
                dis.close();

*/

                sock.close();

            } catch (UnknownHostException e) {
                System.out.println("Fail");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Fail2");
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000) ;
            }  catch (InterruptedException e) {
                // gestion de l'erreur
            }
            receive receivecode = new receive();
            receivecode.execute();
            return null;

        }

    }


    class receive extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void...params){
            Socket sock = null;
            File myFile = new File(outputFile);
            try {
                sock = new Socket("192.168.43.210",8001);
                System.out.println("111111111111111111");

                DataInputStream dis = new DataInputStream(sock.getInputStream());
                System.out.println("222222222222222");

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/result.png";
                File f = new File(filePath);

                FileOutputStream fos = new FileOutputStream(f, false);
                System.out.println("3333333333333");

                byte[] buffer = new byte[4096];
                System.out.println("4444444444444444");

                int filesize = 200123000; // Send file size in separate msg
                System.out.println("555555555555555555");

                int read = 0;
                System.out.println("66666666666666");

                int totalRead = 0;
                System.out.println("777777777777");

                int remaining = filesize;

                System.out.println("8888888888");
                while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }
                System.out.println("99999999999999");
                fos.close();
                dis.close();


                sock.close();
            } catch (UnknownHostException e) {
                System.out.println("Fail");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Fail2");
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, ShowPartition.class);
            startActivity(intent);
            return null;
        }
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
            handler.postDelayed(runnable, 500);
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

