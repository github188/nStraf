package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="dailydeed")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class DailyDeed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	@Column(name = "month_date")
	private String month_date;   //月份
	@Column(name = "remark")
	private String remark; //备注
	@Column(name = "user_id")
	private String user_id;//用户名
	@Column(name = "modify_date")
	private String modify_date; //更新时间
	@Column(name = "group_name")
	private String group_name;  //组名
	
	@Column(name = "man_lock")
	private double man_lock;  //锁定
	@Column(name = "edit_lock")
	private double edit_lock;  //经理是否修改过
	@Column(name = "update_man")
	private String  update_man;//最近更新者
	@Column(name = "subtotal_m")
	private double  subtotal_m;//经理评分
	@Column(name = "subtotal_d")
	private double  subtotal_d;//主任评分
	@Column(name = "subtotal_z")
	private double  subtotal_z;//主任评分
	@Column(name = "subtotal_s")
	private double  subtotal_s;//总分	
	@Column(name = "subtotal_m_1")
	private double  subtotal_m_1;//总分
	@Column(name = "subtotal_m_2")
	private double  subtotal_m_2;//总分
	@Column(name = "subtotal_m_3")
	private double  subtotal_m_3;//总分
	@Column(name = "subtotal_m_4")
	private double  subtotal_m_4;//总分
	@Column(name = "subtotal_m_5")
	private double  subtotal_m_5;//总分
	@Column(name = "subtotal_m_6")
	private double  subtotal_m_6;//总分
	@Column(name = "subtotal_m_7")
	private double  subtotal_m_7;//总分
	@Column(name = "subtotal_m_8")
	private double  subtotal_m_8;//总分

	@Column(name = "subtotal_s_1")
	private double  subtotal_s_1;//总分
	@Column(name = "subtotal_s_2")
	private double  subtotal_s_2;//总分
	@Column(name = "subtotal_s_3")
	private double  subtotal_s_3;//总分
	@Column(name = "subtotal_s_4")
	private double  subtotal_s_4;//总分
	@Column(name = "subtotal_s_5")
	private double  subtotal_s_5;//总分
	@Column(name = "subtotal_s_6")
	private double  subtotal_s_6;//总分
	@Column(name = "subtotal_s_7")
	private double  subtotal_s_7;//总分
	@Column(name = "subtotal_s_8")
	private double  subtotal_s_8;//总分	
	
	@Column(name = "subtotal_d_1")
	private double  subtotal_d_1;//总分
	@Column(name = "subtotal_d_2")
	private double  subtotal_d_2;//总分
	@Column(name = "subtotal_d_3")
	private double  subtotal_d_3;//总分
	@Column(name = "subtotal_d_4")
	private double  subtotal_d_4;//总分
	@Column(name = "subtotal_d_5")
	private double  subtotal_d_5;//总分
	@Column(name = "subtotal_d_6")
	private double  subtotal_d_6;//总分
	@Column(name = "subtotal_d_7")
	private double  subtotal_d_7;//总分
	@Column(name = "subtotal_d_8")
	private double  subtotal_d_8;//总分
	
	@Column(name = "subtotal_z_1")
	private double  subtotal_z_1;//总分
	@Column(name = "subtotal_z_2")
	private double  subtotal_z_2;//总分
	@Column(name = "subtotal_z_3")
	private double  subtotal_z_3;//总分
	@Column(name = "subtotal_z_4")
	private double  subtotal_z_4;//总分
	@Column(name = "subtotal_z_5")
	private double  subtotal_z_5;//总分
	@Column(name = "subtotal_z_6")
	private double  subtotal_z_6;//总分
	@Column(name = "subtotal_z_7")
	private double  subtotal_z_7;//总分
	@Column(name = "subtotal_z_8")
	private double  subtotal_z_8;//总分

	
	
	@Column(name = "remark_z_1")
	private String  remark_z_1;//总分
	@Column(name = "remark_z_2")
	private String  remark_z_2;//总分
	@Column(name = "remark_z_3")
	private String  remark_z_3;//总分
	@Column(name = "remark_z_4")
	private String  remark_z_4;//总分
	@Column(name = "remark_z_5")
	private String  remark_z_5;//总分
	@Column(name = "remark_z_6")
	private String  remark_z_6;//总分
	@Column(name = "remark_z_7")
	private String  remark_z_7;//总分
	@Column(name = "remark_z_8")
	private String  remark_z_8;//总分


	@Column(name = "remark_d_1")
	private String  remark_d_1;//总分
	@Column(name = "remark_d_2")
	private String  remark_d_2;//总分
	@Column(name = "remark_d_3")
	private String  remark_d_3;//总分
	@Column(name = "remark_d_4")
	private String  remark_d_4;//总分
	@Column(name = "remark_d_5")
	private String  remark_d_5;//总分
	@Column(name = "remark_d_6")
	private String  remark_d_6;//总分
	@Column(name = "remark_d_7")
	private String  remark_d_7;//总分
	@Column(name = "remark_d_8")
	private String  remark_d_8;//总分

	
	@Column(name = "pno",updatable=false)
	private String  pno;//总分
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
	public DailyDeed(){
		
	}

	public DailyDeed(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public String getMonth_date() {
		return month_date;
	}


	public String getRemark() {
		return remark;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getModify_date() {
		return modify_date;
	}
	public String getGroup_name() {
		return group_name;
	}
	public String getUpdate_man() {
		return update_man;
	}
	public double getSubtotal_m() {
		return subtotal_m;
	}
	public double getSubtotal_d() {
		return subtotal_d;
	}
	public double getSubtotal_s() {
		return subtotal_s;
	}
	public String getCreateDateString() {
		return createDateString;
	}
	public String getUpdateDateString() {
		return updateDateString;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}
	public void setSubtotal_m(double subtotal_m) {
		this.subtotal_m = subtotal_m;
	}
	public void setSubtotal_d(double subtotal_d) {
		this.subtotal_d = subtotal_d;
	}
	public void setSubtotal_s(double subtotal_s) {
		this.subtotal_s = subtotal_s;
	}
	public double getSubtotal_z() {
		return subtotal_z;
	}

	public void setSubtotal_z(double subtotal_z) {
		this.subtotal_z = subtotal_z;
	}

	public double getSubtotal_m_1() {
		return subtotal_m_1;
	}

	public double getSubtotal_m_2() {
		return subtotal_m_2;
	}

	public double getSubtotal_m_3() {
		return subtotal_m_3;
	}

	public double getSubtotal_m_4() {
		return subtotal_m_4;
	}

	public double getSubtotal_m_5() {
		return subtotal_m_5;
	}

	public double getSubtotal_m_6() {
		return subtotal_m_6;
	}

	public double getSubtotal_m_7() {
		return subtotal_m_7;
	}

	public double getSubtotal_m_8() {
		return subtotal_m_8;
	}

	

	public double getSubtotal_d_1() {
		return subtotal_d_1;
	}

	public double getSubtotal_d_2() {
		return subtotal_d_2;
	}

	public double getSubtotal_d_3() {
		return subtotal_d_3;
	}

	public double getSubtotal_d_4() {
		return subtotal_d_4;
	}

	public double getSubtotal_d_5() {
		return subtotal_d_5;
	}

	public double getSubtotal_d_6() {
		return subtotal_d_6;
	}

	public double getSubtotal_d_7() {
		return subtotal_d_7;
	}

	public double getSubtotal_d_8() {
		return subtotal_d_8;
	}

	




	
	public void setSubtotal_m_1(double subtotal_m_1) {
		this.subtotal_m_1 = subtotal_m_1;
	}

	public void setSubtotal_m_2(double subtotal_m_2) {
		this.subtotal_m_2 = subtotal_m_2;
	}

	public void setSubtotal_m_3(double subtotal_m_3) {
		this.subtotal_m_3 = subtotal_m_3;
	}

	public void setSubtotal_m_4(double subtotal_m_4) {
		this.subtotal_m_4 = subtotal_m_4;
	}

	public void setSubtotal_m_5(double subtotal_m_5) {
		this.subtotal_m_5 = subtotal_m_5;
	}

	public void setSubtotal_m_6(double subtotal_m_6) {
		this.subtotal_m_6 = subtotal_m_6;
	}

	public void setSubtotal_m_7(double subtotal_m_7) {
		this.subtotal_m_7 = subtotal_m_7;
	}

	public void setSubtotal_m_8(double subtotal_m_8) {
		this.subtotal_m_8 = subtotal_m_8;
	}

	
	
	public void setSubtotal_d_1(double subtotal_d_1) {
		this.subtotal_d_1 = subtotal_d_1;
	}

	public void setSubtotal_d_2(double subtotal_d_2) {
		this.subtotal_d_2 = subtotal_d_2;
	}

	public void setSubtotal_d_3(double subtotal_d_3) {
		this.subtotal_d_3 = subtotal_d_3;
	}

	public void setSubtotal_d_4(double subtotal_d_4) {
		this.subtotal_d_4 = subtotal_d_4;
	}

	public void setSubtotal_d_5(double subtotal_d_5) {
		this.subtotal_d_5 = subtotal_d_5;
	}

	public void setSubtotal_d_6(double subtotal_d_6) {
		this.subtotal_d_6 = subtotal_d_6;
	}

	public void setSubtotal_d_7(double subtotal_d_7) {
		this.subtotal_d_7 = subtotal_d_7;
	}

	public void setSubtotal_d_8(double subtotal_d_8) {
		this.subtotal_d_8 = subtotal_d_8;
	}

	
	public double getSubtotal_z_1() {
		return subtotal_z_1;
	}

	public void setSubtotal_z_1(double subtotal_z_1) {
		this.subtotal_z_1 = subtotal_z_1;
	}

	public double getSubtotal_z_2() {
		return subtotal_z_2;
	}

	public void setSubtotal_z_2(double subtotal_z_2) {
		this.subtotal_z_2 = subtotal_z_2;
	}

	public double getSubtotal_z_3() {
		return subtotal_z_3;
	}

	public void setSubtotal_z_3(double subtotal_z_3) {
		this.subtotal_z_3 = subtotal_z_3;
	}

	public double getSubtotal_z_4() {
		return subtotal_z_4;
	}

	public void setSubtotal_z_4(double subtotal_z_4) {
		this.subtotal_z_4 = subtotal_z_4;
	}

	public double getSubtotal_z_5() {
		return subtotal_z_5;
	}

	public void setSubtotal_z_5(double subtotal_z_5) {
		this.subtotal_z_5 = subtotal_z_5;
	}

	public double getSubtotal_z_6() {
		return subtotal_z_6;
	}

	public void setSubtotal_z_6(double subtotal_z_6) {
		this.subtotal_z_6 = subtotal_z_6;
	}

	public double getSubtotal_z_7() {
		return subtotal_z_7;
	}

	public void setSubtotal_z_7(double subtotal_z_7) {
		this.subtotal_z_7 = subtotal_z_7;
	}

	public double getSubtotal_z_8() {
		return subtotal_z_8;
	}

	public void setSubtotal_z_8(double subtotal_z_8) {
		this.subtotal_z_8 = subtotal_z_8;
	}

	

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}
	public void setUpdateDateString(String updateDateString) {
		this.updateDateString = updateDateString;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}

	public double getMan_lock() {
		return man_lock;
	}

	public void setMan_lock(double man_lock) {
		this.man_lock = man_lock;
	}

	public double getEdit_lock() {
		return edit_lock;
	}

	public void setEdit_lock(double edit_lock) {
		this.edit_lock = edit_lock;
	}

	public double getSubtotal_s_1() {
		return subtotal_s_1;
	}

	public void setSubtotal_s_1(double subtotal_s_1) {
		this.subtotal_s_1 = subtotal_s_1;
	}

	public double getSubtotal_s_2() {
		return subtotal_s_2;
	}

	public void setSubtotal_s_2(double subtotal_s_2) {
		this.subtotal_s_2 = subtotal_s_2;
	}

	public double getSubtotal_s_3() {
		return subtotal_s_3;
	}

	public void setSubtotal_s_3(double subtotal_s_3) {
		this.subtotal_s_3 = subtotal_s_3;
	}

	public double getSubtotal_s_4() {
		return subtotal_s_4;
	}

	public void setSubtotal_s_4(double subtotal_s_4) {
		this.subtotal_s_4 = subtotal_s_4;
	}

	public double getSubtotal_s_5() {
		return subtotal_s_5;
	}

	public void setSubtotal_s_5(double subtotal_s_5) {
		this.subtotal_s_5 = subtotal_s_5;
	}

	public double getSubtotal_s_6() {
		return subtotal_s_6;
	}

	public void setSubtotal_s_6(double subtotal_s_6) {
		this.subtotal_s_6 = subtotal_s_6;
	}

	public double getSubtotal_s_7() {
		return subtotal_s_7;
	}

	public void setSubtotal_s_7(double subtotal_s_7) {
		this.subtotal_s_7 = subtotal_s_7;
	}

	public double getSubtotal_s_8() {
		return subtotal_s_8;
	}

	public void setSubtotal_s_8(double subtotal_s_8) {
		this.subtotal_s_8 = subtotal_s_8;
	}


	public String getRemark_z_1() {
		return remark_z_1;
	}

	public void setRemark_z_1(String remark_z_1) {
		this.remark_z_1 = remark_z_1;
	}

	public String getRemark_z_2() {
		return remark_z_2;
	}

	public void setRemark_z_2(String remark_z_2) {
		this.remark_z_2 = remark_z_2;
	}

	public String getRemark_z_3() {
		return remark_z_3;
	}

	public void setRemark_z_3(String remark_z_3) {
		this.remark_z_3 = remark_z_3;
	}

	public String getRemark_z_4() {
		return remark_z_4;
	}

	public void setRemark_z_4(String remark_z_4) {
		this.remark_z_4 = remark_z_4;
	}

	public String getRemark_z_5() {
		return remark_z_5;
	}

	public void setRemark_z_5(String remark_z_5) {
		this.remark_z_5 = remark_z_5;
	}

	public String getRemark_z_6() {
		return remark_z_6;
	}

	public void setRemark_z_6(String remark_z_6) {
		this.remark_z_6 = remark_z_6;
	}

	public String getRemark_z_7() {
		return remark_z_7;
	}

	public void setRemark_z_7(String remark_z_7) {
		this.remark_z_7 = remark_z_7;
	}

	public String getRemark_z_8() {
		return remark_z_8;
	}

	public void setRemark_z_8(String remark_z_8) {
		this.remark_z_8 = remark_z_8;
	}

	
	public String getRemark_d_1() {
		return remark_d_1;
	}

	public void setRemark_d_1(String remark_d_1) {
		this.remark_d_1 = remark_d_1;
	}

	public String getRemark_d_2() {
		return remark_d_2;
	}

	public void setRemark_d_2(String remark_d_2) {
		this.remark_d_2 = remark_d_2;
	}

	public String getRemark_d_3() {
		return remark_d_3;
	}

	public void setRemark_d_3(String remark_d_3) {
		this.remark_d_3 = remark_d_3;
	}

	public String getRemark_d_4() {
		return remark_d_4;
	}

	public void setRemark_d_4(String remark_d_4) {
		this.remark_d_4 = remark_d_4;
	}

	public String getRemark_d_5() {
		return remark_d_5;
	}

	public void setRemark_d_5(String remark_d_5) {
		this.remark_d_5 = remark_d_5;
	}

	public String getRemark_d_6() {
		return remark_d_6;
	}

	public void setRemark_d_6(String remark_d_6) {
		this.remark_d_6 = remark_d_6;
	}

	public String getRemark_d_7() {
		return remark_d_7;
	}

	public void setRemark_d_7(String remark_d_7) {
		this.remark_d_7 = remark_d_7;
	}

	public String getRemark_d_8() {
		return remark_d_8;
	}

	public void setRemark_d_8(String remark_d_8) {
		this.remark_d_8 = remark_d_8;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
