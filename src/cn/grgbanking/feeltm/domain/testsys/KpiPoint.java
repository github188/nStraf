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
@Table(name ="kpipoint")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class KpiPoint implements Serializable {
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
	private String remark; //状态
	
	@Column(name = "user_id")
	private String user_id;//用户名
	@Column(name = "modify_date")
	private String modify_date; //更新时间
	@Column(name = "group_name")
	private String group_name;  //组名


	@Column(name = "edit_lock")
	private double edit_lock;  //经理是否修改过
	@Column(name = "firstedit")
	private String firstedit;  //经理是否修改过
	@Column(name = "update_man")
	private String  update_man;//最近更新者

	@Column(name = "subtotal_s")
	private double  subtotal_s;//总分	

	
	@Column(name = "subtotal_1")
	private double  subtotal_1;//总分
	@Column(name = "subtotal_2")
	private double  subtotal_2;//总分
	@Column(name = "subtotal_3")
	private double  subtotal_3;//总分
	@Column(name = "subtotal_4")
	private double  subtotal_4;//总分
	@Column(name = "subtotal_5")
	private double  subtotal_5;//总分
	@Column(name = "subtotal_6")
	private double  subtotal_6;//总分
	@Column(name = "subtotal_7")
	private double  subtotal_7;//总分
	@Column(name = "subtotal_8")
	private double  subtotal_8;//总分
	@Column(name = "subtotal_9")
	private double  subtotal_9;//总分
	@Column(name = "subtotal_10")
	private double  subtotal_10;//总分
	@Column(name = "subtotal_11")
	private double  subtotal_11;//总分
	@Column(name = "subtotal_12")
	private double  subtotal_12;//总分
	@Column(name = "subtotal_13")
	private double  subtotal_13;//总分
	@Column(name = "subtotal_14")
	private double  subtotal_14;//总分
	@Column(name = "subtotal_15")
	private double  subtotal_15;//总分
	@Column(name = "subtotal_16")
	private double  subtotal_16;//总分
	@Column(name = "subtotal_17")
	private double  subtotal_17;//总分
	@Column(name = "subtotal_18")
	private double  subtotal_18;//总分
	@Column(name = "subtotal_19")
	private double  subtotal_19;//总分
	@Column(name = "subtotal_20")
	private double  subtotal_20;//总分
	@Column(name = "subtotal_21")
	private double  subtotal_21;//总分
	@Column(name = "subtotal_22")
	private double  subtotal_22;//总分
	@Column(name = "subtotal_23")
	private double  subtotal_23;//总分
	@Column(name = "subtotal_24")
	private double  subtotal_24;//总分
	@Column(name = "subtotal_25")
	private double  subtotal_25;//总分
	@Column(name = "subtotal_26")
	private double  subtotal_26;//总分
	@Column(name = "subtotal_27")
	private double  subtotal_27;//总分
	@Column(name = "subtotal_28")
	private double  subtotal_28;//总分
	@Column(name = "subtotal_29")
	private double  subtotal_29;//总分
	@Column(name = "subtotal_30")
	private double  subtotal_30;//总分
	@Column(name = "subtotal_31")
	private double  subtotal_31;//总分
	@Column(name = "subtotal_32")
	private double  subtotal_32;//总分
	@Column(name = "subtotal_33")
	private double  subtotal_33;//总分
	@Column(name = "subtotal_34")
	private double  subtotal_34;//总分
	@Column(name = "subtotal_35")
	private double  subtotal_35;//总分	
	
	@Column(name = "number_1")
	private double  number_1;//总分
	@Column(name = "number_2")
	private double  number_2;//总分
	@Column(name = "number_3")
	private double  number_3;//总分
	@Column(name = "number_4")
	private double  number_4;//总分
	@Column(name = "number_5")
	private double  number_5;//总分
	@Column(name = "number_6")
	private double  number_6;//总分
	@Column(name = "number_7")
	private double  number_7;//总分
	@Column(name = "number_8")
	private double  number_8;//总分
	@Column(name = "number_9")
	private double  number_9;//总分
	@Column(name = "number_10")
	private double  number_10;//总分
	@Column(name = "number_11")
	private double  number_11;//总分
	@Column(name = "number_12")
	private double  number_12;//总分
	@Column(name = "number_13")
	private double  number_13;//总分
	@Column(name = "number_14")
	private double  number_14;//总分
	@Column(name = "number_15")
	private double  number_15;//总分
	@Column(name = "number_16")
	private double  number_16;//总分
	@Column(name = "number_17")
	private double  number_17;//总分
	@Column(name = "number_18")
	private double  number_18;//总分
	@Column(name = "number_19")
	private double  number_19;//总分
	@Column(name = "number_20")
	private double  number_20;//总分
	@Column(name = "number_21")
	private double  number_21;//总分
	@Column(name = "number_22")
	private double  number_22;//总分
	@Column(name = "number_23")
	private double  number_23;//总分
	@Column(name = "number_24")
	private double  number_24;//总分
	@Column(name = "number_25")
	private double  number_25;//总分
	@Column(name = "number_26")
	private double  number_26;//总分
	@Column(name = "number_27")
	private double  number_27;//总分
	@Column(name = "number_28")
	private double  number_28;//总分
	@Column(name = "number_29")
	private double  number_29;//总分
	@Column(name = "number_30")
	private double  number_30;//总分
	@Column(name = "number_31")
	private double  number_31;//总分
	@Column(name = "number_32")
	private double  number_32;//总分
	@Column(name = "number_33")
	private double  number_33;//总分
	@Column(name = "number_34")
	private double  number_34;//总分
	@Column(name = "number_35")
	private double  number_35;//总分
	
	@Column(name = "coeff_1")
	private double  coeff_1;//总分
	@Column(name = "coeff_2")
	private double  coeff_2;//总分
	@Column(name = "coeff_3")
	private double  coeff_3;//总分
	@Column(name = "coeff_4")
	private double  coeff_4;//总分
	@Column(name = "coeff_5")
	private double  coeff_5;//总分
	@Column(name = "coeff_6")
	private double  coeff_6;//总分
	@Column(name = "coeff_7")
	private double  coeff_7;//总分
	@Column(name = "coeff_8")
	private double  coeff_8;//总分
	@Column(name = "coeff_9")
	private double  coeff_9;//总分
	@Column(name = "coeff_10")
	private double  coeff_10;//总分
	@Column(name = "coeff_11")
	private double  coeff_11;//总分
	@Column(name = "coeff_12")
	private String  coeff_12;//总分
	@Column(name = "coeff_13")
	private double  coeff_13;//总分
	@Column(name = "coeff_14")
	private double  coeff_14;//总分
	@Column(name = "coeff_15")
	private double  coeff_15;//总分
	@Column(name = "coeff_16")
	private double  coeff_16;//总分
	@Column(name = "coeff_17")
	private double  coeff_17;//总分
	@Column(name = "coeff_18")
	private double  coeff_18;//总分
	@Column(name = "coeff_19")
	private String  coeff_19;//总分
	@Column(name = "coeff_20")
	private double  coeff_20;//总分
	@Column(name = "coeff_21")
	private double  coeff_21;//总分
	@Column(name = "coeff_22")
	private double  coeff_22;//总分
	@Column(name = "coeff_23")
	private double  coeff_23;//总分
	@Column(name = "coeff_24")
	private double  coeff_24;//总分
	@Column(name = "coeff_25")
	private double  coeff_25;//总分
	@Column(name = "coeff_26")
	private double  coeff_26;//总分
	@Column(name = "coeff_27")
	private double  coeff_27;//总分
	@Column(name = "coeff_28")
	private double  coeff_28;//总分
	@Column(name = "coeff_29")
	private double  coeff_29;//总分
	@Column(name = "coeff_30")
	private double  coeff_30;//总分
	@Column(name = "coeff_31")
	private double  coeff_31;//总分
	@Column(name = "coeff_32")
	private double  coeff_32;//总分
	@Column(name = "coeff_33")
	private double  coeff_33;//总分
	@Column(name = "coeff_34")
	private double  coeff_34;//总分
	@Column(name = "coeff_35")
	private double  coeff_35;//总分
	
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
	
	@Column(name = "pno",updatable=false)
	private String  pno;//总分
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
	public KpiPoint(){
		
	}

	public KpiPoint(String id) {
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
	
	public void setSubtotal_s(double subtotal_s) {
		this.subtotal_s = subtotal_s;
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

	public double getsubtotal_1() {
		return subtotal_1;
	}

	public void setsubtotal_1(double subtotal_1) {
		this.subtotal_1 = subtotal_1;
	}

	public double getsubtotal_2() {
		return subtotal_2;
	}

	public void setsubtotal_2(double subtotal_2) {
		this.subtotal_2 = subtotal_2;
	}

	public double getsubtotal_3() {
		return subtotal_3;
	}

	public void setsubtotal_3(double subtotal_3) {
		this.subtotal_3 = subtotal_3;
	}

	public double getsubtotal_4() {
		return subtotal_4;
	}

	public void setsubtotal_4(double subtotal_4) {
		this.subtotal_4 = subtotal_4;
	}

	public double getsubtotal_5() {
		return subtotal_5;
	}

	public void setsubtotal_5(double subtotal_5) {
		this.subtotal_5 = subtotal_5;
	}

	public double getsubtotal_6() {
		return subtotal_6;
	}

	public void setsubtotal_6(double subtotal_6) {
		this.subtotal_6 = subtotal_6;
	}

	public double getsubtotal_7() {
		return subtotal_7;
	}

	public void setsubtotal_7(double subtotal_7) {
		this.subtotal_7 = subtotal_7;
	}

	public double getsubtotal_8() {
		return subtotal_8;
	}

	public void setsubtotal_8(double subtotal_8) {
		this.subtotal_8 = subtotal_8;
	}

	public double getsubtotal_9() {
		return subtotal_9;
	}

	public void setsubtotal_9(double subtotal_9) {
		this.subtotal_9 = subtotal_9;
	}

	public double getsubtotal_10() {
		return subtotal_10;
	}

	public void setsubtotal_10(double subtotal_10) {
		this.subtotal_10 = subtotal_10;
	}

	public double getsubtotal_11() {
		return subtotal_11;
	}

	public void setsubtotal_11(double subtotal_11) {
		this.subtotal_11 = subtotal_11;
	}

	public double getsubtotal_12() {
		return subtotal_12;
	}

	public void setsubtotal_12(double subtotal_12) {
		this.subtotal_12 = subtotal_12;
	}

	public double getsubtotal_13() {
		return subtotal_13;
	}

	public void setsubtotal_13(double subtotal_13) {
		this.subtotal_13 = subtotal_13;
	}

	public double getsubtotal_14() {
		return subtotal_14;
	}

	public void setsubtotal_14(double subtotal_14) {
		this.subtotal_14 = subtotal_14;
	}

	public double getsubtotal_15() {
		return subtotal_15;
	}

	public void setsubtotal_15(double subtotal_15) {
		this.subtotal_15 = subtotal_15;
	}

	public double getsubtotal_16() {
		return subtotal_16;
	}

	public void setsubtotal_16(double subtotal_16) {
		this.subtotal_16 = subtotal_16;
	}

	public double getsubtotal_17() {
		return subtotal_17;
	}

	public void setsubtotal_17(double subtotal_17) {
		this.subtotal_17 = subtotal_17;
	}

	public double getsubtotal_18() {
		return subtotal_18;
	}

	public void setsubtotal_18(double subtotal_18) {
		this.subtotal_18 = subtotal_18;
	}

	public double getsubtotal_19() {
		return subtotal_19;
	}

	public void setsubtotal_19(double subtotal_19) {
		this.subtotal_19 = subtotal_19;
	}

	public double getsubtotal_20() {
		return subtotal_20;
	}

	public void setsubtotal_20(double subtotal_20) {
		this.subtotal_20 = subtotal_20;
	}

	public double getsubtotal_21() {
		return subtotal_21;
	}

	public void setsubtotal_21(double subtotal_21) {
		this.subtotal_21 = subtotal_21;
	}

	public double getsubtotal_22() {
		return subtotal_22;
	}

	public void setsubtotal_22(double subtotal_22) {
		this.subtotal_22 = subtotal_22;
	}

	public double getsubtotal_23() {
		return subtotal_23;
	}

	public void setsubtotal_23(double subtotal_23) {
		this.subtotal_23 = subtotal_23;
	}

	public double getsubtotal_24() {
		return subtotal_24;
	}

	public void setsubtotal_24(double subtotal_24) {
		this.subtotal_24 = subtotal_24;
	}

	public double getsubtotal_25() {
		return subtotal_25;
	}

	public void setsubtotal_25(double subtotal_25) {
		this.subtotal_25 = subtotal_25;
	}

	public double getsubtotal_26() {
		return subtotal_26;
	}

	public void setsubtotal_26(double subtotal_26) {
		this.subtotal_26 = subtotal_26;
	}

	public double getsubtotal_27() {
		return subtotal_27;
	}

	public void setsubtotal_27(double subtotal_27) {
		this.subtotal_27 = subtotal_27;
	}

	public double getsubtotal_28() {
		return subtotal_28;
	}

	public void setsubtotal_28(double subtotal_28) {
		this.subtotal_28 = subtotal_28;
	}

	public double getsubtotal_29() {
		return subtotal_29;
	}

	public void setsubtotal_29(double subtotal_29) {
		this.subtotal_29 = subtotal_29;
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

	public double getNumber_1() {
		return number_1;
	}

	public void setNumber_1(double number_1) {
		this.number_1 = number_1;
	}

	public double getNumber_2() {
		return number_2;
	}

	public void setNumber_2(double number_2) {
		this.number_2 = number_2;
	}

	public double getNumber_3() {
		return number_3;
	}

	public void setNumber_3(double number_3) {
		this.number_3 = number_3;
	}

	public double getNumber_4() {
		return number_4;
	}

	public void setNumber_4(double number_4) {
		this.number_4 = number_4;
	}

	public double getNumber_5() {
		return number_5;
	}

	public void setNumber_5(double number_5) {
		this.number_5 = number_5;
	}

	public double getNumber_6() {
		return number_6;
	}

	public void setNumber_6(double number_6) {
		this.number_6 = number_6;
	}

	public double getNumber_7() {
		return number_7;
	}

	public void setNumber_7(double number_7) {
		this.number_7 = number_7;
	}

	public double getNumber_8() {
		return number_8;
	}

	public void setNumber_8(double number_8) {
		this.number_8 = number_8;
	}

	public double getNumber_9() {
		return number_9;
	}

	public void setNumber_9(double number_9) {
		this.number_9 = number_9;
	}

	public double getNumber_10() {
		return number_10;
	}

	public void setNumber_10(double number_10) {
		this.number_10 = number_10;
	}

	public double getNumber_11() {
		return number_11;
	}

	public void setNumber_11(double number_11) {
		this.number_11 = number_11;
	}

	public double getNumber_12() {
		return number_12;
	}

	public void setNumber_12(double number_12) {
		this.number_12 = number_12;
	}

	public double getNumber_13() {
		return number_13;
	}

	public void setNumber_13(double number_13) {
		this.number_13 = number_13;
	}

	public double getNumber_14() {
		return number_14;
	}

	public void setNumber_14(double number_14) {
		this.number_14 = number_14;
	}

	public double getNumber_15() {
		return number_15;
	}

	public void setNumber_15(double number_15) {
		this.number_15 = number_15;
	}

	public double getNumber_16() {
		return number_16;
	}

	public void setNumber_16(double number_16) {
		this.number_16 = number_16;
	}

	public double getNumber_17() {
		return number_17;
	}

	public void setNumber_17(double number_17) {
		this.number_17 = number_17;
	}

	public double getNumber_18() {
		return number_18;
	}

	public void setNumber_18(double number_18) {
		this.number_18 = number_18;
	}

	public double getNumber_19() {
		return number_19;
	}

	public void setNumber_19(double number_19) {
		this.number_19 = number_19;
	}

	public double getNumber_20() {
		return number_20;
	}

	public void setNumber_20(double number_20) {
		this.number_20 = number_20;
	}

	public double getNumber_21() {
		return number_21;
	}

	public void setNumber_21(double number_21) {
		this.number_21 = number_21;
	}

	public double getNumber_22() {
		return number_22;
	}

	public void setNumber_22(double number_22) {
		this.number_22 = number_22;
	}

	public double getNumber_23() {
		return number_23;
	}

	public void setNumber_23(double number_23) {
		this.number_23 = number_23;
	}

	public double getNumber_24() {
		return number_24;
	}

	public void setNumber_24(double number_24) {
		this.number_24 = number_24;
	}

	public double getNumber_25() {
		return number_25;
	}

	public void setNumber_25(double number_25) {
		this.number_25 = number_25;
	}

	public double getNumber_26() {
		return number_26;
	}

	public void setNumber_26(double number_26) {
		this.number_26 = number_26;
	}

	public double getNumber_27() {
		return number_27;
	}

	public void setNumber_27(double number_27) {
		this.number_27 = number_27;
	}

	public double getNumber_28() {
		return number_28;
	}

	public void setNumber_28(double number_28) {
		this.number_28 = number_28;
	}

	public double getNumber_29() {
		return number_29;
	}

	public void setNumber_29(double number_29) {
		this.number_29 = number_29;
	}

	public String getFirstedit() {
		return firstedit;
	}

	public void setFirstedit(String firstedit) {
		this.firstedit = firstedit;
	}

	public double getSubtotal_1() {
		return subtotal_1;
	}

	public void setSubtotal_1(double subtotal_1) {
		this.subtotal_1 = subtotal_1;
	}

	public double getSubtotal_2() {
		return subtotal_2;
	}

	public void setSubtotal_2(double subtotal_2) {
		this.subtotal_2 = subtotal_2;
	}

	public double getSubtotal_3() {
		return subtotal_3;
	}

	public void setSubtotal_3(double subtotal_3) {
		this.subtotal_3 = subtotal_3;
	}

	public double getSubtotal_4() {
		return subtotal_4;
	}

	public void setSubtotal_4(double subtotal_4) {
		this.subtotal_4 = subtotal_4;
	}

	public double getSubtotal_5() {
		return subtotal_5;
	}

	public void setSubtotal_5(double subtotal_5) {
		this.subtotal_5 = subtotal_5;
	}

	public double getSubtotal_6() {
		return subtotal_6;
	}

	public void setSubtotal_6(double subtotal_6) {
		this.subtotal_6 = subtotal_6;
	}

	public double getSubtotal_7() {
		return subtotal_7;
	}

	public void setSubtotal_7(double subtotal_7) {
		this.subtotal_7 = subtotal_7;
	}

	public double getSubtotal_8() {
		return subtotal_8;
	}

	public void setSubtotal_8(double subtotal_8) {
		this.subtotal_8 = subtotal_8;
	}

	public double getSubtotal_9() {
		return subtotal_9;
	}

	public void setSubtotal_9(double subtotal_9) {
		this.subtotal_9 = subtotal_9;
	}

	public double getSubtotal_10() {
		return subtotal_10;
	}

	public void setSubtotal_10(double subtotal_10) {
		this.subtotal_10 = subtotal_10;
	}

	public double getSubtotal_11() {
		return subtotal_11;
	}

	public void setSubtotal_11(double subtotal_11) {
		this.subtotal_11 = subtotal_11;
	}

	public double getSubtotal_12() {
		return subtotal_12;
	}

	public void setSubtotal_12(double subtotal_12) {
		this.subtotal_12 = subtotal_12;
	}

	public double getSubtotal_13() {
		return subtotal_13;
	}

	public void setSubtotal_13(double subtotal_13) {
		this.subtotal_13 = subtotal_13;
	}

	public double getSubtotal_14() {
		return subtotal_14;
	}

	public void setSubtotal_14(double subtotal_14) {
		this.subtotal_14 = subtotal_14;
	}

	public double getSubtotal_15() {
		return subtotal_15;
	}

	public void setSubtotal_15(double subtotal_15) {
		this.subtotal_15 = subtotal_15;
	}

	public double getSubtotal_16() {
		return subtotal_16;
	}

	public void setSubtotal_16(double subtotal_16) {
		this.subtotal_16 = subtotal_16;
	}

	public double getSubtotal_17() {
		return subtotal_17;
	}

	public void setSubtotal_17(double subtotal_17) {
		this.subtotal_17 = subtotal_17;
	}

	public double getSubtotal_18() {
		return subtotal_18;
	}

	public void setSubtotal_18(double subtotal_18) {
		this.subtotal_18 = subtotal_18;
	}

	public double getSubtotal_19() {
		return subtotal_19;
	}

	public void setSubtotal_19(double subtotal_19) {
		this.subtotal_19 = subtotal_19;
	}

	public double getSubtotal_20() {
		return subtotal_20;
	}

	public void setSubtotal_20(double subtotal_20) {
		this.subtotal_20 = subtotal_20;
	}

	public double getSubtotal_21() {
		return subtotal_21;
	}

	public void setSubtotal_21(double subtotal_21) {
		this.subtotal_21 = subtotal_21;
	}

	public double getSubtotal_22() {
		return subtotal_22;
	}

	public void setSubtotal_22(double subtotal_22) {
		this.subtotal_22 = subtotal_22;
	}

	public double getSubtotal_23() {
		return subtotal_23;
	}

	public void setSubtotal_23(double subtotal_23) {
		this.subtotal_23 = subtotal_23;
	}

	public double getSubtotal_24() {
		return subtotal_24;
	}

	public void setSubtotal_24(double subtotal_24) {
		this.subtotal_24 = subtotal_24;
	}

	public double getSubtotal_25() {
		return subtotal_25;
	}

	public void setSubtotal_25(double subtotal_25) {
		this.subtotal_25 = subtotal_25;
	}

	public double getSubtotal_26() {
		return subtotal_26;
	}

	public void setSubtotal_26(double subtotal_26) {
		this.subtotal_26 = subtotal_26;
	}

	public double getSubtotal_27() {
		return subtotal_27;
	}

	public void setSubtotal_27(double subtotal_27) {
		this.subtotal_27 = subtotal_27;
	}

	public double getSubtotal_28() {
		return subtotal_28;
	}

	public void setSubtotal_28(double subtotal_28) {
		this.subtotal_28 = subtotal_28;
	}

	public double getSubtotal_29() {
		return subtotal_29;
	}

	public void setSubtotal_29(double subtotal_29) {
		this.subtotal_29 = subtotal_29;
	}

	public double getCoeff_1() {
		return coeff_1;
	}

	public void setCoeff_1(double coeff_1) {
		this.coeff_1 = coeff_1;
	}

	public double getCoeff_2() {
		return coeff_2;
	}

	public void setCoeff_2(double coeff_2) {
		this.coeff_2 = coeff_2;
	}

	public double getCoeff_3() {
		return coeff_3;
	}

	public void setCoeff_3(double coeff_3) {
		this.coeff_3 = coeff_3;
	}

	public double getCoeff_4() {
		return coeff_4;
	}

	public void setCoeff_4(double coeff_4) {
		this.coeff_4 = coeff_4;
	}

	public double getCoeff_5() {
		return coeff_5;
	}

	public void setCoeff_5(double coeff_5) {
		this.coeff_5 = coeff_5;
	}

	public double getCoeff_6() {
		return coeff_6;
	}

	public void setCoeff_6(double coeff_6) {
		this.coeff_6 = coeff_6;
	}

	public double getCoeff_7() {
		return coeff_7;
	}

	public void setCoeff_7(double coeff_7) {
		this.coeff_7 = coeff_7;
	}

	public double getCoeff_8() {
		return coeff_8;
	}

	public void setCoeff_8(double coeff_8) {
		this.coeff_8 = coeff_8;
	}

	public double getCoeff_9() {
		return coeff_9;
	}

	public void setCoeff_9(double coeff_9) {
		this.coeff_9 = coeff_9;
	}

	public double getCoeff_10() {
		return coeff_10;
	}

	public void setCoeff_10(double coeff_10) {
		this.coeff_10 = coeff_10;
	}

	public double getCoeff_11() {
		return coeff_11;
	}

	public void setCoeff_11(double coeff_11) {
		this.coeff_11 = coeff_11;
	}

	public String getCoeff_12() {
		return coeff_12;
	}

	public void setCoeff_12(String coeff_12) {
		this.coeff_12 = coeff_12;
	}

	public double getCoeff_13() {
		return coeff_13;
	}

	public void setCoeff_13(double coeff_13) {
		this.coeff_13 = coeff_13;
	}

	public double getCoeff_14() {
		return coeff_14;
	}

	public void setCoeff_14(double coeff_14) {
		this.coeff_14 = coeff_14;
	}

	public double getCoeff_15() {
		return coeff_15;
	}

	public void setCoeff_15(double coeff_15) {
		this.coeff_15 = coeff_15;
	}

	public double getCoeff_16() {
		return coeff_16;
	}

	public void setCoeff_16(double coeff_16) {
		this.coeff_16 = coeff_16;
	}

	public double getCoeff_17() {
		return coeff_17;
	}

	public void setCoeff_17(double coeff_17) {
		this.coeff_17 = coeff_17;
	}

	public double getCoeff_18() {
		return coeff_18;
	}

	public void setCoeff_18(double coeff_18) {
		this.coeff_18 = coeff_18;
	}

	public String getCoeff_19() {
		return coeff_19;
	}

	public void setCoeff_19(String coeff_19) {
		this.coeff_19 = coeff_19;
	}

	public double getCoeff_20() {
		return coeff_20;
	}

	public void setCoeff_20(double coeff_20) {
		this.coeff_20 = coeff_20;
	}

	public double getCoeff_21() {
		return coeff_21;
	}

	public void setCoeff_21(double coeff_21) {
		this.coeff_21 = coeff_21;
	}

	public double getCoeff_22() {
		return coeff_22;
	}

	public void setCoeff_22(double coeff_22) {
		this.coeff_22 = coeff_22;
	}

	public double getCoeff_23() {
		return coeff_23;
	}

	public void setCoeff_23(double coeff_23) {
		this.coeff_23 = coeff_23;
	}

	public double getCoeff_24() {
		return coeff_24;
	}

	public void setCoeff_24(double coeff_24) {
		this.coeff_24 = coeff_24;
	}

	public double getCoeff_25() {
		return coeff_25;
	}

	public void setCoeff_25(double coeff_25) {
		this.coeff_25 = coeff_25;
	}

	public double getCoeff_26() {
		return coeff_26;
	}

	public void setCoeff_26(double coeff_26) {
		this.coeff_26 = coeff_26;
	}

	public double getCoeff_27() {
		return coeff_27;
	}

	public void setCoeff_27(double coeff_27) {
		this.coeff_27 = coeff_27;
	}

	public double getCoeff_28() {
		return coeff_28;
	}

	public void setCoeff_28(double coeff_28) {
		this.coeff_28 = coeff_28;
	}

	public double getCoeff_29() {
		return coeff_29;
	}

	public void setCoeff_29(double coeff_29) {
		this.coeff_29 = coeff_29;
	}

	public double getCoeff_30() {
		return coeff_30;
	}

	public void setCoeff_30(double coeff_30) {
		this.coeff_30 = coeff_30;
	}

	public double getSubtotal_30() {
		return subtotal_30;
	}

	public void setSubtotal_30(double subtotal_30) {
		this.subtotal_30 = subtotal_30;
	}

	public double getNumber_30() {
		return number_30;
	}

	public void setNumber_30(double number_30) {
		this.number_30 = number_30;
	}

	public String getRemark_30() {
		return remark_30;
	}

	public void setRemark_30(String remark_30) {
		this.remark_30 = remark_30;
	}

	public double getSubtotal_31() {
		return subtotal_31;
	}

	public void setSubtotal_31(double subtotal_31) {
		this.subtotal_31 = subtotal_31;
	}

	public double getSubtotal_32() {
		return subtotal_32;
	}

	public void setSubtotal_32(double subtotal_32) {
		this.subtotal_32 = subtotal_32;
	}

	public double getNumber_31() {
		return number_31;
	}

	public void setNumber_31(double number_31) {
		this.number_31 = number_31;
	}

	public double getNumber_32() {
		return number_32;
	}

	public void setNumber_32(double number_32) {
		this.number_32 = number_32;
	}

	public double getCoeff_31() {
		return coeff_31;
	}

	public void setCoeff_31(double coeff_31) {
		this.coeff_31 = coeff_31;
	}

	public double getCoeff_32() {
		return coeff_32;
	}

	public void setCoeff_32(double coeff_32) {
		this.coeff_32 = coeff_32;
	}

	public String getRemark_31() {
		return remark_31;
	}

	public void setRemark_31(String remark_31) {
		this.remark_31 = remark_31;
	}

	public String getRemark_32() {
		return remark_32;
	}

	public void setRemark_32(String remark_32) {
		this.remark_32 = remark_32;
	}

	public double getSubtotal_33() {
		return subtotal_33;
	}

	public void setSubtotal_33(double subtotal_33) {
		this.subtotal_33 = subtotal_33;
	}

	public double getSubtotal_34() {
		return subtotal_34;
	}

	public void setSubtotal_34(double subtotal_34) {
		this.subtotal_34 = subtotal_34;
	}

	public double getNumber_33() {
		return number_33;
	}

	public void setNumber_33(double number_33) {
		this.number_33 = number_33;
	}

	public double getNumber_34() {
		return number_34;
	}

	public void setNumber_34(double number_34) {
		this.number_34 = number_34;
	}

	public double getCoeff_33() {
		return coeff_33;
	}

	public void setCoeff_33(double coeff_33) {
		this.coeff_33 = coeff_33;
	}

	public double getCoeff_34() {
		return coeff_34;
	}

	public void setCoeff_34(double coeff_34) {
		this.coeff_34 = coeff_34;
	}

	public String getRemark_33() {
		return remark_33;
	}

	public void setRemark_33(String remark_33) {
		this.remark_33 = remark_33;
	}

	public String getRemark_34() {
		return remark_34;
	}

	public void setRemark_34(String remark_34) {
		this.remark_34 = remark_34;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getSubtotal_35() {
		return subtotal_35;
	}

	public void setSubtotal_35(double subtotal_35) {
		this.subtotal_35 = subtotal_35;
	}

	public double getNumber_35() {
		return number_35;
	}

	public void setNumber_35(double number_35) {
		this.number_35 = number_35;
	}

	public double getCoeff_35() {
		return coeff_35;
	}

	public void setCoeff_35(double coeff_35) {
		this.coeff_35 = coeff_35;
	}

	public String getRemark_35() {
		return remark_35;
	}

	public void setRemark_35(String remark_35) {
		this.remark_35 = remark_35;
	}
	
}
