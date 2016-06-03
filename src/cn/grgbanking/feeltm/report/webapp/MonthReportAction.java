package cn.grgbanking.feeltm.report.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.MonthReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.report.service.MonthReportService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class MonthReportAction extends BaseAction {
	private MonthReportService monthReportService;

	private MonthReport monthReport;
	private String start;
	private String end;
	private String groupName;
	
	

	public String add() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		groupName = userModel.getGroupName();
		return "add";
	}

	public String save() throws Exception {
		String startDate = request.getParameter("startDate");
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			monthReport.setResponsor(userModel.getUsername());
			monthReport.setGroupName(userModel.getGroupName());
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			monthReport.setModifyDate(f.format(new Date()));
			 f = new SimpleDateFormat("yyyy-MM");
			monthReport.setStartDate(f.parse(startDate));
			flag = monthReportService.add(monthReport);
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
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}

	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		boolean flag = true;
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: " + ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if ("all".equals(sids[0])) {
				String[] arr = new String[sids.length - 1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					MonthReport temp = monthReportService
							.getMonthReportById(sids[i]);
					if (!temp.getResponsor().equals(userModel.getUsername())) {
						MsgBox msgBox = new MsgBox(request, getText(
								"operInfoform.updatefaile",
								new String[] { "您不能操作其他用户的数据" }));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					monthReportService.delete(temp);
					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, temp
							.getTaskDesc());
					SysLog.info("User:" + userModel.getUserid()
							+ " to delete a SpecialRegulation: "
							+ temp.getTaskDesc());

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
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// 获取当前用户名(ckai 20110318)
		String currentUser = userModel.getUsername();
		
		// ckai 20110318
		try {
			String ids = request.getParameter("ids");
			monthReport = monthReportService.getMonthReportById(ids);
			if(!monthReport.getGroupName().equals(userModel.getGroupName())){
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"您不能操作其他组的数据"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(monthReport.getLockFlag().equals("1")){
				// 获取数据库中当前正在做修改记录的人名(ckai 20110318)
				String lockedUser = monthReport.getCurrentName();
				// ckai 20110318
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"当前数据有 " + lockedUser + " 正在操作，请稍候"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			monthReport.setLockFlag("1");
			// 设置数据库中current_name值为当前用户名(ckai 20110318)
			monthReport.setCurrentName(currentUser);
			monthReportService.update(monthReport);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}
	
	public String close() throws Exception{
		String id=request.getParameter("idFlag");
		monthReport=monthReportService.getMonthReportById(id);
		monthReport.setLockFlag("0");
		// 设置数据库中current_name值为空(ckai 20110318)
		monthReport.setCurrentName("");
		// ckai 20110318
		monthReportService.update(monthReport);
		return "";
	}

	public String update() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			//monthReport.setResponsor(userModel.getUsername());
			//monthReport.setGroupName(userModel.getGroupName());
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			monthReport.setModifyDate(f.format(new Date()));
			String monthDay = request.getParameter("startDate");
			f = new SimpleDateFormat("yyyy-MM");
			monthReport.setStartDate(f.parse(monthDay));
			monthReport.setLockFlag("0");
			// 设置数据库中current_name值为空(ckai 20110318)
			monthReport.setCurrentName("");
			boolean flag = monthReportService.update(monthReport);
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

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if (groupName == null) {
			groupName = userModel.getGroupName();
		} else if (groupName.equals("全选")) {
			groupName = "";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		try {
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = monthReportService.getPage(start, end, groupName,
					pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>) page.getQueryResult();
			System.out.println("list.size()===" + list.size());
			for (int i = 0; i < list.size(); i++) {
				MonthReport report = (MonthReport) list.get(i);
				report.setFinishInfoString(getFinishInfo(report
								.getFinishInfo()));
				report.setStartDateString(f.format(report.getStartDate()));
			}
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					MonthReport report = (MonthReport) list.get(i);
					report.setFinishInfoString(getFinishInfo(report
							.getFinishInfo()));
					report.setStartDateString(f.format(report.getStartDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.MonthReport");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("monthList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			monthReport = monthReportService.getMonthReportById(ids);
			monthReport.setFinishInfoString(getFinishInfo(monthReport
					.getFinishInfo()));
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	/**
	 * <option value="0">很差</option> <option value="1">较差</option> <option
	 * value="2" selected="true">正常</option> <option value="3">良好</option>
	 * <option value="4">优秀</option>
	 */
	private String getFinishInfo(int flag) {
		String finishInfo = null;
		switch (flag) {
		case 0:
			finishInfo = "很差";
			break;
		case 1:
			finishInfo = "较差";
			break;
		case 2:
			finishInfo = "正常";
			break;
		case 3:
			finishInfo = "良好";
			break;
		case 4:
			finishInfo = "优秀";
			break;
		}
		return finishInfo;
	}
	
	
	public String checkExist(){
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		UserModel userModel = (UserModel) request.getSession()
		.getAttribute(Constants.LOGIN_USER_KEY);
		String groupName1=userModel.getGroupName();
		String startDateString=request.getParameter("startDate");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		try {
			PrintWriter out=response.getWriter();
			boolean flag=true;
			Date startDate=f.parse(startDateString);
			if(monthReportService.isExist(groupName1, startDate)){
				flag=false;
			}
			JSONObject obj=new JSONObject();
			obj.put("flag", flag);
			out.print(obj);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public MonthReport getMonthReport() {
		return monthReport;
	}

	public void setMonthReport(MonthReport monthReport) {
		this.monthReport = monthReport;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public MonthReportService getMonthReportService() {
		return monthReportService;
	}

	public void setMonthReportService(MonthReportService monthReportService) {
		this.monthReportService = monthReportService;
	}

	

}
