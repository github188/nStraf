package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * --问题及建议 create table problem_suggestion ( ID VARCHAR2(32) primary key,
 * raise_man VARCHAR2(32), raise_date DATE, summary VARCHAR2(100), status
 * char(1),--0未解决，1已解决，2跟进中 priority char(1), -0,1，2依次为低，中高 desc VARCHAR2(2048),
 * solution VARCHAR2(2048), resolve_man VARCHAR2(32), resolve_date VARCHAR2(32),
 * );
 * 
 * @author feel
 * 
 */
@Entity
@Table(name ="OA_PRJRISK")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class PrjRisk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "c_id", unique = true, nullable = false)
	private String id;

	@Column(name = "d_createdate")
	@Temporal(TemporalType.DATE)
	private Date createdate;  //提出日期
	@Column(name = "c_createman")
	private String createman; //状态
	@Column(name = "c_type")
	private String type; //风陷类别
	@Column(name = "c_prjname")
	private String prjname; //项目名称
	@Column(name = "c_summary")
	private String summary;//风险描述
	@Column(name = "c_riskdesc")
	private String riskdesc;//风险描述
	@Column(name = "c_urgent")
	private String urgent; //紧急程度
	@Column(name = "c_pond")
	private String pond;  //严重性
	@Column(name = "c_status")
	private String status; //状态
	@Column(name = "c_suggest")
	private String suggest ; //建议方案
	@Column(name = "c_plan")
	private String plan; //计划
	@Column(name = "c_fruit")
	private String fruit; //解决情况
	@Column(name = "d_handleterm")
	@Temporal(TemporalType.DATE)
	private Date  handleterm;  //处理期限
	@Column(name = "c_handleman")
	private String  handleman;  //处理者
	@Column(name = "d_factdate")
	@Temporal(TemporalType.DATE)
	private Date  factdate;  //实际处理日期
	@Column(name = "c_note")
	private String   note;  //备注
	@Column(name = "c_updateman")
	private String  updateman;//最近更新者
	@Column(name = "d_update")
	@Temporal(TemporalType.DATE)
	private Date  update;  //最近更新日期
	@Column(name = "c_userid")
	private String userid;//审批人员
	@Column(name = "c_deptname")
	private String deptname;//部门名称
	@Column(name = "c_groupname")
	private String groupname;//组别名称
	
//	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pnoSeq")
	@Column(name="c_rno",updatable=false)
	private String rno;
	
	public PrjRisk(){
		
	}
   public PrjRisk( String id){
		this.id=id;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getRno() {
		return rno;
	}
	public void setRno(String rno) {
		this.rno = rno;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getCreateman() {
		return createman;
	}
	public void setCreateman(String createman) {
		this.createman = createman;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrjname() {
		return prjname;
	}
	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}
	public String getRiskdesc() {
		return riskdesc;
	}
	public void setRiskdesc(String riskdesc) {
		this.riskdesc = riskdesc;
	}
	public String getUrgent() {
		return urgent;
	}
	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}
	public String getPond() {
		return pond;
	}
	public void setPond(String pond) {
		this.pond = pond;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public Date getHandleterm() {
		return handleterm;
	}
	public void setHandleterm(Date handleterm) {
		this.handleterm = handleterm;
	}
	public String getHandleman() {
		return handleman;
	}
	public void setHandleman(String handleman) {
		this.handleman = handleman;
	}
	public Date getFactdate() {
		return factdate;
	}
	public void setFactdate(Date factdate) {
		this.factdate = factdate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUpdateman() {
		return updateman;
	}
	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}
	public Date getUpdate() {
		return update;
	}
	public void setUpdate(Date update) {
		this.update = update;
	}

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFruit() {
		return fruit;
	}
	public void setFruit(String fruit) {
		this.fruit = fruit;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}


}

