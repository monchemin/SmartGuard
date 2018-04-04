package ca.uqac.nyemo.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.uqac.nyemo.R;
import ca.uqac.nyemo.utils.AppString;
import ca.uqac.nyemo.utils.PermissionRequest;
import ca.uqac.nyemo.utils.SysFonctions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static ca.uqac.nyemo.utils.SysFonctions.ONE_SECOND;
import static ca.uqac.nyemo.utils.SysFonctions.TAG;

public class VoiceRecordActivity extends Activity {

    ImageView voiceImage;
    Button voiceButton;
    TextView voiceText;
    SpeechRecognizer mSpeechRecognizer;
    SmartSpeechRecognitionListener mRecognitionListener;
    Intent recognitionItent;
    Long startTime;
    Boolean isRecording = false;
    // Requesting permission to RECORD_AUDIO
    VoiceRecorder voiceRecorder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_record);
        //layout elements
        voiceImage = (ImageView) findViewById(R.id.voiceImage);
        voiceButton = (Button)findViewById(R.id.voiceButton);
        voiceText = (TextView)findViewById(R.id.voiceText);
        // elements fill

        voiceImage.setImageResource(R.drawable.icone_parleur);
        Display display = getWindowManager().getDefaultDisplay();

        voiceText.setText(AppString.voiceTextInit);

        //voiceButton.setSoundEffectsEnabled(false);
        voiceButton.setText(AppString.voiceRecordButtonInitText);

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording) {
                    voiceText.setText(AppString.getVoiceText());
                    cancelSpeechRecognition();
                    intitSpeechRecogntion();
                }

            }
        });


        PermissionRequest.audioPermission(this, this);
       // intitSpeechRecogntion();


    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

    }

    @Override
    public void onPause(){
        super.onPause();
        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        amanager.setStreamMute(AudioManager.STREAM_RING, false);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionToRecordAccepted = false;
        switch (requestCode){
            case PermissionRequest.REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    public void cancelSpeechRecognition() {
        if(mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
            mRecognitionListener = null;
        }

    }

    public void intitSpeechRecogntion() {

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognitionListener = new SmartSpeechRecognitionListener();
        mRecognitionListener.initRecorder();
        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
        recognitionItent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionItent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionItent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        mSpeechRecognizer.startListening(recognitionItent);
        startTime = System.currentTimeMillis();

    }

    private class SmartSpeechRecognitionListener implements RecognitionListener {




        private String recordPath = getExternalFilesDir("voice").getAbsolutePath();

   CountDownTimer     mcountDownTimer =  new CountDownTimer(SysFonctions.ONE_SECOND*3, SysFonctions.ONE_SECOND ) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "onTick: " + millisUntilFinished/1000);

            }

            @Override
            public void onFinish() {
                voiceRecorder.stopRecording();
                initRecorder();


            }
        };


        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

            Log.i(TAG, "onBeginningOfSpeech: ");
            voiceButton.setEnabled(false);
            voiceButton.setText(AppString.voiceRecordButtonDesableText);
           audioRecord();



        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Log.i(TAG, "end: ");


        }

        @Override
        public void onError(int error) {

            Long duration = System.currentTimeMillis() - startTime;
            if (duration < 500 && error == SpeechRecognizer.ERROR_NO_MATCH) {
                Log.i(TAG, "Doesn't seem like the system tried to listen at all. duration = " + duration + "ms. This might be a bug with onError and startListening methods of SpeechRecognizer");
                Log.i(TAG, "Going to ignore the error");
                return;
            } else {
                Log.i(TAG, "Error: " + error); }


        }

        @Override
        public void onResults(Bundle results) {

            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Log.i(TAG, "onResults: " + matches.size());

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

            Log.i(TAG, "onResults partial: ");

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        public void audioRecord() {
            cancelSpeechRecognition(); // cancel speech recognition service and release mic for record

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //audioRecord();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mcountDownTimer.start();
                    voiceRecorder = new VoiceRecorder();
                    voiceRecorder.startRecording();

                }
            }).start();
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } */
/*
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            String audioFileName = recordPath + "/" + currentDateandTime + ".3gp";

*/         /* voiceRecorder = new VoiceRecorder();
            voiceRecorder.startRecording();
            mcountDownTimer.start(); */







/*
10 secondes pour faire l'enregistrement et le countdown faire un seul tour
 */



        }
        public void initRecorder() {

                voiceButton.setEnabled(true);
                voiceButton.setText(AppString.voiceRecordButtonNextText);
                isRecording = false;

            }




        }




    }




