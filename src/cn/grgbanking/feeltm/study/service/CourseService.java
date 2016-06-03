package cn.grgbanking.feeltm.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Course;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.study.dao.CourseDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("courseService")
@Transactional
public class CourseService extends BaseService{
	
	@Autowired
	private CourseDao courseDao;

	public boolean add(Course cource){
		boolean flag=false;
		try{
			String id=courseDao.getNextNo();
			cource.setId(id);
			courseDao.addObject(cource);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean update(Course course){
		boolean flag=false;
		try{
			courseDao.updateObject(course);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(Course course){
		boolean flag = false;
		try {
			courseDao.removeObject(Course.class, course.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public Course getCourseById(String id){
		return (Course)courseDao.getObject(Course.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String id, String courseName,String category, String teacher,int pageNum, int pageSize) {
		return courseDao.getPage(id, courseName, category, teacher, pageNum, pageSize);
	}
	
}
