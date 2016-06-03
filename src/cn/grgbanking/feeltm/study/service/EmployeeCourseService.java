package cn.grgbanking.feeltm.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.EmployeeCourse;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.study.dao.EmployeeCourseDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("ecService")
@Transactional
public class EmployeeCourseService extends BaseService{
	@Autowired
	private EmployeeCourseDao employeeCourseDao;
	
	
	public boolean add(EmployeeCourse ec){
		boolean flag=false;
		try{
			employeeCourseDao.addObject(ec);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		boolean flag=true;
		try {
			for (String id : ids) {
				employeeCourseDao.removeObject(EmployeeCourse.class, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	
	public boolean delete(EmployeeCourse ec){
		boolean flag = false;
		try {
			employeeCourseDao.removeObject(EmployeeCourse.class, ec.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(EmployeeCourse ec){
		boolean flag=false;
		try{
			employeeCourseDao.updateObject(ec);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public EmployeeCourse getEmployCourseById(String id){
		return (EmployeeCourse)employeeCourseDao.getObject(EmployeeCourse.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String courseName, String uname,String category,String start,String end,int pageNum, int pageSize) {
		return employeeCourseDao.getPage(courseName, uname, category, start, end, pageNum, pageSize);
	}
	
}
