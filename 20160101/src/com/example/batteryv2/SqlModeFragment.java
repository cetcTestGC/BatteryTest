package com.example.batteryv2;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.example.SavePowerMode.SavePowerModeActivity;
import com.example.batteryv2.R;
import com.example.JavaEMail.EmailSender;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.BootService.PollingUtils;
import com.example.DBHelper.BatteryInfo;
import com.example.DBHelper.DBService;
/**
 * Created by admin on 13-11-23.
 */
public class SqlModeFragment extends Fragment {
	
	private Context context;
	//数据库按键
	private LinearLayout sqlSwitchLayout;
	//发送邮件按键
	private LinearLayout emaiLayout;
	//数据库游标
	private SQLiteDatabase db;
	//获取SQ模式设置
	private SharedPreferences sqlModeSharePre;
	private TextView sqlSwitchTxtTextView;
	//DBHelper
	private DBService dbHelper;
	private BatteryInfo batteryInfo;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sal_mode_activity, null);
        sqlSwitchLayout = (LinearLayout) view.findViewById(R.id.sql_mode_switch_layout);
        sqlModeSharePre = context.getSharedPreferences("sqlMode", context.MODE_WORLD_READABLE);
        sqlSwitchTxtTextView = (TextView) view.findViewById(R.id.sql_mode_switch_txt);
        emaiLayout = (LinearLayout) view.findViewById(R.id.send_email_switch_layout);
        batteryInfo = new BatteryInfo();
        
        
        if(sqlModeSharePre.getString("sqlswitchtxtmode", "").equals("已打开")){
        	sqlSwitchTxtTextView.setText("已打开");
        	sqlSwitchTxtTextView.setTextColor(0xff70bd34);       	
        }
        
        sqlSwitchLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//创建数据库 
				if (!sqlModeSharePre.contains("sqldb")) {
					// 打开创建 BatteryInfo.BATTY_TABLE
					db = context.openOrCreateDatabase("battery.db", Context.MODE_WORLD_READABLE, null);
					db.execSQL("DROP TABLE IF EXISTS " + BatteryInfo.BATTERY_TABLE);
					// 创建 BatteryInfo.BATTY_TABLE
					db.execSQL(BatteryInfo.CREATE_BATTERY_INFO_TABLE);
					
					//获取低电量开启值以及开启模式
					SharedPreferences.Editor editor = sqlModeSharePre.edit();
					editor.putString("sqldb", "true");
					editor.commit();
					db.close();
				}
				
				if(sqlSwitchTxtTextView.getText().toString().equals("已关闭")){
					sqlSwitchTxtTextView.setText("已打开");
					//荧光绿色
					sqlSwitchTxtTextView.setTextColor(0xff70bd34);
					SharedPreferences.Editor editor = sqlModeSharePre.edit();
					editor.putString("sqlswitchtxtmode", "已打开");
					editor.commit();
					db = context.openOrCreateDatabase("battery.db", Context.MODE_WORLD_READABLE, null);
					// 创建 BatteryInfo.BATTY_TABLE
					db.execSQL(BatteryInfo.DROP_BATTY_TABLE);
					db.execSQL(BatteryInfo.CREATE_BATTERY_INFO_TABLE);
					db.close();
					//启动轮询机制
					PollingUtils.startPollingService(context, 5, DBService.class, DBService.DBService_Path);					
				}else {
					sqlSwitchTxtTextView.setText("已关闭");	
					//关闭颜色
					sqlSwitchTxtTextView.setTextColor(0xff586272);
					SharedPreferences.Editor editor = sqlModeSharePre.edit();
					editor.putString("sqlswitchtxtmode", "已关闭");
					editor.commit();
					//关闭轮询机制
					PollingUtils.stopPollingService(context, DBService.class, DBService.DBService_Path);
					//db.close();
				}
			}
		});
        
        emaiLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

            		@Override
            		public void run() {
            			try {
            				EmailSender sender = new EmailSender();
            				//设置服务器地址和端口，网上搜的到
            				sender.setProperties("smtp.sina.com", "25");
            				//分别设置发件人，邮件标题和文本内容
            				sender.setMessage("cetctestbattery@sina.com", "battery", "Battery APP DB !!");
            				//设置收件人
            				sender.setReceiver(new String[]{"544355066@qq.com"});
            				//添加附件
            				sender.addAttachment();
            				//发送邮件
            				sender.sendEmail("smtp.sina.com", "cetctestbattery@sina.com", "cetctest2015");
            			} catch (AddressException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			} catch (MessagingException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            	}).start();
				
			}
		});
        
        
        return view;
    }
    
    Runnable sendEmailRun = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	};
    
    
    /**
	 * onAttach() 方法在 Fragment 里面最先调用，其次是onCreate()
	 */

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}
	
}
