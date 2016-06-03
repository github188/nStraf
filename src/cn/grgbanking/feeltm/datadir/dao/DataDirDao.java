package cn.grgbanking.feeltm.datadir.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.feeltm.util.StringUtil;
import cn.grgbanking.framework.dao.BaseDao;
@Repository  
@SuppressWarnings("unchecked")  
public class DataDirDao    extends BaseDao <SysDatadir> {
	  //��Ŀ��ָ��
	  public static final String ITEM_SEPARATOR=".";
	  //��Ŀ¼·��
	  public static final String ROOT="\\";
	  //�����Ŀ�ĸ�ID
	  public static final String TOP_PARENT_ID="0";
	// --------------------------------------------------------------------------------

	  //--------------------------------------------------------------------------------
		/**
		 * 根据主键找对象
		 * 
		 * @id 项目主键
		 */
		public SysDatadir findById(String id) {
			if (id.equals("0"))
				return null;
			try {
				return (SysDatadir)this.getHibernateTemplate().get(SysDatadir.class, id);
			} catch (org.springframework.orm.ObjectRetrievalFailureException exp) {
				return null;
			}
		}
		
//		 --------------------------------------------------------------------------------
		/**
		 * 返回父ID符合要求的直属子项目集合
		 * 
		 * @parentId 父项目ID
		 */
		public List queryChildList(String parentId) {
			String hql = "FROM SysDatadir as dir WHERE dir.parentid=? ORDER BY dir.order asc";
			List childList = this.getHibernateTemplate().find(hql, parentId);
			return childList;
			
		}
		
//		 -----------------------------------------------------------------------
		/**
		 * 返回网页导航条
		 */
		public String getNavigation(String itemId) {
			ArrayList aryPath = new ArrayList();
			SysDatadir dataDir = findById(itemId);
			while (dataDir != null) {
				aryPath.add(dataDir);
				if (dataDir.getParentid().equals(TOP_PARENT_ID))
					break;
				dataDir = findById(dataDir.getParentid());
			}
			int iSize = aryPath.size();
			if (iSize == 0)
				return ROOT;

			StringBuffer sbfPath = new StringBuffer();
			for (int i = iSize - 1; i > 0; i--) {
				dataDir = (SysDatadir) aryPath.get(i);
				//sbfPath.append("<a href=\"javascript:ComeIn('");
				//sbfPath.append(dataDir.getId());
				//sbfPath.append("')\">");
				//sbfPath.append(StringEscapeUtils.escapeHtml(dataDir.getNote()));
				sbfPath.append(dataDir.getNote());
				//sbfPath.append("</a><b>-&gt;</b>");
				sbfPath.append("\\");
			}
			dataDir = (SysDatadir) aryPath.get(0);
			//sbfPath.append("<a href=\"javascript:ComeIn('");
			//sbfPath.append(dataDir.getId());
			//sbfPath.append("')\">");
			//sbfPath.append(StringEscapeUtils.escapeHtml(dataDir.getNote()));
			sbfPath.append(dataDir.getNote());
			//sbfPath.append("</a>");
			return sbfPath.toString();
		}
		
//		 -------------------------------------------------------------------------------
		/**
		 * 返回该项目的关键字路径
		 */
		public String getPath(String itemId) {
			ArrayList aryPath = new ArrayList();
			SysDatadir dataDir = findById(itemId);
			while (dataDir != null) {
				aryPath.add(dataDir.getKey());
				if (dataDir.getParentid().equals(TOP_PARENT_ID))
					break;
				dataDir = findById(dataDir.getParentid());
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
		
//		 -----------------------------------------------------------------------
		/**
		 * @return 0:存在相同KEY的兄弟节点 1：新增成功
		 */
		public int addItem(SysDatadir dataDir) {

			int iCount = countByKey(dataDir.getParentid(), dataDir.getKey());
			if (iCount > 0) {
				return 0;
			}

			dataDir.setChildnum(0);
			dataDir.setOrder(getMaxOrderNum(dataDir.getParentid()));
			
			this.getHibernateTemplate().save(dataDir);

			// 更新父节点的孩子数
			refreshChildCount(dataDir.getParentid());
			return 1;
		}
		
		// --------------------------------------------------------------------------------
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
				SysDatadir dataDir = findById(aryIds[i]);

				String[] aryDelIds = StringUtils.split(queryIdList(aryIds[i]), ",");

				String hql = "delete  SysDatadir as dir WHERE "
					+ SqlHelper.fitStrInCondition("dir.id", aryDelIds);			
				iCount+=this.getHibernateTemplate().bulkUpdate(hql);			
				// 更新父项目的孩子数
				refreshChildCount(dataDir.getParentid());
			}// for

			return iCount;
		}
		
		public int reorderItems(String idList)
		{
			   String[] childId = StringUtil.toArray(idList, ",");
			   if(childId!=null){
			     for (int i = 0; i < childId.length; i++)
			     {
				   SysDatadir  dirinfo = findById(childId[i]);
				   dirinfo.setOrder(i + 1);
				   //updateObject(dirinfo);
				   this.getHibernateTemplate().update(dirinfo);
			     }
			     return childId.length;
			   }else{
				   return 0;
			   }
		}
		
//		 -------------------------------------------------------------------------
		/**
		 * @return -1:项目已经不存在 0：存在相同KEY的兄弟节点 1：保存成功
		 */
		public int updateItem(SysDatadir dataDir) {

			SysDatadir oriDir = findById(dataDir.getId());
			// 判断是否存在
			if (oriDir == null)
				return -1;
			// 判断主键值是否重复
			if (!oriDir.getKey().equals(dataDir.getKey())) {
				int iCount = countByKey(dataDir.getParentid(), dataDir.getKey());
				if (iCount > 0)
					return 0;
			}

			oriDir.setKey(dataDir.getKey());
			oriDir.setNote(dataDir.getNote());
			oriDir.setNoteEn(dataDir.getNoteEn());
			oriDir.setValue(dataDir.getValue());
			//updateObject(oriDir);
			this.getHibernateTemplate().update(oriDir);

			return 1;
		}
		
//		 --------------------------------------------------------------------------------
		/**
		 * 计算同一父节点下具有相同key的节点数
		 * 
		 * @parentId
		 * @key
		 */
		public int countByKey(String parentId, String key) {
			String hql = "select count(*) FROM SysDatadir as dir WHERE dir.parentid=? and dir.key=?";
			List list = this.getHibernateTemplate().find(hql,
					new String[] { parentId, key });

			if (list.isEmpty())
				return 0;

			if (list.get(0) == null)
				return 0;
			
			//return ((Integer) list.get(0)).intValue();
			int i=Integer.parseInt(list.get(0).toString());	
	        return i;
		}
		
//		 -----------------------------------------------------------------------
		/**
		 * 更新该项目的childNum值
		 */
		private void refreshChildCount(String itemId) {
			SysDatadir parentDir = findById(itemId);
			if (parentDir != null) {
				parentDir.setChildnum(getChildCount(parentDir.getId()));
				this.getHibernateTemplate().update(parentDir);
			}
		}
		
//		 --------------------------------------------------------------------------------
		/**
		 * 取得父节点目前子节点最大的序号
		 * 
		 * @parentId 父ID
		 */
		public int getMaxOrderNum(String parentId) {
			String hql = "select max(dir.order) FROM SysDatadir as dir WHERE dir.parentid=?";
			List list = this.getHibernateTemplate().find(hql, parentId);
			if (list.isEmpty())
				return 0;

			if (list.get(0) == null)
				return 0;
			return ((Integer) list.get(0)).intValue();
		}
		
//		 -----------------------------------------------------------------------
		/**
		 * 取得子节点数目
		 */
		public int getChildCount(String parentId) {
			String hql = "select count(*) FROM SysDatadir as dir WHERE dir.parentid=?";
			List list = this.getHibernateTemplate().find(hql, parentId);

			if (list.isEmpty())
				return 0;

			if (list.get(0) == null)
				return 0;

			//return ((Integer) list.get(0)).intValue();
			int i=Integer.parseInt(list.get(0).toString());	
	        return i;
		}
		
//		Get a map of userid and username
		public HashMap getSysUserMap() {
			
			HashMap map=new HashMap();
			String hql="SELECT sysUser.userid,sysUser.username FROM SysUser as sysUser ";
		
			List list=this.getHibernateTemplate().find(hql);
			
			for (ListIterator it=list.listIterator();it.hasNext();) {
				Object[] obj=(Object[])it.next();			
				map.put(obj[0],obj[1]);		
			}
			
			return map;
		}
		
//		 --------------------------------------------------------------------------------
		/**
		 * Get a map of orgid and orgname
		 */
		public HashMap getOrgInfoMap(){
			HashMap map = new HashMap();
			String hql = "SELECT orgInfo.orgid,orgInfo.orgname FROM OrgInfo orgInfo";
			List list = this.getHibernateTemplate().find(hql);
			for(ListIterator it = list.listIterator();it.hasNext();){
				Object[] obj = (Object[])it.next();
				map.put(obj[0], obj[1]);
			}
			return map;
		}
		/**
		 * 根据路径返回value note对照表
		 * 以value为Map的key, note为Map的值
		 */
		public Map getChildValueMap(String path) {
			HashMap childMap = new HashMap();
			List childList = queryChildByPath(path);
			Iterator itr = childList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				if(path!=null&&path.equals("systemConfig.systemParamConfig"))
					childMap.put(dir.getKey(), dir.getValue());
				else
				  childMap.put(dir.getValue(), dir.getNote());
			}
			return childMap;
		}
		/**lhy 2014-4-23
		 * 以value为Map的key, value为Map的值
		 */
		public Map getChildValueMap2(String path) {
			HashMap childMap = new HashMap();
			List childList = queryChildByPath(path);
			Iterator itr = childList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				childMap.put(dir.getKey(), dir.getValue());
			}
			return childMap;
		}
		
		public Map getChildObjectMap(String path) {
			HashMap childMap = new HashMap();
			List childList = queryChildByPath(path);
			Iterator itr = childList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				childMap.put(dir.getKey(), dir);
			}
			return childMap;
		}
		
//		 -----------------------------------------------------------------------------
		/**
		 * 根据该项目的全路径识别该项目ID，并返回该项目下属子节点 如果该路径不存在，返回空的列表对象
		 */
		public List queryChildByPath(String path) {
			if (path.equals(ROOT))
				return queryChildList(TOP_PARENT_ID);

			SysDatadir dir = findByPath(path);
			if (dir == null)
				return new ArrayList(0);

			return queryChildList(dir.getId());
		}
		
//		 --------------------------------------------------------------------------------
		/**
		 * 根据路径返回项目信息
		 * 
		 * @path:从父节点一直到该项目的关键字排列，项目关键字间用ITEM_SEPARATOR分隔
		 */
		public SysDatadir findByPath(String path) {
			
			String[] nodes = StringUtils.split(path, ITEM_SEPARATOR);
			String hql = "FROM SysDatadir as dir WHERE dir.parentid=? and dir.key=?";
			String[] params = new String[] { TOP_PARENT_ID, null };
			SysDatadir dir = null;
			for (int i = 0; i < nodes.length; i++) {
				params[1] = nodes[i];
				List list = this.getHibernateTemplate().find(hql, params);
				if (list.isEmpty())
					return null;
				dir = (SysDatadir) list.get(0);
				if (dir == null)
					return null;
				params[0] = dir.getId();
			}
			return dir;
		}
		

//		 --------------------------------------------------------------------------------
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
				SysDatadir dir = (SysDatadir) itr.next();
				sbfIds.append(",").append(queryIdList(dir.getId()));
			}
			return sbfIds.toString();
		}
		
//		 --------------------------------------------------------------------------------

		public String getOptionByPath(String path) {
			StringBuffer sbfXml = new StringBuffer();
			List childList = queryChildByPath(path);
			// childList只会为empty，不会为null
			Iterator itr = childList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				sbfXml.append("<option ");
				sbfXml.append("value='");
				if (dir.getValue() != null)
					sbfXml.append(StringEscapeUtils.escapeXml(dir.getValue()));
				sbfXml.append("'>");
				sbfXml.append(StringEscapeUtils.escapeXml(dir.getNote()));
				sbfXml.append("</option>");
			}
			return sbfXml.toString();
		}
		
		public String getOptionByPathCash(String path) {
			StringBuffer sbfXml = new StringBuffer();
			List childList = queryChildByPath(path);
			// childList只会为empty，不会为null
			Iterator itr = childList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				if(!dir.getValue().equals("3")){
					sbfXml.append("<option ");
					sbfXml.append("value='");
					if (dir.getValue() != null)
						sbfXml.append(StringEscapeUtils.escapeXml(dir.getValue()));
					sbfXml.append("'>");
					sbfXml.append(StringEscapeUtils.escapeXml(dir.getNote()));
					sbfXml.append("</option>");
				}
				
			}
			return sbfXml.toString();
		}

}
