package cn.grgbanking.feeltm.cardRecord.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.commonPlatform.monthlyManage.constants.MonthlyManagerConstants;
import cn.grgbanking.feeltm.cardRecord.domain.CardRecord;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

/**
 * 考勤记录
 * 
 * @author zzhui1
 * 
 */
@Repository
public class CardRecordDao extends BaseDao<CardRecord> {
	/**
	 * 月度考勤状态的数据
	 * 
	 * @param yyyyMM
	 *            当前日期的上一月
	 * @return List（考勤日期yyyy-MM-dd/部门名/考勤状态/迟到.早退人数）
	 */
	public List<Object> getMonthlyAttendance(String yyyyMM) {
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" SELECT ");
		sbdHql.append(" TO_CHAR(t.attendanceDate, 'yyyy-MM-dd') as attendanceDate,");
		sbdHql.append(" TRIM(t.deptname) as deptname,");
		sbdHql.append(" t.attendanceStatus as status,");
		sbdHql.append(" COUNT(t.attendanceStatus) as cnt");
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE TO_CHAR(t.attendanceDate, 'yyyyMM') = ? ");
		sbdHql.append(" AND t.attendanceStatus IN (?, ?)");
		sbdHql.append(" GROUP BY t.attendanceDate,t.deptname,t.attendanceStatus");
		sbdHql.append(" ORDER BY t.attendanceDate ");
		List<Object> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{yyyyMM, MonthlyManagerConstants.ATTENDACE_LATE_KEY,
				MonthlyManagerConstants.ATTENDACE_EARLY_KEY});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<Object>();
		}
	}
	
	/**
	 * 取出广发和产业园的考勤数据
	 * @param yyyy_MM_dd 
	 * @return
	 */
	public List<CardRecord> getCardRecordList(String yyyy_MM_dd){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE t.attendanceDate = to_date(?,'yyyy-MM-dd') ");
		sbdHql.append(" AND t.type != '2' ");
		List<CardRecord> result = this.getHibernateTemplate().find(sbdHql.toString(),yyyy_MM_dd);
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<CardRecord>();
		}
	}
	
	/**
	 * 根据年月、考勤状态和部门，获取当月该部门考勤状态的总数
	 * @param yyyyMM
	 * @param status
	 * @param depyName
	 * @return 考勤状态的总数
	 */
	public long getCountAttendanceStatus(String yyyyMM,int status,String depyName){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append("select count(*) FROM CardRecord t where to_char(t.attendanceDate,'yyyyMM') = ? and t.attendanceStatus = ? and t.deptname = ?");
		List<Object> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{yyyyMM,status,depyName});
		if (result != null && result.size() > 0) {
			Object obj = result.get(0);
			return  (Long) obj;
		} else {
			return 0;
		}
	}
	
	/**
	 * 取出正确的上班和下班时间的记录
	 * @param cardRecord
	 * @return 正确的上班和下班时间的记录（最多2条记录）
	 */
	public List<CardRecord> getStandardStatusByIdDate(CardRecord cardRecord, String saparationTime){
		String yyyy_MM_dd = DateUtil.getDateString(cardRecord.getAttendanceDate());
		String userId = cardRecord.getUserid();
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE t.attendanceDate = to_date(?,'yyyy-MM-dd') ");
		sbdHql.append(" AND t.userid = ? ");
		sbdHql.append(" AND TO_CHAR(t.signtime,'hh24:mi:ss') <= ? ");
		sbdHql.append(" ORDER BY t.type,t.signtime ");
		//取正确的上班时间
		List<CardRecord> result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{yyyy_MM_dd,userId,saparationTime});
		List<CardRecord> list = new ArrayList<CardRecord>();
		CardRecord item = new CardRecord();
		if (result != null && result.size() > 0) {
			list.add(result.get(0));
		} 
		
		sbdHql = new StringBuilder();
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE t.attendanceDate = to_date(?,'yyyy-MM-dd') ");
		sbdHql.append(" AND t.userid = ? ");
		sbdHql.append(" AND TO_CHAR(t.signtime,'hh24:mi:ss') > ? ");
		sbdHql.append(" ORDER BY t.type,t.signtime desc ");
		//取正确的下班时间
		result = this.getHibernateTemplate().find(sbdHql.toString(),
				new Object[]{yyyy_MM_dd,userId,saparationTime});
		if (result != null && result.size() > 0) {
			list.add(result.get(0));
		} 
		return list;
	}
	
	/**
	 * 取得当天的考勤记录
	 * @param userId 用户ID
	 * @param yyyy_MM_dd 考勤日期
	 * @return 当天的考勤记录
	 */
	public List<CardRecord> getAttendanceByUseridDate(String userId, String yyyy_MM_dd){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE t.attendanceDate = to_date(?,'yyyy-MM-dd') ");
		sbdHql.append(" AND t.userid = ? ");
		sbdHql.append(" AND t.attendanceStatus not in (-1, 0) ");
		List<CardRecord> result = this.getHibernateTemplate()
				.find(sbdHql.toString(),new Object[]{yyyy_MM_dd,userId});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<CardRecord>();
		}
	}
	
	/**
	 * 列出所有的考勤数据
	 * @param pageNum
	 * @param pageSize
	 * @param userid 姓名
	 * @param deptname 部门
	 * @param groupname 项目名称
	 * @param signTime 开始日期
	 * @param signEndTime  结束日期
	 * @param userModel
	 * @return
	 */
	public Page getPage(int pageNum,int pageSize,String username,String deptname,String groupname,String signTime,String signEndTime,UserModel userModel){
		String hql = "from CardRecord card where attendanceStatus!=-1";
		//加控制权限
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"cardrecord.userdata.all")){//查看全部数据
			
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"cardrecord.userdata.dept")){
			hql += " and card.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"cardrecord.userdata.project")){
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and card.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and card.userid='"+userModel.getUserid()+"'";
		}
		//查询条件
		if(groupname!=null && !groupname.equals("")){
			hql += " and card.grpname like '%"+groupname.trim()+"%'";
		}
		if (username != null && !username.equals("")){
			hql += " and (card.username like '%"+username.trim()+"%' or card.userid like '%"+username+"%')";
		}
		if (deptname != null && !deptname.equals("全选") && !deptname.equals("")){
			hql += " and card.deptname like '%"+ deptname.trim()+"%'";
		}
		if(signTime!=null){
			hql += " and to_char(to_date('"+ signTime+"','yyyy-MM-dd'),'yyyy-mm-dd')<=to_char(card.signtime,'yyyy-mm-dd')";
		}
        if(signEndTime!=null){
        	hql += " and to_char(to_date('"+ signEndTime+"','yyyy-MM-dd'),'yyyy-mm-dd')>=to_char(card.signtime,'yyyy-mm-dd')";
        }
        hql += " order by card.deptname,card.userid,card.signtime desc";
        return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
	
	/**
	 * 获取广发未处理考勤状态的考勤日期数据
	 * @return
	 */
	public List<String> getGFCardRecordList(){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM CardRecord t ");
		sbdHql.append(" WHERE t.attendanceStatus = '0'");
		sbdHql.append(" AND t.type = '1' ");
		List<CardRecord> result = this.getHibernateTemplate().find(sbdHql.toString());
		List<String> list = new ArrayList<String>();
		for(CardRecord card:result){
			list.add(DateUtil.getDateString(card.getAttendanceDate()));
		}
		return list;
	}
}
