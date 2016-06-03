package cn.grgbanking.feeltm.prj.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.ProjectParams;
import cn.grgbanking.feeltm.domain.testsys.StandardDic;
import cn.grgbanking.feeltm.domain.testsys.VersionDetail;
import cn.grgbanking.feeltm.prj.domain.PrjDetail;
import cn.grgbanking.feeltm.prj.service.PointParamService;
import cn.grgbanking.feeltm.util.DbUtil;
import cn.grgbanking.framework.dao.BaseDao;

/**
 * private String prjName; private String versionNO; private String
 * requireChangeRate; private String casePassRate; private String bugSubtotal;
 * -- private String bugCleanRate; private String bugSeriousRate; -- private
 * String bugResolveRate; -- private String bugDensity; private String
 * prjFeedbackRate;
 * 
 */
/**
 * cstmt = con.prepareCall("{call dbo.get_bug_count(?,?,?,?,?)}");
 * cstmt.setString(1, "1-3.0 PR4核心版"); cstmt.setString(2, "");
 * cstmt.setString(3, "1987-3-20 0:0:0"); cstmt.setString(4, "2011-3-31 0:0:0");
 * cstmt.registerOutParameter(5,java.sql.Types.VARCHAR); cstmt.execute();
 * System.out.println("MANAGER ID: " + cstmt.getString(5));
 * 
 */

@Repository("detailDao")
public class PrjDetailDao extends BaseDao<ProjectParams> {
	
	@Autowired
	private PointParamService pointParamService;
	
	@Autowired
	private PrjPartDao  prjPartDao;
	
	@Autowired
	private VersionDetailDao versionDetailDao;

	@Transactional(readOnly=true)
	public List<PrjPart> getDetails(String prjName, String versionNO,
			String start, String end, SessionMap session) throws Exception {
		List<PrjPart> ds = new ArrayList<PrjPart>();
		if (prjName == null || prjName.trim().equals("")) {
			return ds;
		}
		Connection con = DbUtil.getConnection(session);
		if (start == null || start.trim().equals("")) {
			start = "1987-03";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-03";
		}
		/*取得指定范围内的所有月份*/
//		List<String> allMonth=StringUtil.getAllMonth(start, end);
		
//		Date dat=new Date();
//		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
//		String staticMonth=format.format(dat);

		if (versionNO == null || versionNO.trim().equals("")) {
			if (prjName.equals("全选")) {           //即选择某库下的所有项目以及所有版本
				prjName = "";
				// 针对金融设备综合平台开始
				if(((ProjectDB)session.get("globalDB")).getPrjName().equals("金融设备综合运营平台")){
					PrjPart tmp=new PrjPart("金融设备综合运营平台", start, end);
					staticCurrentMonthPlat( start, end, con, tmp);
	 				if(tmp.getSubmitVersionNumer()!=0)
	 				ds.add(tmp);
					DbUtil.closeConnection(con);
					return ds;
				}
				//针对金融设备综合平台结束
				List<PrjPart> tmps=prjPartDao.getAllList(((ProjectDB)session.get("globalDB")).getPrjName(),start,end);//取得已提交到oracle中质量数据
 			   	if(tmps!=null&&tmps.size()>0){
 			   		ds.addAll(tmps);     //添加到列表中
 			   	}
 			   	
 			   	List<String> prjs = getPrjects(session);
 			   	prjs.remove("全选");
 				for (String prj : prjs) {
 				    //for(String month: allMonth){
	 	 			   PrjPart tmp=new PrjPart(prj, start, end);
	 	 			   if(!tmps.contains(tmp)){
		 	 				   staticCurrentMonth( start, end, con, tmp);
		 	 				   if(tmp.getSubmitVersionNumer()!=0)
		 	 				   ds.add(tmp);
	 	 			   }
 				   // }
			   }
 		   } else {  //取得某个项目下指定月份的质量数据
 			   List<PrjPart> tmps=prjPartDao.getList(prjName, start, end);//取得已提交到oracle中质量数据
 			   if(tmps!=null&&tmps.size()>0){
 				ds.addAll(tmps);     //添加到列表中
 			   }
 			 //  for(String month: allMonth){
	 			   PrjPart tmp=new PrjPart(prjName, start,end);
	 			   if(!tmps.contains(tmp)){    //如果在oracle中没取到当前月份的数据，则去qc库中取
	 				   staticCurrentMonth( start, end, con, tmp);
	 				   if(tmp.getSubmitVersionNumer()!=0)
	 				   ds.add(tmp);
 				
	 			   }
 			   //}
			}
		}
		DbUtil.closeConnection(con);
		return ds;
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
	}
	
	@Transactional
	public boolean deletePart(String prjName,String start,String end){
		
		//this.getHibernateTemplate().delete(new PrjPart(prjName,staticMonth));
		String hql1="delete from PrjPart part where part.prjName=? and part.start=? and part.staticMonth=?";
		String hql="delete from VersionDetail detail where detail.prjName=? and detail.start=? and detail.staticMonth=?";
		this.getHibernateTemplate().bulkUpdate(hql1, new Object[]{prjName,start,end});
		this.getHibernateTemplate().bulkUpdate(hql, new Object[]{prjName,start,end});
		return true;
	}
	
	@Transactional
	public boolean updatePart(PrjPart part,SessionMap session){
		boolean flag=true;
		boolean addOrUpdate=true;
		if(part.getId()!=null&&(!"".equals(part.getId()))){
			addOrUpdate=false;
		}
		
		String rate=part.getBugResolveRate();
		rate=rate.substring(0, rate.indexOf("%"));
		double bugResloveRate=Double.parseDouble(rate)/100;
		int ratePoint=pointParamService.getPoint("bugResloveRate", bugResloveRate);  // 从数据库中取得分
		
		int processQuality=part.getProcessQuality();  //
		int progress=part.getProgress();//
		
		int unFitNum=part.getUnfitNumber();
		int unFitPoint=pointParamService.getPoint("unFitNum", unFitNum);// 从数据库中取得分
		
		int projectQualityPoint=part.getProjectQuality();
			
		int QualityTotalPoint=(projectQualityPoint*40+processQuality*30+progress*25+ratePoint*5)/5-unFitPoint;
		String totalPointLevel=pointParamService.getTotalPointLevel("QualityTotalPoint", QualityTotalPoint)	;	
		part.setQualityPoint(QualityTotalPoint);
		part.setTotalEvaluate(totalPointLevel);
		part.setProjectQuality(projectQualityPoint);
		
		if(addOrUpdate){   //增加到数据库
			prjPartDao.addObject(part);
			 Connection con=DbUtil.getConnection(session);			 
			 try {
				List<VersionDetail> ds=versionStatic(con, part.getPrjName(),part.getStart(), part.getStaticMonth());
				versionDetailDao.addAll(ds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			 DbUtil.closeConnection(con);
			
		}else{  //更新
			prjPartDao.updateObject(part);
		}
		
		return flag;
	}
	
	private List<VersionDetail> versionStatic(Connection con,String prj,String start,String end) throws SQLException{
		List<VersionDetail> ds=new ArrayList<VersionDetail>();
			CallableStatement cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
		   cstmt.setString(1, prj);
		   cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		   cstmt.execute();
		   String vs = cstmt.getString(2);
		   String[] nos = vs.split("\\|");
		   for (String no : nos) {
				if(checkExists(con, prj, no,start , end)){
					VersionDetail d=new VersionDetail(prj, no,start, end);
					versionStatic(con,d,start,end);
					ds.add(d);
				}
			}
		   return ds;
	}
	
	private void versionStatic(Connection con, VersionDetail d,String start,String end) throws SQLException {
		CallableStatement cstmt = con.prepareCall("{call dbo.get_version_quality(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		cstmt.setString(1, d.getPrjName());
		cstmt.setString(2, d.getVersionNO());
		cstmt.setString(3, start);
		cstmt.setString(4, end);
		cstmt.registerOutParameter("suggestion",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("warn",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("general",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("seriou",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("detect",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("validBugCount", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugTotalValue", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugSerious", java.sql.Types.REAL);
		cstmt.registerOutParameter("detection_date", java.sql.Types.VARCHAR);
		
		cstmt.execute();
		
		int validBugCount=cstmt.getInt("validBugCount");
		int bugTotalValue=cstmt.getInt("bugTotalValue");
		double bugSeriousTemp=cstmt.getDouble("bugSerious");
		String detectionDate=cstmt.getString("detection_date");
		
		
		ProjectParams params = getParamsByPrjVersion(d.getPrjName(),d.getVersionNO());
		long codeline = params.getCodeLine();
		int bugDesityPoint=0;
		if (codeline != 0) {
			double bugDensity = (validBugCount * 1000.0) / codeline;
			//缺陷密度得分的计算
			bugDesityPoint=pointParamService.getPoint("bugDensity",bugDensity );
			d.setBugDensity(get2dec(bugDensity));
		}else{
			bugDesityPoint=3;   //没填代码行数，缺陷密码得分默认给其3分
			d.setBugDensity("--");
		}
		
		
		//缺陷严重性得分的计算
		int bugSeriousPoint=pointParamService.getPoint("bugSerious", bugSeriousTemp);
		//版本质量得分=15*(缺陷严重性得分+缺陷密码得分)
		int versionQualityPoint=15*(bugSeriousPoint+bugDesityPoint);
		//计算版本质量得分对应的数据库的范围得分，以及优，良级别
		StandardDic dic=pointParamService.getDateDic("versionQuality", versionQualityPoint);
		
		d.setQualityPoint(versionQualityPoint);  
		d.setVersionQuality(dic.getMin());
		d.setValidBugCount(validBugCount);
		d.setBugSerious(get2dec(bugSeriousTemp*100)+"%");
		d.setBugTotalValue(bugTotalValue);
		d.setDetectionDate(detectionDate);

	}


	@Transactional(readOnly=true)
	public List<VersionDetail> getDetail(String prjName, String start,String end,SessionMap session) throws Exception {
		 List<VersionDetail> ds=new ArrayList<VersionDetail>();
		 ds=versionDetailDao.getList(prjName, end);
		 
//		 Date dat=new Date();
//		 SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
//		 String staticMonth11=format.format(dat);
		 if(!(ds!=null&&ds.size()>0)){  //取QC中的数据
			// if(staticMonth.equals(staticMonth11)){
				 Connection con=DbUtil.getConnection(session);			 
				 ds=versionStatic(con, prjName, start,end);
				 DbUtil.closeConnection(con);
			// }
		 }
		 return ds;
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
	
	private String getPecent(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 4,
				BigDecimal.ROUND_HALF_UP).movePointRight(2);
		return re + "%";
	}
	
	private String get2dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 2,
				BigDecimal.ROUND_HALF_UP);
		return re + "";
	}
	
	//获取相应qc库的项目名称
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

	public List<String> getVersions(String prjName, SessionMap session)
			throws SQLException {
		List<String> ps = new ArrayList<String>();
		ps.add("全选");
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
		cstmt.setString(1, prjName);
		cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		cstmt.execute();
		String prjs = cstmt.getString(2);
		if (prjs != null) {
			String[] nos = prjs.split("\\|");
			for (String no : nos) {
				ps.add(no);
			}
		}
		DbUtil.closeConnection(con);
		return ps;
	}
	
	
	/**
	 * alter PROCEDURE get_whole_count 
   @startDate datetime = '1970-1-1 0:0:0',
   @endDate datetime = '2050-1-1 0:0:0',
   @szCount varchar(70) output
	 * @param start
	 * @param end
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	// 缺陷总数 缺陷严重性 缺陷解决率 缺陷总值 建议:1,警告:52,一般:32,严重:22,拒绝:9,总数:107,关闭:29
	public PrjDetail getDetailSummary(String prjName,SessionMap session) throws Exception{
		PrjDetail detail=(PrjDetail)session.get("prjDetail");
		if(detail!=null){
			Connection con = DbUtil.getConnection(session);
			CallableStatement cstmt = null;
			cstmt = con.prepareCall("{call dbo.gePrjQuality(?,?)}");
			cstmt.setString(1, prjName);     
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			cstmt.execute();
			String  bugResolveRate = cstmt.getString(2);
			detail.setBugResolveRate(bugResolveRate);
			DbUtil.closeConnection(con);
		}else{
			detail=new PrjDetail();
		}
		return detail;
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
	//获得当前版本的质量得分
	/**
	 * int[0]  版本质量
	 * 
	 */
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

	private PrjDetail detailStatic(Connection con,String prj,String no) throws SQLException{
		PrjDetail d = new PrjDetail(prj,no);
		CallableStatement cstmt = con.prepareCall("{call dbo.get_version_quality(?,?,?,?,?,?,?,?,?,?)}");
		cstmt.setString(1, prj);
		cstmt.setString(2, no);
		cstmt.registerOutParameter("suggestion",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("warn",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("general",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("seriou",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("detect",java.sql.Types.INTEGER );
		cstmt.registerOutParameter("validBugCount", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugTotalValue", java.sql.Types.INTEGER);
		cstmt.registerOutParameter("bugSerious", java.sql.Types.REAL);
		cstmt.execute();
		
		d.setBugSubtotal(cstmt.getInt("validBugCount")+"");
		d.setBugFatal(cstmt.getInt("detect"));
		d.setBugSerious(cstmt.getInt("seriou"));
		d.setBugGeneral(cstmt.getInt("general"));
		d.setBugWarn(cstmt.getInt("warn"));
		d.setBugSuguest(cstmt.getInt("suggestion"));
//		ProjectParams params=getParamsByPrjVersion(prj,no);
//		d.setNote(params.getNote());
//		d.setCodeLine(params.getCodeLine());
		
		return d;
	}
}
