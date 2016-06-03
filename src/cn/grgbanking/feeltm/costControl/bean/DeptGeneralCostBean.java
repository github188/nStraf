package cn.grgbanking.feeltm.costControl.bean;


public class DeptGeneralCostBean {
	
	/**
	 * 部门名称
	 */
	private String deptName;
	
	/**
	 * 部门成员人数
	 */
	private String deptMembersNo;
	
	/**
	 * 部门统计人员数
	 */
	private String deptMembersNoStatistic;

	/**
	 * 查询开始时间
	 */
	private String queryStartTime;
	
	/**
	 * 查询结束时间
	 */
	private String queryEndTime;
	
	/**
	 * 项目经理确认
	 */
	private Integer projectManagerConfirm;
	
	/**
	 * 部门经理确认
	 */
	private Integer deptManagerConfirm;
	
	/**
	 * 未确认
	 */
	private Integer notConfirm;
	
	/**
	 * 未登记
	 */
	private Integer notRegist;
	
	/**
	 * 非项目比例
	 */
	private Double notProjectPercent;
	
	/**
	 * 实习生消耗
	 */
	private Integer traineeCost;//当员工为实习生时，该字段有效，表示实习生当日的消耗
	
	public DeptGeneralCostBean(){
		this.projectManagerConfirm=0;
		this.deptManagerConfirm=0;
		this.notConfirm=0;
		this.notRegist=0;
		this.traineeCost=0;
		this.notProjectPercent=0D;
	}
	
	public DeptGeneralCostBean(String deptName,String deptMembersNum,String deptMembersNumStatistic,String queryStartDate,String queryEndDate){
		this();
		this.deptMembersNo=deptMembersNum;
		this.deptMembersNoStatistic=deptMembersNumStatistic;
		this.deptName=deptName;
		this.queryStartTime=queryStartDate;
		this.queryEndTime=queryEndDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


	public String getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(String queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public String getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(String queryEndTime) {
		this.queryEndTime = queryEndTime;
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
