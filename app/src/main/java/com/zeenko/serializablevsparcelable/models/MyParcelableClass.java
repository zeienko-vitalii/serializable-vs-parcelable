package com.zeenko.serializablevsparcelable.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MyParcelableClass implements Parcelable {
    public static final Creator<MyParcelableClass> CREATOR = new Creator<MyParcelableClass>() {
        @Override
        public MyParcelableClass createFromParcel(Parcel in) {
            return new MyParcelableClass(in);
        }

        @Override
        public MyParcelableClass[] newArray(int size) {
            return new MyParcelableClass[size];
        }
    };
    private String stringData = "";
    private int identy = 0;
    private boolean isActive = false;
    private float dest = 0.0f;
    private List<String> stringList;

    public MyParcelableClass() {
        stringData = UUID.randomUUID().toString();

        stringList = new ArrayList<>();
        Random rand = new Random();
        int maxI = rand.nextInt();

        for (int i = 0; i < maxI % 15; i++) {
            stringList.add(UUID.randomUUID().toString());
        }

        identy = rand.nextInt();
        isActive = rand.nextBoolean();
        dest = rand.nextFloat();

    }

    protected MyParcelableClass(Parcel in) {
        stringData = in.readString();
        identy = in.readInt();
        isActive = in.readByte() != 0;
        dest = in.readFloat();
        stringList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel destParcel, int flags) {
        destParcel.writeString(stringData);
        destParcel.writeInt(identy);
        destParcel.writeByte((byte) (isActive ? 1 : 0));
        destParcel.writeFloat(dest);
        destParcel.writeStringList(stringList);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
