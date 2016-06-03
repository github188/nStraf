package cn.grgbanking.feeltm.totalPriceHist.webapp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.SysInfo;
import cn.grgbanking.feeltm.domain.testsys.TotalPriceHist;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.sysinfo.service.SysInfoService;
import cn.grgbanking.feeltm.totalPriceHist.service.TotalPriceHistService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class TotalPriceHistAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private TotalPriceHistService totalPriceHistService;
	private TotalPriceHist totalPriceHist;
	private String monthDate; 
	private String currentName;
	private String groupName; 
	private String workTitleHist;
	private String workTitleFut;
	private String updateName;
	private Date updateDate;
	private double totalScore;
	private SysInfoService sysInfoService;
    private List<String> unames;
    private List<TotalPriceHist> TotalPriceHistList;
    private Map<String,String> MonthMissionMap = new TreeMap<String,String>();
    private String khType = "";
    
	public String getKhType() {
		return khType;
	}

	public void setKhType(String khType) {
		this.khType = khType;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Map<String,String> getMonthMissionMap() {
		return MonthMissionMap;
	}

	public void setMonthMissionMap(Map<String,String> monthMissionMap) {
		MonthMissionMap = monthMissionMap;
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


	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	
	public TotalPriceHistService getTotalPriceHistService() {
		return totalPriceHistService;
	}

	public void setTotalPriceHistService(TotalPriceHistService totalPriceHistService) {
		this.totalPriceHistService = totalPriceHistService;
	}

	public TotalPriceHist getTotalPriceHist() {
		return totalPriceHist;
	}

	public void setTotalPriceHist(TotalPriceHist totalPriceHist) {
		this.totalPriceHist = totalPriceHist;
	}

	public String getWorkTitleHist() {
		return workTitleHist;
	}

	public void setWorkTitleHist(String workTitleHist) {
		this.workTitleHist = workTitleHist;
	}

	public String getWorkTitleFut() {
		return workTitleFut;
	}

	public void setWorkTitleFut(String workTitleFut) {
		this.workTitleFut = workTitleFut;
	}

	public List<TotalPriceHist> getTotalPriceHistList() {
		return TotalPriceHistList;
	}

	public void setTotalPriceHistList(List<TotalPriceHist> totalPriceHistList) {
		TotalPriceHistList = totalPriceHistList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String add(){
		return "add";
	}
	
	public String queryHistMonth(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			
			Class.forName("oracle.jdbc.OracleDriver");
			Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
			StringBuffer sb=new StringBuffer();
			sb.append("select month_date from TOTAL_PRICE ")
			.append(" group by month_date ");
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sb.toString());
			
			JSONArray jsonArray=new JSONArray();
			JSONObject jtmp = new JSONObject();
			while(rs.next()){
				jtmp.put("monthHist", rs.getString(1));
				jsonArray.put(jtmp);
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonArray);
			out.flush();
			out.close();
			rs.close();
			stat.close();
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String queryNoEditMonth(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			
			Class.forName("oracle.jdbc.OracleDriver");
			Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
			StringBuffer sb=new StringBuffer();
			sb.append("select month_date from TOTAL_PRICE_HIST ")
			.append(" where edit_lock='0' ")
			.append(" group by month_date ");
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sb.toString());
			
			JSONArray jsonArray=new JSONArray();
			JSONObject jtmp = new JSONObject();
			while(rs.next()){
				jtmp.put("monthHist", rs.getString(1));
				jsonArray.put(jtmp);
			}
			
			PrintWriter out = response.getWriter();
			out.print(jsonArray);
			out.flush();
			out.close();
			rs.close();
			stat.close();
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String up(){
		return "selmonthLock";
	}
	
	public String upLock(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String _monthDate = request.getParameter("monthDate");
		Boolean flag = totalPriceHistService.checkMonthDate(_monthDate);
		if(!flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"尚无创建当月历史评估"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		flag = totalPriceHistService.checkEditLock(_monthDate);
		if(flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"当月历史评估已提交"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		List<String> allIds = totalPriceHistService.getAllIds(_monthDate);
		for (int i = 0; i < allIds.size(); i++) {
			TotalPriceHist _mm = totalPriceHistService.getTotalPriceById(allIds.get(i));
			
			String sysInfoids = sysInfoService.getUsernamesByname(_mm.getCurrentName());
			SysInfo sysInfo = sysInfoService.getCaseById(sysInfoids);
			sysInfo.setWorkstation(_mm.getWorkTitleHist());
			sysInfoService.update(sysInfo);
			_mm.setUpdateName(userModel.getUsername());
			_mm.setUpdateDate(new Date());
			_mm.setEditLock("1");
			totalPriceHistService.update(_mm);
		}
		MsgBox msgBox = new MsgBox(request,
				getText("operInfoform.updateok", new String[]{"提交成功"}));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	public String save() throws Exception { 
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		StringBuffer sb=new StringBuffer();
		boolean flag = false;
		try
		{
			String _monthDate = totalPriceHist.getMonthDate();
			flag = totalPriceHistService.checkMonthDate(_monthDate);
			if(flag)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度任务已创建，新增失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			ArrayList<String> unames1 = new ArrayList<String>();
			if(!unames1.isEmpty()){
				unames1.clear();
			}
			sb.delete(0, sb.length());
			sb.append("select distinct current_name from TOTAL_PRICE ");
			Statement stat1 = conn.createStatement();
			ResultSet rs1 = stat1.executeQuery(sb.toString());
			while(rs1.next()){
				String szTmp = rs1.getString(1);
				unames1.add(szTmp);
			}
			rs1.close();
			stat1.close();
			
//			unames=totalPriceHistService.getAllNames();
			for(String mm:unames1){
				// 获取所有人的考核数据
				totalPriceHist.setCurrentName(mm);
				totalPriceHist.setGroupName(totalPriceHistService.getUserGroup_name(mm.trim()));
				totalPriceHist.setMonthDate(_monthDate);
				totalPriceHist.setEditLock("0");
				
				//总分，原岗位，现岗位
				double iTotalScore = 0;
				String szWorkTitle = "";
				sb.delete(0, sb.length());
				sb.append("select total_score, work_title ")
				.append(" from TOTAL_PRICE ")
				.append(" where current_name='"+mm.trim()+"' ");
				
				Statement stat = conn.createStatement();
				ResultSet rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					iTotalScore = rs.getDouble(1);
					szWorkTitle = rs.getString(2);
				}
				totalPriceHist.setTotalScore(iTotalScore);
				totalPriceHist.setWorkTitleFut(szWorkTitle);
				//获取半年前的历史岗位
				String szTmpTitle = "";
				sb.delete(0, sb.length());
				//获取半年前月份
				sb.append("select work_title from TOTAL_PRICE  ")
				.append(" where (to_date(month_date, 'yyyy-mm') ")
				.append(" > add_months(to_date('2013-12', 'yyyy-mm'),-7) and ")
				.append(" to_date(month_date, 'yyyy-mm')  ")
				.append(" < add_months(to_date('2013-12', 'yyyy-mm'),-5)) ")
				.append(" and current_name='"+mm.trim()+"' ");;
				Statement stat2 = conn.createStatement();
				ResultSet rs2 = stat2.executeQuery(sb.toString());
				while(rs.next()){
					szTmpTitle = rs2.getString(1);
				}
				
				totalPriceHist.setWorkTitleHist(szTmpTitle);
				totalPriceHist.setUpdateName(userModel.getUsername());
				totalPriceHist.setUpdateDate(new Date());
				flag = totalPriceHistService.add(totalPriceHist);
				rs2.close();
				stat2.close();
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
			conn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SysLog.error(e);
			try{
				conn.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
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
					TotalPriceHist temp = totalPriceHistService.getTotalPriceById(sids[i]);
					
					Boolean flag = totalPriceHistService.checkEditLock(temp.getMonthDate());
					if(flag)
					{
						MsgBox msgBox = new MsgBox(request,
								getText("operator.deletefaile", new String[]{"当月历史评价已提交"}));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					
					totalPriceHistService.delete(temp);
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
			totalPriceHist = totalPriceHistService.getTotalPriceById(ids);
			String _monthDate = totalPriceHist.getMonthDate();
			Boolean flag = totalPriceHistService.checkEditLock(_monthDate);
			if(flag)
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"当月历史记录已提交"}));
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
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			totalPriceHist.setEditLock("0");
			totalPriceHist.setUpdateName(userModel.getUsername());
			totalPriceHist.setUpdateDate(new Date());
			Boolean flag=totalPriceHistService.update(totalPriceHist);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"未存在当月历史记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"未存在当月历史记录"}));
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
		try {
			int queryLevel = userModel.getLevel();
			String from = request.getParameter("from");
			SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = totalPriceHistService.getPage(userModel.getGroupName(), currentName, pageNum, pageSize, monthDate, queryLevel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					TotalPriceHist tmp=(TotalPriceHist)list.get(i);
					  tmp.setUpdateDateString(tmp.getUpdateDate()==null?"":fo1.format(tmp.getUpdateDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.TotalPriceHist");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("totalPriceHistList", list);
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
		List<Object> list=totalPriceHistService.getNames(groupName);
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
			totalPriceHist = totalPriceHistService.getTotalPriceById(ids);
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
			input.put("isHave", totalPriceHistService.checkMonthDate(_monthDate));
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

	public SysInfoService getSysInfoService() {
		return sysInfoService;
	}

	public void setSysInfoService(SysInfoService sysInfoService) {
		this.sysInfoService = sysInfoService;
	}
	
}
