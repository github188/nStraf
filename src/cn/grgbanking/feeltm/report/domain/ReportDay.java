package cn.grgbanking.feeltm.report.domain;
import java.io.Serializable;
import java.util.Date;


public class ReportDay implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date createDate;
	private String username;
	private String groupName;
	private double subsum;
	private String dateString;
	private String modifyDate;

	public ReportDay(){
		super();
	}
	
	public ReportDay(Date createDate, String username, String groupName,
			double subsum,String modifyDate) {
		super();
		this.createDate = createDate;
		this.username = username;
		this.groupName = groupName;
		this.subsum = subsum;
		this.modifyDate = modifyDate;

	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public double getSubsum() {
		return subsum;
	}

	public void setSubsum(double subsum) {
		this.subsum = subsum;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	
	
	
	
	
	
}
