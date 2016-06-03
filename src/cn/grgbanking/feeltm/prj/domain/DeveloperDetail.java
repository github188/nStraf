package cn.grgbanking.feeltm.prj.domain;

import java.io.Serializable;

/**
 * '未解决缺陷数 OpenBugNum + RepenBugNum
 * 
 * '缺陷ReOpen率 = Reopen缺陷数/个人缺陷总数（含Rejected缺陷）
 * 
 * '缺陷修复率 = (FIXED+REJECTED缺陷)/总缺陷数（含REJECTED缺陷）
 * 
 * alter PROCEDURE get_developer
 * 
 * @szName varchar(20) = '',
 * @szPrjName varchar(64) = '',
 * @szVersionNO varchar(20) = '',
 * @startDate datetime = '1970-1-1 0:0:0',
 * @endDate datetime = '2050-1-1 0:0:0',
 * @szCount varchar(1024) output
 */
public class DeveloperDetail implements Serializable,Comparable<DeveloperDetail>{
	private static final long serialVersionUID = 1L;
	
	private String devName;
	private String devName_en;
	private String dept;
	private String  totalPoint;
	private int totalPointInt;
	private String qualityEvalute;
	private String bugTotalCount;
	private String bugTotalValue;
	
	
	private String bugReopenRate;
	private String avrFixTime;
	private String FixRate;
	//
	private String bugOpen;
	private String bugReopen;
	private String bugFix;
	private String bugReject;
	private String bugUnresolve; // open+reopen
	private String bugSubtotal;

	

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getBugReopenRate() {
		return bugReopenRate;
	}

	public void setBugReopenRate(String bugReopenRate) {
		this.bugReopenRate = bugReopenRate;
	}

	public String getAvrFixTime() {
		return avrFixTime;
	}

	public void setAvrFixTime(String avrFixTime) {
		this.avrFixTime = avrFixTime;
	}

	public String getFixRate() {
		return FixRate;
	}

	public void setFixRate(String fixRate) {
		FixRate = fixRate;
	}

	public String getBugOpen() {
		return bugOpen;
	}

	public void setBugOpen(String bugOpen) {
		this.bugOpen = bugOpen;
	}

	public String getBugReopen() {
		return bugReopen;
	}

	public void setBugReopen(String bugReopen) {
		this.bugReopen = bugReopen;
	}

	public String getBugFix() {
		return bugFix;
	}

	public void setBugFix(String bugFix) {
		this.bugFix = bugFix;
	}

	public String getBugReject() {
		return bugReject;
	}

	public void setBugReject(String bugReject) {
		this.bugReject = bugReject;
	}

	public String getBugUnresolve() {
		return bugUnresolve;
	}

	public void setBugUnresolve(String bugUnresolve) {
		this.bugUnresolve = bugUnresolve;
	}

	public String getBugSubtotal() {
		return bugSubtotal;
	}

	public void setBugSubtotal(String bugSubtotal) {
		this.bugSubtotal = bugSubtotal;
	}
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(String totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getQualityEvalute() {
		return qualityEvalute;
	}

	public void setQualityEvalute(String qualityEvalute) {
		this.qualityEvalute = qualityEvalute;
	}

	public String getBugTotalCount() {
		return bugTotalCount;
	}

	public void setBugTotalCount(String bugTotalCount) {
		this.bugTotalCount = bugTotalCount;
	}

	public String getBugTotalValue() {
		return bugTotalValue;
	}

	public void setBugTotalValue(String bugTotalValue) {
		this.bugTotalValue = bugTotalValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getTotalPointInt() {
		return totalPointInt;
	}

	public void setTotalPointInt(int totalPointInt) {
		this.totalPointInt = totalPointInt;
	}

	public int compare(DeveloperDetail o1, DeveloperDetail o2) {
		if(o1.getTotalPointInt()>o2.getTotalPointInt())    
		   return 1;    
		  else   
		   return 0; 
	}
	
	
	public String getDevName_en() {
		return devName_en;
	}

	public void setDevName_en(String devNameEn) {
		devName_en = devNameEn;
	}

	@Override
	public int compareTo(DeveloperDetail o) {
		if(this.getTotalPointInt()<o.getTotalPointInt())    
			   return 1;    
			  else   
			   return 0;
	}
}
