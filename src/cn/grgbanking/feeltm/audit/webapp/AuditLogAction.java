package cn.grgbanking.feeltm.audit.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.audit.service.AuditLogService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({"serial", "unchecked"})
public class AuditLogAction extends BaseAction {
	private AuditLogService auditLogService;

	public AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public void setAuditLogService(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	private String id;
	private String applyId;
	private String userid;
	private String username;
	private Date applayDate;
	private String orgid;
	private String applyType;
	private String applyStatus;
	private String applyResult;
	private String applyNote;

	public String query() throws Exception {
//		UserModel userModel = (UserModel) request.getSession().getAttribute(
//				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");

			AuditLog obj = new AuditLog();

			String str_applyId = request.getParameter("applyId");
			if(str_applyId != null && !str_applyId.equals(""))
				obj.setApplyId(str_applyId);
			
			String str_type = request.getParameter("applyType");
			if (str_type != null && !str_type.equals(""))
				obj.setApplyType(str_type);

			String str_status = request.getParameter("applyStatus");
			if (str_status != null && !str_type.equals(""))
				obj.setApplyStatus(str_status);

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

			Page page = auditLogService.getAuditLogPage(obj, pageNum, pageSize,
					beginDate, endDate);

			request.setAttribute("currPage", page);

			List<AuditLog> list = page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					AuditLog temp = (AuditLog) list.get(i);
					Map applyTypeMap = BusnDataDir.getMap("ruleMgr.applyType");
					Map auditStatusMap = BusnDataDir
							.getMap("auditMgr.auditStatus");
					Object typeObj = applyTypeMap.get(temp.getApplyType());
					Object statuObj = auditStatusMap.get(temp.getApplyStatus());
					if (typeObj != null && !(typeObj.toString().equals("")))
						temp.setApplyType(typeObj.toString());
					if (statuObj != null && !(statuObj.toString().equals("")))
						temp.setApplyStatus(statuObj.toString());
					// temp.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(temp.getApplayDate()));
					l.add((Object) temp);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.AuditLog");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("auditLogList", list);
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
		try {
			AuditLog auditLog = new AuditLog();
			auditLog.setId(getId());
			auditLog.setApplyId(getApplyId());
			auditLog.setUserid(getUserid());
			auditLog.setUsername(getUsername());
			auditLog.setApplayDate(getApplayDate());
			auditLog.setOrgid(getOrgid());
			auditLog.setApplyType(getApplyType());
			auditLog.setApplyStatus(getApplyStatus());
			auditLog.setApplyResult(getApplyResult());
			auditLog.setApplyNote(getApplyNote());
			boolean flag = auditLogService.addAuditLog(auditLog);
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
			AuditLog auditLog = new AuditLog();
			String ids = request.getParameter("ids");
			auditLog.setId(ids);
			boolean flag = auditLogService.deleteAuditLog(auditLog);
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
			AuditLog auditLog = new AuditLog();
			String ids = request.getParameter("ids");
			auditLog = auditLogService.getAuditLogObject(ids);
			ActionContext.getContext().put("auditLog", auditLog);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			AuditLog auditLog = new AuditLog();
			auditLog = auditLogService.getAuditLogObject(getId());
			auditLog.setId(getId());
			auditLog.setApplyId(getApplyId());
			auditLog.setUserid(getUserid());
			auditLog.setUsername(getUsername());
			auditLog.setApplayDate(getApplayDate());
			auditLog.setOrgid(getOrgid());
			auditLog.setApplyType(getApplyType());
			auditLog.setApplyStatus(getApplyStatus());
			auditLog.setApplyResult(getApplyResult());
			auditLog.setApplyNote(getApplyNote());
			boolean flag = auditLogService.updateAuditLog(auditLog);
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

	public String show() throws Exception {
		try {
			AuditLog auditLog = new AuditLog();
			String ids = request.getParameter("ids");
			auditLog = auditLogService.getAuditLogObject(ids);
			AuditInfo auditInfo = new AuditInfo();
			List<AuditInfo> list = auditLogService.getAuditInfoByApplyId(auditLog.getApplyId());
			auditInfo = list.get(0);
			auditInfo.setApplayDate(DateUtil.stringYYYYMMDDHHMMSSTo(auditInfo
					.getApplayDate()));
			request.getSession().setAttribute("applytyp",
					auditInfo.getApplytyp());
			request.getSession().setAttribute("wfid", auditInfo.getApplyId());
			ActionContext.getContext().put("auditInfo", auditInfo);
			//List<AuditLog> auditLogList = new ArrayList<AuditLog>();
			//auditLogList.add(auditLog);
			//ActionContext.getContext().put("auditLogList", auditLogList);
			List<RegulationDeliver> regulationDeliverList = auditLogService.getRegulationDeliverByApplyId(auditLog.getApplyId());
			ActionContext.getContext().put("regulationDeliverList", regulationDeliverList);
			ActionContext.getContext().put("listSize", regulationDeliverList.size());
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getApplayDate() {
		return applayDate;
	}

	public void setApplayDate(Date applayDate) {
		this.applayDate = applayDate;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getApplyResult() {
		return applyResult;
	}

	public void setApplyResult(String applyResult) {
		this.applyResult = applyResult;
	}

	public String getApplyNote() {
		return applyNote;
	}

	public void setApplyNote(String applyNote) {
		this.applyNote = applyNote;
	}

}
