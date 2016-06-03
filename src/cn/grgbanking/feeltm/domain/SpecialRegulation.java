package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * <p>
 * Title: model
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author hail
 * @version 1.0
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "SPECIAL_REGULATION")
public class SpecialRegulation implements Serializable {

	private String id;
	private String applyId;
	private String orgid;
	private String moneyType;
	private String moneyDenomination;
	private String regulation;
	private String source;
	private String result;
	private String createDate;
	private String createName;
	private String specialType;

	public SpecialRegulation(String id, String applyId, String orgid,
			String moneyType, String moneyDenomination, String regulation,
			String source, String result, String createDate, String createName,
			String specialType) {
		this.id = id;
		this.applyId = applyId;
		this.orgid = orgid;
		this.moneyType = moneyType;
		this.moneyDenomination = moneyDenomination;
		this.regulation = regulation;
		this.source = source;
		this.result = result;
		this.createDate = createDate;
		this.createName = createName;
		this.specialType = specialType;
	}

	public SpecialRegulation() {
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

	@Column(name = "C_APPLY_ID")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "C_ORGID")
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
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

	@Column(name = "C_SOURCE")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "C_RESULT")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "C_CREATE_DATE")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "C_CREATE_NAME")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "C_SPECIAL_TYPE")
	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}
}
