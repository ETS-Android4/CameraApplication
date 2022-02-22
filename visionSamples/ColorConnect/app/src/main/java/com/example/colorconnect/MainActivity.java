package com.example.colorconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {
    //active player is 1 for red and 0 for yellow and 2 is for empty
    int[] gameState={2,2,2,2,2,2,2,2,2};
    int[][] winningPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{2,5,8},{1,4,7},{0,4,8},{2,4,6}};
    int activePlayer=0;
    boolean gameActive=true;
    public void dropIn(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());


        if(gameState[tappedCounter]==2 &&gameActive) {
            gameState[tappedCounter] = activePlayer;

            //Log.i(tag:"TAG",counter.getTag().toString());
            counter.setTranslationY(-1500);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;

            }

            counter.animate().translationYBy(1500).setDuration(300).rotation(3600);

            for (int winningPosition[] : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    String winner = "";
                    gameActive = false;
                    if (activePlayer == 1) {
                        winner = "yellow";

                    } else {
                        winner = "red";
                    }
                    Toast.makeText(this, winner + "has won", Toast.LENGTH_LONG).show();
                    Button playAgainButton = findViewById(R.id.playAgainButton);
                    TextView winnerTextView = findViewById(R.id.winnertextView);
                    LottieAnimationView animationView = findViewById(R.id.animation);
                    winnerTextView.setText(winner + " has won");
                    playAgainButton.setVisibility(View.VISIBLE);
                    winnerTextView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                }

                else{
                  Toast.makeText(this,"play again",Toast.LENGTH_LONG).show();
                }

            }
        }
      


        }
        public void playAgain(View view){
            Button playAgainButton=findViewById(R.id.playAgainButton);
            TextView winnerTextView=findViewById(R.id.winnertextView);
            LottieAnimationView animationView=findViewById(R.id.animation);
            animationView.setVisibility(View.INVISIBLE);
            playAgainButton.setVisibility(View.INVISIBLE);
            winnerTextView.setVisibility(View.INVISIBLE);
            GridLayout gridLayout=(GridLayout)findViewById(R.id.gridLayout);
            for(int i=0;i<gridLayout.getChildCount();i++){
                ImageView counter=(ImageView)gridLayout.getChildAt(i);
                counter.setImageDrawable(null);

            }
            for(int i=0; i<gameState.length;i++) {
               gameState[i]=2;

            }
             activePlayer = 0;
             gameActive = true;

        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}