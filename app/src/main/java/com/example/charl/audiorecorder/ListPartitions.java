package com.example.charl.audiorecorder;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ListPartitions extends AppCompatActivity {

    ArrayList<String> files;
    ListView listView ;
    Button recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_partitions);
        listView = (ListView) findViewById(R.id.listView);
        recorder = (Button) findViewById(R.id.recorder);
        getList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) listView.getItemAtPosition(position);
                Intent intent = new Intent(ListPartitions.this, ShowPartition.class);
                intent.putExtra("name", clickedItem);
                startActivity(intent);
            }
        });


        recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListPartitions.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getList() {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/partitions");
        File file[] = f.listFiles();
        files = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            String fi = file[i].getName();
            fi = fi.substring(0, fi.length() - 4);
            files.add(fi);
        }
    }
}
