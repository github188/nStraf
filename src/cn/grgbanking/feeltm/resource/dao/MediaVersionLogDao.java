package cn.grgbanking.feeltm.resource.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.MediaUpdateLog;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("mediaLogDao")
public class MediaVersionLogDao extends BaseDao<MediaUpdateLog>{
	public List<MediaUpdateLog> getLogs(String media){
		//version
		String query="from MediaUpdateLog log where log.media='"+media+"' order by log.update_date desc,log.update_version desc";
		List<MediaUpdateLog> list=(List<MediaUpdateLog>)this.getHibernateTemplate().find(query);
//		List list=this.getHibernateTemplate().find(query);
		return list;
	}
	
	public void addLog(MediaUpdateLog log){
		String version=log.getUpdate_version();
		String query="from MediaUpdateLog log where log.update_version='"+version+"' and log.media='"+log.getMedia()+"'";
		List list=this.getHibernateTemplate().find(query);
		if(list!=null&&list.size()>0){  //有同一版本的数据,则不记录
			
		}else{
			addObject(log);
		}
	}
}
