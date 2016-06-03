package cn.grgbanking.feeltm.modify.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.modify.domain.ModifyRecord;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
public class ModifyRecordDao extends BaseDao<ModifyRecord>{

	/**
	 * 获取修改记录
	 * @param modifyId
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	@SuppressWarnings("unchecked")
	public List<ModifyRecord> findRecordByModifyId(String modifyId) {
		String hql = "from ModifyRecord r where r.modifyId=? order by modifyDate";
		List<ModifyRecord> records = this.getHibernateTemplate().find(hql, modifyId);
		return records;
	}

}
