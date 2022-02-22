package com.example.totalitycroptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Permission;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS=new SparseIntArray();
    static {ORIENTATIONS.append(Surface.ROTATION_0,90);
           ORIENTATIONS.append(Surface.ROTATION_90,0);
           ORIENTATIONS.append(Surface.ROTATION_180,270);
           ORIENTATIONS.append(Surface.ROTATION_270,180);
    }
    private  String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected  CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION=200;
    private  boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = findViewById(R.id.btn_takepicture);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }
        TextureView.SurfaceTextureListener textureListener=new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                //open camera here
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                // Transform you image captured size according to the surface width and height

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        };





    private void openCamera() {
        CameraManager cameraManager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG,"iscameraOpen");
        try {
            cameraId=cameraManager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics=cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap streamConfigurationMap=cameraCharacteristics.get(cameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert streamConfigurationMap !=null;
            imageDimensions=streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED&&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

            return;
            }
            cameraManager.openCamera(cameraId,stateCallback,null);



        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");


    }


    private void takePicture() {

            if(null == cameraDevice) {
                Log.e(TAG, "cameraDevice is null");
                return;
            }
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
                Size[] jpegSizes = null;
                if (characteristics != null) {
                    jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                }
                int width = 640;
                int height = 480;
                if (jpegSizes != null && 0 < jpegSizes.length) {
                    width = jpegSizes[0].getWidth();
                    height = jpegSizes[0].getHeight();
                }
                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputSurfaces = new ArrayList<Surface>(2);
                outputSurfaces.add(reader.getSurface());
                outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
                final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                // Orientation
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpg");
                ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = null;
                        try {
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);

//                            Bitmap preview = BitmapFactory.decodeByteArray(bytes, 0, buffer.capacity());
//                            image.close();
//                            if (preview != null) {
//                                // This gets the canvas for the same mTextureView we would have connected to the
//                                // Camera2 preview directly above.
//                                Canvas canvas = textureView.lockCanvas();
//                                if (canvas != null) {
//                                    float[] colorTransform = {
//                                            0, 0, 0, 0, 0,
//                                            .35f, .45f, .25f, 0, 0,
//                                            0, 0, 0, 0, 0,
//                                            0, 0, 0, 1, 0};
//                               a     ColorMatrix colorMatrix = new ColorMatrix();
//                                    colorMatrix.set(colorTransform); //Apply the monochrome green
//                                    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
//                                    Paint paint = new Paint();
//                                    paint.setColorFilter(colorFilter);
//                                    canvas.drawBitmap(preview, 0, 0, paint);
//                                    textureView.unlockCanvasAndPost(canvas);
//                                }
//                            }

                            save(bytes);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (image != null) {
                                image.close();
                            }
                        }
                    }
                    private void save(byte[] bytes) throws IOException {
                        OutputStream output = null;
                        try {
                            output = new FileOutputStream(file);
                            output.write(bytes);
                        } finally {
                            if (null != output) {
                                output.close();
                            }
                        }
                    }
                };
                reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                        createCameraPreview();
                    }
                };
                cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        try {
                            session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {
                    }
                }, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }


    }

    private final CameraDevice.StateCallback stateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //this is called when camera is open
            Log.e(TAG,"onOpened");
            cameraDevice=camera;
            createCameraPreview();


        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();



        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;

        }
    };
 final CameraCaptureSession.CaptureCallback captureCallbackListner=new CameraCaptureSession.CaptureCallback() {
     @Override
     public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
         super.onCaptureStarted(session, request, timestamp, frameNumber);
     }

     @Override
     public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
         super.onCaptureProgressed(session, request, partialResult);
     }

     @Override
     public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
         super.onCaptureCompleted(session, request, result);
         Toast.makeText(MainActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
         createCameraPreview();
     }
 };
 protected void startBackgroundThread(){
     mBackgroundHandlerThread=new HandlerThread("Camera Background");
     mBackgroundHandlerThread.start();
     mBackgroundHandler=new Handler(mBackgroundHandlerThread.getLooper());

 }
 protected void stopBackgroundThread(){
     mBackgroundHandlerThread.quitSafely();
     try {
         mBackgroundHandlerThread.join();
         mBackgroundHandlerThread = null;
         mBackgroundHandler = null;

     } catch (InterruptedException e) {
         e.printStackTrace();
     }

 }



    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
 }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }

    }

    private void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
}