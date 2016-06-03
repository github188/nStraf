package cn.grgbanking.feeltm.um.webapp.vo;

public class UsrGrproleVO {
	 /** identifier field */
    private String id;

    /** persistent field */
    private String grpcode;

    /** persistent field */
    private String rolecode;
    
    private String rolename;

    private String checked="";

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}
