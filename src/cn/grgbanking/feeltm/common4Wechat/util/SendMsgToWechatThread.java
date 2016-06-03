package cn.grgbanking.feeltm.common4Wechat.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.log.SysLog;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 日志提醒接口用<br>
 * @version 说明：由于执行线程后要返回一些值,采用Callable的方式的线程<br>
 * 			可以实现或者直接普通的非线程类即可，目前采用后者，<br>
 * 			所以将原线程类改为了非线程类本类名没有变
 * @author zzhui1
 * 
 */
public class SendMsgToWechatThread {
	//private String ipAdd = "220.243.6.56";
	private String ipAdd = "221.5.109.28";
	private int socketPortNo = 8888;
	/** 消息模版 */
	private JSONObject outputJson;
	/** 消息提醒用户列表 */
	private List<String> accountList;
	/** 消息列表 */
	private List<String> jsonMsgList;
	int sendTimes= 0;

	/**
	 * 构造函数
	 * @param useridList 消息提醒用户列表
	 * @param outputJson JSON消息模版
	 */
	public SendMsgToWechatThread(List<String> useridList, JSONObject outputJson) {
		String socketIP = Configure.getProperty("socketIP");
		String socketPortNo = Configure.getProperty("socketPortNo");
		if (StringUtils.isNotBlank(socketIP)) {
			this.ipAdd = socketIP;
		}
		if (StringUtils.isNotBlank(socketPortNo)) {
			this.socketPortNo = Integer.parseInt(socketPortNo);
		}
		this.outputJson = outputJson;
		this.accountList = useridList;
	}
	
	public List<String> executeThread() {
		// 将用户列表分割到若干个消息
		this.jsonMsgList = getMsgList(accountList, outputJson);
		List<String> sendSuccussedUserList = send();
		return sendSuccussedUserList;
	}

	/**
	 * 将用户列表分割到若干个消息<br>
	 * 一旦组装的报文超过25600，需要分拆成多条报文发送
	 * @param useridList 消息提醒用户列表
	 * @param outputJson JSON消息模版
	 * @return 消息列表
	 */
	private List<String> getMsgList(List<String> useridList,
			JSONObject outputJson) {
		String fixedMsg = outputJson.toString();
		List<String> list = useridList;
		String encode = Common4Wechat.CHARSET_NAME;
		// 一个字段的最大长度
		int fieldsL = 0;
		int maxTempL = 0;
		try {
			//得到最大的字符数长度
			for (int i = 0; i < useridList.size(); i++) {
				fieldsL = useridList.get(i).getBytes(encode).length;
				if (fieldsL > maxTempL) {
					maxTempL = fieldsL;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		fieldsL = maxTempL;
		byte[] sendMsgBytes;
		// 固定消息的长度
		int fixedMsgLength = 0;
		try {
			sendMsgBytes = fixedMsg.getBytes(encode);
			fixedMsgLength = sendMsgBytes.length - 1;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 用户列表的长度
		int userL = list.size() * fieldsL;
		// 一个字符串中剩余用来放用户名的长度
		int remainL = Common4Wechat.OUTPUT_SCREAM_MAX_LEANGTH - fixedMsgLength + 1;
		int accountArrayLength = remainL;
		// 取list中的fieldCount个出来拼成一组
		int fieldCount = (accountArrayLength % (fieldsL + 1) == 0) ? accountArrayLength
				/ (fieldsL + 1) : accountArrayLength / (fieldsL + 1) + 1;
		int arrayL = fieldCount * (fieldsL + 1);
		// 需要拼的组数
		int arraysCount = (userL % arrayL == 0) ? (userL / arrayL) : ((userL / arrayL) + 1);
		StringBuffer strTemp = new StringBuffer();
		List<String> msgList = new ArrayList<String>();
		String fullMsg = "";
		
		//分割组装成若干个消息列表，供发送使用
		for (int i = 0; i < arraysCount; i++) {
			strTemp = new StringBuffer();
			for (int j = i * fieldCount; j < fieldCount * (i + 1); j++) {
				if (j == list.size()) {
					break;
				}
				strTemp.append(Common4Wechat.SOCKET_SEPARATE + list.get(j));
			}
			fullMsg = fixedMsg.replace(Common4Wechat.REPLACE_STR, strTemp.toString().substring(1));
			msgList.add(fullMsg);
		}
		//消息列表
		return msgList;
	}

	/**
	 * 执行向服务端发送消息
	 */
	private List<String> send() {
		Socket socket = null;
		List<String> sendSuccussedUserList = new ArrayList<String>();
		try {
			SysLog.info("发送开始：" + new Date());
			// 发送报文
			for (String jsonMsg : this.jsonMsgList) {
				for (int i = 0; i < Common4Wechat.SEND_TIMES; i++) {
					try {
						socket = new Socket(ipAdd, socketPortNo);
					} catch (Exception e) {
						//如果连接不上，等待后重新连接，再重发
						continue;
					}
					socket.setSoTimeout(Common4Wechat.TIMEOUT_SECOND);
					//发送
					if (sendMsg(jsonMsg, socket)) {
						sendSuccussedUserList.addAll(getSuccussedUserList(jsonMsg));
						break;
					}
				}
			}
			return sendSuccussedUserList;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return sendSuccussedUserList;
	}
	
	/**
	 * 将发送成功的用户列表放在List里面
	 * @param jsonMsg json格式的字符串
	 * @return 用户列表List
	 */
	private List<String> getSuccussedUserList(String jsonMsg){
		JSONObject parms;
		List<String> userList = new ArrayList<String>();
		try {
			parms = new JSONObject(jsonMsg);
			String sendSuccussedUser = (String)parms.get(Common4Wechat.SOCKET_ACCOUNTS);
			String[] users = sendSuccussedUser.split("\\|");
			userList = Arrays.asList(users);
			return userList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * 发送包到服务端
	 * @param jsonMsg
	 * @return 发送成功与否
	 */
	private boolean sendMsg(String jsonMsg, Socket socket) {
		
		SysLog.info("requestData:"+jsonMsg);
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		boolean ret = true;
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			// 发送到服务端
			outputStream.write(jsonMsg.getBytes(Common4Wechat.CHARSET_NAME));
			outputStream.flush();
			// 接收服务端的信息
			byte[] resultMsgBytes = new byte[Common4Wechat.SOCKET_RETURN_MSG
					.length()];
			int length = inputStream.read(resultMsgBytes);
			if (length != -1) {
				byteArrayOutputStream = new ByteArrayOutputStream();
				byteArrayOutputStream.write(resultMsgBytes, 0, length);

				// 接收的结果报文，成功为“SUCCESS”，若未接受到该字符串，说明出现网络等异常，需要重发，重发3次依旧不成功则不再发送
				String resultMsg = new String(
						byteArrayOutputStream.toByteArray(),
						Common4Wechat.CHARSET_NAME);
				sendTimes++;
				SysLog.info("返回结果，第【" + sendTimes + "】个：" + resultMsg);
				SysLog.info("发送结束：" + new Date());
				byteArrayOutputStream.flush();
				if (resultMsg.equals(Common4Wechat.SOCKET_RETURN_MSG)) {
					ret = true;
				} else {
					ret =  false;
				}
			}else{
				ret = false;
			}
			return ret;
		} catch (Exception e) {

		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (inputStream != null)
					inputStream.close();
				if (byteArrayOutputStream != null)
					byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
