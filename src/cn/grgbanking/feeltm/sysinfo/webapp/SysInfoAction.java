package cn.grgbanking.feeltm.sysinfo.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.SysInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.sysinfo.service.SysInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class SysInfoAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private SysInfoService sysInfoService;
	
	private String modify_man;
	private String username;
//	private String status;
	private String groupname;
	private String status;   //问题编号
    private SysInfo sysInfo;

    
    private List<SysInfo> case1;
	private String flag="0";
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
		public String add(){
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			modify_man=userModel.getUsername();
			boolean l_bExitRecrd;
			l_bExitRecrd = false;
			l_bExitRecrd = sysInfoService.getExistRecord(modify_man);
			if(l_bExitRecrd)
			{
				MsgBox msgBox1 = new MsgBox(request,getText("新增失败,该员工已经添加档案信息! ",new String[]{"该员工已经添加档案信息! "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
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
			flag=sysInfoService.add(sysInfo);
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
					SysInfo temp = sysInfoService.getCaseById(sids[i]);
					sysInfoService.delete(temp);
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
			sysInfo=sysInfoService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		if(userModel.getLevel() != 0)
		{
			if(!sysInfo.getUsername().equals(userModel.getUsername()))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您没有修改权限 "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		return forwardPage;
	 }
	
	public String update() throws Exception {
		System.out.println("update");
		//UserModel userModel = (UserModel) request.getSession().getAttribute(
		//		Constants.LOGIN_USER_KEY);
		//update_man=userModel.getUsername();
		try {
			SysInfo oldSuggestion=sysInfoService.getCaseById(sysInfo.getId());
		//	final String oldStatus=oldSuggestion.getStatus();
		//	final String newStatus=suggestion.getStatus();
			//String ano=oldSuggestion.getPno();
			
			//caseanalyse.setUpdate_date(new Date());
			//caseanalyse.setUpdate_man(update_man);
			//caseanalyse.setPno(ano);
			boolean flag=sysInfoService.update(sysInfo);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				
				
				//发送邮件并oa
			//	if(!oldStatus.equals(newStatus)){	
					/*
					//多线程发
					Thread aa=new Thread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							OaOrEmail oe=new OaOrEmail();
							if(newStatus.equals("不解决")||newStatus.equals("已解决")){
								oe.sendOaOnly(suggestion);
							}else if(newStatus.equals("打开")){
								oe.sendMailOaByNewToOpen(suggestion);
							}							
						}
					});
					aa.start();*/
				}
				
				
				//添加发送邮件的程序，如状态为‘打开’，且为相关的处理人员时发送
//				SendMail sm=new SendMail();
//				StringBuffer sb=new StringBuffer();
//				
//				sm.sendMail("精细化管理："+suggestion.getSummary(), content, toAddress)
//				
//			flag=suggestionService.update(suggestion);
		//	} else {
			//	MsgBox msgBox = new MsgBox(request,
				//		getText("operInfoform.updatefaile"));
			//	msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		//	}
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
			Page page = sysInfoService.getPage(username,groupname,status,pageNum, pageSize);
			//createDate, createMan, update_man, category, pn,pageNum, pageSize, raiseEndDate
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.sysinfo.domain.Perfor");
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
		List<Object> list=sysInfoService.getNames(groupName);
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

	public SysInfoService getSysInfoService() {
		return sysInfoService;
	}

	public void setSysInfoService(SysInfoService sysInfoService) {
		this.sysInfoService = sysInfoService;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModify_man() {
		return modify_man;
	}

	public void setModify_man(String modify_man) {
		this.modify_man = modify_man;
	}

	public SysInfo getSysInfo() {
		return sysInfo;
	}

	public void setSysInfo(SysInfo sysInfo) {
		this.sysInfo = sysInfo;
	}

	public List<SysInfo> getCase1() {
		return case1;
	}

	public void setCase1(List<SysInfo> case1) {
		this.case1 = case1;
	}

	public String show() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String ids = request.getParameter("ids");
			sysInfo=sysInfoService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		if(userModel.getLevel() != 0)
		{
			if(!sysInfo.getUsername().equals(userModel.getUsername()))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您没有查看权限 "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		return "show";
	}
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
