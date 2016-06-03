package cn.grgbanking.feeltm.workhour.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "oa_daylog")
public class WorkhourSummary {
	private String id;
	private String prjName;
	private String username;
	private String deptname;
	private String groupname;
	private String type;
	private double total;
	private double daywork = 0;
	private double requirement = 0;
	private double design = 0;
	private double code = 0;
	private double test = 0;
	private double managerment = 0;
	private double document = 0;
	private double meet = 0;
	private double train = 0;
	private double other = 0;
	private double peopletotal = 0;
	private double sumtotal = 0;
	private double subtotal = 0;
	private Date logdate;
	private String userlevel;
	public WorkhourSummary() {

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
	@Column(name="c_prjname")
	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	@Column(name="c_username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="c_detname")
	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	@Column(name="c_groupname")
	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	@Column(name="c_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name="d_subtotal")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	@Transient
	public double getDaywork() {
		return daywork;
	}

	public void setDaywork(double daywork) {
		this.daywork = daywork;
	}
	@Transient
	public double getRequirement() {
		return requirement;
	}

	public void setRequirement(double requirement) {
		this.requirement = requirement;
	}
	@Transient
	public double getDesign() {
		return design;
	}

	public void setDesign(double design) {
		this.design = design;
	}
	@Transient
	public double getCode() {
		return code;
	}

	public void setCode(double code) {
		this.code = code;
	}
	@Transient
	public double getTest() {
		return test;
	}

	public void setTest(double test) {
		this.test = test;
	}
	@Transient
	public double getManagerment() {
		return managerment;
	}

	public void setManagerment(double managerment) {
		this.managerment = managerment;
	}
	@Transient
	public double getDocument() {
		return document;
	}

	public void setDocument(double document) {
		this.document = document;
	}
	@Transient
	public double getMeet() {
		return meet;
	}

	public void setMeet(double meet) {
		this.meet = meet;
	}
	@Transient
	public double getTrain() {
		return train;
	}

	public void setTrain(double train) {
		this.train = train;
	}
	@Transient
	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}
	@Transient
	public double getPeopletotal() {
		return peopletotal;
	}

	public void setPeopletotal(double peopletotal) {
		this.peopletotal = peopletotal;
	}
	@Transient
	public double getSumtotal() {
		return sumtotal;
	}

	public void setSumtotal(double sumtotal) {
		this.sumtotal = sumtotal;
	}
	@Transient
	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	@Column(name="d_logdate")
	@Temporal(TemporalType.DATE)
	public Date getLogdate() {
		return logdate;
	}

	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}
	@Column(name="c_userlevel")
	public String getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}

}
