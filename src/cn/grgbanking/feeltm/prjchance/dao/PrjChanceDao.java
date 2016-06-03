package cn.grgbanking.feeltm.prjchance.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.PrjChance;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("prjChanceDao")
public class PrjChanceDao extends BaseDao<PrjChance> {
	public Page getPage(int pageNum, int pageSize) {
		String hql = "from PrjChance prj where 1=1 order by prj.creatDate desc";
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
	}

	public Page getPrjChanceByCondition(String prjName, String client,String followMan, int pageNum,int pageSize) {
		String hql = "from PrjChance prj where 1=1 ";
		if (prjName != null && !prjName.equals("")) {
			hql += " and prj.prjName like '%" + prjName + "%'";
		}
		if (client != null && !client.equals("")) {
			hql += " and prj.client like '%" + client + "%'";
		}
		if (followMan != null && !followMan.equals("")) {
			hql += " and prj.followMan like '%" + followMan + "%'";
		}
		hql += " order by prj.creatDate desc";
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(
				hql, pageNum, pageSize);
		return page;
	}

	public List<PrjChance> getListPrjChanceById(String id) {
		String hql = "from PrjChance p where 1=1";
		if (id != null && !"".equals(id)) {
			hql += " and p.id='" + id + "'";
		}
		return this.getHibernateTemplate().find(hql);
	}

	public List<PrjChance> getPrjChanceListByCondition(PrjChance prj) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String prjName=prj.getPrjName();
		String client=prj.getClient();
		String followMan=prj.getFollowMan();
		String hql = "from PrjChance prj where 1=1 ";
		if (prjName != null && !prjName.equals("")) {
			hql += " and prj.prjName like '%" + prjName + "%'";
		}
		if (client != null && !client.equals("")) {
			hql += " and prj.client like '%" + client + "%'";
		}
		if (followMan != null && !followMan.equals("")) {
			hql += " and prj.followMan like '%" + followMan + "%'";
		}
		hql += " order by prj.creatDate desc";
		return getHibernateTemplate().find(hql);
	}

	


	public Page getPrjChanceByCondition(String prjName, String client,
			String followMan, String area, String province,
			String clientManager, String clientType, String prjStage,
			String prjResult, int pageNum, int pageSize) {
		String hql = "from PrjChance prj where 1=1 ";
		if (prjName != null && !prjName.equals("")) {
			hql += " and prj.prjName like '%" + prjName + "%'";
		}
		if (client != null && !client.equals("")) {
			hql += " and prj.client like '%" + client + "%'";
		}
		if (followMan != null && !followMan.equals("")) {
			hql += " and prj.followMan like '%" + followMan + "%'";
		}
		
		if (area != null && !area.equals("")) {
			hql += " and prj.area like '%" + area + "%'";
		}
		if (province != null && !province.equals("")) {
			hql += " and prj.province like '%" + province + "%'";
		}
		if (clientManager != null && !clientManager.equals("")) {
			hql += " and prj.clientManager like '%" + clientManager + "%'";
		}
		if (clientType != null && !clientType.equals("")) {
			hql += " and prj.clientType like '%" + clientType + "%'";
		}
		if (prjStage != null && !prjStage.equals("")) {
			hql += " and prj.prjStage like '%" + prjStage + "%'";
		}
		if (prjResult != null && !prjResult.equals("")) {
			hql += " and prj.prjResult like '%" + prjResult + "%'";
		}
		hql += " order by prj.creatDate desc";

		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(
				hql, pageNum, pageSize);
		return page;
	}

	public List<PrjChance> getPrjChanceByCondition(String prjName,
			String client, String followMan, String area, String province,
			String clientManager, String clientType, String prjStage,
			String prjResult) {
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String hql = "from PrjChance prj where 1=1 ";
		if (prjName != null && !prjName.equals("")) {
			hql += " and prj.prjName like '%" + prjName + "%'";
		}
		if (client != null && !client.equals("")) {
			hql += " and prj.client like '%" + client + "%'";
		}
		if (followMan != null && !followMan.equals("")) {
			hql += " and prj.followMan like '%" + followMan + "%'";
		}
		
		if (area != null && !area.equals("")) {
			hql += " and prj.area like '%" + area + "%'";
		}
		if (province != null && !province.equals("")) {
			hql += " and prj.province like '%" + province + "%'";
		}
		if (clientManager != null && !clientManager.equals("")) {
			hql += " and prj.clientManager like '%" + clientManager + "%'";
		}
		if (clientType != null && !clientType.equals("")) {
			hql += " and prj.clientType like '%" + clientType + "%'";
		}
		if (prjStage != null && !prjStage.equals("")) {
			hql += " and prj.prjStage like '%" + prjStage + "%'";
		}
		if (prjResult != null && !prjResult.equals("")) {
			hql += " and prj.prjResult like '%" + prjResult + "%'";
		}
		hql += " order by prj.creatDate desc";
		return getHibernateTemplate().find(hql);
	}

}
