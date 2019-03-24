package com.zeenko.serializablevsparcelable.models.parcelable_test;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel
public class Package implements Serializable {

    private static final long serialVersionUID = -4912597374403686616L;
    ArrayList<EmptyObject> mList;

    public Package(){

    }

    public Package(ArrayList<EmptyObject> list){
        this.mList = list;
    }

    public ArrayList<EmptyObject> getList() {
        return mList;
    }
}