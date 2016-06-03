package cn.grgbanking.feeltm.costControl.bean;

public class EnterProject {
	private String projectId;
	private String projectName;
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
	public EnterProject(String projectId, String projectName) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
	}
	public EnterProject() {
		super();
	}
	
}
