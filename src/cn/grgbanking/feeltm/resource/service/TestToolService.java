package cn.grgbanking.feeltm.resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.TestTool;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.resource.dao.TestToolDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("toolService")
@Transactional
public class TestToolService extends BaseService{
	@Autowired
	private TestToolDao toolDao;
	
	public boolean add(TestTool tool){
		boolean flag=false;
		try{
			toolDao.addObject(tool);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return toolDao.remove(ids);
	}
	
	public boolean delete(TestTool tool){
		boolean flag = false;
		try {
			toolDao.removeObject(TestTool.class, tool.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean update(TestTool tool){
		boolean flag=false;
		try{
			toolDao.updateObject(tool);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public TestTool getToolById(String id){
		return (TestTool)toolDao.getObject(TestTool.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String toolName, String assort,int pageNum, int pageSize) {
		return toolDao.getPage(toolName,assort, pageNum, pageSize);
	}
}
