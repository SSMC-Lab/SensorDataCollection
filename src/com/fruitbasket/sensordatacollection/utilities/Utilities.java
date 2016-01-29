package com.fruitbasket.sensordatacollection.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

public class Utilities {
	
	private static Utilities mUtilities=new Utilities();
	
	private Utilities(){}
	
	public static Utilities getInstance(){
		return mUtilities;
	}
	
	/**
	 * 创建文件夹。文件夹所在的路径必须原先就存在
	 * @param dir
	 */
	public static void createDir(String dir){
		File appDir=new File(dir);
		if(appDir.exists()==false){
			appDir.mkdir();
		}
	}
	
	/**
	 * 创建文件夹
	 * @param dir
	 */
	public static void createDirs(String dir){
		File appDir=new File(dir);
		if(appDir.exists()==false){
			appDir.mkdirs();
		}
	}
	
	/**
	 * 创建文件。文件所在的路径必须原先就存在
	 * @param filePath
	 * @return true：执行创建；false：没执行创建，文件原先已经存在
	 * @throws IOException
	 */
	public static boolean createFile(String filePath) 
			throws IOException{
		File file=new File(filePath);
		if(file.exists()==false){
			file.createNewFile();
			return true;
		}
		else{
			return false;
		}
	}

	
	public static String getTime(){
		return new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
	}
	
	
	/**
	 * @param context
	 * @param className ///
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
