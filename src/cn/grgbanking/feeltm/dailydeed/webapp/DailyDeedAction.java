package cn.grgbanking.feeltm.dailydeed.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.dailydeed.service.DailyDeedService;
import cn.grgbanking.feeltm.domain.testsys.DailyDeed;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class DailyDeedAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private DailyDeedService dailyDeedService;
	
	private DailyDeed dailydeed;
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
    private List<String> unames;
    
    private List<DailyDeed> DailyDeedList;
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
	
	
	
	
	public String up() throws Exception {
		System.out.println("up");
		boolean flag = false;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		
		String ids = request.getParameter("ids");
		dailydeed=dailyDeedService.getCaseById(ids);
		if(dailydeed.getEdit_lock() == 1)
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"该记录已提交"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(dailydeed.getUser_id().equals(modify_man) && !dailydeed.getUser_id().equals("冯天桂"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"你没有权限提交该记录"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(!dailydeed.getGroup_name().equals(userModel.getGroupName())&& !dailydeed.getUser_id().equals("冯天桂"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"你没有权限提交该记录"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		try {
			dailydeed.setEdit_lock(1);
			flag=dailyDeedService.update(dailydeed);
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
				oldmonth_date = dailyDeedService.getUserMonth_date(dailydeed.getMonth_date());
				if(oldmonth_date == true)
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该月度日常行为已创建，新增失败"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}

				String no= "";
				
				unames=dailyDeedService.getAllNames();
			
				for(String mm:unames){
					no = dailyDeedService.getNextNo();
					dailydeed.setPno(no);
					dailydeed.setUpdate_man(userModel.getUsername());	
					dailydeed.setGroup_name(dailyDeedService.getUserGroup_name(mm.trim()));
					dailydeed.setUser_id(mm.trim());

					SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					dailydeed.setModify_date(f.format(new Date()));
					//performance.setMonth_date(month_date.trim());
					flag=dailyDeedService.add(dailydeed);
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
					DailyDeed temp = dailyDeedService.getCaseById(sids[i]);
					dailyDeedService.delete(temp);
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
			dailydeed=dailyDeedService.getCaseById(ids);
			
			//写获取数量语句
			
			//Performance oldSuggestion=performanceService.getCaseById(performance.getId());
			String oldUsername=dailydeed.getUser_id();
			String oldGname=dailydeed.getGroup_name();
			String oldMonth=dailydeed.getMonth_date();
			String userGname = userModel.getGroupName();
			int headmanLevel = userModel.getLevel();
			double oldeditlock = dailydeed.getEdit_lock();

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

				if(userModel.getUsername().equals(oldUsername))
				{
					forwardPage = "edit";
				}
				else
				{
					forwardPage = "edit_headman";
				}	
			}
			if(0 == headmanLevel){
				if(dailyDeedService.getUserlevel(oldUsername).equals("1"))
				{
					forwardPage = "edit_manage2";
				}
				else
				{
					forwardPage = "edit_manage";
				}
				
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
		DailyDeed oldSuggestion=dailyDeedService.getCaseById(dailydeed.getId());
		String oldUsername=oldSuggestion.getUser_id();
		String oldGname=oldSuggestion.getGroup_name();
		String oldMonth=oldSuggestion.getMonth_date();
		double oldEdit_lock = oldSuggestion.getEdit_lock();
		String userGname = userModel.getGroupName();
		int headmanLevel = userModel.getLevel();
		double oldeditlock = oldSuggestion.getEdit_lock();
		if(oldeditlock == 1)
		{
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"该素质与能力记录已提交，修改失败"}));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		if(1 == headmanLevel){
			if(!userGname.equals(oldGname))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"不能修改其他组的素质与能力记录"}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}		
		}
		try {
			dailydeed.setEdit_lock(oldeditlock);
			String ano=oldSuggestion.getPno();
			dailydeed.setUser_id(oldUsername);
			dailydeed.setGroup_name(oldGname);
			dailydeed.setMonth_date(oldMonth);
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dailydeed.setModify_date(f.format(new Date()));
			dailydeed.setUpdate_man(modify_man);
			dailydeed.setEdit_lock(oldEdit_lock);
			dailydeed.setPno(ano);
		//	stufftAndAbility.setFirstedit("已修改");
			boolean flag=dailyDeedService.update(dailydeed);
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
	}
    
	
	public String query() throws Exception {
		System.out.println("query");
		try {			
				String from = request.getParameter("from");
				if(session.get("empNames")==null){
					unames=dailyDeedService.getAllNames();
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
			Page page = dailyDeedService.getPage(groupname, username,pageNum, pageSize, month);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
			
				  }
				if (from != null && from.equals("refresh")) {
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.stufftandability.domain.Perfor");
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
		List<Object> list=dailyDeedService.getNames(groupName);
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
			dailydeed=dailyDeedService.getCaseById(ids);
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



	public void setModify_man(String modify_man) {
		this.modify_man = modify_man;
	}

	public String getMonth(){
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUser_id(){
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getMonth_date() {
		return month_date;
	}

	public void setMonth_date(String month_date) {
		this.month_date = month_date;
	}

	public DailyDeedService getDailyDeedService() {
		return dailyDeedService;
	}


	public void setDailyDeedService(DailyDeedService dailyDeedService) {
		this.dailyDeedService = dailyDeedService;
	}
	public DailyDeed getDailydeed() {
		return dailydeed;
	}

	public void setDailydeed(DailyDeed dailydeed) {
		this.dailydeed = dailydeed;
	}

	public List<DailyDeed> getDailyDeedList() {
		return DailyDeedList;
	}

	public void setDailyDeedList(List<DailyDeed> dailyDeedList) {
		DailyDeedList = dailyDeedList;
	}

}
