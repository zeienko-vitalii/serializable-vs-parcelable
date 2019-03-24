package com.zeenko.serializablevsparcelable.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        textView = findViewById(R.id.tvResult);
        getData();
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = getIntent();
        StringBuilder stringBuilder = new StringBuilder();
        MySerializableClass class1 = null;
        MyParcelableClass class2 = null;
        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            class2 = intent.getParcelableExtra(key);
        }
        timeUtility.end();

        stringBuilder.append("Time Parcelable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n" +
                        "Data restored: ")
                .append(class2 != null ? class2.toString() : "restored is null")
                .append("\n");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            class1 = (MySerializableClass) intent.getSerializableExtra(key);
        }
        timeUtility.end();

        stringBuilder.append("Time Serializable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n" +
                        "Data restored: ")
                .append(class1 != null ? class1.toString() : "restored is null")
                .append("\n");

        stringBuilder.append("\n")
                .append(intent.getStringExtra("TIME"));

        textView.setText(stringBuilder.toString());
    }

}
