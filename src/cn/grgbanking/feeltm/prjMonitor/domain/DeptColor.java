package cn.grgbanking.feeltm.prjMonitor.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cn.grgbanking.feeltm.base.BaseDomain;
/**
 * 项目
 * @author lping1
 * 2014年9月15日
 */
@Entity
@Table(name="OA_DEPT_COLOR")
public class DeptColor extends BaseDomain implements Serializable {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String deptColorId;
	
	@Column(name="C_USER_ID")
	private String userId;//名字
	
	@Column(name="C_DEPT_ID")
	private String deptId;//部门id
	
	@Transient
	private String deptName;//部门名称
	
	@Column(name="C_COLOR_ID")
	private String deptColorVal;//颜色id
	
	@Column(name="C_MON_FLAG")
	private String monFlag;//是否监控
	
	/*---------------------getter setter---------------------------------*/


	public String getUserId() {
		return userId;
	}

	public String getDeptColorId() {
		return deptColorId;
	}

	public void setDeptColorId(String deptColorId) {
		this.deptColorId = deptColorId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}



	public String getDeptColorVal() {
		return deptColorVal;
	}

	public void setDeptColorVal(String deptColorVal) {
		this.deptColorVal = deptColorVal;
	}

	public String getMonFlag() {
		return monFlag;
	}

	public void setMonFlag(String monFlag) {
		this.monFlag = monFlag;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
}
