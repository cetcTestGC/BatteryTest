package com.example.batteryv2;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.getAppInfo.AppInfoSort;
import com.example.getAppInfo.BrowseRunningAppAdapter;
import com.example.getAppInfo.RunningAppInfo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created by daydreamren on 7/11/14.
 * This fragment implement the battery consumption statistic
 * 1>get app icon, name , pkgname 
 * 2>can stop the running app
 */
public class StatisticFragment extends Fragment {
	private static String TAG = "BrowseRunningAppActivity";

	private ListView listview = null;

	private List<RunningAppInfo> mlistAppInfo = null;
    private TextView tvInfo = null ;
    
	private PackageManager pm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //跳转到显示进程信息界面
    	View view = inflater.inflate(R.layout.browse_app_list, null);
		listview = (ListView) view.findViewById(R.id.listviewApp);
		tvInfo = (TextView)view.findViewById(R.id.tvInfo) ;
		pm= getActivity().getPackageManager();//获得包管理器
	      //获得ActivityManager服务的对象
        //fragment里面获取任何id或service都要有前缀
		mlistAppInfo = new ArrayList<RunningAppInfo>();//动态数组
			// 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
		tvInfo.setText("所有正在运行的应用程序有-------");
		mlistAppInfo = queryAllRunningAppInfo(); 
		BrowseRunningAppAdapter browseAppAdapter = new BrowseRunningAppAdapter(getActivity(), mlistAppInfo);
		listview.setAdapter(browseAppAdapter);//连接数据与ListView的适配器,实现操作分离
		//长按事件，弹出是否关闭应用，若确定则跳转到设置中关闭应用
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final String killPackageName=mlistAppInfo.get(arg2).getPkgName();
				new AlertDialog.Builder(getActivity()).setTitle("Are you sure close").setPositiveButton("sure", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Uri packageURI = Uri.parse("package:" + killPackageName);//通用资源标志符 Universal Resource Identifier
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
		return view;
    }
 // 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
 	// 这儿我直接获取了系统里安装的所有应用程序，然后根据报名pkgname过滤获取所有真正运行的应用程序
 	private List<RunningAppInfo> queryAllRunningAppInfo() {
 		pm = getActivity().getPackageManager();
 		// 查询所有已经安装的应用程序
 		List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
// 		Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(pm));// 排序

 		// 保存所有正在运行的包名 以及它所在的进程信息
 		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

 		ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
 		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
 		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
 				.getRunningAppProcesses();

 		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
 			int pid = appProcess.pid; // pid
 			String processName = appProcess.processName; // 进程名
 			Log.i(TAG, "processName: " + processName + "  pid: " + pid);

 			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包

 			// 输出所有应用程序的包名
 			for (int i = 0; i < pkgNameList.length; i++) {
 				String pkgName = pkgNameList[i];
 				Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
 				// 加入至map对象里
 				pgkProcessAppMap.put(pkgName, appProcess);
 			}
 		}
 		// 保存所有正在运行的应用程序信息
 		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // 保存过滤查到的AppInfo

 		for (ApplicationInfo app : listAppcations) {
 			// 如果该包名存在 则构造一个RunningAppInfo对象
 			//只对非系统应用建立对象
 			if((app.flags&ApplicationInfo.FLAG_SYSTEM)<=0)
 			{
 				if (pgkProcessAppMap.containsKey(app.packageName)) {
 				// 获得该packageName的 pid 和 processName
 				int pid = pgkProcessAppMap.get(app.packageName).pid;
 				String processName = pgkProcessAppMap.get(app.packageName).processName;
 				runningAppInfos.add(getAppInfo(app, pid, processName));
 			}
 			}
 		}
 		AppInfoSort appinfosort=new AppInfoSort();
// 		按耗电比排序
 		Collections.sort(runningAppInfos, appinfosort);
 		return runningAppInfos;

 	}
 	
 	
 	// 构造一个RunningAppInfo对象 ，并赋值
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
