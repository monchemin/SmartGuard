package ca.uqac.nyemo.voice;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ca.uqac.nyemo.utils.SysFonctions.TAG;

/**
 * Created by Nyemo on 20/03/2018.
 */

public class VoiceRecorder {

    private AudioRecord recorder = null;
    private int bufferSize;
    private Thread recThread = null;
    private boolean isRecording = false;


    public VoiceRecorder() {
        // get system buffer size for specifique sample, channel and encoding format
        // information are provided by Audio.java in the same package
        bufferSize = AudioRecord.getMinBufferSize(Audio.SAMPLE_RATE,
                Audio.CHANNEL_TYPE,
                Audio.ENCODINF_FORMAT);
        // if has error take default buffer size
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = 44100 * 2;
        }

        Log.i(TAG, "buffer size " + bufferSize);
        // create instance of audio record with record information
        recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                Audio.SAMPLE_RATE, Audio.CHANNEL_TYPE, Audio.ENCODINF_FORMAT, bufferSize);
    }


    public void startRecording() {


        int state = recorder.getState(); // get state for checking
        Log.i(TAG, "state before: " +  state);

        if (state == AudioRecord.STATE_INITIALIZED) {
            try {
                recorder.startRecording();

            } catch (IllegalStateException ie) {
                Log.e(TAG, "startRecording: " + ie.getCause());
            }
        }

        if(recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            isRecording = true;
            recordingProcess();
        } else {
            Log.e(TAG, "Recording state: " + recorder.getRecordingState() + " state : " + recorder.getState());
        }




         /*   recThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try { */

           /*         } catch (Exception e) {
                        Log.e("VoiceRecorder", e.getMessage());
                    }
                }
            }, "VoiceRecorder Thread");

            recThread.start(); */

    }


protected void recordingProcess() {
    short[] audioBuffer = new short[bufferSize / 2];
    Log.i(TAG, "isrec: " + isRecording);
    long start = System.currentTimeMillis();

    int shortsRead = 0;
    while (isRecording) {
        int numberOfShort = recorder.read(audioBuffer, 0, audioBuffer.length);
        if(numberOfShort != AudioRecord.ERROR_BAD_VALUE && numberOfShort != -1)
        {
            shortsRead += numberOfShort;
            //Log.i(TAG, "shorts read: " + numberOfShort);
        }


    }
    Log.i(TAG, "total shorts read: " + shortsRead);

}


    public void stopRecording() {
        if(recorder != null) {
            isRecording = false;
            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                recorder.stop();
            }
            recorder.release();
            recorder = null;
            recThread = null;

        }
    }

    public void cancelRecording() {
        if(recorder != null) {
            isRecording = false;

            int state = recorder.getState();

            if(state == AudioRecord.STATE_INITIALIZED) {
                recorder.stop();
            }

            recorder.release();
            recorder = null;
            recThread = null;

        }


    }

    private void convertToWAVFile(String raw, String output) throws IOException {
        FileInputStream inputStream;
        FileOutputStream outputStream;
        long fileLength;
        long dataLength;
        long byteRate = Audio.BYTE_PER_SAMPLE * Audio.SAMPLE_RATE* 2 / 8;

        byte[] data = new byte[bufferSize];

        inputStream = new FileInputStream(raw);
        outputStream = new FileOutputStream(output);
        fileLength = inputStream.getChannel().size();
        dataLength = fileLength + 36;

        writeWAVFile(outputStream, fileLength, dataLength, Audio.SAMPLE_RATE,
                2, byteRate);

        while(inputStream.read(data) != -1) {
            outputStream.write(data);
        }

        inputStream.close();
        outputStream.close();
    }

    private void writeWAVFile(FileOutputStream output, Long fileLength, Long dataLength, Integer
            sampleRate, int channels, Long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (dataLength & 0xff);
        header[5] = (byte) ((dataLength >> 8) & 0xff);
        header[6] = (byte) ((dataLength >> 16) & 0xff);
        header[7] = (byte) ((dataLength >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        header[34] = (byte) Audio.BYTE_PER_SAMPLE;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (fileLength & 0xff);
        header[41] = (byte) ((fileLength >> 8) & 0xff);
        header[42] = (byte) ((fileLength >> 16) & 0xff);
        header[43] = (byte) ((fileLength >> 24) & 0xff);

        output.write(header, 0, 44);
    }
}
