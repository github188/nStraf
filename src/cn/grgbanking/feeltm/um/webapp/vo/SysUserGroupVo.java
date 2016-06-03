package cn.grgbanking.feeltm.um.webapp.vo;


public class SysUserGroupVo  {
	 /** identifier field */
    private String id;
    
	/** persistent field */
	private String userid;

	/** persistent field */
	private String grpcode;

	private String grpname;

	private String checked;

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getGrpname() {
		return grpname;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
