package cn.grgbanking.feeltm.workhour.domain;


public class WorkSummary {

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
	private String people;
	private String userlevel;

	public WorkSummary() {

	}

	public WorkSummary(String username,String deptname,String groupname,String prjName,String type,double total,String people,String userlevel) {
		super();
		this.prjName=prjName;
		this.username=username;
		this.deptname=deptname;
		this.groupname=groupname;
		this.type=type;
		this.total=total;
		this.people=people;
		this.userlevel = userlevel;
	}
	
	public WorkSummary(String deptname,String prjName,String type,double total,double peopletotal) {
		this.prjName=prjName;
		this.deptname=deptname;
		this.type=type;
		this.total=total;
		this.peopletotal=peopletotal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getDaywork() {
		return daywork;
	}

	public void setDaywork(double daywork) {
		this.daywork = daywork;
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

	public double getCode() {
		return code;
	}

	public void setCode(double code) {
		this.code = code;
	}

	public double getTest() {
		return test;
	}

	public void setTest(double test) {
		this.test = test;
	}

	public double getManagerment() {
		return managerment;
	}

	public void setManagerment(double managerment) {
		this.managerment = managerment;
	}

	public double getDocument() {
		return document;
	}

	public void setDocument(double document) {
		this.document = document;
	}

	public double getMeet() {
		return meet;
	}

	public void setMeet(double meet) {
		this.meet = meet;
	}

	public double getTrain() {
		return train;
	}

	public void setTrain(double train) {
		this.train = train;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public double getPeopletotal() {
		return peopletotal;
	}

	public void setPeopletotal(double peopletotal) {
		this.peopletotal = peopletotal;
	}

	public double getSumtotal() {
		return sumtotal;
	}

	public void setSumtotal(double sumtotal) {
		this.sumtotal = sumtotal;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}


	
}
