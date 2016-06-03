package cn.grgbanking.feeltm.myadvice.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.commonPlatform.utils.CommonUtil;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.myadvice.bean.AdviceUserBean;
import cn.grgbanking.feeltm.myadvice.domain.AdviceAndResponse;
import cn.grgbanking.feeltm.myadvice.service.AdviceAndResponseService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

/*
 * Author:ljlian2
 * 2014-12-5
 * 
 * */

//建议与反馈
public class AdviceAndResponseAction extends BaseAction {
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private AdviceAndResponseService adviceAndResponseService;

	private AdviceAndResponse adviceandresponse;
	private String from;

	// 点击页面左侧建议与反馈显示所有的建议信息
	public String query() {
		try {
			// 获取当前用户
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			// 如果当前用户有登录
			if (userModel != null) {
				SysUser usr = staffInfoService.findUserByUserid(userModel
						.getUserid());
				String username = usr.getUsername();
				request.setAttribute("longin", "yes");
				request.setAttribute("curUser", username);
			} else {
				request.setAttribute("curUser", "");
				request.setAttribute("longin", "no");
			}

			Map adviceARStautsMap = BusnDataDir
					.getMapKeyValue("systemConfig.adviceAndResponseStatus");
			// System.out.println("数据字典中的值"+adviceARStautsMap);
			request.setAttribute("adviceARStautsMap", adviceARStautsMap);// 查询条件
			int pageNum = 1;
			int pageSize = 20;

			// 获取界面的查询参数
			String selectusername = request.getParameter("selectusername");
			String selectStatus = request.getParameter("selectStatus");
			String selectStarTime = request.getParameter("selectStarTime");
			String selectEndTime = request.getParameter("selectEndTime");
			// 分页
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));

			// Page page = adviceAndResponseService.getPage(pageNum, pageSize);
			Page page = adviceAndResponseService
					.getPage(pageNum, pageSize, selectusername, selectStatus,
							selectStarTime, selectEndTime);

			request.setAttribute("currPage", page);
			List<Object> list = page.getQueryResult();
			if (list != null) {
				for (Object obj : list) {
					AdviceAndResponse aar = (AdviceAndResponse) obj;
					// 将数据库中的status转换为中文
					String statusvalue = (String) (BusnDataDir
							.getMapKeyValue("systemConfig.adviceAndResponseStatus")
							.get(aar.getStatus()));// 获取数据字典该状态的中文数据
					aar.setStatus(statusvalue);
					aar.getPlantime();

				}
			}
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				JSONUtil util = new JSONUtil(
						"cn.grgbanking.feeltm.myadvice.domain.AdviceAndResponse");
				// JSONArray array = util.toJSON(list, map);
				net.sf.json.JSONArray newJsonArray = util.toJSONByJsonlib(list,
						map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));// 总页数
				input.put("recordCount", String.valueOf(page.getRecordCount()));// 记录条数
				input.put("jsonObj", newJsonArray);
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			}
			request.setAttribute("advicelist", list);
			return "query";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 查看详情
	public String details() {
		try {
			String adviceid = request.getParameter("ids");// 获取从界面传过来的建议的id
			String sql = "  from  AdviceAndResponse  a  where  a.id='"
					+ adviceid + "'";
			List<AdviceAndResponse> list = adviceAndResponseService
					.findAllAdvice(sql);
			// 从数据库中获取 建议执行的状态值
			// /Map adviceARStautsMap =
			// BusnDataDir.getMapKeyValue("systemConfig.adviceAndResponseStatus");

			// 数据库中意见状态是 英文 将英文转换为中文
			if (list != null) {
				for (Object obj : list) {
					AdviceAndResponse aar = (AdviceAndResponse) obj;
					// 将数据库中的status转换为中文
					String statusvalue = (String) (BusnDataDir
							.getMapKeyValue("systemConfig.adviceAndResponseStatus")
							.get(aar.getStatus()));// 获取数据字典该状态的中文数据
					aar.setStatus(statusvalue);
					aar.getPlantime();

				}
			}
			// System.out.println("数据字典中的值"+adviceARStautsMap);
			// request.setAttribute("adviceARStautsMap", adviceARStautsMap);
			request.setAttribute("advice", list);
			return "details";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 点击修改按钮
	public String modify() {
		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);

			SysUser usr = staffInfoService.findUserByUserid(userModel
					.getUserid());
			request.setAttribute("curUser", usr);
			// 判断是否是管理员
			boolean isAdmin = UserRoleConfig.upAdminManager(userModel);
			if (isAdmin == false) {
				request.setAttribute("hasEditStatuRight", "no");

			} else {
				request.setAttribute("hasEditStatuRight", "yes");

			}
			String adviceid = request.getParameter("ids");// 获取从界面传过来的建议的id
			String sql = "  from  AdviceAndResponse  a  where  a.id='"
					+ adviceid + "'";
			List<AdviceAndResponse> list = adviceAndResponseService
					.findAllAdvice(sql);

			// 从数据库中获取 建议执行的状态值
			Map adviceARStautsMap = BusnDataDir
					.getMapKeyValue("systemConfig.adviceAndResponseStatus");

			request.setAttribute("adviceARStautsMap", adviceARStautsMap);
			request.setAttribute("advice", list);
			return "modify";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 更新数据操作
	public String update() {
		String msg = "";
		/*
		 * 1 获取要修改的建议的id 系统管理员的name 2 当前登录的用户 与 意见提出人 进行比较 若相等 则可以修改该条建议的conten
		 * 和 tel 判断当前的登录用户是不是系统管理员 是 则可以修改 该条记录的plantime和status
		 */

		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			SysUser usr = staffInfoService.findUserByUserid(userModel
					.getUserid());
			String curuser = usr.getUsername().trim();// 获取当前登录用户
			String adviceid = adviceandresponse.getId();
			String aduser = adviceandresponse.getAdviceMan().trim();// 意见提出人
			// 局部更新
			String content = adviceandresponse.getContent();
			String status = adviceandresponse.getStatus();
			String tel = adviceandresponse.getTel();
			String reply = adviceandresponse.getReply();
			String email = adviceandresponse.getEmail();
			Date plantime = adviceandresponse.getPlantime();
			String ptime = "";
			if (plantime != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				ptime = sdf.format(plantime);
			}

			// 判断当前的用户是不是管理员
			boolean isAdmin = UserRoleConfig.upAdminManager(userModel);

			if (aduser.equals(curuser) || isAdmin) {
				boolean flag = adviceAndResponseService.updateAll(adviceid,
						content, status, tel, ptime, reply== null ? "":reply.trim(), email);

				if (flag = true) {
					MsgBox msgBox = new MsgBox(request, getText("更新数据成功") + msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("update.ok"));
				}
				return "msgBox";
			} else {
				MsgBox msgBox = new MsgBox(request, getText("您不能修改此数据") + msg);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("update.ok"));
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, "更新数据错误!",
					new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
	}

	// 删除意见
	@SuppressWarnings("unused")
	public String delete() {
		String msg = "";
		try {
			String adviceId = request.getParameter("ids");
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			SysUser curuser = staffInfoService.findUserByUserid(userModel
					.getUserid());// 当前登录用户
			String curuserId = curuser.getUserid();// 当前登录用户的id

			String sql = "  from  AdviceAndResponse  a  where  a.id='"
					+ adviceId + "'";
			List<AdviceAndResponse> list = new ArrayList<AdviceAndResponse>();
			list = adviceAndResponseService.findAllAdvice(sql);
			String adviceman = list.get(0).getAdviceMan(); // 意见提出者名字
			SysUser usr = adviceAndResponseService
					.findUserByUsername(adviceman);
			String advicemanid = usr.getUserid();// 意见提出人的id
			// 根据名字到SysUser查找 给名字的id
			if (advicemanid.equals(curuserId)) { // 只能删除自己填写的建议 根据名字
				boolean flag = adviceAndResponseService
						.deleteAdviceInfo(adviceId);
				if (flag = true) {
					MsgBox msgBox = new MsgBox(request, getText("删除数据成功") + msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("delete.ok"));
				}
			} else {
				MsgBox msgBox = new MsgBox(request, getText("只能删除自己提出的意见")
						+ msg);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("delete.fail"));
			}

			return "msgBox";
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, "删除错误!",
					new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
	}

	// 添加建议和反馈的数据信息
	public String add() {
		String msg = "";
		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);

			if (userModel != null) {
				request.setAttribute("longin", "yes");
				SysUser usr = staffInfoService.findUserByUserid(userModel
						.getUserid());
				String username = usr.getUsername();
				request.setAttribute("curUser", username);
				String useremail = usr.getEmail();
				request.setAttribute("usremail", useremail);
				String mobile = usr.getMobile();
				if (mobile != null && !"".equals(mobile)) {
					request.setAttribute("usrtel", mobile);
				} else {
					mobile = "";
					request.setAttribute("usrtel", mobile);
				}

			} else {
				request.setAttribute("longin", "no");
				request.setAttribute("curUser", "");
				request.setAttribute("usremail", "");
				/*
				 * SysUser usr=staffInfoService.findUserByUsername("admin");
				 * String name=usr.getUsername(); System.out.println(name);
				 */
				request.setAttribute("usrtel", "");
			}

			return "add";
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("数据添加失败") + msg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.fail"));
			return "msgBox";
		}
	}

	// 如果没有登录 则通过 用户输入的信息 通过下面函数获取他的信息 回传到添加数据的页面
	public void search() {
		try {
			String username = request.getParameter("adviceMan");
			SysUser usr = adviceAndResponseService.findUserByUsername(username);
			// 将数据转换成json格式
			JSONObject usrJson = new JSONObject();
			if (usr != null) {
				usrJson.put("email", usr.getEmail());
				usrJson.put("phone", usr.getMobile());
				CommonUtil.ajaxPrint(usrJson.toString());
			} else {
				usrJson.put("empty", "empty");
				CommonUtil.ajaxPrint(usrJson.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 进入页面时，用autocomplete控件加载全部人员信息
	 */
	public void searchAllUser() {
		try {
			List<SysUser> users = staffInfoService.listAllUser();
			List<AdviceUserBean> adviceUsers = new ArrayList<AdviceUserBean>();
			if (users != null) {
				for (SysUser user : users) {
					adviceUsers.add(new AdviceUserBean(user));
				}
			}
			// 将数据转换成json格式
			String json = JSONArray.fromObject(adviceUsers).toString();
			CommonUtil.ajaxPrint(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* 保存添加的意见和建议的信息 */
	public String save() {
		String msg = "";
		try {
			boolean flag = false;
			Date date = new Date();
			adviceandresponse.setTime(date);// 意见提出的时间
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			// 用户有登录
			if (userModel != null) {
				SysUser usr = staffInfoService.findUserByUserid(userModel
						.getUserid());// 意见提出人的userid
				String advanceman = usr.getUsername();// 登录者的姓名
				adviceandresponse.setAdviceMan(advanceman);
				String userOa = usr.getUserid();// OA账号
				String username = usr.getUsername();// 姓名
				String aduserId = userOa + username;// 姓名+OA账号
				adviceandresponse.setUserId(aduserId);
			} else {
				// 用户没有登录
				String username = adviceandresponse.getAdviceMan();
				SysUser usr = adviceAndResponseService
						.findUserByUsername(username);
				String advanceman = usr.getUsername();
				adviceandresponse.setAdviceMan(advanceman);
				String userOa = usr.getUserid();// OA账号
				String username1 = usr.getUsername();// 姓名
				String aduserId = userOa + "-" + username1;// 姓名+OA账号
				adviceandresponse.setUserId(aduserId);
			}
			adviceandresponse
					.setContent(adviceandresponse.getContent() == null ? ""
							: adviceandresponse.getContent().trim());
			adviceandresponse
					.setReply(adviceandresponse.getReply() == null ? ""
							: adviceandresponse.getReply().trim());

			flag = adviceAndResponseService
					.addAdviceandResponse(adviceandresponse);
			if (flag = true) {
				MsgBox msgBox = new MsgBox(request, getText("添加数据成功") + msg);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			}
			return "msgBox";
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("添加数据失败") + msg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.fail"));
			return "msgBox";
		}

	}

	public AdviceAndResponse getAdviceandresponse() {
		return adviceandresponse;
	}

	public void setAdviceandresponse(AdviceAndResponse adviceandresponse) {
		this.adviceandresponse = adviceandresponse;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
