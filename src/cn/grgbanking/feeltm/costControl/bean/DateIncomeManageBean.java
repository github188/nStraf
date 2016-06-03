package cn.grgbanking.feeltm.costControl.bean;

import java.util.Date;

public class DateIncomeManageBean {
	
	private String dateIncomeId;//人日收入的id
	private String projectId;//项目名称的id
	private String prjGroup;//项目组
	private Date startTime;//起始时间
	private Date endTime;//结束时间
	private Double dateIncome;//人日收入
	private String  entryPeople;//录入人
	private Date  entryTime;//录入时间
	private String  exit1;//备用属性1
	private String  exit2;//备用属性2
	private String  exit3;//备用属性3
	private String  exit4;//备用属性4
	
	
	public DateIncomeManageBean() {
		super();
	}


	public DateIncomeManageBean(String dateIncomeId, String projectId,
			String prjGroup, Date startTime, Date endTime, Double dateIncome,
			String entryPeople, Date entryTime, String exit1, String exit2,
			String exit3, String exit4) {
		super();
		this.dateIncomeId = dateIncomeId;
		this.projectId = projectId;
		this.prjGroup = prjGroup;
		this.startTime = startTime;
		this.endTime = endTime;
		this.dateIncome = dateIncome;
		this.entryPeople = entryPeople;
		this.entryTime = entryTime;
		this.exit1 = exit1;
		this.exit2 = exit2;
		this.exit3 = exit3;
		this.exit4 = exit4;
	}


	public String getDateIncomeId() {
		return dateIncomeId;
	}


	public void setDateIncomeId(String dateIncomeId) {
		this.dateIncomeId = dateIncomeId;
	}


	public String getProjectId() {
		return projectId;
	}


	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public String getPrjGroup() {
		return prjGroup;
	}


	public void setPrjGroup(String prjGroup) {
		this.prjGroup = prjGroup;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public Double getDateIncome() {
		return dateIncome;
	}


	public void setDateIncome(Double dateIncome) {
		this.dateIncome = dateIncome;
	}


	public String getEntryPeople() {
		return entryPeople;
	}


	public void setEntryPeople(String entryPeople) {
		this.entryPeople = entryPeople;
	}


	public Date getEntryTime() {
		return entryTime;
	}


	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}


	public String getExit1() {
		return exit1;
	}


	public void setExit1(String exit1) {
		this.exit1 = exit1;
	}


	public String getExit2() {
		return exit2;
	}


	public void setExit2(String exit2) {
		this.exit2 = exit2;
	}


	public String getExit3() {
		return exit3;
	}


	public void setExit3(String exit3) {
		this.exit3 = exit3;
	}


	public String getExit4() {
		return exit4;
	}


	public void setExit4(String exit4) {
		this.exit4 = exit4;
	}


	

	
	
}
