package cn.grgbanking.feeltm.trip.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.trip.domain.TripRecord;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("tripRecordDao")
public class TripRecordDao extends BaseDao<TripRecord>{
	
	/**
	 * 获取所有请假数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getLeaveRecord(){
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select c_userid,d_startdate,d_enddate,i_sumtime,c_oarunid from oa_trip t where c_dealstatus='0'");
			List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 执行存储过程,将临时表中的数据同步到正式表中
	 * @return
	 */
	public boolean execProcedure(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call updateTripRecord_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
}
