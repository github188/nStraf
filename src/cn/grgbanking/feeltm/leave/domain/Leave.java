package cn.grgbanking.feeltm.leave.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_LEAVE")
public class Leave {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_USERID")
	private String userid;//用户
	@Column(name="D_SUBTIME")
	private Date subTime;//登记时间
	@Column(name="C_USERNAME")
	private String username;//用户名
	@Column(name="D_STARTTIME")
	private Date startTime;//开始时间
	@Column(name="D_ENDTIME")
	private Date endTime;//结束时间
	@Column(name="I_SUMTIME")
	private double sumtime;//总时长
	@Column(name="C_DEPTNAME")
	private String deptName;//部门
	@Column(name="C_GRPNAME")
	private String grpName;//组别
	@Column(name="C_TYPE")
	private String type;//类型
	@Column(name="C_REASON")
	private String reason;//原因
	@Column(name="C_APPROVER")
	private String approver;//审批人
	@Column(name="C_APPROVERNAME")
	private String approverName;
	@Column(name="C_STATUS")
	private String status;//状态 
	@Column(name="C_UPDATEUSER")
	private String updateUser;//更新人
	@Column(name="D_UPDATETIME")
	private Date updateTime;//更新时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getSubTime() {
		return subTime;
	}
	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public double getSumtime() {
		return sumtime;
	}
	public void setSumtime(double sumtime) {
		this.sumtime = sumtime;
	}
	
}
