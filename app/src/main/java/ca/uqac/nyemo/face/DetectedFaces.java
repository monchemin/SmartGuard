package ca.uqac.nyemo.face;

import android.os.Parcel;
import android.os.Parcelable;

import org.opencv.core.Mat;

/**
 * Created by Nyemo on 01/02/2018.
 */

public class DetectedFaces implements Parcelable {
    private Mat mFace;

    public DetectedFaces(Mat face) {
        mFace = face;
    }

    protected DetectedFaces(Parcel in) {
    }

    public static final Creator<DetectedFaces> CREATOR = new Creator<DetectedFaces>() {
        @Override
        public DetectedFaces createFromParcel(Parcel in) {
            return new DetectedFaces(in);
        }

        @Override
        public DetectedFaces[] newArray(int size) {
            return new DetectedFaces[size];
        }
    };

    public Mat getmFace() {
        return mFace;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeArray();
    }
}
