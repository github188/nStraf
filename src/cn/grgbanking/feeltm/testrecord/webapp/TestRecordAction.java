package cn.grgbanking.feeltm.testrecord.webapp;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.testrecord.domain.TestQuestion;
import cn.grgbanking.feeltm.testrecord.service.TestRecordService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;


public class TestRecordAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	private TestQuestion question;
	private List<TestQuestion> questionList;
	
	private String TestRecordMan;
	private String absentPersons;
	private Map<String,String> umap=new LinkedHashMap<String,String>();
	private String upMonth;
	private String StartMonth;
	private String EndMonth;
	private String projectTypeQuery;
	private String buildType;
	private String prjName;
	private String testStatusQuery;
	private String sumbitProcessQuery;
	private String testMan;

	private String fileName;
	
	private String FindBugSumTotal;
	private String TesterSumTotal;
	private String TestTimeSumTotal;
	private String WorkLoadTotal;

	private List<String> unames;
	
	private TestRecord testRecord;
	
	private TestRecordService testRecordService;
	
	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		question=new TestQuestion();
		question.setFindDate(Calendar.getInstance().getTime());
		question.setSerialNo(testRecordService.getMaxSerialNo()+1+"");
    	
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
	
	public void setUrgeLevel(String urge,TestQuestion question){
		if("紧急".equals(urge)){
			question.setUrgeLevel(10);
			return;
		}
		if("高".equals(urge)){
			question.setUrgeLevel(11);
			return;
		}
		if("中".equals(urge)){
			question.setUrgeLevel(12);
			return;
		}
		if("低".equals(urge)){
			question.setUrgeLevel(13);
			return;
		}
		question.setUrgeLevel(101);
	}
	
	public String save() throws Exception {
		try {
			boolean flag = false;

			question.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
			setUrgeLevel(question.getUrge(), question);
			flag = testRecordService.add(question);
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

	public String edit() throws Exception {
		try {
			String ids = request.getParameter("ids");
			question =testRecordService.getCaseById(ids);
			request.setAttribute("question", question);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {

			Date date=new Date();
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String modifyDate=f.format(date);
			question.setUpdateDate(modifyDate);
			setUrgeLevel(question.getUrge(), question);
			boolean flag = testRecordService.update(question);
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
					TestQuestion temp = testRecordService.getCaseById(sids[i]);
					testRecordService.delete(temp);
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
		try {
			String from = request.getParameter("from");
			// ReportDayInfo info=new ReportDayInfo()

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			String findDate="";
			if(StringUtils.isNotBlank(request.getParameter("searchFindDate"))){
				findDate=request.getParameter("searchFindDate");
			}
			String findMan="";
			if(StringUtils.isNotBlank(request.getParameter("searchFindMan"))){
				findMan=request.getParameter("searchFindMan");
			}
			
			String soloveMan="";
			if(StringUtils.isNotBlank(request.getParameter("searchSoloveMan"))){
				soloveMan=request.getParameter("searchSoloveMan");
			}
			String buglevel = "";
			if(StringUtils.isNotBlank(request.getParameter("buglevel"))){
				buglevel=request.getParameter("buglevel");
			}
			
			String soloveDate="";
			if(StringUtils.isNotBlank(request.getParameter("searchSoloveDate"))){
				soloveDate=request.getParameter("searchSoloveDate");
			}
			
			String questionStaus="";
			if(StringUtils.isNotBlank(request.getParameter("searchStatus"))){
				questionStaus=request.getParameter("searchStatus");
			}
			
			String finishRate="";
			if(StringUtils.isNotBlank(request.getParameter("searchFinishRate"))){
				finishRate=request.getParameter("searchFinishRate");
			}
			
			String modualName="";
			if(StringUtils.isNotBlank(request.getParameter("searchModualName"))){
				modualName=request.getParameter("searchModualName");
			}
			
			String serialNo=""; 
			if(StringUtils.isNotBlank(request.getParameter("serialNo"))){
				serialNo=request.getParameter("serialNo");
			}
			
			String title="";
			if(StringUtils.isNotBlank(request.getParameter("searchTitle"))){
				title=request.getParameter("searchTitle");
			}
			
			String urge="";
			if(StringUtils.isNotBlank(request.getParameter("searchUrge"))){
				urge=request.getParameter("searchUrge");
			}
			
			String updateMan="";
			if(StringUtils.isNotBlank(request.getParameter("searchUpdateMan"))){
				updateMan=request.getParameter("searchUpdateMan");
			}
			
			String deployStatus="";
			if(StringUtils.isNotBlank(request.getParameter("searchDeployStatus"))){
				deployStatus=request.getParameter("searchDeployStatus");
			}
			
			Page page = testRecordService.getPage(buglevel,serialNo,findMan,findDate,soloveMan,soloveDate,questionStaus,finishRate,modualName, pageNum, pageSize,title,urge,updateMan,deployStatus);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>) page.getQueryResult();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					TestQuestion qus=(TestQuestion)list.get(i);
					String updateDate=qus.getUpdateDate();
					Date parseDate=null;
					try{
						parseDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updateDate);
					}catch(Exception e){
						parseDate=new SimpleDateFormat("yyyy-MM-dd").parse(updateDate);
					}
					String fomatdate=new SimpleDateFormat("MM-dd HH:mm").format(parseDate);
					qus.setUpdateDate(fomatdate);
				}
			}
			if (from != null && from.equals("refresh")) {
				 
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.testrecord.domain.TestQuestion");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("questionList", list);
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
			testRecord=testRecordService.getDetailById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public double formatDouble(String s){
		 BigDecimal b = new BigDecimal(s);
		 double f1= b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			//performance.setSubtotal_s(meg(sum_s));
		   // performance.setSubtotal_s(f1);
			//s=s;
			return f1;
		}
	
	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public TestRecord getTestRecord() {
		return testRecord;
	}
	public void setTestRecord(TestRecord testRecord) {
		this.testRecord = testRecord;
	}
	public TestRecordService getTestRecordService() {
		return testRecordService;
	}
	public void setTestRecordService(TestRecordService testRecordService) {
		this.testRecordService = testRecordService;
	}
	public String getTestRecordMan() {
		return TestRecordMan;
	}
	public void setTestRecordMan(String testRecordMan) {
		TestRecordMan = testRecordMan;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Map<String, String> getUmap() {
		return umap;
	}
	public void setUmap(Map<String, String> umap) {
		this.umap = umap;
	}
	public String getAbsentPersons() {
		return absentPersons;
	}
	public void setAbsentPersons(String absentPersons) {
		this.absentPersons = absentPersons;
	}
	public List<String> getUnames() {
		return unames;
	}
	public void setUnames(List<String> unames) {
		this.unames = unames;
	}
	public String getFindBugSumTotal() {
		return FindBugSumTotal;
	}
	public void setFindBugSumTotal(String findBugSumTotal) {
		FindBugSumTotal = findBugSumTotal;
	}
	public String getTesterSumTotal() {
		return TesterSumTotal;
	}
	public void setTesterSumTotal(String testerSumTotal) {
		TesterSumTotal = testerSumTotal;
	}
	public String getTestTimeSumTotal() {
		return TestTimeSumTotal;
	}
	public void setTestTimeSumTotal(String testTimeSumTotal) {
		TestTimeSumTotal = testTimeSumTotal;
	}
	public String getWorkLoadTotal() {
		return WorkLoadTotal;
	}
	public void setWorkLoadTotal(String workLoadTotal) {
		WorkLoadTotal = workLoadTotal;
	}
	public String getUpMonth() {
		return upMonth;
	}
	public void setUpMonth(String upMonth) {
		this.upMonth = upMonth;
	}
	public String getProjectTypeQuery() {
		return projectTypeQuery;
	}
	public void setProjectTypeQuery(String projectTypeQuery) {
		this.projectTypeQuery = projectTypeQuery;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getTestStatusQuery() {
		return testStatusQuery;
	}
	public void setTestStatusQuery(String testStatusQuery) {
		this.testStatusQuery = testStatusQuery;
	}
	public String getSumbitProcessQuery() {
		return sumbitProcessQuery;
	}
	public void setSumbitProcessQuery(String sumbitProcessQuery) {
		this.sumbitProcessQuery = sumbitProcessQuery;
	}
	public String getTestMan() {
		return testMan;
	}
	public void setTestMan(String testMan) {
		this.testMan = testMan;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public TestQuestion getQuestion() {
		return question;
	}
	public void setQuestion(TestQuestion question) {
		this.question = question;
	}

}
