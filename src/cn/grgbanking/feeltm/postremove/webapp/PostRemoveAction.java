package cn.grgbanking.feeltm.postremove.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.PostRemove;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.postremove.service.PostRemoveService;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class PostRemoveAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private PostRemoveService postremoveService;
	private String name;
	private String removedate;
	private String status;
    private PostRemove postremove;
    private String update_man;
    private String createMan;
    private List<String> unames;
    private List<PostRemove> case1;
    private String embracer_manlist;
	private String flag="0";
	
	private int level;
	private String editwirte;
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;

	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		createMan=update_man;
		return "add";
	}
	
	public String getFlag(){
		return flag;
	}

	public void setFlag(String flag){
		this.flag = flag;
	}

	public String save() throws Exception {     //保存suggestion概况的数据		
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
			postremove.setUpdateman(userModel.getUsername());			
			postremove.setUpdatedate(format.format(new Date()));
			postremove.setLocktag("flase");
			flag=postremoveService.add(postremove);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
				//采用多线程
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
					PostRemove temp = postremoveService.getCaseById(sids[i]);
					postremoveService.delete(temp);
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
		level = userModel.getLevel();
		editwirte = userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			postremove=postremoveService.getCaseById(ids);
			if(!postremove.getStatus().equals("未提交"))
			{
				forwardPage = "edit_up";
			}
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
		postremove.setUpdateman(userModel.getUsername());
		postremove.setUpdatedate(format.format(new Date()));
		try {
			boolean flag=postremoveService.update(postremove);
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

	public String UpAuditing() throws Exception {
		System.out.println("uplock");
		boolean flag = false;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String ids = request.getParameter("ids");
		PostRemove oldpost= postremoveService.getCaseById(ids);
		if(oldpost.getLocktag().equals("true"))
		{
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile",new String[]{"该轮岗计划已提交"}));
			addActionMessage(getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		try {
			oldpost.setLocktag("true");
			oldpost.setStatus("已提交");
			flag = postremoveService.update(oldpost);
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
			Page page = postremoveService.getPage(name,removedate,pageNum, pageSize);

			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					  PostRemove tmp=(PostRemove)list.get(i);
					//	tmp.setCreateDateString(tmp.getCreateDate()==null?"":fo.format(tmp.getCreateDate()));
					  //   tmp.setUpdateDateString(tmp.getUpdateDate()==null?"":fo1.format(tmp.getUpdateDate()));
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.PostRemove");
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
			postremove=postremoveService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String getUpdate_man() {
		return update_man;
	}

	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}

	public PostRemoveService getPostRemoveService() {
		return postremoveService;
	}

	public void setPostRemoveService(PostRemoveService postremoveService) {
		this.postremoveService = postremoveService;
	}

	public PostRemove getPostRemove() {
		return postremove;
	}

	public void setPostRemove(PostRemove postremove) {
		this.postremove = postremove;
	}

	public List<PostRemove> getCase1() {
		return case1;
	}

	public void setCase1(List<PostRemove> case1) {
		this.case1 = case1;
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



	public PostRemoveService getPostremoveService() {
		return postremoveService;
	}


	public void setPostremoveService(PostRemoveService postremoveService) {
		this.postremoveService = postremoveService;
	}


	public PostRemove getPostremove() {
		return postremove;
	}


	public void setPostremove(PostRemove postremove) {
		this.postremove = postremove;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemovedate() {
		return removedate;
	}

	public void setRemovedate(String removedate) {
		this.removedate = removedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getEditwirte() {
		return editwirte;
	}

	public void setEditwirte(String editwirte) {
		this.editwirte = editwirte;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
