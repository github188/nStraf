package cn.grgbanking.feeltm.menu.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.MenuFunction;
import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.feeltm.util.StringUtil;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
@SuppressWarnings("unchecked")
public class MenuInfoDao extends BaseDao<MenuInfo> {
	public static final String ITEM_SEPARATOR = ".";
	// ��Ŀ¼·��
	public static final String ROOT = "\\";
	// �����Ŀ�ĸ�ID
	public static final String TOP_PARENT_ID = "0";

	// --------------------------------------------------------------------------------
	/**
	 * ���ظ�ID���Ҫ���ֱ������Ŀ����
	 * 
	 * @parentId ����ĿID
	 */
	public List queryChildList(String parentId) {
		String hql = "FROM MenuInfo as menu WHERE menu.parentid=? ORDER BY menu.order asc";
		List childList = getHibernateTemplate().find(hql, parentId);
		return childList;
	}

	// -----------------------------------------------------------------------
	/**
	 * 返回网页导航条
	 */
	public String getNavigation(String itemId) {
		ArrayList aryPath = new ArrayList();
		MenuInfo menuInfo = findById(itemId);
		while (menuInfo != null) {
			aryPath.add(menuInfo);
			if (menuInfo.getParentid().equals(TOP_PARENT_ID))
				break;
			menuInfo = findById(menuInfo.getParentid());
		}
		int iSize = aryPath.size();
		if (iSize == 0)
			return ROOT;

		StringBuffer sbfPath = new StringBuffer();
		for (int i = iSize - 1; i > 0; i--) {
			menuInfo = (MenuInfo) aryPath.get(i);
			// sbfPath.append("<a href=\"javascript:ComeIn('");
			// sbfPath.append(menuInfo.getMenuid());
			// sbfPath.append("')\">");
			// sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo.getMenuitem()));
			sbfPath.append(menuInfo.getMenuitem());
			// sbfPath.append("</a><b>-&gt;</b>");
			sbfPath.append("\\");
		}
		menuInfo = (MenuInfo) aryPath.get(0);
		// sbfPath.append("<a href=\"javascript:ComeIn('");
		// sbfPath.append(menuInfo.getMenuid());
		// sbfPath.append("')\">");
		sbfPath.append(menuInfo.getMenuitem());
		// sbfPath.append("</a>");
		return sbfPath.toString();
	}

	// --------------------------------------------------------------------------------
	/**
	 * 根据主键找对象
	 * 
	 * @id 项目主键
	 */
	public MenuInfo findById(String id) {
		try {
			if (id.equals("0"))
				return null;

			return (MenuInfo) this.getHibernateTemplate().get(MenuInfo.class,
					id);
		} catch (Exception exp) {
			return null;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * 取得子节点数目
	 */
	public int getChildCount(String parentId) {
		String hql = "select count(*) FROM MenuInfo as menu WHERE menu.parentid=?";
		List list = getHibernateTemplate().find(hql, parentId);

		if (list.isEmpty())
			return 0;

		if (list.get(0) == null)
			return 0;

		// return ((Integer) list.get(0)).intValue();
		int i = Integer.parseInt(list.get(0).toString());
		return i;

	}

	// -----------------------------------------------------------------------
	/**
	 * @return 0:存在相同KEY的兄弟节点 1：新增成功
	 */
	public int addItem(MenuInfo menuInfo) {

		int iCount = countByKey(menuInfo.getParentid(), menuInfo.getMenuid());
		if (iCount > 0) {
			return 0;
		}

		menuInfo.setChildnum(0);
		menuInfo.setOrder(getMaxOrderNum(menuInfo.getParentid()) + 1);
		this.getHibernateTemplate().save(menuInfo);

		// 更新父节点的孩子数
		refreshChildCount(menuInfo.getParentid());
		return 1;
	}

	public void updateMenuOper(String menuid, String[] menuOperids) {

		String hql = "FROM MenuFunction as menufun WHERE menufun.menuid=? ORDER BY menufun.operid asc";
		List menuOperLst = getHibernateTemplate().find(hql, menuid);

		ArrayList<String> menuOperLstNow = new ArrayList<String>();
		if (menuOperids != null) {
			for (int i = 0; i < menuOperids.length; i++) {
				menuOperLstNow.add(menuOperids[i]);
			}
		}

		Iterator itr = menuOperLst.iterator();
		while (itr.hasNext()) {
			MenuFunction menufun = (MenuFunction) itr.next();
			if (menuOperLstNow.contains(menufun.getOperid())) {
				menuOperLstNow.remove(menufun.getOperid());
			} else {

				this.getHibernateTemplate().delete(menufun);
			}
		}

		// ����Ĺ���
		Iterator itrNow = menuOperLstNow.iterator();
		while (itrNow.hasNext()) {
			String operid = (String) itrNow.next();
			MenuFunction menuFunc = new MenuFunction();
			menuFunc.setOperid(operid);
			menuFunc.setMenuid(menuid);

			this.getHibernateTemplate().save(menuFunc);
		}

	}

	public MenuInfo findByMenuId(String menuid) {
		String hql = "from MenuInfo as menufun where menufun.menuid=? ";
		List menuList = getHibernateTemplate().find(hql, menuid);
		MenuInfo menuInfo = (MenuInfo) menuList.get(0);
		return menuInfo;
	}

	// --------------------------------------------------------------------------------
	/**
	 * 删除ID对应的项目以及下属项目
	 * 
	 * @idList 以,分隔的id号集合
	 */
	public int delAll(String idList) {
		String[] aryIds = StringUtils.split(idList, ",");
		int iCount = 0;
		for (int i = 0; i < aryIds.length; i++) {
			MenuInfo menuInfo = findById(aryIds[i]);

			String[] aryDelIds = StringUtils.split(queryIdList(aryIds[i]), ",");
			String hql = "delete MenuInfo as menu WHERE "
					+ SqlHelper.fitStrInCondition("menu.menuid", aryDelIds);

			iCount += this.getHibernateTemplate().bulkUpdate(hql);
			// 更新父项目的孩子数
			refreshChildCount(menuInfo.getParentid());
		}// for

		return iCount;
	}

	public int reorderItems(String idList) {
		String[] childId = StringUtil.toArray(idList, ",");
		for (int i = 0; i < childId.length; i++) {
			MenuInfo menuinfo = findById(childId[i]);
			menuinfo.setOrder(i + 1);

			this.getHibernateTemplate().update(menuinfo);
		}
		return childId.length;
	}

	// -------------------------------------------------------------------------------
	/**
	 * ���ظ���Ŀ�Ĺؼ���·��
	 */
	public String getPath(String itemId) {
		ArrayList<String> aryPath = new ArrayList<String>();
		MenuInfo menuInfo = findById(itemId);
		while (menuInfo != null) {
			aryPath.add(menuInfo.getMenuid());
			if (menuInfo.getParentid().equals(TOP_PARENT_ID))
				break;
			menuInfo = findById(menuInfo.getParentid());
		}

		int iSize = aryPath.size();
		if (iSize == 0)
			return "";

		StringBuffer sbfPath = new StringBuffer();
		for (int i = iSize - 1; i > 0; i--) {
			sbfPath.append(aryPath.get(i));
			sbfPath.append(ITEM_SEPARATOR);
		}
		sbfPath.append(aryPath.get(0));
		return sbfPath.toString();
	}

	public List queryMenuFun(String menuid) {
		String hql = "FROM MenuFunction as menufun WHERE menufun.menuid=? ORDER BY menufun.operid asc";
		List menuOperList = getHibernateTemplate().find(hql, menuid);

		return menuOperList;
	}

	// -------------------------------------------------------------------------
	/**
	 * @return -1:项目已经不存在 0：存在相同KEY的兄弟节点 1：保存成功
	 */
	public int updateItem(MenuInfo menuInfo) {

		MenuInfo ordInfo = findById(menuInfo.getMenuid());
		// 判断是否存在
		if (ordInfo == null)
			return -1;
		// 判断主键值是否重复
		if (!ordInfo.getMenuid().equals(menuInfo.getMenuid())) {
			int iCount = countByKey(menuInfo.getParentid(), menuInfo
					.getMenuid());
			if (iCount > 0)
				return 0;
		}

		ordInfo.setMenuid(menuInfo.getMenuid());
		ordInfo.setMenuitem(menuInfo.getMenuitem());
		ordInfo.setTarget(menuInfo.getTarget());
		ordInfo.setActionto(menuInfo.getActionto());
		ordInfo.setFloor(ordInfo.getFloor());
		ordInfo.setPic(menuInfo.getPic());
		int childnum = getChildCount(menuInfo.getMenuid());

		ordInfo.setChildnum(childnum);
		ordInfo.setOrder(ordInfo.getOrder());
		ordInfo.setParentid(ordInfo.getParentid());
		this.getHibernateTemplate().update(ordInfo);

		return 1;
	}

	// --------------------------------------------------------------------------------
	/**
	 * 计算同一父节点下具有相同key的节点数
	 * 
	 * @parentId
	 * @key
	 */
	public int countByKey(String parentId, String key) {
		String hql = "select count(*) FROM MenuInfo as menu WHERE menu.parentid=? and menu.menuid=?";
		List list = getHibernateTemplate().find(hql,
				new String[] { parentId, key });

		if (list.isEmpty())
			return 0;

		if (list.get(0) == null)
			return 0;

		// return ((Integer) list.get(0)).intValue();
		int i = Integer.parseInt(list.get(0).toString());
		return i;

	}

	// --------------------------------------------------------------------------------
	/**
	 * 取得父节点目前子节点最大的序号
	 * 
	 * @parentId 父ID
	 */
	public int getMaxOrderNum(String parentId) {
		String hql = "select max(menu.order) FROM MenuInfo as menu WHERE menu.parentid=?";
		List list = getHibernateTemplate().find(hql, parentId);
		if (list.isEmpty())
			return 0;

		if (list.get(0) == null)
			return 0;
		return ((Integer) list.get(0)).intValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * 更新该项目的childNum值
	 */
	private void refreshChildCount(String itemId) {
		MenuInfo parentMenu = findById(itemId);
		if (parentMenu != null) {
			parentMenu.setChildnum(getChildCount(parentMenu.getMenuid()));
			this.getHibernateTemplate().update(parentMenu);
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * 返回当前项目ID和下属所有子项目ID，ID之间用,分隔
	 * 
	 * @itemId 要查询的项目ID
	 */
	public String queryIdList(String itemId) {

		StringBuffer sbfIds = new StringBuffer();
		sbfIds.append(itemId);
		List childList = queryChildList(itemId);
		Iterator itr = childList.iterator();
		while (itr.hasNext()) {
			MenuInfo menuInfo = (MenuInfo) itr.next();
			sbfIds.append(",").append(queryIdList(menuInfo.getMenuid()));
		}
		return sbfIds.toString();
	}

	// --------------------------------------------------------------------------------
	/*
	 * 加载菜单转换
	 */
	public HashMap getMenuMap() {
		HashMap<String, String> menuMap = new HashMap();
		try {
			String sql = "  from MenuInfo  ";
			List list = this.getHibernateTemplate().find(sql);
			for (int i = 0; i < list.size(); i++) {
				MenuInfo menuInfo = new MenuInfo();
				menuInfo = (MenuInfo) list.get(i);
				menuMap.put(menuInfo.getMenuid(), menuInfo.getMenuitem());
			}

			return menuMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// --------------------------------------------------------------------------------
		/**
		 * 根据Actionto获取MenuInfo对象
		 * @param actionto
		 * @return
		 */
		public MenuInfo findByAction(String actionto) {
			String hql = " FROM MenuInfo as menu WHERE menu.actionto=?";
			List list = getHibernateTemplate().find(hql, actionto);
			if (list.isEmpty())
				return null;

			if (list.get(0) == null)
				return null;
			return (MenuInfo) list.get(0);
		}
		
		//
		public int ActualOrderNum(String parentId,int order) {
			String hql = "FROM MenuInfo as menu WHERE menu.parentid=? and menu.order < ?";
			List childList = getHibernateTemplate().find(hql,new Object[]{parentId,order});
			if (childList.isEmpty())
				return 0;

			if (childList.get(0) == null)
				return 0;
			return childList.size();
		}
}
