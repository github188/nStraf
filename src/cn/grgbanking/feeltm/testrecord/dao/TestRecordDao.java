package cn.grgbanking.feeltm.testrecord.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("testRecordDao")
public class TestRecordDao extends BaseDao<TestRecord>{
	public Page getPage(String modualName2,String serialNo,String findMan,String findDate,String soloveMan,String soloveDate,String questionStaus,String finishRate,String modualName, int pageNum,int pageSize, String title, String urge, String updateMan, String deployStatus)
	{
		String hql = "FROM TestQuestion trip WHERE 1=1 ";
		
		if(StringUtils.isNotBlank(serialNo)){
			hql+=" and trip.serialNo like '%"+serialNo+"%' ";
		}
		
		if(StringUtils.isNotBlank(findMan)){
			hql+=" and trip.findMan like '%"+findMan+"%' ";
		}
		
		if(StringUtils.isNotBlank(findDate)){
			hql += " and trip.findDate =to_date('"+findDate+"','yyyy-MM-dd') ";
		}
		
		if(StringUtils.isNotBlank(soloveMan)){
			hql+=" and trip.soloveMan like '%"+soloveMan+"%' ";
		}
		
		if(StringUtils.isNotBlank(soloveDate)){
			hql+=" and trip.soloveDate=to_date('"+soloveDate+"','yyyy-MM-dd') ";
		}
		
		if(StringUtils.isNotBlank(questionStaus)){
			hql+=" and trip.questionStatus ='"+questionStaus+"' ";
		}else{//默认情况，不查询，已关闭的问题
			hql+=" and trip.questionStatus !='已关闭' ";
		}
		
		if(StringUtils.isNotBlank(finishRate)){
			hql+=" and trip.finishRate ='"+finishRate+"' ";
		}
		
		if(StringUtils.isNotBlank(modualName)){
			hql+=" and trip.modualName like '%"+modualName+"%' ";
		}
		
		if(StringUtils.isNotBlank(title)){
			hql+=" and trip.title like '%"+title+"%' ";
		}
		
		if(StringUtils.isNotBlank(urge)){
			hql+=" and trip.urge like '%"+urge+"%' ";
		}
		
		if(StringUtils.isNotBlank(updateMan)){
			hql+=" and trip.updateMan like '%"+updateMan+"%'";
		}
		
		if(StringUtils.isNotBlank(deployStatus)){
			hql+=" and trip.deployStatus like '%"+deployStatus+"%'";
		}
		if(StringUtils.isNotBlank(modualName2)){
			hql+=" and trip.buglevel like '%"+modualName2+"%'";
		}
		
		hql += " order by trip.urgeLevel asc, trip.updateDate desc,trip.soloveMan desc,trip.soloveDate desc,trip.findMan desc,trip.findDate desc";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	

	
	public Page getSum(String start,String end,String prjName,String tripMan,int pageNum,int pageSize)
	{
		String hql = "select Sum(FindBugSum) as FindBugSumTotal,Sum(TesterSum) as TesterSumTotal,Sum(TestTimeSum) as TestTimeSumTotal,Sum(WorkLoad) as WorkLoadTotal from TestRecord where 1=1 ";
		if(start!=null&&!start.equals("") ){
			hql += " and ActualSumbitDate >= to_date('"+start.trim()+"','yyyy-MM-dd') ";
		}
		if(end!=null&&!end.equals("")){
			hql += " and ActualSumbitDate <= to_date('"+start.trim()+"','yyyy-MM-dd') ";
		}
		if(prjName!=null && !prjName.equals("")){
			hql += " and ProjectName like '%"+prjName.trim()+"%' ";
		}
		if(tripMan!=null && !tripMan.equals("")){
			hql += " and Tester like '%"+tripMan.trim()+"%' ";
		}
		
		hql += " order by SumbitPlanDate";
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and lower(trim(username)) not in('汤飞','王全胜','开发员','杜高峰') order by groupName desc,level asc";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}

	public long getMaxSerialNo() {
		String hql="select MAX(ques.serialNo) from TestQuestion ques";
		List list=((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		try{
			if(list!=null && list.size()>0){
				if(list.get(0) instanceof Long){
					long maxSerialNumber=(Long)list.get(0);
					return maxSerialNumber;
				}else if(list.get(0) instanceof String){
					long maxSerialNumber=Long.parseLong((String)list.get(0));
					return maxSerialNumber;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 1000L;
	}
}


