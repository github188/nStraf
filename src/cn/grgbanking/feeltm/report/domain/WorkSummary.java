package cn.grgbanking.feeltm.report.domain;

import java.io.Serializable;

public class WorkSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	// private String username;
	// private String groupName;
	private String prjName;
	private double managerment = 0;
	private double requirement = 0;
	private double design = 0;
	private double code = 0;
	private double test = 0;
	private double other = 0;
	private double project = 0;
	private double subtotal = 0;
	private Long personNum;

	public WorkSummary() {

	}

	public WorkSummary(String prjName, double managerment, double requirement,
			double design, double code, double test, double other,
			double project, double subtotal,Long personNum) {
		super();

		this.prjName = prjName;
		this.managerment = managerment;
		this.requirement = requirement;
		this.design = design;
		this.code = code;
		this.test = test;
		this.other = other;
		this.project = project;
		this.subtotal = subtotal;
		this.personNum=personNum;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public double getManagerment() {
		return managerment;
	}

	public void setManagerment(double managerment) {
		this.managerment = managerment;
	}

	public double getRequirement() {
		return requirement;
	}

	public void setRequirement(double requirement) {
		this.requirement = requirement;
	}

	public double getDesign() {
		return design;
	}

	public void setDesign(double design) {
		this.design = design;
	}

	public double getTest() {
		return test;
	}

	public void setTest(double test) {
		this.test = test;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public double getProject() {
		return project;
	}

	public void setProject(double project) {
		this.project = project;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getCode() {
		return code;
	}

	public void setCode(double code) {
		this.code = code;
	}

	public long getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Long personNum) {
		this.personNum = personNum;
	}

	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	

	
}
