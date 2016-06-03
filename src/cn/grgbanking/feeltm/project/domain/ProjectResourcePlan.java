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
@Table(name="OA_PROJECT_RESOURCE_PLAN")
public class ProjectResourcePlan extends BaseDomain{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_USERKEY")
	private String userid;//人员userid
	@Column(name="C_USERNAME")
	private String username;//人员姓名
	@Column(name="C_PROJECT_ROLE")
	private String projectRole;//项目角色
	@Column(name="C_PROJECT_DUTY")
	private String projectDuty;//项目职责
	@Column(name="d_plan_starttime")
	private Date planStartTime;//计划开始时间
	@Column(name="d_plan_endtime")
	private Date planEndTime;//计划结束时间
	@Column(name="d_fact_starttime")
	private Date factStartTime;//实际开始时间
	@Column(name="d_fact_endtime")
	private Date factEndTime;//实际结束时间
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	public Date getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	public Date getFactStartTime() {
		return factStartTime;
	}
	public void setFactStartTime(Date factStartTime) {
		this.factStartTime = factStartTime;
	}
	public Date getFactEndTime() {
		return factEndTime;
	}
	public void setFactEndTime(Date factEndTime) {
		this.factEndTime = factEndTime;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
}
