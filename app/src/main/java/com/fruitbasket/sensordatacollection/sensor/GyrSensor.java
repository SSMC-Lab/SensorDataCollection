package com.fruitbasket.sensordatacollection.sensor;

public class GyrSensor {
	public String time;
	public float[] gyr=new float[3];
	
	public static GyrSensor[] objectArrayDeepCopyOf(GyrSensor[] gyrSensorDatas,int length){
		GyrSensor[] datas=new GyrSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new GyrSensor();
			datas[i].time=gyrSensorDatas[i].time;
			datas[i].gyr[0]=gyrSensorDatas[i].gyr[0];
			datas[i].gyr[1]=gyrSensorDatas[i].gyr[1];
			datas[i].gyr[2]=gyrSensorDatas[i].gyr[2];
		}
		return datas;
	}
}
