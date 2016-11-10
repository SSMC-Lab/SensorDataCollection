package com.fruitbasket.sensordatacollection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private boolean isBinded = false;

    private ToggleButton toggleButton;
    private Button button;
    public  static boolean[] chooseSensor = new boolean[7];
    public static boolean []isready = new boolean[7];
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog ;
    private Intent dcServiceIntent;
    private TextView textView;
    private final String data[] = {
            "","加速度传感器","陀螺仪传感器","磁力传感器","压力传感器","旋转矢量传感器","温度传感器",
    };
    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String d  = "你选择了:\n";
            for(int i = 1;i<=6;i++){
                if(chooseSensor[i]){
                    d+=data[i];
                    d+="\n";
                }
            }
            if(textView==null){
                textView= (TextView)findViewById(R.id.choose_text);
            }
            textView.setText(d);
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "ServiceConnection.onServiceConnection()");
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "ServiceConnection.onServiceDisConnection()");
            isBinded = false;
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void initViews() {
        toggleButton = (ToggleButton) this.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new ToggleClickListener());
        button = (Button) this.findViewById(R.id.choose_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alertlayout, null);
                Button ok = (Button) view.findViewById(R.id.alert_ok);
                textView  = (TextView) view.findViewById(R.id.choose_text);
                final CheckBox acc = (CheckBox) view.findViewById(R.id.acc_ckbox);
                final CheckBox mag = (CheckBox) view.findViewById(R.id.mag_ckbox);
                final CheckBox pre = (CheckBox) view.findViewById(R.id.pre_ckbox);
                final CheckBox rot = (CheckBox) view.findViewById(R.id.rot_ckbox);
                final CheckBox tem = (CheckBox) view.findViewById(R.id.tem_ckbox);
                final CheckBox gyr = (CheckBox) view.findViewById(R.id.gyr_ckbox);
                ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseSensor[1] = acc.isChecked();
                        chooseSensor[2] = gyr.isChecked();
                        chooseSensor[3] = mag.isChecked();
                        chooseSensor[4] = pre.isChecked();
                        chooseSensor[5] = rot.isChecked();
                        chooseSensor[6] = tem.isChecked();
                        alertDialog.dismiss();
                        dcServiceIntent = new Intent(MainActivity.this, DataCollectionService.class);
                        handler.sendMessage(new Message());
                    }
                });
                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    private void startCollection() {
        Log.d(TAG, "startCollection()");
        if (isBinded == false) {
            bindService(dcServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            Toast.makeText(getBaseContext(), "start collects data", Toast.LENGTH_SHORT).show();
            //donot need isBinded=true;
        }
        //do not start the Service
    }

    private void stopCollection() {
        Log.d(TAG, "stopCollection()");
        if (isBinded == true) {
            unbindService(serviceConnection);
            stopService(dcServiceIntent);//must stop the Service
            isBinded = false;
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("正在保存数据,请不要退出");
            progressDialog.show();
            new Thread(){
                public void  run(){
                    boolean flag  = true;
                    while(flag){
                        for(int i=1;i<=6;i++){
                            if(MainActivity.chooseSensor[i]) {
                                if (MainActivity.isready[i]);
                                else {
                                    flag = true;
                                    break;
                                }
                            }
                            if(i==6)
                                flag=false;
                        }

                    }
                    progressDialog.dismiss();
                }
            }.start();
            Toast.makeText(getBaseContext(), "stop", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fruitbasket.sensordatacollection/http/host/path")
        );

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fruitbasket.sensordatacollection/http/host/path")
        );

    }


    class ToggleClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (((ToggleButton) view).isChecked() == true) {
                startCollection();
            } else {
                stopCollection();
            }
        }
    }
}
