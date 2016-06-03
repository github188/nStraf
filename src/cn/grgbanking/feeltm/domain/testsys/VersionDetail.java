package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="version_detail")
public class VersionDetail implements Serializable, Comparable<VersionDetail>{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="prj_name")
	private String prjName;
	
	@Column(name="version_no")
	private String versionNO;
	
	@Column(name="start_date")
	private String start;
	
	@Column(name="static_month")
	private String staticMonth;
	
	@Column(name="quality_point")
	private int qualityPoint;    //89fen
	
	@Column(name="version_quality")
	private String versionQuality;//  相应的级别
	
	@Column(name="valid_bug_count")
	private int validBugCount;
	
	@Column(name="bug_serious")
	private String bugSerious;
	
	@Column(name="bug_total_value")
	private int bugTotalValue;
	
	@Column(name="detection_date")
	private String detectionDate;
	
	private String bugDensity;
	
	
	public VersionDetail() {
	}
	
	public VersionDetail(String prjName, String versionNO,String start, String staticMonth) {
		super();
		this.start=start;
		this.prjName = prjName;
		this.versionNO = versionNO;
		this.staticMonth = staticMonth;
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




	public String getStart() {
		return start;
	}



	public void setStart(String start) {
		this.start = start;
	}



	public String getVersionNO() {
		return versionNO;
	}




	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
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




	public String getVersionQuality() {
		return versionQuality;
	}




	public void setVersionQuality(String versionQuality) {
		this.versionQuality = versionQuality;
	}




	public int getValidBugCount() {
		return validBugCount;
	}




	public void setValidBugCount(int validBugCount) {
		this.validBugCount = validBugCount;
	}




	public String getBugSerious() {
		return bugSerious;
	}




	public void setBugSerious(String bugSerious) {
		this.bugSerious = bugSerious;
	}




	public int getBugTotalValue() {
		return bugTotalValue;
	}




	public void setBugTotalValue(int bugTotalValue) {
		this.bugTotalValue = bugTotalValue;
	}




	public String getBugDensity() {
		return bugDensity;
	}




	public void setBugDensity(String bugDensity) {
		this.bugDensity = bugDensity;
	}

	public String getDetectionDate() {
		return detectionDate;
	}


	public void setDetectionDate(String detectionDate) {
		this.detectionDate = detectionDate;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Override
	public int compareTo(VersionDetail o) {
		if(o.getDetectionDate().compareTo(this.getDetectionDate())<0)
			return 1;
		return 0;
	}
}
