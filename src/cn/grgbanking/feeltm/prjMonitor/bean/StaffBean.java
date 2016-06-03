/**
 * 
 */
package cn.grgbanking.feeltm.prjMonitor.bean;

import java.util.List;

import cn.grgbanking.feeltm.project.domain.Project;

/**
 * @author ping
 * 
 */
public class StaffBean {
	private String userId;
	private String userName;
	private String deptId;
	private String deptName;
	private String deptColor;
	private List<Project> prjList;

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

	public List<Project> getPrjList() {
		return prjList;
	}

	public void setPrjList(List<Project> prjList) {
		this.prjList = prjList;
	}

}
