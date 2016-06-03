package cn.grgbanking.feeltm.common4Wechat.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.grgbanking.feeltm.log.SysLog;

/**
 * 日志提醒SOCKET
 * @author zzhui1
 *
 */
public class SendMsgToWechatSocketer {

	/**
	 * wtjiao 2014年10月28日 下午1:30:20
	 * @param useridList 用户列表
	 * @param noticeDate 提醒时间
	 * @param msgType 1日志  2...
	 * @return 发送成功用户id的列表
	 */
	public List<String> sendMessageToWechat(List<String> useridList, String noticeDate,
			String msgType) {
		JSONObject outputJson = new JSONObject();
		try {
			outputJson.put("msgType", msgType);
			outputJson.put("noticeDate", noticeDate);
			outputJson.put("accounts", "?");
		} catch (JSONException e) {
			System.out.println("JSON异常:" + e.toString());
			SysLog.error("JSON异常:" + e.toString());
		}
		SysLog.info("启动socket向微信端发数据");
		SendMsgToWechatThread socketThread = new SendMsgToWechatThread(useridList,outputJson);
		 List<String> list = socketThread.executeThread();
		SysLog.info("结束socket向微信端发数据");
		StringBuilder userStr = new StringBuilder();
		for (String userName : list) {
			userStr.append(userName);
		}
		SysLog.info("发送提醒成功的用户为:" + userStr.toString());
		return list;
		
	}
	
	/**
	 * 测试用，可无视
	 */
	public static void main(String args[]) throws JSONException {
		List<String> useridList = new ArrayList<String>();
		for (int i = 0; i < 300; i++) {
			useridList.add("test" + (i+1));
		}
		
		String type = "1";
		String dateStr = "2014-02-28 23:59";//"2014-10-23 10:00";
		JSONObject outputJson = new JSONObject();
		outputJson.put("msgType", type);
		outputJson.put("noticeDate", dateStr);
		outputJson.put("accounts", "?");
		List<String> sendSuccussedUserList = new ArrayList<String>();
		SendMsgToWechatThread socketThread = new SendMsgToWechatThread(useridList,outputJson);
		sendSuccussedUserList = socketThread.executeThread();
	}
}
