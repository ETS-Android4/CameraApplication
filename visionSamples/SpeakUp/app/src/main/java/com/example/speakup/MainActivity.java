package com.example.speakup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText writeYourText;
    Button Speak;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeYourText=findViewById(R.id.editText);
        Speak=findViewById(R.id.button);
         textToSpeech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }

            }
        });
         Speak.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String conversionOfText=writeYourText.getText().toString();
                 Toast.makeText(MainActivity.this,conversionOfText,Toast.LENGTH_LONG).show();
                 textToSpeech.speak(conversionOfText,TextToSpeech.QUEUE_FLUSH,null);

             }
         });
    }
}