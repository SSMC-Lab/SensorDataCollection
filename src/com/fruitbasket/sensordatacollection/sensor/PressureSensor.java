package com.fruitbasket.sensordatacollection.sensor;

public class PressureSensor {
	public String time;
	public float pressure;
	public float pressureAttitude;
	
	public static PressureSensor[] objectArrayDeepCopyOf(PressureSensor[] pressureSensorDatas,int length){
		PressureSensor[] datas=new PressureSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new PressureSensor();
			datas[i].time=pressureSensorDatas[i].time;
			datas[i].pressure=pressureSensorDatas[i].pressure;
			datas[i].pressureAttitude=pressureSensorDatas[i].pressureAttitude;
		}
		return datas;
	}
}
