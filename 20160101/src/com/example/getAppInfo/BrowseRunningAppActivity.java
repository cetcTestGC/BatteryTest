package com.example.getAppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.example.batteryv2.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class BrowseRunningAppActivity extends Activity {

	private static String TAG = "BrowseRunningAppActivity";

	private ListView listview = null;

	private List<RunningAppInfo> mlistAppInfo = null;
    private TextView tvInfo = null ;
    
	private PackageManager pm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_app_list);

		listview = (ListView) findViewById(R.id.listviewApp);
		tvInfo = (TextView)findViewById(R.id.tvInfo) ;
		pm= this.getPackageManager();//��ð�������
		mlistAppInfo = new ArrayList<RunningAppInfo>();

		// ��ѯĳһ�ض����̵�����Ӧ�ó���
		Intent intent = getIntent();
		//�Ƿ��ѯĳһ�ض�pid��Ӧ�ó���
		int pid = intent.getIntExtra("EXTRA_PROCESS_ID", -1);
		
		if ( pid != -1) {
			//ĳһ�ض������������������е�Ӧ�ó���
			mlistAppInfo =querySpecailPIDRunningAppInfo(intent, pid);
		}
		else{
			// ��ѯ�����������е�Ӧ�ó�����Ϣ�� �����������ڵĽ���id�ͽ�����
			tvInfo.setText("�����������е�Ӧ�ó�����-------");
		    mlistAppInfo = queryAllRunningAppInfo(); 
		}
		BrowseRunningAppAdapter browseAppAdapter = new BrowseRunningAppAdapter(this, mlistAppInfo);
		listview.setAdapter(browseAppAdapter);
		//�����¼�����ת����ӦӦ�ó��� 
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String packageName=mlistAppInfo.get(arg2).getPkgName();
				//���ݰ�����Ӧ�ó��� 
				Intent intent=new Intent(); 
				intent =pm.getLaunchIntentForPackage(packageName); 
				startActivity(intent); 
//				finish();//��Ӧ�ó���֮��ע����Ӧ�ó���
			}
		});
		//�����¼��������Ƿ�ر�Ӧ�ã���ȷ������ת�������йر�Ӧ��
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final String killPackageName=mlistAppInfo.get(arg2).getPkgName();
				new AlertDialog.Builder(BrowseRunningAppActivity.this).setTitle("Are you sure close").setPositiveButton("sure", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Uri packageURI = Uri.parse("package:" + killPackageName);
				           Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);  
				           startActivity(intent);
					}
				}).setNegativeButton("cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				}).show();
				return false;
			}
		});
	}
	// ��ѯ�����������е�Ӧ�ó�����Ϣ�� �����������ڵĽ���id�ͽ�����
	// �����ֱ�ӻ�ȡ��ϵͳ�ﰲװ������Ӧ�ó���Ȼ����ݱ���pkgname���˻�ȡ�����������е�Ӧ�ó���
	private List<RunningAppInfo> queryAllRunningAppInfo() {
		pm = this.getPackageManager();
		// ��ѯ�����Ѿ���װ��Ӧ�ó���
		List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(pm));// ����

		// ���������������еİ��� �Լ������ڵĽ�����Ϣ
		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// ͨ������ActivityManager��getRunningAppProcesses()�������ϵͳ�������������еĽ���
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			int pid = appProcess.pid; // pid
			String processName = appProcess.processName; // ������
			Log.i(TAG, "processName: " + processName + "  pid: " + pid);

			String[] pkgNameList = appProcess.pkgList; // ��������ڸý����������Ӧ�ó����

			// �������Ӧ�ó���İ���
			for (int i = 0; i < pkgNameList.length; i++) {
				String pkgName = pkgNameList[i];
				Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
				// ������map������
				pgkProcessAppMap.put(pkgName, appProcess);
			}
		}
		// ���������������е�Ӧ�ó�����Ϣ
		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // ������˲鵽��AppInfo

		for (ApplicationInfo app : listAppcations) {
			// ����ð������� ����һ��RunningAppInfo����
			if (pgkProcessAppMap.containsKey(app.packageName)) {
				// ��ø�packageName�� pid �� processName
				int pid = pgkProcessAppMap.get(app.packageName).pid;
				String processName = pgkProcessAppMap.get(app.packageName).processName;
				runningAppInfos.add(getAppInfo(app, pid, processName));
			}
		}

		return runningAppInfos;

	}
	// ĳһ�ض������������������е�Ӧ�ó���
	private List<RunningAppInfo> querySpecailPIDRunningAppInfo(Intent intent , int pid) {


		String[] pkgNameList = intent.getStringArrayExtra("EXTRA_PKGNAMELIST");
		String processName = intent.getStringExtra("EXTRA_PROCESS_NAME");
		
		//update ui
		tvInfo.setText("����idΪ"+pid +" ���е�Ӧ�ó�����  :  "+pkgNameList.length);
				
		pm = this.getPackageManager();
	
		// ���������������е�Ӧ�ó�����Ϣ
		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // ������˲鵽��AppInfo

		for(int i = 0 ; i<pkgNameList.length ;i++){
		   //���ݰ�����ѯ�ض���ApplicationInfo����
		   ApplicationInfo appInfo;
		  try {
			appInfo = pm.getApplicationInfo(pkgNameList[i], 0);
            runningAppInfos.add(getAppInfo(appInfo, pid, processName));
		  }
		  catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }  // 0����û���κα��;
		}
		return runningAppInfos ;
	}
	
	
	// ����һ��RunningAppInfo���� ������ֵ
	private RunningAppInfo getAppInfo(ApplicationInfo app, int pid, String processName) {
		RunningAppInfo appInfo = new RunningAppInfo();
		appInfo.setAppLabel((String) app.loadLabel(pm));
		appInfo.setAppIcon(app.loadIcon(pm));
		appInfo.setPkgName(app.packageName);

		appInfo.setPid(pid);
		appInfo.setProcessName(processName);
		appInfo.setUpFlow(TrafficStats.getUidTxBytes(app.uid));
		appInfo.setDownFlow(TrafficStats.getUidRxBytes(app.uid));
		return appInfo;
	}

	
}