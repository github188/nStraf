package cn.grgbanking.feeltm.overtime.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.overtime.domain.OvertimeRecord;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("overtimeRecordDao")
public class OvertimeRecordDao extends BaseDao<OvertimeRecord>{
	
	/**
	 * 获取所有请假数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getOvertimeRecord(){
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select c_userid,to_char(d_startdate,'yyyy-mm-dd'),to_char(d_startdate,'hh24:mi:ss'),to_char(d_enddate,'yyyy-mm-dd'),to_char(d_enddate,'hh24:mi:ss'),i_sumtime,c_oarunid from oa_overtime t");
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
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call updateOverRecord_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
}
