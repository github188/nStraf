package cn.grgbanking.feeltm.EmployeeInformation.webapp;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.EmployeeInformation.service.EmployeeInformationService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class EmployeeInformationAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	private	String user_id;
	private String username;
	private String groupname;
	private String status;
	private String hiredate;
	private String education;
	private String technicaltitle;
	private String yearnum;
	private String position;
	private String mobile;
	private String pn; //序号
	private List<String> unames;
	private EmployeeInformationService employeeInformationService;
	
	public String query() throws Exception {
		System.out.println("query");
		try {			
			String from = request.getParameter("from");
			if(session.get("empNames")==null){
				unames=employeeInformationService.getAllNames();
				session.put("empNames",unames );
				Map<String,String> umap=new HashMap<String,String>();
			//	Map<String,String> umaps=new HashMap<String,String>();
				for(String u:unames){
					umap.put(u, u);
				}
				session.put("empMap",umap );
				}
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
				&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = employeeInformationService.getPage(groupname, username,pageNum, pageSize, status);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
				if (from != null) {
					Map map = new LinkedHashMap();
					map.put("pageCount", String.valueOf(page.getPageCount()));
					map.put("recordCount", String.valueOf(page.getRecordCount()));
					JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SysUser");
					JSONArray jsonObj = jsonUtil.toJSON(list, map);
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
				ActionContext.getContext().put("employeeinformationList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
		
	}
	/*
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupname");
		PrintWriter out=response.getWriter();
		List<Object> list=employeeInformationService.getNames(groupName);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	

	public String getstatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}

	

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}


	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public EmployeeInformationService getEmployeeInformationService() {
		return employeeInformationService;
	}



	public String getyearnum() {
		return yearnum;
	}

	public void setyearnum(String yearnum) {
		this.yearnum = yearnum;
	}

	public String gethiredate() {
		return hiredate;
	}

	public void sethiredate(String hiredate) {
		this.hiredate = hiredate;
	}
	public String geteducation() {
		return education;
	}

	public void seteducation(String education) {
		this.education = education;
	}
	
	public String gettechnicaltitle() {
		return technicaltitle;
	}

	public void settechnicaltitle(String technicaltitle) {
		this.technicaltitle = technicaltitle;
	}
	
	public String getposition() {
		return position;
	}

	public void setposition(String position) {
		this.position = position;
	}
	
	public String getmobile() {
		return mobile;
	}

	public void setmobile(String mobile) {
		this.mobile = mobile;
	}
	*/

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHiredate() {
		return hiredate;
	}

	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getTechnicaltitle() {
		return technicaltitle;
	}

	public void setTechnicaltitle(String technicaltitle) {
		this.technicaltitle = technicaltitle;
	}

	public String getYearnum() {
		return yearnum;
	}

	public void setYearnum(String yearnum) {
		this.yearnum = yearnum;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public List<String> getUnames() {
		return unames;
	}

	public void setUnames(List<String> unames) {
		this.unames = unames;
	}

	public EmployeeInformationService getEmployeeInformationService() {
		return employeeInformationService;
	}

	public void setEmployeeInformationService(
			EmployeeInformationService employeeInformationService) {
		this.employeeInformationService = employeeInformationService;
	}
}