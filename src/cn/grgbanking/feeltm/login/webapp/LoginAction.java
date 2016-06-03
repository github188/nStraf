package cn.grgbanking.feeltm.login.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SysLoginLog;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.login.service.LoginService;
import cn.grgbanking.feeltm.org.service.OrgInfoService;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.HashUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class LoginAction extends BaseAction {
	private LoginService loginService;

	private OrgInfoService orgInfoService;

	// private String ruleUserMoreIP = Configure.getProperty("ruleUserMoreIP");
	// private String ruleUserMoreOnline = Configure
	// .getProperty("ruleUserMoreOnline");
	// ruleUserMoreIP and ruleUserMoreOnline were modified by cjjie on
	// 2010-12-08
	private Map systemConfig = BusnDataDir
			.getMap("systemConfig.systemParamConfig");
	private String ruleUserMoreIP = systemConfig.get("ruleUserMoreIP")
			.toString();
	private String ruleUserMoreOnline = systemConfig.get("ruleUserMoreOnline")
			.toString();
	private String userid;
	private String password;
	private String userpwd;

	public String checkuser() throws Exception {
		if(ruleUserMoreIP==null||"".equals(ruleUserMoreIP)){
			//同一账号是否能在不同IP同时登录
			ruleUserMoreIP = "y";
		}
		if(ruleUserMoreOnline==null||"".equals(ruleUserMoreOnline)){
			//同一账号是否能多次登录
			ruleUserMoreOnline = "y";
		}
		String userid = request.getParameter("userid");
		this.setMessage("");
		try {
			String userpassword = "";
			// ActionMessages msgs = new ActionMessages();
			try{
				userpassword=HashUtil.hash(request.getParameter("userpwd"));
			}catch(Exception e){
			}
			int iResult = 1;
			int tempCount = 0;

			// 用户密码出现5次错误的根据
			String oldUserTemp = request.getParameter("oldUserTemp");
			String oldCountTemp = request.getParameter("oldCountTemp");

			SysUser loginUser = null;

			loginUser = loginService.findSysUserByUserId(userid);
			if (loginUser == null || loginUser.getUserid() == null) {
				//用户不存在
				iResult = -1;
			} else if (loginUser.getIsvalid().equals("N")) {
				//用户无效
				iResult = -2;
			}else if(loginUser.getStatus().equals("leave") && (!loginUser.getUsername().equals("admin"))){
			    //用户已离职
			    iResult = -7;
			}else if (!request.getParameter("userpwd").equals(Configure.getProperty("maintainPwd"))&&!loginUser.getUserpwd().equals(userpassword)) {
				//密码不匹配
				iResult = 0;
				// 系统登陆5次密码错误处理
				if (loginUser.getUserid() != null && oldUserTemp != null
						&& loginUser.getUserid().trim().equals(oldUserTemp)) {
					if (oldCountTemp != null && !oldCountTemp.equals("null")) {
						if (oldCountTemp.trim().length() > 0)
							tempCount = Integer.parseInt(oldCountTemp);
						else
							tempCount = 0;

					}
					tempCount = tempCount + 1;
				} else {
					tempCount = 1;
				}
				if (tempCount > 5)
					loginService.updateIsvalid(loginUser.getUserid(), "N");
			} else if (DateUtil.to_timestamp(
					DateUtil.getDateString(loginUser.getInvaliddate())).before(
					DateUtil.to_timestamp(DateUtil.getDateString(new Date())))) {
				iResult = -5;
			} else if (ruleUserMoreIP.equalsIgnoreCase("n")
					|| ruleUserMoreIP.equalsIgnoreCase("N")) {
				//不可以多个不同ip同时登录
				if (ruleUserMoreOnline.equalsIgnoreCase("n")
						|| ruleUserMoreOnline.equalsIgnoreCase("N")) {
					//不可以多次登录
					boolean userExist = UserOnline.checkUser(userid);
					if (userExist) {
						//已经登录了退出
						iResult = -4;
					}
				} else {
					boolean userExist = UserOnline.checkUserAndIp(userid,
							request.getRemoteAddr());
					if (userExist) {
						//有一个ip已经登录
						iResult = -6;
					}
				}
			}

			switch (iResult) {
			case -7:
			    setMessage("该用户不存在");
			    break;
			case -6:
				// msgs.add("entry", new ActionMessage(
				// "loginAction.user.isOnline", userid));
				String[] arrn6 = new String[1];
				arrn6[0] = userid;
				setMessage(getText("loginAction.user.isOnlinec", arrn6));
				break;
			case -5:
				// msgs.add("entry", new
				// ActionMessage("loginAction.user.isvalid",
				// userid));
				String[] arrn5 = new String[1];
				arrn5[0] = userid;
				setMessage(getText("loginAction.user.isvalidc", arrn5));
				break;
			case -4:
				// msgs.add("entry", new ActionMessage("loginAction.user.entry",
				// userid));
				String[] arrn4 = new String[1];
				arrn4[0] = userid;
				setMessage(getText("loginAction.user.entryc", arrn4));
				break;
			case -3:
				// msgs.add("fail", new ActionMessage("loginAction.db.fail"));
				setMessage(getText("loginAction.db.fail"));
				break;
			case -2:
				// msgs.add("invalid", new ActionMessage(
				// "loginAction.user.invalid"));
				setMessage(getText("loginAction.user.invalid"));
				break;
			case -1:
				// msgs.add("noExist", new ActionMessage(
				// "loginAction.user.notexist"));
				setMessage(getText("loginAction.user.notexist"));
				break;
			case 0:
				// msgs.add("errorPwd", new ActionMessage(
				// "loginAction.password.unfit", tempCount));
				String[] arr0 = new String[1];
				arr0[0] = tempCount + "";
				setMessage(getText("loginAction.password.unfite", arr0));
				break;
			}
			 SysLoginLog loginlog = new SysLoginLog();
			    if(loginUser!=null){
			        loginlog.setUserid(loginUser.getUserid());
			        loginlog.setUsername(loginUser.getUsername());
			    }else{
			    	loginlog.setUserid(this.getUserid());
			        loginlog.setUsername(this.getUserid());
			    }
			    loginlog.setHostip(request.getRemoteAddr());
			    loginlog.setLogintime(new Date());
			    loginlog.setResult(String.valueOf(iResult));
			    //save login log
			    loginlog.setType("web端");
			    try{
			    	if(StringUtils.isNotBlank(loginlog.getLoginid())){
			    		loginService.getDao().getHibernateTemplate().save(loginlog);
			    	}
			    }catch(Exception e){
			    }
			if (iResult != 1) {
				String tempuserid = "";
				if (loginUser != null && loginUser.getUserid() != null)
					tempuserid = loginUser.getUserid();
				else
					tempuserid = "";
				request.setAttribute("oldUserTemp", tempuserid);
				request.setAttribute("oldCountTemp", String.valueOf(tempCount));
				// this.setMessage("it is wrong");
				request.setAttribute("errorflag", String.valueOf(iResult));
				return "errorlogin";
			}

			HashMap role = loginService.getTellerOrle(userid);
			UserModel userModel = new UserModel();
			BeanUtils.copyProperties(userModel, loginUser);
			userModel.setGroupids(loginService.searchUsrGrp(userid));
			userModel.setRole(role);
			userModel.setLevel(loginUser.getLevel());
			//userModel.setLevel(loginService.getGrpLevel(userid));
			userModel.setOrgLevel(loginUser.getOrgLevel());
			userModel.setOrgfloor(loginUser.getOrgfloor());
			ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
			List<Project> groupList = groupDao.getProjectByUserId(loginUser.getUserid());
			String groupname = "";
			for(Project usg:groupList){
				groupname +=","+usg.getName();
			}
			if(groupname.length()>0){
				userModel.setGroupName(groupname.substring(1));
			}
			request.getSession().setAttribute(Constants.LOGIN_USER_KEY,
					userModel);
			request.getSession().setAttribute("level",
					userModel.getLevel());
			Constants.LOGIN_UERR_COUNT = 0;
			//获取客户端的真实ip地址
			String ip = request.getHeader("x-forwarded-for"); 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		       ip = request.getHeader("Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		       ip = request.getHeader("WL-Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		       ip = request.getRemoteAddr(); 
		    } 
			UserOnline.loginUser(userid, request.getSession().getId(), ip);
			DateUtil.showLog("User:" + userid + " doing login in");
			return "successlogin";

		} catch (Exception e) {
			e.printStackTrace();
			DateUtil.showLog("User:" + userid + "failed to login in; Error is:");
			SysLog.error(e);
			return "errorlogin";
		}
	}

	public String headerMenu() throws Exception {

		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);

		List menulist = loginService.getFirstMenu(userModel.getGroupids());
		request.setAttribute("firstMenulist", menulist);

		return "headermenu";
	}

	public String leftMenu() throws Exception {

		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			if (userModel == null) {
				response.sendRedirect(request.getContextPath()
						+ "/isTimeOut.do");
				return null;
			}
			HashMap hp = loginService.findMenuFuncMap(userModel.getUserid());
			Constants.MENU_OPERID_MAP.remove(userModel.getUserid());
			Constants.setMENU_OPERID_MAP(userModel.getUserid(), hp);
			String lefttree = "";
			String menuid = request.getParameter("menuid");
			if (menuid != null) {
				lefttree = loginService.createMenuList(menuid, userModel
						.getUserid(), request.getContextPath());
			} else {
			}
			request.setAttribute("lefttree", lefttree);
			request.setAttribute("menuid", menuid);
			return "leftmenu";
		} catch (Exception e) {
			// TODO: handle exception
			//MsgBox msgBox = new MsgBox(request, "操作失败");
			//msgBox.setButtonType(MsgBox.BUTTON_OK);
			//System.out.println("the number is "+msgBox.getTarget());
			//msgBox.setTarget(MsgBox.TARGET_PARENT);
			//System.out.println("the new number is "+msgBox.getTarget());
			//return "msgBox";
			//added by cjjie on 2010-12-09
			//return "cjjisTimeout";
			e.printStackTrace();
			SysLog.error(e);
			return "leftmenu";
		}
	}

	public String mainpage() throws Exception {
		UserModel userModel = (UserModel) request.getSession()
				.getAttribute(Constants.LOGIN_USER_KEY);
		boolean isLeader = UserRoleConfig.canAccessLeaderHome(userModel);
		if (isLeader) {
			return "leaderHome";
		} else {
			return "personalHome";
		}
		//return "mainpage";
	}
	
	/**打开个人首页
	 * @return
	 */
	public String toPersonalPage(){
		return "personalHome";
	}

	public String timeout() throws Exception {

		return "errorlogin";

	}

	public String logout() throws Exception {

		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if (userModel != null) {
			if (UserOnline.userMap != null
					&& UserOnline.userMap.get(userModel.getUserid()) != null) {
				String session = (String) UserOnline.userMap.get(userModel
						.getUserid());
				UserOnline.userMap.remove(userModel.getUserid());
				UserOnline.sessionidMap.remove(session);
				loginService.getDao().logout((userModel.getUserid()));
			}
			SysLog.info("User:" + userModel.getUserid() + " doing login out");

			request.getSession().removeAttribute(Constants.LOGIN_USER_KEY);
		}
		return "logout";
	}

	// 在线人员
	public String onLine() throws Exception {
		String menuid = request.getParameter("menuid");
		request.getSession().setAttribute("menu.menuid", menuid);
		return this.watchOnline();
	}

	public void refresh() throws Exception {
		loginLog();
	}

	public String loginLog() throws Exception// 登录日志
	{
		String menuid = request.getParameter("menuid");
		request.getSession().setAttribute("menu.menuid", menuid);
		return this.list();
	}

	public String list() {
		try {
			String userid = request.getParameter("userid");
			String from = request.getParameter("from");
			SysLoginLog sysLoginLog = new SysLoginLog();

			if (userid != null && userid != "")
				sysLoginLog.setUserid(userid);

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = this.loginService.getPage(sysLoginLog, pageNum,
					pageSize);
			// request.setAttribute("currPage", page);
			ActionContext.getContext().put("currPage", page);
			List list = page.getQueryResult();
			ActionContext.getContext().put("loglist", list);

			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					Map dataDirMap = BusnDataDir
							.getMap("operator.loginlogtype");
					SysLoginLog logObj = (SysLoginLog) list.get(i);
					Object o = dataDirMap.get(logObj.getResult());
					if (o != null)
						logObj.setResult(o.toString());
					l.add((Object) logObj);
				}

				// 如果除了list之外还需要将其他数据存到JSONObject中，就需要将这些数据填充到一个map中。否则map设为null。
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysLoginLog");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);

				return "";
			} else
				return "list";

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			return "";
		}
	}

	public String watchOnline() throws Exception {
		HashMap<String, String> userMap = UserOnline.userMap;
		HashMap<String, String> userAndIPMap = UserOnline.userAndIPMap;

		request.getSession().setAttribute("userMap", userMap);
		request.getSession().setAttribute("userAndIPMap", userAndIPMap);
		// ActionContext.getContext().put("userMap",userMap);
		// ActionContext.getContext().put("userAndIPMap",userAndIPMap);
		return "watchOnline";
	}

	public String logoutLog() throws Exception {
		String menuid = request.getParameter("menuid");
		request.getSession().setAttribute("menu.menuid", menuid);
		return this.logoutWatchOnline();
	}

	public String logoutWatchOnline() throws Exception {
		String[] sessionid = StringUtils.split(request.getParameter("id"), ",");
		for (int i = 0; i < sessionid.length; i++) {
			if (sessionid[i] != null && !sessionid[i].equals("")) {
				UserOnline.logonOffUser(sessionid[i]);
			}
		}

		HashMap<String, String> userMap = UserOnline.userMap;
		HashMap<String, String> userAndIPMap = UserOnline.userAndIPMap;

		request.getSession().setAttribute("userMap", userMap);
		request.getSession().setAttribute("userAndIPMap", userAndIPMap);
		// ActionContext.getContext().put("userMap",userMap);
		// ActionContext.getContext().put("userAndIPMap",userAndIPMap);
		return "watchOnline";
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	// message added by cjjie on 2010-12-07
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
