package cn.grgbanking.feeltm.projectPlan.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_PROJECT_PLAN")
public class ProjectPlan {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_GROUPNAME")
	private String groupName;
	
	@Column(name = "C_PRJNAME")
	private String projectName;
	
	@Column(name = "C_PRJMANAGER")
	private String projectManager;
	
	@Column(name = "C_PRJDESC")
	private String projectDesc;
	
	@Column(name = "C_FARE")
	private String fare;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "D_PLANSTART_DATE")
	private Date planStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "D_PLANEND_DATE")
	private Date planEndDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "D_FACTSTART_DATE")
	private Date factStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "D_FACTEND_DATE")
	private Date factEndDate;
	
	@Column(name = "I_PLAN_WORKDATE")
	private Integer planWorkDate;
	
	@Column(name = "I_FACT_WORKDATE")
	private Integer factWorkDate;
	
	@Column(name = "I_PLAN_WORKTIME")
	private Integer planWorkTime;
	
	@Column(name = "I_FACT_WORKTIME")
	private Integer factWorkTime;
	
	@Column(name = "D_UPDATE_TIME")
	private Date updateTime;
	
	@Column(name = "C_UPDATE_USER")
	private String updateUser;
	
	@Column(name = "C_UPDATE_MAN")
	private String updateMan;
	
	@Column(name = "C_CREATOR_USER")
	private String creatorUser;
	

	public String getId() {
		return id;
	}

	

	public String getProjectName() {
		return projectName;
	}



	public Date getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	public String getUpdateUser() {
		return updateUser;
	}



	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}



	public String getUpdateMan() {
		return updateMan;
	}



	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public String getProjectManager() {
		return projectManager;
	}



	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}



	public String getProjectDesc() {
		return projectDesc;
	}



	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}



	public String getFare() {
		return fare;
	}



	public void setFare(String fare) {
		this.fare = fare;
	}



	public Date getPlanStartDate() {
		return planStartDate;
	}



	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}



	public Date getPlanEndDate() {
		return planEndDate;
	}



	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}



	public Date getFactStartDate() {
		return factStartDate;
	}



	public void setFactStartDate(Date factStartDate) {
		this.factStartDate = factStartDate;
	}



	public Date getFactEndDate() {
		return factEndDate;
	}



	public void setFactEndDate(Date factEndDate) {
		this.factEndDate = factEndDate;
	}



	public Integer getPlanWorkDate() {
		return planWorkDate;
	}



	public void setPlanWorkDate(Integer planWorkDate) {
		this.planWorkDate = planWorkDate;
	}



	public Integer getFactWorkDate() {
		return factWorkDate;
	}



	public void setFactWorkDate(Integer factWorkDate) {
		this.factWorkDate = factWorkDate;
	}



	public Integer getPlanWorkTime() {
		return planWorkTime;
	}



	public void setPlanWorkTime(Integer planWorkTime) {
		this.planWorkTime = planWorkTime;
	}



	public Integer getFactWorkTime() {
		return factWorkTime;
	}



	public void setFactWorkTime(Integer factWorkTime) {
		this.factWorkTime = factWorkTime;
	}



	public void setId(String id) {
		this.id = id;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public String getCreatorUser() {
		return creatorUser;
	}



	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}
	
	
	
}
