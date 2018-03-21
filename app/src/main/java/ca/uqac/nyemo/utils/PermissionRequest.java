package ca.uqac.nyemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


/**
 * Created by Nyemo on 26/02/2018.
 */

public class PermissionRequest {
    private boolean permissionToRecordAccepted = false;

    public static final int REQUEST_CAMERA_PERMISSION = 300;
    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 100;


    public static void cameraPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(SysFonctions.TAG, "buld");
            if (context.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d(SysFonctions.TAG, "permission");
                activity.requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }

    }
    public static void audioPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Log.d(SysFonctions.TAG, "permission");
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
    }

    public static void writeExternalPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(SysFonctions.TAG, "permission");
                activity.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }

        }


    }
}
