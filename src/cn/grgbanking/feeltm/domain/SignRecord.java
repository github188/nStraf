package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_SIGNRECORD")
public class SignRecord implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID",unique = true, nullable = false)
	private String id;//主键
	
	@Column(name = "C_USERID")
	private String userId;//用户ID
	
	@Column(name = "D_SIGNTIME")
	private Date signTime;//签到时间
	
	@Column(name = "C_UUID")
	private String uuId;//手机ID
	
	@Column(name="C_USERNAME")
	private String username;
	
	@Column(name = "C_AREANAME")
	private String areaName;//签到位置
	
	@Column(name = "D_LONGITUDE")
	private Double longitude;//经度
	
	@Column(name = "D_LATITUDE")
	private Double latitude;//纬度
	
	@Column(name = "C_DEPTNAME")
	private String deptName;//部门
	
	@Column(name = "C_GRPNAME")
	private String grpName;//组别
	
	@Column(name="c_type")
	private String type;//类型 0,公司签到；1，广发签到；2，移动签到记录;3:补签到
	
	@Column(name="c_status")
	private String status;//状态,当前签到记录是否有对应的打卡记录（公司打卡、广发打卡）
	
	@Column(name="C_VILID")
	private String vilid; //1：有效  0：无效
	
	@Column(name="I_FLAG")
	private int flag;//0 百度-网络签到  1：Html5签到 ;3:其他
	
	@Column(name="C_SIGNWHERE")
	private String signwhere;//签到地点 0:公司 1:广发 2:其他
	
	@Column(name="C_APPROVER")
	private String approvePerson; //审核人
	
	@Column(name="C_SIGNNOTE")
	private String signNote;	//补签到原因或备注签到注明
	
	@Column(name="C_APPROVESTATUS")
	private String approveStatus; //审核状态   0:新增；1:修改中；2：项目经理审核；3：部门经理审核；4：行政审核；5：审核不通过；6：审核通过；   "无":未提交审核的备注签到
	
	/**
	 * 考勤状态显示值
	 */
	@Transient
	private String attendanceStatusValue;//考勤状态   0:无效、不识别考勤 1:出勤 2:退勤3:迟到4:早退
	/**
	 * 考勤状态(注意考勤状态   0:不识别情况，
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别)
	 */
	@Column(name="C_ATTENDANCE_STATUS")
	private int attendanceStatus;
	
	public String getUuId() {
		return uuId;
	}


	public void setUuId(String uuId) {
		this.uuId = uuId;
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


	public Date getSignTime() {
		return signTime;
	}


	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getAreaName() {
		return areaName;
	}


	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String getVilid() {
		return vilid;
	}
	public void setVilid(String vilid) {
		this.vilid = vilid;
	}


	public String getSignwhere() {
		return signwhere;
	}


	public void setSignwhere(String signwhere) {
		this.signwhere = signwhere;
	}


	public String getApprovePerson() {
		return approvePerson;
	}


	public void setApprovePerson(String approvePerson) {
		this.approvePerson = approvePerson;
	}


	public String getSignNote() {
		return signNote;
	}


	public void setSignNote(String signNote) {
		this.signNote = signNote;
	}


	public String getApproveStatus() {
		return approveStatus;
	}


	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	/**
	 * @return 考勤状态   0:不识别、无效  1:出勤 2:退勤3:迟到4:早退
	 * 注意考勤状态   0:不识别情况，
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别
	 */
	public int getAttendanceStatus() {
		return attendanceStatus;
	}

	/**
	 * @param 考勤状态   0:不识别、无效  1:出勤 2:退勤3:迟到4:早退
	 *  注意考勤状态   0:不识别情况，
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别
	 */
	public void setAttendanceStatus(int attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}


	/**
	 * @return 考勤状态显示值
	 */
	public String getAttendanceStatusValue() {
		return attendanceStatusValue;
	}


	/**
	 * @param 考勤状态显示值
	 */
	public void setAttendanceStatusValue(String attendanceStatusValue) {
		this.attendanceStatusValue = attendanceStatusValue;
	}
}
