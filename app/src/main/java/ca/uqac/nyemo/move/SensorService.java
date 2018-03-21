package ca.uqac.nyemo.move;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import ca.uqac.nyemo.R;
import ca.uqac.nyemo.face.FaceActivity;
import ca.uqac.nyemo.utils.EigenFaces;
import ca.uqac.nyemo.utils.SysFonctions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorService extends Service {

    AccelerometerListener accelerometerListener;
    Sensor accelerometer;
    SensorListener lightListener;
    Sensor light;
    SensorManager manager;
    ActivityRecognitionClient activityRecognitionClient;
    ArrayList<Float> xRecords = new ArrayList<Float>() ;
    ArrayList<Float> yRecords = new ArrayList<Float>() ;
    ArrayList<Float> zRecords = new ArrayList<Float>() ;
    ArrayList<Float> meanGaitRecord = new ArrayList<>();
    boolean onRecord, isUser = false;
/*
    public SensorService() {
        super(SysFonctions.TAG);

    } */

    @Override
    public void onCreate() {
        super.onCreate();

        accelerometerListener = new AccelerometerListener();
        manager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        accelerometer  =  manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        manager.registerListener(accelerometerListener,  accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        lightListener = new SensorListener();
        light  =  manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        manager.registerListener(lightListener,  light, SensorManager.SENSOR_DELAY_NORMAL);
        activityRecognitionClient = ActivityRecognition.getClient(getApplicationContext());
        Intent intent = new Intent(this, SensorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        activityRecognitionClient.requestActivityUpdates(SysFonctions.ONE_SECOND, pendingIntent);

        Toast.makeText(this, "acc created ...", Toast.LENGTH_SHORT).show();

}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
/*
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        boolean screen = intent.getBooleanExtra(SysFonctions.SCREEN, false);
        Toast.makeText(this, "onhandle" , Toast.LENGTH_LONG).show();
        if(screen) {
            if (sListener.getValues()[0] > 200) {
                // Log.i(SysFonctions.TAG, "lanch");
                lanchAuth();
            }
        }
        if (ActivityRecognitionResult.hasResult(intent)) {
            moveProcess(intent);
        }

    } */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        onCallService(intent);


/*
        //Log.i("FaceActivity",  "onStartCommand: " + accelerometerListener.getValues()[0] +
              //  ": " + accelerometerListener.getValues()[1] + ": " + accelerometerListener.getValues()[2]);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(SysFonctions.TAG,  "onStartCommand: " + sListener.getValues()[0]);//+
                     //   ": " + accelerometerListener.getValues()[1] + ": " + accelerometerListener.getValues()[2]);

            }
        }, 0, 5000); */


        return super.onStartCommand(intent, flags, startId);
    }

    private void moveProcess(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        DetectedActivity mostProbableActivity = result.getMostProbableActivity();

        int confidence = mostProbableActivity.getConfidence();
        int activityType = mostProbableActivity.getType();

        String type;
        if (activityType == DetectedActivity.IN_VEHICLE) {
            type = "In Car";
        } else if (activityType == DetectedActivity.ON_FOOT) {
            type = "On Foot";
            gaitRecognize();
        } else if (activityType == DetectedActivity.ON_BICYCLE) {
            type = "By Bicycle";
        } else {
            type = "Unknown";
        }
        if (type == "On Foot") {
           // SysFonctions.generateNotification("Activity Recognition", type, getApplicationContext());
        }
    }

    private void gaitRecognize() {

        Log.i(SysFonctions.TAG, "gait fonction ");
      String  modelFile = getApplication().getExternalFilesDir(null).getAbsolutePath() + "/gaitmodel";
        //String  modelFile = SysFonctions.EXT_DIR + "/gaitmodel";

        if (new File(modelFile).exists()) {

            if(!isUser) {
                Log.i(SysFonctions.TAG, "record statue: " + onRecord);
                if(!onRecord) {
                    onRecord = true;
                    SysFonctions.generateNotification("Activity Recognition", "ON FOOT", getApplicationContext());
                    recordGaitInfo();
                    Log.i(SysFonctions.TAG, "gait record: ");


                }
            }

        }
        else {
            Log.i(SysFonctions.TAG, "not file ");
        }



    }

    private void recordGaitInfo() {

        new CountDownTimer(SysFonctions.ONE_SECOND*15, SysFonctions.ONE_SECOND/500) {

            public void onTick(long millisUntilFinished) {

                Log.i(SysFonctions.TAG, "record recog: " + accelerometerListener.getValues()[0] );
                xRecords.add(accelerometerListener.getValues()[0]);
                yRecords.add(accelerometerListener.getValues()[1]);
                zRecords.add(accelerometerListener.getValues()[2]);

            }

            public void onFinish() {

                for(int i = 0; i < xRecords.size(); i++) {

                    meanGaitRecord.add((xRecords.get(i) + yRecords.get(i) + zRecords.get(i) / 3));

                }
                Log.i(SysFonctions.TAG, "on finish: ");
                gaitRecognizeProcess();



            }
        }.start();
    }

    public void gaitRecognizeProcess() {
        String modelFile = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/gaitmodel";
        //String modelFile = SysFonctions.EXT_DIR + "/gaitmodel";
        try {
            EigenFaces testE = EigenFaces.loadModel(modelFile); // load gait model
            if(testE != null) {
                //Log.i(SysFonctions.TAG, "model size" + testE.getMeanMat().length);
                int recSize = testE.getMeanMat().length; // fetch model row size
                double[][] normalizeTable = normalizeGaitRecords(recSize); // normalize with gait info to to recognization
                double distance = testE.find(normalizeTable);
                sendNotification(distance);
                Log.i(SysFonctions.TAG, "recognizeProcess: distance " + distance);
                onRecord = false;
                if (distance <= 500) {
                    isUser = true;
                }
            } else {
                Log.i(SysFonctions.TAG, "null object");
            }
            //Toast.makeText(this, "distance : " + distance, Toast.LENGTH_LONG);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(SysFonctions.TAG, "model file not existe ");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i(SysFonctions.TAG, "model file not class ");
        }
    }

    private double[][] normalizeGaitRecords(int modelSize) {
        double[][] result = new double[modelSize][1];
        int recordSize = meanGaitRecord.size();
        Log.i(SysFonctions.TAG, "rowS , record" + modelSize + " : " + recordSize);
        if(recordSize < modelSize) {
            // make linear interpolation
            for (int i = 0; i < modelSize - recordSize ; i++) {
               meanGaitRecord.add( (meanGaitRecord.get(new Random().nextInt(recordSize)) + meanGaitRecord.get(new Random().nextInt(recordSize)))/2);
            }
        }
        if (recordSize > modelSize) {
            //need to sublist record
            List<Float> normalList = meanGaitRecord.subList(0, modelSize);
            Log.i(SysFonctions.TAG, "avant clear " + meanGaitRecord.size() + " : " + normalList.size());

            ArrayList<Float> newList = new ArrayList<>();
            newList.addAll(normalList);
            meanGaitRecord.clear();
           Log.i(SysFonctions.TAG, "apres clear " + meanGaitRecord.size() );
            meanGaitRecord.addAll(newList);
        }

            for (int line = 0; line < modelSize; line++) {
                result[line][0] = meanGaitRecord.get(line);
            }

        return result;

    }

    private void lanchAuth() {
        Log.i(SysFonctions.TAG, "lanch in");
       String modelFile = "";
        try {
            modelFile = getApplication().getExternalFilesDir(null).getAbsolutePath() + "/model";
            Log.i(SysFonctions.TAG, "file in");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (new File(modelFile).exists()) {
            Log.i(SysFonctions.TAG, "face in");
            Intent faceAuth = new Intent(this, FaceActivity.class);

            faceAuth.putExtra("RECOGNIZE", true);

            faceAuth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(faceAuth);
        }
    }

    private void sendNotification(double distance) {
        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("distance : " + distance);
        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

    private void onCallService(Intent intent) {
        boolean screen = intent.getBooleanExtra(SysFonctions.SCREEN, false);
       // Toast.makeText(this, "onhandle" , Toast.LENGTH_SHORT).show();
        if(screen) {
            isUser = false;
            onRecord = false;
            if (lightListener.getValues()[0] > 200) {
                // Log.i(SysFonctions.TAG, "lanch");
                lanchAuth();
            }
        }
        if (ActivityRecognitionResult.hasResult(intent)) {
            moveProcess(intent);
        }
    }

}
