package com.fruitbasket.sensordatacollection;

import java.io.File;

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

		File appDir=new File(Condition.APP_FILE_DIR);
		if(appDir.exists()==false){
			appDir.mkdirs();
		}
	}
}
