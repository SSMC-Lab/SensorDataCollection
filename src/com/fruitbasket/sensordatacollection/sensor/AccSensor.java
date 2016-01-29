package com.fruitbasket.sensordatacollection.sensor;

public class AccSensor {
	public String time;
	public float[] accels=new float[3];
	
	public static AccSensor[] objectArrayDeepCopyOf(AccSensor[] accSensorDatas,int length){
		AccSensor[] datas=new AccSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new AccSensor();
			datas[i].time=accSensorDatas[i].time;
			datas[i].accels[0]=accSensorDatas[i].accels[0];
			datas[i].accels[1]=accSensorDatas[i].accels[1];
			datas[i].accels[2]=accSensorDatas[i].accels[2];
		}
		return datas;
	}
}
