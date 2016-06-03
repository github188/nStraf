package cn.grgbanking.feeltm.project.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.grgbanking.feeltm.base.BaseDomain;


/**
 * 项目和人员的关联表
 * @author lhyan3
 * 2014年6月25日
 */
@Entity
@Table(name="OA_PROJECT_RESOURCE")
public class UserProject extends BaseDomain{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_USERKEY")
	private String userid;//人员userid
	@Column(name="C_USERNAME")
	private String username;//人员姓名
	@Column(name="C_PROJECTNAME")
	private String projectname;//项目名称
	@Column(name="d_plan_starttime")
	private Date entryTime;//计划开始时间
	@Column(name="d_plan_endtime")
	private Date exitTime;//计划结束时间
	@Column(name="d_updatetime")
	private Date updateTime;
	
	@Column(name="C_PROJECT_ROLE")
	private String projectRole;//项目角色
	@Column(name="C_PROJECT_DUTY")
	private String projectDuty;//项目职责
	@Column(name="C_DEPT_NAME")
	private String deptName;//所属部门
	@Column(name="C_PHONE")
	private String phone;//联系电话
	
	@ManyToOne(optional=false)
	@JoinColumn(name="C_PROJECTID",referencedColumnName="C_ID")
	private Project project;
	

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getProjectRole() {
		return projectRole;
	}
	public void setProjectRole(String projectRole) {
		this.projectRole = projectRole;
	}
	public String getProjectDuty() {
		return projectDuty;
	}
	public void setProjectDuty(String projectDuty) {
		this.projectDuty = projectDuty;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Date getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	public Date getExitTime() {
		return exitTime;
	}
	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}
	
}
