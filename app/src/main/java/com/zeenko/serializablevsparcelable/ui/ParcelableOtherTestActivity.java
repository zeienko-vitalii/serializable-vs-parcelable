package com.zeenko.serializablevsparcelable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;
import com.zeenko.serializablevsparcelable.models.parcelable_test.Package;
import com.zeenko.serializablevsparcelable.ui.adapter.EmptyObjectAdapter;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ParcelableOtherTestActivity extends Activity {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_parcelable_test_activity);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Package aPackage;

        if (ParcelableTestActivity.USE_SERIALIZATION) {
            aPackage = (Package) getIntent().getSerializableExtra("package");
        } else {
            aPackage = Parcels.unwrap(getIntent().getParcelableExtra("package"));
        }

        long endTimestamp = System.nanoTime();

        String message = "Elapsed time in nanoseconds: %s";
        ((TextView) findViewById(R.id.elapsed_time_message)).setText(String.format(message, NUMBER_FORMAT.format((endTimestamp - ParcelableTestActivity.START_TIMESTAMP)/1_000_000)));
        recyclerView.setAdapter(new EmptyObjectAdapter(aPackage.getList()));
    }

}