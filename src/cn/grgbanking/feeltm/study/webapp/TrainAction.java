package cn.grgbanking.feeltm.study.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.EmployeeCourse;
import cn.grgbanking.feeltm.domain.testsys.TrainingRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.study.service.NewEmployeeManageService;
import cn.grgbanking.feeltm.study.service.TrainService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class TrainAction extends BaseAction{
	
	private TrainService trainService;
	private NewEmployeeManageService emService;
	
	private String courseName;
	private String student;
	private String start;
	private String end;
	private String category;
	private String teacher;
	private String anon_flag;
	
	private TrainingRecord train;
	
	private List<EmployeeCourse> courses;
	private Map<String,String> students=new LinkedHashMap<String,String>();
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	
	
	public String add(){
		courses=emService.coursesSelect();

		return "add";
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			train.setUpdateMan(userModel.getUsername());
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			train.setUpdateDate(f.format(new Date()));
			if(anon_flag!=null && anon_flag.equals("1"))
			{
				train.setBound("跨部门");
			}
			else
			{
				train.setBound("部门内");
			}
			//train.setStudent(student);
			flag=trainService.add(train);
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
			courses=emService.coursesSelect();

			
			String ids = request.getParameter("ids");
			train=trainService.getTrainById(ids);
			//train.setCourseName(train.getCid()+":"+train.getCourseName());
			//student=train.getStudent();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			train.setUpdateMan(userModel.getUsername());
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			train.setUpdateDate(f.format(new Date()));
			if(anon_flag!=null && anon_flag.equals("1"))
			{
				train.setBound("跨部门");
			}
			else
			{
				train.setBound("部门内");
			}
			boolean flag=trainService.update(train);
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
			
			Page page = trainService.getPage( courseName, teacher, category,  student, start, end, pageNum,  pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.TrainingRecord");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				List<String> studentstmp=trainService.getAllStudents();
				students.put("王全胜", "王全胜");
				students.put("汤飞", "汤飞");
				for(String stu:studentstmp){
					students.put(stu, stu);
				}
				ActionContext.getContext().put("list", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	/**lhy 2014-4-30
	 * 人员选择
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String select(){
		String see = request.getParameter("see");
		String hidden = request.getParameter("hidden");
		String studentids=request.getParameter("studentids");
		studentids=studentids==null?"":studentids;
		if(!"".equals(studentids)){
			request.setAttribute("studentids", studentids);
		}
		//部门与员工
		Map deptmaMap = BusnDataDir.getMapKeyValue("staffManager.department");
		Set<String> keySet = deptmaMap.keySet();
		Map<String,List<SysUser>> deptUser = new HashMap<String, List<SysUser>>();
		for(Iterator it = keySet.iterator();it.hasNext();){
			String key = (String) it.next();
			List<SysUser> users = staffInfoService.getStaffByDeptKey(key);
			deptUser.put(key, users);
		}
		request.setAttribute("deptUser", deptUser);
		request.setAttribute("deptmaMap", deptmaMap);
		//组别与员工
		List<Project> usrGroups = sysUserGroupService.getAllProjectList();
		Map<String, List<SysUser>> grpUser = new HashMap<String, List<SysUser>>();
		for(Project u:usrGroups){
			List<SysUser> users = sysUserGroupService.findUserByProject(u.getId());
			grpUser.put(u.getId(), users);
		}
		request.setAttribute("grpUser", grpUser);
		request.setAttribute("usrGroups", usrGroups);
		request.setAttribute("seename", see);
		request.setAttribute("seeid", hidden);
		return "select";
	}
	
	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			train=trainService.getTrainById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
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
					TrainingRecord emtmp = trainService.getTrainById(sids[i]);
					trainService.delete(emtmp);
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
	
	
	
	
	
	
	
	
	
	
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public TrainService getTrainService() {
		return trainService;
	}
	public void setTrainService(TrainService trainService) {
		this.trainService = trainService;
	}
	public TrainingRecord getTrain() {
		return train;
	}
	public void setTrain(TrainingRecord train) {
		this.train = train;
	}
	
	
	public List<EmployeeCourse> getCourses() {
		return courses;
	}
	public void setCourses(List<EmployeeCourse> courses) {
		this.courses = courses;
	}
	public NewEmployeeManageService getEmService() {
		return emService;
	}
	public void setEmService(NewEmployeeManageService emService) {
		this.emService = emService;
	}
	public Map<String, String> getStudents() {
		return students;
	}
	public void setStudents(Map<String, String> students) {
		this.students = students;
	}
	public String getAnon_flag() {
		return anon_flag;
	}
	public void setAnon_flag(String anon_flag) {
		this.anon_flag = anon_flag;
	}
	
	
	
}
