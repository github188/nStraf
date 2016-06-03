package cn.grgbanking.feeltm.dayLog.domain;

import java.util.Date;

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
@Table(name="OA_DAYLOG")
public class DayLog{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_USERID")
	private String userId;
	
	@Column(name = "C_USERNAME")
	private String userName;
	
	@Column(name = "C_USERLEVEL")
	private String userLevel;
	
	@Column(name = "C_DETNAME")
	private String detName;
	
	@Column(name = "C_GROUPNAME")
	private String groupName;
	
	@Column(name = "D_LOGDATE")
	private Date logDate;
	
	@Column(name = "C_PRJ_ID")
	private String prjName;
	
	@Column(name = "C_TYPE")
	private String type;
	
	@Column(name = "C_DESC")
	private String desc;
	
	@Column(name = "D_SUBTOTAL")
	private double subTotal;
	
	@Column(name = "C_FINISH_RATE")
	private String finishRate;
	
	@Column(name = "C_STATU")
	private String statu;
	
	@Column(name = "C_REASON")
	private String reason;
	
	@Column(name = "C_UPDATEMAN")
	private String updateman;
	
	@Column(name = "D_UPDATE")
	private Date updateTime;
	
	@Column(name = "C_FILE")
	private String fileName;
	
	@Column(name = "C_PLANORADD")
	private String planOrAdd;
	
	@Column(name = "C_AUDIT_USER")
	private String auditUser;
	
	@Column(name = "C_AUDIT_MAN")
	private String auditMan;
	
	@Column(name = "C_AUDIT_LOG")
	private String auditLog;
	
	@Column(name = "C_AUDIT_STATU")
	private String auditStatus;
	
	@Column(name = "C_AUDIT_TIME")
	private Date auditTime;
	
	@Column(name = "EXT1")
	private String confirmMan;//确认人
	
	@Column(name = "EXT2")
	private String confirmStatus;//确认状态
	
	@Column(name = "EXT3")
	private String confirmDesc;//确认说明
	
	@Column(name="EXT4")
	private String deviceType;//填写日志的设备类型  web端  mobile端  wechat端
	
	@Column(name = "EXT5")
	private Date confirmTime;//确认时间
	/**首次填写日志的时间*/
	@Column(name = "D_FILLLOG_DATE")
	private Date fillLogDate;
	@Column(name = "EXT7")
	private Double confirmHour;//确认工时
	
	@Column(name= "c_plan_taskid")
	private String plan_taskid;//计划任务Id
	
	@Column(name= "c_tasktype")
	private String tasktype;//查询方式
	
	@Column(name= "d_tasksdate")
	private Date tasksdate;//按日期查询开始日期
	
	@Column(name= "d_taskedate")
	private Date taskedate;//按日期查询结束日期
	
	@Column(name="c_delay_reason")
	private String delay_reason;//延迟原因
	
	@Column(name="c_projectHour")
	private Double projectHour;//项目工时
	
	@Column(name="c_projectDay")
	private Double projectDay;//项目人日
	
	@Column(name="c_enterHour")
	private Double enterHour;//确认工时
	
	@Column(name="c_enterDay")
	private Double enterDay;//确认人日
	
	@Column(name="c_enterRole")
	private String enterRole;//确认角色
	
	@Column(name="c_enterPeople")
	private String enterPeople;//确认用户
	
	@Transient
	private Double allProjectHour;//当天总工时
	@Transient
	private Double allConfirmHour;//当天总确认工时
	 
	@Column(name="c_employstatus")
	private String employstatus;//员工的状态  实习、试用、正式  study  
	
	/**
     * 这里有点坑，如果在get 上添加注解，在查询时会提示 ORA-00904 标识符无效error，（why）
     * isLeave
     * @author zzwen6
     * 2016年3月25日9:05:15
     */
    // 请假状态  1 请假，0不是
    @Column(name="C_LEAVE",nullable=true)
    private String isLeave; // 用字符串，好操作
    
	
	public String getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }

    public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(String auditLog) {
		this.auditLog = auditLog;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public String getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUpdateman() {
		return updateman;
	}

	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPlanOrAdd() {
		return planOrAdd;
	}

	public void setPlanOrAdd(String planOrAdd) {
		this.planOrAdd = planOrAdd;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getConfirmMan() {
		return confirmMan;
	}

	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getConfirmDesc() {
		return confirmDesc;
	}

	public void setConfirmDesc(String confirmDesc) {
		this.confirmDesc = confirmDesc;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Double getConfirmHour() {
		return confirmHour;
	}

	public void setConfirmHour(Double confirmHour) {
		this.confirmHour = confirmHour;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPlan_taskid() {
		return plan_taskid;
	}

	public void setPlan_taskid(String plan_taskid) {
		this.plan_taskid = plan_taskid;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	public Date getTasksdate() {
		return tasksdate;
	}

	public void setTasksdate(Date tasksdate) {
		this.tasksdate = tasksdate;
	}

	public Date getTaskedate() {
		return taskedate;
	}

	public void setTaskedate(Date taskedate) {
		this.taskedate = taskedate;
	}

	public String getDelay_reason() {
		return delay_reason;
	}

	public void setDelay_reason(String delay_reason) {
		this.delay_reason = delay_reason;
	}
	
	/**
	 * @return 首次填写日志的时间
	 */
	public Date getFillLogDate() {
		return fillLogDate;
	}

	/**
	 * @param 首次填写日志的时间
	 */
	public void setFillLogDate(Date fillLogDate) {
		this.fillLogDate = fillLogDate;
	}

	public Double getProjectHour() {
		return projectHour;
	}

	public void setProjectHour(Double projectHour) {
		this.projectHour = projectHour;
	}

	public Double getProjectDay() {
		return projectDay;
	}

	public void setProjectDay(Double projectDay) {
		this.projectDay = projectDay;
	}

	public Double getEnterHour() {
		return enterHour;
	}

	public void setEnterHour(Double enterHour) {
		this.enterHour = enterHour;
	}

	public Double getEnterDay() {
		return enterDay;
	}

	public void setEnterDay(Double enterDay) {
		this.enterDay = enterDay;
	}

	public String getEnterRole() {
		return enterRole;
	}

	public void setEnterRole(String enterRole) {
		this.enterRole = enterRole;
	}

	public String getEnterPeople() {
		return enterPeople;
	}

	public void setEnterPeople(String enterPeople) {
		this.enterPeople = enterPeople;
	}

	public Double getAllProjectHour() {
		return allProjectHour;
	}

	public void setAllProjectHour(Double allProjectHour) {
		this.allProjectHour = allProjectHour;
	}

	public Double getAllConfirmHour() {
		return allConfirmHour;
	}

	public void setAllConfirmHour(Double allConfirmHour) {
		this.allConfirmHour = allConfirmHour;
	}

	public String getEmploystatus() {
		return employstatus;
	}

	public void setEmploystatus(String employstatus) {
		this.employstatus = employstatus;
	}

	
	
}
