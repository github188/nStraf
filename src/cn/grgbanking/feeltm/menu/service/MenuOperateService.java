package cn.grgbanking.feeltm.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.menu.dao.MenuOperateDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class MenuOperateService extends BaseService {
	@Autowired
	private MenuOperateDao dao;

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */

	public List getList(int pageNum, int pageSize) {
		// TODO �Զ���ɷ������
		return dao.getList(pageNum, pageSize);
	}

	public Page getPage(int pageNum, int pageSize) {
		// TODO �Զ���ɷ������
		return dao.getPage(pageNum, pageSize);
	}

	public MenuOperate getMenuOperate(String id) {
		return dao.getMenuOperate(id);
	}

	public void addMenuOperate(MenuOperate menuOperate) {
		dao.addMenuOperate(menuOperate);
	}

	public void updateMenuOperate(MenuOperate menuOperate) {
		dao.updateMenuOperate(menuOperate);
	}

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
}
