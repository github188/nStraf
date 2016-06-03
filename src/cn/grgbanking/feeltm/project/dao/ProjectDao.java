package cn.grgbanking.feeltm.project.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class ProjectDao extends BaseDao<Project> {

	/**
	 * @param hql
	 * @return lhyan3 2014年6月25日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getAllNameIds(String hql) {
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * @param pageNum
	 * @param pageSize
	 * @return lhyan3 2014年6月12日
	 */
	public Page getPage(int pageNum, int pageSize) {
		Page page = null; 
		String hql = "from Project where 1=1 order by name desc";
		page = ((BaseHibernateTemplate) this.getHibernateTemplate()).getPage(
				hql, pageNum, pageSize);
		return page;
	}

	/**
	 * @param g
	 *            lhyan3 2014年6月12日
	 */
	public void deleteUserProject(Project g) {
		String hql = "delete from UserProject p where p.project.id='" + g.getId()
				+ "'";
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	/**
	 * @return lhyan3 2014年6月13日
	 */
	@SuppressWarnings("unchecked")
	public List<Project> listAllGroup() {
		String hql = "from Project order by nlssort(name,'NLS_SORT=SCHINESE_PINYIN_M') ";
		List<Project> list = new ArrayList<Project>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 获取未完结项目
	 * @return List<Project>
	 * @author lping1 2014-9-29
	 */
	public List<Project> listUnFinishedGroup() {
		String hql = "from Project where isEnd='0' order by nlssort(name,'NLS_SORT=SCHINESE_PINYIN_M') ";
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 获取未完结项目
	 * @return List<Project>
	 * @author lping1 2014-9-29
	 */
	public List<Project> listUnFinishedGroupExpectOtherProject() {
		String hql = "from Project where isEnd='0'and name!='其他项目' order by nlssort(name,'NLS_SORT=SCHINESE_PINYIN_M') ";
		return this.getHibernateTemplate().find(hql);
	}


	@SuppressWarnings("unchecked")
	public List<Project> getProjectByUserId(String userid) {
		String hql = "from Project group where group.isEnd ='0'  and   group.id in (select u.project.id from UserProject u where u.userid='"
				+ userid + "')";
		return this.getHibernateTemplate().find(hql);
	}

	public List<Object[]> findUserListByGrpDept(String grpcode,
			String deptvalue, String groupValue, String deptValue2) {
		String hql = " select s.username,s.userid from SysUser s where 1=1 ";
		if (deptvalue != null && !deptvalue.equals(deptValue2)) {
			hql += " and s.deptName = '" + deptvalue + "'";
		}
		if (grpcode != null && !grpcode.equals(groupValue)) {
			hql += " and s.userid in "
					+ "(select usrUsrgrp.userid from  UserProject usrUsrgrp "
					+ "where usrUsrgrp.project.id='" + grpcode + "')";
		}
		try {
			List list = this.getHibernateTemplate().find(hql);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param projectId
	 * @return lhyan3 2014年6月25日
	 */
	@SuppressWarnings("unchecked")
	public Project getProjectById(String projectId) {
		String hql = "from Project p where p.id='" + projectId + "'";
		List<Project> list = this.getHibernateTemplate().find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据项目组查询在该项目组中人员
	 * 
	 * @param hql
	 * @return lhyan3 2014年6月26日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getInProjectByProject(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 根据项目组查询不在该项目中人员
	 * 
	 * @param hql
	 * @return lhyan3 2014年6月26日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getNotInProjectByProject(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * @param hql
	 * @return lhyan3 2014年6月26日
	 */
	public List<Project> getProjectByManager(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 根据项目id获取项目关联
	 * 
	 * @param projectId
	 * @return lhyan3 2014年6月30日
	 */
	@SuppressWarnings("unchecked")
	public List<UserProject> getUserProject(String planStartTime,String planEndTime,String factStartTime,String factEndTime) {
		String hql = "from UserProject u where 1=1 ";
		if(!"".equals(planStartTime)){
			hql += " and u.planStartTime=to_date('"+planStartTime+"','yyyy-MM-dd') ";
		}else{
			hql += " and u.planStartTime is null";
		}
		if(!"".equals(planEndTime)){
			hql += " and u.planEndTime=to_date('"+planEndTime+"','yyyy-MM-dd') ";
		}else{
			hql += " and u.planEndTime is null";
		}
		if(!"".equals(factStartTime)){
			hql += " and u.factStartTime=to_date('"+factStartTime+"','yyyy-MM-dd') ";
		}else{
			hql += " and u.factStartTime is null";
		}
		if(!"".equals(factEndTime)){
			hql += " and u.factEndTime=to_date('"+factEndTime+"','yyyy-MM-dd') ";
		}else{
			hql += " and u.factEndTime is null";
		}
		List<UserProject> list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * 
	 * @param projectId
	 * @return lhyan3 2014年6月30日
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserProjectByTime(String projectId) {
		String hql = "select p.planStartTime,p.planEndTime,p.factStartTime,p.factEndTime "
				+ " from UserProject p  "
				+ " where p.project.id='"+projectId+"' "
				+ " group by p.planStartTime,p.planEndTime,p.factStartTime,p.factEndTime"
				+ " order by p.planStartTime";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 判断关联是否存在
	 * @param userid
	 * @param projectId
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	@SuppressWarnings("unchecked")
	public UserProject getUserProject(String userid, String projectId) {
		String hql = "from UserProject u where u.userid='"+userid+"' and u.project.id='"+projectId+"'";
		List<UserProject> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserProject> getUserPojectByProjectId(String projectId) {
		String hql = "from UserProject u where  u.project.id='"+projectId+"'";
		List<UserProject> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 * @param exist
	 * lhyan3
	 * 2014年6月30日
	 */
	public void deleteUserProject(UserProject exist) {
		String hql = "delete from UserProject u where u.id='"+exist.getId()+"'";
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	/**
	 * 历史记录
	 * @param hql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年7月2日
	 */
	public Page getHistoryPage(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}

	public Project getOtherProject() {
		String hql = "from Project p where p.name='其他项目'";
		List<Project> list = this.getHibernateTemplate().find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public String getProjectNameByUserid(String userid) {
		String prjName = "";
		List<Project> projects = getProjectByUserId(userid);
		if(projects!=null && projects.size()>0){
			for(Project project:projects){
				prjName += ","+project.getName();
			}
			prjName = prjName.substring(1);
		}
		return prjName;
	}

	/**
	 * 获取项目经理
	 * @param projectId
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public SysUser getManagerByProjectId(String projectId) {
		String hql = "from SysUser u where u.userid in (select p.proManagerId from Project p where p.id='"+projectId+"')";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取该登录用户的项目经理和部门经理或人事组
	 * @param userModel
	 * @return
	 */
	public List<SysUser> getUpPrjListByUser(UserModel userModel){
		String grpString = null;
		/*if(leader){
			grpString += ",'leader'";
		}
		if(dept){
			grpString += ",'deptManager'";
		}
		if(hr){
			grpString += ",'hr'";
		}
		
		grpString = grpString.substring(1);*/
		String hql = "from SysUser u where 1=1";
		if(UserRoleConfig.getGrpList(userModel).contains("groupManager")){
			grpString = "'deptManager'";
			hql += " and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in("+grpString+"))";
		}else if(UserRoleConfig.getGrpList(userModel).contains("deptManager")||UserRoleConfig.getGrpList(userModel).contains("hr")){
			grpString ="'hr'";
			hql +=" and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in("+grpString+") )";//or u.userid = '"+userModel.getUserid()+"')";
		}else if(!UserRoleConfig.getGrpList(userModel).contains("leader")){
			if(userModel.getGroupName()!=null&&!"".equals(userModel.getGroupName())&&this.getPrjByManager(userModel.getUserid()).size()<1){
				hql +=" and u.userid in (select p.proManagerId from Project p where p.name in ('"+userModel.getGroupName().replace(",", "','")+"'))";
			}
			else{
				//grpString = "'groupManager','deptManager'";
				hql += " and( (u.userid in (select g.userid from UsrUsrgrp g where g.grpcode  = 'groupManager')and u.deptName in ('"+userModel.getDeptName()+"','projectManager'))";
				hql += " or (u.userid in (select g.userid from UsrUsrgrp g where g.grpcode  ='deptManager')and u.deptName  ='"+userModel.getDeptName()+"'))"; 
			}
		}else {
			return new ArrayList<SysUser>();
		}
		hql += "order  by u.level,u.userid";
		System.out.println(hql);
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Project> getPrjByManager(String managerId){
		String hql = " from Project p where p.proManagerId = '"+managerId+"'";
		return this.getHibernateTemplate().find(hql);
		
	}

	public void deleteAttendance(Project project) {
		String hql = "delete from ProjectAttendance att where att.project.id = '"+project.getId()+"'";
		getHibernateTemplate().bulkUpdate(hql);
	}

	public void deleteResourcePlan(Project project) {
		String hql = "delete from ProjectResourcePlan rp where rp.project.id = '"+project.getId()+"'";
		getHibernateTemplate().bulkUpdate(hql);
		
	}
	/**显示所有项目组的简要信息，并除去“其他项目”
	 * wtjiao 2014年9月19日 下午2:38:17
	 * @return
	 */
	public List<Project> listAllGroupBriefExceptOtherProject() {
		try{
			String hql="select new cn.grgbanking.feeltm.project.domain.Project(id,name,proManagerId,proManager,projectType,isEnd) "
					+ "from Project where name!='其他项目' and isEnd = 0 order by  projectType,name ";
			return this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

	public List<Project> listAllGroupBriefProject() {
		try{
			String hql="select new cn.grgbanking.feeltm.project.domain.Project(id,name,proManagerId,proManager,projectType) from Project";
			return this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 根据是否完结排序获取项目Page
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPagebyNum(int pageNum, int pageSize,String prjName,String isEndValue) {
		String hql = " from Project p where 1=1  ";
		if(  prjName != null && !"".equals(prjName)){
			hql += " and p.name like '%"+prjName+"%'";
		}
		
		if(  isEndValue != null && !"".equals(isEndValue)){
			hql += " and p.isEnd='"+isEndValue+"'";
		}
		hql += "order by p.isEnd,p.isVisual,nlssort(p.name,'NLS_SORT=SCHINESE_PINYIN_M')";
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
	}
	
	
	/**
	 * 获取该登录用户的项目经理和部门经理或人事组
	 * @param userModel
	 * @return
	 */
	public List<SysUser> getUserRoleInfoByUser(UserModel userModel,String deptid){
		if("".equals(deptid)){
			deptid = userModel.getDeptName();
		}
		String grpString = null;
		String hql = "from SysUser u where 1=1 and status!='leave'";
		if(UserRoleConfig.getGrpList(userModel).contains("groupManager")){
			grpString = "'deptManager'";
			hql += " and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in("+grpString+"))";
			//部门领导审批时，值列出当前部门的部门领导，其他部门的排除
			hql +=" and u.deptName='"+deptid+"' ";
		}else if(!UserRoleConfig.getGrpList(userModel).contains("leader")){
			if(userModel.getGroupName()!=null&&!"".equals(userModel.getGroupName())&&this.getPrjByManager(userModel.getUserid()).size()<1){
				hql +=" and u.userid in (select p.proManagerId from Project p where p.name in ('"+userModel.getGroupName().replace(",", "','")+"'))";
			}else{
				//hql += " and( (u.userid in (select g.userid from UsrUsrgrp g where g.grpcode  = 'groupManager')and u.deptName in ('"+userModel.getDeptName()+"','projectManager'))";
				//hql += " or (u.userid in (select g.userid from UsrUsrgrp g where g.grpcode  ='deptManager')and u.deptName  ='"+userModel.getDeptName()+"'))";
				hql += " and (u.userid in (select g.userid from UsrUsrgrp g where g.grpcode  ='deptManager') and u.deptName  ='"+deptid+"')";
			}
		}else {
			return new ArrayList<SysUser>();
		}
		hql += "order  by u.level,u.userid";
		return this.getHibernateTemplate().find(hql);
	}
	/**
	 * 根据省份取项目
	 * @param province
	 * @return
	 */
	public List<Project> getProjectByProvince(String province){
		List<Project> list=null;
		String hql="from Project t where t.province = ? and t.isEnd != '1' and t.isVisual != '1' order by nlssort(t.name,'NLS_SORT=SCHINESE_PINYIN_M')";
		list=this.getHibernateTemplate().find(hql,province);
		return list;
	}
	
	/**
	 * 根据任务计划的计划ID查询出所属 的项目
	 * @param planId 计划ID
	 * @return 所属 的项目
	 */
	public Project getProjectByWeekPlanTask(String planId){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM  Project p ");
		sbdHql.append(" WHERE  p.id in ( ");
		sbdHql.append(" SELECT pw.projectId FROM ProjectWeekPlan pw");
		sbdHql.append(" WHERE pw.id = ? ");
		sbdHql.append(" ) ");
		List<Project> result = this.getHibernateTemplate().find(sbdHql.toString(), planId);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}else{
			return null;
		}
	}
	
	@Transactional
	public String getProjectNameByInfo(String prjname){
//		String hql = "from Project u where u.name like '%"+prjname+"%'";
//		List<Project> list = this.getHibernateTemplate().find(hql);
//		if(list!=null && list.size()>0){
//			return list.get(0).getName();
//		}
		String sql = "select c_name,numsofchar('"+prjname+"',c_name) from oa_project order by numsofchar('"+prjname+"',c_name) desc";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		if(list!=null && list.size()>0){
			Object[] record = (Object[])list.get(0);
			return record[0].toString();
		}
		return "";
	}
	
	/**
	 * 获取该登录用户的项目经理和部门经理或人事组
	 * @param userModel
	 * @return
	 */
	public List<SysUser> getHrOaFinaByUser(UserModel userModel){
		String grpString = null;
		String hql = "from SysUser u where 1=1";
		if(UserRoleConfig.getGrpList(userModel).contains("hr")){
			grpString ="'financial'";
			hql +=" and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in("+grpString+") )";
		}else {
			grpString ="'hr'";
			hql +=" and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in("+grpString+") )";
		}
		hql += "order  by u.level,u.userid";
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 获取财务人员数据
	 * @param userModel
	 * @return
	 */
	public List<SysUser> getFinaByUser(){
		String hql = "from SysUser u where 1=1  and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode in('financial'))";
		hql += "order  by u.level,u.userid";
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Project> getAllNonVirtualProject(){
		List<Project> list=null;
		String hql="from Project t where t.isVisual = 0 ";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 获取当前用户负责的项目
	 * @return
	 */
	public List<Project> getMyDutyPrjname(String userid) {
		String hql = "from Project where proManagerId='"+userid+"' order by nlssort(name,'NLS_SORT=SCHINESE_PINYIN_M') ";
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 根据项目名称获取项目的id
	 * @param prjGroup  项目名称
	 */
	public List<Project> getProjectIdByName(String prjGroup) {
		String hql = " from Project p where p.name = '"+prjGroup+"'";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 
	 * @param projectName 项目名称
	 * @param isEndValue  是否完结
	 * @return
	 */
	
	
	public List<Project> getProjectListByCondition(String projectName, String isEndValue) {
		String hql = " from Project p  where 1=1 ";
		if (projectName != null && !projectName.equals("")) {
			hql += " and p.name like '%" + projectName + "%'";
		}
		if (isEndValue != null && !isEndValue.equals("")) {
			hql += " and p.isEnd like '%" + isEndValue + "%'";
		}
		hql += " order by p.isEnd ";
		return this.getHibernateTemplate().find(hql);
	}

    /**
     * getUserProjectByUserId:描述 <br/>
     * 2016年4月20日 下午4:07:22
     * @param user
     * @return
     * @author zzwen6
     * @修改记录: <br/>
     */
    public List<UserProject> getUserProjectByUserId(SysUser user) {
        String hql = "from UserProject u  where 1=1 and u.userid = ?";
        
        return this.getHibernateTemplate().find(hql, user.getUserid());
    }

	

}
