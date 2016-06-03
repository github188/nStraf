package cn.grgbanking.feeltm.um.webapp;

import java.util.List;

import cn.grgbanking.feeltm.domain.UsrGrprole;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.um.service.UserGroupRoleService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;



@SuppressWarnings({"serial", "unchecked"})
public class UserGroupRoleAction extends BaseAction {
	private UserGroupRoleService userGroupRoleService;
	
	/** identifier field */
    private String id;

    /** persistent field */
    private String grpcode;

    /** persistent field */
    private String rolecode;
    
    private String rolename;

    private String checked="";
	

	public UserGroupRoleService getuserGroupRoleService() {
		return userGroupRoleService;
	}

	public void setuserGroupRoleService(UserGroupRoleService userGroupRoleService)
	{
		this.userGroupRoleService = userGroupRoleService;
	}

	
	public String find()
	{
		try
		{
			findGroupRoleList();
			return "list";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error("error in (UserGroupRoleAction.java-find())");
			SysLog.error(e);
			
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";	
		}
	}
	
	public String save()
	{
		return this.saveGroupRole();
	}

	public void findGroupRoleList() throws Exception{
		String grpcode = request.getParameter("grpcode");
					
		List list = userGroupRoleService.findGrpRole(grpcode);
		request.getSession().setAttribute("grpRoleLst", list);
		
		ActionContext.getContext().put("grpRoleLst", list);

	}

	public String saveGroupRole() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String[] rolecodes = request.getParameterValues("rolecode");// ��������ɫ������ϵ�һ������
		
		String grpcode = request.getParameter("grpcode");
		try
		{
			userGroupRoleService.removeGroupRole(grpcode);
	
			if (rolecodes != null)
				for (int i = 0; i < rolecodes.length; i++) {
					UsrGrprole grprole = new UsrGrprole();
						grprole.setGrpcode(grpcode);
					grprole.setRolecode(rolecodes[i]);
					userGroupRoleService.saveGroupRole(grprole);
				}
			SysLog.operLog(request, Constants.OPER_ADD_VALUE, grpcode);//记录日志
			SysLog.info("User:"+userModel.getUserid()+" save role group : "+grpcode);
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok"), "um",null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		catch(Exception e)
		{
			SysLog.error("error in (UserGroupRoleAction.java-saveGroupRole())");
			SysLog.error(request,"failed to save role group information");
			e.printStackTrace();
			
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";				
		}
	}

	public UserGroupRoleService getUserGroupRoleService() {
		return userGroupRoleService;
	}

	public void setUserGroupRoleService(UserGroupRoleService userGroupRoleService) {
		this.userGroupRoleService = userGroupRoleService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
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

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
}
