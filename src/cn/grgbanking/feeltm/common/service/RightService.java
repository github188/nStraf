package cn.grgbanking.feeltm.common.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.common.dao.RightDao;
import cn.grgbanking.feeltm.domain.MenuInfo;

@Service
public class RightService {
	
	@Autowired
	private RightDao rightDao;
	
	public HashMap findMenuFuncMap(String userid) {

		return rightDao.findMenuFuncMap(userid);
	}
	public MenuInfo getMenuInfoByMenuId(String menuid){
		return (MenuInfo) rightDao.getMenuInfoByMenuId(menuid);
	}
	
	/**指定用户是否有“月度管理报告”模块查看权限,此方法供微信端调用，以保证和web端权限一致
	 * wtjiao 2014年11月12日 上午9:50:39
	 * @param userid
	 * @return
	 */
	public boolean hasModuleRights_MonthManageReport(String userid){
		Map rightMap=findMenuFuncMap(userid);
		Set keySet = rightMap.keySet();
		Iterator<String> it = keySet.iterator();
		while(it.hasNext()){
			String key = it.next();
			if("ff80808149989ba00149a18c778e057d".equals(key)){//月度管理报告
				return true;
			}
		}
		return false;
	}
}
