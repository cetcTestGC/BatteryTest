package com.example.getAppInfo;

import java.util.Comparator;

public class AppInfoSort implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		RunningAppInfo app1=(RunningAppInfo) arg0;
		RunningAppInfo app2=(RunningAppInfo) arg1;
		if(app1.getStatistic()>app2.getStatistic())
			return -1;
		else if(app1.getStatistic()==app2.getStatistic())
			return 0;
		else
			return 1;
	}

}
