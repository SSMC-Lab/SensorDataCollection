package com.fruitbasket.sensordatacollection;

import java.io.IOException;

import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;
import com.fruitbasket.sensordatacollection.utilities.Utilities;

import android.app.Application;
import android.util.Log;

/**
 * you can initialize the application in this class
 * @author zcs
 *
 */
public class MyApp extends Application {
	
	private static final String TAG="MyAPP";
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(TAG,"onCreate()");
		//Toast.makeText(this, TAG, Toast.LENGTH_SHORT).show();
		Utilities.createDirs(Condition.DATA_DIR);
		
		String [] dataLine;
		try {
			if(Condition.PRE_ALT_EXCEL.exists()==false){
				dataLine=new String[]{"Time","Pressure","Altitude"};
				ExcelProcessor.createFileWithHeader(Condition.PRE_ALT_EXCEL,dataLine);
			}
			if(Condition.TEMPERATURE_EXCEL.exists()==false){
				dataLine=new String[]{"Time","Temperature"};
				ExcelProcessor.createFileWithHeader(Condition.TEMPERATURE_EXCEL,dataLine);
			}
			if(Condition.ROTATION_EXCEL.exists()==false){
				dataLine=new String[]{"Time","Pitch(x)","Roll(y)","Azimuth(z)"};
				ExcelProcessor.createFileWithHeader(Condition.ROTATION_EXCEL,dataLine);
			}
			if(Condition.ACC_EXCEL.exists()==false){
				dataLine=new String[]{"Time","accX","accY","accZ"};
				ExcelProcessor.createFileWithHeader(Condition.ACC_EXCEL,dataLine);
			}
			if(Condition.GYR_EXCEL.exists()==false){
				dataLine=new String[]{"Time","gyrX","gyrY","gyrZ"};
				ExcelProcessor.createFileWithHeader(Condition.GYR_EXCEL,dataLine);
			}
			if(Condition.MAGS_EXCEL.exists()==false){
				dataLine=new String[]{"Time","magX","magY","magZ"};
				ExcelProcessor.createFileWithHeader(Condition.MAGS_EXCEL,dataLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
