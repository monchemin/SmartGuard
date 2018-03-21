package ca.uqac.nyemo.face;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import ca.uqac.nyemo.receivers.AppReceiver;

/**
 * Created by Nyemo on 22/01/2018.
 */

public class LockServiceFilter extends Service {

    AppReceiver mReceiver;




    @Override
    public void  onCreate() {
        super.onCreate();
        Log.i("UpdateService", "Started");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new AppReceiver();
        registerReceiver(mReceiver, filter);
        Toast.makeText(this, "Service created ...", Toast.LENGTH_LONG).show();
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
