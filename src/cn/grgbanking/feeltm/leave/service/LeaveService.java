package cn.grgbanking.feeltm.leave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.leave.dao.LeaveDao;
import cn.grgbanking.feeltm.leave.domain.Leave;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service
public class LeaveService extends BaseService{

	@Autowired
	private LeaveDao dao;
	
	@Autowired
	private StaffInfoDao staffInfoDao;

	public Page findAll(Leave leave, UserModel userModel,String startTime,String endTime, int pageNum,
			int pageSize) {
		String hql = "from Leave l where 1=1 ";
		//数据权限控制
//		if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
//			if(UserRoleConfig.isDeptManageGroup(userModel)){
//				//部门经理
//				/*hql += " and l.userid in (select s.userid from SysUser s where s.deptName ='"+userModel.getDeptName()+"')";*/
//				hql += " and l.deptName like '%"+staffInfoDao.getDeptNameValueByUserId(userModel.getUserid()).trim()+"%'";
//			}
//			else if(UserRoleConfig.isProjectManageGroup(userModel)){
//				//项目经理
//				String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
//				hql += " and l.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
//			}else{
//				//项目经理以下
//				hql += " and l.userid ='"+userModel.getUserid()+"'";
//			}
//		}
		
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("leave.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("leave.userdata.dept"))){//查看本部门数据
			hql += " and l.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewProjectData(userModel,Configure.getProperty("leave.userdata.project"))){//查看本项目组数据
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and l.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and l.userid='"+userModel.getUserid()+"'";
		}
		//条件查询
		if(startTime!=null && !"".equals(startTime)){
			startTime += " 00:00:00";
			hql += " and l.startTime >=to_date('"+startTime+"','yyyy-MM-dd hh24:mi:ss')";
		}
		if(endTime!=null && !"".equals(endTime)){
			endTime += " 23:59:59";
			hql += " and l.endTime <=to_date('"+endTime+"','yyyy-MM-dd hh24:mi:ss')";
		}
		if(leave.getType()!=null){
			hql += " and l.type='"+leave.getType()+"'";
		}
		if(leave.getUserid()!=null){
			//如果根据用户查询则不需要再加部门组条件
			hql += " and (l.userid like '%"+leave.getUserid()+"%' or l.username  like '%"+leave.getUserid()+"%')";
		}else{
			if(null != leave.getDeptName() && !"".equals(leave.getDeptName())){
				hql += " and l.deptName like '%"+leave.getDeptName()+"%'";
			}
			if(null != leave.getGrpName() && !"".equals(leave.getGrpName())){
				hql += " and l.grpName like '%"+leave.getGrpName()+"%'";
			}
		}
		hql += " order by l.subTime desc nulls last";
		return dao.findAll(hql,pageNum,pageSize);
	}
	
	public Page leaveList(String userid, int pageNum, int pageSize) {
		String hql = "from Leave l where 1=1 and userid='"+userid+"'";
		hql += " order by l.id";
		return dao.findAll(hql,pageNum,pageSize);
	}

	/**
	 * 
	 * @param leave
	 */
	public void save(Leave leave) {
		dao.addObject(leave);
	}

	/**
	 * @param id
	 * @return
	 */
	public Leave findById(String id) {
		return (Leave) dao.findLeaveById(id);
	}

	public void update(Leave leave) {
		dao.updateObject(leave);
	}

	public void updateIds(String substring) {
		dao.updateIds(substring);
	}

	public void removeIds(String substring) {
		dao.removeIds(substring);
	}

}
