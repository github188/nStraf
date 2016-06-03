package cn.grgbanking.feeltm.menu.webapp.vo;

public class MenuInfoVO {


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
    
    private boolean newAnOther;

    /** default constructor */
    public MenuInfoVO() {
    }

    
    public String getMenuid() {
        return this.menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
    
    public String getParentid() {
        return this.parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
   
    public String getMenuitem() {
        return this.menuitem;
    }

    public void setMenuitem(String menuitem) {
        this.menuitem = menuitem;
    }
  
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
  
    public String getActionto() {
        return this.actionto;
    }

    public void setActionto(String actionto) {
        this.actionto = actionto;
    }
 
    public int getTarget() {
        return this.target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
   
    public int getChildnum() {
        return this.childnum;
    }

    public void setChildnum(int childnum) {
        this.childnum = childnum;
    }
  
    public int getFloor() {
        return this.floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
   
    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


	public boolean isNewAnOther() {
		return newAnOther;
	}


	public void setNewAnOther(boolean newAnOther) {
		this.newAnOther = newAnOther;
	}

   

}
