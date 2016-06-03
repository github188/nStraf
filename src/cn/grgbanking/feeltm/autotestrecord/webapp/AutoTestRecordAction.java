package cn.grgbanking.feeltm.autotestrecord.webapp;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.ModuleException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.faces.context.FacesContext; 
import javax.servlet.ServletContext; 
import javax.servlet.ServletOutputStream; 

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.servlet.ServletException;    
import javax.servlet.http.HttpServlet;    
import javax.servlet.http.HttpServletRequest;    

import jxl.Sheet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;


import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

import cn.grgbanking.feeltm.autotestrecord.service.AutoTestRecordService;
import cn.grgbanking.feeltm.domain.testsys.AutoTestRecord;
import cn.grgbanking.feeltm.domain.testsys.KpiPoint;
import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;


public class AutoTestRecordAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private String PrjName;
	private String VersionNo;
	private String Status;
	private Date ExecTime;
	private String TotalCase;
	private String PassCase;
	private String FailCase;
	private String AllTime;
	private String ExecMan;
	private String Note;
	private String StartMonth;
	private String EndMonth;
	
	private List<AutoTestRecord> AutoTestRecordList;

	private AutoTestRecord autoTestRecord;
	private AutoTestRecordService autoTestRecordService;
	
	public String add(){
		return "add";
	}
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			flag = autoTestRecordService.add(autoTestRecord);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("add.fail", new String[]{"添加记录失败"}));
				addActionMessage(getText("add.fail", new String[]{"添加记录失败"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("add.fail"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.fail", new String[]{"添加记录失败"}));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}

	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			autoTestRecord = autoTestRecordService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			// SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// behavior.setModifyDate(f.format(new Date()));
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = autoTestRecordService.update(autoTestRecord);
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
	
	public String delete() throws Exception {
		
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					AutoTestRecord temp = autoTestRecordService.getCaseById(sids[i]);
					autoTestRecordService.delete(temp);
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
	
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
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
			Page page = autoTestRecordService.getPage(StartMonth, EndMonth, PrjName, VersionNo, Status, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					AutoTestRecord autoTestRecord=(AutoTestRecord)list.get(i);
					//overtime.setDateString(fo.format(behavior.getStartDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.AutoTestRecord");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("autoTestRecordList", list);
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
			autoTestRecord=autoTestRecordService.getDetailById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public String getPrjName() {
		return PrjName;
	}
	public void setPrjName(String prjName) {
		PrjName = prjName;
	}
	public String getVersionNo() {
		return VersionNo;
	}
	public void setVersionNo(String versionNo) {
		VersionNo = versionNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Date getExecTime() {
		return ExecTime;
	}
	public void setExecTime(Date execTime) {
		ExecTime = execTime;
	}
	public String getTotalCase() {
		return TotalCase;
	}
	public void setTotalCase(String totalCase) {
		TotalCase = totalCase;
	}
	public String getPassCase() {
		return PassCase;
	}
	public void setPassCase(String passCase) {
		PassCase = passCase;
	}
	public String getFailCase() {
		return FailCase;
	}
	public void setFailCase(String failCase) {
		FailCase = failCase;
	}
	public String getAllTime() {
		return AllTime;
	}
	public void setAllTime(String allTime) {
		AllTime = allTime;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	
	public String getExecMan() {
		return ExecMan;
	}
	public void setExecMan(String execMan) {
		ExecMan = execMan;
	}
	public AutoTestRecord getAutoTestRecord() {
		return autoTestRecord;
	}
	public void setAutoTestRecord(AutoTestRecord autoTestRecord) {
		this.autoTestRecord = autoTestRecord;
	}
	public AutoTestRecordService getAutoTestRecordService() {
		return autoTestRecordService;
	}
	public void setAutoTestRecordService(AutoTestRecordService autoTestRecordService) {
		this.autoTestRecordService = autoTestRecordService;
	}
	public String getStartMonth() {
		return StartMonth;
	}
	public void setStartMonth(String startMonth) {
		StartMonth = startMonth;
	}
	public String getEndMonth() {
		return EndMonth;
	}
	public void setEndMonth(String endMonth) {
		EndMonth = endMonth;
	}
	
	
}
