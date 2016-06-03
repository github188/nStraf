package cn.grgbanking.feeltm.notify.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.SendEmailUtil;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.notify.domain.Notify;
import cn.grgbanking.feeltm.notify.service.NotifyService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

/**
 * 通知
 * @author lhyan3
 *
 */
@SuppressWarnings("serial")
public class NotifyAction extends BaseAction{
	
	private String notifyNum;
	private String type;
	private String title;
	private String startTime;
	private String endTime;
	private String status;
	private String form;
	private String mainids;
	private String extrasids;
	private Notify notify;
	private String notifyids;
	private String result;
	private String option="";
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	
	@Autowired
	private ApprovalService approvalService;
	
	/**
	 * 
	 * @return
	 */
	public String view(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(null != notifyNum && !"".equals(notifyNum)){
				Notify notify = notifyService.findByNotifyNum(notifyNum);
				//通知类型
				Map typeMap = BusnDataDir.getMapKeyValue("staffManager.notifyType");
				notify.setType(BusnDataDir.getValue(typeMap, notify.getType()));
				request.setAttribute("notify", notify);
				//notify.setStatus(BusnDataDir.getValue(BusnDataDir.getMapKeyValue("staffManager.notifyStatus"),notify.getStatus()));
				//获取主送人
				String[] idnames = notifyService.findSendersByNum(notify.getNotifyNum());
				request.setAttribute("mainids", idnames[0]);
				request.setAttribute("mainnames", idnames[1]);
				request.setAttribute("extraids", idnames[2]);
				request.setAttribute("extranames", idnames[3]);
				//页面获取创建人
				List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
				request.setAttribute("approvallist", approvallist);
				request.setAttribute("typeMap", typeMap);
				//获取审批记录
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(notify.getNotifyNum());
				for (int i =0;i<records.size();i++) {
					record += "记录"+(i+1)+":"+"审批人:" + records.get(i).getApprovalUser()
							+ "   审批结果:" + records.get(i).getResult() + "   审批意见:" + records.get(i).getOpinion() + "   审批时间:"
							+ records.get(i).getApprovalTime();
					record += "\n";
				}
				request.setAttribute("record", record);
				return "view";
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error("error in (NotifyAction.java-modifyPage())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 2014-5-8 lhy 审批
	 * @return
	 */
	public String audit(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(notifyNum!=null  && !"".equals(notifyNum)){
				Notify n = notifyService.findByNotifyNum(notifyNum);
				n.setOatype(notify.getOatype());
				n.setEmailtype(notify.getEmailtype());
				n.setMobiletype(notify.getMobiletype());
				if(result!=null && !"".equals(result)){
					//生成审批记录
					//有审批结果
					if("0".equals(result)){
						//通过 
						n.setStatus("3");
						if(1==notify.getOatype()){
							//oa发送
							//String[] touserids = notifyService.findUseridByNum(n.getNotifyNum());
							String toUsers = notifyService.findUseridByNum1(n.getNotifyNum());
							if(toUsers!=null && !"".equals(toUsers)){
								SendEmailUtil.oaSendEmail(n.getSender(),toUsers,n.getContent());
							}
						}
						if(1==notify.getEmailtype()){
							//发送邮件-------------------------
							String mainemails = notifyService.findEmailByNum(n.getNotifyNum(),1); 
							String copyemails = notifyService.findEmailByNum(n.getNotifyNum(), 0);
							String sender = notifyService.findSenderEmailByNum(n.getNotifyNum());
							//邮件发送
							SendEmailUtil.sendEmail(mainemails,copyemails,sender,n.getTitle(),n.getContent());
						}
						/*if(1==notify.getMobiletype()){
							//手机短信
						}*/
						approvalService.makeRecored(n.getNotifyNum(), "通知发送通过", n.getApproverName(), option,"通过");
						n.setOatype(notify.getOatype());
						n.setEmailtype(notify.getEmailtype());
						n.setMobiletype(notify.getMobiletype());
						n.setSendTime(new Date());
					}
					if("1".equals(result)){
						//不通过
						n.setStatus("1");
						approvalService.makeRecored(n.getNotifyNum(), "通知发送不通过", n.getApproverName(), option,"不通过");
					}
					notifyService.updateNotify(n);
				}
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " audit a Notify : " + n.getNotifyNum());
				msgBox = new MsgBox(request, getText("notify.approvalok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("notify.approvalok"));
			}else{
				msgBox = new MsgBox(request, getText("operInfoform.exit"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.addfaile"));
			}
		} catch (Exception e) {
			SysLog.error(request,"error in (NotifyAction.java-audit())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}
		return "msgBox";
		
	}
	
	/**
	 * 审批页面
	 * @return
	 * lhyan3
	 * 2014年6月9日
	 */
	public String auditPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(null != notifyNum && !"".equals(notifyNum)){
				Notify notify = notifyService.findByNotifyNum(notifyNum);
				//notify.setStatus(BusnDataDir.getValue(BusnDataDir.getMapKeyValue("staffManager.notifyStatus"),notify.getStatus()));
				//获取主送人
				String[] idnames = notifyService.findSendersByNum(notify.getNotifyNum());
				request.setAttribute("mainids", idnames[0]);
				request.setAttribute("mainnames", idnames[1]);
				request.setAttribute("extraids", idnames[2]);
				request.setAttribute("extranames", idnames[3]);
				//页面获取创建人
				request.setAttribute("username", userModel.getUsername());
				List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
				request.setAttribute("approvallist", approvallist);
				//通知类型
				Map typeMap = BusnDataDir.getMapKeyValue("staffManager.notifyType");
				request.setAttribute("notify", notify);
				request.setAttribute("typeMap", typeMap);
				//获取审批记录
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(notify.getNotifyNum());
				for (int i =0;i<records.size();i++) {
					record += "记录"+(i+1)+":"+"审批人:" + records.get(i).getApprovalUser()
							+ "   审批结果:" + records.get(i).getResult() + "   审批意见:" + records.get(i).getOpinion() + "   审批时间:"
							+ records.get(i).getApprovalTime();
					record += "\n";
				}
				request.setAttribute("record", record);
				if(notify.getStatus().equals("2")){
					//提交审批的
					if(userModel.getUserid().equals(notify.getApprover())){
						notify.setType(BusnDataDir.getValue(typeMap, notify.getType()));
						return "audit";
					}else{
						msgBox = new MsgBox(request, getText("notify.status.approverself"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				}else{
					if("0".equals(notify.getStatus()) || "1".equals(notify.getStatus())){
						//新增，未通过,不能审批
						msgBox = new MsgBox(request, getText("notify.cannot.cantnew"), "notify");
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
					else if("3".equals(notify.getStatus())){
						msgBox = new MsgBox(request, getText("notify.cannot.approvered"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
					else{
						//不能修改
						msgBox = new MsgBox(request, getText("audit.ActionFailed"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				}
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error("error in (NotifyAction.java-auditPage())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * lhy 2014-5-5
	 * 修改页面
	 * @return
	 */
	public String modifyPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(null != notifyNum && !"".equals(notifyNum)){
				Notify notify = notifyService.findByNotifyNum(notifyNum);
				//notify.setStatus(BusnDataDir.getValue(BusnDataDir.getMapKeyValue("staffManager.notifyStatus"),notify.getStatus()));
				//获取主送人
				String[] idnames = notifyService.findSendersByNum(notify.getNotifyNum());
				request.setAttribute("mainids", idnames[0]);
				request.setAttribute("mainnames", idnames[1]);
				request.setAttribute("extraids", idnames[2]);
				request.setAttribute("extranames", idnames[3]);
				//页面获取创建人
				request.setAttribute("username", userModel.getUsername());
				List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
				request.setAttribute("approvallist", approvallist);
				//通知类型
				Map typeMap = BusnDataDir.getMapKeyValue("staffManager.notifyType");
				request.setAttribute("notify", notify);
				request.setAttribute("typeMap", typeMap);
				//获取审批记录
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(notify.getNotifyNum());
				for (int i =0;i<records.size();i++) {
					record += "记录"+(i+1)+":"+"审批人:" + records.get(i).getApprovalUser()
							+ "   审批结果:" + records.get(i).getResult() + "   审批意见:" + records.get(i).getOpinion() + "   审批时间:"
							+ records.get(i).getApprovalTime();
					record += "\n";
				}
				request.setAttribute("record", record);
				if(notify.getStatus().equals("2")){
					//提交审批的
					msgBox = new MsgBox(request, getText("notify.status.approving"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
					return "msgBox";
				}else{
					if("0".equals(notify.getStatus()) || "1".equals(notify.getStatus())){
						//新增，未通过,可以修改
						if(!notify.getSender().equals(userModel.getUserid())){
							//不是自己的不能修改
							msgBox = new MsgBox(request, getText("notify.cannot.modifymyself"), "notify");
							msgBox.setButtonType(MsgBox.BUTTON_RETURN);
							return "msgBox";
						}else{
							return "modify";
						}
					}
					else if("3".equals(notify.getStatus())){
						msgBox = new MsgBox(request, getText("notify.cannot.approvered"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
					else{
						//不能修改
						msgBox = new MsgBox(request, getText("audit.ActionFailed"));
						msgBox.setButtonType(MsgBox.BUTTON_RETURN);
						return "msgBox";
					}
				}
			}else{
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error("error in (NotifyAction.java-modifyPage())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * lhy 2014-5-5
	 * 提交审核
	 * @return
	 */
	public String auditing(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		StringBuffer strbuf = new StringBuffer();
		if(null != notifyids && !"".equals(notifyids)){
			String notifyidstr[] = notifyids.split(",");
			Notify notify = new Notify();
			try {
				for(String id:notifyidstr){
					if(null != id && !"".equals(id)){
						notify = notifyService.findByNotifyNum(id);
						if(notify.getSender().equals(userModel.getUserid())){
							strbuf.append(notify.getNotifyNum()+",");
						}else{
							//不能提交
							msgBox = new MsgBox(request, getText("notify.approval.audit"), "notify");
							return "msgBox";
						}
					}
				}
					if(strbuf.toString().length()>0){
						int i = notifyService.upAuditing(strbuf.toString());
						SysLog.operLog(request, Constants.OPER_DELETE_VALUE,
								strbuf.toString());// 记录日志
						SysLog.info("User:" + userModel.getUserid()
								+ " audit a notidy : " + notify.getNotifyNum());
						msgBox = new MsgBox(request,
								getText("notify.audit"), "notify",
								new String[] { String.valueOf(i) });
					}else{
						msgBox = new MsgBox(request, getText("notify.notaudit"), "notify");
					}
			} catch (Exception e) {
				SysLog.error(request,
						"error in (NotifyAction.java-auditing())");
				SysLog.error(e);
				SysLog.info("notify " + strbuf.toString() + " is not exist !");
				msgBox = new MsgBox(request, getText("operInfoform.isnotexist"), "um");
			}
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	public void refresh(){
		try {
			list();
		} catch (Exception e) {
			SysLog.error("error in (NotifyAction.java-refresh())");
			e.printStackTrace();
			SysLog.error(e);
		}
	}
	
	/**
	 * lhy 2014-4-29
	 * 添加页面
	 * @return
	 */
	public String addPage(){
		//页面获取创建人
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		request.setAttribute("username", userModel.getUsername());
		request.setAttribute("userid", userModel.getUserid());
		List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
		request.setAttribute("approvallist", approvallist);
		//通知类型
		Map typeMap = BusnDataDir.getMapKeyValue("staffManager.notifyType");
		request.setAttribute("typeMap", typeMap);
		return "add";
	}
	
	/**
	 * lhy 2014-5-4
	 * 保存
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(notifyNum!=null && !"".equals(notifyNum)){
				//修改
				Notify n = notifyService.findByNotifyNum(notifyNum);
				n.setApprover(notify.getApprover());
				n.setApproverName(staffInfoService.getUsernameById(notify.getApprover()));
				n.setContent(notify.getContent());
				n.setEmailtype(notify.getEmailtype());
				n.setMobiletype(notify.getMobiletype());
				n.setOatype(notify.getOatype());
				n.setTitle(notify.getTitle());
				n.setType(notify.getType());
				n.setWriteTime(new Date());
				notifyService.updateNotify(n,mainids,extrasids);
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a Notify : " + n.getNotifyNum());
				msgBox = new MsgBox(request, getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.updateok"));
			}else{
				//保存
				notify.setSender(userModel.getUserid());
				notify.setUsername(userModel.getUsername());
				notify.setWriteTime(new Date());
				notify.setNotifyNum(notifyService.getNextNum());
				notify.setApproverName(staffInfoService.getUsernameById(notify.getApprover()));
				notifyService.addNotify(notify,mainids,extrasids);
				SysLog.operLog(request, Constants.OPER_ADD_VALUE, userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a Notify : " + notify.getNotifyNum());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			}
		} catch (Exception e) {
			SysLog.error(request,"error in (NotifyAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a Notify :" + notify.getNotifyNum());
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}
		return "msgBox";
	}
	
	/**
	 * lhy 2014-5-5
	 * @return
	 */
	public String delete(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		StringBuffer strbuf = new StringBuffer();
		if(null != notifyids && !"".equals(notifyids)){
			String notifyidstr[] = notifyids.split(",");
			Notify notify = new Notify();
			try {
				for(String id:notifyidstr){
					if(null != id && !"".equals(id)){
						notify = notifyService.findByNotifyNum(id);
						if(userModel.getUserid().equals(notify.getSender())){
							if("0".equals(notify.getStatus())){
								strbuf.append(notify.getNotifyNum()+",");
							}else{
								//不能删除
								msgBox = new MsgBox(request, getText("notify.cannot.delete"), "notify");
								return "msgBox";
							}
						}else{
							//不能删除
							msgBox = new MsgBox(request, getText("notify.can.deleteself"), "notify");
							return "msgBox";
						}
					}
				}
					if(strbuf.toString().length()>0){
						int i = notifyService.deleteNotifys(strbuf.toString());
						SysLog.operLog(request, Constants.OPER_DELETE_VALUE,
								strbuf.toString());// 记录日志
						SysLog.info("User:" + userModel.getUserid()
								+ " delete  notidy : " + strbuf.toString());
						msgBox = new MsgBox(request,"删除成功", "notify",
								new String[] { String.valueOf(i) });
					}else{
						msgBox = new MsgBox(request, getText("notify.notaudit"), "notify");
					}
			} catch (Exception e) {
				SysLog.error(request,
						"error in (NotifyAction.java-auditing())");
				SysLog.error(e);
				SysLog.info("notify " + strbuf.toString() + " is not exist !");
				msgBox = new MsgBox(request, getText("operInfoform.isnotexist"), "um");
			}
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**lhy 2014-4-30
	 * 人员选择
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String select(){
		//部门与员工
		String see = request.getParameter("see");
		String hidden = request.getParameter("hidden");
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
		request.setAttribute("seename", see);
		request.setAttribute("seeid", hidden);
		return "select";
	}
	
	/**
	 * lhy 2014-4-30
	 * 通知列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		try{
			Notify notify = new Notify();
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			request.setAttribute("userid", userModel.getUserid());
			//通知类型
			Map typeMap = BusnDataDir.getMapKeyValue("staffManager.notifyType");
			request.setAttribute("typeMap", typeMap);
			//通知类型
			Map statusMap = BusnDataDir.getMapKeyValue("staffManager.notifyStatus");
			request.setAttribute("statusMap", statusMap);
			
			int pageNum = 1;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if(null !=notifyNum && !"".equals(notifyNum)){
				notify.setNotifyNum(notifyNum);
			}
			if(null != type && !"".equals(type)){
				notify.setType(type);
			}
			if(null != title && !"".equals(title)){
				notify.setTitle(title);
			}
			if(null != status && !"".equals(status)){
				notify.setStatus(status);
			}
			
			Page page = notifyService.findNotifyPage(notify,userModel,startTime,endTime,pageNum,pageSize);
			request.setAttribute("currPage", page);
			List list = page.getQueryResult();
			for(int i = 0;i<list.size();i++){
				Notify n = (Notify) list.get(i);
				n.setType(BusnDataDir.getValue(typeMap, n.getType()));
			}
			if (form != null && form.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					l.add((Object) list.get(i));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.notify.domain.Notify");
				JSONArray jsonArray = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonArray);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}else {
				ActionContext.getContext().put("notify", list);
				return "list";
			}
		}catch(Exception e){
			SysLog.error(request, "error in (NotifyoAction.java-list())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	////////
	public String getNotifyNum() {
		return notifyNum;
	}

	public void setNotifyNum(String notifyNum) {
		this.notifyNum = notifyNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}

	public String getMainids() {
		return mainids;
	}

	public void setMainids(String mainids) {
		this.mainids = mainids;
	}

	public String getExtrasids() {
		return extrasids;
	}

	public void setExtrasids(String extrasids) {
		this.extrasids = extrasids;
	}
	public Notify getNotify() {
		return notify;
	}
	public void setNotify(Notify notify) {
		this.notify = notify;
	}
	public String getNotifyids() {
		return notifyids;
	}
	public void setNotifyids(String notifyids) {
		this.notifyids = notifyids;
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
