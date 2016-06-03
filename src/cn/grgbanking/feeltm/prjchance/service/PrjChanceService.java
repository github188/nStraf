package cn.grgbanking.feeltm.prjchance.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.PrjChance;
import cn.grgbanking.feeltm.domain.PrjChanceFollow;
import cn.grgbanking.feeltm.domain.PrjChanceOtherInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prjchance.dao.PrjChanceDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("prjChanceService")
@Transactional
public class PrjChanceService extends BaseService {
	@Autowired
	private PrjChanceDao prjChanceDao;
	
	@Transactional(readOnly = true)
	public Page getPage(int pageNum,int pageSize){
		return prjChanceDao.getPage(pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public Page getPrjChanceByCondition(PrjChance prj,int pageNum,int pageSize){
		return prjChanceDao.getPrjChanceByCondition(prj.getPrjName(), prj.getClient(), prj.getFollowMan(), prj.getArea(),prj.getProvince(),prj.getClientManager(),prj.getClientType(),prj.getPrjStage(),prj.getPrjResult(),pageNum, pageSize);
	}
	
	/**根据条件查询商机列表
	 * @param prj
	 * @return
	 */
	public List<PrjChance> getPrjChanceListByCondition(String prjName,
			String client, String followMan, String area, String province,
			String clientManager, String clientType, String prjStage,
			String prjResult){
		return prjChanceDao.getPrjChanceByCondition(prjName,
				 client, followMan,  area,  province,
				 clientManager, clientType, prjStage,
				 prjResult);
	}
	
	@Transactional(readOnly = true)
	public PrjChance getPrjChanceById(String ids) {
		return (PrjChance) prjChanceDao.getObject(PrjChance.class, ids);
	}
	
	public List<PrjChanceFollow> getPrjChanceFollowByChanceId(String chanceId) {
		return (List<PrjChanceFollow>) prjChanceDao.getObjectList("from PrjChanceFollow where chanceId='"+chanceId+"'");
	}
	
	public List<PrjChanceOtherInfo> getPrjChanceOtherInfoByChanceId(String chanceId) {
		return (List<PrjChanceOtherInfo>) prjChanceDao.getObjectList("from PrjChanceOtherInfo where chanceId='"+chanceId+"'");
	}
	
	/**保存商机
	 * @param prjChance
	 * @return
	 */
	public String addPrjChanceInfo(PrjChance prjChance) {
		try {
			String id=prjChanceDao.addObject(prjChance).toString();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
	}
	
	/**保存商机跟进信息
	 * @param chanceFollow
	 * @return
	 */
	public boolean addPrjChanceFollow(PrjChanceFollow chanceFollow) {
		try {
			prjChanceDao.addObject(chanceFollow);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
	}
	
	/**保存商机其他信息
	 * @param info
	 * @return
	 */
	public boolean addPrjChanceOtherInfo(PrjChanceOtherInfo info) {
		try {
			prjChanceDao.addObject(info);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
	}
	

	/**
	 * 删除商机
	 * @param Id
	 * @return
	 */
	public boolean deletePrjChanceInfo(String Id){
		boolean flag = false;
		try {
			prjChanceDao.removeObject(PrjChance.class, Id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**删除商机跟进信息
	 * @param chanceId
	 * @return
	 */
	public boolean deletePrjChanceFollowByChanceId(String chanceId){
		boolean flag = false;
		try {
			prjChanceDao.getHibernateTemplate().bulkUpdate("delete from PrjChanceFollow where chanceId='"+chanceId+"'");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**删除商机其他信息
	 * @param Id
	 * @return
	 */
	public boolean deletePrjChanceOtherInfoByChanceId(String chanceId){
		boolean flag = false;
		try {
			prjChanceDao.getHibernateTemplate().bulkUpdate("delete from PrjChanceOtherInfo where chanceId='"+chanceId+"'");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	/**修改商机
	 * @param prjChance
	 * @return
	 */
	public boolean updatePrjChanceInfo(PrjChance prjChance) {
		boolean flag = false;
		try {
			prjChanceDao.updateObject(prjChance);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	/**修改指定商机的跟进信息
	 * @param chanceId
	 * @return
	 */
	public boolean updatePrjChanceFollowByChanceId(String chanceId,List<PrjChanceFollow> followList) {
		boolean flag = false;
		try {
			//先删除，再保存
			deletePrjChanceFollowByChanceId(chanceId);
			if(followList!=null && followList.size()>=0){
				for(int i=0;i<followList.size();i++){
					PrjChanceFollow follow=followList.get(i);
					addPrjChanceFollow(follow);
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	/**修改指定商机的跟进信息
	 * @param chanceId
	 * @return
	 */
	public boolean updatePrjChanceOtherInfoByChanceId(String chanceId,List<PrjChanceOtherInfo> infoList) {
		boolean flag = false;
		try {
			//先删除，再保存
			deletePrjChanceOtherInfoByChanceId(chanceId);
			if(infoList!=null && infoList.size()>=0){
				for(int i=0;i<infoList.size();i++){
					PrjChanceOtherInfo info=infoList.get(i);
					addPrjChanceOtherInfo(info);
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	public List<PrjChance> getListPrjChanceById(String id){
		return prjChanceDao.getListPrjChanceById(id);
	}

	public List<PrjChance> findAllPrjChance() {
		String hql="  from  PrjChance";
		return prjChanceDao.getObjectList(hql);
	}
	
	

}
