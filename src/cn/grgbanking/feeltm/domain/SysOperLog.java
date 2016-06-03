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
@Table(name = "SYS_OPER_LOG")
public class SysOperLog implements Serializable {

	/** identifier field */
	private String id;

	/** nullable persistent field */
	private String loginid;

	/** nullable persistent field */
	private String userid;

	/** nullable persistent field */
	private String username;

	/** nullable persistent field */
	private Date logtime;

	/** nullable persistent field */
	private String type;

	/** nullable persistent field */
	private String result;

	/** nullable persistent field */
	private String menuid;

	/** nullable persistent field */
	private String operid;

	/** nullable persistent field */
	private String note;

	private String orgfloor;

	/** full constructor */
	public SysOperLog(String loginid, String userid, String username,
			Date logtime, String type, String result, String menuid,
			String operid, String note, String orgfloor) {
		this.loginid = loginid;
		this.userid = userid;
		this.username = username;
		this.logtime = logtime;
		this.type = type;
		this.result = result;
		this.menuid = menuid;
		this.operid = operid;
		this.note = note;
		this.orgfloor = orgfloor;
	}

	/** default constructor */
	public SysOperLog() {
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "C_LOGINID")
	public String getLoginid() {
		return this.loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	@Column(name = "C_USERID")
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "C_USERNAME")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "D_LOGTIME")
	public Date getLogtime() {
		return this.logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}

	@Column(name = "C_TYPE")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "C_RESULT")
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "C_MENUID")
	public String getMenuid() {
		return this.menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	@Column(name = "C_OPERID")
	public String getOperid() {
		return this.operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	@Column(name = "C_NOTE")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "C_ORGFLOOR")
	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}
}
