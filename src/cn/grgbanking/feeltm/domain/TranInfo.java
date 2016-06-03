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
@Table(name = "TRAN_INFO")
public class TranInfo implements Serializable {
	private String id;
	private String transCode;
	private String transOrgid;
	private String termid;
	private String transDate;
	private String transTime;
	private String accountNo;
	private String journalNo;
	private Integer transAmt;
	private String transResult;
	
	/** 新增字段 */
	private Integer noteNum; //交易张数
	private Integer blNum; //黑名单张数
	private Integer repeatNum; //重号张数
	private Integer callBackNum;//回收张数
	
	@Column(name = "I_TRANS_NOTENUM")
	public Integer getNoteNum() {
		return noteNum;
	}

	@Column(name = "I_BLACK_NOTENUM")
	public Integer getBlNum() {
		return blNum;
	}

	@Column(name = "I_REPEAT_NOTENUM")
	public Integer getRepeatNum() {
		return repeatNum;
	}

	@Column(name = "I_CALLBACK_NOTENUM")
	public Integer getCallBackNum() {
		return callBackNum;
	}

	@Column(name = "C_RESERVE3")
	public String getReserve3() {
		return reserve3;
	}

	public void setNoteNum(Integer noteNum) {
		this.noteNum = noteNum;
	}

	public void setBlNum(Integer blNum) {
		this.blNum = blNum;
	}

	public void setRepeatNum(Integer repeatNum) {
		this.repeatNum = repeatNum;
	}

	public void setCallBackNum(Integer callBackNum) {
		this.callBackNum = callBackNum;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	private String reserve1;
	private String reserve2;
	private String reserve3;
	
	//新增字段，保存时间
	private String createDate;

	@Column(name = "C_CREATE_DATE")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public TranInfo() {
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

	@Column(name = "C_TRANS_CODE")
	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	@Column(name = "C_TRANS_ORGID")
	public String getTransOrgid() {
		return transOrgid;
	}

	public void setTransOrgid(String transOrgid) {
		this.transOrgid = transOrgid;
	}

	@Column(name = "C_TERMID")
	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	@Column(name = "C_TRANS_DATE")
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	@Column(name = "C_TRANS_TIME")
	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	@Column(name = "C_ACCOUNT_NO")
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "C_JOURNAL_NO")
	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	@Column(name = "I_TRANS_AMT")
	public Integer getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(Integer transAmt) {
		this.transAmt = transAmt;
	}

	@Column(name = "C_TRANS_RESULT")
	public String getTransResult() {
		return transResult;
	}

	public void setTransResult(String transResult) {
		this.transResult = transResult;
	}

	@Column(name = "C_RESERVE1")
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	@Column(name = "C_RESERVE2")
	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
}
