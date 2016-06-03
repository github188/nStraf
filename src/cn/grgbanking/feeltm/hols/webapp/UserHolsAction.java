package cn.grgbanking.feeltm.hols.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.hols.domain.UserHols;
import cn.grgbanking.feeltm.hols.service.UserHolsService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author lhyan 2014-5-12
 */
@SuppressWarnings("serial")
public class UserHolsAction extends BaseAction{
	
	@Autowired
	private UserHolsService service;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	
	@Autowired
	private ProjectService projectService;
	
	private UserHols hols;
	
	private int type;
	
	private String grpcode;
	private String deptcode;
	private String userids;
	private double yeartime;
	private double deferredtime;
	private String holsids;
	private double yearholsTime[];
	private double deferredTime[];
	private String userid[];
	private String holsid[];
	private String form;
	
	public void refresh(){
		MsgBox msgBox;
		try {
			list();
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-list())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
	}
	
	/**
	 * 列表页面
	 * @return
	 */
	public String list(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try{
			UserHols hols = new UserHols();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if(deptcode!=null && !"".equals(deptcode)){
				hols.setDeptName(deptcode);
			}
			if(grpcode!=null && !"".equals(grpcode)){
				hols.setGroupName(grpcode);
			}
			if(userids!=null && !"".equals(userids)){
				hols.setUserid(userids);
			}
			Page page = service.findUserholsPage(hols,userModel,pageNum,pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = page.getQueryResult();
			for (int i = 0; i < list.size(); i++) {
				UserHols userHols = (UserHols) list.get(i);
				String prjName = projectService.getProjectNameByUserid(userHols.getUserid());
				userHols.setGroupName(prjName);
			}
			if(form!=null && form.equals("refresh")){
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil util = new JSONUtil("cn.grgbanking.feeltm.hols.domain.UserHols");
				JSONArray array = util.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", array);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}
			ActionContext.getContext().put("userhols", list);
			return "list";
			
		}catch(Exception e){
			SysLog.error(request, "error in (UserHolsAction.java-list())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 跳往新增页面
	 * @return
	 */
	public String addPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				//不是管理员或hr
				msgBox = new MsgBox(request, getText("user.hols.cannotadd"), "hols");
				return "msgBox";
			}
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			request.setAttribute("deptMap", deptMap);
			List<Project> grouos = projectService.listAllGroup();
			request.setAttribute("usrGroups", grouos);
			return "add";
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 2014-5-13
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			List<UserHols> holsList = new ArrayList<UserHols>();
			List<SysUser> listUsers = service.getNotExistUsers(userids,type,deptcode,grpcode);
			String failMsg = userids;
			if(listUsers!=null&&listUsers.size()>0){
				for(int i = 0;i<listUsers.size();i++){
					SysUser user = listUsers.get(i);
					UserHols hols = new UserHols();
					if(type ==0 ){
						hols.setYearholsTime(yeartime);
						hols.setDeferredTime(deferredtime);
						failMsg = failMsg.replaceAll(user.getUserid()+",","");
					}
					hols.setUserid(user.getUserid());
					hols.setUsername(user.getUsername());
					hols.setDeptName(staffInfoService.getDeptNameValueByKey(user.getDeptName()));
					hols.setGroupName(projectService.getProjectNameByUserid(user.getUserid()));
					holsList.add(hols);
				}
				service.addHolsList(holsList);
				if(failMsg!=null&&!failMsg.equals("")) {
					failMsg = failMsg+"记录已存在，不可重复添加；\n"+userids.replaceAll(failMsg,"")+"新增成功!";
					msgBox = new MsgBox(request,failMsg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, userModel.getUserid());
					SysLog.info(request, userModel.getUserid()+"add userhols");
					return "msgBox";
				}
			}else{
				msgBox = new MsgBox(request, getText("user.hols.addfaild")+"\n所选员工记录已存在，不可重复添加!", "hols");
				return "msgBox";
			}
			/*
			switch (type) {
			case 0:
				//个人添加 
				if(null != userids && !"".equals(userids)){
					String failMsg = "";
					String users[] = userids.split(",");
					List<Object[]> list = service.getExistUsers(userids);
					for(int i=0;i<list.size();i++){
						SysUser user = staffInfoService.findUserByUserid(String.valueOf(list.get(i)[0]));
						UserHols hols = new UserHols();
						hols.setYearholsTime(yeartime);
						hols.setDeferredTime(deferredtime);
						hols.setUserid(String.valueOf(list.get(i)[0]));
						hols.setUsername(user.getUsername());
						hols.setDeptName(staffInfoService.getDeptNameValueByKey(user.getDeptName()));
						hols.setGroupName(staffInfoService.getGroupNameByUserId(user.getUserid()));
						holsList.add(hols);
					}
					/*
					for(int i = 0;i<users.length;i++){
						if(!service.existUser(users[i])){
							SysUser user = staffInfoService.findUserByUserid(users[i]);
							UserHols hols = new UserHols();
							hols.setYearholsTime(yeartime);
							hols.setDeferredTime(deferredtime);
							hols.setUserid(users[i]);
							hols.setUsername(user.getUsername());
							hols.setDeptName(staffInfoService.getDeptNameValueByUserId(user.getUserid()));
							hols.setGroupName(staffInfoService.getGroupNameByUserId(user.getUserid()));
							holsList.add(hols);
						} else {
							SysUser user = staffInfoService.findUserByUserid(users[i]);
							failMsg += user.getUsername()+" ";
						}
					}*//*
					
					service.addHolsList(holsList);
					if(holsList.size() != users.length ) {
						for(int j=0;j<list.size();j++){
							userids.replaceAll("",String.valueOf(list.get(j)[0])+",");
						}
						failMsg = userids+"记录已经存在，增加失败！";
						msgBox = new MsgBox(request,failMsg);
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
				}else{
					msgBox = new MsgBox(request, getText("user.hols.addfaild"), "hols");
					return "msgBox";
				}
				break;
			case 1:
				//按部门添加
				List<SysUser> sysUsers = staffInfoService.getStaffByDeptKey(deptcode);
				//List<Object[]> list = service.getExistUsersByUser(sysUsers);
				for(SysUser s:sysUsers){
					UserHols hols = new UserHols();
					if(!service.existUser(s.getUserid())){
						hols.setUsername(s.getUsername());
						hols.setDeptName(staffInfoService.getDeptNameValueByUserId(s.getUserid()));
						hols.setGroupName(staffInfoService.getGroupNameByUserId(s.getUserid()));
						hols.setUserid(s.getUserid());
						holsList.add(hols);
					}
				}
				service.addHolsList(holsList);
				if(holsList.size() != sysUsers.size()) { 
					msgBox = new MsgBox(request,"部分员工记录已生成，请检查是否正确生成!");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				break;
			case 2:
				//按组别添加
				List<SysUser> users = sysUserGroupService.findUserByProject(grpcode);
				for(SysUser s:users){
					UserHols hols = new UserHols();
					if(!service.existUser(s.getUserid())){
						hols.setUsername(s.getUsername());
						hols.setDeptName(staffInfoService.getDeptNameValueByUserId(s.getUserid()));
						hols.setGroupName(staffInfoService.getGroupNameByUserId(s.getUserid()));
						hols.setUserid(s.getUserid());
						holsList.add(hols);
					}
				}
				service.addHolsList(holsList);
				if(holsList.size() != users.size()) { 
					msgBox = new MsgBox(request,"部分员工记录已生成，请检查是否正确生成!");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				break;
			default:
				break;
			}
			*/
			SysLog.operLog(request, Constants.OPER_ADD_VALUE, userModel.getUserid());
			SysLog.info(request, userModel.getUserid()+"add userhols");
			msgBox = new MsgBox(request, "成功新增"+listUsers.size()+"条记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("addSome.ok"));
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}
		return "msgBox";
	}
	
	public String modifyPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				//不是管理员或hr
				msgBox = new MsgBox(request, getText("user.hols.notrole"), "hols");
				return "msgBox";
			}
			if(holsids!=null && !"".equals(holsids)){
				String[] ids = holsids.split(",");
				List<UserHols> userHols = service.findByIds(ids);
				for(UserHols h:userHols){
					h.setUsername(staffInfoService.getUsernameById(h.getUserid()));
				}
				request.setAttribute("userhols", userHols);
				return "modify";
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String delete(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				//不是管理员或hr
				msgBox = new MsgBox(request, getText("user.hols.notrole"), "hols");
				return "msgBox";
			}
			if(holsids!=null && !"".equals(holsids)){
				holsid = holsids.split(",");
				List<UserHols> holslist = service.findByIds(holsid);
				for(UserHols h:holslist){
					h.setFlag(1);
					service.updateHols(h);
				}
				msgBox = new MsgBox(request, getText("user.hols.deletesuccess"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-delete())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 清空年假
	 * @return
	 */
	public String resetYearTime(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				//不是管理员或hr
				msgBox = new MsgBox(request, getText("user.hols.notrole"), "hols");
				return "msgBox";
			}
			if(holsids!=null && !"".equals(holsids)){
				holsid = holsids.split(",");
				List<UserHols> holslist = service.findByIds(holsid);
				for(UserHols h:holslist){
					h.setYearholsTime(0.0);
					service.updateHols(h);
				}
				msgBox = new MsgBox(request, getText("user.hols.resetyeartime"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-resetYearTime())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 清空调休
	 * @return
	 */
	public String resetFreeTime(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				//不是管理员或hr
				msgBox = new MsgBox(request, getText("user.hols.notrole"), "hols");
				return "msgBox";
			}
			if(holsids!=null && !"".equals(holsids)){
				holsid = holsids.split(",");
				List<UserHols> holslist = service.findByIds(holsid);
				for(UserHols h:holslist){
					h.setDeferredTime(0.0);
					service.updateHols(h);
				}
				msgBox = new MsgBox(request, getText("user.hols.resetfreetime"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-resetFreeTime())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	/**
	 * 修改
	 * @return
	 */
	public String modify(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(holsid!=null && holsid.length>0){
				for(int i =0;i<holsid.length;i++){
					UserHols h = service.findByidAndUser(holsid[i],userid[i]);
					if(h!=null){
						h.setYearholsTime(yearholsTime[i]);
						h.setDeferredTime(deferredTime[i]);
						service.updateHols(h);
					}
				}
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, userModel.getUserid());
				SysLog.info(userModel.getUserid()+"change UserHols "+holsid.length);
				msgBox = new MsgBox(request, getText("user.hols.update"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
			
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	public UserHols getHols() {
		return hols;
	}
	public void setHols(UserHols hols) {
		this.hols = hols;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getUserids() {
		return userids;
	}

	public void setUserids(String userids) {
		this.userids = userids;
	}

	
	public double getYeartime() {
		return yeartime;
	}

	public void setYeartime(double yeartime) {
		this.yeartime = yeartime;
	}

	public double getDeferredtime() {
		return deferredtime;
	}

	public void setDeferredtime(double deferredtime) {
		this.deferredtime = deferredtime;
	}

	public String getHolsids() {
		return holsids;
	}
	public void setHolsids(String holsids) {
		this.holsids = holsids;
	}
	public String[] getUserid() {
		return userid;
	}



	public double[] getYearholsTime() {
		return yearholsTime;
	}

	public void setYearholsTime(double[] yearholsTime) {
		this.yearholsTime = yearholsTime;
	}

	public double[] getDeferredTime() {
		return deferredTime;
	}

	public void setDeferredTime(double[] deferredTime) {
		this.deferredTime = deferredTime;
	}

	public void setUserid(String[] userid) {
		this.userid = userid;
	}
	public String[] getHolsid() {
		return holsid;
	}
	public void setHolsid(String[] holsid) {
		this.holsid = holsid;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
}
