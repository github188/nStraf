package cn.grgbanking.feeltm.approval.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 2014-4-29
 * @author lhyan3
 * 审批流程记录
 */
@Entity
@Table(name="OA_ApprovalRecord")
public class ApprovalRecord {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;//主键id
	/**
	 * 流程中唯一的标识号
	 * 如通知notify中就记录流水号
	 */
	@Column(name="C_APPROVALNAME")
	private String approvalName;//审批流程名称
	/**
	 * 记录名称为自动拼接：
	 * 如通知审批：则拼接为（通知模块审批）
	 */
	@Column(name="C_RECODENAME")
	private String recodeName;//记录名称
	@Column(name="C_OPINION")
	private String opinion;//审批意见
	@Column(name="C_RESULT")
	private String result;//审批结果
	@Column(name="C_APPROVALUSER")
	private String approvalUser;//审批人
	@Column(name="D_APPROVALTIME")
	private Date approvalTime;//审批时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApprovalName() {
		return approvalName;
	}
	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getApprovalUser() {
		return approvalUser;
	}
	public void setApprovalUser(String approvalUser) {
		this.approvalUser = approvalUser;
	}
	public Date getApprovalTime() {
		return approvalTime;
	}
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRecodeName() {
		return recodeName;
	}
	public void setRecodeName(String recodeName) {
		this.recodeName = recodeName;
	}

}
