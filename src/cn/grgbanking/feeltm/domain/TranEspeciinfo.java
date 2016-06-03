package cn.grgbanking.feeltm.domain;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
/**
 * <p>Title: model</p>
 * <p>Description: </p>
 * @author hail
 * @version 1.0
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAN_ESPECIINFO")
public class TranEspeciinfo implements Serializable{

  private String id;
  private String tranId;
  private String transCode;
  private String transOrgid;
  private String termid;
  private String transDate;
  private String transTime;
  private String accountNo;
  private String journalNo;
  private Integer transAmt;
  private String transResult;
  private Integer transNotenum;
  private Integer blackNotenum;
  private Integer repeatNotenum;
  private Integer callbackNotenum;
  private String reserve1;
  private String reserve2;
  private String reserve3;
  private String especiseq;
  private String cometype;
  private String result;
  private String sequence;
  private String currency;
  private String denomination;
  private String createDate;
  private Date anyDate;
  private String specialId ;
  private String note  ;

  public TranEspeciinfo (        String  id,       String  tranId,       String  transCode,       String  transOrgid,       String  termid,       String  transDate,       String  transTime,       String  accountNo,       String  journalNo,       Integer  transAmt,       String  transResult,       Integer  transNotenum,       Integer  blackNotenum,       Integer  repeatNotenum,       Integer  callbackNotenum,       String  reserve1,       String  reserve2,       String  reserve3,       String  especiseq,       String  cometype,       String  result,       String  sequence,       String  currency,       String  denomination,       String  createDate,       Date  anyDate ,String specialId,String note) { 
       this.id = id;
       this.tranId = tranId;
       this.transCode = transCode;
       this.transOrgid = transOrgid;
       this.termid = termid;
       this.transDate = transDate;
       this.transTime = transTime;
       this.accountNo = accountNo;
       this.journalNo = journalNo;
       this.transAmt = transAmt;
       this.transResult = transResult;
       this.transNotenum = transNotenum;
       this.blackNotenum = blackNotenum;
       this.repeatNotenum = repeatNotenum;
       this.callbackNotenum = callbackNotenum;
       this.reserve1 = reserve1;
       this.reserve2 = reserve2;
       this.reserve3 = reserve3;
       this.especiseq = especiseq;
       this.cometype = cometype;
       this.result = result;
       this.sequence = sequence;
       this.currency = currency;
       this.denomination = denomination;
       this.createDate = createDate;
       this.anyDate = anyDate;
       this.specialId=specialId;
       this.note=note;
  }
  public  TranEspeciinfo(){
 }
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
  @Column(name = "C_ID", unique = true, nullable = false)
  public String getId() {
    return id;
  }
  public void setId(String id){
    this.id = id;
  }
  @Column(name = "C_TRAN_ID")
  public String getTranId() {
    return tranId;
  }
  public void setTranId(String tranId){
    this.tranId = tranId;
  }
  @Column(name = "C_TRANS_CODE")
  public String getTransCode() {
    return transCode;
  }
  public void setTransCode(String transCode){
    this.transCode = transCode;
  }
  @Column(name = "C_TRANS_ORGID")
  public String getTransOrgid() {
    return transOrgid;
  }
  public void setTransOrgid(String transOrgid){
    this.transOrgid = transOrgid;
  }
  @Column(name = "C_TERMID")
  public String getTermid() {
    return termid;
  }
  public void setTermid(String termid){
    this.termid = termid;
  }
  @Column(name = "C_TRANS_DATE")
  public String getTransDate() {
    return transDate;
  }
  public void setTransDate(String transDate){
    this.transDate = transDate;
  }
  @Column(name = "C_TRANS_TIME")
  public String getTransTime() {
    return transTime;
  }
  public void setTransTime(String transTime){
    this.transTime = transTime;
  }
  @Column(name = "C_ACCOUNT_NO")
  public String getAccountNo() {
    return accountNo;
  }
  public void setAccountNo(String accountNo){
    this.accountNo = accountNo;
  }
  @Column(name = "C_JOURNAL_NO")
  public String getJournalNo() {
    return journalNo;
  }
  public void setJournalNo(String journalNo){
    this.journalNo = journalNo;
  }
  @Column(name = "I_TRANS_AMT")
  public Integer getTransAmt() {
    return transAmt;
  }
  public void setTransAmt(Integer transAmt){
    this.transAmt = transAmt;
  }
  @Column(name = "C_TRANS_RESULT")
  public String getTransResult() {
    return transResult;
  }
  public void setTransResult(String transResult){
    this.transResult = transResult;
  }
  @Column(name = "I_TRANS_NOTENUM")
  public Integer getTransNotenum() {
    return transNotenum;
  }
  public void setTransNotenum(Integer transNotenum){
    this.transNotenum = transNotenum;
  }
  @Column(name = "I_BLACK_NOTENUM")
  public Integer getBlackNotenum() {
    return blackNotenum;
  }
  public void setBlackNotenum(Integer blackNotenum){
    this.blackNotenum = blackNotenum;
  }
  @Column(name = "I_REPEAT_NOTENUM")
  public Integer getRepeatNotenum() {
    return repeatNotenum;
  }
  public void setRepeatNotenum(Integer repeatNotenum){
    this.repeatNotenum = repeatNotenum;
  }
  @Column(name = "I_CALLBACK_NOTENUM")
  public Integer getCallbackNotenum() {
    return callbackNotenum;
  }
  public void setCallbackNotenum(Integer callbackNotenum){
    this.callbackNotenum = callbackNotenum;
  }
  @Column(name = "C_RESERVE1")
  public String getReserve1() {
    return reserve1;
  }
  public void setReserve1(String reserve1){
    this.reserve1 = reserve1;
  }
  @Column(name = "C_RESERVE2")
  public String getReserve2() {
    return reserve2;
  }
  public void setReserve2(String reserve2){
    this.reserve2 = reserve2;
  }
  @Column(name = "C_RESERVE3")
  public String getReserve3() {
    return reserve3;
  }
  public void setReserve3(String reserve3){
    this.reserve3 = reserve3;
  }
  @Column(name = "C_ESPECISEQ")
  public String getEspeciseq() {
    return especiseq;
  }
  public void setEspeciseq(String especiseq){
    this.especiseq = especiseq;
  }
  @Column(name = "C_COMETYPE")
  public String getCometype() {
    return cometype;
  }
  public void setCometype(String cometype){
    this.cometype = cometype;
  }
  @Column(name = "C_RESULT")
  public String getResult() {
    return result;
  }
  public void setResult(String result){
    this.result = result;
  }
  @Column(name = "C_SEQUENCE")
  public String getSequence() {
    return sequence;
  }
  public void setSequence(String sequence){
    this.sequence = sequence;
  }
  @Column(name = "C_CURRENCY")
  public String getCurrency() {
    return currency;
  }
  public void setCurrency(String currency){
    this.currency = currency;
  }
  @Column(name = "C_DENOMINATION")
  public String getDenomination() {
    return denomination;
  }
  public void setDenomination(String denomination){
    this.denomination = denomination;
  }
  @Column(name = "C_CREATE_DATE")
  public String getCreateDate() {
    return createDate;
  }
  public void setCreateDate(String createDate){
    this.createDate = createDate;
  }
  @Column(name = "D_ANY_DATE")
  public Date getAnyDate() {
    return anyDate;
  }
  public void setAnyDate(Date anyDate){
    this.anyDate = anyDate;
  }
  @Column(name = "C_SPECIAL_ID")
  public String getSpecialId() {
	  return specialId;
   }
  public void setSpecialId(String specialId) {
	this.specialId = specialId;
  }
  @Column(name = "C_NOTE")
  public String getNote() {
	return note;
  }
  public void setNote(String note) {
	this.note = note;
  }
  
  
}
