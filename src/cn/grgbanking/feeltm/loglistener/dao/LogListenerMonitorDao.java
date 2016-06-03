package cn.grgbanking.feeltm.loglistener.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.loglistener.domain.LogListenerMonitor;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class LogListenerMonitorDao extends BaseDao<LogListenerMonitor>{

	public Page getAllListenerMonitor(String hql, int pageSize, int pageNum) {
		Page page = null; 
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page; 
	}

	/**
	 * 所有有效监控
	 * @param hql
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public List<LogListenerMonitor> getAllVilidListener(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 根据部门获取前一天没有写日志的人
	 * @param hql
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getNotLogByDept(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 
	 * @param hql
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getNotLogByPrj(String hql) {
		try{
			return getHibernateTemplate().find(hql);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<SysUser>();
		}
	}

	/**
	 * 
	 * @param hql
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public List<SysUser> getNotLogByUserId(String hql) {
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<LogListenerMonitor> getLogListenerByProjectId(String projectId){
		String hql="from LogListenerMonitor where orgId='"+projectId+"'";
		return this.getHibernateTemplate().find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<Project> getNotLogPrjByDept(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List<Object> getNotLogDeptByPrj(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List<Project> getNotLogPrjByUserid(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List<SysUser> getDeptEmail(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List<SysUser> getPrjEmail(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List<Object> getNotLogDeptByUserid(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public void deleteAll() {
		this.getHibernateTemplate().bulkUpdate("delete from LogListener ");
	}

}
