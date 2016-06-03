package cn.grgbanking.feeltm.prj.webapp;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.prj.domain.TesterDetail;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class TesterDetailAction extends BaseAction{
	private TesterDetailDao testerDao;
	private ProjectDbService dbService;
	private String prjName;
	private String versionNO;
	private String start;
	private String end;
	private String testerName;      //testerName
	private PrjDetailDao detailDao;
	private List<String> prjs;
	private List<String> devNames;
	private  TesterDetail prjtest;
	private Map<String,String> map;
	private List<String> prjTypes;

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String query() throws Exception {
		prjTypes=dbService.getPrjTypes();
		if(session.get("globalDB")==null){
			ProjectDB prjDB=dbService.getDB("ATMC");  //default ATMC
			session.put("globalDB", prjDB);
		}
		prjs=detailDao.getPrjects(session);
		//devNames=testerDao.getNames(session);
		map=testerDao.getNames();
		try {
			String from = request.getParameter("from");
			if(("全选").equals(versionNO)){
				versionNO="";
			}
			if(prjName!=null&&prjName.contains("$jia$")){
				prjName = prjName.replace("$jia$", "+");
			}
			if(versionNO!=null&&versionNO.contains("$jia$")){
				versionNO = versionNO.replace("$jia$", "+");
			}
			List list=testerDao.getDetails(prjName, versionNO, start, end, testerName, session);
			Page page=new Page();
			page.setQueryResult(list);
			page.setPageCount(1);
			page.setCurrentPageNo(1);
			page.setPageSize(50);	
			page.setRecordCount(list.size());
			request.setAttribute("currPage", page);
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.prj.domain.TesterDetail");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("devList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	
	public String  show(){
		String req=request.getParameter("req");
		String[] de=req.split("@");
		versionNO=de[0];
		start=de[1];
		end=de[2];
		prjName=de[3];
		testerName=de[4];
		if(start.equals("fq")){
			start=null;
		}
		if(end.equals("fq")){
			end=null;
		}
		if(prjName!=null&&prjName.contains("$jia$")){
			prjName = prjName.replace("$jia$", "+");
		}
		if(versionNO!=null&&versionNO.contains("$jia$")){
			versionNO = versionNO.replace("$jia$", "+");
		}
		try {
			prjtest=testerDao.getDetail(prjName, versionNO, start, end,testerName,session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}
	
	//disable
	public String queryTesterNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		List<String> list=testerDao.getNames(session);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	
	

	public TesterDetailDao getTesterDao() {
		return testerDao;
	}

	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}


	
	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public List<String> getPrjs() {
		return prjs;
	}

	public void setPrjs(List<String> prjs) {
		this.prjs = prjs;
	}

	public List<String> getDevNames() {
		return devNames;
	}

	public void setDevNames(List<String> devNames) {
		this.devNames = devNames;
	}

	public ProjectDbService getDbService() {
		return dbService;
	}

	public void setDbService(ProjectDbService dbService) {
		this.dbService = dbService;
	}

	public PrjDetailDao getDetailDao() {
		return detailDao;
	}

	public void setDetailDao(PrjDetailDao detailDao) {
		this.detailDao = detailDao;
	}

	public TesterDetail getPrjtest() {
		return prjtest;
	}

	public void setPrjtest(TesterDetail prjtest) {
		this.prjtest = prjtest;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public List<String> getPrjTypes() {
		return prjTypes;
	}

	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}

	
	
	
	
}
