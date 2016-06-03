package cn.grgbanking.feeltm.auto.domain;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * 执行者</div></td>
	开始时间</div></td>
	结束时间</div></td>
	执行时间</div></td>
           执行状态</div></td>
            项目名称</div></td>
 * @author feel
 *
 */
public class AutoListInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String username;
	private String startTime;
	private String endTime;
	private String execTime;
	private String status;
	private String prjName;
	private String testType;
	private String machineNo;
	private String versionNo;
	private String standard;
	private String execIP;
	private String execPort;
	private String allScripts;
	private String failScripts;
	private String rankScripts;
	
	public AutoListInfo(){
		
	}
	
	
	public AutoListInfo(String id, String username, String startTime,
			String endTime, String execTime, String status, String prjName,String testType,String machineNo,String versionNo) {
		super();
		this.id = id;
		this.username = username;
		this.startTime = startTime;
		this.endTime = endTime;
		this.execTime = execTime;
		this.status = status;
		this.prjName = prjName;
		this.testType=testType;
		this.machineNo=machineNo;
		this.versionNo=versionNo;
	}
	
	public AutoListInfo(String username, String startTime,
			String endTime, String execTime, String status, String prjName,String testType,String machineNo,String versionNo,String standard,String execIP) {
		super();
		this.username = username;
		this.startTime = startTime;
		this.endTime = endTime;
		this.execTime = execTime;
		this.status = status;
		this.prjName = prjName;
		this.testType=testType;
		this.machineNo=machineNo;
		this.versionNo=versionNo;
		this.standard=standard;
		this.execIP=execIP;
	}
	
	public AutoListInfo(String username, String startTime,
			String endTime, String execTime, String status, String prjName,String testType,String machineNo,String versionNo,String standard,String execIP, String execPort) {
		super();
		this.username = username;
		this.startTime = startTime;
		this.endTime = endTime;
		this.execTime = execTime;
		this.status = status;
		this.prjName = prjName;
		this.testType=testType;
		this.machineNo=machineNo;
		this.versionNo=versionNo;
		this.standard=standard;
		this.execIP=execIP;
		this.execPort = execPort;
	}
	
	public AutoListInfo(String id, String username, String startTime,
			String endTime, String execTime, String status, String prjName,String testType,String machineNo,String versionNo, String allScripts, String failScripts, String rankScripts) {
		super();
		this.id = id;
		this.username = username;
		this.startTime = startTime;
		this.endTime = endTime;
		this.execTime = execTime;
		this.status = status;
		this.prjName = prjName;
		this.testType=testType;
		this.machineNo=machineNo;
		this.versionNo=versionNo;
		this.allScripts = allScripts;
		this.failScripts = failScripts;
		this.rankScripts = rankScripts;
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
		AutoListInfo other = (AutoListInfo) obj;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
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
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
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


	public String getStandard() {
		return standard;
	}


	public void setStandard(String standard) {
		this.standard = standard;
	}


	public String getExecIP() {
		return execIP;
	}


	public void setExecIP(String execIP) {
		this.execIP = execIP;
	}


	public String getExecPort() {
		return execPort;
	}


	public void setExecPort(String execPort) {
		this.execPort = execPort;
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
