package cn.grgbanking.feeltm.menu.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;
@Repository  
@SuppressWarnings("unchecked") 
public class MenuOperateDao extends BaseDao<MenuOperate>{

	//返回page分页
	public Page getPage(int pageNum,int pageSize) {
		String hql="from MenuOperate as mo order by mo.operid ";
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	//分页显示记录
	public List getList(int pageNum, int pageSize) {
		String hql="from MenuOperate as mo order by mo.operid";
		List list=null;
		list=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize).getQueryResult();
		
		return list;
	}
	
	public MenuOperate getMenuOperate(String id) {
		try{
			return (MenuOperate) this.getHibernateTemplate().get(MenuOperate.class, id);
		}catch(Exception e){
			SysLog.debug(e.toString());
		}
		return null ;
	}
	
	public void addMenuOperate(MenuOperate menuOperate) {
		try{
			this.getHibernateTemplate().save(menuOperate);
		}catch(Exception e){
			SysLog.debug(e.toString());
		}
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 删除ID对应的项目以及下属项目
	 * 
	 * @idList 以,分隔的id号集合
	 */
	public int delAll(String idList) {
		String[] aryIds = StringUtils.split(idList, ",");
		String hql = "delete MenuOperate as dir WHERE "
					+ SqlHelper.fitStrInCondition("dir.operid", aryIds);
		int iCount =0;
		iCount+=this.getHibernateTemplate().bulkUpdate(hql);
		return iCount;
	}
	
	public void updateMenuOperate(MenuOperate menuOperate) {
		try{
			this.getHibernateTemplate().update(menuOperate);
		}catch(Exception e){
			SysLog.debug(e.toString());
		}
	}

}
