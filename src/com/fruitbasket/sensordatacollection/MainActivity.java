package com.fruitbasket.sensordatacollection;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private static final String TAG="MainActivity";
	private boolean isBinded=false;
	
	private ToggleButton toggleButton;
	
	private Intent dcServiceIntent;
	private ServiceConnection serviceConnection=new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {	
			Log.d(TAG,"ServiceConnection.onServiceConnection()");
			isBinded=true;
			}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d(TAG,"ServiceConnection.onServiceDisConnection()");
			isBinded=false;
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		dcServiceIntent=new Intent(this,DataCollectionService.class);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG,"onDestroy()");
	}
	
	private void initViews(){
		toggleButton=(ToggleButton)this.findViewById(R.id.toggleButton);
		toggleButton.setOnClickListener(new ToggleClickListener());
	}
	
	private void startCollection(){
		Log.d(TAG,"startCollection()");
		if(isBinded==false){
			bindService(dcServiceIntent,serviceConnection,Context.BIND_AUTO_CREATE);
			Toast.makeText(getBaseContext(), "start collects data", Toast.LENGTH_SHORT).show();
			//donot need isBinded=true;
		}
		//do not start the Service
	}
	
	private void stopCollection(){
		Log.d(TAG,"stopCollection()");
		if(isBinded==true){
			unbindService(serviceConnection);
			stopService(dcServiceIntent);//must stop the Service
			isBinded=false;
			Toast.makeText(getBaseContext(), "stop", Toast.LENGTH_SHORT).show();
		}
	}
	

	
	
	
	class ToggleClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if(((ToggleButton) view).isChecked()==true){
				startCollection();
			}
			else{
				stopCollection();
			}
		}
	}
}
