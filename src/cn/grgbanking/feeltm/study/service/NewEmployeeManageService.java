package cn.grgbanking.feeltm.study.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Course;
import cn.grgbanking.feeltm.domain.testsys.EmployeeCourse;
import cn.grgbanking.feeltm.domain.testsys.NewEmployeeManage;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.study.dao.CourseDao;
import cn.grgbanking.feeltm.study.dao.EmployeeCourseDao;
import cn.grgbanking.feeltm.study.dao.NewEmployeeManageDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("emService")
@Transactional
public class NewEmployeeManageService extends BaseService{
	@Autowired
	private NewEmployeeManageDao newEmployeeManageDao;
	
	@Autowired
	private EmployeeCourseDao ecDao;
	
	@Autowired
	private CourseDao courseDao;

	public boolean add(NewEmployeeManage manage){
		boolean flag=false;
		try{
			newEmployeeManageDao.addObject(manage);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean update(NewEmployeeManage manage){
		boolean flag=false;
		try{
			newEmployeeManageDao.updateObject(manage);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public NewEmployeeManage getNewEmployeeManageById(String id){
		return (NewEmployeeManage)newEmployeeManageDao.getObject(NewEmployeeManage.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String uname, String groupName,String detName,int pageNum, int pageSize) {
		return newEmployeeManageDao.getPage(uname, groupName, detName,pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<EmployeeCourse> showDistributeCourses(String uid){
		List<EmployeeCourse> ecs= ecDao.getCoursesByUid(uid);
		if(ecs!=null&&ecs.size()>0){  //课程修改
			
		}else{
			List<Course> cs=courseDao.getAllCourses();//课程分配
			for(Course c:cs){
				EmployeeCourse ec=new EmployeeCourse();
				ec.setCid(c.getId()+":"+c.getCourseName());
				ec.setCourseName(c.getCourseName());
				ecs.add(ec);
			}
		}
		return ecs;
	}
	
	@Transactional(readOnly = true)
	public List<EmployeeCourse> coursesSelect(){
		List<EmployeeCourse> ecs=new ArrayList<EmployeeCourse>();
			List<Course> cs=courseDao.getAllCourses();
			for(Course c:cs){
				EmployeeCourse ec=new EmployeeCourse();
				ec.setCid(c.getId()+":"+c.getCourseName());  //select的value为C0001:课程名
				ec.setCourseName(c.getCourseName());
				ecs.add(ec);
			}
		
		return ecs;
	}
	
	
	
	@Transactional(readOnly = true)
	public List<EmployeeCourse> showDistributeCoursesByUid(String uid){
		List<EmployeeCourse> ecs= ecDao.getCoursesByUid(uid);
		return ecs;
	}
	
	@Transactional(readOnly = true)
	public List<EmployeeCourse> getCoursesIdByUid(String uid){
		List<EmployeeCourse> ecs= ecDao.getCoursesIdByUid(uid);
		return ecs;
	}
	
	public boolean updateEmployeeCourses(List<EmployeeCourse> ecs){
		boolean flag=false;
		try{
			ecDao.assignCourses(ecs);
			flag=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	public boolean delete(NewEmployeeManage manage){
		boolean flag = false;
		try {
			newEmployeeManageDao.removeObject(NewEmployeeManage.class, manage.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

}
