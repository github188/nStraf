package cn.grgbanking.feeltm.costControl.bean;


public class DeptDetailCostBean {
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 部门名称
	 */
	private String deptName;
	
	/**
	 * 查询开始日期
	 */
	private String queryStartDate;
	
	/**
	 * 查询结束日期
	 */
	private String queryEndDate;
	
	/**
	 * 项目经理确认人日
	 */
	private Double projectManagerConfirm;
	
	/**
	 * 部门经理确认人日
	 */
	private Double deptManagerConfrim;
	
	/**
	 * 未确认人日（员工填写-部门经理确认-部门经理确认，表示未得到认可的值）
	 */
	private Double notConfirm;
	
	/**
	 * 未填写人日（没有填写日志导致损耗的人日）
	 */
	private Double notRegist;
	
	/**
	 * 非项目比例=（部门经理确认+未确认+未登记人日）/（项目经理确认+部门经理确认+未确认+未登记人日）
	 */
	private Double notProjectPercent;
	
	/**
	 * 实习生消耗
	 */
	private Double traineeCost;//当员工为实习生时，该字段有效，表示实习生的消耗
	
	
	public DeptDetailCostBean(){
		this.notConfirm=0D;
		this.notRegist=0D;
		this.projectManagerConfirm=0D;
		this.deptManagerConfrim=0D;
		this.notProjectPercent=0D;
		this.traineeCost=0D;
	}
	
	public DeptDetailCostBean(String deptName,String userId,String userName,String queryStartTime,String queryEndTime){
		this();
		this.deptName=deptName;
		this.userId=userId;
		this.userName=userName;
		this.queryStartDate=queryStartTime;
		this.queryEndDate=queryEndTime;
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


	public String getQueryStartDate() {
		return queryStartDate;
	}


	public void setQueryStartDate(String queryStartDate) {
		this.queryStartDate = queryStartDate;
	}


	public String getQueryEndDate() {
		return queryEndDate;
	}


	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
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
