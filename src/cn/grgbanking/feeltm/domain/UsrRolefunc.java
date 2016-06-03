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
@Table(name = "USR_ROLEFUNC")
public class UsrRolefunc  implements Serializable {

	/** identifier field */
    private String id;

    /** persistent field */
    private String rolecode;

    /** persistent field */
    private String funcid;

    /** full constructor */
    public UsrRolefunc(String rolecode, String funcid) {
        this.rolecode = rolecode;
        this.funcid = funcid;
    }

    /** default constructor */
    public UsrRolefunc() {
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
    @Column(name = "C_ROLECODE")
    public String getRolecode() {
        return this.rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }
    @Column(name = "C_FUNCID")
    public String getFuncid() {
        return this.funcid;
    }

    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }

   
}
