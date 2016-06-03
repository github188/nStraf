package cn.grgbanking.feeltm.report.domain;

import java.util.Date;

public class DayReportStatic {
	private Date startDate;
	private String weekDay;
	private int personNum;
	private double taskTime;
	private String personDetail;  //具体哪些人员未填写周报
	
	public DayReportStatic(){
		
	}
	
	
	
	public DayReportStatic(Date startDate, String weekDay, int personNum,
			double taskTime) {
		this.startDate = startDate;
		this.weekDay = weekDay;
		this.personNum = personNum;
		this.taskTime = taskTime;
	}



	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	public double getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(double taskTime) {
		this.taskTime = taskTime;
	}
	public String getPersonDetail() {
		return personDetail;
	}
	public void setPersonDetail(String personDetail) {
		this.personDetail = personDetail;
	}
	
	
	
}
