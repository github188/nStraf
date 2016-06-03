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

@SuppressWarnings("serial")
@Entity
@Table(name = "OA_USR_CONTACTS")
public class UsrContacts implements Serializable {
	
	private String id;
	private String userId;
	private String conName;
	private String deptName;
	private String groupName;
	private String conMobile;
	private String conTel;
	private String note;
	private String updateManId;
	private Date update;
	private String conEmail;
	
	/** defalut constructor*/
	public UsrContacts() {
	}
	/**  full constructor  */
	public UsrContacts(String id, String userId, String conName,
			String deptName, String groupName, String conMobile, String conTel,
			String note, String updateManId, Date update,String conEmail) {
		super();
		this.id = id;
		this.userId = userId;
		this.conName = conName;
		this.deptName = deptName;
		this.groupName = groupName;
		this.conMobile = conMobile;
		this.conTel = conTel;
		this.note = note;
		this.updateManId = updateManId;
		this.update = update;
		this.conEmail = conEmail;
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
	
	@Column(name="C_CONNAME")
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	
	@Column(name="C_CONTEL")
	public String getConTel() {
		return conTel;
	}
	public void setConTel(String conTel) {
		this.conTel = conTel;
	}
	
	@Column(name="C_CONMOBILE")
	public String getConMobile() {
		return conMobile;
	}
	public void setConMobile(String conMobile) {
		this.conMobile = conMobile;
	}
	
	@Column(name="C_USERID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Transient
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@Transient
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name="C_NOTE")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name="C_UPDATEMAN")
	public String getUpdateManId() {
		return updateManId;
	}
	public void setUpdateManId(String updateManId) {
		this.updateManId = updateManId;
	}
	
	@Column(name="D_UPDATE")
	public Date getUpdate() {
		return update;
	}
	public void setUpdate(Date update) {
		this.update = update;
	}
	
	@Transient
	public String getConEmail() {
		return conEmail;
	}
	public void setConEmail(String conEmail) {
		this.conEmail = conEmail;
	}
	
}
