package cn.grgbanking.feeltm.borrow.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.borrow.domain.Borrow;
import cn.grgbanking.feeltm.borrow.domain.BorrowDetail;
import cn.grgbanking.feeltm.borrow.domain.BorrowRemind;
import cn.grgbanking.feeltm.borrow.service.BorrowService;
import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.config.SendEmailUtil;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.UserGroupRoleService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class BorrowAction extends BaseAction {
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private UserGroupRoleService userGroupService;
	@Autowired
	private ProjectService projectService;
	
	private Borrow borrow;

	/**
	 * 查询
	 */
	public String query() throws Exception {
		try {
			String from = request.getParameter("from");//
			int pageNum = 1;
			int pageSize = 20;
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if (borrow == null) {
				borrow = new Borrow();
				//是否为行政人员或财务人员
				if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isFinancial")){
					borrow.setStatus("2");
				}else{
					borrow.setStatus("0");
				}
			}else{
				if("6".equals(borrow.getStatus())){
					borrow.setStatus("");
				}
			}
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String borrowTime1 = "";
			String borrowTime2 = "";
			if (StringUtils.isNotBlank(request.getParameter("borrowTime1"))) {
				borrowTime1 = request.getParameter("borrowTime1");
			}
			if (StringUtils.isNotBlank(request.getParameter("borrowTime2"))) {
				borrowTime2 = request.getParameter("borrowTime2");
			}
			//是否为行政人员或财务人员
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isFinancial")){
				request.setAttribute("status", "2");
			}else{
				request.setAttribute("status", "0");
			}
			Page page = borrowService.getPage(borrow, pageNum, pageSize, borrowTime1, borrowTime2,userModel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>) page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.borrow.domain.Borrow");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);

				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				ajaxPrint(input.toString());
				return null;
			} else {
				ActionContext.getContext().put("borrowList", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "borrowList";
	}
	
	/**
	 * 查看详细信息
	 */
	public String show(){
		try {
			borrow = borrowService.getById((String) request.getParameter("ids"));
			//findUserByUserid
			borrow.setUpdatemanid(staffInfoService.getUsernameById(borrow.getUpdatemanid()));
			// 获取审批记录
			String record = "";
			List<ApprovalRecord> records = approvalService.getRecordByName(borrow.getId());
			for (int i =0;i<records.size();i++) {
//				record += "记录"+(i+1)+":"+"审批人:" + records.get(i).getApprovalUser()
//						+ "   审批结果:" + records.get(i).getResult() + "   审批意见:" + records.get(i).getOpinion() + "   审批时间:"
//						+ records.get(i).getApprovalTime();
//				record += "\n";
				record += "["+records.get(i).getResult()+"] "+records.get(i).getApprovalUser()+"("+records.get(i).getApprovalTime()+"):"+records.get(i).getOpinion()+"\n";
			}
			String finRecord = "";
			if(records!=null && records.size()>0){
				finRecord += "["+records.get(0).getResult()+"] "+records.get(0).getApprovalUser()+"("+records.get(0).getApprovalTime()+"):"+records.get(0).getOpinion();
			}
			request.setAttribute("record", record.trim());
			request.setAttribute("finRecord", finRecord);
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-view())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "borrowDetail";
	}
	
	/**
	 * 新增跳转
	 * @return
	 */
	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SysUser usr=staffInfoService.findUserByUserid(userModel.getUserid());
		usr.setGroupName(projectService.getProjectNameByUserid(userModel.getUserid()));
		usr.setDeptName(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
		request.setAttribute("usr", usr);
		List<SysUser> approveList = UserRoleConfig.getFinaByUser();
		request.setAttribute("approvePerson", approveList);
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isFinancial")){
			request.setAttribute("isFinancial", "true");
		}
		return "borrowAdd";
	}
	
	public String save(){
		MsgBox msgBox;
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isFinancial")){
				borrow.setUserid(userModel.getUserid());
				borrow.setUserman(staffInfoService.getUsernameById(userModel.getUserid()));
				borrow.setGroupname(projectService.getProjectNameByUserid(userModel.getUserid()));
				borrow.setDetname(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
				borrow.setCreatedate(new Date());
				borrow.setStatus("0");
				borrow.setUpdateMan(userModel.getUsername());
				borrow.setUpdateDate(new Date());
				borrow.setApproveSum(borrow.getAmount());
				borrow.setBorrowdate(new Date());
				borrowService.save(borrow);
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			}else{
				borrow.setUserid(userModel.getUserid());
				borrow.setUserman(staffInfoService.getUsernameById(userModel.getUserid()));
				borrow.setGroupname(projectService.getProjectNameByUserid(userModel.getUserid()));
				borrow.setDetname(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
				borrow.setCreatedate(new Date());
				borrow.setStatus("3");
				borrow.setUpdateMan(userModel.getUsername());
				borrow.setUpdateDate(new Date());
				borrowService.save(borrow);
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-save())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}

	public String edit(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			String ids = request.getParameter("ids");
			borrow = borrowService.getById(ids);
			List<SysUser> approveList = UserRoleConfig.getFinaByUser();
			request.setAttribute("approvePerson", approveList);
			if("1".equals(borrow.getStatus()) || "3".equals(borrow.getStatus())){
				//判断用户是否为当前用户
				if(!userModel.getUserid().equals(borrow.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你不能修改别人的借款记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(ids);
				for(ApprovalRecord r:records){
//					record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
//					record += "\n";
					record += "["+r.getResult()+"] "+r.getApprovalUser()+"("+r.getApprovalTime()+"):"+r.getOpinion()+"\n";
				}
				request.setAttribute("record", record);
				return "borrowUpdate";
			} else {
				MsgBox msgBox = new MsgBox(request,"只能修改状态为新增或财务审核未通过的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e){
			return null;
		}
	}
	
	public String update(){
		MsgBox msgBox;
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String type=request.getParameter("type");
			type=type==null?"":type;
			Borrow updateBorrow = borrowService.getById(borrow.getId());
			if("audit".equals(type)){
				String status = "";
				if("0".equals(borrow.getStatus())){
					status = "财务审核通过，已发款，代还款";
					updateBorrow.setApproveSum(borrow.getApproveSum());
					updateBorrow.setBorrowdate(new Date());
				}else{
					status = "财务审核未通过";
					updateBorrow.setApproveSum(null);
				}
				String approveContent = request.getParameter("approveContent");
				updateBorrow.setStatus(borrow.getStatus());
				updateBorrow.setExpectedRepaydate(borrow.getExpectedRepaydate());
				updateBorrow.setUpdateMan(userModel.getUsername());
				updateBorrow.setUpdateDate(new Date());
//				updateBorrow.setBorrowdate(new Date());
				borrowService.update(updateBorrow);
				approvalService.makeRecored(borrow.getId(), "", userModel.getUsername(), approveContent, status);
				msgBox = new MsgBox(request, getText("审核成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("审核成功"));
			}else{
				updateBorrow.setAmount(borrow.getAmount());
				updateBorrow.setTripcity(borrow.getTripcity());
				updateBorrow.setType(borrow.getType());
				updateBorrow.setUpdatemanid(borrow.getUpdatemanid());
				updateBorrow.setExpectedRepaydate(borrow.getExpectedRepaydate());
				updateBorrow.setBorrowReason(borrow.getBorrowReason());
				updateBorrow.setUpdateMan(userModel.getUsername());
				updateBorrow.setUpdateDate(new Date());
				borrowService.update(updateBorrow);
				msgBox = new MsgBox(request, getText("修改成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("修改成功"));
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-update())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("update.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	public String delete(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			String id = request.getParameter("ids");
			borrow = borrowService.getById(id);
			if("1".equals(borrow.getStatus()) || "3".equals(borrow.getStatus())){
				//判断用户是否为当前用户
				if(!userModel.getUserid().equals(borrow.getUserid())){
					msgBox = new MsgBox(request,"你不能删除别人的借款记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}else{
					borrowService.delete(id);
					msgBox = new MsgBox(request, getText("删除成功"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("删除成功"));
				}
			}else{
				msgBox = new MsgBox(request,"只能删除新增及财务审核未通过的借款记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-delete())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("delete.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	//提交审核
	public String upAuditing(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			String id = request.getParameter("ids");
			borrow = borrowService.getById(id);
			if("3".equals(borrow.getStatus()) || "1".equals(borrow.getStatus())){
				//判断用户是否为当前用户
				if(!userModel.getUserid().equals(borrow.getUserid())){
					msgBox = new MsgBox(request,"你不能提审别人的借款记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}else{
					borrow.setStatus("2");
					borrow.setUpdateDate(new Date());
					borrow.setUpdateMan(userModel.getUsername());
					borrowService.update(borrow);
					msgBox = new MsgBox(request, getText("提审成功"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("提审成功"));
				}
			}else{
				msgBox = new MsgBox(request,"只能提审新增或财务审核未通过的借款记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}catch (Exception e) {
				SysLog.error(request, "error in(borrow-aduiting())");
				SysLog.error(e);
				msgBox = new MsgBox(request, getText("aduiting.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	
	public String auditing(){
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String ids = request.getParameter("ids");
			borrow = borrowService.getById(ids);
			if("2".equals(borrow.getStatus())){
				//判断是否为当前用户的审核信息
				if(!borrow.getUpdatemanid().equals(userModel.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你没有权限审核该记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(borrow.getId());
				for(ApprovalRecord r:records){
//					record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
//					record += "\n";
					record += "["+r.getResult()+"] "+r.getApprovalUser()+"("+r.getApprovalTime()+"):"+r.getOpinion()+"\n";
				}
				request.setAttribute("record", record);
				return "borrowAudit";
			} else {
				MsgBox msgBox = new MsgBox(request,"只能审核状态为待审核的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e){
			return null;
		}
	}
	
	public String borrow(){
		MsgBox msgBox;
		try {
			String id = request.getParameter("ids");
			borrow = borrowService.getById(id);
			if("2".equals(borrow.getStatus()) ){
				borrow.setStatus("5");
				borrow.setBorrowdate(new Date());
				borrowService.update(borrow);
				msgBox = new MsgBox(request, getText("发款成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("发款成功"));
			}else{
				msgBox = new MsgBox(request,"只能对审核后的记录进行发款");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-borrow())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("borrow.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	public String repay(){
		MsgBox msgBox;
		try {
			String id = request.getParameter("ids");
			borrow = borrowService.getById(id);
			if("0".equals(borrow.getStatus()) ){
				List<BorrowDetail> detailList = borrowService.getBorrowDetailByBorrowId(id);
				double replyAmount = 0.0;
				for(BorrowDetail detail:detailList){
					replyAmount+=detail.getAmount();
				}
				request.setAttribute("detailList", detailList);
				request.setAttribute("replyAmount", replyAmount);
				return "borrowRepay";
			}else{
				msgBox = new MsgBox(request,"请选择财务审核通过，已发款，待还款的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-repay())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("repay.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
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
	 * 保存审核信息
	 * @return
	 */
	public String updateAudit(){
		MsgBox msgBox;
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String borrowid = request.getParameter("borrowId");
			String replydate = request.getParameter("replydate");
			String replyamount = request.getParameter("replyamount");
			String residueamount = request.getParameter("residueamount");
			String replynote = request.getParameter("replynote");
			BorrowDetail detail = new BorrowDetail();
			detail.setReplydate(DateUtil.stringToDate(replydate, "yyyy-MM-dd"));
			detail.setAmount(Double.parseDouble(replyamount));
			detail.setBorrowid(borrowid);
			detail.setResidueAmount(Double.parseDouble(residueamount));
			detail.setNote(replynote);
			borrowService.saveDetail(detail);
			//如果还款金额==借款金额，修改借款单状态为“还款结束”
			if(Double.parseDouble(residueamount)==0){
				Borrow inborrow = borrowService.getById(borrowid);
				inborrow.setStatus("4");
				inborrow.setUpdateDate(new Date());
				inborrow.setRepaydate(new Date());
				inborrow.setUpdateMan(userModel.getUsername());
				borrowService.update(inborrow);
			}
			msgBox = new MsgBox(request,"保存成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-update())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("update.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 还款提醒设置
	 * @return
	 */
	public String remind(){
		MsgBox msgBox;
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isFinancial") || UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "borrow.userdata.isAdmin")){
				BorrowRemind remind = borrowService.getRemindById();
				if(remind!=null){
					request.setAttribute("period", remind.getPeriod());
					request.setAttribute("email", remind.getTypeemail());
					request.setAttribute("oa", remind.getTypeoa());
					request.setAttribute("id", remind.getId());
				}
				return "borrowRemind";
			}else{
				msgBox = new MsgBox(request,"你没有设置权限");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-repay())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("repay.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 保存还款提醒信息
	 * @return
	 */
	public String saveRemind(){
		MsgBox msgBox;
		try {
			String remindid = request.getParameter("remindid");
			String email = request.getParameter("email");
			String oa = request.getParameter("oa");
			String period = request.getParameter("period");
			BorrowRemind remind = new BorrowRemind();
			remind.setTypeemail(email);
			remind.setTypeoa(oa);
			remind.setPeriod(period);
			if(!"".equals(remindid)){
				remind.setId(remindid);
				borrowService.updateRemind(remind);
			}else{
				borrowService.saveRemind(remind);
			}
			msgBox = new MsgBox(request,"保存成功");
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		} catch (Exception e) {
			SysLog.error(request, "error in(borrow-update())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("update.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 定时发送还款提醒信息
	 */
	public void synchronizateSendRepayRemind(){
		BorrowRemind remind = borrowService.getRemindById();
		String currentdate = DateUtil.getDateString(new Date());
		List<Borrow> borrowList = borrowService.getRepayInfoById(currentdate);
		for(Borrow borrow:borrowList){
			SysUser sysuser = staffInfoService.findUserByUserid(borrow.getUserid());
			String content = "请及时归还"+DateUtil.getDateString(borrow.getBorrowdate())+"借款单的钱";
			if(borrow.getRemindDate()==null){//第一次发送还款信息，不需要判断还款设置信息
				if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
					continue;
				}
				SendEmailUtil.sendEmail(sysuser.getEmail(), null, SendEmailUtil.EMAIL_USERAME, "还款提醒", content);
				try{
					SendEmailUtil.oaSendEmail("admin", borrow.getUserid(), content);
				}catch(Exception e){
					e.printStackTrace();
				}
				borrow.setRemindDate(new Date());
				borrowService.update(borrow);
			}else{//判断当前时间是否==上传发送邮件的时间+提醒周期
				if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
					continue;
				}
				if(remind==null){
					continue;
				}
				String end = DateUtil.getDateString(new Date());
				String start = DateUtil.getDateString(borrow.getRemindDate());
				Date edate = DateUtil.stringToDate(end, "yyyy-MM-dd");
				Date sdate = DateUtil.stringToDate(start, "yyyy-MM-dd");
				String day = DateUtil.getDayByEdateToSdate(edate, sdate);
				String period = remind.getPeriod();
				if(period.equals(day)){
					if(!(remind.getTypeemail()==null || "".equals(remind.getTypeemail()))){
						SendEmailUtil.sendEmail(sysuser.getEmail(), null, SendEmailUtil.EMAIL_USERAME, "还款提醒", content);
					}
					if(!(remind.getTypeoa()==null || "".equals(remind.getTypeoa()))){
						try{
							SendEmailUtil.oaSendEmail("admin", borrow.getUserid(), content);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					borrow.setRemindDate(edate);
					borrowService.update(borrow);
				}
			}
		}
	}
	
	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}
	
	
}
