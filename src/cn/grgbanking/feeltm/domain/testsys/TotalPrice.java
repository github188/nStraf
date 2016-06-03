package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="total_price")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class TotalPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	
	@Column(name = "month_date")
	private String monthDate; //月份
	@Column(name = "current_name")
	private String currentName; //姓名
	@Column(name = "group_name")
	private String groupName; //组别
	@Column(name = "work_title")
	private String workTitle; //岗位
	@Column(name = "bachela")
	private String bachela;//学历
	@Column(name = "work_len")
	private String workLen; //工资年限
	@Column(name = "technology")
	private String technology; //技术职称
	@Column(name = "swork_len")
	private String sworkLen; //总工作年限
	
	@Column(name = "total_score")
	private double  totalScore;//总分
	@Column(name = "month_score")
	private double  monthScore;//月度分
	@Column(name = "ability_score")
	private double  abilityScore;//能力分
	@Column(name = "behavior_score")
	private double  behaviorScore;//行为分
	@Column(name = "kpi_score")
	private double  kpiScore;//KPI分
	
	@Column(name = "ms_id")
	private String  msId;//月度分id
	@Column(name = "as_id")
	private String  asId;//能力分id
	@Column(name = "bs_id")
	private String  bsId;//行为分id
	@Column(name = "ks_id")
	private String  ksId;//KPI分id
	
	@Column(name = "edit_lock")
	private String editLock; //提交
	
	public TotalPrice(){
		
	}

	public TotalPrice(String id) {
		//super();
		this.id = id;
	}
	

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}

	public String getBachela() {
		return bachela;
	}

	public void setBachela(String bachela) {
		this.bachela = bachela;
	}

	public String getWorkLen() {
		return workLen;
	}

	public void setWorkLen(String workLen) {
		this.workLen = workLen;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public double getMonthScore() {
		return monthScore;
	}

	public void setMonthScore(double monthScore) {
		this.monthScore = monthScore;
	}

	public double getAbilityScore() {
		return abilityScore;
	}

	public void setAbilityScore(double abilityScore) {
		this.abilityScore = abilityScore;
	}

	public double getBehaviorScore() {
		return behaviorScore;
	}

	public void setBehaviorScore(double behaviorScore) {
		this.behaviorScore = behaviorScore;
	}

	public double getKpiScore() {
		return kpiScore;
	}

	public void setKpiScore(double kpiScore) {
		this.kpiScore = kpiScore;
	}

	public String getEditLock() {
		return editLock;
	}

	public void setEditLock(String editLock) {
		this.editLock = editLock;
	}

	public String getMsId() {
		return msId;
	}

	public void setMsId(String msId) {
		this.msId = msId;
	}

	public String getAsId() {
		return asId;
	}

	public void setAsId(String asId) {
		this.asId = asId;
	}

	public String getBsId() {
		return bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}

	public String getKsId() {
		return ksId;
	}

	public void setKsId(String ksId) {
		this.ksId = ksId;
	}

	public String getSworkLen() {
		return sworkLen;
	}

	public void setSworkLen(String sworkLen) {
		this.sworkLen = sworkLen;
	}

}
