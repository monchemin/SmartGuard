package ca.uqac.nyemo.move;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import ca.uqac.nyemo.utils.SysFonctions;

import java.util.ArrayList;

public class GaitTrainInfoService extends Service {
    Sensor accelerometer;
    SensorListener accelerometerListener;
    SensorManager manager;
    Intent intent;
    PendingIntent pendingIntent;
    ActivityRecognitionClient activityRecognitionClient;
    public static final String GAIT_INFO = "GAITINFO";
    ArrayList<Float> xRecords = new ArrayList<Float>() ;
    ArrayList<Float> yRecords = new ArrayList<Float>() ;
    ArrayList<Float> zRecords = new ArrayList<Float>() ;
    int number = 0;
    Long debut;
    boolean onRecord = false;
     public GaitTrainInfoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        accelerometerListener = new SensorListener();
        manager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        accelerometer  =  manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(accelerometerListener,  accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        activityRecognitionClient = ActivityRecognition.getClient(getApplicationContext());
        Intent intent = new Intent(this, GaitTrainInfoService.class);
        //intent.setFlags(Intent)
        pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        activityRecognitionClient.requestActivityUpdates(SysFonctions.ONE_SECOND, pendingIntent);
        Toast.makeText(this, " start gait " , Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            int confidence = mostProbableActivity.getConfidence();
            Toast.makeText(this, " Intent " , Toast.LENGTH_SHORT).show();

            if (mostProbableActivity.getType() == DetectedActivity.ON_FOOT && !onRecord) {
                activityRecognitionClient.removeActivityUpdates(pendingIntent);
                onRecord = true;
                recordGaitInfo();

            }
        }




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void recordGaitInfo() {

       debut = System.currentTimeMillis();
        new CountDownTimer(SysFonctions.ONE_SECOND*15, SysFonctions.ONE_SECOND/500) {

            public void onTick(long millisUntilFinished) {

                Log.i(SysFonctions.TAG, "record train: " + accelerometerListener.getValues()[0] );
                xRecords.add(accelerometerListener.getValues()[0]);
                yRecords.add(accelerometerListener.getValues()[1]);
                zRecords.add(accelerometerListener.getValues()[2]);
                number++;

            }

            public void onFinish() {
                Long fin = System.currentTimeMillis() - debut;
                Log.i(SysFonctions.TAG, "onFinish: temps " + fin);
                ArrayList<Float> meanRecord = new ArrayList<>();
                for(int i = 0; i < xRecords.size(); i++) {

                    meanRecord.add((xRecords.get(i) + yRecords.get(i) + zRecords.get(i) / 3));

                }
                Log.i(SysFonctions.TAG, "onFinish: number " + number);
                Log.i(SysFonctions.TAG, "onFinish: number " + meanRecord.size());

                activityRecognitionClient.removeActivityUpdates(pendingIntent);
                intent = new Intent(GAIT_INFO);
                intent.putExtra("RECORD", new GaitRecords(meanRecord));
                sendBroadcast(intent);
                onRecord = false;

            }
        }.start();




    }
}
