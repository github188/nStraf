package cn.grgbanking.feeltm.personaldevelop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.AbilityAnalyse;
import cn.grgbanking.feeltm.domain.testsys.AbilityDevelopPlan;
import cn.grgbanking.feeltm.domain.testsys.AbilityLog;
import cn.grgbanking.feeltm.domain.testsys.WaitupDevelop;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.personaldevelop.dao.AbilityAnalyseDao;
import cn.grgbanking.feeltm.personaldevelop.dao.AbilityDevelopPlanDao;
import cn.grgbanking.feeltm.personaldevelop.dao.AbilityLogDao;
import cn.grgbanking.feeltm.personaldevelop.dao.WaitupDevelopDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("personaldevelopService")
@Transactional
public class PersonalDevelopService extends BaseService{
	@Autowired
	private AbilityDevelopPlanDao abilityDevelopPlanDao;
	@Autowired
	private AbilityAnalyseDao  abilityAnalyseDao;
	@Autowired
	private WaitupDevelopDao waitupDevelopDao;
	@Autowired
	private AbilityLogDao abilityLogDao;
	
	
	public boolean addPersonalDevelop(AbilityAnalyse personaldevelop){
		boolean flag=false;
		try{
			abilityAnalyseDao.addObject(personaldevelop);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addAbilityDevelopPlan(AbilityDevelopPlan config){
		boolean flag=false;
		try{
			abilityDevelopPlanDao.addObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addAbilityLog(AbilityLog config){
		boolean flag=false;
		try{
			abilityLogDao.addObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean addWaitupDevelop(WaitupDevelop version){
		boolean flag=false;
		try{
			waitupDevelopDao.addObject(version);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		boolean success=true;
		try{
			abilityAnalyseDao.remove(ids);
			abilityLogDao.remove(ids);
			abilityDevelopPlanDao.remove(ids);
			waitupDevelopDao.remove(ids);		
		}catch(Exception e){
			success=false;
		}
		return success;
	}
	
	public boolean delete(AbilityAnalyse abilityanalyse){
		boolean flag = false;
		try {
			abilityAnalyseDao.removeObject(AbilityAnalyse.class, abilityanalyse.getId());
			abilityLogDao.removeObject(AbilityLog.class, abilityanalyse.getId());
			abilityDevelopPlanDao.removeObject(AbilityDevelopPlan.class, abilityanalyse.getId());
			waitupDevelopDao.removeObject(WaitupDevelop.class, abilityanalyse.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updatePersonalDevelop(AbilityAnalyse personaldevelop){
		boolean flag=false;
		try{
			abilityAnalyseDao.updateObject(personaldevelop);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updateWaitupDevelop(WaitupDevelop config){
		boolean flag=false;
		try{
			waitupDevelopDao.updateObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updateAbilityLog(AbilityLog config){
		boolean flag=false;
		try{
			abilityLogDao.updateObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	public boolean updateAbilityDevelopPlan(AbilityDevelopPlan version){
		boolean flag=false;
		try{
			abilityDevelopPlanDao.updateObject(version);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	@Transactional(readOnly = true)
	public AbilityAnalyse getpersonaldevelopById(String id){
		return (AbilityAnalyse)abilityAnalyseDao.getObject(AbilityAnalyse.class, id);
	}
	
	@Transactional(readOnly = true)
	public WaitupDevelop getWaitupDevelopById(String id){
		WaitupDevelop config=null;
		try{
		config=(WaitupDevelop)waitupDevelopDao.getObject(WaitupDevelop.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return config;
	}
	
	@Transactional(readOnly = true)
	public AbilityDevelopPlan getAbilityDevelopPlanById(String id){
		AbilityDevelopPlan version=null;
		
		try{
			version=(AbilityDevelopPlan)abilityDevelopPlanDao.getObject(AbilityDevelopPlan.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return version;
	}
	
	@Transactional(readOnly = true)
	public String getHeadman(String groupname){
		return abilityAnalyseDao.getHeadman(groupname);
	}
	
	@Transactional(readOnly = true)
	public String getUserid(String name){
		return abilityAnalyseDao.getUserid(name);
	}

	@Transactional(readOnly = true)
	public AbilityLog getAbilityLogById(String id){
		AbilityLog version=null;
		
		try{
			version=(AbilityLog)abilityLogDao.getObject(AbilityLog.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return version;
	}
	

	@Transactional(readOnly = true)
	public Page getPage(String name,String year,String status,String querygroup,String queryname,int querylevel, int pageNum, int pageSize) {
		return abilityAnalyseDao.getPage(name, year,status,querygroup,queryname,querylevel, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public boolean checkExist(String deviceNo) {
		return abilityAnalyseDao.check(deviceNo);
	}
}
