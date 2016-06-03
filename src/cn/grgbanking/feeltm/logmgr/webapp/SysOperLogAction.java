package cn.grgbanking.feeltm.logmgr.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.SysOperLog;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.logmgr.service.OperLogService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class SysOperLogAction extends BaseAction {
	/** identifier field */
	private String id;

	/** nullable persistent field */
	private String loginid;

	/** nullable persistent field */
	private String userid;

	/** nullable persistent field */
	private String username;

	/** nullable persistent field */
	private Date logtime;

	/** nullable persistent field */
	private String type;

	/** nullable persistent field */
	private String result;

	/** nullable persistent field */
	private String menuid;

	/** nullable persistent field */
	private String operid;

	/** nullable persistent field */
	private String note;

	private String orgfloor;

	private OperLogService operLogService;

	public void refresh() {
		list();
	}

	public String list() {

		SysOperLog sysOperLog = new SysOperLog();
		String userid = request.getParameter("userid");

		if (userid != null && userid != "")
			sysOperLog.setUserid(userid);
		try {
			Page page = this.findOperLogInfoPage(sysOperLog);
			request.setAttribute("currPage", page);

			List list = page.getQueryResult();

			ActionContext.getContext().put("loglist", list);

			String from = request.getParameter("from");
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					l.add((Object) list.get(i));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysOperLog");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);

				return "";
			} else
				return "list";
		} catch (Exception e) {
			SysLog.error(request, "error in (SysOperLogAction.java-list())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}

	public Page findOperLogInfoPage(SysOperLog sysOperLog) throws Exception {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		Page page = null;

		SysOperLog operLog = sysOperLog;

		operLog.setOrgfloor(loginUser.getOrgfloor());

		int pageNum = 1;
		int pageSize = 20;
		if (request.getParameter("pageNum") != null)
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		if (request.getParameter("pageSize") != null)
			pageSize = Integer.parseInt(request.getParameter("pageSize"));

		page = this.operLogService.getPage(request, operLog, pageNum, pageSize);

		return page;
	}

	/* 删除操作日志信息 */
	@SuppressWarnings("unused")
	private String delete(HttpServletRequest request, SysOperLog sysOperLog) {
		try {
			String[] chkDelete = StringUtils.split(request
					.getParameter("idList"), ",");
			operLogService.deleteInfo(chkDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "delete";
	}

	public OperLogService getOperLogService() {
		return operLogService;
	}

	public void setOperLogService(OperLogService operLogService) {
		this.operLogService = operLogService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
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

	public Date getLogtime() {
		return logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getOperid() {
		return operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}
}
