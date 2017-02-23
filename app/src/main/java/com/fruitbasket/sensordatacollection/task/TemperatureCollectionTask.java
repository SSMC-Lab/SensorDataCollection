package com.fruitbasket.sensordatacollection.task;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.MainActivity;
import com.fruitbasket.sensordatacollection.sensor.TemperatureSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

public class TemperatureCollectionTask implements Runnable {

	private TemperatureSensor[] temperatureSensorDatas;
	private int length=0;
	
	public TemperatureCollectionTask(TemperatureSensor[] temperatureSensorDatas,int length){
		this.temperatureSensorDatas=TemperatureSensor.objectArrayDeepCopyOf(temperatureSensorDatas, length);
		this.length=length;
	}
	
	@Override
	public void run() {
		try {
			ExcelProcessor.appendDataQuickly(Condition.getTemperatureExcel(), temperatureSensorDatas,length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
