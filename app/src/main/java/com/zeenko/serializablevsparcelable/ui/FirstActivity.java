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
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    public static final String PARCELABLE_EXTRA = "PARCELABLE_EXTRA";
    public static final String SERIALIZABLE_EXTRA = "SERIALIZABLE_EXTRA";
    private final int SIZE = 1000;
    private EditText numberPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        numberPicker = findViewById(R.id.numberPicked);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        findViewById(R.id.btnStartSecondActivityWithParcelable).setOnClickListener(
                (view) -> {
                    int size = Integer.parseInt(numberPicker.getText().toString());
                    if (size > 1) {
                        ExtraKeysGeneratorUtility.SIZE = size;
                        startSecondActivityWithArray(); //startSecondActivityWithParcelable()
                    } else if (size == 1) {
                        startSecondActivityWithParcelable();
                    }
                }
        );
//        findViewById(R.id.btnStartSecondActivityWithSerializable).setVisibility(View.GONE);
//        findViewById(R.id.btnStartSecondActivityWithSerializable).setOnClickListener(
//                (view) -> startSecondActivityWithSerializable()
//        );
    }

    private void startSecondActivityWithParcelable() {
        MyParcelableClass myParcelableClass = new MyParcelableClass();
        MySerializableClass serializableClass = new MySerializableClass();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();


        timeUtility.start();
        intent.putExtra(PARCELABLE_EXTRA, myParcelableClass);
        timeUtility.end();

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n");

        timeUtility.start();
        intent.putExtra(SERIALIZABLE_EXTRA, serializableClass);
        timeUtility.end();

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n");

        intent.putExtra("TIME", stringBuilder.toString());
        intent.putExtra("size", 0);
        startActivity(intent);
    }

    private void startSecondActivityWithArray() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        ArrayList<Parcelable> list1 = (ArrayList<Parcelable>) getParcelableArray();
        List<Serializable> list2 = getSerializableArray();

        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putParcelableArrayListExtra(PARCELABLE_EXTRA, list1);
        timeUtility.end();

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ms. Size: ")
                .append(list1.size())
                .append("\n");

        int index = 0;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, list2.get(index));
            index++;
        }
        timeUtility.end();

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ms. Size: ")
                .append(list2.size())
                .append("\n");

        intent.putExtra("TIME", stringBuilder.toString());
        startActivity(intent);
    }

    private List<Parcelable> getParcelableArray() {
        List<Parcelable> data = new ArrayList<>();
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MyParcelableClass());
        }
        return data;
    }

    private List<Serializable> getSerializableArray() {
        List<Serializable> data = new ArrayList<>();
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MySerializableClass());
        }
        return data;
    }

    private void startSecondActivityWithSerializable() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        MySerializableClass serializableClass = new MySerializableClass();
        Intent intent = new Intent(this, SecondActivity.class);
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            intent.putExtra(key, serializableClass);
        }
        startActivity(intent);
    }
}
