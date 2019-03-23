package com.zeenko.serializablevsparcelable.utility;

import java.util.ArrayList;
import java.util.List;

public class ExtraKeysGeneratorUtility {
    public final int SIZE = 100_000;
    private List<String> parcelableKeys = new ArrayList<>();
    private List<String> serializableKeys = new ArrayList<>();

    public ExtraKeysGeneratorUtility() {
        for (int i = 0; i < SIZE; i++) {
            parcelableKeys.add("PARCELABLE_EXTRA_" + i);
            serializableKeys.add("SERIALIZABLE_EXTRA_" + i);
        }
    }

    public List<String> getParcelableKeys() {
        return parcelableKeys;
    }

    public List<String> getSerializableKeys() {
        return serializableKeys;
    }
}
