package cn.grgbanking.feeltm.weekLog.bean;

import java.util.List;

public class DateWeekLog {
	private String id;
	private List daylogList;
	private String weekCount;
	private String desc;
	private String plan;
	private String userId;
	private String userName;
	private int weekLen;//个人一周有多少条日志
	private int allWeekLen;//所有人一周共有多少条日志
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List getDaylogList() {
		return daylogList;
	}
	public void setDaylogList(List daylogList) {
		this.daylogList = daylogList;
	}
	public String getWeekCount() {
		return weekCount;
	}
	public void setWeekCount(String weekCount) {
		this.weekCount = weekCount;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
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
	public int getWeekLen() {
		return weekLen;
	}
	public void setWeekLen(int weekLen) {
		this.weekLen = weekLen;
	}
	public int getAllWeekLen() {
		return allWeekLen;
	}
	public void setAllWeekLen(int allWeekLen) {
		this.allWeekLen = allWeekLen;
	}
	
	
}
