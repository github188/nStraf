package cn.grgbanking.feeltm.menu.webapp.vo;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


public class MenuOperateVO {   

    /** identifier field */
    private String operid;

    /** persistent field */
    private String opername;

    /** nullable persistent field */
    private String picpath;

    /** nullable persistent field */
    private String clickname;

    /** nullable persistent field */
    private String keys;

    /** nullable persistent field */
    private String types;
    
    private String flag;
    
    private String site;
    
    private String checked;
    
    private String menuid;
    
    private boolean newAnOther;
    
    public String getTypes() {
		return types;
	}


	public void setTypes(String types) {
		this.types = types;
	}


	public String getKeys() {
		return keys;
	}


	public void setKeys(String keys) {
		this.keys = keys;
	}


	/** Default empty constructor. */
    public MenuOperateVO() {}

    
    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *                                                javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // reset any boolean data types to false

    }


	public String getOperid() {
		return operid;
	}


	public void setOperid(String operid) {
		this.operid = operid;
	}


	public String getOpername() {
		return opername;
	}


	public void setOpername(String opername) {
		this.opername = opername;
	}


	public String getClickname() {
		return clickname;
	}


	public void setClickname(String clickname) {
		this.clickname = clickname;
	}


	public String getPicpath() {
		return picpath;
	}


	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getSite() {
		return site;
	}


	public void setSite(String site) {
		this.site = site;
	}
	
	public String getChecked() {
			return checked;
	}


	public void setChecked(String checked) {
			this.checked = checked;
	}
	
	public String getMenuid() {
		return menuid;
	}


	public void setMenuid(String menuid) {
			this.menuid = menuid;
	}


	public boolean isNewAnOther() {
		return newAnOther;
	}


	public void setNewAnOther(boolean newAnOther) {
		this.newAnOther = newAnOther;
	}

}
