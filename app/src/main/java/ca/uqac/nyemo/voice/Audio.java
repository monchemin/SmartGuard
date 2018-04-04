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
    public static final int DAUBECHIES_WAVELET_ORDER = 10;
    public static final int WAVELET_ORDER = 8;
    public static final double VAD_THRESHOLD = 0.0000200d;
    public static final int AUDIO_WINWOW_TIME = 15;
    public static final int PROCESS_TRAME_LENGHT = 512; // arround 10 -15 ms with actuel sample rate

}
