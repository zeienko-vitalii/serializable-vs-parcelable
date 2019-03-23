package com.zeenko.serializablevsparcelable.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.TreeNode;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    private static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    private static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    private final int SIZE = 1;
    private LinearLayout results;
    private ScrollView scroll;
    private TimeUtility timeUtility = new TimeUtility();
    private ProgressBar progressBarWrite;
    private ProgressBar progressBarRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        results = findViewById(R.id.results);
        scroll = findViewById(R.id.scroll);
        Button btnParcelable = findViewById(R.id.testParcelable);
        Button btnSerializable = findViewById(R.id.testSerializable);
        btnParcelable.setOnClickListener((view) -> new Thread(() -> testParcelableBundle(view)).start());
        btnSerializable.setOnClickListener((view) -> new Thread(() -> testSerializableBundle(view)).start());
        progressBarWrite = findViewById(R.id.pbProgressWrite);
        progressBarRead = findViewById(R.id.pbProgressRead);
        progressBarWrite.setMax(SIZE);
        progressBarRead.setMax(SIZE);
    }

    private void updateProgressBarWrite(int value) {
        progressBarWrite.post(() -> progressBarWrite.setProgress(value));
    }

    private void updateProgressBarRead(int value) {
        progressBarRead.post(() -> progressBarWrite.setProgress(value));
    }

    public void testParcelable(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();

        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            root.writeToParcel(parcel, 0);
            updateProgressBarWrite(i+1);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
        Parcel parcel1 = Parcel.obtain();
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            restored = TreeNode.CREATOR.createFromParcel(parcel1);
            updateProgressBarRead(i+1);
        }
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + restored.toString());
        parcel.recycle();
        parcel1.recycle();
    }

    public void testParcelableBundle(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();
        Bundle bundle = new Bundle();

        timeUtility.start();
        bundle.putParcelable(PARCELABLE_EXTRA, root);
//        root.writeToParcel(parcel, 0);
        for (int i = 0; i < SIZE; i++) {
            bundle.writeToParcel(parcel, 0);
            updateProgressBarWrite(i+1);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
//        Parcel parcel1 = Parcel.obtain();
        Bundle bundleRestored = new Bundle();
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
//            bundle.readFromParcel(parcel1);
//            bundleRestored.readFromParcel(parcel);
            bundleRestored = Bundle.CREATOR.createFromParcel(parcel);
            restored = bundleRestored.getParcelable(PARCELABLE_EXTRA);
            updateProgressBarRead(i+1);
        }
//        restored = TreeNode.CREATOR.createFromParcel(parcel1);
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + (restored != null ? restored.toString() : "restored is null"));
        parcel.recycle();
//        parcel1.recycle();
    }

    public void testSerializable(View view) {
        TreeNode root = createNode(0);
        byte[] byteArray = new byte[0];

        try (ByteArrayOutputStream bas = new ByteArrayOutputStream(1_000_000);
             ObjectOutputStream out = new ObjectOutputStream(bas)) {
            timeUtility.start();
            for (int i = 0; i < SIZE; i++) {
                out.writeObject(root);
            }
            timeUtility.end();
            byteArray = bas.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int length = byteArray.length;
        TreeNode restored = null;
        long finish1 = timeUtility.getResultInMs();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            timeUtility.start();
            for (int i = 0; i < SIZE; i++) {
                restored = (TreeNode) in.readObject();
            }
            timeUtility.end();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addResult("serialize: " + finish1 + "ms; deserialize: "
                + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, restored != null ? "Restored:\n" + restored.toString() : "restored is null");

    }

    public void testSerializableBundle(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();
        Bundle bundle = new Bundle();

        timeUtility.start();
        bundle.putSerializable(SERIALIZABLE_EXTRA, root);
//        root.writeToParcel(parcel, 0);
        for (int i = 0; i < SIZE; i++) {
            bundle.writeToParcel(parcel, 0);
            updateProgressBarWrite(i);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
//        Parcel parcel1 = Parcel.obtain();
        Bundle bundleRestored = null;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
//            bundle.readFromParcel(parcel1);
            bundleRestored = Bundle.CREATOR.createFromParcel(parcel);
            restored = bundleRestored.getParcelable(SERIALIZABLE_EXTRA);
            updateProgressBarRead(i);
        }
//        restored = TreeNode.CREATOR.createFromParcel(parcel1);
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + (restored != null ? restored.toString() : "restored is null"));
        parcel.recycle();
//        parcel1.recycle();

    }

    private TreeNode createNode(int level) {
        if (level < 4) {
            return createRootNode(level + 1);
        } else {
            return createSimpleNode();
        }

    }

    private TreeNode createRootNode(int level) {
        TreeNode root = createSimpleNode();
        root.children = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            root.children.add(createNode(level));
        }
        return root;
    }

    private TreeNode createSimpleNode() {
        TreeNode root = new TreeNode();
        root.string0 = "aaaaaaaaaa";
        root.string1 = "bbbbbbbbbb";
        root.string2 = "cccccccccc";
        root.int0 = 111111111;
        root.int1 = 222222222;
        root.int2 = 333333333;
        root.boolean0 = true;
        root.boolean1 = false;
        root.boolean2 = true;
        return root;
    }

    private void addResult(String message) {
        Looper.prepare();
        new Handler().post(() -> {
            TextView result = new TextView(this);
            result.setText(message);
            result.setPadding(10, 10, 10, 10);
            results.addView(result);
        });
        scroll.post(() -> scroll.scrollTo(0, scroll.getBottom()));
    }
}
