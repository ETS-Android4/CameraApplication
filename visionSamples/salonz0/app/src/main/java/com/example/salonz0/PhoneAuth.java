package com.example.salonz0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class PhoneAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        Bundle bundle=getIntent().getExtras();
        String contact_number=bundle.getString("mobile");
        Toast.makeText(this,contact_number,Toast.LENGTH_LONG).show();

    }
}