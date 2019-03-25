package com.zeenko.serializablevsparcelable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.models.complex.TreeNode;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.Logger;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.getFormattedBytesToString;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaParceler;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaSerialization;

public class FirstActivity extends AppCompatActivity {
    public static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    public static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    public static final String TIME_EXTRA = "TIME";
    public static final String IS_ONE_EXTRA = "IS_ONE_EXTRA";
    private EditText numberPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        numberPicker = findViewById(R.id.numberPicked);
        findViewById(R.id.btnStartSecondActivityWithParcelable).setOnClickListener(
                (view) -> {
                    int size = Integer.parseInt(numberPicker.getText().toString());
                    if (size > 1) {
                        ExtraKeysGeneratorUtility.SIZE = size;
                        startSecondActivityWithArray();
//                        treeNodeWithArray();
                    } else if (size == 1) {
//                        treeNodeWithOneElement();
                        startSecondActivityWithOneElement();
                    }
                }
        );
    }

    private void startSecondActivityWithOneElement() {
        Parcelable parcelable = new MyParcelableClass();
        Serializable serializable = new MySerializableClass();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putExtra(PARCELABLE_EXTRA, parcelable);
        timeUtility.end();

        byte[] parceledSize = marshallToByteArrayViaParceler(parcelable);

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. Bytes: ")
                .append(getFormattedBytesToString(parceledSize))
                .append("\n");

        timeUtility.start();
        intent.putExtra(SERIALIZABLE_EXTRA, serializable);
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializable);

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. Bytes: ")
                .append(serializedSize != null ? getFormattedBytesToString(serializedSize) : " exception")
                .append("\n");

        intent.putExtra(TIME_EXTRA, stringBuilder.toString());
        intent.putExtra(IS_ONE_EXTRA, true);
        startActivity(intent);
    }

    private void startSecondActivityWithArray() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        ArrayList<Parcelable> parcelables = (ArrayList<Parcelable>) getParcelableArray();
        List<Serializable> serializables = getSerializableArray();

        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putParcelableArrayListExtra(PARCELABLE_EXTRA, parcelables);
        timeUtility.end();

        byte[] parceledSize = marshallToByteArrayViaParceler(parcelables);

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(parcelables.size())
                .append("\nBytes: ")
                .append(getFormattedBytesToString(parceledSize))
                .append("\n");

        int index = 0;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, serializables.get(index));
            index++;
        }
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializables);

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(serializables.size())
                .append("\nBytes: ")
                .append(serializedSize != null ? getFormattedBytesToString(serializedSize) : " exception")
                .append("\n");

        intent.putExtra(TIME_EXTRA, stringBuilder.toString());
        startActivity(intent);
    }

    private void treeNodeWithOneElement() {
        Parcelable parcelable = createNode(0);
        Serializable serializable = createNode(0);
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putExtra(PARCELABLE_EXTRA, parcelable);
        timeUtility.end();

        byte[] parceledSize = marshallToByteArrayViaParceler(parcelable);

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. Bytes: ")
                .append(getFormattedBytesToString(parceledSize))
                .append("\n");

        timeUtility.start();
        intent.putExtra(SERIALIZABLE_EXTRA, serializable);
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializable);

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. Bytes: ")
                .append(serializedSize != null ? getFormattedBytesToString(serializedSize) : " exception")
                .append("\n");

        intent.putExtra(TIME_EXTRA, stringBuilder.toString());
        intent.putExtra(IS_ONE_EXTRA, true);
        Logger.logD(null, stringBuilder.toString());
    }

    private void treeNodeWithArray() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        ArrayList<Parcelable> parcelables = (ArrayList<Parcelable>) createListOfNodesAsParcelable(0);
        List<Serializable> serializables = createListOfNodesAsSerializable(0);

        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putParcelableArrayListExtra(PARCELABLE_EXTRA, parcelables);
        timeUtility.end();

        byte[] parceledSize = marshallToByteArrayViaParceler(parcelables);

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(parcelables.size())
                .append(" Bytes: ")
                .append(getFormattedBytesToString(parceledSize))
                .append("\n");

        int index = 0;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, serializables.get(index));
            index++;
        }
        timeUtility.end();

        byte[] serializedSize = marshallToByteArrayViaSerialization(serializables);

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(serializables.size())
                .append(" Bytes: ")
                .append(serializedSize != null ? getFormattedBytesToString(serializedSize) : " exception")
                .append("\n");

        Logger.logD(null, stringBuilder.toString());

//        intent.putExtra(TIME_EXTRA, stringBuilder.toString());
//        startActivity(intent);
    }

    private List<Parcelable> getParcelableArray() {
        List<Parcelable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE * 2);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MyParcelableClass());
        }
        return data;
    }

    private List<Serializable> getSerializableArray() {
        List<Serializable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE * 2);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MySerializableClass());
        }
        return data;
    }

    private List<Parcelable> createListOfNodesAsParcelable(int level) {
        List<Parcelable> nodes = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            nodes.add(createNode(0));
        }
        return nodes;
    }

    private List<Serializable> createListOfNodesAsSerializable(int level) {
        List<Serializable> nodes = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            nodes.add(createNode(0));
        }
        return nodes;
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
}
