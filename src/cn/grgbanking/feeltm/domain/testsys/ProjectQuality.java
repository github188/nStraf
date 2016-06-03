package cn.grgbanking.feeltm.domain.testsys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="PROJECT_QUALITY")
public class ProjectQuality {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="prj_name")
	private String prjName;
	
	@Column(name="prj_type")
	private String prjType;
	
	@Column(name="prj_start")
	@Temporal(TemporalType.DATE)
	private Date prjStart;
	
	@Column(name="prj_end")
	@Temporal(TemporalType.DATE)
	private Date prjEnd;
	
	@Column(name="quality_mark")
	private double qualityMark;
	
	@Column(name="bug_sum")
	private int bugSum;
	
	@Column(name="bug_zongzhi")
	private double  bugZongzhi;   
	
	@Column(name="code_num")
	private int  codeNum;  
	
	@Column(name="bug_density")
	private double  bugDensity;  
	
	@Column(name="bug_density_mark")
	private double  bugDensityMark;  
	
	@Column(name="bug_closednum")
	private int  bugClosednum;  
	
	@Column(name="bug_resolverate")
	private String  bugResolverate;  
	
	@Column(name="bug_resolvemark")
	private double  bugResolvemark;  
	
	@Column(name="bug_reopennum")
	private int  bugReopennum; 
	
	@Column(name="bug_reopenrate")
	private String  bugReopenrate; 
	
	@Column(name="bug_reopenmark")
	private double  bugReopenmark; 
	
	@Column(name="document_qualitymark")
	private double  documentQualitymark; 
	
	@Column(name="base_qualitymark")
	private double  baseQualitymark; 
	
	@Column(name="teststop_num")
	private int  teststopNum;
	
	@Column(name="teststop_mark")
	private double  teststopMark;
	
	@Column(name="testup_num")
	private int  testupNum;
	
	@Column(name="testup_mark")
	private double  testupMark;
	
	@Column(name="projectcomplain_num")
	private int  projectcomplainNum;
	
	@Column(name="projectcomplain_mark")
	private double  projectcomplainMark;
	
	@Column(name="code_quality")
	private String  codeQuality;
	
	@Column(name="code_qualitymark")
	private double  codeQualitymark;
	
	@Column(name="performance_quality")
	private String  performanceQuality;
	
	@Column(name="performance_qualitymark")
	private double  performanceQualitymark;
	
	@Column(name="module_used")
	private double  moduleUsed;
	
	@Column(name="module_usedmark")
	private double  moduleUsedmark;
	
	@Column(name="createman")
	private String  createMan;
	
	@Column(name="createdate")
	@Temporal(TemporalType.DATE)
	private Date createDate;
	
	@Column(name="updateman")
	private String  updateMan;
	
	@Column(name="updatedate")
	@Temporal(TemporalType.DATE)
	private Date updateDate;
	
	@Column(name="prjqno")
	private String  prjQno;
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public String getPrjName() {
		return prjName;
	}


	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}


	public Date getPrjStart() {
		return prjStart;
	}


	public void setPrjStart(Date prjStart) {
		this.prjStart = prjStart;
	}


	public Date getPrjEnd() {
		return prjEnd;
	}


	public void setPrjEnd(Date prjEnd) {
		this.prjEnd = prjEnd;
	}


	public double getQualityMark() {
		return qualityMark;
	}


	public void setQualityMark(double qualityMark) {
		this.qualityMark = qualityMark;
	}


	

	public double getBugZongzhi() {
		return bugZongzhi;
	}


	public void setBugZongzhi(double bugZongzhi) {
		this.bugZongzhi = bugZongzhi;
	}





	public double getBugDensity() {
		return bugDensity;
	}


	public void setBugDensity(double bugDensity) {
		this.bugDensity = bugDensity;
	}


	public double getBugDensityMark() {
		return bugDensityMark;
	}


	public void setBugDensityMark(double bugDensityMark) {
		this.bugDensityMark = bugDensityMark;
	}


	

	


	public double getBugResolvemark() {
		return bugResolvemark;
	}


	public void setBugResolvemark(double bugResolvemark) {
		this.bugResolvemark = bugResolvemark;
	}





	


	public double getDocumentQualitymark() {
		return documentQualitymark;
	}


	public void setDocumentQualitymark(double documentQualitymark) {
		this.documentQualitymark = documentQualitymark;
	}


	public double getBaseQualitymark() {
		return baseQualitymark;
	}


	public void setBaseQualitymark(double baseQualitymark) {
		this.baseQualitymark = baseQualitymark;
	}


	

	public double getTeststopMark() {
		return teststopMark;
	}


	public void setTeststopMark(double teststopMark) {
		this.teststopMark = teststopMark;
	}



	public double getTestupMark() {
		return testupMark;
	}


	public void setTestupMark(double testupMark) {
		this.testupMark = testupMark;
	}




	public double getProjectcomplainMark() {
		return projectcomplainMark;
	}


	public void setProjectcomplainMark(double projectcomplainMark) {
		this.projectcomplainMark = projectcomplainMark;
	}


	public String getCodeQuality() {
		return codeQuality;
	}


	public void setCodeQuality(String codeQuality) {
		this.codeQuality = codeQuality;
	}


	public double getCodeQualitymark() {
		return codeQualitymark;
	}


	public void setCodeQualitymark(double codeQualitymark) {
		this.codeQualitymark = codeQualitymark;
	}


	public String getPerformanceQuality() {
		return performanceQuality;
	}


	public void setPerformanceQuality(String performanceQuality) {
		this.performanceQuality = performanceQuality;
	}


	public double getPerformanceQualitymark() {
		return performanceQualitymark;
	}


	public void setPerformanceQualitymark(double performanceQualitymark) {
		this.performanceQualitymark = performanceQualitymark;
	}


	public double getModuleUsed() {
		return moduleUsed;
	}


	public void setModuleUsed(double moduleUsed) {
		this.moduleUsed = moduleUsed;
	}


	public double getModuleUsedmark() {
		return moduleUsedmark;
	}


	public void setModuleUsedmark(double moduleUsedmark) {
		this.moduleUsedmark = moduleUsedmark;
	}


	public String getCreateMan() {
		return createMan;
	}


	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getUpdateMan() {
		return updateMan;
	}


	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public String getPrjQno() {
		return prjQno;
	}


	public void setPrjQno(String prjQno) {
		this.prjQno = prjQno;
	}


	public String getPrjType() {
		return prjType;
	}


	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}


	public int getBugSum() {
		return bugSum;
	}


	public void setBugSum(int bugSum) {
		this.bugSum = bugSum;
	}


	public int getCodeNum() {
		return codeNum;
	}


	public void setCodeNum(int codeNum) {
		this.codeNum = codeNum;
	}


	public int getBugClosednum() {
		return bugClosednum;
	}


	public void setBugClosednum(int bugClosednum) {
		this.bugClosednum = bugClosednum;
	}


	public String getBugResolverate() {
		return bugResolverate;
	}


	public void setBugResolverate(String bugResolverate) {
		this.bugResolverate = bugResolverate;
	}


	public int getBugReopennum() {
		return bugReopennum;
	}


	public void setBugReopennum(int bugReopennum) {
		this.bugReopennum = bugReopennum;
	}


	public String getBugReopenrate() {
		return bugReopenrate;
	}


	public void setBugReopenrate(String bugReopenrate) {
		this.bugReopenrate = bugReopenrate;
	}


	public int getTeststopNum() {
		return teststopNum;
	}


	public void setTeststopNum(int teststopNum) {
		this.teststopNum = teststopNum;
	}


	public int getTestupNum() {
		return testupNum;
	}


	public void setTestupNum(int testupNum) {
		this.testupNum = testupNum;
	}


	public int getProjectcomplainNum() {
		return projectcomplainNum;
	}


	public void setProjectcomplainNum(int projectcomplainNum) {
		this.projectcomplainNum = projectcomplainNum;
	}


	public double getBugReopenmark() {
		return bugReopenmark;
	}


	public void setBugReopenmark(double bugReopenmark) {
		this.bugReopenmark = bugReopenmark;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

