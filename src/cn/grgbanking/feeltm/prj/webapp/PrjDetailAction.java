package cn.grgbanking.feeltm.prj.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.VersionDetail;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.domain.PrjDetail;
import cn.grgbanking.feeltm.prj.service.ParamsService;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Collections;

public class PrjDetailAction extends BaseAction{
	private String prjName;
//	private String versionNO;
	private String start;
	private String end;
	private PrjDetail prjdetail;
	private ParamsService paramsService;
	private ProjectDbService dbService;
	private List<String> prjs;
	//private PrjDetailDao detailDao=new PrjDetailDao();
	 PrjDetailDao detailDao;
	private List<String> prjTypes;
	
	private PrjPart part;
	
	private List<VersionDetail> prjVersionDetail;

	
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
		try {
			String from = request.getParameter("from");
			if(prjName!=null&&prjName.contains("$jia$")){
				prjName = prjName.replace("$jia$", "+");
			}
			List list=detailDao.getDetails(prjName, "", start, end,session);
			
			//将项目信息放入session
			List<PrjPart> ps=(List<PrjPart>)list;
			Map<String,PrjPart> mp=new HashMap<String,PrjPart>();
			for(PrjPart p:ps){
				String prjAndMonth=p.getStart()+p.getStaticMonth()+p.getPrjName();
				mp.put(prjAndMonth, p);
			}
			session.put("prjDetails", mp);
			
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
						"cn.grgbanking.feeltm.domain.testsys.PrjPart");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} 
			else {
				ActionContext.getContext().put("prjDetailList", list);
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
		prjName=de[0];
		start=de[1];
		end=de[2];
		if(prjName!=null){   
			prjName = prjName.replace("$jia$", "+");
			prjName = prjName.replace("lquot", "（");
			prjName = prjName.replace("rquot", "）");
		}
		try {
			prjVersionDetail=detailDao.getDetail(prjName, start,end,session);
			Collections.sort(prjVersionDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}
	
	public String edit() throws Exception {
		try {
			String ids = request.getParameter("ids");
			Map<String,PrjPart> mp=(Map<String,PrjPart>)session.get("prjDetails");
			part=mp.get(ids);    //p.getStart()+p.getStaticMonth()+p.getPrjName();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
			
		try {
			
			ProjectDB db=(ProjectDB)session.get("globalDB");
			part.setPrjType(db.getPrjName());
			
			boolean flag=detailDao.updatePart(part,session);
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
			Date dat=new Date();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
			String staticMonthTmp=format.format(dat);
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					String start=sids[i].substring(0, 7);
					String end=sids[i].substring(7,14);
					String prjName=sids[i].substring(14);
					if(!staticMonthTmp.equals(end)){
						MsgBox msgBox = new MsgBox(request, getText("删除失败，只能删除当月的录入数据"));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
					detailDao.deletePart(prjName,  start,end);
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
	
	public String showSummary() throws Exception{
		String req=request.getParameter("req");
		if(req!=null){   //&&prjName.contains("$jia$")
			req = req.replace("$jia$", "+");
			req = req.replace("lquot", "（");
			req = req.replace("rquot", "）");
		}
		prjdetail=detailDao.getDetailSummary(req, session);
		return "showSummary";
	}
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String prjName1=request.getParameter("prjName1");
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
	
	public String selectDB() throws Exception{
		String prjType1=request.getParameter("prjType1");
		ProjectDB prjDB=dbService.getDB(prjType1);
		if(prjDB!=null){
			session.put("globalDB", prjDB);
		}
		return queryPrjs();
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

	public PrjDetail getPrjdetail() {
		return prjdetail;
	}

	public void setPrjdetail(PrjDetail prjdetail) {
		this.prjdetail = prjdetail;
	}

	public ParamsService getParamsService() {
		return paramsService;
	}

	public void setParamsService(ParamsService paramsService) {
		this.paramsService = paramsService;
	}

	public List<String> getPrjs() {
		return prjs;
	}

	public void setPrjs(List<String> prjs) {
		this.prjs = prjs;
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

	public List<String> getPrjTypes() {
		return prjTypes;
	}
	
	

	public List<VersionDetail> getPrjVersionDetail() {
		return prjVersionDetail;
	}

	public void setPrjVersionDetail(List<VersionDetail> prjVersionDetail) {
		this.prjVersionDetail = prjVersionDetail;
	}

	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}

	public PrjPart getPart() {
		return part;
	}

	public void setPart(PrjPart part) {
		this.part = part;
	}

	
	
	
}
