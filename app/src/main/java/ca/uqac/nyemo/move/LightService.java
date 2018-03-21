package ca.uqac.nyemo.move;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import ca.uqac.nyemo.face.FaceActivity;
import ca.uqac.nyemo.utils.SysFonctions;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class LightService extends Service {

    SensorListener sListener;
    Sensor light;
    SensorManager manager;
    public LightService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sListener = new SensorListener();
        manager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        light  =  manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        manager.registerListener(sListener,  light, SensorManager.SENSOR_DELAY_NORMAL);

        //Toast.makeText(this, "acc created ...", Toast.LENGTH_LONG).show();
}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("FaceActivity",  "onStartCommand: " + accelerometerListener.getValues()[0] +
              //  ": " + accelerometerListener.getValues()[1] + ": " + accelerometerListener.getValues()[2]);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("FaceActivity",  "onStartCommand: " + sListener.getValues()[0]);

            }
        }, 0, 2000);


        return super.onStartCommand(intent, flags, startId);
    }

    protected void lunch() {
        Intent i = new Intent(getApplicationContext(), FaceActivity.class);
        Context context = getApplicationContext();
        String modelFile = context.getExternalFilesDir(null).getAbsolutePath() + "/model";
        if (new File(modelFile).exists()) {
            i.putExtra("RECOGNIZE", true);
            Log.i(SysFonctions.TAG, "onReceive: true " );
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }


}
