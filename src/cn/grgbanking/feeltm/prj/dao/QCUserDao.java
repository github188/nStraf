package cn.grgbanking.feeltm.prj.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.QCUser;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("qcUserDao")
public class QCUserDao extends BaseDao<QCUser>{
	public QCUser getUser(String en_name){
		QCUser usr=null;
		if(en_name!=null&&!en_name.equals("")){
			String hql="from QCUser usr where usr.en_name='"+en_name+"'";
			List<QCUser> usrs=this.getHibernateTemplate().find(hql);
			if(usrs!=null&&usrs.size()>=1){
				usr=usrs.get(0);
			}
		}
		return usr;
	}
	
}
