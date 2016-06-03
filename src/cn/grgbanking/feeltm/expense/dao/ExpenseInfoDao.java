package cn.grgbanking.feeltm.expense.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccount;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountCheckCondition;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountProject;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountTravelDetail;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@SuppressWarnings("unchecked")  
@Repository("expenseInfoDao")
public class ExpenseInfoDao extends BaseDao<SysUser> {
	
	public Page getPage(int pageNum,int pageSize,String groupname,UserModel userModel){
		String hql = " from OAExpenseAccount o where 1=1 ";
//		if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
//			if(UserRoleConfig.isDeptManageGroup(userModel)){
//				//部门经理
//				hql += " and o.userId in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
//			}else if(UserRoleConfig.isProjectManageGroup(userModel)){
//				//项目经理
//				String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
//				//项目经理
//				hql += " and o.userId in (select s.userid from SysUser s where s.userid in ("+grp+"))";
//			}else{
//				
//				hql += " and o.userId='"+userModel.getUserid()+"'";
//			}
//		}
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
			hql+=" and o.status='1'";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
			hql+=" and o.status='2'";
		}
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("expense.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("expense.userdata.dept"))){//查看本部门数据
			hql += " and o.userId in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"expense.userdata.project")){//项目经理查看本项目组下的数据
			String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and e.userId in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and o.userId='"+userModel.getUserid()+"'";
		}
		hql += " order by e.updateDate desc,e.prjName desc,e.detName desc,e.userName desc,e.status";
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
		return page;
	}
	/**
	 * 根据条件查询报销信息
	 * @param userName 报销人
	 * @param detName 部门
	 * @param groupName 组别
	 * @param expenseNum 报销流水号
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPageByCondition(OAExpenseAccount expenseAccount,int pageNum, int pageSize,UserModel userModel
			,String submitDate,String submitEndDate) { 
		String hql = "from OAExpenseAccount e where 1=1 ";
//		if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
//			if(UserRoleConfig.isDeptManageGroup(userModel)){
//				//部门经理
//				hql += " and e.userId in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
//			}else if(UserRoleConfig.isProjectManageGroup(userModel)){
//				//项目经理
//				String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
//				//项目经理
//				hql += " and e.userId in (select s.userid from SysUser s where s.userid in ("+grp+"))";
//			}else{
//				
//				hql += " and e.userId='"+userModel.getUserid()+"'";
//			}
//		}
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("expense.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("expense.userdata.dept"))){//查看本部门数据
			hql += " and e.userId in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"expense.userdata.project")){//项目经理查看本项目组下的数据
			String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and e.userId in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and e.userId='"+userModel.getUserid()+"'";
		}
		
		//查询数据
		if (expenseAccount.getUserName()!= null && !expenseAccount.getUserName().equals("")) {
			hql += " and (e.userName like '%" + expenseAccount.getUserName() + "%' or e.userId like '%" + expenseAccount.getUserName() + "%')";
		}
		if(expenseAccount.getDetName()!=null && !expenseAccount.getDetName().equals("")){
			hql += " and e.userId in (select s.userid from SysUser s where s.deptName='"+expenseAccount.getDetName()+"')";
		}
		if(expenseAccount.getPrjName()!=null && !expenseAccount.getPrjName().equals("")){
			hql += " and e.prjName like '%"+expenseAccount.getPrjName()+"%'";
		}
		if (expenseAccount.getExpenseNum() != null && StringUtils.isNotBlank(expenseAccount.getExpenseNum())) {
			hql += " and e.expenseNum like '%" + expenseAccount.getExpenseNum() + "%' ";
		}
		if (submitDate != null && !"".equals(submitDate)) {
			submitDate += " 00:00:00";
			hql+=" and to_date('"+ submitDate+"','yyyy-MM-dd HH24:mi:ss')<=e.submitDate";
		}
		if (submitEndDate != null && !"".equals(submitEndDate)) {
			submitEndDate += " 23:59:59";
			hql+=" and to_date('"+ submitEndDate+"','yyyy-MM-dd HH24:mi:ss')>=e.submitDate";
		}
		if (expenseAccount.getExpenseprjname() != null && !"".equals(expenseAccount.getExpenseprjname())) {
			hql+=" and expenseprjname like '%"+expenseAccount.getExpenseprjname()+"%'";
		}
		if(expenseAccount.getStatus()!=null && !"".equals(expenseAccount.getStatus())){
			if(!expenseAccount.getStatus().equals("6")){
				hql+=" and status='"+expenseAccount.getStatus()+"'";
			}
		}else{
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
				hql+=" and e.status='1'";
			}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
				hql+=" and e.status='2'";
			}
		}
		hql += " order by e.updateDate desc,e.prjName desc,e.detName desc,e.userName desc,e.status";
		Page page = null;
		page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
		return page;
	}
	
	/**
	 * 用于新增报销记录时取下一个报销流水号:根据当前最大流水号自动加1
	 * @return
	 */
	public String getNextEno(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(e.expenseNum) from OAExpenseAccount e");
		if(list!=null&&list.size()>=0){
		      String e=(String)list.get(0);
		      if(e!=null && !e.equals("") && e.contains("E")){
		    	  e=e.substring(1);
		    	  long d=Long.parseLong(e)+1;
		    	  DecimalFormat format=new DecimalFormat("E0000");
		  		  str=format.format(d);
		      } else {
		    	  str = "E0000";
		      }
		} else {
			str = "E0000";
		}
		return str;
	}
	public OAExpenseAccount getOAExpenseAccountByexpenseNum(String expenseNum){
		String hql = "from OAExpenseAccount o where 1=1";
		if(expenseNum!=null){
			hql += " and o.expenseNum='"+expenseNum+"'";
		}
		List<OAExpenseAccount> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<UsrGroup> getAllUserGroup(){
		String hql = "from UsrGroup";
		return this.getHibernateTemplate().find(hql);
	}
	public OAExpenseAccount OAExpenseAccountgetByUsername(String userName){
		String hql = "from OAExpenseAccount o where 1=1";
		if(userName!=null&&!userName.equals(userName)){
			hql +="and o.userName="+userName;
		}
		return (OAExpenseAccount) this.getHibernateTemplate().find(hql).get(0);
	}
	public List<OAExpenseAccountCheckCondition> getDeTailByUUID(String uuid){
		String hql = "from OAExpenseAccountCheckCondition c where c.uuid =?";
		hql += " order by c.number desc";
		Object[] objects = new Object[]{uuid};
		return this.getHibernateTemplate().find(hql,objects);
	}
	public  List<OAExpenseAccountTravelDetail> getTravelDeTailByUUID(String uuid){
		String hql = "from OAExpenseAccountTravelDetail t where t.uuid =?";
		hql += " order by t.number desc";
		Object[] objects = new Object[]{uuid};
		return this.getHibernateTemplate().find(hql,objects);
	}
	
	public List getUserInfo(String userId){
		String hql = " select user.username ,user.tel,user.mobile,user.email,user.deptName from SysUser user where user.userid = ?";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql,userId);
	}
	
	/**
	 * 获取出差报销明细
	 * @param id 报销单id
	 * @return  明细列表
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountCheckCondition> getExpenseDetailByExpenseId(
			String id) {
		String hql = " from OAExpenseAccountCheckCondition c where c.expenseId =? and c.dateTime!=? order by c.number asc";
		Object[] objects = new Object[]{id,"合计"};
		List<OAExpenseAccountCheckCondition> details = this.getHibernateTemplate().find(hql, objects);
		return details;
	}
	
	/**
	 * 费用报销明细
	 * @param id 报销单id
	 * @return 费用明细
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountTravelDetail> getTravelDetailByExpenseId(
			String id) {
		String hql = " from OAExpenseAccountTravelDetail t where t.expenseId = ?  and t.ddTime!=? order by t.number asc";
		Object[] objects = new Object[]{id,"合计"};
		List<OAExpenseAccountTravelDetail> details = this.getHibernateTemplate().find(hql,objects);
		return details;
	}
	
	/**
	 * 批量保存对象
	 * @param list
	 * lhyan3
	 * 2014年7月18日
	 */
	public void saveList(List list) {
		if(list!=null && list.size()>0){
			for(Object o:list){
				addObject(o);
			}
		}
	}
	
	/**
	 * 得到出差合计
	 * @param id
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountCheckCondition getCostSum(String id) {
		String hql = "from OAExpenseAccountCheckCondition c where c.dateTime=? and c.expenseId=?";
		Object[] objects = new Object[]{"合计",id};
		List<OAExpenseAccountCheckCondition> list = this.getHibernateTemplate().find(hql, objects);
		if(list!=null && list.size()>0){
			return (OAExpenseAccountCheckCondition) list.get(0);
		}
		return null;
	}
	
	/**
	 * 得到费用合计
	 * @param id
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountTravelDetail getTravelSum(String id) {
		String hql = "from OAExpenseAccountTravelDetail c where c.ddTime=? and c.expenseId=?";
		Object[] objects = new Object[]{"合计",id};
		List<OAExpenseAccountTravelDetail> list = this.getHibernateTemplate().find(hql, objects);
		if(list!=null && list.size()>0){
			return (OAExpenseAccountTravelDetail) list.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountCheckCondition getCostDetail(String detailId) {
		String hql = "from OAExpenseAccountCheckCondition c where c.id=?";
		List<OAExpenseAccountCheckCondition> list = this.getHibernateTemplate().find(hql, detailId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountTravelDetail getTravelDetail(String detailId) {
		String hql = "from OAExpenseAccountTravelDetail c where c.id=?";
		List<OAExpenseAccountTravelDetail> list = this.getHibernateTemplate().find(hql, detailId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据报销id获取项目金额明细
	 * @param id 报销单id
	 * @return  明细列表
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountProject> getExpenseProjectSumByExpenseId(String id) {
		String hql = " from OAExpenseAccountProject c where c.expenseId =? order by c.id asc";
		Object[] objects = new Object[]{id};
		List<OAExpenseAccountProject> details = this.getHibernateTemplate().find(hql,objects);
		return details;
	}
	
	/**
	 * 获取项目金额明细数据
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountProject getProjectSumDetail(String detailId) {
		String hql = "from OAExpenseAccountProject c where c.id=?";
		List<OAExpenseAccountProject> list = this.getHibernateTemplate().find(hql, detailId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据uuid获取项目金额明细数据
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountProject getProjectSumDetailByUuid(String detailId) {
		String hql = "from OAExpenseAccountProject c where c.uuid=?";
		List<OAExpenseAccountProject> list = this.getHibernateTemplate().find(hql, detailId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
