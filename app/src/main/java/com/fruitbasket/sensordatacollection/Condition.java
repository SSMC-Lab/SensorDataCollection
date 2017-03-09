package com.fruitbasket.sensordatacollection;

;import android.os.Environment;

public final class Condition {
	private static final Condition mCondition=new Condition();
	
	public static final String APP_FILE_DIR= Environment.getExternalStorageDirectory()+"/SensorData";

	public static final String PRE_ALT_FILENAME="pressure&Altitude.xls";
	public static final String TEMPERATURE_FILENAME="temperature.xls";
	public static final String ROTATION_FILENAME="rotation.xls";
	public static final String ACC_FILENAME="acc.xls";
	public static final String GYR_FILENAME="gyr.xls";
	public static final String MAGS_FILENAME="mags.xls";

	//Handler消息
	public static final int BEGIN_SAVE_DATA=0x1001;
	public static final int DATA_SAVED=0x1002;
	
	private Condition(){}
	
	public static final Condition getInstance(){
		return mCondition;
	}
}
