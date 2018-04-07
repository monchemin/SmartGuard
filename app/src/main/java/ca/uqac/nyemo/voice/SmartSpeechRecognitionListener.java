package ca.uqac.nyemo.voice;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

import ca.uqac.nyemo.utils.SysFonctions;

/**
 * Created by Nyemo on 07/04/2018.
 */

public class SmartSpeechRecognitionListener implements RecognitionListener {

    protected VoiceActivityListener voiceActivityListener;
    Long startTime;
    private VoiceRecorder voiceRecorder;

    public void setVoiceActivityListener(VoiceActivityListener val) {
        this.voiceActivityListener = val;
    }

    //private String recordPath = getExternalFilesDir("voice").getAbsolutePath();
//initialize countdown
    CountDownTimer mcountDownTimer =  new CountDownTimer(SysFonctions.ONE_SECOND*3, SysFonctions.ONE_SECOND ) {
        @Override
        public void onTick(long millisUntilFinished) {

            Log.i(SysFonctions.TAG, "onTick: " + millisUntilFinished/1000);

        }

        @Override
        public void onFinish() {

            voiceRecorder.stopRecording(); // stop recording

            //send feature to the activity
            try {
                voiceActivityListener.endRecord(voiceRecorder.getComputeFeatures());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        voiceActivityListener.onBeginningOfSpeech();
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
       // Log.i(TAG, "end: ");


    }

    @Override
    public void onError(int error) {

        Long duration = System.currentTimeMillis() - startTime;
        if (duration < 500 && error == SpeechRecognizer.ERROR_NO_MATCH) {
            Log.i(SysFonctions.TAG, "Doesn't seem like the system tried to listen at all. duration = " + duration + "ms. This might be a bug with onError and startListening methods of SpeechRecognizer");
            Log.i(SysFonctions.TAG, "Going to ignore the error");
            return;
        } else {
            Log.i(SysFonctions.TAG, "Error: " + error); }


    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        //Log.i(TAG, "onResults: " + matches.size());

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

        //Log.i(TAG, "onResults partial: ");

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public void audioRecord() {
        voiceActivityListener.cancelSpeechRecognition(); // cancel speech recognition service and release mic for record

        new Thread(new Runnable() {
            @Override
            public void run() {
                //audioRecord();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                voiceRecorder = new VoiceRecorder();
                voiceRecorder.startRecording(); // begin recording
                mcountDownTimer.start(); // start countdown

            }
        }).start();


    }






        /**      }
         public void initRecorder() {

         voiceButton.setEnabled(true);
         voiceButton.setText(AppString.voiceRecordButtonNextText);
         isRecording = false;
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

         }




         }

         */

    }
