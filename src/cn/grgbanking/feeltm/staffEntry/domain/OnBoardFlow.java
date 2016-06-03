package cn.grgbanking.feeltm.staffEntry.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_ONBOARD_FLOW")
public class OnBoardFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;

	@Column(name="C_USERID")
	private String userId;

	@Column(name = "C_USERNAME")
	private String userName;

	@Temporal(TemporalType.DATE)
	@Column(name = "D_GRG_BEGINDATE")
	private Date grgBeginDate;

	@Column(name = "C_DETNAME")
	private String detName;
	
	@Column(name = "C_GROUPNAME")
	private String groupName;

	@Temporal(TemporalType.DATE)
	@Column(name = "D_REGULAR_DATE")
	private Date regularDate;

	@Column(name = "C_NOTE")
	private String note;
	
	@Column(name = "C_UPDATEMAN")
	private String updateMan;
	
	@Column(name="C_UPDATE")
	private Date updateTime;
    
	@Column(name="C_EXTEND_STATUS")
	private String extendStatus;
	
	@Column(name="I_SERIALNUMBER")
	private Long serialNumber;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getGrgBeginDate() {
		return grgBeginDate;
	}

	public void setGrgBeginDate(Date grgBeginDate) {
		this.grgBeginDate = grgBeginDate;
	}

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public Date getRegularDate() {
		return regularDate;
	}

	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}



	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getExtendStatus() {
		return extendStatus;
	}

	public void setExtendStatus(String extendStatus) {
		this.extendStatus = extendStatus;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

	
}
