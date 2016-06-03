package cn.grgbanking.feeltm.prjMonitor.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.prjMonitor.dao.ColorSettingDao;
import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.framework.service.BaseService;

@Service
public class ColorSettingService extends BaseService {
	@Autowired
	private ProjectDao projectGroupDao;
	@Autowired
	private ColorSettingDao colorSettingDao;
	@Autowired
	private StaffInfoService staffInfoService;
	
	/**
	 * 获取颜色分配表
	 * 
	 * @author lping1 2014.9.16
	 */
	public   List<DeptColor> getDeptColorList(){
		List<DeptColor> list = 	colorSettingDao.getDeptColorList();
		Map map=BusnDataDir.getMapKeyValue("staffManager.department");
		Set<Entry> entrySet = map.entrySet();
		//没有记录时把数据字典中所有部门加到列表
		if(list!=null && list.size()==0){
			for(Iterator<Entry> it = entrySet.iterator();it.hasNext();){	
				Entry e = it.next();
				DeptColor deptColor = new DeptColor();
				deptColor.setDeptId((String)e.getKey());
				deptColor.setDeptName((String)e.getValue());
				deptColor.setMonFlag("0");
				list.add(deptColor);
			}
		}else{
			//如果新增了部门，则把新部门加到配置面板
			List temp = new ArrayList();
			if(list!=null && list.size() < map.keySet().size()){
				for(Entry e : entrySet){
					boolean flag = true;//flag为true表示e.getKey()是新部门
					for(DeptColor d : list){
						String deptId = d.getDeptColorId();
						if(deptId!=null && deptId.equals((String)e.getKey())){
							flag = false;
						}
					}
					if(flag){
						DeptColor deptColor = new DeptColor();
						deptColor.setDeptId((String)e.getKey());
						deptColor.setDeptName((String)e.getValue());
						deptColor.setMonFlag("0");
						temp.add(deptColor);
					}
				}
			}
			for(Iterator<DeptColor> it = list.iterator();it.hasNext();){	
				DeptColor d = it.next();
				d.setDeptName(BusnDataDir.getValue(map, d.getDeptId()));
				temp.add(d);
			}
			list = temp;
		}
		return list;
	}
	
	
	/**
	 * 保存部门颜色配置
	 * 
	 * @author lping1 2014.9.16
	 */
	public void saveDeptColor(List<DeptColor> deptColorList ){
		for(Iterator<DeptColor> it = deptColorList.iterator();it.hasNext();){				
			saveOrUpdate(it.next());
		}
	}
	
	/**
	 * 保存对象
	 * @param group
	 * lhyan3
	 * 2014年6月12日
	 */
	public void saveOrUpdate(DeptColor d) {
		if(d.getDeptColorId()==null || "".equals(d.getDeptColorId())){
			colorSettingDao.addObject(d);
		}else{
			colorSettingDao.updateObject(d);
		}
	}
}
