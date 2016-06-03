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
import cn.grgbanking.feeltm.prj.dao.DeveloperDetailDao;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.domain.DeveloperDetail;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

public class DeveloperDetailAction extends BaseAction{
	private DeveloperDetailDao devDao;
	private ProjectDbService dbService;
	private String start;
	private String end;
	private String devName;
	private PrjDetailDao detailDao;
	private List<String> devNames;
	private DeveloperDetail prjdev;
	private List<String> prjTypes;
	
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String query() throws Exception {
		if(session.get("globalDB")==null){
			ProjectDB prjDB=dbService.getDB("ATMC");  //default ATMC
			session.put("globalDB", prjDB);
		}
		if(devName==null){
			prjTypes=dbService.getPrjTypes();
			try{
			devNames=devDao.getNames(session);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	//	prjs=detailDao.getPrjects(session);
		try {
			String from = request.getParameter("from");
			List list=devDao.getDetails( start+" 00:00:00", end+" 23:59:59", devName, session);
			if(list.size()==0){
				DeveloperDetail detail=new DeveloperDetail();
				list.add(detail);
			}
			//List<DeveloperDetail> ds=(List<DeveloperDetail> )list;
//			for(DeveloperDetail d:ds){
//				System.out.println(d.getDevName()+" "+d.getDept()+" "+d.getQualityEvalute()+" "+d.getTotalPoint()+" "+d.getAvrFixTime()+" "+d.getBugReopenRate()+" "+d.getFixRate()+" "+d.getBugSubtotal()+" "+d.getBugTotalValue());
//			}
			//System.out.println("end");
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
						"cn.grgbanking.feeltm.prj.domain.DeveloperDetail");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				out.flush();
			//	System.out.println(out.checkError());
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
	//	versionNO=de[0];
		start=de[1];
		end=de[2];
		//prjName=de[3];
		devName=de[0];
		if(start.equals("fq")){
			start=null;
		}
		if(end.equals("fq")){
			end=null;
		}
//		if(prjName!=null&&prjName.contains("$jia$")){
//			prjName = prjName.replace("$jia$", "+");
//		}
//		if(versionNO!=null&&versionNO.contains("$jia$")){
//			versionNO = versionNO.replace("$jia$", "+");
//		}
		try {
			prjdev=devDao.getDetail( start+" 00:00:00", end+" 23:59:59", devName,session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}
	
	public String queryDevNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		List<String> list=devDao.getNames(session);
		
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	
	public DeveloperDetailDao getDevDao() {
		return devDao;
	}

	public void setDevDao(DeveloperDetailDao devDao) {
		this.devDao = devDao;
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

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
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

	public DeveloperDetail getPrjdev() {
		return prjdev;
	}

	public void setPrjdev(DeveloperDetail prjdev) {
		this.prjdev = prjdev;
	}

	public List<String> getPrjTypes() {
		return prjTypes;
	}

	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}

	
	
	
}
