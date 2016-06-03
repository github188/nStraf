package cn.grgbanking.feeltm.tmlInfo.dao;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.TmlInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class TmlInfoDao extends BaseDao<TmlInfo> {
	public Page findTmlInfoPage(TmlInfo form, int pageNum, int pageSize) {
		String hql = "from TmlInfo as tml where  tml.termid like ? and tml.termtype like ?";
		Page page = null;

		Object[] obj = new Object[2];
		Type[] types = new Type[2];
		if (form.getTermid() != null)
			obj[0] = "%" + form.getTermid().trim() + "%";
		else
			obj[0] = "%%";
		types[0] = Hibernate.STRING;

		if (form.getTermtype() != null && form.getTermtype() != "")
			obj[1] = form.getTermtype().trim();
		else
			obj[1] = "%%";
		types[1] = Hibernate.STRING;

		page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				obj, types, pageNum, pageSize);

		return page;
	}
}
