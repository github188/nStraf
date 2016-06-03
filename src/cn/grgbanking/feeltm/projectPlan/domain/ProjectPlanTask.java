package cn.grgbanking.feeltm.projectPlan.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="OA_PROJECT_PLAN_TASK")
public class ProjectPlanTask {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_PLAN_ID")
	private String planId;
	
	@Column(name = "C_TASK_RESOURCE")
	private String taskResource;
	
	@Column(name = "C_PRARENT_ID")
	private String parentId;
	
	@Column(name = "C_PREMISE")
	private String premise;
	
	@Column(name = "C_DUTYMAN")
	private String dutyMan;
	
	@Column(name = "C_TASK_DESC")
	private String taskDesc;
	
	@Column(name = "C_TASK_NAME")
	private String taskName;
	
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
	
	@Column(name = "I_SHOW_ORDER")
	private Integer showOrder;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTaskResource() {
		return taskResource;
	}

	public void setTaskResource(String taskResource) {
		this.taskResource = taskResource;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPremise() {
		return premise;
	}

	public void setPremise(String premise) {
		this.premise = premise;
	}

	public String getDutyMan() {
		return dutyMan;
	}

	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
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

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
}
