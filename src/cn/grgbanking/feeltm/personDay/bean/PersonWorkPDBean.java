package cn.grgbanking.feeltm.personDay.bean;

public class PersonWorkPDBean {
	/** 用户id */
	private String userId;
	/** 用户姓名 */
	private String userName;
	/** 项目id */
	private String projectId;
	/** 项目名称 */
	private String projectName;
	/** 所属日期(如：2014-01-01) */
	private String inDateStr;
	/** 所属月份(如：2014-01) */
	private String inMonthStr;
	/** 所属年份 (如：2014)*/
	private String inYearStr;
	/** 所占人日(如：0.5) */
	private float subTotal;
	
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getInDateStr() {
		return inDateStr;
	}
	public void setInDateStr(String inDateStr) {
		this.inDateStr = inDateStr;
	}
	public String getInMonthStr() {
		return inMonthStr;
	}
	public void setInMonthStr(String inMonthStr) {
		this.inMonthStr = inMonthStr;
	}
	public String getInYearStr() {
		return inYearStr;
	}
	public void setInYearStr(String inYearStr) {
		this.inYearStr = inYearStr;
	}
	public float getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}
}
