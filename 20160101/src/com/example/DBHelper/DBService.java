package com.example.DBHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import com.example.DBHelper.BatteryInfo;

public class DBService extends Service {

	public static final String DBService_Path = "com.example.DBHelper.DBService";
	// 计数值
	private int count = 0;
	// 数据库游标
	private SQLiteDatabase dbDatabase;
	private BatteryInfo batteryInfo;

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
		new PollingThread().start();

	}

	public class PollingThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			count++;
			// 当计数能被5整除时弹出通知 10秒通知
			if (count % 5 == 0) {
				dbDatabase = openOrCreateDatabase("battery.db",
						Context.MODE_WORLD_READABLE, null);
				batteryInfo = new BatteryInfo("levle", "℃", "60", "CPU", "MEM", "wifi", "gprs",
						true, "50%", "2G", "-19", "60", "122", "160", "130");
				try {
					dbDatabase
							.execSQL(
									"INSERT INTO batteryinfo VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
									new Object[] { batteryInfo.TIME,
											batteryInfo.ELECTRICITY,
											batteryInfo.TEMPERATURE,
											batteryInfo.VOLTAGE,											
											batteryInfo.CPU_E,
											batteryInfo.MEM_E,
											batteryInfo.WIFI_DATA,
											batteryInfo.MOBILE_DATA,
											batteryInfo.GPS,
											batteryInfo.BRIGHTNESS,
											batteryInfo.CONNECT,
											batteryInfo.RSSI, batteryInfo.RSPR,
											batteryInfo.SNR,
											batteryInfo.LONGITUDE,
											batteryInfo.LATITUDE});

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				dbDatabase.close();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
