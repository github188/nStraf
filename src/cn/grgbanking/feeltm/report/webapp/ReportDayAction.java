package cn.grgbanking.feeltm.report.webapp;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import cn.grgbanking.feeltm.domain.testsys.WorkReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.feeltm.report.domain.ReportDay;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.feeltm.report.domain.ReportInfo;
import cn.grgbanking.feeltm.report.domain.WorkSummary;
import cn.grgbanking.feeltm.report.service.ReportService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

public class ReportDayAction extends BaseAction{
	private ReportService reportService;
	private List<WorkReport> reports;
	private List<WorkReport> dayReports;
	private String username;
	private String start;
	private String end;
	private String groupName1;
	private String prjName1;
	
	private Date createDate;
	private Date createDateHidden;
	private Double subsum;
	private int pageSize;
	
	private List<DayReportStatic> dayStatic;
	//private WorkSummary summ;
	public String add(){
		return "add";
	}
	
	public String save() throws Exception {
		try {
			boolean flag = false;
			Date date = null;
			Date date2 = null;
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    	String str5 = "";
	    	String str4 = "";
	    	
			str5 = inputFormat.format(new Date());  //实例化日期类型
			str4 = inputFormat.format(createDate);
			date2 = inputFormat.parse(str5);
			date = inputFormat.parse(str4);
			long startT=date.getTime(); //定义上机时间  
			long endT=date2.getTime();  //定义下机时间

			 
			long ss=(endT-startT)/(1000); //共计秒数  
			int MM =(int)ss/60;   //共计分钟数  
			int hh=(int)ss/3600;  //共计小时数  
			int dd=(int)hh/24;   //共计天数 
			if(dd > 2)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("新增失败： 不能新增2天以前的日报"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			List<WorkReport> rs=getReports(request);
			flag=reportService.add(rs);
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
		String loginName=userModel.getUsername();
		String dateString=request.getParameter("dateString");
		String[] tmps=dateString.split("@");
		String dat=tmps[0];
		String username=tmps[1];
		if(!loginName.equals(username)){
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			createDate=fo.parse(dat);
			if(reportService.remove(username, createDate)){
				MsgBox msgBox = new MsgBox(request, getText("该天的记录删除成功"), "工作报告删除信息页面");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		return "msgBox";
		
	}
		
	//跳转到修改页面
	public String edit(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		String dateString=request.getParameter("dateString");
		String[] tmps=dateString.split("@");
		String dat=tmps[0];
		String username=tmps[1];
		if(!loginName.equals(username)){
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		try {
			createDate=fo.parse(dat);
			dayReports=reportService.getReportsByDay(username, createDate);
			subsum=reportService.getSum(createDate, username);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "edit";
	}
	
	//跳转到显示详情页面
	public String show(){
		String dateString=request.getParameter("dateString");
		String[] tmps=dateString.split("@");
		String dat=tmps[0];
		String username=tmps[1];
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		try {
			createDate=fo.parse(dat);
			dayReports=reportService.getReportsByDay(username, createDate);
			subsum=reportService.getSum(createDate, username);
//			ActionContext.getContext().put("dayReports", rs);
//			ActionContext.getContext().put("subsum", subsum);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		List<WorkReport> rs=reportService.getReportsByDay(username, dateString);
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
	
	public void refresh1() {
		try {
			querySummary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		try {
			String from = request.getParameter("from");
			ReportDayInfo info=new ReportDayInfo();
			info.setUsername(userModel.getUsername());
//			if(username!=null&&(!"".equals(username.trim()))){
//				info.setUsername(username);
//			}
			if(username!=null){
				if(username.equals("全选")){
					username="";
				}
				info.setUsername(username.trim());
			}
			if(start!=null&&(!"".equals(start.trim()))){
				info.setStart(start.split(" ")[0]);
			}
			if(end!=null&&(!"".equals(end.trim()))){
				info.setEnd(end.split(" ")[0]);
			}
			if(groupName1!=null&&(!"".equals(groupName1.trim()))){
				if(groupName1.equals("全选")){
					groupName1="";
				}
				info.setGroupName(groupName1);
			}
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
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					ReportDay report=(ReportDay)list.get(i);
					report.setDateString(fo.format(report.getCreateDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.report.domain.ReportDay");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("reportDayList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String querySummary() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			ReportInfo info=new ReportInfo();
			info.setGroupName(userModel.getGroupName());
			//info.setUsername(userModel.getUsername());
			if(username!=null){
				if(username.equals("全选")){
					username="";
				}
				info.setUsername(username.trim());
			}
			if(start!=null&&(!"".equals(start.trim()))){
				info.setStart(start.split(" ")[0]);
			}
			if(end!=null&&(!"".equals(end.trim()))){
				info.setEnd(end.split(" ")[0]);
			}
			if(groupName1!=null&&(!"".equals(groupName1.trim()))){
				if(groupName1.equals("全选")){
					groupName1="";
				}
			}
			info.setGroupName(groupName1);
			if(prjName1==null||"0".equals(prjName1)){
				prjName1="";
			}
			info.setPrjName(prjName1);
			List list = reportService.getSummaryPage(info);
			WorkSummary sum=summary(list,prjName1);
//			sum.setPersonNum(null);
			list.add(sum);
			Page page=new Page();
			page.setQueryResult(list);
			page.setPageCount(1);
			page.setCurrentPageNo(1);
			page.setPageSize(50);	
			page.setRecordCount(list.size());
			request.setAttribute("currPage", page);
			if (from != null && from.equals("refresh")) {
//				for (int i = 0; i < list.size(); i++) {
//					WorkSummary report=(WorkSummary)list.get(i);
//				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.report.domain.WorkSummary");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("summaryList", list);
				return "querySummary";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "querySummary";
	}
	
	private WorkSummary summary(List<WorkSummary> summs,String prjName1) {
		WorkSummary mm=new WorkSummary();
		mm.setPrjName("汇总");
		double managerment = 0;
		double requirement = 0;
		double design = 0;
		double code = 0;
		double test = 0;
		double other = 0;
		double project = 0;
		double subtotal = 0;
		long personNum=0;
		if("".equals(prjName1)){
			for(WorkSummary m:summs){
				personNum+=m.getPersonNum();
				if(!m.getPrjName().equals("请假放假")){
					managerment+=m.getManagerment();
					requirement+=m.getRequirement();
					design+=m.getDesign();
					code+=m.getCode();
					test+=m.getTest();
					other+=m.getOther();
					project+=m.getProject();
					subtotal+=m.getSubtotal();
				}
			}
		}else{
			for(WorkSummary m:summs){
					personNum+=m.getPersonNum();
					managerment+=m.getManagerment();
					requirement+=m.getRequirement();
					design+=m.getDesign();
					code+=m.getCode();
					test+=m.getTest();
					other+=m.getOther();
					project+=m.getProject();
					subtotal+=m.getSubtotal();
				}
		}
		mm.setPersonNum(personNum);
		mm.setManagerment(managerment);
		mm.setRequirement(requirement);
		mm.setDesign(design);
		mm.setCode(code);
		mm.setTest(test);
		mm.setOther(other);
		mm.setProject(project);
		mm.setSubtotal(get1dec(subtotal));
		return mm;
	}

	public String update() throws Exception {
		try {
			
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			Date date = null;
			Date date2 = null;
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    	String str5 = "";
	    	String str4 = "";
	    	
			str5 = inputFormat.format(new Date());  //实例化日期类型
			str4 = inputFormat.format(createDate);
			date2 = inputFormat.parse(str5);
			date = inputFormat.parse(str4);
			long startT=date.getTime(); //定义上机时间  
			long endT=date2.getTime();  //定义下机时间

			 
			long ss=(endT-startT)/(1000); //共计秒数  
			int MM =(int)ss/60;   //共计分钟数  
			int hh=(int)ss/3600;  //共计小时数  
			int dd=(int)hh/24;   //共计天数 
			if(dd > 2)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("修改失败： 不能修改2天以前的日报"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			List<WorkReport> rs=getReports(request);
			boolean flag=false;
			if(rs!=null&&rs.size()!=0){
				flag=reportService.updateAll(userModel.getUsername(), createDate,createDateHidden, rs);
			}
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
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupName1");
		PrintWriter out=response.getWriter();
		List<Object> list=reportService.getNames(groupName);
	//	JSONUtil jsonUtil=new JSONUtil("java.lang.String");
		//JSONArray jsonArray=jsonUtil.toJSON(list, null);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 当前月份的未写周报人的统计
	 * @return
	 */
	public String showCurrentMonthStatic(){
		Date d=new Date();
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM");
		dayStatic=reportService.getReportStatics(fo.format(d));
		return "current_month_static";
	}
	
	/**
	 * 指定日期的未写周报的人统计
	 * @return
	 */
	public String showStatic(){
		dayStatic=reportService.getReportStatics(start,end);
		return "static";
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
		Date date=new Date();
		for(int i=0;i<size;i++){
			WorkReport r=new WorkReport();
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      //更新时间
			r.setModifyDate(f.format(date));//
			r.setUsername(userModel.getUsername());
			r.setStartDate(createDate);
			r.setGroupName(userModel.getGroupName());
			if(prjName!=null){
				r.setPrjName(prjName[i]);
			}
			r.setTaskDesc(taskDesc[i]);
			r.setAttachment(attachment[i]);
			r.setFinishRate(finishRate[i]);
			r.setStatus(status[i]);
			r.setTaskReason(taskReason[i]);
			r.setManagerment(Double.parseDouble(managerment[i]));
			r.setRequirement(Double.parseDouble(requirement[i]));
			r.setDesign(Double.parseDouble(design[i]));
			r.setCode(Double.parseDouble(code[i]));
			r.setTest(Double.parseDouble(test[i]));
			r.setOther(Double.parseDouble(other[i]));
			r.setProject(Double.parseDouble(project[i]));
			r.setSubtotal(Double.parseDouble(subtotal[i]));
			reports.add(r);
		}
		return reports;
	}
	
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<WorkReport> getReports() {
		return reports;
	}

	public void setReports(List<WorkReport> reports) {
		this.reports = reports;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
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
	

	public Double getSubsum() {
		return subsum;
	}

	public void setSubsum(Double subsum) {
		this.subsum = subsum;
	}

	public List<WorkReport> getDayReports() {
		return dayReports;
	}

	public void setDayReports(List<WorkReport> dayReports) {
		this.dayReports = dayReports;
	}

	public String getGroupName1() {
		return groupName1;
	}

	public void setGroupName1(String groupName1) {
		this.groupName1 = groupName1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPrjName1() {
		return prjName1;
	}

	public void setPrjName1(String prjName1) {
		this.prjName1 = prjName1;
	}

	public Date getCreateDateHidden() {
		return createDateHidden;
	}

	public void setCreateDateHidden(Date createDateHidden) {
		this.createDateHidden = createDateHidden;
	}

	private double get1dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 1,
				BigDecimal.ROUND_HALF_UP);
		return re.doubleValue();
	}

	public List<DayReportStatic> getDayStatic() {
		return dayStatic;
	}

	public void setDayStatic(List<DayReportStatic> dayStatic) {
		this.dayStatic = dayStatic;
	}
	
	
	
}
