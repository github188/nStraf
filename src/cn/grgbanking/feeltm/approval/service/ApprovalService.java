package cn.grgbanking.feeltm.approval.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.approval.dao.ApprovalDao;
import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

/**
 * 审批记录
 * @author lhy 2014-5-5
 *
 */
@Service
@Transactional
public class ApprovalService extends BaseService{
	
	@Autowired
	private ApprovalDao dao;
	
	
	/**
	 * @param recode  关联的审批编号 (如通知编号 N0001)
	 * @param name  审批名称 拼接（如通知审批）
	 * @param userid 审批人
	 * @param option  审批意见
	 * @param result  审批结果
	 */
	public void makeRecored(String recode,String name,String userid,String option,String result){
		ApprovalRecord record = new ApprovalRecord();
		record.setApprovalName(recode);
		record.setApprovalTime(new Date());
		record.setApprovalUser(userid);
		record.setOpinion(option);
		record.setRecodeName(name);
		record.setResult(result);
		this.save(record);
	}
	
	/**
	 * 用户批量处理同一批数据记录时，做审批记录信息保存
	 * @param recode  关联的审批编号 (如通知编号 N0001)
	 * @param name  审批名称 拼接（如通知审批）
	 * @param userid 审批人
	 * @param option  审批意见
	 * @param result  审批结果
	 */
	public void makeMoreRecored(String recode,String name,String userid,String option,String result,String approveDate){
		ApprovalRecord record = new ApprovalRecord();
		record.setApprovalName(recode);
		record.setApprovalTime(DateUtil.stringToDate(approveDate, "yyyy-MM-dd HH:mm:ss"));
		record.setApprovalUser(userid);
		record.setOpinion(option);
		record.setRecodeName(name);
		record.setResult(result);
		this.save(record);
	}

	/**
	 * lhy 2014-5-5
	 * 保存
	 * @param record
	 */
	public void save(ApprovalRecord record) {
		dao.addObject(record);
	}
	
	/**
	 * lhy 2014-5-5
	 * 根据审批流水号获取该审批记录
	 * @param name
	 * @return
	 */
	public List<ApprovalRecord> getRecordByName(String name){
		return dao.getRecordByName(name);
	}
	
	/**
	 * 根据name值获取所有审批记录信息
	 * @param name
	 * @return
	 */
	public List getMoreRecordByName(String name){
		return dao.getMoreRecordByName(name);
	}
	
	/**
	 * 根据name值得出审核人信息
	 * @param name
	 * @return
	 */
	public String getGroupmanageInfo(String name){
		return dao.getGroupmanageInfo(name);
	}
}
