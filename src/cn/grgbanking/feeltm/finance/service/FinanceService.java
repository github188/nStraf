package cn.grgbanking.feeltm.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.finance.dao.FinanceDao;
import cn.grgbanking.feeltm.finance.domain.BalanceInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;


@Service("financeService")
@Transactional
public class FinanceService extends BaseService{
	@Autowired
	private FinanceDao financeDao;
	
	public boolean add(DepartFinance finance){
		boolean flag=false;
		try{
			financeDao.addObject(finance);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean delete(DepartFinance finance){
		boolean flag = false;
		try {
			financeDao.removeObject(DepartFinance.class, finance.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(DepartFinance finance){
		boolean flag=false;
		try{
			financeDao.updateObject(finance);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public DepartFinance getDetailById(String id){
		return (DepartFinance)financeDao.getObject(DepartFinance.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,String type,String responsible, int pageNum, int pageSize) {
		return financeDao.getPage(start, end, type, responsible, pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public BalanceInfo getBalance(String start,String end){
		return financeDao.getBalance(start, end);
	}
}
