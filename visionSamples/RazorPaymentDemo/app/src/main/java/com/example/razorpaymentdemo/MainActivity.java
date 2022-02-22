package com.example.razorpaymentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
Button payBtn;
TextView payText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(getApplicationContext());
        payBtn=findViewById(R.id.payBtn);
        payText=findViewById(R.id.payText);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });
    }

    private void makePayment() {
        /**
         * Instantiate Checkout
         */

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_WbY7pkw9yYjkEt");


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.salonz);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "SalonzMart");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
          //  options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            //amount works in into 100 methor here your amount is 500 so 500*100
            options.put("amount", "10000");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9518182961");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        payText.setText("successful payment ID"+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        payText.setText("failed and cause is"+s);

    }
}