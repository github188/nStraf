package cn.grgbanking.feeltm.dayLog.bean;

import java.util.List;

public class DateDayLog {
	private String id;
	private List daylogList;
	private String logDate;
	private String userName;
	private String userDept;
	private String userGroup;
	private String updateMan;
	private String updateTime;
	private String auditStatus;
	private String auditMan;
	private String auditTime;
	private double subTotal;
	private String auditContent;
	private String userId;
	
	private String confirmMan;//确认人
	
	private String confirmStatus;//确认状态
	
	private String confirmDesc;//确认说明
	
	private String confirmTime;//确认时间
	
	private Double confirmHour;//确认工时
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public List getDaylogList() {
		return daylogList;
	}
	public void setDaylogList(List daylogList) {
		this.daylogList = daylogList;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDept() {
		return userDept;
	}
	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public String getUpdateMan() {
		return updateMan;
	}
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getConfirmMan() {
		return confirmMan;
	}
	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}
	public String getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getConfirmDesc() {
		return confirmDesc;
	}
	public void setConfirmDesc(String confirmDesc) {
		this.confirmDesc = confirmDesc;
	}

	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public Double getConfirmHour() {
		return confirmHour;
	}
	public void setConfirmHour(Double confirmHour) {
		this.confirmHour = confirmHour;
	}
	
}
