package com.example.SavePowerMode;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MonitorService extends Service {
	
	
	private int lowValue;
	private SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		//获取到低电量触发值
		//Log.i("MonitorService", String.valueOf(stringtoInt(intent.getStringExtra("StartValueOfPower"))));
		//lowValue = stringtoInt(intent.getStringExtra("StartValueOfPower"));
		sharedPreferences = getSharedPreferences("lowPowerMode", SavePowerModeActivity.MODE_WORLD_READABLE);
		lowValue = stringtoInt(sharedPreferences.getString("StartValueOfPower", ""));
		Log.i("MonitorService", String.valueOf(lowValue));
		// 定义电池电量更新广播的过滤器,只接受带有ACTION_BATTERRY_CHANGED事件的Intent  
	    IntentFilter batteryChangedReceiverFilter = new IntentFilter();  
	    batteryChangedReceiverFilter.addAction(Intent.ACTION_BATTERY_CHANGED);  
	       
	    // 向系统注册batteryChangedReceiver接收器，本接收器的实现见代码字段处  
	    registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);  
	  
	   
	  
	    // 由于初始化本服务时系统可能没有发出ACTION_BATTERY_CHANGED广播，那么刚才注册的那个接收器将不会在本服务启动时被激活，这种情况下就无法显示当前电量，因此在这里添加一个匿名广播接收器。  
	    new BroadcastReceiver() {  
	      @Override
	      public void onReceive(Context context, Intent intent) {  
	        int level = intent.getIntExtra("level", 0);  //电池电量等级  
	        if(level <= lowValue){
	        	Log.i("MonitorService", "100");
	        	applyCpuMode();
	        }
	      }  
	    };  
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("MonitorService", "结束服务");
		unregisterReceiver(batteryChangedReceiver);
		super.onDestroy();
	}
	
	/**
     * 将电量百分比转换为整数型 
     * @param string
     */
    public int stringtoInt(String string) {
		if(string.equals("5%"))
			return 5;
		if(string.equals("10%"))
			return 10;
		if(string.equals("15%"))
			return 15;
		if(string.equals("20%"))
			return 20;
		return 0;
	}
    
    // 接受电池信息更新的广播  
    private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {  
      public void onReceive(Context context, Intent intent) {  
        int level = intent.getIntExtra("level", 0);  
        if(level <= lowValue){
        	Log.i("MonitorService", "101");
        	applyCpuMode();
        }
      }  
    };
    
    
    public void applyCpuMode() {
		try {
        	
        	//设置CPU频率
            CommandCapture setGovernor = new CommandCapture(0,
                    "echo \"" + "powersave" +
                            "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
            RootTools.getShell(true).add(setGovernor);
            //RootDeniedException | IOException rde            
        }catch (RootDeniedException rde) {
            rde.printStackTrace();
            Log.i("MonitorService", rde.getMessage());
        }catch (TimeoutException te) {
            te.printStackTrace();
            Log.i("MonitorService", te.getMessage());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("MonitorService", e.getMessage());
		} 
		
	}

}
