package com.zeenko.serializablevsparcelable.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MySerializedClass implements Serializable {

    private static final long serialVersionUID = 2474319131781197735L;
    public String stringData = "";
    public int identy = 0;
    public boolean isActive = false;
    public float dest = 0.0f;
    public ArrayList<String> stringList;

    public MySerializedClass() {

        stringData = UUID.randomUUID().toString();

        stringList = new ArrayList<>();
        Random rand = new Random();
        int maxI = rand.nextInt();

        for (int i = 0; i < maxI % 15; i++) {
            stringList.add(UUID.randomUUID().toString());
        }

        identy = rand.nextInt();
        isActive = rand.nextBoolean();
        dest = rand.nextFloat();
    }
}
