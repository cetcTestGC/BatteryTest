package com.example.SetCpuMode;

import android.R.integer;
import android.R.string;

	/**
	 * Cpu工作模式选择变量
	 */

public class CpuControlMode {

	

	/**
	 * 按需调节cpu频率,不操作手机的时候控制在最低频率,
	 * 滑屏或进入应用后会迅速提升至最高频率,当空闲时迅速降低频率,性能较稳定,但因频率变化幅度过大,
	 * 省电方面只有一般的水平。是一种在电池和性能之间趋向平衡的默认模式, 但是对于智能手机来说,
	 * ondemand在性能表现方面略有欠缺。
	 */
	public static final String OnDemand = "ondemand";
	
	public static final String NORMAL_HELP_INFO =
			"提示框：  "+"\n"+"本模式为手机自带CPU调节模式";

	
	/**
	 * 任何情况下都会控制CPU运行在配置的频率范围内,配置中的用户自己添加的省电设置。
	 *  在此情景模式下,降低CPU最大运行频率可以延长电池待机时间,
	 * 但同时也会降低机器的唤醒速度,建议最好不使用该选项。
	 */
	public static final String UserSpace = "userspace";

	/**
	 * 按设定最低频率运行,日常没有使用价值,除非配合setcpu情景模式,
	 * 关屏睡眠时使用此调节模式,省电但系统响应速度慢。
	 */
	public static final String PowerSave = "powersave";
	public static final String POWERSAVE_HELP_INFO = 
			"提示框：  "+"\n"+"按设定最低频率运行,关屏睡眠时推荐使用此调节模式,非常省电但系统响应速度慢";
	
	/**
	 * 源自三星猎户座处理器的一个调速器,
	 * 可以单独调控单个CPU内核,理论上性能不错也很省电。
	 */
	public static final String Pegasusq = "pegasusq";
	
	/**
	 * 高性能模式,按你设定范围的最高频率运行,即使系统负载非常低cpu的频率也为最高。
	 * 性能很好,因为CPU本身不需要资源去调整频率,但是电量消耗较快,温度也高一些.
	 */
	
	public static final String Perfromance = "performance";
	
	public static final String PERFROMANCE_HELP_INFO = 
			"提示框：  "+"\n"+"高性能模式,按你设定范围的最高频率运行," +
					"即使手机处于空闲状态,频率也为最高,速度极快,但电量消耗较快,温度较高";
	


	public static String getPowersave() {
		return "低耗模式";
	}

	public static String getPegasusq() {
		//三星模式
		return "正常模式";
	}

	public static String getPerfromance() {
		return "最佳模式";
	}

	public static String getNormal() {
		return "正常模式";
	}
	
	
	
	
}
