package cn.grgbanking.feeltm.projectweekplan.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common.bean.DynamicWeek;
import cn.grgbanking.feeltm.common.util.DynamicWeekUtils;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlan;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTarget;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.feeltm.projectweekplan.service.ProjectWeekPlanService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ProjectWeekPlanAction extends BaseAction{
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	public SysUserGroupService userGroupService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	public ProjectWeekPlanService projectWeekPlanService;
	
	private ProjectWeekPlan plan;
	
	//从页面提交的属性
	private List<String> submitTargetList;
	private List<String> submitTaskDoManList;
	private List<String> submitTaskStartDateList;
	private List<String> submitTaskEndDateList;
	private List<String> submitTaskContentList;
	private List<String> submitTaskPlanWorkTimeList;
	private List<String> submitTaskFactWorkTimeList;
	private List<String> submitTaskDescList;
	private List<String> submitPrioritySelectList;//优先级
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat sdf2=new SimpleDateFormat("MM月dd日");
	private static SimpleDateFormat sdf3=new SimpleDateFormat("yyyy-MM-dd");
	
	/** 查询
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			
			
			if(from ==null){
				plan=new ProjectWeekPlan();
				//当前项目：
				List projectList=projectService.getProjectByUserId(userModel.getUserid());
				if(projectList!=null && projectList.size()>0){
					request.setAttribute("curProject",((Project)projectList.get(0)).getId());
				}else{
					request.setAttribute("curProject","");
				}
				plan.setProjectId(request.getAttribute("curProject").toString());
			}
			if(plan==null){
				plan=new ProjectWeekPlan();
			}
			if (request.getParameter("timePeriod") != null && request.getParameter("timePeriod").length() > 0){
				String timePeriod=request.getParameter("timePeriod");
				setPlanTimeInfo(timePeriod,plan);
			}
			if (request.getParameter("startTime") != null && request.getParameter("startTime").length() > 0)
				plan.setWeekStart(sdf3.parse(request.getParameter("startTime")));
			if (request.getParameter("endTime") != null && request.getParameter("endTime").length() > 0)
				plan.setWeekEnd(sdf3.parse(request.getParameter("endTime")));
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			
			
			Page page = projectWeekPlanService.getPage(plan,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlan");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				ajaxPrint(input.toString());
				return null;
			} else {
				ActionContext.getContext().put("projectWeekPlanList", list);
				//查询：项目列表
				request.setAttribute("projectList", getProjectList());
				//查询：周列表
				Calendar startDate=Calendar.getInstance();
				startDate.add(Calendar.MONTH, -2);//往前数两个月
				Calendar endDate=Calendar.getInstance();
				endDate.add(Calendar.MONTH, 2);//往后数两个月
				List<DynamicWeek> weekList=DynamicWeekUtils.getDynamicWeeks(startDate, endDate, Calendar.getInstance());
				
				request.setAttribute("weekList",weekList);
				//查询: 客户名称
				request.setAttribute("customerList", getCustomerList());				
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "list";
	}
	
	
	public String add() throws Exception{
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
		
		//获取当前人所带领的项目组
		List<Project> projectList=projectService.getProjectByManager(usr.getUserid());
		request.setAttribute("projectList", projectList);
		
		Calendar startTime=Calendar.getInstance();
		//获取周计划提供的周范围
		List<DynamicWeek> weekList=getWeekPlanDynamicWeekByTime(startTime);
		
		request.setAttribute("weekList",weekList);
		//客户名称
		request.setAttribute("customerList", getCustomerList());

		return "add";
	}
	
	/**获取周计划提供的周范围
	 * wtjiao 2014年7月4日 上午9:48:12
	 * @param startDate
	 * @return
	 */
	private List<DynamicWeek> getWeekPlanDynamicWeekByTime(Calendar startDate) {
		Calendar oriStartDate=Calendar.getInstance();
		oriStartDate.setTime(startDate.getTime());
		//周列表
		startDate.add(Calendar.MONTH, -1);//往前数一月
		Calendar endDate=Calendar.getInstance();
		endDate.add(Calendar.MONTH, 2);//往后数两个月
		List<DynamicWeek> weekList=DynamicWeekUtils.getDynamicWeeks(startDate, endDate, oriStartDate);
		return weekList;
	}


	public String edit() throws Exception{
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
		
		String id=request.getParameter("ids");
		plan=projectWeekPlanService.getWeekPlanById(id);
		
		request.setAttribute("plan", plan);
		//获取当前人所带领的项目组
		List<Project> projectList=projectService.getProjectByManager(usr.getUserid());
		request.setAttribute("projectList", projectList);
		
		if(projectList==null || projectList.size()<=0){
			MsgBox msgBox = new MsgBox(request,"没有您担任项目经理的项目，请确认!");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		//默认的项目组成员
		List<SysUser> userList=projectService.getInProjectByProject(plan.getProjectId());
		request.setAttribute("userInProject", userList);
		
		//客户名称
		request.setAttribute("customerList", getCustomerList());
		
		//录入的周计划的周开始时间
		Calendar startTime=Calendar.getInstance();
		startTime.setTime(plan.getWeekStart());
		//获取周计划提供的周范围
		List<DynamicWeek> weekList=getWeekPlanDynamicWeekByTime(startTime);
		request.setAttribute("weekList", weekList);

		return "edit";
	}
	
	/**获取项目成员
	 * wtjiao 2014年6月27日 上午11:56:28
	 * @param projectId
	 */
	public String getUserInProject(){
		String projectId=request.getParameter("projectId");
		List<SysUser> userList;
		if(StringUtils.isNotBlank(projectId)){
			userList= projectService.getInProjectByProject(projectId);
		}else{
			userList=new ArrayList<SysUser>();
		}
		String  str=net.sf.json.JSONArray.fromObject(userList).toString();
		ajaxPrint(str);
		return null;
	}
	
	
	private void setPlanTimeInfo(String timePeriod, ProjectWeekPlan plan) {
		try{
			String[] strs=timePeriod.split("-");
			//设置查询时间
			plan.setWeekStart(sdf.parse(strs[0]));
			plan.setWeekEnd(sdf.parse(strs[1]));
			
			//设置时间段
			String weekPeriod=sdf2.format(sdf.parse(strs[0]))+"-"+sdf2.format(sdf.parse(strs[1]));
			plan.setWeekPeriod(weekPeriod);
			//设置描述
			Calendar cal=Calendar.getInstance();
			cal.setTime(sdf.parse(strs[0]));
			String weekDesc=cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月第"+cal.get(Calendar.WEEK_OF_MONTH)+"周";
			plan.setWeekDesc(weekDesc);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/** 修改后保存
	 * @return
	 * @throws Excetion
	 */
	public String update() throws Exception{
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			
			if(StringUtils.isNotBlank(request.getParameter("submitKeyTimePeriod"))){
				String[] keys=request.getParameter("submitKeyTimePeriod").split("-");
				plan.setWeekStart(sdf.parse(keys[0]));
				plan.setWeekEnd(sdf.parse(keys[1]));
			}
			plan.setCustomerName(projectWeekPlanService.getCustomerName(plan.getCustomerKey()));
			plan.setUpdateTime(Calendar.getInstance().getTime());
			plan.setUpdateuserId(userModel.getUserid());
			plan.setUpdateUsername(userModel.getUsername());
			List targetList=buildRequestToTargetList(plan.getId());
			List taskList=buildRequestToTaskList(plan.getId());
			plan.setTargetList(targetList);
			plan.setTaskList(taskList);
			projectWeekPlanService.updateProjectWeekPlan(plan);
			
			this.addActionMessage(getText("add.ok"));
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.updateok"));
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	public String save() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			
			if(StringUtils.isNotBlank(request.getParameter("submitKeyTimePeriod"))){
				String[] keys=request.getParameter("submitKeyTimePeriod").split("-");
				plan.setWeekStart(sdf.parse(keys[0]));
				plan.setWeekEnd(sdf.parse(keys[1]));
			}
			plan.setCustomerName(projectWeekPlanService.getCustomerName(plan.getCustomerKey()));
			plan.setUpdateTime(Calendar.getInstance().getTime());
			plan.setUpdateuserId(userModel.getUserid());
			plan.setUpdateUsername(userModel.getUsername());
			String planId=projectWeekPlanService.saveProjectWeekPlan(plan);
			List targetList=buildRequestToTargetList(planId);
			projectWeekPlanService.saveProjectWeekPlanTargetList(targetList);
			List taskList=buildRequestToTaskList(planId);
			projectWeekPlanService.saveProjectWeekPlanTaskList(taskList);;

			MsgBox msgBox = new MsgBox(request, getText("add.ok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.ok"));
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,"增加失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("增加失败");
			
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	/**删除记录
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String idStr=request.getParameter("ids");
		if(idStr.length()>0){
			idStr=idStr.substring(1);
			String[] ids=idStr.split(",");
			for(int i=0;i<ids.length;i++){
				plan=projectWeekPlanService.getWeekPlanById(ids[i]);
				
				//删除周计划
				projectWeekPlanService.removeProjectWeekPlan(plan);;
			}
		}
		MsgBox msgBox = new MsgBox(request, getText("记录删除成功"), "删除信息页面");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	private List<ProjectWeekPlanTarget> buildRequestToTargetList(String planId) {
		try{
			List targetList=new ArrayList<ProjectWeekPlanTask>();
			if(submitTargetList!=null){
				for(int i=0;i<submitTargetList.size();i++){
					ProjectWeekPlanTarget target=new ProjectWeekPlanTarget();
					target.setPlanId(planId);
					target.setTarget(submitTargetList.get(i));
					targetList.add(target);
				}
			}
			return targetList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private List<ProjectWeekPlanTask> buildRequestToTaskList(String planId) {
		try{
			List taskList=new ArrayList<ProjectWeekPlanTask>();
			if(submitTaskDoManList!=null){
				for(int i=0;i<submitTaskDoManList.size();i++){
					ProjectWeekPlanTask task=new ProjectWeekPlanTask();
					
					if(StringUtils.isNotBlank(submitTaskDescList.get(i)))
						task.setDesc(submitTaskDescList.get(i));
					
					if(StringUtils.isNotBlank(submitTaskEndDateList.get(i)))
						task.setEndDate(sdf3.parse(submitTaskEndDateList.get(i)));
					
					if(StringUtils.isNotBlank(submitTaskFactWorkTimeList.get(i)))
						task.setFactWorkTime(Integer.parseInt(submitTaskFactWorkTimeList.get(i)));
						
					task.setPlanId(planId);
					
					if(StringUtils.isNotBlank(submitTaskPlanWorkTimeList.get(i)))
						task.setPlanWorkTime(Integer.parseInt(submitTaskPlanWorkTimeList.get(i)));
					
					if(StringUtils.isNotBlank(submitTaskStartDateList.get(i)))
						task.setStartDate(sdf3.parse(submitTaskStartDateList.get(i)));
					
					if(StringUtils.isNotBlank(submitTaskContentList.get(i)))
						task.setTaskContent(submitTaskContentList.get(i));
					
					if(StringUtils.isNotBlank(submitTaskDoManList.get(i))){
						task.setUserKey(submitTaskDoManList.get(i));
						task.setUserName(staffInfoService.getUsernameById(submitTaskDoManList.get(i)));
					}
					
					if(StringUtils.isNotBlank(submitPrioritySelectList.get(i))){
						task.setPriority(submitPrioritySelectList.get(i));
					}
						
					taskList.add(task);
				}
			}
			return taskList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**查询详细信息
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
			
			String id=request.getParameter("ids");
			plan=projectWeekPlanService.getWeekPlanById(id);
			
			boolean isProjectManagerOrInProjectManageGroup=isProjectManagerOrInProjectManageGroup(userModel,plan);
			//项目经理或者项目管理组中的人，能查看所有的周任务，其他人只能查看分配给自己的任务
			if(!isProjectManagerOrInProjectManageGroup){
				List taskList=plan.getTaskList();
				if(taskList!=null && taskList.size()>0){
					for(int i=taskList.size()-1;i>=0;i--){
						ProjectWeekPlanTask task=(ProjectWeekPlanTask)taskList.get(i);
						if(!userModel.getUserid().equals(task.getUserKey())){
							taskList.remove(i);
						}
					}
				}
			}
			request.setAttribute("plan", plan);
				
		}catch(Exception e){
			e.printStackTrace();
		}
		return "show";
	}
	
	
	/**是否是本项目的项目经理 或者 是项目管理组中的人员
	 * wtjiao 2014年7月17日 上午8:40:11
	 * @return
	 */
	private boolean isProjectManagerOrInProjectManageGroup(UserModel userModel,ProjectWeekPlan weekPlan) {
		boolean isProjectManager=isCurProjectManager(userModel.getUserid(),weekPlan);
		boolean isInProjectManageGroup=UserRoleConfig.isInPrjManageGroup(userModel);
		return isInProjectManageGroup||isProjectManager;
	}
	private boolean isCurProjectManager(String userid,ProjectWeekPlan weekPlan) {
		Project pg=projectService.getProjectById(plan.getProjectId());
		if(userid.equals(pg.getProManagerId())){
			return true;
		}
		return false;
	}

	/**获取项目列表
	 * wtjiao 2014年6月26日 上午9:32:05
	 * @return
	 */
	private List<Project> getProjectList(){
		return projectService.listAllGroup();
	}
	
	/**用户列表
	 * wtjiao 2014年6月26日 上午9:38:53
	 * @return
	 */
	private List<Map<String,String>> getCustomerList(){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map map=BusnDataDir.getMapKeyValue("projectManage.customer");
		Iterator ite=map.keySet().iterator();
		while(ite.hasNext()){
			String key=(String)ite.next();
			String val=(String)map.get(key);
			Map map2=new HashMap();
			map2.put("key", key);
			map2.put("val", val);
			list.add(map2);
		}
		return list;
	}
	
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
	
	/**
	 * 根据日志信息完善周计划中的任务完成度、偏差度及确认工时
	 */
	private void getDataForDayLogToPrjWeekPlan(){
		
	}


	public ProjectWeekPlan getPlan() {
		return plan;
	}


	public void setPlan(ProjectWeekPlan plan) {
		this.plan = plan;
	}


	public List<String> getSubmitTargetList() {
		return submitTargetList;
	}


	public void setSubmitTargetList(List<String> submitTargetList) {
		this.submitTargetList = submitTargetList;
	}


	public List<String> getSubmitTaskDoManList() {
		return submitTaskDoManList;
	}


	public void setSubmitTaskDoManList(List<String> submitTaskDoManList) {
		this.submitTaskDoManList = submitTaskDoManList;
	}


	public List<String> getSubmitTaskStartDateList() {
		return submitTaskStartDateList;
	}


	public void setSubmitTaskStartDateList(List<String> submitTaskStartDateList) {
		this.submitTaskStartDateList = submitTaskStartDateList;
	}


	public List<String> getSubmitTaskEndDateList() {
		return submitTaskEndDateList;
	}


	public void setSubmitTaskEndDateList(List<String> submitTaskEndDateList) {
		this.submitTaskEndDateList = submitTaskEndDateList;
	}


	public List<String> getSubmitTaskContentList() {
		return submitTaskContentList;
	}


	public void setSubmitTaskContentList(List<String> submitTaskContentList) {
		this.submitTaskContentList = submitTaskContentList;
	}


	public List<String> getSubmitTaskPlanWorkTimeList() {
		return submitTaskPlanWorkTimeList;
	}


	public void setSubmitTaskPlanWorkTimeList(
			List<String> submitTaskPlanWorkTimeList) {
		this.submitTaskPlanWorkTimeList = submitTaskPlanWorkTimeList;
	}


	public List<String> getSubmitTaskFactWorkTimeList() {
		return submitTaskFactWorkTimeList;
	}


	public void setSubmitTaskFactWorkTimeList(
			List<String> submitTaskFactWorkTimeList) {
		this.submitTaskFactWorkTimeList = submitTaskFactWorkTimeList;
	}


	public List<String> getSubmitTaskDescList() {
		return submitTaskDescList;
	}


	public void setSubmitTaskDescList(List<String> submitTaskDescList) {
		this.submitTaskDescList = submitTaskDescList;
	}


	public List<String> getSubmitPrioritySelectList() {
		return submitPrioritySelectList;
	}


	public void setSubmitPrioritySelectList(List<String> submitPrioritySelectList) {
		this.submitPrioritySelectList = submitPrioritySelectList;
	}
	
	
}
