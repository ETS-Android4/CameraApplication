package com.example.gmailsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().
                build();
        GoogleApiClient googleApiClient=new GoogleApiClient.Builder(this).
                enableAutoManage(this,this).
                addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        signInButton=findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,connectionResult.getErrorMessage().toString(),Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult googleSignInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(googleSignInResult);

        }
    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if(googleSignInResult.isSuccess()){
            Intent intent=new Intent(MainActivity.this,Profile.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this,googleSignInResult.toString(),Toast.LENGTH_LONG).show();

        }
    }
}