package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog implements Serializable {
	private String id;
	private String applyId;
	private String userid;
	private String username;
	private Date applayDate;
	private String orgid;
	private String applyType;
	private String applyStatus;
	private String applyResult;
	private String applyNote;

	public AuditLog(String id, String applyId, String userid, String username,
			Date applayDate, String orgid, String applyType,
			String applyStatus, String applyResult, String applyNote) {
		this.id = id;
		this.applyId = applyId;
		this.userid = userid;
		this.username = username;
		this.applayDate = applayDate;
		this.orgid = orgid;
		this.applyType = applyType;
		this.applyStatus = applyStatus;
		this.applyResult = applyResult;
		this.applyNote = applyNote;
	}

	public AuditLog() {
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "C_APPLY_ID")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "C_USERID")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "C_USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "D_APPLAY_DATE")
	public Date getApplayDate() {
		return applayDate;
	}

	public void setApplayDate(Date applayDate) {
		this.applayDate = applayDate;
	}

	@Column(name = "C_ORGID")
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	@Column(name = "C_APPLY_TYPE")
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	@Column(name = "C_APPLY_STATUS")
	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	@Column(name = "C_APPLY_RESULT")
	public String getApplyResult() {
		return applyResult;
	}

	public void setApplyResult(String applyResult) {
		this.applyResult = applyResult;
	}

	@Column(name = "C_APPLY_NOTE")
	public String getApplyNote() {
		return applyNote;
	}

	public void setApplyNote(String applyNote) {
		this.applyNote = applyNote;
	}
}
