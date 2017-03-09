package com.fruitbasket.sensordatacollection.data;

public class PressureData extends Data {
    public float pressureAttitude;

    public PressureData(){}

    public PressureData(long timestamp,float[] values,float pressureAttitude){
        super(timestamp,values);
        this.pressureAttitude=pressureAttitude;
    }

}
