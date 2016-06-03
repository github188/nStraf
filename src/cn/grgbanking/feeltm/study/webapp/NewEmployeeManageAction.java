package cn.grgbanking.feeltm.study.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.SendEmailUtil;
import cn.grgbanking.feeltm.domain.testsys.EmployeeCourse;
import cn.grgbanking.feeltm.domain.testsys.NewEmployeeManage;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.study.service.EmployeeCourseService;
import cn.grgbanking.feeltm.study.service.NewEmployeeManageService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class NewEmployeeManageAction extends BaseAction{
	
	private NewEmployeeManageService emService;
	private EmployeeCourseService ecService;
	
	private NewEmployeeManage em;
	
	private String uname;
	private String groupName;
	private String detName;
	private String userid;
	
	
	private String uid;
	private List<EmployeeCourse> ecList;
	
	private List<EmployeeCourse> courses;
	private List<String> percents;
	
	public String add(){
		return "add";
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			flag=emService.add(em);
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


	public String edit() throws Exception {
		try {
			String ids = request.getParameter("ids");
			em=emService.getNewEmployeeManageById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			boolean flag=emService.update(em);
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
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			Page page = emService.getPage(uname,groupName ,detName,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.NewEmployeeManage");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("list", list);
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
			em=emService.getNewEmployeeManageById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String distribute(){
		ecList=emService.showDistributeCoursesByUid(uid);
		return "distribute";
	}
	
	
	private List<String> generater() {
		List<String> ss=new ArrayList<String>();
		for(int i=0;i<=100;i+=5){
			ss.add(i+"%");
		}
		return ss;
	}
	public String editDistribute(){
		em=emService.getNewEmployeeManageById(uid);
		uname=em.getUname();
		uid=em.getId();
		userid=em.getUserId();
		percents=generater();  //百分比选择项
		courses=emService.coursesSelect();  //显示所有的课程选择项
		
		ecList=emService.showDistributeCourses(uid);   //显示分配的课程，默认的情况下显示所有的课程信息，以便用户操作  C0000:courseName
		if(ecList.get(0).getId()==null){   //类别为空的，代表未进行课程分配  
			return "editDistribute";
		}
		return "editDistribute2";
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
					NewEmployeeManage emtmp = emService.getNewEmployeeManageById(sids[i]);
					emService.delete(emtmp);
					//删除新员工学习记录时，删除对于的已分配课程数据
					List<EmployeeCourse> delList=emService.getCoursesIdByUid(sids[i]);
					for(EmployeeCourse e:delList){
						EmployeeCourse tmp = ecService.getEmployCourseById(e.getId());
						ecService.delete(tmp);
					}
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
	
	public String updateDistribute(){
		try {
			sumbitDistributeCourse(request);
			String touserid=request.getParameter("userid");
			boolean flag=emService.updateEmployeeCourses(ecList);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
				String userId = userModel.getUserid();
				SendEmailUtil.oaSendEmail(userId,touserid,"以为你分配学习课程，请尽快按要求学习。");
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "msgBox";
	}
	
	private void sumbitDistributeCourse(HttpServletRequest request) throws Exception
			 {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String[] cks=request.getParameterValues("ck"); //ck为1代表选中,0为未选中
		String uname=request.getParameter("uname");
		String uid=request.getParameter("uid");
		//ecID
		String[] ids=request.getParameterValues("ecID");
		//String[] cids=request.getParameterValues("cid");
		String[] courseNames=request.getParameterValues("courseName");  //C00001:courseName
		String[] categorys=request.getParameterValues("category");
		String[] planFinishDates=request.getParameterValues("planFinishDate");
		String[] graspStandards=request.getParameterValues("graspStandard");
		String[] prioryLevels=request.getParameterValues("prioryLevel");
		String[] finishPercents=request.getParameterValues("finishPercent");
		String[] actualFinishDates=request.getParameterValues("actualFinishDate");
		String[] finishEffects=request.getParameterValues("finishEffect");
		
		ecList=new ArrayList<EmployeeCourse>();
		for(int i=0;i<ids.length;i++){
			if(cks[i].equals("1")){
				EmployeeCourse ec=new EmployeeCourse();
				
				ec.setUname(uname);
				ec.setNewEmployeeId(uid);
				
				if(ids[i]!=null&&(!ids[i].equals(""))){
					ec.setId(ids[i]);
				}
				ec.setCid(courseNames[i].split(":")[0]);
				ec.setCourseName(courseNames[i].split(":")[1]);
				ec.setCategory(categorys[i]);
				
				if(!planFinishDates[i].equals(""))
				ec.setPlanFinishDate(sdf.parse(planFinishDates[i]));
			
				ec.setGraspStandard(graspStandards[i]);
				ec.setPrioryLevel(prioryLevels[i]);
				ec.setFinishPercent(finishPercents[i]);
				ec.setFinishEffect(finishEffects[i]);
				
				if(!actualFinishDates[i].equals(""))
				ec.setActualFinishDate(sdf.parse(actualFinishDates[i]));
				
				ecList.add(ec);
			}
		}
			
	}
	public NewEmployeeManage getEm() {
		return em;
	}
	public void setEm(NewEmployeeManage em) {
		this.em = em;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<EmployeeCourse> getEcList() {
		return ecList;
	}
	public void setEcList(List<EmployeeCourse> ecList) {
		this.ecList = ecList;
	}
	public NewEmployeeManageService getEmService() {
		return emService;
	}
	public void setEmService(NewEmployeeManageService emService) {
		this.emService = emService;
	}
	public EmployeeCourseService getEcService() {
		return ecService;
	}
	public void setEcService(EmployeeCourseService ecService) {
		this.ecService = ecService;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public List<EmployeeCourse> getCourses() {
		return courses;
	}
	public void setCourses(List<EmployeeCourse> courses) {
		this.courses = courses;
	}
	public List<String> getPercents() {
		return percents;
	}
	public void setPercents(List<String> percents) {
		this.percents = percents;
	}
	public String getDetName() {
		return detName;
	}
	public void setDetName(String detName) {
		this.detName = detName;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
