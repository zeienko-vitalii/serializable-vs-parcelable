package com.zeenko.serializablevsparcelable.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.zeenko.serializablevsparcelable.ui.OldTestActivity.PARCELABLE_EXTRA;
import static com.zeenko.serializablevsparcelable.ui.OldTestActivity.SERIALIZABLE_EXTRA;

public class MyService extends Service {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * method for clients
     */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    public void readFromIntent(Intent intent) {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            MyParcelableClass a = intent.getParcelableExtra(key);
        }
        timeUtility.end();

        Logger.logD(null, "1. Time Parcelable read: " + timeUtility.getResultInMs() + " ms");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            MySerializableClass a = (MySerializableClass) intent.getSerializableExtra(key);
        }
        timeUtility.end();
        Logger.logD(null, "2. Time Serializable read: " + timeUtility.getResultInMs() + " ms");
    }

    public void readFromBundle(Bundle writeToBundle) {
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        Parcelable parcelable = writeToBundle.getParcelable(PARCELABLE_EXTRA);
        timeUtility.end();

        Logger.logD("[bundle]", "1. Time Parcelable read: " + timeUtility.getResult() + " ms");

        timeUtility.start();
        Serializable serializable = writeToBundle.getSerializable(SERIALIZABLE_EXTRA);
        timeUtility.end();
        Logger.logD("[bundle]", "2. Time Serializable read: " + timeUtility.getResult() + " ms");
    }

    public void readArrayFromBundle(Bundle writeToBundle) {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        List<Parcelable> parcelables;
        timeUtility.start();
        parcelables = writeToBundle.getParcelableArrayList(PARCELABLE_EXTRA);
        timeUtility.end();

        Logger.logD("[bundle]", "1. Time Parcelable read: " + timeUtility.getResult() + " ms" + " Data: " + (parcelables != null ? parcelables.isEmpty() ? " restored is empty" : " restored" : " array is null"));
        List<Serializable> serializables = new ArrayList<>();
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            serializables.add(writeToBundle.getSerializable(key));
        }
        timeUtility.end();
        Logger.logD("[bundle]", "2. Read Serializable: " + timeUtility.getResult() + " ms" + " Data: " + (serializables.isEmpty() ? " restored is null" : " restored"));
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);

        }
    }
}
