package com.zeenko.serializablevsparcelable.utility;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class MemoryUtility {
    public static final NumberFormat NUMBER_FORMAT = new DecimalFormat("###,###.##");

    public static String getFormattedBytesToString(byte[] data) {
        return NUMBER_FORMAT.format(data.length);
    }

    public static byte[] marshallToByteArrayViaSerialization(List<Serializable> list) {
        if (list == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            for (Serializable item : list) {
                oos.writeObject(item);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] marshallToByteArrayViaSerialization(Serializable root) {
        if (root == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(root);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] marshallToByteArrayViaParceler(List<Parcelable> list) {
        if (list == null) return null;
        Parcel parcel = Parcel.obtain();
        for (Parcelable item : list) {
            item.writeToParcel(parcel, 0);
        }
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static byte[] marshallToByteArrayViaParceler(Parcelable root) {
        if (root == null) return null;
        Parcel parcel = Parcel.obtain();
        root.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

}
