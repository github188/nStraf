package cn.grgbanking.feeltm.signrecord.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SignRecord;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.signrecord.domain.SignBind;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("signRecordDao")
public class SignRecordDao extends BaseDao<SignRecord> {
	
	@Autowired
	private StaffInfoDao staffInfoDao;
	
	public Page getPage(int pageNum,int pageSize,UserModel userModel){
		String hql = "from SignRecord s where 1=1";
		if(userModel.getLevel()==3){
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				hql += " and s.userId = '"+userModel.getUserid()+"' ";
			}
		}
		hql += " order by s.signTime desc";
		
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
	
	public Page getPageByCondition(SignRecord signRecord,int pageNum,int pageSize,String signTime,String signEndTime,UserModel userModel){
		String hql = "from SignRecord sign where 1=1";
		if(userModel.getLevel()==3){
			if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
				hql += " and sign.userId='"+userModel.getUserid()+"'";
			}
		}
		if(signRecord.getUserId()!=null){
			hql += " and sign.userId='"+signRecord.getUserId()+"'";
		}
		else{
			if(signRecord.getDeptName()!=null && !signRecord.getDeptName().equals("")){
				hql += " and sign.userId in (select s.userid from SysUser s where s.deptName='"+signRecord.getDeptName()+"')";
				/*hql += " and s.deptName='"+signRecord.getDeptName()+"'";*/
			}
			if(signRecord.getGrpName()!=null && !signRecord.getGrpName().equals("")){
				hql += " and sign.userId in (select s.userid from SysUser s where s.userid in (select g.userid from UserProject g where g.project.id='"+signRecord.getGrpName()+"'))";
			}
		}
       if(signTime!=null){
    	   hql += " and to_date('"+ signTime+"','yyyy-MM-dd')<=sign.signTime";
		}
       if(signEndTime!=null){
    	   hql += " and to_date('"+ signEndTime+"','yyyy-MM-dd')>=sign.signTime";
       }
       
       return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
	
	
	public Page getPageByDate(int pageNum,int pageSize,String signTime,String signEndTime){
		String hql = "from SignRecord record where 1=1";
		if(signTime!=null && signTime.length()>0){
			signTime = signTime+" 00:00:00";
			hql += " and to_date('"+ signTime+"','yyyy-MM-dd HH24:mi:ss')<=record.signTime";
		}
		if(signEndTime!=null && signEndTime.length()>0){
			signEndTime=signEndTime+" 23:59:59";
			hql += " and to_date('"+ signEndTime+"','yyyy-MM-dd HH24:mi:ss')>=record.signTime";
		}
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
	
	/**
	 * 根据日期及项目组列出所有移动签到数据
	 * @param sdate
	 * @param edate
	 * @param prjname
	 * @return
	 */
	public List getMobileExportData(String sdate,String edate,String prjname) {
		//改用存储过程执行，存储过程自动将数据查询、对数据进行处理并保存到临时存储oa_signrecord_export表中，数据导出后，删除该表中的数据
		try{
			SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call exportsignrecord_proc(?,?,?)}");
			sqlQuery.setString(0, sdate);
			sqlQuery.setString(1, edate);
			sqlQuery.setString(2, prjname);
			sqlQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		String sql = "select * from oa_signrecord_export order by c_userid,d_signtime";
		List records = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		try{
			sql = "delete from oa_signrecord_export";
			getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return records;
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年6月9日
	 */
	public Page findAllBind(String deptname,String grpname,String userid,String status,int pageNum, int pageSize) {
		String hql = "from SignBind b where 1=1";
		if(userid==null || "".equals(userid)){
			if(deptname!=null && !"".equals(deptname)){
				hql += " and b.userid in (select s.userid from SysUser s where s.deptName='"+deptname+"')";
			}
			if(grpname!=null && !"".equals(grpname)){
				hql += " and b.userid in (select g.userid from UserProject g where g.project.id='"+grpname+"')";
			}
		}
		if(userid!=null && !"".equals(userid)){
			hql += " and (b.userid like '%"+userid+"%' or b.username like '%"+userid+"%')";
		}
		if(status!=null && !"".equals(status)){
			hql += " and b.status = '"+status+"'";
		}
		hql += " order by b.bindTime desc";
		Page page = null;
		page = ((BaseHibernateTemplate)this.getHibernateTemplate()).getPage(hql, pageNum, pageSize);
		return page;
	}

	/**
	 * @param users
	 * lhyan3
	 * 2014年6月9日
	 */
	public void releaseBind(List<SignBind> binds) {
		this.getHibernateTemplate().saveOrUpdateAll(binds);
	}

	/**
	 * @param userid
	 * lhyan3
	 * 2014年6月9日
	 */
	@SuppressWarnings("unchecked")
	public SignBind getBindByUserid(String userid) {
		List<SignBind> list = this.getHibernateTemplate().find("from SignBind b where b.userid='"+userid+"'");
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public Page getPageByCondition(String deptName, String grpName,
			String userId, int pagenum, int pagesize, String signTime,
			String signEndTime, UserModel userModel, String approvePerson, String approveStatus) {
		String hql = "from SignRecord sign where (sign.type='2'or sign.type='3') ";
		//数据权限
//		if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
//			if(UserRoleConfig.isDeptManageGroup(userModel)){
//				//部门经理
//				hql += " and (sign.deptName like '%"+staffInfoDao.getDeptNameValueByUserId(userModel.getUserid()).trim()+"%'  or sign.approvePerson ='"+userModel.getUserid()+"' )";
//			}
//			else if(UserRoleConfig.isProjectManageGroup(userModel)){
//				//项目经理
//				String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
//				hql += " and (sign.userId in ("+grp+") or sign.approvePerson ='"+userModel.getUserid()+"' )";
//			}else{
//				//非项目经理
//				hql += " and sign.userId ='"+userModel.getUserid()+"'";
//			}
//		}
		
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.all")){//查看全部数据
			
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.dept")){
			hql += " and (sign.userId in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"') or sign.approvePerson ='"+userModel.getUserid()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.project")){
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and (sign.userId in (select s.userid from SysUser s where s.userid in ("+grp+")) or sign.approvePerson ='"+userModel.getUserid()+"')";
		}else{
			if(StringUtils.isNotBlank(approvePerson)){
				hql += " and sign.approvePerson in (select s.userid from SysUser s where s.userid like '%"+approvePerson+"%' or s.username like '%"+approvePerson+"%')";
			}else{
				hql +=" and sign.userId='"+userModel.getUserid()+"'";
			}
		}
		//查询条件
		if(userId!=null && !"".equals(userId)){
			hql += " and (sign.userId like '%"+userId+"%' or sign.username like '%"+userId+"%')";
		}else{
			if(deptName!=null && !deptName.equals("")){
				/*hql += " and sign.userId in (select s.userid from SysUser s where s.deptName='"+deptName+"')";*/
				hql += " and sign.deptName like '%"+deptName+"%'";
			}
			if(grpName!=null && !grpName.equals("")){
				/*hql += " and sign.userId in (select s.userid from SysUser s where s.userid in (select g.userid from UserProject g where g.project.id='"+grpName+"'))";*/
				hql += " and sign.grpName like '%"+grpName+"%'";
			}
		}
		if(StringUtils.isNotBlank(approveStatus)){
			hql += " and sign.approveStatus like '%"+approveStatus+"%'";
		}
		if(StringUtils.isNotBlank(approvePerson)){
			hql += " and sign.approvePerson in (select s.userid from SysUser s where s.userid like '%"+approvePerson+"%' or s.username like '%"+approvePerson+"%')";
		}
       if(StringUtils.isNotBlank(signTime)){
    	   signTime += " 00:00:00";
    	   hql += " and to_date('"+ signTime+"','yyyy-MM-dd HH24:mi:ss')<=sign.signTime";
		}
       if(StringUtils.isNotBlank(signEndTime)){
    	   signEndTime += " 23:59:59";
    	   hql += " and to_date('"+ signEndTime+"','yyyy-MM-dd HH24:mi:ss')>=sign.signTime";
       }
       
       hql += " order by sign.signTime desc ,sign.grpName,sign.deptName,sign.userId";
       return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pagenum, pagesize);
      
	}
	
	public List getListByCondition(String deptName, String grpName,
			String userId,  String signTime,
			String signEndTime, UserModel userModel, String approvePerson, String approveStatus) {
		
	
		String hql = "select  sign.C_USERNAME,sign.C_USERID,TO_CHAR(sign.D_SIGNTIME,'yyyy-mm-dd hh24:mi:ss'),sign.C_AREANAME,sign.C_VILID,"
				+ "sign.C_ATTENDANCE_STATUS from OA_SIGNRECORD sign where (sign.c_type='2'or sign.c_type='3')  ";

		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.all")){//查看全部数据
			
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.dept")){
			hql += " and (sign.C_USERID in (select u.C_USERID from SYS_USER u where u.C_DETNAME='"+userModel.getDeptName()+"') or sign.C_APPROVER ='"+userModel.getUserid()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "signrecord.userdata.project")){
			String grp = " select g.C_USERKEY from OA_PROJECT_RESOURCE g where g.C_PROJECTNAME in (select p.C_PROJECTNAME from OA_PROJECT_RESOURCE p where p.C_USERKEY ='"+userModel.getUserid()+"')";
			hql += " and (sign.C_USERID in (select s.C_USERID from SYS_USER s where s.C_USERID in ("+grp+")) or sign.C_APPROVER ='"+userModel.getUserid()+"')";
		}else{
			if(StringUtils.isNotBlank(approvePerson)){
				hql += " and sign.C_APPROVER in (select s.C_USERID from SYS_USER s where s.C_USERID like '%"+approvePerson+"%' or s.C_USERNAME like '%"+approvePerson+"%')";
			}else{
				hql +=" and sign.C_USERID='"+userModel.getUserid()+"'";
			}
		}
		
		//查询条件
		if(userId!=null && !"".equals(userId)){
			hql += " and (sign.C_USERID like '%"+userId+"%' or sign.C_USERNAME like '%"+userId+"%')";
		}else{
			if(deptName!=null && !deptName.equals("")){
				hql += " and sign.C_DEPTNAME like '%"+deptName+"%'";
			}
			if(grpName!=null && !grpName.equals("")){
				hql += " and sign.C_GRPNAME  like '%"+grpName+"%'";	
			}
		}
		if(StringUtils.isNotBlank(approveStatus)){
			hql += " and sign.C_APPROVESTATUS like '%"+approveStatus+"%'";
		}
		if(StringUtils.isNotBlank(approvePerson)){
			hql += " and sign.C_APPROVER in (select s.userid from SysUser s where s.userid like '%"+approvePerson+"%' or s.username like '%"+approvePerson+"%')";
		}
       if(StringUtils.isNotBlank(signTime)){
    	   signTime += " 00:00:00";
    	   hql += " and to_date('"+ signTime+"','yyyy-MM-dd HH24:mi:ss')<=sign.D_SIGNTIME";
		}
       if(StringUtils.isNotBlank(signEndTime)){
    	   signEndTime += " 23:59:59";
    	   hql += " and to_date('"+ signEndTime+"','yyyy-MM-dd HH24:mi:ss')>=sign.D_SIGNTIME";
       }
        
       hql += "  order by sign.D_SIGNTIME desc ,sign.C_GRPNAME,sign.C_DEPTNAME,sign.C_USERID";
       List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
       return list;
	}

	/**
	 * @return
	 * lhyan3
	 * 2014年6月17日
	 */
	@SuppressWarnings("unchecked")
	public List<SignRecord> getRecordAddrisNull() {
		String hql = "from SignRecord s where s.areaName is null and s.type='2'";
		//String hql = "from SignRecord s where s.areaName is null and s.userId like 'wtj%' and s.type='2'";
		List<SignRecord> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 判断用户在该时间段是否存在记录，有return false，无return true
	 * @param userid
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public boolean checkExist(String userid, Date signTime) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss " );
		String str = sdf.format(signTime).trim();
		boolean flag = true;
		String hql = "select * from oa_cardrecord where 1=1 ";
		hql += " and c_userid ='" + userid.trim()+"'";
		hql +=" and to_char(d_signTime,'yyyy-mm-dd hh24:mi:ss')='"+str+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(list.size()!=0){
			flag = false;
		}
		return flag;
	}
	
	public boolean updateStatus(){
		String sql="update oa_signrecord set c_status=''";
		int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(result==0){
			return true;
		}else{
			return false;
		}
	}

	public List getLastAreaByUserId(String userid) {
		String sql = "select s.areaName from SignRecord s where s.userId = '"+userid+"' group by s.areaName order by max(s.signTime) desc";
		return this.getObjectList(sql);
	}

	public List<SignRecord> getRecordsByIds(String ids) {
		String hql = "from SignRecord s where s.id in "+ids;
		List<SignRecord> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 根据用户账号保存用户的外派账号
	 * @param userid
	 * @param outcard
	 * @return
	 */
	public String saveUserIdForOutCard(String[] userid,String[] outcard,String[] usernames){
		String sql = "";
		String returnUserName = "";
		for(int i=0;i<userid.length;i++){
			sql="update sys_user set c_outnumber='"+outcard[i]+"' where c_userid='"+userid[i]+"'";
			int result = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
			if(result==0){
				returnUserName = returnUserName + "," + usernames[i];
			}
		}
		if(returnUserName.length()>0){
			returnUserName=returnUserName.substring(1);
		}
		return returnUserName;
	}
	
	/**
	 * 判断oa账号是否有对应的外派工号
	 * @param userid
	 * @return true有对应的外派工号 false无对应的外派工号
	 */
	public boolean flagHasUserid(String userid){
		String sql="select * from sys_user where c_userid='"+userid+"' and c_outnumber is not null";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 将有效的移动签到数据同步到考勤表中
	 */
	public void synchronizateSignToCard(){
		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("{call SignToCard_proc}").executeUpdate();
	}
	
	/**
	 * 获取用户所在项目组
	 * @param userid
	 * @param signTime
	 * @return
	 * @throws Exception
	 */
	public String getGroupnameByuserid(String userid, String signTime) throws Exception{
		String hql = "select wm_concat(c_projectid) projectid from oa_project_resource_plan t where t.c_userkey='"+userid+"'";
		hql += " and '"+signTime+"' between to_char(t.d_fact_starttime,'yyyy-MM-dd') and to_char(t.d_fact_endtime,'yyyy-MM-dd')";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).list();
		if(null != list && list.size()!=0){
			return list.get(0).toString();
		}
		return "";
	}
	
	/**
	 * 根据当前用户角色列出需要自己进行批量审核的移动签到数据
	 * 数据记录为当前日期前一个月至当天
	 * @param pageNum
	 * @param pageSize
	 * @param userModel
	 * @return
	 */
	public Page getApprovalMoreSign(int pageNum,int pageSize,UserModel userModel,String status){
		String beforeDate = DateUtil.rollDownDate()+"01";
		String hql = "from SignRecord sign where 1=1 and to_char(signTime,'yyyymmdd')>='"+beforeDate+"'";
		hql+=" and approveStatus='"+status+"' and approvePerson='"+userModel.getUserid()+"'";
		hql+=" order by userId,signTime";
       return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}
	
	/**
	 * 获取移动签到数据
	 * @param userid
	 * @param status
	 * @return
	 */
	public List<SignRecord> getApprovalRecord(String userid,String status) {
		String beforeDate = DateUtil.rollDownDate()+"01";
		String hql = "from SignRecord sign where 1=1 and to_char(signTime,'yyyymmdd')>='"+beforeDate+"'";
		hql+=" and approveStatus='"+status+"' and approvePerson='"+userid+"'";
		hql+=" order by userId,signTime";
		List<SignRecord> records = this.getHibernateTemplate().find(hql);
		return records;
	}
	
	/**
	 * 根据部门名称得出部门审核人
	 * @param deptid
	 * @return
	 */
	public String getDeptApprovePeople(String deptid){
		String sql="select c_key||','||c_value from sys_datadir t where t.c_parentid=(select c_id from sys_datadir where c_key='deptapprover') and trim(c_note)='"+deptid.trim()+"'";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		if(list.size()!=0){
			return list.get(0).toString();
		}
		return "";
	}
}
