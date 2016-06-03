package cn.grgbanking.feeltm.domain.testsys;

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
@Table(name = "new_employee_manage")
public class NewEmployeeManage {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	private String uname;
	@Column(name="USERID")
	private String userId;
	@Column(name="entry_date")
	@Temporal(TemporalType.DATE)
	private Date entryDate;

	@Column(name = "DETNAME")
	private String detName;
	
	@Column(name="group_name")
	private String groupName;

	private String position;

	private String teacher;

	@Column(name="study_status")
	private String studyStatus;

	@Column(name="black_score")
	private double blackScore;

	@Column(name="white_score")
	private double whiteScore;
	
	@Column(name="change_date")
	@Temporal(TemporalType.DATE)
	private Date changeDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	public double getBlackScore() {
		return blackScore;
	}

	public void setBlackScore(double blackScore) {
		this.blackScore = blackScore;
	}

	public double getWhiteScore() {
		return whiteScore;
	}

	public void setWhiteScore(double whiteScore) {
		this.whiteScore = whiteScore;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
