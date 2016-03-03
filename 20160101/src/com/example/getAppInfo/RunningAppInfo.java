package com.example.getAppInfo;



//Model类 ，用来存储应用程序信息
import android.graphics.drawable.Drawable;



//Model类 ，用来存储应用程序信息
public class RunningAppInfo {

	private String appLabel;    //应用程序标签
	private Drawable appIcon ;  //应用程序图像
	private String pkgName ;    //应用程序所对应的包名
	
	private int pid ;  //该应用程序所在的进程号
	private String processName ;  // 该应用程序所在的进程名
	private long upFlow;//该应用程序上传的数据量
	private long downFlow;//该应用程序下载的数据量
	public RunningAppInfo(){}
	
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appName) {
		this.appLabel = appName;
	}
	public Drawable getAppIcon() {

			return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
//		if(appIcon.getIntrinsicHeight()>100||appIcon.getIntrinsicWidth()>100)
//		{
//			Log.i("sizechanged", "!!!!!!!");
//			int width=appIcon.getIntrinsicWidth();
//			int height=appIcon.getIntrinsicHeight();
//			System.out.println("w"+width+"h"+height);
//			//取drawable的颜色格式
//			Bitmap.Config config=appIcon.getOpacity()!=PixelFormat.OPAQUE? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
//			//建立对应bitmap
//			Bitmap oldBitmap=Bitmap.createBitmap(width, height, config);
//			//建立对应bitmap的画布
//			Canvas canvas=new Canvas(oldBitmap);
//			appIcon.setBounds(0, 0, width, height);
//			appIcon.draw(canvas);//把drawable内容画到画布上
//			//计算缩放比例
//			Matrix matrix=new Matrix();
//			float scaleWidth=(float) (50.0/width);
//			float scaleHeight=(float) (50.0/height);
//			System.out.println("sw"+scaleWidth+"sh"+scaleHeight);
//			matrix.postScale(scaleWidth, scaleHeight);
//			
//			Bitmap newBitmap=Bitmap.createBitmap(oldBitmap,0,0,width,height,matrix,true);
//			this.appIcon=new BitmapDrawable(newBitmap);
//		}
//		else
			this.appIcon = appIcon;
	}
	public String getPkgName(){
		return pkgName ;
	}
	public void setPkgName(String pkgName){
		this.pkgName=pkgName ;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public long getUpFlow() {
		return upFlow;
	}

	public void setUpFlow(long upFlow) {
		if(upFlow==-1)
			upFlow=0;
		this.upFlow = upFlow;
	}

	public long getDownFlow() {
		return downFlow;
	}
	
	public void setDownFlow(long downFlow) {
		if(downFlow==-1)
			downFlow=0;
		this.downFlow = downFlow;
	}
	//统计排序信息
	public long getStatistic() {
		return upFlow+downFlow;
	}

	
	
}

