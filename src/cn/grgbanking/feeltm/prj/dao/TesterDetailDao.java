package cn.grgbanking.feeltm.prj.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.prj.domain.TesterDetail;
import cn.grgbanking.feeltm.util.DbUtil;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("testerDao")
public class TesterDetailDao extends BaseDao<SysUser>{
	public List<TesterDetail> getDetails(String prjName, String versionNO,
			String start, String end, String name, SessionMap session)
			throws Exception {
		List<TesterDetail> ds = new ArrayList<TesterDetail>();
		if (prjName == null || prjName.trim().equals("")) {
			return ds;
		}
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		if (start == null || start.trim().equals("")) {
			start = "1987-3-20 0:0:0";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-3-31 0:0:0";
		}
		
		if (versionNO == null || versionNO.trim().equals("")) {
			
			if (prjName.equals("全选")) {
				prjName = "";
				List<String> prjs = getPrjects(session);
				
				for (String prj : prjs) {
					cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
					cstmt.setString(1, prj);
					cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
					cstmt.execute();
					String vs = cstmt.getString(2);
					String[] nos = vs.split("\\|");
					for (String no : nos) {
						if(checkExist(prj, no, start, end, name, con)){
							TesterDetail d = new TesterDetail();
							d.setPrjName(prj);
							d.setVersionNO(no);
							d.setTesterName(name);
							cstmt = con
									.prepareCall("{call dbo.get_tester(?,?,?,?,?,?)}");
							cstmt.setString(1, name);
							cstmt.setString(2, prj);
							cstmt.setString(3, no.replace("fqst", ""));
							cstmt.setString(4, start);
							cstmt.setString(5, end);
							cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
							cstmt.execute();
							String str = cstmt.getString(6);
							parseTo(str, d, false);
							ds.add(d);
						}
					}
				}
				
				
			} else {
				cstmt = con.prepareCall("{call dbo.getPrjVersion(?,?)}");
				cstmt.setString(1, prjName);
				cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
				cstmt.execute();
				String vs = cstmt.getString(2);
				String[] nos = vs.split("\\|");
				for (String no : nos) {
					if(checkExist(prjName, no, start, end, name, con)){
						TesterDetail d = new TesterDetail();
						d.setPrjName(prjName);
						d.setVersionNO(no);
						d.setTesterName(name);
						cstmt = con
								.prepareCall("{call dbo.get_tester(?,?,?,?,?,?)}");
						cstmt.setString(1, name);
						cstmt.setString(2, prjName);
						cstmt.setString(3, no.replace("fqst", ""));
						cstmt.setString(4, start);
						cstmt.setString(5, end);
						cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
						cstmt.execute();
						String str = cstmt.getString(6);
						parseTo(str, d, false);
						ds.add(d);
					}
				}
			}
		}else{
			if(checkExist(prjName, versionNO, start, end, name, con)){
				TesterDetail d = new TesterDetail();
				d.setPrjName(prjName);
				d.setVersionNO(versionNO);
				d.setTesterName(name);
				cstmt = con
						.prepareCall("{call dbo.get_tester(?,?,?,?,?,?)}");
				cstmt.setString(1, name);
				cstmt.setString(2, prjName);
				cstmt.setString(3, versionNO.replace("fqst", ""));
				cstmt.setString(4, start);
				cstmt.setString(5, end);
				cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
				cstmt.execute();
				String str = cstmt.getString(6);
				parseTo(str, d, false);
				ds.add(d);
			}
		}
		DbUtil.closeConnection(con);
		return ds;
	}

	public List<String> getNames(SessionMap session) throws Exception {
		List<String> strs = new ArrayList<String>();
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.getTestName(?)}");
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
	
	@Transactional
	public Map<String,String> getNames(){
		Map<String,String> map=new HashMap<String, String>();
		try{
			String hql =" from SysUser users where users.groupName!='项目管理组' and users.level!=0 ";
			List<SysUser> list= this.getHibernateTemplate().find(hql.toString());
			for(SysUser user:list){
				map.put(user.getUserid(), user.getUsername());
			}
		}catch(Exception e){
				e.printStackTrace();
		}
		return map;
	}
	
	@Transactional
	public Map<String,String> getNameList(){
		Map<String,String> map=new HashMap<String,String>();
		try{
			String hql ="select users.username from SysUser users where users.groupName!='项目管理组' and users.level!=0 ";
			List<String> tmpList= this.getHibernateTemplate().find(hql.toString());
			for(String user:tmpList){
				map.put(user,user);
			}
		}catch(Exception e){
				e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 用于问题建议模块
	 * @return
	 */
	@Transactional
	public Map<String,String> getNameListBySuggestion(){
		Map<String,String> map=new HashMap<String,String>();
		try{
			String hql ="select users.username from SysUser users where users.groupName!='项目管理组' and users.username!='开发员' ";
			List<String> tmpList= this.getHibernateTemplate().find(hql.toString());
			for(String user:tmpList){
				map.put(user,user);
			}
		}catch(Exception e){
				e.printStackTrace();
		}
		return map;
	}
	


	public TesterDetail getDetail(String prjName, String versionNO,
			String start, String end, String name, SessionMap session)
			throws Exception {
		if (start == null || start.trim().equals("")) {
			start = "1987-3-20 0:0:0";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-3-31 0:0:0";
		}
		TesterDetail detail = new TesterDetail();
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.get_tester(?,?,?,?,?,?)}");
		cstmt.setString(1, name);
		cstmt.setString(2, prjName);
		cstmt.setString(3, versionNO);
		cstmt.setString(4, start);
		cstmt.setString(5, end);
		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
		cstmt.execute();
		String str = cstmt.getString(6);
		parseTo(str, detail, true);
		DbUtil.closeConnection(con);
		return detail;
	}

	private String getPecent(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 4,
				BigDecimal.ROUND_HALF_UP).movePointRight(2);
		return re + "%";
	}

	/**
	 * @author feel
	 * 
	 *         private String bugFindRate; private String bugErrorRate; private
	 *         String bugValidateNum; private String caseExecRate; private
	 *         String caseExecSpeed;
	 * 
	 * 
	 *         1-建议:3|2-警告:55|3-一般:24|4-严重:22|5-致命:0|已否决:7
	 *         |subTotal:104|noCase:15
	 *         |bugFix:19|caseSubTotal:956|casePassAndFail:910|caseTime:46000|
	 */
	private void parseTo(String str, TesterDetail detail, boolean isSingle) {
		String[] levers = str.split("\\|");
		double bugFindRate = 0.0;
		double bugErrorRate = 0.0;
		int bugValidateNum = 0;
		double caseExecRate = 0.0;
		double caseExecSpeed = 0.0; //
		int bugFatal = 0;
		int bugSerious = 0;
		int bugGeneral = 0;
		int bugWarn = 0;
		int bugSuguest = 0;
		int bugReject = 0;
		int bugNoCase = 0;
		int subtotal = 0;
		int caseExecNum = 0;
		long caseExecTime = 0L;//
		int bugFix = 0;
		int caseSubTotal = 0;
		int casePassAndFail = 0;

		for (String lever : levers) {
			if (lever.startsWith("1-建议")) {
				bugSuguest = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("2-警告")) {
				bugWarn = Integer.parseInt((lever
						.substring(lever.indexOf(":") + 1)));
			} else if (lever.startsWith("3-一般")) {
				bugGeneral = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("4-严重")) {
				bugSerious = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("5-致命")) {
				bugFatal = Integer.parseInt((lever
						.substring(lever.indexOf(":") + 1)));
			} else if (lever.startsWith("已否决")) {
				bugReject = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("subTotal")) {
				subtotal = Integer.parseInt((lever
						.substring(lever.indexOf(":") + 1)));
			} else if (lever.startsWith("noCase")) {
				bugNoCase = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("bugFix")) {
				bugFix = Integer.parseInt((lever
						.substring(lever.indexOf(":") + 1)));
			} else if (lever.startsWith("caseSubTotal")) {
				caseSubTotal = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("casePassAndFail")) {
				casePassAndFail = Integer.parseInt((lever.substring(lever
						.indexOf(":") + 1)));
			} else if (lever.startsWith("caseTime")) {
				caseExecTime = Long.parseLong((lever.substring(lever
						.indexOf(":") + 1)));
				
			}
		}

		// 缺陷发现率 = ∑缺陷数（个） / ∑用例执行时间（小时）ok
		// 缺陷误报率 = REJECTED缺陷/总缺陷数（含REJECTED缺陷）
		// 验证缺陷数 = 测试人员重新打开或验证通过后关闭的缺陷数
		// 用例执行率 = （Failed + Passed）/ 用例总数 * 100%
		// 用例执行效率 = 用例执行数/执行时间(min)
		// double bugFindRate=0.0;
		// double bugErrorRate=0.0;
		// int bugValidateNum=0;
		// double caseExecRate=0.0;
		// double caseExecSpeed=0.0; //
		/*
		 * p private String bugReject; private String bugNoCase; private String
		 * subtotal; private String caseExecNum; private String caseExecTime;
		 */
		if (!isSingle) {
			bugFindRate = caseExecTime == 0 ? 0 : subtotal * 1.0*60
					/ caseExecTime ;
			bugErrorRate = (subtotal + bugReject) == 0 ? 0 : bugReject * 1.0
					/ (subtotal + bugReject); //
			// bugValidateNum 没取到
			caseExecRate = caseSubTotal == 0 ? 0 : casePassAndFail * 1.0
					/ caseSubTotal; //
			caseExecSpeed = caseExecTime == 0 ? 0 : casePassAndFail * 60.0
					/ caseExecTime;
			detail.setBugFindRate(get2dec(bugFindRate));
			detail.setBugErrorRate(getPecent(bugErrorRate));
			detail.setCaseExecRate(getPecent(caseExecRate));
			detail.setCaseExecSpeed(get2dec(caseExecSpeed));
			detail.setBugValidateNum(bugFix+"");
			
		} else {
			detail.setBugFatal(bugFatal + "");
			detail.setBugSerious(bugSerious + "");
			detail.setBugGeneral(bugGeneral + "");
			detail.setBugWarn(bugWarn + "");
			detail.setBugSuguest(bugSuguest + "");
			detail.setBugReject(bugReject + "");
			detail.setBugNoCase(bugNoCase + "");
			detail.setSubtotal(subtotal + "");
			detail.setCaseExecNum(caseSubTotal + "");
			detail.setCaseExecTime(caseExecTime + "");
		}
	}

	private String get2dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 2,
				BigDecimal.ROUND_HALF_UP);
		return re + "";
	}

	public List<String> getPrjects(SessionMap session) throws SQLException {
		List<String> ps = new ArrayList<String>();
		//ps.add("全选");
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
	
	private boolean checkExist(String prjName, String versionNO,
			String start, String end, String name, Connection con){
		boolean flag=false;
		String sql="select count(bg_bug_id) from td.bug where bg_project=? and (ISNULL(bg_detection_version,'fqst')=? or bg_user_05=?) and bg_detected_by=? and (bg_detection_date >=?  and bg_detection_date <=? )";
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, prjName);
			ps.setString(2, versionNO);
			ps.setString(3, versionNO);
			ps.setString(4,name);
			ps.setString(5, start);
			ps.setString(6, end);
			ResultSet rs=ps.executeQuery();
			rs.next();
			long count=rs.getLong(1);
			if(count!=0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	


}
