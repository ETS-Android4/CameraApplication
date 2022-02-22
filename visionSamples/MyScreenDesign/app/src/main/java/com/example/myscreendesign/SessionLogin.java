package com.example.myscreendesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SessionLogin extends AppCompatActivity {
    public static final String SHARED_PREFS="shared_pref";
    public static final String EMAIL_KEY="email_key";
    public static final String PASSWORD_KEY="password_key";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password;


    @Override
    protected void onStart() {
        super.onStart();
        if(email!=null&&password!=null){
            Intent i=new Intent(SessionLogin.this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_login);
        EditText emailEdit=findViewById(R.id.edit_login);
        EditText passEdit=findViewById(R.id.edit_pass);
        Button button=findViewById(R.id.btn_login);
        sharedpreferences=getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email=sharedpreferences.getString(EMAIL_KEY,null);
        password=sharedpreferences.getString(PASSWORD_KEY,null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailEdit.getText().toString())&&TextUtils.isEmpty(passEdit.getText().toString())){
                    Toast.makeText(SessionLogin.this,"please enter email id and password",Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences.Editor  editor=sharedpreferences.edit();
                    editor.putString(EMAIL_KEY,emailEdit.getText().toString());
                    editor.putString(PASSWORD_KEY,passEdit.getText().toString());
                    editor.apply();
                    Intent intent=new Intent(SessionLogin.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}