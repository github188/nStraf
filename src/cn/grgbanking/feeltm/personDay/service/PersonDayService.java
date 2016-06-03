/**
 * 
 */
package cn.grgbanking.feeltm.personDay.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esri.arcgis.geoprocessing.tools.datamanagementtools.SimplifyPolygon;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.personDay.bean.SimplePersonDayBean;
import cn.grgbanking.feeltm.personDay.dao.PersonDayDao;
import cn.grgbanking.feeltm.personDay.domain.DeptMonthPersonDay;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.dao.ProjectResourcePlanDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.ProjectResourcePlan;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.PingYingUtil;
import cn.grgbanking.framework.service.BaseService;


/**
 * @author ping
 *
 */
@Service
public class PersonDayService extends BaseService{
	@Autowired
	PersonDayDao personDayDao;
	@Autowired
	private DayLogDao daylogDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectResourcePlanDao projectResourcePlanDao;
	/** 用户ID */
	private static final String ERROR_RESULT = "result";
	/** 用户ID */
	private static final String ERROR_COUNT = "count";
	/** 用户ID */
	private static final String ERROR_USER_ID = "userId";
	/** 用户名 */
	private static final String ERROR_USER_NAME = "userName";
	/** 未填写日志的日期 */
	private static final String ERROR_NO_LOG_DATE = "noLogDate";
	
	/**
	 * 获取简化的人日统计数据
	 * @return Map<String,SimplePersonDayBean> key:年份 value:人日数据BEAN 
	 * @author lping1 2014-10-14
	 */
	/*
	public Map<String,List<SimplePersonDayBean>> getSimplePersonDayData(){
		List<SimplePersonDayBean> simplePersonDayBeanList;
		Map<String,List<ProjectPersonDayBean>> personDayMap = CommonData.personDayByYear;
		Map<String,List<SimplePersonDayBean>> yearDataMap = new LinkedHashMap<String,List<SimplePersonDayBean>>();
		Set<Entry<String,List<ProjectPersonDayBean>>> set = personDayMap.entrySet();
		for(Entry entry : set){
			SimplePersonDayBean myPersonDayBean;
			simplePersonDayBeanList = new ArrayList();
			List<ProjectPersonDayBean> prjPDBeanList = (List<ProjectPersonDayBean>)entry.getValue();
			for(ProjectPersonDayBean prjPDBean : prjPDBeanList){
				myPersonDayBean = new SimplePersonDayBean();
				myPersonDayBean.setPrjId(prjPDBean.getProjectId());
				myPersonDayBean.setPrjName(prjPDBean.getProjectName());
				//获取12个月统计
				List<ProjectMonthPersonDayBean> detail = prjPDBean.getDetail();
				Integer[] monthCountsArr = new Integer[12];
				for(ProjectMonthPersonDayBean bean : detail){
					int index = bean.getMonthIndex()-1;
					if(index>=0 && index <=11){
						try {
							monthCountsArr[index] = bean.getConfirmPersonDay();
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				myPersonDayBean.setCurTotalNum((int)prjPDBean.getCurYearPersonDay());
				myPersonDayBean.setHisTotalNum((int)prjPDBean.getBeforeCurYearSumPersonDay());
				myPersonDayBean.setMonthCounts(Arrays.asList(monthCountsArr));
				simplePersonDayBeanList.add(myPersonDayBean);
			}
			yearDataMap.put((String)entry.getKey(),simplePersonDayBeanList);	
		}
		return yearDataMap;
	}
	*/
	
	/**
	 * 获取简化的人日统计数据
	 * @return Map<String,SimplePersonDayBean> key:年份 value:人日数据BEAN 
	 * @author whxing 2014-11-3
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<SimplePersonDayBean>> getSimplePersonDayData(){
		Map<String,List<SimplePersonDayBean>> yearDataMap = new HashMap<String, List<SimplePersonDayBean>>();
		Calendar a=Calendar.getInstance();  
		int year = a.get(Calendar.YEAR);//得到年
		Map<String,List<String>> yearMap = new HashMap<String,List<String>>();
		String yearString = "";
		for(int i=year-9;i<=year;i++){
			yearString = String.valueOf(i);
			//获取一年中所有项目的ID
			List projectIdListbyYear = personDayDao.getProjectId(yearString);
			List<SimplePersonDayBean> simplePersonDayBeanList = new ArrayList<SimplePersonDayBean>();
			for (Object obj :projectIdListbyYear) {
				String projectId = (String)obj;
				Project project = projectDao.getProjectById(projectId);
				String isEnd = project.getIsEnd();
				//根据项目ID和年份获取项目一年12个月的记录
				List<PersonDay> list = personDayDao.getProjectForYear(yearString, projectId);
				if (list.size()>0) {
				    String prjName = list.get(0).getProjectName();
					List<Integer> confirmCountsArr =  new ArrayList<Integer>();
					int TotalNum = 0;
					SimplePersonDayBean simplePersonDayBean = new SimplePersonDayBean();
					for (int j = 0; j < 12; j++) {
						confirmCountsArr.add(0);//初始化值为0
					}
					for (int j = 0; j < list.size(); j++) 
					{
							PersonDay personDay = list.get(j);
							int index = Integer.parseInt(personDay.getMonth())-1;
							String confirm = personDay.getPersonDayConfirm();
							confirmCountsArr.set(index, Integer.parseInt(personDay.getPersonDayConfirm()));
							//判断personDay.getPersonDayEdit()是否由数字组成
							if ("true".equals(personDay.getIsEdit())&&StringUtils.isNumeric(personDay.getPersonDayEdit())) {
								int PersonDayEdit = Integer.parseInt(personDay.getPersonDayEdit());
								TotalNum+= PersonDayEdit;
								confirmCountsArr.set(index, PersonDayEdit);
							}else{
								TotalNum+= Integer.parseInt(personDay.getPersonDayConfirm());
							}
					}
					simplePersonDayBean.setCurTotalNum(TotalNum);
					simplePersonDayBean.setConfirmCountsArr(confirmCountsArr);
					simplePersonDayBean.setPrjId(projectId);
					simplePersonDayBean.setPrjName(prjName);
					simplePersonDayBean.setIsEnd(isEnd);
					simplePersonDayBeanList.add(simplePersonDayBean);
					
				}
			}
			Collections.sort(simplePersonDayBeanList,new Comparator<SimplePersonDayBean>() {
				public int compare(SimplePersonDayBean o1, SimplePersonDayBean o2) {
					return PingYingUtil.cn2FirstSpell(o1.getPrjName()).compareTo(PingYingUtil.cn2FirstSpell(o2.getPrjName()));
				}
			});
			yearDataMap.put(yearString, simplePersonDayBeanList);
		}
		return yearDataMap;
	}
	
	
	

	/**保存Personday对象
	 * wtjiao 2014年10月30日 上午10:54:44
	 * @param pd
	 */
	public void save(PersonDay pd) {
		try{
			personDayDao.addObject(pd);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    /**
     *查询Personday对象
     * @param projectId
     * @param year
     * @param month
     * @return
     */
	public List queryPersonDayByclick(String projectId, String year, String month){
		List list=personDayDao.queryPersonDayByclick(projectId, year, month);
		Collections.sort(list,new Comparator<PersonDay>() {
			public int compare(PersonDay o1, PersonDay o2) {
				return PingYingUtil.cn2FirstSpell(o1.getProjectName()).compareTo(PingYingUtil.cn2FirstSpell(o2.getProjectName()));
			}
		});
		return list;
	}

	/**
     *查询Personday对象
     * @param projectid
     * @return
     */
	public List queryPersonDayById(String id){
		return personDayDao.queryPersonDayById(id);
	}
	/**
     *修改Personday对象
     * @return boolean
     */
	public boolean updatePersonDay(PersonDay personDay){
		return personDayDao.updatePersonDay(personDay);
	}
	
	
	/**
	 * 执行人日统计
	 * 对日志表的当月数据进行统计，统计的分组条件：项目名
	 * 统计结果保存到人日统计表
	 * 
	 * 20150302 wangtj 人日确认数据不再重新统计，转为读取日志表中的确认人日字段（在实现成本控制模块时，增加了该字段）
	 * 
	 * @return 统计及结果 true:统计成功  false：统计失败
	 */
	public boolean projectPersonDayStatisticByMonth(String month){
		try{
			List<DayLog> dayLogList=null;
			if(month==null){
				Date d=new  Date();
				SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM");
				month=sdf.format(d);
				//从日志表中查询当月的数据
				dayLogList= daylogDao.queryDayLogStatistic(month);
			}else{
				//从日志表中查询当月的数据
				dayLogList = daylogDao.queryDayLogStatistic(month);
			}
			
			//转为Map<项目id，项目日志>结构,方便根据项目统计
			Map<String,List<DayLog>> projectLogMap=toProjectLogMap(dayLogList);
			//查询所有的项目
			List<Project> projectList=projectDao.listUnFinishedGroup();
			List<PersonDay> pdList=new ArrayList<PersonDay>();
			
			//遍历项目，得到每个项目的本月累计人日
			for(int i=0;i<projectList.size();i++){
				Project p=projectList.get(i);
				
				PersonDay personDay = initPersonDay(p,month);//创建初始化人日实体
				
				//获取该项目的日志
				List<DayLog> projectLogList=projectLogMap.get(p.getId());
				//获取已经累加的人日
				double sumPersonDay=0;
				if(projectLogList!=null){
					for(int j=0;j<projectLogList.size();j++){
						DayLog dayLog = projectLogList.get(j);
						//boolean inTraineeUser=inTraineeUsers(getTraineeUsers(), dayLog.getUserId());
						boolean inTraineeUser = inTraineeUsers(dayLog);
						boolean isGroupManagerConfirm=dayLog.getEnterRole()==null?true:"groupManager".equals(dayLog.getEnterRole())?true:false;
						//   只统计  日志状态（  confirmStatus  1 ）为已经确认的工时  confirmStatus   ---    修改  
						int confirmStatus = Integer.parseInt(dayLog.getConfirmStatus());
						if(!inTraineeUser&&isGroupManagerConfirm){//不是实习人员且是项目经理确认的(实习人员的人日不计算入项目的总人日  20150320 wtjiao)
						//当前日志人日
							Double eleEnterDay=dayLog.getEnterDay();
							//加上当前人日
							sumPersonDay+=eleEnterDay==null?0D:eleEnterDay;
						}
					}
				}
				//设置回对象
				personDay.setPersonDayConfirm(sumPersonDay+"");
				pdList.add(personDay);
			}
			
			//得到personday列表后，遍历这个列表，对人日总数据做一个四舍五入，并保存到数据库 
			for(PersonDay personDay:pdList){
				personDay.setPersonDayConfirm(Math.round(Double.parseDouble(personDay.getPersonDayConfirm()))+"");
				//保存到人日表中
				saveOrUpdateData(personDay);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	/**
	 * 判断是否是实习生
	 * @param dayLog  日志
	 * @return
	 */
	private boolean inTraineeUsers(DayLog dayLog) {
		boolean flag = false;
		try {
			String employStatus = dayLog.getEmploystatus();//员工的状态  实习 试用、正式 注意要去掉前后的空格
			//如果员工的状态为实习生  study  返回true
			if( employStatus != null && "study".equals(employStatus)){
				flag =  true;
			}else{
				flag =  false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}




	/**获取不监控人员列表
	 * @return
	 */
	private String getTraineeUsers() {
		List<SysDatadir> dirs=BusnDataDir.getObjectListInOrder("costControl.traineeNameList");
		if(dirs!=null){
			for(int i=0;i<dirs.size();i++){
				if(dirs.get(i).getKey().equals("trainess")){
					return dirs.get(i).getValue();
				}
			}
		}
		return "";
	}
	
	/**用户是否在实习生名单中
	 * @param traineeUsers 不监控人员列表
	 * @param userid 指定人员
	 * @return
	 */
	private boolean inTraineeUsers(String traineeUsers, String userid) {
		if(StringUtils.isNotBlank(traineeUsers)){
			String[] tus=traineeUsers.split(",");
			for(String trainee:tus){
				if(trainee.equals(userid)){
					return true;
				}
			}
		}
		return false;
	}
	
	private Map<String, List<DayLog>> toProjectLogMap(List<DayLog> dayLogList) {
		Map<String,List<DayLog>> map=new HashMap<String, List<DayLog>>();
		if(dayLogList!=null){
			//遍历日志
			for(DayLog log:dayLogList){
				if(map.get(log.getPrjName())==null){//map中不存在，则构造list，加入到map中
					List<DayLog> list=new ArrayList<DayLog>();
					list.add(log);
					map.put(log.getPrjName(), list);
				}else{//存在，取得原来的list，并在此基础上添加新的log
					List<DayLog> list=map.get(log.getPrjName());
					list.add(log);
					map.put(log.getPrjName(), list);
				}
			}
		}
		return map;
	}




	/**
	 * 保存或者更新表内容
	 * @param personDay 人日表
	 */
	private void saveOrUpdateData(PersonDay personDay){
		String projectId = personDay.getProjectId();
		String year = personDay.getYear();
		String month = personDay.getMonth();
		PersonDay existPersonDay = personDayDao.existPersonDay(projectId, year, month);
		if (existPersonDay != null) {
			existPersonDay.setPersonDayConfirm(personDay.getPersonDayConfirm());
			existPersonDay.setEstimateDetail(personDay.getEstimateDetail());
			existPersonDay.setUpdateTime(new Date());
			existPersonDay.setUpdateuserId("system-timer");
			existPersonDay.setUpdateUsername("system-timer");
			personDayDao.saveOrUpdateData(existPersonDay);
		} else {
			personDay.setUpdateTime(new Date());
			personDay.setUpdateuserId("system-timer");
			personDay.setUpdateUsername("system-timer");
			personDayDao.saveOrUpdateData(personDay);
		}
	}
	
	/**
	 * 初始化人日表实体
	 * @param dayLog 日志表实体
	 * @return 人日表实体
	 */
	private PersonDay initPersonDay(Project p, String month){
		PersonDay personDay = new PersonDay();
		personDay.setIsEdit("false");
		personDay.setType("month");
		personDay.setError("0");//设置默认误差为0，后续在projectPersonDayErrorStatisticByMonth中会重新计算
		personDay.setEstimateDetail("");
		
		personDay.setProjectId(p.getId());
		personDay.setProjectName(p.getName());
		personDay.setYear(month.split("-")[0]);
		personDay.setMonth(month.split("-")[1]);
		personDay.setExt1(month);
		return personDay;
	}
	
	/**
	 * 定时任务方法 <br>
	 * 人日异常数据的统计
	 * @return 统计成功与否
	 */
	public boolean projectPersonDayErrorStatisticByMonth(String m){
		try {
			//日期列表.当天以前到月初的日期，如果当天是月初则取上一个月的所有日期
			List<String> dateList = DateUtil.getWorkdayList(m);
			//从人日表中取项目列表数据
			String year = dateList.get(0).substring(0, 4);
			String month = dateList.get(0).substring(5, 7);
			List<PersonDay> personDayList = personDayDao.queryProjectListByYearMonth(year, month);
			//根据人日表查询出来的数据,查询人力分派表的数据,即每个项目的人员分配情况
			if (personDayList != null && personDayList.size() > 0) {
				for (PersonDay personDay : personDayList) {
					Project project = projectDao.getProjectById(personDay.getProjectId());
					List<ProjectResourcePlan>  prpList = new ArrayList<ProjectResourcePlan>();
					//取出项目人员分配数据
					if (project != null) {
						prpList = project.getProjectResourcePlan();
					}
					if (prpList != null && prpList.size() > 0) {
						List<String> noLogList = new ArrayList<String>();
						float errorTimes = 0f;
						try {
							//查询每个项目中,项目所有成员在计划的时间内的日志填写情况
							for (ProjectResourcePlan projectResourcePlan : prpList) {
								//实际开始日期
								String fsTime = DateUtil.getDateString(projectResourcePlan.getFactStartTime());
								//实际结束日期
								String feTime = DateUtil.getDateString(projectResourcePlan.getFactEndTime());
								//如果上述2个日期为空，则取计划的时间
								if (StringUtils.isBlank(fsTime)) {
									fsTime = DateUtil.getDateString(projectResourcePlan.getPlanStartTime());
								}
								if (StringUtils.isBlank(feTime)) {
									feTime = DateUtil.getDateString(projectResourcePlan.getPlanEndTime());
								}
								
								if (StringUtils.isEmpty(projectResourcePlan.getUserid()) 
										|| StringUtils.isEmpty(projectResourcePlan.getProject().getId())){
									//没有用户ID或者没有分配用户到项目中或者异常的项目数据，则转到下一条数据
									continue;
								}
								for (String date : dateList) {
									//是否填写日志
									boolean fillDayLog = false;
									//判断日期是否在实际的时间段内,而且是在工作日内,节假日未判断
									if (checkDate(fsTime, feTime, date)) {
										fillDayLog = daylogDao.queryDayLogByIdDate(projectResourcePlan.getUserid(), 
												projectResourcePlan.getProject().getId(), date);
									} else{
										//不符合条件跳至下一天
										continue;
									}
									//一个人在多个项目中的情况，只要该人当天有写日志，则不算人日误差数据
									if (!fillDayLog) {
										fillDayLog = daylogDao.queryDayLogByUseridDate(projectResourcePlan.getUserid(), date);
									} 
									
									if (!fillDayLog) {
										JSONObject noLogInfo = new JSONObject();
										//没有写日志则将日期和用户保存
										noLogInfo.put(ERROR_USER_ID, projectResourcePlan.getUserid());
										noLogInfo.put(ERROR_USER_NAME, projectResourcePlan.getUsername());
										noLogInfo.put(ERROR_NO_LOG_DATE, date);
										noLogList.add(noLogInfo.toString());
										int prjNumber = projectResourcePlanDao.getProjectNumberByUseridDate(projectResourcePlan.getUserid(), date);
										errorTimes = sumPrjNumber(errorTimes, prjNumber);
									}
								}
							}
							JSONObject noLogInfoJsonList = new JSONObject();
							noLogInfoJsonList.put(ERROR_RESULT, noLogList.toArray());
							noLogInfoJsonList.put(ERROR_COUNT, noLogList.size());
							personDay.setError(String.valueOf(errorTimes));
							personDay.setEstimateDetail(noLogInfoJsonList.toString());
							//更新人日表的人日异常数据
							saveOrUpdateData(personDay);
						} catch (JSONException e) {
							SysLog.error("JSON数据异常:" + e.toString());
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			SysLog.error("执行有异常:" + e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 根据项目数，平摊人日在进行累加算出总的人日误差
	 * @param sum 累加数
	 * @param prjNumber 项目数
	 * @return 累加数结果
	 */
	private float sumPrjNumber(float sum, int prjNumber){
		float result=0f;
		sum = sum + 1/(float)prjNumber;
		//保留1位小数
		result = (float)(Math.round(sum*10))/10;
		return result;
	}
	
	/**
	 * 判断当天是否在计划的日期内,而且是在工作日内
	 * 
	 * @param factStartTime
	 *            实际工作开始日
	 * @param factEndTime
	 *            实际工作结束日
	 * @param logDate
	 *            进行判断的日期
	 * @return 是否可以使用该日期作为查询条件  true:可以 false:不可以
	 */
	private boolean checkDate(String factStartTime, String factEndTime,
			String logDate) {
		if (!(logDate.compareTo(factStartTime) < 0)
				&& (!(logDate.compareTo(factEndTime) > 0))
				&& (!DateUtil.isWeekend(logDate))) {
			return true;
		}
		return false;
	}
	
	
	
	
	/**
	 * 根据部门划分统计人日<br>
	 * 对日志表的当月数据进行统计，统计的分组条件：部门名<br>
	 * 统计结果保存到oa_personday_dept<br>
	 * @return 统计及结果 true:统计成功  false：统计失败
	 */
	public boolean savePersonDayByDept() {
		//从日志表中查询当月的数据
		List<DayLog> dayLogStatisticList = daylogDao.queryDayLogOrderBydept();
		//List<PersonDay> pdlist = new ArrayList<PersonDay>();
		if (dayLogStatisticList != null && dayLogStatisticList.size() > 0) {
			String logDate = "";
			Double workhours = 0d;
			Double personday = 0d;
			Double persondaySum= 0d;
			double standardWorkhour = 8d;
			try {
				//创建一个初始的人日实体
				DeptMonthPersonDay deptMonthPDay = new DeptMonthPersonDay();
				deptMonthPDay= initDeptMonthPersonDay(dayLogStatisticList.get(0));
				for(int i = 0; i < dayLogStatisticList.size(); i++){
					DayLog dayLog = dayLogStatisticList.get(i);
					logDate = DateUtil.getDateString(dayLog.getLogDate());
					//根据用户ID和日志日期查询该用户的当天总工时
					workhours = daylogDao.queryWorkhoursByUserIdDate(dayLog.getUserId(), logDate);
					//人日=当前项目工时/总工时(总工时<8时,当作8)
					personday = dayLog.getConfirmHour() / (workhours >= standardWorkhour ? workhours : standardWorkhour);
					if (StringUtils.isBlank(dayLog.getPrjName())) {
						continue;
					}
					if (dayLog.getDetName().equals(deptMonthPDay.getDeptName())) {
						//如果item的项目名存在则累加统计当前项目的人日
						persondaySum = persondaySum + personday;
					} else {
						//如果不存在则添加一条人日实体,同时将上一次的人日保存到实体类,将本次的统计也存起来
						deptMonthPDay.setPersonDay(String.valueOf(Math.round(persondaySum)));
						//保存到人日表中
						saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
						//pdlist.add(personDayBean);
						persondaySum = personday;
						deptMonthPDay= initDeptMonthPersonDay(dayLog);
					}
					//最后一条的处理
					if (i == (dayLogStatisticList.size()-1)) {
						deptMonthPDay.setPersonDay(String.valueOf(Math.round(persondaySum)));
						//保存到人日表中
						saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
						//pdlist.add(personDayBean);
						persondaySum = 0d;
					}
				}
			} catch (Exception e) {
				System.out.println("部门人日统计报错");
				return false;
			}
		}
		return true;
	}
	/**
	 * 初始化oa_personday_dept表实体
	 * @param dayLog 日志表实体
	 * @return mon
	 */
	private DeptMonthPersonDay initDeptMonthPersonDay(DayLog dayLog){
		DeptMonthPersonDay deptMonthPersonDay = new DeptMonthPersonDay();
		//根据部门名称获取部门Id
		Map<String,Object> deptMap=BusnDataDir.getMapKeyObject("staffManager.department");
		Set<String> key = deptMap.keySet();        
	   for (Iterator it = key.iterator(); it.hasNext();)
	   { 
		   	String keystr = (String) it.next(); 
		   String  keyValue = BusnDataDir.getMapKeyValue("staffManager.department").get(keystr).toString().trim();
		   if (keyValue.equals(dayLog.getDetName())) {
			   deptMonthPersonDay.setDeptId(keystr);
			   break;
		}
	   }
		deptMonthPersonDay.setDeptName(dayLog.getDetName());
		String logDate = DateUtil.getTimeYYYYMMDDHHMMString(dayLog.getLogDate());
		String year = logDate.substring(0, 4);
		String month = logDate.substring(4, 6);
		deptMonthPersonDay.setYear(year);
		deptMonthPersonDay.setMonth(month);
		deptMonthPersonDay.setCreateTime(new Date());
		return deptMonthPersonDay;
	}
	/**
	 * 保存或者更新表内容
	 * @param personDay 人日表
	 */
	private void saveOrUpdateDeptMonthPersonDay(DeptMonthPersonDay deptMonthPDay){
		String deptName = deptMonthPDay.getDeptName();
		String year = deptMonthPDay.getYear();
		String month = deptMonthPDay.getMonth();
		DeptMonthPersonDay existDeptMonthPersonDay = personDayDao.existDeptMonthPersonDay(deptName, year, month);
		if (existDeptMonthPersonDay != null) {
			existDeptMonthPersonDay.setPersonDay(deptMonthPDay.getPersonDay());
			existDeptMonthPersonDay.setCreateTime(new Date());
			personDayDao.saveOrUpdateDeptMonthPersonDay(existDeptMonthPersonDay);
		} else {
			personDayDao.saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
		}
	}
	
	public void removeByProjectId(String projectId){
		personDayDao.removePersonDayByProjectId(projectId);
	}
}
