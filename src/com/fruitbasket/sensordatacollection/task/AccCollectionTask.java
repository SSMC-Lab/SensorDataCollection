package com.fruitbasket.sensordatacollection.task;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.sensor.AccSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

public class AccCollectionTask implements Runnable {

	private AccSensor[] accSensorDatas;
	private int length=0;//数组中有效数据的长度
	
	public AccCollectionTask(AccSensor[] datas,int length){
		this.accSensorDatas=AccSensor.objectArrayDeepCopyOf(datas, length);
		this.length=length;
	}
	
	@Override
	public void run() {
		try {
			ExcelProcessor.appendDataQuickly(Condition.ACC_EXCEL, accSensorDatas,length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
