package cn.grgbanking.feeltm.costControl.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.costControl.domain.DateIncomeManage;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;
@SuppressWarnings("rawtypes")
@Repository("dateIncomeManageDao")
public class DateIncomeManageDao extends BaseDao {

	public List<DateIncomeManage> listDateIncomeInfo() {
		try{
			String hql="  from DateIncomeManage income where 1=1 ";
			
			
			hql+=" order by income.startTime desc";
			List<DateIncomeManage> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**根据项目名称，及起止月份获取指定的收入列表
	 * @return
	 */
	public List<DateIncomeManage> listDateIncomeByTimeBukcet(String projectName,String startMonth,String endMonth) {
		try{
			//设置查询时间
			Calendar startCal=Calendar.getInstance();
			Calendar endCal=Calendar.getInstance();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			//设置查询的开始时间，为指定月份的1号00:00:00
			startCal.setTime(sdf.parse(startMonth));
			startCal.set(Calendar.DAY_OF_MONTH, 1);//1号
			startCal.set(Calendar.HOUR_OF_DAY, 0);//0时
			startCal.set(Calendar.MINUTE, 0);//0分
			startCal.set(Calendar.SECOND, 0);//0秒
			//设置查询的结束时间，为指定月份最大日期的23:59:59
			endCal.setTime(sdf.parse(endMonth));
			endCal.set(Calendar.DAY_OF_MONTH,endCal.getActualMaximum(Calendar.DAY_OF_MONTH));//最后一天
			endCal.set(Calendar.HOUR_OF_DAY, 23);//23时
			endCal.set(Calendar.MINUTE, 59);//59分
			endCal.set(Calendar.SECOND, 59);//59秒
			
			
			String hql="  from DateIncomeManage income where income.prjGroup like ? and income.startTime>=? and income.endTime<=? ";
			hql+=" order by income.startTime desc";
			
			Date queryStartTime=startCal.getTime();
			Date queryEndTime=endCal.getTime();
			if(projectName==null){
				projectName="";
			}
			List<DateIncomeManage> list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{"%"+projectName+"%",queryStartTime,queryEndTime});
			
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Page getPage(String hql, int pageNum, int pageSize) {
		 return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
	}

	public List<DateIncomeManage>  getDateIncomeInfoById(String ids) {
		try{
			String sql = " from DateIncomeManage a where a.dateIncomeId='" + ids+ "'";
			return  (List<DateIncomeManage>) this.getHibernateTemplate().find(sql);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}	
		}
	
	public boolean updateAll(String starttime, String endtime, String prjGroup,
			String dateincome , String id) {
		try{
			String sql=" update  OA_COSTCONTROL_DATEINCOME  set  C_PRJGROUP='"+prjGroup+"'  , N_DATEINCOME='"+ dateincome+"' where  C_DATEINCOMEID ='"+id+"'";
			System.out.println(sql);
			int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
