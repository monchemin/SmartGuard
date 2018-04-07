package ca.uqac.nyemo.voice;

/**
 * Created by Nyemo on 07/04/2018.
 */

public interface VoiceActivityListener {

    void onBeginningOfSpeech();
    void endRecord(double[] recordFeatures);
    void cancelSpeechRecognition();
    void initRecorder();

}
