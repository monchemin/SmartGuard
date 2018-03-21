package ca.uqac.nyemo.face;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ca.uqac.nyemo.R;
import ca.uqac.nyemo.utils.EigenFaces;
import ca.uqac.nyemo.utils.SysFonctions;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FaceTrainerService extends IntentService {

    private CascadeClassifier cascadeClassifier;
    final private String TAG = "FaceActivity";


    public FaceTrainerService() {
        super("FaceTrainerService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //if(faceDetection()) {
            String modelFile = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/model";
            if (!new File(modelFile).exists()) {
                Log.i(TAG, "trainProcess: begin");
                try {
                    String facesDir = getApplicationContext().getExternalFilesDir("imgfaces").getAbsolutePath();
                    //EigenFaces eigenFaces = EigenFaces.loadModel("D:/ijworkspace/EigenFace/model");
                    EigenFaces eigenFaces = new EigenFaces();
                    eigenFaces.trainProcess(facesDir);
                    eigenFaces.execute();
                    //testE.find(imgtofing);

                    eigenFaces.saveModel(modelFile);
                    Log.i(TAG, "trainProcess: end");
                    sendNotification();
                    //EigenFaces.loadModel("D:/ijworkspace/EigenFace/model");

                } catch (IOException e) {
                    e.printStackTrace();


                }

            } else {
                Log.i(TAG, "onHandleIntent: Train already !");
            }
       // }
    }

    protected boolean faceDetection() {

        Log.i(TAG, "faceDetection: begin");

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);

            //InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);

            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            //File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            // String stream = getResources().getResourceName(R.raw);
            //Mat image = imread(imf.getAbsolutePath());

            for(File file : getApplicationContext().getExternalFilesDir("imgfaces").listFiles()) {
                Mat myImage = imread(file.getAbsolutePath(), IMREAD_GRAYSCALE);

                MatOfRect faceDetections = new MatOfRect();
                cascadeClassifier.detectMultiScale(myImage, faceDetections);
                System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
                // Draw a bounding box around each face.
                for (Rect rect : faceDetections.toArray()) {
                    String path = getApplicationContext().getExternalFilesDir("imgfaces").getAbsolutePath();
                    SysFonctions.saveMatToImage(myImage.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width), path);
                    //saveImage(myImage.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width));
                    //Imgproc.rectangle(myImage, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

                }
            }
            //saveImage(myImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "faceDetection: begin");
        return true;
    }

    private void sendNotification() {
        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Training done !");
        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}
