package cn.grgbanking.feeltm.kpipoint.domain;

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
	private double subsum_1;
	private double subsum_2;
	private String status;
	private String dateString;
	private String modifyDate;

	public Perfor(){
		super();
	}
	
	public Perfor(String pno,String id,long rownum,String monthdate,String username,String groupName,
			double subsum,double subsum_1,double subsum_2,String status,String dateString,String modifyDate) {
		//super();
		this.pno = pno;
		this.id = id;
		this.rownum = rownum;
		this.monthdate = monthdate;
		this.username = username;
		this.groupName = groupName;
		this.subsum = subsum;
		this.subsum_1 = subsum_1;
		this.subsum_2 = subsum_2;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getSubsum_1() {
		return subsum_1;
	}

	public void setSubsum_1(double subsum_1) {
		this.subsum_1 = subsum_1;
	}

	public double getSubsum_2() {
		return subsum_2;
	}

	public void setSubsum_2(double subsum_2) {
		this.subsum_2 = subsum_2;
	}
	
}