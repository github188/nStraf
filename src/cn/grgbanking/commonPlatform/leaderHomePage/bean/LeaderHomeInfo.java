package cn.grgbanking.commonPlatform.leaderHomePage.bean;

import java.util.List;
/**
 * web端领导首页加载页面所有信息
 * @author xing
 *
 */
public class LeaderHomeInfo {

	private List<MapInfo> mapInfos;//地图信息
	private ContractInfo contractInfo;//合同信息
	private List<PersonDayInfo> pDayInfos;//模块人日信息
	private List<DeptMonthPersonDayInfo> deptMonthPDayInfos;//部门人日情况
	
	
	public List<MapInfo> getMapInfos() {
		return mapInfos;
	}
	public void setMapInfos(List<MapInfo> mapInfos) {
		this.mapInfos = mapInfos;
	}
	public ContractInfo getContractInfo() {
		return contractInfo;
	}
	public void setContractInfo(ContractInfo contractInfo) {
		this.contractInfo = contractInfo;
	}
	public List<PersonDayInfo> getpDayInfos() {
		return pDayInfos;
	}
	public void setpDayInfos(List<PersonDayInfo> pDayInfos) {
		this.pDayInfos = pDayInfos;
	}
	public List<DeptMonthPersonDayInfo> getDeptMonthPDayInfos() {
		return deptMonthPDayInfos;
	}
	public void setDeptMonthPDayInfos(
			List<DeptMonthPersonDayInfo> deptMonthPDayInfos) {
		this.deptMonthPDayInfos = deptMonthPDayInfos;
	}
	
	
}
