package ca.uqac.nyemo.voice;

import java.util.ArrayList;

import ca.uqac.nyemo.utils.SignalProcess;
import ca.uqac.nyemo.utils.wavelet.DWT;
import ca.uqac.nyemo.utils.wavelet.Wavelet;

/**
 * Created by Nyemo on 08/03/2018.
 * this section is built follow this paper
 * Speaker Recognition on Mobile Phone: Using Wavelet, Cepstral Coefficients and Probabilisitc Neural Network
 */

public class AudioProcess {

    public int  trameMaxLenght = Audio.PROCESS_TRAME_LENGHT;

    ArrayList<double[]> audioTrameFeatures = new ArrayList<double[]>();

    public ArrayList<double[]> computeTrameFeatures(double[] audioRecord) throws Exception {
// begin loop for process audio
        for (int i = 0; i < audioRecord.length ; i += trameMaxLenght) {

            double[] trame = new double[trameMaxLenght]; // select trame as double[}

            System.arraycopy(audioRecord, i, trame, 0, trameMaxLenght ); // fill trame by audio info

            double[] trameFeature = new double[24]; // array of selected trame features(level1, delta and delta delta 8 * 3 = 24)
            int trameFeatureIndex = 0; // set feature index
//now we have to detected voice activity in the trame.
// trame will be rejected if VAD is below threshold
            boolean isVAD = SignalProcess.VoiceActivityDection(trame); // check Voice Activity
            if(isVAD) {
                // we can now compute trame features

                trame = SignalProcess.normalizeSample(trame); // normalize trame
                //compute DWT
                double[] trameDWT = DWT.forwardDwt(trame, Wavelet.Daubechies, Audio.DAUBECHIES_WAVELET_ORDER, Audio.WAVELET_ORDER);


                int levelLenght = trame.length;

                //retrieve each level and compute features

                for (int levelIndex = 1; levelIndex <= Audio.WAVELET_ORDER  ; levelIndex++) {
                     levelLenght = (int) (levelLenght / Math.pow(2, i)); // each level length is the half of the previous

                    double[] levelSample = new double[levelLenght];

                    System.arraycopy(trameDWT, levelLenght, levelSample, 0, levelLenght); // retrieve level coefficients

                    double levelFeatures = computeTrameFeatures(levelSample, levelIndex); // compute feature
                    trameFeature[trameFeatureIndex] = levelFeatures;
                    trameFeatureIndex++;

                } // in this end levelFeature has the first 8 feature because we set DWT order to 8
                // now compute delta features
                for (int deltaIndex = 1; deltaIndex <= Audio.WAVELET_ORDER  ; deltaIndex++) {

                    double delta = 0;
                    int denominator = 0;
                    for(int p = 1; p <= 2; p++) {
                        delta +=  p*(trameFeature[deltaIndex + p] - trameFeature[deltaIndex - p]);
                        denominator += Math.pow(p, 2);
                    }
                    trameFeature[trameFeatureIndex] = delta / denominator;
                    trameFeatureIndex++;

                }
                // now compute delta delta as delta use delta coefficient (trameFeature[8 - 16]
                //set deltadelat index by wavelet_order: 8 in current context
                for (int deltaDeltaIndex = Audio.WAVELET_ORDER; deltaDeltaIndex <= Audio.WAVELET_ORDER*2  ; deltaDeltaIndex++) {

                    double delta = 0;
                    int denominator = 0;
                    for(int p = 1; p <= 2; p++) {
                        delta +=  p*(trameFeature[deltaDeltaIndex + p] - trameFeature[deltaDeltaIndex - p]);
                        denominator += Math.pow(p, 2);
                    }
                    trameFeature[trameFeatureIndex] = delta / denominator;
                    trameFeatureIndex++;

                } // after this section we have filled trameFeature with all computed coefficients.

            }
            // process end. now add trame feature to arraylist
            audioTrameFeatures.add(trameFeature);


        } // end of the selected trame process

  return audioTrameFeatures;
    }

    public double computeTrameFeatures(double[] trame, int waveletLevelIndex) {
        double noiseTh = SignalProcess.noiseThreshold(trame); // get trame threshold
        // normalize by the threshold
        double trameFeature = 0;
        for (int i = 0; i < trame.length; i++) {
            if(trame[i] < noiseTh) { // trame[i] = 0 if < noiseTh
                trame[i] = 0;
            }
            double zt = computeZ(trame[i]);
            trameFeature += computeZ(trame[i])*Math.cos(waveletLevelIndex*(trame[i]-0.5)*Math.PI/trame.length);
        } // end
        return trameFeature;


    }
// compute Z
    protected double computeZ(double trameIndex) {
        return Math.log(Math.pow(trameIndex, 2));
    }

}
