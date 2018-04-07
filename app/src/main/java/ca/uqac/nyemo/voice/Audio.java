package ca.uqac.nyemo.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.nio.ShortBuffer;

/**
 * Created by Nyemo on 20/03/2018.
 */

public abstract class Audio {


    public static final int SAMPLE_RATE = 44100;
    public static final int BYTE_PER_SAMPLE = 16;
    public static final int CHANNEL_TYPE = AudioFormat.CHANNEL_IN_STEREO;
    public static final int ENCODINF_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int DAUBECHIES_WAVELET_ORDER = 10;
    public static final int WAVELET_ORDER = 8;
    public static final double VAD_THRESHOLD = 0.00002d;
    public static final int AUDIO_WINWOW_TIME = 15;
    public static final int PROCESS_TRAME_LENGHT = 512; // arround 10 -15 ms with actuel sample rate
    public static final int TRAME_NUMBER =  200; //arround 2 seconds speech with same rate
    public static final int TRAIN_ARRAY_SIZE = 5600; //200 trame * 24 (trame size)


    void playAudio(final ShortBuffer mSamples, // the samples to play
                   final int mNumSamples) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean mShouldContinue = true;
                int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                    bufferSize = SAMPLE_RATE * 2;
                }

                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize,
                        AudioTrack.MODE_STREAM);

                audioTrack.play();

                Log.v("", "Audio streaming started");

                short[] buffer = new short[bufferSize];
                mSamples.rewind();
                int limit = mNumSamples;
                int totalWritten = 0;
                while (mSamples.position() < limit && mShouldContinue) {
                    int numSamplesLeft = limit - mSamples.position();
                    int samplesToWrite;
                    if (numSamplesLeft >= buffer.length) {
                        mSamples.get(buffer);
                        samplesToWrite = buffer.length;
                    } else {
                        for (int i = numSamplesLeft; i < buffer.length; i++) {
                            buffer[i] = 0;
                        }
                        mSamples.get(buffer, 0, numSamplesLeft);
                        samplesToWrite = numSamplesLeft;
                    }
                    totalWritten += samplesToWrite;
                    audioTrack.write(buffer, 0, samplesToWrite);
                }

                if (!mShouldContinue) {
                    audioTrack.release();
                }

                Log.v("", "Audio streaming finished. Samples written: " + totalWritten);
            }
        }).start();
    }

}
