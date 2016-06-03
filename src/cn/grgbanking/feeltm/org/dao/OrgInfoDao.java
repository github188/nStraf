package cn.grgbanking.feeltm.org.dao;

import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.OrgInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.org.service.OrgInfoService;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
@SuppressWarnings("unchecked")
public class OrgInfoDao extends BaseDao<OrgInfo> {

	public String findParentName(String parentid) throws DataAccessException {
		String orgname = "";
		Vector vt = new Vector();
		if (parentid.equals(OrgInfoService.TOP_PARENTID))
			vt.add("\\");
		else {
			while (!parentid.equals(OrgInfoService.TOP_PARENTID)
					&& parentid.trim().length() > 0) {
				List list = getHibernateTemplate()
						.find(
								"select info.orgname  from OrgInfo info where info.orgid=?",
								parentid);
				for (ListIterator it = list.listIterator(); it.hasNext();) {
					vt.add(it.next());
				}
				parentid = this.findParentid(parentid);
			}
		}
		for (int i = vt.size(); i > 0; i--) {
			if (i == vt.size())
				orgname += vt.get(i - 1).toString();
			else
				orgname += "->" + vt.get(i - 1);
		}

		return orgname;
	}

	public List getOrgInfoChildList(String parentid) throws DataAccessException {
		List list = this
				.getHibernateTemplate()
				.find(
						"from OrgInfo as info where info.parentid=? order by info.orgid",
						parentid);

		return list;
	}

	public int getChildLevel(OrgInfo orgInfo) throws DataAccessException {
		int i = 0;
		if (!orgInfo.getParentid().equals(OrgInfoService.TOP_PARENTID)) {
			List list = this
					.getHibernateTemplate()
					.find(
							"select info.level  from cn.grgbanking.feeltm.domain.OrgInfo info where info.orgid=?",
							orgInfo.getParentid());

			if (!list.isEmpty())
				i = ((Integer) list.get(0)).intValue() + 1;
		}
		return i;
	}

	/*
	 * 根据orgid得到一个机构对象
	 * 
	 * @see
	 * cn.grgbanking.feeltm.org.dao.OrgInfoDAO#getOrgInfoList(java.lang.String)
	 */
	public OrgInfo getOrgInfoByOrgId(String orgId) throws DataAccessException {
		// 已修改//
		String sql = "  from OrgInfo  info where info.orgid='"+orgId+"' order by info.orgid";
		OrgInfo mif = null;
		List list = this.getHibernateTemplate().find(sql);
		if (list.size() >= 0)
			mif = (OrgInfo) list.get(0);
		return mif;
	}

	public int removeOrgInfo(String id) {
		try {
			String hql = "delete from OrgInfo as bt where bt.orgid='" + id
					+ "'";
			this.getHibernateTemplate().bulkUpdate(hql);

			return 1;
		} catch (Exception e) {
			SysLog.error(e);
			return -1;
		}

	}

	public String findParentid(String orgid) throws DataAccessException {
		String parentId = "";
		List list = getHibernateTemplate().find(
				"select info.parentid  from OrgInfo info where info.orgid=?",
				orgid);
		if (!list.isEmpty())
			parentId = (String) list.get(0);
		return parentId;
	}

	public int getCountChild(String parentid) throws DataAccessException {
		int i = 0;
		List list = this
				.getHibernateTemplate()
				.find(
						"select count(info.orgid)  from OrgInfo info where info.parentid=?",
						parentid);
		if (!list.isEmpty())
			i = ((Long) list.get(0)).intValue();

		return i;

	}

	public int getChildOrder(OrgInfo orgInfo) throws DataAccessException {
		int i = 1;
		String hql = " select max(info.order)  from OrgInfo info where info.parentid=?";
		Object obj[] = { orgInfo.getParentid() };
		List list = this.getHibernateTemplate().find(hql, obj);
		if (list.size() > 0)
			if (list.get(0) != null)
				i = ((Integer) list.get(0)).intValue() + 1;

		return i;
	}

	public String getOrgSelNextTree(String orgId) {
		StringBuffer sbfXml = new StringBuffer();
		String sql = null;

		sql = " FROM OrgInfo  info where info.orgid='" + orgId
				+ "' order by info.order";

		List topList = this.getHibernateTemplate().find(sql);
		int level = 0;
		for (ListIterator it = topList.listIterator(); it.hasNext();) {
			OrgInfo info = (OrgInfo) it.next();
			sbfXml.append("<option ");
			sbfXml.append("value='");
			if (info.getOrgid() != null)
				sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
			sbfXml.append("'>");
			sbfXml.append(info.getOrgname());
			sbfXml.append("</option>");

			if (info.getChildnum() > 0) {
				AppendNextChildSel(info.getOrgid(), sbfXml, level);
			}
		}
		return sbfXml.toString();
	}

	private void AppendNextChildSel(String orgid, StringBuffer sbfXml, int level) {
		level++;
		List topList = this.getOrgInfoList(orgid);

		for (ListIterator it = topList.listIterator(); it.hasNext();) {
			OrgInfo info = (OrgInfo) it.next();

			if (info.getChildnum() > 0) {// 褰搃nfo鐨勬湁瀛愮綉鐐归偅涔堟墠鍔犲叆鍒皊bfXml
				sbfXml.append("<option ");
				sbfXml.append("value='");
				sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
				sbfXml.append("'>");
				sbfXml.append(StringUtils.repeat("&nbsp;", level));
				sbfXml.append("|--");
				sbfXml.append(info.getOrgname());
				sbfXml.append("</option>");
			} else if (info.getChildnum() == 0
					&& info.getOrgLevel() != null
					&& info.getOrgLevel().trim().equals("1")
					&& (info.getIfreckon() != null && (info.getIfreckon()
							.trim().equals("1") || info.getIfreckon().trim()
							.equals("2")))) {
				sbfXml.append("<option ");
				sbfXml.append("value='");
				sbfXml.append(StringEscapeUtils.escapeXml(info.getOrgid()));
				sbfXml.append("'>");
				sbfXml.append(StringUtils.repeat("&nbsp;", level));
				sbfXml.append("|--");
				sbfXml.append(info.getOrgname());
				sbfXml.append("</option>");
			}

			/*
			 * if (info.getChildnum() > 0) AppendPartChildSel(info.getOrgid(),
			 * sbfXml, level);
			 */
		}
	}

	public List getOrgInfoList(String parentid) throws DataAccessException {
		String hql = "from OrgInfo as info where info.parentid='" + parentid
				+ "' order by info.orgid";
		List list = this.getHibernateTemplate().find(hql);

		return list;
	}
}
