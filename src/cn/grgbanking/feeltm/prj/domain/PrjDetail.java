package cn.grgbanking.feeltm.prj.domain;

import java.io.Serializable;

/**
 * <font color="#336699"><s:property value="prjName"/></font> </a> </div></td>
 * <td align="center"><s:property value="versionNO"/></td> <td align="center">
 * <s:property value="requireChangeRate"/></a></td> <td align="center">
 * <s:property value="casePassRate"/></a></td> <td align="center"><s:property
 * value="bugSubtotal"/></a></td> <td align="center"><s:property
 * value="bugCleanRate"/></a></td> <td align="center"><s:property
 * value="bugSeriousRate"/></a></td> <td align="center"><s:property
 * value="bugResolveRate"/></a></td> <td align="center"><s:property
 * value="bugDensity"/></a></td> <td align="center"><s:property
 * value="prjFeedbackRate"/></a></td>
 * 
 * @author feel
 * 
 */
public class PrjDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String prjName;
	private String versionNO;
	private String requireChangeRate;
	private String casePassRate;
	private String bugSubtotal;    //
	private String bugCleanRate;
	private String bugSeriousRate;  //
	private String bugResolveRate;  //
	private String bugDensity;
	private String prjFeedbackRate;
	private String note;   //user input 
	
	private int reqAdd;
	private int reqModify;
	private int reqDelete;
	private int reqSubtotal;

	private int casePass;
	private int caseFail;
	private int caseSubtotal;
	
	private int bugFatal;
	private int bugSerious;
	private int bugGeneral;
	private int bugWarn;
	private int bugSuguest;
	
	private int bugSubvalue;   //
	private Long codeLine;
	private int bugReject;
	private int bugClosed;
	
	//各项得分参数
	private int bugSeriousPoint;
	private int bugDensityPoint;
	private int versionQualityPoint;     //versionQualityPoint=15*(bugSeriousPoint+bugDensityPoint)
	private int finalPoint;   //此项为versionQualityPoint对应的数据库中的范围取得的分值
	private String versionQualityLevel;   //优，良，中，较差，差
	
	
	//汇总统计，totalPoint为该项目所有版本的总得分,versionNum为该项目提交的版本个数
	private int totalPoint;
	private int versionNum;
	private int avgPoint;   //avgPoint=totalPoint/versionNum
	
	
	
	public PrjDetail() {

	}

	public PrjDetail(String prjName, String versionNO) {
		super();
		this.prjName = prjName;
		this.versionNO = versionNO;
	}

	
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getVersionNO() {
		return versionNO;
	}

	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}

	public String getRequireChangeRate() {
		return requireChangeRate;
	}

	public void setRequireChangeRate(String requireChangeRate) {
		this.requireChangeRate = requireChangeRate;
	}

	public String getCasePassRate() {
		return casePassRate;
	}

	public void setCasePassRate(String casePassRate) {
		this.casePassRate = casePassRate;
	}

	public String getBugSubtotal() {
		return bugSubtotal;
	}

	public void setBugSubtotal(String bugSubtotal) {
		this.bugSubtotal = bugSubtotal;
	}

	public String getBugCleanRate() {
		return bugCleanRate;
	}

	public void setBugCleanRate(String bugCleanRate) {
		this.bugCleanRate = bugCleanRate;
	}

	public String getBugSeriousRate() {
		return bugSeriousRate;
	}

	public void setBugSeriousRate(String bugSeriousRate) {
		this.bugSeriousRate = bugSeriousRate;
	}

	public String getBugResolveRate() {
		return bugResolveRate;
	}

	public void setBugResolveRate(String bugResolveRate) {
		this.bugResolveRate = bugResolveRate;
	}

	public String getBugDensity() {
		return bugDensity;
	}

	public void setBugDensity(String bugDensity) {
		this.bugDensity = bugDensity;
	}

	public String getPrjFeedbackRate() {
		return prjFeedbackRate;
	}

	public void setPrjFeedbackRate(String prjFeedbackRate) {
		this.prjFeedbackRate = prjFeedbackRate;
	}

	
	public int getReqAdd() {
		return reqAdd;
	}

	public void setReqAdd(int reqAdd) {
		this.reqAdd = reqAdd;
	}

	public int getReqModify() {
		return reqModify;
	}

	public void setReqModify(int reqModify) {
		this.reqModify = reqModify;
	}
	

	public int getReqDelete() {
		return reqDelete;
	}

	public void setReqDelete(int reqDelete) {
		this.reqDelete = reqDelete;
	}

	public int getReqSubtotal() {
		return reqSubtotal;
	}

	public void setReqSubtotal(int reqSubtotal) {
		this.reqSubtotal = reqSubtotal;
	}

	public int getCasePass() {
		return casePass;
	}

	public void setCasePass(int casePass) {
		this.casePass = casePass;
	}

	public int getCaseFail() {
		return caseFail;
	}

	public void setCaseFail(int caseFail) {
		this.caseFail = caseFail;
	}

	public int getCaseSubtotal() {
		return caseSubtotal;
	}

	public void setCaseSubtotal(int caseSubtotal) {
		this.caseSubtotal = caseSubtotal;
	}

	public int getBugFatal() {
		return bugFatal;
	}

	public void setBugFatal(int bugFatal) {
		this.bugFatal = bugFatal;
	}

	public int getBugSerious() {
		return bugSerious;
	}

	public void setBugSerious(int bugSerious) {
		this.bugSerious = bugSerious;
	}

	public int getBugGeneral() {
		return bugGeneral;
	}

	public void setBugGeneral(int bugGeneral) {
		this.bugGeneral = bugGeneral;
	}

	public int getBugWarn() {
		return bugWarn;
	}

	public void setBugWarn(int bugWarn) {
		this.bugWarn = bugWarn;
	}

	public int getBugSuguest() {
		return bugSuguest;
	}

	public void setBugSuguest(int bugSuguest) {
		this.bugSuguest = bugSuguest;
	}

	public int getBugSubvalue() {
		return bugSubvalue;
	}

	public void setBugSubvalue(int bugSubvalue) {
		this.bugSubvalue = bugSubvalue;
	}
	

	public Long getCodeLine() {
		return codeLine;
	}

	public void setCodeLine(Long codeLine) {
		this.codeLine = codeLine;
	}

	public int getBugReject() {
		return bugReject;
	}

	public void setBugReject(int bugReject) {
		this.bugReject = bugReject;
	}

	public int getBugClosed() {
		return bugClosed;
	}

	public void setBugClosed(int bugClosed) {
		this.bugClosed = bugClosed;
	}
	
	

	public int getBugSeriousPoint() {
		return bugSeriousPoint;
	}

	public void setBugSeriousPoint(int bugSeriousPoint) {
		this.bugSeriousPoint = bugSeriousPoint;
	}

	public int getBugDensityPoint() {
		return bugDensityPoint;
	}

	public void setBugDensityPoint(int bugDensityPoint) {
		this.bugDensityPoint = bugDensityPoint;
	}

	public int getVersionQualityPoint() {
		return versionQualityPoint;
	}

	public void setVersionQualityPoint(int versionQualityPoint) {
		this.versionQualityPoint = versionQualityPoint;
	}

	public int getFinalPoint() {
		return finalPoint;
	}

	public void setFinalPoint(int finalPoint) {
		this.finalPoint = finalPoint;
	}

	public String getVersionQualityLevel() {
		return versionQualityLevel;
	}

	public void setVersionQualityLevel(String versionQualityLevel) {
		this.versionQualityLevel = versionQualityLevel;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	public int getAvgPoint() {
		return avgPoint;
	}

	public void setAvgPoint(int avgPoint) {
		this.avgPoint = avgPoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prjName == null) ? 0 : prjName.hashCode());
		result = prime * result
				+ ((versionNO == null) ? 0 : versionNO.hashCode());
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
		PrjDetail other = (PrjDetail) obj;
		if (prjName == null) {
			if (other.prjName != null)
				return false;
		} else if (!prjName.equals(other.prjName))
			return false;
		if (versionNO == null) {
			if (other.versionNO != null)
				return false;
		} else if (!versionNO.equals(other.versionNO))
			return false;
		return true;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
