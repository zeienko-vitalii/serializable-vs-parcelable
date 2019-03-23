package com.zeenko.serializablevsparcelable.utility;

public class TimeUtility {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void end() {
        endTime = System.nanoTime();
    }

    public long getResult() {
        return endTime - startTime;
    }

    public long getResultInMs() {
        return getResult() / 1_000_000;
    }
}
