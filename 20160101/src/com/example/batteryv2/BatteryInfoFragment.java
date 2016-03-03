package com.example.batteryv2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import android.media.AudioManager;
import android.app.Activity;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * Created by ChenYan on 15-6-3.
 */
public class BatteryInfoFragment extends Fragment {
 
	private DecimalFormat decimalFormat1;//将float数据保留两位小数
	private DecimalFormat decimalFormat2;
	private ImageView battery;
    private int Battery_quantity;  //目前电量  
    private float Battery_voltage;   //电池电压  
    private float Battery_temp;      //电池温度  
    private int scale;
	private Context context;
    private String[] text_data;
	private ImageButton rb_wifi;
	private ImageButton rb_mobiledata;
	private ImageButton rb_gps;
	private ImageButton rb_buletooth;
	private ImageButton rb_bright;
	private ImageButton rb_ringmodel;
	private ListView listView;
	private ContentResolver contentResolver;
	private LocationManager locationManager;
	private BluetoothAdapter bluetoothAdapter;
	private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;
	private AudioManager audioManager;
	private PowerManager powerManager;
    private BrightObserver brightObserver ;
    
	private static final String WIFI_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";
	private static final String NETWORK_CHANGE = "android.intent.action.ANY_DATA_STATE";  
	private static final String BLUETOOTH_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";  
    private static final String BLUETOOTH_ACTION = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";  
    public  static final String RINGER_MODE_CHANGED = "android.media.RINGER_MODE_CHANGED";
    private static final String GPS_CHANGED = "android.location.PROVIDERS_CHANGED"; 
    
    private WifiChangedBroadcastReceiver wifiChangedBroadcastReceiver; 
    private BlueToothChangedBroadcastReceiver blueToothChangedBroadcastReceiver;
    private MobileDataChangedBroadcastReceiver mobileDataChangedBroadcastReceiver ;
    private RingModelChangedBroadcastReceiver ringModelChangedBroadcastReceiver ;
    private GPSChangedBroadcastReceiver gpsChangedBroadcastReceiver;

    private IntentFilter wifiIntentFilter;   
    private IntentFilter mobileDataIntentFilter; 
    private IntentFilter blueToothIntentFilter;  
    private IntentFilter gpsIntentFilter; 
    private IntentFilter ringModelIntentFilter; 
    private IntentFilter batteryIntentFilter;
  
    private static final int LIGHT_25_PERCENT = 64;  
    private static final int LIGHT_50_PERCENT = 127;  
    private static final int LIGHT_75_PERCENT = 191;  
    private static final int LIGHT_100_PERCENT = 255;  
    private static final int LIGHT_AUTO = 0;  
    private static final int LIGHT_ERR = -1;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = getActivity();   
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment1, null);
		return view;	
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		battery=(ImageView)getActivity().findViewById(R.id.img_battery);
		battery.setImageResource(R.drawable.one_battery);
		
		rb_wifi=(ImageButton)getActivity().findViewById(R.id.wifi);
		rb_mobiledata=(ImageButton)getActivity().findViewById(R.id.mobiledata);
		rb_gps=(ImageButton)getActivity().findViewById(R.id.gps);
		rb_buletooth=(ImageButton)getActivity().findViewById(R.id.bluetooth);
		rb_bright=(ImageButton)getActivity().findViewById(R.id.bright);
		rb_ringmodel=(ImageButton)getActivity().findViewById(R.id.ringmodel);

	    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	    connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    brightObserver = new BrightObserver(new Handler());
	    powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
	    locationManager =  (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    wifiChangedBroadcastReceiver = new WifiChangedBroadcastReceiver();
	    ringModelChangedBroadcastReceiver= new RingModelChangedBroadcastReceiver();
	    blueToothChangedBroadcastReceiver=new BlueToothChangedBroadcastReceiver();
	    mobileDataChangedBroadcastReceiver=new MobileDataChangedBroadcastReceiver();
	    gpsChangedBroadcastReceiver=new GPSChangedBroadcastReceiver();
	    
        wifiIntentFilter = new IntentFilter();  
        ringModelIntentFilter=new IntentFilter();
        blueToothIntentFilter=new IntentFilter();
        mobileDataIntentFilter=new IntentFilter();
        batteryIntentFilter=new IntentFilter();
        gpsIntentFilter=new IntentFilter();
        
        wifiIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        ringModelIntentFilter.addAction("android.media.RINGER_MODE_CHANGED");
        mobileDataIntentFilter.addAction("android.intent.action.ANY_DATA_STATE");
        batteryIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        blueToothIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");  
        blueToothIntentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");  
        gpsIntentFilter.addAction("android.location.PROVIDERS_CHANGED");
    	context.registerReceiver(batteryChangedReceiver, batteryIntentFilter);
		listView=(ListView)getActivity().findViewById(R.id.battery_listview);
		
        refreshWifi(); 
        refreshMobileData();
        refreshBlueTooth();
        refreshGPS();
        refreshRingModel();
        refreshBrightness();   
	 }
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	//电池电量改变广播
	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(Intent.ACTION_BATTERY_CHANGED.equals(action)) {
			
				decimalFormat1 =new DecimalFormat("##0.00");
				decimalFormat2 =new DecimalFormat("##0.00");
				
				Battery_quantity= intent.getIntExtra("level", 0);
				scale = intent.getIntExtra("scale", 100);
				
			    Battery_temp = intent.getIntExtra("temperature", 0);//电池温度 
		        String Battery_Temp =decimalFormat1.format(Battery_temp*0.1);//保留两位小数
				
				Battery_voltage = intent.getIntExtra("voltage", 0);  //电池电压  
			    String  Battery_Voltage = decimalFormat2.format(Battery_voltage*0.001);//保留两位小数
	            
	    	    text_data = new String[] {"电池电量:  "+(Battery_quantity*100/scale) + "%" ,"电池温度:  "+Battery_Temp+"℃",
	    	    		"电池电压:  "+Battery_Voltage+"V" };

	    		ArrayAdapter<String> arrayAdapter = (new ArrayAdapter<String>(context, R.layout.one_battery_listitem,
	    				R.id.one_listitem_text,text_data));
	    		listView.setAdapter(arrayAdapter);
				}
			} 
		};
	//监测Wifi是否打开
	private void refreshWifi(){

	    if(wifiManager.isWifiEnabled()==true)	{

	    	rb_wifi.setImageDrawable(getResources().getDrawable(R.drawable.one_wifi_open));
	     }
	    else{
	    	rb_wifi.setImageDrawable(getResources().getDrawable(R.drawable.one_wifi_close));	
	    }	
	}
	
	//wifi改变广播
	 private class WifiChangedBroadcastReceiver extends BroadcastReceiver  
	    {  
	  	        @Override  
	        public void onReceive(Context context, Intent intent)   
	        {  
	            // TODO Auto-generated method stub  
	            String action = intent.getAction();  
	            if (WIFI_CHANGED.equals(action))  
	            {  
	                refreshWifi();  

	            }      
	        }  
	    }
   //监测GPS是否打开
	private void refreshGPS(){

	    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true)	{
	    	rb_gps.setImageDrawable(getResources().getDrawable((R.drawable.one_gps_open)));
	     }
	    else{
	        rb_gps.setImageDrawable(getResources().getDrawable((R.drawable.one_gps_close)));	
	    }	
	}
		
	//gps改变广播
	 private class GPSChangedBroadcastReceiver extends BroadcastReceiver  
	    {  
	  	        @Override  
	        public void onReceive(Context context, Intent intent)   
	        {  
	            // TODO Auto-generated method stub  
	            String action = intent.getAction();  
	            if (GPS_CHANGED.equals(action))  
	            {  
	                refreshGPS();  
 
	            }      
	        }  
	    }
 
	 
	//监测蓝牙是否打开
		private void refreshBlueTooth(){	
			switch (bluetoothAdapter.getState())   
	        {  
	        case BluetoothAdapter.STATE_ON:  
	        	rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_open));
	            break;  
	        case BluetoothAdapter.STATE_TURNING_ON:  
	        	rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_open));
	            break;  
	        case BluetoothAdapter.STATE_OFF:  
	        	rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_close));
	            break;  
	        case BluetoothAdapter.STATE_TURNING_OFF:  
	        	rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_close));
	            break;  
	        }  
		}
	 //蓝牙改变广播
	 private class BlueToothChangedBroadcastReceiver extends BroadcastReceiver  
	    {  
	  	        @Override  
	        public void onReceive(Context context, Intent intent)   
	        {  
	            // TODO Auto-generated method stub  
	            String action = intent.getAction();  
	  
	            if (BLUETOOTH_CHANGED.equals(action) || BLUETOOTH_ACTION.equals(action))  
	            {  
	            	refreshBlueTooth(); 
         
	            }      
	        }  
	    } 
	  //监测移动网络开关是否打开
		private void refreshMobileData(){
		    if(getMobileDataStatus())	{
		    	rb_mobiledata.setImageDrawable(getResources().getDrawable((R.drawable.one_mobiledata_open)));
		     }
		    else{
		        rb_mobiledata.setImageDrawable(getResources().getDrawable((R.drawable.one_mobiledata_close)));	
		    }	
		}
		//获取移动数据的信息
		private boolean getMobileDataStatus()  
	    {  
	        String methodName = "getMobileDataEnabled";  
	        Class<? extends ConnectivityManager> cmClass = connectivityManager.getClass();  
	        Boolean isOpen = null;     
	        try   
	        {  
	            Method method = cmClass.getMethod(methodName);  
	            isOpen = (Boolean) method.invoke(connectivityManager);  
	        }   
	        catch (Exception e)   
	        {  
	            e.printStackTrace();  
	        }  
	        return isOpen;  
	    }  
		// 通过反射实现开启或关闭移动数据  
	    private void setMobileDataStatus(boolean enabled)   
	    {  
	        try   
	        {  
	            Class<?> conMgrClass = Class.forName(connectivityManager.getClass().getName());  
	            //得到ConnectivityManager类的成员变量mService（ConnectivityService类型）  
	            java.lang.reflect.Field iConMgrField = conMgrClass.getDeclaredField("mService");  
	                              iConMgrField.setAccessible(true);  
	            //mService成员初始化  
	            Object iConMgr = iConMgrField.get(connectivityManager);  
	            //得到mService对应的Class对象  
	            Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());  
	            //得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现)， 
	            // * 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE 
	               
	            Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(  
	                    "setMobileDataEnabled", Boolean.TYPE);  
	            setMobileDataEnabledMethod.setAccessible(true);  
	            //调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的）， 
	             //* 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的 
	               
	            setMobileDataEnabledMethod.invoke(iConMgr, enabled);  
	        } catch (ClassNotFoundException e)   
	        {  
	            e.printStackTrace();  
	        } catch (NoSuchFieldException e)   
	        {  
	            e.printStackTrace();  
	        } catch (SecurityException e)   
	        {  
	            e.printStackTrace();  
	        } catch (NoSuchMethodException e)   
	        {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e)   
	        {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e)   
	        {  
	            e.printStackTrace();  
	        } catch (InvocationTargetException e)   
	        {  
	            e.printStackTrace();  
	        }  
	    }  

	  //移动数据改变广播
		 private class MobileDataChangedBroadcastReceiver  extends BroadcastReceiver  
		    {  
		  
		        @Override  
		        public void onReceive(Context context, Intent intent)   
		        {  
		            // TODO Auto-generated method stub  
		            String action = intent.getAction();  
		  
		            if (NETWORK_CHANGE.equals(action))  
		            {  
		            	refreshMobileData();

		            }  
		        }    
		    }  

		//监测铃音状态
	    private void refreshRingModel()  
	    {  
	        switch (audioManager.getRingerMode())  
	        {  
	        case AudioManager.RINGER_MODE_SILENT: 
	        	rb_ringmodel.setImageDrawable(getResources().getDrawable((R.drawable.one_silence)));  
	            break;  
	        case AudioManager.RINGER_MODE_NORMAL:  
	        	rb_ringmodel.setImageDrawable(getResources().getDrawable((R.drawable.one_normal)));  
	            break;        
	        case AudioManager.RINGER_MODE_VIBRATE:  
	        	rb_ringmodel.setImageDrawable(getResources().getDrawable((R.drawable.one_vibrate)));  
	            break;  
	        }  
	    }  
		//铃音改变广播
		private class RingModelChangedBroadcastReceiver  extends BroadcastReceiver  
	    {  
	  
	        @Override  
	        public void onReceive(Context context, Intent intent)   
	        {  
	            // TODO Auto-generated method stub  
	            String action = intent.getAction();  
	  
	            if (RINGER_MODE_CHANGED .equals(action))  
	            {  
	            	refreshRingModel();

	            }  
	        }  
	  
	    }  
		//获取屏幕亮度信息
		private int getBrightness()  
	    {  
	        // TODO Auto-generated method stub  
	        int light = 0;  
	        boolean auto = false;  
	        contentResolver=context.getContentResolver();  
  
	        try   
	        {  
	            auto = Settings.System.getInt(contentResolver,  
	                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;  
	            if (!auto)   
	            {  
	                light = android.provider.Settings.System.getInt(contentResolver,  
	                        Settings.System.SCREEN_BRIGHTNESS, -1);  
	                if (light > 0 && light <= LIGHT_25_PERCENT)   
	                {  
	                    return LIGHT_25_PERCENT;  
	                }  
	                else if (light > LIGHT_25_PERCENT && light <= LIGHT_50_PERCENT)   
	                {  
	                    return LIGHT_50_PERCENT;  
	                }   
	                else if (light > LIGHT_50_PERCENT && light <= LIGHT_75_PERCENT)   
	                {  
	                    return LIGHT_75_PERCENT;  
	                }   
	                else if (light > LIGHT_75_PERCENT && light <= LIGHT_100_PERCENT)  
	                {  
	                    return LIGHT_100_PERCENT;  
	                }  
	            }   
	            else   
	            {  
	                return LIGHT_AUTO;  
	            }  
	        }   
	        catch (SettingNotFoundException e1)   
	        {  
	            // TODO Auto-generated catch block  
	            e1.printStackTrace();  
	        }  
	        return LIGHT_ERR;  
	      
	    } 
		//显示屏幕亮度
		private void refreshBrightness()  
	    {  
	        switch (getBrightness())   
	        {  
	        case LIGHT_25_PERCENT:  
	            rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightness_25)) ; 
	            break;  
	        case LIGHT_50_PERCENT:  
	        	rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_50)) ; 
	            break;  
	        case LIGHT_75_PERCENT:  
	        	rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_75)) ; 
	            break;  
	        case LIGHT_100_PERCENT:  
	        	rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_100)) ;
	            break;  
	        case LIGHT_AUTO:  
	        	rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_auto)) ;  
	            break;  
	        case LIGHT_ERR:  
	        	rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_bright)) ;  
	            break;  
	        }  
	    }  
 
		//通过反射设置屏幕亮度值
		private void setBrightness(int light)  
	    {  
	        try  
	        {  
	            //得到PowerManager类对应的Class对象  
	            Class<?> pmClass = Class.forName(powerManager.getClass().getName());  
	            //得到PowerManager类中的成员mService（mService为PowerManagerService类型）  
	            java.lang.reflect.Field field = pmClass.getDeclaredField("mService");  
	            field.setAccessible(true);  
	            //实例化mService  
	            Object iPM = field.get(powerManager);  
	            //得到PowerManagerService对应的Class对象  
	            Class<?> iPMClass = Class.forName(iPM.getClass().getName());  
	            //得到PowerManagerService的函数setBacklightBrightness对应的Method对象， 
	             //* PowerManager的函数setBacklightBrightness实现在PowerManagerService中 
	               
	            Method method = iPMClass.getDeclaredMethod("setBacklightBrightness", int.class);  
	            method.setAccessible(true);  
	            //调用实现PowerManagerService的setBacklightBrightness  
	            method.invoke(iPM, light);  
	        }  
	        catch (ClassNotFoundException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (NoSuchFieldException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (IllegalArgumentException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (IllegalAccessException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (NoSuchMethodException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        catch (InvocationTargetException e)  
	        {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	  
	    } 
		
		//设置亮度
		private void setBrightStatus()
		{
			int light = 0;
			switch (getBrightness())
			{
			case LIGHT_25_PERCENT:
				light = LIGHT_50_PERCENT ;
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_50));
				break;
			case LIGHT_50_PERCENT:
				light = LIGHT_75_PERCENT;
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_75));
				break;
			case LIGHT_75_PERCENT:
				light = LIGHT_100_PERCENT;
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_100));
				break;
			case LIGHT_100_PERCENT:
				startAutoBrightness(context.getContentResolver());
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightnes_auto));
				break;
			case LIGHT_AUTO:
				light = LIGHT_25_PERCENT-1;
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightness_25));
				stopAutoBrightness(context.getContentResolver());
				break;
			case LIGHT_ERR:
				light = LIGHT_25_PERCENT-1;
				rb_bright.setImageDrawable(getResources().getDrawable(R.drawable.one_brightness_25));
				break;
			}
			setBrightness(light);
			setScreenLightValue(context.getContentResolver(), light);
		}
		
		//启动自动调节亮度  
	    public void startAutoBrightness(ContentResolver cr)   
	    {  
	        Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,  
	                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  
	    }  
	      
	    //关闭自动调节亮度  
	    public void stopAutoBrightness(ContentResolver cr)   
	    {  
	        Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,  
	                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  
	    }  
	      
		//设置屏幕亮度的改变值
		public void setScreenLightValue(ContentResolver resolver, int value)  
	    {  
	        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,  
	                value);  
	    }  
       //亮度改变监测器实时监测
		private class BrightObserver extends ContentObserver  
	    {  
	        ContentResolver mResolver;    
	        public BrightObserver(Handler handler)  
	        {  
	            super(handler);  
	            mResolver = context.getContentResolver();  
	        } 
	        @Override  
	        public void onChange(boolean selfChange)   
	        {  
	            // TODO Auto-generated method stub  
	            super.onChange(selfChange);  
	            refreshBrightness();  
	        }  
			//注册观察  
	        public void startObserver()  
	        {  
	            mResolver.registerContentObserver(Settings.System  
	                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS), false,  
	                    this);  
	            mResolver.registerContentObserver(Settings.System  
	                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false,  
	                    this);  
	        }    
	        //解除观察  
	        public void stopObserver()  
	        {  
	            mResolver.unregisterContentObserver(this);  
	        }  
       }  	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		context.registerReceiver(gpsChangedBroadcastReceiver, gpsIntentFilter);
		context.registerReceiver(wifiChangedBroadcastReceiver, wifiIntentFilter);
		context.registerReceiver(blueToothChangedBroadcastReceiver, blueToothIntentFilter);
		context.registerReceiver(mobileDataChangedBroadcastReceiver, mobileDataIntentFilter);
		context.registerReceiver(ringModelChangedBroadcastReceiver, ringModelIntentFilter);
		brightObserver.startObserver();
	 
		//wifi点击事件
		rb_wifi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(wifiManager.isWifiEnabled()){
					wifiManager.setWifiEnabled(false);
			    	rb_wifi.setImageDrawable(getResources().getDrawable(R.drawable.one_wifi_open));			       
				    }
				else {
					wifiManager.setWifiEnabled(true);
			    	rb_wifi.setImageDrawable(getResources().getDrawable(R.drawable.one_wifi_close));
			     }
				}
				});
		
		//蓝牙点击事件
		rb_buletooth.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (bluetoothAdapter.getState()) {
				case  BluetoothAdapter.STATE_ON :
					 bluetoothAdapter.disable();
					 rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_close));
					 break;
				case  BluetoothAdapter.STATE_TURNING_ON :
					 bluetoothAdapter.disable();
					 rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_close));
					 break;
				case BluetoothAdapter.STATE_OFF :
					 bluetoothAdapter.enable();
					 rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_open));
					 break;
				case BluetoothAdapter.STATE_TURNING_OFF :
					 bluetoothAdapter.enable();
					 rb_buletooth.setImageDrawable(getResources().getDrawable(R.drawable.one_bluetooth_open));
					 break;
			
				}
			}
		});
	  //移动数据点击事件			
         rb_mobiledata.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (getMobileDataStatus())  
	        {  
	            setMobileDataStatus(false);  
	            rb_mobiledata.setImageDrawable(getResources().getDrawable(R.drawable.one_mobiledata_close));
	        }  
	        else  
	        {  
	            setMobileDataStatus(true);  
	            rb_mobiledata.setImageDrawable(getResources().getDrawable(R.drawable.one_mobiledata_open));  
	        }  
		}});
    //铃音模式点击
         rb_ringmodel.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT){
 					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
 					
 					rb_ringmodel.setImageDrawable(getResources().getDrawable(R.drawable.one_vibrate));
 				       
 				    }
 				else {
 					audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
 					rb_ringmodel.setImageDrawable(getResources().getDrawable(R.drawable.one_silence));
 				     }

 				}
 				});	
        //GPS点击事件
         rb_gps.setOnClickListener(new OnClickListener() {
  			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, 0);// 设置完成后返回到原来的界面
 			}
            });	
         //亮度点击事件
         rb_bright.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setBrightStatus();
				}		
		});
	}
         	
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		context.unregisterReceiver(gpsChangedBroadcastReceiver); 
		context.unregisterReceiver(wifiChangedBroadcastReceiver);  
		context.unregisterReceiver(blueToothChangedBroadcastReceiver);
		context.unregisterReceiver(mobileDataChangedBroadcastReceiver);
		context.unregisterReceiver(ringModelChangedBroadcastReceiver);
		brightObserver.stopObserver();
	}
	}
