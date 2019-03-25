package com.zeenko.serializablevsparcelable.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

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
        findViewById(R.id.btnDialog).setOnClickListener((v) -> startDialog());
    }

    private void startTestActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void startDialog() {
        Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.tutorial);
        dialog.findViewById(R.id.dialogView).setClickable(false);
        dialog.findViewById(R.id.btn).setOnClickListener((v) -> dialog.dismiss());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
