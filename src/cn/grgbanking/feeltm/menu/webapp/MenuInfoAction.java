package cn.grgbanking.feeltm.menu.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONArray;
import net.sf.json.JSONObject;

import cn.grgbanking.commonPlatform.utils.CommonUtil;
import cn.grgbanking.feeltm.domain.MenuFunction;
import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.menu.service.MenuInfoService;
import cn.grgbanking.feeltm.menu.webapp.vo.MenuRedirectVo;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.BeanUtils;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
public class MenuInfoAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private MenuInfoService menuInfoService;

	/** identifier field */
	private String menuid;

	/** persistent field */
	private String parentid;

	/** persistent field */
	private String menuitem;

	/** persistent field */
	private int order;

	/** nullable persistent field */
	private String actionto;

	/** persistent field */
	private int target;

	/** persistent field */
	private int childnum;

	/** persistent field */
	private int floor;

	/** nullable persistent field */
	private String pic;

	/**
	 * 功能信息列表
	 * 
	 * @author zyding
	 */
	public String list() throws Exception {
		String menuid = request.getParameter("menuid");
		request.getSession().setAttribute("menuinfo.menuid", menuid);

		if (menuInfoService == null) {
			System.out.println("manager is null");
		}

		MenuInfo menu = new MenuInfo();
		// menu.setMenuid(menuid);
		// menu.setChildnum(childnum);
		// BeanUtils.copyProperties(menu, form);

		if (menu.getParentid() == null) {
			menu.setParentid(menuInfoService.TOP_PARENTID);
			menu.setFloor(1);
		}
		List childList = menuInfoService
				.queryChildList(menuInfoService.TOP_PARENTID);
		ActionContext.getContext().put("menuList", childList);
		ActionContext.getContext().put("menuNavigation",
				menuInfoService.getNavigation(menuInfoService.TOP_PARENTID));
		ActionContext.getContext().put("menuInfo", menu);

		// ActionContext.getContext().put("parentid",
		// menuInfoService.TOP_PARENTID);
		return "list";
	}

	public void refresh() throws Exception {
		search();
	}

	public String search() throws Exception {
		try {
			String menuid = request.getParameter("menuid");
			// request.getSession().setAttribute("menuinfo.menuid", menuid);
			if (menuInfoService == null) {
				System.out.println("manager is null");
			}
			MenuInfo menu = new MenuInfo();
			String parentid = request.getParameter("parentid");
			menu.setParentid(parentid);
			menu.setMenuid(menuid);
			if (menu.getMenuid() == null && parentid == null) {
				menu.setParentid(menuInfoService.TOP_PARENTID);
				menu.setFloor(1);
			} else if (request.getParameter("control") != null
					&& request.getParameter("control").equals("back")) {
				MenuInfo menuinfo = menuInfoService.findById(menu.getMenuid());
				if (menuinfo == null) {
					menu.setParentid(menuInfoService.TOP_PARENTID);
					List childList = menuInfoService.queryChildList(menu
							.getParentid());
					ActionContext.getContext().put("menuList", childList);
					ActionContext.getContext().put("menuNavigation",
							menuInfoService.getNavigation(menu.getParentid()));
					ActionContext.getContext().put("menuInfo", menu);
					return "list";
				}

				menu.setParentid(menuinfo.getParentid());
				menu.setFloor(menuinfo.getFloor());
			} else if (menu.getParentid() == null) {
				MenuInfo menuinfo = menuInfoService.findById(menu.getMenuid());
				menu.setParentid(menuinfo.getMenuid());
				menu.setFloor(menuinfo.getFloor() + 1);
			}
			List childList = menuInfoService.queryChildList(menu.getParentid());
			ActionContext.getContext().put("menuList", childList);
			ActionContext.getContext().put("menuNavigation",
					menuInfoService.getNavigation(menu.getParentid()));
			ActionContext.getContext().put("menuInfo", menu);
			String from = request.getParameter("from");
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < childList.size(); i++) {
					l.add((Object) childList.get(i));
				}
				Map map = new HashMap();
				map.put("recordCount", l.size());

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.MenuInfo");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", 1);
				input.put("recordCount", l.size());
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);

				return "";
			} else
				return "list";
		} catch (Exception e) {
			// TODO: handle exception
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public String add() throws Exception {
		MenuInfo menu = new MenuInfo();
		this.parentid = request.getParameter("parentid");
		menu.setParentid(this.getParentid());
		if (parentid.equals("0")) {
			this.floor = 1;
		} else {
			this.floor = menuInfoService.findById(parentid).getFloor() + 1;
		}
		menu.setFloor(floor);

		// BeanUtils.copyProperties(menu, form);
		menu.setMenuid(null);// 在menuInfoEdit.jsp页面会判断menu.id的值，若为空，则为add，否则为modify
		// menu.setParentid(request.getParameter("parentid"));
		ActionContext.getContext().put("menuInfo", menu);

		return "add";
	}

	public String save() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String newAnOther = request.getParameter("newAnOther");
		MenuInfo menuInfo = new MenuInfo();// //增加MenuInfoVO类的目的只是为了存储menuInfoEdit.jsp页面传过来newAnOther。
		// MenuInfoVO
		// MenuInfoVO menuInfoVO = new MenuInfoVO();
		MsgBox msgBox;

		menuInfo.setMenuitem(getMenuitem());
		menuInfo.setActionto(getActionto());
		menuInfo.setPic(getPic());
		menuInfo.setTarget(getTarget());
		menuInfo.setParentid(getParentid());
		menuInfo.setFloor(getFloor());
		menuInfo.setMenuid(menuid);

		//ActionMessages msgs = new ActionMessages();
		String strAction = request.getParameter("control");
		
		if (strAction.equals("add")) {
			menuInfo.setOrder(menuInfoService.queryChildnum(menuInfo
					.getParentid()) + 1);
			// menuInfo.setFloor(menuInfoService.findById(getParentid()).getFloor()
			// + 1);
			int iResult = menuInfoService.addItem(menuInfo);
			switch (iResult) {
			case 0:
				// msgs.add("sameKey", new
				// ActionMessage("menuForm.error.sameKey",menuInfo.getMenuid()));
				msgBox = new MsgBox(request, getText("menuForm.error.sameKey"));
				this.addActionMessage(getText("menuForm.error.sameKey"));
				break;
			case 1:
				SysLog.operLog(request, Constants.OPER_ADD_VALUE, menuInfo
						.getMenuitem());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " to add a menu: " + menuInfo.getMenuitem());
				msgBox = new MsgBox(request, getText("save.ok"));
				this.addActionMessage(getText("save.ok"));
				break;
			default:
				msgBox = new MsgBox(request, getText("save.ok"));
				this.addActionMessage(getText("save.ok"));
			}
			
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			
			  if ("1".equals(newAnOther)) { 
				  MenuInfo menu = new MenuInfo();
				  menu.setParentid(menuInfo.getParentid());
				  if (menuInfo.getParentid().equals("0")) {
					  menu.setFloor(1);
				  } else {
						menu.setFloor( menuInfoService.findById(menuInfo.getParentid()).getFloor() + 1);	
				  }
				
				  ActionContext.getContext().put("menuInfo", menu);
				  
				  return "add"; 
			  }
			  
		} else {
			int iResult = menuInfoService.updateItem(menuInfo);
			switch (iResult) {
			case -1:
				// msgs.add("noExist", new
				// ActionMessage("menuForm.error.notExist"));
				msgBox = new MsgBox(request, getText("menuForm.error.notExist"));
				this.addActionMessage(getText("menuForm.error.notExist"));
				break;
			case 0:
				msgBox = new MsgBox(request, getText("menuForm.error.sameKey"));
				// msgs.add("sameKey", new
				// ActionMessage("menuForm.error.sameKey",menuInfo.getMenuid()));
				break;
			case 1:
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, menuInfo
						.getMenuitem());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " to modify a menu: " + menuInfo.getMenuitem());
				// msgs.add("updateOk", new ActionMessage("save.ok"));
				msgBox = new MsgBox(request, getText("save.ok"));
				break;
			default:
				msgBox = new MsgBox(request, getText("save.ok"));
			}
			if (iResult != 1) {
				// this.saveMessages(request, msgs);
				msgBox = new MsgBox(request, getText("save.ok"));
				return "modify";
			}
		}
		 //msgBox = new MsgBox(request, "msgs");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		
		
		return "msgBox";
	}

	public String update() throws Exception {
		MenuInfo menu = new MenuInfo();
		String menuid = request.getParameter("menuid");
		MenuInfo obj = (MenuInfo) menuInfoService.findById(menuid);
		BeanUtils.copyProperties(menu, obj);
		ActionContext.getContext().put("menuPath",
				menuInfoService.getPath(menu.getMenuid()));
		ActionContext.getContext().put("menuInfo", menu);

		return "update";
	}

	public String delselected() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);

		MenuInfo menu = new MenuInfo();
		// BeanUtils.copyProperties(menu, form);
		String menuids = request.getParameter("menuid");
		menu.setMenuid(menuids);
		int iCount = 0;
		String[] Menuids = menu.getMenuid().split(",");
		if (Menuids != null) {
			for (int i = 0; i < Menuids.length; i++) {
				MenuInfo menuInfo = menuInfoService.findByMenuId(Menuids[i]);
				menuInfoService.delAll(Menuids[i]);
				SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, menuInfo
						.getMenuitem());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " to delete a menu: " + menuInfo.getMenuitem());
				iCount++;
			}
		}
		MsgBox msgBox = new MsgBox(request, getText("menuInfo.del.ok"), "menu",
				new String[] { String.valueOf(iCount) });
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	public String setting() throws Exception {
		MenuInfo objInfo = new MenuInfo();
		String menuid = request.getParameter("menuid");
		request.setAttribute("temp_menuid", menuid);
		objInfo.setMenuid(menuid);
		List menuOperLst = menuInfoService.queryMenuOperate(objInfo, menuid);

		ActionContext.getContext().put("menuOperLst", menuOperLst);

		return "setting";
	}

	public String saveOperate() throws Exception {
		String[] temp = request.getParameterValues("checkbox");
		String menuid = request.getParameter("menuid");
		List<MenuFunction> menuFunLst = new ArrayList<MenuFunction>();
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				MenuFunction menufun = new MenuFunction();
				menufun.setOperid(temp[i]);
				menufun.setMenuid(menuid);
				menuFunLst.add(menufun);
			}
		}
		this.menuInfoService.updateMenuOper(menuid, temp);
		SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, null);// 记录日志
		SysLog.info(request, "update menu" + menuid);
		MsgBox msgBox = new MsgBox(request, getText("save.ok"), "menu");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	public ActionForward saveOperate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] temp = request.getParameterValues("checkbox");
		String menuid = request.getParameter("menuid");
		List<MenuFunction> menuFunLst = new ArrayList<MenuFunction>();
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				MenuFunction menufun = new MenuFunction();
				menufun.setOperid(temp[i]);
				menufun.setMenuid(menuid);
				menuFunLst.add(menufun);
			}
		}

		this.menuInfoService.updateMenuOper(menuid, temp);
		SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, null);// 记录日志
		SysLog.info(request, "update menu" + menuid);
		ActionMessages msgs = new ActionMessages();
		msgs.add("saveOk", new ActionMessage("save.ok"));
		MsgBox msgBox = new MsgBox(request, msgs);
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return mapping.findForward("msgBox");
	}

	public String saveSort() throws Exception {
		String idList = request.getParameter("idList");
		int iResult = menuInfoService.reorderItems(idList);
		MsgBox msgBox = null;
		// ActionMessages msgs = new ActionMessages();
		if (iResult <= 0) {
			// msgs.add("noExist", new
			// ActionMessage("menuForm.error.notExist"));
			msgBox = new MsgBox(request, getText("menuForm.error.notExist"));
		} else {
			SysLog.operLog(request, Constants.OPER_SORT, null);// 记录日志
			// msgs.add("saveOk", new ActionMessage("save.ok"));
		}
		msgBox = new MsgBox(request, getText("save.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	public String sort() throws Exception {

		String menuid = request.getParameter("menuid");
		List menuInfoLst = menuInfoService.queryChildList(menuid);

		ActionContext.getContext().put("menuInfoLst", menuInfoLst);
		// request.getSession().setAttribute("menuInfoLst", menuInfoLst);
		return "sort";
	}
   
	//点击页面菜单连接跳转时，获取父级菜单信息
	public void getParentMenuInfo() throws Exception {
	String actionto = request.getParameter("actionto");
	MenuRedirectVo menuRedirectVo = menuInfoService.getParentMenuInfo(actionto);
	try {
		JSONObject json = JSONObject.fromObject(menuRedirectVo);	
		CommonUtil.ajaxPrint(json.toString());
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	}

	public MenuInfoService getmenuInfoService() {
		return menuInfoService;
	}

	public void setmenuInfoService(MenuInfoService menuInfoService) {
		this.menuInfoService = menuInfoService;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getMenuitem() {
		return menuitem;
	}

	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getActionto() {
		return actionto;
	}

	public void setActionto(String actionto) {
		this.actionto = actionto;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getChildnum() {
		return childnum;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}