package cn.grgbanking.feeltm.prjcontract.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.PrjContract;
import cn.grgbanking.feeltm.domain.PrjContractPayment;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("prjContractDao")
public class PrjContractDao extends BaseDao<PrjContract> {
	public Page getPage(int pageNum, int pageSize, boolean hasRight,
			String updateMan) {
		String hql = "from PrjContract prjContract where 1=1 ";
		if (!hasRight) {
			hql += " and 1=2 ";
		} else {
			hql += " order by prjContract.startDate desc";
		}
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				pageNum, pageSize);
	}

	public Page getPrjContractByCondition(String prjName, String prjManager,
			String prjStatus, Date startDate, Date endDate, int pageNum,
			int pageSize, boolean hasRight, String updateMan) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String hql = "from PrjContract prj where 1=1 ";
		if (!hasRight) {
			hql += " and 1=2 ";
		} else {
			if (prjName != null && !prjName.equals("")) {
				hql += " and prj.prjName like '%" + prjName + "%'";
			}
			if (prjManager != null && !prjManager.equals("")) {
				hql += " and prj.prjManager like '%" + prjManager + "%'";
			}
			if (prjStatus != null && !prjStatus.equals("")) {
				hql += " and prj.status='" + prjStatus + "'";
			}
			if (startDate != null) {
				hql += " and to_date('" + sdf.format(startDate)
						+ "','yyyy-MM-dd')<=prj.startDate";
			}
			if (endDate != null) {
				hql += " and to_date('" + sdf.format(endDate)
						+ "','yyyy-MM-dd')>=prj.endDate";
			}
			hql += " order by prj.startDate desc";
		}
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(
				hql, pageNum, pageSize);
		return page;
	}

	public List<PrjContract> getListPrjContractById(String id) {
		String hql = "from PrjContract p where 1=1";
		if (id != null && !"".equals(id)) {
			hql += " and p.Id='" + id + "'";
		}
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * @param contractId
	 *            合同ID
	 * @return list 回款记录 lping1 2014年6月24日
	 */
	public List<PrjContractPayment> getListPrjPaymentById(String contractId) {
		String hql = "from PrjContractPayment pay where 1=1";
		if (contractId != null && !"".equals(contractId)) {
			hql += " and pay.contractId='" + contractId
					+ "' order by pay.upDateTime desc";
		}
		return this.getHibernateTemplate().find(hql);
	}

	
	/**
	 * 根据年月获取该年到目前月份的合同总额
	 */
	public BigDecimal prjContractSum(String year,String nowDateStr,String lastDay){
		StringBuilder sbdHql = new StringBuilder();
		String startDate = year+"-01-01";
		String endDate = nowDateStr;
		sbdHql.append("select sum(p.total) from PrjContract p where 1=1");
		sbdHql.append(" and p.signDate >= to_date('"+startDate+"','yyyy-mm-dd')");
		sbdHql.append(" and p.signDate <= to_date('"+endDate+"','yyyy-mm-dd')");
		List result = this.getHibernateTemplate().find(sbdHql.toString());
		if (result.get(0)!=null) {
			Object obj = result.get(0);
			return (BigDecimal) obj;
		}
		return new BigDecimal(0);
	}
}
