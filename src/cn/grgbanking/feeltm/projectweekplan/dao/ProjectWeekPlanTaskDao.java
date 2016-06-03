package cn.grgbanking.feeltm.projectweekplan.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.framework.dao.BaseDao;


/**
 * 周计划任务
 * @author zzhui1
 *
 */
@Repository("projectWeekPlanTaskDao")
public class ProjectWeekPlanTaskDao extends BaseDao<ProjectWeekPlanTask>{
	
	/**
	 * 查询周计划模块对应表数据:本周本人的任务完成情况
	 * @param userId 用户ID
	 * @param startDate 周日
	 * @param endDate 周六
	 * @return 周计划模块对应表数据List
	 */
	public List<ProjectWeekPlanTask> getTaskInfoByWeek(String userId, 
			String startDate, String endDate){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM  ProjectWeekPlanTask t ");
		sbdHql.append(" WHERE  t.userKey = '" + userId + "'");
		sbdHql.append(" AND ( ");
		sbdHql.append(" (t.startDate <= to_date('" + startDate +"', 'yyyy-MM-dd') and "
				+ "t.endDate >= to_date('" + startDate +"', 'yyyy-MM-dd')) ");
		sbdHql.append(" OR ");
		sbdHql.append(" (t.startDate <= to_date('" + startDate +"', 'yyyy-MM-dd') and "
				+ "t.endDate >= to_date('" + endDate +"', 'yyyy-MM-dd')) ");
		sbdHql.append(" OR ");
		sbdHql.append(" (t.startDate >= to_date('" + startDate +"', 'yyyy-MM-dd') and "
				+ "t.endDate <= to_date('" + endDate +"', 'yyyy-MM-dd')) ");
		sbdHql.append(" OR ");
		sbdHql.append(" (t.startDate <= to_date('" + endDate +"', 'yyyy-MM-dd') and "
				+ "t.endDate >= to_date('" + endDate +"', 'yyyy-MM-dd')) ");
		sbdHql.append(" ) ");
		List<ProjectWeekPlanTask> result = this.getHibernateTemplate().find(sbdHql.toString());
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<ProjectWeekPlanTask>();
		}
	}
}
