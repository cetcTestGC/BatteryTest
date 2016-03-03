package com.example.SaveData;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveData {
	/**
	 * 保存程序第一次运行所需数据
	 * 例如 正常模式 
	 */
	
	//正常模式保存
	private static SharedPreferences NormodeSharedPre;
	
	
	
	/**
	 * 初始化NormodeSharePre
	 * @param context
	 */
	
	public static void initShareMode(SharedPreferences share) {
		NormodeSharedPre = share;
	}
	
	/**
	 * 保存手机正常模式
	 * @param context
	 */
	public static void saveNorCpuMode(Context context) {
		ProcessBuilder readOutCurrentGovernor;
        String currentGovernor = "";
        try {
            String[] currentGovernorFile = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"};
            readOutCurrentGovernor = new ProcessBuilder(currentGovernorFile);
            Process readProcess4 = readOutCurrentGovernor.start();
            InputStream readInCurrentGovernor = readProcess4.getInputStream();
            byte[] read4 = new byte[1024];
            while (readInCurrentGovernor.read(read4) != -1) {
                currentGovernor = currentGovernor + new String(read4);
            }
            readInCurrentGovernor.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
        //第一次存储正常模式
        if(!NormodeSharedPre.contains("NormalMode")){
        	SharedPreferences.Editor editor = NormodeSharedPre.edit();
        	editor.putString("NormalMode", currentGovernor);
        	editor.putString("Number", "Two");
        	editor.commit();
        }
		
	}
	
	public static SharedPreferences getNorModeSharePre() {
		return NormodeSharedPre;
	}
	

}
