package cn.grgbanking.feeltm.instance.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Instance;
import cn.grgbanking.feeltm.instance.service.InstanceService;
import cn.grgbanking.feeltm.integralCenter.service.IntegralCenterService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class InstanceAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private InstanceService instanceService;
	
	private String createDate;
	private String createMan;
	private String upMan;
	//private String status;
	private String category;
	private String pn;   //问题编号
	private String planFinishDate; //不用
	private String raiseEndDate;   //不用
    private Instance instance;
    private String update_man;
    private String resloveMan;
    private	String summary;
    private String anon_flag;
    private String status_flag;
    private String oaemail_flag;
    private String embracer_manlist;
    private Map<String,String> embracerlist=new LinkedHashMap<String,String>();
    
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	
	@Autowired
	private IntegralCenterService integralCenterService;
	
	
    private List<Instance> case1;
	private String flag="0";
	
	private List<String> unames;
	
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
		public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		createMan=update_man;      
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
			SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
			unames=instanceService.getAllNames();
			instance.setCreate_manId(userModel.getUserid());//创建人ID
			instance.setUpdate_man(userModel.getUsername());
			instance.setUp_man(userModel.getUsername());
			//去掉接受者最后的逗号
//			String embracer_man = instance.getEmbracer_man();
//			if (StringUtils.isNotBlank(embracer_man)) {
//				instance.setEmbracer_man(embracer_man.substring(0, embracer_man.length()-1));
//			}
			String no=instanceService.getNextNo();
			instance.setIno(no);
			instance.setUpdate_date(new Date());
		//	instance.setEmbracer_man(embracer_manlist.replace(",","、"));			
			if(anon_flag!=null && anon_flag.equals("1"))
			{
				instance.setAnon("匿名");
				//instance.setCreate_man("***");
			}
			else
			{
				instance.setAnon("非匿名");
			}
			if(status_flag!=null && status_flag.equals("1"))
			{
				instance.setStatus("公开");
			}
			else
			{
				instance.setStatus("不公开");
			}
			String instanceId = instanceService.save(instance);
			if (instanceId !=null) {
				if(oaemail_flag!=null&&oaemail_flag.equals("1"))
				{
					/*if(status_flag!=null&&status_flag.equals("1"))
					{
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
					//			OaOrEmail oe=new OaOrEmail();
					//			oe.sendMailOaByInstanceOpen(instance);						
							}
						});
						aa.start();
					}
					else
					{
						//如果不公开的怎么处理
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
					//			OaOrEmail oe=new OaOrEmail();
					//			oe.sendMailOaByInstanceUnopen(instance);						
							}
						});
						aa.start();
					}
					*/
				}
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("usr", usr);
				String msg = integralCenterService.queryPraiseLimitByInstanceParamMap(paramMap);
				
				MsgBox msgBox = new MsgBox(request, getText("add.ok")+msg);
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
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		Instance instance = new Instance();
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			int ncount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			boolean hasComfirmRight = UserRoleConfig.upAdminManager(userModel);
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					Instance temp = instanceService.getCaseById(sids[i]);
					String confirmStatus = temp.getConfirmStatus();
					if (hasComfirmRight) {
						instanceService.delete(temp);
						instance = temp;
						iCount++;
						/**
						 * add by whxing
						 * 删除赞扬同时删除积分
						 */
						integralCenterService.deleteIntegralInfoByInstanceId(temp.getId());
					}
					else if("1".equals(confirmStatus)){
							if(!loginName.equals(temp.getUp_man())){
								ncount++;
								continue;
							}
							else{
								MsgBox msgBox = new MsgBox(request, "该帖子已被确认，无法删除！如有需要请联系部门经理");
								msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
								return "msgBox";
							}
						
					}
					else{
						if(!loginName.equals(temp.getUp_man())){
							ncount++;
							continue;
						}else{
							instanceService.delete(temp);
							instance = temp;
							iCount++;
							/**
							 * add by whxing
							 * 删除赞扬同时删除积分
							 */
							integralCenterService.deleteIntegralInfoByInstanceId(temp.getId());
						}
					}
					
				}
			}
			
			if(ncount>0){
				MsgBox msgBox = new MsgBox(request, "非记录创建者不能删除");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else{
				MsgBox msgBox = new MsgBox(request, "删除成功");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 

	public String edit() throws Exception {
		String forwardPage="edit";
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String ids = request.getParameter("ids");
			instance=instanceService.getCaseById(ids);
			String loginUserId = userModel.getUserid();
			boolean isLogUser = false;
			if(loginUserId.equals(instance.getCreate_manId())){
				isLogUser = true;
			}
			request.setAttribute("create_manId", instance.getCreate_manId());
			request.setAttribute("isLogUser", isLogUser);//判断是否为本人，若是经理签批不能修改发表内容
			//确认爱心小鱼的权限
			boolean hasComfirmRight = hasComfirmRight(userModel,instance);
			request.setAttribute("hasComfirmRight", hasComfirmRight);
			//没有确认爱心小鱼权限，即部门经理级别以下的只能编辑自己的日志
			String confirmStatus = instance.getConfirmStatus();
			request.setAttribute("confirmStatus", confirmStatus);
			if(!hasComfirmRight){
				
				if(!userModel.getUsername().equals(instance.getUp_man()) || userModel.getLevel() == 0)
				{
					MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"只能由提出者进行修改"}));
					msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				else if ("1".equals(confirmStatus)) {//已确认的爱心小鱼只有部门经理级别以上能修改
					MsgBox msgBox = new MsgBox(request,getText("您不能修改已确认的帖子，如需修改请联系部门经理!",new String[]{"您不能修改已确认的帖子，如需修改请联系部门经理!"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
					
				}
				else{
					upMan = instance.getUp_man();
					return forwardPage;
				}
				
				
				
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
		String loginUserId = userModel.getUserid();
		boolean isLogUser = false;
		if(loginUserId.equals(instance.getCreate_manId())){
			isLogUser = true;
		}
		//判断是否为本人，若是经理签批不能修改发表内容
		boolean hasComfirmRight = UserRoleConfig.upDeptManager(userModel);
		request.setAttribute("hasComfirmRight", hasComfirmRight);
		SysUser usr= staffInfoService.findUserByUserid(instance.getCreate_manId());
		update_man=userModel.getUsername();
		String confirmStatus = instance.getConfirmStatus();
		
		String msg ="";
		try {
			    Instance oldSuggestion=instanceService.getCaseById(instance.getId());
			//	final String oldStatus=oldSuggestion.getStatus();
			//	final String newStatus=suggestion.getStatus();
				String ino=oldSuggestion.getIno();
				instance.setUp_man(oldSuggestion.getUp_man());
				instance.setUpdate_date(new Date());
				instance.setUpdate_man(update_man);
				instance.setIno(ino);
				if(anon_flag!=null && anon_flag.equals("1"))
				{
					instance.setAnon("匿名");
					//instance.setCreate_man("***");
				}
				else
				{
					instance.setAnon("非匿名");
					if(userModel.getLevel()!=0)
					{
						if (isLogUser) {
							instance.setCreate_man(update_man);
						}
						
					}
				}
				if(status_flag!=null && status_flag.equals("1"))
				{
					instance.setStatus("公开");
				}
				else
				{
					instance.setStatus("不公开");
				}
			//拥有权限权限则保存确认信息
			if(hasComfirmRight){					
				//确认状态
				instance.setConfirmStatus("1");//已确认
				//确认人
				instance.setConfirmMan(userModel.getUsername());
				//确认时间
				instance.setConfirmTime(Calendar.getInstance().getTime());
			}
			boolean flag = instanceService.update(instance);
			if (flag) {
				if(oaemail_flag!=null&&oaemail_flag.equals("1"))
				{
				/*	if(status_flag!=null&&status_flag.equals("1"))
					{
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
								OaOrEmail oe=new OaOrEmail();
								oe.sendMailOaByInstanceModiOpen(instance);						
							}
						});
						aa.start();
					}
					else
					{
						//如果不公开的怎么处理
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
								OaOrEmail oe=new OaOrEmail();
								oe.sendMailOaByInstanceModiUnopen(instance);						
							}
						});
						aa.start();
					
					}*/
				}
				/**
				 * add by whxing
				 * 添加积分
				 */
//				if(isLogUser){//判断是否为发表人修改
//				integralCenterService.deleteIntegralInfoByInstanceId(oldSuggestion.getId());
//				Map<String, Object> paramMap = new HashMap<String, Object>();
//				paramMap.put("usr", usr);
//				paramMap.put("instance",instance);
//				paramMap.put("instanceId",oldSuggestion.getId());
//				paramMap.put("operat","edit");
//				msg = integralCenterService.saveIntegralInfoByInstanceParamMap(paramMap);
				
			
//				}
				/**
				 * add by whxing
				 * 确认后添加积分
				 */
				if(hasComfirmRight&&!"1".equals(confirmStatus)){
					/**
					 * add by whxing
					 * 添加积分
					 */
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("usr", usr);
					paramMap.put("instance",instance);
					paramMap.put("instanceId",oldSuggestion.getId());
					paramMap.put("operat","save");
					msg = integralCenterService.saveIntegralInfoByInstanceParamMap(paramMap);
					
					MsgBox msgBox = new MsgBox(request,
							getText("确认成功,")+msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
				
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok")+msg);
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

	/**lhy 2014-4-30
	 * 人员选择
	 * @return
	 */
/*	@SuppressWarnings("unchecked")
	public String select(){
		//部门与员工
		Map deptmaMap = BusnDataDir.getMapKeyValue("staffManager.department");
		Set<String> keySet = deptmaMap.keySet();
		Map<String,List<SysUser>> deptUser = new HashMap<String, List<SysUser>>();
		for(Iterator it = keySet.iterator();it.hasNext();){
			String key = (String) it.next();
			List<SysUser> users = staffInfoService.getStaffByDeptKey(key);
			deptUser.put(key, users);
		}
		request.setAttribute("deptUser", deptUser);
		
		request.setAttribute("deptmaMap", deptmaMap);
		//组别与员工
		List<Project> usrGroups = sysUserGroupService.getAllProjectList();
		Map<String, List<SysUser>> grpUser = new HashMap<String, List<SysUser>>();
		for(Project u:usrGroups){
			List<SysUser> users = sysUserGroupService.findUserByProject(u.getId());
			grpUser.put(u.getId(), users);
		}
		request.setAttribute("grpUser", grpUser);
		request.setAttribute("usrGroups", usrGroups);
		return "select";
	}
	*/
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		System.out.println("refesh");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
    
	
	/**
	 * 分配人员的页面
	 * 
	 * @return 
	 * whxing 
	 * 2014年6月26日
	 */
	public String getAllUserByUserIds() {
		try {
			String userids = request.getParameter("userids");
			String usernames = request.getParameter("usernames");
			String[] userid  = null;
				// 项目中人员
				List<SysUser> inusers = new ArrayList<SysUser>();
				if(userids!=null && !"".equals(userids)){
					 userid = userids.split(",");
					String[] username = usernames.split(",");
					if(userid.length>0){
						for(int i=0;i<userid.length;i++){
							SysUser user = new SysUser();
							user.setUserid(userid[i]);
							user.setUsername(username[i]);
							inusers.add(user);
						}
					}
				}
				// 非项目中人员
				List<SysUser> notinusers = instanceService.getNotInInstanceByProject(userid);
				if (StringUtils.isNotBlank(userids)) {
					request.setAttribute("inusers", inusers);
				}
				request.setAttribute("notinusers", notinusers); 
				Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
				request.setAttribute("deptMap", deptMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "selectuser";
	}
	
	
	/**
	 * 根据部门选择的人员分配选择页面 
	 * whxing 
	 * 2014年6月5日
	 */
	@SuppressWarnings("rawtypes")
	public void getUserByDeptName() {
		try {
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			String deptname = request.getParameter("deptname");
			String username = request.getParameter("username");
			List<String[]> notinidname = new ArrayList<String[]>();
				List<SysUser> notinusers = instanceService.getUserByIdOrName(deptname,username);
				if(notinusers!=null && notinusers.size()>0){
					for(SysUser user:notinusers){
						String[] str = new String[2];
						str[0] = user.getUserid();
						str[1] = user.getUsername();
						notinidname.add(str);
					}
				}
			Map map = new HashMap();
			map.put("notinidname", notinidname);
			map.put("dept", deptMap);
			net.sf.json.JSONArray json = net.sf.json.JSONArray.fromObject(map);
			String result = json.toString();
			ajaxPrint(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 向ajax请求返回数据
	 * 
	 * @param str
	 */
	private void ajaxPrint(String str) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			//判断查询权限
			boolean hasComfirmRight = false;
			hasComfirmRight = UserRoleConfig.upDeptManager(userModel);
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
			Page page = instanceService.getPage(createDate, createMan, summary, category,userModel.getUsername(),pageNum, pageSize, raiseEndDate,hasComfirmRight);
			//createDate, createMan, update_man, category, pn,pageNum, pageSize, raiseEndDate
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < list.size(); i++) {
					  Instance tmp=(Instance)list.get(i);
					tmp.setCreateDateString(tmp.getCreate_date()==null?"":fo.format(tmp.getCreate_date()));
				     tmp.setUpdateDateString(tmp.getUpdate_date()==null?"":fo1.format(tmp.getUpdate_date()));
				}
				  if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.Instance");
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
			//设置用户和组别信息
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//用户级别
			request.setAttribute("userLevel", staffInfoService.getLevelName(userModel.getUserid()));
			//检查用户是否部门经理以上以及管理员权限，是则跳转到修改页面
			String ids = request.getParameter("ids");
			instance=instanceService.getCaseById(ids);
			boolean hasComfirmRight = hasComfirmRight(userModel,instance);
			request.setAttribute("hasComfirmRight", hasComfirmRight);
			if(hasComfirmRight){
				return edit();			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	/**
	 * 拥有确认爱心小鱼权限的是 发表者的部门经理和管理员
	 * @param userModel
	 * @return
	 */
	public boolean hasComfirmRight(UserModel userModel,Instance instance){
		//管理员以上权限
		boolean AdminManageRight = UserRoleConfig.upAdminManager(userModel);//确认爱心小鱼权限
		List<String> userIdList = new ArrayList<String>();
		userIdList.add(instance.getCreate_manId());
		List<SysUser> deptManager = staffInfoService.getDeptManagerByUser(userIdList);
		boolean deptManagerFlag = false;
		//属于自己部门经理权限
		for (int i = 0; i < deptManager.size(); i++) {
			if((deptManager.get(i).getUserid().trim()).equals(userModel.getUserid())){
				deptManagerFlag = true;
			}
		}
		if(AdminManageRight||deptManagerFlag){
		return true;	
		}
		else{
			return false;
		}
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

	public InstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(InstanceService instanceService) {
		this.instanceService = instanceService;
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

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public List<Instance> getCase1() {
		return case1;
	}

	public void setCase1(List<Instance> case1) {
		this.case1 = case1;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAnon_flag() {
		return anon_flag;
	}

	public void setAnon_flag(String anon_flag) {
		this.anon_flag = anon_flag;
	}

	public String getStatus_flag() {
		return status_flag;
	}

	public void setStatus_flag(String status_flag) {
		this.status_flag = status_flag;
	}

	public String getOaemail_flag() {
		return oaemail_flag;
	}

	public void setOaemail_flag(String oaemail_flag) {
		this.oaemail_flag = oaemail_flag;
	}

	public String getEmbracer_manlist() {
		return embracer_manlist;
	}

	public void setEmbracer_manlist(String embracer_manlist) {
		this.embracer_manlist = embracer_manlist;
	}



	public String getUpMan() {
		return upMan;
	}

	public void setUpMan(String upMan) {
		this.upMan = upMan;
	}

	public Map<String, String> getEmbracerlist() {
		return embracerlist;
	}

	public void setEmbracerlist(Map<String, String> embracerlist) {
		this.embracerlist = embracerlist;
	}

	public List<String> getUnames() {
		return unames;
	}

	public void setUnames(List<String> unames) {
		this.unames = unames;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
