package cn.grgbanking.feeltm.um.webapp;

import java.text.ParseException;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.HashUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;


@SuppressWarnings("serial")
public class PwdSaveAction extends BaseAction{
	private SysUserInfoService sysUserInfoService;

	public String pwdSave() {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		
		String password=HashUtil.hash(request.getParameter("password"));
		String newPassword=HashUtil.hash(request.getParameter("newPassword"));
		
		int returnid=sysUserInfoService.modifyPassword(loginUser.getUserid(),password,newPassword);
		try{
			if(returnid==1)
			{
				SysLog.operLog(request, Constants.OPER_PWRESET_VALUE, loginUser.getUsername());//记录日志
				SysLog.info("User:"+loginUser.getUserid()+" modify the password");
				MsgBox msg = new MsgBox(request, getText("modifypassword.success"), "um");
				SysLog.error(request,"failed to change password.");
				msg.setButtonType(MsgBox.BUTTON_RETURN);
			}
			else
			{
				MsgBox msg = new MsgBox(request, getText("modifypassword.fail"), "um");
				msg.setButtonType(MsgBox.BUTTON_RETURN);
				SysLog.info(request,"change password fail.");
			}
			return "success";
		}catch(ParseException pex)
		{
			SysLog.info("User:"+loginUser.getUserid()+" modify the password");
			SysLog.error(request,"failed to change password.");
			pex.printStackTrace();
			return "fail";
		}
		
	}

	public SysUserInfoService getsysUserInfoService() {
		return sysUserInfoService;
	}

	public void setsysUserInfoService(
			SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}


}
