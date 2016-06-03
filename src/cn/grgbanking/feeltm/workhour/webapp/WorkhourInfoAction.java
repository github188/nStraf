package cn.grgbanking.feeltm.workhour.webapp;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.projectweekplan.dao.ProjectWeekPlanDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.workhour.domain.WorkSummary;
import cn.grgbanking.feeltm.workhour.domain.WorkhourInfo;
import cn.grgbanking.feeltm.workhour.service.WorkhourService;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

public class WorkhourInfoAction extends BaseAction {
	private WorkhourService workhourService;
	public void refreshList() {
		try {
			querySummary();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String querySummary() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			if (from != null && from.equals("refresh")) {
				String username = request.getParameter("username");
				String start = request.getParameter("start");
				String end = request.getParameter("end");
				String groupName = request.getParameter("groupName");
				String prjName = request.getParameter("prjName");
				String deptName = request.getParameter("deptName");
				String searchtype = request.getParameter("searchtype");
				WorkhourInfo info = new WorkhourInfo();
				info.setGroupName(userModel.getGroupName());
				if (username != null) {
					if (username.equals("全选"))
						username = "";
					info.setUsername(username.trim());
				}
				if (start != null && (!"".equals(start.trim())))
					info.setStart(start.split(" ")[0]);
				if (end != null && (!"".equals(end.trim())))
					info.setEnd(end.split(" ")[0]);
				if (groupName != null && (!"".equals(groupName.trim()))) {
					if (groupName.equals("全选"))
						groupName = "";
				}
				info.setGroupName(groupName);
				if (prjName == null || "0".equals(prjName)) {
					prjName = "";
				}
				info.setPrjName(prjName);
				info.setSearchtype(searchtype);
				if (deptName != null) {
					if (deptName.equals("全选"))
						deptName = "";
					info.setDeptName(deptName.trim());
				}
				List list = workhourService.getSummaryPage(info);
				List returnlist = setline(list);
				WorkSummary sum = summary(returnlist, info);
				list.add(sum);
				Page page = new Page();
				page.setQueryResult(list);
				page.setPageCount(1);
				page.setCurrentPageNo(1);
				page.setPageSize(50);
				page.setRecordCount(list.size());
				request.setAttribute("currPage", page);

				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.workhour.domain.WorkSummary");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				request.setAttribute("currPage", new Page());
				return "querySummary";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "querySummary";
	}
	
	private List setline(List<WorkSummary> summs) {
		//得出岗位级别
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> postlevel = BusnDataDir.getMapKeyValue("staffManager.postlevel");
		String postStr = postlevel.toString();
		if(!"".equals(postStr)){
			postStr=postStr.substring(1, postStr.length()-1);
		}
		String levelArray[]=postStr.toString().split(",");
		for(int b=0;b<levelArray.length;b++){
			String str=levelArray[b];
			if(str.split("=").length>1){
				levelArray[b]=str.split("=")[1].trim();
			}
		}
		//最终返回的数据
		List returnlist=new ArrayList(); 
		int summsize = summs.size();
		ProjectWeekPlanDao projectWeekPlanDao = (ProjectWeekPlanDao) BaseApplicationContext.getAppContext().getBean("projectWeekPlanDao");
		for(int i=0;i<summsize;i++){
			double[] levelInt = new double[levelArray.length]; 
			WorkSummary m=summs.get(i);
			m.setUsername(m.getUsername());
			m.setDeptname(m.getDeptname());
			m.setGroupname(m.getGroupname());
			m.setPrjName(m.getPrjName());
			double daywork=0;
			double requirement=0;
			double design=0;
			double code=0;
			double test=0;
			double managerment=0;
			double document=0;
			double meet=0;
			double train=0;
			double other=0;
			String people="";
			String userlevel="";
			double total = m.getTotal();
			if("日常工作".equals(m.getType())){
				daywork+=total;
			}else if("需求".equals(m.getType())){
				requirement+=total;
			}else if("设计".equals(m.getType())){
				design+=total;
			}else if("编码".equals(m.getType())){
				code+=total;
			}else if("测试".equals(m.getType())){
				test+=total;
			}else if("管理".equals(m.getType())){
				managerment+=total;
			}else if("文档".equals(m.getType())){
				document+=total;
			}else if("会议".equals(m.getType())){
				meet+=total;
			}else if("培训".equals(m.getType())){
				train+=total;
			}else if("其他".equals(m.getType())){
				other+=total;
			}
			if(!"".equals(m.getPeople()) && m.getPeople()!=null)
				people=m.getPeople();
			if(!"".equals(m.getUserlevel())  && m.getUserlevel()!=null){
				userlevel=m.getUserlevel().trim();
				for(int a=0;a<levelArray.length;a++){
					if(userlevel.equals(levelArray[a])){
						levelInt[a] = levelInt[a]+total;
					}
				}
			}
			String username=m.getUsername()==null?"":m.getUsername();
			String deptname=m.getDeptname()==null?"":m.getDeptname();
			String groupname=m.getGroupname()==null?"":m.getGroupname();
			String prjname=m.getPrjName()==null?"":m.getPrjName();
			for(int j=i+1;j<summsize;j++){
				WorkSummary m1=summs.get(j);
				String username1=m1.getUsername()==null?"":m1.getUsername();
				String deptname1=m1.getDeptname()==null?"":m1.getDeptname();
				String groupname1=m1.getGroupname()==null?"":m1.getGroupname();
				String prjname1=m1.getPrjName()==null?"":m1.getPrjName();
				total = m1.getTotal();
				String type=m1.getType();
				if(username1.equals(username)&&deptname1.equals(deptname)&&groupname1.equals(groupname)&&prjname1.equals(prjname)){
					if("日常工作".equals(type)){
						daywork+=total;
					}else if("需求".equals(type)){
						requirement+=total;
					}else if("设计".equals(type)){
						design+=total;
					}else if("编码".equals(type)){
						code+=total;
					}else if("测试".equals(type)){
						test+=total;
					}else if("管理".equals(type)){
						managerment+=total;
					}else if("文档".equals(type)){
						document+=total;
					}else if("会议".equals(type)){
						meet+=total;
					}else if("培训".equals(type)){
						train+=total;
					}else if("其他".equals(type)){
						other+=total;
					}
					if(!"".equals(m1.getPeople()) && m1.getPeople()!=null){
						if(people.indexOf(m1.getPeople())==-1){
							people=people+"、"+m1.getPeople();
						}
					}
					if(!"".equals(m1.getUserlevel())  && m1.getUserlevel()!=null){
						userlevel=m1.getUserlevel();
						for(int a=0;a<levelArray.length;a++){
							if(userlevel.equals(levelArray[a])){
								levelInt[a] = levelInt[a]+total;
							}
						}
					}
					summsize = summsize - 1;
					summs.remove(j);
					j=j-1;
				}
			}
			String user_level="";
			for(int k=0;k<levelArray.length;k++){
				user_level=user_level+levelArray[k]+":"+levelInt[k]+",";
			}
			if(!"".equals(user_level)){
				user_level=user_level.substring(0, user_level.length()-1);
			}
			m.setDaywork(daywork);
			m.setRequirement(requirement);
			m.setDesign(design);
			m.setCode(code);
			m.setTest(test);
			m.setManagerment(managerment);
			m.setDocument(document);
			m.setMeet(meet);
			m.setTrain(train);
			m.setOther(other);
			String people_last="";
			if(!"".equals(people)){
				for(int p=0;p<people.split(",").length;p++){
					if(people_last.indexOf(people.split(",")[p])==-1){
						people_last+=people.split(",")[p]+"、";
					}
				}
				if(people_last.length()>0){
					people_last=people_last.substring(0, people_last.length()-1);
				}
			}else{
				people_last=people;
			}
			m.setPeople(people_last);
			m.setUserlevel(user_level);
			//将各类型的值进行累加，之后再set
			double countnum = m.getDaywork()+m.getRequirement()+m.getDesign()+m.getCode()+m.getTest()+m.getManagerment()+m.getDocument()+m.getMeet()+m.getTrain()+m.getOther();
			m.setSumtotal(get1dec(countnum));
			m.setPrjName(m.getGroupname());
			returnlist.add(m);
		}
		return returnlist;
	}
	private WorkSummary summary(List<WorkSummary> summs,WorkhourInfo info) {
		WorkSummary mm=new WorkSummary();
		if(info!=null){
			String type = info.getSearchtype();
			if("1".equals(type)){//个人
				mm.setUsername("汇总");
				mm.setGroupname("");
				mm.setDeptname("");
				mm.setPrjName("");
			}else if("2".equals(type)){//部门
				mm.setUsername("");
				mm.setGroupname("");
				mm.setDeptname("汇总");
				mm.setPrjName("");
			}else if("3".equals(type)){//组别
				mm.setUsername("");
				mm.setGroupname("汇总");
				mm.setDeptname("");
				mm.setPrjName("");
			}else if("4".equals(type)){//项目
				mm.setUsername("");
				mm.setGroupname("");
				mm.setDeptname("");
				mm.setPrjName("汇总");
			}
		}
		double daywork = 0;
		double requirement = 0;
		double design = 0;
		double code = 0;
		double test = 0;
		double managerment = 0;
		double document = 0;
		double meet = 0;
		double train = 0;
		double other = 0;
		double peopletotal = 0;
		double subtotal = 0;
		for(WorkSummary m:summs){
			daywork+=m.getDaywork();
			requirement+=m.getRequirement();
			design+=m.getDesign();
			code+=m.getCode();
			test+=m.getTest();
			managerment+=m.getManagerment();
			document+=m.getDocument();
			meet+=m.getMeet();
			train+=m.getTrain();
			other+=m.getOther();
			peopletotal+=m.getPeopletotal();
			subtotal+=m.getSubtotal();
		}
		mm.setDaywork(daywork);
		mm.setRequirement(requirement);
		mm.setDesign(design);
		mm.setCode(code);
		mm.setTest(test);
		mm.setManagerment(managerment);
		mm.setDocument(document);
		mm.setMeet(meet);
		mm.setTrain(train);
		mm.setOther(other);
		mm.setPeopletotal(peopletotal);
		mm.setSubtotal(get1dec(subtotal));
		double sumtotal=daywork+requirement+design+code+test+managerment+document+meet+train+other;
		mm.setSumtotal(get1dec(sumtotal));
		return mm;
	}
	private double get1dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 1,BigDecimal.ROUND_HALF_UP);
		return re.doubleValue();
	}

	public WorkhourService getWorkhourService() {
		return workhourService;
	}

	public void setWorkhourService(WorkhourService workhourService) {
		this.workhourService = workhourService;
	}
}
