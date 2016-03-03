package com.example.BasicInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;

public class MemoryInfoUtil {

	private static Context context;

	private static List<String> memoryInfo = new ArrayList(); // 存放内存信息的list

	static {
		// 将总内存的值存入meminfo
		String totalMem = "Total_Memory:" + getTotalMemory();
		memoryInfo.add(totalMem);
	}

	public static List<String> getMemInfo(Context context) {
		MemoryInfoUtil.context = context;
		List<String> memInfoToReturn = new ArrayList();
		memInfoToReturn.addAll(memoryInfo);

		// 将可用内存值存入info
		String availMem = "Available_Memory:" + getAvailMemory();
		memInfoToReturn.add(availMem);
		return memInfoToReturn;
	}

	// 读取总内存的值
	private static String getTotalMemory() {
		String mem = "/proc/meminfo";// 系统内存信息文件
		String valueString;
		long initial_memory = 0;
		double result1;
		String[] arrayOfString = { "", "" };
		String result = null;

		try {
			FileReader localFileReader = new FileReader(mem);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			valueString = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = valueString.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]);// 得到总内存的值，单位为B
			result1 = initial_memory / 1024 / 1024;
			result = result1 + " GB";

			localBufferedReader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获取当前可用内存大小
	private static String getAvailMemory() {
		ActivityManager am = (ActivityManager)context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}
}
