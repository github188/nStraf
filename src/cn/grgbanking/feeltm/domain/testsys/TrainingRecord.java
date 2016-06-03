package cn.grgbanking.feeltm.domain.testsys;

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
@Table(name="train_record")
public class TrainingRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	private String cid;
	
	@Column(name="course_name")
	private String courseName;
	
	private String teacher;
	
	private String category;  //内训，外训，外派
	
	@Column(name="train_hour")
	private String trainHour;
	
	@Temporal(TemporalType.DATE)
	@Column(name="train_date")
	private Date trainDate;
	
	@Column(name="start11")
	private String start;  //培训开始时间 2019-09-19 23:21:11
	@Column(name="end11")
	private String end;   //
	
	private String addr;
	@Column(name="studentids")
	private String studentids;
	@Column(name="student")
	private String student;
	
	private String note;
	
	@Column(name="update_man")
	private String updateMan;
	
	@Column(name="update_date")
	private String updateDate;
	
	@Column(name="bound")
	private String bound;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTrainHour() {
		return trainHour;
	}

	public void setTrainHour(String trainHour) {
		this.trainHour = trainHour;
	}

	

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getStudentids() {
		return studentids;
	}

	public void setStudentids(String studentids) {
		this.studentids = studentids;
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

	
	public Date getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(Date trainDate) {
		this.trainDate = trainDate;
	}

	public String getBound() {
		return bound;
	}

	public void setBound(String bound) {
		this.bound = bound;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
	
	
}
