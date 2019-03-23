package com.zeenko.serializablevsparcelable.models.simple;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    private int data;

    private MyParcelable(Parcel in) {
        data = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(data);
    }
}
