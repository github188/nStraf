package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_PRJCONTRACT")
public class PrjContract implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String Id;//主键
	
	@Column(name = "C_PRJNAME")
	private String prjName;//项目名称
	
	@Column(name = "C_PRJMANAGER")
	private String prjManager;//项目经理
	
	@Column(name = "C_CLIENT")
	private String client;//甲方
	
	@Column(name = "D_STARTDATE")
	private Date startDate;//项目开始时间
	
	@Column(name = "D_ENDDATE")
	private Date endDate;//项目结束时间
	
	@Column(name = "D_FINISHDATE")
	private Date finishDate;//项目交付时间
	
	@Column(name = "I_TOTAL")
	private BigDecimal total;//合同金额
	
	@Column(name = "I_PAYMENT")
	private BigDecimal payment;//合同已付金额
	
	@Column(name = "C_STATUS")
	private String status;//合同状态
	
	@Column(name = "C_RISK")
	private String risk;//合同风险
	
	@Column(name = "C_FARE")
	private String fare;//项目进度
	
	@Column(name = "C_NOTE")
	private String note;//备注
	
	@Column(name = "C_UPDATEMAN")
	private String updateMan;//更新人
	
	@Column(name = "C_UPDATE")
	private Date upDate;//更新时间

	@Column(name = "D_SIGNDATE")
	private Date signDate;//签订时间

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getPrjManager() {
		return prjManager;
	}

	public void setPrjManager(String prjManager) {
		this.prjManager = prjManager;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	


	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
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

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
}
