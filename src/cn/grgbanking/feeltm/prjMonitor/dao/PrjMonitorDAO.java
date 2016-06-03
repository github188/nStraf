package cn.grgbanking.feeltm.prjMonitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
public class PrjMonitorDAO extends BaseDao<Project> {
	/**
	 * 根据项目类型获取项目
	 */
	public List<Project> getPrjByType(String prjType) {
		String hql = "from Project where projectType='" + prjType + "'";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 获取被监控的部门
	 */
	public List<DeptColor> getMonDeptColor(){
		String hql = "from DeptColor d where monFlag='1'";
		return this.getHibernateTemplate().find(hql);
	}
}
