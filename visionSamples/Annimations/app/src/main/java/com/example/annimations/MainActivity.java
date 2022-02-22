package com.example.annimations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
ImageView imageV,imageH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageV=findViewById(R.id.imageViewCat);
        imageH=findViewById(R.id.imageViewDog);
        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fading or complete disappear animation
                //alpha 0 will completely remove image
                //time 2000 is in milliseconds

              //  imageV.animate().alpha(0).setDuration(2000);



                //Another animation
                //move to downward direction cat image


               // imageV.animate().translationYBy(1000).setDuration(2000);

                //to move element into left direction use minus sign
               // imageV.animate().translationXBy(-1000).setDuration(2000);

                //one more animation to rotate the image
               // imageV.animate().rotationYBy(1000).setDuration(2000);

                //rotate completely
                //imageV.animate().rotation(1800).setDuration(2000);

                //animation for scaling an image
              //  imageV.animate().scaleX(0.5f).scaleY(0.5f).setDuration(1000);
               //mix animation
                //first disappear image we can use by two ways first is
                //for instant animation no need to fix durations
             //   imageV.animate().translationXBy(-1000);
                //second way is
                imageV.setX(-1000);
              imageV.animate().translationXBy(1000).rotation(3600).setDuration(2000);

            }

        });


    }
}