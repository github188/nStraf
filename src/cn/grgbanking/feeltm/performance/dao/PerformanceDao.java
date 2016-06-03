package cn.grgbanking.feeltm.performance.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Performance;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("performanceDao")
public class PerformanceDao extends BaseDao<Performance> {
	public Page getPage(String groupname,String username,String queryGroupname,int queryLevel,
			int pageNum, int pageSize,String month) {
		String hql ="";
		//hql = "select pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date from Performance where 1=1 ";
		hql = "select new cn.grgbanking.feeltm.performance.domain.Perfor(pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date) from Performance where 1=1 ";
		//hql = "select new cn.grgbanking.feeltm.performance.domain.Perfor(pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date) from (select t.pno,t.id,rownum,t.month_date,t.user_id,t.group_name,t.subtotal_s,t.remark,t.update_man,t.modify_date from (select pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date from Performance order by subtotal_s desc) t) where 1=1 ";
		if(groupname!=null && !groupname.equals("全选")){
			hql += " and group_name like '%"+groupname.trim()+"%'";
		}
		if (username != null && !username.equals("全选")){
			hql += " and user_id like '%"+username.trim()+"%'";
		}
		if (month != null && !month.equals("")){
			hql += " and month_date like '%"+ month.trim()+"%'";
		}
		if(queryLevel == 2)
		{
			hql += " and group_name like '%"+queryGroupname.trim()+"%' ";
		}
		hql += "order by month_date desc,subtotal_s desc,group_name desc,user_id asc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.pno) from Performance p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p!=null){
		    	  if(p.contains("T")){
			    	  p=p.substring(1);
			    	  long d=Long.parseLong(p)+1;
			    	  DecimalFormat format=new DecimalFormat("T0000");
			  		  str=format.format(d);
			      }
		      }
		      else
		      {
					str = "T0001";
		      }
		     
		}
		return str;		
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and level != 3  and lower(trim(username)) not in('汤飞','开发员')  order by level";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	

	
	 public boolean upEdit_lock(String month_date){   
	        try {   
	        	Session session = this.getHibernateTemplate().getSessionFactory().openSession();
	            Transaction tx = session.beginTransaction();   
	            String hql = "update Performance set edit_lock = 1 where month_date like '%"+month_date.trim()+"%'"; 
	            Query query = session.createQuery(hql);   
	            int ret=query.executeUpdate();
	            tx.commit();     
	             session.close();   
	        } catch (Exception e) {   
	            return false;   
	        }   
	        return true;   
	    }  

		public String getUserEffect_score(String username,String month_date){
			
			String hql="select sum(effect_score) from ProblemOrSuggestion where resolve_man like '%"+username.trim()+"%' and   to_char(pratical_date,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				str=(String)list.get(0);
			}
			return str;
		}
		
		
		
		public String getUserPrice_score(String username,String month_date){
			
			String hql="select sum(price_score) from ProblemOrSuggestion where raise_man like '%"+username.trim()+"%' and   to_char(raise_date,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				str=(String)list.get(0);		     
			}
			return str;
		}
		
		public String getUserEffect_num(String username,String month_date){
			
			String hql="select count(effect_score) from ProblemOrSuggestion where resolve_man like '%"+username.trim()+"%' and   to_char(pratical_date,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				str=list.get(0).toString();		     
			}
			return str;
		}
		
		public String getUserPrice_num(String username,String month_date){
			
			String hql="select count(price_score) from ProblemOrSuggestion where raise_man like '%"+username.trim()+"%' and   to_char(raise_date,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				str=list.get(0).toString();		     
			}
			return str;
		}
		
		public String getUser_priseValue(String username,String month_date){
			
			String hql="select sum(priseValue) from Behavior where username like '%"+username.trim()+"%' and   to_char(startDate,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				if(list.get(0)!= null)
				str=list.get(0).toString();     
			}
			return str;
		}
		
		public String getUser_punishValue(String username,String month_date){
			
			String hql="select sum(punishValue) from Behavior where username like '%"+username.trim()+"%' and   to_char(startDate,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				if(list.get(0)!= null)
					str=list.get(0).toString();  	     
			}
			return str;
		}
		
		public String getUserTrain_num(String username,String month_date){
			
			String hql="select count(teacher) from TrainingRecord where teacher like '%"+username.trim()+"%' and   to_char(train_date,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str=null;
			if(list!=null&&list.size()>=0){
				str=list.get(0).toString();		     
			}
			return str;
		}
		
		public String getUserMeetingUnAudit_num(String group_name,String month_date){
			
			String hql="select content,currentDateTime from Meeting where to_char(currentDateTime,'yyyy-MM') like '%"+month_date.trim()+"%'";
			System.out.println(hql);
			List list=this.getHibernateTemplate().find(hql);
			String str = null;
			String strtmp = null;
			
			StringBuffer sb1=new StringBuffer();
			int Unauditnum = 0;
			int Unpigeonum = 0;
			if(list!=null&&list.size()>=0){
				for(int i = 0;i<list.size();i++)
				{
					Unauditnum = 0;
					Unpigeonum = 0;
					Object[] obj = (Object[])list.get(i);
					str = obj[0].toString();
					if(group_name.equals("基础软件测试组"))
					{
						int strstart = str.indexOf("---基础软件测试组---");
						int strend = str.indexOf("---技术支持组---");
						strtmp = str.substring(strstart, strend);
						String arr[] = strtmp.split("未审核");
						String add[] = strtmp.split("未归档");
						Unauditnum  = arr.length - 1;
						Unpigeonum  = add.length - 1;
					}
					else if(group_name.equals("应用软件测试组"))
					{
						int strstart = str.indexOf("---应用软件测试组---");
						int strend = str.indexOf("---质量管理组---");
						strtmp = str.substring(strstart, strend);
						String arr[] = strtmp.split("未审核");
						String add[] = strtmp.split("未归档");
						Unauditnum  = arr.length - 1;
						Unpigeonum  = add.length - 1;
					}
					else if(group_name.equals("质量管理组"))
					{
						int strstart = str.indexOf("---质量管理组---");
						int strend = str.indexOf("主持人人员顺序");
						strtmp = str.substring(strstart, strend);
						String arr[] = strtmp.split("未审核");
						String add[] = strtmp.split("未归档");
						Unauditnum  = arr.length - 1;
						Unpigeonum  = add.length - 1;
					}
					else 
					{
						int strstart = str.indexOf("---技术支持组---");
						int strend = str.indexOf("---应用软件测试组---");
						strtmp = str.substring(strstart, strend);
						String arr[] = strtmp.split("未审核");
						String add[] = strtmp.split("未归档");
						Unauditnum  = arr.length - 1;
						Unpigeonum  = add.length - 1;
					}
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //显示日期，周，时间（精确到秒）
			    	String str5 = format.format((Date)obj[1]);
					if(Unauditnum > 0 && Unpigeonum > 0)
					{
						sb1.append("日期为").append(str5).append("的会议纪要中共有未审核").append(Unauditnum).append("次,未归档").append(Unpigeonum).append("次      ");
					}
					if(Unauditnum == 0 && Unpigeonum > 0)
					{
						sb1.append("日期为").append(str5).append("的会议纪要中共有未归档").append(Unpigeonum).append("次     ");
					}
					if((Unauditnum > 0 && Unpigeonum == 0))
					{
						sb1.append("日期为").append(str5).append("的会议纪要中共有未审核").append(Unauditnum).append("次      ");
					}
					
				}
					     
			}
			str = sb1.toString();
			return str;
		}
		
	public String getUserGroup_name(String username){
		
		String hql="select groupName from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=(String)list.get(0);		     
		}
		return str;
	}
	
	public String getUserlevel(String username){
		
		String hql="select level from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public List<DayReportStatic> dayStatic(final String currentMonth){
//		String hql="select new cn.grgbanking.feeltm.report.domain.DayReportStatic(t.startDate,to_char(t.startDate,'day'),count(distinct(t.startDate||t.username)),sum(t.subtotal)) from WorkReport t group by t.startDate having to_char(t.startDate,'yyyy-MM')=? order by t.startDate desc ";
		//String hql="select new cn.grgbanking.feeltm.report.domain.DayReportStatic(t.startDate,to_char(t.startDate,'day'),count(distinct(t.startDate||t.username)),sum(t.subtotal)) from WorkReport t group by t.startDate having to_char(t.startDate,'yyyy-MM')=? order by t.startDate desc ";
	//	List<DayReportStatic> ds=this.getHibernateTemplate().find(hql, currentMonth);
	//	List<DayReportStatic> ds=this.getHibernateTemplate().find(hql, currentMonth);
		List<DayReportStatic> list11=new ArrayList<DayReportStatic>();
		List<Object[]> sts=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer sb=new StringBuffer();
				sb.append("select t.start_date,to_char(t.start_date,'day'),count(distinct(t.start_date||t.user_id)),sum(t.subtotal) ");
				sb.append("from report_info t ");
				sb.append("group by t.start_date having to_char(t.start_date,'yyyy-MM')=? ");
				sb.append("order by t.start_date desc");
				System.out.println(sb.toString());
				List list=null;
				try{
					//list=session.createSQLQuery(sb.toString()).setString(0, currentMonth).list();
					list=session.createSQLQuery(sb.toString()).setString(0, currentMonth).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				return list;
			}
		});
		for(Object[] obj:sts){
			Date d=(Date)obj[0];
			String stTmp=(String)obj[1];
			int i=((BigDecimal)obj[2]).intValue();
			double dd=((BigDecimal)obj[3]).doubleValue();
			DayReportStatic drs=new DayReportStatic(d, stTmp, i, dd);
			list11.add(drs);
		}
		
		
		return list11;
	}
	
	
	public String getUnwritePersons(final Date specifidDate){
		String str="";
		 List<String> ss=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
				String date=fo.format(specifidDate);
				StringBuffer sb=new StringBuffer();
				sb.append("select WM_CONCAT(u.c_username) from sys_user u where (u.level1=1 or u.level1=2) ");
				sb.append("and u.c_username not in (select user_id from report_info where to_char(start_date,'yyyy-MM-dd')=?)");
				List  str=null;
				try{
				 str=session.createSQLQuery(sb.toString()).setString(0, date).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				 return str;
			}
		});
		if(ss!=null&&ss.size()>0){
			str=ss.get(0);
		}
		return str;
	}
	
	
	public String getUseredit_lock(String month_date){
		
		String hql="select edit_lock from Performance where month_date like '%"+month_date.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public boolean getUserMonth_date(String month_date){
		
		String hql="from Performance where month_date like '%"+month_date.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		boolean str=false;
		if(list!=null&&list.size()>0){
			str=true;		     
		}
		return str;
	}
	
	public List<Performance> selectPerformance(String pno){
		String query="from Performance report where report.pno=?";
		List<Performance> reports=(List<Performance>)this.getHibernateTemplate().find(query, new Object[]{pno});
//		List<WorkReport> reports=(List<WorkReport>)this.getObjectList(query, new Object[]{username,createDate});
		return reports;
	}
	
	public List<Object> getUsernamesByGroup(String groupName){
		String query="select user.username from SysUser user where 1=1 and lower(trim(user.username)) not in('汤飞','王全胜','杜高峰','开发员','管理员')";
		if(!groupName.equals("全选")){
			query+="and user.groupName like '%"+groupName+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
}
