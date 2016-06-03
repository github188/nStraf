package cn.grgbanking.feeltm.trip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.BusinessTrip;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.trip.dao.TripDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("tripService")
@Transactional
public class TripService extends BaseService{
	@Autowired
	public TripDao tripDao;
	
	public boolean add(BusinessTrip trip){
		boolean flag=false;
		try{
			tripDao.addObject(trip);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

//	public boolean delete(DepartFinance finance){
//		boolean flag = false;
//		try {
//			financeDao.removeObject(DepartFinance.class, finance.getId());
//			flag = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			SysLog.error(e);
//		}
//		return flag;
//	}
	
	public boolean delete(BusinessTrip trip){
		boolean flag = false;
		try {
			tripDao.removeObject(BusinessTrip.class, trip.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(BusinessTrip trip){
		boolean flag=false;
		try{
			tripDao.updateObject(trip);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean checkOt(BusinessTrip trip){
		boolean flag = true;
		try {
			flag = tripDao.checkExist(trip.getUsername(),trip.getStartdate(),trip.getEnddate());
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean checkUpOt(BusinessTrip trip){
		boolean flag = true;
		try {
			flag = tripDao.checkUpExist(trip.getUsername(),trip.getStartdate(),trip.getEnddate(),trip.getId());
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean addTripListInfo(List<BusinessTrip> tripSaveList){
		boolean flag = true;
		try{
			for(BusinessTrip triplist:tripSaveList)
				add(triplist);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public BusinessTrip getDetailById(String id){
		return (BusinessTrip)tripDao.getObject(BusinessTrip.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,String prjName,String username,String deptname,String groupname,int pageNum,int pageSize,UserModel userModel){
		return tripDao.getPage(start, end, prjName, username,deptname,groupname, pageNum, pageSize, userModel);
	}
	
}
