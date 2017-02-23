package com.fruitbasket.sensordatacollection.task;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.MainActivity;
import com.fruitbasket.sensordatacollection.sensor.RotationSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

public class RotationCollectionTask implements Runnable {

	private RotationSensor[] rotationSensorDatas;
	private int length=0;
	
	public RotationCollectionTask(RotationSensor[] rotationSensorDatas,int length){
		this.rotationSensorDatas=RotationSensor.objectArrayDeepCopyOf(rotationSensorDatas, length);
		this.length=length;
	}
	
	@Override
	public void run() {
		try {
			ExcelProcessor.appendDataQuickly(Condition.getRotationExcel(), rotationSensorDatas,length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
