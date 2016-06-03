package cn.grgbanking.feeltm.overtime.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("overtimeDao")
public class OvertimeDao extends BaseDao<Overtime> {
	public Page getPage(String groupname,String username,String deptname,
			int pageNum, int pageSize,String ot, String prjname, String otDayEnd,UserModel userModel) {
		String hql ="";
		hql = "from Overtime where 1=1 ";
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("overtime.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("overtime.userdata.dept"))){//查看本部门数据
			hql += " and trip.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewProjectData(userModel,Configure.getProperty("overtime.userdata.project"))){//查看本项目组数据
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and trip.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and trip.userid='"+userModel.getUserid()+"'";
		}
		//查询条件
		if(groupname!=null && !groupname.equals("全选") && !groupname.equals("")){
			hql += " and groupname like '%"+groupname.trim()+"%'";
		}
		if (username != null && !username.equals("全选") && !username.equals("")){
			hql += " and (username like '%"+username.trim()+"%' or userid  like '%"+username.trim()+"%' )";
		}
		if (deptname != null && !deptname.equals("全选") && !deptname.equals("")){
			hql += " and detname like '%"+ deptname.trim()+"%'";
		}
		if (prjname != null && !prjname.equals("全选") && !prjname.equals("")){
			hql += " and prjname like '%"+ prjname.trim()+"%'";
		}
		if (ot != null && !ot.equals("")){
			ot = ot + " 00:00:00";
			hql += " and startdate >= to_date('"+ot+"','yyyy-mm-dd hh24:mi:ss')";
		}
		if (otDayEnd != null && !otDayEnd.equals("")){
			otDayEnd = otDayEnd + " 23:59:59";
			hql += " and enddate <= to_date('"+otDayEnd+"','yyyy-mm-dd hh24:mi:ss')";
		}
		hql += " order by createdate desc,updatedate desc,groupname desc";
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public Page getOvertimePage(String userid, int pageNum, int pageSize) {
		String hql ="";
		hql = "from Overtime where 1=1 and userid='"+userid+"'";
		hql += " order by createdate desc,updatedate desc";
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public boolean checkExist(String userid, Date start, Date end) throws Exception{
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String sstaart = sdf.format(start);
		String send = sdf.format(end);
		boolean flag = true;
		String hql = "from Overtime where 1=1 ";
		hql += " and userid like '%" + userid.trim()+"%'";
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
	
	public boolean checkUpExist(String userid, Date start, Date end,String id) throws Exception{
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		String sstaart = sdf.format(start);
		String send = sdf.format(end);
		boolean flag = true;
		String hql = "from Overtime where 1=1 ";
		hql += " and userid like '%" + userid.trim()+"%'";
//		hql +=" and ('"+sstaart+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd')";
//		hql +=" or '"+send+"' between to_char(startdate, 'yyyy-mm-dd') and to_char(enddate, 'yyyy-mm-dd'))";
		hql +=" and (? between startdate and enddate";
		hql +=" or ? between startdate and enddate)";
		hql += " and id!=?";
		List list = getHibernateTemplate().find(hql,new Object[]{start,end,id});
		if(list.size()!=0)
		{
			flag = false;
		}
		return flag;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<Overtime> bs=new ArrayList<Overtime>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new Overtime(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and level != 3  and lower(trim(username)) not in('汤飞','开发员')  order by level";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	public String getUserGroup_name(String username){
		
		String hql="select groupName from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=(String)list.get(0);		     
		}
		return str;
	}
	
	public String getUserlevel(String username){
		
		String hql="select level from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public List<Object> getUsernamesByGroup(String groupName){
		String query="select user.username from SysUser user where 1=1 and lower(trim(user.username)) not in('王全胜','杜高峰','开发员','管理员')";
		if(!groupName.equals("全选")){
			query+="and user.groupName like '%"+groupName+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
}
