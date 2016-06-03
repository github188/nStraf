package cn.grgbanking.feeltm.costControl.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.costControl.bean.DeptDetailCostBean;
import cn.grgbanking.feeltm.costControl.bean.UserDateProjectPersonDayBean;
import cn.grgbanking.feeltm.costControl.services.CostControlService;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.PingYingUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class DetailCostControlAction extends BaseAction{
	@Autowired
	private CostControlService costControlService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private HolidayService holidayService;
	
	public String formatDoubleShow(String s){
		if(s.indexOf('.')>0 && s.length()>=(s.indexOf('.')+3)){
			s=s.substring(0,s.indexOf('.')+3);
		}
		return s;
	}
	
	/** 查询部门详细信息
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String queryStartTime=null;
			String queryEndTime=null;
			String queryDept=null;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
				queryStartTime = request.getParameter("queryStartTime");
			if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
				queryEndTime = request.getParameter("queryEndTime");
			if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
				queryDept = request.getParameter("queryDept");
			
			if(from==null){
				Calendar cal=Calendar.getInstance();
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH)+1;
				queryStartTime=year+"-"+(month<10?("0"+month):month)+"-01";
				queryEndTime=year+"-"+(month<10?("0"+month):month)+"-"+DateUtil.getLastDayForYearAndMonth(year, month);
				request.setAttribute("queryStartTime", queryStartTime);
				request.setAttribute("queryEndTime", queryEndTime);
			}
			
			
			Page page = costControlService.getDeptDetailCostBeanPage(queryDept,queryStartTime,queryEndTime,pageNum,pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				//公司的json转换
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.costControl.bean.DeptDetailCostBean");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);

				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("detaillist", list);
				return "list";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "list";
	}
	
	
	/**
	 * @return 导出Excel表
	 */
	@SuppressWarnings("unchecked")
	public String exportData() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute( Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		request.setCharacterEncoding("utf-8");

		String queryStartTime=null;
		String queryEndTime=null;
		String queryDept=null;
		if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
			queryStartTime = request.getParameter("queryStartTime");
		if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
			queryEndTime = request.getParameter("queryEndTime");
		if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
			queryDept = request.getParameter("queryDept");
		
		//查询结果
		List costBeanList = costControlService.getDeptDetailCostBeanList(queryDept,queryStartTime,queryEndTime);
		//对查询的结果按照部门和日期排序
		Collections.sort(costBeanList,new Comparator<DeptDetailCostBean>() {
			@Override
			public int compare(DeptDetailCostBean o1, DeptDetailCostBean o2) {
				if(o1.getDeptName().compareTo(o2.getDeptName())==0){
					return o1.getUserName().compareTo(o2.getUserName());
				}else{
					return o1.getDeptName().compareTo(o2.getDeptName());
				}
			}
		});
		//文件名
		String filename = getWorkSheetName(queryDept,queryStartTime,queryEndTime);//设置文件名
		
		doExport(costBeanList,filename);
		
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}         
	
	/**转入导出页面
	 * @return
	 */
	public String exportProjectPersonDay(){
		try {
			List<Project> projects=projectService.listAllGroup();
			Calendar cal=Calendar.getInstance();
			Calendar cal2=Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DATE));
			request.setAttribute("projects", projects);
			request.setAttribute("startDate", cal.getTime());
			request.setAttribute("endDate", cal2.getTime());
			return "exportProjectPersonDay";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String exportData_ProjectPersonDay(){
		try{
			UserModel userModel = (UserModel) request.getSession().getAttribute( Constants.LOGIN_USER_KEY);
			request.setCharacterEncoding("utf-8");

			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String projectId=request.getParameter("projectId");
			String includeTrainee=request.getParameter("includeTrainee");
			boolean isIncludeTrainee="checked".equals(includeTrainee) || "on".equals(includeTrainee) || "yes".equals(includeTrainee);
			if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && StringUtils.isNotBlank(projectId)){
				//查询结果
				List<DateDayLog> logList = costControlService.getProjectPersonDay(projectId,startDate,endDate);
				
				//所有人员map
				Map<String,SysUser> allUserMap=staffInfoService.listAllUserMap();
				
				//项目信息
				Project project=projectService.getProjectById(projectId);
				
				//组装为UserDateProjectPersonDayBean对象
				List<UserDateProjectPersonDayBean> beanList=toUserDateProjectPersonDayBean(logList,allUserMap,project,isIncludeTrainee);
				
				//排序，先按照部门，再按照姓名拼音排序
				Collections.sort(beanList,new Comparator<UserDateProjectPersonDayBean>() {
					@Override
					public int compare(UserDateProjectPersonDayBean o1, UserDateProjectPersonDayBean o2) {
						if(o1.getDeptName().compareTo(o2.getDeptName())==0){
							String pinying1=PingYingUtil.cn2Spell(o1.getUserName());
							String pingying2=PingYingUtil.cn2Spell(o2.getUserName());
							return pinying1.compareToIgnoreCase(pingying2);
						}else{
							return o1.getDeptName().compareTo(o2.getDeptName());
						}
					}
				});
				
				//文件名
				String filename = getWorkSheetName(projectService.getProjectById(projectId).getName(),startDate,endDate);//设置文件名
				
				doExport_ProjectPersonDay(beanList,filename,startDate,endDate);
				
				MsgBox msgBox = new MsgBox(request,"导出成功");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage("导出成功");
				return "msgBox";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		MsgBox msgBox = new MsgBox(request, "导出操作失败!");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage("导出操作失败!");
		return "msgBox";
	}
	
	/**将查询出来的DateDayLog的list转为用户的人日对象
	 * @param logList
	 * @param allUserMap 所有用户的对象map
	 * @param project 项目信息
	 * @param includeTrainee 是否包含实习生数据
	 * @return
	 */
	private List<UserDateProjectPersonDayBean> toUserDateProjectPersonDayBean(List<DateDayLog> logList, Map<String, SysUser> allUserMap, Project project, boolean includeTrainee) {
		//第一步:根据用户id和日期分组，对象结构为Map<员工id,Map<某个日期,该员工该日期的日志列表>>
		Map<String,Map<String,List<DateDayLog>>> userMap=new HashMap<String, Map<String,List<DateDayLog>>>();
		if(logList!=null){
			for(DateDayLog log:logList){
				//判断是否是实习生
			//	boolean inTraineeUser=inTraineeUsers(getTraineeUsers(), log.getUserId());
				if( log != null ){
					List<DayLog> daylogList = log.getDaylogList();
					if( daylogList != null && daylogList.size()>0){
						for(DayLog daylog : daylogList){
							boolean inTraineeUser=inTraineeUsers(daylog);
							//是实习生且本次不导出实习生数据
							if(inTraineeUser&&!includeTrainee){
								daylogList.remove(daylog);
								continue;
							}
						}
					}
				}
				Map<String,List<DateDayLog>> dateMap=userMap.get(log.getUserId());
				
				if(dateMap==null || dateMap.size()<=0){//日期Map不存在
					//新建一个Map
					dateMap=new HashMap<String, List<DateDayLog>>();
					//新建map中的列表
					List<DateDayLog> list=new ArrayList<DateDayLog>();
					list.add(log);
					//把该日志列表放到新建的map中
					dateMap.put(log.getLogDate(),list);
				}else{//日期Map存在
					//获取当前日期的日志列表
					List<DateDayLog> list=dateMap.get(log.getLogDate());
					//列表为空，则新建一个列表
					if(list==null || list.size()<=0){
						list=new ArrayList<DateDayLog>();
						list.add(log);
					}else{//不为空，直接加入该列表中
						list.add(log);
					}
					//把该日志列表放到已有的map中
					dateMap.put(log.getLogDate(), list);
				}
				//把日期map放到用户map中
				userMap.put(log.getUserId(), dateMap);
			}
		}
		//第二步:遍历得出的map组装人日对象
		List<UserDateProjectPersonDayBean> beanList=new ArrayList<UserDateProjectPersonDayBean>();
		Iterator<String> ite=userMap.keySet().iterator();
		while(ite.hasNext()){
			String userid=ite.next();
			//获取dateMap
			Map<String,List<DateDayLog>> dateMap=userMap.get(userid);
			//转为某日的项目人日消耗map
			Map<String,Double> datePDMap=toDatePersonDayMap(dateMap);
			//组装bean对象
			UserDateProjectPersonDayBean bean=new UserDateProjectPersonDayBean();
			SysUser usr=allUserMap.get(userid);
			bean.setUserId(userid);
			bean.setUserName(usr.getUsername());
			bean.setDeptName(staffInfoService.getDeptNameValueByKey(usr.getDeptName()));
			bean.setProjectName(project.getName());
			bean.setDateProjectPersonDayMap(datePDMap);
			//放入list中
			beanList.add(bean);
		}
		return beanList;
	}

	private boolean inTraineeUsers(DayLog log) {
		boolean flag = false;
		try {
			String employStatus = log.getEmploystatus();//员工的状态  实习 试用、正式 注意要去掉前后的空格
			//如果员工的状态为实习生  study  返回true
			if( employStatus != null && "study".equals(employStatus)){
				flag =  true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**计算dateMap中的值，转为<date,项目人日>的map
	 * @param dateMap
	 * @return
	 */
	private Map<String, Double> toDatePersonDayMap(Map<String, List<DateDayLog>> dateMap) {
		Iterator<String> ite=dateMap.keySet().iterator();
		Map<String, Double> dpdMap=new HashMap<String, Double>();
		while(ite.hasNext()){
			String date=ite.next();
			List<DateDayLog> loglist=dateMap.get(date);
			double total=0D;
			//遍历日志列表，统计出其日志的项目人日消耗总和
			for(DateDayLog datelog:loglist){
				if(datelog.getDaylogList()!=null){
					for(int i=0;i<datelog.getDaylogList().size();i++){
						DayLog log=(DayLog)datelog.getDaylogList().get(i);
						boolean isGroupManagerConfirm=log.getEnterRole()==null?true:"groupManager".equals(log.getEnterRole())?true:false;
						if(isGroupManagerConfirm){//是项目经理确认的
							total+=log.getEnterDay();
						}
					}
				}
			}
			dpdMap.put(date, total);
		}
		return dpdMap ;
	}

	/**获取不监控人员列表
	 * @return
	 */
	private String getTraineeUsers() {
		List<SysDatadir> dirs=BusnDataDir.getObjectListInOrder("costControl.traineeNameList");
		if(dirs!=null){
			for(int i=0;i<dirs.size();i++){
				if(dirs.get(i).getKey().equals("trainess")){
					return dirs.get(i).getValue();
				}
			}
		}
		return "";
	}
	
	/**用户是否在实习生名单中
	 * @param traineeUsers 不监控人员列表
	 * @param userid 指定人员
	 * @return
	 */
	private boolean inTraineeUsers(String traineeUsers, String userid) {
		if(StringUtils.isNotBlank(traineeUsers)){
			String[] tus=traineeUsers.split(",");
			for(String trainee:tus){
				if(trainee.equals(userid)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**导出项目人日明细
	 * @param beanList 人员项目人日数据
	 * @param filename 导出文件名 
	 * @param startDate 导出列开始时间
	 * @param endDate 导出列结束时间
	 * @throws IOException
	 */
	private void doExport_ProjectPersonDay(List<UserDateProjectPersonDayBean> beanList, String filename, String startDate, String endDate) throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar startCal=Calendar.getInstance();
		Calendar endCal=Calendar.getInstance();
		startCal.setTime(sdf.parse(startDate));
		endCal.setTime(sdf.parse(endDate));
	
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb.createSheet();
     	wb.setSheetName(0,filename);//poi3.7版本中只有两个参数
     	
     	/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); //默认字体
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)10);// 字体大小
		
		HSSFFont headfont = wb.createFont(); 
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();  
		cellfont.setFontName("黑体");
		cellfont.setFontHeightInPoints((short)12);
		cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		
		/**	** ** *设置样式* ** ** **/
		//================== 标题 =====================
		HSSFFont cellNamefont = wb.createFont();  
		cellNamefont.setFontName("宋体");
		cellNamefont.setFontHeightInPoints((short)10);
		cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		
		//================== 普通行 (左中)======================
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		
		//================== 普通行 (居中)======================
		HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		
		//================== 日期(居中) ======================
		HSSFCellStyle dateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		defaultCenterStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd "));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "所属部门", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "员工姓名", 0, (short)2, cellNameStyle);
		
		List<String> holidays=holidayService.getHoliday(sdf.parse(startDate), sdf.parse(endDate));
		short colOffset=1;
		while(startCal.compareTo(endCal)<=0){
			String colName=sdf.format(startCal.getTime());
			if(holidays.contains(colName)){
				colName+="(节假)";
			}
			wb = this.setExcelValue(wb, 0,colName, 0, (short)(2+colOffset), cellNameStyle);//设置列名
			startCal.add(Calendar.DAY_OF_YEAR, 1);
			colOffset++;
		}
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;//行
		for(int i=0;i<beanList.size();i++){
			UserDateProjectPersonDayBean udpBean=(UserDateProjectPersonDayBean)beanList.get(i);
			++sumRow;
			HSSFRow row = sheet.createRow(sumRow);
			row.setHeight((short)256);//行高
			HSSFCell cell = row.createCell((short)0);
			
			/*项目名称*/
			cell = sheet.getRow(sumRow).createCell((short)0);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(udpBean.getProjectName());
			sheet.setColumnWidth((short)0, (short) (12*512));
			
			/*所属部门*/
			cell = sheet.getRow(sumRow).createCell((short)1);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(udpBean.getDeptName());
			sheet.setColumnWidth((short)1, (short) (8*512));
			
			/*员工姓名*/
			cell = sheet.getRow(sumRow).createCell((short)2);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(udpBean.getUserName()+"("+udpBean.getUserId()+")");
			sheet.setColumnWidth((short)2, (short) (10*512));
			
			colOffset=1;//列偏移量
			startCal.setTime(sdf.parse(startDate));//遍历的开始时间重置为查询开始时间
			endCal.setTime(sdf.parse(endDate));//遍历的结束时间重置为查询结束时间
			
			/*其他数据列*/
			while(startCal.compareTo(endCal)<=0){
				String date=sdf.format(startCal.getTime());//当前遍历的日期
				Double datePersonDay=udpBean.getDateProjectPersonDayMap().get(date);//根据date获取当天的人日
				//对数据进行四舍五入和截取处理
				String showPersonDay=datePersonDay!=null?formatDoubleShow((Math.round(datePersonDay*100.0)/100.0)+""):"0";
				cell = row.createCell((short)(2+colOffset));
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(showPersonDay);
				sheet.setColumnWidth((short)(2+colOffset), (short) (10*512));
				startCal.add(Calendar.DAY_OF_YEAR, 1);
				colOffset++;
			}
			
		}
		
		wb.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
	}
	
	
	private void doExport(List costBeanList, String filename) throws IOException {
		
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb.createSheet();
//     	wb.setSheetName(0,filename,HSSFWorkbook.ENCODING_UTF_16);
     	wb.setSheetName(0,filename);//poi3.7版本中只有两个参数
     	
     	/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); //默认字体
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)10);// 字体大小
		
		HSSFFont headfont = wb.createFont(); 
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();  
		cellfont.setFontName("黑体");
		cellfont.setFontHeightInPoints((short)12);
		cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		
		/**	** ** *设置样式* ** ** **/
		//================== 标题 =====================
		HSSFFont cellNamefont = wb.createFont();  
		cellNamefont.setFontName("宋体");
		cellNamefont.setFontHeightInPoints((short)10);
		cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		
		//================== 普通行 (左中)======================
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 普通行 (居中)======================
		HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 日期(居中) ======================
		HSSFCellStyle dateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		defaultCenterStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd "));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb = this.setExcelValue(wb, 0, "所属部门", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "员工姓名", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "开始时间", 0, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "结束时间", 0,(short) 3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目经理确认", 0, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "部门经理确认",0, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "未确认", 0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "未登记", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "实习人员消耗", 0, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "非项目比例%", 0, (short)9, cellNameStyle);
		
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;
		for(int i=0;i<costBeanList.size();i++){
			DeptDetailCostBean costBean=(DeptDetailCostBean)costBeanList.get(i);
			++sumRow;
			HSSFRow row = sheet.createRow(sumRow);
			row.setHeight((short)256);//行高
			HSSFCell cell = row.createCell((short)0);
			
			/*所属部门*/
			cell = sheet.getRow(sumRow).createCell((short)0);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptName());
			sheet.setColumnWidth((short)0, (short) (10*512));
			
			/*员工姓名*/
			cell = sheet.getRow(sumRow).createCell((short)1);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getUserName()+"("+costBean.getUserId()+")");
			sheet.setColumnWidth((short)1, (short) (12*512));
			
			/*开始时间*/
			cell = row.createCell((short)2);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getQueryStartDate());
			sheet.setColumnWidth((short)2, (short) (12*512));
			
			/*结束时间*/
			cell = row.createCell((short)3);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getQueryEndDate());
			sheet.setColumnWidth((short)3, (short) (12*512));
			
			/*项目经理确认*/
			cell = row.createCell((short)4);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getProjectManagerConfirm());
			sheet.setColumnWidth((short)4, (short) (10*512));
			
			/*部门经理确认*/
			cell = row.createCell((short)5);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptManagerConfrim());
			sheet.setColumnWidth((short)5, (short) (10*512));
			
			/*未确认*/
			cell = row.createCell((short)6);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotConfirm());
			sheet.setColumnWidth((short)6, (short) (10*512));
			
			/*未登记*/
			cell = row.createCell((short)7);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotRegist());
			sheet.setColumnWidth((short)7, (short) (10*512));
			
			/*实习人员消耗*/
			cell = row.createCell((short)8);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			double traineeCost=costBean.getTraineeCost();
			cell.setCellValue(traineeCost>0?traineeCost+"":"-");
			sheet.setColumnWidth((short)8, (short) (10*512));
			
			/*非项目比例*/
			cell = row.createCell((short)9);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotProjectPercent());
			sheet.setColumnWidth((short)9, (short) (10*512));
		}
		
		wb.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
	}


	/**excel表的名称
	 * @param queryDept
	 * @param queryStartTime
	 * @param queryEndTime
	 * @return
	 */
	private String getWorkSheetName(String queryDept, String queryStartTime,String queryEndTime) {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String name="";
			if(StringUtils.isNotBlank(queryDept)){
				name+=queryDept+" ";
			}
			if(StringUtils.isNotBlank(queryStartTime)&&StringUtils.isNotBlank(queryEndTime)){
				Date st=sdf.parse(queryStartTime);
				Date end=sdf.parse(queryEndTime);
				name+=new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end);
			}
			name+=" 人日成本明细";
			return name;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "部门人日成本明细";
	}
	
	private void setBorderStyle(HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
		style.setBottomBorderColor(HSSFColor.BLACK.index);	
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder 是否显示边框
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			setBorderStyle(style);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
}
