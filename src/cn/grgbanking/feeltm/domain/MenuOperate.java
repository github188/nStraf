package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "MENU_OPERATE")
public class MenuOperate  implements Serializable {

	/** identifier field */
    private String operid;

    /** identifier field */
    private String opername;

    /** identifier field */
    private String picpath;

    /** identifier field */
    private String clickname;

    /** identifier field */
    private String keys;

    /** identifier field */
    private String types;
    
    private String site;

   

	/** full constructor */
    public MenuOperate(String operid, String opername, String picpath, String clickname, String keys, String types,String site) {
        this.operid = operid;
        this.opername = opername;
        this.picpath = picpath;
        this.clickname = clickname;
        this.keys = keys;
        this.types = types;
        this.site =site;
    }

    /** default constructor */
    public MenuOperate() {
    }
    @Id
   // @GeneratedValue(generator = "system-uuid") 
    //@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_OPERID", unique = true, nullable = false)
    public String getOperid() {
        return this.operid;
    }

    public void setOperid(String operid) {
        this.operid = operid;
    }
    @Column(name = "C_OPERNAME")
    public String getOpername() {
        return this.opername;
    }

    public void setOpername(String opername) {
        this.opername = opername;
    }
    @Column(name = "C_PICPATH")
    public String getPicpath() {
        return this.picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
    @Column(name = "C_CLICKNAME")
    public String getClickname() {
        return this.clickname;
    }

    public void setClickname(String clickname) {
        this.clickname = clickname;
    }
    @Column(name = "C_KEYS")
    public String getKeys() {
        return this.keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
    @Column(name = "C_TYPES")
    public String getTypes() {
        return this.types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
    @Column(name = "C_SITE")
    public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}
