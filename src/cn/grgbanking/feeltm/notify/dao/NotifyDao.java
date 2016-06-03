package cn.grgbanking.feeltm.notify.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.notify.domain.Notify;
import cn.grgbanking.feeltm.notify.domain.NotifyUser;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

/**
 * 2014-4-29
 * 通知
 * @author lhyan3
 *
 */
@Repository
public class NotifyDao extends BaseDao<Notify>{
	
	/**
	 * 获取所有的通知
	 * lhy 2014-4-29
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Notify> getAllNotifies(){
		List<Notify> list = new ArrayList<Notify>();
		String hql = "from Notify";
		list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * lhy 2014-4-30
	 * 分页查询
	 * @param hql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page findNotifyPage(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}
	
	/**
	 * lhy 2014-5-4
	 * 获取下一个流水号
	 * @return
	 */
	public String getNextNum(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(n.notifyNum) from Notify n");
		if(list!=null&&list.size()>=0){
		      String e=(String)list.get(0);
		      if(e!=null && !e.equals("") && e.contains("N")){
		    	  e=e.substring(1);
		    	  long d=Long.parseLong(e)+1;
		    	  DecimalFormat format=new DecimalFormat("N0000");
		  		  str=format.format(d);
		      } else {
		    	  str = "N0000";
		      }
		} else {
			str = "N0000";
		}
		return str;
	}

	/**
	 * lhy 2014-5-5
	 * @param id
	 * @return
	 */
	public Notify findByNotifyNum(String id) {
		String hql = "from Notify n where n.notifyNum='"+id+"'";
		List list = this.getHibernateTemplate().find(hql);
		if(null != list && list.size()>0){
			return (Notify) list.get(0);
		}
		return null;
	}

	/**
	 * lhy 2014-5-5
	 * @param notifyNum
	 * @param flag
	 */
	public List<NotifyUser> findSendersByNum(String notifyNum) {
		String hql = "from NotifyUser u where u.notifyNum='"+notifyNum+"'";
		List<NotifyUser> list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * lhy 2014-5-5
	 * @param str
	 * @return
	 */
	public int deleteNotifyByNum(String[] str) {
		String sql = "delete Notify n where "
				+ SqlHelper.fitStrInCondition("n.notifyNum", str);
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(sql);		
		return i;
	}

	public List<String> findEmailByNum(String notifyNum,int flag) {
		String hql = "select s.email from SysUser s where s.userid in (select n.userid from NotifyUser n where n.notifyNum='"+notifyNum+"' and n.flag="+flag+")";
		List<String> list = this.getHibernateTemplate().find(hql);
		return list;
	}

	/**
	 * 
	 * @param notifyNum
	 * @return
	 */
	public String findSenderEmailByNum(String notifyNum) {
		String hql = "select s.email from SysUser s where s.userid in (select n.sender from Notify n where n.notifyNum='"+notifyNum+"')";
		List<String> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public NotifyUser findNotifyUserByUN(String notifyNum, String string) {
		String hql =" from NotifyUser u where u.notifyNum='"+notifyNum+"' and u.userid='"+string+"'";
		List<NotifyUser> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param notifyNum
	 */
	public int  deleteNotifyUser(String notifyNum) {
		String hql = "delete from NotifyUser u where u.notifyNum='"+notifyNum+"'";
		int i = this.getHibernateTemplate().bulkUpdate(hql);
		return i;
	}

	/**
	 * 
	 * @param notifyNum
	 * @return
	 */
	public List<String> findUseridByNum(String notifyNum) {
		String hql = "select n.userid from NotifyUser n where n.notifyNum = '"+notifyNum+"'";
		List<String> list = this.getHibernateTemplate().find(hql);
		return list;
	}

}
