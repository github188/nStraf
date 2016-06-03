package cn.grgbanking.feeltm.study.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.TrainingRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.study.dao.TrainDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("trainService")
public class TrainService extends BaseService{
	@Autowired
	private TrainDao trainDao;

	public boolean add(TrainingRecord record){
		boolean flag=false;
		try{
			trainDao.addObject(record);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean update(TrainingRecord record){
		boolean flag=false;
		try{
			trainDao.updateObject(record);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(TrainingRecord manage){
		boolean flag = false;
		try {
			trainDao.removeObject(TrainingRecord.class, manage.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllStudents(){
		return trainDao.getAllStudents();
	}
	
	@Transactional(readOnly = true)
	public TrainingRecord getTrainById(String id){
		return (TrainingRecord)trainDao.getObject(TrainingRecord.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page  getPage(String courseName,String teacher,String category, String student,String start,String end,int pageNum, int pageSize) {
		if(student!=null&&student.equals("ALL_VALUE")){
			student="";
		}
		return trainDao.getPage(courseName, teacher, category, student, start, end, pageNum, pageSize);
	}
}
