package cn.grgbanking.feeltm.personDay.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="oa_personday_dept")
public class DeptMonthPersonDay {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	/** 年份 */
	@Column(name = "c_year")
	private String year;
	/** 月份 */
	@Column(name = "c_month")
	private String month;
	/** 部门id */
	@Column(name = "c_deptId")
	private String deptId;
	/** 部门名称 */
	@Column(name = "c_deptName")
	private String deptName;
	/** 确认人日 */
	@Column(name = "c_personday")
	private String personDay;
	
	@Column(name="EXT1")
	private String ext1;
	
	@Column(name="EXT2")
	private Date ext2;
	
	@Column(name="D_CREATE_TIME")
	private Date createTime;//更新时间

	@Transient
	private String color;
	
	public DeptMonthPersonDay(){
		
	}
	
	public DeptMonthPersonDay(String deptName,String personDay){
		this.deptName=deptName;
		this.personDay=personDay;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPersonDay() {
		return personDay;
	}

	public void setPersonDay(String personDay) {
		this.personDay = personDay;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public Date getExt2() {
		return ext2;
	}

	public void setExt2(Date ext2) {
		this.ext2 = ext2;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
