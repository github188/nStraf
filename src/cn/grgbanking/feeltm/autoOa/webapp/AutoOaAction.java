package cn.grgbanking.feeltm.autoOa.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import cn.grgbanking.feeltm.util.OaOrEmail;
import cn.grgbanking.feeltm.util.SendMail;
import org.json.JSONArray;
import org.json.JSONObject;



import cn.grgbanking.feeltm.domain.testsys.AutoOa;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.autoOa.service.AutoOaService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class AutoOaAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private AutoOaService autoOaService;
	
	private String description;
	private String sendCycle;
	private String start; //不用
	private String end;   //不用
    private AutoOa autoOas;
    private String update_man;
    private String createMan;
    private List<String> unames;
    private List<AutoOa> case1;
    private String embracer_manlist;
    private String weekDay;
	private String flag="0";
	
	private Map<String,String> map;
	private Map<String,String> embracerlist=new LinkedHashMap<String,String>();
	
	private TesterDetailDao testerDao;
  
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);	 
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	
		public String add(){
		//map=testerDao.getNameListBySuggestion();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		createMan=update_man;
		
		
		weekDay = getWeekOfDate(new Date());
        unames=autoOaService.getAllNames();
        embracerlist.put("王全胜", "王全胜");
        embracerlist.put("汤飞", "汤飞");
        for(String u:unames){
				embracerlist.put(u, u);
		}		
		
		return "add";
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
			//autoOas.setUpdate_man(userModel.getUsername());
			autoOas.setEmbracerMan(embracer_manlist.replace(",","、"));
			autoOas.setUpdateDate(new Date());
			autoOas.setCreateMan(userModel.getUsername());
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat g=new SimpleDateFormat("HH:mm:ss");
			if(autoOas.getEffectend() == null ||autoOas.getEffectend().equals(""))
			{
				autoOas.setEffectend(null);  /////////////////////////////给日期类型敷值,最大化时间表
			}
			if(autoOas.getSendDate()==null || autoOas.getSendDate().equals(""))
			{
				autoOas.setSendDate(g.format(new Date()));  /////////////////////////////给日期类型敷值,最大化时间表
			}
			flag=autoOaService.add(autoOas);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
				//采用多线程
				Thread aa=new Thread(new Runnable() {
					@Override
					public void run() {
						OaOrEmail oe=new OaOrEmail();
						oe.sendOaByNewAutoOa(autoOas);						
				}
				});
				aa.start();
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
					AutoOa temp = autoOaService.getCaseById(sids[i]);
					autoOaService.delete(temp);
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
			autoOas=autoOaService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return forwardPage;
	 }
	
	public String update() throws Exception {
		System.out.println("update");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		autoOas.setUpdateDate(new Date());
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat g=new SimpleDateFormat("HH:mm:ss");
		if(autoOas.getEffectend() == null ||autoOas.getEffectend().equals(""))
		{
			autoOas.setEffectend(null);  /////////////////////////////给日期类型敷值,最大化时间表
		}
		if(autoOas.getSendDate()==null || autoOas.getSendDate().equals(""))
		{
			autoOas.setSendDate(g.format(new Date()));  /////////////////////////////给日期类型敷值,最大化时间表
		}
		try {
			AutoOa oldautoOas=autoOaService.getCaseById(autoOas.getId());
			autoOas.setCreateDate(oldautoOas.getCreateDate());
			autoOas.setCreateMan(oldautoOas.getCreateMan());
			boolean flag=autoOaService.update(autoOas);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				if(!oldautoOas.getSendDate().equals(autoOas.getSendDate()))
				{
					Thread aa=new Thread(new Runnable() {
						@Override
						public void run() {
							OaOrEmail oe=new OaOrEmail();
							oe.sendOaByNewAutoOa(autoOas);						
					}
					});
					aa.start();
				}

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
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createMan = userModel.getUsername();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
				&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = autoOaService.getPage(createMan,description, sendCycle,start,pageNum, pageSize, end);

			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					  AutoOa tmp=(AutoOa)list.get(i);
						tmp.setCreateDateString(tmp.getCreateDate()==null?"":fo.format(tmp.getCreateDate()));
					     tmp.setUpdateDateString(tmp.getUpdateDate()==null?"":fo1.format(tmp.getUpdateDate()));
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.AutoOa");
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
			autoOas=autoOaService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getUpdate_man() {
		return update_man;
	}

	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}

	

	public AutoOaService getAutoOaService() {
		return autoOaService;
	}

	public void setAutoOaService(AutoOaService autoOaService) {
		this.autoOaService = autoOaService;
	}



	public AutoOa getCaseanalyse() {
		return autoOas;
	}

	public void setCaseanalyse(AutoOa autoOas) {
		this.autoOas = autoOas;
	}

	public List<AutoOa> getCase1() {
		return case1;
	}

	public void setCase1(List<AutoOa> case1) {
		this.case1 = case1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSendCycle() {
		return sendCycle;
	}

	public void setSendCycle(String sendCycle) {
		this.sendCycle = sendCycle;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public AutoOa getAutoOas() {
		return autoOas;
	}

	public void setAutoOas(AutoOa autoOas) {
		this.autoOas = autoOas;
	}

	public List<String> getUnames() {
		return unames;
	}

	public void setUnames(List<String> unames) {
		this.unames = unames;
	}

	public String getEmbracer_manlist() {
		return embracer_manlist;
	}

	public void setEmbracer_manlist(String embracer_manlist) {
		this.embracer_manlist = embracer_manlist;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public String getWeekDay() {
		return weekDay;
	}


	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}


	public Map<String, String> getEmbracerlist() {
		return embracerlist;
	}


	public void setEmbracerlist(Map<String, String> embracerlist) {
		this.embracerlist = embracerlist;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
