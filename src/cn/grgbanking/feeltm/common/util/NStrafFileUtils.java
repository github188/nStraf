package cn.grgbanking.feeltm.common.util;

import java.io.File;

import cn.grgbanking.feeltm.config.Configure;


public class NStrafFileUtils {
	
	private static String getSystemFileHome(){
		String dir="";
		if("/".equals(File.separator)){//linux系统
			dir = Configure.getProperty("linuxFileHome");
		}
		if("\\".equals(File.separator)){//window系统
			dir = Configure.getProperty("windowsFileHome");
		}
		return dir;
	}
	
	/**
	 * 获取报销模版下载目录
	 * @return
	 * lhyan3
	 * 2014年7月10日
	 */
	public static String getExpenseTemplatePath(){
		return getSystemFileHome()+File.separator+"expenseTemplate"+File.separator+Configure.getProperty("expenseTemplateName");
	}
	
	/**
	 * 获取考勤模板下载目录
	 * @return
	 */
	public static String getAttendanceTemplatePath(){
		return getSystemFileHome()+File.separator+"attendanceTemplate"+File.separator+Configure.getProperty("AttendanceTemplateName");
	}
	
	/**
	 * 获取客户端上传路径
	 * @return
	 * lhyan3
	 * 2014年7月10日
	 */
	public static String getClientUploadDir(){
		return getSystemFileHome()+File.separator+"client"+File.separator;
	}
	
	/**
	 * 获取年度计划上传路径
	 * @return
	 */
	public static String getAnnualPlayUploadDir(){
		return getSystemFileHome()+File.separator+"annualPlan"+File.separator;
	}
	
}
