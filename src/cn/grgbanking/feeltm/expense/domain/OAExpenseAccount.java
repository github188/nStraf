package cn.grgbanking.feeltm.expense.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="OA_EXPENSE_ACCOUNT")
public class OAExpenseAccount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;// 主键
	
	@Column(name = "C_USERID")
	private String userId; //用户标识
	
	@Column(name = "C_ENO")
	private String expenseNum; //报销流水号
	
	@Column(name = "C_USERNAME")
	private String userName;   //姓名
	
	@Column(name = "C_DETNAME")
	private String detName; //部门
	
	@Column(name = "C_PRJNAME")
	private String prjName; //项目
	
	@Column(name = "C_TYPE")
	private String type; //报销类别
	
	@Column(name = "I_SUM")
	private Double sum; //报销金额
	
	@Column(name = "C_STATUS")
	private String status;  //报销单状态
	
	@Column(name = "C_TRAVEL_DETAIL")
	private String travelDetail;  //费用报销明细
	
	@Column(name = "C_COST_DETAIL")
	private String costDetail;  //费用报销明细
	
	@Column(name = "C_SUDATE")
	private Date  submitDate; // 提交日期
	
	@Column(name = "C_REASON")
	private String  reason;// 打回原因
	
	@Column(name = "C_NOTE")
	private String  note;// 备注
	
	@Column(name = "C_UPDATEMAN")
	private String  updateMan;// 更新人
	
	@Column(name = "C_UPDATE")
	private Date  updateDate;// 更新时间
	
	@Column(name = "C_UPLOG")
	private String upLog;//提交记录
	
	@Column(name = "C_FEEDBACK")
    private String feedBack;//修改反馈
	
	@Column(name = "i_approveSum")
	private Double approveSum;//核准金额
	
	@Column(name = "c_updatemanid")
	private String updatemanid;//审批人id
	
	@Column(name = "c_expenseprjname")
	private String expenseprjname;//报销项目名称
	
	@Column(name = "c_printdetail")
	private String printdetail;//是否已打印报销明细单
	
	@Column(name = "c_printcost")
	private String printcost;//是否已打印报销费用单
	
	
	
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	public OAExpenseAccount(){
		
	}
	public OAExpenseAccount( String id){
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getExpenseNum() {
		return expenseNum;
	}
	public void setExpenseNum(String expenseNum) {
		this.expenseNum = expenseNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDetName() {
		return detName;
	}
	public void setDetName(String detName) {
		this.detName = detName;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTravelDetail() {
		return travelDetail;
	}
	public void setTravelDetail(String travelDetail) {
		this.travelDetail = travelDetail;
	}
	public String getCostDetail() {
		return costDetail;
	}
	public void setCostDetail(String costDetail) {
		this.costDetail = costDetail;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	
	public String getUpLog() {
		return upLog;
	}
	public void setUpLog(String upLog) {
		this.upLog = upLog;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Double getApproveSum() {
		return approveSum;
	}
	public void setApproveSum(Double approveSum) {
		this.approveSum = approveSum;
	}
	public String getUpdatemanid() {
		return updatemanid;
	}
	public void setUpdatemanid(String updatemanid) {
		this.updatemanid = updatemanid;
	}
	public String getExpenseprjname() {
		return expenseprjname;
	}
	public void setExpenseprjname(String expenseprjname) {
		this.expenseprjname = expenseprjname;
	}
	public String getPrintdetail() {
		return printdetail;
	}
	public void setPrintdetail(String printdetail) {
		this.printdetail = printdetail;
	}
	public String getPrintcost() {
		return printcost;
	}
	public void setPrintcost(String printcost) {
		this.printcost = printcost;
	}
	
	
}
