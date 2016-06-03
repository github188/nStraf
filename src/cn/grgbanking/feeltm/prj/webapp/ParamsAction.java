package cn.grgbanking.feeltm.prj.webapp;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.ProjectParams;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.service.ParamsService;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ParamsAction extends BaseAction{
	private ParamsService paramsService;
	private String prjName;
	private String versionNO;
	private ProjectParams prjparams;
	private List<ProjectParams> prjparamsList;
	PrjDetailDao detailDao;
	private ProjectDbService dbService;
	private List<String> prjs;
	private List<String> prjTypes;


	public String add(){
		prjTypes=dbService.getPrjTypes();
		return "add";
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			flag=paramsService.add(prjparams);
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
					ProjectParams temp = paramsService.getParamsById(sids[i]);
					paramsService.delete(temp);
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
		prjTypes=dbService.getPrjTypes();
		try {
			String ids = request.getParameter("ids");
			prjparams=paramsService.getParamsById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			
			boolean flag=paramsService.update(prjparams);
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
		prjTypes=dbService.getPrjTypes();
		if(session.get("globalDB")==null){
			ProjectDB prjDB=dbService.getDB("ATMC");  //default ATMC
			session.put("globalDB", prjDB);
		}
		prjs=detailDao.getPrjects(session);
		String prjType1=request.getParameter("prjType1");
		try {
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = paramsService.getPage(prjName,versionNO, prjType1,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.ProjectParams");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("prjparamsList", list);
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
			prjparams=paramsService.getParamsById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String prjName1=request.getParameter("prjName");
		PrintWriter out=response.getWriter();
		List<String> list=detailDao.getVersions(prjName1,session);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	public String queryPrjs() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		List<String> list=detailDao.getPrjects(session);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	public String checkExist(){
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String prjName1=request.getParameter("prjName1");
		String versionNO1=request.getParameter("versionNO1");
		try {
			PrintWriter out=response.getWriter();
			boolean flag=true;
			if(!paramsService.isExist(prjName1, versionNO1)){
				flag=false;
			}
			JSONObject obj=new JSONObject();
			obj.put("flag", flag);
			out.print(obj);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String checkPrjVersion(){
		return null;
	}
	
	public ParamsService getParamsService() {
		return paramsService;
	}
	public void setParamsService(ParamsService paramsService) {
		this.paramsService = paramsService;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}
	public ProjectParams getPrjparams() {
		return prjparams;
	}
	public void setPrjparams(ProjectParams prjparams) {
		this.prjparams = prjparams;
	}
	public List<ProjectParams> getPrjparamsList() {
		return prjparamsList;
	}
	public void setPrjparamsList(List<ProjectParams> prjparamsList) {
		this.prjparamsList = prjparamsList;
	}
	public PrjDetailDao getDetailDao() {
		return detailDao;
	}
	public void setDetailDao(PrjDetailDao detailDao) {
		this.detailDao = detailDao;
	}
	public ProjectDbService getDbService() {
		return dbService;
	}
	public void setDbService(ProjectDbService dbService) {
		this.dbService = dbService;
	}
	public List<String> getPrjs() {
		return prjs;
	}
	public void setPrjs(List<String> prjs) {
		this.prjs = prjs;
	}
	public List<String> getPrjTypes() {
		return prjTypes;
	}
	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}
	
	
	
	


	
}
