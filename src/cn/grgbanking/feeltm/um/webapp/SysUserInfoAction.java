package cn.grgbanking.feeltm.um.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.OrgInfo;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.login.webapp.UserOnline;
import cn.grgbanking.feeltm.org.service.OrgInfoService;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.HashUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class SysUserInfoAction extends BaseAction {
	private SysUserInfoService sysUserInfoService;
	// private SysUserGroupService sysUserGroupService;
	private OrgInfoService orgInfoService;
	// private CreateChartService createChartService;

	// 封装用户请求参数的属性
	// Struts2的拦截器机制负责解析用户的请求参数，并将请求参数赋值给action对应的属性
	/** identifier field */
	private String userid;

	/** identifier field */
	private String username;

	/** identifier field */
	private String workid;

	/** identifier field */
	private String workcompany;

	/** identifier field */
	private String tel;

	/** identifier field */
	private String mobile;

	/** identifier field */
	private String email;

	/** identifier field */
	private String userpwd;

	/** identifier field */
	private Date createdate;

	/** identifier field */
	private String isvalid;

	/** identifier field */
	private String flag;

	/** identifier field */
	private Date invaliddate;

	/** identifier field */
	private String orgid;

	/** identifier field */
	private String areaid;

	/** identifier field */
	private String id;

	/** identifier field */
	private String orgLevel;

	private String orgfloor;

	private String acceptemailType;
	
	private String groupName;
	
	private Integer level;
	
	private String deptName;
	

	public String view() {
		String userid = request.getParameter("userid");
		try {
			SysUser user = sysUserInfoService.findSysUserByUserId(userid);

			if (user.getEmail() != null && user.getEmail().length() > 0)
				user.setEmail(user.getEmail().trim());

			ActionContext.getContext().put("endDate",
					DateUtil.getDateString(user.getInvaliddate()));
			ActionContext.getContext().put("sysUser", user);
		} catch (Exception e) {
			SysLog
					.error("error in (SysUserInfoAction.java-findOneByUserInfo())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "view";
	}

	public String addpage() {
		try {
			return "insert";
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-addpage())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public void refresh() {
		try {
			list();
		} catch (Exception e) {
			SysLog.error("error in (SysUserInfoAction.java-refresh())");
			e.printStackTrace();
			SysLog.error(e);
		}
	}

	/*
	 * 用户列表
	 */
	public String list() {
		String from = request.getParameter("from");
		String userid = request.getParameter("userid");
		String username = request.getParameter("username");
		try {
			SysUser user = new SysUser();
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			int pageNum = 1;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			user.setOrgid(userModel.getOrgid());
			user.setOrgfloor(userModel.getOrgfloor());

			if (userid != "") {
				user.setUserid(userid);
			}
			if (username != "") {
				user.setUsername(username);
			}
			Page page = sysUserInfoService.findUserInfoPage(user, pageNum,
					pageSize);
			request.setAttribute("currPage", page);

			List<SysUser> list = page.getQueryResult();
			List<String> groupNameList = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				SysUser sysUser = (SysUser) list.get(i);
				String groupName = sysUserInfoService
						.getGroupNameByUserId(sysUser.getUserid());
				groupNameList.add(groupName);
			}

			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					l.add((Object) list.get(i));
				}

				// 如果除了list之外还需要将其他数据存到JSONObject中，就需要将这些数据填充到一个map中。否则map设为null。
				// 由于不论是将pageCount和recordCount存在request中，还是session中，在jsp页面取的时候都取的是旧的值，所以将其存在json对象中
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysUser");
				JSONArray jsonObj = jsonUtil.twoList2JSON(l, groupNameList,
						"groupName", map);
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
				return "list";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (SysUserInfoAction.java-list())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/*
	 * 删除用户
	 */
	public String delete() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String[] users = StringUtils
				.split(request.getParameter("userids"), ",");
		StringBuffer strUser = new StringBuffer();
		MsgBox msgBox;
		SysUser sysuser = null;
		String isflag = "0";
		try {
			for (int i = 0; i < users.length; i++) {
				if (users[i] != null && !users[i].equals("")
						&& !users[i].equals("all")) {

					sysuser = sysUserInfoService.findSysUserByUserId(users[i]);
					if (sysuser != null && sysuser.getFlag() != null
							&& sysuser.getFlag().equals("1")) {
						SysLog.info("user " + sysuser.getUserid()
								+ " can not be delete !");
						continue;
					}
					if (sysuser.getUserid().equals(userModel.getUserid())) {
						isflag = "1";
					}
					strUser.append(users[i] + ",");
				}
			}
			if (isflag.equals("1")) {
				msgBox = new MsgBox(request, getText("um.cannotdel"), "um");
			} else {
				if (strUser.toString().length() > 0) {
					int i = sysUserInfoService.removeSysUserInfo(strUser
							.toString());
					SysLog.operLog(request, Constants.OPER_DELETE_VALUE,
							strUser.toString());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " delete a User : " + sysuser.getUserid());
					msgBox = new MsgBox(request,
							getText("operInfoform.deleteok"), "um",
							new String[] { String.valueOf(i) });
				} else
					msgBox = new MsgBox(request, getText("um.cannotdel"), "um");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			SysLog.error(request,
					"error in (SysUserInfoAction.java-removeSysUserInfo())");
			SysLog.error(e);
			SysLog.info("user " + strUser.toString() + " is not exist !");
			msgBox = new MsgBox(request, getText("um.isnotexist"), "um");

		}
		msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		return "msgBox";
	}

	public String modify() {
		String userid = request.getParameter("userid");
		try {
			SysUser user = sysUserInfoService.findSysUserByUserId(userid);

			if (user.getEmail() != null && user.getEmail().length() > 0)
				user.setEmail(user.getEmail().trim());

			// request.setAttribute("endDate",
			// DateUtil.getDateString(user.getInvaliddate()));
			// request.setAttribute("sysUser", user);
			ActionContext.getContext().put("endDate",
					DateUtil.getDateString(user.getInvaliddate()));
			ActionContext.getContext().put("sysUser", user);
		} catch (Exception e) {
			SysLog
					.error("error in (SysUserInfoAction.java-findOneByUserInfo())");
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "update";
	}

	/*
	 * 增加用户
	 */
	public String add() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		SysUser sysUser = new SysUser();
		String newAnOther = request.getParameter("newAnOther");

		MsgBox msgBox;
		try {

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sysUser.setCreatedate(DateUtil.stringToDate(sdf.format(date),
					"yyyy-MM-dd"));
			String endDate = request.getParameter("endDate");
			if (endDate.equals("") || endDate == null)
				sysUser.setInvaliddate(DateUtil.stringToDate("9999-12-31",
						"yyyy-MM-dd"));
			else if (endDate != null)
				sysUser.setInvaliddate(DateUtil.stringToDate(endDate,
						"yyyy-MM-dd"));

			sysUser.setFlag("0");
			sysUser.setUserid(getUserid());
			sysUser.setUsername(getUsername());
			sysUser.setWorkcompany(getWorkcompany());
			sysUser.setWorkid(getWorkid());
			sysUser.setAcceptemailType(getAcceptemailType());
			sysUser.setEmail(getEmail());
			// sysUser.setFlag(getFlag());
			sysUser.setIsvalid(getIsvalid());
			sysUser.setMobile(getMobile());
			sysUser.setOrgid(getOrgid());
			sysUser.setGroupName(getGroupName());
			sysUser.setLevel(level);
			System.out.println("groupName:"+getGroupName());
			sysUser.setUserpwd(HashUtil.hash(getUserpwd()));// ���������
			if (!sysUserInfoService.isExitsSysUserInfo(sysUser)) {
				OrgInfo orgInfo = orgInfoService.getOrgInfoByOrgId(sysUser
						.getOrgid());
				// 获取用户属于那个等级，4省行 3：二级分行，一级支行 2：二级支行 1 ：网点
				if (orgInfo != null)
					sysUser.setOrgLevel(orgInfo.getOrgLevel());

				UsrUsrgrp usrUsrgrp = new UsrUsrgrp();
				usrUsrgrp.setGrpcode(request.getParameter("userGrp"));
				usrUsrgrp.setUserid(sysUser.getUserid());
				boolean flag = sysUserInfoService.addSysUserInfo(sysUser,
						usrUsrgrp);

				if (flag == true) {
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, sysUser
							.getUserid());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " add a User : " + sysUser.getUserid());
					msgBox = new MsgBox(request, getText("add.ok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
				} else {
					msgBox = new MsgBox(request,
							getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.addfaile"));
				}
			} else {
				msgBox = new MsgBox(request, getText("operInfoform.addfaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.addfaile"));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request,
					"error in (SysUserInfoAction.java-insertSysUserInfo())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a User :" + sysUser.getUserid());
			SysLog.error(e);

			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		// 没有点“提交后继续新增”
		if (newAnOther.equals("true")) {
			return "insert";
		}
		return "msgBox";
	}

	public String resetPW() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String userid = request.getParameter("userid");
		SysUser user = sysUserInfoService.findSysUserByUserId(userid);
		user.setUserpwd(HashUtil.hash(user.getUserid()));
		sysUserInfoService.updateSysUser(user);
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

	public String resetUser() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String userid = request.getParameter("userid");
		SysUser user = sysUserInfoService.findSysUserByUserId(userid);
		user.setIsvalid("Y");
		sysUserInfoService.updateSysUser(user);
		if (UserOnline.userMap != null
				&& UserOnline.userMap.get(user.getUserid()) != null) {
			String session = (String) UserOnline.userMap.get(user.getUserid());
			UserOnline.userMap.remove(user.getUserid());
			UserOnline.sessionidMap.remove(session);
		}
		try {
			SysLog.operLog(request, Constants.OPER_PWRESET_VALUE, userid);// 记录日志
			System.out.println("userid:" + userid + "          u:"
					+ Constants.LOGIN_USER_KEY);
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

	public String update() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String endDate = request.getParameter("endDate");
		String userId = request.getParameter("userid");

		try {
			SysUser user = sysUserInfoService.findSysUserByUserId(userId);// 修改前的user信息

			if (endDate.equals("") || endDate == null)
				user.setInvaliddate(DateUtil.stringToDate("9999-12-31",
						"yyyy-MM-dd"));
			else if (endDate != null)
				user.setInvaliddate(DateUtil
						.stringToDate(endDate, "yyyy-MM-dd"));
			boolean flag = false;
			user.setCreatedate(user.getCreatedate());
			user.setFlag(user.getFlag());
			user.setUserpwd(user.getUserpwd());

			user.setUsername(this.getUsername());
			user.setWorkid(this.getWorkid());
			user.setWorkcompany(this.getWorkcompany());
			user.setTel(this.getTel());
			user.setMobile(this.getMobile());
			user.setEmail(this.getEmail());
			user.setIsvalid(this.getIsvalid());
			user.setGroupName(this.getGroupName());
			user.setLevel(level);
			user.setAcceptemailType(this.getAcceptemailType());

			flag = sysUserInfoService.updateSysUserInfo(user);
			if (flag == true) {
				SysLog.info("User:" + userModel.getUserid()
						+ " update a User : " + user.getUserid());
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"), "um");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"), "um");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}

			return "msgBox";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog
					.error("error in (SysUserInfoAction.java-updateSysUserInfo())");
			SysLog.error(request, "failed to update user's information.");
			SysLog.error(e);

			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	
	
	
	/** 得到审核人员列表 */
	public String getUsrUsrgrp() {
		try {
			String grpCode = "director";
			List<UsrUsrgrp> usrList = sysUserInfoService
					.getSysUserGroupList(grpCode);
			ActionContext.getContext().put("usrList", usrList);
			ActionContext.getContext().put("listSize", usrList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "usrUsrgrgList";
	}

	/**
	 * by wjie5 2014-4-24
	 * 根据部门名称查询用户名称
	 * @return
	 * @throws Exception
	 */
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String deptvalue = request.getParameter("deptValue");
		List<Object[]> list=sysUserInfoService.getNames(deptName,deptvalue);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}

	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}

	public OrgInfoService getOrgInfoService() {
		return orgInfoService;
	}

	public void setOrgInfoService(OrgInfoService orgInfoService) {
		this.orgInfoService = orgInfoService;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public String getWorkcompany() {
		return workcompany;
	}

	public void setWorkcompany(String workcompany) {
		this.workcompany = workcompany;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getInvaliddate() {
		return invaliddate;
	}

	public void setInvaliddate(Date invaliddate) {
		this.invaliddate = invaliddate;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}

	public String getAcceptemailType() {
		return acceptemailType;
	}

	public void setAcceptemailType(String acceptemailType) {
		this.acceptemailType = acceptemailType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
