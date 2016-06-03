package cn.grgbanking.feeltm.report.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.WorkReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.report.domain.ReportInfo;
import cn.grgbanking.feeltm.report.service.ReportService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ReportInfoAction extends BaseAction{
	private ReportService reportService;
	
	private WorkReport report;
	private String username;  //对应用户表中username
	private String start;
	private String end;
	private String prjName;
	private Date createDate;
	private Integer subsum;
	public String add(){
		return "add";
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			List<WorkReport> reports=getReports(request);
			flag=reportService.add(reports);
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
					WorkReport temp = reportService.getReportById(sids[i]);
					reportService.delete(temp);
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
		try {
			String ids = request.getParameter("ids");
			report=reportService.getReportById(ids);
			subsum=reportService.getSum(report.getStartDate(),report.getUsername()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
//			SpecialRegulation specialRegulation = new SpecialRegulation();
//			specialRegulation = specialRegulationService
//					.getSpecialRegulationObject(getId());
//			//specialRegulation.setId(getId());
//			specialRegulation.setApplyId(getApplyId());
//			specialRegulation.setOrgid(getOrgid());
//			specialRegulation.setMoneyType(getMoneyType());
//			specialRegulation.setMoneyDenomination(getMoneyDenomination());
//			specialRegulation.setRegulation(getRegulation());
//			specialRegulation.setSource(getSource());
//			specialRegulation.setResult(getResult());
//			specialRegulation.setCreateDate(DateUtil.getTimeYYYYMMDDHHMMSSString(new Date()));
//			specialRegulation.setCreateName(getCreateName());
//			//specialRegulation.setSpecialType(getSpecialType());
//			boolean flag = specialRegulationService
//					.updateSpecialRegulation(specialRegulation);
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			report.setUsername(userModel.getUsername());
			boolean flag=reportService.update(report);
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
		System.out.println("username:"+username);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		try {
			String from = request.getParameter("from");
			ReportInfo info=new ReportInfo();
			info.setUsername(userModel.getUsername());
//			if(start==null){
//				info.setStart(fo.format(new Date()));
//			}
			if(username!=null&&(!"".equals(username.trim()))){
				info.setUsername(username);
			}
			if(start!=null&&(!"".equals(start.trim()))){
				info.setStart(start.split(" ")[0]);
			}
			if(end!=null&&(!"".equals(end.trim()))){
				info.setEnd(end.split(" ")[0]);
			}
			if(prjName!=null&&(!"".equals(prjName.trim()))){
				info.setPrjName(prjName);
			}
			
			/**
			 * if (str_result != null && !str_result.equals("")) {
				trans.setTransResult(str_result);
			}
			String accountNo = request.getParameter("accountNo");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");

			String journalNo = request.getParameter("journalNo");

			// Object[] obj=new Object[1];

			trans.setAccountNo(accountNo);
			trans.setJournalNo(journalNo);

			 */
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = reportService.getPage(info, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			
			System.out.println("list.size()==="+list.size());
			
			
			if (from != null && from.equals("refresh")) {
//				List<Object> l = new ArrayList<Object>();
//				Map dataDirMap = BusnDataDir.getMap("reportMgr.username");
				for (int i = 0; i < list.size(); i++) {
					WorkReport report=(WorkReport)list.get(i);
					report.setDateString(fo.format(report.getStartDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.WorkReport");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("workReportList", list);
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
			report=reportService.getReportById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public WorkReport getReport() {
		return report;
	}

	public void setReport(WorkReport report) {
		this.report = report;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public void setEnd(String end) {
		this.end = end;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

	private List<WorkReport> getReports(HttpServletRequest request){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		List<WorkReport> reports=new ArrayList<WorkReport>();
		String[] prjName=request.getParameterValues("prjName");
		String[] taskDesc=request.getParameterValues("taskDesc");
		String[] attachment=request.getParameterValues("attachment");
		String[] finishRate=request.getParameterValues("finishRate");
		String[] status=request.getParameterValues("status");
		String[] taskReason=request.getParameterValues("taskReason");
		String[] managerment=request.getParameterValues("managerment");
		String[] requirement=request.getParameterValues("requirement");
		String[] design=request.getParameterValues("design");
		String[] code=request.getParameterValues("code");
		String[] test=request.getParameterValues("test");
		String[] other=request.getParameterValues("other");
		String[] project=request.getParameterValues("project");
		String[] subtotal=request.getParameterValues("subtotal");
		int size=taskDesc.length;
		for(int i=0;i<size;i++){
			WorkReport r=new WorkReport();
			r.setUsername(userModel.getUsername());
			r.setStartDate(createDate);
			r.setPrjName(prjName[i]);
			r.setTaskDesc(taskDesc[i]);
			r.setAttachment(attachment[i]);
			r.setFinishRate(finishRate[i]);
			r.setStatus(status[i]);
			r.setTaskReason(taskReason[i]);
			r.setManagerment(Integer.parseInt(managerment[i]));
			r.setRequirement(Integer.parseInt(requirement[i]));
			r.setDesign(Integer.parseInt(design[i]));
			r.setCode(Integer.parseInt(code[i]));
			r.setTest(Integer.parseInt(test[i]));
			r.setOther(Integer.parseInt(other[i]));
			r.setProject(Integer.parseInt(project[i]));
			r.setSubtotal(Integer.parseInt(subtotal[i]));
			reports.add(r);
		}
		return reports;
	}
	public Integer getSubsum() {
		return subsum;
	}
	public void setSubsum(Integer subsum) {
		this.subsum = subsum;
	}
	
	
	
	
}	
