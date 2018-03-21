package ca.uqac.nyemo.move;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Nyemo on 05/02/2018.
 */

public class AccelerometerListener implements SensorEventListener {
    private float[] sValues = new float[3];
    @Override
    public void onSensorChanged(SensorEvent event) {

        this.sValues = event.values;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public float[] getValues() {
        return sValues;
    }
}
