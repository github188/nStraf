package cn.grgbanking.feeltm.monthMission.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.MonthMission;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.monthMission.service.MonthMissionService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class MonthMissionAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private MonthMissionService monthMissionService;
	private MonthMission monthMission;
	private String monthDate; 
	private String currentName;
	private String groupName; 
	private String examScore;
    private String examRank;
    private	String note;
    private List<String> unames;
    private List<MonthMission> MonthMissionList;
    private List<MonthMission> MonthMissionModList = new ArrayList<MonthMission>();
    
	public List<MonthMission> getMonthMissionModList() {
		return MonthMissionModList;
	}

	public void setMonthMissionModList(List<MonthMission> monthMissionModList) {
		MonthMissionModList = monthMissionModList;
	}

	public MonthMissionService getMonthMissionService() {
		return monthMissionService;
	}

	public void setMonthMissionService(MonthMissionService monthMissionService) {
		this.monthMissionService = monthMissionService;
	}

	public MonthMission getMonthMission() {
		return monthMission;
	}

	public void setMonthMission(MonthMission monthMission) {
		this.monthMission = monthMission;
	}

	public String getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getExamScore() {
		return examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public String getExamRank() {
		return examRank;
	}

	public void setExamRank(String examRank) {
		this.examRank = examRank;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<MonthMission> getMonthMissionList() {
		return MonthMissionList;
	}

	public void setMonthMissionList(List<MonthMission> monthMissionList) {
		MonthMissionList = monthMissionList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String add(){
		return "add";
	}
	
	public String up(){
		return "selmonthLock";
	}
	
	public String upLock(){
		String _monthDate = request.getParameter("monthDate");
		Boolean flag = monthMissionService.checkMonthDate(_monthDate);
		if(!flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"尚无创建当月考核记录"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		flag = monthMissionService.checkEditLock(_monthDate);
		if(flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"当月考核记录已提交"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		List<String> allIds = monthMissionService.getAllIds(_monthDate);
		for (int i = 0; i < allIds.size(); i++) {
			MonthMission _mm = monthMissionService.getMonthMissionById(allIds.get(i));
			_mm.setEditLock("1");
			monthMissionService.update(_mm);
		}
		MsgBox msgBox = new MsgBox(request,
				getText("operInfoform.updateok", new String[]{"提交成功"}));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	public String save() throws Exception { 
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		boolean flag = false;
		try
		{
			String _monthDate = monthMission.getMonthDate();
			flag = monthMissionService.checkMonthDate(_monthDate);
			if(flag)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度任务已创建，新增失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			unames=monthMissionService.getAllNames();
			for(String mm:unames){
				monthMission.setCurrentName(mm);
				monthMission.setGroupName(monthMissionService.getUserGroup_name(mm.trim()));
				monthMission.setMonthDate(_monthDate);
				monthMission.setEditLock("0");
				flag=monthMissionService.add(monthMission);
			}
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
		}
		catch(Exception e)
		{
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
					MonthMission temp = monthMissionService.getMonthMissionById(sids[i]);
					
					Boolean flag = monthMissionService.checkEditLock(temp.getMonthDate());
					if(flag)
					{
						MsgBox msgBox = new MsgBox(request,
								getText("operator.deletefaile", new String[]{"当月考核记录已提交"}));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					
					monthMissionService.delete(temp);
					iCount++;
				}
			}

			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.deletefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 

	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String ids = request.getParameter("ids");
			monthMission = monthMissionService.getMonthMissionById(ids);
			String _monthDate = monthMission.getMonthDate();
			Boolean flag = monthMissionService.checkEditLock(_monthDate);
			if(flag)
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"当月考核记录已提交"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	 }
		
	public String update() throws Exception {
		try {
			monthMission.setEditLock("0");
			Boolean flag=monthMissionService.update(monthMission);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"未存在当月考核记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"未存在当月考核记录"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public void refresh() {
		System.out.println("refesh");
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
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		try {
			int queryLevel = userModel.getLevel();
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = monthMissionService.getPage(userModel.getGroupName(), currentName, pageNum, pageSize, monthDate, examRank, queryLevel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					MonthMission monthMission=(MonthMission)list.get(i);
					//monthMission.setDateString(fo.format(monthMission.getStartDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.MonthMission");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("monthMissionList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupName");
		PrintWriter out=response.getWriter();
		List<Object> list=monthMissionService.getNames(groupName);
		list.set(list.size()-1, "");
	//	JSONUtil jsonUtil=new JSONUtil("java.lang.String");
		//JSONArray jsonArray=jsonUtil.toJSON(list, null);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			monthMission = monthMissionService.getMonthMissionById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String queryMonthDate()
	{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			JSONObject input = new JSONObject();
			String _monthDate = request.getParameter("monthDate");
			input.put("isHave", monthMissionService.checkMonthDate(_monthDate));
			PrintWriter out = response.getWriter();
			out.print(input);
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String userAndGroup()
	{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			JSONObject input = new JSONObject();
			input.put("userName", userModel.getUsername());	
			input.put("groupName", userModel.getGroupName());	
			PrintWriter out = response.getWriter();
			out.print(input);
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String selmonth()
	{
		return "selmonth";
	}
	
	public String modifybat()
	{
		String _monthDate = request.getParameter("monthDate");
		Boolean flag = monthMissionService.checkMonthDate(_monthDate);
		if(!flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"尚无创建当月考核记录"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		flag = monthMissionService.checkEditLock(_monthDate);
		if(flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"当月考核记录已提交"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		List<String> allIds = monthMissionService.getAllIds(_monthDate);
		for (int i = 0; i < allIds.size(); i++) {
			MonthMission _mm = monthMissionService.getMonthMissionById(allIds.get(i));
			MonthMissionModList.add(_mm);
		}
		ActionContext.getContext().put("MonthMissionModList", MonthMissionModList);
		return "modifybat";
	}
	
	public String updatebat() throws Exception {
		try {
			List<MonthMission> modList = getModList(request);
			for(int i=0; i<modList.size(); i++)
			{
				MonthMission _mm = modList.get(i);
				boolean flag=monthMissionService.update(_mm);
				if (flag == true) {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updateok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile", new String[]{"已存在当月考核记录"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"已存在当月考核记录"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	private List<MonthMission> getModList(HttpServletRequest request){
		List<MonthMission> reports=new ArrayList<MonthMission>();
		String[] monthDates=request.getParameterValues("monthDate");
		String[] ids=request.getParameterValues("id");
		String[] currentNames=request.getParameterValues("currentName");
		String[] groupNames=request.getParameterValues("groupName");
		String[] examScores=request.getParameterValues("examScore");
		String[] examRanks=request.getParameterValues("examRank");
		String[] notes=request.getParameterValues("note");
		int size=currentNames.length;
		for(int i=0;i<size;i++){
			MonthMission r=new MonthMission();
			r.setId(ids[i]);
			r.setMonthDate(monthDates[i]);
			r.setCurrentName(currentNames[i]);
			r.setGroupName(groupNames[i]);
			r.setExamScore(examScores[i]);
			r.setExamRank(examRanks[i]);
			r.setNote(notes[i]);
			r.setEditLock("0");
			reports.add(r);
		}
		return reports;
	}
	
}
