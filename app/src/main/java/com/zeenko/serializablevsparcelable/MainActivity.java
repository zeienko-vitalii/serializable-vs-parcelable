package com.zeenko.serializablevsparcelable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.models.MySerializedClass;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String PARCELABLE_ARRAY = "parcelable_array";

    public ArrayList<MySerializedClass> items;
//	public MyParcelClass[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date time = new Date();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Serializable

            items = (ArrayList<MySerializedClass>) extras.getSerializable(PARCELABLE_ARRAY);

            //Parcelable
//			Parcelable[] parcelableArray = extras.getParcelableArray(PARCELABLE_ARRAY);
//			if (parcelableArray != null) {
//				items = new MyParcelClass[parcelableArray.length];
//			    for (int i = 0; i < parcelableArray.length; ++i) {
//			    	items[i] = (MyParcelClass) parcelableArray[i];
//			    }
//			}
        }
        Date now = new Date();
        Log.w("MainActivity", "Create time: " + (now.getTime() - time.getTime()) + " ms.");

        TextView res = findViewById(R.id.resultTimer);
        res.setText(getString(R.string.result_time, (now.getTime() - time.getTime()) * 1.0f / 1000));
    }

    public void startTest(View v) {
        EditText elementsCounter = findViewById(R.id.elementsCounter);
        String text = elementsCounter.getText().toString();
        if (text.length() == 0) return;
        int elementsToCreate = Integer.parseInt(text);
        if (elementsToCreate < 1) return;

        items = new ArrayList<>();
        for (int i = 0; i < elementsToCreate; i++) {
            items.add(new MySerializedClass());
        }

//		items = new MyParcelClass[elementsToCreate];
//		for (int i = 0; i < elementsToCreate; i++)
//		{
//			items[i] = new MyParcelClass();
//		}

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra(PARCELABLE_ARRAY, items);
        startActivity(i);

        finish();
    }
}
