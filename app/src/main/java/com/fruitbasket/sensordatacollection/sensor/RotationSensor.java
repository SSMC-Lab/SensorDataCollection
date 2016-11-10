package com.fruitbasket.sensordatacollection.sensor;

public class RotationSensor {
	public String time;
	public float[] attitude=new float[3];
	
	public static RotationSensor[] objectArrayDeepCopyOf(RotationSensor[] rotationSensorDatas,int length){
		RotationSensor[] datas=new RotationSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new RotationSensor();
			datas[i].time=rotationSensorDatas[i].time;
			datas[i].attitude[0]=rotationSensorDatas[i].attitude[0];
			datas[i].attitude[1]=rotationSensorDatas[i].attitude[1];
			datas[i].attitude[2]=rotationSensorDatas[i].attitude[2];
		}
		return datas;
	}
}
