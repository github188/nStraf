package cn.grgbanking.feeltm.projectquality.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.dispatcher.SessionMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.ProjectParams;
import cn.grgbanking.feeltm.domain.testsys.ProjectQuality;
import cn.grgbanking.feeltm.domain.testsys.StandardDic;
import cn.grgbanking.feeltm.prj.service.PointParamService;
import cn.grgbanking.feeltm.util.DbUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("projectQualityDao")
public class ProjectQualityDao extends BaseDao<ProjectQuality> {
	
	@Autowired
	private PointParamService pointParamService;
	
	public Page getPage(String querydate,String projectname,
			int pageNum, int pageSize) {
		String hql ="";
		hql = "from ProjectQuality where 1=1 ";
		//hql = "select new cn.grgbanking.feeltm.performance.domain.Perfor(pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date) from (select t.pno,t.id,rownum,t.month_date,t.user_id,t.group_name,t.subtotal_s,t.remark,t.update_man,t.modify_date from (select pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date from Performance order by subtotal_s desc) t) where 1=1 ";
		if(projectname!=null && !projectname.equals("全选")){
			hql += " and prjName like '%"+projectname.trim()+"%'";
		}
//		if (projecttype != null && !projecttype.equals("全选")){
//			hql += " and prjType like '%"+projecttype.trim()+"%'";
//		}
		if (querydate != null && !querydate.equals("")){
			hql += " and prjEnd >= to_date('"+ querydate.trim()+"','yyyy-MM-dd') and   prj_start <=  to_date('"+ querydate.trim()+"','yyyy-MM-dd') ";
		}
		hql += "order by prjQno desc,prjEnd desc,prjType desc,prjName desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.prjQno) from ProjectQuality p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p!=null){
		    	  if(p.contains("Q")){
			    	  p=p.substring(1);
			    	  long d=Long.parseLong(p)+1;
			    	  DecimalFormat format=new DecimalFormat("Q0000");
			  		  str=format.format(d);
			      }
		      }
		      else
		      {
					str = "Q0001";
		      }
		     
		}
		return str;		
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and lower(trim(username)) not in('汤飞','开发员')  order by level";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	public List<ProjectDB>  getDB(){
		//	ProjectDB prjDB=null;
			String hql = "FROM ProjectDB params WHERE 1=1 ";
			List<ProjectDB> list=this.getHibernateTemplate().find(hql);

//			prjDB=list.get(0);
			return list;
		}
	
	@Transactional(readOnly=true)
	public List<PrjPart> getDetails(String start, String end, SessionMap session) throws Exception {
		List<PrjPart> ds = new ArrayList<PrjPart>();
		Connection con = DbUtil.getConnection(session);
		if (start == null || start.trim().equals("")) {
			start = "1987-03-01";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-03-01";
		}

				// 针对金融设备综合平台开始
				if(((ProjectDB)session.get("globalDB")).getPrjName().equals("金融设备综合运营平台")){
					PrjPart tmp=new PrjPart("金融设备综合运营平台", start, end);
					staticCurrentMonthPlat( start, end, con, tmp);
	 				if(tmp.getSubmitVersionNumer()!=0)
	 				ds.add(tmp);
				}
				//针对金融设备综合平台结束

 			   	
 			   	List<String> prjs = getPrjects(session);
 			   	prjs.remove("全选");
 				for (String prj : prjs) {
	 	 			   PrjPart tmp=new PrjPart(prj, start, end);
		 	 			staticCurrentMonth( start, end, con, tmp);
		 	 				   if(tmp.getSubmitVersionNumer()!=0)
		 	 				   ds.add(tmp);
			   }

		DbUtil.closeConnection(con);
		return ds;
	}
	
	public List<String> getPrjects(SessionMap session) throws SQLException {
		List<String> ps = new ArrayList<String>();
		ps.add("全选");
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.getNameVersion(?,?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		cstmt.execute();
		String prjs = cstmt.getString(1);
		String[] nos = prjs.split("\\|");
		for (String no : nos) {
			ps.add(no);
		}
		DbUtil.closeConnection(con);
		return ps;
	}
	
	private void staticCurrentMonthPlat(String start, String end,
			Connection con, PrjPart tmp) throws SQLException {
		CallableStatement cstmt;
			int total=0;
		   int count=0;
		   int totalBugCount=0;
		   int totalBugValue=0;
		   int prjValidBugDensityCount=0;
		   double  prjTotalBugDensity=0;
		   cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
		   cstmt.setString(1, tmp.getPrjName());
		   cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		   cstmt.execute();
		   String vs = cstmt.getString(2);
		   String[] nos = vs.split("\\|");
		   for (String no : nos) {
				if(checkExistsPlat(con, tmp.getPrjName(), no, start, end)){
					int  versionQuality=versionStaticPlat(con,tmp,no,start,end);
					totalBugCount+=tmp.getTotalValidBugs();
					totalBugValue+=tmp.getTotalBugValue();
					count++;
					total+=versionQuality;
					//modified
					if(tmp.getPrjAvgBugbugDensity()!=0){  //说明该版本有代码行数的填入
						prjValidBugDensityCount++;
						prjTotalBugDensity+=tmp.getPrjAvgBugbugDensity();
					}
				}
			}
		   if(count==0){
				tmp.setSubmitVersionNumer(0);
				 return;
			}
			BigDecimal bd = new BigDecimal(total);
			BigDecimal bd1 = new BigDecimal(prjTotalBugDensity);
			
			
			BigDecimal re = bd.divide(new BigDecimal(count), 0,
					BigDecimal.ROUND_HALF_UP);
			if(prjValidBugDensityCount!=0){
				BigDecimal re1 = bd1.divide(new BigDecimal(prjValidBugDensityCount), 2,
						BigDecimal.ROUND_HALF_UP);
				double avgDesity=re1.doubleValue();
				tmp.setPrjAvgBugbugDensity(avgDesity);
			}
			int avgPoint=re.intValue();
			//modified
			tmp.setPrjQualityAvgPoint(avgPoint);
			tmp.setTotalValidBugs(totalBugCount);
			tmp.setTotalBugValue(totalBugValue);
			
			StandardDic dic=pointParamService.getDateDic("versionQuality", avgPoint);
			
			
			tmp.setProjectQuality(dic.getPoint());
			tmp.setProjectQualityStr(dic.getMin());
			tmp.setSubmitVersionNumer(count);
			
			cstmt = con.prepareCall("{call dbo.gePrjQuality(?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, start);
			cstmt.setString(3, end);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.execute();
			String  bugResolveRate = cstmt.getString(4);
			tmp.setBugResolveRate(bugResolveRate);
			
			cstmt = con.prepareCall("{call dbo.get_bug_close(?,?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, ""); 
			cstmt.setString(3, start);
			cstmt.setString(4, end);
			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
			cstmt.execute();
			int  bugClosedNum = cstmt.getInt(5);
			tmp.setBugClosedNum(bugClosedNum);
			
			cstmt = con.prepareCall("{call dbo.get_bugReopenRate(?,?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, start);
			cstmt.setString(3, end);
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();
			int bugReopenNum = cstmt.getInt(4);
			String  bugReopenRate = cstmt.getString(5);
			tmp.setBugReopenNum(bugReopenNum);
			tmp.setBugReopenRate(bugReopenRate);
	}
	
	private void staticCurrentMonth(String start, String end,
			Connection con, PrjPart tmp) throws SQLException {
		CallableStatement cstmt;
			int total=0;
		   int count=0;
		   int totalBugCount=0;
		   int totalBugValue=0;
		   int prjValidBugDensityCount=0;
		   double  prjTotalBugDensity=0;
		   cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
		   cstmt.setString(1, tmp.getPrjName());
		   cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		   cstmt.execute();
		   String vs = cstmt.getString(2);
		   String[] nos = vs.split("\\|");
		   for (String no : nos) {
				if(checkExists(con, tmp.getPrjName(), no, start, end)){
					int  versionQuality=versionStatic(con,tmp,no,start,end);
					totalBugCount+=tmp.getTotalValidBugs();
					totalBugValue+=tmp.getTotalBugValue();
					count++;
					total+=versionQuality;
					//modified
					if(tmp.getPrjAvgBugbugDensity()!=0){  //说明该版本有代码行数的填入
						prjValidBugDensityCount++;
						prjTotalBugDensity+=tmp.getPrjAvgBugbugDensity();
					}
				}
			}
		   if(count==0){
				tmp.setSubmitVersionNumer(0);
				 return;
			}
			BigDecimal bd = new BigDecimal(total);
			BigDecimal bd1 = new BigDecimal(prjTotalBugDensity);
			
			
			BigDecimal re = bd.divide(new BigDecimal(count), 0,
					BigDecimal.ROUND_HALF_UP);
			if(prjValidBugDensityCount!=0){
				BigDecimal re1 = bd1.divide(new BigDecimal(prjValidBugDensityCount), 2,
						BigDecimal.ROUND_HALF_UP);
				double avgDesity=re1.doubleValue();
				tmp.setPrjAvgBugbugDensity(avgDesity);
			}
			int avgPoint=re.intValue();
			//modified
			tmp.setPrjQualityAvgPoint(avgPoint);
			tmp.setTotalValidBugs(totalBugCount);
			tmp.setTotalBugValue(totalBugValue);
			
			StandardDic dic=pointParamService.getDateDic("versionQuality", avgPoint);
			
			
			tmp.setProjectQuality(dic.getPoint());
			tmp.setProjectQualityStr(dic.getMin());
			tmp.setSubmitVersionNumer(count);
			
			cstmt = con.prepareCall("{call dbo.gePrjQuality(?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, start);
			cstmt.setString(3, end);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.execute();
			String  bugResolveRate = cstmt.getString(4);
			tmp.setBugResolveRate(bugResolveRate);
			
			cstmt = con.prepareCall("{call dbo.get_bug_close(?,?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, ""); 
			cstmt.setString(3, start);
			cstmt.setString(4, end);
			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
			cstmt.execute();
			int  bugClosedNum = cstmt.getInt(5);
			tmp.setBugClosedNum(bugClosedNum);
			
			cstmt = con.prepareCall("{call dbo.get_bugReopenRate(?,?,?,?,?)}");
			cstmt.setString(1, tmp.getPrjName()); 
			cstmt.setString(2, start);
			cstmt.setString(3, end);
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();
			int bugReopenNum = cstmt.getInt(4);
			String  bugReopenRate = cstmt.getString(5);
			tmp.setBugReopenNum(bugReopenNum);
			tmp.setBugReopenRate(bugReopenRate);
	}
	
	private boolean checkExists(Connection con,String prjName, String versionNO, String start,
			String end){
		boolean flag=false;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//start+="-01";
		//end+="-30";
		
		try {
//			CallableStatement cstmt=con.prepareCall("{call dbo.checkExists(?,?,?,?,?)}");
//			cstmt.setString(1, prjName);
//			cstmt.setString(2, versionNO);
//			cstmt.setString(3, start);   //2011-01 -01 00:00:00
//			cstmt.setString(4, end);   //
//			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
//			cstmt.execute();
//			int count = cstmt.getInt(5);
			
			
			//修改处
			String sql="select count(*) from td.bug where (CONVERT(VARCHAR(7), bg_detection_date, 23) between ? and ?) and bg_project= ? and (bg_detection_version=? or bg_user_05=? or bg_user_02=?) ";
			ps=con.prepareStatement(sql);
			ps.setString(1, start);
			ps.setString(2, end);
			ps.setString(3, prjName);
			ps.setString(4,versionNO);
			ps.setString(5,versionNO);
			ps.setString(6,versionNO);
			rs=ps.executeQuery();
			rs.next();
			int count=rs.getInt(1);
			if(count>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	private boolean checkExistsPlat(Connection con,String prjName, String versionNO, String start,
			String end){
		boolean flag=false;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//start+="-01";
		//end+="-30";
		
		try {
//			CallableStatement cstmt=con.prepareCall("{call dbo.checkExists(?,?,?,?,?)}");
//			cstmt.setString(1, prjName);
//			cstmt.setString(2, versionNO);
//			cstmt.setString(3, start);   //2011-01 -01 00:00:00
//			cstmt.setString(4, end);   //
//			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
//			cstmt.execute();
//			int count = cstmt.getInt(5);
			
			
			//修改处
			String sql="select count(*) from td.bug where (CONVERT(VARCHAR(7), bg_detection_date, 23) between ? and ?)  and (bg_detection_version=? or bg_user_05=? or bg_user_02=?) ";
			ps=con.prepareStatement(sql);
			ps.setString(1, start);
			ps.setString(2, end);
			ps.setString(3,versionNO);
			ps.setString(4,versionNO);
			ps.setString(5,versionNO);
			rs=ps.executeQuery();
			rs.next();
			int count=rs.getInt(1);
			if(count>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	private int versionStatic(Connection con,PrjPart part,String no,String start,String end) throws SQLException{
		//PrjPart d = new PrjPart(part.getPrjName(), staticMonth);
		CallableStatement cstmt = con.prepareCall("{call dbo.get_version_quality(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		cstmt.setString(1, part.getPrjName());
		cstmt.setString(2, no);
		cstmt.setString(3, start);
		cstmt.setString(4, end);
		cstmt.registerOutParameter("suggestion",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("warn",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("general",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("seriou",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("detect",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("bugSerious", java.sql.Types.REAL);
		cstmt.registerOutParameter("detection_date", java.sql.Types.VARCHAR);
		
		cstmt.registerOutParameter("validBugCount", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugTotalValue", java.sql.Types.INTEGER);

		cstmt.execute();
		
		int validBugCount=cstmt.getInt("validBugCount");
		int bugTotalValue=cstmt.getInt("bugTotalValue");
		//modified
		part.setTotalValidBugs(validBugCount);
		part.setTotalBugValue(bugTotalValue);
		
		double bugSeriousTemp=cstmt.getDouble("bugSerious");
		
		ProjectParams params = getParamsByPrjVersion(part.getPrjName(),no);
		long codeline = params.getCodeLine();
		//Integer prjDefect = params.getPrjDefect();
		int bugDesityPoint=0;
		if (codeline != 0) {
			double bugDensity = (validBugCount * 1000.0) / codeline;
			part.setPrjAvgBugbugDensity(bugDensity);
			//缺陷密度得分的计算
			bugDesityPoint=pointParamService.getPoint("bugDensity",bugDensity );
		}else{
			part.setPrjAvgBugbugDensity(0);
			bugDesityPoint=3;   //没填代码行数，缺陷密码得分默认给其3分
		}
	
		
		//缺陷严重性得分的计算
		int bugSeriousPoint=pointParamService.getPoint("bugSerious", bugSeriousTemp);
		//版本质量得分=15*(缺陷严重性得分+缺陷密码得分)
		int versionQualityPoint=15*(bugSeriousPoint+bugDesityPoint);
		return versionQualityPoint;
	}
	
	private int versionStaticPlat(Connection con,PrjPart part,String no,String start,String end) throws SQLException{
		//PrjPart d = new PrjPart(part.getPrjName(), staticMonth);
		CallableStatement cstmt = con.prepareCall("{call dbo.get_version_quality(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		cstmt.setString(1, part.getPrjName());
		cstmt.setString(2, no);
		cstmt.setString(3, start);
		cstmt.setString(4, end);
		cstmt.registerOutParameter("suggestion",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("warn",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("general",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("seriou",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("detect",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("bugSerious", java.sql.Types.REAL);
		cstmt.registerOutParameter("detection_date", java.sql.Types.VARCHAR);
		
		cstmt.registerOutParameter("validBugCount", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugTotalValue", java.sql.Types.INTEGER);

		cstmt.execute();
		
		int validBugCount=cstmt.getInt("validBugCount");
		int bugTotalValue=cstmt.getInt("bugTotalValue");
		//modified
		part.setTotalValidBugs(validBugCount);
		part.setTotalBugValue(bugTotalValue);
		
		double bugSeriousTemp=cstmt.getDouble("bugSerious");
		
		ProjectParams params = getParamsByPrjVersionPlat(part.getPrjName(),no);
		long codeline = params.getCodeLine();
		//Integer prjDefect = params.getPrjDefect();
		int bugDesityPoint=0;
		if (codeline != 0) {
			double bugDensity = (validBugCount * 1000.0) / codeline;
			part.setPrjAvgBugbugDensity(bugDensity);
			//缺陷密度得分的计算
			bugDesityPoint=pointParamService.getPoint("bugDensity",bugDensity );
		}else{
			part.setPrjAvgBugbugDensity(0);
			bugDesityPoint=3;   //没填代码行数，缺陷密码得分默认给其3分
		}
	
		
		//缺陷严重性得分的计算
		int bugSeriousPoint=pointParamService.getPoint("bugSerious", bugSeriousTemp);
		//版本质量得分=15*(缺陷严重性得分+缺陷密码得分)
		int versionQualityPoint=15*(bugSeriousPoint+bugDesityPoint);
		return versionQualityPoint;
	}
	
	private ProjectParams getParamsByPrjVersion(String prjName, String versionNO) {
		ProjectParams parm = new ProjectParams();
		String hql = "FROM ProjectParams params WHERE 1=1 ";
		if (prjName != null && !prjName.equals("")) {
			hql += " and params.prjName = '" + prjName.trim() + "' ";
		}
		if (versionNO != null && !versionNO.equals("")) {
			hql += " and params.versionNO = '" + versionNO.trim() + "' ";
		}
		List<ProjectParams> params = this.getHibernateTemplate().find(hql);
		if (params.size() != 0) {
			parm = params.get(0);
		}
		return parm;
	}
	
	private ProjectParams getParamsByPrjVersionPlat(String prjName, String versionNO) {
		ProjectParams parm = new ProjectParams();
		String hql = "FROM ProjectParams params WHERE 1=1 ";
		if (versionNO != null && !versionNO.equals("")) {
			hql += " and params.versionNO = '" + versionNO.trim() + "' ";
		}
		List<ProjectParams> params = this.getHibernateTemplate().find(hql);
		if (params.size() != 0) {
			parm = params.get(0);
		}
		return parm;
	}
	
	 public boolean upEdit_lock(String month_date){   
	        try {   
	        	Session session = this.getHibernateTemplate().getSessionFactory().openSession();
	            Transaction tx = session.beginTransaction();   
	            String hql = "update ProjectQuality set edit_lock = 1 where month_date like '%"+month_date.trim()+"%'"; 
	            Query query = session.createQuery(hql);   
	            int ret=query.executeUpdate();
	            tx.commit();     
	             session.close();   
	        } catch (Exception e) {   
	            return false;   
	        }   
	        return true;   
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
	

	

	
	public List<ProjectQuality> selectProjectQuality(String pno){
		String query="from ProjectQuality report where report.pno=?";
		List<ProjectQuality> reports=(List<ProjectQuality>)this.getHibernateTemplate().find(query, new Object[]{pno});
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
	public String getUidByLevel(String groupName){
		String query="select user.userid from SysUser user where 1=1 and lower(trim(user.username)) not in('汤飞','王全胜','杜高峰','开发员','管理员')";
		query+="and user.groupName like '%"+groupName+"%'";
		query+="and user.level = 1";
		
		List list= this.getHibernateTemplate().find(query);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
		//return names;
	}
	
}
