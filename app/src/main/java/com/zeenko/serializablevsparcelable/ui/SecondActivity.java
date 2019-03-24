package com.zeenko.serializablevsparcelable.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        textView = findViewById(R.id.tvResult);
        Intent intent = getIntent();
        if (intent.getIntExtra("size", -1) != -1) {
            getData();
        } else {
            getDataArray();
        }
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = getIntent();
        StringBuilder stringBuilder = new StringBuilder();
        MySerializableClass class1;
        MyParcelableClass class2;
        timeUtility.start();
        class2 = intent.getParcelableExtra(FirstActivity.PARCELABLE_EXTRA);
        timeUtility.end();

        stringBuilder.append("Time Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. ")
                .append(class2 != null ? "restored" : "restored is null")
                .append("\n");

        timeUtility.start();
        class1 = (MySerializableClass) intent.getSerializableExtra(FirstActivity.SERIALIZABLE_EXTRA);
        timeUtility.end();

        stringBuilder.append("Time Serializable: ")
                .append(timeUtility.getResultInMs())
                .append(" ns\n" +
                        "Data restored: ")
                .append(class1 != null ? "restored" : "restored is null")
                .append("\n");

        stringBuilder.append("\n")
                .append(intent.getStringExtra("TIME"));

        textView.setText(stringBuilder.toString());
    }

    @SuppressLint("SetTextI18n")
    private void getDataArray() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = getIntent();
        StringBuilder stringBuilder = new StringBuilder();
        List<MySerializableClass> serializableClasses = new ArrayList<>();
        List<MyParcelableClass> parcelableClasses;

        timeUtility.start();
        parcelableClasses = intent.getParcelableArrayListExtra(FirstActivity.PARCELABLE_EXTRA);
        timeUtility.end();

        boolean hasNullable = false;
        for (Parcelable item : parcelableClasses) {
            if (item == null)
                hasNullable = true;
        }

        stringBuilder.append("Read Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ms\n")
                .append(!hasNullable ? "restored, size: " : "restored is null")
                .append(!hasNullable ? parcelableClasses.size() : "")
                .append("\n");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            serializableClasses.add((MySerializableClass) intent.getSerializableExtra(key));
        }
        timeUtility.end();

        hasNullable = false;
        for (MySerializableClass item : serializableClasses) {
            if (item == null)
                hasNullable = true;
        }
        stringBuilder.append("Read Serializable: ")
                .append(timeUtility.getResult())
                .append(" ms\n")
                .append(!hasNullable ? "restored, size: " : "restored is null")
                .append(!hasNullable ? serializableClasses.size() : "")
                .append("\n");

        stringBuilder.append("\n")
                .append(intent.getStringExtra("TIME"));

        textView.setText(stringBuilder.toString());
    }

}
