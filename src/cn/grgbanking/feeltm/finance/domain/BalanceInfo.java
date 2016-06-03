package cn.grgbanking.feeltm.finance.domain;

import java.io.Serializable;
/**
 * 弹出总收入，总支出，当前余额，管理人。
 * @author feel
 *
 */
public class BalanceInfo implements Serializable{
	private double paySum;
	private double incomeSum;
	private double currentBalance;
	private String manage;
	
	public BalanceInfo(double paySum, double incomeSum, double currentBalance) {
		super();
		this.paySum = paySum;
		this.incomeSum = incomeSum;
		this.currentBalance = currentBalance;
	}

	public double getPaySum() {
		return paySum;
	}

	public void setPaySum(double paySum) {
		this.paySum = paySum;
	}

	public double getIncomeSum() {
		return incomeSum;
	}

	public void setIncomeSum(double incomeSum) {
		this.incomeSum = incomeSum;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getManage() {
		return manage;
	}

	public void setManage(String manage) {
		this.manage = manage;
	}
	

	
}