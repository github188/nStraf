package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "REPEAT_REGULATION")
public class RepeatRegulation implements Serializable {
	private String id;
	private Integer repeatnum;
	private String dealwithMode;
	private String logMode;
	private String creenMode;
	private String enterAccountMode;
	private String regulationStatus;
	private String termtype;
	private Date createDate;
	private String createName;
	private Date reversionDate;
	private String reversionName;
	private String applyId;
	private String model;
	// 新增字段
	private int termNum;
	
	//规则下发状态
	private String reguStatus;

	@Column(name = "C_REGU_STATUS")
	public String getReguStatus() {
		return reguStatus;
	}

	public void setReguStatus(String reguStatus) {
		this.reguStatus = reguStatus;
	}

	public RepeatRegulation() {
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

	@Column(name = "I_REPEATNUM")
	public Integer getRepeatnum() {
		return repeatnum;
	}

	public void setRepeatnum(Integer repeatnum) {
		this.repeatnum = repeatnum;
	}

	@Column(name = "C_DEALWITH_MODE")
	public String getDealwithMode() {
		return dealwithMode;
	}

	public void setDealwithMode(String dealwithMode) {
		this.dealwithMode = dealwithMode;
	}

	@Column(name = "C_LOG_MODE")
	public String getLogMode() {
		return logMode;
	}

	public void setLogMode(String logMode) {
		this.logMode = logMode;
	}

	@Column(name = "C_CREEN_MODE")
	public String getCreenMode() {
		return creenMode;
	}

	public void setCreenMode(String creenMode) {
		this.creenMode = creenMode;
	}

	@Column(name = "C_ENTER_ACCOUNT_MODE")
	public String getEnterAccountMode() {
		return enterAccountMode;
	}

	public void setEnterAccountMode(String enterAccountMode) {
		this.enterAccountMode = enterAccountMode;
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
	public int getTermNum() {
		return termNum;
	}

	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}
}
