package cn.grgbanking.feeltm.meeting.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Meeting;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("meetingDao")
public class MeetingDao extends BaseDao<Meeting>{
	public boolean remove(String[] ids){
		boolean flag=false;
		List<Meeting> bs=new ArrayList<Meeting>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new Meeting(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("MeetinglDao delete error!!!!!!!!!!");
		}
		return flag;
	}

	public Page getPage(String start,String end,String subject,String writer,String compere,String content, int pageNum,int pageSize)
	{
		String hql = " from Meeting where 1=1 ";
			if(start!=null&&!start.equals("") ){
				hql += " and to_char(currentDateTime,'YYYY-MM-DD') >= '"+start.trim()+"'"; 
			}
			if(end!=null&&!end.equals("") ){
				hql += " and to_char(currentDateTime,'YYYY-MM-DD') <= '"+end.trim()+"'"; 
			}
			if(subject!=null && !subject.equals("")){
				hql += " and subject like '%"+subject.trim()+"%' ";
			}
			if(compere!=null && !compere.equals("")){
				hql += " and compere like '%"+compere.trim()+"%' ";
			}
			if(writer!=null && !writer.equals("")){
				hql += " and writer like '%"+writer.trim()+"%' ";
			}
			if(content!=null && !content.equals("")){
				hql += " and content like '%"+content.trim()+"%' ";
			}
			
		hql += " order by currentDateTime desc,auditStatus asc ";
		System.out.println(hql);
		List list=getHibernateTemplate().find(hql);
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list, pageNum, pageSize);
	}
	
	public List<SysUser> getAllEmps(){
		List<SysUser> list=null;
		String hql="from SysUser where groupName is not null";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and lower(trim(username)) not in('汤飞','王全胜','开发员','杜高峰') order by groupName desc,level asc";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	
	public SysUser getEmpByName(String name){
		SysUser user=null;
		String hql="from SysUser where username =?";
		List<SysUser> users=this.getHibernateTemplate().find(hql, name);
		if(users!=null&&users.size()>0){
			user=users.get(0);
		}
		return user;
	}
	
	/**
	 * 
	 * @return  map 的key 为用户名，value 为email
	 */
	public Map<String,String> getAllNameAndEmail(){
		Map<String,String> map=new HashMap<String,String>();
		String hql="from SysUser where groupName is not null";
		List<SysUser> list=this.getHibernateTemplate().find(hql);
		for(SysUser user:list){
			map.put(user.getUsername(), user.getEmail());
		}
		return map;
	}
	
}
