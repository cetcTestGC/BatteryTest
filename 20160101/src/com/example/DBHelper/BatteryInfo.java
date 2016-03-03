package com.example.DBHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.R.string;

public class BatteryInfo {
	
	public static final String BATTERY_TABLE = "batteryinfo";
	
	public static String ID = "_id";  //主键
	public static String TIME = "time";  //时间
	public static String CPU_E = "cpu_e"; //CPU效率
	public static String MEM_E = "mem_e"; //内存使用率
	public static String WIFI_DATA = "wifi_data"; //wifi使用量
	public static String MOBILE_DATA = "mobile_data"; //移动流量使用
	public static String GPS = "gps"; //GPS是否开启
	public static String BRIGHTNESS = "brightness"; //屏幕亮度
	public static String CONNECT = "connecting"; //当前网络
	public static String RSSI = "rssi"; //Received Signal Strength Indication接收的信号强度指示
	/*Reference Signal Receiving Power，参考信号接收功率) 
	 * 是LTE网络中可以代表无线信号强度的关键参数以及物理层测量需求之一，
	 * 是在某个符号内承载参考信号的所有RE(资源粒子)上接收到的信号功率的平均值*/
	public static String RSPR = "rspr"; 
	public static String SNR = "snr"; //信噪比
	public static String LONGITUDE = "longitude"; //经度
	public static String LATITUDE = "latitude"; //纬度
	public static String TEMPERATURE = "temperature"; //电池温度 
	public static String VOLTAGE = "voltage"; //电压 
	public static String ELECTRICITY = "level"; //电量 
	
	
	/*创建DB数据表*/
	public static final String CREATE_BATTERY_INFO_TABLE = "create table if not exists "
			+ BATTERY_TABLE 
			+ "(" + ID + " integer primary key autoincrement, "
			+ TIME + " varchar, "
			+ ELECTRICITY + " varchar, "
			+ TEMPERATURE + " varchar, "			
			+ VOLTAGE + " varchar, "			
			+ CPU_E + " varchar, "
			+ MEM_E + " varchar, "
			+ WIFI_DATA + " varchar, "
			+ MOBILE_DATA + " varchar, "
			+ GPS + " varchar, "
			+ BRIGHTNESS + " varchar, "
			+ CONNECT + " varchar, "
			+ RSSI + " varchar, "
			+ RSPR + " varchar, "
			+ SNR + " varchar, "
			+ LONGITUDE + " varchar, "
			+ LATITUDE + " varchar) ";

	/*删除DB数据表 */
	public static final String DROP_BATTY_TABLE = "drop table if exists " + BATTERY_TABLE;
	
	
	public BatteryInfo() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 数据库嵌入数据 ID 为自增长类型 只需初始化为NULL SQLite自行设置 
	 * time 记录时间
	 * @param cpu CPU利用率
	 * @param mem 内存利用率
	 * @param wifi wifi使用量
	 * @param mobile 移动数据使用量
	 * @param gps gps开关
	 * @param brightness  屏幕亮度
	 * @param connecting 当前网络
	 * @param rssi 接收的信号强度指示 单位dBM
	 * @param rspr 参考信号接收功率 单位dBM
	 * @param snr 信噪比 单位dB
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param temperature 电池温度
	 * @param voltage 电压
	 * @param electricity 电池电量
	 */

	public BatteryInfo(String electricity, String temperature, String voltage, String cpu, String mem,
			String wifi, String mobile, Boolean gps, String brightness, String connecting,
			String rssi, String rspr, String snr, String longitude, String latitude) {		
		this.ID = null;
		// 获取时间
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		this.TIME = str;
		this.ELECTRICITY = electricity + "%";
		this.TEMPERATURE = temperature;
		this.VOLTAGE = voltage + "v";
		this.CPU_E = cpu;
		this.MEM_E = mem;
		this.WIFI_DATA = wifi;
		this.MOBILE_DATA = mobile;
		this.GPS = String.valueOf(gps);
		this.BRIGHTNESS = brightness;
		this.CONNECT = connecting;
		this.RSSI = rssi + "dBM";
		this.RSPR = rspr + "dBM";
		this.SNR = snr + "dB";	
		this.LONGITUDE = longitude;
		this.LATITUDE = latitude;
		
		/*try {
		db.execSQL("INSERT INTO batteryinfo VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
				batteryInfo.TIME, batteryInfo.CPU_E, batteryInfo.MEM_E,
				batteryInfo.WIFI_USAGE, batteryInfo.FLOW_USAGE, batteryInfo.GPS,
				batteryInfo.SCREEN_B, batteryInfo.NET_NOW, batteryInfo.RSSI,
				batteryInfo.RSPR, batteryInfo.SNR});
		
		} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		}*/
	}
}
