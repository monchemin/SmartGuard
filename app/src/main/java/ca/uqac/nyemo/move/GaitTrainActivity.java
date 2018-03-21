package ca.uqac.nyemo.move;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.uqac.nyemo.R;
import ca.uqac.nyemo.utils.EigenFaces;
import ca.uqac.nyemo.utils.SysFonctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GaitTrainActivity extends Activity {

    TextView infoText;
    Button actionBtn, trainBtn;
    MyReceiver myReceiver;
    GaitTrainInfoService gtis;
    Intent trainService;
    ArrayList<GaitRecords> records = new ArrayList<GaitRecords>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "oncreate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_gait_train);
        infoText = (TextView) findViewById(R.id.infoText);
        actionBtn = (Button) findViewById(R.id.action_btn);
        trainBtn = (Button) findViewById(R.id.train_btn);
        trainBtn.setEnabled(false);
        trainBtn.setText("Suivant");
        infoText.setText(SysFonctions.INFO_GAIT);
        actionBtn.setText("Demarrer");
        myReceiver = new MyReceiver();
        IntentFilter mIntentFliter = new IntentFilter();
        mIntentFliter.addAction(GaitTrainInfoService.GAIT_INFO);
        registerReceiver(myReceiver, mIntentFliter);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHandle();
            }
        });
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainBtnHandle();
            }
        });
        trainService = new Intent(this, GaitTrainInfoService.class);

    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        manager.registerListener(sListener,  accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        manager.unregisterListener(sListener);
        unregisterReceiver(myReceiver);

    }

    private void btnHandle() {
        actionBtn.setEnabled(false);
        ComponentName cn = startService(trainService);
        Log.i(SysFonctions.TAG, "service: " + cn);

    }

    /*
    call on train button handle
    make gait train and save model for recognition
     */

    private void trainBtnHandle() {

        /* transformation process
        1. serach the small size record
        2. normalize records on the min size
        3. transform recods(arraylist) on [][] to give to the train process
         */
        // search the small size of records and choice it for reference
        int min = records.get(0).getRecord().size(); // record's first size
        int rSize = records.size(); // record size : default 3
        for (int i = 1; i < rSize ; i++) {
            if(records.get(i).getRecord().size() < min) {
                min = records.get(i).getRecord().size();
            }
        }
        //ajustement by sublist
        for (int i = 0; i < rSize; i++) {
            if(records.get(i).getRecord().size() > min) {
              List<Float> normalList = records.get(i).getRecord().subList(0, min); // return list
              ArrayList<Float> normalSize = new ArrayList<Float>();
              normalSize.addAll(normalList); // copie list to arrayList
              records.add(i, new GaitRecords(normalSize)); // change element i with de the normalise

            }
        }
        //change list to [][]

        double[][] result = new double[min][rSize];
        for (int col = 0; col < rSize; col++) {
            for (int line = 0; line < min; line++) {
                result[line][col] = records.get(col).getRecord().get(line);
            }
        }
        // end of transformation process
        /*
        @train process with class EigenFaces
         */
        EigenFaces gaitModel = new EigenFaces();
        gaitModel.execute(result); // train process
        String modelFile = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/gaitmodel";
       //String  modelFile = SysFonctions.EXT_DIR + "/gaitmodel";
        try {
            gaitModel.saveModel(modelFile); //save model
            Toast.makeText(this, "train end", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(GaitTrainActivity.this, "yyy", Toast.LENGTH_SHORT);

            records.add((GaitRecords) intent.getExtras().getParcelable("RECORD"));
            stopService(trainService);

            if(records.size() < 3) {
                actionBtn.setEnabled(true);
            }
            else {
                trainBtn.setEnabled(true);
            }

            //intent.getExtras().getParcelableArrayList("RECORD");


        }
    }
}
