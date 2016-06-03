package cn.grgbanking.feeltm.custom.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.Custom;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("customDao")
public class CustomDao extends BaseDao<Custom> {

	public Page getPage(int pageNum, int pageSize, boolean hasRight) {
		String hql = "from Custom c where 1=1 ";
		// 无权限不给看
		if (!hasRight) {
			hql += " and 1=2 ";
		} else {
			hql += " order by c.creatDate desc";
		}
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				pageNum, pageSize);
	}

	public Page getCustomByCondition(Custom custom, int pageNum, int pageSize,
			boolean hasRight, String creatEndDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String hql = "from Custom cu where 1=1 ";
		if (!hasRight) {
			hql += " and 1=2 ";
		} else {
			if (custom != null && custom.getPrjList() != null
					&& !custom.getPrjList().equals("")) {
				hql += " and cu.prjList like '%" + custom.getPrjList() + "%' ";
			}
			if (custom != null && custom.getClient() != null && !custom.getClient().equals("")) {
				hql += " and cu.client like '%" + custom.getClient() + "%' ";
			}
			if (custom != null && custom.getMouthPiece() != null
					&& !custom.getMouthPiece().equals("")) {
				hql += " and cu.mouthPiece like '%" + custom.getMouthPiece()
						+ "%'";
			}
			if (custom != null && custom.getCreatDate() != null) {
				hql += " and to_date('" + sdf.format(custom.getCreatDate())
						+ "','yyyy-MM-dd')<=cu.creatDate";
			}
			if (creatEndDate != null) {
				hql += " and to_date('" + creatEndDate
						+ "','yyyy-MM-dd')>=cu.creatDate";
			}
			hql += " order by cu.creatDate desc";
		}
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				pageNum, pageSize);
	}

	public List<Custom> getListCustomById(String id) {
		String hql = "from Custom c where 1=1";
		if (id != null && !"".equals(id)) {
			hql += " and c.id='" + id + "'";
		}

		return this.getHibernateTemplate().find(hql);
	}
}
