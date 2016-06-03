package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "BLACK_REGULATION")
public class BlackRegulation implements Serializable {
	private String id;
	private String moneyType;
	private String moneyDenomination;
	private String regulation;
	private String regulationStatus;
	private String termtype;
	private Date createDate;
	private String createName;
	private Date reversionDate;
	private String reversionName;
	private String applyId;
	private String model;
	// 新增字段，终端台数
	private Integer termNum;
	
	//规则下发状态
	private String reguStatus;

	@Column(name = "C_REGU_STATUS")
	public String getReguStatus() {
		return reguStatus;
	}

	public void setReguStatus(String reguStatus) {
		this.reguStatus = reguStatus;
	}

	public BlackRegulation() {
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "C_MONEY_TYPE")
	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	@Column(name = "C_MONEY_DENOMINATION")
	public String getMoneyDenomination() {
		return moneyDenomination;
	}

	public void setMoneyDenomination(String moneyDenomination) {
		this.moneyDenomination = moneyDenomination;
	}

	@Column(name = "C_REGULATION")
	public String getRegulation() {
		return regulation;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	@Column(name = "C_REGULATION_STATUS")
	public String getRegulationStatus() {
		return regulationStatus;
	}

	public void setRegulationStatus(String regulationStatus) {
		this.regulationStatus = regulationStatus;
	}

	@Column(name = "C_TERMTYPE")
	public String getTermtype() {
		return termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	@Column(name = "D_CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "C_CREATE_NAME")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "D_REVERSION_DATE")
	public Date getReversionDate() {
		return reversionDate;
	}

	public void setReversionDate(Date reversionDate) {
		this.reversionDate = reversionDate;
	}

	@Column(name = "C_REVERSION_NAME")
	public String getReversionName() {
		return reversionName;
	}

	public void setReversionName(String reversionName) {
		this.reversionName = reversionName;
	}

	@Column(name = "C_APPLY_ID")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "C_MODEL")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "I_TERM_NUM")
	public Integer getTermNum() {
		return termNum;
	}

	public void setTermNum(Integer termNum) {
		this.termNum = termNum;
	}

}
