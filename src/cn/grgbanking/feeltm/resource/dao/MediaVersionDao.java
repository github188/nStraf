package cn.grgbanking.feeltm.resource.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.MediaVersion;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("mediaVersionDao")
public class MediaVersionDao extends BaseDao<MediaVersion>{
	/**
	 * 增加一个版本修改日志记录
	 */
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<MediaVersion> versions=new ArrayList<MediaVersion>();
		for (int j = 0; j < ids.length; j++) {	
			versions.add(new MediaVersion(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(versions);
			flag=true;
		}catch(Exception e){
			System.out.println("versions delete error!!!!!!!!!!");
		}
		return flag;
	}
}
