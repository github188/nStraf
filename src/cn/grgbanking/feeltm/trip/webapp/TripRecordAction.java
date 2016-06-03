package cn.grgbanking.feeltm.trip.webapp;


import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.trip.service.TripRecordService;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

public class TripRecordAction extends BaseAction{
	@Autowired
	private TripRecordService tripRecordService;
	
	/**
	 * 分析月底汇总数据
	 * 按给出的时间段进行分析，如果已经存在给出的时间段，则提醒数据已经分析过
	 * @return
	 */
	public String countAllAttendanceData(){
		try{
			tripRecordService.getTripRecord();
//			tripRecordService.execProcedure();
		}catch(Exception e){
			e.printStackTrace();
		}
		MsgBox msgBox = new MsgBox(request,"统计完成");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
}
