package com.fruitbasket.sensordatacollection.sensor;

public class TemperatureSensor {
	public String time;
	public float temperature;
	
	public static TemperatureSensor[] objectArrayDeepCopyOf(TemperatureSensor[] temperatureSensorDatas,int length){
		TemperatureSensor[] datas=new TemperatureSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new TemperatureSensor();
			datas[i].time=temperatureSensorDatas[i].time;
			datas[i].temperature=temperatureSensorDatas[i].temperature;
		}
		return datas;
	}
}
