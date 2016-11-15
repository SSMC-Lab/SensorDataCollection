package com.fruitbasket.sensordatacollection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruitbasket.sensordatacollection.sensor.AccSensor;
import com.fruitbasket.sensordatacollection.sensor.GyrSensor;
import com.fruitbasket.sensordatacollection.sensor.MagsSensor;
import com.fruitbasket.sensordatacollection.sensor.OrientationSensor;
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
	private ExecutorService orientationExecutor;
	
	private AccSensor[] accSensorDatas;
	private GyrSensor[] gyrSensorDatas;
	private MagsSensor[] magsSensorDatas;
	private PressureSensor[] pressureSensorDatas;
	private RotationSensor[] rotationSensorDatas;
	private TemperatureSensor[] temperatureSensorDatas;
	private OrientationSensor[] orientationSensorsDatas;
	
	private int accLength=0;
	private int gyrLength=0;
	private int magsLength=0;
	private int pressureLength=0;
	private int rotationLength=0;
	private int temperatureLength=0;
	private int orientationLength = 0;
	
	//aid data
	private float[] rotationMatrix=new float[16];
	private float[] accels=new float[3];
	private float[] mags=new float[3];
	private float[] orientation = new float[3];
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(TAG,"onCreate()");
		mSensorManager= (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
		mySensorListener = new mySensorListener();

		int i;
		orientationSensorsDatas = new OrientationSensor[Condition.FAST_FLUSH_INTERVAL];
		for( i = 0;i<orientationSensorsDatas.length;++i){
			orientationSensorsDatas[i] = new OrientationSensor();
		}
		orientationExecutor = Executors.newSingleThreadExecutor();
		if(MainActivity.chooseSensor[1]) {
			accSensorDatas = new AccSensor[Condition.FAST_FLUSH_INTERVAL];
			for (i = 0; i < accSensorDatas.length; ++i) {
				accSensorDatas[i] = new AccSensor();
			}
			accExecutor = Executors.newSingleThreadExecutor();
		}

		if(MainActivity.chooseSensor[2]) {
			gyrSensorDatas = new GyrSensor[Condition.FAST_FLUSH_INTERVAL];
			for (i = 0; i < gyrSensorDatas.length; ++i) {
				gyrSensorDatas[i] = new GyrSensor();
			}
			gyrExecutor = Executors.newSingleThreadExecutor();
		}

		if(MainActivity.chooseSensor[3]) {
			magsSensorDatas = new MagsSensor[Condition.MID_FLUSH_INTERVAL];
			for (i = 0; i < magsSensorDatas.length; ++i) {
				magsSensorDatas[i] = new MagsSensor();
			}
			magsExecutor = Executors.newSingleThreadExecutor();
		}

		if(MainActivity.chooseSensor[4]) {

			pressureSensorDatas = new PressureSensor[Condition.MID_FLUSH_INTERVAL];
			for (i = 0; i < pressureSensorDatas.length; ++i) {
				pressureSensorDatas[i] = new PressureSensor();
			}
			pressureExecutor = Executors.newSingleThreadExecutor();
		}

		if(MainActivity.chooseSensor[5]) {
			rotationSensorDatas = new RotationSensor[Condition.MID_FLUSH_INTERVAL];
			for (i = 0; i < rotationSensorDatas.length; ++i) {
				rotationSensorDatas[i] = new RotationSensor();
			}
			rotationExecutor = Executors.newSingleThreadExecutor();
		}

		if(MainActivity.chooseSensor[6]) {
			temperatureSensorDatas = new TemperatureSensor[Condition.SLOW_FLUSH_INTERVAL];
			for (i = 0; i < temperatureSensorDatas.length; ++i) {
				temperatureSensorDatas[i] = new TemperatureSensor();
			}
			temperatureExecutor = Executors.newSingleThreadExecutor();
		}
		registerListeners();
	}
	
	@Override
	public void onDestroy(){
		unregisterListeners();
		if(MainActivity.chooseSensor[1]) {
			accExecutor.execute(new AccCollectionTask(accSensorDatas, accLength));
			accLength = 0;
			accExecutor.shutdown();
		}
		if(MainActivity.chooseSensor[2]) {
			gyrExecutor.execute(new GyrCollectionTask(gyrSensorDatas, gyrLength));
			gyrLength = 0;
			gyrExecutor.shutdown();
		}
		if(MainActivity.chooseSensor[3]) {
			magsExecutor.execute(new MagsCollectionTask(magsSensorDatas, magsLength));
			magsLength = 0;
			magsExecutor.shutdown();
		}
		if(MainActivity.chooseSensor[4]) {
			pressureExecutor.execute(new PressureCollectionTask(pressureSensorDatas, pressureLength));
			pressureLength = 0;
			pressureExecutor.shutdown();
		}
		if(MainActivity.chooseSensor[5]) {
			rotationExecutor.execute(new RotationCollectionTask(rotationSensorDatas, rotationLength));
			rotationLength = 0;
			rotationExecutor.shutdown();
		}
		if(MainActivity.chooseSensor[6]) {
			temperatureExecutor.execute(new TemperatureCollectionTask(temperatureSensorDatas, temperatureLength));
			temperatureLength = 0;
			temperatureExecutor.shutdown();
		}

		Log.d(TAG,"onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG,"onBind()");
		return new MyBinder();
	}
	
	private void registerListeners() {
		if(MainActivity.chooseSensor[4])
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_FASTEST);
		if(MainActivity.chooseSensor[1])
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_FASTEST);
		if(MainActivity.chooseSensor[3])
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_FASTEST);
		if(MainActivity.chooseSensor[2])
			mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_FASTEST);
		if(MainActivity.chooseSensor[6])
			mSensorManager.registerListener(mySensorListener, mSensorManager
				.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_FASTEST);
		if(MainActivity.chooseSensor[5])
		mSensorManager.registerListener(mySensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
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
		//计算方向
		private void calculateOrientation() {
			float[] values = new float[3];
			float[] R = new float[9];
			SensorManager.getRotationMatrix(R, null, accSensorDatas[accLength].accels,
					magsSensorDatas[magsLength].mags);
			SensorManager.getOrientation(R, values);
			orientationSensorsDatas[orientationLength].time=Utilities.getTime();
			orientationSensorsDatas[orientationLength].orientation[0]=values[0];
			orientationSensorsDatas[orientationLength].orientation[1]=values[1];
			orientationSensorsDatas[orientationLength].orientation[2]=values[2];
			orientation = values;
			++orientationLength;
			if(orientationLength>=orientationSensorsDatas.length){
				orientationExecutor.execute(new OrientationCollectionTask(orientationSensorsDatas,orientationSensorsDatas.length));
				orientationLength = 0 ;
			}

		}

		private void calculateAcc(){
			float y0 =(float) (-Math.sin(orientation[1]));
			float y1 =(float) (Math.cos(orientation[1])*Math.cos(orientation[0]));
			float y2 =(float) (Math.cos(orientation[1])*Math.sin(orientation[1]));

			float temp =(float) (Math.acos(-(Math.tan(orientation[1])*Math.tan(orientation[2]))));
			float x0 =(float) (-Math.sin(orientation[2]));
			float x1 =(float) (Math.cos(orientation[2])*Math.cos(orientation[0]+temp));
			float x2 = (float) (Math.cos(orientation[2])*Math.sin(orientation[0]+temp));

			float z0 = x2*y1-x1*y2;
			float z1 = x0*y2-x2*y0;
			float z2 = x1*y0-x0*y1;

			float a0 = accels[0]*x0+accels[1]*y0+accels[2]*z0+SensorManager.STANDARD_GRAVITY;//(这里加上标准重力加速度以抵消默认的重力加速度)
			float a1 = accels[0]*x1+accels[1]*y1+accels[2]*z1;
			float a2 = accels[0]*x2+accels[1]*y2+accels[2]*z2 ;

			accSensorDatas[accLength].time=Utilities.getTime();
			accSensorDatas[accLength].accels[0]=a0;
			accSensorDatas[accLength].accels[1]=a1;
			accSensorDatas[accLength].accels[2]=a2;
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch(event.sensor.getType()){
			case Sensor.TYPE_LINEAR_ACCELERATION:///
				//Log.d(TAG,"linear acceleration changed");
				accels=event.values;
				calculateAcc();
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
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
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
			default:
				calculateOrientation();

			}
		}
		
	}
}
