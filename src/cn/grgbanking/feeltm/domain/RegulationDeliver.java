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
@Table(name = "REGULATION_DELIVER")
public class RegulationDeliver implements Serializable {
	private String id;
	private String termid;
	private String role;
	private String version;
	private String date;
	private String status;
	private String applyId;
	// 规则类型
	private String type;
	// 规则下发时间
	private String deliverDate;
	
	// 备份撤销前的规则
	private String note;

	@Column(name = "C_NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "C_DELIVER_DATE")
	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

	public RegulationDeliver(String id, String termid, String role,
			String version, String date) {
		this.id = id;
		this.termid = termid;
		this.role = role;
		this.version = version;
		this.date = date;
	}

	public RegulationDeliver() {
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

	@Column(name = "C_TERMID")
	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	@Column(name = "C_ROLE")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "C_VERSION")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "C_DATE")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(name = "C_REGU_STATUS")
	public String getStatus() {
		return status;
	}

	@Column(name = "C_APPLY_ID")
	public String getApplyId() {
		return applyId;
	}

	@Column(name = "C_REG_TYPE")
	public String getType() {
		return type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public void setType(String type) {
		this.type = type;
	}

}
