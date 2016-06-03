package cn.grgbanking.feeltm.integralCenter.dao;
/**
 * @author whxing
 * 积分中心DAO
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.integralCenter.domain.IntegralInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("integralCenterDao")
public class IntegralCenterDao extends BaseDao<IntegralInfo> {
	
	private String monthBegin; //一个月的开始
	private String monthEnd;//一个月的结束
	
	/**
	 * 保存积分记录
	 * @param integralInfo
	 * @return
	 */
	public boolean saveByIntegralInfo(IntegralInfo integralInfo) {
		getHibernateTemplate().save(integralInfo);
		return true;
	}
	
	/**
	 * 删除日志积分记录
	 * @param userId
	 * @param logDate
	 * @return
	 */
	public boolean deleteIntegralInfoByDayLogParam(String userId, Date logDate) {
		try{
			String queryString="delete from IntegralInfo intg where intg.userId=? and intg.logDate=? and intg.gategoryId = 'rizhi' ";
			this.getHibernateTemplate().bulkUpdate(queryString, new Object[]{userId,logDate});
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 删除周报积分记录
	 * @param userId
	 * @param logDate
	 * @return
	 */
	public boolean deleteIntegralInfoByWeekLogLogParam(String userId, Date logDate) {
		try{
			String queryString="delete from IntegralInfo intg where intg.userId=? and intg.logDate=? and intg.gategoryId = 'zhoubao'";
			this.getHibernateTemplate().bulkUpdate(queryString, new Object[]{userId,logDate});
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 *删除爱心小鱼积分
	 * @param userId
	 * @return
	 */
	public boolean deleteIntegralInfoByInstanceId(String id) {
		try{
			String queryString="delete from IntegralInfo intg where intg.ext4=? ";
			this.getHibernateTemplate().bulkUpdate(queryString, new Object[]{id});
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 计算每天点赞上限
	 * @param userId
	 * @param createTime
	 * @return
	 */
	
	public boolean queryPraiseLimit(String userId, Date createTime) {
		try{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			String creaetimeStr1 = sdf1.format(createTime)+" 00:00:00";
			String creaetimeStr2 = sdf1.format(createTime)+" 23:59:59";
			String queryString=" from IntegralInfo intg where intg.userId='"+userId+"'"+"and intg.note = '1' and  intg.createTime >= " +
					"to_date('"+creaetimeStr1+"','yyyy-mm-dd hh24:mi:ss')"+
					"and intg.createTime <=  to_date('"+creaetimeStr2+"','yyyy-mm-dd hh24:mi:ss')";
			List list =this.getHibernateTemplate().find(queryString);
			if(list.size()<3)
			return true;
			else {
			return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据全公司员工查询
	 * @return
	 * @throws ParseException 
	 */
	public List orderByStaff(){
		//更新日志审核表
		getDateforMonth();//获取当月首日和结束日期
		String sql="select  sum(intg.n_integral) as integralSum ,intg.C_DETNAME as detName,intg.C_USERNAME as userName,intg.C_USERID as userId  FROM OA_INTEGRAL intg WHERE 1=1" 
//				+" and intg.D_CREATE_TIME >= to_date('"+monthBegin+"','yyyy-mm-dd hh24:mi:ss')"
//				+" and intg.D_CREATE_TIME <= to_date('"+monthEnd+"','yyyy-mm-dd hh24:mi:ss')"
				+" and intg.D_LOGDATE >= to_date('"+monthBegin+"','yyyy-mm-dd')"//D_LOGDATE包括了爱心小鱼，周报和日志
				+" and intg.D_LOGDATE <= to_date('"+monthEnd+"','yyyy-mm-dd')"
				+" group by intg.C_USERID,intg.C_DETNAME,intg.C_USERNAME, intg.C_USERID order by integralSum desc ";
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	}
	/**
	 * 根据部门查询
	 * @param deptId
	 * @return
	 */
	public List orderByDept(String deptId){
		StringBuffer sql = new StringBuffer();
		getDateforMonth();//获取当月首日和结束日期
		sql.append("select  sum(intg.n_integral) as integral , intg.C_DETNAME , intg.C_USERNAME,intg.C_USERID  FROM OA_INTEGRAL intg WHERE 1=1 "); 
		sql.append("and intg.C_DETID = '"+ deptId+"'");
		sql.append(" and intg.D_LOGDATE >= to_date('"+monthBegin+"','yyyy-mm-dd')");//D_LOGDATE包括了爱心小鱼，周报和日志
		sql.append(" and intg.D_LOGDATE <= to_date('"+monthEnd+"','yyyy-mm-dd')");
		sql.append(" group by intg.C_USERID,intg.C_DETNAME,intg.C_USERNAME,intg.C_USERID  order by integral desc ");
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	}
	
	/**
	 * 根据项目查询
	 * @param userId
	 * @return
	 */
	public List orderByGroup(String userId){
		getDateforMonth();//获取当月首日和结束日期
		String sql = "select  sum(intg.n_integral) as integralSum , intg.C_DETNAME , intg.C_USERNAME,intg.C_USERID   FROM OA_INTEGRAL intg WHERE 1=1"
				+" and intg.D_LOGDATE >= to_date('"+monthBegin+"','yyyy-mm-dd')"//D_LOGDATE包括了爱心小鱼，周报和日志
				+" and intg.D_LOGDATE <= to_date('"+monthEnd+"','yyyy-mm-dd')"
				+" and intg.c_userid in(select DISTINCT p.c_userkey from oa_project_resource p where p.c_projectid in (select u.c_projectid from oa_project_resource u where u.c_userkey='"+userId+"'))"+
				" group by intg.C_USERID,intg.C_DETNAME,intg.C_USERNAME,intg.C_USERID  order by integralSum desc";
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	}

	/**
	 * 个人总积分
	 * @param userId
	 * @return
	 */
	public List getPersonIntegral(String userId){
		getDateforMonth();//获取当月首日和结束日期
		String hqlString = "select sum(intg.integral) from IntegralInfo intg where intg.userId = ?"
				+" and intg.logDate >= to_date('"+monthBegin+"','yyyy-mm-dd')"//D_LOGDATE包括了爱心小鱼，周报和日志
				+" and intg.logDate <= to_date('"+monthEnd+"','yyyy-mm-dd')";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hqlString,userId);
	}
	//查询积分详情
	public Page getPage(String createDate,String gategoryId,String userId,
			int pageNum, int pageSize,String endDate) {
		getDateforMonth();
		String hql ="";
		hql = " from IntegralInfo  where 1=1 ";
		if (createDate != null && !createDate.equals("")) {
			createDate = createDate+" 00:00:00";
			hql += " and createTime >= to_date('" + createDate.trim()
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		else{
			monthBegin = monthBegin+" 00:00:00";
			hql += " and createTime >= to_date('" + monthBegin.trim()
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (endDate != null && !endDate.equals("")) {
			endDate = endDate+" 23:59:59";
			hql += " and createTime <= to_date('" + endDate.trim()
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		else{
			monthEnd = monthEnd+" 23:59:59";
			hql += " and createTime <= to_date('" + monthEnd.trim()
					+ "','yyyy-mm-dd hh24:mi:ss') ";
		}
		if (gategoryId != null && !gategoryId.equals("")) {
			hql += " and gategoryId = '"+gategoryId.trim()+"'";
		}
		
		if(StringUtils.isNotBlank(userId))
		{
			hql += "and userId = '"+userId.trim()+"'";
		}
		hql += " order by createTime desc";
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
    public void getDateforMonth(){
    	Calendar calendar = Calendar.getInstance();   
		// 设置日期为本月最大日期   
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		monthEnd = format.format(calendar.getTime());  
		monthBegin = monthEnd.substring(0, 8);
//		monthBegin +="01 00:00:00";
		monthBegin +="01";
    }
    
   //当修改员工信息部门时，对应员工积分部门修改
    public void updateIntegral(String detid,String detName,String userId){
    	String hql ="";
    	hql +="update IntegralInfo t set t.detNameId = ?,t.detName = ?   where t.userId = ?";
    	this.getHibernateTemplate().bulkUpdate(hql, new Object[]{detid,detName,userId});
    }
    
}
