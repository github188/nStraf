package cn.grgbanking.feeltm.base;

import java.util.Date;

import javax.persistence.Column;

public class BaseDomain {
	
	@Column(name="EXT1")
	private String ext1;
	
	@Column(name="EXT2")
	private String ext2;
	
	@Column(name="EXT3")
	private String ext3;
	
	@Column(name="EXT4")
	private String ext4;
	
	@Column(name="EXT5")
	private Date ext5;
	
	@Column(name="EXT6")
	private Date ext6;
	
	@Column(name="EXT7")
	private Double ext7;
	
	@Column(name="EXT8")
	private Double ext8;
	
	@Column(name="C_UPDATEUSER_ID")
	private String updateuserId;//更新人userid
	
	@Column(name="C_UPDATEUSER_NAME")
	private String updateUsername;//更新人username
	
	@Column(name="D_UPDATE_TIME")
	private Date updateTime;//更新时间

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public Date getExt5() {
		return ext5;
	}

	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	public Date getExt6() {
		return ext6;
	}

	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	public Double getExt7() {
		return ext7;
	}

	public void setExt7(Double ext7) {
		this.ext7 = ext7;
	}

	public Double getExt8() {
		return ext8;
	}

	public void setExt8(Double ext8) {
		this.ext8 = ext8;
	}
	
	public String getUpdateuserId() {
		return updateuserId;
	}
	public void setUpdateuserId(String updateuserId) {
		this.updateuserId = updateuserId;
	}
	public String getUpdateUsername() {
		return updateUsername;
	}
	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
