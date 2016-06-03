package cn.grgbanking.feeltm.leave.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.leave.domain.Leave;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class LeaveDao extends BaseDao<Leave>{

	public Page findAll(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Leave findLeaveById(String id) {
		String hql = "from Leave l where l.id='"+id+"'";
		List<Leave> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public void updateIds(String substring) {
		String ids[] = substring.split(",");
		for(int i = 0;i<ids.length;i++){
			Leave leave = findLeaveById(ids[i]);
			leave.setUpdateTime(new Date());
			leave.setUpdateUser(leave.getUsername());
			leave.setStatus("待审批");
			updateObject(leave);
		}
		
	}

	public void removeIds(String substring) {
		String ids[] = substring.split(",");
		String hql = "delete from Leave l where "+SqlHelper.fitStrInCondition("l.id", ids);
		this.getHibernateTemplate().bulkUpdate(hql);
	}

}
