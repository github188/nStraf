package cn.grgbanking.feeltm.testrecord.old.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("testRecordDao2")
public class TestRecordDao extends BaseDao<TestRecord>{
	public Page getPage(String upMonth,String StartMonth,String EndMonth,String projectTypeQuery,String buildType,String prjName,String testStatusQuery,String sumbitProcessQuery,String testMan,int pageNum,int pageSize)
	{
		String hql = "FROM TestRecord trip WHERE 1=1 ";
		if(upMonth!=null&&!upMonth.equals("") ){
			hql += " and (trip.createDate like '%"+upMonth.trim()+"%' " ;
		}
		
		if(StartMonth!=null&&!StartMonth.equals("") ){
			hql += " or to_char(trip.TestStartDate,'yyyy-MM') like '%"+StartMonth.trim()+"%' " ;
		}
		if(EndMonth!=null&&!EndMonth.equals("") ){
			hql += " or to_char(trip.TestFinishDate,'yyyy-MM') like '%"+EndMonth.trim()+"%' " ;
		}
		if(upMonth!=null&&!upMonth.equals("") ){
			hql += ")";
		}
		if(projectTypeQuery!=null&&!projectTypeQuery.equals("")){
			hql += " and trip.ProjectType like '%"+projectTypeQuery.trim()+"%' ";
		}
		if(buildType!=null&&!buildType.equals("")){
			hql += " and trip.VerType like '%"+buildType.trim()+"%' ";
		}
		if(prjName!=null && !prjName.equals("")){
			hql += " and  trip.ProjectName like '%"+prjName.trim()+"%' ";
		}
		if(testStatusQuery!=null && !testStatusQuery.equals("")){
			hql += " and trip.TestStatus like '%"+testStatusQuery.trim()+"' ";
		}
		if(sumbitProcessQuery!=null && !sumbitProcessQuery.equals("")){
			hql += " and trip.SumbitProcess like '%"+sumbitProcessQuery.trim()+"%' ";
		}
		if(testMan!=null && !testMan.equals("")){
			hql += " and trip.Tester like '%"+testMan.trim()+"%' ";
		}
		
		hql += " order by trip.createDate desc,trip.ProjectType asc,trip.ProjectName asc,trip.TestVer asc";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public Page getPageExcel(String upMonth,String StartMonth,String EndMonth,String projectTypeQuery,String buildType,String prjName,String testStatusQuery,String sumbitProcessQuery,String testMan,int pageNum,int pageSize)
	{
		String hql = "FROM TestRecord trip WHERE 1=1 ";
		if(upMonth!=null&&!upMonth.equals("") ){
			hql += " and (trip.createDate like '%"+upMonth.trim()+"%' " ;
		}
		
		if(StartMonth!=null&&!StartMonth.equals("") ){
			hql += " or to_char(trip.TestStartDate,'yyyy-MM') like '%"+StartMonth.trim()+"%' " ;
		}
		if(EndMonth!=null&&!EndMonth.equals("") ){
			hql += " or to_char(trip.TestFinishDate,'yyyy-MM') like '%"+EndMonth.trim()+"%' " ;
		}
		if(upMonth!=null&&!upMonth.equals("") ){
			hql += ")";
		}
		if(projectTypeQuery!=null&&!projectTypeQuery.equals("")){
			hql += " and trip.ProjectType like '%"+projectTypeQuery.trim()+"%' ";
		}
		if(buildType!=null&&!buildType.equals("")){
			hql += " and trip.VerType like '%"+buildType.trim()+"%' ";
		}
		if(prjName!=null && !prjName.equals("")){
			hql += " and  trip.ProjectName like '%"+prjName.trim()+"%' ";
		}
		if(testStatusQuery!=null && !testStatusQuery.equals("")){
			hql += " and trip.TestStatus like '%"+testStatusQuery.trim()+"' ";
		}
		if(sumbitProcessQuery!=null && !sumbitProcessQuery.equals("")){
			hql += " and trip.SumbitProcess like '%"+sumbitProcessQuery.trim()+"%' ";
		}
		if(testMan!=null && !testMan.equals("")){
			hql += " and trip.Tester like '%"+testMan.trim()+"%' ";
		}
		
		hql += " order by trip.ProjectType asc,trip.ProjectName asc,trip.SumbitPlanDate asc";
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
}


