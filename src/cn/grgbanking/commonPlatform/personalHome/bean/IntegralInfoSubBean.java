package cn.grgbanking.commonPlatform.personalHome.bean;


public class IntegralInfoSubBean {
	/** 用户ID */
	private String userId;

	/** 用户名 */
	private String userName;

	/** 部门ID */
	private String detNameId;

	/** 部门名 */
	private String detName;

	/** 获得积分 */
	private int integral;

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getDetNameId() {
		return detNameId;
	}

	public String getDetName() {
		return detName;
	}

	public int getIntegral() {
		return integral;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDetNameId(String detNameId) {
		this.detNameId = detNameId;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}
}
