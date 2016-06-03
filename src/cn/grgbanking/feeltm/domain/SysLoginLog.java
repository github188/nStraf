package cn.grgbanking.feeltm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_LOGIN_LOG")
public class SysLoginLog {

	/** identifier field */
	private String loginid;

	/** persistent field */
	private String hostip;

	/** persistent field */
	private String userid;

	/** nullable persistent field */
	private String username;

	/** persistent field */
	private Date logintime;

	/** nullable persistent field */
	private Date logouttime;

	/** persistent field */
	private String result;

	/** nullable persistent field */
	private String note;
	
	private String type;//手机端还是web端

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_LOGINID", unique = true, nullable = false)
	public String getLoginid() {
		return this.loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	@Column(name = "C_HOSTIP")
	public String getHostip() {
		return this.hostip;
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
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

	@Column(name = "D_LOGINTIME")
	public Date getLogintime() {
		return this.logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	@Column(name = "D_LOGOUTTIME")
	public Date getLogouttime() {
		return this.logouttime;
	}

	public void setLogouttime(Date logouttime) {
		this.logouttime = logouttime;
	}

	@Column(name = "C_RESULT")
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "C_NOTE")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name="C_TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
