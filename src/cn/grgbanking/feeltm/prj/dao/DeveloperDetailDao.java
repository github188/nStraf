package cn.grgbanking.feeltm.prj.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.QCUser;
import cn.grgbanking.feeltm.prj.domain.DeveloperDetail;
import cn.grgbanking.feeltm.prj.service.PointParamService;
import cn.grgbanking.feeltm.util.DbUtil;
import cn.grgbanking.framework.service.BaseService;

/**
 * 打开:15|重新打开:0|修复:1|已否决:2|subTotal:33|
 * 
 * @author feel alter PROCEDURE get_developer
 * @szName varchar(20) = '',
 * @szPrjName varchar(64) = '',
 * @szVersionNO varchar(20) = '',
 * @startDate datetime = '1970-1-1 0:0:0',
 * @endDate datetime = '2050-1-1 0:0:0',
 * @szCount varchar(1024) output
 */
@Service("devDao")
@Transactional
public class DeveloperDetailDao extends  BaseService {
	@Autowired
	private QCUserDao qcUserDao;
	
	
	@Autowired
	private PointParamService pointParamService;
	
	@Transactional(readOnly = true)
	public List<DeveloperDetail> getDetails(String start, String end,
			String name, SessionMap session) throws Exception {
		List<DeveloperDetail> ds = new ArrayList<DeveloperDetail>();
		if (name == null || name.trim().equals("")) {
			return ds;
		}
		if (start == null || start.trim().equals("")) {
			start = "1987-3-20 0:0:0";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-3-31 0:0:0";
		}
		Connection con = DbUtil.getConnection(session);		
		if (name.equals("all")) {   //选择全选操作，显示所有开发人员的质量情况
			List<String> names=getNames(session);
			for(String tmp:names){
				if (checkExist(start, end, tmp, con)) {
					DeveloperDetail d = new DeveloperDetail();
					d.setDevName_en(tmp);
					//此处应取数据库中对应的qc中文名，以及部门
					QCUser usr=qcUserDao.getUser(tmp);
					if(usr!=null){
						d.setDevName(usr.getCh_name());
						d.setDept(usr.getDept());
					}else{
						d.setDevName(tmp);
					}
					String str=executeDevStatic(con,tmp,start,end);
					String strvalue=executeDevStaticvalue(con,tmp,start,end);
					parseTo(str, strvalue,d, false, con, start, end);
					ds.add(d);
			    }
		    }
			Collections.sort((List)ds);
		}else{    //选择一个人的质量情况
			if (checkExist(start, end, name, con)) {
				DeveloperDetail d = new DeveloperDetail();
				d.setDevName_en(name);
				QCUser usr=qcUserDao.getUser(name);
				if(usr!=null){
					d.setDevName(usr.getCh_name());
					d.setDept(usr.getDept());
				}else{
					d.setDevName(name);
				}
				String str=executeDevStatic(con,name,start,end);
				String strvalue=executeDevStaticvalue(con,name,start,end);
				parseTo(str, strvalue,d, false, con, start, end);
				ds.add(d);
			}
		}
		return ds;
	}
	
	
	public QCUser getUser(String en_name){
		return qcUserDao.getUser(en_name);
	}
	
	/**
	 * 执行pl/sql语句，用来统计开发人员的质量数据
	 * @return
	 * @throws SQLException 
	 */
	private String executeDevStatic(Connection con,String name,String start,String end) throws SQLException{
		CallableStatement cstmt = con.prepareCall("{call dbo.get_developer(?,?,?,?)}");
		cstmt.setString(1, name);
		cstmt.setString(2, start);
		cstmt.setString(3, end);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmt.execute();
		String str = cstmt.getString(4); 
		System.out.println("developer static :"+name);
		return str;
	}

	private String executeDevStaticvalue(Connection con,String name,String start,String end) throws SQLException{
		CallableStatement cstmt = con.prepareCall("{call dbo.get_developervalue(?,?,?,?)}");
		cstmt.setString(1, name);
		cstmt.setString(2, start);
		cstmt.setString(3, end);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmt.execute();
		String str = cstmt.getString(4); 
		System.out.println("developer static :"+name);
		return str;
	}
	
	public List<String> getNames(SessionMap session) throws Exception {
		List<String> strs = new ArrayList<String>();
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.getDevName(?)}");
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.execute();
		String str = cstmt.getString(1);
		String[] ss = str.split("\\|");
		for (String s : ss) {
			strs.add(s);
		}
		DbUtil.closeConnection(con);
		return strs;
	}

	public DeveloperDetail getDetail(
			String start, String end, String name, SessionMap session)
			throws Exception {
		if (start == null || start.trim().equals("")) {
			start = "1987-3-20 0:0:0";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-3-31 0:0:0";
		}
		DeveloperDetail detail = new DeveloperDetail();
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		CallableStatement cstmtdevvalue = null;
		cstmt = con.prepareCall("{call dbo.get_developer(?,?,?,?)}");
		
		cstmt.setString(1, name);
		cstmt.setString(2, start);
		cstmt.setString(3, end);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmt.execute();
		
		String str = cstmt.getString(4);
		cstmtdevvalue = con.prepareCall("{call dbo.get_developervalue(?,?,?,?)}");
		cstmtdevvalue.setString(1, name);
		cstmtdevvalue.setString(2, start);
		cstmtdevvalue.setString(3, end);
		cstmtdevvalue.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmtdevvalue.execute();
		
		String strdevvalue = cstmtdevvalue.getString(4);
		parseTo(str, strdevvalue, detail, true, con, start, end);
		DbUtil.closeConnection(con);
		return detail;
	}

	private String getPecent(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 4,
				BigDecimal.ROUND_HALF_UP).movePointRight(2);
		return re + "%";
	}

	// 打开:15|重新打开:0|修复:1|已否决:2|subTotal:33|
	/**
	 * private String bugReopenRate; private String avrFixTime; private String
	 * FixRate; // private String bugUnresolve; //open+reopen '未解决缺陷数 OpenBugNum
	 * + RepenBugNum
	 * 
	 * '缺陷ReOpen率 = Reopen缺陷数/个人缺陷总数（含Rejected缺陷）
	 * 
	 * '缺陷修复率 = (FIXED+REJECTED缺陷)/总缺陷数（含REJECTED缺陷）
	 * 
	 * @throws Exception
	 */
	private void parseTo(String str, String strdevvalue, DeveloperDetail detail, boolean isSingle,
			Connection con, String start, String end) throws Exception {

		String[] levers = str.split("\\|");
		String[] leversvalue = strdevvalue.split("\\|");
		int open = 0;
		int reopen = 0;
		int fix = 0;
		int reject = 0;
		int subtotal = 0;
		int closed = 0;
		String avrFixTime = "";
		String totalValue="";
		String totalValueValue="";
		for (String lever : levers) {
			if (lever.startsWith("打开")) {
				open = Integer.parseInt((lever.substring(3)));
			} else if (lever.startsWith("重新打开")) {
				reopen = Integer.parseInt((lever.substring(5)));
			} else if (lever.startsWith("修复")) {
				fix = Integer.parseInt((lever.substring(3)));
			} else if (lever.startsWith("已否决")) {
				reject = Integer.parseInt((lever.substring(4)));
			} else if (lever.startsWith("subTotal")) {
				subtotal = Integer.parseInt((lever.substring(9)));
			} else if (lever.startsWith("已关闭")) {
				closed = Integer.parseInt((lever.substring(4)));
			} else if (lever.startsWith("avgFixTime")) {
				avrFixTime = lever.substring(11);
			}else if (lever.startsWith("totalValue")) {
				totalValue = lever.substring(11);
			}
		}
		for(String lever : leversvalue) {
			if (lever.startsWith("totalValue")) {
				totalValueValue = lever.substring(11);
			}
		}
		if (!isSingle) {

			double bugReopenRate = (subtotal == 0) ? 0
					: ((reopen * 1.0) / subtotal);

			double bugFixRate = (subtotal == 0) ? 0
					: ((fix + reject + closed) * 1.0 / (subtotal + reject));
			detail.setBugSubtotal(subtotal + "");
			// insert
			detail.setFixRate(getPecent(bugFixRate));
			detail.setBugReopenRate(getPecent(bugReopenRate));
			// add 平均缺陷修复时间

			detail.setBugTotalValue(totalValue);
			
			double avgFixTimeDb=Double.parseDouble(avrFixTime)/24.0;
			if(avgFixTimeDb==0){
				detail. setAvrFixTime("--");
			}else{
				detail. setAvrFixTime(get2dec(avgFixTimeDb,1));
			}
			double totalValueDb=Double.parseDouble(totalValueValue);
			int totalPoint=getTotalPoint(avgFixTimeDb, bugFixRate, totalValueDb, bugReopenRate);//平均缺陷修复时间要转化为天
			String evalute=pointParamService.getLevel("devQuality", totalPoint);
			detail.setTotalPoint(totalPoint+"");
			detail.setTotalPointInt(totalPoint);
			detail.setQualityEvalute(evalute);
		} else {
			detail.setBugOpen(open + "");
			detail.setBugReopen(reopen + "");
			detail.setBugFix(fix + "");
			detail.setBugReject(reject + "");
			int unresolve = open + reopen;
			detail.setBugSubtotal(subtotal + "");
			detail.setBugUnresolve(unresolve + "");
		}
	}
	/**
	 *-- 	开发人员质量 = ( 缺陷修复率得分 * 40 + 缺陷Reopen率得分 * 40 + 缺陷平均修复时间得分 * 20 ) / 5+缺陷总值得分；
	 * 开发人员质量 = ( 缺陷修复率得分 * 40 + 缺陷平均修复时间得分 * 20 ) / 5 + 缺陷总值得分 –  (缺陷Reopen率得分 * 5)；
	 * @param avgFixTime
	 * @param bugFixRate
	 * @param bugTotalValue
	 * @param reopenRate
	 * @return
	 */
	private int getTotalPoint(double avgFixTime,double bugFixRate,double bugTotalValue,double reopenRate){
		int totalPoint=0;
		int fixTimePoint=pointParamService.getPoint("avgFixTime", avgFixTime);
		int fixRatePoint=pointParamService.getPoint("bugFixRate", bugFixRate);
		int totalValuePoint=pointParamService.getPoint("bugTotalValue", bugTotalValue);
		int reopenPoint=pointParamService.getPoint("reopenRate", reopenRate);
		//totalPoint=fixRatePoint*8+reopenPoint*8+fixTimePoint*4+totalValuePoint;
		totalPoint=fixRatePoint*8+fixTimePoint*4+totalValuePoint-(reopenPoint*5);
		return totalPoint;
	}
	
	private String get2dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 2,
				BigDecimal.ROUND_HALF_UP);
		return re + "";
	}
	
	private String get2dec(double a,int scale) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), scale,
				BigDecimal.ROUND_HALF_UP);
		return re + "";
	}

	public List<String> getPrjects(SessionMap session) throws SQLException {
		List<String> ps = new ArrayList<String>();
		// ps.add("全选");
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
	 * 
	 * select count(bg_bug_id) from td.bug where bg_project=? and
	 * bg_detection_version=? and bg_responsible=? and (bg_detection_date >=?
	 * and bg_detection_date <=? )
	 */
	private boolean checkExist(String start, String end, String name,
			Connection con) {
		boolean flag = false;
		String sql = "select count(bg_bug_id) from td.bug where bg_status not in( '已否决','挂起','删除','新建') and  bg_responsible=? and (bg_vts >=?  and bg_vts <=? )";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, start);
			ps.setString(3, end);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long count = rs.getLong(1);
			if (count != 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

}
