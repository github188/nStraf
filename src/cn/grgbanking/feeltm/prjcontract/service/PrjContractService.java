package cn.grgbanking.feeltm.prjcontract.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.PrjContract;
import cn.grgbanking.feeltm.domain.PrjContractPayment;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prjcontract.dao.PrjContractDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("prjContractService")
@Transactional
public class PrjContractService extends BaseService{
	@Autowired
	private PrjContractDao prjContractDao;
	
	@Transactional(readOnly = true)
	public Page getPage(int pageNum,int pageSize,boolean hasRight,String updateMan){
		return prjContractDao.getPage(pageNum, pageSize,hasRight,updateMan);
	}
	
	@Transactional(readOnly = true)
	public Page getPrjContractByCondition(PrjContract prj,int pageNum,int pageSize,boolean hasRight,String updateMan){
		return prjContractDao.getPrjContractByCondition(prj.getPrjName(), prj.getPrjManager(), prj.getStatus(), prj.getStartDate(), 
				prj.getEndDate(), pageNum, pageSize,hasRight,updateMan);
	}
	
	@Transactional(readOnly = true)
	public PrjContract getPrjContractById(String ids) {
		return (PrjContract) prjContractDao.getObject(PrjContract.class, ids);
	}
	
	public boolean addPrjContractInfo(PrjContract prjContract) {
		boolean flag = false;
		try {
			prjContractDao.addObject(prjContract);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	/**
	 *新增回款记录 
	 */
	public boolean addPrjPaymentInfo(PrjContractPayment payment) {
		boolean flag = false;
		try {
			prjContractDao.addObject(payment);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	public List<PrjContract> getListPrjContractById(String id){
		return prjContractDao.getListPrjContractById(id);
	}
	
	public boolean updateContactInfo(PrjContract prjContract) {
		boolean flag = false;
		try {
			prjContractDao.updateObject(prjContract);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	/**
	 * 删除
	 * @param Id
	 * @return
	 */
	public boolean deletePrjContractInfo(String Id){
		boolean flag = false;
		try {
			prjContractDao.removeObject(PrjContract.class, Id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * @param contractId 合同ID
	 * @return list 回款记录
	 * lping1
	 * 2014年6月24日
	 */
	public List<PrjContractPayment> getListPrjPaymentById(String contractId){
		return prjContractDao.getListPrjPaymentById(contractId);
	}

}
