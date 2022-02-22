package com.example.camerademo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.File;

public class EditPhotoActivity extends AppCompatActivity {
    ImageView imgEdit;
    String path = "";
    Uri inputImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        imgEdit = findViewById(R.id.imgEdit);
        path = getIntent().getExtras().getString("path");
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Toast.makeText(this, "hi you there", Toast.LENGTH_LONG).show();
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgEdit.setImageBitmap(myBitmap);
        }
        inputImageUri = Uri.fromFile(new File(path));
        edit_trial();

    }

    public void edit_trial() {
        Intent dsphotoeditorIntent = new Intent(this, DsPhotoEditorActivity.class);
        dsphotoeditorIntent.setData(inputImageUri);
        //==now below line are optional to set edited image to your directory==//
        dsphotoeditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Nancy Photos directory");
        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

        dsphotoeditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
        startActivityForResult(dsphotoeditorIntent, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    //==we can handle result uri as we want==//
                    Uri outputUri = data.getData();
                    imgEdit.setImageURI(outputUri);
                    break;


            }
        }
    }
}