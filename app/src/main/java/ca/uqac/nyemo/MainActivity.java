package ca.uqac.nyemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ca.uqac.nyemo.R;
import ca.uqac.nyemo.face.FaceActivity;
import ca.uqac.nyemo.face.LockServiceFilter;
import ca.uqac.nyemo.move.GaitTrainActivity;
import ca.uqac.nyemo.move.SensorService;
import ca.uqac.nyemo.utils.PermissionRequest;
import ca.uqac.nyemo.voice.AudioDecoder;
import ca.uqac.nyemo.voice.VoiceRecordActivity;
import ca.uqac.nyemo.voice.VoiceRecorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static ca.uqac.nyemo.utils.SysFonctions.TAG;

public class MainActivity extends Activity {

    ListView menuList;
    ArrayList<String> menuArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuList = (ListView) findViewById(R.id.menuList);
       //menuList = new ListView();
        PermissionRequest.writeExternalPermission(this, this);
       fillMenuList();
       startAppService();


    }

    private void fillMenuList() {
        menuArray.add("SmartID PIN");
        menuArray.add("Reconnaissance de  mouvement");
        menuArray.add("Reconnaissance du visage");
        menuArray.add("Reconnaissance de la voix");
        menuArray.add("Applications Sensibles");
    }
    private void startAppService() {
        Intent screenStateService = new Intent(MainActivity.this, LockServiceFilter.class);
        Intent appService = new Intent(MainActivity.this, SensorService.class);
        // alerteService.putExtra("screenState", false);
        startService(screenStateService);
        startService(appService);
        startService(screenStateService);
        startService(appService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> menuAdpter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray);
        menuList.setAdapter(menuAdpter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        startService(new Intent(MainActivity.this, SensorService.class));
                        break;
                    case 1 :
                        startActivity(new Intent(MainActivity.this, GaitTrainActivity.class));
                        break;
                    case 2 :
                        startActivity(new Intent(MainActivity.this, FaceActivity.class));
                        break;
                    case 3 :
                        startActivity(new Intent(MainActivity.this, VoiceRecordActivity.class));
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "yeaaa", Toast.LENGTH_LONG).show();
                      /*  try {
                            getAudioInfo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } */
                      voiceRecord();


                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permission = false;
        switch (requestCode){
            case PermissionRequest.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                permission  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permission ) finish();
    }


    public void getAudioInfo() throws IOException {

        File[] listFile = getExternalFilesDir("voice").listFiles();
        if(listFile.length != 0) {
            AudioDecoder.AudioDecoder(listFile[0].getAbsolutePath());
        }


    }

    public void voiceRecord() {
        VoiceRecorder voiceRecorder = new VoiceRecorder();
        voiceRecorder.startRecording();


    }


}
