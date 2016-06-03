package cn.grgbanking.feeltm.leave.webapp;


import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.leave.service.LeaveRecordService;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

public class LeaveRecordAction extends BaseAction{
	@Autowired
	private LeaveRecordService leaveRecordService;
	
	/**
	 * 分析月底汇总数据
	 * 按给出的时间段进行分析，如果已经存在给出的时间段，则提醒数据已经分析过
	 * @return
	 */
	public String countAllAttendanceData(){
		try{
			leaveRecordService.getLeaveRecord();
			leaveRecordService.execProcedure();
		}catch(Exception e){
			e.printStackTrace();
		}
		MsgBox msgBox = new MsgBox(request,"统计完成");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
}
