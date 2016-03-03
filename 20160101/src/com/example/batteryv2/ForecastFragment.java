package com.example.batteryv2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.PrivateCredentialPermission;

import com.example.BasicInfo.CpuInfoUtil;
import com.example.BasicInfo.MemoryInfoUtil;
import com.example.batteryv2.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by Jamie on 15-04-20.
 */
public class ForecastFragment extends Fragment {

	private View bView;
	private Context context;

	private TextView cTextView;
	private ListView cListView;
	private TextView mTextView;
	private ListView mListView;
	private TextView sTextView;

	private TextView accTextView;
	private TextView accTextViewValue;
	private TextView lightTextView;
	private TextView lightTextViewValue;
	private TextView magneticTextView;
	private TextView magneticTextViewValue;
	private TextView orientationTextView;
	private TextView orientationTextViewValue;
//	private ProgressDialog dialog;

	private MySensorListener sensorListener;
	private SensorManager sm;
	private Timer timer;

	private List<String> cpuInfo;
	private SimpleAdapter cpuSimpleAdapter;
	private List<HashMap<String, Object>> cpuListItem = new ArrayList<HashMap<String, Object>>();

	private List<String> memoryInfo;
	private SimpleAdapter memSimpleAdapter;
	private List<HashMap<String, Object>> memListItem = new ArrayList<HashMap<String, Object>>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bView = inflater.inflate(R.layout.fragment2, null);

		cTextView = (TextView) bView.findViewById(R.id.txt_content_cpu);
		cListView = (ListView) bView.findViewById(R.id.cpuLv);
		mTextView = (TextView) bView.findViewById(R.id.txt_content_mem);
		mListView = (ListView) bView.findViewById(R.id.memLv);

		initCpuWidget(); // 填充cpu信息
		initMemWidget(context); // 填充内存信息

		sensorListener = new MySensorListener();
		sm = (SensorManager) getActivity().getSystemService("sensor");
		accTextView = (TextView) bView.findViewById(R.id.accTextView);
		lightTextView = (TextView) bView.findViewById(R.id.lightTextView);
		magneticTextView = (TextView) bView.findViewById(R.id.magneticTextView);
		orientationTextView = (TextView) bView
				.findViewById(R.id.orientationTextView);

		accTextViewValue = (TextView) bView.findViewById(R.id.accTextViewValue);
		lightTextViewValue = (TextView) bView
				.findViewById(R.id.lightTextViewValue);
		magneticTextViewValue = (TextView) bView
				.findViewById(R.id.magneticTextViewValue);
		orientationTextViewValue = (TextView) bView
				.findViewById(R.id.orientationTextViewValue);

		timer = new Timer();
		timer.schedule(new MyTask(), 2000, 600);

		return bView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try{
					Thread.sleep(800);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
				dialog.dismiss();
				}
			}
		});
		thread.start();
	}
	
	public void initCpuWidget() {
		// 获得cpu信息并将其使用listview显示
		cpuInfo = CpuInfoUtil.getCpuInfo();
		this.setCpuValue(cpuInfo, cpuListItem);
		cpuSimpleAdapter = new SimpleAdapter(context, cpuListItem,
				R.layout.cpu_item, new String[] { "keyText", "keyValue" },
				new int[] { R.id.keyText, R.id.keyValue });

		cListView.setAdapter(cpuSimpleAdapter);
	}

	public void initMemWidget(Context context) {
		memoryInfo = MemoryInfoUtil.getMemInfo(context);
		this.setMemoryValue(memoryInfo, memListItem);
		memSimpleAdapter = new SimpleAdapter(context, memListItem,
				R.layout.mem_item,
				new String[] { "keyTextMem", "keyValueMem" }, new int[] {
						R.id.keyTextMem, R.id.keyValueMem });

		mListView.setAdapter(memSimpleAdapter);
	}

	public void setCpuValue(List<String> info,
			List<HashMap<String, Object>> listItem) {
		Iterator it = info.iterator();
		while (it.hasNext()) {
			String valueString = (String) it.next();
			if (valueString.isEmpty())
				continue;
			String[] cVlaueString = valueString.split(":"); //
			// 将info中的数据按照':'分割，分别显示
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("keyText", cVlaueString[0]);
			hm.put("keyValue", cVlaueString[1]);
			listItem.add(hm);
		}
	}

	public void setMemoryValue(List<String> info,
			List<HashMap<String, Object>> listItem) {
		Iterator it = info.iterator();
		while (it.hasNext()) {
			String valueString = (String) it.next();
			if (valueString.isEmpty())
				continue;
			String[] cVlaueString = valueString.split(":"); //
			// 将info中的数据按照':'分割，分别显示
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("keyTextMem", cVlaueString[0]);
			hm.put("keyValueMem", cVlaueString[1]);
			listItem.add(hm);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// bindService();

		// 为系统的磁场传感器注册监听器
		sm.registerListener(sensorListener,
				sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
		// 为系统的方向传感器注册监听器
		sm.registerListener(sensorListener,
				sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		// 为系统的光传感器注册监听器
		sm.registerListener(sensorListener,
				sm.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_NORMAL);
		// 为系统的加速度传感器注册监听器
		sm.registerListener(sensorListener,
				sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 程序暂停时取消注册传感器监听器
		sm.unregisterListener(sensorListener);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		timer.cancel();
//		ProgressDialog.dismiss();
	}

	public final class MySensorListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float[] values = event.values;
			DecimalFormat accFormat = new DecimalFormat("0.00");

			// 得到方向的值

			StringBuffer sb = null;
			switch (event.sensor.getType()) {
			// 加速度传感器
			case Sensor.TYPE_ACCELEROMETER:
				sb = new StringBuffer();
				sb.append("x的值:  " + accFormat.format(values[0]) + "\n");
				sb.append("y的值:  " + accFormat.format(values[1]) + "\n");
				sb.append("z的值:  " + accFormat.format(values[2]));

				accTextView.setText("accelerometer:");
				accTextViewValue.setText(sb.toString());
				break;
			// 方向传感器
			case Sensor.TYPE_ORIENTATION:
				sb = new StringBuffer();
				sb.append("绕Z轴转过的角度:  " + accFormat.format(values[0]) + "\n");
				sb.append("绕X轴转过的角度:  " + accFormat.format(values[1]) + "\n");
				sb.append("绕Y轴转过的角度:  " + accFormat.format(values[2]));
				orientationTextView.setText("orientation:");
				orientationTextViewValue.setText(sb.toString());
				break;
			// 磁场传感器
			case Sensor.TYPE_MAGNETIC_FIELD:
				sb = new StringBuffer();
				sb.append("X方向上的磁场分量:  " + accFormat.format(values[0]) + "\n");
				sb.append("Y方向上的磁场分量:  " + accFormat.format(values[1]) + "\n");
				sb.append("Z方向上的磁场分量:  " + accFormat.format(values[2]));
				magneticTextView.setText("magnetic:");
				magneticTextViewValue.setText(sb.toString());
				break;
			// 光传感器
			case Sensor.TYPE_LIGHT:
				sb = new StringBuffer();
				sb.append(values[0]);
				lightTextView.setText("light Strength:");
				lightTextViewValue.setText(sb.toString());
				break;
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new UpdateAsyncTask().execute("task");
		}

	}

	private class UpdateAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arguments) {
			return null;
		}

		@Override
		protected void onPostExecute(String string1) {
			cpuListItem.clear();
			memListItem.clear();

			// System.out.println("-------------------------UpdateAsyncTask--------------------------");
			cpuInfo = CpuInfoUtil.getCpuInfo();
			memoryInfo = MemoryInfoUtil.getMemInfo(context);

			setCpuValue(cpuInfo, cpuListItem);
			setMemoryValue(memoryInfo, memListItem);

			System.out.println(memoryInfo);

			ListView cpuListView = (ListView) getView()
					.findViewById(R.id.cpuLv);
			ListView memListView = (ListView) getView()
					.findViewById(R.id.memLv);

			((BaseAdapter) cpuListView.getAdapter()).notifyDataSetChanged();
			((BaseAdapter) memListView.getAdapter()).notifyDataSetChanged();
		}
	}
}
