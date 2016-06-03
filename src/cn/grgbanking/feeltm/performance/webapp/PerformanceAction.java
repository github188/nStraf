package cn.grgbanking.feeltm.performance.webapp;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.Performance;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.performance.service.PerformanceService;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class PerformanceAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private PerformanceService performanceService;
	
	private Performance performance;
//	private String status;
	private String category;
	private String pn;   //问题编号
	private String raiseEndDate;   //不用
   
    private String modify_man;
    private	String groupname;
    private	String month;
    private	String month_date;
    private	String user_id;
    private	String username;
    private String UserEffect_score;
    private String UserPrice_score;
    private String UserEffect_num;
    private String UserPrice_num;
    private String UserpriseValue;
    private String UserpunishValue;
    private int UserUnwriteNum;
    private String UserTrain_num;
    private String UserMeetingUnAudit_num;
    private String UserUnAudit;
    private List<String> unames;
    
    private List<Performance> PerformanceList;
	private String flag="0";
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
		public String add(){
		unames=(List<String>)session.get("empNames");
		map=testerDao.getNameListBySuggestion();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		return "add";
	}
	
	public String up(){
			System.out.println("up");
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			modify_man=userModel.getUsername();
			return "up";
	}
	
	public String uplock() throws Exception {
		System.out.println("uplock");
		boolean flag = false;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		boolean oldmonth_date =false;
		oldmonth_date = performanceService.getUserMonth_date(month_date);
		if(oldmonth_date == false)
		{
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度绩效没有创建，提交失败"}));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		String oldedit = performanceService.getUseredit_lock(month_date);
		if(oldedit.equals("1.0"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"该月度绩效已提交"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		try {
			flag=performanceService.upEdit_lock(month_date);
			if (flag == true){
				MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		catch (Exception e) {
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
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 发送oa短信，及邮件
	 * 标题：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * 正文：
	 * oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {     //保存suggestion概况的数据
		
		try {
				boolean flag = false;
				UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			//	user_id = user_id.replace(",","、");
			//	String[] usergroup=user_id.trim().split("、");
				boolean oldmonth_date =false;
				oldmonth_date = performanceService.getUserMonth_date(performance.getMonth_date());
				if(oldmonth_date == true)
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度绩效已创建，新增失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				
				String no= "";
				
				unames=performanceService.getAllNames();
			
				for(String mm:unames){
					no = performanceService.getNextNo();
					performance.setPno(no);
					performance.setUpdate_man(userModel.getUsername());	
					performance.setGroup_name(performanceService.getUserGroup_name(mm.trim()));
					performance.setUser_id(mm.trim());
					SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					performance.setModify_date(f.format(new Date()));
					//performance.setMonth_date(month_date.trim());
					flag=performanceService.add(performance);
				}
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
				//采用多线程
			//	Thread aa=new Thread(new Runnable() {
			//		@Override
			//		public void run() {
			//			OaOrEmail oe=new OaOrEmail();
			//			oe.sendMailOaByNew(suggestion);						
			//		}
			//	});
			//	aa.start();
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
					Performance temp = performanceService.getCaseById(sids[i]);
					performanceService.delete(temp);
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
		map=testerDao.getNameListBySuggestion();
		String forwardPage="edit";
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);

		System.out.println("yyyyy");
		try {
			String ids = request.getParameter("ids");
			performance=performanceService.getCaseById(ids);
			
			
			//写获取数量语句
			
			//Performance oldSuggestion=performanceService.getCaseById(performance.getId());
			String oldUsername=performance.getUser_id();
			String oldGname=performance.getGroup_name();
			String oldMonth=performance.getMonth_date();
			String userGname = userModel.getGroupName();
			int headmanLevel = userModel.getLevel();
			String userLevel = performanceService.getUserlevel(oldUsername);
			double oldmanlock = performance.getMan_lock();
			double oldeditlock = performance.getEdit_lock();
			if(userModel.getLevel()==0){
				forwardPage="edit_manage";
			}else if(userModel.getLevel()==1 && !userModel.getUsername().equals(oldUsername)){
				forwardPage="edit_headman";
			}
			if(userModel.getLevel()!=1 && userModel.getLevel()!=0 && !userModel.getUsername().equals(oldUsername))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您没有修改该月度绩效的权限 "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			else if(userModel.getLevel()==1 && userModel.getUsername().equals(oldUsername))
			{
				forwardPage="edit";
			}
			if(oldeditlock == 1)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度绩效已提交，修改失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(1 == headmanLevel){
				if(!userGname.equals(oldGname))
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"不能修改其他组的绩效记录"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			String Usernametmp = oldUsername;
			String Month_datetmp = oldMonth;
			String UserGroup_nametmp = performanceService.getUserGroup_name(Usernametmp);
			
		
			Date date2 = null;
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    	String str5 = "";
			str5 = inputFormat.format(new Date());  //实例化日期类型
			date2 = inputFormat.parse(str5);
			
			UserEffect_score=performanceService.getUserEffect_score(Usernametmp, Month_datetmp);
			UserPrice_score=performanceService.getUserPrice_score(Usernametmp, Month_datetmp);
			UserEffect_num=performanceService.getUserEffect_num(Usernametmp, Month_datetmp);
			UserPrice_num=performanceService.getUserPrice_num(Usernametmp, Month_datetmp);
			UserpriseValue=performanceService.getUser_priseValue(Usernametmp, Month_datetmp);
			UserpunishValue=performanceService.getUser_punishValue(Usernametmp, Month_datetmp);
			UserUnwriteNum=performanceService.getReportStatics(Usernametmp, Month_datetmp,date2);  //传入当前时间值date2
			UserTrain_num=performanceService.getUserTrain_num(Usernametmp, Month_datetmp);
			UserUnAudit = null;
			if(performanceService.getUserlevel(Usernametmp).equals("1"))
			{
				UserUnAudit = performanceService.getUserMeetingUnAudit_num(UserGroup_nametmp,Month_datetmp);////////
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}

		return forwardPage;
	 }
	
	public double meg(double i){
		  int b = (int)Math.round(i * 10); //小数点后两位前移，并四舍五入 
		  double c = ((double)b/10.0); //还原小数点后两位
		  if((c*10)%5!=0){
		   int d = (int)Math.round(c); //小数点前移，并四舍五入 
		   c = ((double)d); //还原小数点
		  }
		  return c;
		 }

	
	public String update() throws Exception {
		System.out.println("update");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		Performance oldSuggestion=performanceService.getCaseById(performance.getId());
		String oldUsername=oldSuggestion.getUser_id();
		String oldGname=oldSuggestion.getGroup_name();
		String oldMonth=oldSuggestion.getMonth_date();
		String userGname = userModel.getGroupName();
		int headmanLevel = userModel.getLevel();
		String userLevel = performanceService.getUserlevel(oldUsername);
		double oldmanlock = oldSuggestion.getMan_lock();
		double oldeditlock = oldSuggestion.getEdit_lock();
	//	final String newStatus=suggestion.getStatus();
		if(oldeditlock == 1)
		{
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度绩效已提交，修改失败"}));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(1 == headmanLevel){
			if(!userGname.equals(oldGname))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"不能修改其他组的绩效记录"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			else if(oldmanlock == 0)
			{		
				performance.setSubtotal_m_1(performance.getSubtotal_d_1());
				performance.setSubtotal_m_2(performance.getSubtotal_d_2());
				performance.setSubtotal_m_3(performance.getSubtotal_d_3());
				performance.setSubtotal_m_4(performance.getSubtotal_d_4());
				performance.setSubtotal_m_5(performance.getSubtotal_d_5());
				performance.setSubtotal_m_6(performance.getSubtotal_d_6());
				performance.setSubtotal_m_7(performance.getSubtotal_d_7());
				performance.setSubtotal_m_8(performance.getSubtotal_d_8());
				performance.setSubtotal_m_9(performance.getSubtotal_d_9());
				performance.setSubtotal_m_10(performance.getSubtotal_d_10());
				performance.setSubtotal_m_11(performance.getSubtotal_d_11());
				performance.setSubtotal_m_12(performance.getSubtotal_d_12());
				performance.setSubtotal_m_13(performance.getSubtotal_d_13());
				performance.setSubtotal_m_14(performance.getSubtotal_d_14());
				performance.setSubtotal_m_15(performance.getSubtotal_d_15());
				performance.setSubtotal_m_16(performance.getSubtotal_d_16());
				performance.setSubtotal_m_17(performance.getSubtotal_d_17());
				performance.setSubtotal_m_18(performance.getSubtotal_d_18());
				performance.setSubtotal_m_19(performance.getSubtotal_d_19());
				performance.setSubtotal_m_20(performance.getSubtotal_d_20());
				performance.setSubtotal_m_21(performance.getSubtotal_d_21());
				performance.setSubtotal_m_22(performance.getSubtotal_d_22());
				performance.setSubtotal_m_23(performance.getSubtotal_d_23());
				performance.setSubtotal_m_24(performance.getSubtotal_d_24());
				performance.setSubtotal_m_25(performance.getSubtotal_d_25());
				performance.setSubtotal_m_26(performance.getSubtotal_d_26());
				performance.setSubtotal_m_27(performance.getSubtotal_d_27());
				performance.setSubtotal_m_28(performance.getSubtotal_d_28());
				performance.setSubtotal_m_29(performance.getSubtotal_d_29());
				performance.setSubtotal_m_30(performance.getSubtotal_d_30());
				performance.setSubtotal_m_31(performance.getSubtotal_d_31());
				performance.setSubtotal_m_32(performance.getSubtotal_d_32());
				performance.setSubtotal_m_33(performance.getSubtotal_d_33());
				performance.setSubtotal_m_34(performance.getSubtotal_d_34());
				performance.setSubtotal_m_35(performance.getSubtotal_d_35());
				performance.setSubtotal_m_36(performance.getSubtotal_d_36());
				performance.setSubtotal_m_37(performance.getSubtotal_d_37());
				performance.setSubtotal_m_38(performance.getSubtotal_d_38());
				performance.setSubtotal_m_39(performance.getSubtotal_d_39());
				performance.setSubtotal_m_40(performance.getSubtotal_d_40());
				performance.setSubtotal_m_41(performance.getSubtotal_d_41());
				performance.setSubtotal_m_42(performance.getSubtotal_d_42());
				performance.setSubtotal_m_43(performance.getSubtotal_d_43());
				performance.setSubtotal_m_44(performance.getSubtotal_d_44());
				performance.setSubtotal_m_45(performance.getSubtotal_d_45());
				performance.setSubtotal_m_46(performance.getSubtotal_d_46());
				performance.setSubtotal_m_47(performance.getSubtotal_d_47());
				performance.setSubtotal_m_48(performance.getSubtotal_d_48());
				performance.setSubtotal_m_49(performance.getSubtotal_d_49());
				performance.setSubtotal_m_50(performance.getSubtotal_d_50());
				performance.setSubtotal_m_51(performance.getSubtotal_d_51());
				performance.setSubtotal_m_52(performance.getSubtotal_d_52());
				performance.setSubtotal_m_53(performance.getSubtotal_d_53());
				performance.setSubtotal_m_54(performance.getSubtotal_d_54());
				performance.setSubtotal_m_55(performance.getSubtotal_d_55());
				performance.setSubtotal_m_56(performance.getSubtotal_d_56());
				performance.setSubtotal_m(performance.getSubtotal_d());
			}
			else
			{
				
			}			
		}
		try {
			double sum_m = 0,sum_d = 0,sum_s = 0;
			sum_m = performance.getSubtotal_m();
			sum_d = performance.getSubtotal_d();
			if(userLevel.equals("2") && sum_d != 0){
				
					sum_s = (sum_d*0.7) + (sum_m*0.3);		
			}
			else
			{
				sum_s = sum_m;
			}				     
		    BigDecimal b = new BigDecimal(sum_s);
		     
		    double f1= b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			//performance.setSubtotal_s(meg(sum_s));
		    performance.setSubtotal_s(f1);
			performance.setMan_lock(oldmanlock);
			performance.setEdit_lock(oldeditlock);
			if(0 == headmanLevel){
				performance.setMan_lock(1);
			}
			String ano=oldSuggestion.getPno();
			performance.setUser_id(oldUsername);
			performance.setGroup_name(oldGname);
			performance.setMonth_date(oldMonth);
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			performance.setModify_date(f.format(new Date()));
			performance.setUpdate_man(modify_man);
			performance.setPno(ano);
			//performance.setRemark(performance.getRemark().replace("\r\n","<br>").replace("\n","<br>"));
			boolean flag=performanceService.update(performance);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
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
		System.out.println("refesh");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
    
	
	public String query() throws Exception {
		System.out.println("query");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {			
				String from = request.getParameter("from");
				if(session.get("empNames")==null){
					unames=performanceService.getAllNames();
					session.put("empNames",unames );
					Map<String,String> umap=new HashMap<String,String>();
				//	Map<String,String> umaps=new HashMap<String,String>();
					for(String u:unames){
						umap.put(u, u);
					}
					session.put("empMap",umap );
				}
			String queryGroupname = userModel.getGroupName();
			String queryName = userModel.getUsername();
			int queryLevel = userModel.getLevel();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
				&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			//month = request.getParameter("month");
			Page page = performanceService.getPage(groupname, username,queryGroupname,queryLevel,pageNum, pageSize, month);
			//createDate, createMan, update_man, category, pn,pageNum, pageSize, raiseEndDate
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
				//	  Performance tmp=(Performance)list.get(i);
				//    tmp.setUpdateDateString(tmp.getModify_date()==null?"":fo1.format(tmp.getModify_date()));
					  
				  }
				if (from != null && from.equals("refresh")) {
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.performance.domain.Perfor");
					JSONArray jsonObj = jsonUtil.toJSON(list, map);
					JSONObject input = new JSONObject();
					if(page.getRecordCount()==0){
						input.put("pageCount", String.valueOf(page.getPageCount()+1));	
						input.put("recordCount", String.valueOf(page.getRecordCount()));
						input.put("jsonObj", jsonObj);	
				}
				else{
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("behaviorList", list);
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
		String groupName=request.getParameter("groupname");
		PrintWriter out=response.getWriter();
		List<Object> list=performanceService.getNames(groupName);
	//	JSONUtil jsonUtil=new JSONUtil("java.lang.String");
		//JSONArray jsonArray=jsonUtil.toJSON(list, null);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public TesterDetailDao getTesterDao() {
		return testerDao;
	}

	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			performance=performanceService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	
	public String getRaiseEndDate() {
		return raiseEndDate;
	}

	public void setRaiseEndDate(String raiseEndDate) {
		this.raiseEndDate = raiseEndDate;
	}


	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getModify_man() {
		return modify_man;
	}

	public List<Performance> getPerformanceList() {
		return PerformanceList;
	}

	public void setModify_man(String modify_man) {
		this.modify_man = modify_man;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setPerformanceList(List<Performance> PerformanceList) {
		this.PerformanceList = PerformanceList;
	}

	public List<String> getUnames() {
		return unames;
	}

	public void setUnames(List<String> unames) {
		this.unames = unames;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PerformanceService getPerformanceService() {
		return performanceService;
	}

	public Performance getPerformance() {
		return performance;
	}

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public void setPerformanceService(PerformanceService performanceService) {
		this.performanceService = performanceService;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public String getUserEffect_score() {
		return UserEffect_score;
	}

	public void setUserEffect_score(String userEffect_score) {
		UserEffect_score = userEffect_score;
	}

	public String getUserPrice_score() {
		return UserPrice_score;
	}

	public void setUserPrice_score(String userPrice_score) {
		UserPrice_score = userPrice_score;
	}

	public String getUserEffect_num() {
		return UserEffect_num;
	}

	public void setUserEffect_num(String userEffect_num) {
		UserEffect_num = userEffect_num;
	}

	public String getUserPrice_num() {
		return UserPrice_num;
	}

	public void setUserPrice_num(String userPrice_num) {
		UserPrice_num = userPrice_num;
	}

	public String getUserpriseValue() {
		return UserpriseValue;
	}

	public void setUserpriseValue(String userpriseValue) {
		UserpriseValue = userpriseValue;
	}

	public String getUserpunishValue() {
		return UserpunishValue;
	}

	public void setUserpunishValue(String userpunishValue) {
		UserpunishValue = userpunishValue;
	}

	public int getUserUnwriteNum() {
		return UserUnwriteNum;
	}

	public void setUserUnwriteNum(int userUnwriteNum) {
		UserUnwriteNum = userUnwriteNum;
	}

	public String getUserTrain_num() {
		return UserTrain_num;
	}

	public void setUserTrain_num(String userTrain_num) {
		UserTrain_num = userTrain_num;
	}

	public String getUserMeetingUnAudit_num() {
		return UserMeetingUnAudit_num;
	}

	public void setUserMeetingUnAudit_num(String userMeetingUnAudit_num) {
		UserMeetingUnAudit_num = userMeetingUnAudit_num;
	}

	public String getUserUnAudit() {
		return UserUnAudit;
	}

	public void setUserUnAudit(String userUnAudit) {
		UserUnAudit = userUnAudit;
	}

}
