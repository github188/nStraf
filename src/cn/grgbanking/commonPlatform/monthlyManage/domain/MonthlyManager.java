package cn.grgbanking.commonPlatform.monthlyManage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 月度管理报告信息表
 * @author zzhui1
 *
 */
@Entity
@Table(name = "OA_MONTHLY_MANAGER")
public class MonthlyManager {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;

	/** 年份 */
	@Column(name = "C_YEAR")
	private String year;

	/** 月份 */
	@Column(name = "C_MONTH")
	private String month;

	/** 月度部门信息 */
	@Column(name = "C_DEPT_INFO")
	private String deptInfo;

	/** 月度员工流动信息 */
	@Column(name = "C_STAFF_CHANGE_INFO")
	private String staffChangeInfo;

	/** 月度合同信息 */
	@Column(name = "C_CONTRACT_INFO")
	private String contractInfo;

	/** 月度管理项目信息 */
	@Column(name = "C_PROJECT_INFO")
	private String projectInfo;

	/** 月度优秀员工信息 */
	@Column(name = "C_GOOD_USER_INFO")
	private String goodUserInfo;

	/** 月度考勤状态信息 */
	@Column(name = "C_ATTENDANCE_INFO")
	private String attendanceInfo;
	
	/** 月度管理总数统计信息 */
	@Column(name = "C_TOTAL_INFO")
	private String totalInfo;

	/**
	 * @param ID
	 * 
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param 年份
	 *            yyyy
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @param 月份
	 *            MM
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @param 月度部门信息
	 *            JSON格式
	 */
	public void setDeptInfo(String deptInfo) {
		this.deptInfo = deptInfo;
	}

	/**
	 * @param 月度员工流动信息
	 *            JSON格式
	 */
	public void setStaffChangeInfo(String staffChangeInfo) {
		this.staffChangeInfo = staffChangeInfo;
	}

	/**
	 * @param 月度合同信息
	 *            JSON格式
	 */
	public void setContractInfo(String contractInfo) {
		this.contractInfo = contractInfo;
	}

	/**
	 * @param 月度管理项目信息
	 *            JSON格式
	 */
	public void setProjectInfo(String projectInfo) {
		this.projectInfo = projectInfo;
	}

	/**
	 * @param 月度优秀员工信息
	 *            JSON格式
	 */
	public void setGoodUserInfo(String goodUserInfo) {
		this.goodUserInfo = goodUserInfo;
	}

	/**
	 * @param 月度考勤状态信息
	 *            JSON格式
	 */
	public void setAttendanceInfo(String attendanceInfo) {
		this.attendanceInfo = attendanceInfo;
	}

	/**
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * yyyy
	 * 
	 * @return 年份
	 */
	public String getYear() {
		return year;
	}

	/**
	 * MM
	 * 
	 * @return 月份
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度部门信息
	 */
	public String getDeptInfo() {
		return deptInfo;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度员工流动信息
	 */
	public String getStaffChangeInfo() {
		return staffChangeInfo;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度合同信息
	 */
	public String getContractInfo() {
		return contractInfo;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度管理项目信息
	 */
	public String getProjectInfo() {
		return projectInfo;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度优秀员工信息
	 */
	public String getGoodUserInfo() {
		return goodUserInfo;
	}

	/**
	 * JSON格式
	 * 
	 * @return 月度考勤状态信息
	 */
	public String getAttendanceInfo() {
		return attendanceInfo;
	}

	/**
	 * @return 月度管理总数统计信息
	 */
	public String getTotalInfo() {
		return totalInfo;
	}

	/**
	 * @param totalInfo 月度管理总数统计信息
	 */
	public void setTotalInfo(String totalInfo) {
		this.totalInfo = totalInfo;
	}
}
