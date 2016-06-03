package cn.grgbanking.feeltm.hols.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.hols.domain.UserHols;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

/**
 * 
 * @author lhyan3 2014-5-12
 *
 */
@Repository
public class UserHolsDao extends BaseDao<UserHols>{

	/**
	 * 列表
	 * @param hql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page findUserholsPage(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}

	/**
	 * lhy 2014-5-13
	 * @param userid
	 * @return
	 */
	public boolean existUser(String userid) {
		String hql = "from UserHols h where h.userid='"+userid+"' and h.flag=0";
		List<UserHols> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}

	public void addHolsList(List<UserHols> holsList) {
		for(UserHols h:holsList){
			this.getHibernateTemplate().save(h);
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserHols> findByIds(String[] holsids) {
		String hql = "from UserHols u where "+SqlHelper.fitStrInCondition("u.id", holsids);
		List<UserHols> userHols = this.getHibernateTemplate().find(hql);
		return userHols;
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> findUserByIds(String[] holsids) {
		String hql = "from SysUser s where s.userid in "
				+ "(select u.userid from UserHols u where "+SqlHelper.fitStrInCondition("u.id", holsids)+")";
		List<SysUser> sysUsers = this.getHibernateTemplate().find(hql, holsids);
		return sysUsers;
	}

	@SuppressWarnings("unchecked")
	public UserHols findByidAndUser(String holsId, String userid) {
		String hql = "from UserHols u where u.id='"+holsId+"' and u.userid='"+userid+"' and u.flag=0";
		List<UserHols> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public UserHols findByUserid(String userid) {
		String hql = "from UserHols h where h.userid='"+userid+"'";
		List<UserHols> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public Object[] getRestTime(String userid,int type) {
		String hql = "select h.yearholsTime,h.deferredTime from UserHols h where h.userid='"+userid+"'";
		List<Object[]> times = this.getHibernateTemplate().find(hql);
		if(times!=null && times.size()>0){
			return times.get(0);
		}
		return null;
	}

	public List<SysUser> getNotExistUsers(String userids, int type, String deptcode, String grpcode) {
		String hql = " from SysUser u where 1=1 ";
		switch (type){
			case 0:
				if(null != userids && !"".equals(userids)){
					userids = userids.replaceAll(",", "','");
					userids = "'"+userids.substring(0, userids.length()-2);
					hql += "  and u.userid in ("+userids+") and u.userid not in(select distinct(h.userid) from UserHols h where h.flag=0)";
				}else{
					return null;
				}
				break;
			case 1: 
				hql += " and u.deptName ='"+deptcode+"' and u.userid not in (select distinct(h.userid) from UserHols h where h.flag=0)";
				break;
			case 2: 
				hql += " and u.userid in (select usrUsrgrp.userid from  UserProject usrUsrgrp where usrUsrgrp.project.id='"+ grpcode + "' and usrUsrgrp.userid not in (select distinct(h.userid) from UserHols h where h.flag=0))";
				break;
			default: return null;
		}
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		return list;
	}


}
