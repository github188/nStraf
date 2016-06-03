package cn.grgbanking.feeltm.common4Wechat.webapp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common4Wechat.service.Common4WechatService;
import cn.grgbanking.feeltm.common4Wechat.util.Common4Wechat;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.feeltm.projectweekplan.service.ProjectWeekPlanService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 提供微信应用的接口
 * @author zzhui1
 * 
 */
@SuppressWarnings({ "serial", "unchecked" })
public class Common4WechatAction extends BaseAction {
	/** 微信用户绑定校验Service */
	@Autowired
	private Common4WechatService accountBindService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectWeekPlanService projectWeekPlanService;
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	private StaffInfoService staffInfoService;
	
	/**
	 * 微信用户绑定校验<br>
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	public void checkAccountBind() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try{
			String userId=request.getParameter("accountno");
			String userPwd=request.getParameter("password");
			// 返回值枚举*/
			Common4Wechat.CHECK_RESULT enumResult;			
			// 页面传过来的json对象
			JSONObject requestJson = new JSONObject("{accountno:'"+userId+"',password:'"+userPwd+"'}");
			SysUser loginUser = null;
			if (requestJson.get(Common4Wechat.NO_KEY) == null || requestJson.get(Common4Wechat.PWD_KEY) == null) {
				// 请求参数存在空值
				enumResult = Common4Wechat.CHECK_RESULT.BLANK_PARAM;
			} else {
				String accountno = requestJson.get(Common4Wechat.NO_KEY).toString();
				String password = requestJson.get(Common4Wechat.PWD_KEY).toString();
				enumResult = Common4Wechat.CHECK_RESULT.DEFAULT;
				if (StringUtils.isBlank(accountno) || StringUtils.isBlank(password)) {
					// 请求参数存在空值
					enumResult = Common4Wechat.CHECK_RESULT.BLANK_PARAM;
				} else {
					// 根据用户ID查询
					loginUser = accountBindService.findSysUserByUserId(accountno);
					if (loginUser == null || loginUser.getUserid() == null) {
						// 用户不存在
						enumResult = Common4Wechat.CHECK_RESULT.NO_EXIST;
					} else if ("N".equals(loginUser.getIsvalid())) {
						// 用户无效
						enumResult = Common4Wechat.CHECK_RESULT.USER_INVALID;
					} else if (!doCheckPwd(password, loginUser.getUserpwd())) {
						// 密码不匹配
						enumResult = Common4Wechat.CHECK_RESULT.PWD_INVALID;
					}
				}
			}
			boolean getSuccussed = false;
			switch (enumResult) {
			case DEFAULT:
				// 校验绑定成功
				getSuccussed = true;
				responseObj.put(Common4Wechat.RETCODE_KEY, getRetCodeByEnum(enumResult.ordinal()));
				responseObj.put(Common4Wechat.RETMSG_KEY, "成功");
				break;
			case BLANK_PARAM:
				responseObj.put(Common4Wechat.RETCODE_KEY, getRetCodeByEnum(enumResult.ordinal()));
				responseObj.put(Common4Wechat.RETMSG_KEY, "请求参数存在空值");
				break;
			case NO_EXIST:
				responseObj.put(Common4Wechat.RETCODE_KEY, getRetCodeByEnum(enumResult.ordinal()));
				responseObj.put(Common4Wechat.RETMSG_KEY, "查询不到该员工信息");
				break;
			case USER_INVALID:
				responseObj.put(Common4Wechat.RETCODE_KEY, getRetCodeByEnum(enumResult.ordinal()));
				responseObj.put(Common4Wechat.RETMSG_KEY, "用户被禁用了");
				break;
			case PWD_INVALID:
				responseObj.put(Common4Wechat.RETCODE_KEY, getRetCodeByEnum(enumResult.ordinal()));
				responseObj.put(Common4Wechat.RETMSG_KEY, "密码不匹配 ");
				break;
			default:
				responseObj.put(Common4Wechat.RETCODE_KEY, "9999");
				responseObj.put(Common4Wechat.RETMSG_KEY, "数据处理失败 ");
				break;
			}
			
			// 设定返回值
			if (getSuccussed) {
				responseObj.put(Common4Wechat.NAME_KEY, loginUser.getUsername());
				responseObj.put(Common4Wechat.NO_KEY, loginUser.getUserid());
				responseObj.put(Common4Wechat.JOBNO_KEY, loginUser.getJobNumber());
				responseObj.put(Common4Wechat.EMAIL, loginUser.getEmail());
			} else {
				responseObj.put(Common4Wechat.NAME_KEY, retBlank);
				responseObj.put(Common4Wechat.NO_KEY, retBlank);
				responseObj.put(Common4Wechat.JOBNO_KEY, retBlank);
				responseObj.put(Common4Wechat.EMAIL, retBlank);
			}
		}catch(Exception e){
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, "9999");
				responseObj.put(Common4Wechat.RETMSG_KEY, "数据处理失败 ");
				responseObj.put(Common4Wechat.NAME_KEY, retBlank);
				responseObj.put(Common4Wechat.NO_KEY, retBlank);
				responseObj.put(Common4Wechat.JOBNO_KEY, retBlank);
				responseObj.put(Common4Wechat.EMAIL, retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}finally{
			String jsonStr = responseObj.toString();
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 获取个人日志任务(已废止，该方法已经移动到nStrafMobile下)
	 */
	public void getProjectAndTaskInfo() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		List<Object> projectListJson = new ArrayList<Object>();
		JSONObject projectItemJson = new JSONObject();
		List<Object> taskListJson = new ArrayList<Object>();
		JSONObject taskItemJson = new JSONObject();
		String failsCode = Common4Wechat.DAYLOG_RETCODE_NG;
		String failsMsg = Common4Wechat.DAYLOG_RETMSG_NG;
		try {
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			String taskDate = request.getParameter(Common4Wechat.DAYLOG_TASK_DATE);
			//获取当前用户参与的项目
			List<Project> projects=projectService.getProjectByUserId(userId);
			//获取名称为"其他项目"的项目
			Project otherProject=projectService.getOtherProject();
			if (otherProject != null) {
				projects.add(otherProject);
			}
			// 取得当前日期的用户所有的任务
			Date queryDate = null;
			if (StringUtils.isNotBlank(taskDate)) {
				queryDate = new SimpleDateFormat("yyyy-MM-dd").parse(taskDate);
			} else {
				// 查询当日
				queryDate = Calendar.getInstance().getTime();
			}
			Date startDate = queryDate;
			Date endDate = queryDate;
			List<ProjectWeekPlanTask> taskList = new ArrayList<ProjectWeekPlanTask>();
			if (projects != null) {
				for (Project project : projects) {
					taskList = projectWeekPlanService.getTaskInProject(project.getId(), userId, startDate, endDate);
					for (ProjectWeekPlanTask projectWeekPlanTask : taskList) {
						taskItemJson.put(Common4Wechat.DAYLOG_PROJECT_TASK_ID, projectWeekPlanTask.getId());
						taskItemJson.put(Common4Wechat.DAYLOG_PROJECT_TASK_NAME, projectWeekPlanTask.getTaskContent());
						taskListJson.add(taskItemJson);
					}
					projectItemJson.put(Common4Wechat.DAYLOG_PROJECT_ID, project.getId());
					projectItemJson.put(Common4Wechat.DAYLOG_PROJECT_NAME, project.getName());
					projectItemJson.put(Common4Wechat.DAYLOG_PROJECT_TASKS, taskListJson.toArray());
					projectListJson.add(projectItemJson);
				}
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.DAYLOG_RETMSG_OK);
				responseObj.put(Common4Wechat.DAYLOG_PROJECT_LIST, projectListJson.toArray());
			}else{
				responseObj.put(Common4Wechat.RETCODE_KEY, failsCode);
				responseObj.put(Common4Wechat.RETMSG_KEY, failsMsg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, failsCode);
				responseObj.put(Common4Wechat.RETMSG_KEY, failsMsg);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, failsCode);
				responseObj.put(Common4Wechat.RETMSG_KEY, failsMsg);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}finally{
			String jsonStr = responseObj.toString();
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 填写日志接口(已废止，该方法已经移动到nStrafMobile下)
	 */
	public void fillDayLog() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		try {
			// OA账号
			String accountno = request.getParameter(Common4Wechat.NO_KEY);
			// 项目ID
			String projectid = request.getParameter(Common4Wechat.DAYLOG_PROJECT_ID);
			// 项目名 
			String projectname = request.getParameter(Common4Wechat.DAYLOG_PROJECT_NAME);
			// 计划任务
			String taskname = request.getParameter(Common4Wechat.DAYLOG_PROJECT_TASK_NAME);
			// 任务描述
			String taskdesc = request.getParameter(Common4Wechat.DAYLOG_PROJECT_TASK_DESCR);
			// 日期
			String filldate = request.getParameter(Common4Wechat.DAYLOG_FILL_DATE);
			// 工时
			String worktime = request.getParameter(Common4Wechat.DAYLOG_WORK_TIME);
			// 工作类别
			String worktype = request.getParameter(Common4Wechat.DAYLOG_WORK_TYPE);
			// 状态
			String status = request.getParameter(Common4Wechat.DAYLOG_STATUS);
			// 完成进度
			String process = request.getParameter(Common4Wechat.DAYLOG_PROCESS);
			// 日志类型
			String logtype = request.getParameter(Common4Wechat.DAYLOG_LOG_TYPE);
			DayLog dayLog = new DayLog();
			SysUser sysUser = staffInfoService.findUserByUserid(accountno);
			dayLog.setDesc(taskname);
			dayLog.setDetName(staffInfoService.getDeptNameValueByKey(sysUser.getDeptName()));
			dayLog.setFinishRate(process);
			dayLog.setGroupName(projectname);
			Date logDate = DateUtil.stringToDate(filldate, "yyyy-MM-dd");
			dayLog.setLogDate(logDate);
			dayLog.setPlanOrAdd(Common4Wechat.LOG_TYPE[Integer.parseInt(logtype) - 1]);
			// 项目ID
			dayLog.setPrjName(projectid);
			// 补充说明 
			dayLog.setReason(taskdesc);
			dayLog.setStatu(Common4Wechat.WORK_STATUS[Integer.parseInt(status) - 1]);
			dayLog.setSubTotal(Double.parseDouble(worktime));
			dayLog.setType(Common4Wechat.WORK_TYPES[Integer.parseInt(worktype) - 1]);
			dayLog.setUpdateman(sysUser.getUsername());
			dayLog.setUpdateTime(new Date());
			dayLog.setUserId(accountno);
			dayLog.setUserLevel(staffInfoService.getLevelName(sysUser.getUserid()));
			dayLog.setUserName(sysUser.getUsername());
			dayLog.setAuditStatus("新增");
			boolean hasDayLog = dayLogService.hasDayLogList(accountno, logDate);
			if (hasDayLog) {
				dayLogService.remove(accountno, logDate);
			}
			dayLogService.save(dayLog);
			responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_OK);
			responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.DAYLOG_RETMSG_OK);
		} catch (Exception e) {
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.DAYLOG_RETMSG_NG);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}finally{
			String jsonStr = responseObj.toString();
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}

	/**
	 * 将1转成0001
	 * 
	 * @param retcode
	 *            返回值
	 * @return
	 */
	private String getRetCodeByEnum(int retcode) {
		return String.valueOf(10000 + retcode).substring(1);
	}

	/**
	 * 密码不匹配的校验
	 * 
	 * @param requestPwd
	 *            请求的密码
	 * @param dbPwd
	 *            DB的密码
	 * @return
	 */
	private boolean doCheckPwd(String requestPwd, String dbPwd) {
		String maintainPwd = Configure.getProperty("maintainPwd");
		if ((!maintainPwd.equals(requestPwd)) && (!requestPwd.equals(dbPwd))) {
			return false;
		} else {
			return true;
		}
	}
}
