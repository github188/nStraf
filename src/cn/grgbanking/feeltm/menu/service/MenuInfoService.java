package cn.grgbanking.feeltm.menu.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.MenuFunction;
import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.menu.dao.MenuInfoDao;
import cn.grgbanking.feeltm.menu.webapp.vo.MenuOperateVO;
import cn.grgbanking.feeltm.menu.webapp.vo.MenuRedirectVo;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class MenuInfoService extends BaseService {
	@Autowired
	private MenuInfoDao dao;

	public String TOP_PARENTID = "0";

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */

	// -------------------------------------------------------------------------
	public List queryChildList(String parentId) {
		return dao.queryChildList(parentId);
	}

	public String getNavigation(String itemId) {
		return dao.getNavigation(itemId);
	}

	// -------------------------------------------------------------------------
	public MenuInfo findById(String id) {
		MenuInfo dir = (MenuInfo) dao.findById(id);
		return dir;
	}

	public int queryChildnum(String parentId) {
		return dao.getChildCount(parentId);
	}

	// -------------------------------------------------------------------------
	/**
	 * @return 0:������ͬKEY���ֵܽڵ� 1������ɹ�
	 */
	public int addItem(MenuInfo menuInfo) {
		return dao.addItem(menuInfo);
	}

	public void updateMenuOper(String menuid, String[] menuFunLst) {
		// TODO Auto-generated method stub
		this.dao.updateMenuOper(menuid, menuFunLst);

	}

	public MenuInfo findByMenuId(String menuId) {
		return dao.findByMenuId(menuId);
	}

	// -------------------------------------------------------------------------
	/**
	 * ɾ����Ŀ
	 * 
	 * @param idList
	 *            Ҫɾ�����ĿID����ͬID��,�ָ�
	 * @return ɾ�����Ŀ��
	 */
	public int delAll(String idList) {
		return dao.delAll(idList);
	}

	public int reorderItems(String idList) {
		return dao.reorderItems(idList);
	}

	public String getPath(String itemId) {
		return dao.getPath(itemId);
	}

	// -------------------------------------------------------------------------
	public List queryMenuOperate(MenuInfo form, String menuid) {
		List operateLst = this.dao.getHibernateTemplate().loadAll(
				MenuOperate.class);

		List menuOperLst = new ArrayList();
		menuOperLst = this.dao.queryMenuFun(menuid);
		List<MenuOperateVO> menuFunFormLst = new ArrayList<MenuOperateVO>();

		for (int i = 0; i < operateLst.size(); i++) {
			MenuOperate group = (MenuOperate) operateLst.get(i);
			MenuOperateVO menuOperateVO = new MenuOperateVO();

			try {
				BeanUtils.copyProperties(menuOperateVO, group);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 锟斤拷form锟斤拷isCheck,menuid锟斤拷值

			if (menuOperLst.size() != 0 && menuOperLst != null) {
				for (int j = 0; j < menuOperLst.size(); j++) {
					MenuFunction menuOper = (MenuFunction) menuOperLst.get(j);
					if (menuOper.getOperid().equals(menuOperateVO.getOperid())) {
						menuOperateVO.setChecked("checked");
					}
				}
			}
			menuOperateVO.setMenuid(menuid);
			menuFunFormLst.add(menuOperateVO);
		}
		return menuFunFormLst;
	}

	// -------------------------------------------------------------------------
	/**
	 * @return -1:��Ŀ�Ѿ������� 0��������ͬKEY���ֵܽڵ� 1������ɹ�
	 */
	public int updateItem(MenuInfo menuInfo) {
		return dao.updateItem(menuInfo);
	}

	/**
	 * //点击页面菜单连接跳转时，获取父级菜单信息
	 * @param actionto
	 * @throws Exception
	 */
	public MenuRedirectVo getParentMenuInfo(String actionto) throws Exception {
		 int order = 0;
		 MenuRedirectVo menuRedirectVo = new MenuRedirectVo();
			ArrayList<MenuInfo> aryPath = new ArrayList<MenuInfo>();
			MenuInfo menuInfo = dao.findByAction(actionto);
			menuRedirectVo.setMenuId(menuInfo.getMenuid());
			//获取实际的排序
			order =dao.ActualOrderNum(menuInfo.getParentid(),menuInfo.getOrder());
			menuRedirectVo.setLeftMenuOrder(order);
			while (menuInfo != null) {
				aryPath.add(menuInfo);
				if (menuInfo.getParentid().equals(TOP_PARENTID))
					break;
				menuInfo = dao.findById(menuInfo.getParentid());
			}
			if(aryPath.size()>1){
				menuInfo = aryPath.get(1);
				if (menuInfo.getParentid().equals(TOP_PARENTID)) {//二级菜单
					menuRedirectVo.setLeftContentOrder(0);//直接打开第一个div['class=content']
				}else{//三级菜单
					order =dao.ActualOrderNum(menuInfo.getParentid(),menuInfo.getOrder());
					menuRedirectVo.setLeftContentOrder(order);
				}
			}
			if(aryPath.size()>2){//三级菜单
				menuInfo = aryPath.get(2);
				order =dao.ActualOrderNum(menuInfo.getParentid(),menuInfo.getOrder());
				menuRedirectVo.setTopMenuId(menuInfo.getMenuid());
				//还有首页菜单算上，所有加一
				menuRedirectVo.setTopMenuOrder(order+1);
			}
			else{//当菜单只有二级
				menuInfo = aryPath.get(1);
				order =dao.ActualOrderNum(menuInfo.getParentid(),menuInfo.getOrder());
				menuRedirectVo.setTopMenuId(menuInfo.getMenuid());
				//还有首页菜单算上，所有加一
				menuRedirectVo.setTopMenuOrder(order+1);
			}
			return menuRedirectVo;
		}
	// //////////////////////////////////////////////////
}
