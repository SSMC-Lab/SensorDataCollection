package com.fruitbasket.sensordatacollection.sensor;

public class MagsSensor {
	public String time;
	public float[] mags=new float[3];
	
	public static MagsSensor[] objectArrayDeepCopyOf(MagsSensor[] magsSensorDatas,int length){
		MagsSensor[] datas=new MagsSensor[length];
		for(int i=0;i<length;++i){
			datas[i]=new MagsSensor();
			datas[i].time=magsSensorDatas[i].time;
			datas[i].mags[0]=magsSensorDatas[i].mags[0];
			datas[i].mags[1]=magsSensorDatas[i].mags[1];
			datas[i].mags[2]=magsSensorDatas[i].mags[2];
		}
		return datas;
	}
}
