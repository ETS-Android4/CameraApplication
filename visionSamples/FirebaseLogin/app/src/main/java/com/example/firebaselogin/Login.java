package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    private EditText editEmail,editPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        editEmail=findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPass);
        btnLogin=findViewById(R.id.loginBtn);
        progressBar=findViewById(R.id.progressBar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });
    }

    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);
       String email=editEmail.getText().toString();
       String password=editPassword.getText().toString();
       if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(password)){
           Toast.makeText(Login.this,"please enter email and password",Toast.LENGTH_LONG).show();
       }else{
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       progressBar.setVisibility(View.GONE);
                       Intent intent=new Intent(Login.this,MainActivity.class);
                       startActivity(intent);
                   }
                   else{
                       Toast.makeText(Login.this,"pleaselogin with your credentials",Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.GONE);
                   }

               }
           });
       }

    }
}