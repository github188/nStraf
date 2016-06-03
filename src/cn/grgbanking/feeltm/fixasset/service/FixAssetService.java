package cn.grgbanking.feeltm.fixasset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.fixasset.dao.FixAssetDao;
import cn.grgbanking.feeltm.fixasset.domain.FixAsset;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;
@Service("fixAssetService")
@Transactional
public class FixAssetService extends BaseService{
	@Autowired
	private FixAssetDao fixAssetDao;
	
	@Transactional(readOnly=true)
	public Page getPage(int pageNum,int pageSize,String type,String no,String name,String status,String inman,String useman,UserModel userModel){
		return fixAssetDao.getPage(pageNum, pageSize,type,no,name,status,inman,useman,userModel);
	}
	public boolean add(FixAsset fixAsset){
		try{
			fixAssetDao.addObject(fixAsset);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return false;
		}
	}
	public boolean update(FixAsset fixAsset){
		try{
			fixAssetDao.updateObject(fixAsset);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return false;
			
		}
	}
	public boolean delete(String[] ids){
		return fixAssetDao.remove(ids);
	}
	@Transactional(readOnly = true)
	public FixAsset getFixAssetById(String id){
		return (FixAsset)fixAssetDao.getObject(FixAsset.class, id);
	}
	
	public int checkFixAssetNo(String no,String id){
		return fixAssetDao.checkFixAssetNo(no, id);
	}
}
