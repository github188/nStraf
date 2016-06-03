package cn.grgbanking.feeltm.expense.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.expense.dao.ExpenseInfoDao;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccount;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountCheckCondition;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountProject;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountTravelDetail;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("expenseInfoService")
@Transactional
public class ExpenseInfoService extends BaseService {
	
	@Autowired
	private ExpenseInfoDao expenseInfoDao;
	
	/**
	 * 根据条件查询报销信息
	 * @param userName 报销人
	 * @param detName 部门
	 * @param groupName 组别
	 * @param expenseNum 报销流水号
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getPageByCondition(OAExpenseAccount expenseAccount,int pageNum, int pageSize,UserModel userModel,String submitDate,String submitEndDate) {
		return expenseInfoDao.getPageByCondition(expenseAccount, pageNum, pageSize,userModel,submitDate,submitEndDate);
	}
	
	/**
	 * 增加
	 * @param expenseAccount
	 * @return
	 */
	public boolean addExpenseInfo(OAExpenseAccount expenseAccount){
		boolean flag=false;
		try{
			expenseInfoDao.addObject(expenseAccount);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	public boolean addOAExpenseCheckCondition(OAExpenseAccountCheckCondition o){
		boolean flag = false;
		try{
			expenseInfoDao.addObject(o);
			flag = true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addOAExpenseTravelDetail(OAExpenseAccountTravelDetail o){
		boolean flag = false;
		try{
			expenseInfoDao.addObject(o);
			flag = true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	/**
	 * 删除
	 * @param expenseId
	 * @return
	 */
	public boolean deleteExpenseInfo(String expenseId){
		boolean flag = false;
		try {
			expenseInfoDao.removeObject(OAExpenseAccount.class, expenseId);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 删除出差明细
	 */
	public boolean deleteCostDetail(String id){
		boolean flag = false;
		try{
			expenseInfoDao.removeObject(OAExpenseAccountCheckCondition.class, id);
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 删除费用明细表
	 */
	public boolean deleteTravelDetail(String id){
		boolean flag = false;
		try{
			expenseInfoDao.removeObject(OAExpenseAccountTravelDetail.class, id);
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 修改
	 * @param expenseAccount
	 * @return
	 */
	public boolean updateExpenseInfo(OAExpenseAccount expenseAccount){
		boolean flag=false;
		try{
			expenseInfoDao.updateObject(expenseAccount);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 新增报销记录时获得下一个报销流水号
	 * @return
	 */
	@Transactional(readOnly=true)
	public String getNextEno(){
		return expenseInfoDao.getNextEno();
	}
	
	/**
	 * 根据id查找报销记录
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public OAExpenseAccount getExpenseInfoById(String id) {
		return (OAExpenseAccount)expenseInfoDao.getObject(OAExpenseAccount.class, id);
	}
	
	public OAExpenseAccount getExpenseInfoByexpenseNum(String expenseNum){
		return expenseInfoDao.getOAExpenseAccountByexpenseNum(expenseNum);
	}
	
	public List<UsrGroup> getAllUsrgroup(){
		return expenseInfoDao.getAllUserGroup();
	}
	public List<OAExpenseAccountCheckCondition> getDetailByUUID(String userId){
		return expenseInfoDao.getDeTailByUUID(userId);
	}
	public List<OAExpenseAccountTravelDetail> getTarvelByUUID(String userId){
		return expenseInfoDao.getTravelDeTailByUUID(userId);
	}
	
	public OAExpenseAccount getExpenseInfoByuserName(String userName){
		return expenseInfoDao.OAExpenseAccountgetByUsername(userName);
	}
	
	@Transactional(readOnly=true)
	public Object[] getUserInfo(String userId) {
		List list = expenseInfoDao.getUserInfo(userId);
		if(list!=null&&list.size()==1){
			return (Object[]) list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取报销出差明细
	 * @param id  报销单id
	 * @return  
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountCheckCondition> getExpenseDetailByExpenseId(
			String id) {
		return expenseInfoDao.getExpenseDetailByExpenseId(id);
	}
	
	/**
	 * 费用报销明细
	 * @param id 报销单id
	 * @return 费用明细
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountTravelDetail> getTravelDetailByExpenseId(
			String id) {
		return expenseInfoDao.getTravelDetailByExpenseId(id);
	}

	/**
	 * 批量保存对象
	 * @param list
	 * lhyan3
	 * 2014年7月18日
	 */
	public void saveList(List list) {
		expenseInfoDao.saveList(list);
	}

	/**
	 * 保存
	 * @param costSum
	 * lhyan3
	 * 2014年7月18日
	 */
	public void saveCostDetail(OAExpenseAccountCheckCondition costSum) {
		expenseInfoDao.addObject(costSum);
		
	}

	/**
	 * 
	 * @param travelSum
	 * lhyan3
	 * 2014年7月18日
	 */
	public void saveTravelDetail(OAExpenseAccountTravelDetail travelSum) {
		expenseInfoDao.addObject(travelSum);
		
	}

	/**
	 * 得到出差合计
	 * @param id
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountCheckCondition getCostSum(String id) {
		return expenseInfoDao.getCostSum(id);
	}

	/**
	 * 得到费用合计
	 * @param id
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountTravelDetail getTravelSum(String id) {
		return expenseInfoDao.getTravelSum(id);
	}

	/**
	 * 删除出差明细详细并作为修改记录
	 * @param detailId
	 * lhyan3
	 * 2014年7月18日
	 */
	public void deleteCostDetailAsRecord(String detailId) {
		
	}

	/**
	 * 根据id获取明细记录
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountCheckCondition getCostDetail(String detailId) {
		return expenseInfoDao.getCostDetail(detailId);
	}

	/**
	 * 
	 * @param costDetail
	 * lhyan3
	 * 2014年7月18日
	 */
	public void updateCostDetail(OAExpenseAccountCheckCondition costDetail) {
		expenseInfoDao.updateObject(costDetail);
		
	}

	/**
	 * 
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public OAExpenseAccountTravelDetail getTravelDetail(String detailId) {
		return expenseInfoDao.getTravelDetail(detailId);
	}

	/**
	 * 
	 * @param travelSum
	 * lhyan3
	 * 2014年7月21日
	 */
	public void updateTravel(OAExpenseAccountTravelDetail travelSum) {
		expenseInfoDao.updateObject(travelSum);
	}
	
	
	/**
	 *  获取项目金额明细
	 * @param id  报销单id
	 * @return  
	 * lhyan3
	 * 2014年7月18日
	 */
	public List<OAExpenseAccountProject> getExpenseProjectSumByExpenseId(
			String id) {
		return expenseInfoDao.getExpenseProjectSumByExpenseId(id);
	}
	
	/**
	 * 根据id获取项目金额明细
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountProject getProjectSumDetail(String detailId) {
		return expenseInfoDao.getProjectSumDetail(detailId);
	}
	
	/**
	 * 根据uuid获取项目金额明细
	 * @param detailId
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public OAExpenseAccountProject getProjectSumDetailByUuid(String detailId) {
		return expenseInfoDao.getProjectSumDetailByUuid(detailId);
	}
	
	/**
	 * 删除项目金额明细
	 */
	public boolean deleteProjectSumDetail(String id){
		boolean flag = false;
		try{
			expenseInfoDao.removeObject(OAExpenseAccountProject.class, id);
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	/**
	 * 修改
	 * @param expenseAccount
	 * @return
	 */
	public boolean updateExpenseProjectSumInfo(OAExpenseAccountProject projectSumAccount){
		boolean flag=false;
		try{
			expenseInfoDao.updateObject(projectSumAccount);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
}






