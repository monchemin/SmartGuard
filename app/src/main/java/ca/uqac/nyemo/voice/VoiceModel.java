package ca.uqac.nyemo.voice;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import ca.uqac.nyemo.utils.SysFonctions;

/**
 * Created by Nyemo on 06/04/2018.
 */

public class VoiceModel implements Serializable {
    protected ArrayList<double[]> currentModel = new ArrayList<>();
    public static final long serialVersionUID = 2307891229331897661L;

    protected void addEntry(double[] newAdd) {
        currentModel.add(newAdd);
    }

    protected void saveModel(String modelFile) throws IOException {

        SysFonctions.saveModel(modelFile, this);

    }

    public static VoiceModel loadVoiceModel(String modelFile) {
        try {
            return (VoiceModel) SysFonctions.loadModel(modelFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
