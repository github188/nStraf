package cn.grgbanking.feeltm.weekLog.webapp;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common.bean.DynamicWeek;
import cn.grgbanking.feeltm.common.util.DynamicWeekUtils;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.bean.DateVote;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.integralCenter.service.IntegralCenterService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectweekplan.dao.ProjectWeekPlanDao;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.weekLog.bean.DateWeekLog;
import cn.grgbanking.feeltm.weekLog.domain.PersonWeekLog;
import cn.grgbanking.feeltm.weekLog.service.WeekLogService;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class WeekLogAction extends BaseAction{
	@Autowired
	private WeekLogService weekLogService;
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	public SysUserGroupService userGroupService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IntegralCenterService integralCenterService;
	
	private SimpleDateFormat sdf2=new SimpleDateFormat("yyyy年MM月dd日");
	 
	
	
	private PersonWeekLog personWeekLog;
	private List<DateDayLog> dateDayLogList; 
	private List<DayLogVote> voteList;
	private Date submitOldDate;
	private DateDayLog dateDayLog;
	
	//具有编辑权限的时间(对最近几个月的日志有编辑权限,根据配置文件确定)
	int weeklogHasEditRightTime=Integer.parseInt(StringUtils.isBlank(Configure.getProperty("weeklogEditRightTime"))?"2":Configure.getProperty("weeklogEditRightTime"));
	
	
	/** 具有编辑权限的最早一周的开始时间:(当前日期所处周的开始日期往前推指定的月数)
	 * wtjiao 2014年5月15日 上午9:38:59
	 * @return
	 */
	private Calendar getLatestEditWeekStartDate(){
		Calendar startCal=Calendar.getInstance();
		//从今天往前数，找到第一个周日
		while(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
			startCal.add(Calendar.DAY_OF_YEAR, -1);
		}
		//把时间设置为这个周日的00:00:00
		startCal.set(Calendar.HOUR, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		
		//从这个星期日往前推指定的月数
		startCal.add(Calendar.MONTH,-1*weeklogHasEditRightTime);
		
		return startCal;
	}
	
	/**具有编辑权限的最晚一周的结束时间:(当前日期所处周的最后一天)
	 * wtjiao 2014年5月15日 上午9:39:38
	 * @return
	 */
	private Calendar getLatestEditWeekEndDate(){
		Calendar endCal=Calendar.getInstance();
		//从今天往后数，找到第一个周六
		while(endCal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY){
			endCal.add(Calendar.DAY_OF_YEAR, 1);
		}
		//把时间设置为这个周六的23:59:59
		endCal.set(Calendar.HOUR, 23);
		endCal.set(Calendar.MINUTE, 59);
		endCal.set(Calendar.SECOND,59);

		return endCal;
	}
	
	
	public boolean isTimeInEditWeeks(){
		return false;
	}
	
	/** 转向增加页面
	 * @return
	 */
	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		//设置日志和评论列表等信息
		setDayLogAndVoteList(Calendar.getInstance().getTime(),userModel.getUserid());
		return "personAdd";
	}
	
	/**获取选定周的日志、评论等信息
	 * wtjiao 2014年5月13日 下午3:29:48
	 */
	public String refreshByWeek(){
		try{
			String selectedWeek=request.getParameter("selectedWeek");
			String startDateStr=selectedWeek.split("-")[0];
			Date startDate=sdf2.parse(startDateStr);
			//起始时间往后推一天，以这天来计算它是位于哪一周
			Calendar startDateCal=Calendar.getInstance();
			startDateCal.setTime(startDate);
			startDateCal.add(Calendar.DAY_OF_YEAR, 1);
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//设置日志和评论列表等信息
			setDayLogAndVoteList(startDateCal.getTime(),userModel.getUserid());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "personAdd";
	}
	
	/**获取周计划提供的周范围
	 * wtjiao 2014年7月4日 上午9:48:12
	 * @param startDate
	 * @return
	 */
	private List<DynamicWeek> getWeekPlanDynamicWeekByTime(Calendar startDate) {
		Calendar oriStartDate=Calendar.getInstance();
		oriStartDate.setTime(startDate.getTime());
		//周列表
		startDate.add(Calendar.MONTH, -1*weeklogHasEditRightTime);//往前数指定的月
		Calendar endDate=Calendar.getInstance();
		//endDate.add(Calendar.DAY_OF_YEAR,7);//往后数一周
		List<DynamicWeek> weekList=DynamicWeekUtils.getDynamicWeeks(startDate, endDate, oriStartDate);
		return weekList;
	}
	
	/**设置日志和评论列表等信息
	 * wtjiao 2014年5月13日 下午3:29:21
	 * @param string 
	 */
	public void setDayLogAndVoteList(Date selectedDate, String userId){
		//周选择下拉框列表
		Calendar cal=Calendar.getInstance();
		cal.setTime(selectedDate);
		List<DynamicWeek> weekList=getWeekPlanDynamicWeekByTime(cal);
		request.setAttribute("weeks",weekList);
		//用户和用户所在组
		SysUser usr= staffInfoService.findUserByUserid(userId);
		request.setAttribute("groupStr", projectService.getProjectNameByUserid(userId));
		request.setAttribute("deptNameStr", staffInfoService.getDeptNameValueByUserId(userId).trim());
		request.setAttribute("usr", usr);
		
		for(DynamicWeek week:weekList){
			if(week.isSelected()){
				//获取用户指定时间段内的日期日志列表
				dateDayLogList=weekLogService.queryDateDayLog(userId,week.getStartTime(),week.getEndTime());
				sortDateDaylog(dateDayLogList);
				//将projectcode转为projectname
				ProjectWeekPlanDao projectWeekPlanDao = (ProjectWeekPlanDao) BaseApplicationContext.getAppContext().getBean("projectWeekPlanDao");
				if(dateDayLogList!=null){
					for(int i=0;i<dateDayLogList.size();i++){
						List<DayLog> daylogList = dateDayLogList.get(i).getDaylogList();
						for(int j=0;j<daylogList.size();j++)
							daylogList.get(j).setPrjName(((Project)projectWeekPlanDao.getObject(Project.class, daylogList.get(j).getPrjName())).getName());
					}
				}
				//获取用户指定时间段内的日志评论列表
				List<DateVote> dateVoteList=new ArrayList<DateVote>();
				dateVoteList=assemblyVoteMapList(weekLogService.queryVote(userId,week.getStartTime(),week.getEndTime()));
				request.setAttribute("dateVoteList", dateVoteList);
				break;
			}
		}
	}
	
	/**根据时间
	 * wtjiao 2014年7月14日 下午12:30:49
	 * @param dateDayLogList2
	 */
	private void sortDateDaylog(List<DateDayLog> dateDayLogList2) {
		Collections.sort(dateDayLogList2, new Comparator<DateDayLog>() {
			@Override
			public int compare(DateDayLog o1, DateDayLog o2) {
				return o1.getLogDate().compareToIgnoreCase(o2.getLogDate());
			}
		});
	}

	/**根据时间组装成（日期:评论列表）格式
	 * wtjiao 2014年5月14日 上午9:24:23
	 * @param queryVote
	 * @return
	 */
	private List<DateVote> assemblyVoteMapList(List<DayLogVote> voteList) {
		List<DateVote> dateVoteList =new ArrayList<DateVote>();
		Map<String,List<DayLogVote>> voteMap=new HashMap<String,List<DayLogVote>>();
		
		for(DayLogVote vote:voteList){
			//获取评论所指向的日志的日期，以此为key，日期对应的评论列表为value，构建map
			String logDateStr=new SimpleDateFormat("yyyy-MM-dd").format(dayLogService.getCaseById(vote.getDaylogId()).getLogDate());
			if(voteMap.get(logDateStr)==null){
				List<DayLogVote> tmp=new ArrayList<DayLogVote>();
				tmp.add(vote);
				voteMap.put(logDateStr, tmp);
			}else{
				List<DayLogVote> tmp= voteMap.get(logDateStr);
				tmp.add(vote);
				voteMap.put(logDateStr, tmp);
			}
		}
		
		//遍历map，组装为DateVote对象
		Iterator ite=voteMap.keySet().iterator();
		while(ite.hasNext()){
			String logDateStr=(String)ite.next();
			List<DayLogVote> tmpList=voteMap.get(logDateStr);
			DateVote dVote=new DateVote();
			dVote.setDate(logDateStr);
			dVote.setVoteList(tmpList);
			dateVoteList.add(dVote);
		}
		
		return dateVoteList;
	}

	/** 保存新增日志
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		String msgString ="";
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//获取用户
			SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
			//获取组
			String group=projectService.getProjectNameByUserid(userModel.getUserid());
			//获取部门
			String deptName=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
			String selectedWeekStr=request.getParameter("selectedWeekParam");
			if(StringUtils.isNotBlank(selectedWeekStr)){
				//解析字符串
				Date startDate=sdf2.parse(selectedWeekStr.split("-")[0]);
				Date endDate=sdf2.parse(selectedWeekStr.split("-")[1]);
				
				//判断该时间段内的周报是否存在，若存在，则提示不能新建
				boolean hasPersonalLog=weekLogService.hasPersonalWeekLog(userModel.getUserid(),startDate,endDate);
				if(hasPersonalLog){
					MsgBox msgBox = new MsgBox(request,"已经存在该周周报，请直接修改!");
					addActionMessage(getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
				//设置到实体对象中
				personWeekLog.setStartDate(startDate);
				personWeekLog.setEndDate(endDate);
				personWeekLog.setDetName(deptName);
				personWeekLog.setGroupName(group);
				personWeekLog.setUserId(usr.getUserid());
				personWeekLog.setUserName(usr.getUsername());
				personWeekLog.setUpdateman(usr.getUsername());
				personWeekLog.setUpdateTime(Calendar.getInstance().getTime());
				
				boolean flag=weekLogService.savePersonWeekLog(personWeekLog);
				if (flag == true) {
					/**
					 * add by whxing
					 * 添加积分
					 */
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("usr", usr);
					paramMap.put("endDate",endDate);
					msgString = integralCenterService.saveIntegralInfoByWeekDayParamMap(paramMap);
					
					
					MsgBox msgBox = new MsgBox(request, getText("add.ok")+msgString);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
					
					
					
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.addfaile")+msgString);
					addActionMessage(getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.addfaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	
	/** 转向修改页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		
		String id=request.getParameter("ids");
		
		boolean isEditPageWeekSelectRedirect=false;//是否是从编辑页面跳转过来的
		
		//在列表页面选择某个条目进行修改
		if(StringUtils.isNotBlank(id)){
			personWeekLog=weekLogService.getCaseById(id);
			//保存原来周报的时间
//			submitOldDate = personWeekLog.getEndDate();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd");
			String oldWeekendDateString = sdf2.format(personWeekLog.getEndDate());
			request.setAttribute("oldWeekendDate", oldWeekendDateString);
			
			
		}else{//在修改页面跳转到某周进行修改
			request.setAttribute("oldWeekendDate", request.getParameter("oldWeekendDate"));
			String selectedWeek=request.getParameter("selectedWeek");
			String startDateStr=selectedWeek.split("-")[0];
			String endDateStr=selectedWeek.split("-")[1];
			Date startDate=sdf2.parse(startDateStr);
			Date endDate=sdf2.parse(endDateStr);
			
			//根据用户以及周的起止时间，获取这周的周报
			personWeekLog=weekLogService.queryPersonWeekLog(userModel.getUserid(),startDate,endDate);
			//当跳转到这周在数据库中并没有数据时，取出值为空
			if(personWeekLog==null){
				personWeekLog=new PersonWeekLog();
				personWeekLog.setStartDate(startDate);
				personWeekLog.setEndDate(endDate);
				isEditPageWeekSelectRedirect=true;
			}
		}
		
		//从编辑页面跳转过来的，肯定是自己的日志，并且在指定范围内，这里就不需要验证了 
		if(!isEditPageWeekSelectRedirect){
			//只能编辑自己的日志
			if(!userModel.getUserid().equals(personWeekLog.getUserId())){
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			//判断要编辑的周报是否在规定的几个月份内
			/*boolean hasTimeEditRight= hasTimeEditRight(personWeekLog.getStartDate(),personWeekLog.getEndDate());
			if(!hasTimeEditRight){
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您只能编辑最近"+weeklogHasEditRightTime+"个月的周报，该周报已被封存!"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}*/
		}
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(personWeekLog.getStartDate());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		//设置日志和评论列表等信息
		setDayLogAndVoteList(cal.getTime(),userModel.getUserid());
		return "personEdit";
	}
	
	
	/**判断要编辑的周报是否在规定的几个月份内
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private boolean hasTimeEditRight(Date startDate, Date endDate) {
		
		//查询起始日期：设为某月第一天
		Calendar startCal=getLatestEditWeekStartDate();
		Calendar endCal=getLatestEditWeekEndDate();
		
		Calendar queryStartCal=Calendar.getInstance();
		Calendar queryEndCal=Calendar.getInstance();
		queryStartCal.setTime(startDate);
		queryEndCal.setTime(endDate);
		
		//查询的日志周的开始日期在最早允许查询日期之后，结束日期在最迟允许日期之前
		if(queryStartCal.after(startCal) && queryEndCal.before(endCal)){
			return true;
		}else{
			return false;
		}
		
	}

	/** 修改后保存
	 * @return
	 * @throws Excetion
	 */
	public String update() throws Exception{
		String msg = "";//保存积分返回信息
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//获取用户
			SysUser usr= staffInfoService.findUserByUserid(userModel.getUserid());
			//获取组
			String group=projectService.getProjectNameByUserid(userModel.getUserid());
			//获取部门
			String deptName=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
			String selectedWeekStr=request.getParameter("selectedWeekParam");
			if(StringUtils.isNotBlank(selectedWeekStr)){
				//解析字符串
				Date startDate=sdf2.parse(selectedWeekStr.split("-")[0]);
				Date endDate=sdf2.parse(selectedWeekStr.split("-")[1]);
				
				//先记录页面传过来的本周总结和下周计划
				String sumary=personWeekLog.getDesc();
				String plan=personWeekLog.getPlan();
				
				//根据用户以及周的起止时间，获取这周的周报
				personWeekLog=weekLogService.queryPersonWeekLog(userModel.getUserid(),startDate,endDate);
				//当用户通过界面选择了另一周并且数据库中没有那周的数据时，取出的值为null
				if(personWeekLog==null){
					personWeekLog=new PersonWeekLog();
				}
				
				//设置到实体对象中
				personWeekLog.setDesc(sumary);
				personWeekLog.setPlan(plan);
				personWeekLog.setStartDate(startDate);
				personWeekLog.setEndDate(endDate);
				personWeekLog.setDetName(deptName);
				personWeekLog.setGroupName(group);
				personWeekLog.setUserId(usr.getUserid());
				personWeekLog.setUserName(usr.getUsername());
				personWeekLog.setUpdateman(usr.getUsername());
				personWeekLog.setUpdateTime(Calendar.getInstance().getTime());
				
				boolean flag=weekLogService.savePersonWeekLog(personWeekLog);
				if (flag == true) {
					/**
					 * add by whxing
					 * 修改积分
					 */
					//修改前的日期
//					Date oldWeekendDate = DateUtil.to_date(request.getParameter("oldWeekendDate"));
					SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd");
					String oldWeekendDateStr = request.getParameter("oldWeekendDate").trim();
//					String oldWeekendDateStr = requestString.substring(0, 9);
//					String replaceString = requestString.replace("", "-").trim();
//					if (replaceString.length()<6){
//						requestString = requestString.substring(beginIndex, endIndex)
//					}
//					String oldWeekendDateStr = sdf2.format(submitOldDate);
					String endDateStr = sdf2.format(endDate);
					endDate= sdf2.parse(endDateStr);
                    if (!oldWeekendDateStr.equals(endDateStr)){
                    	Map<String, Object> paramMap = new HashMap<String, Object>();
    					paramMap.put("usr", usr);
    					paramMap.put("endDate",endDate);
    					msg = integralCenterService.saveIntegralInfoByWeekDayParamMap(paramMap);
                    }
                    
                    MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok")+msg);
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.updateok"));
				} else {
					MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"));
					addActionMessage(getText("operInfoform.updatefaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "msgBox";
	}
	

	/**删除某天的日志
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id=request.getParameter("ids");
		personWeekLog=weekLogService.getCaseById(id);
		
		//只能删除自己的日志
		if(!userModel.getUserid().equals(personWeekLog.getUserId())){
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		//判断要编辑的周报是否在规定的几个月份内
		boolean hasTimeEditRight= hasTimeEditRight(personWeekLog.getStartDate(),personWeekLog.getEndDate());
		if(!hasTimeEditRight){
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您只能删除最近"+weeklogHasEditRightTime+"个月的周报，该周报已被封存!"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		if(weekLogService.remove(personWeekLog.getId())){
			/**
			 * add by whxing
			 * 删除日志对应删除积分
			 */
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userId", userModel.getUserid());
			paramMap.put("endDate", personWeekLog.getEndDate());
			integralCenterService.deleteIntegralInfoByWeekLogParam(paramMap);
			
			MsgBox msgBox = new MsgBox(request, getText("记录删除成功")+",同时扣取相应积分", "周报删除信息页面");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	
	/**查询详细信息
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String id=request.getParameter("ids");

			//在列表页面选择点击某个条目查看
			if(StringUtils.isNotBlank(id)){
				personWeekLog=weekLogService.getCaseById(id);
			}else{//在查看页面跳转到另一周
				String selectedWeek=request.getParameter("selectedWeek");
				String startDateStr=selectedWeek.split("-")[0];
				String endDateStr=selectedWeek.split("-")[1];
				Date startDate=sdf2.parse(startDateStr);
				Date endDate=sdf2.parse(endDateStr);
				//查看的哪个用户的周报
				String personUserId=request.getParameter("personUserId");
				//根据用户以及周的起止时间，获取这周的周报
				personWeekLog=weekLogService.queryPersonWeekLog(personUserId,startDate,endDate);
				if(personWeekLog==null){
					personWeekLog=new PersonWeekLog();
					personWeekLog.setStartDate(startDate);
					personWeekLog.setEndDate(endDate);
					personWeekLog.setUserId(personUserId);
				}
			}
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(personWeekLog.getStartDate());
			cal.add(Calendar.DAY_OF_YEAR, 1);
			
			//设置日志和评论列表等信息
			setDayLogAndVoteList(cal.getTime(),personWeekLog.getUserId());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "personShow";
	}

	/** 查询
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String queryStartTime=null;
			String queryEndTime=null;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
				queryStartTime = request.getParameter("queryStartTime");
			if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
				queryEndTime = request.getParameter("queryEndTime");
			if(from == null){
				personWeekLog = new PersonWeekLog();
				personWeekLog.setUserName(userModel.getUsername());
			}
			
			String deptName="";
			String groupName="";
			String userName="";
			if(!(from != null && from.equals("refresh"))){
				personWeekLog=new PersonWeekLog();
				//是部门经理，默认查询当前部门
				if(UserRoleConfig.isDeptManageGroup(userModel)){
					deptName=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
					personWeekLog.setDetName(deptName);
					userName="";
				}else if(UserRoleConfig.isProjectManageGroup(userModel)){//项目经理，默认查询第一个项目
					try{
						groupName=projectService.getProjectNameByUserid(userModel.getUserid());
						groupName=groupName.indexOf(',')>0?groupName.substring(0,groupName.indexOf(',')):groupName;
						personWeekLog.setGroupName(groupName);
						userName="";
					}catch(Exception e){}
				}else{//查询个人
					personWeekLog.setUserName(userModel.getUsername());
					userName=userModel.getUsername();
				}
			}
			
			Page page = weekLogService.getPage(personWeekLog,queryStartTime,queryEndTime,pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				//公司的json转换
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.weekLog.domain.PersonWeekLog");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				
				//json-lib包提供的转换
				//JSONArray jsonArray=JSONArray.fromObject(list);
				//jsonArray.add(map);
				//String jsonObj = jsonArray.toString(); 
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				ajaxPrint(input.toString());
				return null;
			} else {
				ActionContext.getContext().put("personWeekLogList", list);
				//list页面被选中的查询条件：用户姓名
				request.setAttribute("queryUserName",userName);
				//list页面被选中的查询条件：用户部门
				request.setAttribute("queryDeptName", deptName);
				//list页面被选中的查询条件：用户项目
				request.setAttribute("queryGroupName",groupName);
				return "personQuery";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "personQuery";
	}

	
	/** 向ajax请求返回数据
	 * @param str
	 */
	private void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出excel表
	 * @return
	 * @throws Exception
	 * zqsheng1
	 * 2014年7月2日
	 */
	@SuppressWarnings("rawtypes")
	public String exportData() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		request.setCharacterEncoding("utf-8");
		String queryStartTime=null;
		String queryEndTime=null;
		if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
			queryStartTime = request.getParameter("queryStartTime");
		if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
			queryEndTime = request.getParameter("queryEndTime");
		
		String exportTimeStr=request.getParameter("timeGroup");
		String exportArea=request.getParameter("areaGroup");
		
		//导出指定时间指定范围的日志
		if(StringUtils.isNotBlank(exportTimeStr)&&StringUtils.isNotBlank(exportArea)){
			personWeekLog=new PersonWeekLog();
			
			if("beforeweek".equals(exportTimeStr)){//上周
				String[] date = DateUtil.getBeforeWeekDate();
				queryStartTime = date[1];
				queryEndTime = date[0];
			}else if("week".equals(exportTimeStr)){//本周
				DynamicWeek dw=DynamicWeekUtils.getDateDynamicWeek(Calendar.getInstance().getTime());
				queryStartTime=fo.format(dw.getStartTime());
				queryEndTime=fo.format(dw.getEndTime());
			}else if("month".equals(exportTimeStr)){
			    //获取当前月第一天：
		        Calendar c = Calendar.getInstance();    
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        queryStartTime= fo.format(c.getTime());
		        
		        //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		        queryEndTime= fo.format(ca.getTime());
			}
				
			if("person".equals(exportArea)){
				personWeekLog.setUserName(staffInfoService.findUserByUserid(userModel.getUserid()).getUsername());
			}else if("project".equals(exportArea)){
				List groupList=projectService.getProjectByUserId(userModel.getUserid());
				if(groupList!=null&&groupList.size()>0){
					personWeekLog.setGroupName(((Project)groupList.get(0)).getName());
				}else{
					personWeekLog.setGroupName("无项目");
				}
			}else if("dept".equals(exportArea)){
				personWeekLog.setDetName(staffInfoService.getDeptNameValueByUserId(userModel.getUserid()));
			}
		}
		
		//根据查询出的数据，进行个人周报导出数据的封装
		Page page = weekLogService.getPage(personWeekLog,queryStartTime,queryEndTime,1, 10000);
		List<Object> list = (List<Object>)page.getQueryResult();
		List<DateWeekLog> dateWeekLogList = new ArrayList<DateWeekLog>();
		for(int i=0;i<list.size();i++){
			DateWeekLog dateWeekLog = new DateWeekLog();
			PersonWeekLog weeklog = (PersonWeekLog)list.get(i);
			String sdate = DateUtil.getDateString(weeklog.getStartDate());
			String edate = DateUtil.getDateString(weeklog.getEndDate());
			dateWeekLog.setId(weeklog.getId());
			dateWeekLog.setWeekCount(DateUtil.getWeekCountByDate(DateUtil.stringToDate(sdate, "yyyy-MM-dd")));
			dateWeekLog.setUserId(weeklog.getUserId());
			dateWeekLog.setUserName(weeklog.getUserName());
			dateWeekLog.setDesc(weeklog.getDesc());
			dateWeekLog.setPlan(weeklog.getPlan());
			dateDayLog = new DateDayLog();
			dateDayLog.setUserId("'"+weeklog.getUserId()+"'");
			List<DateDayLog> datelogList  = dayLogService.queryDateDaylogList(dateDayLog, sdate, edate);
			dateWeekLog.setDaylogList(datelogList);
			dateWeekLogList.add(dateWeekLog);
		}
		Date date = new Date();
		String filename = getWorkSheetName(personWeekLog,dateWeekLogList,queryStartTime,queryEndTime);//设置文件名
//		String filename = "个人周报";
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb.createSheet();
//     	wb.setSheetName(0,filename,HSSFWorkbook.ENCODING_UTF_16);
     	
     	/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); //默认字体
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)10);// 字体大小
		
		HSSFFont headfont = wb.createFont(); 
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();  
		cellfont.setFontName("黑体");
		cellfont.setFontHeightInPoints((short)12);
		cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		
		/**	** ** *设置样式* ** ** **/
		//================== 标题 =====================
		HSSFFont cellNamefont = wb.createFont();  
		cellNamefont.setFontName("宋体");
		cellNamefont.setFontHeightInPoints((short)10);
		cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		
		//================== 普通行 (左中)======================
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 普通行 (居中)======================
		HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 日期(居中) ======================
		HSSFCellStyle dateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		defaultCenterStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd "));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb = this.setExcelValue(wb, 0, "周次", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "姓名", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "类别", 0,(short) 2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "日期", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务类别",0, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务内容",0, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "补充说明", 0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "任务工作量", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "日工作量", 0, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "完成%", 0, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "状态", 0, (short)10, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "计划/新增", 0, (short)11, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)12, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "所属部门", 0, (short)13, cellNameStyle);
		
		
		
		if(dateWeekLogList!=null && dateWeekLogList.size()!=0){
			/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
			int sumRow=0;
			String no_week = dateWeekLogList.get(0).getWeekCount();
			String before_week = "";
			int no_weekLen = 0;
			int before_weekLen = 0;
			int people_weekLen = 0;
			int region_weekLen = 0;
			for(int week=0;week<dateWeekLogList.size();week++){
				before_week = no_week;
				List<DateDayLog> datelogList = dateWeekLogList.get(week).getDaylogList();
				int dayRow = 0;//记录一个人一周有多少条日志记录
				int weekLen = 0;
				for(int i=0;i<datelogList.size();i++){
					List daylogList=datelogList.get(i).getDaylogList();
					for(int j=0;j<daylogList.size();j++){
						weekLen++;
					}
				}
				DateWeekLog weeklog = dateWeekLogList.get(week);
				weeklog.setWeekLen(weekLen);
				if(!no_week.equals(weeklog.getWeekCount())){
					no_week = weeklog.getWeekCount();
					no_weekLen = sumRow;
					before_weekLen=people_weekLen;
					people_weekLen=weeklog.getWeekLen()+2;
				}else{
					people_weekLen=people_weekLen+weeklog.getWeekLen()+2;
				}
				//按周输出数据
				for(int i=0;i<datelogList.size();i++){
					List daylogList=datelogList.get(i).getDaylogList();
					int sumSubTotal=0;
					for(int j=0;j<daylogList.size();j++){
						sumSubTotal+=((DayLog)daylogList.get(j)).getSubTotal();
					}
					dayRow+=daylogList.size();
					for(int j=0;j<daylogList.size();j++){
						++sumRow;
						DayLog daylog = (DayLog) daylogList.get(j);
						HSSFRow row = sheet.createRow(sumRow);
						row.setHeight((short)256);//行高
						HSSFCell cell = row.createCell((short)0);
						if(i==0 && j==0){
							/*类别*/
							sheet.addMergedRegion(new Region(sumRow,(short)2, sumRow+weeklog.getWeekLen()-1,(short)2));
						}
						/*跨行合并单元格*/
						if(j==0){
							/*日期*/
							sheet.addMergedRegion(new Region(sumRow,(short)3, sumRow+daylogList.size()-1,(short)3));
						}
						/*
						 wtjiao 20140715 POI对合并后边框操作存在缺陷，需要对参与合并的每个单元格进行边框设置，故下面这段代码不能放到if(j==0)判断中.
						                 POI中规定如果参与合并的每个单元格都有值，则以左上角的单元格值为准
						 */
						if(i==0){
							/*类别 */
							cell = sheet.getRow(sumRow).createCell((short)2);
							defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
							setBorderStyle(defaultCenterStyle);
							cell.setCellStyle(defaultCenterStyle);
//							//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue("本周日志");
							sheet.setColumnWidth((short)2, (short) (5*512));
						}
						
						/*周次 */
						cell = sheet.getRow(sumRow).createCell((short)0);
						defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						setBorderStyle(defaultCenterStyle);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(dateWeekLogList.get(week).getWeekCount());
						sheet.setColumnWidth((short)0, (short) (5*512));
						
						/*姓名 */
						cell = sheet.getRow(sumRow).createCell((short)1);
						defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						setBorderStyle(defaultCenterStyle);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getUserName());
						sheet.setColumnWidth((short)1, (short) (5*512));
						
						/*日期*/
						cell = sheet.getRow(sumRow).createCell((short)3);
						dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						setBorderStyle(defaultCenterStyle);
						cell.setCellStyle(dateStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getLogDate());
						sheet.setColumnWidth((short)3, (short) (6*512));
						
						/*任务类别*/
						cell = row.createCell((short)4);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getType());
						sheet.setColumnWidth((short)4, (short) (5*512));
						
						/*任务内容*/
						cell = row.createCell((short)5);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getDesc());
						sheet.setColumnWidth((short)5, (short)(5*512));
						
						/*补充说明*/
						cell = row.createCell((short)6);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getReason());
						sheet.setColumnWidth((short)6, (short)(20*512));
						
						/*任务工作量*/
						cell = row.createCell((short)7);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getSubTotal());
						sheet.setColumnWidth((short)7, (short)(9*512));
						
						/*跨行合并单元格*/
						if(j==0){
							/*日工时*/
							sheet.addMergedRegion(new Region(sumRow,(short)8,sumRow+daylogList.size()-1,(short)8));
						}
						/*日工作量*/
						cell = sheet.getRow(sumRow).createCell((short)8);
						defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						setBorderStyle(defaultCenterStyle);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(sumSubTotal+"");
						sheet.setColumnWidth((short)8, (short) (20*512));
						
						/*完成百分比*/
						cell = row.createCell((short)9);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getFinishRate());
						sheet.setColumnWidth((short)9, (short) (5*512));
						
						/*状态*/
						cell = row.createCell((short)10);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getStatu());
						sheet.setColumnWidth((short)10, (short) (5*512));
						
						/*计划/新增*/
						cell = row.createCell((short)11);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getPlanOrAdd());
						sheet.setColumnWidth((short)11, (short) (6*512));
						
						/*所在项目*/
						cell = row.createCell((short)12);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getGroupName());
						sheet.setColumnWidth((short)12, (short) (14*512));
						
						/*所属部门*/
						cell = row.createCell((short)13);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(daylog.getDetName().trim());
						sheet.setColumnWidth((short)13, (short) (10*512));
					}
				}
				for(int a=0;a<2;a++){
					++sumRow;
					HSSFRow row = sheet.createRow(sumRow);
					row.setHeight((short)256);//行高
					HSSFCell cell = row.createCell((short)0);
					if(a==0){
						/*周次*/
	//					sheet.addMergedRegion(new Region(sumRow-dayRow,(short)0,sumRow+1,(short)0));
						/*姓名*/
						sheet.addMergedRegion(new Region(sumRow-dayRow,(short)1,sumRow+1,(short)1));
					}
					/*本周总结 下周计划*/
					sheet.addMergedRegion(new Region(sumRow,(short)3, sumRow,(short)13));
					if(a==0){
						cell = row.createCell((short)2);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue("本周总结");
						sheet.setColumnWidth((short)2, (short) (10*512));
						for(int b=3;b<14;b++){
							cell = row.createCell((short)b);
							defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
							cell.setCellStyle(defaultStyle);
//							//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(weeklog.getDesc());
							sheet.setColumnWidth((short)b, (short) (10*512));
						}
						cell = row.createCell((short)0);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultCenterStyle);
						cell.setCellValue(dateWeekLogList.get(week).getWeekCount());
					}
					if(a==1){
						cell = row.createCell((short)2);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue("下周计划");
						sheet.setColumnWidth((short)2, (short) (10*512));
						for(int b=3;b<14;b++){
							cell = row.createCell((short)b);
							defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
							cell.setCellStyle(defaultStyle);
//							//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(weeklog.getPlan());
							sheet.setColumnWidth((short)b, (short) (10*512));
						}
						cell = row.createCell((short)0);
						defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cell.setCellStyle(defaultCenterStyle);
						cell.setCellValue(dateWeekLogList.get(week).getWeekCount());
					}
					//填充姓名值
					cell = sheet.getRow(sumRow).createCell((short)1);
					defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					setBorderStyle(defaultCenterStyle);
					cell.setCellStyle(defaultCenterStyle);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(weeklog.getUserName());
					sheet.setColumnWidth((short)1, (short) (5*512));
				}
				if(no_weekLen!=0){
					//ling.tu 2014-12-29 在poi3.7版本下，不能新增行，如果新增一行会导致合并行异常，在此将row及cell注释掉
//					HSSFRow row = sheet.createRow(no_weekLen-before_weekLen+1);
//					row.setHeight((short)256);//行高
//					HSSFCell cell = row.createCell((short)0);
					/*周次*/
					sheet.addMergedRegion(new Region(no_weekLen-before_weekLen+1,(short)0,no_weekLen,(short)0));
					//填充周次值
//					cell = sheet.getRow(no_weekLen-before_weekLen+1).createCell((short)0);
//					defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//					setBorderStyle(defaultCenterStyle);
//					cell.setCellStyle(defaultCenterStyle);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(before_week);
					sheet.setColumnWidth((short)0, (short) (5*512));
					region_weekLen = no_weekLen;
					no_weekLen=0;
				}
				if(week==dateWeekLogList.size()-1){
					String weekCount = dateWeekLogList.get(week).getWeekCount();
//					HSSFRow row = sheet.createRow(region_weekLen+1);
//					row.setHeight((short)256);//行高
//					HSSFCell cell = row.createCell((short)0);
					/*周次*/
					sheet.addMergedRegion(new Region(region_weekLen+1,(short)0,sumRow,(short)0));
					//先合并单元格，之后给每个单元格补充值，如果只补充一个单元格的值，则下边框无显示
					for(int i=region_weekLen+1;i<sumRow+1;i++){
						//填充周次值
//						cell = sheet.getRow(i).createCell((short)0);
//						defaultCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//						setBorderStyle(defaultCenterStyle);
//						cell.setCellStyle(defaultCenterStyle);
//						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//						cell.setCellValue(weekCount);
						sheet.setColumnWidth((short)0, (short) (5*512));
					}
				}
			}
		}
		wb.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder 是否显示边框
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			setBorderStyle(style);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	private void setBorderStyle(HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
		style.setBottomBorderColor(HSSFColor.BLACK.index);	
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	private String getWorkSheetName(PersonWeekLog dl, List<DateWeekLog> dateWeekLogList, String queryStartTime, String queryEndTime) {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String name="";
			if(StringUtils.isNotBlank(dl.getDetName())){
				if(dl.getDetName().length()>10){
					name+=dl.getDetName().substring(0,10)+"_";
				}else{
					name+=dl.getDetName()+"_";
				}
			}
			if(StringUtils.isNotBlank(dl.getGroupName())){
				if(dl.getGroupName().length()>10){
					name+=dl.getGroupName().substring(0,10)+"_";
				}else{
					name+=dl.getGroupName()+"_";
				}
			}
			if(StringUtils.isNotBlank(dl.getUserName())){
				name+=dl.getUserName()+"_";
			}
			name+="个人周报";
			if(StringUtils.isNotBlank(queryStartTime)&&StringUtils.isNotBlank(queryEndTime)){
				Date st=sdf.parse(queryStartTime);
				Date end=sdf.parse(queryEndTime);
				name+="（"+new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end)+"）";
			}else if(dateWeekLogList!=null && dateWeekLogList.size()!=0){
				List<DayLog> daylog = dateWeekLogList.get(0).getDaylogList();
				Date st = daylog.get(0).getLogDate();
				List<DayLog> minDayLog = dateWeekLogList.get(dateWeekLogList.size()-1).getDaylogList();
				Date end = minDayLog.get(minDayLog.size()-1).getLogDate();
				name+="（"+new SimpleDateFormat("MM月dd日").format(end)+"-"+new SimpleDateFormat("MM月dd日").format(st)+"）";
			}
			return name;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "个人周报";
	}
	
	public String toExport(){
		try {
			return "export";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DateDayLog> getDateDayLogList() {
		return dateDayLogList;
	}

	public void setDateDayLogList(List<DateDayLog> dateDayLogList) {
		this.dateDayLogList = dateDayLogList;
	}

	public List<DayLogVote> getVoteList() {
		return voteList;
	}
	public void setVoteList(List<DayLogVote> voteList) {
		this.voteList = voteList;
	}

	public PersonWeekLog getPersonWeekLog() {
		return personWeekLog;
	}

	public void setPersonWeekLog(PersonWeekLog personWeekLog) {
		this.personWeekLog = personWeekLog;
	}

	public DateDayLog getDateDayLog() {
		return dateDayLog;
	}

	public void setDateDayLog(DateDayLog dateDayLog) {
		this.dateDayLog = dateDayLog;
	}
}
