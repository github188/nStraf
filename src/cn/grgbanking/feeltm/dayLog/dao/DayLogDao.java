package cn.grgbanking.feeltm.dayLog.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.costControl.domain.DeptGeneralCost;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.feeltm.integralCenter.dao.IntegralCenterDao;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("dayLogDao")
public class DayLogDao extends BaseDao<WeekReport>{
	@Autowired
	private IntegralCenterDao integralCenterDao;
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<WeekReport> reports=new ArrayList<WeekReport>();
		for (int j = 0; j < ids.length; j++) {	
				reports.add(new WeekReport(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(reports);
			flag=true;
		}catch(Exception e){
			System.out.println("WeekReportDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
//	返回page分页
	public List queryList(DateDayLog dateDayLog,String queryStartTime,String queryEndTime)
	{
		String hql = "FROM DayLog log WHERE 1=1 ";
		
			if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserDept())){
				hql += " and log.detName like '%"+dateDayLog.getUserDept()+"%' ";
			}
			if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserName())){
				hql += " and (log.userName like '%"+dateDayLog.getUserName()+"%' or log.userId like '%"+dateDayLog.getUserName()+"%')";
			}
			if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserId())){
				hql += " and log.userId in(" + dateDayLog.getUserId() + ")";
			}else{
				if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserGroup())){
					hql += " and log.groupName like '%"+dateDayLog.getUserGroup()+"%' ";
				}
			}
			if(StringUtils.isNotBlank(queryStartTime)){
				hql += " and log.logDate >= to_date('"+queryStartTime+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(queryEndTime)){
				hql += " and log.logDate <= to_date('"+queryEndTime+"','yyyy-MM-dd') ";
			}
			if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getConfirmStatus())){
				hql += " and log.confirmStatus='" + dateDayLog.getConfirmStatus() + "'";
			}
			
		hql += " order by log.logDate desc,log.updateTime desc, log.detName asc ";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}
	/**返回以项目为名的page分页
	 * @author tscheng
	 * @param dateDayLog
	 * @param queryStartTime
	 * @param queryEndTime
	 * @param onlyGroupLogs  是否查询与项目有关的日志
	 * @return
	 */
	public List queryListByGroup(DateDayLog dateDayLog,String queryStartTime,String queryEndTime,boolean onlyGroupLogs)
	{
		String hql = "FROM DayLog log WHERE 1=1 ";
		String queryProjectName="";
		if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserGroup())){
			queryProjectName=dateDayLog.getUserGroup();
		}
		if(!onlyGroupLogs){//查询项目的日志，而不是查询项目组成员日志(谁填写了该项目的日志就查谁的)
			hql += " and log.groupName like '%"+queryProjectName+"%' ";
			
		}else{//查询项目组成员日志(查询当日项目组成员的日志，不管该成员是否填写了该项目)
			hql+=" and log.userId in (";//查询时间和员工的进出时间有交集，则查询该员工(查询截至>=员工进入 && 查询开始<=员工退出)
			hql+=" select distinct(userid) from ProjectResourcePlan plan where plan.project.name like '%"+queryProjectName+"%' and  plan.factStartTime<= to_date('"+queryEndTime+"','yyyy-MM-dd') and plan.factEndTime>=to_date('"+queryStartTime+"','yyyy-MM-dd')";
			hql+=")";
		}
		if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserDept())){
			hql += " and log.detName like '%"+dateDayLog.getUserDept()+"%' ";
		}
		if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getUserName())){
			hql += " and (log.userName like '%"+dateDayLog.getUserName()+"%' or log.userId like '%"+dateDayLog.getUserName()+"%')";
		}
		if(StringUtils.isNotBlank(queryStartTime)){
			hql += " and log.logDate >= to_date('"+queryStartTime+"','yyyy-MM-dd') ";
		}
		if(StringUtils.isNotBlank(queryEndTime)){
			hql += " and log.logDate <= to_date('"+queryEndTime+"','yyyy-MM-dd') ";
		}
		if(dateDayLog!=null && StringUtils.isNotBlank(dateDayLog.getConfirmStatus())){
			hql += " and log.confirmStatus='" + dateDayLog.getConfirmStatus() + "'";
		}
			
		hql += " order by log.logDate desc,log.updateTime desc, log.detName asc ";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}
	
	 /**重写getPage方法，这里取出的不是Page<DayLog> 而是Page(DateDayLog)
	 * @param queryString
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getMyPage(DateDayLog dateDayLog,String queryStartTime,String queryEndTime, int pageNum,int pageSize,boolean queryListByGroup) {
		List daylogList=queryListByGroup(dateDayLog, queryStartTime, queryEndTime,queryListByGroup);
		List dateDayLogList=getDateDayLogList(daylogList,"desc",dateDayLog);
        Page page = new Page();
        pageNum = (pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize < 1) ? 15 : pageSize;

        int recordCount = dateDayLogList!=null?dateDayLogList.size():0;
        //设置当前页和记录总数
        page.setCurrentPageNo(pageNum);
        page.setRecordCount(recordCount);

        //设置总页数和页面尺寸
        page.setPageCount(computedPageCount(recordCount, pageSize));
        page.setPageSize(pageSize);
        if (recordCount > 0) {
        	//获取page列表
            page.setQueryResult(getPageData(dateDayLogList,pageNum,pageSize));
        }
        return page;
    }

	
	/**
	 * 从DayLog中转换为DateDayLog
	 */
	@SuppressWarnings("unchecked")
	public List getDateDayLogList(List srcList,final String sort, DateDayLog queryCon){
		return getDateDayLogList(srcList,sort);
	}
	
	@SuppressWarnings("rawtypes")
	public List getDateDayLogList(List srcList,final String sort){
		return getDateDayLogList(srcList);
	}
	
	@SuppressWarnings("rawtypes")
	public List getDateDayLogList(List srcList){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Map map=new HashMap();
			if(srcList!=null){
				for(int i=0;i<srcList.size();i++){
					DayLog daylog=(DayLog)srcList.get(i);
					String logDateStr=sdf.format(daylog.getLogDate());
					String userId=daylog.getUserId();
					//以logDate,userId为key，DayLog列表对象为value，放到map中（遍历srcList后，相同的logDate的数据会在同一个value中）
					String key=logDateStr+","+userId;
					if(map.get(key)!=null){
						List mapList=(List)map.get(key);
						mapList.add(daylog);
						map.put(key, mapList);
					}else{
						List mapList=new ArrayList();
						mapList.add(daylog);
						map.put(key, mapList);
					}
				}
			}
			//遍历map，取出数据，组成DateDayLog的list
			List desList=new ArrayList();
			Iterator keySetIte=map.keySet().iterator();
			while(keySetIte.hasNext()){
				String key=(String)keySetIte.next();
				List value=(List)map.get(key);
				
				DateDayLog dateDayLog=generateDateDaylog(value);
				//注：Id为当天第一条日志的id
				dateDayLog.setId(generateDateDaylogId(value));
				desList.add(dateDayLog);
			}
			
			//排序
			Collections.sort(desList, new Comparator<DateDayLog>() {
				public int compare(DateDayLog log1, DateDayLog log2) {
					if(log1.getLogDate().compareTo(log2.getLogDate())<0){
						return 1;
					}else if(log1.getLogDate().compareTo(log2.getLogDate())==0){
						if(log1.getUserName().compareTo(log2.getUserName())<0){
							return -1;
						}else{
							return 1;
						}
					}else{
						return -1;
					}
				}
			});
			return desList;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//按照姓名排序
	@SuppressWarnings("unchecked")
	public void sortByUserName(List desList){
		Collections.sort(desList, new Comparator<DateDayLog>() {
			public int compare(DateDayLog log1, DateDayLog log2) {
				//按人员从小到大
				if(log1.getUserName().compareTo(log2.getUserName())<0){
					return -1;
				}else if(log1.getUserName().compareTo(log2.getUserName())==0){
					return 0;
				}else{
					return 1;
				}
			}
		});
	}
	
	//按照日志日期排序
	@SuppressWarnings("unchecked")
	public void sortByLogDate(List desList,final String sort){
		Collections.sort(desList, new Comparator<DateDayLog>() {
			public int compare(DateDayLog log1, DateDayLog log2) {
				int sortFlag="asc".equals(sort)?-1:1;//如果传入的是asc，则从小到大排
				if(log1.getLogDate().compareTo(log2.getLogDate())<0){
					return sortFlag;
				}else{
					return sortFlag*-1;
				}
			}
		});
	}
	
	//按照部门排序
	@SuppressWarnings("unchecked")
	public void sortByDept(List desList){
		Collections.sort(desList, new Comparator<DateDayLog>() {
			public int compare(DateDayLog log1, DateDayLog log2) {
				if(log1.getUserDept().compareTo(log2.getUserDept())<0){
					return -1;
				}else{
					return 1;
				}
			}
		});
	}
	
	//按照项目排序
	@SuppressWarnings("unchecked")
	public void sortByProject(List desList){
		Collections.sort(desList, new Comparator<DateDayLog>() {
			public int compare(DateDayLog log1, DateDayLog log2) {
				if(log1.getUserGroup().compareTo(log2.getUserGroup())<0){
					return -1;
				}else{
					return 1;
				}
			}
		});
	}
	
	/**获取dateDaylogId
	 * wtjiao 2014年7月8日 下午1:53:57
	 * @param dayloglist 
	 * @return
	 */
	public String generateDateDaylogId(List dayloglist) {
		if(dayloglist!=null && dayloglist.size()>0){
			String id=((DayLog)dayloglist.get(0)).getId();
			for(int i=1;i<dayloglist.size();i++){
				if(ltCompareId(((DayLog)dayloglist.get(i)).getId(),id)){
					id=((DayLog)dayloglist.get(i)).getId();
				}
			}
			return id;
		}
		return null;
	}
	
	private boolean ltCompareId(String id1,String id2){
		return id1.hashCode()-id2.hashCode()<0;
	}

	/** 
	 * wtjiao 2014年6月4日 上午8:47:24
	 * @param value
	 * @return
	 */
	private DateDayLog generateDateDaylog(List<DayLog> daylogList) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		String firstDaylogId=generateDateDaylogId(daylogList);

		if(daylogList!=null && daylogList.size()>0){
			DayLog firstDaylog=null;
			for(int i=0;i<daylogList.size();i++){
				if(((DayLog)daylogList.get(i)).getId().equals(firstDaylogId)){
					firstDaylog=(DayLog)daylogList.get(i);
				}
			}
			//构建DateDayLog对象
			DateDayLog dateDayLog=new DateDayLog();
			dateDayLog.setDaylogList(daylogList);
			dateDayLog.setSubTotal(getSubTotal(daylogList));
			dateDayLog.setLogDate(sdf.format(firstDaylog.getLogDate()));
			dateDayLog.setUpdateMan(firstDaylog.getAuditMan());
			dateDayLog.setUpdateTime(sdf2.format(firstDaylog.getUpdateTime()));
			dateDayLog.setUserDept(firstDaylog.getDetName());
			dateDayLog.setUserGroup(firstDaylog.getGroupName());
			dateDayLog.setUserName(firstDaylog.getUserName());
			dateDayLog.setAuditMan(firstDaylog.getAuditMan());
			dateDayLog.setAuditTime(firstDaylog.getAuditTime()==null?"":sdf2.format(firstDaylog.getAuditTime()));
			dateDayLog.setAuditStatus(firstDaylog.getAuditStatus());
			dateDayLog.setUpdateMan(firstDaylog.getUpdateman());
			dateDayLog.setUserId(firstDaylog.getUserId());
			Date confirmDate = firstDaylog.getConfirmTime();
			dateDayLog.setConfirmMan(firstDaylog.getConfirmMan());
			if(confirmDate!=null){				
				dateDayLog.setConfirmTime(sdf2.format(confirmDate));
			}
			Double totalConfirmHour = 0.0;
			//将所有DayLog中的确认工时加起来
			for(DayLog d: daylogList){
				totalConfirmHour += d.getConfirmHour()==null?0:d.getConfirmHour();
			}
			dateDayLog.setConfirmHour(totalConfirmHour);
			
			//如果有一项任务被确认，那么该用户当天日志的确认状态为“已确认”
			String confirmStatus = "";
			Map statusMap = BusnDataDir.getMap("projectManage.confirmStatus");
			for(DayLog d: daylogList){
				if("1".equals(d.getConfirmStatus())){
					//确认状态数据字典转换
					confirmStatus = BusnDataDir.getValue(statusMap, d.getConfirmStatus());
					break;
				}else{
					confirmStatus = BusnDataDir.getValue(statusMap, d.getConfirmStatus());
				}
			}
			dateDayLog.setConfirmStatus(confirmStatus);
			
			dateDayLog.setConfirmDesc(firstDaylog.getConfirmDesc());
			return dateDayLog;
		}
		return null;
	}

	//获取任务总时间
	private double getSubTotal(List daylogList) {
		double count=0;
		for(int i=0;i<daylogList.size();i++){
			count+=((DayLog)daylogList.get(i)).getSubTotal();
		}
		return count;
	}

	private static int computedPageCount(int recordCount, int pageSize) {
        int div = recordCount / pageSize;
        int mod = recordCount % pageSize;

        //如果剩下的记录数不够页面尺寸，页将它作为一页对待
        int pageCount = (mod == 0) ? div : div + 1;
        return pageCount;
    }
	
	/**截取list中的部分数据，组成page列表
	 */
	private List getPageData(List resultList, int pageNum, int pageSize) {
		int startIndex=(pageNum-1)*pageSize;
		int endIndex=pageNum*pageSize-1;
		if(endIndex+1>resultList.size()){
			endIndex=resultList.size()-1;
		}
		List list=new ArrayList();
		for(int i=0;i<resultList.size();i++){
			if(i>=startIndex && i<=endIndex){
				list.add(resultList.get(i));
			}
		}
		return list;
	}

	/** 批量保存
	 */
	public void saveDayLogList(List<DayLog> daylogList) {
		this.getHibernateTemplate().saveOrUpdateAll(daylogList);
	}

	/** 获取指定用户的指定日期的所有日志
	 * @param userId
	 * @param logDate
	 * @return
	 */
	public List<DayLog> selectReports(String userId, Date logDate) {
		String query="from DayLog log where log.userId=? and log.logDate=?";
		List<DayLog> logs=(List<DayLog>)this.getHibernateTemplate().find(query, new Object[]{userId,logDate});
		return logs;
	}

	/** 删除指定用户和日期的所有日志，并插入新的日志
	 * @param userid 用户id
	 * @param submitOldDate 日期
	 * @param daylogList 新的日志
	 * @return
	 */
	public boolean updateAll(String userid, Date submitOldDate,List<DayLog> daylogList) {
		try{
			//获取旧的dayloglist，并从中获取DateDayLogId
			List<DayLog> oldDayLogList=selectReports(userid, submitOldDate);
			String oldId=generateDateDaylogId(oldDayLogList);
			
			//删除原来的数据，保存新数据
			remove(userid,submitOldDate);
			//删除原来积分数据
			integralCenterDao.deleteIntegralInfoByDayLogParam(userid,submitOldDate);
			
			saveDayLogList(daylogList);
			for(int i=0;i<oldDayLogList.size();i++){
				DayLog log=oldDayLogList.get(i);
				SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call weekplantaskupdate_proc(?,?)}");
				sqlQuery.setString(0, userid);
				sqlQuery.setString(1, log.getPlan_taskid());
				sqlQuery.executeUpdate();
			}
			
			//获取新的dayloglist，并从中获取DateDayLogId
			List<DayLog> newDayLogList=selectReports(userid, daylogList.get(0).getLogDate());
			String newId=generateDateDaylogId(newDayLogList);
			
			//更新评论表
			String sql="update OA_DAYLOG_VOTE set C_DAYLOGID='"+newId+"' where C_DAYLOGID='"+oldId+"'";
			getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			//更新日志审核表
			sql="update OA_APPROVALRECORD set C_APPROVALNAME='"+newId+"' where C_APPROVALNAME='"+oldId+"'";
			getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**删除指定用户指定日期的日志
	 */
	public boolean remove(String userId, Date logDate) {
		try{
			String queryString="delete from DayLog log where log.userId=? and log.logDate=?";
			this.getHibernateTemplate().bulkUpdate(queryString, new Object[]{userId,logDate});
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/** 获取指定日期的日志评论
	 */
	public List<DayLogVote> getVoteHistoryList(DayLog daylog) {
		String query="from DayLogVote vote where vote.daylogId=? order by vote.voteTime desc";
		List<DayLogVote> votes=(List<DayLogVote>)this.getHibernateTemplate().find(query, new Object[]{daylog.getId()});
		return votes;
	}

	/**
	 * 保存评论
	 */
	public boolean saveDayLogVote(DayLogVote vote) {
		getHibernateTemplate().save(vote);
		return true;
	}

	/**根据日期查询日志
	 * wtjiao 2014年9月30日 下午1:36:14
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<DayLog> queryLogByDate(Date startTime, Date endTime) {
		String hql = "FROM DayLog log WHERE 1=1 ";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(sdf.format(startTime))){
			hql += " and log.logDate >= to_date('"+sdf.format(startTime)+"','yyyy-MM-dd') ";
		}
		if(StringUtils.isNotBlank(sdf.format(endTime))){
			hql += " and log.logDate <= to_date('"+sdf.format(endTime)+"','yyyy-MM-dd') ";
		}
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}
	
	/**
	 * 从日志表中查询当月的数据
	 * @return 当月的日志数据
	 */
	public List<DayLog> queryDayLogStatistic(String month){		
		//如果当前为月首日,则取上一个月的整月来进行人日统计
		//如果不是为首日,则取当前月来统计
		List<String> dateList = DateUtil.getWorkdayList(month);
		String startDate ="";
		String endDate = "";
		if (dateList.size() == 1) {
			startDate = endDate = dateList.get(0);
		}else{
			startDate =  dateList.get(0);
			endDate = dateList.get(dateList.size() - 1);
		}
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.logDate >= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" AND l.logDate <= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" ORDER BY l.prjName, l.userId,l.logDate");
		List<DayLog> logs=(List<DayLog>)this.getHibernateTemplate().find(sbdHql.toString()
				,new Object[]{startDate,endDate});
		return logs;
	}
	
	/**根据时间段和项目id查询日志
	 * @param projectId 项目id
	 * @param starTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<DateDayLog> queryDayLogByProjectAndTimePierod(String projectId,String startTime,String endTime){
		String hql = "FROM DayLog log WHERE 1=1 ";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(projectId)){
			hql+=" and log.prjName='"+projectId+"'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql += " and log.logDate >= to_date('"+startTime+"','yyyy-MM-dd') ";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql += " and log.logDate <= to_date('"+endTime+"','yyyy-MM-dd') ";
		}
		List<DayLog> daylogList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return getDateDayLogList(daylogList);
	}
	
	
	/**
	 * 从日志表中查询当月的数据
	 * @return 当月的日志数据
	 */
	public List<DayLog> queryDayLogOrderBydept(){		
		//如果当前为月首日,则取上一个月的整月来进行人日统计
		//如果不是为首日,则取当前月来统计
		List<String> dateList = DateUtil.getWorkdayList();
		String startDate ="";
		String endDate = "";
		if (dateList.size() == 1) {
			startDate = endDate = dateList.get(0);
		}else{
			startDate =  dateList.get(0);
			endDate = dateList.get(dateList.size() - 1);
		}
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.logDate >= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" AND l.logDate <= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" ORDER BY l.detName, l.userId,l.logDate");
		List<DayLog> logs=(List<DayLog>)this.getHibernateTemplate().find(sbdHql.toString()
				,new Object[]{startDate,endDate});
		return logs;
	}

	/**
	 * 根据用户ID和日志日期查询该用户的当天总工时
	 * @param userId 用户ID
	 * @param date 日志日期
	 * @return 当天总工时
	 */
	public Double queryWorkhoursByUserIdDate(String userId, String date){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append("SELECT SUM(l.confirmHour) FROM DayLog l ");
		sbdHql.append(" WHERE l.userId = ?");
		sbdHql.append(" AND l.logDate = to_date(?,'yyyy-MM-dd')");
		List<Object> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userId,date});
		Double workhours = 0d;
		//由于查询的结果是自定义的字段组成的，没有查询记录时结果为‘null’
		if (result != null && result.size() > 0) {
			if(result.get(0) != null){
				workhours = Double.valueOf((result.get(0).toString()));
			}
		}
		return workhours;
	}
	
	/**
	 * 根据项目ID,用户ID,日志日期查询是否有写日志
	 * 
	 * @param userId
	 *            用户ID
	 * @param projectId
	 *            项目ID
	 * @param date
	 *            日志日期
	 * @return true:有日志 false:无日志
	 */
	public boolean queryDayLogByIdDate(String userId, String projectId, String date){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.userId = ?");
		sbdHql.append(" AND l.prjName = ?");
		sbdHql.append(" AND l.logDate = to_date(?,'yyyy-MM-dd')");
		List<DayLog> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userId, projectId, date});
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户ID,日志日期查询是否有写日志
	 * 
	 * @param userid
	 *            用户ID
	 * @param date
	 *            日志日期
	 * @return true:有日志 false:无日志
	 */
	public boolean queryDayLogByUseridDate(String userid,  String date){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.userId = ?");
		sbdHql.append(" AND l.logDate = to_date(?,'yyyy-MM-dd')");
		List<DayLog> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userid,  date});
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}	
	
	/**
	 * 查询日志管理对应表数据，计算出某人某日的工时总和，按日期排序展现本月日志工时
	 * @param userid 用户ID
	 * @param dateYYYY-MM 日志月份 （比如：2014-01）
	 * @return
	 */
	public List queryWorkHoursByUseridMonth(String userid,  String dateYYYYMM){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append("SELECT SUM(l.subTotal) as subTotal, l.logDate  as logDate FROM DayLog l ");
		sbdHql.append(" WHERE l.userId = ?");
		sbdHql.append(" AND TO_CHAR(l.logDate,'yyyy-MM') = ? ");
		sbdHql.append(" GROUP BY l.logDate ");
		sbdHql.append(" ORDER BY l.logDate ");
		List result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userid,dateYYYYMM});
		return result;
	}
	
	/**
	 * 查询当天的日志填写情况，只返回一条记录即可
	 * @param userid 用户ID
	 * @param logDate 日志日期
	 * @return 
	 */
	public DayLog queryDayLogByUseridLogDate(String userid,  String logDate){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.userId = ?");
		sbdHql.append(" AND l.logDate = to_date(?,'yyyy-MM-dd')");
		List<DayLog> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userid,  logDate});
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 获取批量确认工时数据List(项目经理)未确认
	 * @param userModel
	 * @param prjname
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnterRecord(UserModel userModel,String startDate,String endDate) {
		String hql = "from DayLog where 1=1 and prjName in (";
		hql+="select id from Project where proManagerId='"+userModel.getUserid()+"'";
		hql+= ") and to_char(logDate,'yyyy-mm-dd') between '";
		hql+=startDate+"' and '"+endDate+"' and confirmStatus='0'";
		hql+=" order by userId,logDate";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 获取批量确认工时数据List(项目经理)已确认
	 * @param userModel
	 * @param prjname
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnteredRecord(UserModel userModel,String startDate,String endDate) {
		String hql = "from DayLog where 1=1 and prjName in(";
		hql+="select id from Project where proManagerId='"+userModel.getUserid()+"'";
		hql+= ") and to_char(logDate,'yyyy-mm-dd') between '";
		hql+=startDate+"' and '"+endDate+"' and confirmStatus='1'";
		hql+=" order by userId,logDate";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 获取批量确认工时数据List(部门经理)未确认
	 * @param userModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnterRecordForDept(String deptname,String startDate,String endDate) {
		String hql = "from DayLog where 1=1 and detName='"+deptname+"' and to_char(logDate,'yyyy-mm-dd') between '";
		hql+=startDate+"' and '"+endDate+"' and confirmStatus='0'";
		hql+=" order by userId,logDate,groupName";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 获取批量确认工时数据List(部门经理)已确认
	 * @param userModel
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLog> getMoreWorkEnteredRecordForDept(String deptname,String startDate,String endDate) {
		String hql = "from DayLog where 1=1 and detName='"+deptname+"' and to_char(logDate,'yyyy-mm-dd') between '";
		hql+=startDate+"' and '"+endDate+"' and confirmStatus='1'";
		hql+=" order by userId,logDate,groupName";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 获取当天项目总工时
	 * @param userid
	 * @param logdate
	 * @return
	 */
	public Double getAllProjectHour(String userid,String logdate){
		String hql="select nvl(sum(d_subtotal),0) from oa_daylog";
		hql+=" where c_userid='"+userid+"' and to_char(d_logdate,'yyyy-mm-dd')='"+logdate+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).toString());
		}
		return 0.0;
	}
	
	/**
	 * 获取项目的总确认工时
	 * @param userid
	 * @param logdate
	 * @return
	 */
	public Double getConfirmHourByPrjname(String userid,String logdate,String prjname){
		String hql="select nvl(sum(ext7),0) from oa_daylog";
		hql+=" where c_userid='"+userid+"' and to_char(d_logdate,'yyyy-mm-dd')='"+logdate+"' and c_groupName='"+prjname+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).toString());
		}
		return 0.0;
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
		String hql = "from DayLog where userId='"+userid+"' and to_char(logDate,'yyyy-mm-dd')='"+logdate+"' and groupName='"+prjname+"' and confirmStatus='"+confrimStatus+"'";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 根据条件获取总任务工时
	 * @param userid
	 * @param logdate
	 * @param prjname
	 * @return
	 */
	public Double getProjectHourByCondition(String userid,String logdate,String prjname,String confrimStatus){
		String hql="select nvl(sum(d_subtotal),0) from oa_daylog";
		hql+=" where c_userid='"+userid+"' and to_char(d_logdate,'yyyy-mm-dd')='"+logdate+"' and c_groupName='"+prjname+"' and ext2='"+confrimStatus+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).toString());
		}
		return 0.0;
	}
	
	/**
	 * 根据项目统计项目的月总工时（项目经理）
	 * @param startdate 2014-03-02 12
	 * @return
	 */
	public Double getMonthAllConfirmHour(String startdate,String prjid){
		//截取月份，时间大于当前查询的月份的第一天
		/*String hql="select nvl(sum(c_enterday),0) from oa_daylog";
		hql+=" where to_char(d_logdate,'yyyy-mm')>='"+startdate.substring(0,7)+"' and C_PRJ_ID='"+prjid+"' and ext2='1'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).toString());
		}*/
		//修改为从项目人日模块中获取，保持项目人日消耗模块和此处的统计逻辑一致 20150320 wtjiao
		String hql="from PersonDay where projectId='"+prjid+"' and year='"+startdate.substring(0,4)+"' and month='"+startdate.substring(5,7)+"'";
		List<PersonDay> list =getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).getPersonDayConfirm());
		}
		return 0.0;
	}
	
	/**
	 * 统计月总工时（部门经理）
	 * @param startdate
	 * @return
	 */
	public Double getMonthAllConfirmHourByDept(String startdate,String deptname){
		/*String hql="select nvl(sum(c_enterday),0) from oa_daylog";
		hql+=" where to_char(d_logdate,'yyyy-mm')>='"+startdate.substring(0,7)+"' and c_detname='"+deptname+"' and ext2='1'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).toString());
		}*/
		//修改为从部门人日模块中获取，保持部门人日统计与此处的逻辑一致
		String hql="from DeptGeneralCost where deptName='"+deptname+"' and to_char(statisticDate,'yyyy-mm')>='"+startdate.substring(0,7)+"'";
		List<DeptGeneralCost> list =getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return Double.parseDouble(list.get(0).getDeptManagerConfirm()+"");
		}
		return 0.0;
	}
	
	/**
	 * 获取昨天未确认的项目及项目经理信息，除“其他项目”外
	 * @param yesterday
	 * @return
	 */
	public List<Project> getNoConfirmPrjname(String yesterday){
		String enddate = DateUtil.getYesterdayByCurrentDate();
		String hql = "from Project t ";
		hql+=" where t.name in (select distinct groupName from DayLog t where to_char(logDate,'yyyy-mm-dd')>='"+yesterday+"'";
		hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+enddate+"' and confirmStatus='0')";
		hql+=" and name!='其他项目'";
		List<Project> list = new ArrayList<Project>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 获取昨天未确认的部门经理信息
	 * @param yesterday
	 * @return
	 */
	public List<SysUser> getNoConfirmDept(String yesterday){
		String enddate = DateUtil.getYesterdayByCurrentDate();
		String hql = "from SysUser where userid in (select userid from UsrUsrgrp where grpcode='deptManager')";
		hql+=" and deptName in (select key from SysDatadir where parentid=(select id from SysDatadir where key='department')";
		hql+=" and value in (select distinct detName from DayLog where to_char(logDate,'yyyy-mm-dd')>='"+yesterday+"'";
		hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+enddate+"' and confirmStatus='0'))";
		List<SysUser> list = new ArrayList<SysUser>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 根据时间段获取未确认的日志数据
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public List<DayLog> getNoConfirmDayLogForTool(String startdate,String enddate){
		String hql = "from DayLog where 1=1 and (projectDay is null or enterDay is null or confirmStatus='0')";
		if(!"".equals(startdate) && "".equals(enddate)){
			hql+=" and to_char(logDate,'yyyy-mm-dd')>='"+startdate+"'";
		}
		if(!"".equals(enddate) && "".equals(startdate)){
			hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+enddate+"'";
		}
		if(!"".equals(startdate) && !"".equals(enddate)){
			hql+=" and to_char(logDate,'yyyy-mm-dd')>='"+startdate+"'";
			hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+enddate+"'";
		}
		hql+=" order by userId,logDate";
		List<DayLog> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 获取前一天的工作日
	 * @param currentDate
	 * @return
	 */
	public String getYesterdayWorkDate(String currentDate){
		String hql="select to_char(to_date(check_date,'yyyy-mm-dd'),'yyyy-mm-dd') from oa_holiday where to_char(to_date(check_date,'yyyy-mm-dd'),'yyyy-mm-dd')<'"+currentDate+"' and type='0' order by check_date desc";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}
	
	/**
	 * 根据项目名称，得出日志确认角色
	 * @param prjname
	 * @return
	 */
	public String getProjectManagerByPrjname(String prjname){
		try{
			if("其他项目".equals(prjname)){
				return "deptManager";
			}else{
				String hql="select t.c_managerkey from oa_project t where t.c_name='"+prjname+"'";
				List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
				if(list!=null && list.size()>0){
					String manager = list.get(0).toString();
					if("".equals(manager)){
						return "deptManager";
					}else{
						return "groupManager";
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "deptManager";
		}
		return "deptManager";
	}
	
	/**
	 * 根据条件获取未确认日志的所有日期
	 * @param yesterday
	 * @return
	 */
	public String getNoConfirmDayLogDate(String yesterday,String prjname){
		String enddate = DateUtil.getYesterdayByCurrentDate();
		String hql="select wm_concat(d_logdate) from (";    
		hql+=" select distinct to_char(d_logdate,'yyyy-mm-dd')d_logdate from oa_daylog where to_char(d_logDate,'yyyy-mm-dd')>='"+yesterday+"'";
		hql+="and to_char(d_logDate,'yyyy-mm-dd')<='"+enddate+"' and c_groupname='"+prjname+"' and ext2='0')";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}
	
	/**
	 * 根据条件条件获取未确认日志的日期
	 * @param yesterday
	 * @param deptname
	 * @return
	 */
	public String getNoConfirmDayLogDateForDept(String yesterday,String deptname){
		String enddate = DateUtil.getYesterdayByCurrentDate();
		String hql="select wm_concat(d_logdate) from (";    
		hql+=" select distinct to_char(d_logdate,'yyyy-mm-dd')d_logdate from oa_daylog where to_char(d_logDate,'yyyy-mm-dd')>='"+yesterday+"'";
		hql+="and to_char(d_logDate,'yyyy-mm-dd')<='"+enddate+"' and c_detname='"+deptname+"' and ext2='0')";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}

	/**
	 author:ljlian2
	 *  2015-03-30
	 * 获取用户当天的日志
	 * @param userId  用户id
	 * @param today
	 */
	public boolean getTodayDayLog(String userId, String today) {
		String hql=" from DayLog where  userId='"+userId+"' and logDate=to_date('"+today+"','yyyy-MM-dd')";
		List list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 按部门获取前一天的日志内容
	 * @param deptName  部门名称   如开发二部
	 * @param yesterday  昨天
	 * @return
	 * @author ljlian
	 */
	public List<DayLog> getAllDaylogListByDept(String deptName, String yesterday) {
		String hql = " from DayLog  d where  d.detName ='"+deptName+"' and logDate=to_date('"+yesterday+"','yyyy-MM-dd') order by d.userName";
		List list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}

    /**
     * 用createSQLQuery实现native sql的分页查询； 
     * 然而 不足的是，这里执行了两次查询，影响了效率。当数据量达到一万条时。。。
     * getPage:描述 <br/>
     * 2016年3月29日 下午2:25:47
     * @param queryString
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    public Page getPage(String queryString, int pageNum,
            int pageSize ,Session session) {
         
        Query query = session.createSQLQuery(queryString);
        Page page = new Page();
        pageNum = (pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize < 1) ? 15 : pageSize;
        
        
        
        int recordCount = query.list().size();
         
        
        //设置当前页和记录总数
        page.setCurrentPageNo(pageNum);
        page.setRecordCount(recordCount);

        //设置总页数和页面尺寸
        page.setPageCount(computedPageCount(recordCount, pageSize));
        page.setPageSize(pageSize);
        if (recordCount > 0) {
            page.setQueryResult(find(queryString, pageNum, pageSize,session));
        }
        return page;
    }
    
 
    /**
     * 获得某一页的数据集
     * find:描述 <br/>
     * 2016年3月29日 下午2:27:40
     * @param queryString
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    private List find( String queryString, int pageNum, int pageSize,Session session)  {
        Query queryObject = session.createSQLQuery(queryString);
        queryObject.setMaxResults(pageSize);
        queryObject.setFirstResult((pageNum - 1) * pageSize);

        return queryObject.list();
    }
    
    
    /**
     * 根据参数获得查询条件字符串，默认开始时间为2016-1-1
     * queryConditions:描述 <br/>
     * 2016年3月25日
     * @return
     * @author zzwen6
     * @param deptName 
     * @param endDate 
     * @param startDate 
     * @修改记录: <br/>
     */
    private String queryConditions(String startDate, String endDate, DateDayLog dayLog) {
        // 
        String queryConditions = "";	
        
        //System.out.println(dayLog.getUserDept()+dayLog.getUserGroup()+dayLog.getUserName());
		if (null != dayLog) {

			if (StringUtils.isNotBlank(dayLog.getUserDept())) {
				queryConditions += " and d.c_detname = '"
						+ dayLog.getUserDept() + "' ";
			}
			if (StringUtils.isNotBlank(dayLog.getUserGroup())) {
				queryConditions += " and d.c_groupname = '"
						+ dayLog.getUserGroup() + "' ";
			}
			if (StringUtils.isNotBlank(dayLog.getUserName())) {
				queryConditions += " and ( d.c_username like '%"
						+ dayLog.getUserName() + "%' or u.c_userid like '%"+dayLog.getUserName()+"%' )";
			}
		}
		if (StringUtils.isNotBlank(startDate)) {// d_logdate:填写日志日期
			queryConditions += " and d.d_logdate >= to_date('" + startDate
					+ "', 'yyyy-MM-dd')   ";
//			queryConditions += " and d.ext5 >= to_date('" + startDate
//					+ "', 'yyyy-MM-dd')   ";
		} else {
			queryConditions += " and d.d_logdate >= to_date('2016-1-1', 'yyyy-MM-dd')   ";
//			queryConditions += " and d.ext5 >= to_date('2016-1-1', 'yyyy-MM-dd')   ";
		}

		if (StringUtils.isNotBlank(endDate)) {// ext5：确认日期
			queryConditions += " and d.d_logdate <= to_date('" + endDate
					+ "', 'yyyy-MM-dd')   ";
		}else {
            queryConditions += " and d.d_logdate <= to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-mm-dd')  ";
        }
        
        return queryConditions;
    }

    /**
     * findPrjCensusInfosByPages:分页查询项目统计信息
     * 由BaseHibernateTemplate 模板中的getPage方法获得结果集，getPage中其实用了Query这个接口
     * 此接口可以接收原生的sql进行查询，所以这里不用关心查询语句是否面向对象 
     *  <br/>
     * 2016年3月25日 下午4:29:06
     * @param startDate 时间范围
     * @param endDate
     * @param deptName 部门
     * @param pageNum 页码
     * @param pageSize 大小
     * @return page工具
     * @author zzwen6
     * @修改记录: 重写自有的getPage方法
     */
    public Page findPrjCensusInfosByPages(String startDate, String endDate, DateDayLog dayLog, int pageNum,int pageSize) {
       // 
        
       String queryCondition = queryConditions( startDate, endDate, dayLog);
       // Natvie SQL语句
       StringBuilder hql =               new StringBuilder(" select r.c_userid,  r.c_username, r.c_detname,r.c_groupname,r.prjDate, ");
                                                                  hql.append(" r.totaldate, r.c_status,to_number( r.c_leave ) * r.prjdate as leave_date ");
                                                hql.append(" from ( select d.c_userid, ");
                                                            hql.append(" d.c_username, ");
                                                            hql.append(" d.c_detname, ");
                                                            hql.append(" d.c_groupname, ");
                                                            hql.append(" sum( LEAST(d.ext7 / 8,1) ) as prjDate, ");
                                                            hql.append(" sum( d.d_subtotal ) / 8 as totalDate, ");
                                                            hql.append(" u.c_Status, ");
                                                            hql.append(" d.c_leave ");
                                                            hql.append(" from oa_daylog d, sys_user u ");
                                               
                                                hql.append(" where 1 = 1 ");
                                                          /*and u.c_userid = 'lqxiang'*/
                                                hql.append(" and u.c_userid = d.c_userid ");
                                                // 添加查询条件、参数
                                                hql.append(queryCondition);
                                                
                                                hql.append(" group by d.c_userid, ");
                                                        hql.append(" d.c_username, ");
                                                        hql.append(" d.c_detname, ");
                                                        hql.append(" d.c_groupname, ");
                                                        hql.append(" u.c_Status, ");
                                                        hql.append(" c_leave order by d.c_userid) r ");
                                                        
                                                   
       Session seesion = this.getSession(true);
       return getPage(hql.toString(), pageNum, pageSize,seesion);
    }

    /**
     * 查找资源池的人：请假的，其他项目的。
     * 仅查找当前日期及往前七天的
     * finLeaveAndOtherPrjPersons:描述 <br/>
     * 2016年3月31日 上午10:03:41
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    public List<Object[]> findLeaveAndOtherPrjPersons() {
         
        String hql = "select distinct d.c_userid, d.c_username, d.c_detname  from oa_daylog d where 1 = 1   "
                + " and (d.c_leave = '1' or d.c_groupname = '其他项目')   and d.d_logdate > sysdate - interval '7' day   and d.d_logdate < sysdate";
        
        Query query = getSession().createSQLQuery(hql);
        
        return query.list();
    }
	
    
    
    
    /**
     * 查找所有符合条件的项目信息：用于导出
     * findAllPrjCensusInfos:描述 <br/>
     * 2016年4月13日 下午1:49:18
     * @param startDate
     * @param endDate
     * @param dateDayLog
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    public List<Object[]> findAllPrjCensusInfos(String startDate,String endDate,DateDayLog dateDayLog){
	  
	 String queryCondition = queryConditions( startDate, endDate, dateDayLog);
	  StringBuilder nativeSql =  new StringBuilder(" select r.c_userid,  r.c_username, r.c_detname,r.c_groupname,r.prjDate, ");
	  nativeSql.append(" r.totaldate, r.c_status,to_number( r.c_leave ) * r.prjdate as leave_date ");
	                     nativeSql.append(" from ( select d.c_userid, ");
	                     nativeSql.append(" d.c_username, ");
	                     nativeSql.append(" d.c_detname, ");
	                     nativeSql.append(" d.c_groupname, ");
	                     nativeSql.append(" sum( LEAST(d.ext7 / 8,1) ) as prjDate, ");
	                     nativeSql.append(" sum( d.d_subtotal ) / 8 as totalDate, ");
	                     nativeSql.append(" u.c_Status, ");
	                     nativeSql.append(" d.c_leave ");
	                     nativeSql.append(" from oa_daylog d, sys_user u ");
						
	                     nativeSql.append(" where 1 = 1 ");
						/*and u.c_userid = 'lqxiang'*/
	                     nativeSql.append(" and u.c_userid = d.c_userid ");
						// 添加查询条件、参数
	                     nativeSql.append(queryCondition);
						
	                     nativeSql.append(" group by d.c_userid, ");
	                     nativeSql.append(" d.c_username, ");
	                     nativeSql.append(" d.c_detname, ");
	                     nativeSql.append(" d.c_groupname, ");
	                     nativeSql.append(" u.c_Status, ");
	                     nativeSql.append(" c_leave  order by d.c_userid  ) r ");
						
     Session seesion = this.getSession(true);
	 Query query = seesion.createSQLQuery(nativeSql.toString());
//							seesion.createSQLQuery(arg0)
	  return query.list();
   }
	
	
}
