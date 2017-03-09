package com.fruitbasket.sensordatacollection;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    ///迁移这些常量的位置
    public static final int NUMBER_SENSOR =6;
    public static final int INDEX_ACC=0;
    public static final int INDEX_GYR=1;
    public static final int INDEX_MAG=2;
    public static final int INDEX_PRESSURE=3;
    public static final int INDEX_ROTATION=4;
    public static final int INDEX_TEMPERATURE=5;

    public boolean[] chooseSensor=new boolean[NUMBER_SENSOR];
    private boolean isBinded = false;

    private CheckBox check_box_acc;
    private CheckBox check_box_gyr;
    private CheckBox check_box_mag;
    private CheckBox check_box_pressure;
    private CheckBox check_box_rotation;
    private CheckBox check_box_temperature;
    private ToggleButton toggle_button_begin;

    private DataCollectionService.MyBinder binder ;
    private Handler handler=new MyHandler();
    private Intent dcServiceIntent;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "ServiceConnection.onServiceConnection()");
            isBinded = true;
            MainActivity.this.binder=(DataCollectionService.MyBinder)binder;
            MainActivity.this.binder.setHandler(handler);
            MainActivity.this.binder.setChooseSensor(chooseSensor);
            MainActivity.this.binder.beginDataCollecting();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "ServiceConnection.onServiceDisConnection()");
            isBinded = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreat()");
        setContentView(R.layout.activity_main);
        initViews();
        dcServiceIntent = new Intent(MainActivity.this, DataCollectionService.class);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    private void initViews() {
        check_box_acc=(CheckBox)findViewById(R.id.check_box_acc);
        check_box_gyr=(CheckBox)findViewById(R.id.check_box_gyr);
        check_box_mag=(CheckBox)findViewById(R.id.check_box_mag);
        check_box_pressure=(CheckBox)findViewById(R.id.check_box_pressure);
        check_box_rotation=(CheckBox)findViewById(R.id.check_box_rotation);
        check_box_temperature=(CheckBox)findViewById(R.id.check_box_temperature);
        toggle_button_begin=(ToggleButton)findViewById(R.id.toggle_button_begin);
        toggle_button_begin.setOnClickListener(new ToggleClickListener());
    }

    private void updateChooseSensor(){
        if(check_box_acc!=null&&
                check_box_gyr!=null&&
                check_box_mag!=null&&
                check_box_pressure!=null&&
                check_box_rotation!=null&&
                check_box_temperature!=null){

            chooseSensor[INDEX_ACC]=check_box_acc.isChecked();
            chooseSensor[INDEX_GYR]=check_box_gyr.isChecked();
            chooseSensor[INDEX_MAG]=check_box_mag.isChecked();
            chooseSensor[INDEX_PRESSURE]=check_box_pressure.isChecked();
            chooseSensor[INDEX_ROTATION]=check_box_rotation.isChecked();
            chooseSensor[INDEX_TEMPERATURE]=check_box_temperature.isChecked();
        }
    }

    private void startCollection() {
        Log.d(TAG, "startCollection()");
        if (isBinded == false) {
            bindService(dcServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            Toast.makeText(getBaseContext(), "start collects data", Toast.LENGTH_SHORT).show();
            //do not need isBinded=true;
        }
        //do not start the Service
    }

    private void stopCollection() {
        Log.d(TAG, "stopCollection()");
        if (isBinded == true) {
            unbindService(serviceConnection);
            stopService(dcServiceIntent);//must stop the Service
            isBinded = false;
        }
    }



    private class ToggleClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (((ToggleButton) view).isChecked() == true) {
                updateChooseSensor();
                startCollection();
            } else {
                stopCollection();
            }
        }
    }

    private class MyHandler extends Handler {

        private ProgressDialog progressDialog;

        @Override
        public void handleMessage(Message msg){
            Log.i(TAG,"handleMessage()");

            if(msg.what==Condition.BEGIN_SAVE_DATA){
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle(R.string.save_data_prompt);
                progressDialog.show();
            }
            else if(msg.what==Condition.DATA_SAVED){
                Log.i(TAG,"msg.what==Condition.DATA_SAVED");
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            }
        }
    }
}
