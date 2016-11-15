package com.fruitbasket.sensordatacollection.sensor;

/**
 * Created by zzbpc on 2016/11/12.
 */

public class OrientationSensor {
    public String time;
    public float[] orientation=new float[3];

    public static OrientationSensor[] objectArrayDeepCopyOf(OrientationSensor[] OrientationSensorDatas,int length){
        OrientationSensor[] datas=new OrientationSensor[length];
        for(int i=0;i<length;++i){
            datas[i]=new OrientationSensor();
            datas[i].time=OrientationSensorDatas[i].time;
            datas[i].orientation[0]=OrientationSensorDatas[i].orientation[0];
            datas[i].orientation[1]=OrientationSensorDatas[i].orientation[1];
            datas[i].orientation[2]=OrientationSensorDatas[i].orientation[2];
        }
        return datas;
    }
}
