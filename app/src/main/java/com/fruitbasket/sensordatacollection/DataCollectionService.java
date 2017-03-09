package com.fruitbasket.sensordatacollection;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import com.fruitbasket.sensordatacollection.data.Data;
import com.fruitbasket.sensordatacollection.data.DataSaveTask;
import com.fruitbasket.sensordatacollection.data.PressureData;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import static com.fruitbasket.sensordatacollection.Condition.APP_FILE_DIR;

public class DataCollectionService extends Service {
	private static final String TAG="DataCollectionService";

	private boolean chooseSensor[];
	private SensorManager mSensorManager;
	private Handler handler;

	private SensorEventListener mySensorListener;
	private Thread[]  threads;
	private DataSaveTask[] dataSaveTasks;
	private LinkedBlockingQueue<Data>[] queues;
	private String subDir;

	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(TAG,"onCreate()");
		mSensorManager= (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
		mySensorListener = new mySensorListener();
	}

	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		Log.i(TAG,"onStartCommand()");
		return super.onStartCommand(intent,flags,startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG,"onBind()");
		return new MyBinder();
	}

	@Override
	public boolean onUnbind(Intent intent){
		Log.d(TAG,"onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy(){
		Log.d(TAG,"onDestroy()");
		unregisterListeners();
		Log.i(TAG,"onDestroy(): dataSaveTasks.length=="+dataSaveTasks.length);
		for(int i=0;i<dataSaveTasks.length;i++){
			if(dataSaveTasks[i]!=null){
				Log.i(TAG,"dataSaveTasks["+i+"]!=null");
				dataSaveTasks[i].stop();
			}
			else{
				Log.i(TAG,"dataSaveTasks["+i+"]==null");
			}
		}
		Log.i(TAG,"onDestroy(): begin waitting data saving");
		new Thread(){
			@Override
			public void run(){
				if(handler!=null) {
					Log.i(TAG,"handler!=null");
					handler.sendEmptyMessage(Condition.BEGIN_SAVE_DATA);
					try {
						for (int i = 0; i < threads.length; i++) {
							if (threads[i] != null) {
								Log.i(TAG, "threads[" + i + "]!=null");
								threads[i].join();
							} else {
								Log.i(TAG, "threads[" + i + "]==null");
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i(TAG,"发送结束对话框信息");
					handler.sendEmptyMessage(Condition.DATA_SAVED);
				}
				else{
					Log.w(TAG,"handler==null");
				}
			}
		}.start();

		super.onDestroy();
	}

	/**
	 * 准备采集数据前的数据
	 * @return
     */
	private boolean prepareData(){
		Log.i(TAG,"prepareData()");
		if(chooseSensor!=null&&chooseSensor.length==MainActivity.NUMBER_SENSOR){
			queues=new LinkedBlockingQueue[chooseSensor.length];
			dataSaveTasks=new DataSaveTask[chooseSensor.length];
			threads=new Thread[chooseSensor.length];

			String filePath,header;
			for(int i=0;i<chooseSensor.length;i++){
				if(chooseSensor[i]==true){
					queues[i]=new LinkedBlockingQueue<>();
					switch(i){
						case MainActivity.INDEX_ACC:
							filePath=subDir+"/"+ Condition.ACC_FILENAME;
							///header="Time\taccX\taccY\taccZ";
							header="accX\taccY\taccZ";
							break;

						case MainActivity.INDEX_GYR:
							filePath=subDir+"/"+Condition.GYR_FILENAME;
							///header="Time\tgyrX\tgyrY\tgyrZ";
							header="gyrX\tgyrY\tgyrZ";
							break;

						case MainActivity.INDEX_MAG:
							filePath=subDir+"/"+Condition.MAGS_FILENAME;
							///header="Time\tmagX\tmagY\tmagZ";
							header="magX\tmagY\tmagZ";
							break;

						case MainActivity.INDEX_PRESSURE:
							filePath=subDir+"/"+Condition.PRE_ALT_FILENAME;
							///header="Time\tPressure\tAltitude";
							header="Pressure\tAltitude";
							break;

						case MainActivity.INDEX_ROTATION:
							filePath=subDir+"/"+Condition.ROTATION_FILENAME;
							///header="Time\tPitch(x)\tRoll(y)\tAzimuth(z)";
							header="Pitch(x)\tRoll(y)\tAzimuth(z)";
							break;

						case MainActivity.INDEX_TEMPERATURE:
							filePath=subDir+"/"+Condition.TEMPERATURE_FILENAME;
							///header="Time\tTemperature";
							header="Temperature";
							break;

						default:
							Log.w(TAG,"prepareData() error");
							continue;
					}
					Log.i(TAG,"prepareData() : filePath=="+filePath);
					dataSaveTasks[i]=new DataSaveTask(
							queues[i],
							filePath,
							header
					);
					threads[i]=new Thread(dataSaveTasks[i]);
				}
			}

			Log.d(TAG,"prepareData(): prepare data succeed");
			return true;
		}
		else{
			Log.d(TAG,"prepareData(): prepare data failed");
			return false;
		}
	}

	/**
	 * 注册传感器的监听器
	 * @return
     */
	private boolean registerListeners() {
		Log.i(TAG,"registerListeners()");

		if(chooseSensor!=null&chooseSensor.length==MainActivity.NUMBER_SENSOR){
			if(chooseSensor[MainActivity.INDEX_ACC])
				mSensorManager.registerListener(mySensorListener,
						mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_FASTEST);

			if(chooseSensor[MainActivity.INDEX_GYR])
				mSensorManager.registerListener(mySensorListener,
						mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
						SensorManager.SENSOR_DELAY_FASTEST);

			if(chooseSensor[MainActivity.INDEX_MAG])
				mSensorManager.registerListener(mySensorListener,
						mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
						SensorManager.SENSOR_DELAY_FASTEST);

			if(chooseSensor[MainActivity.INDEX_PRESSURE])
				mSensorManager.registerListener(mySensorListener,
						mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
						SensorManager.SENSOR_DELAY_FASTEST);

			if(chooseSensor[MainActivity.INDEX_ROTATION])
				mSensorManager.registerListener(mySensorListener,
						mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
						SensorManager.SENSOR_DELAY_FASTEST);

			if(chooseSensor[MainActivity.INDEX_TEMPERATURE])
				mSensorManager.registerListener(mySensorListener, mSensorManager
								.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
						SensorManager.SENSOR_DELAY_FASTEST);

			Log.d(TAG,"registerListeners(): succeed");
			return true;
		}
		else{
			Log.d(TAG,"registerListeners(): failed");
			return false;
		}
	}

	private void unregisterListeners(){
		mSensorManager.unregisterListener(mySensorListener);
	}

	/**
	 * 创建数据存放的目录
	 * @return
     */
	private boolean createDataDir(){
		Log.i(TAG,"createDataDir()");

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
			Log.w(TAG,"names==null");
		}
		Log.i(TAG,"maxNumber=="+maxNumber);
		subDir=appFileDir.getPath()+"/"+(maxNumber+1);
		Log.i(TAG,subDir);
		(new File(subDir)).mkdir();
		return true;
	}

	/**
	 * MyBinder作为代理，让外界与DataCollectService能够进行通信
	 */
	public class MyBinder extends Binder{
		public DataCollectionService getService(){
			return DataCollectionService.this;
		}

		public void setHandler(Handler handler){
			DataCollectionService.this.handler=handler;
		}

		/**
		 * 设置所选择的传感器
		 * @param chooseSensor
         */
		public void setChooseSensor(boolean[] chooseSensor){
			DataCollectionService.this.chooseSensor=chooseSensor;
		}

		/**
		 * 启动数据收集
		 * @return
         */
		public boolean beginDataCollecting(){
			Log.i(TAG,"beginDataCollecting()");
			createDataDir();
			if(prepareData()==true){
				//启动数据存储线程
				for(int i=0;i<threads.length;i++){
					if(threads[i]!=null){
						threads[i].start();
					}
				}

				if(registerListeners()==true){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}

	}

	private class mySensorListener implements SensorEventListener{

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch(event.sensor.getType()){
				case Sensor.TYPE_LINEAR_ACCELERATION:
					//Log.d(TAG,"linear acceleration changed");
					queues[MainActivity.INDEX_ACC].offer(
							new Data(event.timestamp,event.values)
					);
					break;
				case Sensor.TYPE_GYROSCOPE:
					queues[MainActivity.INDEX_GYR].offer(
							new Data(event.timestamp,event.values)
					);
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:///此数据很奇怪
					queues[MainActivity.INDEX_MAG].offer(
							new Data(event.timestamp,event.values)
					);
					break;
				case Sensor.TYPE_ROTATION_VECTOR:
					///这里的计算方法看着有点不妥
					queues[MainActivity.INDEX_ROTATION].offer(
							new Data(event.timestamp,event.values)
					);
					break;
				case Sensor.TYPE_PRESSURE:
					queues[MainActivity.INDEX_PRESSURE].offer(
							new PressureData(
									event.timestamp,
									event.values,
									SensorManager.getAltitude(
											SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
											event.values[0]))
					);
					break;
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					queues[MainActivity.INDEX_TEMPERATURE].offer(
							new Data(event.timestamp,event.values)
					);
					break;
			}
		}

	}
}
