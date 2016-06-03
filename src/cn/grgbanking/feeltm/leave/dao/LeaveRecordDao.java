package cn.grgbanking.feeltm.leave.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.leave.domain.LeaveRecord;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("leaveRecordDao")
public class LeaveRecordDao extends BaseDao<LeaveRecord>{
	
	/**
	 * 获取所有请假数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getLeaveRecord(){
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select to_char(t.d_starttime,'yyyy-mm-dd') startdate,to_char(t.d_endtime,'yyyy-mm-dd') enddate,")
			.append("to_char(t.d_starttime,'hh24:mi:ss') starttime,to_char(t.d_endtime,'hh24:mi:ss') endtime,")
			.append("c_userid,c_username,c_deptname,i_sumtime,c_type,c_oarunid")
			.append(" from oa_leave t where c_dealstatus='0'");
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
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call updateleaverecord_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
}
