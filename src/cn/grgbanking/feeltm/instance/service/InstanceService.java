package cn.grgbanking.feeltm.instance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Instance;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.instance.dao.InstanceDao;
import cn.grgbanking.framework.util.Page;

@Service("instanceService")
@Transactional
public class InstanceService {
	
	@Autowired
	private InstanceDao instanceDao;
	public boolean add(Instance Case1){
		boolean flag=false;
		try{
			instanceDao.addObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public String  save(Instance Case1){
		try{
			String instanceId = (String) instanceDao.addObject(Case1);
			return instanceId;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return null;
		}
		
	}
	
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean delete(Instance Case1){
		boolean flag = false;
		try {
			instanceDao.removeObject(Instance.class, Case1.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(Instance Case1){
		boolean flag=false;
		try{
			instanceDao.updateObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public Instance getCaseById(String id){
		return (Instance)instanceDao.getObject(Instance.class, id);
	}
	/**
	 * 根据项目id或部门key查询不在该项目部门中人员
	 * @param projectId  项目id
	 * @param deptId  部门key
	 * @param username
	 * @return
	 * lhyan3
	 * 014年6月26日
	 */
	public List<SysUser> getNotInInstanceByProject(String[] userids) {
		String hql = "from SysUser s where 1=1";
		if (userids!=null) {
			hql+= "and s.userid not in (";
			for (String userId : userids) {
				if(userId!=null && !"".equals(userId)){
					hql +="'"+userId+"',";
				}
			}
			hql = hql.substring(0, hql.length()-1);
			hql+=")";
		}
		return instanceDao.getNotInInstanceByProject(hql);
	}
	
	/**查询人员
	 * wtjiao 2014年10月10日 下午2:39:05
	 * @param projectId
	 * @param deptId
	 * @param username
	 * @return
	 */
	public List<SysUser> getUserByIdOrName(String deptId,String username) {
		String hql = "from SysUser s where 1=1 ";
		if(deptId!=null && !"".equals(deptId)){
			hql += " and s.deptName='"+deptId+"'";
		}
		if(username!=null && !"".equals(username)){
			hql += " and (s.userid like '%"+username+"%' or s.username like '%"+username+"%')";
		}
		return instanceDao.getNotInInstanceByProject(hql);
	}

	
	
	
	
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String createDate, String createMan,String summary,String category,String username,int pageNum, int pageSize,String raiseEndDate,boolean hasComfirmRight) {
		return instanceDao.getPage(createDate,createMan,summary,category,username,pageNum,pageSize,raiseEndDate,hasComfirmRight);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return instanceDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public Map<String,String> getAllNameAndEmail(){
		return instanceDao.getAllNameAndEmail();
	}
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return instanceDao.getNextNo();
	}
	
}
