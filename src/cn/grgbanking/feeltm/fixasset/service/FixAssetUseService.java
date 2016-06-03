package cn.grgbanking.feeltm.fixasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.fixasset.dao.FixAssetUseDao;
import cn.grgbanking.feeltm.fixasset.domain.FixAssetUse;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
@Service("fixAssetUseService")
@Transactional
public class FixAssetUseService extends BaseService{
	@Autowired
	private FixAssetUseDao fixAssetUseDao;
	
	public List<FixAssetUse> getRecordById(String id){
		return fixAssetUseDao.getRecordById(id);
	}
	
	public boolean add(FixAssetUse fixAssetUse){
		try{
			fixAssetUseDao.addObject(fixAssetUse);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return false;
		}
	}
	public boolean delete(String[] ids){
		return fixAssetUseDao.remove(ids);
	}
	
}
