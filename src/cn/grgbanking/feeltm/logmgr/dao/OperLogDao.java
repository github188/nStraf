package cn.grgbanking.feeltm.logmgr.dao;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysOperLog;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository
public class OperLogDao extends BaseDao<SysOperLog> {
	public Page getPage(HttpServletRequest request, SysOperLog operLog,
			int pageNum, int pageSize) {
		// 如果没有输入查询条件，那么userid，operid，beginDate，endDate都为空。但还是要根据当前用户的orgfloor进行查询
		String userid = operLog.getUserid();
		String operid = operLog.getOperid();
		String orgfloor = operLog.getOrgfloor();

		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		userid = (userid == null || userid.equals("")) ? null : userid;
		operid = (operid == null || operid.equals("")) ? null : operid;
		orgfloor = (orgfloor == null || orgfloor.equals("")) ? null : orgfloor;
		beginDate = (beginDate == null) || beginDate.equals("") ? null
				: beginDate;
		endDate = (endDate == null) || endDate.equals("") ? null : endDate;

		String hql = " From SysOperLog where 1=1 ";
		if (userid != null) {
			hql += " and userid ='" + userid + "'";
		}
		if (orgfloor != null) {
			hql += " and orgfloor like '%" + orgfloor + "%' ";
		}
		if (beginDate != null) {
			hql += " and logtime >=to_date('" + beginDate
					+ "','yyyy-MM-dd HH24:mi:ss') ";
		}
		if (endDate != null) {
			hql += " and logtime <to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') ";
		}

		Page page = null;

		page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,
				pageNum, pageSize);

		return page;
	}

	// 删除操作日志
	public int deleteInfo(String[] chkDelete) {
		//String[] var = new String[chkDelete.length];
		//int j = 0;
		for (int i = 0; i < chkDelete.length; i++) {

			String sql = "delete from SysOperLog  where id='" + chkDelete[i]
					+ "'";
			this.getHibernateTemplate().bulkUpdate(sql);
		}

		return chkDelete.length;
	}
}