package com.zeenko.serializablevsparcelable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zeenko.serializablevsparcelable.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnOldTest).setOnClickListener((v) -> startTestActivity(OldTestActivity.class));
        findViewById(R.id.btnPerformanceMemoryTest).setOnClickListener((v) -> startTestActivity(PerformanceMemoryTestActivity.class));
        findViewById(R.id.btnTransferTest).setOnClickListener((v) -> startTestActivity(FirstActivity.class));
        findViewById(R.id.btnParcelableTestActivity).setOnClickListener((v) -> startTestActivity(ParcelableTestActivity.class));
    }

    private void startTestActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
