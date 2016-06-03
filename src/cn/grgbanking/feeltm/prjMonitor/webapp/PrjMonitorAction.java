package cn.grgbanking.feeltm.prjMonitor.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.prjMonitor.bean.StaffBean;
import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.prjMonitor.service.PrjMonitorService;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.framework.webapp.BaseAction;

@SuppressWarnings("serial")
public class PrjMonitorAction extends BaseAction {
	@Autowired
	private ProjectService prjService;
	@Autowired
	private PrjMonitorService prjMonService;
	private Map staffAllocMap;
	private List<StaffBean> restStaffList;//闲置人员LIST
	private List<DeptColor> monDeptList;//被监控的部门
	private List<List<String>> countList ;//部门统计数据
	/**
	 * 获取人力资源分布情况
	 * 
	 * @author lping1 2014.9.2
	 */
	@SuppressWarnings("rawtypes")
	public String staffAllocMon() {
		try{
			
			restStaffList = prjMonService.getRestStaff();
			staffAllocMap = prjMonService.getStaffAllocMap();
			monDeptList = prjMonService.getMonDeptColor();
			countList =new ArrayList();
			
			if(monDeptList!=null && monDeptList.size()>0){
				for(DeptColor d : monDeptList){
					if(d.getDeptId()!=null && !"".equals(d.getDeptId())){
						List list = prjMonService.getCountsByDeptId(d.getDeptId());
						if(list!=null && list.size()>0){
							countList.add(list);
						}
						
					}
					
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "staffAllocPage";
	}


	/*---------------------getter setter---------------------------------*/
	public Map getStaffAllocMap() {
		return staffAllocMap;
	}

	public void setStaffAllocMap(Map staffAllocMap) {
		this.staffAllocMap = staffAllocMap;
	}








	public List<StaffBean> getRestStaffList() {
		return restStaffList;
	}


	public void setRestStaffList(List<StaffBean> restStaffList) {
		this.restStaffList = restStaffList;
	}


	public ProjectService getPrjService() {
		return prjService;
	}

	public void setPrjService(ProjectService prjService) {
		this.prjService = prjService;
	}

	public PrjMonitorService getPrjMonService() {
		return prjMonService;
	}

	public void setPrjMonService(PrjMonitorService prjMonService) {
		this.prjMonService = prjMonService;
	}



	public List<DeptColor> getMonDeptList() {
		return monDeptList;
	}


	public void setMonDeptList(List<DeptColor> monDeptList) {
		this.monDeptList = monDeptList;
	}


	public List<List<String>> getCountList() {
		return countList;
	}


	public void setCountList(List<List<String>> countList) {
		this.countList = countList;
	}

	
}
