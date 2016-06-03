/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.List;

import org.springframework.stereotype.Repository;








import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;



@Repository  
@SuppressWarnings("unchecked") 
public class SysUserGroupDao extends BaseDao<UsrUsrgrp> {
	// ��ɾ���û���ɫ��ϵ
	public int removeSysUserGroup(String[] userids) {
		String sql = "delete UsrUsrgrp usrUsrgrp where "
				+ SqlHelper.fitStrInCondition("usrUsrgrp.userid", userids);
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(sql);		
		return i;
	}
	
	public int removeSysUserGroup(String userid) {
		String sql = "delete  UsrUsrgrp usrUsrgrp where usrUsrgrp.userid='"
				+ userid + "'";
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(sql);

		return i;
	}
	

	public List findSysUserGroupList(String userid) {
		String sql = "  from UsrUsrgrp  usrUsrgrp where usrUsrgrp.userid='"
				+ userid + "'";
		
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	/**lhy 2014-4-23
	 * 查询该组的用户列表
	 * @param userid
	 * @return
	 */
	public List findUserListByGrpcode(String grpcode) {
		String sql = "from SysUser s where  s.userid in "
				+ "(select usrUsrgrp.userid from  UsrUsrgrp usrUsrgrp "
				+ "where usrUsrgrp.grpcode='"
				+ grpcode + "')";
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List findUserListByProject(String grpcode) {
		String sql = "from SysUser s where  s.userid in "
				+ "(select usrUsrgrp.userid from  UserProject usrUsrgrp "
				+ "where usrUsrgrp.project.id='"
				+ grpcode + "')";
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List findUserListByProject1(String grpcode) {
		String sql = "select s.userid,s.username from SysUser s where  s.userid in "
				+ "(select usrUsrgrp.userid from  UserProject usrUsrgrp "
				+ "where usrUsrgrp.project.id='"
				+ grpcode + "')";
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**lhy 2014-4-23
	 * 查询该组的用户列表
	 * @param userid
	 * @return
	 */
	public List findUserListByGrpcode1(String grpcode) {
		String sql = "select s.userid,s.username from SysUser s where  s.userid in "
				+ "(select usrUsrgrp.userid from  UsrUsrgrp usrUsrgrp "
				+ "where usrUsrgrp.grpcode='"
				+ grpcode + "')";
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**lhy 2014-4-23
	 * 查询某部门的该组的用户列表
	 * @param userid
	 * @return
	 */
	public List findUserListByGrpcode(String deptname,String grpcode) {
		String sql = "from SysUser s where  "
				+ " s.deptName='"+deptname+"'"
				+ " and s.userid in "
				+ "(select usrUsrgrp.userid from  UsrUsrgrp usrUsrgrp where usrUsrgrp.grpcode='"
				+ grpcode + "')";
		try{
			List list= this.getHibernateTemplate().find(sql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**lhy 2014-4-23
	 * 获取不属于某个组的人员
	 * @param grpcode
	 * @return
	 */
	public List findUserNotGroupByGrp(String grpcode) {
		String sql = "from SysUser s where s.userid not in "
				+ "(select s.userid  from UsrUsrgrp s "
				+ "where s.grpcode='"+grpcode+"')";
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}
	
	/**
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List findUserNotGroupByProject(String grpcode) {
		String sql = "from SysUser s where s.userid not in "
				+ "(select s.userid  from UserProject s "
				+ "where s.project.id='"+grpcode+"')";
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}
	
	/**lhy 2014-4-23
	 * 获取不属于某个组的人员
	 * @param grpcode
	 * @return
	 */
	public List findUserNotGroupByGrp1(String deptname,String grpcode,String username) {
		String sql = "select s.userid,s.username from SysUser s where"
				+ " s.userid not in "
				+ "(select s.userid  from UsrUsrgrp s "
				+ "where s.grpcode='"+grpcode+"')";
		if(deptname!=null && !"".equals(deptname)){
			sql += " and s.deptName='"+deptname+"'";
		}
		if(username!=null && !"".equals(username)){
			sql += " and (s.userid like '%"+username+"%' or s.username like '%"+username+"%')";
		}
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}
	
	public List findUserNotProjectByProject1(String deptname,String grpcode) {
		String sql = "select s.userid,s.username from SysUser s where"
				+ " s.deptName='"+deptname+"'"
				+ " and s.userid not in "
				+ "(select s.userid  from UserProject s "
				+ "where s.project.id='"+grpcode+"')";
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}
	
	/**
	 * 获取某部门的不属于该组的人员
	 * @param deptname
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月5日
	 */
	public List findUserNotGroupByGrp(String deptname,String grpcode) {
		String sql = "from SysUser s where"
				+ " s.deptName='"+deptname+"'"
				+ " and s.userid not in "
				+ "(select s.userid  from UsrUsrgrp s "
				+ "where s.grpcode='"+grpcode+"')";
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}

	/**lhy 2014-4-24
	 * 根据组删除组与用户的关联
	 * @param grpcode
	 * @return
	 */
	public int removeGroupByUserid(String grpcode) {
		String sql = "delete  UsrUsrgrp usrUsrgrp where usrUsrgrp.grpcode='"
				+ grpcode + "'";
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(sql);
		return i;
	}
	
	/**lhy 2014-4-24
	 * 根据用户获取该用户所属组
	 * @param id
	 * @return
	 */
	public List getUserGropByUserId(String id) {
		  String hql="from UsrGroup group where group.grpcode in (select usrUsrgrp.grpcode from UsrUsrgrp usrUsrgrp where usrUsrgrp.userid='"+id+"')";
		  return  this.getHibernateTemplate().find(hql);
		 }

	/**
	 * lhy 2014-4-28
	 * 获取组code
	 * @param userid
	 * @return
	 */
	public List<String> getgroupcodeByUserid(String userid){
		String sql = " select usrUsrgrp.grpcode  from UsrUsrgrp  usrUsrgrp where usrUsrgrp.userid='"
				+ userid + "'";
		return this.getHibernateTemplate().find(sql);
	}


	/**
	 * By wjie5 2014-4-28
	 * 查询所有组别信息
	 * @return
	 */
	public List listALlGroup(){
		String hql = " from UsrGroup group ";
		return  this.getHibernateTemplate().find(hql);
	}
	
	/**By wjie5 2014-4-28
	 * 查询该组并且属于deptvalue部门的用户列表
	 * @param deptValue2 
	 * @param deptValue 
	 * @param userid
	 * @return
	 */
	public List findUserListByGrpDept(String grpcode,String deptvalue, String groupValue, String deptValue2) {
		String hql = " select s.username,s.userid from SysUser s where 1=1 ";
		if(deptvalue!=null&&!deptvalue.equals(deptValue2)){
			hql +=" and s.deptName like '%"+deptvalue+"%'"; 
		}
		if(grpcode!=null&&!grpcode.equals(groupValue)){
			hql +=" and s.userid in "+ "(select usrUsrgrp.userid from  UsrUsrgrp usrUsrgrp "
				+ "where usrUsrgrp.grpcode='"
				+ grpcode + "')"; 
		}
		try{
			List list= this.getHibernateTemplate().find(hql);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 判断该角色是否有该用户的信息
	 * @param userid
	 * @param role
	 * @return
	 */
	public boolean findRoleByUserid(String userid,String role){
		try{
			String sql = "select usrUsrgrp.grpcode from UsrUsrgrp usrUsrgrp where usrUsrgrp.userid='" + userid + "' and usrUsrgrp.grpcode='"+role+"'";
			List list = this.getHibernateTemplate().find(sql);
			if(list!=null && list.size()>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据角色用户删除信息
	 * @param userid
	 * @param role
	 */
	public void deleteInfoByRoleUserid(String userid,String role){
		String sql = "delete from usr_usrgrp usrUsrgrp where usrUsrgrp.c_userid='" + userid + "' and usrUsrgrp.c_grpcode='"+role+"'";
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
	}
}
