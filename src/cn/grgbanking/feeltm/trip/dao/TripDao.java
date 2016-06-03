package cn.grgbanking.feeltm.trip.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.domain.testsys.BusinessTrip;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("tripDao")
public class TripDao extends BaseDao<BusinessTrip>{
	public Page getPage(String start,String end,String prjName,String username,String deptname,String groupname,int pageNum,int pageSize,UserModel userModel)
	{
		String hql = "FROM BusinessTrip trip WHERE 1=1 ";
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("trip.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("trip.userdata.dept"))){//查看本部门数据
			hql += " and trip.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewProjectData(userModel,Configure.getProperty("trip.userdata.project"))){//查看本项目组数据
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and trip.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and trip.userid='"+userModel.getUserid()+"'";
		}
		//查询条件
		if(start!=null&&!start.equals("") ){
			hql += " and trip.startdate >= to_date('"+start.trim()+"','yyyy-MM-dd') ";
		}
		if(end!=null&&!end.equals("")){
			hql += " and trip.enddate <= to_date('"+end.trim()+"','yyyy-MM-dd') ";
		}
		if(prjName!=null && !prjName.equals("")){
			hql += " and trip.prjname like '%"+prjName.trim()+"%' ";
		}
		if(groupname!=null && !groupname.equals("全选")&& !"".equals(groupname)){
			hql += " and groupname like '%"+groupname.trim()+"%'";
		}
		if (username != null && !username.equals("全选") && !"".equals(username)){
			hql += " and (username like '%"+username.trim()+"%' or userid like '%"+username.trim()+"%')";
		}
		if (deptname != null && !deptname.equals("全选") && !"".equals(deptname)){
			hql += " and detname like '%"+ deptname.trim()+"%'";
		}
		hql += " order by trip.createdate desc,trip.prjname ";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public boolean checkExist(String username, Date start, Date end) throws Exception{
		boolean flag = true;
		String hql = "from BusinessTrip where 1=1 ";
		hql += " and username like '%" + username.trim()+"%'";
//		hql +=" and ('"+sstaart+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd')";
//		hql +=" or '"+send+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd'))";
		hql +=" and (? between startdate and enddate";
		hql +=" or ? between startdate and enddate)";
		List list = getHibernateTemplate().find(hql,new Object[]{start,end});
		if(list.size()!=0)
		{
			flag = false;
		}
		return flag;
	}
	
	public boolean checkUpExist(String username, Date start, Date end,String id) throws Exception{
		boolean flag = true;
		String hql = "from BusinessTrip where 1=1 ";
		hql += " and username like '%" + username.trim()+"%'";
//		hql +=" and ('"+sstaart+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd')";
//		hql +=" or '"+send+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd'))";
		hql +=" and (? between startdate and enddate";
		hql +=" or ? between startdate and enddate)";
		hql += " and id!=?";
		System.out.println("sss==="+hql);
		List list = getHibernateTemplate().find(hql,new Object[]{start,end,id});
		if(list.size()!=0)
		{
			flag = false;
		}
		return flag;
	}
	
	public double getSumtime(Date startdate,Date enddate){
		//
		return 0;
	}
}
