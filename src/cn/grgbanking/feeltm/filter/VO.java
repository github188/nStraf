package cn.grgbanking.feeltm.filter;

public class VO {
	private String username;
	private String password;
	private String tel;
	private String sex;
	private String address;
	private int age;
	 
	public VO(){}
	
	public VO(String username , String password , String tel, String sex , String address , int age)
	{
		this.username = username;
		this.password = password;
		this.tel = tel;
		this.sex = sex;
		this.address = address;
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}
