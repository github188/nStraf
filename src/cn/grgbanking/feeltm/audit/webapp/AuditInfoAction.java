package cn.grgbanking.feeltm.audit.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.audit.service.AuditInfoService;
import cn.grgbanking.feeltm.audit.service.AuditLogService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.datadir.dao.DataDirDao;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.OrgInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.org.service.OrgInfoService;
import cn.grgbanking.feeltm.parseMsg.Transfer;
import cn.grgbanking.feeltm.parseMsg.appMessage.R2001Msg;
import cn.grgbanking.feeltm.parseMsg.appMessage.S2001Msg;
import cn.grgbanking.feeltm.rule.service.BlackRegulationService;
import cn.grgbanking.feeltm.rule.service.RegulationDeliverService;
import cn.grgbanking.feeltm.rule.service.RepeatRegulationService;
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
public class AuditInfoAction extends BaseAction {
	@Autowired
	private   DataDirDao dataDirDao;
	private AuditInfoService auditInfoService;
	private AuditLogService auditLogService;
	private BlackRegulationService blackRegulationService;
	private RepeatRegulationService repeatRegulationService;
	private RegulationDeliverService regulationDeliverService;
	private OrgInfoService orgInfoService = new OrgInfoService();
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

	public RepeatRegulationService getRepeatRegulationService() {
		return repeatRegulationService;
	}

	public void setBlackRegulationService(
			BlackRegulationService blackRegulationService) {
		this.blackRegulationService = blackRegulationService;
	}

	public void setRepeatRegulationService(
			RepeatRegulationService repeatRegulationService) {
		this.repeatRegulationService = repeatRegulationService;
	}

	public AuditInfoService getAuditInfoService() {
		return auditInfoService;
	}

	public void setAuditInfoService(AuditInfoService auditInfoService) {
		this.auditInfoService = auditInfoService;
	}

	private String auditStatus;
	private String orgId;
	private String applyName;
	private String applayDate;
	private String applytyp;
	private String id;
	private String applyId;

	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");

			AuditInfo auditInfo = new AuditInfo();
			String str_status = request.getParameter("auditStatus");

			if (str_status != null && !str_status.equals("")) {
				auditInfo.setAuditStatus(str_status);
			}

			String str_type = request.getParameter("applytyp");
			if (str_type != null && !str_type.equals("")) {
				auditInfo.setApplytyp(str_type);
			}

			String flowStatus = request.getParameter("flowStatus");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			String[] params = { flowStatus };

			Page page = auditInfoService
					.getAuditInfoPage(auditInfo, pageNum, pageSize, userModel
							.getUserid(), beginDate, endDate, params);

			request.setAttribute("currPage", page);

			List<AuditInfo> list = page.getQueryResult();
			
			// 将申请时间格式化成 yyyy-MM-dd HH:mm:ss 格式
			for (int i = 0; i < list.size(); i++) {
				AuditInfo tmp = list.get(i);
				tmp.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(tmp
						.getApplayDate()));
			
//				List<OrgInfo> orgList = orgInfoService.getOrgInfoListByOrgid(tmp.getOrgId());
//				if(orgList!=null)
//				tmp.setOrgId(orgList.get(0).getOrgid());
				//OrgInfo org = new OrgInfo();
					//org = orgInfoService.getOrgInfoByOrgId(tmp.getOrgId());
					//if(org!=null)
					//	tmp.setOrgId(org.getOrgname());
				//System.out.println(org.getOrgname());
				//OrgInfo org = orgInfoService.getOrgInfoByOrgId(tmp.getOrgId());
				//System.out.println(org.getOrgname());
			}
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					AuditInfo temp = (AuditInfo) list.get(i);
					Map applyTypeMap = BusnDataDir.getMap("ruleMgr.applyType");
					Map auditStatusMap = BusnDataDir
							.getMap("auditMgr.flowStatus");
					Map orgInfoMap = dataDirDao.getOrgInfoMap();
					Object orgidObj = orgInfoMap.get(temp.getOrgId());
					Object typeObj = applyTypeMap.get(temp.getApplytyp());
					Object statuObj = auditStatusMap.get(temp.getAuditStatus());
					if(orgidObj!=null&&!(orgidObj.toString().equals(""))){
						temp.setOrgId(orgidObj.toString());
					}
					if (typeObj != null && !(typeObj.toString().equals("")))
						temp.setApplytyp(typeObj.toString());
					if (statuObj != null && !(statuObj.toString().equals("")))
						temp.setAuditStatus(statuObj.toString());
					// temp.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(temp.getApplayDate()));
					l.add((Object) temp);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.AuditInfo");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("auditInfoList", list);
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

	public String audit() {
		// UserModel userModel = (UserModel) request.getSession().getAttribute(
		// Constants.LOGIN_USER_KEY);
		String itemid = (String) request.getParameter("ids");
		try {
			AuditInfo auditInfo = new AuditInfo();
			auditInfo = auditInfoService.getAuditInfoObject(itemid);
			auditInfo.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(auditInfo
					.getApplayDate()));
			request.getSession().setAttribute("applytyp",
					auditInfo.getApplytyp());
			request.getSession().setAttribute("wfid", auditInfo.getApplyId());
			List<AuditLog> auditLogList = auditLogService
					.getAuditLogByApplyId(auditInfo.getApplyId());
			ActionContext.getContext().put("auditInfo", auditInfo);
			ActionContext.getContext().put("auditLogList", auditLogList);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "audit";
	}

	public String save() throws Exception {
		try {
			AuditInfo auditInfo = new AuditInfo();
			auditInfo.setAuditStatus(getAuditStatus());
			auditInfo.setOrgId(getOrgId());
			auditInfo.setApplyName(getApplyName());
			auditInfo.setApplayDate(getApplayDate());
			auditInfo.setApplytyp(getApplytyp());
			auditInfo.setId(getId());
			auditInfo.setApplyId(getApplyId());
			boolean flag = false;
			try {
				AuditLog auditLog = new AuditLog();
				auditLog.setApplayDate(new Date());

				// BlackRegulation blackRegulation = new BlackRegulation();

				flag = auditInfoService.addAuditInfo(auditInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				;
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
			return "msgBox";
		}
		return "msgBox";
	}

	public String delete() throws Exception {
		try {
			AuditInfo auditInfo = new AuditInfo();
			String ids = request.getParameter("ids");
			auditInfo.setId(ids);
			boolean flag = auditInfoService.deleteAuditInfo(auditInfo);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.deleteok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operator.addfaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
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

			AuditInfo auditInfo = new AuditInfo();
			String ids = request.getParameter("ids");
			auditInfo = auditInfoService.getAuditInfoObject(ids);
			List autditLog = auditLogService.getAuditLogList(auditInfo
					.getApplyId());

			request.setAttribute("username", "5602");
			request.setAttribute("wfid", auditInfo.getApplyId());
			ActionContext.getContext().put("auditInfo", auditInfo);
			ActionContext.getContext().put("autditLog", autditLog);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	/**
	 * 审核结果提交页面
	 * 
	 * @author Yondy Chow
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			// 最好从页面中传一个auditInfo.getApplytyp()参数，这样不需要查询数据库
			AuditInfo auditInfo = new AuditInfo();
			auditInfo = auditInfoService.getAuditInfoObject(getId());
			boolean flag = false;
			try {
				AuditLog auditLog = new AuditLog();
				auditLog.setApplayDate(new Date());
				auditLog.setApplyId(auditInfo.getApplyId());
				auditLog.setUsername(userModel.getUsername());
				auditLog.setOrgid(userModel.getOrgid());
				auditLog.setApplyType(auditInfo.getApplytyp());
				// auditLog.setApplyStatus("9"); // 通过
				auditLog.setUserid(userModel.getUserid());
				String applyNote = request.getParameter("approveNote");
				String applyResult = request.getParameter("approveResult");
				String str_action = request.getParameter("approveRst");
				// String applyResult=re
				auditLog.setApplyNote(applyNote);
				auditLog.setApplyResult(applyResult);

				Workflow wf = new BasicWorkflow(userModel.getUserid());
				long wfid = Long.parseLong(auditInfo.getApplyId());
				int action = Integer.parseInt(str_action);
				String newOwner = request.getParameter("nextHandlerName");
				String regulationStatus = request
						.getParameter("regulationStatus");
				Map map = new HashMap();
				map.put("newOwner", newOwner);
				map.put("applyId", auditInfo.getApplyId());
				map.put("regulationStatus", regulationStatus);
				// map.put("reguStatus", "1");
				if (auditInfo.getApplytyp().equals("1")) {

					map.put("flowName", "blacklist_apply");

					// auditInfo.setAuditStatus("9");

				} else {
					map.put("flowName", "repeat_apply");
				}
				wf.doAction(wfid, action, map);
				flag = auditInfoService.audit(auditLog);

//				List<RegulationDeliver> reList = regulationDeliverService
//						.getRegulationDeliListByApplyId(auditInfo.getApplyId()); // 根据申请编号得到规则下发列表
//				int reListSize = reList.size();
//
//				/** 重组规则 */
//				String str_date = DateUtil
//						.getTimeYYYYMMDDHHMMSSString(new Date());
//				String str_version = "v_"
//						+ Long.toString(System.currentTimeMillis());
//				// System.out.println(str_version.length());
//				// List<BlackRegulation> blList = blackRegulationService
//				// .getBlackRegulationList(); // 审核通过的黑名单列表
//				List<BlackRegulation> blList = blackRegulationService
//						.getBlackRegulationByApplyId(auditInfo.getApplyId()); // 根据ApplyId得到当前审核规则
//				// List<RepeatRegulation> rList = repeatRegulationService
//				// .getLastRepeatRegulationList(); // 审核通过的重号规则列表
//				List<RepeatRegulation> rList = repeatRegulationService
//						.getRepeatRegulationByApplyId(auditInfo.getApplyId()); //根据ApplyId得到当前审核规则
//
//				// 黑名单规则
//				StringBuffer blackRegu = new StringBuffer();
//				if (blList != null && blList.size() != 0) {
//					BlackRegulation blObj = blList.get(0);
//					blackRegu.append(blObj.getMoneyType()); // 币种
//					blackRegu.append(blObj.getMoneyDenomination()); // 面额
//					blackRegu.append(blObj.getRegulation()); // 规则
//				} else {
//					blackRegu.append("0000");
//				}
//
//				// 重号规则
//				StringBuffer repeatRegu = new StringBuffer();
//				if (rList != null && rList.size() != 0) {
//					RepeatRegulation temp = rList.get(0);
//					repeatRegu.append(temp.getRepeatnum() + ","
//							+ temp.getDealwithMode() + "," + temp.getLogMode()
//							+ "," + temp.getCreenMode() + ","
//							+ temp.getEnterAccountMode());
//				} else {
//					repeatRegu.append("0000");
//				}
//
//				for (int i = 0; i < reListSize; i++) {
//					RegulationDeliver temp = reList.get(i);
//					temp.setDate(str_date);
//					// temp.setStatus("1"); // 待下发状态
//					temp.setVersion(str_version);
//
//					String role_str = temp.getRole();
//					int index = role_str.indexOf("|");
//					String bl_str = role_str.substring(0, index);
//					String re_str = role_str.substring(index + 1);
//					if (!bl_str.equals("0000")) {
//						if (!blackRegu.toString().equals("0000")) {
//							bl_str += "," + blackRegu.toString();
//						}
//					} else {
//						bl_str = blackRegu.toString();
//					}
//					if(!re_str.equals("0000")) {
//						if(!repeatRegu.toString().equals("0000")) {
//							re_str = repeatRegu.toString();
//						}
//					} else {
//						re_str = repeatRegu.toString();
//					}
//					temp.setRole(bl_str + "|" + re_str);
//
//					regulationDeliverService.updateRegulationDeliver(temp);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("audit.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request, getText("audit.fail"));
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

	@SuppressWarnings("unused")
	private R2001Msg getReturnPack(String messageNo) throws Exception {
		S2001Msg smsg = new S2001Msg();
		smsg.setMessageno(messageNo);
		// String time = DateUtil.sdf_yyyyMMddHHmmss.format(new Date());
		// smsg.setTxdate(time.substring(0, 8));
		// smsg.setTxtime(time.substring(8));

		byte[] returnpack = null;
		byte[] sendpack = smsg.packMessage();

		// 发送报文，并等待获取返回报文
		Transfer trans = new Transfer();
		returnpack = trans.sendPack(sendpack);

		if (returnpack == null) {
			return null;
		} else {
			R2001Msg rmsg = new R2001Msg();
			rmsg.unpackMessage(returnpack);
			return rmsg;
		}
	}

	public String show() throws Exception {
		try {
			AuditInfo auditInfo = new AuditInfo();
			String ids = request.getParameter("ids");
			auditInfo = auditInfoService.getAuditInfoObject(ids);
			auditInfo.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(auditInfo
					.getApplayDate()));
			ActionContext.getContext().put("auditInfo", auditInfo);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplayDate() {
		return applayDate;
	}

	public void setApplayDate(String applayDate) {
		this.applayDate = applayDate;
	}

	public String getApplytyp() {
		return applytyp;
	}

	public void setApplytyp(String applytyp) {
		this.applytyp = applytyp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public void setAuditLogService(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}
		private String approveRst ;
		public String getApproveRst() {
			return approveRst;
		}
	 
		public void setApproveRst(String approveRst) {
			this.approveRst = approveRst;
		}
}
