package cn.grgbanking.feeltm.testrecord.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * wtjiao 2014年6月19日 下午12:52:05
 */
@Entity
@Table(name = "testquestion")
public class TestQuestion {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name = "serial_no")
	private String serialNo;
	
	@Column(name = "project_name")
	private String projectName;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "modual_name")
	private String modualName;
	
	@Column(name = "find_man")
	private String findMan;
	
	@Temporal(TemporalType.DATE)
	@Column(name="find_date")
	private Date findDate;
	
	@Column(name = "question_status")
	private String questionStatus;
	
	@Column(name = "question_desc")
	private String questionDesc;
	
	@Column(name = "solove_man")
	private String soloveMan;
	
	@Temporal(TemporalType.DATE)
	@Column(name="solove_date")
	private Date soloveDate;
	
	@Column(name = "finish_rate")
	private String finishRate;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "update_man")
	private String updateMan;

	@Column(name = "update_date")
	private String updateDate;
	
	@Column(name = "QUESTION_URGE")
	private String urge;
	
	@Column(name = "URGE_LEVEL")
	private Integer urgeLevel;
	
	
	@Column(name = "DEPLOY_STATUS")
	private String deployStatus;
	
	@Column(name = "BUG_LEVEL")
	private String buglevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModualName() {
		return modualName;
	}

	public void setModualName(String modualName) {
		this.modualName = modualName;
	}

	public String getFindMan() {
		return findMan;
	}

	public void setFindMan(String findMan) {
		this.findMan = findMan;
	}

	public Date getFindDate() {
		return findDate;
	}

	public void setFindDate(Date findDate) {
		this.findDate = findDate;
	}

	public String getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getQuestionDesc() {
		return questionDesc;
	}

	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}

	public String getSoloveMan() {
		return soloveMan;
	}

	public void setSoloveMan(String soloveMan) {
		this.soloveMan = soloveMan;
	}

	public Date getSoloveDate() {
		return soloveDate;
	}

	public void setSoloveDate(Date soloveDate) {
		this.soloveDate = soloveDate;
	}

	public String getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
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

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUrge() {
		return urge;
	}

	public void setUrge(String urge) {
		this.urge = urge;
	}

	public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

	public Integer getUrgeLevel() {
		return urgeLevel;
	}

	public void setUrgeLevel(Integer urgeLevel) {
		this.urgeLevel = urgeLevel;
	}

	public String getBuglevel() {
		return buglevel;
	}

	public void setBuglevel(String buglevel) {
		this.buglevel = buglevel;
	}
	
}
