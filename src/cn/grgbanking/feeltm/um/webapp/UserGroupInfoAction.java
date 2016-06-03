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

import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.um.service.UserGroupInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;



@SuppressWarnings({"serial"})
public class UserGroupInfoAction extends BaseAction {

	   /** identifier field */
    private String grpcode;

    /** persistent field */
    private String grpname;
    
    private String deptName;
    
    private String grpLevel;
    
	private UserGroupInfoService userGroupInfoService;

	public UserGroupInfoService getuserGroupInfoService() {
		return userGroupInfoService;
	}

	public void setuserGroupInfoService(
			UserGroupInfoService userGroupInfoService) {
		this.userGroupInfoService = userGroupInfoService;
	}

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
			request.getSession().setAttribute("userGroup.menuid", menuid);
			this.saveAllGroupInfo();
			String from = request.getParameter("from");
			if(from !=null && from.equals("refresh") )
				return "";
			else
				return "list";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error("error in (SysGroupInfoAction.java-list())");
			SysLog.error(e);
			
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";			
		}
	}
	
	private void saveAllGroupInfo() throws Exception {
		List list = userGroupInfoService.getAllUserGroupInfoList();
		request.getSession().setAttribute("grp.groupList", list);
		ActionContext.getContext().put("grp.groupList", list);
		
		//list页面刷新
		String from = request.getParameter("from");
		if(from !=null && from.equals("refresh") )
		{
			List<Object> l = new ArrayList<Object >();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				l.add((Object)list.get(i));
			}
			JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.domain.UsrGroup");
			JSONArray jsonObj =jsonUtil.toJSON(l,null);
		
		
			JSONObject input = new JSONObject();
			input.put("pageCount", 1);
			input.put("recordCount", l.size());
			input.put("jsonObj", jsonObj);								
			PrintWriter out = response.getWriter();
			out.print(input);
	    }
    }
		
	public String insert() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String newAnOther=request.getParameter("newAnOther");
		
		//String grpLevel = request.getParameter("grpLevel");
		UsrGroup userGroup = new UsrGroup();
		
		MsgBox msgBox;
		try {
			userGroup.setGrpcode(this.getGrpcode());
			//userGroup.setGrpLevel(this.getGrpLevel());
			userGroup.setGrpname(this.getGrpname());
			
			if (!userGroupInfoService.isExitxUserGroup(userGroup)) {
				//userGroup.setGrpLevel(grpLevel);
				userGroupInfoService.addUserGroupInfo(userGroup);
				SysLog.operLog(request, Constants.OPER_ADD_VALUE, userGroup.getGrpname());//记录日志
				SysLog.info("User:"+userModel.getUserid()+"add a user group ,user group name : "+userGroup.getGrpname()+" ,user group code: "+userGroup.getGrpcode());
				this.saveAllGroupInfo();
				this.addActionMessage(getText("add.ok"));
			} else {
				msgBox = new MsgBox(request, getText("grpInfo.addfaile"), "um",null);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("grpInfo.addfaile"));
			}
		} catch (Exception e) {
			SysLog.error(request,"error in (UserGroupInfoAction.java-insertUserGroupInfo())");
			SysLog.error(e);
			e.printStackTrace();
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("grpInfo.exit"));
		}
		
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		
		if(newAnOther.equals("false")) {
			msgBox = new MsgBox(request,  getText("add.ok"));
			msgBox.setBackUrl(request.getContextPath()+"/pages/um/userGroupInfoList.jsp");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		return "insert";

	}
	
	public String modify()
	{
		try
		{
			this.findOneGroupInfo();
			return "update";
		}
		catch(Exception e)
		{
			SysLog.error("error in (UserGroupInfoAction.java-modify())");
			SysLog.error(e);
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	
	private void findOneGroupInfo() throws Exception{
		String grpcode = request.getParameter("id");

			UsrGroup usrGroup = userGroupInfoService.findOnebyUserGroupInfo(grpcode);
			ActionContext.getContext().put("groupInfo", usrGroup);
	}
	
	public String update()
	{
		return this.updateUserGroupInfo();
	}
	
	public String delete()
	{
		return removeUserGroupInfo();
	}
	private String removeUserGroupInfo(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String[] rolecodes = StringUtils.split(request.getParameter("id"),",");
		try{
			for (int j = 0; j < rolecodes.length; j++) {
				UsrGroup  ug =userGroupInfoService.findOnebyUserGroupInfo(rolecodes[j]);
				SysLog.operLog(request, Constants.OPER_DELETE_VALUE, ug.getGrpname());//记录日志
				SysLog.info("User:"+userModel.getUserid()+"delete a user group ,user group code : "+ug.getGrpname());
			}
		
			int i = userGroupInfoService.removeUserGroupInfo(rolecodes);
		
			this.saveAllGroupInfo();
			
			MsgBox msgBox = new MsgBox(request, getText("grpInfo.deleteok"), "um",
					new String[] { String.valueOf(i) });
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		catch(Exception e){
			SysLog.info("User:"+userModel.getUserid()+"failed to delete user's group");
			SysLog.error(e);
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}

		
		return "msgBox";

	}
	
	private String updateUserGroupInfo() {
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
	
		UsrGroup userGroup = new UsrGroup();
		try {
			userGroup.setGrpcode(this.getGrpcode());
			//userGroup.setGrpLevel(this.getGrpLevel());
			userGroup.setGrpname(this.getGrpname());
			
			userGroupInfoService.updateUserGroupInfo(userGroup);
			SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, userGroup.getGrpname());//记录日志
			SysLog.info("User:"+userModel.getUserid()+"modify a user group ,user group name : "+userGroup.getGrpname()+" ,user group code: "+userGroup.getGrpcode());
			this.saveAllGroupInfo();

			MsgBox msgBox = new MsgBox(request, getText("grpInfo.updateok"), "um", null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error("error in (UserGroupInfoAction.java-updateUserGroupInfo())");
			SysLog.info("User:"+userModel.getUserid()+"failed to modify a user group ,user group name : "+userGroup.getGrpname()+" ,user group code: "+userGroup.getGrpcode());
			SysLog.error(e);

			MsgBox msgBox = new MsgBox(request, getText("grpInfo.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}

	}
	
//////////////////////////////////////

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getGrpname() {
		return grpname;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	public String getGrpLevel() {
		return grpLevel;
	}

	public void setGrpLevel(String grpLevel) {
		this.grpLevel = grpLevel;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
