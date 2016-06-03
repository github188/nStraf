package cn.grgbanking.feeltm.costControl.bean;

import java.util.Map;

public class UserDateProjectPersonDayBean {
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 *用户姓名 
	 */
	private String userName;
	/**
	 * 用户的所有日期的项目人日数据,key为某个日期，value为该用户该日期的项目确认人日
	 */
	private Map<String,Double> dateProjectPersonDayMap;
	
	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Map<String, Double> getDateProjectPersonDayMap() {
		return dateProjectPersonDayMap;
	}


	public void setDateProjectPersonDayMap(
			Map<String, Double> dateProjectPersonDayMap) {
		this.dateProjectPersonDayMap = dateProjectPersonDayMap;
	}


	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
