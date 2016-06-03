package cn.grgbanking.feeltm.staff.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.xwork.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.contact.service.ContactService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrContacts;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.hols.service.UserHolsService;
import cn.grgbanking.feeltm.integralCenter.service.IntegralCenterService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.login.webapp.UserOnline;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.HashUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

/**
 * 2014-4-24 员工信息
 * @author lhyan3
 *
 */
@SuppressWarnings("serial")
public class StaffInfoAction extends BaseAction{

	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private UserHolsService holsService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IntegralCenterService integralCenterService;
	
	private SysUser staff;
	
	private String userids;
	
	private String userid;
	
	private String project;
	
	private boolean flag;
	
	
	/**
	 * 主页面个人信息展示
	 * @return
	 * lhyan3
	 * 2014年7月9日
	 */
	public String personself(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			SysUser user = staffInfoService.findUserByUserid(userModel.getUserid());
			request.setAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "person";
	}
	
	/**
	 * @return
	 * @throws Exception
	 * lhyan3
	 * 2014年6月13日
	 */
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String deptName = request.getParameter("deptName");
		String deptvalue = request.getParameter("deptValue");
		String groupId = request.getParameter("groupId");
		List<Object[]> list=staffInfoService.getNames(deptName,deptvalue,groupId);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * lhy 2014-4-28
	 * 查看
	 * @return
	 */
	public String view(){
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if(userModel!=null && userModel.getUserid().equals(userid)){
				return modifyPage();
			}
			
			SysUser user = staffInfoService.findUserByUserid(userid);
			String prjName = projectService.getProjectNameByUserid(userid);
			double sumage = 0;
			double historyage  = 0;
			double grgage = DateUtil.getYearMus(new Date(), user.getGrgBegindate());
			if(user.getWorkBegindate()!=null){
				sumage = DateUtil.getYearMus(new Date(), user.getWorkBegindate());
				historyage = DateUtil.getYearMus(user.getGrgBegindate(), user.getWorkBegindate());
			}else{
				sumage=grgage;
			}
			request.setAttribute("sumage", sumage);
			request.setAttribute("historyage", historyage);
			request.setAttribute("grgage", grgage);
			request.setAttribute("sysUser", user);
			//ActionContext.getContext().put("sysUser", user);
			request.setAttribute("groupNames", prjName);
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-findOneByUserInfo())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "view";
	}
	
	/**
	 * lhy 2014-4-28
	 * 页面异步查询
	 */
	public void refresh(){
		try {
			list();
		} catch (Exception e) {
			SysLog.error("error in (StaffInfoAction.java-refresh())");
			e.printStackTrace();
			SysLog.error(e);
		}
	}
	
	/**
	 * lhy 2014-4-28
	 * 解锁
	 * @return
	 */
	public String resetUser() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String userid = request.getParameter("userid");
		SysUser user = staffInfoService.findUserByUserid(userid);
		user.setIsvalid("Y");
		staffInfoService.updateStaffInfo(user);
		if (UserOnline.userMap != null
				&& UserOnline.userMap.get(user.getUserid()) != null) {
			String session = (String) UserOnline.userMap.get(user.getUserid());
			UserOnline.userMap.remove(user.getUserid());
			UserOnline.sessionidMap.remove(session);
		}
		try {
			SysLog.operLog(request, Constants.OPER_PWRESET_VALUE, userid);// 记录日志
			SysLog.info(userModel.getUserid() + " change " + userid
					+ "'s lock as default !");

			MsgBox msgBox = new MsgBox(request, getText("um.locksuccess"),
					"um", null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			SysLog.error(request, "failed to change " + userid
					+ "'s lock as default.");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * lhy 2014-4-28
	 * 重置密码
	 * @return
	 */
	public String resetPW() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		SysUser user = staffInfoService.findUserByUserid(userid);
		user.setUserpwd(HashUtil.hash(user.getUserid()));
		staffInfoService.updateStaffInfo(user);
		try {
			SysLog.operLog(request, Constants.OPER_PWRESET_VALUE, userid);// 记录日志
			SysLog.info(userModel.getUserid() + " change " + userid
					+ "'s password as default !");
			MsgBox msgBox = new MsgBox(request,
					getText("modifypassword.success"), "um", null);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			SysLog.error(request, "failed to change " + userid
					+ "'s password as default.");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "msgBox";
	}
	
	/**lhy 2014-4-24
	 * 员工信息列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		try {
			String from = request.getParameter("from");
			String userid = request.getParameter("userid");
			String deptNo = request.getParameter("deptNo");
			String grpCode = request.getParameter("grpCode");
			String startstr = request.getParameter("startTime");
			String endstr = request.getParameter("endTime");
			String status = request.getParameter("status");
			String orderField = request.getParameter("orderField");
			String regulation = request.getParameter("regulation");
			SysUser user = new SysUser();
			ActionContext.getContext().put("sysUser", user);
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			request.setAttribute("loginUserId", userModel.getUserid());
			Map statusMap = BusnDataDir.getMapKeyValue("staffManager.status");
			request.setAttribute("statusMap", statusMap);
			List<UsrGroup> usrGroups = sysUserGroupService.getAllGroupList();
			int pageNum = 1;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			if (null != userid && !"".equals(userid)) {
				user.setUserid(userid);
			}
			if(null != deptNo && !"".equals(deptNo)){
				user.setDeptName(deptNo);
			}
			if(null != status && !"".equals(status)){
				user.setStatus(status);
			}
			
			Page page = staffInfoService.findUserInfoPage(user,userModel,grpCode,startstr,endstr,pageNum,
					pageSize,orderField,regulation);
			request.setAttribute("currPage", page);
			
			List<SysUser> list = page.getQueryResult();
			List<String> groupNameList = new ArrayList<String>();
			List<String> userGroupNameList = new ArrayList<String>();
			List<String> workYearList = new ArrayList<String>();
			List<String> statusList = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				SysUser sysUser = (SysUser) list.get(i);
				//转换学历数据字典
				Map eduMap = BusnDataDir.getMapKeyValue("staffManager.education");
				if(sysUser.getEducation()!=null){
					String eduName = BusnDataDir.getValue(eduMap, sysUser.getEducation());
					sysUser.setEducation(eduName);					
				}
				//转换岗位级别字典
				Map lvMap = BusnDataDir.getMapKeyValue("staffManager.postlevel");
				if(sysUser.getPostLevel()!=null){
					String lvName = BusnDataDir.getValue(lvMap, sysUser.getPostLevel());
					sysUser.setPostLevel(lvName);					
				}
				//组别
				StringBuffer sb = new StringBuffer();
				List<UsrGroup> usrGroupList = sysUserGroupService.getUserGropByUserId(sysUser.getUserid());
				for(UsrGroup group : usrGroupList){
					sb.append(group.getGrpname()+",");
				}
				if(sb.length()>1){
					userGroupNameList.add(sb.substring(0, sb.length()-1));					
				}else{
					userGroupNameList.add("");
				}
				//项目名称
				String prjName = projectService.getProjectNameByUserid(sysUser.getUserid());
				groupNameList.add(prjName);
				//工龄
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date workBeginDate = sysUser.getWorkBegindate();
				Date grgBeginDate = sysUser.getGrgBegindate();
				String nowDate = sdf.format(new Date());
				String workDate = nowDate;
				String grgDate = nowDate;
				if(workBeginDate!=null){
					workDate = sdf.format(workBeginDate);					
				}
				if(workBeginDate!=null){
				    grgDate = sdf.format(grgBeginDate);
				}
				//更新总工龄与运通工龄begin
				SysUser user1 = staffInfoService.findUserByUserid(sysUser.getUserid());
				double sumage = 0;
				double historyage  = 0;
				double grgage = DateUtil.getYearMus(new Date(), user1.getGrgBegindate());
				if(user1.getWorkBegindate()!=null){
					sumage = DateUtil.getYearMus(new Date(), user1.getWorkBegindate());
					historyage = DateUtil.getYearMus(user1.getGrgBegindate(), user1.getWorkBegindate());
				}else{
					sumage = grgage;
				}
				String grgYear = String.valueOf(grgage);
				String workYear = String.valueOf(historyage);
				String totalYear = String.valueOf(sumage);
				//end
				workYearList.add(grgYear+"年/"+totalYear+"年");
				//在职状态
				String statusTemp = sysUser.getStatus();
				String statusName = "";
				Set<String> keys = statusMap.keySet();
				for (String key : keys) {
					if(key.equals(statusTemp)){
						statusName = (String)statusMap.get(key);
						break;
					}
				}
				statusList.add(statusName);
			}

			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			request.setAttribute("deptMap", deptMap);
			request.setAttribute("usrgroups", usrGroups);
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					l.add((Object) list.get(i));
				}

				// 如果除了list之外还需要将其他数据存到JSONObject中，就需要将这些数据填充到一个map中。否则map设为null。
				// 由于不论是将pageCount和recordCount存在request中，还是session中，在jsp页面取的时候都取的是旧的值，所以将其存在json对象中
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf("1"));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysUser");
				map.put("userGroupName",userGroupNameList);
				map.put("workYear",workYearList);
				map.put("statusList",statusList);
				/*JSONArray jsonObj = jsonUtil.twoList2JSON(l, groupNameList,
						"groupName", map);*/
				JSONArray jsonObj = jsonUtil.twoList2JSON2(l, groupNameList,
						"groupName", map,"deptName",deptMap);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("groupNameList", groupNameList);
				ActionContext.getContext().put("userList", list);
				ActionContext.getContext().put("userGroupNameList", userGroupNameList);
				ActionContext.getContext().put("workYearList", workYearList);
				ActionContext.getContext().put("statusList", statusList);
				return "list";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (StaffInfoAction.java-list())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**lhy 2014-4-24
	 * 添加页面
	 * @return
	 */
	public String addpage(){
		Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
		Map userLevel = BusnDataDir.getMapKeyValue("staffManager.userlevel");
		Map postlevel = BusnDataDir.getMapKeyValue("staffManager.postlevel");
		Map status = BusnDataDir.getMapKeyValue("staffManager.status");
		Map education = BusnDataDir.getMapKeyValue("staffManager.education");
		Map validate = BusnDataDir.getMapKeyValue("staffManager.validate");
		List<Project> groups = projectService.listAllGroup();
		request.setAttribute("group", groups);
		request.setAttribute("deptMap", deptMap);
		request.setAttribute("userLevel", userLevel);
		request.setAttribute("postlevel", postlevel);
		request.setAttribute("status", status);
		request.setAttribute("education", education);
		request.setAttribute("validate", validate);
		return "addpage";
	}
	
	/**lhy 2014-4-25
	 * 保存
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
				//添加
				if(!staffInfoService.isExitStaffByUserid(staff.getUserid())){
					//不存在保存
					staff.setUserpwd(HashUtil.hash(staff.getUserid()));
					staff.setFlag("0");
					staff.setIsvalid("Y");
					staff.setCreatedate(new Date());
					if(null == staff.getInvaliddate() || "".equals(staff.getInvaliddate())){
						staff.setInvaliddate(DateUtil.stringToDate("9999-12-31", "yyyy-MM-dd"));
					}
					staffInfoService.addStaffInfo(staff);
					//添加通讯录
					contactService.saveNewConstact(userModel.getUsername(),staff);
					/*UserProject userProject = new UserProject();
					userProject.setGcode(project);
					userProject.setUserid(staff.getUserid());
					groupService.save(userModel);*/
					//添加假期数据
					holsService.addHols(staff.getUserid());
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, staff
							.getUserid());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " add a User : " + staff.getUserid());
					msgBox = new MsgBox(request, getText("add.ok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
				}else {
					//存在不保存
					msgBox = new MsgBox(request, getText("staff.save.exists"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.addfaile"));
				}
		} catch (Exception e) {
			SysLog.error(request,
					"error in (StaffInfoAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a User :" + staff.getUserid());
			SysLog.error(e);

			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}
		return "msgBox";
	}
	
	/**
	 * lhy 2014-4-25
	 * 删除，但是不能删除自己
	 * @return
	 */
	public String delete(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox ;
		int ifself = 0;//是否是自己删除自己
		StringBuffer strbuf = new StringBuffer();
		StringBuffer cons = new StringBuffer();
		if(null != userids && !"".equals(userids)){
			String useridStr[] = userids.split(",");
			SysUser user = new SysUser();
			UsrContacts contacts = new UsrContacts();
			try {
				for(String id:useridStr){
					if(null != id && !"".equals(id)){
						user = staffInfoService.findUserByUserid(id);
						contacts = contactService.getContactByUserId(id);
						if(null != user && user.getFlag()!=null && user.getFlag().equals("1")){
							//已删除状态的不能删除
							SysLog.info("user " + user.getUserid()
									+ " can not be delete !");
							continue;
						}
						if(user.getUserid().equals(userModel.getUserid())){
							ifself = 1;
						}
						strbuf.append(user.getUserid()+",");
						if(contacts!=null){
							contactService.delete(contacts);
						}
					}
				}
				if(ifself==1){
					//国际化文件
					msgBox = new MsgBox(request, getText("staff.cannotdel"), "staff");
				}else{
					if(strbuf.toString().length()>0){
						int i = staffInfoService.removeStaffInfo(strbuf.toString());
						
						SysLog.operLog(request, Constants.OPER_DELETE_VALUE,
								strbuf.toString());// 记录日志
						SysLog.info("User:" + userModel.getUserid()
								+ " delete a User : " + user.getUserid());
						msgBox = new MsgBox(request,
								getText("删除成功"), "um",
								new String[] { String.valueOf(i) });
					}else{
						msgBox = new MsgBox(request, getText("operInfoform.cannotdel"), "um");
					}
				}
			} catch (Exception e) {
				SysLog.error(request,
						"error in (StaffInfoAction.java-removeSysUserInfo())");
				SysLog.error(e);
				SysLog.info("user " + strbuf.toString() + " is not exist !");
				msgBox = new MsgBox(request, getText("operInfoform.isnotexist"), "um");
			}
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * lhy 2014-4-47
	 * 跳往修改页面
	 * @return
	 */
	public String modifyPage(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			SysUser user = staffInfoService.findUserByUserid(userid);
			staff=user;
			Map deptMap = BusnDataDir.getMapKeyValue("staffManager.department");
			Map userLevel = BusnDataDir.getMapKeyValue("staffManager.userlevel");
			Map postlevel = BusnDataDir.getMapKeyValue("staffManager.postlevel");
			Map status = BusnDataDir.getMapKeyValue("staffManager.status");
			Map education = BusnDataDir.getMapKeyValue("staffManager.education");
			Map validate = BusnDataDir.getMapKeyValue("staffManager.validate");
			request.setAttribute("deptMap", deptMap);
			request.setAttribute("userLevel", userLevel);
			request.setAttribute("postlevel", postlevel);
			request.setAttribute("status", status);
			request.setAttribute("education", education);
			request.setAttribute("validate", validate);
			request.setAttribute("user", user);
			List<UsrGroup> usrGroupList = sysUserGroupService.getUserGropByUserId(user.getUserid());
			StringBuffer grpNameBuff = new StringBuffer();
			for(UsrGroup ug : usrGroupList){
				grpNameBuff.append(ug.getGrpname()+",");
			}
			if(grpNameBuff.length()>0){
				request.setAttribute("grpNames", grpNameBuff.substring(0, grpNameBuff.length()-1));
			}
			boolean flag = false;
			if(UserRoleConfig.ifAdministratorOrHr(userModel)){
				//人事修改
				flag = true;
				request.setAttribute("flag", flag);
				return "hrmodify";
			}else if(!userModel.getUserid().equals(user.getUserid())){
				//不是自己修改//别人不能修改
				MsgBox msgBox = new MsgBox(request, getText("只能修改自己的人员信息"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
			request.setAttribute("flag", flag);
		} catch (Exception e) {
			SysLog.error("error in (StaffInfoAction.java-findOneByUserInfo())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "modifyPage";
	}
	
	/**
	 * lhy 2014-4-28 
	 * 修改保存
	 * @return
	 */
	public String modify(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			if(null != userid && !"".equals(userid)){
				SysUser user = staffInfoService.findUserByUserid(userid);
				if(flag){
					//hr修改
					if(StringUtils.isNotBlank(staff.getDeptName())){
						//如果更改部门则需要更新个人积分记录
						if (!StringUtils.trim(user.getDeptName()).equals(StringUtils.trim(staff.getDeptName()))) {
							user.setDeptName(staff.getDeptName());
							String deptName = (String) BusnDataDir.getMapKeyValue("staffManager.department").get(user.getDeptName());
							integralCenterService.updateIntegral(user.getDeptName(),deptName, user.getUserid());
						}
					}
					if(staff.getGrgBegindate()!=null){
						user.setGrgBegindate(staff.getGrgBegindate());
					}
					if(staff.getLevel()!=null){
						user.setLevel(staff.getLevel());
					}
					if(StringUtils.isNotBlank(staff.getPostLevel())){
						user.setPostLevel(staff.getPostLevel());
					}
					if(StringUtils.isNotBlank(staff.getStatus())){
						user.setStatus(staff.getStatus());
					}
					if(StringUtils.isNotBlank(staff.getJobNumber())){
						user.setJobNumber(staff.getJobNumber());
					}
					if(StringUtils.isNotBlank(staff.getOutNumber())){
						user.setOutNumber(staff.getOutNumber());
					}
				}
				if(staff.getBirthDate()!=null){
					user.setBirthDate(staff.getBirthDate());
				}
				user.setCreatedate(new Date());
				if(staff.getEducation()!=null){
					user.setEducation(staff.getEducation());
				}
				if(StringUtils.isNotBlank(staff.getEmail())){
					user.setEmail(staff.getEmail());
				}
				if(StringUtils.isNotBlank(user.getFlag())){
					user.setFlag(user.getFlag());
				}
				if(staff.getGraduateDate()!=null){
					user.setGraduateDate(staff.getGraduateDate());
				}
				if(StringUtils.isNotBlank(staff.getGraduateSchool())){
					user.setGraduateSchool(staff.getGraduateSchool());
				}
				if(StringUtils.isNotBlank(staff.getIdCardNo())){
					user.setIdCardNo(staff.getIdCardNo());
				}
				if(StringUtils.isNotBlank(staff.getMajor())){
					user.setMajor(staff.getMajor());
				}
				if(StringUtils.isNotBlank(staff.getMobile())){
					user.setMobile(staff.getMobile());
				}
				if(StringUtils.isNotBlank(staff.getTel())){
					user.setTel(staff.getTel());
				}
				if(StringUtils.isNotBlank(staff.getUsername())){
					user.setUsername(staff.getUsername());
				}
				if(StringUtils.isNotBlank(user.getUserpwd())){
					user.setUserpwd(user.getUserpwd());
				}
				if(staff.getWorkBegindate()!=null){
					user.setWorkBegindate(staff.getWorkBegindate());
				}
				if(StringUtils.isNotBlank(staff.getRelativeName())){
					user.setRelativeName(staff.getRelativeName());
				}
				if(StringUtils.isNotBlank(staff.getRelativeTel())){
					user.setRelativeTel(staff.getRelativeTel());
				}
				if(StringUtils.isNotBlank(staff.getAddress())){
					user.setAddress(staff.getAddress());
				}
				user.setUpdateTime(new Date());
				staffInfoService.updateStaffInfo(user);
				//更新通讯录
				contactService.updateContactByUser(user,userModel);
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, staff
						.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a User : " + staff.getUserid());
					msgBox = new MsgBox(request, getText("operInfoform.updateok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.updateok"));
				}else {
					msgBox = new MsgBox(request, getText("operInfoform.updateok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.updatefaile"));
				}
		} catch (Exception e) {
			SysLog.error(request,
					"error in (StaffInfoAction.java-UpdateStaffInfo())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to modify a User :" + staff.getUserid());
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.updatefaile"));
		}
		return "msgBox";
	}
	/**
	 * 用户自己更改自己的 电话和E-mail
	 * @author ljlian2 2014-12-16
	 * @return 
	 */
	public String modifyBySelf(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String msg ="";
		try{
				if(null != userid && !"".equals(userid)){
					SysUser user = staffInfoService.findUserByUserid(userid);
					String mobile=staff.getMobile();
					String email=staff.getEmail();
					user.setMobile(mobile);
					user.setEmail(email);
					boolean flag=staffInfoService.updatePartStaffInfo(mobile,email,userid);
					//更新通讯录
					contactService.updateContactByUser(user,userModel);
					System.out.println("更新通讯录");
					if(flag){
						MsgBox msgBox = new MsgBox(request, getText("更新数据成功")+msg);
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						this.addActionMessage(getText("update.ok"));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				MsgBox msgBox = new MsgBox(request, "更新数据错误!",
						new String[] { e.toString() });
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.updatefaile"));
				boolean hasActionMessage = this.hasActionMessages();
				request.setAttribute("hasActionMessage", hasActionMessage);
				return "msgBox";
			}
		return "msgBox";
		
	}
	/**
	 * 检查并更新从EHR导入的新用户，设置其默认密码为userid,用户组为普通用户
	 * @author lping1 2014-9-23
	 */
	public void initNewUserFromEHR(){
		staffInfoService.initNewUserFromEHR();
	}
	/**
	 * 同步用户数据
	 * @author ljlian
	 */
	public void synSysUserFromEHR(){
		try {
			staffInfoService.synSysUserFromEHR();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	public SysUser getStaff() {
		return staff;
	}
	public void setStaff(SysUser staff) {
		this.staff = staff;
	}
	public String getUserids() {
		return userids;
	}
	public void setUserids(String userids) {
		this.userids = userids;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
