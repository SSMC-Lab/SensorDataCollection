package com.fruitbasket.sensordatacollection.task;

import com.fruitbasket.sensordatacollection.Condition;
import com.fruitbasket.sensordatacollection.MainActivity;
import com.fruitbasket.sensordatacollection.sensor.OrientationSensor;
import com.fruitbasket.sensordatacollection.utilities.ExcelProcessor;

import java.io.IOException;

/**
 * Created by zzbpc on 2016/11/12.
 */

public class OrientationCollectionTask implements Runnable {
    private OrientationSensor[] OrientationSensorDatas;
    private int length=0;

    public OrientationCollectionTask(OrientationSensor[] datas,int length){
        this.OrientationSensorDatas=OrientationSensor.objectArrayDeepCopyOf(datas, length);
        this.length=length;
    }

    @Override
    public void run() {
        try {
            MainActivity.isready[1] =false;
            ExcelProcessor.appendDataQuickly(Condition.ORIENTATION_EXCEL, OrientationSensorDatas,length);
            MainActivity.isready[1]=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
