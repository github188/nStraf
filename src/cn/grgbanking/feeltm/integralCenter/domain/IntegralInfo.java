package cn.grgbanking.feeltm.integralCenter.domain;
/**
 * @author whxing
 * 积分中心实体类
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="OA_INTEGRAL")
public class IntegralInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_USERID")
	private String userId;      //用户ID
	
	@Column(name = "C_USERNAME")
	private String userName;   //用户名 
	
	@Column(name = "C_DETID")
	private String detNameId;  //部门ID
	
	@Column(name = "C_DETNAME") //部门名
	private String detName;
	
	@Column(name = "C_GROUPID") //项目组ID
	private String groupId;
	
	@Column(name = "C_GROUPNAME") //项目名称
	private String groupName;
	
	@Column(name = "N_INTEGRAL")  //获得积分
 	private int integral;
	
	@Column(name = "C_REASON")  //获得积分原由
	private String reason;
	
	@Column(name = "C_GATEGORY")  //获得积分类别
	private String gategory;
	
	@Column(name = "C_GATEGORYID") //获得积分类别ID
	private String gategoryId;
	
	@Column(name = "C_STATU")  //获得积分状态
	private String statu;
	
	@Column(name = "C_APPROVALID") // 审批人ID
	private String approvalId;
	
	@Column(name = "C_APPROVALNAME")  //审批人名字
	private String approvalName;
	
	@Column(name = "D_APPROVALDATE")  //审批日期
	private Date approvalDate;
	
	@Column(name = "C_UPDATEUSER_ID") //修改人ID
	private String updateManId;
	
	@Column(name = "C_UPDATEUSER_NAME") //修改人姓名
	private Date updateMan;
	
	@Column(name = "D_UPDATE_TIME")  //修改时间
	private Date updateTime;
	
	@Column(name = "D_CREATE_TIME") //创建时间
	private Date createTime;
	
	@Column(name = "C_NOTE") //被赞或点赞标志
	private String note;
	
	@Column(name = "D_LOGDATE") //被赞或点赞标志
	private Date logDate;
	
	@Column(name="EXT1")
	private String ext1;
	
	@Column(name="EXT2")
	private String ext2;
	
	@Column(name="EXT3")
	private String ext3;
	
	@Column(name="EXT4")
	private String ext4;
	
	@Column(name="EXT5")
	private Date ext5;
	
	@Column(name="EXT6")
	private Date ext6;
	
	@Column(name="EXT7")
	private Double ext7;
	
	@Column(name="EXT8")
	private Double ext8;

    public IntegralInfo(){
		
	}
	

	//额外属性
//	@Transient
// 	private String integralSum;//获得总积分
//	
//	
//	public IntegralInfo(){
//		
//	}
//    public IntegralInfo(String integralSum){
//		
//	}
//	
//	public String getIntegralSum() {
//		return integralSum;
//	}
//
//	public void setIntegralSum(String integralSum) {
//		this.integralSum = integralSum;
//	}

    public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDetNameId() {
		return detNameId;
	}

	public void setDetNameId(String detNameId) {
		this.detNameId = detNameId;
	}

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGategory() {
		return gategory;
	}

	public void setGategory(String gategory) {
		this.gategory = gategory;
	}

	public String getGategoryId() {
		return gategoryId;
	}

	public void setGategoryId(String gategoryId) {
		this.gategoryId = gategoryId;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getUpdateManId() {
		return updateManId;
	}

	public void setUpdateManId(String updateManId) {
		this.updateManId = updateManId;
	}

	public Date getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(Date updateMan) {
		this.updateMan = updateMan;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public Date getExt5() {
		return ext5;
	}

	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	public Date getExt6() {
		return ext6;
	}

	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	public Double getExt7() {
		return ext7;
	}

	public void setExt7(Double ext7) {
		this.ext7 = ext7;
	}

	public Double getExt8() {
		return ext8;
	}

	public void setExt8(Double ext8) {
		this.ext8 = ext8;
	}

	
	
	
}
