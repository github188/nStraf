package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "TML_INFO")
public class TmlInfo implements Serializable {
	private String termid;
	private String termseq;
	private String counterid;
	private String termtype;
	private String brand;
	private Date opertermid;
	private String netaddr;
	private Integer netport;
	private String softversion;
	private Integer savebox;
	private Integer cwdbox;
	private String isinteratm;
	private String orgid;
	private String areaid;
	private String termaddress;
	private String mark;
	private String laytype;
	private String settype;
	private String selfbankid;
	private String insttype;
	private String instarea;
	private String snproperty;
	private String instproperty;
	private String runbegintime;
	private String runendtime;
	private Integer daytime;
	private String procurementyear;
	private String procurementbatch;
	private Date arrivaldate;
	private Date instdate;
	private Date activedate;
	private Integer warrantystatus;
	private Date warrantybegindate;
	private Date warrantyenddate;
	private Date warrantydays;
	private String maintainid;
	private String id;

	public TmlInfo(String termid, String termseq, String counterid,
			String termtype, String brand, Date opertermid, String netaddr,
			Integer netport, String softversion, Integer savebox,
			Integer cwdbox, String isinteratm, String orgid, String areaid,
			String termaddress, String mark, String laytype, String settype,
			String selfbankid, String insttype, String instarea,
			String snproperty, String instproperty, String runbegintime,
			String runendtime, Integer daytime, String procurementyear,
			String procurementbatch, Date arrivaldate, Date instdate,
			Date activedate, Integer warrantystatus, Date warrantybegindate,
			Date warrantyenddate, Date warrantydays, String maintainid,
			String id) {
		this.termid = termid;
		this.termseq = termseq;
		this.counterid = counterid;
		this.termtype = termtype;
		this.brand = brand;
		this.opertermid = opertermid;
		this.netaddr = netaddr;
		this.netport = netport;
		this.softversion = softversion;
		this.savebox = savebox;
		this.cwdbox = cwdbox;
		this.isinteratm = isinteratm;
		this.orgid = orgid;
		this.areaid = areaid;
		this.termaddress = termaddress;
		this.mark = mark;
		this.laytype = laytype;
		this.settype = settype;
		this.selfbankid = selfbankid;
		this.insttype = insttype;
		this.instarea = instarea;
		this.snproperty = snproperty;
		this.instproperty = instproperty;
		this.runbegintime = runbegintime;
		this.runendtime = runendtime;
		this.daytime = daytime;
		this.procurementyear = procurementyear;
		this.procurementbatch = procurementbatch;
		this.arrivaldate = arrivaldate;
		this.instdate = instdate;
		this.activedate = activedate;
		this.warrantystatus = warrantystatus;
		this.warrantybegindate = warrantybegindate;
		this.warrantyenddate = warrantyenddate;
		this.warrantydays = warrantydays;
		this.maintainid = maintainid;
		this.id = id;
	}

	public TmlInfo() {
	}

	@Column(name = "C_TERMID")
	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	@Column(name = "C_TERMSEQ")
	public String getTermseq() {
		return termseq;
	}

	public void setTermseq(String termseq) {
		this.termseq = termseq;
	}

	@Column(name = "C_COUNTERID")
	public String getCounterid() {
		return counterid;
	}

	public void setCounterid(String counterid) {
		this.counterid = counterid;
	}

	@Column(name = "C_TERMTYPE")
	public String getTermtype() {
		return termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	@Column(name = "C_BRAND")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "D_OPERTERMID")
	public Date getOpertermid() {
		return opertermid;
	}

	public void setOpertermid(Date opertermid) {
		this.opertermid = opertermid;
	}

	@Column(name = "C_NETADDR")
	public String getNetaddr() {
		return netaddr;
	}

	public void setNetaddr(String netaddr) {
		this.netaddr = netaddr;
	}

	@Column(name = "I_NETPORT")
	public Integer getNetport() {
		return netport;
	}

	public void setNetport(Integer netport) {
		this.netport = netport;
	}

	@Column(name = "C_SOFTVERSION")
	public String getSoftversion() {
		return softversion;
	}

	public void setSoftversion(String softversion) {
		this.softversion = softversion;
	}

	@Column(name = "I_SAVEBOX")
	public Integer getSavebox() {
		return savebox;
	}

	public void setSavebox(Integer savebox) {
		this.savebox = savebox;
	}

	@Column(name = "I_CWDBOX")
	public Integer getCwdbox() {
		return cwdbox;
	}

	public void setCwdbox(Integer cwdbox) {
		this.cwdbox = cwdbox;
	}

	@Column(name = "C_ISINTERATM")
	public String getIsinteratm() {
		return isinteratm;
	}

	public void setIsinteratm(String isinteratm) {
		this.isinteratm = isinteratm;
	}

	@Column(name = "C_ORGID")
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	@Column(name = "C_AREAID")
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	@Column(name = "C_TERMADDRESS")
	public String getTermaddress() {
		return termaddress;
	}

	public void setTermaddress(String termaddress) {
		this.termaddress = termaddress;
	}

	@Column(name = "C_MARK")
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@Column(name = "C_LAYTYPE")
	public String getLaytype() {
		return laytype;
	}

	public void setLaytype(String laytype) {
		this.laytype = laytype;
	}

	@Column(name = "C_SETTYPE")
	public String getSettype() {
		return settype;
	}

	public void setSettype(String settype) {
		this.settype = settype;
	}

	@Column(name = "C_SELFBANKID")
	public String getSelfbankid() {
		return selfbankid;
	}

	public void setSelfbankid(String selfbankid) {
		this.selfbankid = selfbankid;
	}

	@Column(name = "C_INSTTYPE")
	public String getInsttype() {
		return insttype;
	}

	public void setInsttype(String insttype) {
		this.insttype = insttype;
	}

	@Column(name = "C_INSTAREA")
	public String getInstarea() {
		return instarea;
	}

	public void setInstarea(String instarea) {
		this.instarea = instarea;
	}

	@Column(name = "C_SNPROPERTY")
	public String getSnproperty() {
		return snproperty;
	}

	public void setSnproperty(String snproperty) {
		this.snproperty = snproperty;
	}

	@Column(name = "C_INSTPROPERTY")
	public String getInstproperty() {
		return instproperty;
	}

	public void setInstproperty(String instproperty) {
		this.instproperty = instproperty;
	}

	@Column(name = "C_RUNBEGINTIME")
	public String getRunbegintime() {
		return runbegintime;
	}

	public void setRunbegintime(String runbegintime) {
		this.runbegintime = runbegintime;
	}

	@Column(name = "C_RUNENDTIME")
	public String getRunendtime() {
		return runendtime;
	}

	public void setRunendtime(String runendtime) {
		this.runendtime = runendtime;
	}

	@Column(name = "I_DAYTIME")
	public Integer getDaytime() {
		return daytime;
	}

	public void setDaytime(Integer daytime) {
		this.daytime = daytime;
	}

	@Column(name = "C_PROCUREMENTYEAR")
	public String getProcurementyear() {
		return procurementyear;
	}

	public void setProcurementyear(String procurementyear) {
		this.procurementyear = procurementyear;
	}

	@Column(name = "C_PROCUREMENTBATCH")
	public String getProcurementbatch() {
		return procurementbatch;
	}

	public void setProcurementbatch(String procurementbatch) {
		this.procurementbatch = procurementbatch;
	}

	@Column(name = "D_ARRIVALDATE")
	public Date getArrivaldate() {
		return arrivaldate;
	}

	public void setArrivaldate(Date arrivaldate) {
		this.arrivaldate = arrivaldate;
	}

	@Column(name = "D_INSTDATE")
	public Date getInstdate() {
		return instdate;
	}

	public void setInstdate(Date instdate) {
		this.instdate = instdate;
	}

	@Column(name = "D_ACTIVEDATE")
	public Date getActivedate() {
		return activedate;
	}

	public void setActivedate(Date activedate) {
		this.activedate = activedate;
	}

	@Column(name = "I_WARRANTYSTATUS")
	public Integer getWarrantystatus() {
		return warrantystatus;
	}

	public void setWarrantystatus(Integer warrantystatus) {
		this.warrantystatus = warrantystatus;
	}

	@Column(name = "D_WARRANTYBEGINDATE")
	public Date getWarrantybegindate() {
		return warrantybegindate;
	}

	public void setWarrantybegindate(Date warrantybegindate) {
		this.warrantybegindate = warrantybegindate;
	}

	@Column(name = "D_WARRANTYENDDATE")
	public Date getWarrantyenddate() {
		return warrantyenddate;
	}

	public void setWarrantyenddate(Date warrantyenddate) {
		this.warrantyenddate = warrantyenddate;
	}

	@Column(name = "D_WARRANTYDAYS")
	public Date getWarrantydays() {
		return warrantydays;
	}

	public void setWarrantydays(Date warrantydays) {
		this.warrantydays = warrantydays;
	}

	@Column(name = "C_MAINTAINID")
	public String getMaintainid() {
		return maintainid;
	}

	public void setMaintainid(String maintainid) {
		this.maintainid = maintainid;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
