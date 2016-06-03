/**
 * 
 */
package cn.grgbanking.feeltm.um.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.UsrRole;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.um.service.UserRoleInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;



@SuppressWarnings({"serial", "unchecked"})
public class UserRoleInfoAction extends BaseAction {
	private UserRoleInfoService userRoleInfoService;

	 /** identifier field */
    private String rolecode;

    /** persistent field */
    private String rolename;

    public void refresh()
	{
		list();
	}
    
    public String addpage()
    {
    	return "insert";
    }
	public String list()
	{
		try
		{
			String menuid = request.getParameter("menuid");
			request.getSession().setAttribute("userRole.menuid", menuid);
			this.saveAllRoleInfo();
			
			String from = request.getParameter("from");
			if(from !=null && from.equals("refresh") )
				return "";
			else 
				return "list";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error("error in (UserRoleInfoAction.java-list())");
			SysLog.error(e);
			
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";	
		}
	}
	private void saveAllRoleInfo() throws Exception{
		List list = userRoleInfoService.getAllRoleInfoList();
		request.getSession().setAttribute("role.roleList", list);
		ActionContext.getContext().put("role.roleList", list);
		
		//list页面刷新
		String from = request.getParameter("from");
		if(from !=null && from.equals("refresh") )
		{
			List<Object> l = new ArrayList<Object >();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				l.add((Object)list.get(i));
			}
			JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.domain.UsrRole");
			JSONArray jsonObj =jsonUtil.toJSON(l,null);
			
			JSONObject input = new JSONObject();
			input.put("pageCount", 1);
			input.put("recordCount", l.size());
			input.put("jsonObj", jsonObj);								
			PrintWriter out = response.getWriter();
			out.print(input);	
		   
	    }
		
	}
	
	public String add() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String newAnOther=request.getParameter("newAnOther");
		
		UsrRole userRole = new UsrRole();
		MsgBox msgBox;
		try {
			userRole.setRolecode(this.getRolecode());
			userRole.setRolename(this.getRolename());
			
			if (!userRoleInfoService.isExitxRole(userRole)) {
				userRoleInfoService.addUserRoleInfo(userRole);
				this.saveAllRoleInfo();
				SysLog.operLog(request, Constants.OPER_ADD_VALUE, userRole.getRolename());//记录日志
				SysLog.info("User:"+userModel.getUserid()+" add a role,name is : "+userRole.getRolename()+" ,role id : "+userRole.getRolecode());
				this.addActionMessage(getText("add.ok"));
			} else {
				msgBox = new MsgBox(request, getText("roleInfo.addfaile"), "um",null);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("roleInfo.addfaile"));
			}
		} catch (Exception e) {
			SysLog.error(request,"error in (UserRoleInfoAction.java-insertUserRoleInfo())");
			SysLog.info("User:"+userModel.getUserid()+"failed to add a role: "+userRole.getRolename()+" role id : "+userRole.getRolecode());
			SysLog.error(e);
			e.printStackTrace();
			msgBox = new MsgBox(request, getText("roleInfo.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("roleInfo.exit"));
		}
		
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		
		//没有点“提交后继续新增”
		if(newAnOther.equals("false")) {
			msgBox = new MsgBox(request,  getText("add.ok"));
			msgBox.setBackUrl(request.getContextPath()+"/pages/um/userRoleInfoList.jsp");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
			}
	
		return "insert";

	}
	
	public String modify()
	{
		try
		{
			this.findOneRoleInfo();
			return "update";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error("error in (UserRoleInfoAction.java-modify())");
			SysLog.error(e);
			
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";	
		}
	}
	
	private void findOneRoleInfo() throws Exception {
		String rolecode = request.getParameter("id");
		
		UsrRole usrRole = userRoleInfoService.findOnebyRoleInfo(rolecode);
		ActionContext.getContext().put("usrRole", usrRole);
	}
	
	public String update()
	{
		return this.updateUserRoleInfo();
	}
	
	private String updateUserRoleInfo() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		
		UsrRole userRole = new UsrRole();
		try {
			userRole.setRolecode(this.getRolecode());
			userRole.setRolename(this.getRolename());
			userRoleInfoService.updateUserRoleInfo(userRole);
			this.saveAllRoleInfo();
			SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, userRole.getRolename());//记录日志
			SysLog.info("User:"+userModel.getUserid()+"update a role,name is : "+userRole.getRolename()+" role id : "+userRole.getRolecode());
			//this.saveMessages(request, msgs);
			MsgBox msgBox = new MsgBox(request, getText("roleInfo.updateok"), "um", null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,"error in (UserRoleInfoAction.java-updateUserRoleInfo())");
			SysLog.info("User:"+userModel.getUserid()+"failed to update a role,name is : "+userRole.getRolename()+" role id : "+userRole.getRolecode());
			SysLog.error(e);
			//this.saveMessages(request, msgs);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}

	}
	
	public String delete()
	{
		return removeUserRoleInfo();
	}
	
	private String removeUserRoleInfo() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String[] rolecodes = StringUtils.split(request.getParameter("id"),",");
		int i = userRoleInfoService.removeUserRoleInfo(rolecodes);
		try{
			for(int j=0 ; j<rolecodes.length; j++){
				SysLog.operLog(request, Constants.OPER_DELETE_VALUE, rolecodes[0]);//记录日志
				SysLog.info("User:"+userModel.getUserid()+"delete a role,role code is : "+rolecodes[0]);
			}
			
			this.saveAllRoleInfo();
		
			MsgBox msgBox = new MsgBox(request, getText("roleInfo.deleteok"), "um",
					new String[] { String.valueOf(i) });
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			
		}
		catch(Exception e){
			SysLog.error("User:"+userModel.getUserid()+"failed to delete a role,role code is : "+rolecodes[0]);
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	public UserRoleInfoService getuserRoleInfoService() {
		return userRoleInfoService;
	}

	public void setuserRoleInfoService(
			UserRoleInfoService userRoleInfoService) {
		this.userRoleInfoService = userRoleInfoService;
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
