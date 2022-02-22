package com.example.salonz0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentProviderClient;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.salonz0.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
//    EditText phone_number;
//    Button send_otp;
//viewBinding
    private ActivityMainBinding binding;
    //if code send failed we useed to resend code
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG="MAIN_TAG";
    private FirebaseAuth firebaseAuth;

    private String mVerificationId; //will hold otp

    private ProgressDialog pd;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        phone_number=findViewById(R.id.phone_number);
//        send_otp=findViewById(R.id.send_otp);
//        send_otp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String number=phone_number.getText().toString();
////                Toast.makeText(MainActivity.this,number,Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(MainActivity.this,PhoneAuth.class);
//                intent.putExtra("mobile",number);
//                startActivity(intent);
//
//            }
//                                    }
//
//        );
//


        binding.phoneLl.setVisibility(View.VISIBLE); //show phone layout
        binding.codeLl.setVisibility(View.GONE);//hide code layout ,when otp sent then hide phone layout and show code layout
        firebaseAuth=FirebaseAuth.getInstance();

        //Init progress dialogue
        pd=new ProgressDialog(this);
        pd.setTitle("please wait...");
        pd.setCanceledOnTouchOutside(false);
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                //this callback will invoke in two situations
                //1.instant verification in some cases phone number can be
                // instantly verified without needing to send and entering a verification code
                //2. Auto retrieval in some devices google play service automatically  detect the incoming verification code and perform
               // verification without user action


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

// this callback is invoke in an invalid request for verification is made
                //for instance if the phone number is not valid
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                //sms verification code has been sent to the provided number
                //now need to ask the user to enter the code and construct credential
                //b combining the code with verification id
                Log.d(TAG," onCodeSent:"+ verificationId);
                mVerificationId=verificationId;
                forceResendingToken=token;
                pd.dismiss();
                //hide phone layout show code layout

                binding.phoneLl.setVisibility(View.GONE);
                binding.codeLl.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,"Verification code sent",Toast.LENGTH_LONG).show();
                binding.codeSentDescription.setText("please type the verification code we sent\n to"+binding.phoneEt.getText().toString().trim());
            }
        };
binding.phoneContinueButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String phone=binding.phoneEt.getText().toString().trim();
         if(TextUtils.isEmpty(phone)){
             Toast.makeText(MainActivity.this,"please enter phone number",Toast.LENGTH_LONG).show();

         }
         else{
             startPhoneNumberVerification(phone);
         }

    }
});
binding.resendCodeTv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String phone=binding.phoneEt.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(MainActivity.this,"please enter phone number",Toast.LENGTH_LONG).show();

        }
        else{
            resendVerificationCode(phone,forceResendingToken);
        }

    }
});
binding.codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String code=binding.codeEt.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            Toast.makeText(MainActivity.this,"please enter verification code",Toast.LENGTH_LONG).show();
        }
        else{
            verifyPhoneNumberWithCode(mVerificationId, code);
        }

    }
});
    }
    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("Verifying phone number...");
        pd.show();
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Redending code...");
        pd.show();
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pd.setMessage("verifying code");
        pd.show();
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pd.dismiss();//successfully login
                String phone=firebaseAuth.getCurrentUser().getPhoneNumber();
                Toast.makeText(MainActivity.this,"logged in as"+phone,Toast.LENGTH_LONG).show();
               //start profile Activity
               // startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//failed signing in
                pd.dismiss();
                Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}