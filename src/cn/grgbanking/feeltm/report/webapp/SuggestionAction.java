package cn.grgbanking.feeltm.report.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.report.service.SuggestionService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.OaOrEmail;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class SuggestionAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private SuggestionService suggestionService;
	
	private String riseDate;
	private String raiseMan;
	private String status;
	private String category;
	private String pn;   //问题编号
	private String planFinishDate;
	private String summary;
	private String description;
	private String raiseEndDate;
    private ProblemOrSuggestion suggestion;
    private String update_man;
    private String resloveMan;
    private String nowdate;
    
    private List<ProblemOrSuggestion> suggestions;
	private String flag="0";
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
	public String add(){
		map=testerDao.getNameListBySuggestion();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		raiseMan=update_man;
		 /* Integer	 integer= userModel.getLevel();  //用level来判断  0 经理 1 主任 2 普通用户
	    if (integer==0){  
	    	 flag="1";
	     }*/
		 Map  role=userModel.getRole();     //用角色来判，是经理的可以开放
		if(role.containsValue("manager"))
		{
			flag="1";
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
			suggestion.setUpdate_man(userModel.getUsername());
			String no=suggestionService.getNextNo();
			suggestion.setPno(no);
			suggestion.setUpdate_date(new Date());
			flag=suggestionService.add(suggestion);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
				//采用多线程
				Thread aa=new Thread(new Runnable() {
					@Override
					public void run() {
						OaOrEmail oe=new OaOrEmail();
						oe.sendMailOaByNew(suggestion);						
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
					ProblemOrSuggestion temp = suggestionService.getSuggestionById(sids[i]);
					suggestionService.delete(temp);
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
		if(userModel.getLevel()==0){
			forwardPage="edit_manage";
		}else if(userModel.getLevel()==1){
			forwardPage="edit_headman";
		}
		System.out.println("yyyyy");
		try {
			String ids = request.getParameter("ids");
			suggestion=suggestionService.getSuggestionById(ids);
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
			ProblemOrSuggestion oldSuggestion=suggestionService.getSuggestionById(suggestion.getId());
			final String oldStatus=oldSuggestion.getStatus();
			final String newStatus=suggestion.getStatus();
			String pno=oldSuggestion.getPno();
			
			suggestion.setUpdate_date(new Date());
			suggestion.setUpdate_man(update_man);
			suggestion.setPno(pno);
			boolean flag=suggestionService.update(suggestion);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				
				
				//发送邮件并oa
				if(!oldStatus.equals(newStatus)){					
					//多线程发
					Thread aa=new Thread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							OaOrEmail oe=new OaOrEmail();
							if(!newStatus.equals("新建")){
								oe.sendMailOaByNewToOpen(suggestion);
							}							
						}
					});
					aa.start();
				}
				
				
				//添加发送邮件的程序，如状态为‘打开’，且为相关的处理人员时发送
//				SendMail sm=new SendMail();
//				StringBuffer sb=new StringBuffer();
//				
//				sm.sendMail("精细化管理："+suggestion.getSummary(), content, toAddress)
//				
//			flag=suggestionService.update(suggestion);
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
			Page page = suggestionService.getPage(riseDate, raiseMan,resloveMan, status, category,summary,description,pn,planFinishDate,pageNum, pageSize,raiseEndDate);
			request.setAttribute("currPage", page);
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
			nowdate = f.format(new Date());
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					ProblemOrSuggestion tmp=(ProblemOrSuggestion)list.get(i);
					tmp.setRaiseDateString(tmp.getRaise_date()==null?"":fo.format(tmp.getRaise_date()));
					tmp.setFinishing_dateString(tmp.getFinishing_date()==null?"":fo.format(tmp.getFinishing_date()));
					tmp.setPratical_dateString(tmp.getPratical_date()==null?"":fo.format(tmp.getPratical_date()));
				     tmp.setUpdateDateString(tmp.getUpdate_date()==null?"":fo1.format(tmp.getUpdate_date()));
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion");
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
			suggestion=suggestionService.getSuggestionById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String getRiseDate() {
		return riseDate;
	}

	public void setRiseDate(String riseDate) {
		this.riseDate = riseDate;
	}

	public String getRaiseMman() {
		return raiseMan;
	}

	public void setRaiseMman(String raiseMan) {
		this.raiseMan = raiseMan;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProblemOrSuggestion getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(ProblemOrSuggestion suggestion) {
		this.suggestion = suggestion;
	}
	public SuggestionService getSuggestionService() {
		return suggestionService;
	}
	public void setSuggestionService(SuggestionService suggestionService) {
		this.suggestionService = suggestionService;
	}
	public List<ProblemOrSuggestion> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<ProblemOrSuggestion> suggestions) {
		this.suggestions = suggestions;
	}
	public String getRaiseMan() {
		return raiseMan;
	}

	public void setRaiseMan(String raiseMan) {
		this.raiseMan = raiseMan;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNowdate() {
		return nowdate;
	}

	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	
	
}
