package cn.grgbanking.feeltm.integralCenter.webApp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.integralCenter.bean.IntegralInfoVo;
import cn.grgbanking.feeltm.integralCenter.domain.IntegralInfo;
import cn.grgbanking.feeltm.integralCenter.service.IntegralCenterService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class IntegralCenterAction extends BaseAction{
	
	@Autowired
	private IntegralCenterService integralCenterService;
	@Autowired
	private StaffInfoService staffInfoService;

	
	private String endDate;   //不用
	 
    private String createDate;
	
    List<List<IntegralInfoVo>> resultList = new ArrayList<List<IntegralInfoVo>>();
    
    IntegralInfoVo integralSort = new IntegralInfoVo(); 
   
    
     @SuppressWarnings("unchecked")
	public   String listIntegralCenterData(){
    	 UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
    	 SysUser usr = staffInfoService.findUserByUserid(userModel.getUserid());
    	 List<IntegralInfoVo> stafflist = setTop5(integralCenterService.orderByStaff());
    	 List<IntegralInfoVo> deptIdlist = setTop5(integralCenterService.orderByDept(usr.getDeptName()));
    	 List<IntegralInfoVo> groupIdlist = setTop5(integralCenterService.orderByGroup(userModel.getUserid()));
    	 resultList.add(stafflist);
    	 resultList.add(deptIdlist);
    	 resultList.add(groupIdlist);
    	 //个人积分
    	 
    	 List<Object> ObjectList = integralCenterService.getPersonIntegral(userModel.getUserid());
    	 String integralSum = String.valueOf(ObjectList.get(0)) ;
    	 int companySort = sortListByUserName(userModel.getUserid());
    	 int deptSort = sortListBydeptId(usr.getDeptName(),userModel.getUserid());
    	 int groupSort = sortProjectListBy(userModel.getUserid());
    	 integralSort.setCompanySort(String.valueOf(companySort));
    	 integralSort.setDeptSort(String.valueOf(deptSort));
    	 integralSort.setGroupSort(String.valueOf(groupSort));
    	 if (!"null".equals(integralSum)) {
    		 integralSort.setIntegralSum(integralSum);
		}else{
			integralSort.setIntegralSum("0");
		}
    	//每日准时（每日23:55前）填写日志获得2积分
    	 String dayLogGrade =  StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogGrade"));
    	 integralSort.setDayLogGrade(dayLogGrade);
		//7日内补填日志获得1积分
		 String dayLogNotOnTimeGrade = StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogNotOnTimeGrade"));
		 integralSort.setDayLogNotOnTimeGrade(dayLogNotOnTimeGrade);
		 //每周准时（每周结束之前）填写个人周报获得2积分
		 String weekLogGrade = StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogGrade"));;
		 integralSort.setWeekLogGrade(weekLogGrade);
		 //超时补填个人周报获得1积分
		 String weekLogTimeout = StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogTimeout"));;
		 integralSort.setWeekLogTimeout(weekLogTimeout);
		 //爱心小鱼被赞或发表赞扬 
		 String praiseOrPraised = StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"));;
		 integralSort.setPraiseOrPraised(praiseOrPraised);
		 //发表赞扬每日得分上限
		 String sendPraiseLimit = StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("sendPraiseLimit"));;
		 integralSort.setSendPraiseLimit(sendPraiseLimit);
    	 
    	 
    	 
 		 return "show";
     }
     //查询详情
 	public   String detailList(){
 		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
 		try{
 		String from = request.getParameter("from");
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int pageNum = 1;
		int pageSize = 20;
		if (request.getParameter("pageNum") != null
			&& request.getParameter("pageNum").length() > 0)
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		if (request.getParameter("pageSize") != null
				&& request.getParameter("pageSize").length() > 0)
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
 		Page page = integralCenterService.getPage(createDate, "", userModel.getUserid(), pageNum, pageSize, endDate);
 		request.setAttribute("currPage", page);
 		// 	    List list =  page.getQueryResult();
 	   List<Object> list = (List<Object>)page.getQueryResult();
 	  for (int i = 0; i < list.size(); i++) {
		  IntegralInfo tmp=(IntegralInfo)list.get(i);
		  
	}
 	   if (from != null && from.equals("refresh")) {
		Map map = new LinkedHashMap();
		map.put("pageCount", String.valueOf(page.getPageCount()));
		map.put("recordCount", String.valueOf(page.getRecordCount()));
		JSONUtil jsonUtil = new JSONUtil(
				"cn.grgbanking.feeltm.integralCenter.domain.IntegralInfo");
		JSONArray jsonObj = jsonUtil.toJSON(list, map);
		JSONObject input = new JSONObject();
		if(page.getRecordCount()==0){
			input.put("pageCount", String.valueOf(page.getPageCount()+1));	
			input.put("recordCount", String.valueOf(page.getRecordCount()));
			input.put("jsonObj", jsonObj);	
		}
		else{
			input.put("pageCount", String.valueOf(page.getPageCount()));
			input.put("recordCount", String.valueOf(page.getRecordCount()));
			input.put("jsonObj", jsonObj);	
		}
		input.put("jsonObj", jsonObj);								
		PrintWriter out = response.getWriter();
		out.print(input);
		return null;
	} else {
		ActionContext.getContext().put("detailList", list);
		return "list";
	}
} catch (Exception e) {
	e.printStackTrace();
	SysLog.error(e);
}
 		return "list";
}

	public IntegralInfoVo getIntegralSort() {
		return integralSort;
	}
	public void setIntegralSort(IntegralInfoVo integralSort) {
		this.integralSort = integralSort;
	}
	public List<List<IntegralInfoVo>> getResultList() {
		return resultList;
	}
	public void setResultList(List<List<IntegralInfoVo>> resultList) {
		this.resultList = resultList;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	//top5 设值
	@SuppressWarnings("unchecked")
	public List<IntegralInfoVo> setTop5(List list){
		List<IntegralInfoVo> resultList = new ArrayList<IntegralInfoVo>();
		List<Object[]> ObjectList = (List<Object[]>)list;
		int five = 0;
		for (Object[] obj : ObjectList) {
			if (five<5){
				IntegralInfoVo integralInfoVo = new IntegralInfoVo();
				if(null!=obj[0])
					integralInfoVo.setIntegralSum(obj[0].toString());
				if(null!=obj[1])
					integralInfoVo.setDetName((String)obj[1]);
				if(null!=obj[2])
					integralInfoVo.setUserName((String)obj[2]);
				resultList.add(integralInfoVo);
			    five++;
			}
			else{
				break;
			}
		}
		return resultList;
	}
	
    
	//全公司排名
    public int sortListByUserName(String userId ){
    	int sort = 1;
    	 List<Object[]> stafflist = integralCenterService.orderByStaff();
    	 for(Object[] obj : stafflist){
    		 if (userId.equals(obj[3].toString().trim())) {
				break;
			}
    		 sort++;
    	 }
    	return sort;
    }
  //全部门排名
    public int sortListBydeptId(String deptId,String userId ){
    	int sort = 1;
    	 List<Object[]> stafflist = integralCenterService.orderByDept(deptId);
    	 for(Object[] obj : stafflist){
    		 if (userId.equals(obj[3].toString().trim())) {
				break;
			}
    		 sort++;
    	 }
    	return sort;
    }
    //全项目排名
    public int sortProjectListBy(String userId){
    	int sort = 1;
    	 List<Object[]> stafflist = integralCenterService.orderByGroup(userId);
    	 for(Object[] obj : stafflist){
    		 if (userId.equals(obj[3].toString().trim())) {
				break;
			}
    		 sort++;
    	 }
    	return sort;
    }

}
