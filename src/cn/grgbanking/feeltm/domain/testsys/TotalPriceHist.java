package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="total_price_hist")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class TotalPriceHist implements Serializable {
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
	@Column(name = "work_title_hist")
	private String workTitleHist; //历史岗位
	@Column(name = "work_title_fut")
	private String workTitleFut;	//预计岗位
	
	@Column(name = "total_score")
	private double  totalScore;//总分
	
	@Column(name = "edit_lock")
	private String editLock; //提交
	
	@Column(name = "update_name")
	private String updateName; //修改人
	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate; //修改时间
	
	@Transient
	private String updateDateString;
	public TotalPriceHist(){
		
	}

	public TotalPriceHist(String id) {
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

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public String getEditLock() {
		return editLock;
	}

	public void setEditLock(String editLock) {
		this.editLock = editLock;
	}

	public String getWorkTitleHist() {
		return workTitleHist;
	}

	public void setWorkTitleHist(String workTitleHist) {
		this.workTitleHist = workTitleHist;
	}

	public String getWorkTitleFut() {
		return workTitleFut;
	}

	public void setWorkTitleFut(String workTitleFut) {
		this.workTitleFut = workTitleFut;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDateString() {
		return updateDateString;
	}

	public void setUpdateDateString(String updateDateString) {
		this.updateDateString = updateDateString;
	}

}
