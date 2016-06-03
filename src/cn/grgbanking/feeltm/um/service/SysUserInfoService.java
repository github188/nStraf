package cn.grgbanking.feeltm.um.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.um.dao.SysUserGroupDao;
import cn.grgbanking.feeltm.um.dao.SysUserInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class SysUserInfoService extends BaseService {
	@Autowired
	private SysUserInfoDao dao;
	@Autowired
	private SysUserGroupDao sysUserdao;
	@Autowired
	private UsrUsrgrpDao usrUsrgrpDao;

	@Transactional(readOnly = true)
	public SysUser findSysUserByUserId(String userid) {
		return dao.findSysUserByUserId(userid);
	}

	/*
	 * 根据用户获取用户组名称
	 */
	public String getGroupNameByUserId(String userId) {
		return dao.getGroupNameByUserId(userId);
	}

	/*
	 * 删除用户，同时也删除用户组的信息
	 */
	public int removeSysUserInfo(String userids) {
		String[] userid = StringUtils.split(userids, ",");
		sysUserdao.removeSysUserGroup(userid);
		return dao.removeSysUserInfo(userid);
	}

	/*
	 * 增加用户，同时增加用户组信息。 采用spring的事务处理
	 */
	public boolean addSysUserInfo(SysUser sysUser, UsrUsrgrp usrUsrgrp) {
		boolean flag = false;
		try {
			String orgfloor = dao.getOrgfloorByOrgId(sysUser);
			sysUser.setOrgfloor(orgfloor);
			dao.addObject(sysUser);
			dao.addObject(usrUsrgrp);

			flag = true;
			;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	// ///////////////////////////////////////////////////////////

	/*
	 * 判断用户是否存在
	 */
	@Transactional(readOnly = true)
	public boolean isExitsSysUserInfo(SysUser user) {
		boolean flag = false;
		SysUser sysUser = dao.findSysUserByUserId(user.getUserid());
		if (sysUser != null)
			flag = true;
		return flag;

	}

	public boolean updateSysUser(SysUser sysUser) {
		boolean flag = false;
		try {
			dao.updateObject(sysUser);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}

		return flag;
	}

	@Transactional(readOnly = true)
	public Page findUserInfoPage(SysUser form, int pageNum, int pageSize) {
		return dao.findUserInfoPage(form, pageNum, pageSize);
	}

	/*
	 * 修改密码
	 */
	public int modifyPassword(String userid, String password, String newpassword) {
		int flag = 0;
		SysUser sysUser = dao.findSysUserByUserId(userid);
		if (sysUser != null) {
			if (sysUser.getUserpwd().equals(password)) {
				sysUser.setUserpwd(newpassword);
				dao.updateObject(sysUser);
				flag = 1;
			}
		}
		return flag;
	}

	public boolean updateSysUserInfo(SysUser sysUser) {
		boolean flag = false;
		try {
			String orgfloor = dao.getOrgfloorByOrgId(sysUser);
			sysUser.setOrgfloor(orgfloor);
			dao.updateObject(sysUser);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);

		}
		return flag;

	}
	
	@Transactional(readOnly = true)
	public List<UsrUsrgrp> getSysUserGroupList(String grpCode) {
		String sql = " from UsrUsrgrp  usrUsrgrp where usrUsrgrp.grpcode='"
				+ grpCode + "'";
		return usrUsrgrpDao.getObjectList(sql);
	}

	/**
	 * by wjie5 2014-4-24 
	 * 根据部门名称查询部门人员名
	 * @param deptName
	 * @param deptvalue 
	 * @return
	 */
	public List<Object[]> getNames(String deptName, String deptvalue) {
		
		return dao.getNamesByDept(deptName,deptvalue);
	}

}
