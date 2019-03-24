package com.zeenko.serializablevsparcelable.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.ui.services.MyService;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

public class OldTestActivity extends AppCompatActivity {

    public static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    public static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    MyService service;
    boolean isBound = false;
    private TextView tvMessage;

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
        tvMessage = findViewById(R.id.tvMessage);
    }

    private void updateMessageInMainThread(String msg) {
        tvMessage.setText(msg);
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

    /**
     * Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute)
     */
    public void onButtonClick(View v) {
        if (isBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = service.getRandomNumber();
            updateMessageInMainThread("number: " + num);
//            new Thread(() -> service.readFromIntent(writeToIntent())).start();
            new Thread(() -> service.readFromBundle(writeToBundle())).start();
        }
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

        MyParcelableClass myParcelableClass = new MyParcelableClass();
        MySerializableClass mySerializableClass = new MySerializableClass();
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            bundle.putParcelable(key, myParcelableClass);
        }
        timeUtility.end();
        Logger.logD("[bundle]", "1. Time Parcelable write to Bundle: " + timeUtility.getResultInMs() + " ms");
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            bundle.putSerializable(key, mySerializableClass);
        }
        timeUtility.end();
        Logger.logD("[bundle]", "2. Time Serializable write: " + timeUtility.getResultInMs() + " ms");

        return bundle;
    }


}
