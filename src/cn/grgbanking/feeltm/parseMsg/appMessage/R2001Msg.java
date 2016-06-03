package cn.grgbanking.feeltm.parseMsg.appMessage;

import org.apache.commons.lang.ArrayUtils;
import cn.grgbanking.feeltm.log.SysLog;

import cn.grgbanking.feeltm.parseMsg.baseMessage.BaseMessage;

public class R2001Msg extends BaseMessage {

	public void unpackMessage(byte[] msg) {

		String msgHead = new String(bytesToChars(msg));
		// System.out.println(msgHead.length()+":msg:"+msgHead);
		SysLog.info("received: " + msgHead);
		int msgLength = Integer.parseInt(msgHead.substring(4, 8));

		if (msgLength != (msg.length - 8)) {
			System.out.println("The pack message length is not right !");
			return;
		}

		char[] message = bytesToChars(msg);

		message = ArrayUtils.subarray(message, 8, msgHead.length());

		telno = new String(ArrayUtils.subarray(message, 0, 12));

		message = ArrayUtils.subarray(message, 13, message.length);

		ttyp = new String(ArrayUtils.subarray(message, 0, 8));

		message = ArrayUtils.subarray(message, 9, message.length);

		devid = new String(ArrayUtils.subarray(message, 0, 8));

		message = ArrayUtils.subarray(message, 9, message.length);

		ogcd = new String(ArrayUtils.subarray(message, 0, 12));

		message = ArrayUtils.subarray(message, 13, message.length);

		messageno = new String(ArrayUtils.subarray(message, 0, 12));

		message = ArrayUtils.subarray(message, 13, message.length);

		retcode = new String(ArrayUtils.subarray(message, 0, 12));

	}

	public char[] packMessage() {
		// char[] msgHead = "01030070".toCharArray();
		char[] txcode = "2001".toCharArray();
		char[] length = "0070".toCharArray();
		char[] code1 = telno.toCharArray();
		char[] code2 = ttyp.toCharArray();
		char[] code3 = devid.toCharArray();
		char[] code4 = ogcd.toCharArray();
		char[] code5 = messageno.toCharArray();

		char[] code6 = retcode.toCharArray();

		char[] msg = ArrayUtils.addAll(txcode, length);

		msg = ArrayUtils.addAll(msg, code1);
		msg = ArrayUtils.add(msg, endSign);
		msg = ArrayUtils.addAll(msg, code2);
		msg = ArrayUtils.add(msg, endSign);
		msg = ArrayUtils.addAll(msg, code3);
		msg = ArrayUtils.add(msg, endSign);
		msg = ArrayUtils.addAll(msg, code4);
		msg = ArrayUtils.add(msg, endSign);
		msg = ArrayUtils.addAll(msg, code5);
		msg = ArrayUtils.add(msg, endSign);
		msg = ArrayUtils.addAll(msg, code6);
		msg = ArrayUtils.add(msg, endSign);

		SysLog.info("send :" + new String((msg)));

		return msg;
	}

}
