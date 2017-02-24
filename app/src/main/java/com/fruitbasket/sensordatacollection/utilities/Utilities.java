package com.fruitbasket.sensordatacollection.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.MainActivity;

import static com.fruitbasket.sensordatacollection.Condition.APP_FILE_DIR;

public class Utilities {
	private static final String TAG="utilities.Utilities";
	
	private static Utilities mUtilities=new Utilities();
	
	private Utilities(){}
	
	public static Utilities getInstance(){
		return mUtilities;
	}

	/**
	 *
	 */
	public static final boolean createDataFile(boolean[] chooseSensor){
		Log.i(TAG,"createDataFile()");
		if(chooseSensor==null || chooseSensor.length!= MainActivity.NUMBER_SENSOR){
			return false;
		}

		File appFileDir=new File(APP_FILE_DIR);
		if(appFileDir.exists()==false||appFileDir.isDirectory()==false){
			appFileDir.mkdirs();
		}

		int maxNumber=0;
		int tem;
		String[] names=appFileDir.list();
		if(names!=null){
			Log.d(TAG,"names!=null");
			for(String name:names){
				if(name.matches("[0-9]{1,}")) {
					Log.i(TAG,"name.matches(\"[0-9]{1,}\")==ture");
					tem = Integer.parseInt(name);
					if (maxNumber < tem) {
						maxNumber = tem;
					}
				}
				else{
					Log.i(TAG,"name.matches(\"[0-9]{1,}\")==false");
					continue;
				}
			}
		}
		else{
			Log.d(TAG,"names==null");
		}
		Log.i(TAG,"maxNumber=="+maxNumber);

		String subDir;
		subDir=appFileDir.getPath()+"/"+(maxNumber+1);
		(new File(subDir)).mkdir();

		try {
			String[] dataLine;

			if(chooseSensor[MainActivity.INDEX_PRESSURE]){
				Condition.setPreAltExcel(new File(subDir+"/"+ Condition.PRE_ALT_FILENAME));
				Log.i(TAG,"the path is : "+subDir+"/"+ Condition.PRE_ALT_FILENAME);
				if(Condition.getPreAltExcel().exists()==false){
					Log.i(TAG,"Condition.getPreAltExcel().exists()==false");
					dataLine=new String[]{"Time","Pressure","Altitude"};
					ExcelProcessor.createFileWithHeader(Condition.getPreAltExcel(),dataLine);
				}
			}

			if(chooseSensor[MainActivity.INDEX_TEMPERATURE]){
				Condition.setTemperatureExcel(new File(subDir+"/"+Condition.TEMPERATURE_FILENAME));
				if(Condition.getTemperatureExcel().exists()==false){
					Log.i(TAG,"Condition.getTemperatureExcel().exists()==false");
					dataLine=new String[]{"Time","Temperature"};
					ExcelProcessor.createFileWithHeader(Condition.getTemperatureExcel(),dataLine);
				}
			}

			if(chooseSensor[MainActivity.INDEX_ROTATION]){
				Condition.setRotationExcel(new File(subDir+"/"+Condition.ROTATION_FILENAME));
				if(Condition.getRotationExcel().exists()==false){
					Log.i(TAG,"Condition.getRotationExcel().exists()==false");
					dataLine=new String[]{"Time","Pitch(x)","Roll(y)","Azimuth(z)"};
					ExcelProcessor.createFileWithHeader(Condition.getRotationExcel(),dataLine);
				}
			}

			if(chooseSensor[MainActivity.INDEX_ACC]){
				Condition.setAccExcel(new File(subDir+"/"+Condition.ACC_FILENAME));
				if(Condition.getAccExcel().exists()==false){
					Log.i(TAG,"Condition.getAccExcel().exists()==false");
					dataLine=new String[]{"Time","accX","accY","accZ"};
					ExcelProcessor.createFileWithHeader(Condition.getAccExcel(),dataLine);
				}
			}

			if(chooseSensor[MainActivity.INDEX_GYR]){
				Condition.setGyrExcel(new File(subDir+"/"+Condition.GYR_FILENAME));
				if(Condition.getGyrExcel().exists()==false){
					Log.i(TAG,"Condition.getGyrExcel().exists()==false");
					dataLine=new String[]{"Time","gyrX","gyrY","gyrZ"};
					ExcelProcessor.createFileWithHeader(Condition.getGyrExcel(),dataLine);
				}
			}

			if(chooseSensor[MainActivity.INDEX_MAG]){
				Condition.setMagsExcel(new File(subDir+"/"+Condition.MAGS_FILENAME));
				if(Condition.getMagsExcel().exists()==false){
					Log.i(TAG,"Condition.getMagsExcel().exists()==false");
					dataLine=new String[]{"Time","magX","magY","magZ"};
					ExcelProcessor.createFileWithHeader(Condition.getMagsExcel(),dataLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 *
	 * @return
     */
	public static String getTime(){
		return new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
	}

}
