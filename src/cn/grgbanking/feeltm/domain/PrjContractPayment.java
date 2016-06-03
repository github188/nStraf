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
@Table(name = "OA_PRJCONTRACT_PAY")
public class PrjContractPayment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String Id;//主键
	
	@Column(name = "C_CONTRACTID")
	private String contractId;//合同ID
	
	@Column(name = "C_CLIENT")
	private String client;//客户名称
	
	@Column(name = "C_PRJNAME")
	private String prjName;//项目名称
	
	@Column(name = "D_PAYMENTDATE")
	private Date paymentDate;//回款时间
	
	@Column(name = "I_PAYMENT")
	private Double payment;//本次回款金额
	
	@Column(name = "C_UPDATEMAN")
	private String updateMan;//收款人
	
	@Column(name = "C_UPDATETIME")
	private Date upDateTime;//更新时间
	
	@Column(name = "C_NOTE")
	private String note;//备注

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

	public Date getUpDateTime() {
		return upDateTime;
	}

	public void setUpDateTime(Date upDateTime) {
		this.upDateTime = upDateTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
