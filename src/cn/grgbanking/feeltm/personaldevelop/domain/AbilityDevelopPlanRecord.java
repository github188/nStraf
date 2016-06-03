package cn.grgbanking.feeltm.personaldevelop.domain;

import java.io.Serializable;

/**
 *     
 *
 */
public class AbilityDevelopPlanRecord implements Serializable,Comparable<AbilityDevelopPlanRecord>{
	private String suggestMode;
	private String taskDesc;
	private String finishInstance;
	private String wishInstance;
	private String planDate;
	private String finishDate;
	private String headmanScore;
	private String manageScore;
	
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	public String getSuggestMode() {
		return suggestMode;
	}
	public void setSuggestMode(String suggestMode) {
		this.suggestMode = suggestMode;
	}
	public String getFinishInstance() {
		return finishInstance;
	}
	public void setFinishInstance(String finishInstance) {
		this.finishInstance = finishInstance;
	}
	public String getPlanDate() {
		return planDate;
	}
	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getHeadmanScore() {
		return headmanScore;
	}
	public void setHeadmanScore(String headmanScore) {
		this.headmanScore = headmanScore;
	}
	public String getManageScore() {
		return manageScore;
	}
	public void setManageScore(String manageScore) {
		this.manageScore = manageScore;
	}
	public String getWishInstance() {
		return wishInstance;
	}
	public void setWishInstance(String wishInstance) {
		this.wishInstance = wishInstance;
	}
	@Override
	public int compareTo(AbilityDevelopPlanRecord that) {
		//项目名称升序
		int	result=that.getTaskDesc().compareTo(this.getTaskDesc());
		if(result==0){
			 result=this.getTaskDesc().compareTo(that.getTaskDesc());
			//result=this.getPrjType().compareTo(that.getPrjType());
		}
		return result;
	}
	
}
