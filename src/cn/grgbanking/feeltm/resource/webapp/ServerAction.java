package cn.grgbanking.feeltm.resource.webapp;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.Server;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.resource.service.ServerService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ServerAction extends BaseAction{
	private ServerService serverService;
	private String deviceName;
	private String netIP;
	private Server server;
	private List<Server> servers;
	
	public String add(){
		return "add";
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			flag=serverService.add(server);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
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
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					Server temp = serverService.getServerById(sids[i]);
					serverService.delete(temp);
//					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, temp
//							.getTaskDesc());
//					SysLog.info("User:" + userModel.getUserid()
//							+ " to delete a SpecialRegulation: "
//							+ temp.getTaskDesc());

					iCount++;
				}
			}

			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public String edit() throws Exception {
		try {
			String ids = request.getParameter("ids");
			server=serverService.getServerById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			
			boolean flag=serverService.update(server);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
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

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public String query() throws Exception {
		try {
			String from = request.getParameter("from");
//			if(toolName!=null&&(!"".equals(toolName.trim()))){
				
//			}
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = serverService.getPage(deviceName,netIP, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
//				for (int i = 0; i < list.size(); i++) {
//					TestTool t=(TestTool)list.get(i);
//					t.setDateString(fo.format(report.getStartDate()));
//				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.Server");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("serverList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			server=serverService.getServerById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	
	
	
	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getNetIP() {
		return netIP;
	}

	public void setNetIP(String netIP) {
		this.netIP = netIP;
	}

	public ServerService getServerService() {
		return serverService;
	}

	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	
	
	
}
