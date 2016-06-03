package cn.grgbanking.feeltm.performance.domain;

import java.io.Serializable;


public class Perfor implements Serializable{
	private static final long serialVersionUID = 1L;
	private String pno;
	private String id;
	private long rownum;
	private String monthdate;
	private String username;
	private String groupName;
	private double subsum;
	private String remarks;
	private String dateString;
	private String modifyDate;

	public Perfor(){
		super();
	}
	
	public Perfor(String pno,String id,long rownum,String monthdate,String username,String groupName,
			double subsum,String remarks,String dateString,String modifyDate) {
		//super();
		this.pno = pno;
		this.id = id;
		this.rownum = rownum;
		this.monthdate = monthdate;
		this.username = username;
		this.groupName = groupName;
		this.remarks = remarks;
		this.subsum = subsum;
		this.dateString = dateString;
		this.modifyDate = modifyDate;
	}

	public String getPno() {
		return pno;
	}

	public String getId() {
		return id;
	}

	public String getMonthdate() {
		return monthdate;
	}

	public String getUsername() {
		return username;
	}

	public String getGroupName() {
		return groupName;
	}

	public double getSubsum() {
		return subsum;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getDateString() {
		return dateString;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMonthdate(String monthdate) {
		this.monthdate = monthdate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setSubsum(double subsum) {
		this.subsum = subsum;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}


	
	
}