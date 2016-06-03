package cn.grgbanking.feeltm.menu.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.menu.service.MenuOperateService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class MenuOperateAction extends BaseAction {
	private static final long serialVersionUID = -5051647022334517802L;
	private MenuOperateService menuOperateService;
	/** identifier field */
	private String operid;

	/** identifier field */
	private String opername;

	/** identifier field */
	private String picpath;

	/** identifier field */
	private String clickname;

	/** identifier field */
	private String keys;

	/** identifier field */
	private String types;

	private String site;

	public void refresh() {
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		try {
			String from = request.getParameter("from");
			List grpLst = null;
			if (menuOperateService == null)
				System.out.println("manager is null");

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			grpLst = menuOperateService.getList(pageNum, pageSize);
			if (grpLst.isEmpty())
				System.out.println("no grp");

			ActionContext.getContext().put("grpLst", grpLst);
			// 用于分布标签
			Page page = menuOperateService.getPage(pageNum, pageSize);
			request.setAttribute("menu.currPage", page);
			// 用于功能按钮控制标签

			if (from != null && from.equals("refresh")) {
				List<Object> lst = new ArrayList<Object>();
				for (int i = 0; i < grpLst.size(); i++) {
					lst.add((Object) grpLst.get(i));
				}

				// 如果除了list之外还需要将其他数据存到JSONObject中，就需要将这些数据填充到一个map中。否则map设为null。
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.MenuOperate");
				JSONArray jsonObj = jsonUtil.toJSON(lst, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}

			return "list";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public String add() throws Exception {
		MenuOperate menuOperate = new MenuOperate();
		menuOperate.setOperid(null);

		ActionContext.getContext().put("menuOperateForm", menuOperate);

		return "add";
	}

	public String save() throws Exception {
		// String operid = request.getParameter("operid");
		 String newAnOther = request.getParameter("newAnOther");

		MenuOperate menuOperate = new MenuOperate();

		MsgBox msgBox = null;

		try {
			Object obj = menuOperateService.getMenuOperate(operid);
			if (obj != null)
				menuOperate = (MenuOperate) obj;
		} catch (Exception e1) {

		}
		if (menuOperate != null && menuOperate.getOperid() != null) {
			SysLog.info(" menu Operid " + operid + " same Key !");
			msgBox = new MsgBox(request, getText("menuForm.error.sameKey"));
			this.addActionMessage(getText("menuForm.error.sameKey"));
			//return "msgBox";
		} else {
			menuOperate.setOperid(operid);
			menuOperate.setOpername(opername);
			menuOperate.setClickname(clickname);
			menuOperate.setKeys(keys);
			menuOperate.setPicpath(picpath);
			menuOperate.setSite(site);
			menuOperate.setTypes(types);

			menuOperateService.addMenuOperate(menuOperate);
			msgBox = new MsgBox(request, getText("save.ok"));
			this.addActionMessage(getText("save.ok"));
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		if (newAnOther != null && (newAnOther.equals("true") || newAnOther.equals("1"))) {
			  return "add";
		 }
		msgBox = new MsgBox(request, getText("save.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	public String delselected() throws Exception {
		MenuOperate menuOperate = new MenuOperate();
		String operid = request.getParameter("operid");
		menuOperate.setOperid(operid);
		int iCount = menuOperateService.delAll(menuOperate.getOperid());
		MsgBox msgBox = new MsgBox(request, getText("menuOperate.del.ok"),
				"menu", new String[] { String.valueOf(iCount) });
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	public String update() throws Exception {
		String objcode = request.getParameter("objcode");
		MenuOperate obj = (MenuOperate) this.menuOperateService
				.getMenuOperate(objcode);

		ActionContext.getContext().put("menuOperate", obj);
		return "update";
	}

	public String saveupdate() throws Exception {
		String operid = request.getParameter("operid");
		MenuOperate menuOperate = new MenuOperate();
		menuOperate.setClickname(clickname);
		menuOperate.setKeys(keys);
		menuOperate.setOpername(opername);
		menuOperate.setPicpath(picpath);
		menuOperate.setSite(site);
		menuOperate.setTypes(types);
		menuOperate.setOperid(operid);

		menuOperateService.updateMenuOperate(menuOperate);

		MsgBox msgBox = new MsgBox(request, getText("save.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	public MenuOperateService getmenuOperateService() {
		return menuOperateService;
	}

	public void setmenuOperateService(MenuOperateService menuOperateService) {
		this.menuOperateService = menuOperateService;
	}

	public MenuOperateService getMenuOperateService() {
		return menuOperateService;
	}

	public void setMenuOperateService(MenuOperateService menuOperateService) {
		this.menuOperateService = menuOperateService;
	}

	public String getOperid() {
		return operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public String getOpername() {
		return opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public String getClickname() {
		return clickname;
	}

	public void setClickname(String clickname) {
		this.clickname = clickname;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}
