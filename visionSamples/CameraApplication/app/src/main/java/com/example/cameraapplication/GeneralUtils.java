package com.example.cameraapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class GeneralUtils {

    public String getStringImage(Bitmap bmp) {
        String encodedimage = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            // Log.d("Imagedata>><<<",encodedimage);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return encodedimage;
    }

    /**
     * @param number
     * @return String
     * @author Amit Kumar
     */
    public String IntToString(int number) {

        String Stringnumber = null;
        try {
            Stringnumber = String.valueOf(number);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return Stringnumber;
    }

    /**
     * @param number
     * @return
     * @author Amit Kumar
     */
    public int StringToInt(String number) {
        int Intnumber = 0;
        try {
            Intnumber = Integer.parseInt(number);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return Intnumber;
    }


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public String getTimeAgo(String stringtime, Context ctx) {
        long time = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            String inputString = stringtime;

            Date date = sdf.parse(inputString);
            time = date.getTime();
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(time);
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = current_date_time();
        if (time > now || time <= 0) {
            System.out.println("1");
            return null;

        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static long MilliSeconds(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            String inputString = time;

            Date date = sdf.parse(inputString);
            return date.getTime();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;

    }

    public long current_date_time() {
        String date_time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date_time = df.format(calendar.getTime());
        return calendar.getTimeInMillis();

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String ChangeDataFormate(String formatString) {

        String yourDate = null;
        try {

            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String date = format.format(new Date(formatString));

            if (date.endsWith("1") && !date.endsWith("11"))
                format = new SimpleDateFormat("EE MMM d'st', yyyy");
            else if (date.endsWith("2") && !date.endsWith("12"))
                format = new SimpleDateFormat("EE MMM d'nd', yyyy");
            else if (date.endsWith("3") && !date.endsWith("13"))
                format = new SimpleDateFormat("EE MMM d'rd', yyyy");
            else
                format = new SimpleDateFormat("EE MMM d'th', yyyy");
            yourDate = format.format(new Date(formatString));

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return yourDate;
    }

    /**
     * @param min
     * @param max
     * @return
     * @author amit
     */
    public int RandomNumber(int min, int max) {
        int ramdom = min + (int) (Math.random() * max);
        return ramdom;
    }

    /**
     * @param context
     * @param contentUri
     * @return
     * @author amit
     */

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * @param inContext
     * @param inImage
     * @return

     */

    public Uri getImageResizedUri(Context inContext, Bitmap inImage) {
        String path = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap resized = Bitmap.createScaledBitmap(inImage, 600, 600, true);
            resized.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String filename = ImageNameToSave();
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), resized, filename, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    public static String ImageNameToSave() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        return "IMG_" + currentDateandTime;
    }

    public void saveImageToSDCard(Bitmap bitmap, Context _context) {
        File myDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "DIR_");

        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "IMG-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(
                    _context,
                    "Download Complete",
                    Toast.LENGTH_SHORT).show();
            Log.d("IMAGE", "Wallpaper saved to: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context,
                    "Download Failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
