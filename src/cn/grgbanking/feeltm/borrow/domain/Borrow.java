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
@Table(name="OA_BORROW")
public class Borrow {
/**
 * create by ywei9
 */
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	
	@Column(name="C_USERID")
	private String userid;//用户标示
	
	@Temporal(TemporalType.DATE)
	@Column(name="D_CREATEDATE")
	private Date createdate;//申请时间
	
	@Column(name="C_USERMAN")
	private String userman;//申请人
	
	@Column(name="C_DETNAME")
	private String detname;//部门
	
	@Column(name="C_GROUPNAME")
	private String groupname;//项目组
	
	@Column(name="C_TRIPCITY")
	private String tripcity;//出差地
	
	@Column(name="C_TYPE")
	private String type;//借款类型（出差、备用金）
	
	@Column(name="I_AMOUNT")
	private double amount;//借款金额
	
	@Column(name="C_BORROW_REASON")
	private String borrowReason;//借款原因
	
	@Temporal(TemporalType.DATE)
	@Column(name="D_BORROWDATE")
	private Date borrowdate;//发款时间 修改为“借款日期”
	
	@Temporal(TemporalType.DATE)
	@Column(name="D_EXPECTED_REPAYDATE")
	private Date expectedRepaydate;//预计还款时间
	
	@Temporal(TemporalType.DATE)
	@Column(name="D_REPAYDATE")
	private Date repaydate;//还款结束时间
	
	@Column(name="C_REASON")
	private String reason;//打回原因
	
	@Column(name="C_STATUS")
	private String status;//状态 	(新增；待审核；打回；审核通过（待发款）；已发款；还款超时（变红）；已还款)
	
	@Column(name="i_approveSum")
	private Double approveSum;//核准金额
	
	@Column(name = "C_UPDATEMAN")
	private String  updateMan;// 更新人
	
	@Temporal(TemporalType.DATE)
	@Column(name = "C_UPDATE")
	private Date  updateDate;// 更新时间
	
	@Column(name = "c_updatemanid")
	private String updatemanid;//审批人id
	
	@Column(name = "d_reminddate")
	private Date remindDate;//超时还款上次提醒日期
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getUserman() {
		return userman;
	}
	public void setUserman(String userman) {
		this.userman = userman;
	}
	public String getDetname() {
		return detname;
	}
	public void setDetname(String detname) {
		this.detname = detname;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getTripcity() {
		return tripcity;
	}
	public void setTripcity(String tripcity) {
		this.tripcity = tripcity;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getBorrowdate() {
		return borrowdate;
	}
	public void setBorrowdate(Date borrowdate) {
		this.borrowdate = borrowdate;
	}
	public Date getExpectedRepaydate() {
		return expectedRepaydate;
	}
	public void setExpectedRepaydate(Date expectedRepaydate) {
		this.expectedRepaydate = expectedRepaydate;
	}
	public Date getRepaydate() {
		return repaydate;
	}
	public void setRepaydate(Date repaydate) {
		this.repaydate = repaydate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBorrowReason() {
		return borrowReason;
	}
	public void setBorrowReason(String borrowReason) {
		this.borrowReason = borrowReason;
	}
	public Double getApproveSum() {
		return approveSum;
	}
	public void setApproveSum(Double approveSum) {
		this.approveSum = approveSum;
	}
	public String getUpdateMan() {
		return updateMan;
	}
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdatemanid() {
		return updatemanid;
	}
	public void setUpdatemanid(String updatemanid) {
		this.updatemanid = updatemanid;
	}
	public Date getRemindDate() {
		return remindDate;
	}
	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}
}
