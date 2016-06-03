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
@Table(name = "SYS_DATADIR")
public class SysDatadir implements Serializable {

	/** identifier field */
	private String id;

	/** persistent field */
	private String parentid;

	/** persistent field */
	private String key;

	/** nullable persistent field */
	private String value;

	/** persistent field */
	private int order;

	/** persistent field */
	private int childnum;

	/** persistent field */
	private String note;

	/** nullable persistent field */
	private String noteEn;

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

	@Column(name = "C_PARENTID")
	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	@Column(name = "C_KEY")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "C_VALUE")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "I_ORDER")
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "I_CHILDNUM")
	public int getChildnum() {
		return this.childnum;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	@Column(name = "C_NOTE")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return
	 * @struts.validator type="required"
	 */
	@Column(name = "C_NOTE_EN")
	public String getNoteEn() {
		return this.noteEn;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}
	
}