package cn.grgbanking.feeltm.postremove.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.PostRemove;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.postremove.dao.PostRemoveDao;
import cn.grgbanking.framework.util.Page;

@Service("postremoveService")
@Transactional
public class PostRemoveService {
	@Autowired
	private PostRemoveDao postremoveDao;
	public boolean add(PostRemove Case1){
		boolean flag=false;
		try{
			postremoveDao.addObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(PostRemove Case1){
		boolean flag = false;
		try {
			postremoveDao.removeObject(PostRemove.class, Case1.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(PostRemove Case1){
		boolean flag=false;
		try{
			postremoveDao.updateObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public PostRemove getCaseById(String id){
		return (PostRemove)postremoveDao.getObject(PostRemove.class, id);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String name,String advencedate,int pageNum, int pageSize) {
		return postremoveDao.getPage(name,advencedate,pageNum,pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return postremoveDao.getAllNames();
	}
	
	

	
}
