package com.fruitbasket.sensordatacollection.data;

/**
 * 传感器数据
 */

public class Data {
    public long timestamp;
    public float[] values;

    public Data(){}

    public Data(long timestamp,float[] values){
        this.timestamp=timestamp;
        this.values=values;
    }
}
