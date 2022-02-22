package com.example.audioapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
MediaPlayer mediaPlayer;
Button play,pause;
SeekBar seekBar;
AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer=MediaPlayer.create(this,R.raw.dog);

        play=findViewById(R.id.playAudio);
        pause=findViewById(R.id.pauseAudio);
        seekBar=findViewById(R.id.volumeControl);
        seekBar.setMax(maxVolume);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("TAG",Integer.toString(progress));
                   audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,1,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}