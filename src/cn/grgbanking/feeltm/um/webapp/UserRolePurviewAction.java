package cn.grgbanking.feeltm.um.webapp;

import java.util.List;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.um.service.UserRolePurviewService;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;


@SuppressWarnings({"serial", "unchecked"})
public class UserRolePurviewAction extends BaseAction {
	private UserRolePurviewService userRolePurviewService;

	public String saveRoleFunc()
	{
		//return saveRoleFunc();
		try
		{
			this.getMenuTree();
			return "success";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error("error in (UserRolePurviewAction.java-saveRoleFunc())");
			SysLog.error(e);
			
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";	
		}
	}
	
	public String save()
	{
		return this.toSaveRoleFunc();
	}

	public UserRolePurviewService getuserRolePurviewService() {
		return userRolePurviewService;
	}



	public void setuserRolePurviewService(
			UserRolePurviewService userRolePurviewService) {
		this.userRolePurviewService = userRolePurviewService;
	}



	public void getMenuTree() throws Exception{
		String rolecode = request.getParameter("rolecode");
		request.setAttribute("rolecode", rolecode);
		List menuList = userRolePurviewService.getMenuTree(rolecode);
		
		request.getSession().setAttribute("menuList", menuList);
		ActionContext.getContext().put("menuList", menuList);
	}

	public String toSaveRoleFunc() {
		//ActionMessages msgs = new ActionMessages();
		String rolecode = request.getParameter("rolecode");
		String count = request.getParameter("count");
		try
		{
			userRolePurviewService.removeRoleFunc(rolecode);
			for (int n = 0; n < Integer.parseInt(count); n++) {
				String funcid = "checkbox" + (n + 1);
				String[] funcids = request.getParameterValues(funcid);
				if (funcids != null)
				{	userRolePurviewService.saveRoleFunc(rolecode, funcids);	
					SysLog.info(request,"save a role function:"+rolecode+funcids);
				}
			}
			this.getMenuTree();
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok"), "um",null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		catch(Exception e)
		{
			SysLog.error("error in (UserRolePurviewAction.java-saveRoleFunc())");
			SysLog.error(request,"failed to save role function."+rolecode);
			SysLog.error(e);
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";				
		}
	}
}
