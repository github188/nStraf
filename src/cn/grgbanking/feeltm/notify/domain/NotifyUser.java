package cn.grgbanking.feeltm.notify.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 通知和人的关联表
 * @author lhyan3
 *2014-4-29
 */
@Entity
@Table(name="OA_NOTIFYUSER")
public class NotifyUser {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_NOTIFYNUM")
	private String notifyNum;//通知流水号
	@Column(name="C_USERID")
	private String userid;//用户标识
	@Column(name="I_FLAG")
	private int flag;//主送1还是抄送0
	public String getNotifyNum() {
		return notifyNum;
	}
	public void setNotifyNum(String notifyNum) {
		this.notifyNum = notifyNum;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
