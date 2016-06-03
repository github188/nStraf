package cn.grgbanking.feeltm.loglistener.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.loglistener.dao.LogListenerMonitorDao;
import cn.grgbanking.feeltm.loglistener.domain.LogListenerMonitor;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service
public class LogListenerService extends BaseService {

    @Autowired
    private LogListenerMonitorDao dao;

    @Autowired
    private StaffInfoService staffInfoService;

    @Autowired
    private ProjectService projectService;
	
	/**
	 * 更新项目的监控信息
	 */
	public void saveOrUpdateListenerMonitorByProject(String projectId){
		Project p=projectService.getProjectById(projectId);
		if(p!=null){
			LogListenerMonitor moni=null;
			List<LogListenerMonitor> moniList=getLogListenerByOrgId(projectId);
			if(moniList==null || moniList.size()<=0){
				moni=new LogListenerMonitor();
			}else{
				moni=moniList.get(0);
				for(LogListenerMonitor m:moniList){
					dao.removeObject(m.getClass(),m.getId());
				}
			}
			moni.setOrgType("project");
			moni.setAutoUpdate("1");//默认自动更新
			moni.setOrgId(projectId);
			moni.setOrgName(p.getName());
			
			String[] idValue=projectId.split(",");
			projectId=idValue[0];
			//获取监控人id
			List<SysUser> monitors=new ArrayList<SysUser>();
			monitors.addAll(getRoleUser_ProjectManager(p));
			monitors.addAll(getRoleUser_DeptManager(p));
			moni.setMonitorId(toUserIds(monitors));
			moni.setMonitorName(toUserNames(monitors));
			//获取被监控人
			List<SysUser> watched=getRoleUser_ProjectMember(p);
			moni.setWatchedId(toUserIds(watched));
			moni.setWatchedName(toUserNames(watched));
			
			save(moni);
		}
	}
	
	/**删除指定项目的监控 
	 * @param projectId
	 */
	public void deleteListenerMonitorByProjectId(String projectId){
		List<LogListenerMonitor> moniList=getLogListenerByOrgId(projectId);
		if(moniList!=null && moniList.size()>=0){
			for(LogListenerMonitor m:moniList){
				dao.removeObject(m.getClass(),m.getId());
			}
		}
	}

	
	private List<LogListenerMonitor> getLogListenerByOrgId(String projectId) {
		List<LogListenerMonitor> list= dao.getLogListenerByProjectId(projectId);
		return list;
	}


	/**部门经理
	 * wtjiao 2014年11月21日 上午9:09:11
	 * @param p
	 * @return
	 */
	public List<SysUser> getRoleUser_DeptManager(Project p) {
		List<SysUser> users=new ArrayList<SysUser>();
		List<UserProject> ups=p.getUserProjects();//遍历项目组成员
		List<String> userIds=new ArrayList<String>();
		for(int i=0;i<ups.size();i++){
			try{
				userIds.add(ups.get(i).getUserid());
			}catch(Exception e){}
		}
		if(userIds==null || userIds.size()<=0){
			return users; 
		}
		List<SysUser> deptManagers=staffInfoService.getDeptManagerByUser(userIds);
		if(deptManagers!=null){
			for(int i=0;i<deptManagers.size();i++){
				SysUser user=deptManagers.get(i);
				if(user.getGrgEndDate()==null){
					users.add(user);
				}
			}
		}
		return users;
	}

	/**项目组成员
	 * wtjiao 2014年11月21日 上午9:07:08
	 * @param p
	 * @return
	 */
	public List<SysUser> getRoleUser_ProjectMember(Project p) {
		List<SysUser> users=new ArrayList<SysUser>();
		List<UserProject> ups=p.getUserProjects();
		for(int i=0;i<ups.size();i++){
			try{
				SysUser user=staffInfoService.findUserByUserid(ups.get(i).getUserid());
				if(user !=null && user.getGrgEndDate()==null){
					users.add(user);
				}
			}catch(Exception e){}
		}
		return users;
	}

	/**项目经理
	 * wtjiao 2014年11月21日 上午9:03:38
	 * @param p
	 * @return
	 */
	public List<SysUser> getRoleUser_ProjectManager(Project p){
		List<SysUser> users=new ArrayList<SysUser>();
		try{
			SysUser user=staffInfoService.findUserByUserid(p.getProManagerId());
			 
			if(user !=null && user.getGrgEndDate()==null){
				users.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	
	/**获取用户id
	 * wtjiao 2014年11月21日 上午11:30:19
	 * @param list
	 * @return
	 */
	public String toUserIds(List<SysUser> users){
		String userids="";
		if(users!=null){
			for(int i=0;i<users.size();i++){
				SysUser user=users.get(i);
				userids+=","+user.getUserid();
			}
			if(users.size()>0){
				userids=userids.substring(1);
			}
		}
		return userids;
	}
	
	/**获取用户姓名
	 * wtjiao 2014年11月21日 上午11:30:19
	 * @param list
	 * @return
	 */
	public String toUserNames(List<SysUser> users){
		String usernames="";
		if(users!=null){
			for(int i=0;i<users.size();i++){
				SysUser user=users.get(i);
				usernames+=","+user.getUsername();
			}
			if(users.size()>0){
				usernames=usernames.substring(1);
			}
		}
		return usernames;
	}
	
	
	/**
	 * 获取列表
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return lhyan3 2014年7月28日
	 */
	public Page getAllListenerMonitor(int pageSize, int pageNum) {
		String hql = "from LogListenerMonitor where 1 = 1 order by orgType asc, nlssort(orgName,'NLS_SORT=SCHINESE_PINYIN_M') ";
		return dao.getAllListenerMonitor(hql, pageSize, pageNum);
	}

	/**
	 * 保存
	 * 
	 * @param logListener
	 *            lhyan3 2014年7月29日
	 */
	public void save(LogListenerMonitor logListenerMonitor) {
		dao.addObject(logListenerMonitor);
	}
	
	/**
	 * 所有自动更新的监控
	 * 
	 * @return lhyan3 2014年7月29日
	 */
	public List<LogListenerMonitor> getAutoUpdateListener() {
		String hql = "from LogListenerMonitor moni where moni.autoUpdate='1'";
		return dao.getAllVilidListener(hql);
	}
	
	/**
	 * 所有监控
	 * 
	 * @return lhyan3 2014年7月29日
	 */
	public List<LogListenerMonitor> getAllListener() {
		String hql = "from LogListenerMonitor";
		return dao.getAllVilidListener(hql);
	}

	/**
	 * 根据部门获取不在指定项目中的前一天没有写日志的人
	 * 
	 * @param code
	 *            部门key
	 * @param prjname
	 *            指定项目
	 * @param calday
	 *            日期
	 * @return lhyan3 2014年7月29日
	 */
	public List<SysUser> getNotLogByDept(String code, String prjname,String calday) {
		String hql = "from SysUser u where u.deptName='"+ code+ "' "
				+ " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday
				+ "','yyyy-MM-dd'))";
		if(prjname!=null && !"".equals(prjname)){
			hql += " and u.userid not in (select p.userid from UserProject p where p.project.id in ("
					+ prjname + "))";
		}
		//部门经理除外
		hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.deptManagerGroup+"')";
		//项目经理除外
		hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.prjManagerGroup+"')";
		return dao.getNotLogByDept(hql);
	}

	/**
	 * 根据项目获取没有写日志的人
	 * 
	 * @param code
	 * @param calday
	 * @return lhyan3 2014年7月29日
	 */
	public List<SysUser> getNotLogByPrj(String code,String prjname, String calday) {
		String hql = "from SysUser u where u.userid not in "
					+ "(select d.userId from DayLog d where d.logDate=to_date('"+calday+"','yyyy-MM-dd')) "
				    + " and u.userid in "
				    	+ "(select p.userid from UserProject p where p.project.id='"+ code + "')";
		hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.prjManagerGroup+"') ";
		if(prjname!=null && !"".equals(prjname)){
			if(prjname.endsWith(",")){
				prjname = prjname.substring(0, prjname.length()-1);
				if(!"".equals(prjname)){
					hql += " and u.userid not in (select p.userid from UserProject p where p.project.id in ("+prjname+"))";
				}
			} 
		}
		return dao.getNotLogByPrj(hql);
	}
	public List<SysUser> getNotLogByPrj(String code, String calday) {
		List<SysUser> list=null;
		try{
			String hql = "from SysUser u where  u.status !='leave' and u.userid not in "
					+ "(select d.userId from DayLog d where d.logDate=to_date('"+calday+"','yyyy-MM-dd')) "
					+ " and u.userid in "
					+ "(select p.userid from UserProject p where p.project.id='"+ code + "')" ;
			       // + " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.projectManagerGroup+"') ";
			list=dao.getNotLogByPrj(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<SysUser>();
		}
	}

	/**
	 * 获取不在已监听部门，已监听项目中的人
	 * 
	 * @param userids
	 *            人员
	 * @param dept
	 *            已监听部门
	 * @param prjname
	 *            已监听项目
	 * @param calday
	 *            日期
	 * @return lhyan3 2014年7月29日
	 */
	public List<SysUser> getNotLogByUserId(String userids, String dept,
			String prjname, String calday) {
		String hql = "from SysUser u where u.userid in (" + userids + ")";
		if (dept != null && !"".equals(dept)) {
			hql += " and u.deptName not in (" + dept + ")";
		}
		if(prjname!=null && !"".equals(prjname)){
			hql += " and u.userid not in (select p.userid from UserProject p where p.project.id in ("+prjname+"))";
		}
		hql += " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday + "','yyyy-MM-dd'))";
		return dao.getNotLogByUserId(hql);
	}
	
	public List<SysUser> getNotLogByUserId(String userids, String dept, String calday) {
		String hql = "from SysUser u where u.userid in (" + userids + ")";
		if (dept != null && !"".equals(dept)) {
			hql += " and u.deptName not in (" + dept + ")";
		}
		hql += " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday + "','yyyy-MM-dd'))";
		return dao.getNotLogByUserId(hql);
	}

	/**
	 * @param id
	 * @return lhyan3 2014年7月29日
	 */
	public LogListenerMonitor getLogListenerById(String id) {
		return (LogListenerMonitor) dao.getObject(LogListenerMonitor.class, id);
	}

	/**
	 * @param listener
	 *            lhyan3 2014年7月29日
	 */
	public void update(LogListenerMonitor listener) {
		dao.updateObject(listener);
	}

	/**
	 * 
	 * @param id
	 *            lhyan3 2014年7月29日
	 */
	public void delete(String id) {
		dao.removeObject(LogListenerMonitor.class, id);
	}
	
	public void deleteAll() {
		dao.deleteAll();
	}

	/**
	 * 根据部门获取人员
	 * @param code
	 * @param calday
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<SysUser> getNotLogByDept(String code, String calday) {
		String hql = "from SysUser u where u.status !='leave' and  u.deptName='"+ code+ "' "
				+ " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"+ calday+ "','yyyy-MM-dd'))";
		//部门经理除外
		//hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.deptManagerGroup+"')";
		return dao.getNotLogByDept(hql);
	}

	/**
	 * 获取当前部门下未写日志的人的项目经理
	 * @param code
	 * @param calday
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<Project> getNotLogPrjByDept(String code, String calday) {
		String hql = "from Project p where p.id in "
					+ "(select p.project.id from UserProject p where p.userid in "
						+ "(select u.userid from SysUser u where u.deptName='"+ code+ "' "
							+ " and u.userid not in "
							+ "(select d.userId from DayLog d where d.logDate=to_date('"+ calday+ "','yyyy-MM-dd'))"
						+ ")"
					+ ")";
		return dao.getNotLogPrjByDept(hql);
	}

	/**
	 * 获取部门
	 * @param code
	 * @param calday
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<Object> getNotLogDeptByPrj(String code, String calday) {
		String hql = "select u.deptName from SysUser u where u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday
				+ "','yyyy-MM-dd'))"
				+ " and u.userid in (select p.userid from UserProject p where p.project.id='"
				+ code + "')";
		hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.prjManagerGroup+"')";
		hql += " group by u.deptName";
		return dao.getNotLogDeptByPrj(hql);
	}

	/**
	 * 
	 * @param code
	 * @param prjname
	 * @param calday
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<Project> getNotLogPrjByUserid(String code, String prjname,
			String calday) {
		String hql = "from Project g where g.code in "
				+ "(select p.project.id from UserProject p where p.userid in "
					+ "(select u.userid from SysUser u where u.userid in (" + code + ")";
		if(prjname!=null && !"".equals(prjname)){
			hql += " and u.userid not in "
					+ " (select p.userid from UserProject p where p.project.id in ("+prjname+"))";
		}
		hql += " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday + "','yyyy-MM-dd'))"
						+ ")"
				+ ")";
		return dao.getNotLogPrjByUserid(hql);
	}
	
	public List<Project> getNotLogPrjByUserid(String code,String calday) {
		String hql = "from Project g where g.code in "
				+ "(select p.project.id from UserProject p where p.userid in "
					+ "(select u.userid from SysUser u where u.userid in (" + code + ")";
		hql += " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday + "','yyyy-MM-dd'))"
						+ ")"
				+ ")";
		return dao.getNotLogPrjByUserid(hql);
	}

	/**
	 * 
	 * @param code
	 * @param dept
	 * @param calday
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<Object> getNotLogDeptByUserid(String code, String dept,
			String calday) {
		String hql = "select u.deptName from SysUser u where u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"
				+ calday
				+ "','yyyy-MM-dd'))"
				+ " and u.userid in ("+code+")";
		if(dept!=null && !"".equals(dept)){
			hql += " and u.deptName not in ("+dept+")";
		}
		hql += " and u.userid not in (select p.userid from UsrUsrgrp p where p.grpcode='"+UserRoleConfig.prjManagerGroup+"')";
		hql += " group by u.deptName";
		return dao.getNotLogDeptByUserid(hql);
	}

	/**
	 * 
	 * @param deptList
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<SysUser> getDeptEmail(String deptListStr) {
		if(deptListStr.endsWith(",")){
			deptListStr = deptListStr.substring(0, deptListStr.length()-1);
		}
		if(deptListStr!=null && !"".equals(deptListStr)){
			String hql = "from SysUser u where u.deptName in ("+deptListStr+")"
					+ " and u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.deptManagerGroup+"')";
			return dao.getDeptEmail(hql);
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @param prjList
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public List<SysUser> getPrjEmail(String prjListStr) {
		if(prjListStr.endsWith(",")){
			prjListStr = prjListStr.substring(0, prjListStr.length()-1);
		}
		if(prjListStr!=null && !"".equals(prjListStr)){
			String hql = "from SysUser u where u.userid in (select p.proManagerId from Project p where p.id in ("+prjListStr+"))";
			return dao.getPrjEmail(hql);
		}else{
			return null;
		}
	}

}
