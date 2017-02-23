package com.fruitbasket.sensordatacollection.task;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.MainActivity;
import com.fruitbasket.sensordatacollection.sensor.GyrSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

public class GyrCollectionTask implements Runnable {

	private GyrSensor[] gyrSensorDatas;
	private int length=0;
	
	public GyrCollectionTask(GyrSensor[] gyrSensorDatas,int length){
		this.gyrSensorDatas=GyrSensor.objectArrayDeepCopyOf(gyrSensorDatas, length);
		this.length=length;
	}
	
	@Override
	public void run() {
		try {
			ExcelProcessor.appendDataQuickly(Condition.getGyrExcel(), gyrSensorDatas,length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
