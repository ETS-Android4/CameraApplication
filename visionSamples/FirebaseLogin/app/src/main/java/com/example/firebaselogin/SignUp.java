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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText emailTextView,passwordTextView;
    private Button signUp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       // FirebaseApp.initializeApp(this);
        // taking FirebaseAuth instance
        mAuth=FirebaseAuth.getInstance();
        emailTextView=findViewById(R.id.editEmail);
        passwordTextView=findViewById(R.id.editPass);
        signUp=findViewById(R.id.regBtn);
        progressBar=findViewById(R.id.progressBar);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);
        String email,password;
        email=emailTextView.getText().toString();
        password=passwordTextView.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUp.this,"please enter email address",Toast.LENGTH_LONG).show();
            return;
        }if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUp.this,"please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"registration successful",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(SignUp.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignUp.this,"registration fail",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }
}