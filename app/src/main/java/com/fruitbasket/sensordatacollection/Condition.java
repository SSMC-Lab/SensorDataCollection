package com.fruitbasket.sensordatacollection;

import java.io.File;
import java.util.Calendar;

import android.os.Environment;
import android.provider.MediaStore;

public final class Condition {
	
	private static final Condition mCondition=new Condition();
	
	public static final String APP_FILE_DIR=Environment.getExternalStorageDirectory()+"/SensorData";

	public static final String PRE_ALT_FILENAME="pressure&Altitude.xls";
	public static final String TEMPERATURE_FILENAME="emperature.xls";
	public static final String ROTATION_FILENAME="rotation.xls";
	public static final String ACC_FILENAME="acc.xls";
	public static final String GYR_FILENAME="gyr.xls";
	public static final String MAGS_FILENAME="mags.xls";

	private static File preAltExcel;
	private static File temperatureExcel;
	private static File rotationExcel;
	private static File accExcel;
	private static File gyrExcel;
	private static File magsExcel;

	 
	public static final int FAST_FLUSH_INTERVAL=100;//传感器每采集FAST_FLUSH_INTERVAL次数据，就将数据输出到文件
	public static final int MID_FLUSH_INTERVAL=50;
	public static final int SLOW_FLUSH_INTERVAL=5;

	//Handler消息
	public static final int BEGIN_SAVE_DATA=0x1001;
	public static final int DATA_SAVED=0x1002;
	
	private Condition(){}
	
	public static final Condition getInstance(){
		return mCondition;
	}

	public static File getPreAltExcel() {
		return preAltExcel;
	}

	public static File getRotationExcel() {
		return rotationExcel;
	}

	public static File getTemperatureExcel() {
		return temperatureExcel;
	}

	public static File getAccExcel() {
		return accExcel;
	}

	public static File getGyrExcel() {
		return gyrExcel;
	}

	public static File getMagsExcel() {
		return magsExcel;
	}

	public static void setPreAltExcel(File preAltExcel) {
		Condition.preAltExcel = preAltExcel;
	}

	public static void setTemperatureExcel(File temperatureExcel) {
		Condition.temperatureExcel = temperatureExcel;
	}

	public static void setRotationExcel(File rotationExcel) {
		Condition.rotationExcel = rotationExcel;
	}

	public static void setAccExcel(File accExcel) {
		Condition.accExcel = accExcel;
	}

	public static void setGyrExcel(File gyrExcel) {
		Condition.gyrExcel = gyrExcel;
	}

	public static void setMagsExcel(File magsExcel) {
		Condition.magsExcel = magsExcel;
	}
}
