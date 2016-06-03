package cn.grgbanking.feeltm.um.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.um.dao.UserRolePurviewDao;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class UserRolePurviewService extends BaseService{
	@Autowired
	private UserRolePurviewDao dao;
	

	public List getMenuTree(String rolecode) {
		return dao.getMenuTree(rolecode);
	}

	public List getChildMenu(String menuid,List menuList) {
		return dao.getChildMenu(menuid,menuList);
	}

	public String getParentMenu(MenuInfo menu) {
		return dao.getParentMenu(menu);
	}

	public HashMap getOperHashMap() {
		return dao.getOperHashMap();
	}

	public List getUsrRolefunc(String rolecode) {
		return dao.getUsrRolefunc(rolecode);
	}

	public void saveRoleFunc(String rolecode, String[] funcids) {
		dao.saveRoleFunc(rolecode,funcids);
		
	}

	public int removeRoleFunc(String rolecode) {
		return dao.removeRoleFunc(rolecode);
	}

}
