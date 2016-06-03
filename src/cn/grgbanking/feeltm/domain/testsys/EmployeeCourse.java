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
@Table(name = "employee_course")
public class EmployeeCourse {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	private String uname;
	
	@Column(name="new_employee_id",updatable=false)
	private String newEmployeeId;
	
	private String cid;
	
	@Column(name="course_name")
	private String courseName;
	
	private String category; // 必修 选修
	
	@Column(name="plan_finish_date")
	@Temporal(TemporalType.DATE)
	private Date planFinishDate;
	
	@Column(name="grasp_standard")
	private String graspStandard;

	@Column(name="priory_level")
	private String prioryLevel;
	
	@Column(name="finish_percent")
	private String finishPercent;
	
	@Column(name="actual_finish_date")
	@Temporal(TemporalType.DATE)
	private Date actualFinishDate;
	
	@Column(name="finish_effect")
	private String finishEffect;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getPlanFinishDate() {
		return planFinishDate;
	}

	public void setPlanFinishDate(Date planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	public String getGraspStandard() {
		return graspStandard;
	}

	public void setGraspStandard(String graspStandard) {
		this.graspStandard = graspStandard;
	}

	public String getPrioryLevel() {
		return prioryLevel;
	}

	public void setPrioryLevel(String prioryLevel) {
		this.prioryLevel = prioryLevel;
	}

	public String getFinishPercent() {
		return finishPercent;
	}

	public void setFinishPercent(String finishPercent) {
		this.finishPercent = finishPercent;
	}

	public Date getActualFinishDate() {
		return actualFinishDate;
	}

	public void setActualFinishDate(Date actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}

	public String getFinishEffect() {
		return finishEffect;
	}

	public void setFinishEffect(String finishEffect) {
		this.finishEffect = finishEffect;
	}
	

	public String getNewEmployeeId() {
		return newEmployeeId;
	}

	public void setNewEmployeeId(String newEmployeeId) {
		this.newEmployeeId = newEmployeeId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
