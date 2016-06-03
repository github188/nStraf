package cn.grgbanking.feeltm.expense.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 出差报销明细
 * @author lhyan3
 * 2014年7月18日
 */
@Entity
@Table(name = "OA_EXPENSE_ACCOUNT_COSTDETAIL")
public class OAExpenseAccountCheckCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_DATE")
	private String dateTime;
	
	@Column(name = "C_PLACE")
	private String place;
	
	@Column(name = "C_TASK")
	private String task;
	
	@Column(name = "C_FLY")
	private String fly;
	
	@Column(name = "C_TAXI")
	private String taxi;
	
	@Column(name = "C_BUS")
	private String bus;
	
	@Column(name = "C_LIVING")
	private String living;
	
	@Column(name = "C_CONTACT")
	private String contact;
	
	@Column(name = "C_BUSINESS")
	private String business;
	
	@Column(name = "C_OTHER")
	private String other;
	
	@Column(name = "C_BUZHU")
	private String buzhu;
	
	@Column(name = "C_ACCOUNT")
	private String account;
	
	@Column(name = "C_PATH")
	private String path;
	
	@Column(name = "C_UUID")
	private String uuid;
	
	@Column(name = "C_NUMBER")
	private int number;
	
	@Column(name="C_EXPENSEID")
	private String expenseId;//报销单id
	
	@Column(name="c_prjname")
	private String prjname;//项目名称

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getFly() {
		return fly;
	}

	public void setFly(String fly) {
		this.fly = fly;
	}

	public String getTaxi() {
		return taxi;
	}

	public void setTaxi(String taxi) {
		this.taxi = taxi;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public String Living() {
		return living;
	}

	public String getLiving() {
		return living;
	}

	public void setLiving(String living) {
		this.living = living;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getBuzhu() {
		return buzhu;
	}

	public void setBuzhu(String buzhu) {
		this.buzhu = buzhu;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getExpenseId() {
		return expenseId;
	}
	
	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	public String getPrjname() {
		return prjname;
	}

	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}
	
	
	
	
	

}
