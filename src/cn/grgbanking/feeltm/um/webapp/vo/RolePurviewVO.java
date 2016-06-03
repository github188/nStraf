package cn.grgbanking.feeltm.um.webapp.vo;

import java.util.List;

@SuppressWarnings("unchecked")
public class RolePurviewVO {
	private String parentMenu="";
	private String menunname="";
	private String menuid="";
	private List menuFucList=null;
	

	public List getMenuFucList() {
		return menuFucList;
	}
	public void setMenuFucList(List menuFucList) {
		this.menuFucList = menuFucList;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getMenunname() {
		return menunname;
	}
	public void setMenunname(String menunname) {
		this.menunname = menunname;
	}
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

}
