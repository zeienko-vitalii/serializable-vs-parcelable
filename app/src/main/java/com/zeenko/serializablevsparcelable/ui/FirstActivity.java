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
                    } else if (size == 1) {
                        startSecondActivityWithOneElement();
                    }
                }
        );
    }

    private void startSecondActivityWithOneElement() {
        Parcelable parcelableClass = new MyParcelableClass();
        Serializable serializableClass = new MySerializableClass();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        timeUtility.start();
        intent.putExtra(PARCELABLE_EXTRA, parcelableClass);
        timeUtility.end();

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns\n");

        timeUtility.start();
        intent.putExtra(SERIALIZABLE_EXTRA, serializableClass);
        timeUtility.end();

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns\n");

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
//        intent.putParcelableArrayListExtra(PARCELABLE_EXTRA, parcelables);
        timeUtility.end();

        stringBuilder.append("Write Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(parcelables.size())
                .append("\n");

        int index = 0;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, serializables.get(index));
            index++;
        }
        timeUtility.end();

        stringBuilder.append("Write Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. Size: ")
                .append(serializables.size())
                .append("\n");

        intent.putExtra(TIME_EXTRA, stringBuilder.toString());
        startActivity(intent);
    }

    private List<Parcelable> getParcelableArray() {
        List<Parcelable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE*2);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MyParcelableClass());
        }
        return data;
    }

    private List<Serializable> getSerializableArray() {
        List<Serializable> data = new ArrayList<>(ExtraKeysGeneratorUtility.SIZE*2);
        for (int i = 0; i < ExtraKeysGeneratorUtility.SIZE; i++) {
            data.add(new MySerializableClass());
        }
        return data;
    }
}
