package cn.grgbanking.feeltm.integralCenter.bean;

import java.util.Date;


public class IntegralInfoVo {
	
	private String userId;//用户ID
	
	private String userName;   //用户名 
	
	private String detName;//部门名
	
	private String groupName; //项目名称
	
 	private String integralSum;//获得积分
	
	private String reason;//获得积分原由
	
	private String gategory;//获得积分类别

	private Date createTime;//创建时间
	
	private String companySort;//全公司排名
	private String deptSort;//部门排名
	private String groupSort;//项目组排名

	//每日准时（每日23:55前）填写日志获得2积分
	private String dayLogGrade;
	//7日内补填日志获得1积分
	private String dayLogNotOnTimeGrade;
	//每周准时（每周结束之前）填写个人周报获得2积分
	private String weekLogGrade;
	//超时补填个人周报获得1积分
	private String weekLogTimeout;
	//爱心小鱼被赞或发表赞扬 
	private String praiseOrPraised;
	//发表赞扬每日得分上限
	private String sendPraiseLimit;
	
	public String getCompanySort() {
		return companySort;
	}

	public void setCompanySort(String companySort) {
		this.companySort = companySort;
	}

	public String getDeptSort() {
		return deptSort;
	}

	public void setDeptSort(String deptSort) {
		this.deptSort = deptSort;
	}

	public String getGroupSort() {
		return groupSort;
	}

	public void setGroupSort(String groupSort) {
		this.groupSort = groupSort;
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

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIntegralSum() {
		return integralSum;
	}

	public void setIntegralSum(String integralSum) {
		this.integralSum = integralSum;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGategory() {
		return gategory;
	}

	public void setGategory(String gategory) {
		this.gategory = gategory;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDayLogGrade() {
		return dayLogGrade;
	}

	public void setDayLogGrade(String dayLogGrade) {
		this.dayLogGrade = dayLogGrade;
	}

	public String getDayLogNotOnTimeGrade() {
		return dayLogNotOnTimeGrade;
	}

	public void setDayLogNotOnTimeGrade(String dayLogNotOnTimeGrade) {
		this.dayLogNotOnTimeGrade = dayLogNotOnTimeGrade;
	}

	public String getWeekLogGrade() {
		return weekLogGrade;
	}

	public void setWeekLogGrade(String weekLogGrade) {
		this.weekLogGrade = weekLogGrade;
	}

	public String getWeekLogTimeout() {
		return weekLogTimeout;
	}

	public void setWeekLogTimeout(String weekLogTimeout) {
		this.weekLogTimeout = weekLogTimeout;
	}

	public String getPraiseOrPraised() {
		return praiseOrPraised;
	}

	public void setPraiseOrPraised(String praiseOrPraised) {
		this.praiseOrPraised = praiseOrPraised;
	}

	public String getSendPraiseLimit() {
		return sendPraiseLimit;
	}

	public void setSendPraiseLimit(String sendPraiseLimit) {
		this.sendPraiseLimit = sendPraiseLimit;
	}

	
	
}
