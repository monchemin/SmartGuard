package ca.uqac.nyemo.voice;

import android.util.Log;

import java.util.ArrayList;

import ca.uqac.nyemo.utils.SignalProcess;
import ca.uqac.nyemo.utils.SysFonctions;
import ca.uqac.nyemo.utils.wavelet.DWT;
import ca.uqac.nyemo.utils.wavelet.Wavelet;

/**
 * Created by Nyemo on 08/03/2018.
 * this section is built follow this paper
 * Speaker Recognition on Mobile Phone: Using Wavelet, Cepstral Coefficients and Probabilisitc Neural Network
 */

public class AudioProcess {

    public static int  trameMaxLenght = Audio.PROCESS_TRAME_LENGHT;

    static  ArrayList<double[]> signalTrameFeatures = new ArrayList<double[]>();

    protected  double[] signalFeatures = new double[Audio.TRAIN_ARRAY_SIZE];

    public ArrayList<double[]> computeTrameFeatures(double[] audioRecord) throws Exception {
        signalTrameFeatures.clear();
       int  signalFeaturesCurrentPos = 0;
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
            signalTrameFeatures.add(trameFeature);
            System.arraycopy(trameFeature, 0, signalFeatures, signalFeaturesCurrentPos, trameFeature.length);
            signalFeaturesCurrentPos += trameFeature.length;

        } // end of the selected trame process

  return signalTrameFeatures;
    }

    public  double[] computeSignalFeatures(ArrayList<short[]> audioRecord) throws Exception {
        signalTrameFeatures.clear();
        int signalFeaturesCurrentPos = 0;
        int trameNumber = 0;
        // begin loop for process audio
       for(short[] shortTrame: audioRecord)
       //for(int audirecIndex = 10; audirecIndex < 15; audirecIndex++)

        {
           /* short[] shortTrame = audioRecord.get(audirecIndex);
            for(short amplitude : shortTrame) {
                Log.e(SysFonctions.TAG, " " + amplitude );
            }*/
            double[] trame = SysFonctions.convertShortToDouble(shortTrame); //convert short array to double array

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


                int levelLenght = trameDWT.length;

                //retrieve each level and compute features

                for (int levelIndex = 1; levelIndex <= Audio.WAVELET_ORDER  ; levelIndex++) {
                    levelLenght /= 2 ; // each level length is the half of the previous

                    double[] levelSample = new double[levelLenght];

                    System.arraycopy(trameDWT, levelLenght-1, levelSample, 0, levelLenght); // retrieve level coefficients


                    double levelFeatures = computeTrameFeatures(levelSample, levelIndex); // compute feature
                    trameFeature[trameFeatureIndex] = levelFeatures;
                    //Log.e(SysFonctions.TAG, " feature["+trameFeatureIndex+"] : " + levelFeatures );
                    trameFeatureIndex++;

                } // in this end levelFeature has the first 8 feature because we set DWT order to 8
                // now compute delta features
                for (int deltaIndex = 1; deltaIndex <= Audio.WAVELET_ORDER  ; deltaIndex++) {

                    double delta = 0;
                    int denominator = 0;
                    for(int p = 1; p <= 2; p++) {
                        double first = trameFeature[deltaIndex + p];
                        double second = 0;

                        if(deltaIndex - p >= 0) {
                            second = trameFeature[deltaIndex - p];
                        }
                        delta +=  p*(first - second);
                        denominator += Math.pow(p, 2);
                    }
                    trameFeature[trameFeatureIndex] = delta / denominator;
                   // Log.e(SysFonctions.TAG, " feature["+trameFeatureIndex+"] : " + delta/denominator );
                    trameFeatureIndex++;


                }
                // now compute delta delta as delta use delta coefficient (trameFeature[8 - 16]
                //set deltadelat index by wavelet_order: 8 in current context
                for (int deltaDeltaIndex = Audio.WAVELET_ORDER; deltaDeltaIndex < Audio.WAVELET_ORDER*2  ; deltaDeltaIndex++) {

                    double delta = 0;
                    int denominator = 0;
                    for(int p = 1; p <= 2; p++) {
                        delta +=  p*(trameFeature[deltaDeltaIndex + p] - trameFeature[deltaDeltaIndex - p]);
                        denominator += Math.pow(p, 2);
                    }
                    trameFeature[trameFeatureIndex] = delta / denominator;
                    trameFeatureIndex++;

                } // after this section we have filled trameFeature with all computed coefficients.

                // process end. now add trame feature to arraylist
                signalTrameFeatures.add(trameFeature);
                System.arraycopy(trameFeature, 0, signalFeatures, signalFeaturesCurrentPos, 24);
                signalFeaturesCurrentPos += 24;
                trameNumber++;
                if(trameNumber == Audio.TRAME_NUMBER) {
                    break;
                }
            }



        } // end of the selected trame process

        return signalFeatures;
    }

    protected static double computeTrameFeatures(double[] trame, int waveletLevelIndex) {
        double noiseTh = SignalProcess.noiseThreshold(trame); // get trame threshold
        // normalize by the threshold
        double trameFeature = 0;
        for (int i = 0; i < trame.length; i++) {
          //  Log.e(SysFonctions.TAG, " trame["+i+"] : " + trame[i] + " noise : " + noiseTh);
            if(trame[i] < noiseTh) { // trame[i] = 0 if < noiseTh
                trame[i] = 0;
            }

            double zt = computeZ(trame[i]);
            trameFeature += computeZ(trame[i])*Math.cos(waveletLevelIndex*(i-0.5)*Math.PI/trame.length);
        } // end
        return trameFeature;


    }
// compute Z
    protected static double computeZ(double trameIndex) {
        if(trameIndex != 0) {
            return Math.log(Math.pow(trameIndex, 2));
        } else return 0;
    }


}
