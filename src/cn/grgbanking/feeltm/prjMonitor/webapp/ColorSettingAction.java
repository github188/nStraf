package cn.grgbanking.feeltm.prjMonitor.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.prjMonitor.service.ColorSettingService;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

@SuppressWarnings("serial")
public class ColorSettingAction extends BaseAction {
	@Autowired
	private ProjectService prjService;
	@Autowired
	private ColorSettingService colorSettingService;
	private String[] deptColorId;
	private String[] deptId;
	private String[] deptColorVal;
	private String[] monFlag;
	private List<DeptColor> deptColorList;//部门颜色BEAN
	
	/**
	 * 获取颜色分配表
	 * 
	 * @author lping1 2014.9.16
	 */
	public String getColorSettings(){
		deptColorList = colorSettingService.getDeptColorList();
		return "colorSettingPage";
	}

	/**
	 * 保存颜色配置
	 * 
	 * @author lping1 2014.9.16
	 */
	public String saveColorSettings(){
		MsgBox msgBox;
		deptColorList = new ArrayList();
		try {
			for(int i=0;i<deptId.length;i++){
				DeptColor d = new DeptColor();
				d.setDeptColorId(deptColorId[i]);
				if(i<deptId.length){
					d.setDeptId(deptId[i]);					
				}
				if(i<deptColorVal.length){
					d.setDeptColorVal(deptColorVal[i].substring(1));//去掉#
				}
				if(i<monFlag.length){
					d.setMonFlag(monFlag[i]);
				}
				deptColorList.add(d);
			}

			colorSettingService.saveDeptColor(deptColorList);
			
		} catch (Exception e) {
			SysLog.error(request,"failed:java-ProjectAction-saveDeptColor()");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("颜色分配失败"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return getColorSettings();
	}

	/*---------------------getter setter---------------------------------*/


	public List<DeptColor> getDeptColorList() {
		return deptColorList;
	}

	public void setDeptColorList(List<DeptColor> deptColorList) {
		this.deptColorList = deptColorList;
	}



	public String[] getDeptId() {
		return deptId;
	}

	public void setDeptId(String[] deptId) {
		this.deptId = deptId;
	}



	public String[] getDeptColorVal() {
		return deptColorVal;
	}

	public void setDeptColorVal(String[] deptColorVal) {
		this.deptColorVal = deptColorVal;
	}

	public String[] getMonFlag() {
		return monFlag;
	}

	public void setMonFlag(String[] monFlag) {
		this.monFlag = monFlag;
	}

	public ProjectService getPrjService() {
		return prjService;
	}

	public void setPrjService(ProjectService prjService) {
		this.prjService = prjService;
	}

	public ColorSettingService getPrjMonService() {
		return colorSettingService;
	}

	public void setPrjMonService(ColorSettingService colorSettingService) {
		this.colorSettingService = colorSettingService;
	}

	public String[] getDeptColorId() {
		return deptColorId;
	}

	public void setDeptColorId(String[] deptColorId) {
		this.deptColorId = deptColorId;
	}




	

}
