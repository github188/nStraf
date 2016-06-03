package cn.grgbanking.feeltm.rule.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.rule.service.BlackRegulationService;
import cn.grgbanking.feeltm.rule.service.RegulationDeliverService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class BlackRegulationAction extends BaseAction {
	private BlackRegulationService blackRegulationService;
	private RegulationDeliverService regulationDeliverService;

	public RegulationDeliverService getRegulationDeliverService() {
		return regulationDeliverService;
	}

	public void setRegulationDeliverService(
			RegulationDeliverService regulationDeliverService) {
		this.regulationDeliverService = regulationDeliverService;
	}

	public BlackRegulationService getBlackRegulationService() {
		return blackRegulationService;
	}

	public void setBlackRegulationService(
			BlackRegulationService blackRegulationService) {
		this.blackRegulationService = blackRegulationService;
	}

	private String id;
	private String moneyType;
	private String moneyDenomination;
	private String regulation;
	private String regulationStatus;
	private String termtype;
	private Date createDate;
	private String createName;
	private Date reversionDate;
	private String reversionName;
	private String applyId;
	private String model;
	// 新增字段，终端台数
	private int termNum;

	// 规则下发状态
	private String reguStatus;

	public String getReguStatus() {
		return reguStatus;
	}

	public void setReguStatus(String reguStatus) {
		this.reguStatus = reguStatus;
	}

	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String reg = request.getParameter("regulation");
		String type = request.getParameter("moneyType");
		String status = request.getParameter("reguStatus");

		try {
			String from = request.getParameter("from");
			BlackRegulation obj = new BlackRegulation();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			if (reg != null && !reg.equals(""))
				obj.setRegulation(reg);
			if (type != null && !type.equals(""))
				obj.setMoneyType(type);
			if (status != null)
				obj.setReguStatus(status);

			Page page = blackRegulationService.getBlackRegulationPage(obj,
					pageNum, pageSize, userModel.getUserid());

			request.setAttribute("currPage", page);
			List<BlackRegulation> list = page.getQueryResult();
			if (from != null && from.equals("refresh")) {

				List<Object> l = new ArrayList<Object>();
				Map<String, String> moneyTypeMap = BusnDataDir
						.getMap("ruleMgr.moneyType");
				Map<String, String> moneyDenominationMap = BusnDataDir
						.getMap("ruleMgr.moneyDenomination");
				Map<String, String> regulationStatusMap = BusnDataDir
						.getMap("ruleMgr.blMgr");
				Map<String, String> reguStatusMap = BusnDataDir
						.getMap("ruleMgr.reguDeliverStatus");

				for (int i = 0; i < list.size(); i++) {
					BlackRegulation temp = (BlackRegulation) list.get(i);

					Object moneyTypeObj = moneyTypeMap.get(temp.getMoneyType());
					Object moneyDenominationObj = moneyDenominationMap.get(temp
							.getMoneyDenomination());
					Object regulationStatusObj = regulationStatusMap.get(temp
							.getRegulationStatus());

					Object reguStatusObj = reguStatusMap.get(temp
							.getReguStatus());

					if (moneyTypeObj != null)
						temp.setMoneyType(moneyTypeObj.toString());
					if (moneyDenominationObj != null)
						temp.setMoneyDenomination(moneyDenominationObj
								.toString());
					if (regulationStatusObj != null)
						temp
								.setRegulationStatus(regulationStatusObj
										.toString());
					if (reguStatusObj != null)
						temp.setReguStatus(reguStatusObj.toString());

					l.add(temp);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.BlackRegulation");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("blackRegulationList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		//return "query";
	}

	public String add() throws Exception {

		return "add";
	}

	public String save() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String newAnOther = request.getParameter("newAnOther");
		String termid = request.getParameter("termid");
		String[] termids = termid.split(",");
		int term_count = termids.length; // 终端台数
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			blackRegulation.setId(getId());
			blackRegulation.setMoneyType(getMoneyType());
			blackRegulation.setMoneyDenomination(getMoneyDenomination());
			blackRegulation.setRegulation(getRegulation().toUpperCase());
			blackRegulation.setRegulationStatus("1");
			blackRegulation.setTermtype(getTermtype());
			blackRegulation.setCreateDate(new Date());
			blackRegulation.setCreateName(userModel.getUserid());
			blackRegulation.setReversionDate(getReversionDate());
			blackRegulation.setReversionName(getReversionName());
			blackRegulation.setTermNum(term_count);
			blackRegulation.setReguStatus("0");

			boolean flag = false;

			// boolean
			// flag=blackRegulationService.addBlackRegulation(blackRegulation);
			/*
			 * 工作流流 开始
			 */
			Workflow wf = new BasicWorkflow(userModel.getUserid());
			try {

				System.out.println("Begin to Initialize workflow 100 2!");
				long wfid = wf.initialize("blacklist_apply", 100, null);

				// auditInfo
				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setApplayDate(DateUtil
						.getTimeYYYYMMDDHHMMSSString(blackRegulation
								.getCreateDate()));
				auditInfo.setApplyName(blackRegulation.getCreateName()); // 按照数据库文档
				auditInfo.setApplytyp("1");
				auditInfo.setAuditStatus("1");
				auditInfo.setApplyId(String.valueOf(wfid));

				// 增加字段
				blackRegulation.setApplyId(String.valueOf(wfid));

				/******* 规则下发表初始化 *******/
				RegulationDeliver regulationDeliver = new RegulationDeliver();
				regulationDeliver.setApplyId(String.valueOf(wfid)); // 申请编号
				regulationDeliver.setStatus("0"); // 初始状态
				regulationDeliver.setType("1"); // 规则类型 "黑名单"
				// regulationDeliver.setDate(DateUtil
				// .getTimeYYYYMMDDHHMMSSString(blackRegulation
				// .getCreateDate())); // 创建日期
				// regulationDeliver.setRole(blackRegulation.getRegulation());
				// //规则
				for (int i = 0; i < term_count; i++) {
					regulationDeliver.setTermid(termids[i]);
					regulationDeliverService
							.addRegulationDeliver(regulationDeliver); // 添加到数据库
				}
				/********** 规则下发表初始化完成 **********/

				auditInfo.setOrgId(userModel.getOrgid());

				AuditLog auditLog = new AuditLog();
				auditLog.setApplayDate(blackRegulation.getCreateDate());
				auditLog.setApplyId(String.valueOf(wfid));
				auditLog.setApplyStatus("1");
				auditLog.setApplyNote(getText("audit.applyAsk"));
				auditLog.setApplyResult(getText("audit.applyAccross"));
				auditLog.setApplyType("1");
				auditLog.setOrgid(userModel.getOrgid());
				auditLog.setUserid(userModel.getUserid());
				auditLog.setUsername(userModel.getUsername());

				System.out.println("Initialize workflow 100 OK!");
				Map map = new HashMap();
				map.put("applyType", "1");
				map.put("newOwner", blackRegulation.getReversionName());
				wf.doAction(wfid, 1, map);

				flag = blackRegulationService.addAuditBlackRegulation(
						blackRegulation, auditInfo, auditLog);

				System.out.print("申请已经成功提交，请等待审批！");
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.print(ex.getStackTrace());
				System.out.print("提交申请时出现异常，可能没有增加您提交需求的权限！");
			}

			/*
			 * 工作流结束
			 */
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.addfaile"));
				addActionMessage(getText("operInfoform.addfaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
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
			if (newAnOther.equals("true")) {
				return "add";
			}
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		if (newAnOther.equals("true")) {
			// 得到业务主管用户组中用户列表，并保存
			return "add";
		}
		return "msgBox";
	}

	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			String ids = request.getParameter("ids");
			blackRegulation.setId(ids);
			int iCount = 0;
			String[] blids = blackRegulation.getId().split(",");

			if (blids != null) {
				for (int i = 0; i < blids.length; i++) {
					BlackRegulation temp = blackRegulationService
							.getBlackRegulationByApplyId(blids[i]).get(0);
					blackRegulationService.deleteBlackRegulation(temp);
					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, temp
							.getRegulation());
					SysLog.info("User:" + userModel.getUserid()
							+ " to delete a BlackRegulation: "
							+ temp.getRegulation());

					iCount++;
				}
			}

			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public String edit() throws Exception {
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			String ids = request.getParameter("ids");
			blackRegulation = blackRegulationService
					.getBlackRegulationObject(ids);
			ActionContext.getContext().put("blackRegulation", blackRegulation);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			blackRegulation = blackRegulationService
					.getBlackRegulationObject(getId());
			blackRegulation.setId(getId());
			blackRegulation.setMoneyType(getMoneyType());
			blackRegulation.setMoneyDenomination(getMoneyDenomination());
			blackRegulation.setRegulation(getRegulation());
			blackRegulation.setRegulationStatus(getRegulationStatus());
			blackRegulation.setTermtype(getTermtype());
			blackRegulation.setCreateDate(getCreateDate());
			blackRegulation.setCreateName(getCreateName());
			blackRegulation.setReversionDate(getReversionDate());
			blackRegulation.setReversionName(getReversionName());
			boolean flag = blackRegulationService
					.updateBlackRegulation(blackRegulation);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	/**
	 * 显示规则详情，并显示要升级的终端
	 * 
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			String ids = request.getParameter("ids");
			blackRegulation = blackRegulationService
					.getBlackRegulationObject(ids);
			List<RegulationDeliver> rdList = regulationDeliverService
					.getRegulationDeliListByApplyId(blackRegulation
							.getApplyId()); // 得到规则下发详情列表
			ActionContext.getContext().put("blackRegulation", blackRegulation);
			ActionContext.getContext().put("regulationDeliverList", rdList);
			ActionContext.getContext().put("listSize", rdList.size());
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public String info() throws Exception {
		try {
			BlackRegulation blackRegulation = new BlackRegulation();
			String applyIds = request.getParameter("applyId");
			if (applyIds != null && !applyIds.equals("")) {
				List<BlackRegulation> blackList = blackRegulationService
						.getBlackRegulationByApplyId(applyIds);
				blackRegulation = blackList.get(0);
			}
			ActionContext.getContext().put("blackRegulation", blackRegulation);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}

		return "info";
	}

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getMoneyDenomination() {
		return moneyDenomination;
	}

	public void setMoneyDenomination(String moneyDenomination) {
		this.moneyDenomination = moneyDenomination;
	}

	public String getRegulation() {
		return regulation;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	public String getRegulationStatus() {
		return regulationStatus;
	}

	public void setRegulationStatus(String regulationStatus) {
		this.regulationStatus = regulationStatus;
	}

	public String getTermtype() {
		return termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getReversionDate() {
		return reversionDate;
	}

	public void setReversionDate(Date reversionDate) {
		this.reversionDate = reversionDate;
	}

	public String getReversionName() {
		return reversionName;
	}

	public void setReversionName(String reversionName) {
		this.reversionName = reversionName;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getTermNum() {
		return termNum;
	}

	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}

}
