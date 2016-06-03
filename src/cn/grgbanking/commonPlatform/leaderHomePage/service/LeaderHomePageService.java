package cn.grgbanking.commonPlatform.leaderHomePage.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.commonPlatform.leaderHomePage.bean.ContractInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.DeptMonthPersonDayInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.MapInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.PersonDayInfo;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.personDay.dao.PersonDayDao;
import cn.grgbanking.feeltm.personDay.domain.DeptMonthPersonDay;
import cn.grgbanking.feeltm.prjMonitor.dao.ColorSettingDao;
import cn.grgbanking.feeltm.prjcontract.dao.PrjContractDao;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;

@Service("leaderHomePageService")
@Transactional
public class LeaderHomePageService {

	@Autowired 
	private ProjectDao projectDao;
	@Autowired
	private PrjContractDao prjContractDao;
	@Autowired
	private PersonDayDao personDayDao;
	@Autowired
	private ColorSettingDao colorSettingDao;
	@Autowired
	private StaffInfoService staffInfoService;
	/**
	 * @return  按照地图的设定，对这些省市进行设值
	 */
	public List<MapInfo>  mkData(){
		List<MapInfo> mapInfoList = new ArrayList<MapInfo>();
		Map<String,String> myMap= new HashMap<String,String>(40);
		myMap.put("吉林省","cn-jl");        
		myMap.put("天津市","cn-tj");        
		myMap.put("安徽省","cn-ah");        
		myMap.put("山东省","cn-sd");        
		myMap.put("山西省","cn-sx");        
		myMap.put("新疆维吾尔族自治区","cn-xj");
		myMap.put("河北省","cn-hb");        
		myMap.put("河南省","cn-he");        
		myMap.put("湖南省","cn-hn");        
		myMap.put("甘肃省","cn-gs");        
		myMap.put("福建省","cn-fj");        
		myMap.put("贵州省","cn-gz");        
		myMap.put("重庆市","cn-cq");        
		myMap.put("江苏省","cn-js");        
		myMap.put("湖北省","cn-hu");        
		myMap.put("内蒙古自治区","cn-nm");  
		myMap.put("广西壮族自治区","cn-gx"); 
		myMap.put("黑龙江省","cn-hl");      
		myMap.put("云南省","cn-yn");        
		myMap.put("辽宁省","cn-ln");        
		myMap.put("香港特别行政区","cn-6668"); 
		myMap.put("浙江省","cn-zj");        
		myMap.put("上海市","cn-sh");        
		myMap.put("北京市","cn-bj");        
		myMap.put("广东省","cn-gd");        
		myMap.put("澳门特别行政区","cn-3681"); 
		myMap.put("西藏自治区","cn-xz");    
		myMap.put("陕西省","cn-sa");        
		myMap.put("四川省","cn-sc");        
		myMap.put("海南省","cn-ha");        
		myMap.put("宁夏回族自治区","cn-nx"); 
		myMap.put("青海省","cn-qh");        
		myMap.put("江西省","cn-jx");        
		myMap.put("台湾省","tw-tw");        
		
		//  {"hc-key": "cn-sh","value": 0},
		for (Object o : myMap.keySet()) { 
			MapInfo mapInfo = new MapInfo();
			List<Project> prolList= projectDao.getProjectByProvince((String)o);
			mapInfo.setHckey((myMap.get(o)));
			//让地图颜色区分有项目省份和没项目省份更加明显
			if (prolList.size()!=0) {
				mapInfo.setValue(prolList.size()+10);
			}
			StringBuffer projectSb = new StringBuffer("");
			for (Project project: prolList) {
				projectSb.append(project.getName()+" - "+project.getProManager()+",");
			}
			if (prolList.size()>0) {
				projectSb.deleteCharAt(projectSb.length()-1);
				mapInfo.setProjects(projectSb.toString());
			}
			else{
				mapInfo.setProjects("业务有待拓展");
			}
			mapInfoList.add(mapInfo);
		}
		return  mapInfoList;
	}
	
	//合同完成度
	public ContractInfo prjContractSum() {
		
		ContractInfo contractInfo = new ContractInfo();
		//从数字字典获取参数,合同速度计刻度颜色分布比例
		float fristPercent = 0;
		float secondPercent = 0;
		String unitSTr = "";
		try{
			String fristPercentStr = BusnDataDir.getMapKeyValue("leaderHomePage.contract").get("fristPercent").toString().trim();
			String secondPercentSTr = BusnDataDir.getMapKeyValue("leaderHomePage.contract").get("secondPercent").toString().trim();
			if (StringUtils.isNotBlank(fristPercentStr)) {
				fristPercent = Float.parseFloat(fristPercentStr);
			}
			if (StringUtils.isNotBlank(secondPercentSTr)) {
				secondPercent = Float.parseFloat(secondPercentSTr);
			}
			unitSTr = BusnDataDir.getMapKeyValue("leaderHomePage.contract").get("unit").toString().trim();
			contractInfo.setUnit(unitSTr);//设置的单位
			contractInfo.setFristPercent(fristPercent);//前部分合同速度计刻度颜色分布比例
			contractInfo.setSecondPercent(secondPercent);//后部分合同速度计刻度颜色分布比例
			//获取项目合同金额到目前为止是多少
			Date nowDate=new Date();
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		    String nowDateStr = sdf.format(nowDate);
			String yearStr = nowDateStr.substring(0, 4);
			String lastDayStr = ""; 
			BigDecimal sumContract = new BigDecimal(0);
		    sumContract = prjContractDao.prjContractSum(yearStr, nowDateStr, lastDayStr);
			if (sumContract!=null) {
				sumContract = sumContract.setScale(0, BigDecimal.ROUND_HALF_UP);
			}
			contractInfo.setSumContract(sumContract);
			//从数字字典中读取年度合同计划金额
			String annualContractString =  StringUtils.trim((String)BusnDataDir.getMapKeyValue("projectManage.contractPlan").get(yearStr));
		    if (StringUtils.isNotBlank(annualContractString)) {
		    	contractInfo.setContractPlan(new BigDecimal(annualContractString));
			}
		}catch (Exception e) {
				e.printStackTrace();
			}
	    return contractInfo;
	}
	/**
	 * 获取每个模块本年度到该月的人日情况
	 * @return
	 */
	public List<PersonDayInfo>  getpersonDayInfo(){
		List<PersonDayInfo> personDayInfos =new ArrayList<PersonDayInfo>();
		Calendar calendar = Calendar.getInstance();
		String yearStr = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()).substring(0, 4);
		String monthStr = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()).substring(4, 6);
		int month = Integer.parseInt(monthStr);
		Map<String,Object> typeMap=BusnDataDir.getMapKeyObject("projectMonitor.projectType");
		String colorString = "";
		try {
			//获取折线线条的颜色
		 colorString = BusnDataDir.getMapKeyValue("leaderHomePage.leaderHomePageColor").get("dataColor").toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] colorArray = null;
		if (StringUtils.isNotBlank(colorString)) {
		  colorArray = colorString.split(",");
		}
		if (typeMap!=null) {
			Set<String> key = typeMap.keySet();        
			         
				//String s = (String) it.next();          
				//System.out.println(map.get(s));        }
			int index = 0;
			String keyValue = "";
			for (Iterator it = key.iterator(); it.hasNext();)
			{ 
				    String keystr = (String) it.next(); 
				    keyValue = BusnDataDir.getMapKeyValue("projectMonitor.projectType").get(keystr).toString().trim();
					PersonDayInfo pDayInfo = new PersonDayInfo();
					
					//12个月的人日
					BigDecimal[] personDayDecimals = new BigDecimal[month];
					 for (int i = 1; i <=month; i++) {
						 if (i>=10) {
							 monthStr = String.valueOf(i);
							}
							else{
								monthStr = "0"+String.valueOf(i);
							}   
						 //获取某月的版本人日消耗数
						 BigDecimal personDay =  personDayDao.getPersonDayByMoudle(yearStr, monthStr, keystr);
						 personDayDecimals[i-1] = personDay;
				 }
					 pDayInfo.setYear(yearStr);
					 pDayInfo.setName(keyValue);
					 pDayInfo.setData(personDayDecimals);
					 pDayInfo.setColor(colorArray[index]);
					 index++;
					 personDayInfos.add(pDayInfo);
			}
			return personDayInfos;
		}
		else{
			return null;
		}
	}
	/**
	 * 获取每日在定时器中更新当月部门人日消耗。
	 * @return
	 */
	public List<DeptMonthPersonDayInfo>  getDeptMonthPersonDayInfos(){
		//如果当前为月首日,则取上一个月的整月来进行人日统计
		//如果不是为首日,则取当前月来统计
		List<String> dateList = DateUtil.getWorkdayList();
		String startDate ="";
		startDate = dateList.get(0);
		String yearStr = startDate.substring(0, 4);
		String monthStr = startDate.substring(5, 7);
		List<DeptMonthPersonDay> deptMonthPersonDayList = new ArrayList<DeptMonthPersonDay>();
		List<DeptMonthPersonDayInfo> deptMonthPersonDayInfos = new ArrayList<DeptMonthPersonDayInfo>();
		//20150326 wtjiao 修改原来的逻辑，为了和成本控制模块保持一致，该模块修改为展现经过部门经理确认的，算在部门头上的人日消耗。而不再展示该部门下的人员项目消耗
		deptMonthPersonDayList = personDayDao.queryDeptMonthPersonDay(yearStr, monthStr);
		if (deptMonthPersonDayList!=null) {
			for (DeptMonthPersonDay deptMonthPersonDay : deptMonthPersonDayList) {
				   DeptMonthPersonDayInfo dPersonDayInfo = new DeptMonthPersonDayInfo();
				   dPersonDayInfo.setDeptId(staffInfoService.getDeptkeyByValue(deptMonthPersonDay.getDeptName()));
				   dPersonDayInfo.setDeptName(deptMonthPersonDay.getDeptName());
				   dPersonDayInfo.setPersonDay(Long.parseLong(deptMonthPersonDay.getPersonDay()));
				   String color = colorSettingDao.getDeptColorById(staffInfoService.getDeptkeyByValue(deptMonthPersonDay.getDeptName()));
				   dPersonDayInfo.setDeptColor(color);
				   dPersonDayInfo.setMonth(monthStr);
				   dPersonDayInfo.setYear(yearStr);
				   deptMonthPersonDayInfos.add(dPersonDayInfo);
				}
				return deptMonthPersonDayInfos;
		}
		else{
			return null;
		}
		
	}
	
}
