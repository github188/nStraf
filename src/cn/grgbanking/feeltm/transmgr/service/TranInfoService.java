package cn.grgbanking.feeltm.transmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.TranAbnoinfo;
import cn.grgbanking.feeltm.domain.TranCallbackAbnoinfo;
import cn.grgbanking.feeltm.domain.TranEspeciinfo;
import cn.grgbanking.feeltm.domain.TranInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.transmgr.dao.TranInfoDao;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class TranInfoService extends BaseService {
	@Autowired
	private TranInfoDao tranInfoDao;

	public boolean addTranInfo(TranInfo obj) {
		boolean flag = false;
		try {
			tranInfoDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteTranInfo(TranInfo obj) {
		boolean flag = false;
		try {
			tranInfoDao.removeObject(TranInfo.class, obj.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateTranInfo(TranInfo obj) {
		boolean flag = false;
		try {
			tranInfoDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}


	@Transactional(readOnly = true)
	public List getTranInfoList(Object[] obj) {
		String sql = "  from TranInfo ";
		return tranInfoDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public TranInfo getTranInfoObject(String id) {
		return (TranInfo) tranInfoDao.getObject(TranInfo.class, id);
	}

	@Transactional(readOnly = true)
	public Page getTranInfoPage(TranInfo trans, int pageNum, int pageSize,
			String beginDateString, String endDateString) {
		String sql = "  from TranInfo trans ";

		if (beginDateString != null && !beginDateString.equals("")) {
			beginDateString = DateUtil
					.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(beginDateString);
		}

		if (endDateString != null && !endDateString.equals("")) {
			endDateString = DateUtil
					.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(endDateString);
		}

		boolean flag = false;
		if (trans.getTransResult() != null && !trans.getTransResult().equals("")) {
			sql += "where trans.transResult=" + trans.getTransResult();
			flag = true;
		}
		if (trans.getAccountNo() != null && !trans.getAccountNo().equals("")) {
			if (flag)
				sql += " and trans.accountNo='" + trans.getAccountNo() + "' ";
			else {
				sql += " where trans.accountNo='" + trans.getAccountNo() + "' ";
				flag = true;
			}
		}
		
		if (trans.getJournalNo() != null && !trans.getJournalNo().equals("")) {
			if (flag)
				sql += " and trans.journalNo='" + trans.getJournalNo() + "'";
			else {
				sql += "  where trans.journalNo='" + trans.getJournalNo() + "'";
				flag = true;
			}
		}

		if (beginDateString != null && !beginDateString.equals("")) {
			if (flag)
				sql += " and trans.transDate||trans.transTime >='"
						+ beginDateString + "'";
			else {
				sql += " where trans.transDate||trans.transTime >='"
						+ beginDateString + "'";
				flag = true;
			}
		}
		if (endDateString != null && !endDateString.equals("")) {
			if (flag)
				sql += " and trans.transDate||trans.transTime <'"
						+ endDateString + "'";
			else {
				sql += " where trans.transDate||trans.transTime <'"
						+ endDateString + "'";
				flag = true;
			}
		}

		sql += "  order by trans.transDate || trans.transTime desc";
		return tranInfoDao.getObjectPage(sql, pageNum, pageSize);
	}
	public boolean  analyseTran(String date){
		  boolean flag=false;
		 
		  try{
			List<TranAbnoinfo> abnoBlackList=this.tranInfoDao.analyseAbnormitBlack(date);
			List<TranAbnoinfo> abnoRepeatList=this.tranInfoDao.analyseAbnormitRepeat(date);
			List<TranCallbackAbnoinfo> abnoCallBackList=this.tranInfoDao.analyseAbnormitCallback(date);
			List<TranEspeciinfo> abnoEspecialList=this.tranInfoDao.analyseAbnormitEspecial(date);
			
			this.tranInfoDao.analyseTranDataAdd(abnoBlackList, abnoRepeatList,abnoCallBackList, abnoEspecialList);
			this.tranInfoDao.removeTranHourDateList(date);
			//this.tranInfoDao.removeTranHourDate(date);
		   // this.tranInfoDao.test();removeTranHourDateList
			flag=true;
			
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		 return flag;
	  }
}
