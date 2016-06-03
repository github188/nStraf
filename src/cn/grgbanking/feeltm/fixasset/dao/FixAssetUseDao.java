package cn.grgbanking.feeltm.fixasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.fixasset.domain.FixAssetUse;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("fixAssetUseDao")
public class FixAssetUseDao extends BaseDao<FixAssetUse>{
	@SuppressWarnings("unchecked")
	public List<FixAssetUse> getRecordById(String id) {
		String hql = "from FixAssetUse where fixid='"+id+"' order by date desc";
		List<FixAssetUse> records = new ArrayList<FixAssetUse>();
		records = this.getHibernateTemplate().find(hql);
		return records;
	}
	public boolean remove(String[] ids){
		List<FixAssetUse> delList = new ArrayList<FixAssetUse>(); 
		for(int i=0;i<ids.length;i++){
			delList.add(new FixAssetUse(ids[i]));
		}
		try{
			this.getHibernateTemplate().deleteAll(delList);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return false;
		}
	}
}
