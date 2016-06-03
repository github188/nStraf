package cn.grgbanking.feeltm.totalPrice.webapp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.testsys.TotalPrice;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.totalPrice.service.TotalPriceService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class TotalPriceAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private TotalPriceService totalPriceService;
	private TotalPrice totalPrice;
	private String monthDate; 
	private String currentName;
	private String groupName; 
	private String workTitle;
	private String bachela;
	private String workLen;
	private String sworkLen;
	private String technology;
	private double  totalScore;
	private double  monthScore;
	private double  abilityScore;
	private double  behaviorScore;
	private double  kpiScore;
	private String msId;
	private String asId;
	private String bsId;
	private String ksId;
	
    private List<String> unames;
    private List<TotalPrice> TotalPriceList;
    private Map<String,String> MonthMissionMap = new TreeMap<String,String>();
    private String khType = "";
    
	public String getSworkLen() {
		return sworkLen;
	}

	public void setSworkLen(String sworkLen) {
		this.sworkLen = sworkLen;
	}

	public String getKhType() {
		return khType;
	}

	public void setKhType(String khType) {
		this.khType = khType;
	}

	public Map<String,String> getMonthMissionMap() {
		return MonthMissionMap;
	}

	public void setMonthMissionMap(Map<String,String> monthMissionMap) {
		MonthMissionMap = monthMissionMap;
	}

	public TotalPriceService getTotalPriceService() {
		return totalPriceService;
	}

	public void setTotalPriceService(TotalPriceService totalPriceService) {
		this.totalPriceService = totalPriceService;
	}

	public TotalPrice getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(TotalPrice totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getMsId() {
		return msId;
	}

	public void setMsId(String msId) {
		this.msId = msId;
	}

	public String getAsId() {
		return asId;
	}

	public void setAsId(String asId) {
		this.asId = asId;
	}

	public String getBsId() {
		return bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}

	public String getKsId() {
		return ksId;
	}

	public void setKsId(String ksId) {
		this.ksId = ksId;
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

	public String getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}

	public String getBachela() {
		return bachela;
	}

	public void setBachela(String bachela) {
		this.bachela = bachela;
	}

	public String getWorkLen() {
		return workLen;
	}

	public void setWorkLen(String workLen) {
		this.workLen = workLen;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public double getMonthScore() {
		return monthScore;
	}

	public void setMonthScore(double monthScore) {
		this.monthScore = monthScore;
	}

	public double getAbilityScore() {
		return abilityScore;
	}

	public void setAbilityScore(double abilityScore) {
		this.abilityScore = abilityScore;
	}

	public double getBehaviorScore() {
		return behaviorScore;
	}

	public void setBehaviorScore(double behaviorScore) {
		this.behaviorScore = behaviorScore;
	}

	public double getKpiScore() {
		return kpiScore;
	}

	public void setKpiScore(double kpiScore) {
		this.kpiScore = kpiScore;
	}

	public List<TotalPrice> getTotalPriceList() {
		return TotalPriceList;
	}

	public void setTotalPriceList(List<TotalPrice> totalPriceList) {
		TotalPriceList = totalPriceList;
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
		Boolean flag = totalPriceService.checkMonthDate(_monthDate);
		if(!flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"尚无创建当月综合评估"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		flag = totalPriceService.checkEditLock(_monthDate);
		if(flag)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"当月综合评估已提交"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		List<String> allIds = totalPriceService.getAllIds(_monthDate);
		for (int i = 0; i < allIds.size(); i++) {
			TotalPrice _mm = totalPriceService.getTotalPriceById(allIds.get(i));
			_mm.setEditLock("1");
			totalPriceService.update(_mm);
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
		String kpiS = "";
		double kpiD = 0.0;
		double totalS = 0.0;
		String itemId = "";
		try
		{
			String _monthDate = totalPrice.getMonthDate();
			flag = totalPriceService.checkMonthDate(_monthDate);
			if(flag)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度任务已创建，新增失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			unames=totalPriceService.getAllNames();
			for(String mm:unames){
				totalPrice.setCurrentName(mm);
				totalPrice.setGroupName(totalPriceService.getUserGroup_name(mm.trim()));
				totalPrice.setMonthDate(_monthDate);
				totalPrice.setEditLock("0");
				totalS = 0.0;
				
				//学历，岗位，职称，在运通年限
				String szEdu = "";
				String szPos = "";
				String szTech = "";
				String szHire = "";
				String szSWorkDate = "";
				String szNewTech = "";
				Date dHire = null;
				Date dSWorkDate = null;
				int iLevelN = 0;
				sb.delete(0, sb.length());
				sb.append("select c_education, c_workstation, c_technicaltitle, c_workingdate, c_startworddate ")
				.append(" from SYS_INFO ")
				.append(" where c_username='"+mm.trim()+"' ");
				
				Statement stat = conn.createStatement();
				ResultSet rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					szEdu = rs.getString(1);
					szPos = rs.getString(2);
					szTech = rs.getString(2);
					dHire = rs.getDate(4);
					dSWorkDate = rs.getDate(5);
					szNewTech = rs.getString(3);
				}
				Date NowDate = new Date();
				
				try{
					if(dHire!=null){
						double grgagetmp = 0;
						grgagetmp = NowDate.getTime() - dHire.getTime();
						double grgage =  grgagetmp / (24 * 60 * 60 * 1000)/365;
						int b = (int)Math.round(grgage * 100.0);
						Double _it = b/100.0;
						szHire = _it.toString();
					}else{
						szHire = "0";
					}
				}catch(Exception ex1){
					szHire = "0";
				}
				
				try{
					if(dHire!=null){
						double grgagetmp = 0;
						grgagetmp = NowDate.getTime() - dSWorkDate.getTime();
						double grgage =  grgagetmp / (24 * 60 * 60 * 1000)/365;
						int b = (int)Math.round(grgage * 100.0);
						Double _it = b/100.0;
						szSWorkDate = _it.toString();
					}else{
						szSWorkDate = "0";
					}
				}catch(Exception ex1){
					szSWorkDate = "0";
				}
				
				totalPrice.setBachela(szEdu);
				totalPrice.setTechnology(szNewTech);
				totalPrice.setWorkTitle(szPos);
				totalPrice.setWorkLen(szHire);
				totalPrice.setSworkLen(szSWorkDate);
				
				//获取等级的数字iLevelNum
				if(szTech==null){
					iLevelN = 0;
				}else{
					iLevelN = iLevelNum(szTech);
				}
				
				//获取学历分
				int iEdu = 0;
				if(szEdu==null){
					iEdu = 0;
				}else{
					iEdu = iEduScore(szEdu);
				}
				
				//获取年限分
				int iWorkLen = 0;
				double dWL = 0.0;
				try{
					dWL = Double.parseDouble(szHire);
				}catch(Exception e){
					dWL = 0.0;
				}
				iWorkLen = iWorkYearScore(dWL);
				
				//获取总年限分
				int iSWorkLen = 0;
				dWL = 0.0;
				try{
					dWL = Double.parseDouble(szSWorkDate);
				}catch(Exception e){
					dWL = 0.0;
				}
				iSWorkLen = iWorkTotalYearScore(dWL);
				
				
				//获取职称分
				int iTech = 0;
				if(szNewTech==null){
					iTech = 0;
				}else{
					iTech = iTechScore(szNewTech);
				}
				
				//设置日常分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select subtotal_s, id ")
				.append("from DAILYDEED ")
				.append("where month_date='"+_monthDate+"' " )
				.append("and user_id='"+mm.trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
					itemId = rs.getString(2);
				}
				totalPrice.setBsId(itemId);
				totalPrice.setBehaviorScore(kpiD);
				if(iLevelN>=3){
					totalS += kpiD*0.2;
				}else{
					totalS += kpiD*0.2;
				}
				
				//设置能力分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select subtotal_s, id ")
				.append("from STUFFTANDABILITY ")
				.append("where month_date='"+_monthDate+"' " )
				.append("and user_id='"+mm.trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
					itemId = rs.getString(2);
				}
				totalPrice.setAsId(itemId);
				totalPrice.setAbilityScore(kpiD);
				if(iLevelN>=3){
					totalS += kpiD*0.3;
				}else{
					totalS += kpiD*0.3;
				}
				
				//设置月度分
				kpiD = 0;
				kpiS = "";
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select avg(exam_score) ")
				.append("from MONTH_MISSION ")
				.append("where month_date like '%" + _monthDate.substring(0, _monthDate.indexOf("-")) + "%' " )
				.append("and current_name='"+mm.trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiS = rs.getString(1);
				}
				try
				{
					kpiD = Double.parseDouble(kpiS);
				}catch(Exception e)
				{
					kpiD = 0;
				}
				//totalPrice.setMsId(itemId);
				totalPrice.setMonthScore(kpiD);
				totalS += kpiD*0.3;
				
				//设置KPI分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select avg(subtotal_s) ")
				.append("from KPIPOINT ")
				.append("where month_date like '%" + _monthDate.substring(0, _monthDate.indexOf("-")) + "%' " )
				.append("and user_id='"+mm.trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
				}
				//totalPrice.setKsId(itemId);
				totalPrice.setKpiScore(kpiD);
				totalS += kpiD*0.2;
				
				totalS += iEdu + iWorkLen + iTech;
				
				totalPrice.setTotalScore(Math.round(totalS*100)/100.0);
				flag=totalPriceService.add(totalPrice);
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
					TotalPrice temp = totalPriceService.getTotalPriceById(sids[i]);
					
					Boolean flag = totalPriceService.checkEditLock(temp.getMonthDate());
					if(flag)
					{
						MsgBox msgBox = new MsgBox(request,
								getText("operator.deletefaile", new String[]{"当月综合评价已提交"}));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					
					totalPriceService.delete(temp);
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
			totalPrice = totalPriceService.getTotalPriceById(ids);
			String _monthDate = totalPrice.getMonthDate();
			Boolean flag = totalPriceService.checkEditLock(_monthDate);
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
			totalPrice.setEditLock("0");
			Boolean flag=totalPriceService.update(totalPrice);
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
			Page page = totalPriceService.getPage(userModel.getGroupName(), currentName, pageNum, pageSize, monthDate, queryLevel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					TotalPrice totalPrice=(TotalPrice)list.get(i);
					//monthMission.setDateString(fo.format(monthMission.getStartDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.TotalPrice");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("totalPriceList", list);
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
		List<Object> list=totalPriceService.getNames(groupName);
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
			totalPrice = totalPriceService.getTotalPriceById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String showMS() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		try {
			String ids = request.getParameter("ids");
			totalPrice = totalPriceService.getTotalPriceById(ids);
			MonthMissionMap.clear();
			khType = "MS";
			
			StringBuffer sb=new StringBuffer();
			sb.append("select month_date, exam_score ")
			.append(" from MONTH_MISSION ")
			.append(" where current_name='" + totalPrice.getCurrentName().trim() + "'")
			.append(" and month_date like '%" + totalPrice.getMonthDate().substring(0, totalPrice.getMonthDate().indexOf("-")) + "%' " )
			.append(" order by month_date ");
			
			PreparedStatement stat=conn.prepareStatement(sb.toString());
			ResultSet rs=stat.executeQuery();
			
			while(rs.next()){
				String _month=rs.getString(1);
				String _score=rs.getString(2);
				MonthMissionMap.put(_month, _score);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			try{
				conn.close();
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return "showMS";
	}
	
	public String showKS() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		try {
			String ids = request.getParameter("ids");
			totalPrice = totalPriceService.getTotalPriceById(ids);
			MonthMissionMap.clear();
			khType = "KS";
			
			StringBuffer sb=new StringBuffer();
			sb.append("select month_date, subtotal_s ")
			.append(" from KPIPOINT ")
			.append(" where user_id='" + totalPrice.getCurrentName().trim() + "'")
			.append(" and month_date like '%" + totalPrice.getMonthDate().substring(0, totalPrice.getMonthDate().indexOf("-")) + "%' " )
			.append(" order by month_date ");
			
			PreparedStatement stat=conn.prepareStatement(sb.toString());
			ResultSet rs=stat.executeQuery();
			
			while(rs.next()){
				String _month=rs.getString(1);
				String _score=rs.getString(2);
				MonthMissionMap.put(_month, _score);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			try{
				conn.close();
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return "showMS";
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
			input.put("isHave", totalPriceService.checkMonthDate(_monthDate));
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
	
	public String modifybat() throws Exception
	{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		try{
			String _monthDate = request.getParameter("monthDate");
			Boolean flag = totalPriceService.checkMonthDate(_monthDate);
			if(!flag)
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"尚无创建当月综合评估"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			flag = totalPriceService.checkEditLock(_monthDate);
			if(flag)
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile", new String[]{"当月综合评估已提交"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			double totalS = 0;
			double kpiD = 0;
			String kpiS = "";
			String itemId = "";
			StringBuffer sb=new StringBuffer();
			List<String> allIds = totalPriceService.getAllIds(_monthDate);
			for (int i = 0; i < allIds.size(); i++) {
				TotalPrice _mm = totalPriceService.getTotalPriceById(allIds.get(i));
				_mm.setEditLock("0");
				totalS = 0.0;
				
				//学历，岗位，职称，在运通年限
				String szEdu = "";
				String szPos = "";
				String szTech = "";
				String szHire = "";
				String szSWorkDate = "";
				String szNewTech = "";
				Date dHire = null;
				Date dSWorkDate = null;
				int iLevelN = 0;
				sb.delete(0, sb.length());
				sb.append("select c_education, c_workstation, c_technicaltitle, c_workingdate, c_startworddate ")
				.append(" from SYS_INFO ")
				.append(" where c_username='"+_mm.getCurrentName().trim()+"' ");
				
				Statement stat = conn.createStatement();
				ResultSet rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					szEdu = rs.getString(1);
					szPos = rs.getString(2);
					szTech = rs.getString(2);
					dHire = rs.getDate(4);
					dSWorkDate = rs.getDate(5);
					szNewTech = rs.getString(3);
				}
				Date NowDate = new Date();
				
				try{
					if(dHire!=null){
						double grgagetmp = 0;
						grgagetmp = NowDate.getTime() - dHire.getTime();
						double grgage =  grgagetmp / (24 * 60 * 60 * 1000)/365;
						int b = (int)Math.round(grgage * 100.0);
						Double _it = b/100.0;
						szHire = _it.toString();
					}else{
						szHire = "0";
					}
				}catch(Exception ex1){
					szHire = "0";
				}
				
				try{
					if(dHire!=null){
						double grgagetmp = 0;
						grgagetmp = NowDate.getTime() - dSWorkDate.getTime();
						double grgage =  grgagetmp / (24 * 60 * 60 * 1000)/365;
						int b = (int)Math.round(grgage * 100.0);
						Double _it = b/100.0;
						szSWorkDate = _it.toString();
					}else{
						szSWorkDate = "0";
					}
				}catch(Exception ex1){
					szSWorkDate = "0";
				}
				
				_mm.setBachela(szEdu);
				_mm.setTechnology(szNewTech);
				_mm.setWorkTitle(szPos);
				_mm.setWorkLen(szHire);
				//_mm.setSworkLen(szSWorkDate);
				
				
				//获取等级的数字iLevelNum
				if(szTech==null){
					iLevelN = 0;
				}else{
					iLevelN = iLevelNum(szTech);
				}
				
				//获取学历分
				int iEdu = 0;
				if(szEdu==null){
					iEdu = 0;
				}else{
					iEdu = iEduScore(szEdu);
				}
				
				//获取年限分
				int iWorkLen = 0;
				double dWL = 0.0;
				try{
					dWL = Double.parseDouble(szHire);
				}catch(Exception e){
					dWL = 0.0;
				}
				iWorkLen = iWorkYearScore(dWL);
				
				//获取总年限分
				int iSWorkLen = 0;
				dWL = 0.0;
				try{
					dWL = Double.parseDouble(szSWorkDate);
				}catch(Exception e){
					dWL = 0.0;
				}
				iSWorkLen = iWorkTotalYearScore(dWL);
				
				
				//获取职称分
				int iTech = 0;
				if(szNewTech==null){
					iTech = 0;
				}else{
					iTech = iTechScore(szNewTech);
				}
				
				//设置日常分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select subtotal_s,id ")
				.append("from DAILYDEED ")
				.append("where month_date='"+_monthDate+"' " )
				//.append("and man_lock='1' ")
				.append("and user_id='"+_mm.getCurrentName().trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
					itemId = rs.getString(2);
				}
				_mm.setBsId(itemId);
				_mm.setBehaviorScore(kpiD);
				if(iLevelN>=3){
					totalS += kpiD*0.2;
				}else{
					totalS += kpiD*0.2;
				}
				
				//设置能力分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select subtotal_s,id ")
				.append("from STUFFTANDABILITY ")
				.append("where month_date='"+_monthDate+"' " )
				//.append("and man_lock='1' ")
				.append("and user_id='"+_mm.getCurrentName().trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
					itemId = rs.getString(2);
				}
				_mm.setAsId(itemId);
				_mm.setAbilityScore(kpiD);
				if(iLevelN>=3){
					totalS += kpiD*0.3;
				}else{
					totalS += kpiD*0.3;
				}
				
				//设置月度分
				kpiD = 0;
				kpiS = "";
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select avg(exam_score) ")
				.append("from MONTH_MISSION ")
				.append("where month_date like '%" + _monthDate.substring(0, _monthDate.indexOf("-")) + "%' " )
				.append("and current_name='"+_mm.getCurrentName().trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiS = rs.getString(1);
				}
				try
				{
					kpiD = Double.parseDouble(kpiS);
				}catch(Exception e)
				{
					kpiD = 0;
				}
				//_mm.setMsId(itemId);
				_mm.setMonthScore(kpiD);
				totalS += kpiD*0.3;
				
				//设置KPI分
				kpiD = 0;
				itemId = "";
				sb.delete(0, sb.length());
				sb.append("select avg(subtotal_s) ")
				.append("from KPIPOINT ")
				.append("where month_date like '%" + _monthDate.substring(0, _monthDate.indexOf("-")) + "%' " )
				.append("and user_id='"+_mm.getCurrentName().trim()+"' ");
				
				stat = conn.createStatement();
				rs = stat.executeQuery(sb.toString());
				
				while(rs.next()){
					kpiD = rs.getDouble(1);
				}
				//_mm.setKsId(itemId);
				_mm.setKpiScore(kpiD);
				totalS += kpiD*0.2;
				
				totalS += iEdu + iWorkLen + iTech + iSWorkLen;
				
				_mm.setTotalScore(Math.round(totalS*100)/100.0);
				
				
				flag=totalPriceService.update(_mm);
				if (flag == true) {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updateok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile", new String[]{"请再次刷新"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			try{
				conn.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile", new String[]{"请再次刷新"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	private int iLevelNum(String szTech){
		String szLevel = "0";
		int _iTechScore = 0;
		Map systemConfig = BusnDataDir.getMap("systemConfig.levelNum");
		Set<String> ks = systemConfig.keySet();
		for(String uid:ks)
		{
			String[] uids = uid.split("\\|");
			//if(uids[0].trim().equals(szTech.trim())){
			if( szTech.trim().contains( uids[0].trim() ) ){
				szLevel = uids[1].trim();
				break;
			}
		}
		try{
			_iTechScore = Integer.parseInt(szLevel);
		}catch(Exception e){
			_iTechScore = 0;
		}
		return _iTechScore;
	}
	
	private int iTechScore(String szTech){
		String szLevel = "0";
		int _iTechScore = 0;
		Map systemConfig = BusnDataDir.getMap("systemConfig.levelRank");
		Set<String> ks = systemConfig.keySet();
		for(String uid:ks)
		{
			String[] uids = uid.split("\\|");
			//if(uids[1].trim().equals(szTech.trim())){
			if(szTech.trim().contains(uids[1].trim())){
				szLevel = uids[2].trim();
				break;
			}
		}
		try{
			_iTechScore = Integer.parseInt(szLevel);
		}catch(Exception e){
			_iTechScore = 0;
		}
		return _iTechScore;
	}
	
	private int iEduScore(String szTech){
		String szLevel = "0";
		int _iTechScore = 0;
		Map systemConfig = BusnDataDir.getMap("systemConfig.xlf");
		Set<String> ks = systemConfig.keySet();
		for(String uid:ks)
		{
			String[] uids = uid.split("\\|");
			//if(uids[0].trim().equals(szTech.trim())){
			if(szTech.trim().contains(uids[0].trim())){
				szLevel = uids[1].trim();
				break;
			}
		}
		try{
			_iTechScore = Integer.parseInt(szLevel);
		}catch(Exception e){
			_iTechScore = 0;
		}
		return _iTechScore;
	}
	
	private int iWorkYearScore(double workLen){
		int iWYScore = 0;
		int _iWYScore = (int)Math.floor(workLen);
		if(_iWYScore<2)
		{
			_iWYScore = 1;
		}else if(_iWYScore>7)
		{
			_iWYScore = 7;
		}
		String szYear = Integer.valueOf(_iWYScore).toString();
		Map systemConfig = BusnDataDir.getMap("systemConfig.ytnx");
		String szWYScore = systemConfig.get("Y"+szYear).toString();
		try{
			iWYScore = Integer.parseInt(szWYScore);
		}catch(Exception e){
			iWYScore = 0;
		}
		return iWYScore;
	}
	
	private int iWorkTotalYearScore(double workLen){
		int iWYScore = 0;
		int _iWYScore = (int)Math.floor(workLen);
		if(_iWYScore<2)
		{
			_iWYScore = 1;
		}
		else if(_iWYScore>7)
		{
			_iWYScore = 7;
		}
		String szYear = Integer.valueOf(_iWYScore).toString();
		Map systemConfig = BusnDataDir.getMap("systemConfig.zglf");
		String szWYScore = systemConfig.get("Y"+szYear).toString();
		try{
			iWYScore = Integer.parseInt(szWYScore);
		}catch(Exception e){
			iWYScore = 0;
		}
		return iWYScore;
	}
	
}
