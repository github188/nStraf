package cn.grgbanking.feeltm.costControl.bean;

public class ProjectProfitBean {
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 项目名称 
	 */
	private String projectName;
	/**
	 * 年月
	 */
	private String month;
	/**
	 * 收入
	 */
	private int income;
	
	/**
	 * 支出
	 */
	private int cost;
	
	/**
	 * 利润
	 */
	private int profit;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getProfit() {
		return profit;
	}

	public void setProfit(int profit) {
		this.profit = profit;
	}


	
}
