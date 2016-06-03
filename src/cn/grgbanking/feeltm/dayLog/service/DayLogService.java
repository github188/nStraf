package cn.grgbanking.feeltm.dayLog.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.util.Arith;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.util.Page;
import flex.messaging.io.ArrayList;

@Service("dayLogService")
@Transactional
public class DayLogService {
	@Autowired
	private DayLogDao dayLogDao;
	//拥有确认工时权限的用户组
	private final String [] AUTHGROUPARR  = {"administrator","deptManager","groupManager","prjManageGroup"};


	public boolean delete(String[] ids) {
		return dayLogDao.remove(ids);
	}


	@Transactional(readOnly = true)
	public Page getPage(DateDayLog dateDayLog, String queryStartTime, String queryEndTime,int pageNum, int pageSize,boolean queryListByGroup) {
		return dayLogDao.getMyPage(dateDayLog, queryStartTime, queryEndTime, pageNum, pageSize,queryListByGroup);
	}


	/** 批量保存
	 * @param daylogList
	 * @return
	 */
	public boolean saveDayLogList(List<DayLog> daylogList) {
		boolean flag = false;
		try {
			dayLogDao.saveDayLogList(daylogList);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	/**根据id获取对象
	 * @param id
	 * @return
	 */
	public DayLog getCaseById(String id) {
		return (DayLog)dayLogDao.getObject(DayLog.class, id);
	}

	/** 获取指定用户的指定日期的所有日志
	 * @param userId
	 * @param logDate
	 * @return
	 */
	public List<DayLog> getReportsByDay(String userId, Date logDate) {
		return dayLogDao.selectReports(userId, logDate);
	}

	/** 删除指定用户和日期的所有日志，并插入新的日志
	 * @param userid 用户id
	 * @param submitOldDate 日期
	 * @param daylogList 新的日志
	 * @return
	 */
	public boolean updateAll(String userid, Date submitOldDate,List<DayLog> daylogList) {
		return dayLogDao.updateAll(userid, submitOldDate,daylogList);
	}

	/**判断是否存在指定日期的日志
	 * @param userid
	 * @param logDate
	 * @return
	 */
	public boolean hasDayLogList(String userid, Date logDate) {
		List list=getReportsByDay(userid, logDate);
		return (list!=null&&list.size()>0)?true:false;
	}
	
	public boolean hasDayLogList(String projectId){
		List list=dayLogDao.getObjectList("from DayLog where prjName ='"+projectId+"'");
		return (list!=null&&list.size()>0)?true:false;
	}

	/**删除指定用户指定日期的日志
	 * @return
	 */
	public boolean remove(String userId, Date logDate) {
		return dayLogDao.remove(userId,logDate);
	}


	/** 获取指定用户指定日期的日志评论
	 */
	public List<DayLogVote> getVoteHistoryList(DayLog daylog) {
		return dayLogDao.getVoteHistoryList(daylog);
	}


	/**保存评论
	 */
	public boolean saveDayLogVote(DayLogVote vote) {
		return dayLogDao.saveDayLogVote(vote);
	}


	/**更新日志daylog
	 * wtjiao 2014年7月2日 上午8:57:33
	 * @param daylog
	 */
	public void updateDayLog(DayLog daylog) {
		 dayLogDao.updateObject(daylog);
	}
	public String getFirstDaylogId(List dayloglist){
		return dayLogDao.generateDateDaylogId(dayloglist);
	}
	
	public List queryList(DateDayLog dateDayLog,String queryStartTime,String queryEndTime) {
		return dayLogDao.queryList(dateDayLog, queryStartTime, queryEndTime);
	}
	
	public List queryDateDaylogList(DateDayLog dateDayLog,String queryStartTime,String queryEndTime) {
		List list=dayLogDao.queryList(dateDayLog, queryStartTime, queryEndTime);
		return dayLogDao.getDateDayLogList(list,"asc",dateDayLog);
	}

	/**根据时间段查询日志
	 * wtjiao 2014年9月30日 下午1:22:45
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<DayLog> queryLogByDate(Date startTime, Date endTime) {
		return dayLogDao.queryLogByDate(startTime,endTime);
	}
	
	/**
	 * 
	 * @param dayLog
	 * zzhui1
	 * 2014年10月27日
	 */
	public void save(DayLog dayLog) {
		this.dayLogDao.addObject(dayLog);
	}
	
	/**
	 * 查询日志管理对应表数据，计算出某人某日的工时总和，按日期排序展现本月日志工时
	 * @param userid 用户ID
	 * @param date 日志日期
	 * @return
	 */
	public Double queryWorkhoursByUserIdDate(String userid,  String date){
		return dayLogDao.queryWorkhoursByUserIdDate(userid, date);
	}
	
	/**
	 * 根据用户ID,日志日期查询当天的日志填写情况，只返回一条记录即可
	 * 
	 * @param userid
	 *            用户ID
	 * @param logDate
	 *            日志日期
	 * @return true:有日志 false:无日志
	 */
	public DayLog queryDayLogByUseridLogDate(String userid,  String logDate){
		return dayLogDao.queryDayLogByUseridLogDate(userid, logDate);
	}
	
	/**
	 * 获取批量确认工时数据(项目经理)未确认
	 * @param userModel
	 * @param prjname
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnterRecord(UserModel userModel,String startDate,String endDate) {
		return dayLogDao.getMoreWorkEnterRecord(userModel, startDate, endDate);
	}
	
	/**
	 * 获取批量确认工时数据(项目经理)已确认
	 * @param userModel
	 * @param prjname
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnteredRecord(UserModel userModel,String startDate,String endDate) {
		return dayLogDao.getMoreWorkEnteredRecord(userModel, startDate, endDate);
	}
	
	/**
	 * 获取批量确认工时数据(部门经理)
	 * @param userModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnterRecordForDept(String deptname,String startDate,String endDate) {
		return dayLogDao.getMoreWorkEnterRecordForDept(deptname, startDate, endDate);
	}
	
	/**
	 * 获取批量确认工时数据(部门经理)已确认
	 * @param userModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnteredRecordForDept(String deptname,String startDate,String endDate) {
		return dayLogDao.getMoreWorkEnteredRecordForDept(deptname, startDate, endDate);
	}
	
	/**
	 * 获取当天项目总工时
	 * @param userid
	 * @param logdate
	 * @return
	 */
	public Double getAllProjectHour(String userid,String logdate){
		return dayLogDao.getAllProjectHour(userid, logdate);
	}
	
	/**
	 * 获取项目的总确认工时
	 * @param userid
	 * @param logdate
	 * @return
	 */
	public Double getConfirmHourByPrjname(String userid,String logdate,String prjname){
		return dayLogDao.getConfirmHourByPrjname(userid, logdate, prjname);
	}
	
	/**
	 * 根据条件获取日志数据
	 * @param userid
	 * @param logdate
	 * @param userModel
	 * @param prjname
	 * @param confrimStatus
	 * @return
	 */
	public List<DayLog> getDaylogInfoByUseridAndLogdate(String userid,String logdate,UserModel userModel,String prjname,String confrimStatus){
		return dayLogDao.getDaylogInfoByUseridAndLogdate(userid, logdate, userModel, prjname, confrimStatus);
	}
	
	/**
	 * 根据条件获取总任务工时
	 * @param userid
	 * @param logdate
	 * @param prjname
	 * @return
	 */
	public Double getProjectHourByCondition(String userid,String logdate,String prjname,String confrimStatus){
		return dayLogDao.getProjectHourByCondition(userid, logdate, prjname, confrimStatus);
	}
	
	/**
	 * 根据项目统计项目的月总工时（项目经理）
	 * @param startdate
	 * @return
	 */
	public Double getMonthAllConfirmHour(String startdate,String prjid){
		return dayLogDao.getMonthAllConfirmHour(startdate,prjid);
	}
	
	/**
	 * 统计月总工时（部门经理）
	 * @param startdate
	 * @return
	 */
	public Double getMonthAllConfirmHourByDept(String startdate,String deptname){
		return dayLogDao.getMonthAllConfirmHourByDept(startdate, deptname);
	}
	
	/**
	 * 获取昨天未确认的项目及项目经理信息，除“其他项目”外
	 * @param yesterday
	 * @return
	 */
	public List<Project> getNoConfirmPrjname(String yesterday){
		return dayLogDao.getNoConfirmPrjname(yesterday);
	}
	
	/**
	 * 获取昨天未确认的部门经理信息
	 * @param yesterday
	 * @return
	 */
	public List<SysUser> getNoConfirmDept(String yesterday){
		return dayLogDao.getNoConfirmDept(yesterday);
	}
	
	
	/**
	 * 根据时间段获取未确认的日志数据
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public boolean getNoConfirmDayLogForTool(String startdate,String enddate){
		try{
			List<DayLog> list = dayLogDao.getNoConfirmDayLogForTool(startdate, enddate);
			for(int i=0;i<list.size();i++){
				DayLog log = list.get(i);
				Double allprojectHour = getAllProjectHour(log.getUserId(),DateUtil.getDateString(log.getLogDate()));
				Double projectHour = log.getSubTotal();
				Double projectDay = Arith.round(Arith.div(projectHour,allprojectHour),2);
				Double enterHour = log.getConfirmHour();
				Double enterDay = Arith.round(projectDay*(Arith.div(enterHour,projectHour)),2);
				log.setProjectDay(projectDay);
				log.setEnterDay(enterDay);
//				log.setConfirmStatus("1");
//				log.setEnterRole("deptManager");
//				log.setEnterPeople("admin");
				updateDayLog(log);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获取前一天的工作日
	 * @param currentDate
	 * @return
	 */
	public String getYesterdayWorkDate(String currentDate){
		return dayLogDao.getYesterdayWorkDate(currentDate);
	}
	
	/**
	 * 根据项目名称，得出日志确认角色
	 * @param prjname
	 * @return
	 */
	public String getProjectManagerByPrjname(String prjname){
		return dayLogDao.getProjectManagerByPrjname(prjname);
	}
	
	/**
	 * 根据条件获取未确认日志的所有日期
	 * @param yesterday
	 * @return
	 */
	public String getNoConfirmDayLogDate(String yesterday,String prjname){
		return dayLogDao.getNoConfirmDayLogDate(yesterday,prjname);
	}
	
	/**
	 * 根据条件条件获取未确认日志的日期
	 * @param yesterday
	 * @param deptname
	 * @return
	 */
	public String getNoConfirmDayLogDateForDept(String yesterday,String deptname){
		return dayLogDao.getNoConfirmDayLogDateForDept(yesterday, deptname);
	}


	
/**
 *  author:ljlian2
 *  2015-03-30
 * 获取用户当天的日志
 * @param userId  用户id
 * @param today
 */
	public boolean  getUserTodayDaylog(String userId, String today) {
		return dayLogDao.getTodayDayLog(userId,today);
		
	}

	/**
	 * 按部门获取前一天的日志内容
	 * @param deptName   部门名称  开发二部
	 * @param yesterday 昨天
	 * @return
	 * @author ljlian
	 */
	public List<DayLog> getAllDaylogListByDept(String deptName, String yesterday) {
		return dayLogDao.getAllDaylogListByDept(deptName,yesterday);
	}
	
	
	
	 
    /**
     * getPrjCensusInfos:描述 
     * 将数据 List<Object[]> 转成List<Map>
     * <br/>
     * 2016年3月25日 下午4:53:34
     * @param censusPrjInfos 原数据集
     * @return List<Map>
     * @author zzwen6
     * @修改记录: <br/>
     */
    public  List<Map> getPrjCensusInfos(List<Object[]>  censusPrjInfos){
        
       
        Iterator it = censusPrjInfos.iterator();
        Map map = null;
        List paramsList = new ArrayList(); 
        Object[] objs = null;
        while(it.hasNext()){
            objs = (Object[]) it.next();
            map = new HashMap(); 
            // 这个写法有点死，后来者请修改！
            // 2016年3月25日16:04:17
            map.put("cuserid", objs[0]);
            map.put("cusername", objs[1]);
            map.put("cdetname", objs[2]);
            map.put("cgroupname", objs[3]);
            map.put("cprjdays", objs[4].toString());
            map.put("totaldays", objs[5].toString());
            map.put("cstatus", objs[6]);
            map.put("leavedays", objs[7]==null?"":objs[7].toString());
             
            
            paramsList.add(map);
        }
        
        return  paramsList;
    }


    /**
     * 这里是新添加的方法，如果用现在的方法将查询不到；
     * 通过源代码，template用到createQuery()方法，无法查询native sql;
     * 所以添加一个新的getpage dao
     * 查询分页工具，得到一个分页配置page
     * getPrjCensusInfosByPages:描述 <br/>
     * 2016年3月25日 下午4:25:49
     * @param startDate 查询范围时间
     * @param endDate 
     * @param deptName 部门名称
     * @param pageNum 页码
     * @param pageSize 页面大小 default= 20
     * @return page 工具
     * @author zzwen6
     * @修改记录: <br/>
     */
    public Page getPrjCensusInfosByPages(String startDate, String endDate, DateDayLog dayLog, int pageNum, int pageSize) {
         
        return dayLogDao.findPrjCensusInfosByPages(startDate,endDate,dayLog,pageNum,pageSize);
    }
    
    public List<Map> getPrjCensusInfos(String startDate, String endDate, DateDayLog dateDayLog){
         List list = dayLogDao.findAllPrjCensusInfos(startDate, endDate, dateDayLog);
         List<Map> lists = getPrjCensusInfos(list);
         return lists;
    }
}
