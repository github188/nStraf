package cn.grgbanking.feeltm.project.webapp;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.CommonData;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.loglistener.service.LogListenerService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.ProjectAttendance;
import cn.grgbanking.feeltm.project.domain.ProjectResourcePlan;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.project.util.UserProjectUpdateUtil;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.GeocodeAddressUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * @author lhyan3 2014年6月12日
 */
@SuppressWarnings("serial")
public class ProjectAction extends BaseAction{
	@Autowired
	private ProjectService service;
	@Autowired
	private UserProjectUpdateUtil userProjectUpdateUtil;
	@Autowired
	private SysUserGroupService sysUserGroupService;
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	private LogListenerService listenerService;

	public static String LATITUDE = "latitude";//纬度
	public static String LONGITUDE = "longitude";//经度
	
	private String form;

	private String groupId;

	private Project group;

	private String pcode;

	private String usergroups;

	private Project project;
	
	private UserProject userProject;

	private String projectPath = "systemConfig.projectname";
	
	private String userids;
	private String usernames;
	private String projectId;
	
	private Date planStartTime;
	private Date planEndTime;
	private Date factStartTime;
	private Date factEndTime;
	
	private Date[] planStartTimes;
	private Date[] planEndTimes;
	private String[] useridss;
	private String[] usernamess;
	private Date[] factStartTimes;
	private Date[] factEndTimes;
	
	private List<String> attendanceEntryTimeList;
	private List<String> attendanceExitTimeList;
	private List<String> attendanceTypeList;
	private List<String> attendanceAddressList;
	private List<String> planStartTimeList;
	private List<String> factStartTimeList;
	private List<String> planEndTimeList;
	private List<String> factEndTimeList;
	private List<String> useridList;
	private List<String> usernameList;
	private List<String> projectDutyList;
	private List<String> projectRoleList;
	
	/**
	 * 查看项目详情
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public String view(){
		String projectId = request.getParameter("projectId");
		Project project = service.getProjectById(projectId);
		project=showUserProjectDesc(showDesc(project));
		request.setAttribute("project", project);
		return "view";
	}
	
	
	public List<Object> showDesc(List<Object> projects){
		if(projects!=null){
			for (int i = 0; i < projects.size(); i++) {
				Project project=(Project)projects.get(i);
				project=showDesc(project);
				projects.set(i, project);
			}
			
		}
		return projects;
	}
	
	public Project showDesc(Project project){
		project.setMembers(project.getUserProjects()==null?0:project.getUserProjects().size());
		String key=project.getProjectType();
		Map map=BusnDataDir.getMapKeyValue("projectMonitor.projectType");
		project.setProjectType((String)map.get(key));
		//获取几个数据字典对象
		Map attendanceTypeMap=BusnDataDir.getMapKeyValue("projectMonitor.attendanceType");
		Map projectRoleMap=BusnDataDir.getMapKeyValue("projectMonitor.projectRole");
		Map projectDutyMap=BusnDataDir.getMapKeyValue("projectMonitor.projectDuty");
		Map projectIsEndMap=BusnDataDir.getMapKeyValue("projectManage.isEnd");
		Map customerMap=BusnDataDir.getMapKeyValue("projectManage.customer");
		String isEndVal = BusnDataDir.getValue(projectIsEndMap, project.getIsEnd());
		String customerVal = BusnDataDir.getValue(customerMap, project.getCustomer());
		project.setIsEnd(isEndVal);
		project.setCustomer(customerVal);
		if(project.getAttendances()!=null){
			for(ProjectAttendance att:project.getAttendances()){
				att.setAttendanceType((String)attendanceTypeMap.get(att.getAttendanceType()));
			}
		}
		if(project.getProjectResourcePlan()!=null){
			for(ProjectResourcePlan rp:project.getProjectResourcePlan()){
				rp.setProjectRole((String)projectRoleMap.get(rp.getProjectRole()));
				rp.setProjectDuty((String)projectDutyMap.get(rp.getProjectDuty()));
			}
		}
		return project;
	}
	public Project showUserProjectDesc(Project project){
		//获取几个数据字典对象
		Map attendanceTypeMap=BusnDataDir.getMapKeyValue("projectMonitor.attendanceType");
		Map projectRoleMap=BusnDataDir.getMapKeyValue("projectMonitor.projectRole");
		Map projectDutyMap=BusnDataDir.getMapKeyValue("projectMonitor.projectDuty");
		Map deptMap=BusnDataDir.getMapKeyValue("staffManager.department");
		if(project.getUserProjects()!=null){
			for(UserProject up:project.getUserProjects()){
				up.setProjectRole((String)projectRoleMap.get(up.getProjectRole()));
				up.setProjectDuty((String)projectDutyMap.get(up.getProjectDuty()));
				up.setDeptName((String)deptMap.get(up.getDeptName()));
			}
		}
		return project;
	}
	
	/**
	 * 跳往修改页面
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	@SuppressWarnings("rawtypes")
	public String modifyPage(){
		MsgBox msgBox;
		try {
			Project project = service.getProjectById(projectId);
			project=showUserProjectDesc(project);
			List<SysDatadir> projectTypeList=BusnDataDir.getObjectListInOrder("projectMonitor.projectType");
			List<SysDatadir> attendanceTypeList=BusnDataDir.getObjectListInOrder("projectMonitor.attendanceType");
			List<SysDatadir> projectIsEndList=BusnDataDir.getObjectListInOrder("projectManage.isEnd");
			List<SysDatadir> customerTypeList=BusnDataDir.getObjectListInOrder("projectManage.customer");
			List projectRole=BusnDataDir.getObjectListInOrder("projectMonitor.projectRole");
			List projectDuty=BusnDataDir.getObjectListInOrder("projectMonitor.projectDuty");
			request.setAttribute("projectRole", projectRole);
			request.setAttribute("projectDuty", projectDuty);
			request.setAttribute("customerType", customerTypeList);
			request.setAttribute("project", project);
			request.setAttribute("projectType", projectTypeList);
			request.setAttribute("attendanceType", attendanceTypeList);
			request.setAttribute("projectIsEndType", projectIsEndList);
			//新建时没有人员计划分配表，默认添加一个空白的
			List<ProjectResourcePlan> resourcePlanList= project.getProjectResourcePlan();
			if(resourcePlanList==null||resourcePlanList.size()<=0){
				ProjectResourcePlan rp=new ProjectResourcePlan();
				rp.setPlanStartTime(project.getPlanStartTime());
				rp.setPlanEndTime(project.getPlanEndTime());
				rp.setFactStartTime(project.getFactStartTime()==null?project.getPlanStartTime():project.getFactStartTime());
				rp.setFactEndTime(project.getFactEndTime()==null?project.getPlanEndTime():project.getFactEndTime());
				resourcePlanList=new ArrayList<ProjectResourcePlan>();
				resourcePlanList.add(rp);
				project.setProjectResourcePlan(resourcePlanList);
			}
			return "modify";
		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-modifyPage())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		
	}
	
	
	/**保存人员分配
	 * wtjiao 2014年9月22日 上午8:19:46
	 * @return
	 */
	public String saveUserProject(){
		MsgBox msgBox;
		try {
			project = service.getProjectById(projectId);
			List<ProjectResourcePlan> plans=generateResourcePlanList(project);
			//删除现有的计划分配
			service.deleteResourcePlan(project);
			//保存新计划
			service.saveResourcePlans(plans);
			
			//将project中的计划更新为新计划
			project.setProjectResourcePlan(plans);
			
			//更新现有的项目组用户
			userProjectUpdateUtil.registerRequest(request).synchronizeUserProject(project);
			
		} catch (Exception e) {
			SysLog.error(request,"failed:java-ProjectAction-saveUserProject()");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("人员分配成功"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		msgBox = new MsgBox(request, getText("人员分配成功"));
		msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		return "msgBox";
	}
	
	
	private List<ProjectResourcePlan> generateResourcePlanList(Project project2) {
		List<ProjectResourcePlan> planList=new ArrayList<ProjectResourcePlan>();
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(useridList!=null){
				for(int i=0;i<useridList.size();i++){
					String[] finalUserIds=useridList.get(i).split(",");
					String[] finalUsernames=usernameList.get(i).split(",");
					for(int j=0;j<finalUserIds.length;j++){
						ProjectResourcePlan plan=new ProjectResourcePlan();
						plan.setProject(project2);
						if(((String)factEndTimeList.get(i)).equals("")){
							plan.setFactEndTime(null);
						}else{
							plan.setFactEndTime(sdf.parse((String)factEndTimeList.get(i)));
						}
						if(((String)factStartTimeList.get(i)).equals("")){
							plan.setFactStartTime(null);
						}else{
							plan.setFactStartTime(sdf.parse((String)factStartTimeList.get(i)));
						}
						if(((String)planEndTimeList.get(i)).equals("")){
							plan.setPlanEndTime(null);
						}else{
							plan.setPlanEndTime(sdf.parse((String)planEndTimeList.get(i)));
						}
						if(((String)planStartTimeList.get(i)).equals("")){
							plan.setPlanStartTime(null);
						}else{
							plan.setPlanStartTime(sdf.parse((String)planStartTimeList.get(i)));
						}
						plan.setProjectDuty((String)projectDutyList.get(i));
						plan.setProjectRole(projectRoleList.get(i));
						plan.setUserid(finalUserIds[j]);
						plan.setUsername(finalUsernames[j]);
						plan.setUpdateTime(new Date());
						plan.setUpdateuserId(((UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY)).getUserid());
						plan.setUpdateUsername(((UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY)).getUsername());
						planList.add(plan);	
					}
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return planList;
	}

	/**
	 * 人员分配页面
	 * @return
	 * lhyan3
	 * 2014年6月26日
	 */
	@SuppressWarnings("unused")
	public String distribution(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String projectId = request.getParameter("projectId");
		project = service.getProjectById(projectId);
		project = showUserProjectDesc(project);
		List projectRole=BusnDataDir.getObjectListInOrder("projectMonitor.projectRole");
		List projectDuty=BusnDataDir.getObjectListInOrder("projectMonitor.projectDuty");
		request.setAttribute("projectRole", projectRole);
		request.setAttribute("projectDuty", projectDuty);
		
		//新建时没有人员计划分配表，默认添加一个空白的
		List<ProjectResourcePlan> resourcePlanList= project.getProjectResourcePlan();
		if(resourcePlanList==null||resourcePlanList.size()<=0){
			ProjectResourcePlan rp=new ProjectResourcePlan();
			rp.setPlanStartTime(project.getPlanStartTime());
			rp.setPlanEndTime(project.getPlanEndTime());
			rp.setFactStartTime(project.getFactStartTime()==null?project.getPlanStartTime():project.getFactStartTime());
			rp.setFactEndTime(project.getFactEndTime()==null?project.getPlanEndTime():project.getFactEndTime());
			resourcePlanList=new ArrayList<ProjectResourcePlan>();
			resourcePlanList.add(rp);
			project.setProjectResourcePlan(resourcePlanList);
		}
		request.setAttribute("project", project);
		return "distribution";
	}
	
	/**查看人员计划分配
	 * wtjiao 2014年9月4日 下午4:17:41
	 * @return
	 */
	@SuppressWarnings("unused")
	public String viewDistribution(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String projectId = request.getParameter("projectId");
		project = service.getProjectById(projectId);
		project=showUserProjectDesc(showDesc(project));
		Map projectRoleMap=BusnDataDir.getMapKeyValue("projectMonitor.projectRole");
		Map projectDutyMap=BusnDataDir.getMapKeyValue("projectMonitor.projectDuty");
		request.setAttribute("projectRole", projectRoleMap);
		request.setAttribute("projectDuty", projectDutyMap);
		
		request.setAttribute("project", project);
		return "viewDistribution";
	}
	
	/**
	 * 项目经理选择页面
	 * 
	 * @return 
	 * lhyan3 
	 * 2014年6月13日
	 */
	public String findAllUser() {
		try {
			// 获取页面传过来的参数
			List<SysUser> userlist = new ArrayList<SysUser>();
			String userid = "";
			String deptId = "";
			if (projectId != null && !"".equals(projectId)) {
				project = service.getProjectById(projectId);
				if (project != null) {
					userid = project.getProManagerId();
				}
			}
			// 获取除该项目经理以外所有用户选择
			userlist = service.getAllNameIds(userid, deptId,"");	
			List<SysUser> userListNotNull=new ArrayList<SysUser>();
			if(userlist!=null){
				for(SysUser u:userlist){
					if(u!=null){
						userListNotNull.add(u);
					}
				}
			}
			request.setAttribute("project", project);
			request.setAttribute("userlist", userListNotNull);
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
		return "selectmanager";
	}

	/**
	 * 分配人员的页面
	 * 
	 * @return 
	 * lhyan3 
	 * 2014年6月26日
	 */
	public String getAllUserByProject() {		
		String projectId = request.getParameter("projectId");
		String indexvalue = request.getParameter("indexvalue");
		try {
			if (projectId != null && !"".equals(projectId)) {
				// 项目中人员
				List<SysUser> inusers = new ArrayList<SysUser>();
				if(userids!=null && !"".equals(userids)){
					String[] userid = userids.split(",");
					String[] username = usernames.split(",");
					if(userid.length>0){
						for(int i=0;i<userid.length;i++){
							SysUser user = new SysUser();
							user.setUserid(userid[i]);
							user.setUsername(username[i]);
							inusers.add(user);
						}
					}
				}
				// 非项目中人员
				List<SysUser> notinusers = service.getNotInProjectByProject(projectId,"","");
				
				request.setAttribute("inusers", inusers);
				request.setAttribute("notinusers", notinusers); 
				Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
				request.setAttribute("deptMap", deptMap);
				request.setAttribute("projectId", projectId);
				request.setAttribute("indexvalue", indexvalue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "selectuser";
	}

	/**
	 * 根据部门选择的人员分配选择页面 
	 * lhyan3 
	 * 2014年6月5日
	 */
	@SuppressWarnings("rawtypes")
	public void getUserProjectByDeptName() {
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String projectId = request.getParameter("projectId");
			String username = request.getParameter("username");
			//List<String[]> inidname = new ArrayList<String[]>();
			List<String[]> notinidname = new ArrayList<String[]>();
			if (projectId != null && !"".equals(projectId)) {
				// 项目中人员
				//List<SysUser> inusers = service.getInProjectByProject(projectId,deptname);
				// 非项目中人员
				List<SysUser> notinusers = service.getUserByIdOrName(projectId,deptname,username);
				/*if (inusers != null && inusers.size()>0) {
					for(SysUser u:inusers){
						String[] str = new String[2];
						str[0] = u.getUserid();
						str[1] = u.getUsername();
						inidname.add(str);
					}
				}*/
				if(notinusers!=null && notinusers.size()>0){
					for(SysUser user:notinusers){
						String[] str = new String[2];
						if(user!=null){
							str[0] = user.getUserid();
							str[1] = user.getUsername();
							notinidname.add(str);
						}
					}
				}
			}
			Map map = new HashMap();
			//map.put("inidname", inidname);
			map.put("notinidname", notinidname);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据部门选择的项目经理选择页面
	 *  lhyan3 
	 *  2014年6月5日
	 */
	@SuppressWarnings("rawtypes")
	public void getStaffByDeptName() {
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String projectId = request.getParameter("projectId");
			String username = request.getParameter("username");
			String userid = "";
			List<String> list = new ArrayList<String>();
			List<String[]> idname = new ArrayList();
			if (projectId != null && !"".equals(projectId)) {
				project = service.getProjectById(projectId);
				if (project != null) {
					userid = project.getProManagerId();
					list.add(project.getProManagerId());
					list.add(project.getProManager());
				}
			}
			// 不在该组的用户
			List<SysUser> userlist = service.getAllNameIds(userid,
					deptname,username);
			if (userlist != null && userlist.size() > 0) {
				for (SysUser u : userlist) {
					String[] str = new String[2];
					if(u!=null){
						str[0] = u.getUserid();
						str[1] = u.getUsername();
						idname.add(str);
					}
				}
			}
			Map map = new HashMap();
			map.put("userlist", idname);
			map.put("projectGroup", list);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 部门项目级联用到
	 * @return
	 * @throws Exception
	 *             lhyan3 2014年6月13日
	 */
	public String queryNames() throws Exception {
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String deptvalue = request.getParameter("deptvalue");
		String grpovalue = request.getParameter("grpvalue");
		String deptValue = request.getParameter("deptValue");
		String groupValue = request.getParameter("groupValue");
		List<Object[]> list = service.findUserListByGrpDept(grpovalue,
				deptvalue, groupValue, deptValue);
		JSONArray jsonArray = new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 部门项目级联用到
	 * @return
	 * @throws Exception
	 *             lhyan3 2014年6月13日
	 */
	@SuppressWarnings("unchecked")
	public String getGroupNameByUserName() throws Exception {
		String userid = request.getParameter("userid");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out = response.getWriter();
		List<Object> list = service.getProjectByUserId(userid);
		JSONUtil jsonUtil = new JSONUtil(
				"cn.grgbanking.feeltm.project.domain.Project");
		JSONArray jsonObj = jsonUtil.toJSON(list, null);
		out.print(jsonObj);
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 删除项目
	 * @return lhyan3 2014年6月12日
	 */
	public String delete() {
		MsgBox msgBox;
		try {
			String[] prjIds=projectId.split(",");
			for(String id:prjIds){
				Project g = service.getProjectById(id);
				if(dayLogService.hasDayLogList(id)){
					msgBox = new MsgBox(request, "删除\""+g.getName()+"\"失败，查询到该项目已经关联日志!");
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
					return "msgBox";
				}
				service.delete(g);
				
				//删除日志监听中对该项目的监听 20150307 wtjiao
				listenerService.deleteListenerMonitorByProjectId(g.getId());
				//删除项目信息时，同时删除权限组别中项目经理对应的用户信息
				sysUserGroupService.deleteInfoByRoleUserid(g.getProManagerId(), "groupManager");
				//同时删除该项目人日统计信息
				CommonData commonData=(CommonData)BaseApplicationContext.getAppContext().getBean("commonData"); 
				commonData.removePersonDayByProjectId(g.getId());
			}
			msgBox = new MsgBox(request, getText("删除成功"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("删除成功"));
		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-delete())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	/**
	 * lhyan3 2014年6月12日
	 */
	public void refresh() {
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	public String modify() {
		MsgBox msgBox;
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			project.setUpdateTime(new Date());
			project.setUpdateuserId(userModel.getUserid());
			project.setUpdateUsername(userModel.getUsername());
			//project.setUserProjects(service.getUserProjectsByProjectId(project.getId()));
			//如果需要依赖hibernate的级联去更新签到地点，就必须getAttendance而不是new Attendance,这里比较麻烦，就不通过hibernate来做了
			//hibernate对new出来的对象(id=null)，都直接进行保存，需要在保存前，删除原有的对象
			service.deleteAttendance(project);
			service.update(project);
			
			List<ProjectResourcePlan> plans=generateResourcePlanList(project);
			//删除计划
			service.deleteResourcePlan(project);
			//保存新计划
			service.saveResourcePlans(plans);
			//删除考勤地点
			service.deleteAttendance(project);
			//保存新地点
			service.saveAttendances(generateAttendance());
			
			////根据计划更新现有的项目组用户
			project.setProjectResourcePlan(plans);
			userProjectUpdateUtil.registerRequest(request).synchronizeUserProject(project);
			
			//在日志监听中加入该项目 20150307 wtjiao
			listenerService.saveOrUpdateListenerMonitorByProject(project.getId());
			//修改项目信息管理数据时，同步一次权限组别中的项目经理数据20150204ling.tu
			synchronizeProjectManage();

			msgBox = new MsgBox(request, getText("修改成功"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("修改成功"));

		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-modify())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	public String toAdd(){
		MsgBox msgBox;
		try {
			List<SysDatadir> projectTypeList=BusnDataDir.getObjectListInOrder("projectMonitor.projectType");
			List<SysDatadir> attendanceTypeList=BusnDataDir.getObjectListInOrder("projectMonitor.attendanceType");
			List<SysDatadir> customerTypeList=BusnDataDir.getObjectListInOrder("projectManage.customer");
			List<SysDatadir> projectIsEndList=BusnDataDir.getObjectListInOrder("projectManage.isEnd");
			request.setAttribute("projectType", projectTypeList);
			request.setAttribute("attendanceType", attendanceTypeList);
			request.setAttribute("customerType", customerTypeList);
			request.setAttribute("projectIsEndType", projectIsEndList);
			return "add";
		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-toAdd())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * @return lhyan3 2014年6月12日
	 */
	public String add() {
		MsgBox msgBox;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			project.setUpdateTime(new Date());
			project.setUpdateuserId(userModel.getUserid());
			project.setUpdateUsername(userModel.getUsername());
			project.setAttendances(generateAttendance());
			String projectId=service.saveOrUpdate(project);
			
			/*//保存考勤地点
			if(project.getAttendances()!=null){
				List<ProjectAttendance> list=new ArrayList<ProjectAttendance>();
				for(ProjectAttendance pAtt:project.getAttendances()){
					pAtt.getProject().setId(projectId);
					list.add(pAtt);
				}
				service.saveAttendances(list);
			}*/
			
			//在日志监听中加入该项目 20150307 wtjiao
			listenerService.saveOrUpdateListenerMonitorByProject(projectId);
			//新增项目信息管理数据时，同步一次权限组别中的项目经理数据20150204ling.tu
			synchronizeProjectManage();
			
			msgBox = new MsgBox(request, getText("添加成功"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("添加成功"));
		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-add())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	private List<ProjectAttendance> generateAttendance() {
		List<ProjectAttendance> atts=new ArrayList<ProjectAttendance>();
		try{
			if(attendanceTypeList!=null){
				SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
				for(int i=0;i<attendanceTypeList.size();i++){
					ProjectAttendance pa=new ProjectAttendance();
					pa.setAttendanceType(attendanceTypeList.get(i));
					pa.setEntryTime(format.parse(attendanceEntryTimeList.get(i)));
					pa.setExitTime(format.parse(attendanceExitTimeList.get(i)));
					pa.setSignPlace(attendanceAddressList.get(i));
					if(attendanceAddressList.get(i)!=null && !"".equals(attendanceAddressList.get(i))){
						double[] lanlng = GeocodeAddressUtil.getLanLonByAddress(attendanceAddressList.get(i));
						pa.setLatitude(lanlng[0]);
						pa.setLongitude(lanlng[1]);
					}
					pa.setProject(project);
					atts.add(pa);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return atts;
	}
	public void getLatitudeAndLongitude(){
		String address = request.getParameter("address");
		net.sf.json.JSONObject responseObj = new net.sf.json.JSONObject();
		double[] lanlng = GeocodeAddressUtil.getLanLonByAddress(address);
		responseObj.put(LATITUDE, lanlng[0]);
		responseObj.put(LONGITUDE, lanlng[1]);
		ajaxPrint(responseObj.toString());
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

	/**
	 * @return lhyan3 2014年6月12日
	 */
	@SuppressWarnings("unchecked")
	public String list() {
		MsgBox msgBox;
		try {
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			
			String prjName = request.getParameter("projectName");
			String isEndValue = request.getParameter("isEndValue");
			
			Page page = service.getPagebyNum(pageNum, pageSize,prjName,isEndValue);
			request.setAttribute("currPage", page);
			List<Object> list = page.getQueryResult();
			list=showDesc(list);
			if (form != null && form.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil util = new JSONUtil(
						"cn.grgbanking.feeltm.project.domain.Project");
				//JSONArray array = util.toJSON(list, map);
				if(list!=null){
					for(int i=0;i<list.size();i++){
						Project prj=(Project)list.get(i);
						List<ProjectAttendance> atts=prj.getAttendances();
						if(atts!=null){
							for(int j=0;j<atts.size();j++){
								atts.get(j).setProject(null);
							}
						}
						List<UserProject> ups=prj.getUserProjects();
						if(ups!=null){
							for(int j=0;j<ups.size();j++){
								ups.get(j).setProject(null);
							}
						}
						List<ProjectResourcePlan> rps=prj.getProjectResourcePlan();
						if(rps!=null){
							for(int j=0;j<rps.size();j++){
								rps.get(j).setProject(null);
							}
						}
					}
				}
				net.sf.json.JSONArray newJsonArray=util.toJSONByJsonlib(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", newJsonArray);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}
			request.setAttribute("projects", list);
			return "list";
		} catch (Exception e) {
			SysLog.error("error in (ProjectAction.java-list())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 将ProjcetResourcePlan 中的数据同步到UserProject中
	 * @author lping1 2014-10-10
	 */
	public String synchronizeUserProject(){
		MsgBox msgBox;
		try{
			userProjectUpdateUtil.registerRequest(request).synchronizeUserProject();	
			msgBox = new MsgBox(request, getText("同步成功！"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}catch (Exception e){
			SysLog.error("error in (ProjectAction.java-list())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("同步失败！"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	/**
	 * 同步项目负责人数据到权限组别中的项目经理角色中
	 */
	public void synchronizeProjectManage(){
		List<Project> list = service.listAllGroup();
		if(list!=null && list.size()>0){
			sysUserGroupService.removeGroupByUserid("groupManager");
			for(Project record:list){
				if(!sysUserGroupService.findRoleByUserid(record.getProManagerId(), "groupManager")){
					UsrUsrgrp usrusrgrp = new UsrUsrgrp();
					usrusrgrp.setUserid(record.getProManagerId());
					usrusrgrp.setGrpcode("groupManager");
					sysUserGroupService.saveSysUserGroup(usrusrgrp);
				}
			}
		}
	}

	
	/**
	 * @return 导出Excel表
	 */
	public String exportData() throws Exception{ 
		try {
			request.setCharacterEncoding("utf-8");
			String projectName=request.getParameter("projectName");
			String isEndValue=request.getParameter("isEndValue");
			
			List<Project>  beanList = service.getProjectListByCondition(projectName,isEndValue);
			//文件名
			String filename = "项目信息表";//设置文件名
			
			Date date = new Date();
			OutputStream os = response.getOutputStream();// 取得输出流 
			response.reset();// 清空输出流  
			response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
			response.setContentType("application/vnd.ms-excel");// 定义输出类型  
			//第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet();
//     	wb.setSheetName(0,filename,HSSFWorkbook.ENCODING_UTF_16);
			wb.setSheetName(0,filename);//poi3.7版本中只有两个参数
			
			/**	** ** *设置字体* ** ** **/
			HSSFFont defaultFont = wb.createFont(); //默认字体
			defaultFont.setFontName("宋体");
			defaultFont.setFontHeightInPoints((short)10);// 字体大小
			
			HSSFFont headfont = wb.createFont(); 
			headfont.setFontName("黑体");
			headfont.setFontHeightInPoints((short)30);
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			HSSFFont cellfont = wb.createFont();  
			cellfont.setFontName("黑体");
			cellfont.setFontHeightInPoints((short)12);
			cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			HSSFFont allFont = wb.createFont();
			allFont.setFontName("黑体");
			allFont.setColor(HSSFColor.RED.index);
			allFont.setFontHeightInPoints((short)12);
			allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			/**	** ** *设置样式* ** ** **/
			//================== 标题 =====================
			HSSFFont cellNamefont = wb.createFont();  
			cellNamefont.setFontName("宋体");
			cellNamefont.setFontHeightInPoints((short)10);
			cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
			cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
			
			//================== 普通行 (左中)======================
			HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
			cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			//================== 普通行 (居中)======================
			HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
			cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			//================== 日期(居中) ======================
			HSSFCellStyle dateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
			cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			/**	** ** *设置文本格式* ** ** **/
			HSSFDataFormat format = wb.createDataFormat();
			defaultStyle.setDataFormat(format.getFormat("@"));
			defaultCenterStyle.setDataFormat(format.getFormat("@"));
			dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd "));
			
			/**		创建列名		*/
			HSSFRow cellNameRow = sheet.createRow((int) 0);
			cellNameRow.setHeight((short)350);
			wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)0, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "项目类型", 0, (short)1, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "项目负责人", 0,(short) 2, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "成员人数", 0, (short)3, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "计划开始时间",0, (short)4, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "计划结束时间", 0, (short)5, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "考勤类型", 0, (short)6, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "实际开始时间", 0, (short)7, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "实际结束时间", 0, (short)8, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "是否虚拟项目", 0, (short)9, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "是否完结", 0, (short)10, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "考勤时间(进入)", 0, (short)11, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "考勤时间(退出)", 0, (short)12, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "签到地点", 0, (short)13, cellNameStyle);

			/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
			int sumRow=0;
			
				for(int j=0;j<beanList.size();j++){
					++sumRow;
					Project prj = (Project) beanList.get(j);
					HSSFRow row = sheet.createRow(sumRow);
					row.setHeight((short)256);//行高
					HSSFCell cell = row.createCell((short)0);
					
					/*跨行合并单元格*/
					/*
					 wtjiao 20140715 POI对合并后边框操作存在缺陷，需要对参与合并的每个单元格进行边框设置，故下面这段代码不能放到if(j==0)判断中.
					                 POI中规定如果参与合并的每个单元格都有值，则以左上角的单元格值为准
					 */
					/*项目名称 */
					cell = sheet.getRow(sumRow).createCell((short)0);
					defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					setBorderStyle(defaultCenterStyle);
					cell.setCellStyle(defaultCenterStyle);
					cell.setCellValue(prj.getName());
					sheet.setColumnWidth((short)0, (short) (12*512));
					
					/*项目类型*/
					cell = sheet.getRow(sumRow).createCell((short)1);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					setBorderStyle(defaultCenterStyle);
					cell.setCellStyle(dateStyle);
					
					String projectType = (String) (BusnDataDir
							.getMapKeyValue("projectMonitor.projectType")
							.get(prj.getProjectType()));// 获取数据字典该项目类型的中文数据
					cell.setCellValue(projectType);
					sheet.setColumnWidth((short)1, (short) (6*512));
					
					/*项目负责人*/
					cell = row.createCell((short)2);
					defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(prj.getProManager());
					sheet.setColumnWidth((short)2, (short) (5*512));
					
					/*成员人数*/
					cell = row.createCell((short)3);
					defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(prj.getUserProjects().size());
					sheet.setColumnWidth((short)3, (short)(3*512));
					String name = prj.getName();
					if( "在线考试系统-广电运通".equals(name)){
						System.out.println("在线考试系统-广电运通");
					}
					
					/*计划开始时间*/
					cell = row.createCell((short)4);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					Date planStartTime = prj.getPlanStartTime();
					if( planStartTime != null && !"".equals(planStartTime)){
						cell.setCellValue(planStartTime.toString());
					}
					sheet.setColumnWidth((short)4, (short) (12*512));
					
					/*计划结束时间*/
					cell = row.createCell((short)5);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					Date planEndTime = prj.getPlanEndTime();
					if( planStartTime != null && !"".equals(planStartTime)){
						cell.setCellValue(planEndTime.toString());
					}
					sheet.setColumnWidth((short)5, (short) (12*512));
					
					/*跨行合并单元格*/
					/*考勤类型*/
					cell = sheet.getRow(sumRow).createCell((short)6);
					defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					setBorderStyle(defaultCenterStyle);
					cell.setCellStyle(defaultCenterStyle);
					Map attendanceTypeMap=BusnDataDir.getMapKeyValue("projectMonitor.attendanceType");

					if(prj.getAttendances()!=null){
						for(ProjectAttendance att:prj.getAttendances()){
							//att.setAttendanceType((String)attendanceTypeMap.get(att.getAttendanceType()));
							cell.setCellValue((String)attendanceTypeMap.get(att.getAttendanceType()));
						}
					}
					sheet.setColumnWidth((short)6, (short) (9*512));
					
					/*实际开始时间*/
					cell = row.createCell((short)7);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					Date factStartTime = prj.getFactStartTime();
					if( factStartTime != null && !"".equals(factStartTime)){
						cell.setCellValue(factStartTime.toString());
					}
					sheet.setColumnWidth((short)7, (short) (12*512));
					
					
					
					/*实际结束时间*/	
					cell = row.createCell((short)8);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					Date factEndTime = prj.getFactEndTime();
					if( factEndTime != null && !"".equals(factEndTime)){
						cell.setCellValue(factEndTime.toString());
					}
					sheet.setColumnWidth((short)8, (short) (12*512));
					
					
					/*是否虚拟项目*/
					cell = row.createCell((short)9);
					defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(prj.getIsVisual());
					//是否虚拟项目 0：否 1：是
					String isVisual = prj.getIsVisual();
					if(isVisual != null && !"".equals(isVisual)){
						if("1".equals(isVisual)){
							cell.setCellValue("是");
						}else if("0".equals(isVisual)){
							cell.setCellValue("否");							
						}
					}
					sheet.setColumnWidth((short)9, (short) (3*512));
					
					/*是否完结*/
					cell = row.createCell((short)10);
					defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					String isEnd = prj.getIsEnd();
					if(isEnd != null && !"".equals(isEnd)){
						if("1".equals(isEnd)){
							cell.setCellValue("已完结");
						}else if("0".equals(isEnd)){
							cell.setCellValue("未完结");							
						}
					}					
					sheet.setColumnWidth((short)10, (short) (5*512));
					
					/*考勤进入时间*/
					
					cell = row.createCell((short)11);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					for(ProjectAttendance att:prj.getAttendances()){
						//att.setAttendanceType((String)attendanceTypeMap.get(att.getAttendanceType()));
						Date entryTime = att.getEntryTime() ;
						if(entryTime != null && !"".equals(entryTime) ){
							cell.setCellValue(entryTime.toString());
						}
						
					}	
					sheet.setColumnWidth((short)11, (short) (12*512));
					
					
					
					/*考情退出时间*/
					cell = row.createCell((short)12);
					dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					for(ProjectAttendance att:prj.getAttendances()){
						//att.setAttendanceType((String)attendanceTypeMap.get(att.getAttendanceType()));
						Date exitTime = att.getExitTime();
						if(exitTime != null && !"".equals(exitTime) ){
							cell.setCellValue(exitTime.toString());
						}						
					}
					sheet.setColumnWidth((short)12, (short) (12*512));
					/*签到地点*/
					cell = row.createCell((short)13);
					defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					cell.setCellStyle(defaultStyle);
					for(ProjectAttendance att:prj.getAttendances()){
						//att.setAttendanceType((String)attendanceTypeMap.get(att.getAttendanceType()));
						String signP = att.getSignPlace();
						if(signP != null && !"".equals(signP) ){
							cell.setCellValue(signP.toString());
						}
						
					}
					sheet.setColumnWidth((short)13, (short) (15*512));
					
					
					
				}
			
			
			wb.write(os);
			os.close();
			
			//清理刷新缓冲区，将缓存中的数据将数据导出excel
			os.flush();
			//关闭os
			if(os!=null){
				os.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}         
	
	private String getWorkSheetName(DateDayLog dl, List<DateDayLog> datelogList, String queryStartTime, String queryEndTime) {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String name="";
			if(StringUtils.isNotBlank(dl.getUserDept())){
				if(dl.getUserDept().length()>10){
					name+=dl.getUserDept().substring(0,10)+" ";
				}else{
					name+=dl.getUserDept()+" ";
				}
			}
			if(StringUtils.isNotBlank(dl.getUserGroup())){
				if(dl.getUserGroup().length()>10){
					name+=dl.getUserGroup().substring(0,10)+" ";
				}else{
					name+=dl.getUserGroup()+" ";
				}
			}
			if(StringUtils.isNotBlank(dl.getUserName())){
				name+=dl.getUserName()+" ";
			}
			name+="工作日志";
			if(StringUtils.isNotBlank(queryStartTime)&&StringUtils.isNotBlank(queryEndTime)){
				Date st=sdf.parse(queryStartTime);
				Date end=sdf.parse(queryEndTime);
				name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
			}else if(datelogList!=null){
				Date st=sdf.parse(datelogList.get(0).getLogDate());
				Date end=sdf.parse(datelogList.get(datelogList.size()-1).getLogDate());
				name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
			}
			return name;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "工作日志";
	}

	private void setBorderStyle(HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
		style.setBottomBorderColor(HSSFColor.BLACK.index);	
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder 是否显示边框
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			setBorderStyle(style);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	
	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Project getGroup() {
		return group;
	}

	public void setGroup(Project group) {
		this.group = group;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(String usergroups) {
		this.usergroups = usergroups;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	public UserProject getUserProject() {
		return userProject;
	}
	public void setUserProject(UserProject userProject) {
		this.userProject = userProject;
	}
	public String getUserids() {
		return userids;
	}
	public void setUserids(String userids) {
		this.userids = userids;
	}
	public String getUsernames() {
		return usernames;
	}
	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public Date getFactStartTime() {
		return factStartTime;
	}

	public void setFactStartTime(Date factStartTime) {
		this.factStartTime = factStartTime;
	}

	public Date getFactEndTime() {
		return factEndTime;
	}

	public void setFactEndTime(Date factEndTime) {
		this.factEndTime = factEndTime;
	}

	public Date[] getPlanStartTimes() {
		return planStartTimes;
	}

	public void setPlanStartTimes(Date[] planStartTimes) {
		this.planStartTimes = planStartTimes;
	}

	public Date[] getPlanEndTimes() {
		return planEndTimes;
	}

	public void setPlanEndTimes(Date[] planEndTimes) {
		this.planEndTimes = planEndTimes;
	}

	public String[] getUseridss() {
		return useridss;
	}

	public void setUseridss(String[] useridss) {
		this.useridss = useridss;
	}

	public String[] getUsernamess() {
		return usernamess;
	}

	public void setUsernamess(String[] usernamess) {
		this.usernamess = usernamess;
	}

	public Date[] getFactStartTimes() {
		return factStartTimes;
	}

	public void setFactStartTimes(Date[] factStartTimes) {
		this.factStartTimes = factStartTimes;
	}

	public Date[] getFactEndTimes() {
		return factEndTimes;
	}

	public void setFactEndTimes(Date[] factEndTimes) {
		this.factEndTimes = factEndTimes;
	}

	public List<String> getAttendanceEntryTimeList() {
		return attendanceEntryTimeList;
	}

	public void setAttendanceEntryTimeList(List<String> attendanceEntryTimeList) {
		this.attendanceEntryTimeList = attendanceEntryTimeList;
	}

	public List<String> getAttendanceExitTimeList() {
		return attendanceExitTimeList;
	}

	public void setAttendanceExitTimeList(List<String> attendanceExitTimeList) {
		this.attendanceExitTimeList = attendanceExitTimeList;
	}

	public List<String> getAttendanceTypeList() {
		return attendanceTypeList;
	}

	public void setAttendanceTypeList(List<String> attendanceTypeList) {
		this.attendanceTypeList = attendanceTypeList;
	}

	public List<String> getAttendanceAddressList() {
		return attendanceAddressList;
	}

	public void setAttendanceAddressList(List<String> attendanceAddressList) {
		this.attendanceAddressList = attendanceAddressList;
	}


	public List<String> getPlanStartTimeList() {
		return planStartTimeList;
	}


	public void setPlanStartTimeList(List<String> planStartTimeList) {
		this.planStartTimeList = planStartTimeList;
	}


	public List<String> getFactStartTimeList() {
		return factStartTimeList;
	}


	public void setFactStartTimeList(List<String> factStartTimeList) {
		this.factStartTimeList = factStartTimeList;
	}


	public List<String> getPlanEndTimeList() {
		return planEndTimeList;
	}


	public void setPlanEndTimeList(List<String> planEndTimeList) {
		this.planEndTimeList = planEndTimeList;
	}


	public List<String> getFactEndTimeList() {
		return factEndTimeList;
	}


	public void setFactEndTimeList(List<String> factEndTimeList) {
		this.factEndTimeList = factEndTimeList;
	}


	public List<String> getUseridList() {
		return useridList;
	}


	public void setUseridList(List<String> useridList) {
		this.useridList = useridList;
	}


	public List<String> getUsernameList() {
		return usernameList;
	}


	public void setUsernameList(List<String> usernameList) {
		this.usernameList = usernameList;
	}


	public List<String> getProjectDutyList() {
		return projectDutyList;
	}


	public void setProjectDutyList(List<String> projectDutyList) {
		this.projectDutyList = projectDutyList;
	}


	public List<String> getProjectRoleList() {
		return projectRoleList;
	}


	public void setProjectRoleList(List<String> projectRoleList) {
		this.projectRoleList = projectRoleList;
	}
	
}
