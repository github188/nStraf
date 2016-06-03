package cn.grgbanking.feeltm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "OA_PRJCHANCE_FOLLOW")//商机跟进表
public class PrjChanceFollow {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;//主键
	
	@Column(name = "c_chanceid")
	private String chanceId;//商机id
	
	@Column(name = "d_followdate")
	private Date followDate;//跟进日期 
	
	@Column(name = "c_followman")
	private String followMan;//跟进人
	
	@Column(name = "c_followcontent")
	private String followContent;//跟进内容
	
	@Column(name = "c_prjstage")
	private String prjStage;//项目阶段
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChanceId() {
		return chanceId;
	}

	public void setChanceId(String chanceId) {
		this.chanceId = chanceId;
	}

	public Date getFollowDate() {
		return followDate;
	}

	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}

	public String getFollowMan() {
		return followMan;
	}

	public void setFollowMan(String followMan) {
		this.followMan = followMan;
	}

	public String getFollowContent() {
		return followContent;
	}

	public void setFollowContent(String followContent) {
		this.followContent = followContent;
	}

	public String getPrjStage() {
		return prjStage;
	}

	public void setPrjStage(String prjStage) {
		this.prjStage = prjStage;
	}
	
}
