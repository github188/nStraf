package cn.grgbanking.feeltm.cardRecord.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 考勤数据表(含广发银行.产业园.移动签到数据)
 * @author zzhui1
 *
 */
@Entity
@Table(name = "OA_CARDRECORD")
public class CardRecord {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	@Column(name = "C_UUID")
	private String uuid;
	/** 用户ID */
	@Column(name = "C_USERID")
	private String userid;

	/** 考勤时间 */
	@Column(name = "D_SIGNTIME")
	private Date signtime;

	/** 考勤地点 */
	@Column(name = "C_AREANAME")
	private String areaname;

	/** 地点经度 */
	@Column(name = "D_LONGITUDE")
	private Double longitude;

	/** 地点纬度 */
	@Column(name = "D_LATITUDE")
	private Double latitude;

	/** 部门 */
	@Column(name = "C_DEPTNAME")
	private String deptname;

	/** 项目组 */
	@Column(name = "C_GRPNAME")
	private String grpname;

	/** 用户名 */
	@Column(name = "C_USERNAME")
	private String username;

	/** 考勤类型（0产业园打卡、1广发打卡、2移动打卡） */
	@Column(name = "C_TYPE")
	private String type;

	/** 状态 （作废字段）*/
	@Column(name = "C_STATUS")
	private String status;

	/** 签到手机类型（0 android  IOS：1 ;3:其他） */
	@Column(name = "I_FLAG")
	private int flag;

	/** 有效性 （是否在有效的打卡地点范围内）*/
	@Column(name = "C_VILID")
	private String vilid;

	/** 签到地点 */
	@Column(name = "C_SIGNWHERE")
	private String signwhere;

	/** 处理类型（作废字段，在原来考勤统计中用来标识是否已经处理过） */
	@Column(name = "C_DEALSTATUS")
	private String dealstatus;

	/** 考勤  0:不识别 (该项目没有填写规定的上下班时间) 1:正常签到 2:正常签退 3:迟到4:早退 <br>
	 * -1:无效打卡(例如：上班有多次打卡时，不是最早的打卡记录为无效-1) <br>
	 *  注意考勤状态   0:不识别情况，<br>
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据<br>
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别<br>
	 * */
	@Column(name = "C_ATTENDANCE_STATUS")
	private int attendanceStatus;

	/** 考勤日期 （上班日期yyyy-MM-dd）*/
	@Column(name = "D_ATTENDANCE_DATE")
	private Date attendanceDate;

	/**
	 * @param 考勤时间
	 * 
	 */
	public void setSigntime(Date signtime) {
		this.signtime = signtime;
	}

	/**
	 * @param 考勤地点
	 * 
	 */
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	/**
	 * @param 地点经度
	 * 
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @param 地点纬度
	 * 
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param 部门
	 * 
	 */
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	/**
	 * @param 项目组
	 * 
	 */
	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	/**
	 * @param 用户名
	 * 
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param 考勤类型
	 *            类型: 0公司 1:外派 2:移动签到
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param 状态
	 * 
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param 签到手机类型
	 *            0:andorid 1:ios
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @param 有效性
	 * 
	 */
	public void setVilid(String vilid) {
		this.vilid = vilid;
	}

	/**
	 * @param 签到地点
	 *            签到地点 0:公司 1:广发 2:其他
	 */
	public void setSignwhere(String signwhere) {
		this.signwhere = signwhere;
	}

	/**
	 * @param 处理类型
	 *            0:未处理 1:已处理
	 */
	public void setDealstatus(String dealstatus) {
		this.dealstatus = dealstatus;
	}

	/**
	 * @param 考勤
	 * 考勤  0:不识别 (该项目没有填写规定的上下班时间) 1:出勤 2:退勤3:迟到4:早退 <br>
	 * -1:无效打卡(例如：上班有多次打卡时，不是最早的打卡记录为无效-1) <br>
	 *  注意考勤状态   0:不识别情况，<br>
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据<br>
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别<br>
	 */
	public void setAttendanceStatus(int attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	/**
	 * @param 考勤日期
	 *            考勤日期（yyyy-mm-dd）
	 */
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	/**
	 * 
	 * @return 考勤时间
	 */
	public Date getSigntime() {
		return signtime;
	}

	/**
	 * 
	 * @return 考勤地点
	 */
	public String getAreaname() {
		return areaname;
	}

	/**
	 * 
	 * @return 地点经度
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @return 地点纬度
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @return 部门
	 */
	public String getDeptname() {
		return deptname;
	}

	/**
	 * 
	 * @return 项目组
	 */
	public String getGrpname() {
		return grpname;
	}

	/**
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 类型: 0公司 1:外派 2:移动签到
	 * 
	 * @return 考勤类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @return 状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 0:andorid 1:ios
	 * 
	 * @return 签到手机类型
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * 
	 * @return 有效性
	 */
	public String getVilid() {
		return vilid;
	}

	/**
	 * 签到地点 0:公司 1:广发 2:其他
	 * 
	 * @return 签到地点
	 */
	public String getSignwhere() {
		return signwhere;
	}

	/**
	 * 0:未处理 1:已处理
	 * 
	 * @return 处理类型
	 */
	public String getDealstatus() {
		return dealstatus;
	}

	/**
	 * 考勤 0:不识别(该项目没有填写规定的上下班时间)  1:出勤 2:退勤3:迟到4:早退 -1:无效打卡
	 * 
	 * 注意考勤状态   0:不识别情况，<br>
	 * 		1.当签到地点为无效地点时，则作为无效的不识别的数据<br>
	 * 		2.如果项目信息管理里面的项目没有配置考勤时间，也会为不识别<br>
	 * 
	 * @return 考勤
	 */
	public int getAttendanceStatus() {
		return attendanceStatus;
	}

	/**
	 * 考勤日期（yyyy-mm-dd）
	 * 
	 * @return 考勤日期
	 */
	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public String getUserid() {
		return userid;
	}

	public String getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @param 用户ID
	 * 
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

}
