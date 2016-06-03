package cn.grgbanking.feeltm.common4Wechat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.dao.BaseDao;

/**
 * 微信用户绑定校验<br>
 * @author zzhui1 
 */
@Repository  
@SuppressWarnings("unchecked") 
public class Common4WechatDao extends BaseDao<SysUser> {

	/**
	 * 根据用户ID查询
	 * @param userid 用户ID
	 * @return 用户信息
	 */
	public SysUser findSysUserByUserId(String userid) {
		try {
			SysUser su = null;
			String sql = " from  SysUser  su where su.userid= '" + userid + "'";
			List<SysUser> list = this.getHibernateTemplate().find(sql);
			if (list != null && list.size() > 0){
				su = (SysUser) list.get(0);
			}
			return su;
		} catch (Exception e) {
			SysLog.error(e);
			return null;
		}
	}
}
