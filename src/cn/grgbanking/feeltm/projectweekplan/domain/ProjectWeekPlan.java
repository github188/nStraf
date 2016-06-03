package cn.grgbanking.feeltm.projectweekplan.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cn.grgbanking.feeltm.base.BaseDomain;


@Entity
@Table(name="OA_PROJECT_WEEK_PLAN")
public class ProjectWeekPlan extends BaseDomain{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	/** 客户 key（数据字典中配置） */
	@Column(name = "c_customerkey")
	private String customerKey;
	
	/** 客户名称 */
	@Column(name = "c_customername")
	private String customerName;
	
	/** 计划关联的项目 */
	@Column(name = "c_projectid")
	private String projectId;
	
	/** 周时间段(如：6.21-6.27) */
	@Column(name = "c_week_period")
	private String weekPeriod;
	
	/** 周描述(如：6月第3周) */
	@Column(name = "c_week_desc")
	private String weekDesc;
	
	/** 标准时长（如：5日） */
	@Column(name = "i_standard_duration")
	private Integer standardDuration;
	
	/** 整体状态（项目进度延迟、项目进度超前、项目进度正常） */
	@Column(name = "c_schedule_state")
	private String scheduleState;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_week_start")
	private Date weekStart;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_week_end")
	private Date weekEnd;
	
	@Transient
	private List targetList;

	@Transient
	private List taskList;
	
	@Transient
	private String projectName;
	
	@Column(name="c_projectrish")
	private String projectrish;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerKey() {
		return customerKey;
	}

	public void setCustomerKey(String customerKey) {
		this.customerKey = customerKey;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getWeekPeriod() {
		return weekPeriod;
	}

	public void setWeekPeriod(String weekPeriod) {
		this.weekPeriod = weekPeriod;
	}

	public String getWeekDesc() {
		return weekDesc;
	}

	public void setWeekDesc(String weekDesc) {
		this.weekDesc = weekDesc;
	}

	public Integer getStandardDuration() {
		return standardDuration;
	}

	public void setStandardDuration(Integer standardDuration) {
		this.standardDuration = standardDuration;
	}

	public String getScheduleState() {
		return scheduleState;
	}

	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List getTargetList() {
		return targetList;
	}

	public void setTargetList(List targetList) {
		this.targetList = targetList;
	}

	public List getTaskList() {
		return taskList;
	}

	public void setTaskList(List taskList) {
		this.taskList = taskList;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Date weekStart) {
		this.weekStart = weekStart;
	}

	public Date getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(Date weekEnd) {
		this.weekEnd = weekEnd;
	}
	
	
	@Column(name="EXT1")
	private String ext1;
	
	@Column(name="EXT2")
	private String ext2;
	
	@Column(name="EXT3")
	private String ext3;
	
	@Column(name="EXT4")
	private String ext4;
	
	@Column(name="EXT5")
	private Date ext5;
	
	@Column(name="EXT6")
	private Date ext6;
	
	@Column(name="EXT7")
	private Double ext7;
	
	@Column(name="EXT8")
	private Double ext8;
	
	@Column(name="C_UPDATEUSER_ID")
	private String updateuserId;//更新人userid
	
	@Column(name="C_UPDATEUSER_NAME")
	private String updateUsername;//更新人username
	
	@Column(name="D_UPDATE_TIME")
	private Date updateTime;//更新时间

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public Date getExt5() {
		return ext5;
	}

	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	public Date getExt6() {
		return ext6;
	}

	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	public Double getExt7() {
		return ext7;
	}

	public void setExt7(Double ext7) {
		this.ext7 = ext7;
	}

	public Double getExt8() {
		return ext8;
	}

	public void setExt8(Double ext8) {
		this.ext8 = ext8;
	}
	
	public String getUpdateuserId() {
		return updateuserId;
	}
	public void setUpdateuserId(String updateuserId) {
		this.updateuserId = updateuserId;
	}
	public String getUpdateUsername() {
		return updateUsername;
	}
	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getProjectrish() {
		return projectrish;
	}
	public void setProjectrish(String projectrish) {
		this.projectrish = projectrish;
	}
	
}
