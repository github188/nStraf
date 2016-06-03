package cn.grgbanking.feeltm.signrecord.dao;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.signrecord.domain.NoAttendanceData;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("noAttendanceDataDao")
public class NoAttendanceDataDao extends BaseDao<NoAttendanceData> {
	public Page getPage(int pageNum,int pageSize,UserModel userModel,String year, String month, String type,String prjname){
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new java.util.Date();
		String curdate = sf.format(date);
		String curmont = curdate.split("-")[1];
		String smonth = "";
		String emonth = "";
		if(month==null || "".equals(month)){
			smonth = "1";
			emonth = curmont;
		}else{
			smonth=month;
			emonth=month;
		}
		if(Integer.parseInt(smonth)<10){
			smonth="0"+smonth;
		}
		if(Integer.parseInt(emonth)<10){
			emonth="0"+emonth;
		}
		int day = DateUtil.getLastDayForYearAndMonth(Integer.parseInt(year), Integer.parseInt(emonth));
		String sdate = year + "-" + smonth + "-01";
		String edate = year + "-" + emonth + "-" + day;
		String yearMonth = year + "-" + month;
		StringBuffer sql = new StringBuffer();
		//首先删除为导入记录表中的数据
		sql.append("delete from oa_noattendancedata");
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
		//将所有为导入考勤数据的记录存到导入记录表中
		sql.delete(0, sql.length());
		sql.append("insert into oa_noattendancedata(c_id,c_prjname,c_workdate) select dboa_seq.nextval,c_grpname,to_date(signdate,'yyyy-mm-dd') from (")
			.append("  select t1.signdate,t2.c_grpname from ")
			.append("  (").append(" select to_char(to_date(check_date,'yyyy-mm-dd'),'yyyy-mm-dd') signdate from oa_holiday t ")
			.append(" where to_char(to_date(check_date,'yyyy-mm-dd'),'yyyy-mm-dd')>='").append(sdate).append("'")
			.append(" and to_char(to_date(check_date,'yyyy-mm-dd'),'yyyy-mm-dd')<='").append(edate).append("' and type='0'")
			.append(")  t1,")
			.append("  (select distinct c_note c_grpname from sys_datadir where c_parentid='ff8080814979e75e014983f8dea006b3')t2")
			.append("  minus")
			.append("  select distinct to_char(d_signtime, 'yyyy-mm-dd') signdate,t1.c_note")
			.append("  from oa_cardrecord t,(select distinct c_note,c_key from SYS_DATADIR where c_parentid='ff8080814979e75e014983f8dea006b3') t1")
			.append("  where  t.c_type=t1.c_key and t.c_type in (select distinct c_key from sys_datadir where c_parentid='ff8080814979e75e014983f8dea006b3')");
		if(!"all".equals(type)){
			sql.append("   and to_char(d_signtime, 'yyyy-mm') = to_char(to_date('").append(yearMonth).append("','yyyy-mm'),'yyyy-mm')");
		}
		sql.append(") where 1=1");
		
		if(prjname!=null && !"".equals(prjname)){
			sql.append(" and c_grpname='").append(prjname).append("'");
		}
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
		//列表上列出为导入考勤数据的page
		String hql = "from NoAttendanceData s where 1=1";
		if(!"all".equals(type)){
			hql+=" and to_char(workdate,'yyyy-mm')=to_char(to_date('"+yearMonth+"','yyyy-mm'),'yyyy-mm')";
		}
		if(prjname!=null && !"".equals(prjname)){
			hql+=" and prjname='"+prjname+"'";
		}
		hql+=" order by prjname,workdate desc";
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
}
