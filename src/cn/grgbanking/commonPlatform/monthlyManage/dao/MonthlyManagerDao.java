package cn.grgbanking.commonPlatform.monthlyManage.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.commonPlatform.monthlyManage.domain.MonthlyManager;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.framework.dao.BaseDao;
/**
 * 月度管理报告
 * 
 * @author zzhui1
 * 
 */
@Repository
public class MonthlyManagerDao extends BaseDao<MonthlyManager> {
	/**
	 * 得到月度管理报告信息
	 * @param year 年度
	 * @param month 月份
	 * @return 月度管理报告信息
	 */
	public MonthlyManager getMonthDataByYearMonth(String year, String month){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM  MonthlyManager M ");
		sbdHql.append(" WHERE  M.year = ? ");
		sbdHql.append(" AND  M.month = ? ");
		List<MonthlyManager> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{year, month});
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 得到月度管理报告信息
	 * @param year 年度
	 * @param month 月份
	 * @return 月度管理报告信息
	 */
	public List<MonthlyManager> getMonthDataListByYearMonth(String year, String month){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM  MonthlyManager M ");
		sbdHql.append(" WHERE  M.year = ? ");
		sbdHql.append(" AND  M.month <= ? ");
		sbdHql.append(" ORDER BY M.month DESC ");
		List<MonthlyManager> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{year, month});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	/**
	 * 项目情况
	 * 获当年的所有项目
	 * @return List<Project>
	 * @author whxing 2014-11-10
	 */
	public List<Project> getAllProjectByYear(String year) {
//		StringBuilder sbdHql = new StringBuilder();
//		String startDate = year+"-01-01";
//		String endDate = year+"-12-31";
//		sbdHql.append("from Project p where p.isVisual = '0'");
//		sbdHql.append(" and p.planStartTime >= to_date('"+startDate+"','yyyy-mm-dd')");
//		sbdHql.append(" and p.planStartTime <= to_date('"+endDate+"','yyyy-mm-dd')");
//		sbdHql.append(" order by nlssort(p.name,'NLS_SORT=SCHINESE_PINYIN_M')");
		String hql = " from Project p where p.isEnd = '0' and p.isVisual='0' order by nlssort(p.name,'NLS_SORT=SCHINESE_PINYIN_M')";
		List<Project> result=  this.getHibernateTemplate().find(hql);
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * 项目情况
	 * 根据项目ID和年份上一月份获取上月的记录
	 * @return
	 */
	public PersonDay getLastPersonDay(String year,String projectId,String month){
		String hql = "from PersonDay t where t.year= ?  and t.projectId= ? and t.month = ?";
		List result = this.getHibernateTemplate().find(hql.toString(),
				new Object[]{year,projectId,month});
		if (result != null && result.size() > 0) {
			return (PersonDay) result.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据项目ID和年份获取该年该项目的风险个数
	 * @param year
	 * @param projectId
	 * @return
	 */
	public long getRiskCountByPrjName(String year,String prjName,String month,String lastDay){
		StringBuilder sbdHql = new StringBuilder();
		String startDate = year+"-"+month+"-01";
		String endDate = year+"-"+month+"-"+lastDay;
		sbdHql.append("select count(*) from PrjRisk p where p.prjname='"+prjName.trim()+"'");
		sbdHql.append(" and p.createdate >= to_date('"+startDate+"','yyyy-mm-dd')");
		sbdHql.append(" and p.createdate <= to_date('"+endDate+"','yyyy-mm-dd')");
		List result = this.getHibernateTemplate().find(sbdHql.toString());
		if (result.size()>0) {
			Object obj = result.get(0);
			return  (Long) obj;
		}
		return 0;
	}
	
	public Object prjContractSum(String year,String month,String lastDay){
		StringBuilder sbdHql = new StringBuilder();
		String startDate = year+"-"+month+"-"+"01";
		String endDate = year+"-"+month+"-"+lastDay;
		sbdHql.append("select sum(p.total) from PrjContract p where 1=1");
		sbdHql.append(" and p.signDate >= to_date('"+startDate+"','yyyy-mm-dd')");
		sbdHql.append(" and p.signDate <= to_date('"+endDate+"','yyyy-mm-dd')");
		List result = this.getHibernateTemplate().find(sbdHql.toString());
		
		if (result!=null&&!result.isEmpty()) {
			Object obj = result.get(0);
			return obj;
		}
		return 0;
	}
}
