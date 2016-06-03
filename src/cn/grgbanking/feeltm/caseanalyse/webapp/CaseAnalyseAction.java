package cn.grgbanking.feeltm.caseanalyse.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



import cn.grgbanking.feeltm.domain.testsys.CaseAnalyse;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.caseanalyse.service.CaseAnalyseService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class CaseAnalyseAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private CaseAnalyseService caseAnalyseService;
	
	private String createDate;
	private String createMan;
//	private String status;
	private String category;
	private String pn;   //问题编号
	private String planFinishDate; //不用
	private String raiseEndDate;   //不用
    private CaseAnalyse caseanalyse;
    private String update_man;
    private String resloveMan;
    private	String summary;
    
    private List<CaseAnalyse> case1;
	private String flag="0";
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
		public String add(){
		map=testerDao.getNameListBySuggestion();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		createMan=update_man;
		 // Integer	 integer= userModel.getLevel();  //用level来判断  0 经理 1 主任 2 普通用户
	  //  if (integer==0){  
	   // 	 flag="1";
	   //  }
	     
		// Map  role=userModel.getRole();     //用角色来判，是经理的可以开放
		//if(role.containsValue("manager"))
		//{
		//	flag="1";
		//}
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
			caseanalyse.setUpdate_man(userModel.getUsername());
			String no=caseAnalyseService.getNextNo();
			caseanalyse.setPno(no);
			caseanalyse.setUpdate_date(new Date());
			flag=caseAnalyseService.add(caseanalyse);
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
					CaseAnalyse temp = caseAnalyseService.getCaseById(sids[i]);
					caseAnalyseService.delete(temp);
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
			caseanalyse=caseAnalyseService.getCaseById(ids);
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
		update_man=userModel.getUsername();
		try {
			CaseAnalyse oldSuggestion=caseAnalyseService.getCaseById(caseanalyse.getId());
		//	final String oldStatus=oldSuggestion.getStatus();
		//	final String newStatus=suggestion.getStatus();
			String ano=oldSuggestion.getPno();
			
			caseanalyse.setUpdate_date(new Date());
			caseanalyse.setUpdate_man(update_man);
			caseanalyse.setPno(ano);
			boolean flag=caseAnalyseService.update(caseanalyse);
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
			Page page = caseAnalyseService.getPage(createDate, createMan, summary, category,pageNum, pageSize, raiseEndDate);
			//createDate, createMan, update_man, category, pn,pageNum, pageSize, raiseEndDate
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					  CaseAnalyse tmp=(CaseAnalyse)list.get(i);
					tmp.setCreateDateString(tmp.getCreate_date()==null?"":fo.format(tmp.getCreate_date()));
				     tmp.setUpdateDateString(tmp.getUpdate_date()==null?"":fo1.format(tmp.getUpdate_date()));
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.CaseAnalyse");
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
			caseanalyse=caseAnalyseService.getCaseById(ids);
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

	public String getUpdate_man() {
		return update_man;
	}

	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}

	public String getResloveMan() {
		return resloveMan;
	}

	public void setResloveMan(String resloveMan) {
		this.resloveMan = resloveMan;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getPlanFinishDate() {
		return planFinishDate;
	}

	public void setPlanFinishDate(String planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	public String getRaiseEndDate() {
		return raiseEndDate;
	}

	public void setRaiseEndDate(String raiseEndDate) {
		this.raiseEndDate = raiseEndDate;
	}

	public CaseAnalyseService getCaseAnalyseService() {
		return caseAnalyseService;
	}

	public void setCaseAnalyseService(CaseAnalyseService caseAnalyseService) {
		this.caseAnalyseService = caseAnalyseService;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public CaseAnalyse getCaseanalyse() {
		return caseanalyse;
	}

	public void setCaseanalyse(CaseAnalyse caseanalyse) {
		this.caseanalyse = caseanalyse;
	}

	public List<CaseAnalyse> getCase1() {
		return case1;
	}

	public void setCase1(List<CaseAnalyse> case1) {
		this.case1 = case1;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
