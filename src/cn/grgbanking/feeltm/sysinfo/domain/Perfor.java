package cn.grgbanking.feeltm.sysinfo.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;


public class Perfor implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private long rownum;
	private String username;
	private String groupname;
	private String workstation;
	private Date workingdate;
	private Date startworkingdate;
	private double grgage;
	private double sumage;
	private String education;
	private String mobile;

	private String birthplace;
	private String status;
	private String qq;
	public Perfor(){
		super();
	}
	
	public Perfor(String id,long rownum,String username,String groupname,
			String workstation,Date workingdate,Date startworkingdate,String education
			,String mobile,String birthplace,String status,String qq) {
		//super();
		this.id = id;
		this.rownum = rownum;
		this.username = username;
		this.groupname = groupname;
		this.workstation = workstation;
		this.workingdate = workingdate;
		this.startworkingdate  = startworkingdate;
		Date NowDate = new Date();
		DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
		forma.applyPattern("0.0");


		if(workingdate!=null){
			double grgagetmp = 0;
			grgagetmp = NowDate.getTime() - workingdate.getTime();
			this.grgage =  grgagetmp / (24 * 60 * 60 * 1000)/365; 
			this.grgage=Double.parseDouble(forma.format(this.grgage));
		}
		else
		{
			this.grgage = 0;
		}
		if(startworkingdate!=null){  
			double sumagetmp = 0;
			sumagetmp = NowDate.getTime() - startworkingdate.getTime();
			this.sumage = sumagetmp / (24 * 60 * 60 * 1000)/365;
			this.sumage=Double.parseDouble(forma.format(this.sumage));
		}
		else
		{
			this.sumage = 0;
		}
		this.education = education;
		this.mobile = mobile;
		this.birthplace = birthplace;
		this.status = status;
		this.qq = qq;
	}

	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	

	public double getGrgage() {
		return grgage;
	}

	public void setGrgage(double grgage) {
		this.grgage = grgage;
	}

	public double getSumage() {
		return sumage;
	}

	public void setSumage(double sumage) {
		this.sumage = sumage;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getWorkingdate() {
		return workingdate;
	}

	public void setWorkingdate(Date workingdate) {
		this.workingdate = workingdate;
	}

	public Date getStartworkingdate() {
		return startworkingdate;
	}

	public void setStartworkingdate(Date startworkingdate) {
		this.startworkingdate = startworkingdate;
	}

}