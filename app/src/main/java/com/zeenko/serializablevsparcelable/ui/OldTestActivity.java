package com.zeenko.serializablevsparcelable.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.ui.services.MyService;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.getFormattedBytesToString;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaParceler;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaSerialization;

public class OldTestActivity extends AppCompatActivity {

    public static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    public static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    MyService service;
    boolean isBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            OldTestActivity.this.service = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_test_activity);

        EditText numberPicker = findViewById(R.id.numberPicked);
        findViewById(R.id.Start).setOnClickListener(
                (view) -> {
                    int size = Integer.parseInt(numberPicker.getText().toString());
                    if (size > 1) {
                        ExtraKeysGeneratorUtility.SIZE = size;
                        if (isBound) {
                            new Thread(() -> service.readArrayFromBundle(writeArraysToBundle())).start();
                        }
                    } else if (size == 1) {
                        if (isBound) {
                            new Thread(() -> service.readFromBundle(writeToBundle())).start();
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        isBound = false;
    }

    private Intent writeToIntent() {
        Intent intent = new Intent();

        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            new MyParcelableClass();
        }
        timeUtility.end();
        Logger.logD(null, "1. Time Parcelable create: " + timeUtility.getResultInMs() + " ms");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            intent.putExtra(key, new MyParcelableClass());
        }
        timeUtility.end();
        Logger.logD(null, "1. Time Parcelable write: " + timeUtility.getResultInMs() + " ms");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            new MySerializableClass();
        }
        timeUtility.end();
        Logger.logD(null, "2. Time Serializable create: " + timeUtility.getResultInMs() + " ms");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, new MySerializableClass());
        }
        timeUtility.end();
        Logger.logD(null, "2. Time Serializable write: " + timeUtility.getResultInMs() + " ms");

        return intent;
    }

    private Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        Parcelable parcelable = new MyParcelableClass();
        Serializable serializable = new MySerializableClass();
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        bundle.putParcelable(PARCELABLE_EXTRA, parcelable);
        timeUtility.end();
        byte[] parceledSize = marshallToByteArrayViaParceler(parcelable);

        Logger.logD("[bundle]", "1. Write Parcelable: " + timeUtility.getResult() + " ns. Bytes: " + getFormattedBytesToString(parceledSize));
        timeUtility.start();
        bundle.putSerializable(SERIALIZABLE_EXTRA, serializable);
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializable);
        Logger.logD("[bundle]", "2. Write Serializable: " + timeUtility.getResult() + " ns. Bytes: " + getFormattedBytesToString(serializedSize));

        return bundle;
    }

    private Bundle writeArraysToBundle() {
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelables = (ArrayList<Parcelable>) getParcelableArray();
        List<Serializable> serializables = getSerializableArray();
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        bundle.putParcelableArrayList(PARCELABLE_EXTRA, parcelables);
        timeUtility.end();
        byte[] parceledSize = marshallToByteArrayViaParceler(parcelables);
        Logger.logD("[bundle]", "1. Write Parcelable: " + timeUtility.getResult() + " ns. Bytes: " + getFormattedBytesToString(parceledSize));
        int index = 0;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            bundle.putSerializable(key, serializables.get(index));
            index++;
        }
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializables);
        Logger.logD("[bundle]", "2. Write Serializable: " + timeUtility.getResult() + " ns. Bytes: " + getFormattedBytesToString(serializedSize));

        return bundle;
    }

    private List<Parcelable> getParcelableArray() {
        List<Parcelable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MyParcelableClass());
        }
        return data;
    }

    private List<Serializable> getSerializableArray() {
        List<Serializable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MySerializableClass());
        }
        return data;
    }

}
