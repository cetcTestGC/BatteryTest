package com.example.BootService;

import com.example.DBHelper.DBService;
import com.example.SavePowerMode.MonitorService;
import com.example.SavePowerMode.SavePowerModeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
	
	/**
	 * 开启判断是否启动Service 
	 */
	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		SharedPreferences pLoeModesharedPreferences = context.getSharedPreferences("lowPowerMode", SavePowerModeActivity.MODE_WORLD_READABLE);
		if(pLoeModesharedPreferences.getString("pLowModeSwitchTxt", "").equals("已打开")){
			if (intent.getAction().equals(ACTION)){   
				context.startService(new Intent(context, MonitorService.class));            
			} 
		}
		
		SharedPreferences sqlSwitchSharedPreferences = context.getSharedPreferences("sqlMode", context.MODE_WORLD_READABLE);
		if(sqlSwitchSharedPreferences.getString("sqlswitchtxtmode", "").equals("已打开")){
			if (intent.getAction().equals(ACTION)) {
				context.startService(new Intent(context, DBService.class));
			}
		}
	}

}
