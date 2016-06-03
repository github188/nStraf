package cn.grgbanking.feeltm.common;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

public class CommonAction extends BaseAction{
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;

	/**lhy 2014-4-30
	 * 人员选择
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String select(){
		//部门与员工
		Map deptmaMap = BusnDataDir.getMapKeyValue("staffManager.department");
		Set<String> keySet = deptmaMap.keySet();
		Map<String,List<SysUser>> deptUser = new HashMap<String, List<SysUser>>();
		for(Iterator it = keySet.iterator();it.hasNext();){
			String key = (String) it.next();
			List<SysUser> users = staffInfoService.getStaffByDeptKey(key);
			deptUser.put(key, users);
		}
		request.setAttribute("deptUser", deptUser);
		request.setAttribute("deptmaMap", deptmaMap);
		//组别与员工
		List<Project> usrGroups = sysUserGroupService.getAllProjectList();
		Map<String, List<SysUser>> grpUser = new HashMap<String, List<SysUser>>();
		for(Project u:usrGroups){
			List<SysUser> users = sysUserGroupService.findUserByProject(u.getId());
			grpUser.put(u.getId(), users);
		}
		request.setAttribute("grpUser", grpUser);
		request.setAttribute("usrGroups", usrGroups);
		return "select";
	}
	
	
	public String selectOrSearchUser(){
		try {
			String see = request.getParameter("see");
			String hidden = request.getParameter("hidden");
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			request.setAttribute("deptMap", deptMap);
			//已经存在的用户列表
			String[] userIds=null;
			if(StringUtils.isNotBlank(request.getParameter("userIdInList"))){
				userIds=request.getParameter("userIdInList").split(",");
			}
			if(userIds!=null&&userIds.length!=0){
				List<SysUser> userInList=staffInfoService.findUserByUserIds(userIds);
				request.setAttribute("userInList",userInList);
			}
			
			List<SysUser> userlist= staffInfoService.listUserExpectTheseUsers(userIds);
			request.setAttribute("userlist",userlist);
			request.setAttribute("seename", see);
			request.setAttribute("seeid", hidden);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request,
					"failed:java-SysUserGroupAction-findGroupUser()");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "selectOrSearchUser";
	}
	
	public String getSearchUser(){
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String username = request.getParameter("username");

			//过滤掉，不需要查询的用户列表
			String[] userIds=null;
			if(StringUtils.isNotBlank(request.getParameter("userIdInList"))){
				userIds=request.getParameter("userIdInList").split(",");
			}
			
			List<SysUser> notinusers = staffInfoService.queryUserByDeptAndUsernameExpectTheseUsers(deptname,username,userIds);
			List<String[]> notinidname = new ArrayList<String[]>();
			if(notinusers!=null && notinusers.size()>0){
				for(SysUser user:notinusers){
					String[] str = new String[2];
					str[0] = user.getUserid();
					str[1] = user.getUsername();
					notinidname.add(str);
				}
			}
			
			//查看已经存在的用户列表
		
			Map map = new HashMap();
			map.put("notinidname", notinidname);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 向ajax请求返回数据
	 * 
	 * @param str
	 */
	private void ajaxPrint(String str) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
