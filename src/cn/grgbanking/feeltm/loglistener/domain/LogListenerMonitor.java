package cn.grgbanking.feeltm.loglistener.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 日志填写监控
 * @author lhyan3
 * 2014年7月23日
 */
@Entity
@Table(name="OA_LOGLISTEN_MONITOR")
public class  LogListenerMonitor{ 
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID")
	private String id;
	
	@Column(name="C_ORGID")
	private String orgId;
	
	@Column(name="C_ORGNAME")
	private String orgName;
	
	@Column(name="c_monitor_id")
	private String monitorId;
	
	@Column(name="c_monitor_name")
	private String monitorName;
	
	@Column(name="c_watched_id")
	private String watchedId;
	
	@Column(name="c_watched_name")
	private String watchedName;
	
	@Column(name="c_autoupdate")
	private String autoUpdate;//1自动更新  0手动更新  -1数据异常（-1表示异常，例如某个项目是手动更新，但后来项目被删除，根据其监控项目id找不到对应项目）
	
	@Column(name="C_ORGTYPE")
	private String orgType;//处理类型，project表示项目，dept表示部门
	
	@Column(name="EXT2")
	private String ext2;
	
	@Column(name="EXT3")
	private String ext3;
	
	@Column(name="EXT4")
	private String ext4;
	
	@Column(name="EXT5")
	private Date ext5;
	
	@Column(name="EXT6")
	private Date ext6;
	
	@Column(name="EXT7")
	private Double ext7;
	
	@Column(name="EXT8")
	private Double ext8;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getWatchedId() {
		return watchedId;
	}

	public void setWatchedId(String watchedId) {
		this.watchedId = watchedId;
	}

	public String getWatchedName() {
		return watchedName;
	}

	public void setWatchedName(String watchedName) {
		this.watchedName = watchedName;
	}

	public String getAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public Date getExt5() {
		return ext5;
	}

	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	public Date getExt6() {
		return ext6;
	}

	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	public Double getExt7() {
		return ext7;
	}

	public void setExt7(Double ext7) {
		this.ext7 = ext7;
	}

	public Double getExt8() {
		return ext8;
	}

	public void setExt8(Double ext8) {
		this.ext8 = ext8;
	}
}
