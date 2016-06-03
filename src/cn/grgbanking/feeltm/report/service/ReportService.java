package cn.grgbanking.feeltm.report.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.WorkReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.ReportDao;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.feeltm.report.domain.ReportInfo;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("reportService")
@Transactional
public class ReportService extends BaseService {
	@Autowired
	private ReportDao reportDao;

	/**
	 * boolean flag = false; try { specialRegulationDao.addObject(obj); flag =
	 * true; } catch (Exception e) { flag = false; e.printStackTrace();
	 * SysLog.error(e); } return flag;
	 * 
	 * @param report
	 * @return
	 */
	public boolean add(WorkReport report) {
		boolean flag = false;
		try {
			reportDao.addObject(report);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean add(List<WorkReport> reports) {
		boolean flag = false;
		try {
			reportDao.add(reports);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean delete(String[] ids) {
		return reportDao.remove(ids);
	}

	public boolean delete(WorkReport report) {
		boolean flag = false;
		try {
			reportDao.removeObject(WorkReport.class, report.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean update(WorkReport report) {
		boolean flag = false;
		try {
			reportDao.updateObject(report);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public WorkReport getReportById(String id) {
		return (WorkReport) reportDao.getObject(WorkReport.class, id);
	}

	@Transactional(readOnly = true)
	public Page getPage(ReportInfo info, int pageNum, int pageSize) {
		return reportDao.getPage(info, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getSummaryPage(ReportInfo info) {
		return reportDao.getSummaryPage(info);
	}

	@Transactional(readOnly = true)
	public Double getSum(Date createDate, String username) {
		return reportDao.selectSum(createDate, username);
	}

	public boolean updateAll(String username, Date createDate,
			Date createDateHidden, List<WorkReport> reports) {
		boolean flag = false;
		try {
			reportDao
					.updateAll(username, createDate, createDateHidden, reports);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getPage(ReportDayInfo info, int pageNum, int pageSize) {
		return reportDao.getPage(info, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List<WorkReport> getReportsByDay(String username, Date createDate) {
		return reportDao.selectReports(username, createDate);
	}

	public boolean remove(String username, Date createDate) {
		return reportDao.remove(username, createDate);
	}

	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return reportDao.getUsernamesByGroup(groupName);
	}

	@Transactional(readOnly = true)
	public List<DayReportStatic> getReportStatics(String currentMontn) {
		List<DayReportStatic> list = reportDao.dayStatic(currentMontn);// 获得前四项数据
		if (list != null && list.size() > 0) {
			for (DayReportStatic st : list) {
				Date date = st.getStartDate();
				String str="";
				if(st.getWeekDay().equals("星期六")||st.getWeekDay().equals("星期日")){
					//获得填写日报的人员，即周末的加班人员
					String str1="<font color='blue'>加班人员：</font>";
					String str2=reportDao.getWritePersons(date);
					str=str1+str2;
				}else{
					str = reportDao.getUnwritePersons(date); // 获得未填写的人员
				}
				st.setPersonDetail(str);
			}
		}
		return list;
	}

	@Transactional(readOnly = true)
	public List<DayReportStatic> getReportStatics(String start, String end) {
		if (start == null || "".equals(start.trim())) {
			start="1987-09-19";
		}
		if (end == null || "".equals(end.trim())) {
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			Date d=new Date();
			end=fo.format(d);
		}
		List<DayReportStatic> list = reportDao.dayStatic(start, end);// 获得前四项数据
		if (list != null && list.size() > 0) {
			for (DayReportStatic st : list) {
				Date date = st.getStartDate();
				String str="";
				if(st.getWeekDay().equals("星期六")||st.getWeekDay().equals("星期日")){
					//获得填写日报的人员，即周末的加班人员
					String str1="<font color='blue'>加班人员：</font>";
					String str2=reportDao.getWritePersons(date);
					str=str1+str2;
				}else{
					str = reportDao.getUnwritePersons(date); // 获得未填写的人员
				}
				st.setPersonDetail(str);
			}
		}
		return list;
	}

}
