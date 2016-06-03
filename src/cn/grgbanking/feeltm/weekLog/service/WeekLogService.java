package cn.grgbanking.feeltm.weekLog.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;
import cn.grgbanking.feeltm.weekLog.dao.WeekLogDao;
import cn.grgbanking.feeltm.weekLog.domain.PersonWeekLog;
import cn.grgbanking.framework.util.Page;

@Service("weekLogService")
@Transactional
public class WeekLogService {

	@Autowired
	private WeekLogDao weekLogDao;


	@Transactional(readOnly = true)
	public Page getPage(PersonWeekLog personWeekLog, String queryStartTime, String queryEndTime,int pageNum, int pageSize) {
		return weekLogDao.getPage(personWeekLog,queryStartTime,queryEndTime,pageNum,pageSize);
	}


	/**根据id获取对象
	 * @param id
	 * @return
	 */
	public PersonWeekLog getCaseById(String id) {
		return (PersonWeekLog)weekLogDao.getObject(PersonWeekLog.class, id);
	}



	/** 根据用户查询指定时间段内的日期日志列表
	 * wtjiao 2014年5月13日 下午2:36:45
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DateDayLog> queryDateDayLog(String userid, Date startDate,Date endDate) {
		return weekLogDao.queryDateDayLog(userid,startDate,endDate);
	}


	/**根据用户查询指定时间段内的日志评论列表
	 * wtjiao 2014年5月13日 下午2:54:31
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DayLogVote> queryVote(String userid, Date startDate,Date endDate) {
		try{
			return weekLogDao.queryVote(userid,startDate,endDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}


	/**保存个人周报
	 * wtjiao 2014年5月14日 下午2:07:56
	 * @param personWeekLog
	 * @return 
	 */
	public boolean savePersonWeekLog(PersonWeekLog personWeekLog) {
		return weekLogDao.savePersonWeekLog(personWeekLog);
	}


	/**判断用户是否具有某个起止时间的周报
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean hasPersonalWeekLog(String userid, Date startDate,Date endDate) {
		List list=weekLogDao.queryPersonWeekLog(userid, startDate, endDate);
		if(list==null || list.size()<=0){
			return false;
		}else{
			return true;
		}
	}


	/**查询用户起止时间的周报
	 * @param userid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public PersonWeekLog queryPersonWeekLog(String userid, Date startDate,Date endDate) {
		List list=weekLogDao.queryPersonWeekLog(userid, startDate, endDate);
		if(list==null || list.size()<=0){
			return null;
		}
		return (PersonWeekLog)list.get(0);
	}


	/**删除某个id的周报
	 * @param id
	 * @return
	 */
	public boolean remove(String id) {
		weekLogDao.removeObject(PersonWeekLog.class, id);
		return true;
	}


}
