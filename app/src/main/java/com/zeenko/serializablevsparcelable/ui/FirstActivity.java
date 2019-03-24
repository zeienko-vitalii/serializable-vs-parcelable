package com.zeenko.serializablevsparcelable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.complex.MyParcelableClass;
import com.zeenko.serializablevsparcelable.models.complex.MySerializableClass;
import com.zeenko.serializablevsparcelable.utility.ExtraKeysGeneratorUtility;
import com.zeenko.serializablevsparcelable.utility.TimeUtility;

public class FirstActivity extends AppCompatActivity {
    private final int SIZE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        findViewById(R.id.btnStartSecondActivityWithParcelable).setOnClickListener(
                (view) -> startSecondActivityWithParcelable()
        );
        findViewById(R.id.btnStartSecondActivityWithSerializable).setVisibility(View.GONE);
//        findViewById(R.id.btnStartSecondActivityWithSerializable).setOnClickListener(
//                (view) -> startSecondActivityWithSerializable()
//        );
    }

    private void startSecondActivityWithParcelable() {
        ExtraKeysGeneratorUtility extraKeysGeneratorUtility = new ExtraKeysGeneratorUtility();
        MyParcelableClass myParcelableClass = new MyParcelableClass();
        MySerializableClass serializableClass = new MySerializableClass();
        TimeUtility timeUtility = new TimeUtility();
        Intent intent = new Intent(this, SecondActivity.class);
        StringBuilder stringBuilder = new StringBuilder();


        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getParcelableKeys()) {
            intent.putExtra(key, myParcelableClass);
        }

        timeUtility.end();

        stringBuilder.append("Time to put Parcelable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n")
                .append("Data: ")
                .append(myParcelableClass.toString())
                .append("\n");

        timeUtility.start();
        for (String key : extraKeysGeneratorUtility.getSerializableKeys()) {
            intent.putExtra(key, serializableClass);
        }
        timeUtility.end();

        stringBuilder.append("Time to put Serializable: ")
                .append(timeUtility.getResultInMs())
                .append(" ms\n")
                .append("Data: ")
                .append(serializableClass)
                .append("\n");

        intent.putExtra("TIME", stringBuilder.toString());
        startActivity(intent);
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
