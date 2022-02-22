package com.example.cameraxapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {

    ImageView img_cam;
    private int REQUEST_CODE_PERMISSIONS=101;
    private final String [] REQUIRED_PERMISSIONS=new String[]{"android.permission.Camera","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_cam = findViewById(R.id.imagrView);
        if(allPermissionsGranted()){
            Toast.makeText(this,"permission granted",Toast.LENGTH_LONG).show();

        }else{
           ActivityCompat.requestPermissions(this,REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSIONS);
        }
        img_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);

            }
        });


    }

    private boolean allPermissionsGranted(){
        for (String permission:REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
       return false;

        }
        return true;
    }

}