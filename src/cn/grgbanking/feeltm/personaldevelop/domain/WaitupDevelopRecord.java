package cn.grgbanking.feeltm.personaldevelop.domain;

import java.io.Serializable;

/**
 *     
 *
 */
public class WaitupDevelopRecord implements Serializable,Comparable<WaitupDevelopRecord>{
	private String capabilityExplain;
	private String taskDesc;

	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	public String getCapabilityExplain() {
		return capabilityExplain;
	}
	public void setCapabilityExplain(String capabilityExplain) {
		this.capabilityExplain = capabilityExplain;
	}
	@Override
	public int compareTo(WaitupDevelopRecord that) {
		//项目名称升序
		int	result=that.getTaskDesc().compareTo(this.getTaskDesc());
		if(result==0){
			 result=this.getTaskDesc().compareTo(that.getTaskDesc());
			//result=this.getPrjType().compareTo(that.getPrjType());
		}
		return result;
	}
	
}
