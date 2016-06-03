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

import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.report.domain.WeekRecord;
import cn.grgbanking.feeltm.report.service.WeekReportService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Collections;

public class WeekReportAction extends BaseAction{
	private WeekReportService weekReportService;
	private TesterDetailDao testerDao;
	
	private WeekReport weekReport;
	private String start;
	private String end;
	private String groupName;
	private List<WeekRecord> records;
	
	//private Map<String,String> responsible;
	
	public String add(){
		String redirectPage="add";
		//responsible=testerDao.getNameList();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		groupName=userModel.getGroupName();
		if(userModel.getLevel()==1){
			redirectPage="add_audit";
		}
		
		return redirectPage;
	}
	public String save() throws Exception {
		String monthDay=request.getParameter("monthDay");
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			weekReport.setResponsor(userModel.getUsername());
			weekReport.setCurrentName(userModel.getUsername());
			weekReport.setGroupName(userModel.getGroupName());
			weekReport.setTaskOverview("weekReport");
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			weekReport.setModifyDate(f.format(new Date()));
			System.out.println("modiryDate:   "+weekReport.getModifyDate());
			 f=new SimpleDateFormat("yyyy-MM");
			weekReport.setMonthDay(f.parse(monthDay));
			weekReport.setFinishInfo(2);
			String taskDesc=getTaskDescDetail(request);
			weekReport.setTaskDesc(taskDesc);
			flag=weekReportService.add(weekReport);
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

	private String getTaskDescDetail(HttpServletRequest request) {
		StringBuffer sb=new StringBuffer();
		String[] prjName=request.getParameterValues("prjName");
		String[] taskDesc=request.getParameterValues("taskDesc");
		String[] prjType=request.getParameterValues("prjType");
		String[] responsible=request.getParameterValues("responsible");
		
		String[] finishRate=request.getParameterValues("finishRate");
		String[] delayReason=request.getParameterValues("delayReason");
		String[] file=request.getParameterValues("file");
		String[] audit=request.getParameterValues("audit");
		int size=taskDesc.length;
		
		for(int i=0;i<size;i++){
			sb.append(prjName[i]).append("$$");
			sb.append(taskDesc[i]).append("$$");
			sb.append(prjType[i]).append("$$");	
			sb.append(responsible[i]).append("$$");
			
			if(finishRate==null){
				sb.append("0%").append("$$");
			}else{
				sb.append(finishRate[i]).append("$$");
			}
			if(delayReason==null){
				sb.append("").append("$$");
			}else{
				sb.append(delayReason[i]).append("$$");
			}
			if(file==null){
				sb.append("1").append("$$");
			}else{
				sb.append(file[i]).append("$$");
			}
			if(audit==null){
			sb.append("0").append("@@@");
			}else{
				sb.append(audit[i]).append("@@@");
			}
		}
		
		return sb.toString();
	}
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		boolean flag=true;
		try {
			String ids = request.getParameter("ids");
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					WeekReport temp = weekReportService.getWeekReportById(sids[i]);
					if(!temp.getResponsor().equals(userModel.getUsername())){
						MsgBox msgBox = new MsgBox(request,
								getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					weekReportService.delete(temp);
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
		String redirect="edit";
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		
		if(userModel.getLevel()==1){
			redirect="edit_audit";
		}
		// 获取当前用户名(ckai 20110318)
		String currentUser = userModel.getUsername();
		// ckai 20110318
		try {
			String ids = request.getParameter("ids");
			weekReport=weekReportService.getWeekReportById(ids);		
		/*
			if(!weekReport.getGroupName().equals(userModel.getGroupName())){
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"您不能操作其他组的数据"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		*/
			if("1".equals(weekReport.getLockFlag())){
				// 获取数据库中当前正在做修改记录的人名(ckai 20110318)
				String lockedUser = weekReport.getCurrentName();
				// ckai 20110318
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"当前数据有 " + lockedUser + " 正在操作，请稍候"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		//	responsible=testerDao.getNameList();
			String taskDesc=weekReport.getTaskDesc();
			if(!taskDesc.contains("$$")){
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"历史数据只能查看，不能进行修改"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			weekReport.setLockFlag("1");
			// 设置数据库中current_name值为当前用户名(ckai 20110318)
			weekReport.setCurrentName(currentUser);
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			weekReport.setHandle_time(f.format(new Date()));
			//对weekReport中的taskDesc进行解析放入records中
			parseToRecords(taskDesc);
			
			// ckai 20110318
			weekReportService.update(weekReport);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return redirect;
	}
	
	
	private void parseToRecords(String taskDesc) {
		records=new ArrayList<WeekRecord>();
		String[] record=taskDesc.split("@@@");
		for(int i=0;i<record.length;i++){
			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			String prjNameTmp=recordField[0];   //unuse
			String taskDescTmp=recordField[1];
			String prjTypeTmp=recordField[2];
			String responsibleTmp=recordField[3];
			String finishRateTmp=recordField[4];
			String delayReasonTmp=recordField[5];
			String fileTmp=recordField[6];
			String auditTmp=recordField[7];
			
			WeekRecord recordTmp=new WeekRecord();
			recordTmp.setPrjName(prjNameTmp);
			recordTmp.setTaskDesc(taskDescTmp);
			recordTmp.setPrjType(prjTypeTmp);
			recordTmp.setResponsible(responsibleTmp);
			recordTmp.setFinishRate(finishRateTmp);
			recordTmp.setDelayReason(delayReasonTmp);
			recordTmp.setFile(fileTmp);
			recordTmp.setAudit(auditTmp);
			records.add(recordTmp);
		}
	}
	
	public String resetLock() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String ids = request.getParameter("ids");
		weekReport=weekReportService.getWeekReportById(ids);
		if(weekReport.getLockFlag().equals("0"))
		{
			MsgBox msgBox1 = new MsgBox(request,getText("解锁失败： 本周报记录没有被锁定"));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		Date date = null;
		Date date2 = null;
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		date = inputFormat.parse(weekReport.getHandle_time());
    	String str5 = "";
		str5 = inputFormat.format(new Date());  //实例化日期类型
		date2 = inputFormat.parse(str5);
		long startT=date.getTime(); //定义上机时间  
		long endT=date2.getTime();  //定义下机时间

		 
		long ss=(endT-startT)/(1000); //共计秒数  
		int MM =(int)ss/60;   //共计分钟数  
		int hh=(int)ss/3600;  //共计小时数  
		int dd=(int)hh/24;   //共计天数 
		if(!userModel.getUsername().equals(weekReport.getCurrentName()))
		{
			if(MM < 30){
				MsgBox msgBox1 = new MsgBox(request,getText("解锁失败： 本周报记录没有超过可编辑时间"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		weekReport.setLockFlag("0");	
		weekReportService.update(weekReport);
		MsgBox msgBox1 = new MsgBox(request,getText("解锁成功"));
		msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	public String close() throws Exception{
		String id=request.getParameter("idFlag");
		weekReport=weekReportService.getWeekReportById(id);
		weekReport.setLockFlag("0");
		// 设置数据库中current_name值为空(ckai 20110318)
		//weekReport.setCurrentName("");
		// ckai 20110318
		weekReportService.update(weekReport);
		return "";
	}

	public String update() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
//			weekReport.setResponsor(userModel.getUsername());
	//		weekReport.setGroupName(userModel.getGroupName());
			String monthDay=request.getParameter("monthDay");
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM");
			weekReport.setMonthDay(f.parse(monthDay));
			 f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			weekReport.setModifyDate(f.format(new Date()));
			weekReport.setHandle_time(f.format(new Date()));
			weekReport.setLockFlag("0");
			// 设置数据库中current_name值为空(ckai 20110318)
			weekReport.setCurrentName(userModel.getUsername());
			String taskDesc=getTaskDescDetail(request);
			weekReport.setTaskDesc(taskDesc);
			// ckai 20110318
			boolean flag=weekReportService.update(weekReport);
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
		if(groupName==null){
			groupName=userModel.getGroupName();
		}else if(groupName.equals("全选")){
			groupName="";
		}
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM");
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
			Page page = weekReportService.getPage(start,end,groupName,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			System.out.println("list.size()==="+list.size());
			for (int i = 0; i < list.size(); i++) {
				WeekReport report=(WeekReport)list.get(i);
				report.setFinishInfoString(getFinishInfo(report.getFinishInfo()));
				report.setMonthDayString(f.format(report.getMonthDay()));
			}
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					WeekReport report=(WeekReport)list.get(i);
					report.setStartString(fo.format(report.getStartDate()));
					report.setEndString(fo.format(report.getEndDate()));
					report.setFinishInfoString(getFinishInfo(report.getFinishInfo()));
					report.setMonthDayString(f.format(report.getMonthDay()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.WeekReport");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("weekList", list);
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
			weekReport=weekReportService.getWeekReportById(ids);
			String taskDesc=weekReport.getTaskDesc();
			if(taskDesc.contains("$$")){  //改造后的数据
				String taskDescDetail=parseTaskDesc(taskDesc);
				weekReport.setTaskDesc(taskDescDetail);
			}	
				//获得下周的计划
				WeekReport next=weekReportService.getNextWeekPlan(weekReport.getGroupName(), weekReport.getEndDate());
				if(next!=null){
					weekReport.setNextStartDate(next.getStartDate());
					weekReport.setNextEndDate(next.getEndDate());
					if(next.getTaskDesc().contains("$$")){
						String res=next.getTaskDesc();
						String detail=parseTaskDesc(res);
						weekReport.setNote(detail);
					}else{
						weekReport.setNote(next.getTaskDesc());
					}
				}
			//weekReport.setFinishInfoString(getFinishInfo(weekReport.getFinishInfo()));
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	
	/**
	 *    11$$22$$333$$44@@@@222$$33
	 * @param taskDesc
	 * sb.append(prjName[i]).append("$$");
			sb.append(taskDesc[i]).append("$$");
			sb.append(responsible[i]).append("$$");
			sb.append(finishRate[i]).append("$$");
			sb.append(file[i]).append("$$");
			sb.append(audit[i]).append("$$");
			sb.append(managerment[i]).append("@@@");
			prjName	$$taskDesc$$prjType$$responsible$$ finishRate$$ file$$ audi@@@ 
	 * @return
	 */
	private String parseTaskDesc(String taskDesc) {
		StringBuffer sb=new StringBuffer();
		String[] record=taskDesc.split("@@@");
		
		//add
		List<WeekRecord> rs=new ArrayList<WeekRecord>();
		
		for(int i=0;i<record.length;i++){
			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			
			String prjNameTmp=recordField[0];   //unuse
			String taskDescTmp=recordField[1];
			String prjTypeTmp=recordField[2].equals("0")?"计划":"新增";
			String responsibleTmp=recordField[3];
			String finishRateTmp=recordField[4];
			String delayReasonTmp=recordField[5];
			String fileTmp=recordField[6];
			if(fileTmp.equals("0")){
				fileTmp="无需归档";
			}else if(fileTmp.equals("1")){
				fileTmp="未归档";
			}else{
				fileTmp="已归档";
			}
			String auditTmp=recordField[7].equals("0")?"未审核":"已审核";
			//ADD
			WeekRecord r=new WeekRecord();
			r.setPrjName(prjNameTmp);
			r.setTaskDesc(taskDescTmp);
			r.setPrjType(prjTypeTmp);
			r.setResponsible(responsibleTmp);
			r.setFinishRate(finishRateTmp);
			r.setDelayReason(delayReasonTmp);
			r.setFile(fileTmp);
			r.setAudit(auditTmp);
			rs.add(r);
		}
			Collections.sort(rs);  //ADD
			
			//1.我完成了很多工作（熊磊，计划，30%，未归档，未审核）
			for(int i=0;i<rs.size();i++){
				WeekRecord wr=rs.get(i);  //add
				sb.append(i+1).append(".");  //序号
				//【】
				sb.append("【").append(wr.getPrjType()).append("】");
				sb.append("<strong>").append(wr.getPrjName()).append("</strong>").append("：");
				sb.append(wr.getTaskDesc()).append("；（");
				
				if(wr.getFinishRate().equals("0%")&&wr.getDelayReason().trim().equals("")&&wr.getFile().equals("未归档")&&wr.getAudit().equals("未审核")){
					sb.append(wr.getResponsible()).append("）");
				}else{
					sb.append(wr.getResponsible()).append("，");
					if(wr.getFinishRate().equals("100%")){
						sb.append("完成").append(wr.getFinishRate()).append("，");
						if(wr.getDelayReason()!=null&&!wr.getDelayReason().trim().equals(""))
						sb.append(wr.getDelayReason()).append("；");;
					//	sb.append("；");
					}else{
						sb.append("<font color='red'>");
						sb.append("完成").append(wr.getFinishRate());
						sb.append("</font>");
						sb.append("，");
						sb.append(wr.getDelayReason()).append("；");
					}
					if(wr.getFile().equals("无需归档")){
						sb.append("<font color='green'>").append(wr.getFile()).append("</font>");
					}else if(wr.getFile().equals("未归档")){
						sb.append("<font color='red'>").append(wr.getFile()).append("</font>");
					}else{
						sb.append(wr.getFile());
					}
					sb.append("）");
					sb.append("--");
					if(wr.getAudit().equals("未审核")){
						sb.append("<font color='red'>").append(wr.getAudit()).append("</font>");
					}else{
						sb.append(wr.getAudit());
					}
				}
				//sb.append(" ）");	
				//modify未完成原因
				//sb.append("<br/>");
				sb.append("<br/>");
				sb.append("&#13;&#10;");
				sb.append("\r");
			}
		return sb.toString();
	}
	/**
	 *   <option value="0">很差</option>
                                 <option value="1">较差</option>
                                 <option value="2" selected="true">正常</option>
                                 <option value="3">良好</option>
                                 <option value="4">优秀</option>
	 */
	private String getFinishInfo(int flag){
		String finishInfo=null;
		switch(flag){
			case 0:finishInfo="很差";break;
			case 1:finishInfo="较差";break;
			case 2:finishInfo="正常";break;
			case 3:finishInfo="良好";break;
			case 4:finishInfo="优秀";break;
		}
		return finishInfo;
	}
	
	
	
	public WeekReport getWeekReport() {
		return weekReport;
	}

	public void setWeekReport(WeekReport weekReport) {
		this.weekReport = weekReport;
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

	public WeekReportService getWeekReportService() {
		return weekReportService;
	}

	public void setWeekReportService(WeekReportService weekReportService) {
		this.weekReportService = weekReportService;
	}
	public List<WeekRecord> getRecords() {
		return records;
	}
	public void setRecords(List<WeekRecord> records) {
		this.records = records;
	}
	public TesterDetailDao getTesterDao() {
		return testerDao;
	}
	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
	}
//	public Map<String, String> getResponsible() {
//		return responsible;
//	}
//	public void setResponsible(Map<String, String> responsible) {
//		this.responsible = responsible;
//	}
	
	
}
