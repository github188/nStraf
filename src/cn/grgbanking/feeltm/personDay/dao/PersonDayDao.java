package cn.grgbanking.feeltm.personDay.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.personDay.domain.DeptMonthPersonDay;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("personDayDao")
public class PersonDayDao extends BaseDao<PersonDay> {
	
	/**
	 * 查询Personday对象
	 * @param projectId
	 * @param year
	 * @param month
	 * @return
	 */
	public List queryPersonDayByclick(String projectId, String year, String month){
		try{
			String queryString=" from PersonDay p where p.projectId = ? and p.year = ? and p.month=?";
			List list =this.getHibernateTemplate().find(queryString, new Object[]{projectId,year,month});
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询Personday对象
	 * @param id
	 * @return
	 */
	public List queryPersonDayById(String id){
		try{
			String queryString=" from PersonDay p where p.id = ?";
			List list =this.getHibernateTemplate().find(queryString, new Object[]{id});
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 修改Personday对象
	 * @return
	 */
	public boolean updatePersonDay(PersonDay personDay){
		boolean flag=false;
		try{
			updateObject(personDay);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 获取每一年的projectid
	 * @return
	 */
	public List getProjectId(String year){
		try{
		String queryString="select distinct p.projectId from PersonDay p where p.year = ?";
		List list =this.getHibernateTemplate().find(queryString, new Object[]{year});
		return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取当年的每个项目的12个月的数据
	 * @return
	 */
	public List getProjectForYear(String year,String projectId){
		String hql = "from PersonDay t where t.year= ?  and t.projectId= ? order by t.month";
		List result = this.getHibernateTemplate().find(hql.toString(),
				new Object[]{year,projectId});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	/**
	 * 查询是否存在当月的该项目的记录
	 * @param projectId 项目ID
	 * @param year 年份
	 * @param month 月份
	 * @return 人日实体
	 */
	public PersonDay existPersonDay(String projectId, String year, String month){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM PersonDay p ");
		sbdHql.append(" WHERE p.projectId = ? AND p.year = ? AND p.month = ? ");
		List<PersonDay> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{projectId, year, month});
		if (result != null && result.size() > 0) {
			return (PersonDay)result.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * 保存或者更新人日
	 * @param personDay 人日实体
	 */
	public void saveOrUpdateData(PersonDay personDay){
		this.getHibernateTemplate().saveOrUpdate(personDay);
	}
	
	/**
	 * 保存或者更新人日
	 * @param personDay 人日实体
	 */
	public void saveOrUpdateDeptMonthPersonDay(DeptMonthPersonDay deptMonthPDay){
		this.getHibernateTemplate().saveOrUpdate(deptMonthPDay);
	}
	
	/**
	 * 根据年，月从人日表中查询项目数据列表
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 项目数据LIST
	 */
	public List<PersonDay> queryProjectListByYearMonth(String year, String month){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM PersonDay p ");
		sbdHql.append(" WHERE p.year = ? AND p.month = ? ");
		List<PersonDay> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{year, month});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	
	/**根据部门名称，以及时间段获取用户人日
	 * @param deptName
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	public List<PersonDay> queryProjectListByMonthBucket(String projectName,String startMonth,String endMonth){
		List<String> monthList=DateUtil.getMonthListByBucket(startMonth,endMonth);
		String monthListStr="";
		for(String month:monthList){
			monthListStr+=",'"+month+"'";
		}
		if(StringUtils.isNotBlank(monthListStr)){
			monthListStr=monthListStr.substring(1);
		}
		if(projectName==null){
			projectName="";
		}
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM PersonDay p ");
		sbdHql.append(" WHERE p.projectName like '%"+projectName+"%' AND p.ext1 in ("+monthListStr+") order by nlssort(p.projectName,'NLS_SORT=SCHINESE_PINYIN_M'),ext1 asc ");
		List<PersonDay> result = this.getHibernateTemplate().find(sbdHql.toString());
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据项目类型获取该月人日统计
	 * @param year
	 * @param month
	 * @param projectType
	 * @return
	 */
	public BigDecimal getPersonDayByMoudle(String year ,String month,String projectType){
		//select sum(t.c_personday_confirm) from oa_personday t where t.c_year = '2014' and t.c_month = '10' and t.c_is_edit ='false' and t.c_projectid in (select p.c_id from oa_project p where p.c_isend='0' and p.ext1='0' and p.c_project_type = 'fieldProject' )
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append("select sum(t.personDayConfirm) from PersonDay t where 1=1 and t.year = ? and t.month = ? ");
		sbdHql.append("and t.projectId in (select p.id from Project p where p.isEnd='0' and p.isVisual='0' and p.projectType = ?)");
		List result = this.getHibernateTemplate().find(sbdHql.toString(),new Object[]{year,month,projectType});
		if (result.size()>0) {
			Object obj = result.get(0);
			if (obj!=null) {
				return new BigDecimal(obj.toString());
			}
			return new BigDecimal(0);
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 查询是否存在当月的该项目的记录
	 * @param deptName 项目ID
	 * @param year 年份
	 * @param month 月份
	 * @return deptMonthPerdonDay
	 */
	public DeptMonthPersonDay existDeptMonthPersonDay(String deptName, String year, String month){
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DeptMonthPersonDay p ");
		sbdHql.append(" WHERE p.deptName = ? AND p.year = ? AND p.month = ? ");
		List<DeptMonthPersonDay> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{deptName, year, month});
		if (result != null && result.size() > 0) {
			return (DeptMonthPersonDay)result.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 获取部门人日统计信息表中的信息
	 * 20150326 wtjiao 修改原来的逻辑，为了和成本控制模块保持一致，该模块修改为展现经过部门经理确认的，算在部门头上的人日消耗。而不再展示该部门下的人员项目消耗
	 * @param year 年份
	 * @param month 月份
	 * @return deptMonthPerdonDay
	 */
	public List<DeptMonthPersonDay> queryDeptMonthPersonDay(String year, String month){
		try{
			Date date=new SimpleDateFormat("yyyy-MM").parse(year+"-"+month);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			Date firstDay=DateUtil.getStartEndDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),0);
			Date lastDay=DateUtil.getStartEndDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1);
			
			StringBuilder sbdHql = new StringBuilder("");
			sbdHql.append(" select new DeptMonthPersonDay(cost.deptName,sum(cost.deptManagerConfirm)||'') from DeptGeneralCost cost ");
			sbdHql.append(" WHERE  cost.statisticDate>=? and cost.statisticDate<=? group by cost.deptName");
			List<DeptMonthPersonDay> result = this.getHibernateTemplate().find(sbdHql.toString(),
					new Object[]{ firstDay, lastDay});
			if (result != null && result.size() > 0) {
				return result;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**删除项目时，同时删除对应的人日数据
	 * @param projectId
	 */
	public void removePersonDayByProjectId(String projectId) {
		String hql="delete from PersonDay where projectId='"+projectId+"'";
		getHibernateTemplate().bulkUpdate(hql);
	}
	
}
