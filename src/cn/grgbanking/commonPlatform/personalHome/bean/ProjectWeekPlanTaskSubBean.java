package cn.grgbanking.commonPlatform.personalHome.bean;

public class ProjectWeekPlanTaskSubBean {
	/** 计划任务ID */
	private String taskId;
	/** 项目ID */
	private String projectId;
	/** 项目名称 */
	private String projectName;
	/** 计划任务内容 */
	private String taskContent;
	/** 计划工时 */
	private Integer planWorkTime;
	/** 实际工时 */
	private Integer factWorkTime;
	/** 完成度 */
	private String finish;
	/** 偏差度 */
	private String deviation;
	
	/**
	 * @return 计划任务ID 
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @return 项目ID
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @return 项目名称
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @return 计划任务内容
	 */
	public String getTaskContent() {
		return taskContent;
	}
	/**
	 * @return 计划工时
	 */
	public Integer getPlanWorkTime() {
		return planWorkTime;
	}
	/**
	 * @return 实际工时
	 */
	public Integer getFactWorkTime() {
		return factWorkTime;
	}
	/**
	 * @return 偏差度
	 */
	public String getFinish() {
		return finish;
	}
	/**
	 * @return 偏差度
	 */
	public String getDeviation() {
		return deviation;
	}
	/**
	 * @param 计划任务ID
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * @param 项目ID
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @param 项目名称
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @param 计划任务内容
	 */
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	/**
	 * @param 计划工时 
	 */
	public void setPlanWorkTime(Integer planWorkTime) {
		this.planWorkTime = planWorkTime;
	}
	/**
	 * @param 实际工时
	 */
	public void setFactWorkTime(Integer factWorkTime) {
		this.factWorkTime = factWorkTime;
	}
	/**
	 * @param 完成度
	 */
	public void setFinish(String finish) {
		this.finish = finish;
	}
	/**
	 * @param 偏差度
	 */
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	
	
}
