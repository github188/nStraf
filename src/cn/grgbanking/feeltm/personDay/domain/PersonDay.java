package cn.grgbanking.feeltm.personDay.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_PERSONDAY")
public class PersonDay {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	/** 年份 */
	@Column(name = "c_year")
	private String year;
	/** 月份 */
	@Column(name = "c_month")
	private String month;
	/** 项目id */
	@Column(name = "c_projectId")
	private String projectId;
	/** 项目名称 */
	@Column(name = "c_projectName")
	private String projectName;
	/** 类型  month表示月数据  total表示总数据*/
	@Column(name = "c_type")
	private String type;
	/** 是否修改   true 已经修改   false 尚未修改*/
	@Column(name = "c_is_edit")
	private String isEdit;
	/** 确认人日（修改之前的人日） */
	@Column(name = "c_personday_confirm")
	private String personDayConfirm;
	/** 修改后人日 */
	@Column(name = "c_personday_eidt")
	private String personDayEdit;
	/** 修改备注 */
	@Column(name = "c_note")
	private String note;
	/** 误差人日详情 */
	@Column(name = "c_error")
	private String error;
	/** 误差详情（记录何日何人日志未填写） */
	@Column(name = "c_estimate_detail")
	private String estimateDetail;
	
	/**
	 * 年月  如2014-01
	 */
	@Column(name="EXT1")
	private String ext1;
	
	@Column(name="C_UPDATEUSER_ID")
	private String updateuserId;//更新人userid
	
	@Column(name="C_UPDATEUSER_NAME")
	private String updateUsername;//更新人username
	
	@Column(name="D_UPDATE_TIME")
	private Date updateTime;//更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getPersonDayConfirm() {
		return personDayConfirm;
	}

	public void setPersonDayConfirm(String personDayConfirm) {
		this.personDayConfirm = personDayConfirm;
	}

	public String getPersonDayEdit() {
		return personDayEdit;
	}

	public void setPersonDayEdit(String personDayEdit) {
		this.personDayEdit = personDayEdit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getEstimateDetail() {
		return estimateDetail;
	}

	public void setEstimateDetail(String estimateDetail) {
		this.estimateDetail = estimateDetail;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
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
}
