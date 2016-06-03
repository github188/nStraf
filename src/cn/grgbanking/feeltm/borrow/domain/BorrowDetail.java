package cn.grgbanking.feeltm.borrow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_BORROW_DETAIL")
public class BorrowDetail {
/**
 * create by ywei9
 */
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="C_REPLYDATE")
	private Date replydate;//还款日期
	
	@Column(name="C_BORROWID")
	private String borrowid;//借款id
	
	@Column(name="C_AMOUNT")
	private double amount;//借款金额
	
	@Column(name = "c_note")
	private String note;//审批人id
	
	@Column(name="c_residueAmount")
	private double residueAmount;//剩余金额
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getReplydate() {
		return replydate;
	}
	public void setReplydate(Date replydate) {
		this.replydate = replydate;
	}
	public String getBorrowid() {
		return borrowid;
	}
	public void setBorrowid(String borrowid) {
		this.borrowid = borrowid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public double getResidueAmount() {
		return residueAmount;
	}
	public void setResidueAmount(double residueAmount) {
		this.residueAmount = residueAmount;
	}
	
}
