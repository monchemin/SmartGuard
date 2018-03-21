package ca.uqac.nyemo.voice;

import android.media.MediaExtractor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nyemo on 08/03/2018.
 */

public class AudioProcess {
    MediaExtractor extractor;
    String audioFile;
    ArrayList<Long> audioFeatures = new ArrayList<>();

    public AudioProcess(String audioFile) {
        this.audioFile = audioFile;
        try {
            extractor.setDataSource(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Long> getAudioFeatures() {
        audioFeatures.add((long)extractor.getTrackCount());

        return audioFeatures;
    }

}
