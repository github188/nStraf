package cn.grgbanking.feeltm.kpipoint.webapp;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.KpiPoint;
import cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion;
import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.kpipoint.dao.KpiPointDao;
import cn.grgbanking.feeltm.kpipoint.service.KpiPointService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.OaOrEmail;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class KpiPointAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private KpiPointService kpiPointService;
	
	private KpiPoint kpipoint;
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
    private String UserheadmanAddValue;
    private String UserfellowValue;
    private int UserUnwriteNum;
    private String UserTrainStride_num;
    private String UserTrainInner_num;
    private String UserMeetingUnAudit_num;
	private String SuggestionRemark;
	private double SuggestionDay;
    private String UserUnAudit;
    private List<String> unames;
    private List<ProblemOrSuggestion> UnFixedSuggestion;
    private KpiPointDao kpiPointDao;
    private List<KpiPoint> KpiPointList;
	private String flag="0";
	private int UserbugYanZhengtotal;
	private int Userbugtotal;
	private int UserModifybugtotal;
	private int UserCaseExec;
	private int UserCaseNewModify;
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
	
		public String UpAuditing() throws Exception {
			System.out.println("UpAuditing");
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			modify_man=userModel.getUsername();
			
			String ids = request.getParameter("ids");
			kpipoint=kpiPointService.getCaseById(ids);
			
			if(kpipoint.getEdit_lock() == 1)
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"该KPI记录已提交"}));
				addActionMessage(getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(!kpipoint.getStatus().equals("未提交审核"))
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"该KPI记录已提交审核"}));
				addActionMessage(getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(!kpipoint.getUser_id().equals(modify_man))
			{
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"您没有权限提交审核该KPI记录"}));
				addActionMessage(getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			try {
				kpipoint.setStatus("已提交审核");
				flag=kpiPointService.update(kpipoint);
				OaOrEmail oa=new OaOrEmail();
				if(userModel.getLevel() == 1)
				{
			    	String strOaSub;
			    	strOaSub = "请审核" + kpipoint.getUser_id()+ kpipoint.getMonth_date() + "月KPI记录,审核完成后请提交!";
					oa.oaContent("ftgui", strOaSub);
				}
				if(userModel.getLevel() == 2)
				{
			    	String strOaSub;
			    	String strOaSend;
			    	strOaSub = "请审核" + kpipoint.getUser_id()+ kpipoint.getMonth_date() + "月KPI记录,审核完成后请提交!";
			    	strOaSend = kpiPointService.getUidByLevel(kpipoint.getGroup_name());
					oa.oaContent(strOaSend, strOaSub);
				}
				if(flag == true){
					MsgBox msgBox = new MsgBox(request,
						getText("提交审核成功"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				SysLog.error(e);
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"), new String[] { e
								.toString() });
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.updatefaile"));
				boolean hasActionMessage = this.hasActionMessages();
				request.setAttribute("hasActionMessage", hasActionMessage);
				return "msgBox";
			}
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
	
	
	public String up() throws Exception {
		System.out.println("up");
		boolean flag = false;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		
		String ids = request.getParameter("ids");
		kpipoint=kpiPointService.getCaseById(ids);
		if(kpipoint.getStatus().equals("未提交审核"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"该KPI记录未提交审核!"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(kpipoint.getEdit_lock() == 1)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"该KPI记录已提交"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(kpipoint.getUser_id().equals(modify_man) && !kpipoint.getUser_id().equals("冯天桂"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"你没有权限提交该KPI记录"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(!kpipoint.getGroup_name().equals(userModel.getGroupName())&& !kpipoint.getUser_id().equals("冯天桂"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"你没有权限提交该KPI记录"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		try {
			kpipoint.setEdit_lock(1);
			flag=kpiPointService.update(kpipoint);
			if (flag == true){
				MsgBox msgBox = new MsgBox(request,
					getText("提交成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.updatefaile"));
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
				oldmonth_date = kpiPointService.getUserMonth_date(kpipoint.getMonth_date());
				if(oldmonth_date == true)
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度绩效已创建，新增失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				
				String no= "";
				
				unames=kpiPointService.getAllNames();
			
				for(String mm:unames){
					no = kpiPointService.getNextNo();
					kpipoint.setPno(no);
					kpipoint.setUpdate_man(userModel.getUsername());	
					kpipoint.setGroup_name(kpiPointService.getUserGroup_name(mm.trim()));
					kpipoint.setUser_id(mm.trim());
					kpipoint.setStatus("未提交审核");
					kpipoint.setFirstedit("初次修改");
					SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					kpipoint.setModify_date(f.format(new Date()));
					//performance.setMonth_date(month_date.trim());
					flag=kpiPointService.add(kpipoint);
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
					KpiPoint temp = kpiPointService.getCaseById(sids[i]);
					kpiPointService.delete(temp);
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
			kpipoint=kpiPointService.getCaseById(ids);
			
			//写获取数量语句
			
			//Performance oldSuggestion=performanceService.getCaseById(performance.getId());
			String oldUsername=kpipoint.getUser_id();
			String oldGname=kpipoint.getGroup_name();
			String oldMonth=kpipoint.getMonth_date();
			String userGname = userModel.getGroupName();
			int headmanLevel = userModel.getLevel();
			double oldeditlock = kpipoint.getEdit_lock();
			if(userModel.getLevel()==2 && !kpipoint.getStatus().equals("未提交审核"))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"已提交审核、修改失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(userModel.getLevel()==2 && !userModel.getUsername().equals(oldUsername))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您没有修改该记录的权限 "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(oldeditlock == 1.0)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"已提交，修改失败"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(1 == headmanLevel){
				if(!userGname.equals(oldGname))
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"不能修改其他组的记录"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				if(userModel.getUsername().equals(oldUsername) && !kpipoint.getStatus().equals("未提交审核"))
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"已提交审核、修改失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				else if(!userModel.getUsername().equals(oldUsername) && kpipoint.getStatus().equals("未提交审核"))
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"未提交审核、修改失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				else if(userModel.getUsername().equals(oldUsername) && kpipoint.getStatus().equals("未提交审核"))
				{
					forwardPage = "edit";
				}
				else
				{
					forwardPage = "edit_headman";
				}	
			}
			if(0 == headmanLevel){
				if(kpipoint.getStatus().equals("未提交审核"))
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"未提交审核、修改失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				forwardPage = "edit_headman";
			}
			String Usernametmp = oldUsername;
			String Month_datetmp = oldMonth;
			String UserGroup_nametmp = kpiPointService.getUserGroup_name(Usernametmp);
			
		
			Date date2 = null;
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    	String str5 = "";
			str5 = inputFormat.format(new Date());  //实例化日期类型
			date2 = inputFormat.parse(str5);
			
			UserEffect_score=kpiPointService.getUserEffect_score(Usernametmp, Month_datetmp);
			UserPrice_score=kpiPointService.getUserPrice_score(Usernametmp, Month_datetmp);
			UserEffect_num=kpiPointService.getUserEffect_num(Usernametmp, Month_datetmp);
			UserPrice_num=kpiPointService.getUserPrice_num(Usernametmp, Month_datetmp);
			UserheadmanAddValue=kpiPointService.getHeadmanAddValue(Usernametmp, Month_datetmp);
			UserfellowValue=kpiPointService.getFellowValue(Usernametmp, Month_datetmp);
			UserUnwriteNum=kpiPointService.getReportStatics(Usernametmp, Month_datetmp,date2);  //传入当前时间值date2
			UserTrainStride_num=kpiPointService.getUserTrainStride_num(Usernametmp, Month_datetmp);
			UserTrainInner_num=kpiPointService.getUserTrainInner_num(Usernametmp, Month_datetmp);
			UnFixedSuggestion = kpiPointService.getUnFixedSuggestion(Usernametmp);
			ProblemOrSuggestion SuggestionTmp;
			
			
			
			Date NowDate = new Date();
			DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
			forma.applyPattern("0.0");
			date2 = inputFormat.parse(kpipoint.getMonth_date()+"-01");
			SuggestionDay = 0;
			SuggestionRemark = "当月延期处理问题:";
			for(int i=0;i<UnFixedSuggestion.size();i++)
			{
				SuggestionTmp = UnFixedSuggestion.get(i);	
				if(SuggestionTmp.getPno()!=null){		

						if(SuggestionTmp.getStatus().equals("打开")){
							double grgagetmp = 0;
							grgagetmp = NowDate.getTime() - SuggestionTmp.getFinishing_date().getTime();
							if(grgagetmp>0){
								grgagetmp = grgagetmp / (24 * 60 * 60 * 1000); 	
								SuggestionDay +=grgagetmp;
								SuggestionRemark += SuggestionTmp.getPno() + "; ";
							}
						}
						else if(SuggestionTmp.getPratical_date().getTime()>SuggestionTmp.getFinishing_date().getTime())
						{
							if(SuggestionTmp.getPratical_date().getYear() == date2.getYear() && SuggestionTmp.getPratical_date().getMonth() == date2.getMonth())
							{
								double grgagetmp = 0;
								grgagetmp = SuggestionTmp.getPratical_date().getTime() - SuggestionTmp.getFinishing_date().getTime();
								if(grgagetmp>0){
									grgagetmp = grgagetmp / (24 * 60 * 60 * 1000); 	
									SuggestionDay +=grgagetmp;
									SuggestionRemark += SuggestionTmp.getPno() + "; ";
								}
							}
						}		
				}
			}
			SuggestionDay = Double.parseDouble(forma.format(SuggestionDay));
			UserUnAudit = null;
			
			List<ProjectDB> prjDB=kpiPointService.getDB();
			UserbugYanZhengtotal = 0;
			Userbugtotal = 0;
			UserModifybugtotal = 0;
			UserCaseExec = 0;
			UserCaseNewModify = 0;
			ProjectDB prjtmp;
			String UserId = "";
			UserId = kpiPointDao.getUserid(oldUsername);
			for(int i=0;i<prjDB.size();i++)
			{
				prjtmp = prjDB.get(i);
				if(!prjtmp.getPrjName().equals("对账系统"))
				{
					session.put("globalDB", prjDB.get(i));
					Userbugtotal += kpiPointDao.getBugtotal(UserId,oldMonth,session);
					UserbugYanZhengtotal += kpiPointDao.getBugYanZhengtotal(UserId,oldMonth,session);
					UserModifybugtotal += kpiPointDao.getModifyBugtotal(UserId,oldMonth,session);
					UserCaseExec += kpiPointDao.getCaseExec(UserId,oldMonth,session);
					UserCaseNewModify += kpiPointDao.getCaseNerMoify(UserId,oldMonth,session);	
				}
			}
				//default ATMC
				
			
			if(kpiPointService.getUserlevel(Usernametmp).equals("1"))
			{
				UserUnAudit = kpiPointService.getUserMeetingUnAudit_num(UserGroup_nametmp,Month_datetmp);////////
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
		KpiPoint oldSuggestion=kpiPointService.getCaseById(kpipoint.getId());
		String oldUsername=oldSuggestion.getUser_id();
		String oldGname=oldSuggestion.getGroup_name();
		String oldMonth=oldSuggestion.getMonth_date();
		double oldEdit_lock = oldSuggestion.getEdit_lock();
		String oldStatus = oldSuggestion.getStatus();
		String userGname = userModel.getGroupName();
		int headmanLevel = userModel.getLevel();
		double oldeditlock = oldSuggestion.getEdit_lock();
		if(oldeditlock == 1)
		{
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该KPI记录已提交，修改失败"}));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(1 == headmanLevel){
			if(!userGname.equals(oldGname))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"不能修改其他组的KPI记录"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}		
		}
		try {
		    kpipoint.setEdit_lock(oldeditlock);
			String ano=oldSuggestion.getPno();
			kpipoint.setUser_id(oldUsername);
			kpipoint.setGroup_name(oldGname);
			kpipoint.setMonth_date(oldMonth);
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			kpipoint.setModify_date(f.format(new Date()));
			kpipoint.setUpdate_man(modify_man);
			kpipoint.setEdit_lock(oldEdit_lock);
			kpipoint.setStatus(oldStatus);
			kpipoint.setPno(ano);
			kpipoint.setFirstedit("已修改");
			boolean flag=kpiPointService.update(kpipoint);
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
		try {			
				String from = request.getParameter("from");
				if(session.get("empNames")==null){
					unames=kpiPointService.getAllNames();
					session.put("empNames",unames );
					Map<String,String> umap=new HashMap<String,String>();
				//	Map<String,String> umaps=new HashMap<String,String>();
					for(String u:unames){
						umap.put(u, u);
					}
					session.put("empMap",umap );
				}
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
			Page page = kpiPointService.getPage(groupname, username,pageNum, pageSize, month);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
			
				  }
				if (from != null && from.equals("refresh")) {
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.kpipoint.domain.Perfor");
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
		List<Object> list=kpiPointService.getNames(groupName);
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
			kpipoint=kpiPointService.getCaseById(ids);
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

	public List<KpiPoint> getKpiPointList() {
		return KpiPointList;
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

	public void setKpiPointList(List<KpiPoint> KpiPointList) {
		this.KpiPointList = KpiPointList;
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


	public KpiPoint getKpiPoint() {
		return kpipoint;
	}

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
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

	public int getUserUnwriteNum() {
		return UserUnwriteNum;
	}

	public void setUserUnwriteNum(int userUnwriteNum) {
		UserUnwriteNum = userUnwriteNum;
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

	public KpiPointService getKpiPointService() {
		return kpiPointService;
	}

	public void setKpiPointService(KpiPointService kpiPointService) {
		this.kpiPointService = kpiPointService;
	}

	public KpiPoint getKpipoint() {
		return kpipoint;
	}

	public void setKpipoint(KpiPoint kpipoint) {
		this.kpipoint = kpipoint;
	}

	public String getUserheadmanAddValue() {
		return UserheadmanAddValue;
	}

	public void setUserheadmanAddValue(String userheadmanAddValue) {
		UserheadmanAddValue = userheadmanAddValue;
	}

	public String getUserfellowValue() {
		return UserfellowValue;
	}

	public void setUserfellowValue(String userfellowValue) {
		UserfellowValue = userfellowValue;
	}

	public String getUserTrainStride_num() {
		return UserTrainStride_num;
	}

	public void setUserTrainStride_num(String userTrainStride_num) {
		UserTrainStride_num = userTrainStride_num;
	}

	public String getUserTrainInner_num() {
		return UserTrainInner_num;
	}

	public void setUserTrainInner_num(String userTrainInner_num) {
		UserTrainInner_num = userTrainInner_num;
	}

	public KpiPointDao getKpiPointDao() {
		return kpiPointDao;
	}

	public void setKpiPointDao(KpiPointDao kpiPointDao) {
		this.kpiPointDao = kpiPointDao;
	}



	public int getUserModifybugtotal() {
		return UserModifybugtotal;
	}

	public void setUserModifybugtotal(int userModifybugtotal) {
		UserModifybugtotal = userModifybugtotal;
	}

	public int getUserCaseExec() {
		return UserCaseExec;
	}

	public void setUserCaseExec(int userCaseExec) {
		UserCaseExec = userCaseExec;
	}

	public int getUserCaseNewModify() {
		return UserCaseNewModify;
	}

	public void setUserCaseNewModify(int userCaseNewModify) {
		UserCaseNewModify = userCaseNewModify;
	}

	public int getUserbugYanZhengtotal() {
		return UserbugYanZhengtotal;
	}

	public void setUserbugYanZhengtotal(int userbugYanZhengtotal) {
		UserbugYanZhengtotal = userbugYanZhengtotal;
	}

	public int getUserbugtotal() {
		return Userbugtotal;
	}

	public void setUserbugtotal(int userbugtotal) {
		Userbugtotal = userbugtotal;
	}

	public List<ProblemOrSuggestion> getUnFixedSuggestion() {
		return UnFixedSuggestion;
	}

	public void setUnFixedSuggestion(List<ProblemOrSuggestion> unFixedSuggestion) {
		UnFixedSuggestion = unFixedSuggestion;
	}

	public String getSuggestionRemark() {
		return SuggestionRemark;
	}

	public void setSuggestionRemark(String suggestionRemark) {
		SuggestionRemark = suggestionRemark;
	}

	public double getSuggestionDay() {
		return SuggestionDay;
	}

	public void setSuggestionDay(double suggestionDay) {
		SuggestionDay = suggestionDay;
	}

}
