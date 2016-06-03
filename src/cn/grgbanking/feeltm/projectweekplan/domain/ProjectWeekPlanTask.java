package cn.grgbanking.feeltm.projectweekplan.domain;

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
@Table(name="OA_PROJECT_WEEK_PLAN_TASK")
public class ProjectWeekPlanTask {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "c_planid")
	private String planId;
	
	@Column(name = "c_userkey")
	private String userKey;
	
	@Column(name = "c_username")
	private String userName;
	
	@Column(name = "c_taskcontent")
	private String taskContent;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_startdate")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_enddate")
	private Date endDate;
	
	@Column(name = "i_plan_worktime")
	private Integer planWorkTime;
	
	@Column(name = "i_fact_worktime")
	private Integer factWorkTime;
	
	@Column(name = "c_desc")
	private String desc;
	
	@Column(name = "c_finish")
	private String finish;
	
	@Column(name = "c_deviation")
	private String deviation;
	
	@Column(name = "c_priority")
	private String priority;
	
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
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public String getDeviation() {
		return deviation;
	}
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
}
