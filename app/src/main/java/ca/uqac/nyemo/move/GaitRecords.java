package ca.uqac.nyemo.move;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Nyemo on 15/02/2018.
 * this is a wrapper class for easy using of gait records send by GaitTrainService
 */

public class GaitRecords implements Parcelable {

    ArrayList<Float> gRecord = new ArrayList<>();

    public   GaitRecords(ArrayList<Float> gRecord) {
        this.gRecord = gRecord;
    }
    public   GaitRecords(Parcel in) {
       this.gRecord = in.readArrayList(GaitRecords.class.getClassLoader());
    }

    public  ArrayList<Float> getRecord () {
        return this.gRecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.gRecord);

    }
    public static final Parcelable.Creator<GaitRecords> CREATOR =
            new Parcelable.Creator<GaitRecords>() {

                @Override
                public GaitRecords createFromParcel(Parcel source) {
                    return new GaitRecords(source);
                }

                @Override
                public GaitRecords[] newArray(int size) {
                    return new GaitRecords[0];
                }
            };
}
