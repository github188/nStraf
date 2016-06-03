package cn.grgbanking.feeltm.personDay.bean;

import java.util.List;
/**
 * wtjiao 2014年9月30日 上午11:19:38
 */
public class ProjectPersonDayBean {
	/** 项目id */
	private String projectId;
	/** 项目名称 */
	private String projectName;
	/** 项目经理id */
	private String projectManagerId;
	/** 项目经理姓名 */
	private String projectManagerName;
	/** 本年度人日数 */
	private float curYearPersonDay;
	/** 累计人日数 (当前年之前的累计数)*/
	private float beforeCurYearSumPersonDay;
	/** 人日消耗详情 */
	private List<ProjectMonthPersonDayBean> detail;
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
	public String getProjectManagerId() {
		return projectManagerId;
	}
	public void setProjectManagerId(String projectManagerId) {
		this.projectManagerId = projectManagerId;
	}
	public String getProjectManagerName() {
		return projectManagerName;
	}
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	public float getCurYearPersonDay() {
		return curYearPersonDay;
	}
	public void setCurYearPersonDay(float curYearPersonDay) {
		this.curYearPersonDay = curYearPersonDay;
	}
	public float getBeforeCurYearSumPersonDay() {
		return beforeCurYearSumPersonDay;
	}
	public void setBeforeCurYearSumPersonDay(float beforeCurYearSumPersonDay) {
		this.beforeCurYearSumPersonDay = beforeCurYearSumPersonDay;
	}
	public List<ProjectMonthPersonDayBean> getDetail() {
		return detail;
	}
	public void setDetail(List<ProjectMonthPersonDayBean> detail) {
		this.detail = detail;
	}
	
}
