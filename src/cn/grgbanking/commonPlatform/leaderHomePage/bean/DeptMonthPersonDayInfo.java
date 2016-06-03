package cn.grgbanking.commonPlatform.leaderHomePage.bean;
/**
 * 圆饼图当月部门人日消耗
 * @author xing
 *
 */

public class DeptMonthPersonDayInfo {
	/**年份*/
	private String year;
	/** 月份 */
	private String month;
	/** 部门id */
	private String deptId;
	/** 部门名称 */
	private String deptName;
	/** 部门颜色 */
	private String deptColor;
	/** 确认人日 */
	private long personDay;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptColor() {
		return deptColor;
	}
	public void setDeptColor(String deptColor) {
		this.deptColor = deptColor;
	}
	public long getPersonDay() {
		return personDay;
	}
	public void setPersonDay(long personDay) {
		this.personDay = personDay;
	}
	
	
	
}
