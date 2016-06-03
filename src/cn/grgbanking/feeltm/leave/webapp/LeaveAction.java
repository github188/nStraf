package cn.grgbanking.feeltm.leave.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.TimeConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.hols.service.UserHolsService;
import cn.grgbanking.feeltm.leave.domain.Leave;
import cn.grgbanking.feeltm.leave.service.LeaveService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class LeaveAction extends BaseAction {

	@Autowired
	private LeaveService service;

	@Autowired
	private StaffInfoService staffInfoService;

	@Autowired
	private UserHolsService userHolsService;

	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private ProjectService projectService;

	private Leave leave;
	private String form;
	private String startTime;
	private String endTime;
	private String type;
	private String deptcode;
	private String grpcode;
	private String userid;
	private String id;
	private String result;
	private String option;
	
	public String auditPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				leave = service.findById(id);
				// 获取审批记录
				String record = "";
				List<ApprovalRecord> records = approvalService
						.getRecordByName(leave.getId());
				for (int i =0;i<records.size();i++) {
					record += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+"\t"
							+records.get(i).getApprovalUser()+"\t审批"+records.get(i).getResult()+"\t审批意见："+records.get(i).getOpinion()
							+"\n";
				}
				request.setAttribute("record", record);
				request.setAttribute("leave", leave);
				Map<String, String> typeMap = BusnDataDir
						.getMapKeyValue("hrManager.leaveType");
				request.setAttribute("typeMap", typeMap);
				List<SysUser> approverlist = staffInfoService
						.getgroupManagerUp();
				request.setAttribute("approverlist", approverlist);
				if (leave.getStatus().equals("待审批")) {
					// 待审批
					if (leave.getApprover().equals(userModel.getUserid())) {
						return "audit";
					} else {
						msgBox = new MsgBox(request,
								getText("user.leave.approverself"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				} else {
					// 修改
					if (leave.getStatus().equals("新增")
							|| leave.getStatus().equals("不通过")) {
						// 新建和返回修改不能审批
						msgBox = new MsgBox(request,
								getText("user.leave.cantnew"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
					else if(leave.getStatus().equals("审批通过")) {
						msgBox = new MsgBox(request,
								getText("user.leave.approvered"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}else{
						msgBox = new MsgBox(request, getText("audit.ActionFailed"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				}
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-auditPage())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	public String view(){
		try {
			Leave leave = service.findById(id);
			// 获取审批记录
			String record = "";
			List<ApprovalRecord> records = approvalService
					.getRecordByName(leave.getId());
			for (int i =0;i<records.size();i++) {
				record += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+"\t"
						+records.get(i).getApprovalUser()+"\t审批"+records.get(i).getResult()+"\t审批意见："+records.get(i).getOpinion()
						+"\n";
			}
			request.setAttribute("record", record.trim());
			ActionContext.getContext().put("leave", leave);
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-view())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "view";
	}

	/**
	 * 提交审批
	 * 
	 * @return
	 */
	public String auditing() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				StringBuffer buffer = new StringBuffer();
				String ids[] = id.split(",");
				for (String s : ids) {
					leave = service.findById(s);
					if (!leave.getStatus().equals("新增")
							&& !leave.getStatus().equals("不通过")) {
						msgBox = new MsgBox(request,
								getText("user.leave.auditnew"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					} else {
						if (!leave.getUserid().equals(userModel.getUserid())) {
							msgBox = new MsgBox(request,
									getText("user.leave.selfaudit"));
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						} else {
							buffer.append("," + s);
						}
					}
				}
				if (!buffer.toString().equals("")) {
					service.updateIds(buffer.toString().substring(1));
				}
				msgBox = new MsgBox(request, getText("user.leave.auditings"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-auditing())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public String delete() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				StringBuffer buffer = new StringBuffer();
				String ids[] = id.split(",");
				for (String s : ids) {
					leave = service.findById(s);
					if (!leave.getStatus().equals("新增")) {
						// 不能删除非新增的数据
						msgBox = new MsgBox(request,
								getText("user.leave.deletenew"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					} else {
						if (!leave.getUserid().equals(userModel.getUserid())) {
							msgBox = new MsgBox(request,
									getText("user.leave.notdelete"));
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						} else {
							buffer.append("," + s);
						}
					}
				}
				if (!buffer.toString().equals("")) {
					service.removeIds(buffer.toString().substring(1));
				}
				msgBox = new MsgBox(request, getText("user.leave.deletesucc"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-delete())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";

	}

	/**
	 * 审批
	 * 
	 * @return
	 */
	public String audit() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				leave = service.findById(id);
				if ("0".equals(result)) {
					// 通过
					leave.setStatus("审批通过");
					if(option==null||(option!=null&&option.equals(""))){
						option = "同意";
					}
					approvalService.makeRecored(leave.getId(), "请假申请通过",
							leave.getApproverName(),option ,"通过");
					
					double resttime = 0;
					//如果是年假
					if("年假".equals(leave.getType())){
						//扣减年假时间
						resttime = userHolsService.getRestTime(leave.getUserid(), 1);
						double time = resttime - leave.getSumtime();
						if(time>=0){
							userHolsService.updateHolsTime(leave.getUserid(), time, 1);
						}else{
							msgBox = new MsgBox(request, getText("user.leave.yhols"));
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						}
					}
					if("调休".equals(leave.getType())){
						//扣减事假时间
						resttime = userHolsService.getRestTime(leave.getUserid(), 2);
						double time = resttime - leave.getSumtime();
						if(time>=0){
							userHolsService.updateHolsTime(leave.getUserid(), time, 2);
						}else{
							msgBox = new MsgBox(request, getText("user.leavce.dhols"));
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						}
					}
				} else {
					// 不通过
					leave.setStatus("不通过");
					approvalService.makeRecored(leave.getId(), "请假申请不通过",
							leave.getApproverName(), option,"不通过");
				}
				leave.setUpdateTime(new Date());
				leave.setUpdateUser(userModel.getUsername());
				service.update(leave);
				msgBox = new MsgBox(request, getText("user.leave.appsuccess"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-audit())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	public void refresh() {
		MsgBox msgBox;
		try {
			list();
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-refresh())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
	}

	@SuppressWarnings("unchecked")
	public String list() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			Leave leave = new Leave();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (deptcode != null && !"".equals(deptcode)) {
				leave.setDeptName(deptcode.trim());
			}
			if (grpcode != null && !"".equals(grpcode)) {
				leave.setGrpName(grpcode.trim());
			}
			if (userid != null && !"".equals(userid)) {
				leave.setUserid(userid.trim());
			}
			if (type != null && !"".equals(type)) {
				leave.setType(type.trim());
			}
			Page page = service.findAll(leave, userModel, startTime, endTime,
					pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = page.getQueryResult();
			Map<String, String> typeMap = BusnDataDir
					.getMapKeyValue("hrManager.leaveType");
			request.setAttribute("typeMap", typeMap);
			if (form != null && form.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil util = new JSONUtil(
						"cn.grgbanking.feeltm.leave.domain.Leave");
				JSONArray array = util.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", array);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}
			request.setAttribute("leaves", list);
			return "list";
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-list())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String addPage() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		request.setAttribute("username", userModel.getUsername());
		MsgBox msgBox;
		try {
			String deptname = staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
			String grpname = projectService.getProjectNameByUserid(userModel.getUserid());
			request.setAttribute("deptname", deptname);
			request.setAttribute("grpname", grpname);
			Map<String, String> typeMap = BusnDataDir
					.getMapKeyValue("hrManager.leaveType");
			request.setAttribute("typeMap", typeMap);
			List<SysUser> approverList = staffInfoService.getgroupManagerUp();
			request.setAttribute("approverlist", approverList);
			return "add";
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/**
	 * 
	 * @return
	 */
	public String save() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				// 修改
				Leave l = service.findById(id);
				l.setApprover(leave.getApprover());
				l.setApproverName(staffInfoService.getUsernameById(leave.getApprover()));
				l.setEndTime(leave.getEndTime());
				l.setReason(leave.getReason().trim());
				l.setStartTime(leave.getStartTime());
				l.setStatus(leave.getStatus());
				l.setSumtime(leave.getSumtime());
				l.setType(leave.getType().trim());
				l.setUpdateTime(new Date());
				service.update(l);
				msgBox = new MsgBox(request, getText("user.hols.update"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("user.hols.update"));
			} else {
				// 新增
				leave.setType(leave.getType().trim());
				leave.setUserid(userModel.getUserid());
				leave.setReason(leave.getReason().trim());
				leave.setSubTime(new Date());
				leave.setUsername(userModel.getUsername());
				ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
				List<Project> groupList = groupDao.getProjectByUserId(userModel.getUserid());
				String groupname = "";
				for(Project usg:groupList){
					groupname +=","+usg.getName();
				}
				if(groupname.length()>0){
					leave.setGrpName(groupname.substring(1));
				}
				leave.setApproverName(staffInfoService.getUsernameById(leave.getApprover()));
				service.save(leave);
				SysLog.operLog(request, Constants.OPER_ADD_VALUE,
						userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a Notify : " + leave.getId());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-save())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String modifyPage() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if (id != null && !"".equals(id)) {
				leave = service.findById(id);
				double time = 0;
				if (leave.getType().equals("年假")) {
					time = userHolsService.getRestTime(leave.getUserid(), 1);
				}
				if (leave.getType().equals("调休")) {
					time = userHolsService.getRestTime(leave.getUserid(), 2);
				}
				request.setAttribute("time", time);
				// 获取审批记录
				String record = "";
				List<ApprovalRecord> records = approvalService
						.getRecordByName(leave.getId());
				/*for (int i =0;i<records.size();i++) {
					record += "记录"+(i+1)+":"+"审批人:" + records.get(i).getApprovalUser()
							+ "   审批结果:" + records.get(i).getResult() + "   审批意见:" + records.get(i).getOpinion() + "   审批时间:"
							+ records.get(i).getApprovalTime();
					record += "\n";
				}*/
				for (int i =0;i<records.size();i++) {
					record += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+"\t"
							+records.get(i).getApprovalUser()+"\t审批"+records.get(i).getResult()+"\t审批意见："+records.get(i).getOpinion()
							+"\n";
				}
				request.setAttribute("record", record);
				request.setAttribute("leave", leave);
				Map<String, String> typeMap = BusnDataDir
						.getMapKeyValue("hrManager.leaveType");
				request.setAttribute("typeMap", typeMap);
				List<SysUser> approverlist = staffInfoService
						.getgroupManagerUp();
				request.setAttribute("approverlist", approverlist);
				if (leave.getStatus().equals("待审批")) {
					// 待审批
					msgBox = new MsgBox(request,
							getText("user.leave.approving"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
					return "msgBox";
				} else {
					// 修改
					if (leave.getStatus().equals("新增")
							|| leave.getStatus().equals("不通过")) {
						// 新建和返回修改的可以修改
						if (leave.getUserid().equals(userModel.getUserid())) {
							// 如果是修改自己数据
							return "modify";
						} else {
							msgBox = new MsgBox(request,
									getText("user.leave.modifyself"));
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						}
					}
					else if(leave.getStatus().equals("审批通过")) {
						msgBox = new MsgBox(request,
								getText("user.leave.approvered"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}else{
						msgBox = new MsgBox(request, getText("audit.ActionFailed"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				}
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-save())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/**
	 * 计算两日期差
	 * 
	 * @return
	 */
	public String calculate() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String method = request.getParameter("method");
		double time = 0;
		if (method != null && !"".equals(method)) {
			if ("0".equals(method)) {
				// 计算请假时间
				Date start = DateUtil.stringToDate(startTime,
						"yyyy-MM-dd HH:mm:ss");
				Date end = DateUtil
						.stringToDate(endTime, "yyyy-MM-dd HH:mm:ss");
				if (start.after(end) || start.equals(end)) {
					// 如果开始时间大于结束时间，返回重新修改
					time = -1;
				} else {
					time = TimeConfig.getSumTime(start, end);
				}
			}
			if ("1".equals(method)) {
				// 计算年假剩余时间
				time = userHolsService.getRestTime(userModel.getUserid(), 1);
			}
			if ("2".equals(method)) {
				// 计算调休剩余时间
				time = userHolsService.getRestTime(userModel.getUserid(), 2);
			}
		}
		ajaxPrint("{\"id\":\"" + time + "\"}");
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

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
