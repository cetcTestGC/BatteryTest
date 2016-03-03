package com.example.RootManager;

import java.io.DataOutputStream;

import android.app.Activity;
import android.util.Log;

public class SystemManager extends Activity {
	
	public static boolean RootCommand(String command) {
		
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("DeBug", "Root Ree" + e.getMessage());
			return false;
		}finally{
			try {
				if(os != null)
					os.close();
				process.destroy();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		Log.d("DeBug", "Root SUC");
		return true;
		
		
	}

}
