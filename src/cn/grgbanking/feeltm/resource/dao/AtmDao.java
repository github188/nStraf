package cn.grgbanking.feeltm.resource.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Atm;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("atmDao")
public class AtmDao extends BaseDao<Atm>{
	/**
	 * 
	 * @param type  机器型号
	 * @param seriaNum  设备编号
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPage(String type,String seriaNum,int pageNum,int pageSize)
	{
		String hql = "FROM Atm atm WHERE 1=1 ";
			if(type!=null && !type.equals("")){
				hql += " and atm.machineNo like '%"+type.trim()+"%' ";
			}
			if(seriaNum!=null && !seriaNum.equals("")){
				hql += " and atm.deviceNo like '%"+seriaNum.trim()+"%' ";
			}
		hql += " order by atm.machineNo ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean remove(String[] ids){  //通过pl/sql关联进行删除其他内容
		boolean flag=false;
		List<Atm> atms=new ArrayList<Atm>();
		for (int j = 0; j < ids.length; j++) {	
				atms.add(new Atm(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(atms);
			flag=true;
		}catch(Exception e){
			System.out.println("ServerDao delete error!!!!!!!!!!");
		}
		return flag;
	}

	public boolean check(String deviceNo) {   //true为可用，false为不可用
		boolean flag=true;
		String hql = "FROM Atm atm WHERE atm.deviceNo='"+deviceNo+"'";		
		List list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0)
			flag=false;
		return flag;
	}
}
