package com.example.myscreendesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<InformationModel>list;
    List<String> titles= Arrays.asList("introduction","android tutorial one","android tutorial two");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for(int i=0;i<titles.size();i++){
            list = new ArrayList<>();
            String name=titles.get(i);
            list.add(new InformationModel(name));
            InformationAdapter adapter = new InformationAdapter(list,this);
            recyclerView.setAdapter(adapter);

            //setting adapter to recyclerview


        }

        //initializing the productlist

    }

}