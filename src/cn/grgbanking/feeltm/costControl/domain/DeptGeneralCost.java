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
@Table(name="OA_COSTCONTROL_DEPT_GENERAL")
public class DeptGeneralCost {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	
	/**
	 * 部门名称
	 */
	@Column(name="C_DEPT_NAME")
	private String deptName;
	
	/**
	 * 部门成员人数
	 */
	@Column(name="C_DEPT_MEMBERS_NO")
	private String deptMembersNo;
	
	/**
	 * 部门统计成员人数(部门经理及指定人员不统计)
	 */
	@Column(name="C_DEPT_MEMBERS_NO_STATISTIC")
	private String deptMembersNoStatistic;
	
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
	private Integer projectManagerConfirm;
	
	/**
	 * 部门经理确认人日
	 */
	@Column(name="N_DEPTMANAGER_CONFRIM")
	private Integer deptManagerConfirm;
	
	/**
	 * 未确认人日（员工填写-部门经理确认-部门经理确认，表示未得到认可的值）
	 */
	@Column(name="N_NOT_CONFRIM")
	private Integer notConfirm;
	
	/**
	 * 未填写人日（没有填写日志导致损耗的人日）
	 */
	@Column(name="N_NOT_REGIST")
	private Integer notRegist;
	
	/**
	 * 非项目比例=（部门经理确认+未确认+未登记人日）/（项目经理确认+部门经理确认+未确认+未登记人日）
	 */
	@Column(name="N_NOT_PROJECT_PERCENT")
	private Double notProjectPercent;
	
	/**
	 * 实习生消耗
	 */
	@Column(name="N_TRAINEE_COST")
	private Integer traineeCost;
	
	public DeptGeneralCost(){
		this.notConfirm=0;
		this.notRegist=0;
		this.projectManagerConfirm=0;
		this.deptManagerConfirm=0;
		this.notProjectPercent=0D;
		this.traineeCost=0;
	}
	
	public DeptGeneralCost(String deptName,Date date){
		this.deptName=deptName;
		this.statisticDate=date;
		this.notConfirm=0;
		this.notRegist=0;
		this.projectManagerConfirm=0;
		this.deptManagerConfirm=0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getProjectManagerConfirm() {
		return projectManagerConfirm;
	}

	public void setProjectManagerConfirm(Integer projectManagerConfirm) {
		this.projectManagerConfirm = projectManagerConfirm;
	}

	public Integer getDeptManagerConfirm() {
		return deptManagerConfirm;
	}

	public void setDeptManagerConfirm(Integer deptManagerConfirm) {
		this.deptManagerConfirm = deptManagerConfirm;
	}

	public Integer getNotConfirm() {
		return notConfirm;
	}

	public void setNotConfirm(Integer notConfirm) {
		this.notConfirm = notConfirm;
	}

	public Integer getNotRegist() {
		return notRegist;
	}

	public void setNotRegist(Integer notRegist) {
		this.notRegist = notRegist;
	}

	public Double getNotProjectPercent() {
		return notProjectPercent;
	}

	public void setNotProjectPercent(Double notProjectPercent) {
		this.notProjectPercent = notProjectPercent;
	}

	public String getDeptMembersNo() {
		return deptMembersNo;
	}

	public void setDeptMembersNo(String deptMembersNo) {
		this.deptMembersNo = deptMembersNo;
	}

	public String getDeptMembersNoStatistic() {
		return deptMembersNoStatistic;
	}

	public void setDeptMembersNoStatistic(String deptMembersNoStatistic) {
		this.deptMembersNoStatistic = deptMembersNoStatistic;
	}

	public Integer getTraineeCost() {
		return traineeCost;
	}

	public void setTraineeCost(Integer traineeCost) {
		this.traineeCost = traineeCost;
	}
	
	
}
