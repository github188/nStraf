package cn.grgbanking.feeltm.domain;

public class AccountAnalyse {
	private String accountNo;
	private Long count;
	
	public AccountAnalyse() {
		
	}
	
	public AccountAnalyse(String accountNo, Long count) {
		this.accountNo = accountNo;
		this.count = count;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
