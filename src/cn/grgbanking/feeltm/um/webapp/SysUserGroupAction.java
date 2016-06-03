package cn.grgbanking.feeltm.um.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.um.webapp.vo.SysUserGroupVo;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({ "serial" })
public class SysUserGroupAction extends BaseAction {
	private SysUserGroupService sysUserGroupService;
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private StaffInfoService staffInfoService;

	private String userid;
	private String[] grpcode;
	private String usergroups;

	/**
	 * lhy 2014-4-29 跳往组页面数据
	 * 
	 * @return
	 */
	public String findStaffGroupList() {
		List<SysUserGroupVo> voList = new ArrayList<SysUserGroupVo>();
		if (null != userid && !"".equals(userid)) {
			List grpUser = sysUserGroupService.getUserGropByUserId(userid);
			List grplist = sysUserGroupService.getAllGroupList();
			if (null != grplist && grplist.size() > 0) {
				for (int i = 0; i < grplist.size(); i++) {
					SysUserGroupVo vo = new SysUserGroupVo();
					UsrGroup userGroup = (UsrGroup) grplist.get(i);
					vo.setGrpcode(userGroup.getGrpcode());
					vo.setGrpname(userGroup.getGrpname());
					if (null != grpUser && grpUser.size() > 0) {
						for (int j = 0; j < grpUser.size(); j++) {
							UsrGroup userUsrgrp = (UsrGroup) grpUser.get(j);
							if (userUsrgrp.getGrpcode().equals(
									userGroup.getGrpcode())) {
								vo.setChecked("checked");
							}
						}
					}
					vo.setUserid(userid);
					voList.add(vo);
				}
			}
		}
		ActionContext.getContext().put("usrGrpLst", voList);
		ActionContext.getContext().put("userid", userid);
		return "list";
	}

	/**
	 * lhy 2014-4-23 更新用户与组之间的关系
	 * 
	 * @return
	 */
	public String updateusrGroup() {
		MsgBox msgBox;
		try {
			String grpcode = request.getParameter("grpcode");
			// 删除该组与人员的关联
			sysUserGroupService.removeGroupByUserid(grpcode);
			if (null != usergroups && !"".equals(usergroups)) {
				String userids[] = usergroups.split(",");
				for (int i = 0; i < userids.length; i++) {
					UsrUsrgrp usrUsrgrp = new UsrUsrgrp();
					usrUsrgrp.setGrpcode(grpcode);
					usrUsrgrp.setUserid(userids[i]);
					sysUserGroupService.saveSysUserGroup(usrUsrgrp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,
					"failed:java-SysUserGroupAction-updateusrGroup()");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		msgBox = new MsgBox(request, getText("operInfoform.updateok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	/**
	 * lhy 2014-4-23 根据组名获取该组员工，同时获取所有不是该组员工提供选择
	 * 
	 * @return
	 */
	public String findAllUser() {
		try {
			// 获取页面传过来的参数
			String grpcode = request.getParameter("grpcode");
			// 获取该组的用户
			List usergrpList = sysUserGroupService.findUserByGroupCode(grpcode);
			List userList = sysUserGroupService.findUserNotGroupByGrp(grpcode);
			request.setAttribute("usergrpList", usergrpList);
			request.setAttribute("userList", userList);
			request.setAttribute("grpcode", grpcode);
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			request.setAttribute("deptMap", deptMap);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,
					"failed:java-SysUserGroupAction-findGroupUser()");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "userList";
	}
	/**
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public String findAllUserByProject() {
		try {
			// 获取页面传过来的参数
			String groupId = request.getParameter("groupId");
			// 获取该组的用户
			List usergrpList = sysUserGroupService.findUserByProject(groupId);
			List userList = sysUserGroupService.findUserNotGroupByProject(groupId);
			request.setAttribute("usergrpList", usergrpList);
			request.setAttribute("userList", userList);
			request.setAttribute("grpcode", groupId);
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			request.setAttribute("deptMap", deptMap);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,
					"failed:java-SysUserGroupAction-findGroupUser()");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "userproject";
	}
	
	/**
	 * 
	 * lhyan3
	 * 2014年6月5日
	 */
	@SuppressWarnings("rawtypes")
	public void getStaffByDeptName(){
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String grpcode = request.getParameter("grpcode");
			String username = request.getParameter("username");
			//获取该组的用户
			List usergrpList = sysUserGroupService.findUserByGroupCode1(grpcode);
			//不在该组的用户
			List userList = sysUserGroupService.findUserNotGroupByGrp1(deptname,grpcode,username);
			Map map = new HashMap();
			map.put("grplist", usergrpList);
			map.put("notgrp", userList);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 项目组部门 
	 * lhyan3
	 * 2014年6月13日
	 */
	public void getStaffProjectByDeptName(){
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String grpcode = request.getParameter("grpcode");
			//获取该组的用户
			List usergrpList = sysUserGroupService.findUserByProject1(grpcode);
			//不在该组的用户
			List userList = sysUserGroupService.findUserNotGroupByGrp1(deptname,grpcode,"");
			Map map = new HashMap();
			map.put("grplist", usergrpList);
			map.put("notgrp", userList);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 向ajax请求返回数据
	 * @param str
	 */
	private void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String findSysUserGroupList() {
		try {
			String param = request.getParameter("param");
			// String userid=request.getParameter("userid");
			// String level=request.getParameter("level");
			String userid = param.split(",")[0];

			String level = param.split(",")[1];

			// UserModel userModel =
			// (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			List list = sysUserGroupService.findSysUserGroupList(userid, level);
			ActionContext.getContext().put("usrGrpLst", list);
			ActionContext.getContext().put("userid", userid);

			return "list";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,
					"failed:java-SysUserGroupAction-findSysUserGroupList()");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public String saveSysUserGroup() {
		String[] grpcodes = this.getGrpcode();// ��������ɫ������ϵ�һ������
		String userid = this.getUserid();
		String action = request.getParameter("action");
		// ActionMessages msgs = new ActionMessages();
		try {
			sysUserGroupService.removeSysUserGroup(userid);

			if (grpcodes != null)
				for (int i = 0; i < grpcodes.length; i++) {
					UsrUsrgrp usrgrp = new UsrUsrgrp();
					usrgrp.setUserid(userid);
					usrgrp.setGrpcode(grpcodes[i]);
					sysUserGroupService.saveSysUserGroup(usrgrp);
					SysLog.info(request, "save user " + userid + "to group "
							+ usrgrp + " successfully.");
				}
			// this.saveMessages(request,msgs);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updateok"), "um", null);
			if("save".equals(action)){
				msgBox.setButtonType(MsgBox.BUTTON_OK);
			}else{
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
			return "msgBox";
		} catch (Exception e) {
			SysLog.error("error in (SysUserGroupAction.java-saveSysUserGroup())");
			SysLog.error(request, "failed to save user" + userid
					+ "'s group information.");
			SysLog.error(e);
			e.printStackTrace();
			// this.saveMessages(request,msgs);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}

	}

	/**
	 * by wjie5 2014-4-25 根据用户id查询所在组信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getGroupNameByUserName() throws Exception {
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		List list = sysUserGroupService.getUserGropByUserId(userid);

		JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.domain.UsrGroup");
		JSONArray jsonObj = jsonUtil.toJSON(list, null);
		out.print(jsonObj);
		out.flush();
		out.close();
		return null;
	}

	/**By wjie5 2014-4-28
	 * 查询该组并且属于deptvalue部门的用户列表
	 * @param userid
	 * @return
	 */
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String deptvalue = request.getParameter("deptvalue");
		String grpovalue = request.getParameter("grpvalue");
		String deptValue = request.getParameter("deptValue");
		String groupValue = request.getParameter("groupValue");
		List<Object[]> list=sysUserGroupService.findUserListByGrpDept(grpovalue, deptvalue,groupValue,deptValue);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	public SysUserGroupService getsysUserGroupService() {
		return sysUserGroupService;
	}

	public void setsysUserGroupService(SysUserGroupService sysUserGroupService) {
		this.sysUserGroupService = sysUserGroupService;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String[] getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String[] grpcode) {
		this.grpcode = grpcode;
	}

	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}

	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}

	public String getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(String usergroups) {
		this.usergroups = usergroups;
	}
}
