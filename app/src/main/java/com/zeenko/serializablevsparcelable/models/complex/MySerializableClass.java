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

//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.writeUTF(stringData);
//        out.writeInt(identity);
//        out.writeBoolean(isActive);
//        out.writeFloat(dest);
//        if (stringList != null) {
//            out.writeInt(stringList.size());
//            for (String child : stringList) {
//                out.writeUTF(child);
//            }
//        } else {
//            out.writeInt(0);
//        }
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        stringData = in.readUTF();
//        identity = in.readInt();
//        isActive = in.readBoolean();
//        dest = in.readFloat();
//        int childCount = in.readInt();
//        if (childCount > 0) {
//            stringList = new ArrayList<>(childCount);
//            for (int i = 0; i < childCount; i++) {
//                stringList.add(in.readUTF());
//            }
//        }
//    }

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
