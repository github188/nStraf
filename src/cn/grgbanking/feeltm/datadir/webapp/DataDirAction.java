package cn.grgbanking.feeltm.datadir.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.datadir.service.DataDirService;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class DataDirAction extends BaseAction {

	private DataDirService dataDirService;
	private BusnDataDir busnDataDir;

	public BusnDataDir getBusnDataDir() {
		return busnDataDir;
	}

	public void setBusnDataDir(BusnDataDir busnDataDir) {
		this.busnDataDir = busnDataDir;
	}

	/** identifier field */
	private String id;

	/** persistent field */
	private String parentid;

	/** persistent field */
	private String key;

	/** nullable persistent field */
	private String value;

	/** persistent field */
	private int order;

	/** persistent field */
	private int childnum;

	/** persistent field */
	private String note;

	/** nullable persistent field */
	private String noteEn;

	public void refresh() throws Exception {
		list();
	}

	public String list() throws Exception {
		try {
			String menuid = request.getParameter("menuid");
			String action = request.getParameter("action");

			if (menuid == null)
				menuid = (String) request.getSession().getAttribute("dir.menuid");
			request.getSession().setAttribute("dir.menuid", menuid);
			String parentid = request.getParameter("parentid");
			String id = request.getParameter("id");

			SysDatadir sysDataDir = new SysDatadir();
			sysDataDir.setParentid(parentid);
			sysDataDir.setId(id);
			if (action != null && action.equals("back"))
				sysDataDir.setParentid(dataDirService.getParentId(sysDataDir
						.getId()));

			if (sysDataDir.getParentid() == null) {
				sysDataDir.setParentid(dataDirService.TOP_PARENTID);
			}

			List childList = dataDirService
					.queryChildList(sysDataDir.getParentid());
			ActionContext.getContext().put("dirList", childList);
			ActionContext.getContext().put("dirNavigation",
					dataDirService.getNavigation(sysDataDir.getParentid()));
			ActionContext.getContext().put("sysDataDir", sysDataDir);
			List operList = (List) ActionContext.getContext().get("dir.operList");
			ActionContext.getContext().put("dir.operatorList", operList);

			String from = request.getParameter("from");
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < childList.size(); i++) {
					l.add((Object) childList.get(i));
				}

				Map map = new HashMap();
				map.put("recordCount", l.size());

				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysDatadir");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", 1);
				input.put("recordCount", l.size());
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);

				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}

		return "list";
	}

	public String add() throws Exception {
		SysDatadir sysDatadir = new SysDatadir();
		this.parentid = request.getParameter("parentid");
		sysDatadir.setParentid(getParentid());
		sysDatadir.setId(null);

		ActionContext.getContext().put("sysDatadir", sysDatadir);

		return "add";
	}

	public String modify() throws Exception {
		SysDatadir sysDatadir = new SysDatadir();
		this.id = request.getParameter("id");
		sysDatadir.setId(getId());
		SysDatadir dir = (SysDatadir) dataDirService.findById(sysDatadir
				.getId());
		ActionContext.getContext().put("sysDatadir", dir);
		ActionContext.getContext().put("dirPath",
				dataDirService.getPath(dir.getId()));
		this.setPath(dataDirService.getAllPath(dir.getId()));
		return "modify";
	}

	public String sort() throws Exception {
		SysDatadir sysDatadir = new SysDatadir();
		this.parentid = request.getParameter("parentid");
		sysDatadir.setParentid(parentid);

		if (sysDatadir.getParentid() == null)
			sysDatadir.setParentid(dataDirService.TOP_PARENTID);
		List dataDirInfoLst = this.dataDirService.queryChildList(sysDatadir
				.getParentid());
		request.setAttribute("sortRequest", sysDatadir);
		request.setAttribute("dataDirInfoLst", dataDirInfoLst);

		return "sort";
	}

	public String sortSave() throws Exception {
		// SysDatadir dir=new SysDatadir();
		// BeanUtils.copyProperties(dir,form);
		String idList = request.getParameter("idList");
		int iResult = this.dataDirService.reorderItems(idList);
		// ActionMessages msgs=new ActionMessages();
		MsgBox msgBox = null;
		if (iResult <= 0) {
			// msgs.add("noExist",new ActionMessage("menuForm.error.notExist"));
			msgBox = new MsgBox(request, getText("menuForm.error.notExist"));
		} else {
			SysLog.operLog(request, Constants.OPER_SORT, null);// 记录日志
			// msgs.add("saveOk",new ActionMessage("save.ok"));
			msgBox = new MsgBox(request, getText("save.ok"));
		}

		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	public String delselected() throws Exception {
		// UserModel userModel = (UserModel) request.getSession().getAttribute(
		// Constants.LOGIN_USER_KEY);
		SysDatadir dir = new SysDatadir();
		this.id = request.getParameter("id");
		dir.setId(id);

		int iCount = dataDirService.delAll(dir.getId());
		SysLog.operLog(request, Constants.OPER_DELETE_VALUE, dir.getNote());// 记录日志
		SysLog.info(request, "del dataDir:" + dir.getId());
		MsgBox msgBox = new MsgBox(request, getText("dataDir.del.ok"),
				"datadir", new String[] { String.valueOf(iCount) });
		busnDataDir.loadDataDir();// 更新数据字典
		busnDataDir.loadDataKeyValue();
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	public String save() throws Exception {
		// UserModel userModel = (UserModel) request.getSession().getAttribute(
		// Constants.LOGIN_USER_KEY);
		SysDatadir dir = new SysDatadir();
		getParentid();
		// BeanUtils.copyProperties(dir, form);
		dir.setChildnum(childnum);
		dir.setId(id);
		dir.setKey(key);
		dir.setNote(note);
		dir.setNoteEn(noteEn);
		dir.setOrder(order);
		dir.setParentid(parentid);
		dir.setValue(value);

		// dir.setParentid(request.getParameter("parentid"));

		// ActionMessages msgs = new ActionMessages();
		MsgBox msgBox = null;

		String action = request.getParameter("control");
		// String newAnOther = request.getParameter("newAnOther");

		if (action.equals("add")) {
			int iResult = dataDirService.addItem(dir);
			switch (iResult) {
			case 0:
				// msgs.add("sameKey", new
				// ActionMessage("dataDir.error.sameKey",
				// dir.getKey()));
				msgBox = new MsgBox(request, getText("dataDir.error.sameKey"));
				SysLog.error(request, "failed to add dataDir:" + dir.getKey());
				this.addActionMessage(getText("dataDir.error.sameKey"));
				break;
			case 1:
				// msgs.add("addOk", new ActionMessage("save.ok"));
				msgBox = new MsgBox(request, getText("save.ok"));
				SysLog
						.operLog(request, Constants.OPER_ADD_VALUE, dir
								.getNote());// 记录日志
				SysLog.info(request, "add dataDir:" + dir.getNote()
						+ "successfully.");
				this.addActionMessage(getText("save.ok"));
				busnDataDir.loadDataDir();// 更新数据字典
				busnDataDir.loadDataKeyValue();
				break;
			default:
			}
			
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			String newAnOther = request.getParameter("newAnOther");
			if (newAnOther.equals("true")) { 	  
				  return add();
			  }
			// if (newAnOther.equals("true") || iResult != 1) {
			// // this.saveMessages(request,msgs);
			//
			// request.setAttribute("flag", "add");
			// return mapping.findForward(action);
			// }
		} else {
			if (action.equals("modify")) {
				dir.setId(request.getParameter("id"));
				int iResult = dataDirService.updateItem(dir);
				switch (iResult) {
				case -1:
					// msgs.add("noExist", new ActionMessage(
					// "dataDir.error.notExist"));
					msgBox = new MsgBox(request,
							getText("dataDir.error.notExist"));
					break;
				case 0:
					// msgs.add("sameKey", new ActionMessage(
					// "dataDir.error.sameKey", dir.getKey()));
					msgBox = new MsgBox(request,
							getText("dataDir.error.sameKey"));
					SysLog.error(request, "failed to update dataDir:"
							+ dir.getKey());
					break;
				case 1:
					// msgs.add("saveOk", new ActionMessage("save.ok"));
					msgBox = new MsgBox(request, getText("save.ok"));
					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, dir
							.getNote());// 记录日志
					SysLog.info(request, "update dataDir:" + dir.getNote()
							+ "successfully.");
					busnDataDir.loadDataDir();// 更新数据字典
					busnDataDir.loadDataKeyValue();
					break;
				default:
				}
				// if (iResult != 1) {
				// // this.saveMessages(request,msgs);
				// return mapping.findForward(action);
				// }
			} else {
				if (action.equals("del")) {
					int iCount = dataDirService.delAll(dir.getId());
					SysLog.operLog(request, Constants.OPER_DELETE_VALUE, dir
							.getNote());// 记录日志
					SysLog.info(request, "del dataDir:" + dir.getId());
					msgBox = new MsgBox(request, "dataDir.del.ok", "datadir",
							new String[] { String.valueOf(iCount) });
					busnDataDir.loadData();// 更新数据字典
					busnDataDir.loadDataKeyValue();
				}
			}
		}// end else

		// MsgBox msgBox = new MsgBox(request, msgs);
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	public DataDirService getdataDirService() {
		return dataDirService;
	}

	public void setdataDirService(DataDirService dataDirService) {
		this.dataDirService = dataDirService;
	}

	public DataDirService getDataDirService() {
		return dataDirService;
	}

	public void setDataDirService(DataDirService dataDirService) {
		this.dataDirService = dataDirService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getChildnum() {
		return childnum;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNoteEn() {
		return noteEn;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}
	//field path was added by cjjie on 2010-11-15
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
