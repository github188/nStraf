package cn.grgbanking.feeltm.staffEntry.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlow;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlowCheckCondition;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("staffEntryDao")
public class StaffEntryDao extends BaseDao<TestRecord>{
	public Page getPage(boolean hasRight, String curUserId, OnBoardFlow entryInfo, String searchContent, Date queryEntryStartTime, Date queryEntryEndTime, int pageNum,int pageSize)
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String hql = "FROM OnBoardFlow onBoard WHERE 1=1 ";
		
		//没有查看权限的，只能查看自己的入职记录
		if(!hasRight){
			hql+=" and onBoard.userId='"+curUserId+"'";
		}
		
		if(entryInfo!=null && StringUtils.isNotBlank(entryInfo.getDetName())){
			hql+=" and onBoard.detName like '%"+entryInfo.getDetName()+"%'";
		}
		if(entryInfo!=null && StringUtils.isNotBlank(entryInfo.getGroupName())){
			hql+=" and onBoard.groupName like '%"+entryInfo.getGroupName()+"%'";
		}
		if(entryInfo!=null && StringUtils.isNotBlank(entryInfo.getUserName())){
			hql+=" and onBoard.userName like '%"+entryInfo.getUserName()+"%'";
		}
		
		//对部门、组别、用户模糊搜索
		if(searchContent!=null){
			hql+=" and (onBoard.detName like '%"+searchContent+"%' or onBoard.groupName like '%"+searchContent+"%' or onBoard.userName like '%"+searchContent+"%') ";
		}
		
		if(queryEntryStartTime!=null){
			hql+=" and to_date('"+ sdf.format(queryEntryStartTime)+"','yyyy-MM-dd')<=onBoard.grgBeginDate";
		}
		if(queryEntryEndTime!=null){
			hql+=" and to_date('"+ sdf.format(queryEntryEndTime)+"','yyyy-MM-dd')>=onBoard.grgBeginDate";
		}
		hql += " order by onBoard.grgBeginDate desc,onBoard.updateTime desc,onBoard.detName desc desc";
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}

	/** 根据部门名称获取这个部门下所有的用户
	 */
	public List getUsersByDept(String dept) {
		String hql="from SysUser where deptName='"+dept+"'";
		List queryList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return queryList;
	}

	/** 获取指定模版的检查条件
	 * @param folder
	 */
	public List getFolderCheckConditions(String folder) {
		if(folder==null){
			folder="default";
		}
		String hql="FROM OnBoardFlowCheckCondition where folder='"+folder+"'";
		List queryList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return queryList;
	}

	/**保存检查条件
	 */
	public void saveCheckCondition(OnBoardFlowCheckCondition checkCondition) {
		if(checkCondition.getFolder()==null){
			checkCondition.setFolder("default");//设置为默认模版
		}
		checkCondition.setUpdateTime(Calendar.getInstance().getTime());
		((BaseHibernateTemplate) getHibernateTemplate()).saveOrUpdate(checkCondition);
	}

	/**查询指定模版的检查条件
	 */
	public List queryCheckCondtion(String folder) {
		if(folder==null){
			folder="default";//没有指定，则查询默认模版的检查条件
		}
		String hql="from OnBoardFlowCheckCondition con where con.folder='"+folder+"' order by con.updateTime desc";
		List queryList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return queryList;
	}

	/**
	 * 查询指定模版的检查条件
	 */
	public List queryCheckCondtionById(String conId) {
		String hql="from OnBoardFlowCheckCondition con where con.id='"+conId+"'";
		List queryList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return queryList;
	}

	//删除指定id的检查条件
	public void delCheckCondition(String id) {
		final String hql="delete from OnBoardFlowCheckCondition con where con.id='"+id+"'";
		((BaseHibernateTemplate) getHibernateTemplate()).execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				session.createQuery(hql).executeUpdate();
				return Boolean.TRUE;
			}
		});
	}

	/**
	 * 保存或者修改入职信息
	 */
	public void saveOrUpdateStaffEntryInfo(OnBoardFlow entryInfo) {
		((BaseHibernateTemplate) getHibernateTemplate()).saveOrUpdate(entryInfo);
	}

	/** 根据用户id获取入职信息
	 * @param userId
	 * @return
	 */
	public OnBoardFlow getCaseByUserId(String userId) {
		String hql="from OnBoardFlow entryInfo where entryInfo.userId='"+userId+"'";
		List<OnBoardFlow> queryList=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		if(queryList!=null && queryList.size()>0){
			return queryList.get(queryList.size()-1);
		}else{
			return null;
		}
	}

	public long getMaxSerialNumber() {
		String hql="select MAX(entryInfo.serialNumber) from OnBoardFlow entryInfo";
		List list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		try{
			if(list!=null && list.size()>0){
				long maxSerialNumber=(Long)list.get(0);
				return maxSerialNumber;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 1000L;
	}

}


