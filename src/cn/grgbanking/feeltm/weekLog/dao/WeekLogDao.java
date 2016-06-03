package cn.grgbanking.feeltm.weekLog.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.weekLog.domain.PersonWeekLog;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("weekLogDao")
public class WeekLogDao extends BaseDao<Object>{
	
//	返回page分页
	public List queryList(String userId,Date startDate,Date endDate)
	{
		String hql = "FROM DayLog log where log.userId=? and log.logDate>=? and log.logDate<=? order by log.logDate desc,log.updateTime desc, log.detName asc ";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql,new Object[]{userId,startDate,endDate});
	}
	
	/**查询用户指定时间段内的日期日志
	 * wtjiao 2014年5月13日 下午2:56:28
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<DateDayLog> queryDateDayLog(String userid, Date startDate,Date endDate) {
		List daylogList=queryList(userid, startDate, endDate);
		List dateDayLogList=getDateDayLogList(daylogList);
        return dateDayLogList;
	}
	/**查询用户指定时间段内的评论
	 * wtjiao 2014年5月13日 下午2:56:52
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLogVote> queryVote(String userid, Date startDate,Date endDate) throws Exception{
		//将2014-10-10类型转为2014-10-10 23:59:59(评论精确到秒，日期精确到日，根据日期查询时，需要将两者精度保持一致)
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr=sdf2.format(startDate);
		String endDateStr=sdf2.format(endDate);
		startDate=sdf.parse(startDateStr+" 00:00:00");
		endDate=sdf.parse(endDateStr+" 23:59:59");
		
		String query="from DayLogVote vote where vote.daylogId in( "
				    +"   from DayLog log where log.userId=? and log.logDate>=? and log.logDate<=? "
				    +") order by vote.voteTime desc";
		List<DayLogVote> votes=(List<DayLogVote>)this.getHibernateTemplate().find(query, new Object[]{userid,startDate,endDate});
		return votes;
	}

	
	/**
	 * 从DayLog中转换为DateDayLog
	 */
	private List getDateDayLogList(List srcList){
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
				//构建DateDayLog对象
				DateDayLog dateDayLog=new DateDayLog();
				dateDayLog.setDaylogList(value);
				dateDayLog.setSubTotal(getSubTotal(value));
				dateDayLog.setLogDate(key.substring(0,key.indexOf(',')));
				dateDayLog.setUpdateTime(sdf.format(((DayLog)value.get(0)).getUpdateTime()));
				dateDayLog.setUserDept(((DayLog)value.get(0)).getDetName());
				dateDayLog.setUserGroup(((DayLog)value.get(0)).getGroupName());
				dateDayLog.setUserName(((DayLog)value.get(0)).getUserName());
				//注：Id为当天第一条日志的id
				dateDayLog.setId(((DayLog)value.get(0)).getId());
				desList.add(dateDayLog);
			}
			
			return desList;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	//获取任务总时间
	private int getSubTotal(List daylogList) {
		int count=0;
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


	/**保存个人周报
	 * wtjiao 2014年5月14日 下午2:12:26
	 * @param personWeekLog
	 * @return 
	 */
	public boolean savePersonWeekLog(PersonWeekLog personWeekLog) {
		try{
			this.getHibernateTemplate().saveOrUpdate(personWeekLog);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getPage(PersonWeekLog personWeekLog, String queryStartTime,String queryEndTime, int pageNum, int pageSize) {
		try{
			String hql="from PersonWeekLog log where 1=1 ";
			if(personWeekLog!=null && StringUtils.isNotBlank(personWeekLog.getDetName())){
				hql+=" and log.detName like '%"+personWeekLog.getDetName()+"%'";
			}
			if(personWeekLog!=null && StringUtils.isNotBlank(personWeekLog.getGroupName())){
				hql+=" and log.groupName like '%"+personWeekLog.getGroupName()+"%'";
			}
			if(personWeekLog!=null && StringUtils.isNotBlank(personWeekLog.getUserName())){
				hql+=" and (log.userName like '%"+personWeekLog.getUserName()+"%' or log.userId like '%"+personWeekLog.getUserName()+"%' )";
			}
			if(StringUtils.isNotBlank(queryStartTime)){
				hql+=" and to_date('"+queryStartTime+"','yyyy-MM-dd')<=log.startDate " ;
			}
			if(StringUtils.isNotBlank(queryEndTime)){
				hql+=" and log.endDate<=to_date('"+queryEndTime+"','yyyy-MM-dd') " ;
			}
		
			hql+=" order by log.startDate desc,log.endDate desc,log.detName asc,log.groupName asc";
			System.out.println("hql==="+hql);
			Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
			return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**查询个人指定时间段内的周报
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List queryPersonWeekLog(String userid, Date startDate,Date endDate) {
		try{
			String hql="from PersonWeekLog log where log.userId=? and log.startDate=? and log.endDate=?";
			List list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{userid,startDate,endDate});
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}
