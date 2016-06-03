package cn.grgbanking.feeltm.domain.testsys;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="PRJ_PART")
public class PrjPart {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="prj_name")
	private String prjName;
	
	private String PM;
	@Column(name="start_date")
	private String start;
	
	@Column(name="static_month")
	private String staticMonth;
	
	@Column(name="quality_point")
	private int qualityPoint;
	
	@Column(name="total_evaluate")
	private String totalEvaluate;
	
	
	@Column(name="process_quality")
	private int  processQuality;   //5为优，4为良，3 2 1 
	@Transient
	private String processQualityStr;
	
	private int  progress;   //5为提前>=2周
	@Transient
	private String progressStr;
	
	@Column(name="project_quality")
	private int projectQuality;  //5为优，4为良
	@Transient
	private String projectQualityStr;
	
	
	@Column(name="submit_version_number")
	private int submitVersionNumer;
	
	@Column(name="bug_reslove_rate")
	private String bugResolveRate;
	
	@Column(name="bug_closed_rate")
	private String bugClosedRate;
	
	@Column(name="bug_reopen_num")
	private int bugReopenNum;
	
	@Column(name="bug_reopen_rate")
	private String bugReopenRate;
	
	@Column(name="bug_closed_num")
	private int bugClosedNum;
	
	@Column(name="unfit_number")
	private int unfitNumber;  //不符合准入测试的次数
	
	private String status; //项目所处的状态
	
	@Column(name="last_update_time")
	private String lastUpdateTime;
	
	@Column(name="last_update_man")
	private String lastUpdateMan;
	
	@Column(name="prj_type")
	private String prjType;
	
	@Transient
	private int prjQualityAvgPoint;
	
	@Transient
	private int totalValidBugs;
	
	@Transient
	private int totalBugValue;

	@Transient
	private double prjAvgBugbugDensity;
	
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


	public String getPM() {
		return PM;
	}

	public void setPM(String pM) {
		PM = pM;
	}

	public String getStaticMonth() {
		return staticMonth;
	}

	public void setStaticMonth(String staticMonth) {
		this.staticMonth = staticMonth;
	}

	public int getQualityPoint() {
		return qualityPoint;
	}


	public void setQualityPoint(int qualityPoint) {
		this.qualityPoint = qualityPoint;
	}


	public String getTotalEvaluate() {
		return totalEvaluate;
	}


	public void setTotalEvaluate(String totalEvaluate) {
		this.totalEvaluate = totalEvaluate;
	}


	public int getProcessQuality() {
		return processQuality;
	}


	public void setProcessQuality(int processQuality) {
		this.processQuality = processQuality;
	}


	public String getProcessQualityStr() {
		return processQualityStr;
	}


	public void setProcessQualityStr(String processQualityStr) {
		this.processQualityStr = processQualityStr;
	}


	public int getProgress() {
		return progress;
	}


	public void setProgress(int progress) {
		this.progress = progress;
	}


	public String getProgressStr() {
		return progressStr;
	}


	public void setProgressStr(String progressStr) {
		this.progressStr = progressStr;
	}


	public int getProjectQuality() {
		return projectQuality;
	}
	

	public String getPrjType() {
		return prjType;
	}


	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}


	public void setProjectQuality(int projectQuality) {
		this.projectQuality = projectQuality;
	}


	public String getProjectQualityStr() {
		return projectQualityStr;
	}


	public void setProjectQualityStr(String projectQualityStr) {
		this.projectQualityStr = projectQualityStr;
	}


	public int getSubmitVersionNumer() {
		return submitVersionNumer;
	}


	public void setSubmitVersionNumer(int submitVersionNumer) {
		this.submitVersionNumer = submitVersionNumer;
	}


	public String getBugResolveRate() {
		return bugResolveRate;
	}


	public void setBugResolveRate(String bugResolveRate) {
		this.bugResolveRate = bugResolveRate;
	}


	public int getUnfitNumber() {
		return unfitNumber;
	}


	public void setUnfitNumber(int unfitNumber) {
		this.unfitNumber = unfitNumber;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public String getLastUpdateMan() {
		return lastUpdateMan;
	}


	public void setLastUpdateMan(String lastUpdateMan) {
		this.lastUpdateMan = lastUpdateMan;
	}
	
	
	public PrjPart(String id) {
		this.id=id;
	}
	
	public PrjPart(){
		
	}

	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public PrjPart(String prjName, String start,String staticMonth) {
		super();
		this.prjName = prjName;
		this.start=start;
		this.staticMonth = staticMonth;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prjName == null) ? 0 : prjName.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result
				+ ((staticMonth == null) ? 0 : staticMonth.hashCode());
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
		PrjPart other = (PrjPart) obj;
		if (prjName == null) {
			if (other.prjName != null)
				return false;
		} else if (!prjName.equals(other.prjName))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (staticMonth == null) {
			if (other.staticMonth != null)
				return false;
		} else if (!staticMonth.equals(other.staticMonth))
			return false;
		return true;
	}


	public int getPrjQualityAvgPoint() {
		return prjQualityAvgPoint;
	}


	public void setPrjQualityAvgPoint(int prjQualityAvgPoint) {
		this.prjQualityAvgPoint = prjQualityAvgPoint;
	}


	public int getTotalValidBugs() {
		return totalValidBugs;
	}


	public void setTotalValidBugs(int totalValidBugs) {
		this.totalValidBugs = totalValidBugs;
	}


	public int getTotalBugValue() {
		return totalBugValue;
	}


	public void setTotalBugValue(int totalBugValue) {
		this.totalBugValue = totalBugValue;
	}


	public String getBugClosedRate() {
		return bugClosedRate;
	}


	public void setBugClosedRate(String bugClosedRate) {
		this.bugClosedRate = bugClosedRate;
	}


	public int getBugClosedNum() {
		return bugClosedNum;
	}


	public void setBugClosedNum(int bugClosedNum) {
		this.bugClosedNum = bugClosedNum;
	}


	public int getBugReopenNum() {
		return bugReopenNum;
	}


	public void setBugReopenNum(int bugReopenNum) {
		this.bugReopenNum = bugReopenNum;
	}


	public String getBugReopenRate() {
		return bugReopenRate;
	}


	public void setBugReopenRate(String bugReopenRate) {
		this.bugReopenRate = bugReopenRate;
	}


	public double getPrjAvgBugbugDensity() {
		return prjAvgBugbugDensity;
	}


	public void setPrjAvgBugbugDensity(double prjAvgBugbugDensity) {
		this.prjAvgBugbugDensity = prjAvgBugbugDensity;
	}


	

		

	}

