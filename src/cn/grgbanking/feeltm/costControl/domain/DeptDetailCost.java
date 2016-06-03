package cn.grgbanking.feeltm.costControl.domain;

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
@Table(name="OA_COSTCONTROL_DEPT_DETAIL")
public class DeptDetailCost {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	
	/**
	 * 用户id
	 */
	@Column(name="C_USER_ID")
	private String userId;
	
	/**
	 * 用户名
	 */
	@Column(name="C_USER_NAME")
	private String userName;
	
	/**
	 * 部门名称
	 */
	@Column(name="C_DEPT_NAME")
	private String deptName;
	
	/**
	 * 统计日期
	 */
	@Column(name="D_STATISTIC_DATE")
	@Temporal(TemporalType.DATE)
	private Date statisticDate;
	
	/**
	 * 项目经理确认人日
	 */
	@Column(name="N_PRJMANAGER_CONFIRM")
	private Double projectManagerConfirm;
	
	/**
	 * 部门经理确认人日
	 */
	@Column(name="N_DEPTMANAGER_CONFRIM")
	private Double deptManagerConfrim;
	
	/**
	 * 未确认人日（员工填写-部门经理确认-部门经理确认，表示未得到认可的值）
	 */
	@Column(name="N_NOT_CONFRIM")
	private Double notConfirm;
	
	/**
	 * 未填写人日（没有填写日志导致损耗的人日）
	 */
	@Column(name="N_NOT_REGIST")
	private Double notRegist;
	
	/**
	 * 非项目比例=（部门经理确认+未确认+未登记人日）/（项目经理确认+部门经理确认+未确认+未登记人日）
	 */
	@Column(name="N_NOT_PROJECT_PERCENT")
	private Double notProjectPercent;
	
	/**
	 * 实习生消耗
	 */
	@Column(name="N_TRAINEE_COST")
	private Double traineeCost;
	
	
	public DeptDetailCost(){
		this.notConfirm=0D;
		this.notRegist=0D;
		this.projectManagerConfirm=0D;
		this.deptManagerConfrim=0D;
		this.notProjectPercent=0D;
		this.traineeCost=0D;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getDeptName() {
		return deptName;
	}


	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


	public Date getStatisticDate() {
		return statisticDate;
	}


	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}


	public Double getProjectManagerConfirm() {
		return projectManagerConfirm;
	}


	public void setProjectManagerConfirm(Double projectManagerConfirm) {
		this.projectManagerConfirm = projectManagerConfirm;
	}


	public Double getDeptManagerConfrim() {
		return deptManagerConfrim;
	}


	public void setDeptManagerConfrim(Double deptManagerConfrim) {
		this.deptManagerConfrim = deptManagerConfrim;
	}


	public Double getNotConfirm() {
		return notConfirm;
	}


	public void setNotConfirm(Double notConfirm) {
		this.notConfirm = notConfirm;
	}


	public Double getNotRegist() {
		return notRegist;
	}


	public void setNotRegist(Double notRegist) {
		this.notRegist = notRegist;
	}


	public Double getNotProjectPercent() {
		return notProjectPercent;
	}


	public void setNotProjectPercent(Double notProjectPercent) {
		this.notProjectPercent = notProjectPercent;
	}


	public Double getTraineeCost() {
		return traineeCost;
	}


	public void setTraineeCost(Double traineeCost) {
		this.traineeCost = traineeCost;
	}
	
	
}
