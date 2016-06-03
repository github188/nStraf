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
@Table(name = "MENU_INFO")
public class MenuInfo implements Serializable {

	/** identifier field */
	private String menuid;

	/** persistent field */
	private String parentid;

	/** persistent field */
	private String menuitem;

	/** persistent field */
	private int order;

	/** nullable persistent field */
	private String actionto;

	/** persistent field */
	private int target;

	/** persistent field */
	private int childnum;

	/** persistent field */
	private int floor;

	/** nullable persistent field */
	private String pic;

	/** full constructor */
	public MenuInfo(String parentid, String menuitem, int order,
			String actionto, int target, int childnum, int floor, String pic) {
		this.parentid = parentid;
		this.menuitem = menuitem;
		this.order = order;
		this.actionto = actionto;
		this.target = target;
		this.childnum = childnum;
		this.floor = floor;
		this.pic = pic;
	}

	/** default constructor */
	public MenuInfo() {
	}

	/** minimal constructor */
	public MenuInfo(String parentid, String menuitem, int order, int target,
			int childnum, int floor) {
		this.parentid = parentid;
		this.menuitem = menuitem;
		this.order = order;
		this.target = target;
		this.childnum = childnum;
		this.floor = floor;
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

	@Column(name = "C_PARENTID")
	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	@Column(name = "C_MENUITEM")
	public String getMenuitem() {
		return this.menuitem;
	}

	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}

	@Column(name = "I_ORDER")
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "C_ACTIONTO")
	public String getActionto() {
		return this.actionto;
	}

	public void setActionto(String actionto) {
		this.actionto = actionto;
	}

	@Column(name = "I_TARGET")
	public int getTarget() {
		return this.target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	@Column(name = "I_CHILDNUM")
	public int getChildnum() {
		return this.childnum;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	@Column(name = "I_FLOOR")
	public int getFloor() {
		return this.floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	@Column(name = "C_PIC")
	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
