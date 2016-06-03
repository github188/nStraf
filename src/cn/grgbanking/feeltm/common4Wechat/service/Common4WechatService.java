package cn.grgbanking.feeltm.common4Wechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.common4Wechat.dao.Common4WechatDao;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.framework.service.BaseService;

/**
 * 微信用户绑定校验Service
 * @author zzhui1
 *
 */
@SuppressWarnings("unchecked")
@Service
public class Common4WechatService extends BaseService {
	/** 微信用户绑定校验 */
	@Autowired
	private Common4WechatDao accountBindDao;

	/**
	 * 根据用户ID查询<br>
	 * @param accountno 用户ID
	 * @return 用户信息
	 */
	public SysUser findSysUserByUserId(String accountno) {
		return accountBindDao.findSysUserByUserId(accountno);
	}
	
	/**
	 * 
	 * @param userid
	 * @param isvalid
	 */
	public void updateIsvalid(String userid, String isvalid) {
		SysUser sysUser = accountBindDao.findSysUserByUserId(userid);
		sysUser.setIsvalid(isvalid);
		accountBindDao.updateObject(sysUser);

	}
}