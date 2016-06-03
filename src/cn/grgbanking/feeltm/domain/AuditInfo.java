package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AUDIT_INFO")
public class AuditInfo implements Serializable {
	private String auditStatus;
	private String orgId;
	private String applyName;
	private String applayDate;
	private String applytyp;
	private String id;
	private String applyId;

	public AuditInfo(String auditStatus, String orgId, String applyName,
			String applayDate, String applytyp, String id, String applyId) {
		this.auditStatus = auditStatus;
		this.orgId = orgId;
		this.applyName = applyName;
		this.applayDate = applayDate;
		this.applytyp = applytyp;
		this.id = id;
		this.applyId = applyId;
	}

	public AuditInfo() {
	}

	@Column(name = "C_AUDIT_STATUS")
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name = "C_ORG_ID")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "C_APPLY_NAME")
	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	@Column(name = "C_APPLAY_DATE")
	public String getApplayDate() {
		return applayDate;
	}

	public void setApplayDate(String applayDate) {
		this.applayDate = applayDate;
	}

	@Column(name = "C_APPLYTYP")
	public String getApplytyp() {
		return applytyp;
	}

	public void setApplytyp(String applytyp) {
		this.applytyp = applytyp;
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
}
