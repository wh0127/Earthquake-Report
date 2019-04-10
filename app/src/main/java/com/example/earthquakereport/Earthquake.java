package com.example.earthquakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    /**
     * 构造一个新 {@link Earthquake} 对象。
     *
     * @param magnitude 表示地震的震级（大小）
     * @param location 表示地震发生的位置
     * @param timeInMilliseconds 表示地震发生时以毫秒（根据 Epoch）计的时间
     */
    public Earthquake(double magnitude, String location, long timeInMilliseconds,String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }
    public String getLocation() {
        return mLocation;
    }
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
    public String getUrl() {
        return mUrl;
    }

}
