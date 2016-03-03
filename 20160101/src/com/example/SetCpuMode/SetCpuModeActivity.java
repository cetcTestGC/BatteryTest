package com.example.SetCpuMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.example.SaveData.SaveData;
import com.example.SavePowerMode.SavePowerModeActivity;
import com.example.batteryv2.R;
import com.example.batteryv2.MainActivity;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SetCpuModeActivity extends Activity {
	
	
	static Context context;
	String[] availableFrequenciesArray;
	String selectedGovernor, selectedMaximumFrequency, selectedMinimumFrequency;
	
	Button applySelectedCPUFrequencyButton;
	
	
	Spinner maxFreqSpinner, minFreqSpinner, governorSpinner;
	
	
	ArrayList<String> availableFrequencies, availableFrequenciesForSpinner,
	            availableGovernorsForSpinner;
	
	int frequency;
	
	String  governor, currentCPUFrequency;
	
	
	TextView currentGovernorTV;
	
	
	
	private CpuControlMode cpuControlMode;
	
	//存储数据
	private SharedPreferences mySharedPreferences;
	
	//三种模式监控
	private LinearLayout norCpuModeLayout;
	private LinearLayout bestCpuModeLayout;
	private LinearLayout lowCpuModeLayout;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_cpu_mode);
		_context();
		
		//获取正常模式
		mySharedPreferences = SaveData.getNorModeSharePre();
		
		norCpuModeLayout = (LinearLayout) findViewById(R.id.sel_cpu_nor_mode_linearlayout);
		bestCpuModeLayout = (LinearLayout) findViewById(R.id.sel_cpu_best_mode_linearlayout);
		lowCpuModeLayout = (LinearLayout) findViewById(R.id.sel_cpu_low_mode_linearlayout);

		currentGovernorTV = (TextView) findViewById(R.id.currentGovernor);
        //governorSpinner = (Spinner) findViewById(R.id.governorSpinner);
        
		try {
			_readAvailableGovernorsForSpinner();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		_readCurrentGovernor();
		
		/*  正常模式监听*/
		
		norCpuModeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SetCpuModeActivity.this);    
		        builder.setTitle("提示框");  
		        builder.setMessage("是否打开此模式");  
		        builder.setPositiveButton("取消",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                        dialog.dismiss();

		                    }  
		                });  
		        builder.setNeutralButton("开启",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                    	selectedGovernor = mySharedPreferences.getString("NormalMode", "");
		                    	applyCpuMode();
		                    }  
		                });    
		        builder.show();  
			}
		});
		
		/* 最佳模式监听 */
		
		bestCpuModeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SetCpuModeActivity.this);    
		        builder.setTitle("提示框");  
		        builder.setMessage("是否打开此模式");  
		        builder.setPositiveButton("取消",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                        dialog.dismiss();

		                    }  
		                });  
		        builder.setNeutralButton("开启",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                    	selectedGovernor = CpuControlMode.Perfromance;
		                    	applyCpuMode();
		                    }  
		                });    
		        builder.show();
			}
		});
		
		/* 低功耗模式监听 */
		
		lowCpuModeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SetCpuModeActivity.this);    
		        builder.setTitle("提示框");  
		        builder.setMessage("是否打开此模式");  
		        builder.setPositiveButton("取消",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                        dialog.dismiss();

		                    }  
		                });  
		        builder.setNeutralButton("开启",  
		                new DialogInterface.OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int whichButton) {  
		                    	selectedGovernor = CpuControlMode.PowerSave;
		                    	applyCpuMode();
		                    }  
		                });    
		        builder.show();
			}
		});
		
		
	}
	

	
	
	
	public void applyCpuMode() {
		try {
        	
        	//设置CPU频率
            CommandCapture setGovernor = new CommandCapture(0,
                    "echo \"" + selectedGovernor +
                            "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
            RootTools.getShell(true).add(setGovernor);
            //RootDeniedException | IOException rde            
        }catch (RootDeniedException rde) {
            rde.printStackTrace();
            Toast.makeText(context, rde.getMessage(), Toast.LENGTH_LONG).show();
        }catch (TimeoutException te) {
            te.printStackTrace();
            Toast.makeText(context, te.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		_readCurrentGovernor();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	 public Context _context() {
	    	
	    	
	        context = getApplicationContext();

	        return context;
	    }
	
	    
	    
	    
	    public void _readAvailableGovernorsForSpinner() throws Exception {
	        File scalingAvailableGovernorsFile = new File(
	                "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors");
	        availableGovernorsForSpinner = new ArrayList<String>();
	        Scanner scanner4 = new Scanner(scalingAvailableGovernorsFile);
	        while (scanner4.hasNext()) {
	            governor = scanner4.next();
	            availableGovernorsForSpinner.add(governor);
	            
	        }
	        //_selectGovernorSpinner(availableGovernorsForSpinner);
	    }
	    
	    
	   
	   /* @SuppressWarnings("unused")
		private String _selectGovernorSpinner(final List<String> availableGovernorsForSpinner) {
	        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, R.layout.row_layout,
	                availableGovernorsForSpinner);
	        governorSpinner.setAdapter(adapter4);
	        governorSpinner.setOnItemSelectedListener(
	                new AdapterView.OnItemSelectedListener() {

	                    @Override
	                    public void onItemSelected(AdapterView<?> parent, View view, int position,
	                                               long id) {
	                        selectedGovernor = availableGovernorsForSpinner.get(position);
	                    }

	                    @Override
	                    public void onNothingSelected(AdapterView<?> parent) {

	                    }
	                }
	        );

	        return selectedGovernor;
	    }*/
	    
	    
	    
	    public void _setCurrentGovernorTextLabel(String currentGovernor) {
	    	currentGovernorTV.setText(charToStr(currentGovernor));
	    }
	    
	    
	    
	    /**
	     * CPU模式中英转换
	     * @return
	     */
	    public String charToStr(String str) {
	    	
	    	if (str.substring(0, 8).equals(cpuControlMode.Pegasusq) ) {
	    		return cpuControlMode.getPegasusq();
			}
	    	else if (str.substring(0, 11).equals(cpuControlMode.Perfromance) ) {
	    		return cpuControlMode.getPerfromance();
			}
	    	else if (str.substring(0, 9).equals(cpuControlMode.PowerSave)) {
	    		return cpuControlMode.getPowersave();
			}
	    	else {
				return cpuControlMode.getNormal();
			}
		}
	    
	    
	    public String _readCurrentGovernor() {
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
	        _setCurrentGovernorTextLabel(currentGovernor);
	        return currentGovernor;
	    }
	    
	    
	    
	    public void _applySelectedGovernor(View view) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Hey you!");
	        builder.setMessage("Are you sure? Do you want to set your selected governor?");
	        builder.setPositiveButton("Yep, let it go!", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                try {
	                	
	                	//璁剧疆CPU棰戠巼妯″紡
	                    CommandCapture setGovernor = new CommandCapture(0,
	                            "echo \"" + selectedGovernor +
	                                    "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
	                    RootTools.getShell(true).add(setGovernor);
	                    //RootDeniedException | IOException rde            
	                }catch (RootDeniedException rde) {
	                    rde.printStackTrace();
	                    Toast.makeText(context, rde.getMessage(), Toast.LENGTH_LONG).show();
	                }catch (TimeoutException te) {
	                    te.printStackTrace();
	                    Toast.makeText(context, te.getMessage(), Toast.LENGTH_SHORT).show();
	                } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	                Toast.makeText(context, "Set " + selectedGovernor + " governor",
	                        Toast.LENGTH_LONG).show();
	            }
	        });
	        builder.setNegativeButton("Hell no! Step back!", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.show();

	        _readCurrentGovernor();
	    }
	
	

}
