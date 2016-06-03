package cn.grgbanking.feeltm.myadvice.bean;

import cn.grgbanking.feeltm.domain.SysUser;

public class AdviceUserBean {
	/**
	 * 建议人 提示信息
	 */
	private String value;
	/**
	 * 建议人id
	 */
	private String data;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String email;
	
	
	public AdviceUserBean() {
	}
	
	/**根据SysUser对象构造bean
	 * @param user
	 */
	public AdviceUserBean(SysUser user) {
		this.data=user.getUserid();
		this.username=user.getUsername();
		this.tel=user.getMobile();
		this.email=user.getEmail();
		this.value=user.getUsername()+"("+user.getUserid()+")";
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
