package com.fruitbasket.sensordatacollection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruitbasket.sensordatacollection.sensor.AccSensor;
import com.fruitbasket.sensordatacollection.sensor.GyrSensor;
import com.fruitbasket.sensordatacollection.sensor.MagsSensor;
import com.fruitbasket.sensordatacollection.sensor.PressureSensor;
import com.fruitbasket.sensordatacollection.sensor.RotationSensor;
import com.fruitbasket.sensordatacollection.sensor.TemperatureSensor;
import com.fruitbasket.sensordatacollection.task.*;
import com.fruitbasket.sensordatacollection.utilities.Utilities;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class DataCollectionService extends Service {

	private static final String TAG="DataCollectionService";
	
	private SensorManager mSensorManager;
	private SensorEventListener mySensorListener;
	private ExecutorService accExecutor;
	private ExecutorService gyrExecutor;
	private ExecutorService magsExecutor;
	private ExecutorService pressureExecutor;
	private ExecutorService rotationExecutor;
	private ExecutorService temperatureExecutor;
	
	private AccSensor[] accSensorDatas;
	private GyrSensor[] gyrSensorDatas;
	private MagsSensor[] magsSensorDatas;
	private PressureSensor[] pressureSensorDatas;
	private RotationSensor[] rotationSensorDatas;
	private TemperatureSensor[] temperatureSensorDatas;
	
	private int accLength=0;
	private int gyrLength=0;
	private int magsLength=0;
	private int pressureLength=0;
	private int rotationLength=0;
	private int temperatureLength=0;
	
	//aid data
	private float[] rotationMatrix=new float[16];
	private float[] accels=new float[3];
	private float[] mags=new float[3];
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(TAG,"onCreate()");
		mSensorManager= (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
		mySensorListener = new mySensorListener();
		
		int i;
		accSensorDatas=new AccSensor[Condition.FAST_FLUSH_INTERVAL];
		for(i=0;i<accSensorDatas.length;++i){
			accSensorDatas[i]=new AccSensor();
		}
		gyrSensorDatas=new GyrSensor[Condition.FAST_FLUSH_INTERVAL];
		for(i=0;i<gyrSensorDatas.length;++i){
			gyrSensorDatas[i]=new GyrSensor();
		}
		magsSensorDatas=new MagsSensor[Condition.MID_FLUSH_INTERVAL];
		for(i=0;i<magsSensorDatas.length;++i){
			magsSensorDatas[i]=new MagsSensor();
		}
		pressureSensorDatas=new PressureSensor[Condition.MID_FLUSH_INTERVAL];
		for(i=0;i<pressureSensorDatas.length;++i){
			pressureSensorDatas[i]=new PressureSensor();
		}
		rotationSensorDatas=new RotationSensor[Condition.MID_FLUSH_INTERVAL];
		for(i=0;i<rotationSensorDatas.length;++i){
			rotationSensorDatas[i]=new RotationSensor();
		}
		temperatureSensorDatas=new TemperatureSensor[Condition.SLOW_FLUSH_INTERVAL];
		for(i=0;i<temperatureSensorDatas.length;++i){
			temperatureSensorDatas[i]=new TemperatureSensor();
		}
		accExecutor=Executors.newSingleThreadExecutor();
		gyrExecutor=Executors.newSingleThreadExecutor();
		magsExecutor=Executors.newSingleThreadExecutor();
		pressureExecutor=Executors.newSingleThreadExecutor();
		rotationExecutor=Executors.newSingleThreadExecutor();
		temperatureExecutor=Executors.newSingleThreadExecutor();
		registerListeners();
	}
	
	@Override
	public void onDestroy(){
		unregisterListeners();
		
		accExecutor.execute(new AccCollectionTask(accSensorDatas,accLength));
		accLength=0;
		
		gyrExecutor.execute(new GyrCollectionTask(gyrSensorDatas,gyrLength));
		gyrLength=0;
		
		magsExecutor.execute(new MagsCollectionTask(magsSensorDatas,magsLength));
		magsLength=0;
		
		pressureExecutor.execute(new PressureCollectionTask(pressureSensorDatas,pressureLength));
		pressureLength=0;
		
		rotationExecutor.execute(new RotationCollectionTask(rotationSensorDatas,rotationLength));
		rotationLength=0;
		
		temperatureExecutor.execute(new TemperatureCollectionTask(temperatureSensorDatas,temperatureLength));
		temperatureLength=0;
		
		accExecutor.shutdown();
		gyrExecutor.shutdown();
	    magsExecutor.shutdown();
	    pressureExecutor.shutdown();
	    rotationExecutor.shutdown();
	    temperatureExecutor.shutdown();
		
		Log.d(TAG,"onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG,"onBind()");
		return new MyBinder();
	}
	
	private void registerListeners() {
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mySensorListener, mSensorManager
				.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void unregisterListeners(){
		mSensorManager.unregisterListener(mySensorListener);
	}
	
	
	
	
	
	class MyBinder extends Binder{
		DataCollectionService getService(){
			return DataCollectionService.this;
		}
	}

	class mySensorListener implements SensorEventListener{

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch(event.sensor.getType()){
			case Sensor.TYPE_LINEAR_ACCELERATION:///
				//Log.d(TAG,"linear acceleration changed");
				accSensorDatas[accLength].time=Utilities.getTime();
				accSensorDatas[accLength].accels[0]=event.values[0];
				accSensorDatas[accLength].accels[1]=event.values[1];
				accSensorDatas[accLength].accels[2]=event.values[2];
				accels=event.values;
				++accLength;
				if(accLength>=accSensorDatas.length){
					accExecutor.execute(new AccCollectionTask(accSensorDatas,accSensorDatas.length));
					accLength=0;
				}
				break;
			case Sensor.TYPE_GYROSCOPE:
				gyrSensorDatas[gyrLength].time=Utilities.getTime();
				//gyrSensorDatas[gyrLength].gyr=event.values;
				gyrSensorDatas[gyrLength].gyr[0]=event.values[0];
				gyrSensorDatas[gyrLength].gyr[1]=event.values[1];
				gyrSensorDatas[gyrLength].gyr[2]=event.values[2];
				++gyrLength;
				if(gyrLength>=gyrSensorDatas.length){
					gyrExecutor.execute(new GyrCollectionTask(gyrSensorDatas,gyrSensorDatas.length));
					gyrLength=0;
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				magsSensorDatas[magsLength].time=Utilities.getTime();
				magsSensorDatas[magsLength].mags[0]=event.values[0];
				magsSensorDatas[magsLength].mags[1]=event.values[1];
				magsSensorDatas[magsLength].mags[2]=event.values[2];
				mags=event.values;
				++magsLength;
				if(magsLength>=magsSensorDatas.length){
					magsExecutor.execute(new MagsCollectionTask(magsSensorDatas,magsSensorDatas.length));
					magsLength=0;
				}
				
				rotationSensorDatas[rotationLength].time=Utilities.getTime();
				SensorManager.getRotationMatrix(rotationMatrix, null, accels, mags);
				SensorManager.getOrientation(rotationMatrix, rotationSensorDatas[rotationLength].attitude);
				rotationSensorDatas[rotationLength].attitude[0]=(float) Math.toDegrees(rotationSensorDatas[rotationLength].attitude[1]);
				rotationSensorDatas[rotationLength].attitude[1]=(float) Math.toDegrees(rotationSensorDatas[rotationLength].attitude[2]);
				rotationSensorDatas[rotationLength].attitude[2]=(float) Math.toDegrees(rotationSensorDatas[rotationLength].attitude[0]);
				++rotationLength;
				if(rotationLength>=rotationSensorDatas.length){
					rotationExecutor.execute(new RotationCollectionTask(rotationSensorDatas,rotationLength));
					rotationLength=0;
				}
				break;
			case Sensor.TYPE_PRESSURE:
				pressureSensorDatas[pressureLength].time=Utilities.getTime();
				pressureSensorDatas[pressureLength].pressure=event.values[0];
				pressureSensorDatas[pressureLength].pressureAttitude=
						SensorManager.getAltitude(
								SensorManager.PRESSURE_STANDARD_ATMOSPHERE, 
								pressureSensorDatas[pressureLength].pressure);
				++pressureLength;
				if(pressureLength>=pressureSensorDatas.length){
					pressureExecutor.execute(new PressureCollectionTask(pressureSensorDatas,pressureSensorDatas.length));
					pressureLength=0;
				}
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				temperatureSensorDatas[temperatureLength].temperature=event.values[0];
				++temperatureLength;
				if(temperatureLength>=temperatureSensorDatas.length){
					temperatureExecutor.execute(new TemperatureCollectionTask(temperatureSensorDatas,temperatureSensorDatas.length));
					temperatureLength=0;
				}
				break;
			}
		}
		
	}
}
