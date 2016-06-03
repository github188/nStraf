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
@Table(name = "USR_USRGRP")
public class UsrUsrgrp  implements Serializable {

	/** identifier field */
    private String id;

    /** persistent field */
    private String userid;

    /** persistent field */
    private String grpcode;

    /** full constructor */
    public UsrUsrgrp(String userid, String grpcode) {
        this.userid = userid;
        this.grpcode = grpcode;
    }

    /** default constructor */
    public UsrUsrgrp() {
    }
   // @Id
	//@Column(name = "C_ID", unique = true, nullable = false)
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
    @Column(name = "C_USERID")
    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    @Column(name = "C_GRPCODE")
    public String getGrpcode() {
        return this.grpcode;
    }

    public void setGrpcode(String grpcode) {
        this.grpcode = grpcode;
    }

   

}
