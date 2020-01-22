package com.example.charl.audiorecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ShowPDF extends AppCompatActivity {

    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/result.pdf");
        pdfView.fromFile(newFile).load();
    }
}
