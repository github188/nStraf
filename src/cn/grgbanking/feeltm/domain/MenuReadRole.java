package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "MENU_READ_ROLE")
public class MenuReadRole implements Serializable {

	/** identifier field */
    private String menuid;

    /** identifier field */
    private String roleid;

    /** identifier field */
    private String ordinal;

    /** full constructor */
    public MenuReadRole(String menuid, String roleid, String ordinal) {
        this.menuid = menuid;
        this.roleid = roleid;
        this.ordinal = ordinal;
    }

    /** default constructor */
    public MenuReadRole() {
    }
    @Id
    @GeneratedValue(generator = "system-uuid") 
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_MENUID", unique = true, nullable = false)
    public String getMenuid() {
        return this.menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
    @Column(name = "C_ROLEID")
    public String getRoleid() {
        return this.roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
    @Column(name = "C_ORDINAL")
    public String getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("menuid", getMenuid())
            .append("roleid", getRoleid())
            .append("ordinal", getOrdinal())
            .toString();
    }

   

}
