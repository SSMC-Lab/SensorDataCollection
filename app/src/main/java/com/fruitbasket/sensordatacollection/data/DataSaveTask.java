package com.fruitbasket.sensordatacollection.data;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DataSaveTask implements Runnable {

    private static final String TAG="data.DataSaveTask";
    private LinkedBlockingQueue<Data> queue;
    private String filePath;//指示数据存放的完整路径和文件名
    private String header;//存放文件头

    private boolean isStop;///暂时先使用结束标记

    public DataSaveTask(){
    }

    public DataSaveTask(LinkedBlockingQueue<Data>queue){
        this(queue,null,null);
    }

    public DataSaveTask(LinkedBlockingQueue<Data> queue,String filePath,String header){
        this.queue=queue;
        this.filePath=filePath;
        this.header=header;
    }


    public void setQueue(LinkedBlockingQueue<Data> queue){
        this.queue=queue;
    }

    public void setOutFile(String filePath){
        this.filePath=filePath;
    }

    public void setHeader(String header){
        this.header=header;
    }

    @Override
    final public void run() {
        Log.i(TAG,"run()");
        if(queue==null||filePath==null||header==null){
            Log.w(TAG,"run(): parameter is null");
            return;
        }

        Data data;
        int i;
        try {
            Log.i(TAG,"run() : filePath="+filePath);
            File outFile=new File(filePath);
            outFile.createNewFile();//创建文件，这里认为文件的所在的文件夹已经存在

            DataOutputStream outputStream=new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(outFile)
                    )
            );
            outputStream.writeBytes(header+'\n');
            Log.i(TAG,"run(): writed header succeeded");
            isStop=false;
            while(!isStop){
                //Log.i(TAG,"begin a loop");
                if(queue!=null){
                    ///这样去除数据，性能会不会很低
                    data=queue.poll(500, TimeUnit.MILLISECONDS);
                    if(data==null){
                        continue;
                    }
                    //Log.i(TAG,"queue.take() succeeded");
                    ///此处应判断是否写入时间
                    //outputStream.writeBytes(data.timestamp+"	");
                    for(i=0;i<data.values.length;i++){
                        outputStream.writeBytes(data.values[i]+"	");
                    }
                    outputStream.write('\n');
                }
                else{
                    Log.w(TAG,"queue==null");
                }
                //Log.i(TAG,"end a loop");
            }
            outputStream.flush();
            outputStream.close();
            Log.i(TAG,"run(): data saving end");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        Log.i(TAG,"stop()");
        isStop=true;
    }
}
