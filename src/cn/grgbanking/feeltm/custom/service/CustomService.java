package cn.grgbanking.feeltm.custom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.custom.dao.CustomDao;
import cn.grgbanking.feeltm.domain.Custom;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("customService")
@Transactional
public class CustomService extends BaseService {

	@Autowired
	private CustomDao customDao;
	
	/**
	 * 获取全部信息
	 * @param pageNum
	 * @param pageSize
	 * @param hasRight
	 * @param updateMan
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getPage(int pageNum,int pageSize,boolean hasRight){
		return customDao.getPage(pageNum, pageSize, hasRight);
	}
	
	/**
	 * 根据条件查询信息
	 * @param custom
	 * @param pageNum
	 * @param pageSize
	 * @param hasRight
	 * @param updateMan
	 * @param creatEndDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getPageByCondition(Custom custom,int pageNum,int pageSize,
			boolean hasRight,String creatEndDate){
		return customDao.getCustomByCondition(custom, pageNum, pageSize, hasRight, creatEndDate);
	}
	
	/**
	 * 根据ids获取信息
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = true)
	public Custom getCustomById(String ids) {
		return (Custom) customDao.getObject(Custom.class, ids);
	}
	
	public List<Custom> getListCustomById(String id){
		return customDao.getListCustomById(id);
	}
	
	/**
	 * 添加
	 * @param custom
	 * @return
	 */
	public boolean addCustomInfo(Custom custom) {
		boolean flag = false;
		try {
			customDao.addObject(custom);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	/**
	 * 修改
	 * @param custom
	 * @return
	 */
	public boolean updateCustomInfo(Custom custom) {
		boolean flag = false;
		try {
			customDao.updateObject(custom);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	/**
	 * 删除
	 * @param Id
	 * @return
	 */
	public boolean deleteCustomInfo(String Id){
		boolean flag = false;
		try {
			customDao.removeObject(Custom.class, Id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

}
