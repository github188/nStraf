package cn.grgbanking.feeltm.client.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.client.domain.ClientUpload;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class ClientUploadDao extends BaseDao<ClientUpload>{

	/**
	 * 获取列表
	 * @param hql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public Page getpage(String hql, int pageNum, int pageSize) {
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}

	/**
	 * 获取最新版本
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	@SuppressWarnings("unchecked")
	public Object getNewVersion(String hql) {
		List<Object> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
