package cn.grgbanking.feeltm.attendance.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.attendance.domain.AttendanceAnalysisCount;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("attendanceAnalysisDao")
public class AttendanceAnalysisDao extends BaseDao<AttendanceAnalysisCount>{
	/**
	 * 统计前从ehr同步一次数据
	 * @return
	 */
	public Boolean execDataForEhr(){
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call holsdef_proc}").executeUpdate();
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call hols_proc}").executeUpdate();
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call leave_proc}").executeUpdate();
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call overtime_proc}").executeUpdate();
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call trip_proc}").executeUpdate();
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call cardrecord_proc}").executeUpdate();
		return true;
	}
	
	/**
	 * 统计数据时，执行存储过程，将oa_signrecord表中的移动签到数据同步一次到oa_cardrecord表中
	 * @return
	 */
	public Boolean execMobilecard_Proc(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call mobilecard_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 统计数据时，执行存储过程，修改有打卡记录又有移动签到的记录
	 * @return
	 */
	public boolean execProcedure(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call updatemobilestatus_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 导入广发数据后，将oa_signrecord表中数据同步到oa_cardrecord,并删除oa_signrecord表中c_type='1'的数据
	 * @return
	 */
	public boolean execOutcard_Proc(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call outcard_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据时间段查询出所有考勤数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getSignRecord(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select c_userid,c_username,c_date,replace(max(c_time),',','@')c_time,replace(max(c_type),',','@')c_type from (")
		.append(" select c_userid,c_username,c_date,wm_concat(c_time || '|' || c_type) over (partition BY c_userid,c_username,c_date order by c_userid,c_date,c_time) c_time,")
		.append("    wm_concat(c_type || nvl(c_signwhere, 0)) over (partition BY c_userid,c_username,c_date order by c_userid,c_date,c_time) c_type from (")
		.append("    select c_userid,c_username,")
		.append("           to_char(d_signtime, 'yyyy-mm-dd') c_date,")
		.append("           to_char(d_signtime, 'hh24:mi:ss') c_time,")
		.append("           c_type,c_signwhere")
		.append("      from oa_cardrecord")
		.append("     where to_char(d_signtime, 'yyyy-mm-dd') >= '").append(sdate).append("'")
		.append("       and to_char(d_signtime, 'yyyy-mm-dd') <= '").append(edate).append("'")
		.append("       and c_status is null and c_vilid='1' and c_dealstatus='0'))")
		.append("       group by c_userid,c_username,c_date");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 根据时间段获取所有假期数据信息
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getLeaveData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select c_userid,c_startdate,c_starttime||'@'||c_endtime from oa_leaverecord ")
		.append(" where c_startdate>='").append(sdate).append("' and c_enddate<='").append(edate).append("'")
		.append(" order by c_userid,c_startdate");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 根据用户id，日期判断该用户在当天是否有上班信息
	 * @param sdate
	 * @param userid
	 * @return
	 */
	public List flagLeaveIsInAttendance(String sdate,String userid){
		StringBuffer sql = new StringBuffer();
		sql.append("select c_time1,c_time2,c_time3,c_time4,c_time5,c_time6,mindate,maxdate from oa_attendance where c_workdate='").append(sdate).append("' and c_userid='").append(userid).append("'");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 根据假期数据，修改考勤数据中的时间段
	 * @param record
	 * @return
	 */
	public boolean updateAttendanceDateForLeave(AttendanceAnalysisCount record,String type){
		StringBuffer sql = new StringBuffer();
		if(type.equals("status")){
			sql.append("update oa_attendance set morn_islast='").append(record.getMorn_islast()==null?"":record.getMorn_islast())
			.append("',after_isleave='").append(record.getAfter_isleave()==null?"":record.getAfter_isleave())
			.append("',noon_islast='").append(record.getNoon_islast()==null?"":record.getNoon_islast())
			.append("',noon_isleave='").append(record.getNoon_isleave()==null?"":record.getNoon_isleave()).append("'")
			.append(" where c_userid='").append(record.getUserid()).append("' and c_workdate='").append(record.getWorkdate()).append("'");
		}else if(type.equals("times")){
			sql.append("update oa_attendance set c_time1='").append(record.getTime1()==null?"":record.getTime1()).append("',c_time2='").append(record.getTime2()==null?"":record.getTime2())
			.append("',c_time3='").append(record.getTime3()==null?"":record.getTime3()).append("',c_time4='").append(record.getTime4()==null?"":record.getTime4()).append("',c_time5='")
			.append(record.getTime5()==null?"":record.getTime5()).append("',c_time6='").append(record.getTime6()==null?"":record.getTime6()).append("',mindate='")
			.append(record.getMindate()==null?"":record.getMindate())
			.append("',maxdate='").append(record.getMaxdate()==null?"":record.getMaxdate()).append("' where c_workdate='").append(record.getWorkdate())
			.append("' and c_userid='").append(record.getUserid()).append("'");
		}
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将临时分析表中的数据copy到正式数据中
	 * @return
	 */
	public boolean copyAttendanceTempTo(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call updatemobilestatus_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取考勤数据，根据时间判断迟到早退
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getAttendanceData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select c_userid,c_workdate,c_time1,c_time2,c_time3,mindate,maxdate,c_attendancetype,c_stime,c_etime,c_mtime,c_mtime2")
		.append(" from oa_attendance t where c_workdate>='")
		.append(sdate).append("' and c_workdate<='").append(edate).append("'");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 为考勤表添加假期说明
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getLeaveDateByDesc(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.c_userid,round(sum(t.i_sumtime)/8,1),t.c_type,replace(to_char(wm_concat(t.c_oarunid)),',','@')c_oarunid from oa_leaverecord t")
		.append("   where t.c_startdate>='").append(sdate).append("' and t.c_startdate<='").append(edate).append("'")
		.append("   group by t.c_userid,c_type ")
		.append("   order by c_userid,c_type");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 根据日期判断当天是为工作日
	 * @param date
	 * @param filed
	 * @return
	 */
	public List flagIsWorkday(String date,String filed){
		String sql = "select count(id) num from oa_holiday where "+filed+"=to_date('"+date+"','yyyy-MM-dd')";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		return list;
	}
	
	public List exportAllAttendanceData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
//		sql.append("select distinct sysuser.c_username 姓名,")
//		.append("                            m.mday-nvl(h.sjiadesc,0)-nvl(i.bjiadesc,0)-nvl(j.njiadesc,0)-nvl(k.otherdesc,0)-nvl(l.bxjiadesc,0)-")
//		.append("                            (select decode(nvl(getdays('").append(sdate).append("',to_char(d_grg_begindate,'yyyy-mm-dd')),0),0,0,nvl(getdays('").append(sdate).append("',to_char(d_grg_begindate,'yyyy-mm-dd')),0)-1) from dual)-")
//		.append("                            (select decode(nvl(getdays(to_char(c_grg_enddate,'yyyy-mm-dd'),'").append(edate).append("'),0),0,0,nvl(getdays(to_char(c_grg_enddate,'yyyy-mm-dd'),'").append(edate).append("'),0)-1) from dual) 出勤,")
//		.append("                            decode(p.d_grg_begindate,null,'',to_char(p.d_grg_begindate,'yyyy-mm-dd')||'入职')  迟到,")
//		.append("                            decode(q.c_grg_enddate,null,'',to_char(q.c_grg_enddate,'yyyy-mm-dd')||'离职') 早退,")
//		.append("                            h.sjiadesc 事假,")
//		.append("                            h.c_oarunid 说明1,")
//		.append("                            i.bjiadesc 病假,")
//		.append("                            i.c_oarunid 说明2,")
//		.append("                            j.njiadesc 年假,")
//		.append("                            j.c_oarunid 说明3,")
//		.append("                            k.otherdesc 其他假,")
//		.append("                            k.c_oarunid 说明4,")
//		.append("                            f.sumtime 出差天数,")
//		.append("                            f.c_oarunid 说明5,")
//		.append("                            case when sysuser.c_attendancetype=2 then nvl(g.i_deferred_time,0)")
//		.append("                            else nvl(g.i_deferred_time,0)-nvl(n.sumtime,0)-nvl(o.sumtime,0)-nvl(e.overtime,0)+nvl(d.sumtime,0) end  上月剩余调休,")
//		.append("                            '' 出差新增调休,")
//		.append("                            '' 说明,")
//		.append("                            e.overtime 加班,")
//		.append("                            e.c_oarunid 说明6,")
//		.append("                            l.bxjiadesc 补休,")
//		.append("                            l.c_oarunid 说明7,")
//		.append("                            nvl(g.i_deferred_time, 0)-nvl(n.sumtime,0)-nvl(o.sumtime,0) 本月剩余调休")
//		.append("                            ,C_BEFORE_DEFERRED 上月剩余调休_oa,nvl(C_BEFORE_DEFERRED,0)+nvl(e.overtime,0)-nvl(l.bxjiadesc,0)  本月剩余调休_oa")
//		.append("                            ,p.d_grg_begindate,q.c_grg_enddate,base.c_deptname,base.c_userid,base.c_innernum,sysuser.c_order,sysuser.c_attendancetype")
//		.append("              from (select atd.c_userid, count(*) days")
//		.append("                      from oa_attendancerecord atd")
//		.append("                     where c_worktype = '0'")
//		.append("                       and c_workdate >= '").append(sdate).append("'")
//		.append("                       and c_workdate <= '").append(edate).append("'")
//		.append("                     group by atd.c_userid) a,")
//		.append("                   (select c_userid,")
//		.append("                           round(sum((to_date(c_workdate || ' ' || mindate,")
//		.append("                                              'yyyy-mm-dd hh24:mi:ss') -")
//		.append("                                     to_date(c_workdate || ' ' || c_stime,")
//		.append("                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60),")
//		.append("                                 2) times")
//		.append("                      from oa_attendancerecord t")
//		.append("                     where t.morn_islast = '迟到'")
//		.append("                       and t.c_worktype = '0'")
//		.append("                       and c_workdate >= '").append(sdate).append("'")
//		.append("                       and c_workdate <= '").append(edate).append("'")
//		.append("                     group by c_userid) b,")
//		.append("                   (select c_userid,")
//		.append("                           round(sum((to_date(c_workdate || ' ' || c_etime,")
//		.append("                                              'yyyy-mm-dd hh24:mi:ss') -")
//		.append("                                     to_date(c_workdate || ' ' || maxdate,")
//		.append("                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60),")
//		.append("                                 2) times")
//		.append("                      from oa_attendancerecord t")
//		.append("                     where t.after_isleave = '早退'")
//		.append("                       and t.c_worktype = '0'")
//		.append("                       and c_workdate >= '").append(sdate).append("'")
//		.append("                       and c_workdate <= '").append(edate).append("'")
//		.append("                     group by c_userid) c,")
//		.append("                   (select c_userid, round(sum(t.i_sumtime) / 8, 4) sumtime")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and c_enddate <= '").append(edate).append("'")
//		.append("                       and c_type = '补休假'")
//		.append("                     group by c_userid) d,")
//		.append("                   (select distinct c_userid,")
//		.append("                                    round(sum(i_sumtime) / 8, 4) overtime,")
//		.append("                                    replace(to_char(wm_concat(distinct c_oarunid)),")
//		.append("                                            ',',")
//		.append("                                            '@') c_oarunid")
//		.append("                      from oa_overrecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                     group by c_userid) e,")
//		.append("                   (select distinct c_userid,")
//		.append("                                    sum(i_sumtime) sumtime,")
//		.append("                                    replace(to_char(wm_concat(distinct c_oarunid)),")
//		.append("                                            ',',")
//		.append("                                            '@') c_oarunid")
//		.append("                      from oa_triprecord")
//		.append("                     where c_startdate >= '").append(sdate).append("'")
//		.append("                       and c_enddate <= '").append(edate).append("'")
//		.append("                     group by c_userid) f,")
//		.append("                   (select distinct c_userid, t.i_deferred_time")
//		.append("                      from usr_hols t) g,")
//		.append("                   (select t.c_username,")
//		.append("                           t.c_userid,t.c_deptname,t.c_innernum")
//		.append("                      from oa_attendancerecord t")
//		.append("                     where t.c_worktype = '0'")
//		.append("                       and c_workdate >= '").append(sdate).append("'")
//		.append("                       and c_workdate <= '").append(edate).append("') base,")
//		.append("                       (select c_userid,c_username,c_order,c_attendancetype from sys_user) sysuser,")
//		.append("                       (select c_userid,")
//		.append("                           round(sum(i_sumtime)/8,4) sjiadesc,")
//		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                       and t.c_type = '事假'")
//		.append("                     group by c_userid) h,")
//		.append("                   (select c_userid,")
//		.append("                           round(sum(i_sumtime)/8,4) bjiadesc,")
//		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                       and t.c_type = '病假'")
//		.append("                     group by c_userid) i,")
//		.append("                   (select c_userid,")
//		.append("                           round(sum(i_sumtime)/8,4) njiadesc,")
//		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                       and t.c_type = '年假'")
//		.append("                     group by c_userid) j,")
//		.append("                   (select c_userid,")
//		.append("                           round(sum(i_sumtime)/8,4) otherdesc,")
//		.append("                           replace(to_char(wm_concat(distinct decode(c_oarunid,null,'',c_oarunid || '@') || c_type)),',','@') c_oarunid")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                       and t.c_type not in ('事假', '病假', '年假','补休假')")
//		.append("                     group by c_userid) k,")
//		.append("                     (select c_userid,")
//		.append("                           round(sum(i_sumtime)/8,4) bxjiadesc,")
//		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
//		.append("                      from oa_leaverecord t")
//		.append("                     where t.c_startdate >= '").append(sdate).append("'")
//		.append("                       and t.c_enddate <= '").append(edate).append("'")
//		.append("                       and t.c_type='补休假'")
//		.append("                     group by c_userid)l,")
//		.append("                     (select count(*)mday from (")
//		.append("                            select to_char(c_date,'yyyy-mm-dd')c_date from (")
//		.append("                            select to_date('").append(sdate).append("', ' yyyy-MM-dd ') + rownum - 1 c_date from user_objects ")
//		.append("                            where rownum <= (to_date('").append(edate).append("', ' yyyy-MM-dd') - to_date('").append(sdate).append("', 'yyyy-MM-dd') + 1))a")
//		.append("                            where c_date not in (select holiday_date from oa_holiday where holiday_date is not null) ")
//		.append("                            and (select (to_char(to_date(to_char(c_date,'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss'),'D')-1) from dual)<>'0' ")
//		.append("                            and (select (to_char(to_date(to_char(c_date,'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss'),'D')-1) from dual)<>'6'")
//		.append("                            union all")
//		.append("                            select to_char(work_date,'yyyy-mm-dd')c_date from oa_holiday where work_date is not null ")
//		.append("                            and to_char(work_date,'yyyy-mm-dd') between '").append(sdate).append("' and '").append(edate).append("'))m,")
//		.append("                      (select round(sum(i_sumtime)/8,4) sumtime,c_userid from oa_overrecord where substr(c_startdate,1,10)>'").append(edate).append("' group by c_userid)n,")
//		.append("                      (select round(sum(i_sumtime)/8,4) sumtime,c_userid from oa_leaverecord where c_startdate>'").append(edate).append("' and c_type='补休假' group by c_userid) o,")
//		.append("                      (select c_userid,d_grg_begindate from sys_user t where to_char(t.d_grg_begindate,'yyyy-mm-dd')>='").append(sdate).append("') p,")
//		.append("                      (select c_userid,c_grg_enddate from sys_user t where to_char(t.c_grg_enddate,'yyyy-mm-dd')>='").append(sdate).append("') q,")
//		.append("                      (select c_userid,C_BEFORE_DEFERRED from usr_hols )w")
//		.append("             where sysuser.c_userid=base.c_userid(+)")
//		.append("               and sysuser.c_userid = a.c_userid(+)")
//		.append("               and sysuser.c_userid = b.c_userid(+)")
//		.append("               and sysuser.c_userid = c.c_userid(+)")
//		.append("               and sysuser.c_userid = d.c_userid(+)")
//		.append("               and sysuser.c_userid = e.c_userid(+)")
//		.append("               and sysuser.c_userid = f.c_userid(+)")
//		.append("               and sysuser.c_userid = g.c_userid(+)")
//		.append("               and sysuser.c_userid = h.c_userid(+)")
//		.append("               and sysuser.c_userid = i.c_userid(+)")
//		.append("               and sysuser.c_userid = j.c_userid(+)")
//		.append("               and sysuser.c_userid = k.c_userid(+)")
//		.append("               and sysuser.c_userid = l.c_userid(+)")
//		.append("               and sysuser.c_userid = n.c_userid(+)")
//		.append("               and sysuser.c_userid = o.c_userid(+)")
//		.append("               and sysuser.c_userid = p.c_userid(+)")
//		.append("               and sysuser.c_userid = q.c_userid(+)")
//		.append("               and sysuser.c_userid = w.c_userid(+)")
//		.append("               and sysuser.c_userid!='admin'and sysuser.c_order is not null")
//		.append("               order by to_number(sysuser.c_order)");
		sql.append("select distinct sysuser.c_username 姓名,")
		.append("                            m.mday-nvl(h.sjiadesc,0)-nvl(i.bjiadesc,0)-nvl(j.njiadesc,0)-nvl(k.otherdesc,0)-nvl(l.bxjiadesc,0)-")
		.append("                            (select decode(nvl(getdays('").append(sdate).append("',to_char(d_grg_begindate,'yyyy-mm-dd')),0),0,0,nvl(getdays('").append(sdate).append("',to_char(d_grg_begindate,'yyyy-mm-dd')),0)-1) from dual)-")
		.append("                            (select decode(nvl(getdays(to_char(c_grg_enddate,'yyyy-mm-dd'),'").append(edate).append("'),0),0,0,nvl(getdays(to_char(c_grg_enddate,'yyyy-mm-dd'),'").append(edate).append("'),0)-1) from dual) 出勤,")
		.append("                            decode(p.d_grg_begindate,null,'',to_char(p.d_grg_begindate,'yyyy-mm-dd')||'入职')  迟到,")
		.append("                            decode(q.c_grg_enddate,null,'',to_char(q.c_grg_enddate,'yyyy-mm-dd')||'离职') 早退,")
		.append("                            h.sjiadesc 事假,")
		.append("                            h.c_oarunid 说明1,")
		.append("                            i.bjiadesc 病假,")
		.append("                            i.c_oarunid 说明2,")
		.append("                            j.njiadesc 年假,")
		.append("                            j.c_oarunid 说明3,")
		.append("                            k.otherdesc 其他假,")
		.append("                            k.c_oarunid 说明4,")
		.append("                            f.sumtime 出差天数,")
		.append("                            f.c_oarunid 说明5,")
		.append("                           nvl(g.i_deferred_time,0)-nvl(n.sumtime,0)-nvl(o.sumtime,0)-nvl(e.overtime,0)+nvl(d.sumtime,0) 上月剩余调休,")
		.append("                            '' 出差新增调休,")
		.append("                            '' 说明,")
		.append("                            e.overtime 加班,")
		.append("                            e.c_oarunid 说明6,")
		.append("                            l.bxjiadesc 补休,")
		.append("                            l.c_oarunid 说明7,")
		.append("                           nvl(g.i_deferred_time, 0)-nvl(n.sumtime,0)-nvl(o.sumtime,0) 本月剩余调休")
		.append("                           ,nvl(C_BEFORE_DEFERRED,0)-nvl(e.overtime,0)+nvl(l.bxjiadesc,0)-nvl(n.sumtime,0)-nvl(o.sumtime,0) 上月剩余调休_oa,C_BEFORE_DEFERRED-nvl(n.sumtime,0)-nvl(o.sumtime,0) 本月剩余调休_oa")
		.append("                            ,p.d_grg_begindate,q.c_grg_enddate,base.c_deptname,base.c_userid,base.c_innernum,sysuser.c_order,sysuser.c_attendancetype")
		.append("              from (select atd.c_userid, count(*) days")
		.append("                      from oa_attendancerecord atd")
		.append("                     where c_worktype = '0'")
		.append("                       and c_workdate >= '").append(sdate).append("'")
		.append("                       and c_workdate <= '").append(edate).append("'")
		.append("                     group by atd.c_userid) a,")
		.append("                   (select c_userid,")
		.append("                           round(sum((to_date(c_workdate || ' ' || mindate,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                     to_date(c_workdate || ' ' || c_stime,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60),")
		.append("                                 2) times")
		.append("                      from oa_attendancerecord t")
		.append("                     where t.morn_islast = '迟到'")
		.append("                       and t.c_worktype = '0'")
		.append("                       and c_workdate >= '").append(sdate).append("'")
		.append("                       and c_workdate <= '").append(edate).append("'")
		.append("                     group by c_userid) b,")
		.append("                   (select c_userid,")
		.append("                           round(sum((to_date(c_workdate || ' ' || c_etime,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                     to_date(c_workdate || ' ' || maxdate,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60),")
		.append("                                 2) times")
		.append("                      from oa_attendancerecord t")
		.append("                     where t.after_isleave = '早退'")
		.append("                       and t.c_worktype = '0'")
		.append("                       and c_workdate >= '").append(sdate).append("'")
		.append("                       and c_workdate <= '").append(edate).append("'")
		.append("                     group by c_userid) c,")
		.append("                   (select c_userid, round(sum(t.i_sumtime) / 8, 4) sumtime")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and c_enddate <= '").append(edate).append("'")
		.append("                       and c_type = '补休假'")
		.append("                     group by c_userid) d,")
		.append("                   (select distinct c_userid,")
		.append("                                    round(sum(i_sumtime) / 8, 4) overtime,")
		.append("                                    replace(to_char(wm_concat(distinct c_oarunid)),")
		.append("                                            ',',")
		.append("                                            '@') c_oarunid")
		.append("                      from oa_overrecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                     group by c_userid) e,")
		.append("                   (select distinct c_userid,")
		.append("                                    sum(i_sumtime) sumtime,")
		.append("                                    replace(to_char(wm_concat(distinct c_oarunid)),")
		.append("                                            ',',")
		.append("                                            '@') c_oarunid")
		.append("                      from oa_triprecord")
		.append("                     where c_startdate >= '").append(sdate).append("'")
		.append("                       and c_enddate <= '").append(edate).append("'")
		.append("                     group by c_userid) f,")
		.append("                   (select distinct c_userid, t.i_deferred_time")
		.append("                      from usr_hols t) g,")
		.append("                   (select t.c_username,")
		.append("                           t.c_userid,t.c_deptname,t.c_innernum")
		.append("                      from oa_attendancerecord t")
		.append("                     where t.c_worktype = '0'")
		.append("                       and c_workdate >= '").append(sdate).append("'")
		.append("                       and c_workdate <= '").append(edate).append("') base,")
		//.append("                       (select c_userid,c_username,c_order,c_attendancetype from sys_user where c_userid!='admin' and to_char(d_grg_begindate,'yyyy-mm-dd')<='").append(edate).append("') sysuser,")
		.append("                       (select c_userid,c_username,c_order,c_attendancetype from sys_user where c_order is not null) sysuser,")
		.append("                       (select c_userid,")
		.append("                           round(sum(i_sumtime)/8,4) sjiadesc,")
		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                       and t.c_type = '事假'")
		.append("                     group by c_userid) h,")
		.append("                   (select c_userid,")
		.append("                           round(sum(i_sumtime)/8,4) bjiadesc,")
		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                       and t.c_type = '病假'")
		.append("                     group by c_userid) i,")
		.append("                   (select c_userid,")
		.append("                           round(sum(i_sumtime)/8,4) njiadesc,")
		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                       and t.c_type = '年假'")
		.append("                     group by c_userid) j,")
		.append("                   (select c_userid,")
		.append("                           round(sum(i_sumtime)/8,4) otherdesc,")
		.append("                           replace(to_char(wm_concat(distinct decode(c_oarunid,null,'',c_oarunid || '@') || c_type)),',','@') c_oarunid")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                       and t.c_type not in ('事假', '病假', '年假','补休假')")
		.append("                     group by c_userid) k,")
		.append("                     (select c_userid,")
		.append("                           round(sum(i_sumtime)/8,4) bxjiadesc,")
		.append("                           replace(to_char(wm_concat(distinct c_oarunid)),',','@') c_oarunid")
		.append("                      from oa_leaverecord t")
		.append("                     where t.c_startdate >= '").append(sdate).append("'")
		.append("                       and t.c_enddate <= '").append(edate).append("'")
		.append("                       and t.c_type='补休假'")
		.append("                     group by c_userid)l,")
		.append("                     (select count(*)mday from (")
		.append("                            select to_char(c_date,'yyyy-mm-dd')c_date from (")
		.append("                            select to_date('").append(sdate).append("', ' yyyy-MM-dd ') + rownum - 1 c_date from user_objects ")
		.append("                            where rownum <= (to_date('").append(edate).append("', ' yyyy-MM-dd') - to_date('").append(sdate).append("', 'yyyy-MM-dd') + 1))a")
		.append("                            where (select (to_char(to_date(to_char(c_date,'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss'),'D')-1) from dual)<>'0' ")
		.append("                            and (select (to_char(to_date(to_char(c_date,'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss'),'D')-1) from dual)<>'6'))m,")
		.append("                      (select round(sum(i_sumtime)/8,4) sumtime,c_userid from oa_overrecord where substr(c_startdate,1,10)>'").append(edate).append("' group by c_userid)n,")
		.append("                      (select round(sum(i_sumtime)/8,4) sumtime,c_userid from oa_leaverecord where c_startdate>'").append(edate).append("' and c_type='补休假' group by c_userid) o,")
		.append("                      (select c_userid,d_grg_begindate from sys_user t where to_char(t.d_grg_begindate,'yyyy-mm-dd')>='").append(sdate).append("' and to_char(t.d_grg_begindate,'yyyy-mm-dd')<='").append(edate).append("') p,")
		.append("                      (select c_userid,c_grg_enddate from sys_user t where to_char(t.c_grg_enddate,'yyyy-mm-dd')>='").append(sdate).append("' and to_char(t.c_grg_enddate,'yyyy-mm-dd')<='").append(edate).append("') q,")
		.append("                      (select c_userid,C_BEFORE_DEFERRED from usr_hols )w")
		.append("             where sysuser.c_userid=base.c_userid(+)")
		.append("               and sysuser.c_userid = a.c_userid(+)")
		.append("               and sysuser.c_userid = b.c_userid(+)")
		.append("               and sysuser.c_userid = c.c_userid(+)")
		.append("               and sysuser.c_userid = d.c_userid(+)")
		.append("               and sysuser.c_userid = e.c_userid(+)")
		.append("               and sysuser.c_userid = f.c_userid(+)")
		.append("               and sysuser.c_userid = g.c_userid(+)")
		.append("               and sysuser.c_userid = h.c_userid(+)")
		.append("               and sysuser.c_userid = i.c_userid(+)")
		.append("               and sysuser.c_userid = j.c_userid(+)")
		.append("               and sysuser.c_userid = k.c_userid(+)")
		.append("               and sysuser.c_userid = l.c_userid(+)")
		.append("               and sysuser.c_userid = n.c_userid(+)")
		.append("               and sysuser.c_userid = o.c_userid(+)")
		.append("               and sysuser.c_userid = p.c_userid(+)")
		.append("               and sysuser.c_userid = q.c_userid(+)")
		.append("               and sysuser.c_userid = w.c_userid(+)")
		.append("               order by to_number(sysuser.c_order)");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportOutAttendanceData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct base.c_username username,")
		.append("                (a.days - nvl(f.fdays, 0)) workday,")
		.append("                b.times mornlast,")
		.append("                c.times afterlast,")
		.append("                replace(c.c_time3, ',', '@') desc1,")
		.append("                d.times leave,")
		.append("                d.c_time3 desc2,")
		.append("                e.countnum nowork,")
		.append("                e.c desc3,")
		.append("                g.sumcount catchnum,")
		.append("                h.sjiadesc sjia,")
		.append("                h.c_oarunid sjiadesc,")
		.append("                i.bjiadesc bjia,")
		.append("                i.c_oarunid bjiadesc,")
		.append("                j.njiadesc njia,")
		.append("                j.c_oarunid njiadesc,")
		.append("                k.otherdesc otherjia,")
		.append("                k.c_oarunid otherdesc")
		.append("")
		.append("  from oa_attendancerecord base,")
		.append("       (select atd.c_userid, count(*) days")
		.append("          from oa_attendancerecord atd")
		.append("         where c_worktype = '0'")
		.append("           and c_attendanceType = '1'")
		.append("		   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("         group by atd.c_userid) a,")
		.append("       (select c_userid,")
		.append("               sum((to_date(c_workdate || ' ' || mindate,")
		.append("                            'yyyy-mm-dd hh24:mi:ss') -")
		.append("                   to_date(c_workdate || ' ' || c_stime,")
		.append("                            'yyyy-mm-dd hh24:mi:ss')) * 24 * 60) times")
		.append("          from oa_attendancerecord t")
		.append("         where t.morn_islast = '迟到'")
		.append("           and t.c_worktype = '0'")
		.append("           and c_attendanceType = '1'")
		.append("		   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("         group by c_userid) b,")
		.append("       (select *")
		.append("          from (select c_userid,")
		.append("                       case")
		.append("                         when c_time2 is null then")
		.append("                          to_char(sum(to_date(c_workdate || ' ' || c_time3,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                      to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60)")
		.append("                         else")
		.append("                          '0'")
		.append("                       end times,")
		.append("                       c_workdate || ' ' || c_time3 c_time3")
		.append("                  from oa_attendancerecord t")
		.append("                 where t.noon_islast = '迟到'")
		.append("                   and c_time3 is not null")
		.append("                   and t.c_worktype = '0'")
		.append("                   and c_attendanceType = '1'")
		.append("				   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                 group by c_userid, c_time2, c_time3, c_workdate)")
		.append("         where times != 0")
		.append("        union all")
		.append("        select t.c_userid, '无打卡记录', wm_concat(c_workdate)")
		.append("          from oa_attendancerecord t")
		.append("         where t.noon_islast = '无记录'")
		.append("           and c_worktype = '0'")
		.append("           and c_attendanceType = '1'")
		.append("		   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("         group by c_userid) c,")
		.append("       (select c_userid,")
		.append("               case")
		.append("                 when c_time2 is not null then")
		.append("                  sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                              'yyyy-mm-dd hh24:mi:ss') -")
		.append("                      to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                              'yyyy-mm-dd hh24:mi:ss')) * 24")
		.append("                 else")
		.append("                  case")
		.append("                    when c_time3 is not null then")
		.append("                     case")
		.append("                       when (sum(to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                         'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                 to_date(c_workdate || ' ' || t.c_time3,")
		.append("                                         'yyyy-mm-dd hh24:mi:ss')) * 24) > 0 then")
		.append("                        sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                                    'yyyy-mm-dd hh24:mi:ss') -")
		.append("                            to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                    'yyyy-mm-dd hh24:mi:ss')) * 24")
		.append("                       else")
		.append("                        trunc(sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                                          'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                  to_date(c_workdate || ' ' || t.c_time3,")
		.append("                                          'yyyy-mm-dd hh24:mi:ss')) * 24,")
		.append("                              2)")
		.append("                     end")
		.append("                    else")
		.append("                     0")
		.append("                  end")
		.append("               end times,")
		.append("               case")
		.append("                 when c_time2 is not null then")
		.append("                  c_workdate || ' ' || c_time2")
		.append("                 else")
		.append("                  c_workdate || ' ' || c_time3")
		.append("               end c_time3")
		.append("          from oa_attendancerecord t")
		.append("         where t.after_isleave = '早退'")
		.append("           and c_worktype = '0'")
		.append("           and c_attendanceType = '1'")
		.append("		   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("           and (c_time2 is not null or c_time3 is not null)")
		.append("         group by c_userid, c_time2, c_time3, c_workdate) d,")
		.append("       (select c_userid, to_char(wm_concat(c)) c, count(*) countnum")
		.append("          from (select distinct aa.c_userid, bb.c")
		.append("                  from (select c")
		.append("                          from (with test as (select (select distinct to_char(wm_concat(c_workdate))")
		.append("                                                        from (select c_workdate")
		.append("                                                                from oa_attendancerecord")
		.append("                                                               where c_worktype = '0'")
		.append("                                                                 and c_attendanceType = '1'")
		.append("																 and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                                                               group by c_workdate")
		.append("                                                               order by c_workdate)) c")
		.append("                                                from dual)")
		.append("                                 select substr(t.ca,")
		.append("                                               instr(t.ca, ',', 1, c.lv) + 1,")
		.append("                                               instr(t.ca, ',', 1, c.lv + 1) -")
		.append("                                               (instr(t.ca, ',', 1, c.lv) + 1)) AS c")
		.append("                                   from (select ',' || c || ',' AS ca,")
		.append("                                                length(c || ',') -")
		.append("                                                nvl(length(REPLACE(c, ',')), 0) AS cnt")
		.append("                                           FROM test) t,")
		.append("                                        (select LEVEL lv")
		.append("                                           from dual")
		.append("                                         CONNECT BY LEVEL <= 500) c")
		.append("                                  where c.lv <= t.cnt)")
		.append("                        ) bb,")
		.append("                       oa_attendancerecord aa")
		.append("                 where c_worktype = '0'")
		.append("                   and c_attendanceType = '1'")
		.append("				   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                minus")
		.append("                select c_userid, c_workdate")
		.append("                  from oa_attendancerecord aa")
		.append("                 where c_worktype = '0'")
		.append("                   and c_attendanceType = '1'")
		.append("				   and c_workdate between '").append(sdate).append("' and '").append(edate).append("') ta")
		.append("         where ta.c not in (select c_startdate")
		.append("                              from oa_leaverecord")
		.append("                             where ta.c_userid = c_userid)")
		.append("         group by c_userid) e,")
		.append("       (select count(*) * 0.5 fdays, c_userid")
		.append("          from (select c_userid,")
		.append("                       case")
		.append("                         when c_time2 is not null then")
		.append("                          sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                                      'yyyy-mm-dd hh24:mi:ss') -")
		.append("                              to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                      'yyyy-mm-dd hh24:mi:ss')) * 24")
		.append("                         else")
		.append("                          case")
		.append("                            when c_time3 is not null then")
		.append("                             case")
		.append("                               when (sum(to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                                 'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                         to_date(c_workdate || ' ' || t.c_time3,")
		.append("                                                 'yyyy-mm-dd hh24:mi:ss')) * 24) > 0 then")
		.append("                                sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                                            'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                    to_date(c_workdate || ' ' || t.c_mtime2,")
		.append("                                            'yyyy-mm-dd hh24:mi:ss')) * 24")
		.append("                               else")
		.append("                                trunc(sum(to_date(c_workdate || ' ' || t.c_etime,")
		.append("                                                  'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                          to_date(c_workdate || ' ' || t.c_time3,")
		.append("                                                  'yyyy-mm-dd hh24:mi:ss')) * 24,")
		.append("                                      2)")
		.append("                             end")
		.append("                            else")
		.append("                             0")
		.append("                          end")
		.append("                       end times")
		.append("                  from oa_attendancerecord t")
		.append("                 where t.after_isleave = '早退'")
		.append("                   and c_worktype = '0'")
		.append("                   and c_attendanceType = '1'")
		.append("				   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                   and (c_time2 is not null or c_time3 is not null)")
		.append("                 group by c_userid, c_time2, c_time3)")
		.append("         group by c_userid) f,")
		.append("       (select distinct nvl(a.countnum, 0) + nvl(b.countnum, 0) sumcount,")
		.append("                        a.c_userid")
		.append("          from (select count(*) countnum, c_userid")
		.append("                  from oa_attendancerecord t")
		.append("                 where t.morn_islast = '迟到'")
		.append("                   and t.c_worktype = '0'")
		.append("                   and c_attendanceType = '1'")
		.append("				   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                 group by c_userid) a,")
		.append("               (select c_userid, count(*) countnum")
		.append("                  from (select *")
		.append("                          from (select c_userid,")
		.append("                                       case")
		.append("                                         when c_time2 is null then")
		.append("                                          to_char(sum(to_date(c_workdate || ' ' ||")
		.append("                                                              c_time3,")
		.append("                                                              'yyyy-mm-dd hh24:mi:ss') -")
		.append("                                                      to_date(c_workdate || ' ' ||")
		.append("                                                              t.c_mtime2,")
		.append("                                                              'yyyy-mm-dd hh24:mi:ss')) * 24 * 60)")
		.append("                                         else")
		.append("                                          '0'")
		.append("                                       end times,")
		.append("                                       c_time3")
		.append("                                  from oa_attendancerecord t")
		.append("                                 where t.noon_islast = '迟到'")
		.append("                                   and c_time3 is not null")
		.append("                                   and t.c_worktype = '0'")
		.append("                                   and c_attendanceType = '1'")
		.append("								   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("                                 group by c_userid, c_time2, c_time3)")
		.append("                         where times != 0")
		.append("                        union all")
		.append("                        select t.c_userid, '无打卡记录', c_workdate")
		.append("                          from oa_attendancerecord t")
		.append("                         where t.noon_islast = '无记录'")
		.append("                           and c_worktype = '0'")
		.append("                           and c_attendanceType = '1'")
		.append("						   and c_workdate between '").append(sdate).append("' and '").append(edate).append("')")
		.append("                 group by c_userid) b")
		.append("         where a.c_userid = b.c_userid(+)) g,")
		.append("       (select c_userid,")
		.append("               wm_concat(distinct c_startdate || '|' || round(i_sumtime / 8, 2) || '天') sjiadesc,")
		.append("               wm_concat(distinct c_oarunid) c_oarunid")
		.append("          from oa_leaverecord t")
		.append("         where t.c_startdate >= '").append(sdate).append("'")
		.append("           and t.c_enddate <= '").append(edate).append("'")
		.append("           and t.c_type = '事假'")
		.append("         group by c_userid) h,")
		.append("       (select c_userid,")
		.append("               wm_concat(distinct c_startdate || '|' || round(i_sumtime / 8, 2) || '天') bjiadesc,")
		.append("               wm_concat(distinct c_oarunid) c_oarunid")
		.append("          from oa_leaverecord t")
		.append("         where t.c_startdate >= '").append(sdate).append("'")
		.append("           and t.c_enddate <= '").append(edate).append("'")
		.append("           and t.c_type = '病假'")
		.append("         group by c_userid) i,")
		.append("       (select c_userid,")
		.append("               wm_concat(distinct c_startdate || '|' || round(i_sumtime / 8, 2) || '天') njiadesc,")
		.append("               wm_concat(distinct c_oarunid) c_oarunid")
		.append("          from oa_leaverecord t")
		.append("         where t.c_startdate >= '").append(sdate).append("'")
		.append("           and t.c_enddate <= '").append(edate).append("'")
		.append("           and t.c_type = '年假'")
		.append("         group by c_userid) j,")
		.append("       (select c_userid,")
		.append("               wm_concat(distinct c_startdate || '|' || round(i_sumtime / 8, 2) || '天' ||")
		.append("                         decode(c_type, null, '', '|' || c_type)) otherdesc,")
		.append("               wm_concat(distinct c_oarunid) c_oarunid")
		.append("          from oa_leaverecord t")
		.append("         where t.c_startdate >= '").append(sdate).append("'")
		.append("           and t.c_enddate <= '").append(edate).append("'")
		.append("           and t.c_type not in ('事假', '病假', '年假')")
		.append("         group by c_userid) k")
		.append(" where base.c_worktype = '0'")
		.append("   and base.c_attendanceType = '1'")
		.append("   and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append("   and base.c_userid = a.c_userid(+)")
		.append("   and base.c_userid = b.c_userid(+)")
		.append("   and base.c_userid = c.c_userid(+)")
		.append("   and base.c_userid = d.c_userid(+)")
		.append("   and base.c_userid = e.c_userid(+)")
		.append("   and base.c_userid = f.c_userid(+)")
		.append("   and base.c_userid = g.c_userid(+)")
		.append("   and base.c_userid = h.c_userid(+)")
		.append("   and base.c_userid = i.c_userid(+)")
		.append("   and base.c_userid = j.c_userid(+)")
		.append("   and base.c_userid = k.c_userid(+)")
		.append(" order by username");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 导出外派明细表数据
	 * @return
	 */
	public List exportAttendanceDetailTableData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.c_username,")
		.append("       t.c_workdate,")
		.append("       t.c_stime,")
		.append("       case")
		.append("         when c_time1 is null then")
		.append("        t.mindate")
		.append("         else")
		.append("          c_time1")
		.append("       end c_time1,")
		.append("       t.morn_islast,")
		.append("       t.c_etime,")
		.append("       case")
		.append("         when c_time2 is null then")
		.append("          c_time3")
		.append("         else")
		.append("          c_time2")
		.append("       end c_time2,")
		.append("       t.after_isleave")
		.append("  from oa_attendancerecord t")
		.append(" where t.c_worktype = '0' and c_attendanceType = '1'")
		.append("   and (t.morn_islast = '迟到'")
		.append("    or t.after_isleave = '早退')")
		.append("	 and c_workdate between '").append(sdate).append("' and '").append(edate).append("'")
		.append(" order by morn_islast, c_username, c_workdate");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 到外派异常打卡数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportAttendanceCatchData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("select c_username,c_workdate || ' ' || c_time2 c_time2 from (")
		.append("  select c_username,")
		.append("         case")
		.append("           when c_time2 is null then")
		.append("            c_time3")
		.append("           else")
		.append("            c_time2")
		.append("         end c_time2,")
		.append("         c_workdate,")
		.append("         '11:00:00' stime,")
		.append("         '15:00:00' etime,t.noon_isleave,t.noon_islast")
		.append("    from oa_attendancerecord t")
		.append("   where t.c_worktype='0' and c_attendanceType = '1' and (t.noon_isleave = '早退' or t.noon_islast = '迟到')")
		.append("	   and c_workdate between '").append(sdate).append("' and '").append(edate).append("')")
		.append("   where c_time2 between stime and etime");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	/**
	 * 广发考勤列表
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getAttendanceOut(String sdate,String edate){
		List list = exportOutAttendanceData(sdate,edate);
		return list;
	}
	
	/**
	 * 公司考勤列表
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getAttendanceInner(String sdate,String edate){
		List list = exportAllAttendanceData(sdate,edate);
		return list;
	}
	
	public List getExpenseData(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (")
		.append(" select c_username,c_workdate,c_stime,c_etime,mindate,maxdate,25 money from oa_attendancerecord tt ")
		.append(" where c_attendancetype!='0' and maxdate<'20:00:00'")
		.append(" and c_workdate between '").append(sdate).append("' and '").append(edate).append("' and c_worktype='0'")
		.append(" union all")
		.append(" select c_username,c_workdate,c_stime,c_etime,mindate,maxdate,50 money from oa_attendancerecord tt ")
		.append(" where c_etime='17:30:00' and maxdate>='20:00:00'")
		.append(" and c_workdate between '").append(sdate).append("' and '").append(edate).append("' and c_worktype='0')")
		.append(" order by c_username,c_workdate");
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return list;
	}
	
	public boolean updateCardDealStatus(String sdate,String edate){
		StringBuffer sql = new StringBuffer();
		sql.append("update oa_cardrecord set c_dealstatus='1' where to_char(d_signtime,'yyyy-mm-dd') between '")
		.append(sdate).append("' and '").append(edate).append("'");
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 执行考勤存储过程
	 * @return
	 */
	public boolean execAttendanceProc(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call attendance_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean execTimeAttendanceProc(String sdate,String edate){
		try{
		SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call time_proc(?,?)}");
		sqlQuery.setString(0, sdate);
		sqlQuery.setString(1, edate);
		sqlQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean execTripProc(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call gettriprecord_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean execOvertimeProc(){
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call getovertimerecord_proc}").executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean updateUserHols(String userid,String deferred){
		StringBuffer sql = new StringBuffer();
		sql.append("update usr_hols set c_before_deferred='").append(deferred).append("' where c_userid='").append(userid).append("'");
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}
}
