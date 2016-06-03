package cn.grgbanking.feeltm.holiday.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Holiday;
import cn.grgbanking.framework.dao.BaseDao;

/**
 * 节假日配置
 * @author lhyan3
 * 2014年8月11日
 */
@Repository
public class HolidayDao extends BaseDao<Holiday>{

	/**
	 * 执行hql语句
	 * @param hql
	 * @return
	 * lhyan3
	 * 2014年8月11日
	 */
	@SuppressWarnings("unchecked")
	public List<Holiday> findByHql(String hql) {
		return this.getHibernateTemplate().find(hql);
	}
	
	public void deleteByYear(String year){
		String hql="delete from Holiday h where h.checkYear='"+year+"'";
		this.getHibernateTemplate().bulkUpdate(hql);
	}

}
