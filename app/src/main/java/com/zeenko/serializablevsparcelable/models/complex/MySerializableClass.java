package com.zeenko.serializablevsparcelable.models.complex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MySerializableClass implements Serializable {

    //    private static final long serialVersionUID = 2474319131781197735L;
    public String stringData;
    public int identity;
    public boolean isActive;
    public float dest;
    public List<String> stringList;

    public MySerializableClass() {
        stringData = UUID.randomUUID().toString();
        stringList = new ArrayList<>();
        Random rand = new Random();
        int maxI = rand.nextInt();
        for (int i = 0; i < maxI % 15; i++) {
            stringList.add(UUID.randomUUID().toString());
        }
        identity = rand.nextInt();
        isActive = rand.nextBoolean();
        dest = rand.nextFloat();
    }

    @Override
    public String toString() {
        return "MySerializableClass{" +
                "stringData='" + stringData + '\'' +
                ", identity=" + identity +
                ", isActive=" + isActive +
                ", dest=" + dest +
                ", stringList=" + stringList +
                '}';
    }
}
