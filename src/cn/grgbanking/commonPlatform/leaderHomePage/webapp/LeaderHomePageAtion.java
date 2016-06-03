package cn.grgbanking.commonPlatform.leaderHomePage.webapp;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.commonPlatform.leaderHomePage.bean.ContractInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.DeptMonthPersonDayInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.LeaderHomeInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.MapInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.bean.PersonDayInfo;
import cn.grgbanking.commonPlatform.leaderHomePage.service.LeaderHomePageService;
import cn.grgbanking.commonPlatform.utils.CommonUtil;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 领导首页加载
 * @author xing
 *
 */

public class LeaderHomePageAtion extends BaseAction{
	
	@Autowired
	private LeaderHomePageService leaderHomePageService;
	
	/** 跨域访问jsonp的回调函数 */
	private String jsonpCallBack;
	/**
	 * 加载地图信息 --手机端
	 */
	public void loadMap(){
		List<MapInfo> data = leaderHomePageService.mkData();
		CommonUtil.outputJson(data,jsonpCallBack);
	}
	/**
	 * 加载合同信息 -- 手机端
	 */
	public void loadContract(){
		ContractInfo contractInfo = leaderHomePageService.prjContractSum();
		CommonUtil.outputJson(contractInfo,jsonpCallBack);
	}
	/**
	 * 加载模块人日情况 --  手机端
	 */
	public void loadPersonDay(){
		List<PersonDayInfo> pDayInfos= leaderHomePageService.getpersonDayInfo();
		CommonUtil.outputJson(pDayInfos,jsonpCallBack);
	}
	/**
	 * 加载部门人日 -- 手机端
	 */
	public void loadDeptMonthPersonDayInfos(){
		List<DeptMonthPersonDayInfo> deptMonthPersonDayInfos= leaderHomePageService.getDeptMonthPersonDayInfos();
		CommonUtil.outputJson(deptMonthPersonDayInfos,jsonpCallBack);
	}
	
	/**
	 * 手机端请求
	 */
	public void getleaderHomePageInfo(){
		//跨域访问jsonp的回调函数
		jsonpCallBack = request.getParameter("jsoncallback");
		int actionFlag = Integer.valueOf(request.getParameter("actionFlag").toString());
		switch (actionFlag) {
		case 1:
			loadMap();
			break;
		case 2:
			loadContract();
			break;
		case 3:
			loadPersonDay();
			break;
		case 4:
			loadDeptMonthPersonDayInfos();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * Web端请求
	 */
	public void loadLeaderHomePageInfoForWeb(){
		LeaderHomeInfo leaderHomeInfo = new LeaderHomeInfo();
		List<MapInfo> mapInfos = leaderHomePageService.mkData();
		ContractInfo contractInfo = leaderHomePageService.prjContractSum();
		List<PersonDayInfo> pDayInfos= leaderHomePageService.getpersonDayInfo();
		//20150326 wtjiao 修改原来的逻辑，为了和成本控制模块保持一致，该模块修改为展现经过部门经理确认的，算在部门头上的人日消耗。而不再展示该部门下的人员项目消耗
		List<DeptMonthPersonDayInfo> deptMonthPDayInfos= leaderHomePageService.getDeptMonthPersonDayInfos();
		leaderHomeInfo.setMapInfos(mapInfos);
		leaderHomeInfo.setContractInfo(contractInfo);
		leaderHomeInfo.setpDayInfos(pDayInfos);
		leaderHomeInfo.setDeptMonthPDayInfos(deptMonthPDayInfos);
		try {
			JSONObject json = JSONObject.fromObject(leaderHomeInfo);	
			CommonUtil.ajaxPrint(json.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
