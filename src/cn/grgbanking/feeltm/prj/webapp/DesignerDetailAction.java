package cn.grgbanking.feeltm.prj.webapp;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.DesignerDetailDao;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.prj.domain.DesignerDetail;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

public class DesignerDetailAction extends BaseAction{
	private DesignerDetailDao designerDao;
	private TesterDetailDao testerDao;
	private ProjectDbService dbService;
	private String prjName;
	private String start;
	private String end;
	private String designerName;      //testerName
	private PrjDetailDao detailDao;
	private List<String> prjs;
	private DesignerDetail detail;
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
		map=testerDao.getNames();
		try {
			String from = request.getParameter("from");
			if(prjName!=null&&prjName.contains("$jia$")){
				prjName = prjName.replace("$jia$", "+");
			}
			List list=designerDao.getDetails(prjName, start, end, designerName, session);
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
						"cn.grgbanking.feeltm.prj.domain.DesignerDetail");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("designerList", list);
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
		start=de[0];
		end=de[1];
		prjName=de[2];
		designerName=de[3];
		if(start.equals("fq")){
			start=null;
		}
		if(end.equals("fq")){
			end=null;
		}
		if(prjName!=null&&prjName.contains("$jia$")){
			prjName = prjName.replace("$jia$", "+");
		}
		try {
			detail=designerDao.getDetail(prjName, start, end,designerName,session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}
	

	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
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
	
	public List<String> getPrjs() {
		return prjs;
	}
	public void setPrjs(List<String> prjs) {
		this.prjs = prjs;
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


	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public DesignerDetailDao getDesignerDao() {
		return designerDao;
	}

	public void setDesignerDao(DesignerDetailDao designerDao) {
		this.designerDao = designerDao;
	}

	public TesterDetailDao getTesterDao() {
		return testerDao;
	}

	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
	}

	public String getDesignerName() {
		return designerName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public DesignerDetail getDetail() {
		return detail;
	}
	

	public List<String> getPrjTypes() {
		return prjTypes;
	}

	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}

	public void setDetail(DesignerDetail detail) {
		this.detail = detail;
	}
}
