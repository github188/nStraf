package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="month_mission")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class MonthMission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	
	@Column(name = "month_date")
	private String monthDate; //月份
	@Column(name = "current_name")
	private String currentName; //姓名
	@Column(name = "group_name")
	private String groupName; //组别
	@Column(name = "exam_score")
	private String examScore; //考核分
	@Column(name = "exam_rank")
	private String examRank;//考核等级
	@Column(name = "note")
	private String note; //备注
	@Column(name = "edit_lock")
	private String editLock; //提交
	
	public MonthMission(){
		
	}

	public MonthMission(String id) {
		//super();
		this.id = id;
	}
	

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getExamScore() {
		return examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public String getExamRank() {
		return examRank;
	}

	public void setExamRank(String examRank) {
		this.examRank = examRank;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEditLock() {
		return editLock;
	}

	public void setEditLock(String editLock) {
		this.editLock = editLock;
	}
	
}
