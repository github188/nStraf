/**
 * 
 */
package cn.grgbanking.feeltm.org.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.OrgInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.org.dao.OrgInfoDao;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class OrgInfoService extends BaseService {
	@Autowired
	private OrgInfoDao dao = new OrgInfoDao();

	public static final String TOP_ID = "0000000000";// �����ID
	public static final String TOP_PARENTID = "Top_Parentid";//

	/*
	 * 根据orgid得到一个机构对象
	 * 
	 * @see
	 * cn.grgbanking.feeltm.org.dao.OrgInfoDAO#getOrgInfoList(java.lang.String)
	 */
	@Transactional(readOnly = true)
	public OrgInfo getOrgInfoByOrgId(String orgId) {
		//return dao.getOrgInfoByOrgId(orgId);
		String sql = " from OrgInfo org where org.orgid ='"+orgId+"' ";
		//String sql = " from OrgInfo ";
		List<OrgInfo> list = dao.getObjectList(sql);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public int getCountChild(String parentid) throws DataAccessException {
		return dao.getCountChild(parentid);
	}

	@Transactional(readOnly = true)
	public String findParentName(String printid) throws DataAccessException {
		return dao.findParentName(printid);
	}

	@Transactional(readOnly = true)
	public String findParentid(String orgid) throws DataAccessException {
		return dao.findParentid(orgid);
	}

	@Transactional(readOnly = true)
	public List getOrgChildList(String parentid) {
		List list = null;
		try {
			list = dao.getOrgInfoChildList(parentid);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<OrgInfo> getOrgInfoListByOrgid(String orgid){
		String sql = "from OrgInfo o where o.orgid ='"+orgid+"' ";
		return dao.getObjectList(sql);
	}
	@Transactional(readOnly = true)
	public int getChildLevel(OrgInfo orgInfo) throws DataAccessException {
		return dao.getChildLevel(orgInfo);
	}

	@Transactional(readOnly = true)
	public boolean isOnlyOrgid(OrgInfo orgInfo) throws DataAccessException {
		boolean flag = false;
		try {
			OrgInfo orgInfos = dao.getOrgInfoByOrgId(orgInfo.getOrgid());
			if (orgInfos == null)
				flag = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return flag;
	}

	/*
	 * 增加机构
	 */
	public boolean addOrgInfo(OrgInfo orgInfo, OrgInfo parentOrgInfo)
			throws DataAccessException {
		boolean flag = false;
		try {
			dao.addObject(orgInfo);
			if (parentOrgInfo != null) {
				parentOrgInfo.setChildnum(parentOrgInfo.getChildnum() + 1);
				dao.updateObject(parentOrgInfo);

			}
			flag = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;

	}

	public int updateOrgInfo(OrgInfo orgInfo) {
		int flag = 0;
		try {
			dao.updateObject(orgInfo);
			flag = 1;
		} catch (Exception e) {

		}
		return flag;
	}

	public boolean removeOrgInfo(OrgInfo orgInfo, OrgInfo parentOrgInfo) {
		boolean flag = false;
		try {
			dao.removeOrgInfo(orgInfo.getOrgid());

			if (parentOrgInfo != null) {
				parentOrgInfo.setChildnum(parentOrgInfo.getChildnum() - 1);
				dao.updateObject(parentOrgInfo);
			}

			flag = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;

	}

	@Transactional(readOnly = true)
	public int getChildOrder(OrgInfo orgInfo) throws DataAccessException {
		return dao.getChildOrder(orgInfo);
	}
	// ///////////////////////////////////////////////////

}
