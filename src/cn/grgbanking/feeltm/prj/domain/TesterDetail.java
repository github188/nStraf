package cn.grgbanking.feeltm.prj.domain;

import java.io.Serializable;

/**
 * <font color="#336699"><s:property value="prjName"/></font> </a> </div></td>
 * <td align="center"><s:property value="versionNO"/></td> <td align="center">
 * <s:property value="testerName"/></a></td> <td align="center"><s:property
 * value="bugFindRate"/></a></td> <td align="center"><s:property
 * value="bugErrorRate"/></a></td> <td align="center"><s:property
 * value="bugValidateNum"/></a></td> <td align="center"><s:property
 * value="caseExecRate"/></a></td> <td align="center"><s:property
 * value="caseExecSpeed"/></a></t
 * 
 * @author feel
 * 
 */
public class TesterDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	private String prjName;
	private String versionNO;
	private String testerName;
	private String bugFindRate;
	private String bugErrorRate;
	private String bugValidateNum;
	private String caseExecRate;
	private String caseExecSpeed;

	private String bugFatal;
	private String bugSerious;
	private String bugGeneral;
	private String bugWarn;
	private String bugSuguest;
	private String bugReject;
	private String bugNoCase;
	private String subtotal;
	private String caseExecNum;
	private String caseExecTime;

	public TesterDetail(){
		
	}
	
	
	public String getVersionNO() {
		return versionNO;
	}


	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}


	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public String getBugFindRate() {
		return bugFindRate;
	}

	public void setBugFindRate(String bugFindRate) {
		this.bugFindRate = bugFindRate;
	}

	public String getBugErrorRate() {
		return bugErrorRate;
	}

	public void setBugErrorRate(String bugErrorRate) {
		this.bugErrorRate = bugErrorRate;
	}

	public String getBugValidateNum() {
		return bugValidateNum;
	}

	public void setBugValidateNum(String bugValidateNum) {
		this.bugValidateNum = bugValidateNum;
	}

	public String getCaseExecRate() {
		return caseExecRate;
	}

	public void setCaseExecRate(String caseExecRate) {
		this.caseExecRate = caseExecRate;
	}

	public String getCaseExecSpeed() {
		return caseExecSpeed;
	}

	public void setCaseExecSpeed(String caseExecSpeed) {
		this.caseExecSpeed = caseExecSpeed;
	}

	public String getBugFatal() {
		return bugFatal;
	}

	public void setBugFatal(String bugFatal) {
		this.bugFatal = bugFatal;
	}

	public String getBugSerious() {
		return bugSerious;
	}

	public void setBugSerious(String bugSerious) {
		this.bugSerious = bugSerious;
	}

	public String getBugGeneral() {
		return bugGeneral;
	}

	public void setBugGeneral(String bugGeneral) {
		this.bugGeneral = bugGeneral;
	}

	public String getBugWarn() {
		return bugWarn;
	}

	public void setBugWarn(String bugWarn) {
		this.bugWarn = bugWarn;
	}

	public String getBugSuguest() {
		return bugSuguest;
	}

	public void setBugSuguest(String bugSuguest) {
		this.bugSuguest = bugSuguest;
	}

	public String getBugReject() {
		return bugReject;
	}

	public void setBugReject(String bugReject) {
		this.bugReject = bugReject;
	}

	public String getBugNoCase() {
		return bugNoCase;
	}

	public void setBugNoCase(String bugNoCase) {
		this.bugNoCase = bugNoCase;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getCaseExecNum() {
		return caseExecNum;
	}

	public void setCaseExecNum(String caseExecNum) {
		this.caseExecNum = caseExecNum;
	}

	public String getCaseExecTime() {
		return caseExecTime;
	}

	public void setCaseExecTime(String caseExecTime) {
		this.caseExecTime = caseExecTime;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
