package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "ORG_INFO")
public class OrgInfo  implements Serializable {

	/** identifier field */
    private String id;

    /** persistent field */
    private String orgid;

    /** persistent field */
    private String parentid;

    /** persistent field */
    private String orgname;

    /** persistent field */
    private String orgfullname;

    /** nullable persistent field */
    private String orgnameEn;

    /** nullable persistent field */
    private String contact;

    /** nullable persistent field */
    private String tel;

    /** nullable persistent field */
    private String address;

    /** persistent field */
    private int order;

    /** persistent field */
    private int level;

    /** persistent field */
    private int childnum;

    /** nullable persistent field */
    private String nowstatus;

    /** nullable persistent field */
    private String remark;

   

    /** nullable persistent field */
    private String ifreckon;
  

    /** nullable persistent field */
    private String orgLevel;
    
    private String orgfloor;

    /** full constructor */
    public OrgInfo(String orgid, String parentid, String orgname, String orgfullname, String orgnameEn, String contact, String tel, String address, int order, int level, int childnum, String nowstatus, String remark, String orgLevel, String ifreckon) {
        this.orgid = orgid;
        this.parentid = parentid;
        this.orgname = orgname;
        this.orgfullname = orgfullname;
        this.orgnameEn = orgnameEn;
        this.contact = contact;
        this.tel = tel;
        this.address = address;
        this.order = order;
        this.level = level;
        this.childnum = childnum;
        this.nowstatus = nowstatus;
        this.remark = remark;
        this.orgLevel = orgLevel;
        this.ifreckon = ifreckon;
    }

    /** default constructor */
    public OrgInfo() {
    }

    /** minimal constructor */
    public OrgInfo(String orgid, String parentid, String orgname, String orgfullname, int order, int level, int childnum) {
        this.orgid = orgid;
        this.parentid = parentid;
        this.orgname = orgname;
        this.orgfullname = orgfullname;
        this.order = order;
        this.level = level;
        this.childnum = childnum;
    }
    @Id
    @GeneratedValue(generator = "system-uuid") 
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "C_ORGID")
    public String getOrgid() {
        return this.orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    @Column(name = "C_PARENTID")
    public String getParentid() {
        return this.parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
    @Column(name = "C_ORGNAME")
    public String getOrgname() {
        return this.orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    @Column(name = "C_ORGFULLNAME")
    public String getOrgfullname() {
        return this.orgfullname;
    }

    public void setOrgfullname(String orgfullname) {
        this.orgfullname = orgfullname;
    }
    @Column(name = "C_ORGNAME_EN")
    public String getOrgnameEn() {
        return this.orgnameEn;
    }

    public void setOrgnameEn(String orgnameEn) {
        this.orgnameEn = orgnameEn;
    }
    @Column(name = "C_CONTACT")
    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    @Column(name = "C_TEL")
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    @Column(name = "C_ADDRESS")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "I_ORDER")
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    @Column(name = "I_LEVEL")
    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    @Column(name = "I_CHILDNUM")
    public int getChildnum() {
        return this.childnum;
    }

    public void setChildnum(int childnum) {
        this.childnum = childnum;
    }
    @Column(name = "C_NOWSTATUS")
    public String getNowstatus() {
        return this.nowstatus;
    }

    public void setNowstatus(String nowstatus) {
        this.nowstatus = nowstatus;
    }
    @Column(name = "C_REMARK")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "C_ORG_LEVEL")
    public String getOrgLevel() {
        return this.orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }
    @Column(name = "C_IFRECKON")
    public String getIfreckon() {
        return this.ifreckon;
    }

    public void setIfreckon(String ifreckon) {
        this.ifreckon = ifreckon;
    }

   
    @Column(name = "C_ORGFLOOR")
	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}

}
