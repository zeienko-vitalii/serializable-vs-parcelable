package com.zeenko.serializablevsparcelable.models.complex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MySerializableClass implements Serializable {

    private static final long serialVersionUID = 2474319131781197735L;
    private String stringData;
    private int identity;
    private boolean isActive;
    private float dest;
    private List<String> stringList;

    public MySerializableClass() {
        stringData = UUID.randomUUID().toString();
        stringList = new ArrayList<>(32);
        Random rand = new Random();
        for (int i = 0; i < 32; i++) {
            stringList.add("duck");
        }
        identity = rand.nextInt();
        isActive = rand.nextBoolean();
        dest = rand.nextFloat();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(stringData);
        out.writeInt(identity);
        out.writeBoolean(isActive);
        out.writeFloat(dest);
        if (stringList != null) {
            out.writeInt(stringList.size());
            for (String child : stringList) {
                out.writeUTF(child);
            }
        } else {
            out.writeInt(0);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        stringData = in.readUTF();
        identity = in.readInt();
        isActive = in.readBoolean();
        dest = in.readFloat();
        int childCount = in.readInt();
        if (childCount > 0) {
            stringList = new ArrayList<>(childCount);
            for (int i = 0; i < childCount; i++) {
                stringList.add(in.readUTF());
            }
        }
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
