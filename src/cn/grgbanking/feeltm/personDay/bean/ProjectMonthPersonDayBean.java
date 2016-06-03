package cn.grgbanking.feeltm.personDay.bean;


public class ProjectMonthPersonDayBean {
	/**月份序号（1，2，3...）  */
	private int monthIndex;
	/** 所属月份(如：2014-01) */
	private String month;
	/** 填写人日数 */
	private int logPersonDay;
	/** 确认人日数 */
	private int confirmPersonDay;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getMonthIndex() {
		return monthIndex;
	}
	public void setMonthIndex(int monthIndex) {
		this.monthIndex = monthIndex;
	}
	public int getLogPersonDay() {
		return logPersonDay;
	}
	public void setLogPersonDay(int logPersonDay) {
		this.logPersonDay = logPersonDay;
	}
	public int getConfirmPersonDay() {
		return confirmPersonDay;
	}
	public void setConfirmPersonDay(int confirmPersonDay) {
		this.confirmPersonDay = confirmPersonDay;
	}
}
