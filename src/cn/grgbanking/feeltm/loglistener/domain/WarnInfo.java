package cn.grgbanking.feeltm.loglistener.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="OA_WARN_INFO")
public class WarnInfo {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID")
	private String id;
	
	/** 提醒对象id */
	@Column(name="c_toUserId")
	private String toUserId;
	
	/** 提醒对象姓名 */
	@Column(name="c_toUserName")
	private String toUserName;
	
	
	/**提醒方式    email表示邮件    weChat表示微信*/
	@Column(name="c_warnWay")
	private String warnWay;
	
	/** 提醒类型    remind 表示提醒   notification表示通报 */
	@Column(name="c_warnType")
	private String warnType;
	
	/** 提醒时间 */
	@Column(name="d_warnTime")
	private Date warnTime;
	
	/** 提醒内容 */
	@Column(name="c_content")
	private String warnContent;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getWarnWay() {
		return warnWay;
	}

	public void setWarnWay(String warnWay) {
		this.warnWay = warnWay;
	}

	public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

	public Date getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}

	public String getWarnContent() {
		return warnContent;
	}

	public void setWarnContent(String warnContent) {
		this.warnContent = warnContent;
	}

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
}
