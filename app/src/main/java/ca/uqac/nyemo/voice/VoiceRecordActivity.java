package ca.uqac.nyemo.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ca.uqac.nyemo.R;
import ca.uqac.nyemo.utils.AppString;
import ca.uqac.nyemo.utils.PermissionRequest;

import static ca.uqac.nyemo.utils.SysFonctions.TAG;

/**
 * activity for audio record
 * after recording, features are computed and the model saved
 */
public class VoiceRecordActivity extends Activity implements VoiceActivityListener {

    ImageView voiceImage;
    Button voiceButton;
    TextView voiceText;
    SpeechRecognizer mSpeechRecognizer;
    SmartSpeechRecognitionListener mRecognitionListener;
    Intent recognitionItent;

    Boolean isRecording = false;
    // Requesting permission to RECORD_AUDIO
    VoiceRecorder voiceRecorder;
    int recordNumber = 0;
    VoiceModel voiceTrainFeatures = new VoiceModel();



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
//audio manager to mute mic
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
        if (!permissionToRecordAccepted ) finish(); // end activity if no permission
    }
//cancel android speech recognition to release mic for audio record
    public void cancelSpeechRecognition() {
        if(mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
            mRecognitionListener = null;
        }

    }

    /**
     * initialize speech recognition settings
     */
    public void intitSpeechRecogntion() {

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognitionListener = new SmartSpeechRecognitionListener();
        mRecognitionListener.setVoiceActivityListener(this);
       // mRecognitionListener.initRecorder();

        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
        recognitionItent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionItent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionItent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        mSpeechRecognizer.startListening(recognitionItent);

       // initRecorder();

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech: ");
        voiceButton.setEnabled(false); // turn off button
        voiceButton.setText(AppString.voiceRecordButtonDesableText);

    }

    /**
     * call in the end of record
     * if number of reccords == 3, save model
     * @param recordFeatures
     */
    @Override
    public void endRecord(double[] recordFeatures) {

        voiceTrainFeatures.addEntry(recordFeatures); // add info to model
        recordNumber++;
        Log.i(TAG, "recod" + recordFeatures.length);
        if(recordNumber == 3) {
            Toast.makeText(getApplicationContext(), "Train done !", Toast.LENGTH_LONG).show();
            String modelFile = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/voicemodel";
            try {
                voiceTrainFeatures.saveModel(modelFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }
        voiceText.setText("CLIQUEZ POUR LE TEXTE SUIVANT");
        initRecorder();

    }

    public void initRecorder() {

        voiceButton.setEnabled(true);
        voiceButton.setText(AppString.voiceRecordButtonNextText);
        isRecording = false;

    }


    }




