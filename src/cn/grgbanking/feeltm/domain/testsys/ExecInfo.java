package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 * id		id		uuid		primary key
执行者		username	varchar2(10)	not null
执行机器	net_ip		varchar2(30)
执行机器端口	net_port		varchar2(10)
执行时间(发请求的时间)	exec_time	DateTime
开始时间	start_time	DateTime	
结束时间	end_time		DateTime
执行状态	status		varchar2(20)	not null
项目名称	prj_name		varchar2(50)
支持标准	standard	varchar2(30)
测试类型	test_type	varchar2(30)
测试机型	machine_no	varchar2(30)
版本号		version_no	varchar2(10)
dailyBuild地址	daily_build_addr	varchar2(100)
报告文件	report_file	CLOB
 */
@Entity
@Table(name="AUTO_TEST")
public class ExecInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String username;
	
	@Column(name="net_ip")
	private String netIP;
	
	@Column(name="net_port")
	private String netPort;
	
	@Column(name="exec_time")
	private Date execTime;
	
	@Column(name="start_time")
	private Date startTime;
	
	@Column(name="end_time")
	private Date endTime;
	
	private String status;
	
	@Column(name="prj_name")
	private String prjName;
	private String standard;
	@Column(name="test_type")
	private String testType;
	@Column(name="machine_no")
	private String machineNo;
	@Column(name="version_no")
	private String versionNo;
	@Column(name="daily_build_addr")
	private String dailyBuildAddr;
	@Column(name="report_file")
	private String reportFile;
	
	@Column(name="ALL_SCRIPTS")
	private String allScripts;
	@Column(name="FAIL_SCRIPTS")
	private String failScripts;
	@Column(name="RANK_SCRIPTS")
	private String rankScripts;
	
	
	public ExecInfo(){
		
	}
	
	public ExecInfo(String id){
		this.id=id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecInfo other = (ExecInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNetIP() {
		return netIP;
	}

	public void setNetIP(String netIP) {
		this.netIP = netIP;
	}
	
	public String getNetPort() {
		return netPort;
	}

	public void setNetPort(String netPort) {
		this.netPort = netPort;
	}

	public Date getExecTime() {
		return execTime;
	}

	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getDailyBuildAddr() {
		return dailyBuildAddr;
	}

	public void setDailyBuildAddr(String dailyBuildAddr) {
		this.dailyBuildAddr = dailyBuildAddr;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAllScripts() {
		return allScripts;
	}

	public void setAllScripts(String allScripts) {
		this.allScripts = allScripts;
	}

	public String getFailScripts() {
		return failScripts;
	}

	public void setFailScripts(String failScripts) {
		this.failScripts = failScripts;
	}

	public String getRankScripts() {
		return rankScripts;
	}

	public void setRankScripts(String rankScripts) {
		this.rankScripts = rankScripts;
	}
}
