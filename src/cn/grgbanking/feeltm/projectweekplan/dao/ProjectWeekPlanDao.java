package cn.grgbanking.feeltm.projectweekplan.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlan;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTarget;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("projectWeekPlanDao")
public class ProjectWeekPlanDao extends BaseDao<Object>{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getPage(ProjectWeekPlan plan, int pageNum, int pageSize) {
		try{
			String hql="from ProjectWeekPlan plan where plan.projectId in( select id from Project) ";
			if(plan!=null && StringUtils.isNotBlank(plan.getProjectId())){
				hql+=" and plan.projectId='"+plan.getProjectId()+"'";
			}
			
			if(plan!=null && StringUtils.isNotBlank(plan.getWeekDesc())){
				hql+=" and plan.weekDesc='"+plan.getWeekDesc()+"'";
			}
			if(plan!=null && StringUtils.isNotBlank(plan.getWeekPeriod())){
				hql+=" and plan.weekPeriod='"+plan.getWeekPeriod()+"'";
			}
			if(plan!=null && StringUtils.isNotBlank(plan.getCustomerKey())){
				hql+=" and plan.customerKey='"+plan.getCustomerKey()+"'";
			}
			if(plan!=null && StringUtils.isNotBlank(plan.getScheduleState())){
				hql+=" and plan.scheduleState='"+plan.getScheduleState()+"'";
			}
			if(plan!=null &&plan.getWeekStart()!=null){
				hql += " and plan.weekStart >=to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(plan.getWeekStart())+"','yyyy-MM-dd') ";
			}
			if(plan!=null &&plan.getWeekEnd()!=null){
				hql += " and plan.weekEnd <=to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(plan.getWeekEnd())+"','yyyy-MM-dd') ";
			}
			hql+=" order by plan.weekStart desc,plan.projectId desc,plan.customerKey desc";
			/*if(StringUtils.isNotBlank(planStartTime1)){
				hql += " and plan.planStartDate >=to_date('"+planStartTime1+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planStartTime2)){
				hql += " and plan.planStartDate <=to_date('"+planStartTime2+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planEndTime1)){
				hql += " and plan.planEndDate >=to_date('"+planEndTime1+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planEndTime2)){
				hql += " and plan.planEndDate <=to_date('"+planEndTime2+"','yyyy-MM-dd') ";
			}*/
			
			
			Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
			return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**获取周计划的目标列表
	 * wtjiao 2014年5月21日 下午3:02:48
	 * @param projectId
	 * @return
	 */
	public List<ProjectWeekPlanTarget> getProjectWeekPlanTargetList(String planId) {
		try{
			String hql="from ProjectWeekPlanTarget target where target.planId=?";
			List<ProjectWeekPlanTarget> list=(List<ProjectWeekPlanTarget>)((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{planId});
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**获取周计划的任务列表
	 * wtjiao 2014年6月25日 下午4:29:08
	 * @param planId
	 * @return
	 */
	public List<ProjectWeekPlanTask> getProjectWeekTaskList(String planId) {
		try{
			String hql="from ProjectWeekPlanTask task where task.planId=?";
			List<ProjectWeekPlanTask> list=(List<ProjectWeekPlanTask>)((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{planId});
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
		
		
	/**保存周计划基本信息
	 * wtjiao 2014年5月26日 下午4:09:56
	 * @param project
	 * @return
	 */
	public String saveProjectWeekPlan(ProjectWeekPlan plan) {
		((BaseHibernateTemplate) getHibernateTemplate()).save(plan);
		String id=plan.getId();
		return id;
	}
	
	public boolean updateProjectWeekPlan(ProjectWeekPlan plan) {
		((BaseHibernateTemplate) getHibernateTemplate()).update(plan);;
		return true;
	}

	/**保存周计划目标
	 * wtjiao 2014年5月26日 下午4:10:05
	 */
	public void saveProjectWeekPlanTargetList(List<ProjectWeekPlanTarget> list) {
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ProjectWeekPlanTarget target=list.get(i);
				((BaseHibernateTemplate) getHibernateTemplate()).save(target);
			}
		}
	}
	
	/**保存周计划任务
	 * wtjiao 2014年5月26日 下午4:10:05
	 */
	public void saveProjectWeekPlanTaskList(List<ProjectWeekPlanTask> list) {
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ProjectWeekPlanTask task=list.get(i);
				((BaseHibernateTemplate) getHibernateTemplate()).save(task);
			}
		}
	}

	/**删除周计划的目标列表
	 * wtjiao 2014年5月26日 下午4:29:53
	 */
	public void removeProjectWeekPlanTargets(String planId) {
		/*String hql="delete from ProjectWeekPlanTarget target where target.planId=?";
		((BaseHibernateTemplate)getHibernateTemplate()).bulkUpdate(hql, new Object[]{planId});*/
		String hql="delete from ProjectWeekPlanTarget target where target.planId='"+planId+"'";
		((BaseHibernateTemplate)getHibernateTemplate()).bulkUpdate(hql);
	}

	/**删除周计划的任务列表
	 * wtjiao 2014年5月26日 下午4:29:53
	 */
	public void removeProjectWeekPlanTasks(String planId) {
		/*String hql="delete from ProjectWeekPlanTask task where task.planId=?";
		((BaseHibernateTemplate)getHibernateTemplate()).bulkUpdate(hql, new Object[]{planId});*/
		String hql="delete from ProjectWeekPlanTask task where task.planId='"+planId+"'";
		((BaseHibernateTemplate)getHibernateTemplate()).bulkUpdate(hql);
	}

	/**获取项目的任务列表
	 * @param projectId
	 * @param userKey
	 * @param startDate
	 * @param endDate
	 */
	public List<ProjectWeekPlanTask> getTaskInProject(String projectId, String userKey,Date startDate, Date endDate) {
		String hql="from ProjectWeekPlanTask task where 1=1 ";
		if(StringUtils.isNotBlank(projectId)){
			hql+=" and task.planId in ( select plan.id from ProjectWeekPlan plan where plan.projectId='"+projectId+"')";
		}
		
		if(StringUtils.isNotBlank(userKey)){
			hql+=" and task.userKey='"+userKey+"'";
		}
		/**修改sql语句,计划任务可以跨日期查找**/
//		hql+=" and (task.startDate between to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"','yyyy-MM-dd') and to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(endDate)+"','yyyy-MM-dd')";
//		hql+=" or task.endDate between to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"','yyyy-MM-dd') and to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(endDate)+"','yyyy-MM-dd'))";
		hql+=" and ((to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"', 'yyyy-MM-dd') between task.startDate and task.endDate)";
		hql+=" or (to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(endDate)+"', 'yyyy-MM-dd') between task.startDate and task.endDate)";
		hql+=" or (task.startDate between to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"', 'yyyy-MM-dd') and to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(endDate)+"', 'yyyy-MM-dd'))";
		hql+=" or (task.endDate between to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"', 'yyyy-MM-dd') and to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(endDate)+"', 'yyyy-MM-dd'))";
		hql+=" )";
		hql+=" order by task.startDate";
		return (List<ProjectWeekPlanTask>)((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}
	
	/**
	 * 根据日志信息完善周计划中的任务完成度、偏差度及确认工时
	 */
	public void getDataForDayLogToPrjWeekPlan(String userid,String planid) {
		try{
			SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call weekplantaskupdate_proc(?,?)}");
			sqlQuery.setString(0, userid);
			sqlQuery.setString(1, planid);
			sqlQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getPlanTaskDescById(String plan_id) {
		try{
			String hql="from ProjectWeekPlanTask task where task.id=?";
			List<ProjectWeekPlanTask> list=(List<ProjectWeekPlanTask>)((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{plan_id});
			if(list!=null && list.size()>0){
				ProjectWeekPlanTask plantask = list.get(0);
				return plantask.getTaskContent();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

}
