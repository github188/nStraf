package cn.grgbanking.feeltm.hols.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 假期数据
 * @author lhyan 2014-5-12
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="USR_HOLS")
public class UserHols implements Serializable{
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name="C_ID", unique = true, nullable = false)
	private String id;
	@Column(name="C_USERID")
	private String userid;//用户标识
	@Column(name="C_USERNAME")
	private String username;//姓名
	@Column(name="C_DEPTNAME")
	private String deptName;//部门
	@Column(name="C_GROUPNAME")
	private String groupName;//组别
	@Column(name="I_DEFERRED_TIME")
	private double deferredTime=0.0;//调休时间
	@Column(name="I_YEARHOLS_TIME")
	private double yearholsTime=0.0;//年假
	@Column(name="I_FLAG")
	private int flag=0;//0未删除 1删除
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public double getDeferredTime() {
		return deferredTime;
	}
	public void setDeferredTime(double deferredTime) {
		this.deferredTime = deferredTime;
	}
	public double getYearholsTime() {
		return yearholsTime;
	}
	public void setYearholsTime(double yearholsTime) {
		this.yearholsTime = yearholsTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
