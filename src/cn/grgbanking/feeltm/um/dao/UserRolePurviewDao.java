/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.MenuFunction;
import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.domain.UsrRolefunc;

import cn.grgbanking.feeltm.um.webapp.vo.MenuFunctionVO;
import cn.grgbanking.feeltm.um.webapp.vo.RolePurviewVO;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;



@Repository  
@SuppressWarnings("unchecked")
public class UserRolePurviewDao extends BaseDao<MenuInfo> {
	


	public List getMenuTree(String rolecode) {
		List menuList = new ArrayList();
		List list = this.getHibernateTemplate().find(
				"from MenuInfo as mf where mf.childnum=0 order by mf.floor, mf.parentid, mf.order");
		
		//*******************************end*******************************//
		for (ListIterator it = list.listIterator(); it.hasNext();) {
			RolePurviewVO vo = new RolePurviewVO();
			MenuInfo menu = (MenuInfo) it.next();
			vo.setParentMenu(this.getParentMenu(menu));
			if (menu.getChildnum() == 0)
				vo.setMenunname(menu.getMenuitem());
			vo.setMenuid(menu.getMenuid());
			vo.setMenuFucList(this.getMenuFunction(menu.getMenuid(),rolecode));
			menuList.add(vo);
		}
		return menuList;

	}

	public List getChildMenu(String menuid, List menuList) {

		String hql = "from MenuInfo as menu where menu.parentid='" + menuid
				+ "'";
		List list = this.getHibernateTemplate().find(hql);

		for (ListIterator it = list.listIterator(); it.hasNext();) {
			MenuInfo menuInfo = (MenuInfo) it.next();
			menuList.add(menuInfo);
			if (menuInfo.getChildnum() > 0) {
				this.getChildMenu(menuInfo.getMenuid(), menuList);
			}

		}
		return menuList;
	}

	// �õ��˵��ĸ��˵�
	public String getParentMenu(MenuInfo menu) {
		List parentModule = new ArrayList();
		MenuInfo paremenu = menu;
		while (!paremenu.getParentid().equals("0")) {
			paremenu = (MenuInfo) this.getHibernateTemplate().get(MenuInfo.class, paremenu
					.getParentid());

			parentModule.add(paremenu.getMenuitem());

		}
		String parentmenuname = new String();
		for (int i = parentModule.size() - 1; i >= 0; i--) {
			String menuname = (String) parentModule.get(i);
			parentmenuname += menuname;
			parentmenuname += " ";
		}
		return parentmenuname.toString();
	}

//	 功能权限表(funcid,menuid,operid) 根据菜单代码获取其所对应的功能点，如增加、删除、修改等
	public List getMenuFunction(String menuid,String rolecode) {
		HashMap operhp = this.getOperHashMap();
		HashMap funhp=this.getFuncidChecked(rolecode);
		List menuFunList = new ArrayList();
		String hql = "from MenuFunction as mf where mf.menuid=?";
		try {
			List list = this.getHibernateTemplate().find(hql, menuid);
			for (ListIterator it = list.listIterator(); it.hasNext();) {
				MenuFunction mf = (MenuFunction) it.next();
				MenuFunctionVO vo = new MenuFunctionVO();
				BeanUtils.copyProperties(vo, mf);
				vo.setOpername((String) operhp.get(mf.getOperid()));//功能点名称
				vo.setChecked((String) funhp.get(mf.getFuncid()));
				menuFunList.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuFunList;
	}


	// 操作功能点表 获取所有不重复的操作员信息 返回一个HashTable，operid为key,opername为values
	public HashMap getOperHashMap() {
		HashMap hp = new HashMap();
		String hql = "from MenuOperate";
		List list = this.getHibernateTemplate().find(hql);
		for (ListIterator it = list.listIterator(); it.hasNext();) {
			MenuOperate moper = (MenuOperate) it.next();
			hp.put(moper.getOperid(), moper.getOpername());
		}
		return hp;
	}

	// 角色功能权限表(id,rolecode,funcid) 根据角色ID获取对象
	public List getUsrRolefunc(String rolecode) {
		String hql = "from UsrRolefunc as urf where urf.rolecode=?";
		List list = this.getHibernateTemplate().find(hql, rolecode);
		return list;
	}
	//把角色所具有权限功能点保存到hashmap中，以funcid为key,对应的值都为checked
	public HashMap getFuncidChecked(String rolecode) {
		List list=this.getUsrRolefunc(rolecode);
		HashMap hp=new HashMap();
		for(int i=0;i<list.size();i++){
			
			UsrRolefunc urf=(UsrRolefunc)list.get(i);
			hp.put(urf.getFuncid(),"checked");
		}
		return hp;
	}
	//保存角色权限的功能点
	public void saveRoleFunc(String rolecode,String[] funcids) {
		for (int i=0;i<funcids.length;i++) {
			UsrRolefunc urf=new UsrRolefunc();
			urf.setRolecode(rolecode);
			urf.setFuncid(funcids[i]);
			this.getHibernateTemplate().save(urf);
		}
	}
	//删除角色权限表中角色所对应的功能点
	public int removeRoleFunc(String rolecode) {
		String hql="delete UsrRolefunc as urf where urf.rolecode='"+rolecode+"'";
		int i=0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		return i;
	}

	public int removeRoleFunc(String[] rolecode) {
		String hql="delete UsrRolefunc as urf where "+SqlHelper.fitStrInCondition("urf.rolecode", rolecode);;
		int i=0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		return 0;
	}
}
