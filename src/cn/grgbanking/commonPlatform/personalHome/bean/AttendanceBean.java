package cn.grgbanking.commonPlatform.personalHome.bean;

public class AttendanceBean {
	/** 上班时间 */
	private String entryTime = "";
	/** 下班时间 */
	private String exitTime = "";
	/** 上班考勤状态 */
	private int attendanceStatusEntry;
	/** 下班考勤状态 */
	private int attendanceStatusExit;	
	/** 考勤日期 */
	private String signDay;
	
	/**
	 * @return entryTime
	 */
	public String getEntryTime() {
		return entryTime;
	}
	/**
	 * @return exitTime
	 */
	public String getExitTime() {
		return exitTime;
	}
	/**
	 * @return signDay
	 */
	public String getSignDay() {
		return signDay;
	}
	/**
	 * @param entryTime
	 */
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	/**
	 * @param exitTime
	 */
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	/**
	 * @param signDay
	 */
	public void setSignDay(String signDay) {
		this.signDay = signDay;
	}
	
	/**
	 * @return attendanceStatusEntry
	 */
	public int getAttendanceStatusEntry() {
		return attendanceStatusEntry;
	}
	/**
	 * @return attendanceStatusExit
	 */
	public int getAttendanceStatusExit() {
		return attendanceStatusExit;
	}
	/**
	 * @param attendanceStatusEntry
	 */
	public void setAttendanceStatusEntry(int attendanceStatusEntry) {
		this.attendanceStatusEntry = attendanceStatusEntry;
	}
	/**
	 * @param attendanceStatusExit
	 */
	public void setAttendanceStatusExit(int attendanceStatusExit) {
		this.attendanceStatusExit = attendanceStatusExit;
	}
}
