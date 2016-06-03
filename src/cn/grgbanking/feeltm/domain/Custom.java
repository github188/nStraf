package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_CUSTOM")
public class Custom implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;// 主键
	
	@Column(name = "D_CREATDATE")
	private Date creatDate;//创建时间
	
	@Column(name = "C_CLIENT")
	private String client;//客户
	
	@Column(name = "C_ADDRESS")
	private String address;//地址
	
	@Column(name = "C_MOUTHPIECE")
	private String mouthPiece;//客户接口人
	
	@Column(name = "C_PRJLIST")
	private String prjList;//项目列表
	
	@Column(name = "C_TEL")
	private String tel;//联系电话
	
	@Column(name = "C_MAIL")
	private String mail;//联系邮箱
	
	@Column(name = "C_NOTE")
	private String note;//备注
	
	@Column(name = "C_UPDATEMAN")
	private String updateMan;//更新人
	
	@Column(name = "C_UPDATE")
	private Date upDate;//更新时间

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Date getCreatDate() {
		return creatDate;
	}



	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}



	public String getClient() {
		return client;
	}



	public void setClient(String client) {
		this.client = client;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getMouthPiece() {
		return mouthPiece;
	}



	public void setMouthPiece(String mouthPiece) {
		this.mouthPiece = mouthPiece;
	}



	public String getPrjList() {
		return prjList;
	}



	public void setPrjList(String prjList) {
		this.prjList = prjList;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getMail() {
		return mail;
	}



	public void setMail(String mail) {
		this.mail = mail;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getUpdateMan() {
		return updateMan;
	}



	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}



	public Date getUpDate() {
		return upDate;
	}



	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
