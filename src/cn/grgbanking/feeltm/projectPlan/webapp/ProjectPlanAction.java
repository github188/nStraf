package cn.grgbanking.feeltm.projectPlan.webapp;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlan;
import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlanTask;
import cn.grgbanking.feeltm.projectPlan.service.ProjectPlanService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ProjectPlanAction extends BaseAction{
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	public SysUserGroupService userGroupService;
	@Autowired
	private ProjectPlanService planService;
	@Autowired
	private ProjectService projectService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	
	private ProjectPlan plan;
	private List<ProjectPlan> planList; 
	private List<ProjectPlanTask> taskList;
	
	//从页面提交的属性
	private List<String> submitTaskName;
	private List<String> submitId;
	private List<String> submitPlanStartDate;
	private List<String> submitFactStartDate;
	private List<String> submitPlanEndDate;
	private List<String> submitFactEndDate;
	private List<String> submitPlanWorkDate;
	private List<String> submitFactWorkDate;
	private List<String> submitPlanWorkTime;
	private List<String> submitFactWorkTime;
	private List<String> submitPremise;
	private List<String> submitDutyMan;
	private List<String> submitFare;
	private List<String> submitShowOrder;
	private List<String> submitParentId;
	

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
			
			if(plan==null){
				plan=new ProjectPlan();
			}
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("queryProjectName") != null && request.getParameter("queryProjectName").length() > 0){
				String projectName=request.getParameter("queryProjectName");
				String decodeName=URLDecoder.decode(projectName,"utf-8");
				plan.setProjectName(decodeName);
			}
			String planStartTime1="";
			String planStartTime2="";
			String planEndTime1="";
			String planEndTime2="";
			if(StringUtils.isNotBlank(request.getParameter("planStartTime1"))){
				planStartTime1=request.getParameter("planStartTime1");
			}
			if(StringUtils.isNotBlank(request.getParameter("planStartTime2"))){
				planStartTime2=request.getParameter("planStartTime2");
			}
			
			if(StringUtils.isNotBlank(request.getParameter("planEndTime1"))){
				planEndTime1=request.getParameter("planEndTime1");
			}
			
			if(StringUtils.isNotBlank(request.getParameter("planEndTime2"))){
				planEndTime2=request.getParameter("planEndTime2");
			}
			
			Page page = planService.getPage(plan,pageNum, pageSize,planStartTime1,planStartTime2,planEndTime1,planEndTime2);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.projectPlan.domain.ProjectPlan");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				ajaxPrint(input.toString());
				return null;
			} else {
				ActionContext.getContext().put("projectPlanList", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "projectList";
	}
	
	public String add() throws Exception{
		
		taskList=new ArrayList();
		request.setAttribute("taskList", taskList);
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
		/*boolean isGroupManager=UserRoleConfig.isGroupManager(userModel);
		if(!isGroupManager){
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不是项目经理，不能新增项目计划"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}*/
		
		List<Project> groupList=projectService.getProjectByUserId(userModel.getUserid());
		request.setAttribute("usr", usr);
		request.setAttribute("groupList", groupList);
		
		return "projectAdd";
	}
	
	public String save() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			plan.setUpdateMan(staffInfoService.findUserByUserid(userModel.getUserid()).getUsername());
			plan.setUpdateUser(userModel.getUserid());
			plan.setUpdateTime(Calendar.getInstance().getTime());
			plan.setCreatorUser(userModel.getUserid());
			
			String projcetPlanId=planService.saveProject(plan);
			
			taskList=buildRequestToTaskList(projcetPlanId);
			planService.saveTaskList(taskList);
			
			ajaxPrint("true");
			/*MsgBox msgBox = new MsgBox(request, getText("add.ok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.ok"));*/
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			ajaxPrint("报错出错!");
			/*MsgBox msgBox = new MsgBox(request,getText("operInfoform.addfaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			return "msgBox";*/
		}
		/*boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);*/
		return null;
	}
	
	/**查询详细信息
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String id=request.getParameter("ids");

			if(StringUtils.isNotBlank(id)){
				plan=planService.getPlanById(id);
				taskList=planService.getTaskListByPlanId(id);
				request.setAttribute("plan", plan);
				request.setAttribute("taskList", taskList);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return "projectShow";
	}
	
	/** 是否具有某个项目的修改和删除等编辑权限
	 * wtjiao 2014年5月29日 上午9:28:02
	 * @param userModel 当前登录人
	 * @param plan 项目
	 * @return
	 */
	public boolean hasProjectEditRight(UserModel userModel,ProjectPlan plan){
		String groupName=plan.getGroupName();
		String groups=projectService.getProjectNameByUserid(userModel.getUserid());
		
		//登录人是否在这个项目组中
		boolean inProject=false;
		//组别的字符串中包含groupName
		if(groups.equals(groupName)|| groups.startsWith(groupName+",") || groups.endsWith(","+groupName) || groups.indexOf(","+groupName+",")>0){
			inProject=true;
		}
		//是否是项目经理
		boolean isManager=UserRoleConfig.isProjectManageGroup(userModel);
		
		//权限判断：如果是项目经理，且参与了这个项目组,就认为其有权限
		//(有这样一种情况，A项目组的项目经理是B组的成员，按照当前的逻辑，这个项目经理是有权限修改B的项目计划的，这个bug暂时不考虑)
		if(inProject&&isManager){
			return true;
		}else{
			return false;
		}
	}
	
	/** 转向修改页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id=request.getParameter("ids");
		plan=planService.getPlanById(id);
		taskList=planService.getPlanTaskList(id);
		
		/*boolean hasEditRight=hasProjectEditRight(userModel, plan);
		if(!hasEditRight){
			MsgBox msgBox = new MsgBox(request,"您不在该项目组中或者不是该项目的项目经理，无权修改该项目计划");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}*/
		
		boolean hasEditRight=isProjectManagerOrCreator(userModel, plan);
		if(!hasEditRight){
			MsgBox msgBox = new MsgBox(request,"您不是该项目项目经理或计划的创建者，无权修改该项目计划");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
		List<Project> groupList=projectService.getProjectByUserId(userModel.getUserid());
		request.setAttribute("usr", usr);
		request.setAttribute("groupList", groupList);
		request.setAttribute("plan", plan);
		request.setAttribute("taskList", taskList);
		
		return "projectEdit";
	}
	
	
	/**是否是记录的创建者或者该项目项目经理 
	 * wtjiao 2014年6月22日 上午11:21:58
	 * @param userModel
	 * @param plan2
	 * @return
	 */
	private boolean isProjectManagerOrCreator(UserModel userModel,ProjectPlan plan) {
		try{
			//是项目经理
			if(staffInfoService.getUsernameById(userModel.getUserid()).equals(plan.getProjectManager())){
				return true;
			}
			//项目创建者
			if(userModel.getUserid().equals(plan.getCreatorUser())){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/** 修改后保存
	 * @return
	 * @throws Excetion
	 */
	public String update() throws Exception{
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			plan.setUpdateMan(staffInfoService.findUserByUserid(userModel.getUserid()).getUsername());
			plan.setUpdateUser(userModel.getUserid());
			plan.setUpdateTime(Calendar.getInstance().getTime());
			
			planService.updateProject(plan);
			taskList=buildRequestToTaskList(plan.getId());
			planService.saveTaskList(taskList);

			/*this.addActionMessage(getText("add.ok"));
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.updateok"));*/
			ajaxPrint("true");
		
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			/*MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";*/
			ajaxPrint("保存错误!");
		}
		return null;
	}
	
	/**删除记录
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id=request.getParameter("ids");
		plan=planService.getPlanById(id);
		
		/*boolean hasEditRight=hasProjectEditRight(userModel, plan);
		
		if(!hasEditRight){
			MsgBox msgBox = new MsgBox(request,"您不是项目经理，无权删除该项目计划");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}*/
		
		boolean hasEditRight=isProjectManagerOrCreator(userModel, plan);
		
		if(!hasEditRight){
			MsgBox msgBox = new MsgBox(request,"您不是该项目项目经理或计划的创建者，无权删除该项目计划");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		//删除项目计划
		planService.removeProjectPlan(plan);
		//根据计划id删除项目计划任务列表
		planService.removeProjectTaskByProjectId(plan.getId());
		
		MsgBox msgBox = new MsgBox(request, getText("记录删除成功"), "删除信息页面");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	/**将struct封装的request构建成ProjectPlanTask对象
	 * wtjiao 2014年5月26日 下午1:31:36
	 * @return
	 */
	private List<ProjectPlanTask> buildRequestToTaskList(String planId) {
		try{
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			taskList=new ArrayList<ProjectPlanTask>();
			if(submitId!=null){
				for(int i=0;i<submitId.size();i++){
					ProjectPlanTask task=new ProjectPlanTask();
					task.setDutyMan(submitDutyMan.get(i));
					task.setFactEndDate(StringUtils.isNotBlank(submitFactEndDate.get(i))?sdf.parse(submitFactEndDate.get(i)):null);
					task.setFactStartDate(StringUtils.isNotBlank(submitFactStartDate.get(i))?sdf.parse(submitFactStartDate.get(i)):null);
					task.setFactWorkDate(StringUtils.isNotBlank(submitFactWorkDate.get(i))?Integer.valueOf(submitFactWorkDate.get(i)):0);
					task.setFactWorkTime(StringUtils.isNotBlank(submitFactWorkTime.get(i))?Integer.valueOf(submitFactWorkTime.get(i)):0);
					task.setFare(submitFare.get(i));
					task.setId(submitId.get(i));
					task.setParentId(submitParentId.get(i));
					task.setPlanEndDate(StringUtils.isNotBlank(submitPlanEndDate.get(i))?sdf.parse(submitPlanEndDate.get(i)):null);
					task.setPlanId(planId);
					task.setPlanStartDate(StringUtils.isNotBlank(submitPlanStartDate.get(i))?sdf.parse(submitPlanStartDate.get(i)):null);
					task.setPlanWorkDate(StringUtils.isNotBlank(submitPlanWorkDate.get(i))?Integer.valueOf(submitPlanWorkDate.get(i)):0);
					task.setPlanWorkTime(StringUtils.isNotBlank(submitPlanWorkTime.get(i))?Integer.valueOf(submitPlanWorkTime.get(i)):0);
					task.setPremise(submitPremise.get(i));
					task.setShowOrder(StringUtils.isNotBlank(submitShowOrder.get(i))?Integer.valueOf(submitShowOrder.get(i)):0);
					task.setTaskDesc("");
					task.setTaskName(submitTaskName.get(i));
					task.setTaskResource("");
					taskList.add(task);
				}
			}
			return taskList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
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

	public List<String> getSubmitTaskName() {
		return submitTaskName;
	}

	public void setSubmitTaskName(List<String> submitTaskName) {
		this.submitTaskName = submitTaskName;
	}

	public List<String> getSubmitId() {
		return submitId;
	}

	public void setSubmitId(List<String> submitId) {
		this.submitId = submitId;
	}

	public List<String> getSubmitPlanStartDate() {
		return submitPlanStartDate;
	}

	public void setSubmitPlanStartDate(List<String> submitPlanStartDate) {
		this.submitPlanStartDate = submitPlanStartDate;
	}

	public List<String> getSubmitFactStartDate() {
		return submitFactStartDate;
	}

	public void setSubmitFactStartDate(List<String> submitFactStartDate) {
		this.submitFactStartDate = submitFactStartDate;
	}

	public List<String> getSubmitPlanEndDate() {
		return submitPlanEndDate;
	}

	public void setSubmitPlanEndDate(List<String> submitPlanEndDate) {
		this.submitPlanEndDate = submitPlanEndDate;
	}

	public List<String> getSubmitFactEndDate() {
		return submitFactEndDate;
	}

	public void setSubmitFactEndDate(List<String> submitFactEndDate) {
		this.submitFactEndDate = submitFactEndDate;
	}

	public List<String> getSubmitPlanWorkDate() {
		return submitPlanWorkDate;
	}

	public void setSubmitPlanWorkDate(List<String> submitPlanWorkDate) {
		this.submitPlanWorkDate = submitPlanWorkDate;
	}

	public List<String> getSubmitFactWorkDate() {
		return submitFactWorkDate;
	}

	public void setSubmitFactWorkDate(List<String> submitFactWorkDate) {
		this.submitFactWorkDate = submitFactWorkDate;
	}

	public List<String> getSubmitPlanWorkTime() {
		return submitPlanWorkTime;
	}

	public void setSubmitPlanWorkTime(List<String> submitPlanWorkTime) {
		this.submitPlanWorkTime = submitPlanWorkTime;
	}

	public List<String> getSubmitFactWorkTime() {
		return submitFactWorkTime;
	}

	public void setSubmitFactWorkTime(List<String> submitFactWorkTime) {
		this.submitFactWorkTime = submitFactWorkTime;
	}

	public List<String> getSubmitPremise() {
		return submitPremise;
	}

	public void setSubmitPremise(List<String> submitPremise) {
		this.submitPremise = submitPremise;
	}

	public List<String> getSubmitDutyMan() {
		return submitDutyMan;
	}

	public void setSubmitDutyMan(List<String> submitDutyMan) {
		this.submitDutyMan = submitDutyMan;
	}

	public List<String> getSubmitFare() {
		return submitFare;
	}

	public void setSubmitFare(List<String> submitFare) {
		this.submitFare = submitFare;
	}

	public List<String> getSubmitShowOrder() {
		return submitShowOrder;
	}

	public void setSubmitShowOrder(List<String> submitShowOrder) {
		this.submitShowOrder = submitShowOrder;
	}

	public List<String> getSubmitParentId() {
		return submitParentId;
	}

	public void setSubmitParentId(List<String> submitParentId) {
		this.submitParentId = submitParentId;
	}

	public ProjectPlan getPlan() {
		return plan;
	}

	public void setPlan(ProjectPlan plan) {
		this.plan = plan;
	}
}
