package ca.uqac.nyemo.utils;

import android.util.Log;

import ca.uqac.nyemo.utils.wavelet.DWT;
import ca.uqac.nyemo.utils.wavelet.Wavelet;
import ca.uqac.nyemo.voice.Audio;

/**
 * Created by Nyemo on 26/03/2018.
 * this class contains some methods for signal processing
 */


public abstract class SignalProcess {
    /***
        compute hamming window for gived signal as double[]
     method : w(n) =
     @return : window
     */
    public static void hammingWindow(int windowSize) {

        double[] window = new double[windowSize]; // create returned window

        for (int i = 0; i <windowSize; i++) {

            window[i] = 0.54d + 0.46*Math.cos(2*Math.PI*i)/windowSize;
        }

    }


    /**
     * Teager Energy Operator let to differenciate noise in the signal
     * @param dwt
     * @return teo
     * phi[x(n)] = x(n)2 - x(n + 1)x(n -1)
     */
    public static double[] computeTEO(double[] dwt) {
        int len = dwt.length;
        double[] teo = new double[len];

        teo[0] = Math.pow(dwt[0], 2);

        for (int i = 1; i < len-1; i++) {
            teo[i] = Math.pow(dwt[i], 2) - (dwt[i+1]*dwt[i-1]);
        }
        teo[len-1] = Math.pow(dwt[len-1], 2);

        return teo;
    }

    /**
     * variance
     * @param sample
     * @return
     */
    public static double computeVariance(double[] sample) {
        double variance = 0.0d;
        int sampleLen = sample.length;

        double mean = computeMean(sample); //original sample

        for (int i = 0; i < sampleLen-1; i++) {
            sample[i] = Math.pow((sample[i]-mean), 2);
        }

        variance = computeMean(sample); // variance sample

        return variance;
    }

    /**
     * mean value
     * @param sample
     * @return
     */
    public static double computeMean(double[] sample) {
        double mean = 0.0d;

        for (int i = 0; i < sample.length ; i++) {
            mean += sample[i];
        }

        return mean / sample.length;
    }

    public static double computeDeviation (Double variance) {
        return Math.sqrt(variance);
    }

    /**
     * compture sample threshold : ecart-type * sqrt(2*log(n))
     * @param sample

     * @return
     */
    public static double noiseThreshold(double[] sample) {

        int sampleLen = sample.length;
        double variance = computeVariance(sample);

        return computeDeviation(variance)*Math.sqrt(2*Math.log(sampleLen));

    }

    /**
     * VAD is compute follow :
     * @param sample
     * @return
     * @throws Exception
     */
    public static boolean VoiceActivityDection(double[] sample) throws Exception {
        boolean vad = false; // suppose that no activity
        double variance = 0;

       // Log.e(SysFonctions.TAG, " trame " );
      //  for(double e : sample) {Log.e(SysFonctions.TAG, "" + e ); } //

        // compute DWT of the trame
        double[] dwt = DWT.forwardDwt(sample, Wavelet.Daubechies, Audio.DAUBECHIES_WAVELET_ORDER, Audio.WAVELET_ORDER);
      //  for(int di = 0; di < dwt.length; di++) {
        //    Log.e(SysFonctions.TAG, "trame : " + sample[di] + " dwt : " + dwt[di] );
       // }
        int levelLenght = dwt.length;
        for (int levelIndex = 1; levelIndex <= Audio.WAVELET_ORDER  ; levelIndex++) {
            levelLenght /= 2 ; // each level length is the half of the previous

            double[] levelSample = new double[levelLenght];

            System.arraycopy(dwt, levelLenght-1, levelSample, 0, levelLenght); // retrieve level coefficients
           // Log.e(SysFonctions.TAG, " dwt["+levelIndex+"] : " );
            //for(double e : levelSample) {Log.e(SysFonctions.TAG, "" + e ); }
            double[] teo = computeTEO(levelSample); // compute TEO
            variance += computeVariance(teo); // variance of TEO

        }

        //Log.i(SysFonctions.TAG, "VoiceActivityDection: " + variance);
        if (variance >= Audio.VAD_THRESHOLD) // if variance is up defined threshold
        {
            vad = true;
        }
        return vad;
    }

    /**
     * normalization follow Speaker Recognition on Mobile Phone: Using Wavelet, Cepstral Coefficients and Probabilisitc Neural Network
     * @param sample
     * @return normalize sample
     */
    public static double[] normalizeSample(double[] sample) {

        double mean = computeMean(sample);
        double deviation = computeDeviation(computeVariance(sample));

        for (int i = 0; i < sample.length ; i++) {
            sample[i] = (sample[i] - mean)/deviation;
        }
        return sample;

    }
}
