package cn.grgbanking.feeltm.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.project.domain.ProjectResourcePlan;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
public class ProjectResourcePlanDao extends BaseDao<ProjectResourcePlan> {

	/**
	 * 根据用户ID,日志日期查询当前所在的项目数
	 * 
	 * @param userid
	 *            用户ID
	 * @param date
	 *            日志日期
	 * @return 当前所在的项目数
	 */
	public int getProjectNumberByUseridDate(String userid,  String date){
		int prjNumber = 0;
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM ProjectResourcePlan p ");
		sbdHql.append(" WHERE p.userid = ?");
		sbdHql.append(" AND p.factStartTime <= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" AND p.factEndTime >= to_date(?,'yyyy-MM-dd')");
		List<ProjectResourcePlan> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{userid,  date, date});
		if (result != null && result.size() > 0) {
			prjNumber = result.size();
		}
		return prjNumber;
	}
}
