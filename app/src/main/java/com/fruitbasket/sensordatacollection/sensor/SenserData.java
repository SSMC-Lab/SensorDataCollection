package com.fruitbasket.sensordatacollection.sensor;

/**
 * 暂时没用的类
 * Created by FruitBasket on 2017/2/23.
 */

public abstract class SenserData {
    private String time;

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time=time;
    }

    public abstract SenserData[] objectArrayDeepCopyOf(SenserData[] senserDatas,int length);
}
