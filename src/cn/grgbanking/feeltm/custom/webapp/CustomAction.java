package cn.grgbanking.feeltm.custom.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.custom.service.CustomService;
import cn.grgbanking.feeltm.domain.Custom;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings( { "serial", "unchecked" })
public class CustomAction extends BaseAction implements ModelDriven<Custom> {
	@Autowired
	private CustomService customService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SysUserInfoService sysUserInfoService;
	
	private Custom custom = new Custom();
	private List<Custom> list = new ArrayList<Custom>();
	public String pageNum;
	public String pageSize;
	private List<String> groupNameList = new ArrayList<String>();


	/**
	 * 查询所有客户管理信息，带有分页
	 * @return
	 * @throws Exception
	 */
	public String listAll() throws Exception{
		//初始化下拉列表
		initSelectData();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		int pagenum = 1;
		int pagesize = 20;
		if(pageNum!=null && !pageNum.equals("")){
			pagenum = Integer.parseInt(pageNum);
		}
		if(pageSize!=null && !pageSize.equals("")){
			pagesize = Integer.parseInt(pageSize);
		}
		boolean hasRight = isManagerOrNot(userModel.getUserid());
		Page page = customService.getPage(pagenum, pagesize,hasRight);
		request.setAttribute("currPage", page);
		list = page.getQueryResult();
		ActionContext.getContext().put("customlist", list);
		request.getSession().setAttribute("custominfo.menuid",  request.getParameter("menuid"));
		return "listsuccess";
	}
	
	/**
	 * 根据查询条件查询
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			int pagenum = 1;
			int pagesize = 20;
			String creatEndDate = null;
			if (pageNum!= null&&pageNum.length() > 0)
				pagenum = Integer.parseInt(pageNum);
			if (pageSize!= null&&pageSize.length() > 0)
				pagesize = Integer.parseInt(pageSize);
			if (request.getParameter("creatEndDate") != null && request.getParameter("creatEndDate").length() > 0)
				creatEndDate = request.getParameter("creatEndDate");
			
			//根据查询条件查询数据
			boolean hasRight = isManagerOrNot(userModel.getUserid());
			Page page = customService.getPageByCondition(custom, pagenum, pagesize,hasRight,creatEndDate);
			request.setAttribute("currPage", page);
			List list = (List<Object>)page.getQueryResult();
			
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.Custom");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);//将查询数据转换为jsonArray，
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
				input.put("jsonObj", jsonObj);		//将数据以json的方式传入前台						
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("customlist", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	/**
	 * 判断是否为部门经理以上
	 * @param userId
	 * @return
	 */
	private boolean isManagerOrNot(String userId){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		return UserRoleConfig.upDeptManager(userModel);
		
	}
	/**
	 * 跳转到添加通讯录信息页面
	 * @return
	 */
	public String add(){
		//初始化下拉列表
		initSelectData();
		return "add";
	}
	
	/**
	 * 查看详情页面
	 * @return
	 */
	public String view(){
		try{
			String customId = request.getParameter("customId");
			 list = customService.getListCustomById(customId);
			ActionContext.getContext().put("customList", list);
			
		}catch (Exception e){
			MsgBox msgBox = new MsgBox(request,"查看详情失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		return "view";
	}
	
	/**
	 * 保存添加的信息
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {

			Date date = new Date();
			custom.setUpDate(date);
			custom.setUpdateMan(userModel.getUsername());
				boolean flag = customService.addCustomInfo(custom);
				if (flag == true) {		//添加是否成功
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, custom.getUpdateMan());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " add a Custom : " + custom.getUpdateMan());
					msgBox = new MsgBox(request, getText("add.ok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
				} else {
					msgBox = new MsgBox(request,
							getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.addfaile"));
				}
			
		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request,
					"error in (CustomAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a UserCustom : " + custom.getUpdateMan());
			SysLog.error(e);

			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		return "msgBox";
	}
	/**
	 * 删除信息
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		System.out.println("删除报销信息... ");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String[] delItem = StringUtils.split(request.getParameter("ids"), ",");
			int iCount = 0;
			for (int i = 0; i < delItem.length; i++) {
				Custom cu = new Custom();
				cu = customService.getCustomById(delItem[i]);
				if(!cu.getUpdateMan().equals(userModel.getUsername())){
					MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"没有权限删除此记录！"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				else{
				customService.deleteCustomInfo(delItem[i]);
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
	/**
	 * 修改报销单
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{ 
		System.out.println("update()方法修改保存中....");
		try{
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			custom.setUpdateMan(userModel.getUsername());
			custom.setUpDate(Calendar.getInstance().getTime());
		boolean flag = customService.updateCustomInfo(custom);
		if(flag==true){
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updateok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		}catch (Exception e){
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		
		return "msgBox";
	}
	
	/**
	 * 进入编辑数据页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		//初始化下拉列表
		initSelectData();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String Id = request.getParameter("id");
			custom =customService.getCustomById(Id);
			if(custom.getUpdateMan().equals(userModel.getUsername())){
				request.setAttribute("Id", Id);
				return "edit";
			}
			else{
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"没有权限修改此记录！"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	 }
	/**
	 * 初始化下拉列表
	 * */
	private void initSelectData(){
		//获取项目名称
		List<Project> prjGroupList = projectService.listAllGroup();
		for(Iterator<Project> it = prjGroupList.iterator();it.hasNext();){
			groupNameList.add(it.next().getName());
		}
	}
	
	public CustomService getCustomService() {
		return customService;
	}



	public void setCustomService(CustomService customService) {
		this.customService = customService;
	}



	public Custom getCustom() {
		return custom;
	}



	public void setCustom(Custom custom) {
		this.custom = custom;
	}



	public List<Custom> getList() {
		return list;
	}



	public void setList(List<Custom> list) {
		this.list = list;
	}



	public String getPageNum() {
		return pageNum;
	}



	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}



	public String getPageSize() {
		return pageSize;
	}



	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}



	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}



	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}



	public Custom getModel() {
		return custom;
	}

	public List<String> getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(List<String> groupNameList) {
		this.groupNameList = groupNameList;
	}

}
