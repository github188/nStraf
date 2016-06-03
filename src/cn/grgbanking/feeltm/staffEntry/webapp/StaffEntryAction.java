package cn.grgbanking.feeltm.staffEntry.webapp;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlow;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlowCheckCondition;
import cn.grgbanking.feeltm.staffEntry.service.StaffEntryService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class StaffEntryAction extends BaseAction {
	@Autowired
	private StaffEntryService staffEntryService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	public SysUserGroupService userGroupService;
	
	@Autowired
	private ProjectService projectService;
	
	private OnBoardFlowCheckCondition checkCondition;
	private List<OnBoardFlowCheckCondition> checkConditionList;
	private OnBoardFlow entryInfo;
	
	public OnBoardFlowCheckCondition getCheckCondition() {
		return checkCondition;
	}
	public void setCheckCondition(OnBoardFlowCheckCondition checkCondition) {
		this.checkCondition = checkCondition;
	}
	public OnBoardFlow getEntryInfo() {
		return entryInfo;
	}
	public void setEntryInfo(OnBoardFlow entryInfo) {
		this.entryInfo = entryInfo;
	}
	public List<OnBoardFlowCheckCondition> getCheckConditionList() {
		return checkConditionList;
	}
	public void setCheckConditionList(
			List<OnBoardFlowCheckCondition> checkConditionList) {
		this.checkConditionList = checkConditionList;
	}


	/** 查询，跳转列表页或者个人信息页
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String searchContent=null;
			Date queryEntryStartTime=null;
			Date queryEntryEndTime=null;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0){
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			}
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0){
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if(StringUtils.isNotBlank(request.getParameter("searchContent"))){
				searchContent=request.getParameter("searchContent");
			}
			if (request.getParameter("queryEntryStartTime") != null && request.getParameter("queryEntryStartTime").length() > 0){
				queryEntryStartTime =new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("queryEntryStartTime").trim());
			}
			if (request.getParameter("queryEntryEndTime") != null && request.getParameter("queryEntryEndTime").length() > 0){
				queryEntryEndTime =new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("queryEntryEndTime").trim());
			}
			
			//没有权限的，只显示个人数据
			String curUserId=userModel.getUserid();
			boolean hasRight=hasQueryListRight(curUserId);
			
			//获取分页数据
			Page page = staffEntryService.getPage(hasRight,curUserId,entryInfo,searchContent,queryEntryStartTime,queryEntryEndTime,pageNum,pageSize);
			List<Object> list = (List<Object>) page.getQueryResult();
			
			request.setAttribute("currPage", page);
			ActionContext.getContext().put("entryInfoList", list);
			
			//ajax请求需要通过out.print方式返回结果（刷新页面或者点击查询时，只需要更新列表，而不更新查询条件，所以通过ajax方式刷新数据）
			if (from != null && from.equals("refresh")) {
				 
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				//原来的实现方式
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlow");
				org.json.JSONArray jsonObj = jsonUtil.toJSON(list, map);
		        //tring jsonObj = JSONArray.fromObject(list).toString(); 
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				ajaxPrint(input.toString());
				return null;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	/** 判断是否有查看入职信息列表权限
	 */
	private boolean hasQueryListRight(String userid) {
		/*//获取用户的组别列表
		List<UsrGroup> usrGroupList=userGroupService.getUserGropByUserId(userid);
		for(UsrGroup uGroup:usrGroupList){
			String groupCode=uGroup.getGrpcode();
			//读取配置文件(从viewConfig.properties中读取配置)
			String defaultHrGroup=Configure.getProperty("defaultHrGroup");
			defaultHrGroup=StringUtils.isBlank(defaultHrGroup)?"hrGroup":defaultHrGroup;
			if(defaultHrGroup.indexOf(groupCode)>=0){//配置文件中的组别包含了用户所在组别，则认为具有权限（这里的判断并不严谨，后续可完善）
				return true;
			}
		}
		return false;*/
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		return UserRoleConfig.ifHr(userModel);
	}
	/** 跳转到新增入职信息页面
	 * @return
	 */
	public String add() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		
		if(!hasQueryListRight(userModel.getUserid())){
			MsgBox msgBox = new MsgBox(request,getText("operator.addfaile",new String[]{"您不是人事组成员，不能添加入职信息!"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		//设置默认值
		entryInfo=new OnBoardFlow();
		entryInfo.setGrgBeginDate(Calendar.getInstance().getTime());
		entryInfo.setRegularDate(Calendar.getInstance().getTime());
		
		//获取默认检查条件
		List checkList=staffEntryService.getFolderCheckConditions(null);
		try{
			String checkListStr=JSONArray.fromObject(checkList).toString();
			String encodeCheckListStr=URLEncoder.encode(checkListStr,"utf-8");
			ActionContext.getContext().put("encodeCheckListStr", encodeCheckListStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.getSession().setAttribute("operate", "add");
		return "add";
	}
	
	//保存
	public String save() throws Exception {
		try {
			
			if(checkConditionList!=null){
				//去掉类型、项目、关联部门、状态全为空的行转换成的对象
				Iterator<OnBoardFlowCheckCondition> ite=checkConditionList.iterator();
				while(ite.hasNext()){
					OnBoardFlowCheckCondition con=ite.next();
					if(con==null || con.usefulFieldEmpty()){//有用的字段是否都为空
						ite.remove();
					}
				}
				if(checkConditionList!=null && checkConditionList.size()>0){
					//转为json
					String jsonStr=JSONArray.fromObject(checkConditionList).toString();
					entryInfo.setExtendStatus(jsonStr);//设置到对象中
				}
			}
			
			//操作人信息
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
//			entryInfo.setUserId(userModel.getUserid());
//			entryInfo.setUserName(userModel.getUsername());
			entryInfo.setUpdateMan(userModel.getUsername());
			entryInfo.setUpdateTime(Calendar.getInstance().getTime());
			//设置项目组别
			String groupName=projectService.getProjectNameByUserid(userModel.getUserid());
			entryInfo.setGroupName(StringUtils.isNotBlank(groupName)?groupName:"");
			//保存或者更新入职信息
			boolean flag=staffEntryService.saveOrUpdateStaffEntryInfo(entryInfo);
			if (flag == true) {
				MsgBox msgBox;
				if("add".equals(request.getSession().getAttribute("operate"))){
					msgBox = new MsgBox(request, getText("add.ok"));					
				}else if("update".equals(request.getSession().getAttribute("operate"))){
					msgBox = new MsgBox(request, "修改成功");
				}else{
					msgBox = new MsgBox(request, getText("save.ok"));
				}
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("save.ok"));
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
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	//更新
	public String update() throws Exception {
		request.getSession().setAttribute("operate", "update");
		return save();
	}
	
	//跳转到修改页面
	public String edit() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String ids = request.getParameter("ids");
		//获取默认检查条件
		entryInfo=staffEntryService.getCaseById(ids);
		
		if(!(hasQueryListRight(userModel.getUserid())||entryInfo.getUserId().equals(userModel.getUserid()))){
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不是人事组成员，不能修改该入职信息"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		try{
			if(entryInfo.getExtendStatus()!=null){
				//修改时不从模版中解析，而是从入职表里面查询
				String jsonStr= entryInfo.getExtendStatus();
				checkConditionList=(List) JSONArray.toList(JSONArray.fromObject(jsonStr),OnBoardFlowCheckCondition.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "edit";
	}
	//删除
	public String delete() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if(!(hasQueryListRight(userModel.getUserid()))){
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不是人事组成员，不能删除入职信息!"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			String ids = request.getParameter("ids");
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					OnBoardFlow temp = staffEntryService.getCaseById(sids[i]);
					staffEntryService.delete(temp);
					iCount++;
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule", new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operInfoform.updatefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 
	
	/** 显示指定id的入职信息
	 * @return
	 */
	public String showExtendStatus(){
		OnBoardFlow staffEntry= staffEntryService.getCaseById(request.getParameter("staffEntryId"));
		String jsonStr= staffEntry.getExtendStatus();
		List<OnBoardFlowCheckCondition> conditionList=(List) JSONArray.toList(JSONArray.fromObject(jsonStr),OnBoardFlowCheckCondition.class);
		request.setAttribute("conditionList", conditionList);
		return "showExtendStatus";
	}
	
	/** 向ajax请求返回数据
	 * @param str
	 */
	private void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
