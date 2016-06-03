package cn.grgbanking.feeltm.costControl.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import cn.grgbanking.feeltm.costControl.domain.DateIncomeManage;
import cn.grgbanking.feeltm.costControl.services.DateIncomeManageService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;


public class DateIncomeManageAction  extends BaseAction{
	@Autowired
	private DateIncomeManageService  dateIncomeManageService;
	@Autowired
	private ProjectService service;
	
	@Autowired
	private StaffInfoService staffInfoService;
	private String queryPrj;//查询的项目组
	private DateIncomeManage dateincomeManage;
	private String from;
	private List<DateIncomeManage> dateincomelist;
	

	public  String list(){
		try {
				List<Project>  pro=service.listAllGroup();
				if(pro!=null){
					request.setAttribute("pro", pro);
				}
			// 获取当前用户
				UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
				int pageNum = 1;
				int pageSize = 20;
				request.setCharacterEncoding("UTF-8");
				queryPrj=request.getParameter("queryPrj");
				// 分页
				if (request.getParameter("pageSize") != null
						&& request.getParameter("pageSize").length() > 0) {
					pageSize = Integer.parseInt(request.getParameter("pageSize"));
				}
				if (request.getParameter("pageNum") != null
						&& request.getParameter("pageNum").length() > 0)
					pageNum = Integer.parseInt(request.getParameter("pageNum"));
				
				Page page = dateIncomeManageService.getDateIncomePage(queryPrj,pageNum,pageSize);
				request.setAttribute("currPage", page);
				List<Object> list = (List<Object>)page.getQueryResult();
				
				if (from != null && from.equals("refresh")) {
					
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					//公司的json转换
					JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.costControl.bean.DateIncomeManageBean");
					JSONArray jsonObj = jsonUtil.toJSON(list, map);
	
					JSONObject input = new JSONObject();
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);								
					PrintWriter out = response.getWriter();
					out.print(input);
					return null;
				} 
					request.setAttribute("dateincomelist", list);
					return "list";
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return "list";
	}
	public void refresh() {
		System.out.println("refesh");
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	
	
	public  String add(){
		List<Project> pro;
		try {
			pro = service.listAllGroup();
			if(pro!=null){
				request.setAttribute("pro", pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			return "add";
		}
	
	/**
	 * 保存新增的数据
	 * @return
	 */
	public String save(){
		String msg = "";	
		try {
			
			Date time=new Date();
			dateincomeManage.setEntryTime(time);
			// 获取当前登录用户
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr = staffInfoService.findUserByUserid(userModel.getUserid());
			String curuser = usr.getUsername().trim();
			//将当前的登录用户设置为录入人
			dateincomeManage.setEntryPeople(curuser);
			int count=0;//标记新增记录的条数；
			for(int i=0;i<dateincomelist.size();i++){
				DateIncomeManage incomelist=dateincomelist.get(i);
				String  prjGrounpId="";
				String prjGrounp=incomelist.getPrjGroup();
				List<Project> list=service.getProjectIdByName(prjGrounp);
				prjGrounpId=list.get(0).getId();
				dateincomeManage.setProjectId(prjGrounpId);
				dateincomeManage.setPrjGroup(prjGrounp);
				Double income=incomelist.getDateIncome();
				dateincomeManage.setDateIncome(income);
				boolean flag=dateIncomeManageService.save(dateincomeManage);
				if(flag){
					count++;
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("成功新增"+count+"条数据") + msg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("添加数据失败") + msg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
	}
	
	/**
	 * 从list页面跳转到修改页面
	 * @return
	 */
	public  String update(){
		
		try {
			List<Project>  pro=service.listAllGroup();
			if(pro!=null){	
				request.setAttribute("pro", pro);
				
			}
			String ids=request.getParameter("ids");
			List<DateIncomeManage>  list=dateIncomeManageService.getDateIncomeById(ids);
			if(list!=null){
				request.setAttribute("income", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modify";
	}
	
	/**
	 * 保存修改
	 * @return
	 */
	public String saveModify(){
		String msg = "";
		try {
			Date time=new Date();
			dateincomeManage.setEntryTime(time);
			// 获取当前登录用户
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr = staffInfoService.findUserByUserid(userModel.getUserid());
			String curuser = usr.getUsername().trim();// 获取当前登录用户
			dateincomeManage.setEntryPeople(curuser);
			String ids=dateincomeManage.getDateIncomeId();
			List<DateIncomeManage>  list=dateIncomeManageService.getDateIncomeById(ids);
			String prjGrounp=dateincomeManage.getPrjGroup();
			String  prjGrounpId="";
			//根据项目组的名称查找项目的id
			List<Project> listid=service.getProjectIdByName(prjGrounp);
			prjGrounpId=listid.get(0).getId();
			dateincomeManage.setProjectId(prjGrounpId);
			String incomeInfoman="";//录入人
			if(list!=null){
				for(DateIncomeManage  dm:list){
					incomeInfoman=dm.getEntryPeople();
				}
				
			}
			//当前登陆用户与与该条记录的记录人相同  则允许修改数据  否则不允许修改数据
			if(curuser.equals(incomeInfoman)){
				boolean flag = dateIncomeManageService.update(dateincomeManage);
				if(flag){
					MsgBox msgBox = new MsgBox(request, getText("修改此数据成功") + msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}else{
				MsgBox msgBox = new MsgBox(request, getText("您不能修改此数据") + msg);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return "msgBox";
	}
	
	
/**
 * 删除数据
 * @return
 */
	public  String del(){
		String msg = "";
		try {
			// 获取当前登录用户
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr = staffInfoService.findUserByUserid(userModel.getUserid());
			String curuser = usr.getUsername().trim();// 获取当前登录用户
			//将jsp页面传过来的 id字符串   分割成一个数组
			String[] delItem=request.getParameter("ids").split(",");
				int count=0;
				for (int i = 0; i < delItem.length; i++) {
					String incomeid=delItem[i];
					List<DateIncomeManage>  list=dateIncomeManageService.getDateIncomeById(incomeid);
					String incomeInfoman="";
					if(list!=null){
						for(DateIncomeManage  dm:list){
							incomeInfoman=dm.getEntryPeople();
						}
						
					}
					
					//只有当前的登录者与 该条数据的 录入人相同时  则允许删除数据
					if(incomeInfoman.equals(curuser)){
						boolean flag=dateIncomeManageService.delDateIncomeById(incomeid);
						if (flag = true) {
							count++;
						}
					}else{
						MsgBox msgBox = new MsgBox(request, getText("您没有删除该数据的权限") + msg);
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					
				}
				MsgBox msgBox = new MsgBox(request, getText("成功删除"+count+"数据") + msg);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("删除数据失败")
					+ msg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		
	}
	
	
	
	public String getQueryPrj() {
		return queryPrj;
	}
	public void setQueryPrj(String queryPrj) {
		this.queryPrj = queryPrj;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public DateIncomeManage getDateincomeManage() {
		return dateincomeManage;
	}


	public void setDateincomeManage(DateIncomeManage dateincomeManage) {
		this.dateincomeManage = dateincomeManage;
	}
	
	public List<DateIncomeManage> getDateincomelist() {
		return dateincomelist;
	}
	public void setDateincomelist(List<DateIncomeManage> dateincomelist) {
		this.dateincomelist = dateincomelist;
	}
	

}
