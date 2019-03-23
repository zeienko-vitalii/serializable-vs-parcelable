package com.zeenko.serializablevsparcelable.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.util.Random;

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
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            MyParcelableClass a = writeToBundle.getParcelable(key);
        }
        timeUtility.end();

        Logger.logD("[bundle]", "1. Time Parcelable read: " + timeUtility.getResultInMs() + " ms");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            MySerializableClass a = (MySerializableClass) writeToBundle.getSerializable(key);
        }
        timeUtility.end();
        Logger.logD("[bundle]", "2. Time Serializable read: " + timeUtility.getResultInMs() + " ms");
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }
}
