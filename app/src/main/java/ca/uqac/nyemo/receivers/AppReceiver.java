package ca.uqac.nyemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.uqac.nyemo.move.SensorService;
import ca.uqac.nyemo.utils.SysFonctions;

/**
 * Created by Nyemo on 22/01/2018.
 * listen for the screen on intent and call a service to start a specific activity
 */

public class AppReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(action.equals(Intent.ACTION_SCREEN_ON)) {
                Intent i = new Intent(context.getApplicationContext(), SensorService.class);
                i.putExtra(SysFonctions.SCREEN, SysFonctions.SCREEN_ON);
                context.startService(i);

            }
        }

    }

