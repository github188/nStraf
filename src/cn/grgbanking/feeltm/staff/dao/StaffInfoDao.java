package cn.grgbanking.feeltm.staff.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

/**
 * 2014-4-24 员工信息
 * 
 * @author lhyan3
 * 
 */
@Repository
public class StaffInfoDao extends BaseDao<SysUser> {

	/**
	 * 获取月度部门人员信息
	 * @return
	 */
	public List<Object> getStaffsGroupbyDept(){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" select ");
		sbdHql.append(" s.deptName as dept, count(s.deptName) as cnt");
		sbdHql.append(" from SysUser s");
		sbdHql.append(" where s.status != 'leave' ");
		sbdHql.append(" group by s.deptName");
		List<Object> result = this.getHibernateTemplate().find(sbdHql.toString());
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<Object>();
		}
	}
	
	/**
	 * 查询月入职人数
	 * 
	 * @param yyyyMM
	 *            入职的月份
	 * @return 入职信息
	 */
	public List<SysUser> getEntryUserInfo(String yyyyMM){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" from SysUser s");
		sbdHql.append(" where s.status != 'leave' ");
		sbdHql.append(" and to_char(s.grgBegindate,'yyyyMM') = ? ");
		List<SysUser> result = this.getHibernateTemplate().find(sbdHql.toString(),yyyyMM);
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<SysUser>();
		}
	}
	/**
	 * 查询月离职人数
	 * 
	 * @param yyyyMM
	 *            离职的月份
	 * @return 离职信息
	 */
	public List<SysUser> getLeaveUserInfo(String yyyyMM){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" from SysUser s");
		sbdHql.append(" where s.status = 'leave' ");
		sbdHql.append(" and to_char(s.grgEndDate,'yyyyMM') = ? ");
		List<SysUser> result = this.getHibernateTemplate().find(sbdHql.toString(),yyyyMM);
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<SysUser>();
		}
	}
	
	/**
	 * lhy 2014-4-25 判断标识是否已经存在
	 * 
	 * @param userid
	 * @return
	 */
	public boolean isExitStaffByUserid(String userid) {
		String sql = "from SysUser u where u.userid='" + userid + "'";
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (null != list && list.size() > 0) {
			// 存在
			return true;
		}
		return false;
	}

	
	/**
	 * ljlian  判断用户是否在职
	 * 
	 * @param userid
	 * @return
	 */
	public boolean  beonthejob(String userid) {
		String sql = "from SysUser u where u.userid='" + userid + "' and u.status!='leave'";
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (list != null && list.size()>0 ) {
			// 存在
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 根据用户名后用户id得出用id信息
	 * 
	 * @param username
	 * @return
	 */
	public String getUseridByUsername(String username) {
		// 先根据姓名查找，如果无，再根据用户Id查找
		String sql = "select userid from SysUser u where u.username='"
				+ username + "'";
		List<Object> list = this.getHibernateTemplate().find(sql);
		if (null == list && list.size() == 0) {
			sql = "select userid from SysUser u where u.userid='" + username
					+ "'";
			list = this.getHibernateTemplate().find(sql);
			if (null == list && list.size() == 0) {
				return "";
			} else if (list.size() > 0) {
				return list.get(0).toString();
			} else {
				return "";
			}
		} else if (list.size() > 0) {
			return list.get(0).toString();
		} else {
			return "";
		}
	}

	/**
	 * lhy 2014-4-25 保存
	 * 
	 * @param staff
	 */
	public void addStaffInfo(SysUser staff) {
		this.getHibernateTemplate().save(staff);

	}

	/**
	 * lhy 2014-4-25
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SysUser findUserByUserid(String id) {
		String sql = "from SysUser u where u.userid='" + id + "'";
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * lhy 2014-4-27 删除
	 * 
	 * @param strids
	 * @return
	 */
	public int removeStaffInfo(String[] strids) {
		String hql = "delete SysUser as user where "
				+ SqlHelper.fitStrInCondition("user.userid", strids);
		int i = 0;
		i += this.getHibernateTemplate().bulkUpdate(hql);
		return i;
	}
	/**
	 * ljlian 2015-07-21 删除
	 * 
	 * @param strids 工号
	 * @return
	 */
	public int removeStaffInfo(String jobNum) {
		String hql = "delete SysUser as user where user.jobNumber='"+ jobNum+"'";
		return  this.getHibernateTemplate().bulkUpdate(hql);
	}

	/**
	 * lhy 2014-4-29 根据hql分页查询
	 * 
	 * @param hql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page findUserInfoPage(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				pageNum, pageSize);
		return page;
	}

	/**
	 * lhy 2014-5-4
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getStaffByDeptKey(String key) {
		String hql = "from SysUser u where u.deptName='" + key + "' and u.status!='leave'";
		List<SysUser> users = new ArrayList<SysUser>();
		users = this.getHibernateTemplate().find(hql);
		return users;
	}


	/**
	 * 获取部门经理以上
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getdeptManagerUp() {
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g "
				+ " where g.grpcode='"+UserRoleConfig.deptManagerGroup+"' "
						+ "or g.grpcode='"+UserRoleConfig.leader+"')";
		List<SysUser> list = new ArrayList<SysUser>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * 获取项目经理
	 * 
	 * @return
	 */
	public List<SysUser> getGroupManager() {
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.projectManagerGroup+"')";
		List<SysUser> list = new ArrayList<SysUser>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * lhy 2014-5-6
	 * 
	 * @param userid
	 */
	public String getUsernameById(String userid) {
		String hql = "select u.username from SysUser u where u.userid='"
				+ userid + "'";
		List<Object> list = this.getHibernateTemplate().find(hql);
		String name = "";
		if (list != null && list.size() > 0) {
			name = list.get(0).toString();
		}
		return name;
	}

	/**
	 * lhy 2014-5-8
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEmailByUserid(String str) {
		String hql = "select u.email from SysUser u where u.userid in '" + str
				+ "'";
		return this.getHibernateTemplate().find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getgroupManagerUp() {
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g "
				+ "where g.grpcode='"+UserRoleConfig.projectManagerGroup+"'"
						+ " or g.grpcode='"+UserRoleConfig.deptManagerGroup+"' "
						+ " or g.grpcode='"+UserRoleConfig.leader+"' "
						+ ")";
		List<SysUser> list = new ArrayList<SysUser>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * @param deptName
	 * @param deptvalue
	 * @return lhyan3 2014年6月13日
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getNamesByDept(String deptName, String deptvalue,
			String groupId) {
		String hql = " select user.username,user.userid from SysUser user where 1=1 ";
		if (deptName != null && !"".equals(deptName)) {
			hql += " and user.deptName like '" + deptName + "'";
		}
		if (groupId != null && !"".equals(groupId)) {
			String grp = "(select p.userid from UserProject p where p.project.id ='"
					+ groupId + "' )";
			hql += " and user.userid in " + grp;
		}
		List<Object[]> names = this.getHibernateTemplate().find(hql);
		return names;
	}

	/**
	 * @param userId
	 * @return lhyan3 2014年6月15日
	 */
	public String getDeptNameValueByUserId(String userId) {
		String key = findUserByUserid(userId).getDeptName();
		return getDeptNameValueByKey(key);
	}

	/**
	 * @param key
	 * @return lhyan3 2014年6月15日
	 */
	public String getDeptNameValueByKey(String key) {
		String dept = (String) BusnDataDir.getMapKeyValue(
				"staffManager.department").get(key);
		if (dept == null) {
			dept = "";
		}
		return dept;
	}


	/**
	 * 根据工号判断用户是否存在
	 * 
	 * @param userNumber
	 *            工号
	 * @param NumType
	 *            工号类型：0(公司工号);1(外派工号)
	 * @return zqsheng1 2014年7月1日
	 */
	public boolean isExitStaffByUserNumber(String userNumber, String numType) {
		String sql = "";
		if (numType != null && numType.equals("0")) {
			sql = "from SysUser u where u.jobNumber='" + userNumber + "'";
		}
		if (numType != null && numType.equals("1")) {
			sql = "from SysUser u where u.outNumber='" + userNumber + "'";
		}
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (null != list && list.size() > 0) {
			// 存在
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户姓名判断该用户是否在sysuser表中
	 * @param username
	 * @return
	 */
	public boolean isExitStaffByUserName(String username) {
		String sql = "from SysUser u where u.username='" + username + "'";
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (null != list && list.size() > 0) {
			// 存在
			return true;
		}
		return false;
	}

	/**
	 * 根据工号查找用户信息
	 * 
	 * @param userNumber
	 *            工号
	 * @param numType
	 *            工号类型：0(公司工号);1(外派工号)
	 * @return zqsheng1 2014年7月1日
	 */
	public SysUser findUserByUserNumber(String userNumber, String numType) {
		String sql = "";
		if (numType != null && numType.equals("0")) {
			sql = "from SysUser u where u.jobNumber='" + userNumber + "'";
		}
		if (numType != null && numType.equals("1")) {
			sql = "from SysUser u where u.outNumber='" + userNumber + "'";
		}
		if (numType != null && numType.equals("2")) {
			sql = "from SysUser u where u.userid='" + userNumber + "'";
		}
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据部门获取人员
	 * @param code
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getUserByDept(String code) {
		String hql = "from SysUser u where u.deptName='"+code+"'";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 根据部门获取部门经理
	 * @param code
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public SysUser getDeptManagerByDept(String code) {
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.deptManagerGroup+"')"
				+ " and u.deptName='"+code+"'";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 
	 * @param code  部门名称    如：kaifaTwo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getAllNotLeaveDeptManagerByDept(String code) {
		String hql = "from SysUser u where u.status!='leave' and u.status!='离职' and  u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.deptManagerGroup+"')"
				+ " and u.deptName='"+code+"'";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**根据用户获取其部门经理
	 * wtjiao 2014年10月23日 下午2:44:59
	 * @param userId
	 */
	public List<SysUser> getDeptManagerByUser(List<String> userIds){
		String hql = "from SysUser u where u.status!='leave' and u.status!='离职' and  u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.deptManagerGroup+"')"
				+ " and u.deptName in ( select innerUser.deptName from SysUser innerUser where 1=1 ";
		if(userIds!=null && userIds.size()>0){
			for(int i=0;i<userIds.size();i++){
				if(i==0){
					hql+="and (1=0 ";
				}
				hql+=" or innerUser.userid='"+userIds.get(i)+"'";
				if(i==userIds.size()-1){
					hql+=")";
				}
			}
		}
		hql+=")";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}
	
	/**
	 *  获取空闲员工
	 * @return List<SysUser>
	 * @author lping1 2014-9-23
	 */
	public List<SysUser> getRestStaff(){
		String hql = "from SysUser su where su.status!='leave' and su.status!='离职' and su.userid not in(select up.userid from UserProject up where up.project.isEnd=0)" +
				" and su.userid not in(select p.proManagerId from Project p where p.isEnd=0 )" + 
				" and su.deptName in(select deptId from DeptColor where monFlag='1')";
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 根据部门ID获取部门人员
	 * @param  deptId 部门ID
	 * @return List<SysUser>
	 * @author lping1 2014-9-23
	 */
	public List<SysUser> getUsersByDeptId(String deptId){
		String hql = "from SysUser su where su.status!='leave' and su.status!='离职'"; 		
		if(null!=deptId && !"".equals(deptId)){
			hql += " and deptName='" + deptId + "'";	
		}else{
			hql += " and 1=2";
		}
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 获取密码为空的员工，即从EHR获取的新员工
	 * @return List<SysUser>
	 * @author lping1 2014-9-23
	 */
	public List<SysUser> getNewUserFromEHR(){
		String hql = "from SysUser where userpwd is null";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * listAllUser:查询离职的员工 <br/>
	 * 2016年4月21日 上午10:04:33
	 * @return
	 * @author zzwen6
	 * @修改记录: <br/>
	 */
	public List<SysUser> listAllUser() {
	    //
		String hql = "from SysUser u  where u.status != 'leave'";
		return this.getHibernateTemplate().find(hql);
	}

	public List<SysUser> queryUser(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public boolean updatePartStaffInfo(String mobile, String email , String userid) {
		// TODO Auto-generated method stub
		
		try{
			String sql=" update  SYS_USER  set  C_MOBILE='"+mobile+"'  , C_EMAIL='"+ email+"'  where C_USERID='"+userid+"'";
			System.out.println(sql);
			getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} 
		
	}
	
	/**
	 * 根据value值获取部门key值
	 * @param value
	 * @return
	 */
	public String getDeptkeyByValue(String value){
		if(value==null){
			value="";
		}
		String sql = "select c_key from sys_datadir where trim(c_value)='"+value.trim()+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		if(list.size()!=0){
			return list.get(0).toString();
		}
		return "";
	}
	
	public String getEmailForUserid(String userid){
		String hql="select c_email from sys_user where c_userid='"+userid+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			String email = list.get(0).toString();
			if(email != null && !"".equals(email)){
				
				return email;
			} else{
				return "";
			}
		}
		return "";
	}
	
	/**在职且部门人员
	 * @param deptKey
	 * @return
	 */
	public List<SysUser> getNotLeaveStaffByDeptKey(String deptKey) {
		String hql = "from SysUser u where u.deptName='" + deptKey + "' and u.status != 'leave' and u.status !='离职'";
		List<SysUser> users = new ArrayList<SysUser>();
		users = this.getHibernateTemplate().find(hql);
		return users;
	}
	
	/**
	 * 判断用户是否离职
	 * @param userid
	 * @return
	 */
	public boolean flagUserisLeave(String userid){
		String hql = "from SysUser u where u.userid='"+userid+"' and u.status != 'leave' and u.status !='离职'";
		List<SysUser> users = new ArrayList<SysUser>();
		users = this.getHibernateTemplate().find(hql);
		if(users!=null && users.size()>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * @param realWatchedUserList  没有填写日志的人
	 * @param depname  部门名称
	 * @return
	 */
	public List<SysUser> getnotwritedaylogdeptlist(
			List<SysUser> realWatchedUserList, String depname) {
		String	hql="   from  SysUser u   where u.status!='leave'   and  u.deptName='"+depname+"'";	
		
		if (realWatchedUserList != null  &&  realWatchedUserList.size()>0) {
			hql += "   and u.userid  in ( ";
			for (int i = 0; i < realWatchedUserList.size(); i++) {
				if (i == (realWatchedUserList.size() - 1)) {
					hql += "'"+realWatchedUserList.get(i).getUserid()+"'" ;
				} else {
					hql += "'"+realWatchedUserList.get(i).getUserid()+"',";
				}
			}
			hql += "  )";
		}
		return  ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}
	
	
	/**
	 * 根据部门获取部门经理
	 * @param code
	 * @return
	 * ljlian
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getDeptManagerByDeptname(String code) {
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g where g.grpcode='"+UserRoleConfig.deptManagerGroup+"')"
				+ " and u.deptName='"+code+"'";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		
		return list;
	}
	
	/**
	 * 根据员工的工号 查找员工信息
	 * @param employeeid
	 * @return
	 */
	public List<SysUser> findStaffByEmployeeId(String employeeid) {
		String hql=" from  SysUser u where u.jobNumber='"+employeeid+"'";
		List<SysUser> list=this.getHibernateTemplate().find(hql);
		return list;
	}
}
