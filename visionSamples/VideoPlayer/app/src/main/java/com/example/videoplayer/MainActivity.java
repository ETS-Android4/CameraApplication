package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        VideoView videoView=findViewById(R.id.videoView);
//        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.videoPlayer);
//        videoView.start();




        // let start making and play an audio sound
        MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.dog);
        mediaPlayer.start();


    }
}