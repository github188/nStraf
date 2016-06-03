package cn.grgbanking.feeltm.meeting.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Meeting;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.meeting.dao.MeetingDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("meetingService")
@Transactional
public class MeetingService extends BaseService{
	@Autowired
	private MeetingDao meetingDao;
	
	public boolean add(Meeting meeting){
		boolean flag=false;
		try{
			meetingDao.addObject(meeting);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return meetingDao.remove(ids);
	}
	
	public boolean delete(Meeting meeting){
		boolean flag = false;
		try {
			meetingDao.removeObject(Meeting.class, meeting.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(Meeting meeting){
		boolean flag=false;
		try{
			meetingDao.updateObject(meeting);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public Meeting getMeetingById(String id){
		return (Meeting)meetingDao.getObject(Meeting.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,String subject,String writer,String compere,String content,int pageNum, int pageSize) {
		return meetingDao.getPage(start, end, subject,writer,compere,content,pageNum, pageSize);
	}
	
	
	@Transactional(readOnly = true)
	public List<SysUser> getAllEmps(){
		return  meetingDao.getAllEmps();
	}
	
	@Transactional(readOnly = true)
	public SysUser getEmpByName(String name){
		return meetingDao.getEmpByName(name);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return meetingDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public Map<String,String> getAllNameAndEmail(){
		return meetingDao.getAllNameAndEmail();
	}
}
