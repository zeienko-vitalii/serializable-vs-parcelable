package com.zeenko.serializablevsparcelable.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.getFormattedBytesToString;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaParceler;
import static com.zeenko.serializablevsparcelable.utility.MemoryUtility.marshallToByteArrayViaSerialization;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        textView = findViewById(R.id.tvResult);
        Intent intent = getIntent();
        if (intent.getBooleanExtra(FirstActivity.IS_ONE_EXTRA, false)) {
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
        Serializable serializable;
        Parcelable parcelable;

        timeUtility.start();
        parcelable = intent.getParcelableExtra(FirstActivity.PARCELABLE_EXTRA);
        timeUtility.end();

        byte[] parcelizedSize = marshallToByteArrayViaParceler(parcelable);

        stringBuilder.append("Read Parcelable:  ")
                .append(timeUtility.getResult())
                .append(" ns. ")
                .append(parcelable != null ? "restored" : "restored is null")
                .append(" Bytes: ")
                .append(parcelizedSize != null ? getFormattedBytesToString(parcelizedSize) : " exception")
                .append("\n");

        timeUtility.start();
        serializable = intent.getSerializableExtra(FirstActivity.SERIALIZABLE_EXTRA);
        timeUtility.end();

        byte[] serialiazedSize = marshallToByteArrayViaSerialization(serializable);

        stringBuilder.append("Read Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. ")
                .append(serializable != null ? "restored" : "restored is null")
                .append(" Bytes: ")
                .append(serialiazedSize != null ? getFormattedBytesToString(serialiazedSize) : " exception")
                .append("\n");

        stringBuilder.insert(0, intent.getStringExtra(FirstActivity.TIME_EXTRA));

        textView.setText(stringBuilder.toString());
    }

    @SuppressLint("SetTextI18n")
    private void getDataArray() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = getIntent();
        StringBuilder stringBuilder = new StringBuilder();
        List<Serializable> serializables = new ArrayList<>();
        List<Parcelable> parcelables;

        timeUtility.start();
        parcelables = intent.getParcelableArrayListExtra(FirstActivity.PARCELABLE_EXTRA);
        timeUtility.end();

        byte[] parcelizedSize = marshallToByteArrayViaParceler(parcelables);

        boolean hasNullable = false;
        for (Parcelable item : parcelables) {
            if (item == null)
                hasNullable = true;
        }

        stringBuilder.append("Read Parcelable: ")
                .append(timeUtility.getResult())
                .append(" ns. ")
                .append(!hasNullable ? "Size: " : "restored is null")
                .append(!hasNullable ? parcelables.size() : "")
                .append(" Bytes: ")
                .append(parcelizedSize != null ? getFormattedBytesToString(parcelizedSize) : " exception")
                .append("\n");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            serializables.add(intent.getSerializableExtra(key));
        }
        timeUtility.end();

        hasNullable = false;
        for (Serializable item : serializables) {
            if (item == null)
                hasNullable = true;
        }

        byte[] serialiazedSize = marshallToByteArrayViaSerialization(serializables);

        stringBuilder.append("Read Serializable: ")
                .append(timeUtility.getResult())
                .append(" ns. ")
                .append(!hasNullable ? "Size: " : "restored is null")
                .append(!hasNullable ? serializables.size() : "")
                .append(" Bytes: ")
                .append(serialiazedSize != null ? getFormattedBytesToString(serialiazedSize) : " exception")
                .append("\n");

//        stringBuilder.append("\n")
//                .append(intent.getStringExtra(FirstActivity.TIME_EXTRA));
        stringBuilder.insert(0, intent.getStringExtra(FirstActivity.TIME_EXTRA));
        textView.setText(stringBuilder.toString());
    }

}
