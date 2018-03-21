package ca.uqac.nyemo.voice;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import ca.uqac.nyemo.utils.SysFonctions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nyemo on 21/02/2018.
 */

public class SmartIDRecognitionListerner implements RecognitionListener {
    Long startTime;

    private MediaRecorder mRecorder = null;
    private String recordPath;


    public SmartIDRecognitionListerner(Context context) {

        startTime = System.currentTimeMillis();
        recordPath = context.getExternalFilesDir("voice").getAbsolutePath();
    }
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

        Log.i(SysFonctions.TAG, "onBeginningOfSpeech: ");

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
        Log.i(SysFonctions.TAG, "end: ");

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
        Log.i(SysFonctions.TAG, "onResults: " + matches.size());

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

        Log.i(SysFonctions.TAG, "onResults partial: ");

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public void audioRecord() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String audioFileName = recordPath + "/" + currentDateandTime + ".3gp";
        mRecorder = new MediaRecorder();
        //mRecorder.release();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(audioFileName);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException ie) {
            Log.e(SysFonctions.TAG, "" + ie.getMessage());
        } catch (IOException e) {
            Log.e(SysFonctions.TAG, "prepare() failed");
        }




        new CountDownTimer(SysFonctions.ONE_SECOND * 15, SysFonctions.ONE_SECOND * 15) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }

            }
        }.start();

    }
}
