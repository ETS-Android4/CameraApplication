package com.example.androidtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView myListView;
    private static String FILE_NAME="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListView=findViewById(R.id.myListView);
        ArrayList<String> items=new ArrayList<>();
        items.add("Introduction");
        items.add("android tutorial one");
        items.add("android tutorial two");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              String text= String.valueOf(l);
              if(text.equals("0")){
                  FILE_NAME="android_tutorial.pdf";
                  Toast.makeText(MainActivity.this,"hello world",Toast.LENGTH_LONG).show();
                  Intent in = new Intent(MainActivity.this, Tutorial.class);
                  in.putExtra("key",FILE_NAME);
                  startActivity(in);
              }
              else if(text.equals("1")){
                  FILE_NAME="AndroidNotesForProfessionals.pdf";
                  Toast.makeText(MainActivity.this,"hello world",Toast.LENGTH_LONG).show();
                  Intent in = new Intent(MainActivity.this, Tutorial.class);
                  in.putExtra("key",FILE_NAME);
                  startActivity(in);
              }


            }
        });
    }
}