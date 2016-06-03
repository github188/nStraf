package cn.grgbanking.feeltm.overtime.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="oa_overrecord")
public class OvertimeRecord implements Serializable {
	private String id;
	private String userid;
	private double sumtime;
	private String startdate;
	private String enddate;
	private String oarunid;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "c_id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="c_userid")
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Column(name="i_sumtime")
	public double getSumtime() {
		return sumtime;
	}
	public void setSumtime(double sumtime) {
		this.sumtime = sumtime;
	}
	@Column(name="c_startdate")
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	@Column(name="c_enddate")
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	@Column(name="c_oarunid")
	public String getOarunid() {
		return oarunid;
	}
	public void setOarunid(String oarunid) {
		this.oarunid = oarunid;
	}
	
}
