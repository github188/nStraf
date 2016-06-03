package cn.grgbanking.feeltm.prj.domain;

import java.io.Serializable;

/**
 *需求覆盖率	用例缺陷数	用例缺陷解决率	用例漏出率	用例有效性
 *新增  修改	删除	已评审	未评审	实现自动化用例
 * @author feel
 */
public class DesignerDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String prjName;
	private String name;
	private String reqCoveredRate;
	private String  caseBugNum;
	private String  caseBugResolveRate;
	private String  caseLowRate;//用例漏出率
	private String caseValidRate;
	
	private String add;
	private String update;
	private String delete;
	private String audited;
	private String unAudit;
	private String autoFlag;   //暂时未使用该字段
	private String undefined;
	
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReqCoveredRate() {
		return reqCoveredRate;
	}
	public void setReqCoveredRate(String reqCoveredRate) {
		this.reqCoveredRate = reqCoveredRate;
	}
	public String getCaseBugNum() {
		return caseBugNum;
	}
	public void setCaseBugNum(String caseBugNum) {
		this.caseBugNum = caseBugNum;
	}
	public String getCaseBugResolveRate() {
		return caseBugResolveRate;
	}
	public void setCaseBugResolveRate(String caseBugResolveRate) {
		this.caseBugResolveRate = caseBugResolveRate;
	}
	public String getCaseLowRate() {
		return caseLowRate;
	}
	public void setCaseLowRate(String caseLowRate) {
		this.caseLowRate = caseLowRate;
	}
	public String getCaseValidRate() {
		return caseValidRate;
	}
	public void setCaseValidRate(String caseValidRate) {
		this.caseValidRate = caseValidRate;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	public String getAudited() {
		return audited;
	}
	public void setAudited(String audited) {
		this.audited = audited;
	}
	public String getUnAudit() {
		return unAudit;
	}
	public void setUnAudit(String unAudit) {
		this.unAudit = unAudit;
	}
	public String getAutoFlag() {
		return autoFlag;
	}
	public void setAutoFlag(String autoFlag) {
		this.autoFlag = autoFlag;
	}
	public String getUndefined() {
		return undefined;
	}
	public void setUndefined(String undefined) {
		this.undefined = undefined;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
