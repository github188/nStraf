package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRANS_BANKNOTE_SEQ_HOUR")
public class TransBanknoteSeqHour implements Serializable {
	private String checkResult;
	private String createDate;
	private String tranId;
	private String noteFlag;
	private String noteType;
	private String id;
	private String termid;
	private String journalNo;
	private String sequence;
	private String currency;
	private String denomination;
	private String cashBoxId;
	private String seriaNo;
	private String pictureName;
	private String verifyNo;
	private String urlName;
	private String transDate;
	private String tranTime;

	public TransBanknoteSeqHour(String checkResult, String createDate,
			String tranId, String noteFlag, String noteType, String id,
			String termid, String journalNo, String sequence, String currency,
			String denomination, String cashBoxId, String seriaNo,
			String pictureName, String verifyNo, String urlName,
			String transDate, String tranTime) {
		this.checkResult = checkResult;
		this.createDate = createDate;
		this.tranId = tranId;
		this.noteFlag = noteFlag;
		this.noteType = noteType;
		this.id = id;
		this.termid = termid;
		this.journalNo = journalNo;
		this.sequence = sequence;
		this.currency = currency;
		this.denomination = denomination;
		this.cashBoxId = cashBoxId;
		this.seriaNo = seriaNo;
		this.pictureName = pictureName;
		this.verifyNo = verifyNo;
		this.urlName = urlName;
		this.transDate = transDate;
		this.tranTime = tranTime;
	}

	public TransBanknoteSeqHour() {
	}

	@Column(name = "C_CHECK_RESULT")
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	@Column(name = "C_CREATE_DATE")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "C_TRAN_ID")
	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	@Column(name = "C_NOTE_FLAG")
	public String getNoteFlag() {
		return noteFlag;
	}

	public void setNoteFlag(String noteFlag) {
		this.noteFlag = noteFlag;
	}

	@Column(name = "C_NOTE_TYPE")
	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
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

	@Column(name = "C_TERMID")
	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	@Column(name = "C_JOURNAL_NO")
	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	@Column(name = "I_SEQUENCE")
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Column(name = "C_CURRENCY")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "C_DENOMINATION")
	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	@Column(name = "C_CASH_BOX_ID")
	public String getCashBoxId() {
		return cashBoxId;
	}

	public void setCashBoxId(String cashBoxId) {
		this.cashBoxId = cashBoxId;
	}

	@Column(name = "C_SERIA_NO")
	public String getSeriaNo() {
		return seriaNo;
	}

	public void setSeriaNo(String seriaNo) {
		this.seriaNo = seriaNo;
	}

	@Column(name = "C_PICTURE_NAME")
	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	@Column(name = "C_VERIFY_NO")
	public String getVerifyNo() {
		return verifyNo;
	}

	public void setVerifyNo(String verifyNo) {
		this.verifyNo = verifyNo;
	}

	@Column(name = "C_URL_NAME")
	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	@Column(name = "C_TRANS_DATE")
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	@Column(name = "C_TRAN_TIME")
	public String getTranTime() {
		return tranTime;
	}

	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
}
