package com.example.getAppInfo;

import java.util.List;

import com.example.batteryv2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


//自定义适配器类，提供给listView的自定义view
public class BrowseRunningAppAdapter extends BaseAdapter {
	
	private List<RunningAppInfo> mlistAppInfo = null;
	
	//界面布局文件xml的findviewbyid
	LayoutInflater infater = null;
  
	public BrowseRunningAppAdapter(Context context,  List<RunningAppInfo> apps) {
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistAppInfo = apps ;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("size" + mlistAppInfo.size());
		return mlistAppInfo.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistAppInfo.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		//System.out.println("getView at " + position);
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.browse_app_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else{
			view = convertview ;
			holder = (ViewHolder) convertview.getTag() ;
		}
		RunningAppInfo appInfo = (RunningAppInfo) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getAppIcon());
		holder.tvAppLabel.setText(appInfo.getAppLabel());
//		holder.tvPkgName.setText(appInfo.getPkgName());
//		holder.tvProcessId.setText(appInfo.getPid()+"") ;
//		holder.tvProcessName.setText(appInfo.getProcessName()) ;
		
		//若流量超过1024b就记为kb
//		if(appInfo.getUpFlow()>=1024)
			holder.tvAppUpFlow.setText(appInfo.getUpFlow()/1024+"KB");
//		else
//			holder.tvAppUpFlow.setText(appInfo.getUpFlow()+"bytes");
//		if(appInfo.getDownFlow()>=1024)
			holder.tvAppDownFlow.setText(appInfo.getDownFlow()/1024+"KB");
//		else
//			holder.tvAppDownFlow.setText(appInfo.getDownFlow()+"bytes");
		
		return view;
	}

	class ViewHolder {
		ImageView appIcon; //应用图标
		TextView tvAppLabel; //应用名
//		TextView tvPkgName; //应用包名
//      TextView tvProcessId ; //应用进程id
//		TextView tvProcessName ; //应用进程名
		TextView tvAppUpFlow;//应用上传流量
		TextView tvAppDownFlow;//应用下载流量
      
      
		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.fragment3_imgApp);
			this.tvAppLabel = (TextView) view.findViewById(R.id.fragment3_tvAppLabel);
//			this.tvPkgName = (TextView) view.findViewById(R.id.tvPkgName);
//			this.tvProcessId = (TextView) view.findViewById(R.id.tvProcessId);
//			this.tvProcessName = (TextView) view.findViewById(R.id.tvProcessName);
			this.tvAppUpFlow=(TextView)view.findViewById(R.id.fragment3_tvuplabel);
			this.tvAppDownFlow=(TextView)view.findViewById(R.id.fragment3_tvdownlabel);
		}
	}
}