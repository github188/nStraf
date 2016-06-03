package cn.grgbanking.feeltm.myadvice.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.myadvice.domain.AdviceAndResponse;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;
/*
 * Author:ljlian
 * 2014-12-5
 * 
 * */
@Repository("adviceAndResponseDao")
public class AdviceAndResponseDao extends BaseDao<AdviceAndResponse>{

	
	/**
	 * @param id  建议的id
	 * @return
	 */
	public AdviceAndResponse findAdviceById(String id) {
		try{
		String sql = " from AdviceAndResponse a where a.id='" + id + "'";
		return  (AdviceAndResponse) this.getHibernateTemplate().find(sql);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	/**更新意见的信息
	 * @param adviceid  建议的id
	 * @param content  修改内容
	 * @param status   意见的处理状态
	 * @param tel  		 联系方式
	 * @param ptime
	 * @param reply 
	 * @return  
	 */
	public boolean updateAll(String adviceid, String content, String status, String tel, String ptime, String reply,String email) {
		
		try{
			String sql=" update  OA_ADVICEANDRESPONSE  set  C_CONTENT='"+content+"'  , C_STATUS='"+ status+"', C_TEL='"+tel+"' ,D_PLANTIME=to_date('"+ptime+"', 'yyyy-mm-dd') , C_REPLY ='"+reply+"' , C_EMAIL='"+email+"'  where  C_id ='"+adviceid+"'";
			System.out.println(sql);
			getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**获取分页对象
	 * @param sql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPage(String hql, int pageNum, int pageSize) {
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
	}
	/**根据用户的姓名和oa账号查询意见
	 * ljlian2 2014-12-19
	 * 
	 * @param username
	 * @return
	 */
	
	public SysUser findUserByUsername(String username) {
       // String sql="select * from  SYS_USER  u where u.C_USERNAME  like '%"+username+"%'   or   u.C_USERID  like '%"+username+"%'";
        //select * from  SYS_USER  u where u.C_USERNAME  like '%ljlian2%'   or   u.C_USERID  like '%ljlian2%';
		//String sql = "from SysUser u where u.username  like '%" + username + "%'   or   u.userId  like '%"+username+"%'";
		String sql = "from SysUser u where u.username  like '%" + username+ "%' or   u.userid  like '%"+username+"%'" ;

		System.out.println(sql);
		List<SysUser> list = this.getHibernateTemplate().find(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	
	
}
