package ca.uqac.nyemo.voice;

import android.media.AudioFormat;

/**
 * Created by Nyemo on 20/03/2018.
 */

public abstract class Audio {


    public static final int SAMPLE_RATE = 44100;
    public static final int BYTE_PER_SAMPLE = 16;
    public static final int CHANNEL_TYPE = AudioFormat.CHANNEL_IN_STEREO;
    public static final int ENCODINF_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
}
