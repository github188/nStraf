/**
 * 
 */
package cn.grgbanking.feeltm.um.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.um.dao.SysUserGroupDao;
import cn.grgbanking.feeltm.um.dao.SysUserInfoDao;
import cn.grgbanking.feeltm.um.dao.UserGroupInfoDao;
import cn.grgbanking.feeltm.um.webapp.vo.SysUserGroupVo;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.BeanUtils;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class SysUserGroupService extends BaseService {
	@Autowired
	private SysUserGroupDao dao;
	@Autowired
	private UserGroupInfoDao userGroupInfoDao;
	@Autowired
	private SysUserInfoDao userInfoDao;

	/**lhy 2014-4-23
	 * 根据组号获取该组号的用户
	 * @param grpcode
	 * @return
	 */
	public List findUserByGroupCode(String grpcode){
		List sysUserGrp = dao.findUserListByGrpcode(grpcode);
		return sysUserGrp;
	}
	/**
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List findUserByProject(String grpcode){
		List sysUserGrp = dao.findUserListByProject(grpcode);
		return sysUserGrp;
	}
	public List findUserByProject1(String grpcode){
		List sysUserGrp = dao.findUserListByProject(grpcode);
		return sysUserGrp;
	}
	
	/**lhy 2014-4-23
	 * 根据组号获取该组号的用户
	 * @param grpcode
	 * @return
	 */
	public List findUserByGroupCode1(String grpcode){
		List sysUserGrp = dao.findUserListByGrpcode1(grpcode);
		return sysUserGrp;
	}
	
	/**
	 * 获取部门 的该组的人员
	 * @param deptname
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月5日
	 */
	public List findUserByGroupCode(String deptname,String grpcode){
		List sysUserGrp = dao.findUserListByGrpcode(grpcode);
		return sysUserGrp;
	}
	
	/** lhy 2014-4-24
	 * 根据用户id获取该用户所属组
	 * @param userid
	 * @return
	 */
	public List<UsrGroup> getUserGropByUserId(String userid){
		return dao.getUserGropByUserId(userid);
	}
	
	/**
	 * lhy 2014-4-28
	 * @param userid
	 * @return
	 */
	public List<String> getgroupcodeByUserid(String userid){
		return dao.getgroupcodeByUserid(userid);
	}

	/**By wjie5 2014-4-28
	 * 查询该组并且属于deptvalue部门的用户列表
	 * @param deptValue2 
	 * @param deptValue3 
	 * @param userid
	 * @return
	 */
	public List findUserListByGrpDept(String grpcode,String deptvalue, String groupValue2, String deptValue2) {
		return dao.findUserListByGrpDept(grpcode, deptvalue,groupValue2,deptValue2);
	}

	
	
	/*
	 * ���� Javadoc��
	 * 
	 * @see
	 * cn.grgbanking.feeltm.um.service.SysUserGroupManager#setSysUserGroupDAO
	 * (cn.grgbanking.feeltm.um.dao.SysUserGroupDAO)
	 */

	/*
	 * ���� Javadoc��
	 * 
	 * @see
	 * cn.grgbanking.feeltm.um.service.SysUserGroupManager#findSysUserGroupList
	 * (java.lang.String)
	 */
	public List findSysUserGroupList(String userid, String grpLevel) {
		List groupList = this.getGroupListByGrpLevel(userid, grpLevel);
		List userGroupList = new ArrayList();
		userGroupList = dao.findSysUserGroupList(userid);
		List userGroupFormList = new ArrayList();

		for (ListIterator it = groupList.listIterator(); it.hasNext();) {
			UsrGroup usrGroup = (UsrGroup) it.next();
			SysUserGroupVo sysUserGroupVo = new SysUserGroupVo();
			try {
				BeanUtils.copyProperties(sysUserGroupVo, usrGroup);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (userGroupList.size() != 0 && userGroupList != null) {
				for (int j = 0; j < userGroupList.size(); j++) {
					UsrUsrgrp grprole = (UsrUsrgrp) userGroupList.get(j);
					if (grprole.getGrpcode() != null) {
						if (grprole.getGrpcode().equals(
								sysUserGroupVo.getGrpcode())) {
							sysUserGroupVo.setChecked("checked");
						}
					}

				}
			}
			sysUserGroupVo.setUserid(userid);
			userGroupFormList.add(sysUserGroupVo);
		}

		return userGroupFormList;
	}

	public List getGroupListByGrpLevel(String userid, String grpLevel) {

		return userGroupInfoDao
				.getUserGroupInfoListByGrpLevel(userid, grpLevel);
	}

	public int removeSysUserGroup(String userid) {

		return dao.removeSysUserGroup(userid);
	}

	public void saveSysUserGroup(UsrUsrgrp usrUsrgrp) {
		dao.addObject(usrUsrgrp);

	}

	public int removeSysUserGroup(String[] userids) {

		return dao.removeSysUserGroup(userids);
	}

	public List getAllGroupList() {

		return userGroupInfoDao.getAllUserGroupInfoList();
	}
	public List getAllProjectList() {
		
		return userGroupInfoDao.getAllUserProjectInfoList();
	}


	/**lhy 2014-4-23
	 * 查询不在该组 的用户列表
	 * @param grpcode
	 * @return
	 */
	public List findUserNotGroupByGrp(String grpcode) {
		return dao.findUserNotGroupByGrp(grpcode);
	}
	/**
	 * 查询不在项目组的用户
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List findUserNotGroupByProject(String grpcode) {
		return dao.findUserNotGroupByProject(grpcode);
	}
	
	/**
	 * 获取部门 的不属于该组的人员
	 * @param deptname
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月5日
	 */
	public List findUserNotGroupByGrp(String deptname,String grpcode) {
		return dao.findUserNotGroupByGrp(grpcode);
	}
	/**
	 * 获取部门 的不属于该组的人员
	 * @param deptname
	 * @param grpcode
	 * @return
	 * lhyan3
	 * 2014年6月5日
	 */
	public List findUserNotGroupByGrp1(String deptname,String grpcode,String username) {
		return dao.findUserNotGroupByGrp1(deptname,grpcode,username);
	}
	public List findUserNotProjectByProject1(String deptname,String grpcode) {
		return dao.findUserNotProjectByProject1(deptname,grpcode);
	}

	/**lhy 2014-4-24
	 * 根据组删除该组与用户的关联
	 * @param grpcode
	 */
	public int removeGroupByUserid(String grpcode) {
		return dao.removeGroupByUserid(grpcode);
	}
	
	/**
	 * 判断该角色是否有该用户的信息
	 * @param userid
	 * @param role
	 * @return
	 */
	public boolean findRoleByUserid(String userid,String role){
		return dao.findRoleByUserid(userid, role);
	}
	
	/**
	 * 根据角色用户删除信息
	 * @param userid
	 * @param role
	 */
	public void deleteInfoByRoleUserid(String userid,String role){
		dao.deleteInfoByRoleUserid(userid, role);
	}
}
