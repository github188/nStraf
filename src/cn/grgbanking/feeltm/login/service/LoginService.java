/**
 * 
 */
package cn.grgbanking.feeltm.login.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.domain.SysLoginLog;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrGrprole;
import cn.grgbanking.feeltm.login.dao.LoginDao;
import cn.grgbanking.feeltm.um.dao.SysUserInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class LoginService extends BaseService {
	@Autowired
	private LoginDao dao;

	@Autowired
	private SysUserInfoDao sysUserDao;

	// 根据用户id查找用户
	public SysUser findSysUserByUserId(String userid) {
		return sysUserDao.findSysUserByUserId(userid);
	}

	//
	public HashMap getTellerOrle(String userid) {
		HashMap role = new HashMap();
		List list = new ArrayList();
		String temp = dao.getTellerPurview(userid);
		if (temp != null) {
			list = dao.getTellerOrleList(temp);
		}
		String rolecode = "";
		int k = list.size();
		for (int i = 0; i < k; i++) {

			rolecode = ((UsrGrprole) list.get(i)).getRolecode();
			role.put(rolecode, rolecode);
		}
		return role;
	}

	public List getFirstMenu(String grps[]) {
		HashMap menuidMap = new HashMap();

		List modelMenu = dao.getMenubyGrp(grps);
		Iterator modelMenuIterat = modelMenu.iterator();
		while (modelMenuIterat.hasNext()) {
			MenuInfo menuinfo = (MenuInfo) modelMenuIterat.next();
			getFirstLeverMenu(menuinfo, menuidMap);
		}// end for
		List menuModellist = dao.getMenubyModel();
		Iterator menuIterat = menuModellist.iterator();
		while (menuIterat.hasNext()) {
			MenuInfo menuModel = (MenuInfo) menuIterat.next();

			if (menuidMap.get(menuModel.getMenuid()) == null) {
				menuIterat.remove();
			}
		}
		return menuModellist;
	}

	public void getFirstLeverMenu(MenuInfo menuinfo, HashMap menuMap) {
		MenuInfo menuParent = (MenuInfo) dao.getObject(MenuInfo.class, menuinfo
				.getParentid());
		if (menuParent != null) {
			if (!menuParent.getParentid().equals("0")) {
				getFirstLeverMenu(menuParent, menuMap);
			} else {
				menuMap.put(menuParent.getMenuid(), menuParent);
			}
		}
	}

	public HashMap findMenuFuncMap(String userid) {

		return dao.findMenuFuncMap(userid);
	}

	public String createMenuList(String menuid, String userid, String surl) {
		StringBuffer sbfParentStart = new StringBuffer();
		List mymenu = dao.getMenuByAuth(menuid, userid);
		if (mymenu.size() <= 0) {
			return sbfParentStart.toString();
		}
		MenuInfo menuinfo = (MenuInfo) dao.getObject(MenuInfo.class, menuid);
		sbfParentStart
				.append("<H1 class=title><A href=\"javascript:void(0)\">")
				.append(menuinfo.getMenuitem()).append("</A></H1> \n");
		sbfParentStart.append("<DIV class=content><UL>");

		StringBuffer sbf = new StringBuffer();
		List mysonmenu = dao.getMenuByParentAuth(menuid, userid);
		String menuStatus = "noGrandson";
		Iterator iteratorson = mysonmenu.iterator();
		while (iteratorson.hasNext()) {
			MenuInfo menuinfoson = (MenuInfo) iteratorson.next();
			if (menuinfoson.getChildnum() > 0) {
				if (dao
						.getMenuLeafByParentAuth(menuinfoson.getMenuid(),
								userid).size() > 0) {
					sbf.append(createMenuList(menuinfoson.getMenuid(), userid,
							surl));
					menuStatus = "hasGrandson";
				}
			} else {
				sbf.append("<LI><A href=\"").append(surl).append(
						menuinfoson.getActionto().trim());

				if (menuinfoson.getActionto().trim().indexOf("!") > 0) {
					sbf.append("?menuid=");
				} else {
					sbf.append("?menuid=");
				}
				sbf.append(menuinfoson.getMenuid()).append("\" target=\"mainframe\">");
				sbf.append("<div class = \"itemBackground\" style=\"background-color:#e8eaf7;padding-top:5px;height:53px\">");
				sbf.append("<div style=\"float:left;width:50px;margin-left:2em;\"><img src=\"").append(menuinfoson.getPic()).append("\" style=\"width:48px;height:48px;border:0px\" /></div>");
				sbf.append("<div style=\"float:left;margin-top: 2em;margin-left: 1.5em;\">").append(menuinfoson.getMenuitem()).append("</div>");
				sbf.append("</div>");
				sbf.append("</A></LI>");
			}
		}
		StringBuffer sbfParentEnd = new StringBuffer();
		sbfParentEnd.append("</UL></DIV>");

		if (menuStatus.equals("noGrandson")) {
			sbfParentStart.append(sbf).append(sbfParentEnd);
			return sbfParentStart.toString();
		}

		return sbf.toString();
	}

	public void updateIsvalid(String userid, String isvalid) {

		SysUser sysUser = sysUserDao.findSysUserByUserId(userid);
		sysUser.setIsvalid(isvalid);
		sysUserDao.updateObject(sysUser);

	}

	public String[] searchUsrGrp(String userid) {
		return dao.searchUsrGrp(userid);
	}

	public Page getPage(SysLoginLog sysLoginLog, int pageNum, int pageSize) {
		return dao.getPage(sysLoginLog, pageNum, pageSize);
	}

	public LoginDao getDao() {
		return dao;
	}

	public void setDao(LoginDao dao) {
		this.dao = dao;
	}

	/**
	 * 根据用户获取该用户查看数据的级别
	 * @param userid
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	public Integer getGrpLevel(String userid) {
		String hql = "select min(u.grpLevel) from UsrGroup u where u.grpcode in (select usrgrp.grpcode FROM UsrUsrgrp usrgrp WHERE usrgrp.userid=?)";
		Integer level = dao.getGrpLevel(hql,userid);
		return level;
	}

	// /////////////////////////////////////////////

	/*
	 * public List findMenuFunction(String userid) {
	 * 
	 * return dao.findMenuFunction(userid); }
	 * 
	 * public MenuInfo getMenuInfo(String menuid){ return
	 * (MenuInfo)dao.getObject(MenuInfo.class,menuid); }
	 */
}
