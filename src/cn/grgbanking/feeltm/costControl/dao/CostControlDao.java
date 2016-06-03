package cn.grgbanking.feeltm.costControl.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.costControl.domain.DeptDetailCost;
import cn.grgbanking.feeltm.costControl.domain.DeptGeneralCost;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;

@Repository("costControlDao")
public class CostControlDao extends BaseDao{
	
	public boolean deleteDeptGeneralByDate(Date date){
		try{
			String hql=" delete from DeptGeneralCost where statisticDate =?";
			getHibernateTemplate().bulkUpdate(hql, date);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteDeptDetailByDate(Date date) {
		try{
			String hql=" delete from DeptDetailCost where statisticDate =?";
			getHibernateTemplate().bulkUpdate(hql, date);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	/**部门总体情况原始的列表数据
	 * @return
	 */
	public List<DeptGeneralCost> getGeneralCostList(String queryDept, String queryStartTime,String queryEndTime) {
		try{
			String hql="from DeptGeneralCost cost where 1=1 ";
			if(StringUtils.isNotBlank(queryDept)){
				hql+=" and cost.deptName like '%"+queryDept+"%'";
			}
			if(StringUtils.isNotBlank(queryStartTime)){
				hql+=" and to_date('"+queryStartTime+"','yyyy-MM-dd')<=cost.statisticDate " ;
			}
			if(StringUtils.isNotBlank(queryEndTime)){
				hql+=" and cost.statisticDate<=to_date('"+queryEndTime+"','yyyy-MM-dd') " ;
			}
		
			hql+="order by cost.deptName desc";
			List<DeptGeneralCost> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**部门详情原始的列表数据
	 * @param queryDept
	 * @param queryStartTime
	 * @param queryEndTime
	 * @return
	 */
	public List getDetailCostList(String queryDept, String queryStartTime,String queryEndTime) {
		try{
			String hql="from DeptDetailCost cost where 1=1 ";
			if(StringUtils.isNotBlank(queryDept)){
				hql+=" and cost.deptName like '%"+queryDept+"%'";
			}
			if(StringUtils.isNotBlank(queryStartTime)){
				hql+=" and to_date('"+queryStartTime+"','yyyy-MM-dd')<=cost.statisticDate " ;
			}
			if(StringUtils.isNotBlank(queryEndTime)){
				hql+=" and cost.statisticDate<=to_date('"+queryEndTime+"','yyyy-MM-dd') " ;
			}
		
			hql+="order by cost.deptName desc";
			List<DeptDetailCost> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取昨天的部门统计信息
	 * @param yesterday
	 * @return
	 */
	public List<DeptGeneralCost> getDeptCostInfo(String yesterday){
		try{
			String enddate = DateUtil.getYesterdayByCurrentDate();
			String hql="from DeptGeneralCost cost where 1=1 and to_char(statisticDate,'yyyy-mm-dd')>='"+yesterday+"'";
			hql+=" and to_char(statisticDate,'yyyy-mm-dd')<='"+enddate+"'";
			hql+="order by cost.deptName desc";
			List<DeptGeneralCost> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取昨天的部门人员统计信息
	 * @param yesterday
	 * @return
	 */
	public List<DeptDetailCost> getDeptUserCostInfo(String yesterday){
		try{
			String enddate = DateUtil.getYesterdayByCurrentDate();
			String hql="from DeptDetailCost cost where 1=1 and to_char(statisticDate,'yyyy-mm-dd')>='"+yesterday+"'";
			hql+=" and to_char(statisticDate,'yyyy-mm-dd')<='"+enddate+"'";
			hql+="order by cost.deptName desc";
			List<DeptDetailCost> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	
}
