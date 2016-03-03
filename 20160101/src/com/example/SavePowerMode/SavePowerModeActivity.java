package com.example.SavePowerMode;


import java.util.List;

import com.example.batteryv2.R;

import android.R.plurals;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SavePowerModeActivity extends Activity {
	
	
	//低电量模式 按键
	private LinearLayout lowmodeSwitchLayout;
	private TextView pLowModeSwitchTextView;
	
	//低电量触发值
	private LinearLayout pLowValueSwitchLayout;
	private TextView pLowValueSwitchTextView;
	
	private LinearLayout pLowModeChoiceLayout;
	private TextView pLowModeChoiceTextView;
	
	//获取低电量模式设置
	private SharedPreferences lowPowerModeSharePre;
	
	//线程检测
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.power_low_mode_activity);
		lowmodeSwitchLayout = (LinearLayout) findViewById(R.id.power_low_mode_switch);
		pLowModeSwitchTextView = (TextView) findViewById(R.id.power_low_state_switch);
		
		pLowValueSwitchLayout = (LinearLayout) findViewById(R.id.power_low_value_layout);
		pLowValueSwitchTextView = (TextView) findViewById(R.id.power_low_value_txt);
		
		pLowModeChoiceLayout = (LinearLayout) findViewById(R.id.power_low_mode_layout);
		pLowModeChoiceTextView = (TextView) findViewById(R.id.power_low_mode_txt);
		
		lowPowerModeSharePre = getSharedPreferences("lowPowerMode", SavePowerModeActivity.MODE_WORLD_READABLE);
	
		//判断是否已经打开了低电量监控
		if(lowPowerModeSharePre.getString("pLowModeSwitchTxt", "").equals("已打开")){
			//开关设置
			pLowModeSwitchTextView.setText("已打开");
			pLowModeSwitchTextView.setTextColor(0xff70bd34);
			//电量值设置
			pLowValueSwitchTextView.setText(lowPowerModeSharePre.getString("StartValueOfPower", ""));
            pLowValueSwitchTextView.setTextColor(0xff586272);
            
            //registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		}
		
		/* 低电量模式开启设置   */
		final Intent intent = new Intent(this, MonitorService.class);
		lowmodeSwitchLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pLowModeSwitchTextView.getText().toString().equals("已关闭")){
					pLowModeSwitchTextView.setText("已打开");
					//registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
					//荧光绿色
					pLowModeSwitchTextView.setTextColor(0xff70bd34);
					//获取低电量开启值以及开启模式
					SharedPreferences.Editor editor = lowPowerModeSharePre.edit();
					editor.putString("StartValueOfPower", pLowValueSwitchTextView.getText().toString());
					editor.putString("pLowModeSwitchTxt", "已打开");
					editor.putString("StartValueOfPower", pLowValueSwitchTextView.getText().toString());
					editor.commit();
					//向 Service传值 
					//intent.putExtra("StartValueOfPower", pLowValueSwitchTextView.getText().toString());
					//启动电池监控服务  	
					startService(intent);
				    			
				}else {
					pLowModeSwitchTextView.setText("已关闭");	
					stopService(intent);					
					SharedPreferences.Editor editor = lowPowerModeSharePre.edit();
					editor.putString("pLowModeSwitchTxt", "已关闭");
					editor.commit();
					//unregisterReceiver(batteryChangedReceiver);
					//关闭颜色
					pLowModeSwitchTextView.setTextColor(0xff586272);
				}
			}
		});
		
		/*   低电量开启值监听   */
		 
		pLowValueSwitchLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SavePowerModeActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("选择一个开启值");
                //    指定下拉列表的显示数据
                final String[] values = {"5%", "10%", "15%", "20%"};
                //    设置一个下拉的列表选择项
                builder.setItems(values, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int value)
                    {
                    	SharedPreferences.Editor editor = lowPowerModeSharePre.edit();
    					editor.putString("StartValueOfPower", values[value]);
    					editor.commit();
                        pLowValueSwitchTextView.setText(values[value]);
                        pLowValueSwitchTextView.setTextColor(0xff586272);
                    }
                });
                builder.show();
			}
		});
		
		
		
		/*  模式开启设置   
		pLowModeChoiceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SavePowerModeActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("选择一种模式");
                //    指定下拉列表的显示数据
                final String[] values = {"飞行模式", "低功耗模式"};
                //    设置一个下拉的列表选择项
                builder.setItems(values, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int value)
                    {
                        pLowModeChoiceTextView.setText(values[value]);
                        pLowModeChoiceTextView.setTextColor(0xff586272);
                        
                        /* 飞行模式设置  */
                        /*boolean isEnabled = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;  
                        Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled?0:1);  
                        Intent i=new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);  
                        i.putExtra("state", !isEnabled);  
                        sendBroadcast(i);
                    }
                });
                builder.show();
			}
		});*/
	}
	
	
	/**
	 * 获取低电量触发值
	 * @return
	 */
	
	public int getLowPowerValue() {	
		return stringtoInt(lowPowerModeSharePre.getString("StartValueOfPower", ""));
	}
	
	/**
	 * 广播接听电量低于%值 
	 * @author GYC
	 *
	 */
    /*private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {  
          
        @Override  
        public void onReceive(Context context, Intent intent) {
              
            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){  
                int lowPowValue;  
                int level = intent.getIntExtra("level", 0);   
                lowPowValue = stringtoInt(lowPowerModeSharePre.getString("StartValueOfPower", ""));
                if(level > lowPowValue){
                	Toast.makeText(context, "Test:"+lowPowValue, Toast.LENGTH_LONG).show();
                }
            } 
        }  
    };*/
    
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
    
    

	
}
