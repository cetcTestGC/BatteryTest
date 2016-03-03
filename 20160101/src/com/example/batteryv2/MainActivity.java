package com.example.batteryv2;


import com.example.RootManager.SystemManager;
import com.example.SaveData.SaveData;
import com.example.SetCpuMode.SetCpuModeActivity;
import com.example.batteryv2.R;
import com.stericson.RootTools.RootTools;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	
	//CPU 模式数据保存
	private static  SharedPreferences cpuModeSharedPre;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//Root  授权
		String apkRoot = "battery" + getPackageCodePath();
		SystemManager.RootCommand(apkRoot);
		
		/* 完成Root授权     记录手机自身CPU运行模式  */
		cpuModeSharedPre = getSharedPreferences("cpumode", MainActivity.MODE_PRIVATE);
		SaveData.initShareMode(cpuModeSharedPre);
		SaveData.saveNorCpuMode(this);

		fragmentManager = getFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
		
		//开启主线程网络访问
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()  
        .penaltyLog()  
        .build());   
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()  
        .detectLeakedClosableObjects()  
        .penaltyLog()  
        .penaltyDeath()  
        .build());

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(R.id.bt1);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}
	
	
	

	/**
	 * 一旦activity到达了resumed状态,
	 * 你可以自由地在activity添加和移除fragment.因此,仅当activity处于resumed状态时,
	 * fragment的生命周期才可以独立变化.
	 * 
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						FragmentTransaction transaction = fragmentManager
								.beginTransaction();
						Fragment fragment = FragmentFactory
								.getInstanceByIndex(checkedId);
						transaction.replace(R.id.content, fragment);

						transaction.commit();
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
		// 或者下面这种方式
		// android.os.Process.killProcess(android.os.Process.myPid());
	}
}
