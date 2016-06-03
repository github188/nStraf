package cn.grgbanking.feeltm.menu.webapp.vo;
/**
 * 
 *跳转菜单返回对应父级参数
 */
public class MenuRedirectVo {
	
	private int topMenuOrder;//跳转菜单父级ID序号
    private int leftContentOrder;//调转菜单左侧子父级序号
    private int leftMenuOrder;//该跳转菜单序号
    private String MenuId;//调转菜单ID
    private String topMenuId;//跳转菜单父级ID
    
	public int getTopMenuOrder() {
		return topMenuOrder;
	}
	public void setTopMenuOrder(int topMenuOrder) {
		this.topMenuOrder = topMenuOrder;
	}
	public int getLeftContentOrder() {
		return leftContentOrder;
	}
	public void setLeftContentOrder(int leftContentOrder) {
		this.leftContentOrder = leftContentOrder;
	}
	public int getLeftMenuOrder() {
		return leftMenuOrder;
	}
	public void setLeftMenuOrder(int leftMenuOrder) {
		this.leftMenuOrder = leftMenuOrder;
	}
	public String getMenuId() {
		return MenuId;
	}
	public void setMenuId(String menuId) {
		MenuId = menuId;
	}
	public String getTopMenuId() {
		return topMenuId;
	}
	public void setTopMenuId(String topMenuId) {
		this.topMenuId = topMenuId;
	}
    
    
}
