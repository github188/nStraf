package cn.grgbanking.feeltm.modify.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.modify.dao.ModifyRecordDao;
import cn.grgbanking.feeltm.modify.domain.ModifyRecord;
import cn.grgbanking.framework.service.BaseService;

@Service
public class ModifyRecordService extends BaseService{
	
	@Autowired
	private ModifyRecordDao dao;
	
	/**
	 * 保存修改记录
	 * @param modifyId  修改的id
	 * @param username  修改用户
	 * @param operate  修改操作
	 * @param modifyDate  修改日期
	 * @param recorde  修改前的记录
	 * lhyan3
	 * 2014年7月21日
	 */
	public void saveRecord(String modifyId,String username,String operate,Date modifyDate,String record){
		ModifyRecord records = new ModifyRecord();
		records.setModifyId(modifyId);
		records.setUsername(username);
		records.setOperate(operate);
		records.setRecorde(record);
		records.setModifyDate(modifyDate);
		dao.addObject(records);
	}

	/**
	 * 根据id查询记录
	 * @param modifyId
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public List<ModifyRecord> findRecordByModifyId(String modifyId) {
		return dao.findRecordByModifyId(modifyId);
	}
	
}
