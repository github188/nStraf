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
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.prj.domain.DesignerDetail;
import cn.grgbanking.feeltm.prj.domain.DeveloperDetail;
import cn.grgbanking.feeltm.util.DbUtil;
import cn.grgbanking.framework.dao.BaseDao;

/**
 * @szName varchar(20) = '',
 * @szPrjName varchar(64) = '',
 * @startDate datetime = '1970-1-1 0:0:0',
 * @endDate datetime = '2050-1-1 0:0:0',
 * @szCount varchar(1024) output
 */
@Repository("designerDao")
public class DesignerDetailDao extends BaseDao<DeveloperDetail> {
	public List<DesignerDetail> getDetails(String prjName, String start,
			String end, String name, SessionMap session) throws Exception {
		List<DesignerDetail> ds = new ArrayList<DesignerDetail>();
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
		if (prjName.equals("全选")) {
			prjName = "";
			List<String> prjs = getPrjects(session);
			for (String prj : prjs) {
				DesignerDetail d = new DesignerDetail();
				d.setPrjName(prj);
				d.setName(name);
				cstmt = con.prepareCall("{call dbo.get_designer(?,?,?,?,?)}");
				cstmt.setString(1, name);
				cstmt.setString(2, prj);
				cstmt.setString(3, start);
				cstmt.setString(4, end);
				cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
				cstmt.execute();
				String str = cstmt.getString(5);
				parseTo(str, d, false, con, start, end);
				ds.add(d);
			}
		} else {
			DesignerDetail d = new DesignerDetail();
			d.setPrjName(prjName);
			d.setName(name);
			cstmt = con.prepareCall("{call dbo.get_designer(?,?,?,?,?)}");
			cstmt.setString(1, name);
			cstmt.setString(2, prjName);
			cstmt.setString(3, start);
			cstmt.setString(4, end);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();
			String str = cstmt.getString(5);
			parseTo(str, d, false, con, start, end);
			ds.add(d);
		}
		DbUtil.closeConnection(con);
		return ds;
	}

	public DesignerDetail getDetail(String prjName, String start, String end,
			String name, SessionMap session) throws Exception {
		if (start == null || start.trim().equals("")) {
			start = "1987-3-20 0:0:0";
		}
		if (end == null || end.trim().equals("")) {
			end = "2050-3-31 0:0:0";
		}
		DesignerDetail detail = new DesignerDetail();
		Connection con = DbUtil.getConnection(session);
		CallableStatement cstmt = null;
		cstmt = con.prepareCall("{call dbo.get_designer_case(?,?,?,?,?)}");
		cstmt.setString(1, name);
		cstmt.setString(2, prjName);
		cstmt.setString(3, start);
		cstmt.setString(4, end);
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.execute();
		String str = cstmt.getString(5);
		parseTo(str, detail, true, con, start, end);
		DbUtil.closeConnection(con);
		return detail;
	}

	private String getPecent(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 4,
				BigDecimal.ROUND_HALF_UP).movePointRight(2);
		return re + "%";
	}

	// 已覆盖需求:0|所有的需求:0|已解决的用例缺陷:0|用例缺陷数:0|通过用例发现的缺陷:2
	// |缺陷总数:15|用例总数:0|新增用例数:0|
	private void parseTo(String str, DesignerDetail detail, boolean isSingle,
			Connection con, String start, String end) throws Exception {
		String[] levers = str.split("\\|");
		// 需求覆盖率 用例缺陷数 用例缺陷解决率 用例漏出率 用例有效性 新增
		if (!isSingle) {
			int iCoverReq = 0;
			int iTotalReq = 0;
			int iCaseBugRes = 0;
			int iCaseBug = 0;
			int iCaseToBug = 0;
			int iBugTotal = 0;
			int iCaseTotal = 0;
			int iCaseAdd = 0;
			for (String lever : levers) {
				if (lever.startsWith("已覆盖需求")) {
					iCoverReq = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("所有的需求")) {
					iTotalReq = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("已解决的用例缺陷")) {
					iCaseBugRes = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("用例缺陷数")) {
					iCaseBug = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("通过用例发现的缺陷")) {
					iCaseToBug = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("缺陷总数")) {
					iBugTotal = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("用例总数")) {
					iCaseTotal = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				} else if (lever.startsWith("新增用例数")) {
					iCaseAdd = Integer.parseInt((lever.substring(lever
							.indexOf(":") + 1)));
				}
			}
			double reqCoverRate = (iTotalReq == 0) ? 0
					: (iCoverReq * 1.0 / iTotalReq);
			double caseLowRate = (iCaseTotal == 0) ? 0
					: (iCaseAdd * 1.0 / iCaseTotal);
			double caseValidRate = (iBugTotal == 0) ? 0
					: (iCaseToBug * 1.0 / iBugTotal);
			detail.setReqCoveredRate(getPecent(reqCoverRate));
			detail.setCaseBugNum(iCaseBug + "");
			if (iCaseBug == 0) {
				detail.setCaseBugResolveRate("无用例缺陷");
			} else {
				double caseBugResolveRate = iCaseBugRes * 1.0 / iCaseBug;
				detail.setCaseBugResolveRate(getPecent(caseBugResolveRate));
			}
			detail.setCaseLowRate(getPecent(caseLowRate));
			detail.setCaseValidRate(getPecent(caseValidRate));
		} else {
			// 详情页面对应的参数
			for (String lever : levers) {
				if (lever.startsWith("新增")) {
					String add = lever.substring(lever.indexOf(":") + 1);
					detail.setAdd(add);
				} else if (lever.startsWith("修改")) {
					String update = lever.substring(lever.indexOf(":") + 1);
					detail.setUpdate(update);
				} else if (lever.startsWith("删除")) {
					String delete = lever.substring(lever.indexOf(":") + 1);
					detail.setDelete(delete);
				} else if (lever.startsWith("已评审")) {
					String audited = lever.substring(lever.indexOf(":") + 1);
					detail.setAudited(audited);
				} else if (lever.startsWith("未评审")) {
					String unAudit = lever.substring(lever.indexOf(":") + 1);
					detail.setUnAudit(unAudit);
				} else if (lever.startsWith("实现自动化用例")) {
					String autoFlag = lever.substring(lever.indexOf(":") + 1);
					detail.setAutoFlag(autoFlag);
				}else if (lever.startsWith("未定义")) {
					String undefined = lever.substring(lever.indexOf(":") + 1);
					detail.setUndefined(undefined);
				}
			}
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

	/**
	 * 
	 * select count(bg_bug_id) from td.bug where bg_project=? and
	 * bg_detection_version=? and bg_responsible=? and (bg_detection_date >=?
	 * and bg_detection_date <=? )
	 */
	private boolean checkExist(String prjName, String versionNO, String start,
			String end, String name, Connection con) {
		boolean flag = false;
		String sql = "select count(bg_bug_id) from td.bug where bg_project=? and ISNULL(bg_detection_version,'fqst')=? and bg_responsible=? and (bg_detection_date >=?  and bg_detection_date <=? )";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, prjName);
			ps.setString(2, versionNO);
			ps.setString(3, name);
			ps.setString(4, start);
			ps.setString(5, end);
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
