package cn.grgbanking.feeltm.report.domain;

import java.io.Serializable;

/**
 *     
 *
 */
public class WeekRecord implements Serializable,Comparable<WeekRecord>{
	private String prjName;
	private String taskDesc;
	private String prjType;
	private String responsible;
	private String finishRate;
	private String delayReason;
	private String audit;
	private String file;
	
	
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getPrjType() {
		return prjType;
	}
	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	public String getDelayReason() {
		return delayReason;
	}
	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}
	public String getAudit() {
		return audit;
	}
	public void setAudit(String audit) {
		this.audit = audit;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * 按项目名称然后是项目类型进行排序  prjName,prjType
	 */
	@Override
	public int compareTo(WeekRecord that) {
		//项目名称升序
		int	result=that.getPrjType().compareTo(this.getPrjType());
		if(result==0){
			 result=this.getPrjName().compareTo(that.getPrjName());
			//result=this.getPrjType().compareTo(that.getPrjType());
		}
		return result;
	}
	
}
