package com.zeenko.serializablevsparcelable.ui;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class PerformanceMemoryTestActivity extends AppCompatActivity {
    private static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    private static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    private int SIZE = 1;
    private int nodesDepth = 0;
    private LinearLayout results;
    private ScrollView scroll;
    private TimeUtility timeUtility = new TimeUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performance_memory_test_activity);
        results = findViewById(R.id.results);
        scroll = findViewById(R.id.scroll);
        Button btnParcelable = findViewById(R.id.testParcelable);
        Button btnSerializable = findViewById(R.id.testSerializable);
        EditText edSize = findViewById(R.id.edSize);
        EditText edNodes = findViewById(R.id.edNodesNumber);
//        btnParcelable.setOnClickListener((view) -> new Thread(() -> testParcelableWithParcel(view)).start());
        btnParcelable.setOnClickListener((view) -> new Thread(() -> {
            try {
                SIZE = Integer.parseInt(edSize.getText().toString());
                nodesDepth = Integer.parseInt(edNodes.getText().toString());
            } catch (Exception ignored) {
            }
            testParcelable(view);
        }).start());
//        btnSerializable.setOnClickListener((view) -> new Thread(() -> testSerializableWithParcel(view)).start());
        btnSerializable.setOnClickListener((view) -> new Thread(() -> {
            try {
                SIZE = Integer.parseInt(edSize.getText().toString());
                nodesDepth = Integer.parseInt(edNodes.getText().toString());
            } catch (Exception ignored) {
            }
            testSerializable(view);
        }).start());
    }


    public void testParcelable(View view) {
        TreeNode root = createNode(nodesDepth);
        Parcel parcel = Parcel.obtain();

        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            root.writeToParcel(parcel, 0);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            restored = TreeNode.CREATOR.createFromParcel(parcel);
        }
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, restored != null ? "Restored:\n" + restored.toString() : "restored is null");
        parcel.recycle();
    }


    public void testSerializable(View view) {
        TreeNode root = createNode(nodesDepth);
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

    public void testParcelableWithParcel(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();

        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            parcel.writeParcelable(root, 0);
//            root.writeToParcel(parcel, 0);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            restored = parcel.readParcelable(TreeNode.class.getClassLoader());
//            restored = TreeNode.CREATOR.createFromParcel(parcel);
        }
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + (restored != null ? restored.toString() : "restored is null"));
        parcel.recycle();
    }

    public void testSerializableWithParcel(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();

        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            parcel.writeSerializable(root);
//            root.writeToParcel(parcel, 0);
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
            restored = (TreeNode) parcel.readSerializable();
//            restored = TreeNode.CREATOR.createFromParcel(parcel);
        }
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + (restored != null ? restored.toString() : "restored is null"));
        parcel.recycle();
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
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
//        Parcel parcel1 = Parcel.obtain();
        Bundle bundleRestored;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
//            bundle.readFromParcel(parcel1);
//            bundleRestored.readFromParcel(parcel);
            bundleRestored = Bundle.CREATOR.createFromParcel(parcel);
            restored = bundleRestored.getParcelable(PARCELABLE_EXTRA);
        }
//        restored = TreeNode.CREATOR.createFromParcel(parcel1);
        timeUtility.end();

        addResult("parcel: " + finish + "ms; unparcel: " + timeUtility.getResultInMs() + "ms; size: " + length);

        Logger.logD(null, "Origin:\n" + root.toString());
        Logger.logD(null, "Restored:\n" + (restored != null ? restored.toString() : "restored is null"));
        parcel.recycle();
//        parcel1.recycle();
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
        }
        timeUtility.end();
        long finish = timeUtility.getResultInMs();

        int length = parcel.dataSize();
        parcel.setDataPosition(0); // reset for reading
        TreeNode restored = null;
//        Parcel parcel1 = Parcel.obtain();
        Bundle bundleRestored;
        timeUtility.start();
        for (int i = 0; i < SIZE; i++) {
//            bundle.readFromParcel(parcel1);
            bundleRestored = Bundle.CREATOR.createFromParcel(parcel);
            restored = bundleRestored.getParcelable(SERIALIZABLE_EXTRA);

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

    public void clear(View v) {
        results.post(() -> results.removeAllViews());
        scroll.post(() -> scroll.scrollTo(0, scroll.getBottom()));
    }

    private void addResult(String message) {
        TextView result = new TextView(this);
        result.setText(message);
        result.setPadding(10, 10, 10, 10);
        results.post(() -> results.addView(result));
        scroll.post(() -> scroll.scrollTo(0, scroll.getBottom()));
    }

}
