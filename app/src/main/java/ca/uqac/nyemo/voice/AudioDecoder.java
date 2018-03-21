package ca.uqac.nyemo.voice;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static ca.uqac.nyemo.utils.SysFonctions.TAG;

/**
 * Created by Nyemo on 09/03/2018.
 */

public abstract class AudioDecoder {

    private static MediaExtractor extractor = new MediaExtractor();
    private static MediaCodec decoder;

    private static MediaFormat inputFormat;

    private static ByteBuffer[] inputBuffers;
    private static boolean end_of_input_file = false;
    private static ArrayList<ByteBuffer> mArrayBuffer = new ArrayList<ByteBuffer>();

    public static void AudioDecoder(String audioFile) throws IOException, IllegalArgumentException {

        try {
            extractor.setDataSource(audioFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (extractor != null) {
            inputFormat = extractor.getTrackFormat(0);
            extractor.selectTrack(0);
            ByteBuffer audioBuffer = ByteBuffer.allocate(10000);
            int i = 0;
            boolean sampleAvailable = true;
     /*       while(sampleAvailable) {

               // Log.i(SysFonctions.TAG, " time: " + extractor.getSampleTime());
//                int size = extractor.readSampleData(audioBuffer, i);
                Log.i(SysFonctions.TAG, " size: " + size);
                i += size;
                sampleAvailable = extractor.advance();
            } */

           // int size = extractor.readSampleData(audioBuffer, 0);
            String mime = inputFormat.getString(MediaFormat.KEY_MIME);
            decoder = MediaCodec.createEncoderByType(mime);
            Log.i(TAG, " track: " + extractor.getTrackCount());

          // Log.i(SysFonctions.TAG, " avance: " + extractor.advance());
            Log.i(TAG, " mime: " + mime);
            Log.i(TAG, " i: " + i);
           // Log.i(SysFonctions.TAG, " size: " + size);
            Log.i(TAG, " rate: " + inputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE));
            Log.i(TAG, " duration: " + inputFormat.getLong(MediaFormat.KEY_DURATION)/1000000);
           // Log.i(SysFonctions.TAG, " encoding: " + inputFormat.getEncoding());
        }
       if (decoder == null) {
            throw new IllegalArgumentException("Error mime");
        }
        try {

            decoder.configure(inputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            inputFormat = decoder.getInputFormat();
            Log.i(TAG, " rate: " + inputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE));
            Log.i(TAG, " encoding: " + inputFormat.getInteger(MediaFormat.KEY_PCM_ENCODING));

            decoder.start();
            inputBuffers = decoder.getInputBuffers();
            Log.i(TAG, " taille " + inputBuffers.length);
        } catch (MediaCodec.CodecException ce) {
            Log.i(TAG, " codec erro: " + ce.getErrorCode());
        }
        int sampleBufferSize = 0;
        int sampleSize = 0;
        do {
            int inputBufferIndex = decoder.dequeueInputBuffer(10000);

            short[] rsample = getSamplesForChannel(decoder, inputBufferIndex);
            for(int i = 0; i < rsample.length; i++) {
                Log.i(TAG, "AudioDecoder: " + rsample[i]);
            }


           //ByteBuffer currentBuffer = ByteBuffer.allocate(100);
            if (inputBufferIndex >= 0) {
                //int size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
                ByteBuffer currentBuffer = decoder.getInputBuffer(inputBufferIndex);
                int size = extractor.readSampleData(currentBuffer, 0);
                mArrayBuffer.add(currentBuffer);
                sampleBufferSize += size;
                //tempstotal += extractor.getSampleTime();
                Log.i(TAG, " inputBufferIndex: " + inputBufferIndex);
                Log.i(TAG, " size: " + size);
                Log.i(TAG, " buffer: " + currentBuffer.get());
                Log.i(TAG, " temps: " + extractor.getSampleTime());
               // Log.i(SysFonctions.TAG, " rate: " + extractor.get);
                if (size < 0) {
                    // End Of File
                    decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    end_of_input_file = true;
                } else {
                    decoder.queueInputBuffer(inputBufferIndex, 0, size, extractor.getSampleTime(), 0);
                    //extractor.advance();
                } }

        sampleSize++;

        } while (extractor.advance());
/*
        Log.i(TAG, " sample size: " + sampleSize );
        if (sampleBufferSize != 0 ) {
            ByteBuffer sampleBuffer = ByteBuffer.allocate(sampleBufferSize);

            sampleBuffer.clear();
            for (int i = 0; i < mArrayBuffer.size(); i++) {
                //Byte mb = mArrayBuffer.get(i).get();
                sampleBuffer.put(mArrayBuffer.get(i));
            }
            //int[] mib = sampleBuffer.
            //DoubleBuffer dib = sampleBuffer.asDoubleBuffer();
            byte[] sampleBytes = sampleBuffer.array();
            for(int j =0; j < sampleBytes.length; j++) {
                Log.i(TAG, " sample[ " + j + "] :" + sampleBytes[j]);
            }

           /* Log.i(SysFonctions.TAG, " samplebuffer: " + sampleBuffer.get(0));
            Log.i(SysFonctions.TAG, " samplebuffer array: " + sampleBuffer.array()[0]);
            Log.i(SysFonctions.TAG, " samplebuffer array size: " + sampleBuffer.array().length); */

 //       }



      /*  for (; ; ) {
            // Read data from the file into the codec.
            if (!end_of_input_file) {
                int inputBufferIndex = decoder.dequeueInputBuffer(10000);
                Log.i(SysFonctions.TAG, " inputBufferIndex: " + inputBufferIndex);
                if (inputBufferIndex >= 0) {
                    int size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
                    Log.i(SysFonctions.TAG, " size: " + size);
                    if (size < 0) {
                        // End Of File
                        decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        end_of_input_file = true;
                    } else {
                        decoder.queueInputBuffer(inputBufferIndex, 0, size, extractor.getSampleTime(), 0);
                        extractor.advance();
                    }
                }
            }


        } */
    }

    private static short[] getSamplesForChannel(MediaCodec codec, int bufferId) {
        ByteBuffer outputBuffer = codec.getOutputBuffer(bufferId);
        MediaFormat format = codec.getOutputFormat(bufferId);
        ShortBuffer samples = outputBuffer.order(ByteOrder.nativeOrder()).asShortBuffer();

        short[] res = new short[samples.remaining()];
        for (int i = 0; i < res.length; ++i) {
            res[i] = samples.get(i);
        }
        return res;
    }

    public static void readAudioSample(String audioFile) throws IOException, IllegalArgumentException {

        try {
            extractor.setDataSource(audioFile);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
