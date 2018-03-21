package ca.uqac.nyemo.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import ca.uqac.nyemo.MainActivity;

import org.opencv.core.Mat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * Created by Nyemo on 02/02/2018.
 */

public class SysFonctions {

    public static final String TAG = "SmartID";
    public static final String SCREEN = "SCREEN";
    public static final boolean SCREEN_ON = true;
    public static final boolean SCREEN_OF = false;
    public static final long FIVE_MINUTES = 1000*60*30;
    public static final String INFO_GAIT = "Vous allez devoir marcher quelques minutes en suivant les instructions";
    public static final String EXT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final long ONE_MINUTE = 1000*60;
    public static final long ONE_SECOND = 1000;

    public static void saveMatToImage(Mat matToSave, String fileName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        int i = new Random().nextInt(100);
        imwrite(fileName + "/sample_picture_" + currentDateandTime + i + ".jpg", matToSave);

    }

    public static void generateNotification(String title, String content, Context context) {
        long when = System.currentTimeMillis();

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.putExtra("title", title);
        notifyIntent.putExtra("content", content);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }





/*
    public void saveImage(Mat imgToSave) {
        //Log.i(TAG, "mface : " );


        Toast.makeText(getApplicationContext(),  "onCameraFrame: face :" + detectfaces.size() + " ", Toast.LENGTH_SHORT).show();
        this.faceNumber++;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        int i = new Random().nextInt(100);
        String fileName = getApplicationContext().getExternalFilesDir("faces").getAbsolutePath()  + //Environment.getExternalStorageDirectory().getPath() +
                "/sample_picture_" + currentDateandTime + i + ".jpg";
        //Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "saveImage: " + fileName);
        Log.i(TAG, "saveImage: " + imgToSave.width() + " : " + imgToSave.height());
        //String filename = "/storage/emulated/0/DCIM/Camera/samplepass.jpg";
        imwrite(fileName, imgToSave);
            /*
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(fileName);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent); */
   // }

}
