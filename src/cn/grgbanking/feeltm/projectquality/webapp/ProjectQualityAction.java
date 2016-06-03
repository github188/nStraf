package cn.grgbanking.feeltm.projectquality.webapp;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.ProjectQuality;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.PrjDetailDao;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.prj.service.ProjectDbService;
import cn.grgbanking.feeltm.projectquality.dao.ProjectQualityDao;
import cn.grgbanking.feeltm.projectquality.service.ProjectQualityService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class ProjectQualityAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private ProjectQualityService projectQualityService;
	private ProjectQualityDao projectQualityDao;
	private ProjectDbService dbService;
	private ProjectQuality projectQuality;
	private PrjDetailDao detailDao;
	private String pn;   //问题编号
	private String querydate;
	private String startdate;
	private String enddate;
	private String prjName;
	
    private String modify_man;
    private List<String> prjTypes;
    private List<String> prjs;
    
    private List<ProjectQuality> projectQualityList= new ArrayList<ProjectQuality>();
	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
		public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		return "add";
	}
	
	
	
	
	public String up() throws Exception {
		System.out.println("up");
		boolean flag = false;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		
		String ids = request.getParameter("ids");
		projectQuality=projectQualityService.getCaseById(ids);

		try {
			//projectQuality.setEdit_lock(1);
			flag=projectQualityService.update(projectQuality);
			if (flag == true){
				MsgBox msgBox = new MsgBox(request,
					getText("提交成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.updatefaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}

	/**
	 * 发送oa短信，及邮件
	 * 标题：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * 正文：
	 * oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {     //保存suggestion概况的数据
		
		try {
				boolean flag = false;
				UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
				String no= "";
				SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
				startdate = f.format(projectQuality.getPrjStart());
				enddate = f.format(projectQuality.getPrjEnd());
				List<ProjectDB> prjDB=projectQualityService.getDB();
				for(int i=0;i<prjDB.size();i++)
				{

					session.put("globalDB", prjDB.get(i));
					List list=projectQualityDao.getDetails(startdate,enddate,session);
					List<PrjPart> ps=(List<PrjPart>)list;
					for(PrjPart mm:ps){
						no = projectQualityService.getNextNo();
						projectQuality.setPrjQno(no);
						projectQuality.setPrjName(mm.getPrjName());
						projectQuality.setBugSum(mm.getTotalValidBugs());
						projectQuality.setBugZongzhi(mm.getTotalBugValue());
						projectQuality.setBugClosednum(mm.getBugClosedNum());
						projectQuality.setBugResolverate(mm.getBugResolveRate());
						projectQuality.setBugReopennum(mm.getBugReopenNum());
						projectQuality.setBugReopenrate(mm.getBugReopenRate());
						projectQuality.setCreateDate(new Date());
						projectQuality.setCreateMan(userModel.getUsername());
						projectQuality.setUpdateDate(new Date());
						flag=projectQualityService.add(projectQuality);
					}
				}
	
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
					ProjectQuality temp = projectQualityService.getCaseById(sids[i]);
					projectQualityService.delete(temp);
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
		map=testerDao.getNameListBySuggestion();
		String forwardPage="edit";
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);

		System.out.println("yyyyy");
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);

			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					ProjectQuality temp = projectQualityService.getCaseById(sids[i]);
					projectQualityList.add(temp);
				}
			}
			ActionContext.getContext().put("projectQualityList", projectQualityList);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}

		return forwardPage;
	 }
	
	private List<ProjectQuality> getModList(HttpServletRequest request) throws ParseException{
		List<ProjectQuality> reports=new ArrayList<ProjectQuality>();
		String[] prjQno=request.getParameterValues("prjQno");
		String[] ids=request.getParameterValues("id");
		String[] prjName=request.getParameterValues("prjName");
		String[] prjStart=request.getParameterValues("prjStart");
		String[] prjEnd=request.getParameterValues("prjEnd");
		
		
		String[] bugSum=request.getParameterValues("bugSum");
		String[] bugZongzhi=request.getParameterValues("bugZongzhi");
		String[] bugClosednum=request.getParameterValues("bugClosednum");
		String[] bugResolverate=request.getParameterValues("bugResolverate");
		String[] bugReopennum=request.getParameterValues("bugReopennum");
		String[] bugReopenrate=request.getParameterValues("bugReopenrate");
		String[] createDate=request.getParameterValues("createDate");
		String[] createMan=request.getParameterValues("createMan");
		
		String[] documentQualitymark=request.getParameterValues("documentQualitymark");
		String[] teststopNum=request.getParameterValues("teststopNum");
		String[] testupNum=request.getParameterValues("testupNum");
		String[] projectcomplainNum=request.getParameterValues("projectcomplainNum");
		String[] codeQuality=request.getParameterValues("codeQuality");
		String[] performanceQuality=request.getParameterValues("performanceQuality");
		String[] moduleUsed=request.getParameterValues("moduleUsed");
		String[] codeNum=request.getParameterValues("codeNum");
		int size=prjName.length;
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<size;i++){
			ProjectQuality r=new ProjectQuality();
			r.setId(ids[i]);
			r.setPrjQno(prjQno[i]);
			r.setPrjName(prjName[i]);
			r.setPrjStart(f.parse(prjStart[i]));
			r.setPrjEnd(f.parse(prjEnd[i]));
			
			r.setBugSum(Integer.parseInt(bugSum[i]));
			r.setBugZongzhi(Double.parseDouble(bugZongzhi[i]));
			r.setBugClosednum(Integer.parseInt(bugClosednum[i]));
			r.setBugResolverate(bugResolverate[i]);
			r.setBugReopennum(Integer.parseInt(bugReopennum[i]));
			r.setBugReopenrate(bugReopenrate[i]);
			r.setCreateDate(f.parse(createDate[i]));
			r.setCreateMan(createMan[i]);
			
			r.setDocumentQualitymark(Double.parseDouble(documentQualitymark[i]));
			r.setTeststopNum(Integer.parseInt(teststopNum[i]));
			r.setTestupNum(Integer.parseInt(testupNum[i]));
			r.setProjectcomplainNum(Integer.parseInt(projectcomplainNum[i]));
			r.setCodeQuality(codeQuality[i]);
			r.setPerformanceQuality(performanceQuality[i]);
			r.setModuleUsed(Double.parseDouble(moduleUsed[i]));
			r.setCodeNum(Integer.parseInt(codeNum[i]));
			
			//缺陷密度
			DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
			forma.applyPattern("0.0");
			if(Double.parseDouble(codeNum[i])>0){
				double density = (double)(Double.parseDouble(bugZongzhi[i])/5.0/(Double.parseDouble(codeNum[i])/1000));
				r.setBugDensity(density);
				if(density>=3.5)
				{
					r.setBugDensityMark(20);
				}
				else if(density<3.5 && density>2.5)
				{
					r.setBugDensityMark(40);
				}
				else if(density<=2.5 && density>1.5)
				{
					r.setBugDensityMark(60);
				}
				else if(density<=1.5 && density>0.5)
				{
					r.setBugDensityMark(80);
				}
				else if(density<=0.5 && density>0)
				{
					r.setBugDensityMark(100);
				}
				else
				{
					r.setBugDensityMark(100);
				}
				
			}
			
			//缺陷解决率得分
			
			double BugResolvemark = 0 ;
			String BugResolvemarkstr[]= r.getBugResolverate().split("%");
			BugResolvemark = Double.parseDouble(BugResolvemarkstr[0]);
			r.setBugResolvemark(BugResolvemark);
			
			//缺陷ReOpen率得分
			double BugReopenmark = 0 ;
			String BugReopenmarkstr[]= r.getBugReopenrate().split("%");
			BugReopenmark = Double.parseDouble(BugReopenmarkstr[0]);
			if(BugReopenmark>=50)
			{
				r.setBugReopenmark(0);
			}
			else if(BugReopenmark<50 && BugReopenmark>30)
			{
				r.setBugReopenmark(20);
			}
			else if(BugReopenmark<=30 && BugReopenmark>20)
			{
				r.setBugReopenmark(50);
			}
			else if(BugReopenmark<=20 && BugReopenmark>10)
			{
				r.setBugReopenmark(80);
			}
			else if(BugReopenmark<=10 && BugReopenmark>0)
			{
				r.setBugReopenmark(100);
			}
			else
			{
				r.setBugReopenmark(100);
			}
			//基础质量得分
			double baseQualitymarktmp =  r.getBugDensityMark()*0.4 + r.getBugResolvemark()*0.3 + r.getBugReopenmark()*0.2 + r.getDocumentQualitymark()*0.1;
			baseQualitymarktmp = Double.parseDouble(forma.format(baseQualitymarktmp));
			r.setBaseQualitymark(baseQualitymarktmp);
			
			
			//版本中止测试次数得分
			if(r.getTeststopNum()!=0){
				r.setTeststopMark(r.getTeststopNum()*-2);
			}
			else
			{
				r.setTeststopMark(5);
			}
			
			//版本提交次数得分
			if(r.getTestupNum()>=4){
				r.setTestupMark((r.getTestupNum()-3)*-2);
			}
			else
			{
				r.setTestupMark(5);
			}
			
			//工程投诉次数
			if(r.getProjectcomplainNum()!=0)
			{
				r.setProjectcomplainMark(r.getProjectcomplainNum()*-5);
			}
			if(r.getCodeQuality()!=null){
				if(r.getCodeQuality().equals("优"))
				{
					r.setCodeQualitymark(4);
				}
				else if(r.getCodeQuality().equals("中"))
				{
					r.setCodeQualitymark(2);
				}
				else if(r.getCodeQuality().equals("差"))
				{
					r.setCodeQualitymark(-4);
				}
			}
			//性能得分
			if(r.getPerformanceQuality()!=null){
				if(r.getPerformanceQuality().equals("优"))
				{
					r.setPerformanceQualitymark(6);
				}
				else if(r.getPerformanceQuality().equals("中"))
				{
					r.setPerformanceQualitymark(-3);
				}
				else if(r.getPerformanceQuality().equals("差"))
				{
					r.setPerformanceQualitymark(-6);
				}
			}
			//组件复用得分
			if(r.getModuleUsed()!=0)
			{
				r.setModuleUsedmark(Double.parseDouble(forma.format(r.getModuleUsed()*3)));
			}
			//质量得分
			double QualityMarktmp = r.getBaseQualitymark()+r.getTeststopMark()+r.getTestupMark()+r.getProjectcomplainMark()+r.getCodeQualitymark()+r.getPerformanceQualitymark()+r.getModuleUsedmark();
			QualityMarktmp = Double.parseDouble(forma.format(QualityMarktmp));
			r.setQualityMark(QualityMarktmp);		
			reports.add(r);
		}
		return reports;
	}
	
	
	public double meg(double i){
		  int b = (int)Math.round(i * 10); //小数点后两位前移，并四舍五入 
		  double c = ((double)b/10.0); //还原小数点后两位
		  if((c*10)%5!=0){
		   int d = (int)Math.round(c); //小数点前移，并四舍五入 
		   c = ((double)d); //还原小数点
		  }
		  return c;
		 }

	
	public String update() throws Exception {
		System.out.println("update");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		modify_man=userModel.getUsername();
		
		try {
			List<ProjectQuality> modList = getModList(request);
			for(int i=0; i<modList.size(); i++)
			{
				ProjectQuality _mm = modList.get(i);
				boolean flag=projectQualityService.update(_mm);
				if (flag == true) {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updateok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile", new String[]{"已存在当月考核记录"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
			//boolean flag=projectQualityService.update(projectQuality);
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
		System.out.println("refesh");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}	
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
	
	public String query() throws Exception {
		System.out.println("query");
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
			List list=detailDao.getDetails(prjName, "", startdate, enddate,session);
			
			//将项目信息放入session
			List<PrjPart> ps=(List<PrjPart>)list;
			Map<String,PrjPart> mp=new HashMap<String,PrjPart>();
			for(PrjPart p:ps){
				String prjAndMonth=p.getStart()+p.getStaticMonth()+p.getPrjName();
				mp.put(prjAndMonth, p);
			}
			session.put("prjDetails", mp);
			
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
				&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			Page page = projectQualityService.getPage(querydate,prjName,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> listtmp = (List<Object>)page.getQueryResult();
				  for (int i = 0; i < listtmp.size(); i++) {
			
				  }
				if (from != null && from.equals("refresh")) {
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.ProjectQuality");
					JSONArray jsonObj = jsonUtil.toJSON(listtmp, map);
					JSONObject input = new JSONObject();
					if(page.getRecordCount()==0){
						input.put("pageCount", String.valueOf(page.getPageCount()+1));	
						input.put("recordCount", String.valueOf(page.getRecordCount()));
						input.put("jsonObj", jsonObj);	
				}
				else{
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("behaviorList", listtmp);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupname");
		PrintWriter out=response.getWriter();
		List<Object> list=projectQualityService.getNames(groupName);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	public Map<String, String> getMap() {
		return map;
	}

	

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public TesterDetailDao getTesterDao() {
		return testerDao;
	}

	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			projectQuality=projectQualityService.getCaseById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getModify_man() {
		return modify_man;
	}

	public void setModify_man(String modify_man) {
		this.modify_man = modify_man;
	}




	public ProjectQualityService getProjectQualityService() {
		return projectQualityService;
	}




	public void setProjectQualityService(ProjectQualityService projectQualityService) {
		this.projectQualityService = projectQualityService;
	}




	public ProjectQuality getProjectQuality() {
		return projectQuality;
	}




	public void setProjectQuality(ProjectQuality projectQuality) {
		this.projectQuality = projectQuality;
	}




	public String getQuerydate() {
		return querydate;
	}




	public void setQuerydate(String querydate) {
		this.querydate = querydate;
	}

	public List<ProjectQuality> getProjectQualityList() {
		return projectQualityList;
	}




	public String getStartdate() {
		return startdate;
	}




	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}




	public String getEnddate() {
		return enddate;
	}




	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}




	public void setProjectQualityList(List<ProjectQuality> projectQualityList) {
		this.projectQualityList = projectQualityList;
	}




	public ProjectQualityDao getProjectQualityDao() {
		return projectQualityDao;
	}




	public void setProjectQualityDao(ProjectQualityDao projectQualityDao) {
		this.projectQualityDao = projectQualityDao;
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




	public List<String> getPrjTypes() {
		return prjTypes;
	}




	public void setPrjTypes(List<String> prjTypes) {
		this.prjTypes = prjTypes;
	}




	public List<String> getPrjs() {
		return prjs;
	}




	public void setPrjs(List<String> prjs) {
		this.prjs = prjs;
	}




	public String getPrjName() {
		return prjName;
	}




	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	
}
