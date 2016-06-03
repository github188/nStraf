package cn.grgbanking.feeltm.hols.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.hols.dao.UserHolsDao;
import cn.grgbanking.feeltm.hols.domain.UserHols;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service
public class UserHolsService extends BaseService{
	
	@Autowired
	private UserHolsDao dao;
	
	@Autowired
	private StaffInfoDao staffInfoDao;
	
	@Autowired
	private ProjectDao projectDao;

	/**
	 * 列表页面
	 * @param hols
	 * @param userModel
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page findUserholsPage(UserHols hols, UserModel userModel,
			int pageNum, int pageSize) {
		String hql = "from UserHols h where 1=1 and h.flag=0";
		if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("hols.userdata.all"))){//查看全部数据
			
		}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("hols.userdata.dept"))){//查看本部门数据
			hql += " and h.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewProjectData(userModel,Configure.getProperty("hols.userdata.project"))){//查看本项目组数据
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and h.userid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and h.userid='"+userModel.getUserid()+"'";
		}
		//查询条件
		if(hols.getUserid()!=null){
			hql += " and (h.userid like '%"+hols.getUserid()+"%'";
			hql += " or h.username like '%"+hols.getUserid()+"%')";
		}else{
			if(hols.getDeptName()!=null){
				hql += " and h.userid in (select u.userid from SysUser u where u.deptName='"+hols.getDeptName()+"')";
			}
			if(hols.getGroupName()!=null){
				hql += "and h.userid in (select u.userid from SysUser u where u.userid in (select g.userid from UserProject g where g.project.id='"+hols.getGroupName()+"'))";
			}
		}
		return dao.findUserholsPage(hql, pageNum, pageSize);
	}
	
	/**
	 * 添加假期数据
	 * 一般在添加用户时同时新增该用户的假期数据
	 */
	public void addHols(String userid){
		UserHols hols = new UserHols();
		hols.setUserid(userid);
		hols.setUsername(staffInfoDao.getUsernameById(userid));
		hols.setDeptName(staffInfoDao.getDeptNameValueByUserId(userid));
		hols.setGroupName(projectDao.getProjectNameByUserid(userid));
		hols.setDeferredTime(0.0);
		hols.setYearholsTime(0.0);
		hols.setFlag(0);
		this.save(hols);
	}
	/**
	 * 添加加班调休数据
	 * @param userid  加班人
	 * @param time  加班小时
	 */
	public void updateFreeTime(String userid,double time){
		UserHols hols = findByUserid(userid);
		if(hols!=null){
			hols.setDeferredTime(hols.getDeferredTime()+time);
			updateHols(hols);
		}else{
			hols = new UserHols();
			hols.setDeferredTime(hols.getDeferredTime()+time);
			hols.setUserid(userid);
			hols.setYearholsTime(0.0);
			hols.setFlag(0);
			save(hols);
		}
	}
	
	/**
	 * 
	 * @param userid
	 * @param time
	 * @param type 1年假  2调休
	 * lhyan3
	 * 2014年6月4日
	 */
	public void updateHolsTime(String userid,double time,int type){
		UserHols hols = findByUserid(userid);
		if(hols!=null){
			if(type==1){
				//年假
				hols.setYearholsTime(time);
				updateHols(hols);
			}
			if(type==2){
				//调休
				hols.setDeferredTime(time);
				updateHols(hols);
			}
		}
	}

	private UserHols findByUserid(String userid) {
		return dao.findByUserid(userid);
	}

	private void save(UserHols hols) {
		dao.addObject(hols);
	}

	/**
	 * lhy 2014-5-13
	 * 查看是否存在
	 * @param string
	 * @return
	 */
	public boolean existUser(String userid) {
		return dao.existUser(userid);
	}

	/**
	 * lhy 2014-5-13
	 * @param holsList
	 */
	public void addHolsList(List<UserHols> holsList) {
		dao.addHolsList(holsList);
	}

	public List<UserHols> findByIds(String[] holsids) {
		return dao.findByIds(holsids);
	}

	public List<SysUser> findUserByIds(String[] holsids) {
		return dao.findUserByIds(holsids);
	}

	public UserHols findByidAndUser(String holsId, String userid) {
		return dao.findByidAndUser(holsId,userid);
	}

	public void updateHols(UserHols h) {
		dao.updateObject(h);
	}

	/**
	 * 获取年假或调休剩余时间
	 * @param userid
	 * @param type
	 * @return
	 */
	public double getRestTime(String userid,int type) {
		Object[] times =  dao.getRestTime(userid,type);
		double time = 0;
		if(times!=null){
			if(type==1){
				//年假
				time = Double.parseDouble(times[0].toString());
			}
			if(type==2){
				time = Double.parseDouble(times[1].toString());
			}
		}
		return time;
	}

	public List<SysUser> getNotExistUsers(String userids, int type, String deptcode, String grpcode) {
		return dao.getNotExistUsers(userids,type,deptcode,grpcode);
	}

}
