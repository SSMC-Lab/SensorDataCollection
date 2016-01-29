package com.fruitbasket.sensordatacollection.task;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.sensor.PressureSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

public class PressureCollectionTask implements Runnable {

	private PressureSensor[] pressureSensorDatas;
	private int length=0;//数组中有效数据的长度
	
	public PressureCollectionTask(PressureSensor[] pressureSensorDatas,int length){
		this.pressureSensorDatas=PressureSensor.objectArrayDeepCopyOf(pressureSensorDatas, length);
		this.length=length;
	}
	
	@Override
	public void run() {
		try {
			ExcelProcessor.appendDataQuickly(Condition.PRE_ALT_EXCEL, pressureSensorDatas,length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
