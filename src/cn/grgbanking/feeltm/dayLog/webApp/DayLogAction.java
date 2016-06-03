package cn.grgbanking.feeltm.dayLog.webApp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.struts2.ServletActionContext;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.common.bean.DynamicWeek;
import cn.grgbanking.feeltm.common.util.DynamicWeekUtils;
import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.costControl.bean.EnterProject;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.integralCenter.service.IntegralCenterService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.LogListenerService;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.feeltm.projectweekplan.service.ProjectWeekPlanService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Arith;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.lowagie.tools.split_pdf;
import com.opensymphony.xwork2.ActionContext;

public class DayLogAction extends BaseAction{
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	public SysUserGroupService userGroupService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	public ProjectWeekPlanService projectWeekPlanService;
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private IntegralCenterService integralCenterService;
	@Autowired
	private LogListenerService service;
	@Autowired
	private WarnInfoService warnInfoService;
	@Autowired
	private HolidayService holidayService;
	
	private DateDayLog dateDayLog;
	private DayLogVote vote;
	private List<DateDayLog> dateDayLogList; 
	private List<DayLog> daylogList;
	
	
	private List<String> daylogList_prjName;
	private List<String> daylogList_type;
	private List<String> daylogList_statu;
	private List<String> daylogList_finishRate;
	private List<String> daylogList_planOrAdd;
	private List<String> daylogList_subTotal;//总工时
	private List<String> daylogList_confirmHour;//确认工时
	private List<String> daylogList_desc;
	private List<String> daylogList_reason;
	private List<String> daylogList_confirmReason;//确认工时说明
	private List<String> daylogList_tasktype;//查询方式
	private List<String> daylogList_tasksdate;//按日期查询开始日期
	private List<String> daylogList_taskedate;//按日期查询结束日期
	private List<String> daylogList_delayReason;//延迟原因
	
	private List<String> enterlogList_confirmHour;//确认工时
	private List<String> enterlogList_confirmDesc;//确认备注
	private List<String> enterlogList_projectHour;//任务总工时
	private List<String> enterlogList_id;//userid+logdate+prjname
	private List<String> isRowSelect;//行是否被选择
	
	
	
	/**
     * calculateList:描述 <br/>
     * 
     * @return
     * @author zzwen6
     * @throws IOException 
     * @throws JSONException 
     * @修改记录:(日期,修改人,描述) (可选) <br/>
     */
    public  String calculateList() throws IOException, JSONException {
        // TODO Auto-generated method stub
        // 查询条件 calculateList
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        String from = request.getParameter("from");
        
        int pageNum = 1;
        int pageSize = 20;
        
        if (null != request.getParameter("pageNum") && request.getParameter("pageNum").length() > 0) {
            pageNum =  Integer.parseInt(request.getParameter("pageNum"));
        }
        Page page = dayLogService.getPrjCensusInfosByPages(startDate,endDate,dateDayLog==null?null:dateDayLog,pageNum,pageSize);
        request.setAttribute("currPage", page);
        List<Object[]>  censusPrjInfos  = page.getQueryResult();
        
        if ( null!=from && "refresh".equals(from)) {
            
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonObj", dayLogService.getPrjCensusInfos(censusPrjInfos));
            jsonObject.put("pageCount", String.valueOf(page.getPageCount()));
            jsonObject.put("recordCount", String.valueOf(page.getRecordCount()));
             
            PrintWriter out = response.getWriter();
             
            //System.out.println(jsonObject);
            out.print(jsonObject);
            
            
            
            return null;
        }else{
            // 将数据以list<map>封装出去
            ActionContext.getContext().put("censusPrjInfos", dayLogService.getPrjCensusInfos(censusPrjInfos));
        }
        return "list";
    }
    
    
	/** 转向增加页面
	 * @return
	 */
	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
		request.setAttribute("userLevel", staffInfoService.getLevelName(userModel.getUserid()));
		request.setAttribute("deptNameStr", staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
		request.setAttribute("usr", usr);
		
		//获取当前用户参与的项目
		List<Project> projects=projectService.getProjectByUserId(userModel.getUserid());
		
		//获取名称为"其他项目"的项目
		Project otherProject=projectService.getOtherProject();
		projects=projects==null?new ArrayList<Project>():projects;
		projects.add(otherProject);
		request.setAttribute("projects", projects);
		
		
		//参与的项目中的任务
		List<ProjectWeekPlanTask> taskList=new ArrayList<ProjectWeekPlanTask>();
		if(projects!=null && projects.size()>1){
			taskList.addAll(getTaskListInProject(projects.get(0).getId()));
		}
		ProjectWeekPlanTask otherTask=new ProjectWeekPlanTask();
		otherTask.setTaskContent("其他任务");
		otherTask.setId("其他任务");
		taskList.add(otherTask);
		request.setAttribute("tasks", taskList);

		
		return "add";
	}
	
	/** 保存新增日志
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		String msg ="";//保存积分返回提示
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//获取用户
			SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
			
			if(daylogList!=null && daylogList.size()>0){
			
				
				//获取新增的时间
				Date logDate=Calendar.getInstance().getTime();
				if(StringUtils.isNotBlank(request.getParameter("submitCreateDate"))){
					logDate=new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("submitCreateDate"));
				}
				//新增的时候需要检查，当前的日期中有没有日志，如果有，则新增不成功，同时提示可以修改该日期的日志
				boolean hasDaylogList=dayLogService.hasDayLogList(userModel.getUserid(),logDate);
				if(hasDaylogList){
					MsgBox msgBox1 = new MsgBox(request,getText("新增失败：已经存在该日期的日志，请直接修改!"));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}

 				//判断月份，不能对日志进行跨月修改
				// @author lffei1
				// 时间：2016年3月28日09:27:30
				String getDate = request.getParameter("submitCreateDate"); 
//				int getYear = (Integer.parseInt(getDate.substring(0,4)));
//				int getMonth = Integer.parseInt(getDate.substring(5,7));
				
                String[] objs = getDate.split("-");
                int getYear = (Integer.parseInt(objs[0]));
                int getMonth = Integer.parseInt(objs[1]);

                Calendar nowDate = Calendar.getInstance();
                int nowYear = nowDate.get(Calendar.YEAR);
                int nowMonth = nowDate.get(Calendar.MONTH);
                if (getYear == nowYear) {
                    if (getMonth < nowMonth) {
                        MsgBox otherBox = new MsgBox(request, getText("新增失败：您不能对上个月的日志进行修改!"));
                        otherBox.setButtonType(MsgBox.BUTTON_CLOSE);
                        return "msgBox";
                    }
                }
                if (getYear < nowYear) {
                    MsgBox otherBox = new MsgBox(request, getText("新增失败：您不能对上一年的日志进行修改!"));
                    otherBox.setButtonType(MsgBox.BUTTON_CLOSE);
                    return "msgBox";
                }

				//================================================
			    
			    double sumWorkhours = 0d;
			    double allprojectHour = 0.0;
			    for(int i=0;i<daylogList.size();i++){
			    	DayLog log=daylogList.get(i);
			    	allprojectHour += log.getSubTotal();
			    }
				//对对象中的每个属性添加上没有通过structs注入的值
				for(int i=0;i<daylogList.size();i++){
					DayLog log=daylogList.get(i);
					sumWorkhours += log.getSubTotal();
					//根据计划任务id,得出计划任务名称
					String plan_taskid = log.getDesc();
					String desc=projectWeekPlanService.getPlanTaskDescById(plan_taskid);
					log.setDesc(desc);
					log.setPlan_taskid(plan_taskid);
					//用户部门
					log.setDetName(staffInfoService.getDeptNameValueByKey(usr.getDeptName()));
					//添加日志时间
					log.setLogDate(logDate);
					//首次填写日志的时间
					log.setFillLogDate(Calendar.getInstance().getTime());
					//更新人
					log.setUpdateman(usr.getUsername());
					//更新时间
					log.setUpdateTime(Calendar.getInstance().getTime());
					//添加日志人id
					log.setUserId(usr.getUserid());
					//添加日志用户名
					log.setUserName(usr.getUsername());
					//添加用户的状态  
					String status = usr.getStatus();
					if(status != null && !"".equals(status)){
						log.setEmploystatus(status);
					}
					
					/**
                     * 更改请假值，从页面传过来是on->1 ，其他->''
                     * 勾选的是 'on" 没有的是null
                     * zzwen6 
                     * 2016年3月29日14:20:28
                     */
                     if ("on".equals(log.getIsLeave())) {
                        log.setIsLeave(Constants.LEAVE);
                    }else {
                        log.setIsLeave(Constants.NOT_LEAVE);
                    } 
                    
					//实习、试用、正式、离职
					//添加用户所在组（修改为用户所在项目）
					try{
						log.setGroupName(projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName()));
					}catch(Exception e){e.printStackTrace();}
					//设置用户级别
					log.setUserLevel(staffInfoService.getLevelName(userModel.getUserid()));
					//设置审核状态
					log.setAuditStatus("新增");
					//默认确认工时=工时
					log.setConfirmHour(log.getSubTotal());
					//默认确认状态为“未确认”
					log.setConfirmStatus("0");
					//通过Web端填写的日志
					log.setDeviceType("web");
					//项目人日
					log.setProjectDay(Arith.round(Arith.div(log.getSubTotal(),allprojectHour),2));
					//确认人日
					Double enterDay = Arith.round(Arith.div(log.getSubTotal(),allprojectHour)*(Arith.div(log.getConfirmHour(),log.getSubTotal())),2);
					log.setEnterDay(enterDay);
					//确认角色
					String role=dayLogService.getProjectManagerByPrjname(log.getGroupName());
					log.setEnterRole(role);
				}
				if (sumWorkhours > 24) {
					MsgBox overWorkhoursBox = new MsgBox(request,getText("您当天日志总时长超过24小时,请重新确认工时"));
					overWorkhoursBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				boolean flag=dayLogService.saveDayLogList(daylogList);
				if (flag == true) {
					/**
					 * add by whxing
					 * 添加积分
					 */
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("usr", usr);
					paramMap.put("daylogList",daylogList);
					paramMap.put("logDate", logDate);
					msg = integralCenterService.saveIntegralInfoByLogDayParamMap(paramMap);
					
					/**
					 * add by tling1
					 * 添加日志后，更新周计划的完成度、偏移度及确认工时 
					 */
					for(int i=0;i<daylogList.size();i++){
						DayLog log=daylogList.get(i);
						projectWeekPlanService.getDataForDayLogToPrjWeekPlan(log.getUserId(), log.getPlan_taskid());
					}
					MsgBox msgBox = new MsgBox(request, getText("add.ok")+msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
					
					
					
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.addfaile"));
					addActionMessage(getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.addfaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	
	/** 转向修改页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id=request.getParameter("ids");
		request.getSession().setAttribute("dayLogId", id);
		DayLog daylog= dayLogService.getCaseById(id);
		String loginUserId = userModel.getUserid();
		boolean isLogUser = false;
		if(loginUserId.equals(daylog.getUserId())){
			isLogUser = true;
		}
		request.setAttribute("isLogUser", isLogUser);
		//获取当前用户选取日期的所有日志
		daylogList=dayLogService.getReportsByDay(daylog.getUserId(), daylog.getLogDate());
		
		for(DayLog log:daylogList){
			//设置默认的确认时长
			if(log.getConfirmHour()==null || log.getConfirmHour()<0){
				log.setConfirmHour(log.getSubTotal());
			}
		}
		
		boolean hasComfirmRight = UserRoleConfig.hasConfirmRight(userModel);//确认工时权限
		request.setAttribute("hasComfirmRight", hasComfirmRight);
		//没有确认工时权限，即项目经理级别以下的只能编辑自己的日志
		if(!userModel.getUserid().equals(daylog.getUserId())){//只能编辑自己的日志
			MsgBox msgBox = new MsgBox(request,getText("提示:您不能操作其他用户的数据!",new String[]{"您不能操作其他用户的数据"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}			
		//已确认工时日志只有项目经理级别以上能修改
		String confirmStatus = daylog.getConfirmStatus();
		if("1".equals(confirmStatus)){
			MsgBox msgBox = new MsgBox(request,getText("您不能修改已确认工时的日志，如需修改请联系项目经理!",new String[]{"您不能修改已确认工时的日志，如需修改请联系项目经理!"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}	
		
		//是否在允许修改的时间范围内
		if(!isAllowedChange(daylog.getLogDate())){
			MsgBox msgBox = new MsgBox(request,getText("提示:您只能修改最近一周的日志!",new String[]{"您只能修改最近一周的日志"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		Date logDate=daylog.getLogDate();
		
		/*//只能编辑当月的日志
		Calendar logCal=Calendar.getInstance();
		logCal.setTime(logDate);
		Calendar nowCal=Calendar.getInstance();
		if(!(logCal.get(Calendar.YEAR)==nowCal.get(Calendar.YEAR) && logCal.get(Calendar.MONTH)==nowCal.get(Calendar.MONTH))){
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"只能操作本月的日志，该日志已经被封存"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}*/
		
		
		//创建日志的人的信息
		request.setAttribute("logUserIdStr", daylog.getUserId());
		request.setAttribute("logUserNameStr",daylog.getUserName());
		request.setAttribute("logDeptNameStr", staffInfoService.getDeptNameValueByUserId(daylog.getUserId()).trim());
		
		//获取当前用户参与的项目
		List<Project> projects=projectService.getProjectByUserId(userModel.getUserid());
		//获取名称为"其他项目"的项目
		Project otherProject=projectService.getOtherProject();
		projects=projects==null?new ArrayList<Project>():projects;
		projects.add(otherProject);
		request.setAttribute("projects", projects);
		//获取所有未完结项目
		List<Project> allProject = projectService.listUnFinishedGroup();
		request.setAttribute("allProject", allProject);
		request.setAttribute("logDate",new SimpleDateFormat("yyyy-MM-dd").format(logDate));
		//如果当前登录人是日志的创建者，并且有项目经理的审核意见，就把审核意见放到页面显示
		if(userModel.getUserid().equals(daylog.getUserId())&&("审核通过".equals(daylog.getAuditStatus())||"审核不通过".equals(daylog.getAuditStatus()))){
			request.setAttribute("showAudit", true);
			if(StringUtils.isNotBlank(daylog.getAuditLog())){
				request.setAttribute("showAuditContent", true);
			}
			request.setAttribute("auditMan", daylog.getAuditMan());
			request.setAttribute("auditTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(daylog.getAuditTime()));
			request.setAttribute("auditResult", daylog.getAuditStatus());
			request.setAttribute("auditLog", daylog.getAuditLog());
		}
		
		return "edit";
	}
	
	/**是否在允许修改的时间范围内
	 * wtjiao 2014年7月17日 下午1:32:51
	 * @return
	 */
	private boolean isAllowedChange(Date logDate) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(logDate);
		cal.add(Calendar.DAY_OF_YEAR, 15);
		Calendar now=Calendar.getInstance();
		if(cal.after(now)){
			return true;
		}
		return false;
	}

	private boolean isProjectManagerOrInProjectManageGroup(UserModel userModel,List<DayLog> dlList) {
		try{
			boolean isProjectManager=isCurDaylogProjectManager(userModel.getUserid(),dlList);
			boolean isInProjectManageGroup=UserRoleConfig.isInPrjManageGroup(userModel);
			
			return isInProjectManageGroup||isProjectManager;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/** 修改后保存
	 * @return
	 * @throws Excetion
	 */
	public String update() throws Exception{
		//检查添加日期
		if(StringUtils.isNotBlank(request.getParameter("submitCreateDate"))){
			Date submitDate=new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("submitCreateDate"));
			if(!isAllowedChange(submitDate)){
				MsgBox msgBox1 = new MsgBox(request,getText("修改失败：日志提交日期只能延后15天"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		//修改前的日期
		Date submitOldDate=new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("submitOldDate"));
		//获取用户
		SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
		
		String logUserId=request.getParameter("logUserIdStr");
		//构造列表
		daylogList=buildDayLogList();
		//对对象中的每个属性添加上没有通过structs注入的值
		double sumWorkhours = 0d;
		double allprojectHour = 0.0;
		if(daylogList!=null){
			for(int i=0;i<daylogList.size();i++){
				DayLog log=daylogList.get(i);
				allprojectHour += log.getSubTotal();
			}
		}
		if(daylogList!=null){
			for(int i=0;i<daylogList.size();i++){
				DayLog log=daylogList.get(i);
				sumWorkhours += log.getSubTotal();
				String plan_taskid = log.getDesc();
				String desc=projectWeekPlanService.getPlanTaskDescById(plan_taskid);
				log.setDesc(desc);
				log.setPlan_taskid(plan_taskid);
				//添加日志人id
				log.setUserId(logUserId);
				//添加日志用户名
				log.setUserName(staffInfoService.findUserByUserid(logUserId).getUsername());
				//用户部门
				log.setDetName(staffInfoService.getDeptNameValueByKey(staffInfoService.findUserByUserid(logUserId).getDeptName()));
				//设置用户级别
				log.setUserLevel(staffInfoService.getLevelName(logUserId));
				String  employStatus = usr.getStatus();
				if( employStatus !=null && !"".equals(employStatus) ){
					log.setEmploystatus(employStatus);
				}
				//修改的时候一样一样要设置日志用户的状态
//				boolean hasComfirmRight = UserRoleConfig.hasConfirmRight(userModel);//确认工时权限				
//				//拥有权限权限则保存确认信息
//				if(hasComfirmRight){					
//					//确认状态
//					//log.setConfirmStatus("1");//已确认
//					log.setConfirmStatus("0");//不再进行单个数据的确认，统一用批量审批
//					//确认人
//					log.setConfirmMan(usr.getUsername());
//					//确认时间
//					log.setConfirmTime(Calendar.getInstance().getTime());
//				}else{
					//确认状态
					log.setConfirmStatus("0");//未确认
					//确认人
					log.setConfirmMan(null);
					//确认时间
					log.setConfirmTime(null);
					//默认确认工时=工时
					log.setConfirmHour(log.getSubTotal());
//				}
				//更新人
				log.setUpdateman(usr.getUsername());
				//更新时间
				log.setUpdateTime(Calendar.getInstance().getTime());
				//更新为web端填写
				log.setDeviceType("web");
				//项目人日
				log.setProjectDay(Arith.round(Arith.div(log.getSubTotal(),allprojectHour),2));
				//确认人日
				Double enterDay = Arith.round(Arith.div(log.getSubTotal(),allprojectHour)*(Arith.div(log.getConfirmHour(),log.getSubTotal())),2);
				log.setEnterDay(enterDay);
				//确认角色
				String role=dayLogService.getProjectManagerByPrjname(projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName()));
				log.setEnterRole(role);
				
				//添加日志时间
				if(StringUtils.isNotBlank(request.getParameter("submitCreateDate"))){
					log.setLogDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("submitCreateDate")));
				}else{
					log.setLogDate(Calendar.getInstance().getTime());
				}
				String logdate = DateUtil.getDateString(log.getLogDate());
				DayLog logTemp = dayLogService.queryDayLogByUseridLogDate(logUserId,logdate);
				if (logTemp != null) {
					log.setFillLogDate(logTemp.getFillLogDate());
				}else{
					log.setFillLogDate(log.getLogDate());
				}
				
				//添加用户所在组(修改为所在项目)
				try{
					log.setGroupName(projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName()));
				}catch(Exception e){e.printStackTrace();}
				log.setAuditStatus("新增");
			}
		}
		if (sumWorkhours > 24) {
			MsgBox overWorkhoursBox = new MsgBox(request,getText("您当天日志总时长超过24小时,请重新确认工时"));
			overWorkhoursBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		try {
			boolean flag=dayLogService.updateAll(logUserId,submitOldDate,daylogList);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				
				/**
				 * add by whxing
				 * 修改后添加积分
				 */
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("usr", usr);
				paramMap.put("daylogList",daylogList);
				Date logDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("submitCreateDate"));
				paramMap.put("logDate", logDate);
			    integralCenterService.saveIntegralInfoByLogDayParamMap(paramMap);
			    
			    /**
				 * add by tling1
				 * 添加日志后，更新周计划的完成度、偏移度及确认工时 
				 */
				for(int i=0;i<daylogList.size();i++){
					DayLog log=daylogList.get(i);
					projectWeekPlanService.getDataForDayLogToPrjWeekPlan(log.getUserId(), log.getPlan_taskid());
				}
			} else {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	

	private List<DayLog> buildDayLogList() {
		List list=new ArrayList();
		if(daylogList_prjName!=null && daylogList_prjName.size()>0){
			for(int i=0;i<daylogList_prjName.size();i++){
				DayLog log=new DayLog();
				log.setDesc(daylogList_desc.get(i));
				log.setFinishRate(daylogList_finishRate.get(i));
				log.setPlanOrAdd(daylogList_planOrAdd.get(i));
				log.setPrjName(daylogList_prjName.get(i));
				log.setReason(daylogList_reason.get(i));
				log.setConfirmHour(Double.parseDouble(StringUtils.isBlank(daylogList_confirmHour.get(i))?daylogList_subTotal.get(i):daylogList_confirmHour.get(i)));//确认工时
				log.setConfirmDesc(daylogList_confirmReason.get(i));//确认工时说明
				log.setStatu(daylogList_statu.get(i));
				log.setSubTotal(Double.parseDouble(daylogList_subTotal.get(i)));
				log.setType(daylogList_type.get(i));
				DayLog logList=daylogList.get(i);
				log.setTasktype(logList.getTasktype());
				log.setTasksdate(logList.getTasksdate());
				log.setTaskedate(logList.getTaskedate());
				log.setDelay_reason(daylogList_delayReason.get(i));
				list.add(log);
			}
		}
		return list;
	}

	/**删除某天的日志
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id=request.getParameter("ids");
		DayLog daylog= dayLogService.getCaseById(id);
		List<DayLog> dellog= dayLogService.getReportsByDay(daylog.getUserId(),daylog.getLogDate());
		//只能删除自己的日志
		if(!userModel.getUserid().equals(daylog.getUserId())){
			MsgBox msgBox = new MsgBox(request,getText("提示：您不能操作其他用户的数据",new String[]{"您不能操作其他用户的数据"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		//是否在删除的范围内
		if(!isAllowedChange(daylog.getLogDate())){
			MsgBox msgBox = new MsgBox(request,getText("提示:您只能删除最近15天的日志!",new String[]{"您只能删除最近15天的日志"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		//只能删除状态为新增和未确认的日志
		daylogList=dayLogService.getReportsByDay(daylog.getUserId(), daylog.getLogDate());
		if(daylogList!=null && daylogList.size()>0){
			if(!"新增".equals(daylogList.get(0).getAuditStatus())){
				MsgBox msgBox = new MsgBox(request,getText("提示：只能删除新增的日志",new String[]{"只能删除新增的日志"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		if(dayLogService.remove(daylog.getUserId(),daylog.getLogDate())){
			
			/**
			 * add by whxing
			 * 删除日志对应删除积分
			 */
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userId", userModel.getUserid());
			paramMap.put("logDate", daylog.getLogDate());
			integralCenterService.deleteIntegralInfoByParam(paramMap);
			
			/**
			 * add by tling1
			 * 删除日志后，更新周计划的完成度、偏移度及确认工时 
			 */
			for(DayLog log:dellog){
				projectWeekPlanService.getDataForDayLogToPrjWeekPlan(log.getUserId(), log.getPlan_taskid());
			}
			
			MsgBox msgBox = new MsgBox(request, getText("该天的记录删除成功")+",同时扣取相应积分", "工作报告删除信息页面");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			
			
		}
		return "msgBox";
	}

	
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	
	/**查询详细信息
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		String operate = request.getParameter("operate");
		try {
			String ids = request.getParameter("ids");
			request.getSession().setAttribute("dayLogId", ids);
			DayLog daylog=dayLogService.getCaseById(ids);
			//获取点击条目所代表的用户和日期的日志列表
			daylogList=dayLogService.getReportsByDay(daylog.getUserId(), daylog.getLogDate());
			//设置用户和组别信息
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr = staffInfoService.findUserByUserid(userModel
					.getUserid());
			Date today=new Date();
			//用户级别
			request.setAttribute("userLevel", staffInfoService.getLevelName(userModel.getUserid()));
			//检查用户是否拥有确认工时权限，是则跳转到修改页面
			boolean hasComfirmRight = UserRoleConfig.hasConfirmRight(userModel);//确认工时权限
			request.setAttribute("hasComfirmRight", hasComfirmRight);
//			if(hasComfirmRight){
//				return edit();			
//			}
			//获取当前用户选取日期的所有日志
			daylogList=dayLogService.getReportsByDay(daylog.getUserId(), daylog.getLogDate());
			
			//将projectcode转为projectname
			if(daylogList!=null){
				for(int i=0;i<daylogList.size();i++){
					try{
						String prjname=projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName());
						daylogList.get(i).setPrjName(projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName()));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			//获取当前用户日期的历史点评列表
			List<DayLogVote> voteHistoryList=dayLogService.getVoteHistoryList(daylog);
			request.setAttribute("voteHistoryList",voteHistoryList);
			
			//日志日期
			request.setAttribute("logDate",new SimpleDateFormat("yyyy-MM-dd").format(daylog.getLogDate()));
			//点评人信息：关联的DateDayLog
			request.setAttribute("dateDaylogId",daylog.getId());
			
			//被查询人信息：用户姓名
			request.setAttribute("userNameStr",daylog.getUserName().trim());
			//被查询人信息：用户部门
			request.setAttribute("deptNameStr",daylog.getDetName().trim());
			
			//点评人信息：用户Id
			request.setAttribute("votePersonId",userModel.getUserid());
			//点评人信息：用户姓名
			request.setAttribute("votePersonUserName",staffInfoService.findUserByUserid(userModel.getUserid()).getUsername().trim());
			//点评人信息：用户部门
			request.setAttribute("voatePersonDeptName", staffInfoService.getDeptNameValueByUserId(userModel.getUserid()).trim());
			//点评人信息: 点评时间
			request.setAttribute("voteTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			
			//如果当前登录人是日志的创建者，并且有项目经理的审核意见，就把审核意见放到页面显示
			if(userModel.getUserid().equals(daylog.getUserId())&&("审核通过".equals(daylog.getAuditStatus())||"审核不通过".equals(daylog.getAuditStatus()))){
				request.setAttribute("showAudit", true);
				if(StringUtils.isNotBlank(daylog.getAuditLog())){
					request.setAttribute("showAuditContent", true);
				}
				request.setAttribute("auditMan", daylog.getAuditMan());
				request.setAttribute("auditTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(daylog.getAuditTime()));
				request.setAttribute("auditResult", daylog.getAuditStatus());
				request.setAttribute("auditLog", daylog.getAuditLog());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if("remark".equals(operate)){
			return "remark";
		}
		return "show";
	}
	
	
	public String getTaskInProject(){
		try{
			String type = request.getParameter("type");
			Date sdate = null;
			Date edate = null;
			String queryDateStr=request.getParameter("queryDate");
			if("0".equals(type)){
				if(StringUtils.isNotBlank(queryDateStr)){
					sdate=new SimpleDateFormat("yyyy-MM-dd").parse(queryDateStr);
					edate=sdate;
				}else{
					sdate = Calendar.getInstance().getTime();
					edate = Calendar.getInstance().getTime();
				}
			}else if("1".equals(type)){
				//得出当前周的日期
//				DynamicWeek dw=DynamicWeekUtils.getDateDynamicWeek(new Date());
//				String startTime=new SimpleDateFormat("yyyy-MM-dd").format(dw.getStartTime());
//				String endTime=new SimpleDateFormat("yyyy-MM-dd").format(dw.getEndTime());
//				sdate = DateUtil.stringToDate(startTime, "yyyy-MM-dd");
//				edate = DateUtil.stringToDate(endTime, "yyyy-MM-dd");
				sdate = DateUtil.stringToDate(DateUtil.getWeekDate(DateUtil.stringToDate(queryDateStr, "yyyy-MM-dd"))[0], "yyyy-MM-dd");
				edate = DateUtil.stringToDate(DateUtil.getWeekDate(DateUtil.stringToDate(queryDateStr, "yyyy-MM-dd"))[1], "yyyy-MM-dd");
			}else if("2".equals(type)){
//				Calendar cal = Calendar.getInstance();
//				int year = cal.get(Calendar.YEAR);
//				int month = cal.get(Calendar.MONTH)+1;
//				int day = DateUtil.getLastDayForYearAndMonth(year, month);
//				String startTime = year + "-" + month + "-" + "01";
//				String endTime = year + "-" + month + "-" + day;
//				sdate = DateUtil.stringToDate(startTime, "yyyy-MM-dd");
//				edate = DateUtil.stringToDate(endTime, "yyyy-MM-dd");	
				String startdate = request.getParameter("sdate");
				String enddate = request.getParameter("edate");
				if(!"".equals(startdate) && !"".equals(enddate)){
					sdate = DateUtil.stringToDate(startdate, "yyyy-MM-dd");
					edate = DateUtil.stringToDate(enddate, "yyyy-MM-dd");
				}
				if(!"".equals(startdate) && "".equals(enddate)){
					sdate = DateUtil.stringToDate(startdate, "yyyy-MM-dd");
					edate = sdate;
				}
				if("".equals(startdate) && !"".equals(enddate)){
					sdate = DateUtil.stringToDate(enddate, "yyyy-MM-dd");
					edate = sdate;
				}
			}
	        String  str=net.sf.json.JSONArray.fromObject(getTaskInProjectByTime(sdate,edate,null)).toString();
	        ajaxPrint(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List getTaskListInProject(String projectId){
		try{
			Date queryDate=null;
			String queryDateStr=request.getParameter("queryDate");
			if(StringUtils.isNotBlank(queryDateStr)){
				queryDate=new SimpleDateFormat("yyyy-MM-dd").parse(queryDateStr);
			}else{
				//查询当日
				queryDate=Calendar.getInstance().getTime();
			}
			Date startDate=queryDate;
			Date endDate=queryDate;
	        return getTaskInProjectByTime(startDate,endDate,projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List getTaskInProjectByTime(Date startDate,Date endDate,String projectId){
		if(request.getParameter("projectId")!=null){
 			projectId=request.getParameter("projectId");
		}
		//当前用户
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		//如果是项目经理修改确认工时时，计划任务是按当前用户来展示数据的，在此加判断是为了列出当前日志人员的计划任务
		String userid=request.getParameter("userid");
		if(userid==null || userid.equals("")){
			userid = userModel.getUserid();
		}
		List<ProjectWeekPlanTask> taskList;
		if(!"otherProject".equals(projectId)){
			taskList= projectWeekPlanService.getTaskInProject(projectId, userid, startDate, endDate);
		}else{
			taskList=new ArrayList<ProjectWeekPlanTask>();
		}
		if(taskList!=null){
			for(int i=0;i<taskList.size();i++){
				String sdate = DateUtil.getDateString(taskList.get(i).getStartDate());
				taskList.get(i).setDesc(sdate);//获取计划任务是，用于显示开始日期的
				taskList.get(i).setStartDate(null);
				taskList.get(i).setEndDate(null);
			}
		}
		return taskList;
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

	/** 查询
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			
			//判断当前用户当天是否填写了日志   若填写了  将dosewritedaylog =true   否则dosewritedaylog=false
			//若当前的用户填写了日志   则不允许再次添加日志  
			
			String userId=userModel.getUserid();
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String today=sdf.format(date);
			boolean dosewritedaylog=dayLogService.getUserTodayDaylog(userId,today);
			if(dosewritedaylog){
				request.setAttribute("dosewritedaylog", "yes");
			}else{
				request.setAttribute("dosewritedaylog", "no");
			}
			
			
			
			
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String queryStartTime=null;
			String queryEndTime=null;
			boolean isCheck=false;
			if(request.getParameter("isCheck") !=null && request.getParameter("isCheck").length()>0){
				isCheck=Boolean.parseBoolean(request.getParameter("isCheck"));
			}
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
				queryStartTime = request.getParameter("queryStartTime");
			if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
				queryEndTime = request.getParameter("queryEndTime");
			
			String deptName="";
			String groupName="";
			String userName="";
			String confirmStatus = request.getParameter("confirmStatus");
			if(!(from != null && from.equals("refresh"))){
				dateDayLog=new DateDayLog();
				//是部门经理，默认查询当前部门
				if(UserRoleConfig.isDeptManageGroup(userModel)){
					deptName=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
					dateDayLog.setUserDept(deptName);
					userName="";
				}
				/*else if(UserRoleConfig.isProjectManageGroup(userModel)){//项目经理，默认查询第一个项目
					try{
						groupName=projectService.getProjectNameByUserid(userModel.getUserid());
						groupName=groupName.indexOf(',')>0?groupName.substring(0,groupName.indexOf(',')):groupName;
						dateDayLog.setUserGroup(groupName);
						userName="";
					}catch(Exception e){}
				}*/
				else{//查询个人
					dateDayLog.setUserName(userModel.getUsername());
					userName=userModel.getUsername();
				}
				
				//默认查询当周的日志
				DynamicWeek dw=DynamicWeekUtils.getDateDynamicWeek(new Date());
				queryStartTime=new SimpleDateFormat("yyyy-MM-dd").format(dw.getStartTime());
				queryEndTime=new SimpleDateFormat("yyyy-MM-dd").format(dw.getEndTime());
				
				//list页面被选中的查询条件：用户姓名
				request.setAttribute("queryUserName",userName);
				//list页面被选中的查询条件：用户部门
				request.setAttribute("queryDeptName", deptName);
				//list页面被选中的查询条件：项目
				request.setAttribute("queryGroupName",groupName);
				//list页面被选中的查询条件：开始时间
				request.setAttribute("queryStartTime", queryStartTime);
				//list页面被选中的查询条件：结束时间
				request.setAttribute("queryEndTime", queryEndTime);
			}
			dateDayLog.setConfirmStatus(confirmStatus);
			Page page = dayLogService.getPage(dateDayLog,queryStartTime,queryEndTime,pageNum, pageSize,isCheck);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				//公司的json转换
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.dayLog.bean.DateDayLog");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);

				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("dateDayLogList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	/** 保存日志点评
	 * @return
	 * @throws Exception
	 */
	public String saveVote() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//获取用户
			SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
			//获取项目
			String group=projectService.getProjectNameByUserid(userModel.getUserid());
			vote.setDaylogId((String) request.getSession().getAttribute("dayLogId"));
			vote.setVoterGroupName(group);
			//保存评论
			dayLogService.saveDayLogVote(vote);
			
			MsgBox msgBox = new MsgBox(request, "点评成功!");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("点评成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,"点评成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("点评失败");
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	
	/**
	 * 提交审核
	 * @return
	 */
	public String upauditing(){
		try{
			String ids = request.getParameter("ids");
			
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			DayLog dl=dayLogService.getCaseById(ids);
			daylogList=dayLogService.getReportsByDay(dl.getUserId(), dl.getLogDate());
			if(daylogList!=null && daylogList.size()>0){
				DayLog firstDaylog=daylogList.get(0);
				
				if(!userModel.getUserid().equals(firstDaylog.getUserId())){
					MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不是该日志的创建者，不能提交审核该日志"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				if("待审核".equals(firstDaylog.getAuditStatus())){
					MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"日志已经提交审核，请等待项目经理审核"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				if(!("".equals(firstDaylog.getAuditUser()) && ("新增".equals(firstDaylog.getAuditStatus()) || "审核不通过".equals(firstDaylog.getAuditStatus())))){
					if(!userModel.getUserid().equals(firstDaylog.getUserId())){
						MsgBox msgBox = new MsgBox(request,"您无权提交审核该记录");
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					for(int i=0;i<daylogList.size();i++){
						DayLog daylog=daylogList.get(i);
						daylog.setAuditStatus("待审核");
						String userid=userModel.getUserid();
						String username=staffInfoService.getUsernameById(userid);
						daylog.setUpdateTime(new Date());
						daylog.setUpdateman(username);
						dayLogService.updateDayLog(daylog);
					}	
				}else{
					MsgBox msgBox = new MsgBox(request,"只能提交状态为新增、审核不通过的记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request,"只能提交状态为新增、审核不通过的记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		MsgBox msgBox = new MsgBox(request,"提交审核成功，项目经理将对日志进行审核");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	/**转向审核页面
	 * wtjiao 2014年7月2日 上午10:38:23
	 * @return
	 * @throws Exception
	 */
	public String toAuditing() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			
			DayLog dl=dayLogService.getCaseById(request.getParameter("ids"));
			daylogList=dayLogService.getReportsByDay(dl.getUserId(), dl.getLogDate());
			
			if(daylogList!=null && daylogList.size()>0){
				//20140703 wtjiao 修改为只要有审核权限的，都能修改
				
				/*//判断当前用户是不是日志中某个项目的项目经理
				boolean isCurDaylogProjectManager=isCurDaylogProjectManager(userModel.getUserid(),daylogList);
				
				//不是项目经理，则没有权限审核
				if(!isCurDaylogProjectManager){
					MsgBox msgBox = new MsgBox(request,"该日志中没有您管理的项目，您无权审核");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}*/
				
				if(!"待审核".equals(daylogList.get(0).getAuditStatus())){
					MsgBox msgBox = new MsgBox(request,"只能审核状态为待审核的日志");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				//------------------------------------------ 转向审核页面 ------------------------------------------------------
				
				
				//获取当前用户选取日期的所有日志
				daylogList=dayLogService.getReportsByDay(dl.getUserId(), dl.getLogDate());
				request.setAttribute("userNameStr",userModel.getUsername().trim());
				request.setAttribute("deptNameStr", staffInfoService.getDeptNameValueByUserId(userModel.getUserid()).trim());
				request.setAttribute("userLevel", staffInfoService.getLevelName(userModel.getUserid()));
				
				//将projectcode转为projectname
				if(daylogList!=null){
					for(int i=0;i<daylogList.size();i++){
						daylogList.get(i).setPrjName(projectWeekPlanService.getProjectName(daylogList.get(i).getPrjName()));
					}
				}

				request.setAttribute("logDate",new SimpleDateFormat("yyyy-MM-dd").format(dl.getLogDate()));
				request.setAttribute("firstDaylogId", dl.getId());
				
				//审核记录
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				List<ApprovalRecord> records = approvalService.getRecordByName(dayLogService.getFirstDaylogId(daylogList));
				int i = 0;
				String record = "";
				for(ApprovalRecord r:records){
					i++;
					record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
					record += "\n";
				}
				request.setAttribute("record", record);
				return "audit";	
				
				
			}else{
				MsgBox msgBox = new MsgBox(request,"该日志中没有项目");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,"只能修改状态为新增、审核不通过的记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	 }
	
	/**指定人员是否担任日志列表中的某个项目的项目经理
	 * wtjiao 2014年7月2日 上午11:21:00
	 * @param userid
	 * @param daylogList2
	 * @return
	 */
	private boolean isCurDaylogProjectManager(String userid,List<DayLog> dlList) {
		//找出该日志中各个项目的项目经理
		if(daylogList!=null && daylogList.size()>0){
			for(int i=0;i<daylogList.size();i++){
				DayLog daylog=daylogList.get(i);
				String projectId=daylog.getPrjName();//name在保存时存入的是id
				Project project=projectService.getProjectById(projectId);//获取项目
				if(userid.equals(project.getProManagerId())){
					return true;
				}
			}
		}
		return false;
	}
	
	public String toExport(){
		try {
			return "export";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 导出excel表
	 * @return
	 * @throws Exception
	 * zqsheng1
	 * 2014年7月2日
	 */
	@SuppressWarnings("rawtypes")
	public String exportData() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		request.setCharacterEncoding("utf-8");
		String queryStartTime=null;
		String queryEndTime=null;
		if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
			queryStartTime = request.getParameter("queryStartTime");
		if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
			queryEndTime = request.getParameter("queryEndTime");
		
		String exportTimeStr=request.getParameter("timeGroup");
		String exportArea=request.getParameter("areaGroup");
		
		//导出指定时间指定范围的日志
		if(StringUtils.isNotBlank(exportTimeStr)&&StringUtils.isNotBlank(exportArea)){
			dateDayLog=new DateDayLog();
			
			if("day".equals(exportTimeStr)){
				queryStartTime=queryEndTime=fo.format(Calendar.getInstance().getTime());
			}else if("week".equals(exportTimeStr)){
				DynamicWeek dw=DynamicWeekUtils.getDateDynamicWeek(Calendar.getInstance().getTime());
				queryStartTime=fo.format(dw.getStartTime());
				queryEndTime=fo.format(dw.getEndTime());
			}else if("month".equals(exportTimeStr)){
			    //获取当前月第一天：
		        Calendar c = Calendar.getInstance();    
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        queryStartTime= fo.format(c.getTime());
		        
		        //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		        queryEndTime= fo.format(ca.getTime());
			}
				
			if("person".equals(exportArea)){
				dateDayLog.setUserName(staffInfoService.findUserByUserid(userModel.getUserid()).getUsername());
			}else if("project".equals(exportArea)){
				List groupList=projectService.getProjectByUserId(userModel.getUserid());
				if(groupList!=null&&groupList.size()>0){
					dateDayLog.setUserGroup(((Project)groupList.get(0)).getName());
				}
			}else if("dept".equals(exportArea)){
				dateDayLog.setUserDept(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
			}
		}
		List<DateDayLog> datelogList  = dayLogService.queryDateDaylogList(dateDayLog, queryStartTime, queryEndTime);
	
		Date date = new Date();
		String filename = getWorkSheetName(dateDayLog,datelogList,queryStartTime,queryEndTime);//设置文件名
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
		wb = this.setExcelValue(wb, 0, "姓名", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "日期", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务类别", 0,(short) 2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务内容", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "补充说明",0, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务工作量(人时)", 0, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "每日工作量(人时)", 0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "完成%", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "状态", 0, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "计划/新增", 0, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)10, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "所属部门", 0, (short)11, cellNameStyle);
		
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;
		for(int i=0;i<datelogList.size();i++){
			List daylogList=datelogList.get(i).getDaylogList();
			int sumSubTotal=0;
			for(int j=0;j<daylogList.size();j++){
				sumSubTotal+=((DayLog)daylogList.get(j)).getSubTotal();
			}
			for(int j=0;j<daylogList.size();j++){
				++sumRow;
				DayLog daylog = (DayLog) daylogList.get(j);
				HSSFRow row = sheet.createRow(sumRow);
				row.setHeight((short)256);//行高
				HSSFCell cell = row.createCell((short)0);
				
				/*跨行合并单元格*/
				if(j==0){
					/*姓名*/
					sheet.addMergedRegion(new Region(sumRow,(short)0, sumRow+daylogList.size()-1,(short)0));
					
					/*日期*/
					sheet.addMergedRegion(new Region(sumRow,(short)1, sumRow+daylogList.size()-1,(short)1));
				}
				/*
				 wtjiao 20140715 POI对合并后边框操作存在缺陷，需要对参与合并的每个单元格进行边框设置，故下面这段代码不能放到if(j==0)判断中.
				                 POI中规定如果参与合并的每个单元格都有值，则以左上角的单元格值为准
				 */
				/*姓名 */
				cell = sheet.getRow(sumRow).createCell((short)0);
				defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getUserName());
				sheet.setColumnWidth((short)0, (short) (5*512));
				
				/*日期*/
				cell = sheet.getRow(sumRow).createCell((short)1);
				dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(dateStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getLogDate());
				sheet.setColumnWidth((short)1, (short) (6*512));
				
				/*类别*/
				cell = row.createCell((short)2);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getType());
				sheet.setColumnWidth((short)2, (short) (5*512));
				
				/*任务描述*/
				cell = row.createCell((short)3);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getDesc());
				sheet.setColumnWidth((short)3, (short)(20*512));
				
				/*补充说明*/
				cell = row.createCell((short)4);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getReason());
				sheet.setColumnWidth((short)4, (short) (25*512));
				
				
				/*任务工时*/
				cell = row.createCell((short)5);
				cell.setCellStyle(defaultCenterStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getSubTotal());
				sheet.setColumnWidth((short)5, (short) (10*512));
				
				/*跨行合并单元格*/
				if(j==0){
					/*日工时*/
					sheet.addMergedRegion(new Region(sumRow,(short)6,sumRow+daylogList.size()-1,(short)6));
				}
				/*日工时*/
				cell = sheet.getRow(sumRow).createCell((short)6);
				defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(sumSubTotal+"");
				sheet.setColumnWidth((short)6, (short) (9*512));
				
				/*完成百分比*/
				cell = row.createCell((short)7);
				cell.setCellStyle(defaultCenterStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getFinishRate());
				sheet.setColumnWidth((short)7, (short) (5*512));
				
				/*状态*/
				cell = row.createCell((short)8);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getStatu());
				sheet.setColumnWidth((short)8, (short) (5*512));
				
				/*计划/新增*/
				cell = row.createCell((short)9);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getPlanOrAdd());
				sheet.setColumnWidth((short)9, (short) (6*512));
				
				/*所在项目*/
				cell = row.createCell((short)10);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getGroupName());
				sheet.setColumnWidth((short)10, (short) (14*512));
				
				/*所属部门*/
				cell = row.createCell((short)11);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(daylog.getDetName().trim());
				sheet.setColumnWidth((short)11, (short) (10*512));
			}
		}
		
		wb.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
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
	
	/**
	 * 跳转到项目经理确认工时列表中
	 * @return
	 */
	public String projectSelect(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String role = request.getParameter("role");
		//上一个工作日 
		String previousWorkDate=DateUtil.getPreviousWorkDate();
		request.setAttribute("previousWorkDate", previousWorkDate);
		if("deptManager".equals(role)){//部门经理审批
			request.setAttribute("role", "deptManager");
			return "deptSelect";
		}else if("groupManager".equals(role)){//项目经理审批
			request.setAttribute("role", "groupManager");
			List<Project> projects = projectService.getMyDutyPrjname(userModel.getUserid());
			if(projects==null || projects.size()==0){
				request.setAttribute("message", "你没有负责的项目");
			}else{
				request.setAttribute("message", "");
			}
			//request.setAttribute("projects", projects);
			return "projectSelect";
		}
		return null;
	}
	
	/**
	 * 跳转到批量工时确认页面
	 * @return
	 */
	public String moreWorkEnterPage(){
		try{
			UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String role = request.getParameter("role");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String prjname = request.getParameter("prjname");
			prjname=prjname==null?"":prjname;
			if(prjname!=null && !"".equals(prjname)){
				prjname = URLDecoder.decode(prjname, "utf-8");
			}
			List list = null;
			List confirmList = null;
			
			if("deptManager".equals(role)){
				list = dayLogService.getMoreWorkEnterRecordForDept(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()), startDate, endDate);
				confirmList = dayLogService.getMoreWorkEnteredRecordForDept(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()), startDate, endDate);
			}else if("groupManager".equals(role)){
				list = dayLogService.getMoreWorkEnterRecord(userModel, startDate, endDate);
				confirmList = dayLogService.getMoreWorkEnteredRecord(userModel, startDate, endDate);
			}
			if((list==null || list.size()==0) && (confirmList==null || confirmList.size()==0)){
				MsgBox msgBox = new MsgBox(request, "暂时没有需要确认的记录");
				msgBox.setButtonType(MsgBox.BUTTON_OK);
				return "msgBox";
			}
			List returnList = bulidDayLog(list);
			List returnConfirmList = bulidDayLog(confirmList);
			
			//根据待确认日志列表，得到该列表中包含的所有项目，供用户选择，仅确认哪些项目
			List<EnterProject> enterProjectList= getEnterProjectFromLoglist(returnList);
			
			ActionContext.getContext().put("worklist", returnList);
			ActionContext.getContext().put("confirmworklist", returnConfirmList);
			request.setAttribute("role", role);
			request.setAttribute("startdate", startDate);
			request.setAttribute("prjname", prjname);
			request.setAttribute("enterProjectList", enterProjectList);
			return "workEnter";
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			
			msgBox.setButtonType(MsgBox.BUTTON_OK);
			return "msgBox";
		}
	}
	
	
	/**根据待确认日志列表，得到该列表中包含的所有项目，供用户选择，仅确认哪些项目
	 * @param toConfrimList 待确认的列表
	 * @return
	 */
	private List<EnterProject> getEnterProjectFromLoglist(List toConfrimList) {
		List<DayLog> confirmList=(List<DayLog>)toConfrimList;
		List<EnterProject> epList=new ArrayList<EnterProject>();
		if(confirmList!=null){
			for(DayLog dl:confirmList){
				String projectId=dl.getPrjName();
				String projectName=dl.getGroupName();
				boolean contains=contailsEnterProject(projectId,epList);
				if(!contains){
					EnterProject ep=new EnterProject(projectId, projectName);
					epList.add(ep);
				}
			}
		}
		return epList;
	}

	/**指定的列表中是否包含某个项目
	 * @param projectId
	 * @param epList
	 * @return
	 */
	private boolean contailsEnterProject(String projectId, List<EnterProject> epList) {
		for(int i=0;i<epList.size();i++){
			if(epList.get(i).getProjectId().equals(projectId)){
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public List<DayLog> bulidDayLog(List logList){
		List<DayLog> returnList = new ArrayList<DayLog>();
		for(int i=0;i<logList.size();i++){
			DayLog log = (DayLog)logList.get(i);
			Double projectHour=log.getSubTotal();
			Double projectDay=log.getProjectDay()==null?0.0:log.getProjectDay();
			Double enterHour=log.getConfirmHour()==null?0.0:log.getConfirmHour();
			Double enterDay=log.getEnterDay()==null?0.0:log.getEnterDay();
			for(int j=i+1;j<logList.size();j++){
				DayLog innerLog = (DayLog)logList.get(j);
				if(log.getUserId().equals(innerLog.getUserId()) && log.getLogDate().equals(innerLog.getLogDate()) && log.getGroupName().equals(innerLog.getGroupName())){
//					System.out.println(innerLog.getConfirmHour()==null?0.0:innerLog.getConfirmHour());
					projectHour = Arith.round(Arith.add(projectHour,innerLog.getSubTotal()),2);
					projectDay = Arith.round(Arith.add(projectDay,innerLog.getProjectDay()==null?0.0:innerLog.getProjectDay()),2);
					enterHour = Arith.round(Arith.add(enterHour,innerLog.getConfirmHour()==null?0.0:innerLog.getConfirmHour()),2);
					enterDay = Arith.round(Arith.add(enterDay,innerLog.getEnterDay()==null?0.0:innerLog.getEnterDay()),2);
					logList.remove(j);
					j--;
				}else{
					break;
				}
			}
			//当天日志总项目工时
			Double subtotal = dayLogService.getAllProjectHour(log.getUserId(), DateUtil.getDateString(log.getLogDate()));
			//当天日志所在项目总确认工时
			//Double prjConfirmHoutr = dayLogService.getConfirmHourByPrjname(log.getUserId(), DateUtil.getDateString(log.getLogDate()), log.getGroupName());
			//项目工时
			log.setProjectHour(projectHour);
			//当天日志所在项目项目人日=当天日志所在项目项目工时/当天日志总项目工时
			log.setProjectDay(Arith.round(Arith.div(projectHour,subtotal),2));
//			log.setProjectDay(Arith.round(Arith.div(subtotal,projectHour),2));
			//确认工时
			log.setConfirmHour(enterHour);
			//当天日期所在项目确认人日=当天日志所在项目项目人日*（当天日志所在项目总确认工时/当天日志所在项目项目工时）
			Double confirmDay = Arith.round((Arith.div(projectHour,subtotal))*(Arith.div(enterHour,projectHour)),2);
			log.setEnterDay(confirmDay);
			log.setAllConfirmHour(enterHour);//项目确认总确认工时
			//log.setAllConfirmHour(prjConfirmHoutr);//项目确认总确认工时
			log.setAllProjectHour(subtotal);
			returnList.add(log);
		}
		return returnList;
	}
	
	
	public String getConfirmInfoString(){
		try {
			String role = request.getParameter("role");
			String startdate = request.getParameter("startdate");
			HttpSession session = request.getSession();
			System.out.println(session.getId());
			UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String message = "";
			String month=Integer.parseInt(startdate.split("-")[1])+"";
			if("groupManager".equals(role)){//项目经理
				message+= "截至上一次统计，您负责的项目"+month+"月已确认消耗如下：\n";
				List<Project> projects = projectService.getMyDutyPrjname(userModel.getUserid());
				if(projects!=null){
					for(Project p:projects){
						Double allConfirmHour = dayLogService.getMonthAllConfirmHour(startdate,p.getId());
						message+="\n《"+p.getName()+"》已确认"+allConfirmHour+"人日\n";
					}
				}
				message+="\n您需要向客户完成以上人日投入确认，并在月末统一反馈回公司。\n";
				message+="\n您可以在\"项目管理→成本控制→项目人日消耗\"中查询项目人日消耗情况";
			}else if("deptManager".equals(role)){//部门经理
				String deptname = staffInfoService.getDeptNameValueByKey(userModel.getDeptName());
				Double allConfirmHour = dayLogService.getMonthAllConfirmHourByDept(startdate, deptname);
				message+= "截至上一次统计，《"+deptname+"》"+month+"月已确认"+allConfirmHour+"人日\n";
				message+= "\n您可以在\"项目管理→成本控制→部门投入统计\"中查询本部门月度人日消耗情况";
			}
			JSONObject json=new JSONObject();
			json.put("info", message);
			ajaxPrint(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public String saveMoreConfirmInfo(){
		try{
			String role = request.getParameter("role");
			String startdate = request.getParameter("startdate");
			UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			for(int i=0;i<enterlogList_id.size();i++){
				if("true".equals(isRowSelect.get(i))){//只对选择的行的数据进行处理
					String key = enterlogList_id.get(i);
					String userid = key.split(",")[0];
					String logdate = key.split(",")[1];
					String prjname = key.split(",")[2];
					String confrimStatus = key.split(",")[3];
					Double noConfirmHour = Double.parseDouble(enterlogList_confirmHour.get(i));//各项目未确认的工时
					String confirmDesc = enterlogList_confirmDesc.get(i);//各项目确认说明
					Double allProjectHour = Double.parseDouble(enterlogList_projectHour.get(i));//当天日志的总任务工时
					Double projectHourPrjname = dayLogService.getProjectHourByCondition(userid, logdate, prjname, confrimStatus);//各项目的总任务工时
					List list = dayLogService.getDaylogInfoByUseridAndLogdate(userid, logdate, userModel, prjname, confrimStatus);//各项目的任务记录数据
					for(int k=0;k<list.size();k++){
						DayLog log = (DayLog)list.get(k);
						log.setConfirmDesc(confirmDesc);
						log.setProjectDay(Arith.round(Arith.div(log.getSubTotal(),allProjectHour),2));
						log.setConfirmHour(Arith.round((Arith.div(log.getSubTotal(),projectHourPrjname))*noConfirmHour,2));
						log.setEnterDay((Arith.round(Arith.div(log.getSubTotal(),allProjectHour),2))*(Arith.round(Arith.div(noConfirmHour,projectHourPrjname),2)));
						log.setEnterRole(role);
						log.setEnterPeople(userModel.getUserid());
						log.setConfirmStatus("1");
						log.setUpdateTime(new Date());
						log.setUpdateman(userModel.getUsername());
						log.setConfirmMan(userModel.getUsername());//确认人
						log.setConfirmTime(new Date());//确认时间
						dayLogService.updateDayLog(log);
					}
				}
			}
			String message = "确认成功";
			MsgBox msgBox = new MsgBox(request, message);
			msgBox.setButtonType(MsgBox.BUTTON_OK);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, "确认失败");
			msgBox.setButtonType(MsgBox.BUTTON_OK);
			return "msgBox";
		}
	}
	
	/**
	 * 向项目经理发送邮件
	 */
	public void sendEmailForGroupmanage(){
		if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
			return;
		}
		String sendEmail = (String)BusnDataDir.getMapKeyValue("costControl.sendEmail").get("send");
		if("0".equals(sendEmail.trim())){
			return;
		}
		boolean isHoliday=holidayService.isHoliday(new Date());
		if(isHoliday){
			return;
		}
		try{
			String currentDay = DateUtil.getDateString(new Date());
			String yesterday = DateUtil.getPreviousWorkDate();
			String title = "项目经理日志确认提醒("+yesterday+")";
			List<Project> projectList = dayLogService.getNoConfirmPrjname(yesterday);
			//邮件发送用户list
			List<String> sendPersonList = new ArrayList<String>();
			//将未确认工时的用户添加到邮件发送用户list中
			List<String> noConfirmUseridList=userListToList(projectList);
			sendPersonList.addAll(noConfirmUseridList);
			//抄送用户
			//String copyUserEmail = Configure.getProperty("noconfirm_copyemail");
			String copyUserEmail = (String)BusnDataDir.getMapKeyValue("costControl.sendEmailUserId").get("projectmanageruser");
			if(!"".equals(copyUserEmail.trim())){
				List<String> copyUseridList=userStrToList(copyUserEmail);
				sendPersonList.addAll(copyUseridList);
			}
			//去除发送人员中的重复数据
			sendPersonList=removeSameUser(sendPersonList);
			String content="";
			if(projectList!=null && projectList.size()>0){ 
				for(int i = 0;i<projectList.size();i++){
					if(!staffInfoService.flagUserisLeave(projectList.get(i).getProManagerId())){
						continue;
					}
					Double allConfirmHour = dayLogService.getMonthAllConfirmHour(yesterday, projectList.get(i).getId());
					String noConfirmLogDate = dayLogService.getNoConfirmDayLogDate(yesterday, projectList.get(i).getName());
					content+="<br/>项目经理 "+projectList.get(i).getProManager()+"，截至"+currentDay+" 12 :00，您尚未对《"+projectList.get(i).getName()+"》"+noConfirmLogDate+"日的日志进行确认，本项目本月已累计确认"+allConfirmHour+"人日，您需向客户完成"+allConfirmHour+"人日投入确认，并统一与月末反馈回公司。<br/>";
				}
				//发送并保存发送结果
				if(!"".equals(content)){
					sendNoConfirmInfoToUser(sendPersonList,title,content);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 向部门经理发送邮件
	 */
	public void sendEmailForDeptmanage(){
		if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
			return;
		}
		String sendEmail = (String)BusnDataDir.getMapKeyValue("costControl.sendEmail").get("send");
		if("0".equals(sendEmail.trim())){
			return;
		}
		boolean isHoliday=holidayService.isHoliday(new Date());
		if(isHoliday){
			return;
		}
		try{
			String currentDay = DateUtil.getDateString(new Date());
			String yesterday = DateUtil.getPreviousWorkDate();
			String title =  "部门经理日志确认提醒("+yesterday+")";
			List<SysUser> userList = dayLogService.getNoConfirmDept(yesterday);
			//邮件发送用户list
			List<String> sendPersonList = new ArrayList<String>();
			//将未确认工时的用户添加到邮件发送用户list中
			List<String> noConfirmUseridList=userListToStringList(userList);
			sendPersonList.addAll(noConfirmUseridList);
			//抄送用户
			//String copyUserEmail = Configure.getProperty("noconfirm_copyemail");
			String copyUserEmail = (String)BusnDataDir.getMapKeyValue("costControl.sendEmailUserId").get("deptmanageruser");
			if(!"".equals(copyUserEmail.trim())){
				List<String> copyUseridList=userStrToList(copyUserEmail);
				sendPersonList.addAll(copyUseridList);
			}
			//去除发送人员中的重复数据
			sendPersonList=removeSameUser(sendPersonList);
			String content="";
			if(userList!=null && userList.size()>0){ 
				for(int i = 0;i<userList.size();i++){
					if(!staffInfoService.flagUserisLeave(userList.get(i).getUserid())){
						continue;
					}
					String noConfirmDate = dayLogService.getNoConfirmDayLogDateForDept(yesterday, staffInfoService.getDeptNameValueByKey(userList.get(i).getDeptName()));
					content+="<br/>部门经理 "+userList.get(i).getUsername()+"，截至"+currentDay+" 16 :00，您尚未对《"+staffInfoService.getDeptNameValueByKey(userList.get(i).getDeptName())+"》部门内部安排的非项目工作"+noConfirmDate+"日的日志进行确认，请尽快确认。<br/>";
				}
				//发送并保存发送结果
				if(!"".equals(content)){
					sendNoConfirmInfoToUser(sendPersonList,title,content);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 项各项目经理发送未确认的日志信息
	 * @param sendPersonList
	 * @param title
	 * @param content
	 */
	private void sendNoConfirmInfoToUser(List<String> sendPersonList,String title, String content) {
		TemplateEmailService emailService=new TemplateEmailService();
		//发送邮件
		for(int i=0;i<sendPersonList.size();i++){
			String userid = sendPersonList.get(i);
			//先判断用户是否存在，如果存在则判断用户是否离职；否则不做判断。例如：pm@grgbanking.com不做处理
			if(staffInfoService.isExitStaffByUserid(userid.split("@")[0])){
				if(!staffInfoService.flagUserisLeave(userid.split("@")[0])){
					continue;
				}
			}
			//获取用户email
			String email="";
			if(userid.indexOf("@")!=-1){
				email=userid;
			}else{
				email=staffInfoService.getEmailForUserid(userid);
			}
			//填充模版中的指定字段
			Map<String,String> templateTagMap=new HashMap<String,String>();
			String currentDay = DateUtil.getDateString(new Date());
			templateTagMap.put("logdate",currentDay);
			templateTagMap.put("warnInfo",content);
			String sendContent =emailService.sendTemplateMail(email, title, "daylogNoConfirmDayToUser.ftl", templateTagMap);
			//保存发送记录
			if(!"0".equals(sendContent)){
				WarnInfo warn=new WarnInfo();
				warn.setToUserId(userid);
				warn.setToUserName(email);
				warn.setWarnWay("email");
				warn.setWarnType("remind");
				warn.setWarnTime(Calendar.getInstance().getTime());
				warn.setWarnContent(sendContent);
				warnInfoService.save(warn);
			}
		}
	}
	
	/**
	 * 获取要接受邮件的用户
	 * @param projectList
	 * @return
	 */
	public List<String> userListToList(List<Project> projectList) {
		List<String> list=new ArrayList();
		for(int i=0;i<projectList.size();i++){
			String userid = projectList.get(i).getProManagerId();
			list.add(userid);
		}
		return list;
	}
	
	/**
	 * 字符串转换成list
	 * @param str
	 * @return
	 */
	public List<String> userStrToList(String str) {
		List<String> list=new ArrayList();
		String[] strs = str.split(",");
		for(int i=0;i<strs.length;i++){
			if(!"".equals(strs[i])){
				list.add(strs[i].trim());
			}
		}
		return list;
	}
	
	/**
	 * 获取部门经理信息
	 * @param projectList
	 * @return
	 */
	public List<String> userListToStringList(List<SysUser> userList) {
		List<String> list=new ArrayList();
		for(int i=0;i<userList.size();i++){
			String userid = userList.get(i).getUserid();
			list.add(userid);
		}
		return list;
	}
	
	/**
	 * 去掉重复的用户信息
	 * @param sysuser
	 * @return
	 */
	public List<String> removeSameUser(List<String> sysuser){
		for (int i = 0; i < sysuser.size(); i++){
            for (int j = sysuser.size() - 1 ; j > i; j--){
                if (sysuser.get(i).equals(sysuser.get(j))){
                	sysuser.remove(j);
                }
            }
        }
		return sysuser;
	}
	
	public List<DateDayLog> getDateDayLogList() {
		return dateDayLogList;
	}

	public void setDateDayLogList(List<DateDayLog> dateDayLogList) {
		this.dateDayLogList = dateDayLogList;
	}

	public List<DayLog> getDaylogList() {
		return daylogList;
	}
	public void setDaylogList(List<DayLog> daylogList) {
		this.daylogList = daylogList;
	}

	public DateDayLog getDateDayLog() {
		return dateDayLog;
	}

	public void setDateDayLog(DateDayLog dateDayLog) {
		this.dateDayLog = dateDayLog;
	}

	public DayLogVote getVote() {
		return vote;
	}

	public void setVote(DayLogVote vote) {
		this.vote = vote;
	}

	public List<String> getDaylogList_prjName() {
		return daylogList_prjName;
	}

	public void setDaylogList_prjName(List<String> daylogList_prjName) {
		this.daylogList_prjName = daylogList_prjName;
	}

	public List<String> getDaylogList_type() {
		return daylogList_type;
	}

	public void setDaylogList_type(List<String> daylogList_type) {
		this.daylogList_type = daylogList_type;
	}

	public List<String> getDaylogList_statu() {
		return daylogList_statu;
	}

	public void setDaylogList_statu(List<String> daylogList_statu) {
		this.daylogList_statu = daylogList_statu;
	}

	public List<String> getDaylogList_finishRate() {
		return daylogList_finishRate;
	}

	public void setDaylogList_finishRate(List<String> daylogList_finishRate) {
		this.daylogList_finishRate = daylogList_finishRate;
	}

	public List<String> getDaylogList_planOrAdd() {
		return daylogList_planOrAdd;
	}

	public void setDaylogList_planOrAdd(List<String> daylogList_planOrAdd) {
		this.daylogList_planOrAdd = daylogList_planOrAdd;
	}

	public List<String> getDaylogList_subTotal() {
		return daylogList_subTotal;
	}

	public void setDaylogList_subTotal(List<String> daylogList_subTotal) {
		this.daylogList_subTotal = daylogList_subTotal;
	}

	public List<String> getDaylogList_desc() {
		return daylogList_desc;
	}

	public void setDaylogList_desc(List<String> daylogList_desc) {
		this.daylogList_desc = daylogList_desc;
	}

	public List<String> getDaylogList_reason() {
		return daylogList_reason;
	}

	public void setDaylogList_reason(List<String> daylogList_reason) {
		this.daylogList_reason = daylogList_reason;
	}

	public List<String> getDaylogList_confirmHour() {
		return daylogList_confirmHour;
	}

	public void setDaylogList_confirmHour(List<String> daylogList_confirmHour) {
		this.daylogList_confirmHour = daylogList_confirmHour;
	}

	public List<String> getDaylogList_confirmReason() {
		return daylogList_confirmReason;
	}

	public void setDaylogList_confirmReason(List<String> daylogList_confirmReason) {
		this.daylogList_confirmReason = daylogList_confirmReason;
	}

	public List<String> getDaylogList_tasktype() {
		return daylogList_tasktype;
	}

	public void setDaylogList_tasktype(List<String> daylogList_tasktype) {
		this.daylogList_tasktype = daylogList_tasktype;
	}

	public List<String> getDaylogList_tasksdate() {
		return daylogList_tasksdate;
	}

	public void setDaylogList_tasksdate(List<String> daylogList_tasksdate) {
		this.daylogList_tasksdate = daylogList_tasksdate;
	}

	public List<String> getDaylogList_taskedate() {
		return daylogList_taskedate;
	}

	public void setDaylogList_taskedate(List<String> daylogList_taskedate) {
		this.daylogList_taskedate = daylogList_taskedate;
	}

	public List<String> getDaylogList_delayReason() {
		return daylogList_delayReason;
	}

	public void setDaylogList_delayReason(List<String> daylogList_delayReason) {
		this.daylogList_delayReason = daylogList_delayReason;
	}

	public List<String> getEnterlogList_confirmHour() {
		return enterlogList_confirmHour;
	}

	public void setEnterlogList_confirmHour(List<String> enterlogList_confirmHour) {
		this.enterlogList_confirmHour = enterlogList_confirmHour;
	}

	public List<String> getEnterlogList_confirmDesc() {
		return enterlogList_confirmDesc;
	}

	public void setEnterlogList_confirmDesc(List<String> enterlogList_confirmDesc) {
		this.enterlogList_confirmDesc = enterlogList_confirmDesc;
	}

	public List<String> getEnterlogList_id() {
		return enterlogList_id;
	}

	public void setEnterlogList_id(List<String> enterlogList_id) {
		this.enterlogList_id = enterlogList_id;
	}

	public List<String> getEnterlogList_projectHour() {
		return enterlogList_projectHour;
	}

	public void setEnterlogList_projectHour(List<String> enterlogList_projectHour) {
		this.enterlogList_projectHour = enterlogList_projectHour;
	}

	public List<String> getIsRowSelect() {
		return isRowSelect;
	}

	public void setIsRowSelect(List<String> isRowSelect) {
		this.isRowSelect = isRowSelect;
	}
	
	 
	
	

	   /****************************导出excel******************************/
	/**
	 * 导出项目成本的excel表
	 * @return
	 * @throws Exception
	 */
	    @SuppressWarnings("rawtypes")
        public String exportList() {
	    //	System.out.println("===================");
	    	try{
	    	String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
	    	
	    
            List<Map> list = dayLogService.getPrjCensusInfos(startDate, endDate, dateDayLog==null?null:dateDayLog);
			
			Date date = new Date();
			String filename = getWorkSheetName(dateDayLog, startDate, endDate);//设置文件名
			OutputStream os = response.getOutputStream();// 取得输出流 
			response.reset();// 清空输出流  
	        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
	        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
	        //第一步，创建一个webbook，对应一个Excel文件
	     	HSSFWorkbook wb = new HSSFWorkbook();
	     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	     	HSSFSheet sheet = wb.createSheet();
            //wb.setSheetName(0,filename,HSSFWorkbook.ENCODING_UTF_16);
	     	wb.setSheetName(0,filename);//poi3.7版本中只有两个参数 
	     	
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
			// 右中
			HSSFCellStyle defaultRightStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_RIGHT);
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
	     	
			HSSFRow cellNameRow = sheet.createRow((int) 0);
			cellNameRow.setHeight((short)350);
			wb = this.setExcelValue(wb, 0, "编号", 0, (short)0, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "姓名", 0, (short)1, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "开发二部", 0,(short) 2, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)3, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "项目人日（天）",0, (short)4, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "投入人日（天）", 0, (short)5, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "请假人日", 0, (short)6, cellNameStyle);
			wb = this.setExcelValue(wb, 0, "实习生标识", 0, (short)7, cellNameStyle);
			
			int sumRow=0;
			for(int i=0;i<list.size();i++){
				Map infoList=list.get(i);
				
		//		System.out.println("========="+infoList.get("cuserid"));
				
				 
					++sumRow;
					 
					HSSFRow row = sheet.createRow(sumRow);
					row.setHeight((short)256);//行高
					HSSFCell cell = row.createCell((short)0);
								
				/*编号 */
				cell = sheet.getRow(sumRow).createCell((short)0);
				defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(infoList.get("cuserid").toString());
				sheet.setColumnWidth((short)0, (short) (5*512));
				
				/*姓名*/
				cell = sheet.getRow(sumRow).createCell((short)1);
				dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(dateStyle);
				cell.setCellValue(infoList.get("cusername").toString());
				sheet.setColumnWidth((short)1, (short) (6*512));
				
				/*开发二部*/
				cell = row.createCell((short)2);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
				cell.setCellValue(infoList.get("cdetname")==null?"":infoList.get("cdetname").toString());
				sheet.setColumnWidth((short)2, (short) (8*512));
				
				/*项目名称*/
				cell = row.createCell((short)3);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultStyle);
				cell.setCellValue(infoList.get("cgroupname").toString());
				sheet.setColumnWidth((short)3, (short)(20*512));
				
				/*项目人日*/
				cell = row.createCell((short)4);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
				cell.setCellStyle(defaultRightStyle);
				cell.setCellValue(infoList.get("cprjdays").toString());
				sheet.setColumnWidth((short)4, (short) (8*512));
				
				
				/*投入人日*/
				cell = row.createCell((short)5);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
				cell.setCellStyle(defaultRightStyle);
				cell.setCellValue(infoList.get("totaldays").toString());
				sheet.setColumnWidth((short)5, (short) (8*512));
				
				
				
				/*请假人日*/
				cell = sheet.getRow(sumRow).createCell((short)6);
				defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultRightStyle);
				cell.setCellValue(infoList.get("leavedays").toString());
				sheet.setColumnWidth((short)6, (short) (6*512));
				
				/*实习生标识*/
				cell = row.createCell((short)7);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(infoList.get("cstatus").toString().equals("study")?"是":"");
				sheet.setColumnWidth((short)7, (short) (6*512));
				
				 
			}
			//wait(5000);
			wb.write(os);
			//os.close();
			
			//清理刷新缓冲区，将缓存中的数据将数据导出excel
			os.flush();
			//关闭os
			if(os!=null){
				os.close();
			}
			MsgBox msgBox = new MsgBox(request, getText("add.ok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.ok"));
			return "msgBox";
	    	}catch(Exception e){
	    	    e.printStackTrace();
	    	}
	    	return null;
	    }
		//**********************************************************************


    /**
     * getWorkSheetName:描述 <br/>
     * 2016年4月13日 下午2:37:42
     * @param dateDayLog2
     * @param startDate
     * @param endDate
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    private String getWorkSheetName(DateDayLog dateDayLog, String startDate, String endDate) {
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String name="";
            if(StringUtils.isNotBlank(dateDayLog.getUserDept())){
                if(dateDayLog.getUserDept().length()>10){
                    name+=dateDayLog.getUserDept().substring(0,10)+" ";
                }else{
                    name+=dateDayLog.getUserDept()+" ";
                }
            }
            if(StringUtils.isNotBlank(dateDayLog.getUserGroup())){
                if(dateDayLog.getUserGroup().length()>10){
                    name+=dateDayLog.getUserGroup().substring(0,10)+" ";
                }else{
                    name+=dateDayLog.getUserGroup()+" ";
                }
            }
            if(StringUtils.isNotBlank(dateDayLog.getUserName())){
                name+=dateDayLog.getUserName()+" ";
            }
            name+="项目统计信息";
            
            Date st = null;
            Date end = null;
            
            if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)){
                st = sdf.parse(startDate);
                end = sdf.parse(endDate);
                //name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
            } else if(StringUtils.isNotBlank(startDate)){
                st = sdf.parse(startDate);
                end = new Date();
                //name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
            }else if(StringUtils.isNotBlank(endDate)){
                st = sdf.parse("2016-1-1");
                end = sdf.parse(endDate);
            }else {
                st = sdf.parse("2016-1-1");
                end = new Date();
            }
            
            name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
            
            
            
            return name;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "项目统计信息";
    }
}
