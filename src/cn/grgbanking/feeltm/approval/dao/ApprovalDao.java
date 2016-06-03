package cn.grgbanking.feeltm.approval.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
public class ApprovalDao extends BaseDao<ApprovalRecord>{

	/**
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ApprovalRecord> getRecordByName(String name) {
		String hql = "from ApprovalRecord r where r.approvalName='"+name+"' order by r.approvalTime desc";
		List<ApprovalRecord> records = new ArrayList<ApprovalRecord>();
		records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 根据name值获取所有审批记录信息
	 * @param name
	 * @return
	 */
	public List getMoreRecordByName(String name) {
		String hql = "select * from (select distinct t.c_recodename,t.c_opinion,t.c_result,t.c_approvaluser,to_char(t.d_approvaltime,'yyyy-mm-dd hh24:mi:ss')d_approvaltime from OA_ApprovalRecord t";
		hql+=" where t.c_approvalname in ("+name+")) order by d_approvaltime";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		return list;
	}
	
	/**
	 * 根据name值得出审核人信息
	 * @param name
	 * @return
	 */
	public String getGroupmanageInfo(String name){
		String hql = "select c_approvaluser from OA_ApprovalRecord where c_approvalname='"+name+"' order by c_id desc";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}
}
