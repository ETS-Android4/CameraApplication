package com.example.camerademo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    TextureView view_finder;
    ImageButton imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        view_finder = findViewById(R.id.view_finder);
        imageCapture = findViewById(R.id.imgCapture);
        startCamera();
    }

    public void startCamera() {
        CameraX.unbindAll();
        Rational aspectRation = new Rational(view_finder.getWidth(), view_finder.getHeight());
        Size screen = new Size(view_finder.getWidth(), view_finder.getHeight());
        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRation).setTargetResolution(screen).build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) view_finder.getParent();
                parent.removeView(view_finder);
                parent.addView(view_finder, 0);
                //=== code for always render refresh camera screen to view===
                view_finder.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).
                setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imageCap = new ImageCapture(imageCaptureConfig);

        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
                imageCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "img captured at" + file.getAbsolutePath();
                        Toast.makeText(CameraActivity.this, msg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CameraActivity.this, ShowPhotoActivity.class);
                        intent.putExtra("path", file.getAbsolutePath() + "");
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "img captured Failed" + message;
                        Toast.makeText(CameraActivity.this, msg, Toast.LENGTH_LONG).show();
                        if (cause != null) {
                            cause.printStackTrace();
                        }

                    }
                });

            }
        });
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, imageCap);
    }

    public void updateTransform() {
        Matrix mx = new Matrix();
        float w = view_finder.getMeasuredWidth();
        float h = view_finder.getMeasuredHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        int rotationDgr = 90;
        int rotation = (int) view_finder.getRotation();
        switch (rotation) {
            case Surface
                    .ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface
                    .ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface
                    .ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface
                    .ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        //==rotation degree and point to rotate ====
        mx.postRotate((float) rotationDgr, cx, cy);
        //=== in older mobile it works fine,this code is for new mobile
        view_finder.setTransform(mx);
    }
}