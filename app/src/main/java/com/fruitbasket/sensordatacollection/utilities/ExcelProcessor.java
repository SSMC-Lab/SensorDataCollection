package com.fruitbasket.sensordatacollection.utilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.fruitbasket.sensordatacollection.sensor.AccSensor;
import com.fruitbasket.sensordatacollection.sensor.GyrSensor;
import com.fruitbasket.sensordatacollection.sensor.MagsSensor;
import com.fruitbasket.sensordatacollection.sensor.OrientationSensor;
import com.fruitbasket.sensordatacollection.sensor.PressureSensor;
import com.fruitbasket.sensordatacollection.sensor.RotationSensor;
import com.fruitbasket.sensordatacollection.sensor.TemperatureSensor;

public class ExcelProcessor {
	private static final ExcelProcessor mExcelProcesser=new ExcelProcessor();
	
	private ExcelProcessor(){}
	
	public ExcelProcessor getInstance(){
		return mExcelProcesser;
	}

	/**
	 * 向Excel表格追加多个数据行。参数必须要正确
	 * @param excelFile
	 * @param accSensorDatas
	 * @return
	 * @throws IOException
	 */
	public synchronized static boolean appendDataQuickly(File excelFile,AccSensor accSensorDatas[],int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(accSensorDatas[i].time+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].accels[0]+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].accels[1]+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].accels[2]+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}

	public synchronized static boolean appendDataQuickly(File excelFile, OrientationSensor accSensorDatas[], int length)
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(accSensorDatas[i].time+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].orientation[0]+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].orientation[1]+"	");
			randomAccessFile.writeBytes(accSensorDatas[i].orientation[2]+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}

	public synchronized static boolean appendDataQuickly(File excelFile,GyrSensor gyrSensorDatas[],int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(gyrSensorDatas[i].time+"	");
			randomAccessFile.writeBytes(gyrSensorDatas[i].gyr[0]+"	");
			randomAccessFile.writeBytes(gyrSensorDatas[i].gyr[1]+"	");
			randomAccessFile.writeBytes(gyrSensorDatas[i].gyr[2]+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}
	
	public synchronized static boolean appendDataQuickly(File excelFile,MagsSensor[] magsSensroDatas,int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(magsSensroDatas[i].time+"	");
			randomAccessFile.writeBytes(magsSensroDatas[i].mags[0]+"	");
			randomAccessFile.writeBytes(magsSensroDatas[i].mags[1]+"	");
			randomAccessFile.writeBytes(magsSensroDatas[i].mags[2]+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}
	
	public synchronized static boolean appendDataQuickly(File excelFile,PressureSensor[] PressureSensroDatas,int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(PressureSensroDatas[i].time+"	");
			randomAccessFile.writeBytes(PressureSensroDatas[i].pressure+"	");
			randomAccessFile.writeBytes(PressureSensroDatas[i].pressureAttitude+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}
	
	public synchronized static boolean appendDataQuickly(File excelFile,RotationSensor[] rotationSensorDatas,int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(rotationSensorDatas[i].time+"	");
			randomAccessFile.writeBytes(rotationSensorDatas[i].attitude[0]+"	");
			randomAccessFile.writeBytes(rotationSensorDatas[i].attitude[1]+"	");
			randomAccessFile.writeBytes(rotationSensorDatas[i].attitude[2]+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}
	
	public synchronized static boolean appendDataQuickly(File excelFile,TemperatureSensor[] temperatureSensroDatas,int length) 
			throws IOException{
		RandomAccessFile randomAccessFile=new RandomAccessFile(excelFile,"rwd");
		randomAccessFile.seek(excelFile.length());
		for(int i=0;i<length;++i){
			randomAccessFile.writeBytes(temperatureSensroDatas[i].time+"	");
			randomAccessFile.writeBytes(temperatureSensroDatas[i].temperature+"	");
			randomAccessFile.write('\n');
		}
		randomAccessFile.close();
		return true;
	}
	
	/**
	 * 如果文件不存在，创建带表头的Excel文件
	 * 非线程安全方法，仅用于创建文件
	 * @param excelFile
	 * @param header
	 * @return true:创建了文件；false: 没有创建文件
	 * @throws IOException
	 */
	public static boolean createFileWithHeader(File excelFile,String[] header) 
			throws IOException{
		if(excelFile!=null
				&&header!=null
				&&excelFile.exists()==false){
			excelFile.createNewFile();
			RandomAccessFile raf=new RandomAccessFile(excelFile,"rwd");
			raf.setLength(0);
			for(int list=0;list<header.length;++list){
				raf.writeBytes(header[list]+"	");
			}
			raf.write('\n');
			raf.close();
			return true;
		}
		else{
			return false;
		}
	}

}
