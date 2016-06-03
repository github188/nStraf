package cn.grgbanking.feeltm.login.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.MenuFunction;
import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.domain.SysLoginLog;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository  
@SuppressWarnings("unchecked") 
public class LoginDao  extends BaseDao<SysUser> {
	
//	获取用户组
	public String getTellerPurview(String tel) {
		try{
		String hql =" from UsrUsrgrp  where userid=? ";
		List list= this.getHibernateTemplate().find(hql.toString(),tel);
		String teller=null;
		if(list.size()>0){
			teller= ((UsrUsrgrp)list.get(0)).getGrpcode();
		}
		return teller;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 *  （非 Javadoc）
	 * @see cn.grgbanking.feeltm.login.dao.LoginDAO#getMenubyGrp(java.lang.String[])
	 * 1、根据组别代码，从组角色关系表中取得角色代号
	 * 2、根据角色代号，从角色功能权限表中取得功能ID
	 * 3、根据功能ID，从功能权限表中取得菜单ID
	 * 4、根据菜单ID，从菜单信息表是得到子节点数＝0的菜单信息
	 */
	public List getMenubyGrp(String[] grps){
		String hql = "FROM MenuInfo as  menuinfo WHERE menuinfo.menuid in "
			+"( select menufun.menuid from MenuFunction menufun where menufun.funcid in " 
			+" ( select rolefun.funcid from UsrRolefunc rolefun where rolefun.rolecode in "
			+"  ( select grprole.rolecode from UsrGrprole grprole where grprole.grpcode in (";
		if(grps.length>0)
		   hql+="'"+grps[0]+"'";
		else
			hql+="''";
		for(int i=1;i<grps.length;i++){
			hql += ", '"+grps[i]+"'"; 
		}		
		hql = hql+"))))"
		    +" and menuinfo.childnum=0 ORDER BY menuinfo.parentid,menuinfo.order desc";		
		List menuList = getHibernateTemplate().find(hql, null);		
		
		return menuList;
	}
	
//	得到父ID为0(ID为0，表示没有父节点)的所有子功能菜单的信息
	public List getMenubyModel(){
		String hql = "FROM MenuInfo as menuinfo WHERE menuinfo.parentid='0' order by menuinfo.order ";
		List menuList = this.getObjectList(hql);		
		return menuList;
	}
	
	//用户所对应菜单的功能保存到一个HashMap中，key=menuid,value为menuid所对应的所有操作ID，一个List对象
	public HashMap findMenuFuncMap(String userid) {
		HashMap hp=new HashMap();
		List list=this.findMenuFunction(userid);
		for (ListIterator it=list.listIterator();it.hasNext();) {
			List al=new ArrayList();
			MenuFunction mfun=(MenuFunction)it.next();
			int k=list.size();
			for (int i=0;i<k;i++) {
				MenuFunction mf=(MenuFunction)list.get(i);
				if (mf.getMenuid().equals(mfun.getMenuid())){
					try {
						al.add(this.getHibernateTemplate().get(MenuOperate.class,mf.getOperid()));
						
					} catch (DataAccessException e) {
						// TODO Auto-generated catch block
					}
				}
			}
			hp.put(mfun.getMenuid(),al);
		}
		return hp;
	}
	
	 /**
	   * 查询符合i_menuid和c_userid的用户有可视权限的菜单记录
	   */
	public List getMenuByAuth(String menuid, String userid){
	    String QUERY_MENU = "FROM MenuInfo as menuinfo WHERE (menuinfo.menuid in "
			+"( select menufun.menuid from MenuFunction menufun where menufun.funcid in " 
			+" ( select rolefun.funcid from UsrRolefunc rolefun where rolefun.rolecode in "
			+"  ( select grprole.rolecode from UsrGrprole grprole where grprole.grpcode in "
			+"   ( select usrgrp.grpcode from UsrUsrgrp usrgrp where usrgrp.userid=?)))) "
			+" or menuinfo.actionto='#') and menuinfo.menuid=?";
	    List menuList = getHibernateTemplate().find(QUERY_MENU, new String[]{userid, menuid});		
		return menuList;
	  }
	

	  /**
	  * 查询符合i_parentid和c_userid的用户有可视权限的菜单记录,并且是有效功能的最后一级
	  * 2006-09-12 修改QUERY_MENU_1： 去掉135行and menuinfo.actionto!='#'
	  */
	public List getMenuLeafByParentAuth(String menuid, String userid){
	    String QUERY_MENU_1 = "FROM MenuInfo menuinfo WHERE (menuinfo.menuid in "
			+"( select menufun.menuid from MenuFunction menufun where menufun.funcid in " 
			+" ( select rolefun.funcid from UsrRolefunc rolefun where rolefun.rolecode in "
			+"  ( select grprole.rolecode from UsrGrprole grprole where grprole.grpcode in "
			+"   ( select usrgrp.grpcode from UsrUsrgrp usrgrp where usrgrp.userid=?)))) "
			+" ) and menuinfo.parentid=? ";
	    List menuList = getHibernateTemplate().find(QUERY_MENU_1, new String[]{userid, menuid});		
		return menuList;	  
	}
	

//	获取角色
	public List getTellerOrleList(String grpcode) {
		StringBuffer hql = new StringBuffer(
				"from UsrGrprole as t1 where t1.grpcode=? ");
		List list = this.getHibernateTemplate().find(hql.toString(),
				 grpcode );
		
		return list;
	}
	
//	根据操作编号得出操作员所属的用户组代码
	public String[] searchUsrGrp(String userid){
		String hql = "FROM UsrUsrgrp  usrgrp WHERE usrgrp.userid=? ORDER BY usrgrp.grpcode asc";
		String[]userids={userid};
		List grpList = this.getObjectList(hql, userids);
		if(grpList==null)
			return null;
		String[] usrGrps = new String[grpList.size()];
		int k=grpList.size();
		for(int i=0;i<k;i++){
			usrGrps[i] = ((UsrUsrgrp)(grpList.get(i))).getGrpcode();
		}
		return usrGrps;
	}
	
	  /**
	   * 查询符合i_parentid和c_userid的用户有可视权限的菜单记录
	   */
	public List getMenuByParentAuth(String menuid, String userid){	  
	    String QUERY_SUBMENUS = "FROM MenuInfo   menuinfo WHERE (menuinfo.menuid in "
			+"( select menufun.menuid from MenuFunction menufun where menufun.funcid in " 
			+" ( select rolefun.funcid from UsrRolefunc rolefun where rolefun.rolecode in "
			+"  ( select grprole.rolecode from UsrGrprole grprole where grprole.grpcode in "
			+"   ( select usrgrp.grpcode from UsrUsrgrp usrgrp where usrgrp.userid=?)))) "
			+" or menuinfo.actionto='#') and menuinfo.parentid=?  order by menuinfo.order asc";
	    List menuList = getHibernateTemplate().find(QUERY_SUBMENUS, new String[]{userid, menuid});		
		return menuList;	  
	}	  
	
//	返回page分页
	public Page getPage(SysLoginLog sysLoginLog,int pageNum,int pageSize)
	{
		String hql = "FROM SysLoginLog loginlog WHERE 1=1 ";
		if(sysLoginLog!=null){
			if(sysLoginLog.getUserid()!=null && !sysLoginLog.getUserid().equals("")){
				hql += " and loginlog.userid like '%"+sysLoginLog.getUserid().trim()+"%' ";
			}
		}
		hql += " order by loginlog.logintime desc, loginlog.logouttime desc ";
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
//	根据用户ID取得功能权限表的所有对象
	public List findMenuFunction(String userid) {
		String hql="from MenuFunction as mf where mf.funcid in " +
		"( select rolefun.funcid from UsrRolefunc as rolefun where rolefun.rolecode in " +
		" ( select grprole.rolecode from UsrGrprole as grprole where grprole.grpcode in " +
		" ( select usrgrp.grpcode from UsrUsrgrp as usrgrp where usrgrp.userid=?))) order by mf.operid";
		List list=this.getHibernateTemplate().find(hql,userid);
		return list;
	}
	
//////////////////////////////////////////////////////////////////////////////////


	public void logout(String userid){
		String hql = "FROM SysLoginLog as loginlog WHERE loginlog.userid=? and loginlog.type='web端' ORDER BY loginlog.logintime desc";
		List qList = getHibernateTemplate().find(hql, userid);
		if(qList==null)
			return;
		Iterator qIterat = qList.iterator();
		if (qIterat.hasNext()) {
			SysLoginLog loginlog = (SysLoginLog)qIterat.next();
			loginlog.setLogouttime(new Date());
			this.getHibernateTemplate().update(loginlog);
		}
	}

	/**
	 * 根据用户获取该用户查看数据的级别
	 * @param hql
	 * @param userid
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	public Integer getGrpLevel(String hql, String userid) {
		List<Object> list = this.getHibernateTemplate().find(hql, userid);
		Integer level = null;
		if(list!=null && list.size()>0){
			String str = list.get(0).toString().trim();
			level = Integer.parseInt(str);
		}
		return level;
	}
	
	/*	
	
//	根据角色获取显示的功能
	public List getMenuByRole(String roleid){
		String hql = "FROM MenuReadRole as  menu WHERE menu.roleid=? ";
		hql += " order by menu.ordinal ";
		List menuList = getHibernateTemplate().find(hql, new String[]{roleid});		
		return menuList;
	}
	*/
	
	/**
	 * 根据用户ID判断是否为领导
	 * @param userId 用户ID
	 * @return true:领导 /false:非领导
	 */
	public boolean getLeaderByUserId(String userId){
		String hql = "from SysUser u where u.userid in (select g.userid from UsrUsrgrp g "
				+ " where g.userid= '" + userId + "' and g.grpcode='"+UserRoleConfig.leader+"')";
		List<SysUser> list = this.getHibernateTemplate().find(hql);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return true;
		}
		return false;
	}
}
