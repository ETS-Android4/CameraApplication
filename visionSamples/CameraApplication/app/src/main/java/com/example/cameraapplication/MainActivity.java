package com.example.cameraapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button click;
    ImageView imageView;
    GeneralUtils generalUtils;
    static final int RESULT_LOAD_IMAGE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    String customer_img;
    Bitmap resized;
    Uri path;
    private Uri finalImageUrl;

//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click=findViewById(R.id.take_photo);
        imageView=findViewById(R.id.captured_image);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


                    }
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
//                    Crop.pickImage(BasicProfile.this);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    Crop.pickImage(MainActivity.this);
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, );
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        generalUtils.checkPermission(this);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {

                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                Toast.makeText(MainActivity.this,"this is the change i made",Toast.LENGTH_LONG).show();
                finalImageUrl = new GeneralUtils().getImageResizedUri(this, imageBitmap);
                imageView.setImageBitmap(imageBitmap);
               // System.out.println("AT CAMERA--->" + finalImageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
           resized = Bitmap.createScaledBitmap(imageBitmap, 600, 600, true);


        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            beginCrop(data.getData());
            finalImageUrl = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), finalImageUrl);
                // Log.d(TAG, String.valueOf(bitmap));
                finalImageUrl=new GeneralUtils().getImageResizedUri(MainActivity.this, bitmap);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        else if(requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }


       }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(600, 600).start(this);
    }
    private void handleCrop(int resultCode, Intent result) {
        try {
            if (resultCode == RESULT_OK) {
                imageBitmap = BitmapFactory
                        .decodeStream(getContentResolver().openInputStream(
                                Crop.getOutput(result)));
//                imageBitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);

               imageView.setImageBitmap(imageBitmap);
                Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 600, 600, false);

                // Handle Your Bitmap here
            } else if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(this, Crop.getError(result).getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}