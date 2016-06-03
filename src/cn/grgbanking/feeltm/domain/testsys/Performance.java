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
@Table(name ="performance")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class Performance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	@Column(name = "month_date")
	private String month_date;   //月份
	@Column(name = "performance_desc")
	private String performance_desc; //绩效项
	@Column(name = "status")
	private String status; //状态
	@Column(name = "remark")
	private String remark; //备注
	@Column(name = "user_id")
	private String user_id;//用户名
	@Column(name = "modify_date")
	private String modify_date; //更新时间
	@Column(name = "group_name")
	private String group_name;  //组名
	@Column(name = "sub_project")
	private String sub_project;  //组名
	@Column(name = "add_project")
	private String add_project;  //组名
	
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
	@Column(name = "subtotal_m_9")
	private double  subtotal_m_9;//总分
	@Column(name = "subtotal_m_10")
	private double  subtotal_m_10;//总分
	@Column(name = "subtotal_m_11")
	private double  subtotal_m_11;//总分
	@Column(name = "subtotal_m_12")
	private double  subtotal_m_12;//总分
	@Column(name = "subtotal_m_13")
	private double  subtotal_m_13;//总分
	@Column(name = "subtotal_m_14")
	private double  subtotal_m_14;//总分
	@Column(name = "subtotal_m_15")
	private double  subtotal_m_15;//总分
	@Column(name = "subtotal_m_16")
	private double  subtotal_m_16;//总分
	@Column(name = "subtotal_m_17")
	private double  subtotal_m_17;//总分
	@Column(name = "subtotal_m_18")
	private double  subtotal_m_18;//总分
	@Column(name = "subtotal_m_19")
	private double  subtotal_m_19;//总分
	@Column(name = "subtotal_m_20")
	private double  subtotal_m_20;//总分
	@Column(name = "subtotal_m_21")
	private double  subtotal_m_21;//总分
	@Column(name = "subtotal_m_22")
	private double  subtotal_m_22;//总分
	@Column(name = "subtotal_m_23")
	private double  subtotal_m_23;//总分
	@Column(name = "subtotal_m_24")
	private double  subtotal_m_24;//总分
	@Column(name = "subtotal_m_25")
	private double  subtotal_m_25;//总分
	@Column(name = "subtotal_m_26")
	private double  subtotal_m_26;//总分
	@Column(name = "subtotal_m_27")
	private double  subtotal_m_27;//总分
	@Column(name = "subtotal_m_28")
	private double  subtotal_m_28;//总分
	@Column(name = "subtotal_m_29")
	private double  subtotal_m_29;//总分
	@Column(name = "subtotal_m_30")
	private double  subtotal_m_30;//总分
	@Column(name = "subtotal_m_31")
	private double  subtotal_m_31;//总分
	@Column(name = "subtotal_m_32")
	private double  subtotal_m_32;//总分
	@Column(name = "subtotal_m_33")
	private double  subtotal_m_33;//总分
	@Column(name = "subtotal_m_34")
	private double  subtotal_m_34;//总分
	@Column(name = "subtotal_m_35")
	private double  subtotal_m_35;//总分
	@Column(name = "subtotal_m_36")
	private double  subtotal_m_36;//总分
	@Column(name = "subtotal_m_37")
	private double  subtotal_m_37;//总分
	@Column(name = "subtotal_m_38")
	private double  subtotal_m_38;//总分
	@Column(name = "subtotal_m_39")
	private double  subtotal_m_39;//总分
	@Column(name = "subtotal_m_40")
	private double  subtotal_m_40;//总分
	@Column(name = "subtotal_m_41")
	private double  subtotal_m_41;//总分
	@Column(name = "subtotal_m_42")
	private double  subtotal_m_42;//总分
	@Column(name = "subtotal_m_43")
	private double  subtotal_m_43;//总分
	@Column(name = "subtotal_m_44")
	private double  subtotal_m_44;//总分
	@Column(name = "subtotal_m_45")
	private double  subtotal_m_45;//总分
	@Column(name = "subtotal_m_46")
	private double  subtotal_m_46;//总分
	@Column(name = "subtotal_m_47")
	private double  subtotal_m_47;//总分
	@Column(name = "subtotal_m_48")
	private double  subtotal_m_48;//总分
	@Column(name = "subtotal_m_49")
	private double  subtotal_m_49;//总分
	@Column(name = "subtotal_m_50")
	private double  subtotal_m_50;//总分
	@Column(name = "subtotal_m_51")
	private double  subtotal_m_51;//总分
	@Column(name = "subtotal_m_52")
	private double  subtotal_m_52;//总分
	@Column(name = "subtotal_m_53")
	private double  subtotal_m_53;//总分
	@Column(name = "subtotal_m_54")
	private double  subtotal_m_54;//总分
	@Column(name = "subtotal_m_55")
	private double  subtotal_m_55;//总分
	@Column(name = "subtotal_m_56")
	private double  subtotal_m_56;//总分
	
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
	@Column(name = "subtotal_d_9")
	private double  subtotal_d_9;//总分
	@Column(name = "subtotal_d_10")
	private double  subtotal_d_10;//总分
	@Column(name = "subtotal_d_11")
	private double  subtotal_d_11;//总分
	@Column(name = "subtotal_d_12")
	private double  subtotal_d_12;//总分
	@Column(name = "subtotal_d_13")
	private double  subtotal_d_13;//总分
	@Column(name = "subtotal_d_14")
	private double  subtotal_d_14;//总分
	@Column(name = "subtotal_d_15")
	private double  subtotal_d_15;//总分
	@Column(name = "subtotal_d_16")
	private double  subtotal_d_16;//总分
	@Column(name = "subtotal_d_17")
	private double  subtotal_d_17;//总分
	@Column(name = "subtotal_d_18")
	private double  subtotal_d_18;//总分
	@Column(name = "subtotal_d_19")
	private double  subtotal_d_19;//总分
	@Column(name = "subtotal_d_20")
	private double  subtotal_d_20;//总分
	@Column(name = "subtotal_d_21")
	private double  subtotal_d_21;//总分
	@Column(name = "subtotal_d_22")
	private double  subtotal_d_22;//总分
	@Column(name = "subtotal_d_23")
	private double  subtotal_d_23;//总分
	@Column(name = "subtotal_d_24")
	private double  subtotal_d_24;//总分
	@Column(name = "subtotal_d_25")
	private double  subtotal_d_25;//总分
	@Column(name = "subtotal_d_26")
	private double  subtotal_d_26;//总分
	@Column(name = "subtotal_d_27")
	private double  subtotal_d_27;//总分
	@Column(name = "subtotal_d_28")
	private double  subtotal_d_28;//总分
	@Column(name = "subtotal_d_29")
	private double  subtotal_d_29;//总分
	@Column(name = "subtotal_d_30")
	private double  subtotal_d_30;//总分
	@Column(name = "subtotal_d_31")
	private double  subtotal_d_31;//总分
	@Column(name = "subtotal_d_32")
	private double  subtotal_d_32;//总分
	@Column(name = "subtotal_d_33")
	private double  subtotal_d_33;//总分
	@Column(name = "subtotal_d_34")
	private double  subtotal_d_34;//总分
	@Column(name = "subtotal_d_35")
	private double  subtotal_d_35;//总分
	@Column(name = "subtotal_d_36")
	private double  subtotal_d_36;//总分
	@Column(name = "subtotal_d_37")
	private double  subtotal_d_37;//总分
	@Column(name = "subtotal_d_38")
	private double  subtotal_d_38;//总分
	@Column(name = "subtotal_d_39")
	private double  subtotal_d_39;//总分
	@Column(name = "subtotal_d_40")
	private double  subtotal_d_40;//总分
	@Column(name = "subtotal_d_41")
	private double  subtotal_d_41;//总分
	@Column(name = "subtotal_d_42")
	private double  subtotal_d_42;//总分
	@Column(name = "subtotal_d_43")
	private double  subtotal_d_43;//总分
	@Column(name = "subtotal_d_44")
	private double  subtotal_d_44;//总分
	@Column(name = "subtotal_d_45")
	private double  subtotal_d_45;//总分
	@Column(name = "subtotal_d_46")
	private double  subtotal_d_46;//总分
	@Column(name = "subtotal_d_47")
	private double  subtotal_d_47;//总分
	@Column(name = "subtotal_d_48")
	private double  subtotal_d_48;//总分
	@Column(name = "subtotal_d_49")
	private double  subtotal_d_49;//总分
	@Column(name = "subtotal_d_50")
	private double  subtotal_d_50;//总分
	@Column(name = "subtotal_d_51")
	private double  subtotal_d_51;//总分
	@Column(name = "subtotal_d_52")
	private double  subtotal_d_52;//总分
	@Column(name = "subtotal_d_53")
	private double  subtotal_d_53;//总分
	@Column(name = "subtotal_d_54")
	private double  subtotal_d_54;//总分
	@Column(name = "subtotal_d_55")
	private double  subtotal_d_55;//总分
	@Column(name = "subtotal_d_56")
	private double  subtotal_d_56;//总分
	
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
	@Column(name = "subtotal_z_9")
	private double  subtotal_z_9;//总分
	@Column(name = "subtotal_z_10")
	private double  subtotal_z_10;//总分
	@Column(name = "subtotal_z_11")
	private double  subtotal_z_11;//总分
	@Column(name = "subtotal_z_12")
	private double  subtotal_z_12;//总分
	@Column(name = "subtotal_z_13")
	private double  subtotal_z_13;//总分
	@Column(name = "subtotal_z_14")
	private double  subtotal_z_14;//总分
	@Column(name = "subtotal_z_15")
	private double  subtotal_z_15;//总分
	@Column(name = "subtotal_z_16")
	private double  subtotal_z_16;//总分
	@Column(name = "subtotal_z_17")
	private double  subtotal_z_17;//总分
	@Column(name = "subtotal_z_18")
	private double  subtotal_z_18;//总分
	@Column(name = "subtotal_z_19")
	private double  subtotal_z_19;//总分
	@Column(name = "subtotal_z_20")
	private double  subtotal_z_20;//总分
	@Column(name = "subtotal_z_21")
	private double  subtotal_z_21;//总分
	@Column(name = "subtotal_z_22")
	private double  subtotal_z_22;//总分
	@Column(name = "subtotal_z_23")
	private double  subtotal_z_23;//总分
	@Column(name = "subtotal_z_24")
	private double  subtotal_z_24;//总分
	@Column(name = "subtotal_z_25")
	private double  subtotal_z_25;//总分
	@Column(name = "subtotal_z_26")
	private double  subtotal_z_26;//总分
	@Column(name = "subtotal_z_27")
	private double  subtotal_z_27;//总分
	@Column(name = "subtotal_z_28")
	private double  subtotal_z_28;//总分
	@Column(name = "subtotal_z_29")
	private double  subtotal_z_29;//总分
	@Column(name = "subtotal_z_30")
	private double  subtotal_z_30;//总分
	@Column(name = "subtotal_z_31")
	private double  subtotal_z_31;//总分
	@Column(name = "subtotal_z_32")
	private double  subtotal_z_32;//总分
	@Column(name = "subtotal_z_33")
	private double  subtotal_z_33;//总分
	@Column(name = "subtotal_z_34")
	private double  subtotal_z_34;//总分
	@Column(name = "subtotal_z_35")
	private double  subtotal_z_35;//总分
	@Column(name = "subtotal_z_36")
	private double  subtotal_z_36;//总分
	@Column(name = "subtotal_z_37")
	private double  subtotal_z_37;//总分
	@Column(name = "subtotal_z_38")
	private double  subtotal_z_38;//总分
	@Column(name = "subtotal_z_39")
	private double  subtotal_z_39;//总分
	@Column(name = "subtotal_z_40")
	private double  subtotal_z_40;//总分
	@Column(name = "subtotal_z_41")
	private double  subtotal_z_41;//总分
	@Column(name = "subtotal_z_42")
	private double  subtotal_z_42;//总分
	@Column(name = "subtotal_z_43")
	private double  subtotal_z_43;//总分
	@Column(name = "subtotal_z_44")
	private double  subtotal_z_44;//总分
	@Column(name = "subtotal_z_45")
	private double  subtotal_z_45;//总分
	@Column(name = "subtotal_z_46")
	private double  subtotal_z_46;//总分
	@Column(name = "subtotal_z_47")
	private double  subtotal_z_47;//总分
	@Column(name = "subtotal_z_48")
	private double  subtotal_z_48;//总分
	@Column(name = "subtotal_z_49")
	private double  subtotal_z_49;//总分
	@Column(name = "subtotal_z_50")
	private double  subtotal_z_50;//总分
	@Column(name = "subtotal_z_51")
	private double  subtotal_z_51;//总分
	@Column(name = "subtotal_z_52")
	private double  subtotal_z_52;//总分
	@Column(name = "subtotal_z_53")
	private double  subtotal_z_53;//总分
	@Column(name = "subtotal_z_54")
	private double  subtotal_z_54;//总分
	@Column(name = "subtotal_z_55")
	private double  subtotal_z_55;//总分
	@Column(name = "subtotal_z_56")
	private double  subtotal_z_56;//总分
	
	@Column(name = "remark_1")
	private String  remark_1;//总分
	@Column(name = "remark_2")
	private String  remark_2;//总分
	@Column(name = "remark_3")
	private String  remark_3;//总分
	@Column(name = "remark_4")
	private String  remark_4;//总分
	@Column(name = "remark_5")
	private String  remark_5;//总分
	@Column(name = "remark_6")
	private String  remark_6;//总分
	@Column(name = "remark_7")
	private String  remark_7;//总分
	@Column(name = "remark_8")
	private String  remark_8;//总分
	@Column(name = "remark_9")
	private String  remark_9;//总分
	@Column(name = "remark_10")
	private String  remark_10;//总分
	@Column(name = "remark_11")
	private String  remark_11;//总分
	@Column(name = "remark_12")
	private String  remark_12;//总分
	@Column(name = "remark_13")
	private String  remark_13;//总分
	@Column(name = "remark_14")
	private String  remark_14;//总分
	@Column(name = "remark_15")
	private String  remark_15;//总分
	@Column(name = "remark_16")
	private String  remark_16;//总分
	@Column(name = "remark_17")
	private String  remark_17;//总分
	@Column(name = "remark_18")
	private String  remark_18;//总分
	@Column(name = "remark_19")
	private String  remark_19;//总分
	@Column(name = "remark_20")
	private String  remark_20;//总分
	@Column(name = "remark_21")
	private String  remark_21;//总分
	@Column(name = "remark_22")
	private String  remark_22;//总分
	@Column(name = "remark_23")
	private String  remark_23;//总分
	@Column(name = "remark_24")
	private String  remark_24;//总分
	@Column(name = "remark_25")
	private String  remark_25;//总分
	@Column(name = "remark_26")
	private String  remark_26;//总分
	@Column(name = "remark_27")
	private String  remark_27;//总分
	@Column(name = "remark_28")
	private String  remark_28;//总分
	@Column(name = "remark_29")
	private String  remark_29;//总分
	@Column(name = "remark_30")
	private String  remark_30;//总分
	@Column(name = "remark_31")
	private String  remark_31;//总分
	@Column(name = "remark_32")
	private String  remark_32;//总分
	@Column(name = "remark_33")
	private String  remark_33;//总分
	@Column(name = "remark_34")
	private String  remark_34;//总分
	@Column(name = "remark_35")
	private String  remark_35;//总分
	@Column(name = "remark_36")
	private String  remark_36;//总分
	@Column(name = "remark_37")
	private String  remark_37;//总分
	@Column(name = "remark_38")
	private String  remark_38;//总分
	@Column(name = "remark_39")
	private String  remark_39;//总分
	@Column(name = "remark_40")
	private String  remark_40;//总分
	@Column(name = "remark_41")
	private String  remark_41;//总分
	@Column(name = "remark_42")
	private String  remark_42;//总分
	@Column(name = "remark_43")
	private String  remark_43;//总分
	@Column(name = "remark_44")
	private String  remark_44;//总分
	@Column(name = "remark_45")
	private String  remark_45;//总分
	@Column(name = "remark_46")
	private String  remark_46;//总分
	@Column(name = "remark_47")
	private String  remark_47;//总分
	@Column(name = "remark_48")
	private String  remark_48;//总分
	@Column(name = "remark_49")
	private String  remark_49;//总分
	@Column(name = "remark_50")
	private String  remark_50;//总分
	@Column(name = "remark_51")
	private String  remark_51;//总分
	@Column(name = "remark_52")
	private String  remark_52;//总分
	@Column(name = "remark_53")
	private String  remark_53;//总分
	@Column(name = "remark_54")
	private String  remark_54;//总分
	@Column(name = "remark_55")
	private String  remark_55;//总分
	@Column(name = "remark_56")
	private String  remark_56;//总分
	
	@Column(name = "pno",updatable=false)
	private String  pno;//总分
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
	public Performance(){
		
	}

	public Performance(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public String getMonth_date() {
		return month_date;
	}
	public String getPerformance_desc() {
		return performance_desc;
	}
	public String getStatus() {
		return status;
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
	public void setPerformance_desc(String performance_desc) {
		this.performance_desc = performance_desc;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public double getSubtotal_m_9() {
		return subtotal_m_9;
	}

	public double getSubtotal_m_10() {
		return subtotal_m_10;
	}

	public double getSubtotal_m_11() {
		return subtotal_m_11;
	}

	public double getSubtotal_m_12() {
		return subtotal_m_12;
	}

	public double getSubtotal_m_13() {
		return subtotal_m_13;
	}

	public double getSubtotal_m_14() {
		return subtotal_m_14;
	}

	public double getSubtotal_m_15() {
		return subtotal_m_15;
	}

	public double getSubtotal_m_16() {
		return subtotal_m_16;
	}

	public double getSubtotal_m_17() {
		return subtotal_m_17;
	}

	public double getSubtotal_m_18() {
		return subtotal_m_18;
	}

	public double getSubtotal_m_19() {
		return subtotal_m_19;
	}

	public double getSubtotal_m_20() {
		return subtotal_m_20;
	}

	public double getSubtotal_m_21() {
		return subtotal_m_21;
	}

	public double getSubtotal_m_22() {
		return subtotal_m_22;
	}

	public double getSubtotal_m_23() {
		return subtotal_m_23;
	}

	public double getSubtotal_m_24() {
		return subtotal_m_24;
	}

	public double getSubtotal_m_25() {
		return subtotal_m_25;
	}

	public double getSubtotal_m_26() {
		return subtotal_m_26;
	}

	public double getSubtotal_m_27() {
		return subtotal_m_27;
	}

	public double getSubtotal_m_28() {
		return subtotal_m_28;
	}

	public double getSubtotal_m_29() {
		return subtotal_m_29;
	}

	public double getSubtotal_m_30() {
		return subtotal_m_30;
	}

	public double getSubtotal_m_31() {
		return subtotal_m_31;
	}

	public double getSubtotal_m_32() {
		return subtotal_m_32;
	}

	public double getSubtotal_m_33() {
		return subtotal_m_33;
	}

	public double getSubtotal_m_34() {
		return subtotal_m_34;
	}

	public double getSubtotal_m_35() {
		return subtotal_m_35;
	}

	public double getSubtotal_m_36() {
		return subtotal_m_36;
	}

	public double getSubtotal_m_37() {
		return subtotal_m_37;
	}

	public double getSubtotal_m_38() {
		return subtotal_m_38;
	}

	public double getSubtotal_m_39() {
		return subtotal_m_39;
	}

	public double getSubtotal_m_40() {
		return subtotal_m_40;
	}

	public double getSubtotal_m_41() {
		return subtotal_m_41;
	}

	public double getSubtotal_m_42() {
		return subtotal_m_42;
	}

	public double getSubtotal_m_43() {
		return subtotal_m_43;
	}

	public double getSubtotal_m_44() {
		return subtotal_m_44;
	}

	public double getSubtotal_m_45() {
		return subtotal_m_45;
	}

	public double getSubtotal_m_46() {
		return subtotal_m_46;
	}

	public double getSubtotal_m_47() {
		return subtotal_m_47;
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

	public double getSubtotal_d_9() {
		return subtotal_d_9;
	}

	public double getSubtotal_d_10() {
		return subtotal_d_10;
	}

	public double getSubtotal_d_11() {
		return subtotal_d_11;
	}

	public double getSubtotal_d_12() {
		return subtotal_d_12;
	}

	public double getSubtotal_d_13() {
		return subtotal_d_13;
	}

	public double getSubtotal_d_14() {
		return subtotal_d_14;
	}

	public double getSubtotal_d_15() {
		return subtotal_d_15;
	}

	public double getSubtotal_d_16() {
		return subtotal_d_16;
	}

	public double getSubtotal_d_17() {
		return subtotal_d_17;
	}

	public double getSubtotal_d_18() {
		return subtotal_d_18;
	}

	public double getSubtotal_d_19() {
		return subtotal_d_19;
	}

	public double getSubtotal_d_20() {
		return subtotal_d_20;
	}

	public double getSubtotal_d_21() {
		return subtotal_d_21;
	}

	public double getSubtotal_d_22() {
		return subtotal_d_22;
	}

	public double getSubtotal_d_23() {
		return subtotal_d_23;
	}

	public double getSubtotal_d_24() {
		return subtotal_d_24;
	}

	public double getSubtotal_d_25() {
		return subtotal_d_25;
	}

	public double getSubtotal_d_26() {
		return subtotal_d_26;
	}

	public double getSubtotal_d_27() {
		return subtotal_d_27;
	}

	public double getSubtotal_d_28() {
		return subtotal_d_28;
	}

	public double getSubtotal_d_29() {
		return subtotal_d_29;
	}

	public double getSubtotal_d_30() {
		return subtotal_d_30;
	}

	public double getSubtotal_d_31() {
		return subtotal_d_31;
	}

	public double getSubtotal_d_32() {
		return subtotal_d_32;
	}

	public double getSubtotal_d_33() {
		return subtotal_d_33;
	}

	public double getSubtotal_d_34() {
		return subtotal_d_34;
	}

	public double getSubtotal_d_35() {
		return subtotal_d_35;
	}

	public double getSubtotal_d_36() {
		return subtotal_d_36;
	}

	public double getSubtotal_d_37() {
		return subtotal_d_37;
	}

	public double getSubtotal_d_38() {
		return subtotal_d_38;
	}

	public double getSubtotal_d_39() {
		return subtotal_d_39;
	}

	public double getSubtotal_d_40() {
		return subtotal_d_40;
	}

	public double getSubtotal_d_41() {
		return subtotal_d_41;
	}

	public double getSubtotal_d_42() {
		return subtotal_d_42;
	}

	public double getSubtotal_d_43() {
		return subtotal_d_43;
	}

	public double getSubtotal_d_44() {
		return subtotal_d_44;
	}

	public double getSubtotal_d_45() {
		return subtotal_d_45;
	}

	public double getSubtotal_d_46() {
		return subtotal_d_46;
	}

	public double getSubtotal_d_47() {
		return subtotal_d_47;
	}

	public String getRemark_1() {
		return remark_1;
	}

	public String getRemark_2() {
		return remark_2;
	}

	public String getRemark_3() {
		return remark_3;
	}

	public String getRemark_4() {
		return remark_4;
	}

	public String getRemark_5() {
		return remark_5;
	}

	public String getRemark_6() {
		return remark_6;
	}

	public String getRemark_7() {
		return remark_7;
	}

	public String getRemark_8() {
		return remark_8;
	}

	public String getRemark_9() {
		return remark_9;
	}

	public String getRemark_10() {
		return remark_10;
	}

	public String getRemark_11() {
		return remark_11;
	}

	public String getRemark_12() {
		return remark_12;
	}

	public String getRemark_13() {
		return remark_13;
	}

	public String getRemark_14() {
		return remark_14;
	}

	public String getRemark_15() {
		return remark_15;
	}

	public String getRemark_16() {
		return remark_16;
	}

	public String getRemark_17() {
		return remark_17;
	}

	public String getRemark_18() {
		return remark_18;
	}

	public String getRemark_19() {
		return remark_19;
	}

	public String getRemark_20() {
		return remark_20;
	}

	public String getRemark_21() {
		return remark_21;
	}

	public String getRemark_22() {
		return remark_22;
	}

	public String getRemark_23() {
		return remark_23;
	}

	public String getRemark_24() {
		return remark_24;
	}

	public String getRemark_25() {
		return remark_25;
	}

	public String getRemark_26() {
		return remark_26;
	}

	public String getRemark_27() {
		return remark_27;
	}

	public String getRemark_28() {
		return remark_28;
	}

	public String getRemark_29() {
		return remark_29;
	}

	public String getRemark_30() {
		return remark_30;
	}

	public String getRemark_31() {
		return remark_31;
	}

	public String getRemark_32() {
		return remark_32;
	}

	public String getRemark_33() {
		return remark_33;
	}

	public String getRemark_34() {
		return remark_34;
	}

	public String getRemark_35() {
		return remark_35;
	}

	public String getRemark_36() {
		return remark_36;
	}

	public String getRemark_37() {
		return remark_37;
	}

	public String getRemark_38() {
		return remark_38;
	}

	public String getRemark_39() {
		return remark_39;
	}

	public String getRemark_40() {
		return remark_40;
	}

	public String getRemark_41() {
		return remark_41;
	}

	public String getRemark_42() {
		return remark_42;
	}

	public String getRemark_43() {
		return remark_43;
	}

	public String getRemark_44() {
		return remark_44;
	}

	public String getRemark_45() {
		return remark_45;
	}

	public String getRemark_46() {
		return remark_46;
	}

	public String getRemark_47() {
		return remark_47;
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

	public void setSubtotal_m_9(double subtotal_m_9) {
		this.subtotal_m_9 = subtotal_m_9;
	}

	public void setSubtotal_m_10(double subtotal_m_10) {
		this.subtotal_m_10 = subtotal_m_10;
	}

	public void setSubtotal_m_11(double subtotal_m_11) {
		this.subtotal_m_11 = subtotal_m_11;
	}

	public void setSubtotal_m_12(double subtotal_m_12) {
		this.subtotal_m_12 = subtotal_m_12;
	}

	public void setSubtotal_m_13(double subtotal_m_13) {
		this.subtotal_m_13 = subtotal_m_13;
	}

	public void setSubtotal_m_14(double subtotal_m_14) {
		this.subtotal_m_14 = subtotal_m_14;
	}

	public void setSubtotal_m_15(double subtotal_m_15) {
		this.subtotal_m_15 = subtotal_m_15;
	}

	public void setSubtotal_m_16(double subtotal_m_16) {
		this.subtotal_m_16 = subtotal_m_16;
	}

	public void setSubtotal_m_17(double subtotal_m_17) {
		this.subtotal_m_17 = subtotal_m_17;
	}

	public void setSubtotal_m_18(double subtotal_m_18) {
		this.subtotal_m_18 = subtotal_m_18;
	}

	public void setSubtotal_m_19(double subtotal_m_19) {
		this.subtotal_m_19 = subtotal_m_19;
	}

	public void setSubtotal_m_20(double subtotal_m_20) {
		this.subtotal_m_20 = subtotal_m_20;
	}

	public void setSubtotal_m_21(double subtotal_m_21) {
		this.subtotal_m_21 = subtotal_m_21;
	}

	public void setSubtotal_m_22(double subtotal_m_22) {
		this.subtotal_m_22 = subtotal_m_22;
	}

	public void setSubtotal_m_23(double subtotal_m_23) {
		this.subtotal_m_23 = subtotal_m_23;
	}

	public void setSubtotal_m_24(double subtotal_m_24) {
		this.subtotal_m_24 = subtotal_m_24;
	}

	public void setSubtotal_m_25(double subtotal_m_25) {
		this.subtotal_m_25 = subtotal_m_25;
	}

	public void setSubtotal_m_26(double subtotal_m_26) {
		this.subtotal_m_26 = subtotal_m_26;
	}

	public void setSubtotal_m_27(double subtotal_m_27) {
		this.subtotal_m_27 = subtotal_m_27;
	}

	public void setSubtotal_m_28(double subtotal_m_28) {
		this.subtotal_m_28 = subtotal_m_28;
	}

	public void setSubtotal_m_29(double subtotal_m_29) {
		this.subtotal_m_29 = subtotal_m_29;
	}

	public void setSubtotal_m_30(double subtotal_m_30) {
		this.subtotal_m_30 = subtotal_m_30;
	}

	public void setSubtotal_m_31(double subtotal_m_31) {
		this.subtotal_m_31 = subtotal_m_31;
	}

	public void setSubtotal_m_32(double subtotal_m_32) {
		this.subtotal_m_32 = subtotal_m_32;
	}

	public void setSubtotal_m_33(double subtotal_m_33) {
		this.subtotal_m_33 = subtotal_m_33;
	}

	public void setSubtotal_m_34(double subtotal_m_34) {
		this.subtotal_m_34 = subtotal_m_34;
	}

	public void setSubtotal_m_35(double subtotal_m_35) {
		this.subtotal_m_35 = subtotal_m_35;
	}

	public void setSubtotal_m_36(double subtotal_m_36) {
		this.subtotal_m_36 = subtotal_m_36;
	}

	public void setSubtotal_m_37(double subtotal_m_37) {
		this.subtotal_m_37 = subtotal_m_37;
	}

	public void setSubtotal_m_38(double subtotal_m_38) {
		this.subtotal_m_38 = subtotal_m_38;
	}

	public void setSubtotal_m_39(double subtotal_m_39) {
		this.subtotal_m_39 = subtotal_m_39;
	}

	public void setSubtotal_m_40(double subtotal_m_40) {
		this.subtotal_m_40 = subtotal_m_40;
	}

	public void setSubtotal_m_41(double subtotal_m_41) {
		this.subtotal_m_41 = subtotal_m_41;
	}

	public void setSubtotal_m_42(double subtotal_m_42) {
		this.subtotal_m_42 = subtotal_m_42;
	}

	public void setSubtotal_m_43(double subtotal_m_43) {
		this.subtotal_m_43 = subtotal_m_43;
	}

	public void setSubtotal_m_44(double subtotal_m_44) {
		this.subtotal_m_44 = subtotal_m_44;
	}

	public void setSubtotal_m_45(double subtotal_m_45) {
		this.subtotal_m_45 = subtotal_m_45;
	}

	public void setSubtotal_m_46(double subtotal_m_46) {
		this.subtotal_m_46 = subtotal_m_46;
	}

	public void setSubtotal_m_47(double subtotal_m_47) {
		this.subtotal_m_47 = subtotal_m_47;
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

	public void setSubtotal_d_9(double subtotal_d_9) {
		this.subtotal_d_9 = subtotal_d_9;
	}

	public void setSubtotal_d_10(double subtotal_d_10) {
		this.subtotal_d_10 = subtotal_d_10;
	}

	public void setSubtotal_d_11(double subtotal_d_11) {
		this.subtotal_d_11 = subtotal_d_11;
	}

	public void setSubtotal_d_12(double subtotal_d_12) {
		this.subtotal_d_12 = subtotal_d_12;
	}

	public void setSubtotal_d_13(double subtotal_d_13) {
		this.subtotal_d_13 = subtotal_d_13;
	}

	public void setSubtotal_d_14(double subtotal_d_14) {
		this.subtotal_d_14 = subtotal_d_14;
	}

	public void setSubtotal_d_15(double subtotal_d_15) {
		this.subtotal_d_15 = subtotal_d_15;
	}

	public void setSubtotal_d_16(double subtotal_d_16) {
		this.subtotal_d_16 = subtotal_d_16;
	}

	public void setSubtotal_d_17(double subtotal_d_17) {
		this.subtotal_d_17 = subtotal_d_17;
	}

	public void setSubtotal_d_18(double subtotal_d_18) {
		this.subtotal_d_18 = subtotal_d_18;
	}

	public void setSubtotal_d_19(double subtotal_d_19) {
		this.subtotal_d_19 = subtotal_d_19;
	}

	public void setSubtotal_d_20(double subtotal_d_20) {
		this.subtotal_d_20 = subtotal_d_20;
	}

	public void setSubtotal_d_21(double subtotal_d_21) {
		this.subtotal_d_21 = subtotal_d_21;
	}

	public void setSubtotal_d_22(double subtotal_d_22) {
		this.subtotal_d_22 = subtotal_d_22;
	}

	public void setSubtotal_d_23(double subtotal_d_23) {
		this.subtotal_d_23 = subtotal_d_23;
	}

	public void setSubtotal_d_24(double subtotal_d_24) {
		this.subtotal_d_24 = subtotal_d_24;
	}

	public void setSubtotal_d_25(double subtotal_d_25) {
		this.subtotal_d_25 = subtotal_d_25;
	}

	public void setSubtotal_d_26(double subtotal_d_26) {
		this.subtotal_d_26 = subtotal_d_26;
	}

	public void setSubtotal_d_27(double subtotal_d_27) {
		this.subtotal_d_27 = subtotal_d_27;
	}

	public void setSubtotal_d_28(double subtotal_d_28) {
		this.subtotal_d_28 = subtotal_d_28;
	}

	public void setSubtotal_d_29(double subtotal_d_29) {
		this.subtotal_d_29 = subtotal_d_29;
	}

	public void setSubtotal_d_30(double subtotal_d_30) {
		this.subtotal_d_30 = subtotal_d_30;
	}

	public void setSubtotal_d_31(double subtotal_d_31) {
		this.subtotal_d_31 = subtotal_d_31;
	}

	public void setSubtotal_d_32(double subtotal_d_32) {
		this.subtotal_d_32 = subtotal_d_32;
	}

	public void setSubtotal_d_33(double subtotal_d_33) {
		this.subtotal_d_33 = subtotal_d_33;
	}

	public void setSubtotal_d_34(double subtotal_d_34) {
		this.subtotal_d_34 = subtotal_d_34;
	}

	public void setSubtotal_d_35(double subtotal_d_35) {
		this.subtotal_d_35 = subtotal_d_35;
	}

	public void setSubtotal_d_36(double subtotal_d_36) {
		this.subtotal_d_36 = subtotal_d_36;
	}

	public void setSubtotal_d_37(double subtotal_d_37) {
		this.subtotal_d_37 = subtotal_d_37;
	}

	public void setSubtotal_d_38(double subtotal_d_38) {
		this.subtotal_d_38 = subtotal_d_38;
	}

	public void setSubtotal_d_39(double subtotal_d_39) {
		this.subtotal_d_39 = subtotal_d_39;
	}

	public void setSubtotal_d_40(double subtotal_d_40) {
		this.subtotal_d_40 = subtotal_d_40;
	}

	public void setSubtotal_d_41(double subtotal_d_41) {
		this.subtotal_d_41 = subtotal_d_41;
	}

	public void setSubtotal_d_42(double subtotal_d_42) {
		this.subtotal_d_42 = subtotal_d_42;
	}

	public void setSubtotal_d_43(double subtotal_d_43) {
		this.subtotal_d_43 = subtotal_d_43;
	}

	public void setSubtotal_d_44(double subtotal_d_44) {
		this.subtotal_d_44 = subtotal_d_44;
	}

	public void setSubtotal_d_45(double subtotal_d_45) {
		this.subtotal_d_45 = subtotal_d_45;
	}

	public void setSubtotal_d_46(double subtotal_d_46) {
		this.subtotal_d_46 = subtotal_d_46;
	}

	public void setSubtotal_d_47(double subtotal_d_47) {
		this.subtotal_d_47 = subtotal_d_47;
	}

	public void setRemark_1(String remark_1) {
		this.remark_1 = remark_1;
	}

	public void setRemark_2(String remark_2) {
		this.remark_2 = remark_2;
	}

	public void setRemark_3(String remark_3) {
		this.remark_3 = remark_3;
	}

	public void setRemark_4(String remark_4) {
		this.remark_4 = remark_4;
	}

	public void setRemark_5(String remark_5) {
		this.remark_5 = remark_5;
	}

	public void setRemark_6(String remark_6) {
		this.remark_6 = remark_6;
	}

	public void setRemark_7(String remark_7) {
		this.remark_7 = remark_7;
	}

	public void setRemark_8(String remark_8) {
		this.remark_8 = remark_8;
	}

	public void setRemark_9(String remark_9) {
		this.remark_9 = remark_9;
	}

	public void setRemark_10(String remark_10) {
		this.remark_10 = remark_10;
	}

	public void setRemark_11(String remark_11) {
		this.remark_11 = remark_11;
	}

	public void setRemark_12(String remark_12) {
		this.remark_12 = remark_12;
	}

	public void setRemark_13(String remark_13) {
		this.remark_13 = remark_13;
	}

	public void setRemark_14(String remark_14) {
		this.remark_14 = remark_14;
	}

	public void setRemark_15(String remark_15) {
		this.remark_15 = remark_15;
	}

	public void setRemark_16(String remark_16) {
		this.remark_16 = remark_16;
	}

	public void setRemark_17(String remark_17) {
		this.remark_17 = remark_17;
	}

	public void setRemark_18(String remark_18) {
		this.remark_18 = remark_18;
	}

	public void setRemark_19(String remark_19) {
		this.remark_19 = remark_19;
	}

	public void setRemark_20(String remark_20) {
		this.remark_20 = remark_20;
	}

	public void setRemark_21(String remark_21) {
		this.remark_21 = remark_21;
	}

	public void setRemark_22(String remark_22) {
		this.remark_22 = remark_22;
	}

	public void setRemark_23(String remark_23) {
		this.remark_23 = remark_23;
	}

	public void setRemark_24(String remark_24) {
		this.remark_24 = remark_24;
	}

	public void setRemark_25(String remark_25) {
		this.remark_25 = remark_25;
	}

	public void setRemark_26(String remark_26) {
		this.remark_26 = remark_26;
	}

	public void setRemark_27(String remark_27) {
		this.remark_27 = remark_27;
	}

	public void setRemark_28(String remark_28) {
		this.remark_28 = remark_28;
	}

	public void setRemark_29(String remark_29) {
		this.remark_29 = remark_29;
	}

	public void setRemark_30(String remark_30) {
		this.remark_30 = remark_30;
	}

	public void setRemark_31(String remark_31) {
		this.remark_31 = remark_31;
	}

	public void setRemark_32(String remark_32) {
		this.remark_32 = remark_32;
	}

	public void setRemark_33(String remark_33) {
		this.remark_33 = remark_33;
	}

	public void setRemark_34(String remark_34) {
		this.remark_34 = remark_34;
	}

	public void setRemark_35(String remark_35) {
		this.remark_35 = remark_35;
	}

	public void setRemark_36(String remark_36) {
		this.remark_36 = remark_36;
	}

	public void setRemark_37(String remark_37) {
		this.remark_37 = remark_37;
	}

	public void setRemark_38(String remark_38) {
		this.remark_38 = remark_38;
	}

	public void setRemark_39(String remark_39) {
		this.remark_39 = remark_39;
	}

	public void setRemark_40(String remark_40) {
		this.remark_40 = remark_40;
	}

	public void setRemark_41(String remark_41) {
		this.remark_41 = remark_41;
	}

	public void setRemark_42(String remark_42) {
		this.remark_42 = remark_42;
	}

	public void setRemark_43(String remark_43) {
		this.remark_43 = remark_43;
	}

	public void setRemark_44(String remark_44) {
		this.remark_44 = remark_44;
	}

	public void setRemark_45(String remark_45) {
		this.remark_45 = remark_45;
	}

	public void setRemark_46(String remark_46) {
		this.remark_46 = remark_46;
	}

	public void setRemark_47(String remark_47) {
		this.remark_47 = remark_47;
	}

	public double getSubtotal_m_48() {
		return subtotal_m_48;
	}

	public void setSubtotal_m_48(double subtotal_m_48) {
		this.subtotal_m_48 = subtotal_m_48;
	}

	public double getSubtotal_m_49() {
		return subtotal_m_49;
	}

	public void setSubtotal_m_49(double subtotal_m_49) {
		this.subtotal_m_49 = subtotal_m_49;
	}

	public double getSubtotal_m_50() {
		return subtotal_m_50;
	}

	public void setSubtotal_m_50(double subtotal_m_50) {
		this.subtotal_m_50 = subtotal_m_50;
	}

	public double getSubtotal_d_48() {
		return subtotal_d_48;
	}

	public void setSubtotal_d_48(double subtotal_d_48) {
		this.subtotal_d_48 = subtotal_d_48;
	}

	public double getSubtotal_d_49() {
		return subtotal_d_49;
	}

	public void setSubtotal_d_49(double subtotal_d_49) {
		this.subtotal_d_49 = subtotal_d_49;
	}

	public double getSubtotal_d_50() {
		return subtotal_d_50;
	}

	public void setSubtotal_d_50(double subtotal_d_50) {
		this.subtotal_d_50 = subtotal_d_50;
	}

	public double getSubtotal_m_51() {
		return subtotal_m_51;
	}

	public void setSubtotal_m_51(double subtotal_m_51) {
		this.subtotal_m_51 = subtotal_m_51;
	}

	public double getSubtotal_m_52() {
		return subtotal_m_52;
	}

	public void setSubtotal_m_52(double subtotal_m_52) {
		this.subtotal_m_52 = subtotal_m_52;
	}

	public double getSubtotal_m_53() {
		return subtotal_m_53;
	}

	public void setSubtotal_m_53(double subtotal_m_53) {
		this.subtotal_m_53 = subtotal_m_53;
	}

	public double getSubtotal_m_54() {
		return subtotal_m_54;
	}

	public void setSubtotal_m_54(double subtotal_m_54) {
		this.subtotal_m_54 = subtotal_m_54;
	}

	public double getSubtotal_d_51() {
		return subtotal_d_51;
	}

	public void setSubtotal_d_51(double subtotal_d_51) {
		this.subtotal_d_51 = subtotal_d_51;
	}

	public double getSubtotal_d_52() {
		return subtotal_d_52;
	}

	public void setSubtotal_d_52(double subtotal_d_52) {
		this.subtotal_d_52 = subtotal_d_52;
	}

	public double getSubtotal_d_53() {
		return subtotal_d_53;
	}

	public void setSubtotal_d_53(double subtotal_d_53) {
		this.subtotal_d_53 = subtotal_d_53;
	}

	public double getSubtotal_d_54() {
		return subtotal_d_54;
	}

	public void setSubtotal_d_54(double subtotal_d_54) {
		this.subtotal_d_54 = subtotal_d_54;
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

	public double getSubtotal_z_9() {
		return subtotal_z_9;
	}

	public void setSubtotal_z_9(double subtotal_z_9) {
		this.subtotal_z_9 = subtotal_z_9;
	}

	public double getSubtotal_z_10() {
		return subtotal_z_10;
	}

	public void setSubtotal_z_10(double subtotal_z_10) {
		this.subtotal_z_10 = subtotal_z_10;
	}

	public double getSubtotal_z_11() {
		return subtotal_z_11;
	}

	public void setSubtotal_z_11(double subtotal_z_11) {
		this.subtotal_z_11 = subtotal_z_11;
	}

	public double getSubtotal_z_12() {
		return subtotal_z_12;
	}

	public void setSubtotal_z_12(double subtotal_z_12) {
		this.subtotal_z_12 = subtotal_z_12;
	}

	public double getSubtotal_z_13() {
		return subtotal_z_13;
	}

	public void setSubtotal_z_13(double subtotal_z_13) {
		this.subtotal_z_13 = subtotal_z_13;
	}

	public double getSubtotal_z_14() {
		return subtotal_z_14;
	}

	public void setSubtotal_z_14(double subtotal_z_14) {
		this.subtotal_z_14 = subtotal_z_14;
	}

	public double getSubtotal_z_15() {
		return subtotal_z_15;
	}

	public void setSubtotal_z_15(double subtotal_z_15) {
		this.subtotal_z_15 = subtotal_z_15;
	}

	public double getSubtotal_z_16() {
		return subtotal_z_16;
	}

	public void setSubtotal_z_16(double subtotal_z_16) {
		this.subtotal_z_16 = subtotal_z_16;
	}

	public double getSubtotal_z_17() {
		return subtotal_z_17;
	}

	public void setSubtotal_z_17(double subtotal_z_17) {
		this.subtotal_z_17 = subtotal_z_17;
	}

	public double getSubtotal_z_18() {
		return subtotal_z_18;
	}

	public void setSubtotal_z_18(double subtotal_z_18) {
		this.subtotal_z_18 = subtotal_z_18;
	}

	public double getSubtotal_z_19() {
		return subtotal_z_19;
	}

	public void setSubtotal_z_19(double subtotal_z_19) {
		this.subtotal_z_19 = subtotal_z_19;
	}

	public double getSubtotal_z_20() {
		return subtotal_z_20;
	}

	public void setSubtotal_z_20(double subtotal_z_20) {
		this.subtotal_z_20 = subtotal_z_20;
	}

	public double getSubtotal_z_21() {
		return subtotal_z_21;
	}

	public void setSubtotal_z_21(double subtotal_z_21) {
		this.subtotal_z_21 = subtotal_z_21;
	}

	public double getSubtotal_z_22() {
		return subtotal_z_22;
	}

	public void setSubtotal_z_22(double subtotal_z_22) {
		this.subtotal_z_22 = subtotal_z_22;
	}

	public double getSubtotal_z_23() {
		return subtotal_z_23;
	}

	public void setSubtotal_z_23(double subtotal_z_23) {
		this.subtotal_z_23 = subtotal_z_23;
	}

	public double getSubtotal_z_24() {
		return subtotal_z_24;
	}

	public void setSubtotal_z_24(double subtotal_z_24) {
		this.subtotal_z_24 = subtotal_z_24;
	}

	public double getSubtotal_z_25() {
		return subtotal_z_25;
	}

	public void setSubtotal_z_25(double subtotal_z_25) {
		this.subtotal_z_25 = subtotal_z_25;
	}

	public double getSubtotal_z_26() {
		return subtotal_z_26;
	}

	public void setSubtotal_z_26(double subtotal_z_26) {
		this.subtotal_z_26 = subtotal_z_26;
	}

	public double getSubtotal_z_27() {
		return subtotal_z_27;
	}

	public void setSubtotal_z_27(double subtotal_z_27) {
		this.subtotal_z_27 = subtotal_z_27;
	}

	public double getSubtotal_z_28() {
		return subtotal_z_28;
	}

	public void setSubtotal_z_28(double subtotal_z_28) {
		this.subtotal_z_28 = subtotal_z_28;
	}

	public double getSubtotal_z_29() {
		return subtotal_z_29;
	}

	public void setSubtotal_z_29(double subtotal_z_29) {
		this.subtotal_z_29 = subtotal_z_29;
	}

	public double getSubtotal_z_30() {
		return subtotal_z_30;
	}

	public void setSubtotal_z_30(double subtotal_z_30) {
		this.subtotal_z_30 = subtotal_z_30;
	}

	public double getSubtotal_z_31() {
		return subtotal_z_31;
	}

	public void setSubtotal_z_31(double subtotal_z_31) {
		this.subtotal_z_31 = subtotal_z_31;
	}

	public double getSubtotal_z_32() {
		return subtotal_z_32;
	}

	public void setSubtotal_z_32(double subtotal_z_32) {
		this.subtotal_z_32 = subtotal_z_32;
	}

	public double getSubtotal_z_33() {
		return subtotal_z_33;
	}

	public void setSubtotal_z_33(double subtotal_z_33) {
		this.subtotal_z_33 = subtotal_z_33;
	}

	public double getSubtotal_z_34() {
		return subtotal_z_34;
	}

	public void setSubtotal_z_34(double subtotal_z_34) {
		this.subtotal_z_34 = subtotal_z_34;
	}

	public double getSubtotal_z_35() {
		return subtotal_z_35;
	}

	public void setSubtotal_z_35(double subtotal_z_35) {
		this.subtotal_z_35 = subtotal_z_35;
	}

	public double getSubtotal_z_36() {
		return subtotal_z_36;
	}

	public void setSubtotal_z_36(double subtotal_z_36) {
		this.subtotal_z_36 = subtotal_z_36;
	}

	public double getSubtotal_z_37() {
		return subtotal_z_37;
	}

	public void setSubtotal_z_37(double subtotal_z_37) {
		this.subtotal_z_37 = subtotal_z_37;
	}

	public double getSubtotal_z_38() {
		return subtotal_z_38;
	}

	public void setSubtotal_z_38(double subtotal_z_38) {
		this.subtotal_z_38 = subtotal_z_38;
	}

	public double getSubtotal_z_39() {
		return subtotal_z_39;
	}

	public void setSubtotal_z_39(double subtotal_z_39) {
		this.subtotal_z_39 = subtotal_z_39;
	}

	public double getSubtotal_z_40() {
		return subtotal_z_40;
	}

	public void setSubtotal_z_40(double subtotal_z_40) {
		this.subtotal_z_40 = subtotal_z_40;
	}

	public double getSubtotal_z_41() {
		return subtotal_z_41;
	}

	public void setSubtotal_z_41(double subtotal_z_41) {
		this.subtotal_z_41 = subtotal_z_41;
	}

	public double getSubtotal_z_42() {
		return subtotal_z_42;
	}

	public void setSubtotal_z_42(double subtotal_z_42) {
		this.subtotal_z_42 = subtotal_z_42;
	}

	public double getSubtotal_z_43() {
		return subtotal_z_43;
	}

	public void setSubtotal_z_43(double subtotal_z_43) {
		this.subtotal_z_43 = subtotal_z_43;
	}

	public double getSubtotal_z_44() {
		return subtotal_z_44;
	}

	public void setSubtotal_z_44(double subtotal_z_44) {
		this.subtotal_z_44 = subtotal_z_44;
	}

	public double getSubtotal_z_45() {
		return subtotal_z_45;
	}

	public void setSubtotal_z_45(double subtotal_z_45) {
		this.subtotal_z_45 = subtotal_z_45;
	}

	public double getSubtotal_z_46() {
		return subtotal_z_46;
	}

	public void setSubtotal_z_46(double subtotal_z_46) {
		this.subtotal_z_46 = subtotal_z_46;
	}

	public double getSubtotal_z_47() {
		return subtotal_z_47;
	}

	public void setSubtotal_z_47(double subtotal_z_47) {
		this.subtotal_z_47 = subtotal_z_47;
	}

	public double getSubtotal_z_48() {
		return subtotal_z_48;
	}

	public void setSubtotal_z_48(double subtotal_z_48) {
		this.subtotal_z_48 = subtotal_z_48;
	}

	public double getSubtotal_z_49() {
		return subtotal_z_49;
	}

	public void setSubtotal_z_49(double subtotal_z_49) {
		this.subtotal_z_49 = subtotal_z_49;
	}

	public double getSubtotal_z_50() {
		return subtotal_z_50;
	}

	public void setSubtotal_z_50(double subtotal_z_50) {
		this.subtotal_z_50 = subtotal_z_50;
	}

	public double getSubtotal_z_51() {
		return subtotal_z_51;
	}

	public void setSubtotal_z_51(double subtotal_z_51) {
		this.subtotal_z_51 = subtotal_z_51;
	}

	public double getSubtotal_z_52() {
		return subtotal_z_52;
	}

	public void setSubtotal_z_52(double subtotal_z_52) {
		this.subtotal_z_52 = subtotal_z_52;
	}

	public double getSubtotal_z_53() {
		return subtotal_z_53;
	}

	public void setSubtotal_z_53(double subtotal_z_53) {
		this.subtotal_z_53 = subtotal_z_53;
	}

	public double getSubtotal_z_54() {
		return subtotal_z_54;
	}

	public void setSubtotal_z_54(double subtotal_z_54) {
		this.subtotal_z_54 = subtotal_z_54;
	}

	public String getRemark_51() {
		return remark_51;
	}

	public void setRemark_51(String remark_51) {
		this.remark_51 = remark_51;
	}

	public String getRemark_52() {
		return remark_52;
	}

	public void setRemark_52(String remark_52) {
		this.remark_52 = remark_52;
	}

	public String getRemark_53() {
		return remark_53;
	}

	public void setRemark_53(String remark_53) {
		this.remark_53 = remark_53;
	}

	public String getRemark_54() {
		return remark_54;
	}

	public void setRemark_54(String remark_54) {
		this.remark_54 = remark_54;
	}

	public String getRemark_48() {
		return remark_48;
	}

	public void setRemark_48(String remark_48) {
		this.remark_48 = remark_48;
	}

	public String getRemark_49() {
		return remark_49;
	}

	public void setRemark_49(String remark_49) {
		this.remark_49 = remark_49;
	}

	public String getRemark_50() {
		return remark_50;
	}

	public void setRemark_50(String remark_50) {
		this.remark_50 = remark_50;
	}

	public String getSub_project() {
		return sub_project;
	}

	public void setSub_project(String sub_project) {
		this.sub_project = sub_project;
	}

	public String getAdd_project() {
		return add_project;
	}

	public void setAdd_project(String add_project) {
		this.add_project = add_project;
	}

	public double getSubtotal_m_55() {
		return subtotal_m_55;
	}

	public void setSubtotal_m_55(double subtotal_m_55) {
		this.subtotal_m_55 = subtotal_m_55;
	}

	public double getSubtotal_m_56() {
		return subtotal_m_56;
	}

	public void setSubtotal_m_56(double subtotal_m_56) {
		this.subtotal_m_56 = subtotal_m_56;
	}

	public double getSubtotal_d_55() {
		return subtotal_d_55;
	}

	public void setSubtotal_d_55(double subtotal_d_55) {
		this.subtotal_d_55 = subtotal_d_55;
	}

	public double getSubtotal_d_56() {
		return subtotal_d_56;
	}

	public void setSubtotal_d_56(double subtotal_d_56) {
		this.subtotal_d_56 = subtotal_d_56;
	}

	public double getSubtotal_z_55() {
		return subtotal_z_55;
	}

	public void setSubtotal_z_55(double subtotal_z_55) {
		this.subtotal_z_55 = subtotal_z_55;
	}

	public double getSubtotal_z_56() {
		return subtotal_z_56;
	}

	public void setSubtotal_z_56(double subtotal_z_56) {
		this.subtotal_z_56 = subtotal_z_56;
	}

	public String getRemark_55() {
		return remark_55;
	}

	public void setRemark_55(String remark_55) {
		this.remark_55 = remark_55;
	}

	public String getRemark_56() {
		return remark_56;
	}

	public void setRemark_56(String remark_56) {
		this.remark_56 = remark_56;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
