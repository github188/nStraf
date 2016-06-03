package cn.grgbanking.feeltm.parseMsg.appMessage;

import org.apache.commons.lang.ArrayUtils;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.parseMsg.baseMessage.BaseMessage;

public class S2002Msg extends BaseMessage {

	private String picturename = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";// 64

	private String checkcode = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";// 32

	public void setPicturename(String picturename) {
		picturename += "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
		this.picturename = picturename.substring(0, 64);
	}

	public void setCheckcode(String checkcode) {
		checkcode += "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"; //32位
		this.checkcode = checkcode.substring(0, 32);
	}

	public byte[] packMessage() {
		// char[] msgHead = "01030070".toCharArray();
		char[] txcode = "2002".toCharArray();
		char[] length = "0155".toCharArray();
		char[] code1 = telno.toCharArray();
		char[] code2 = ttyp.toCharArray();
		char[] code3 = devid.toCharArray();
		char[] code4 = ogcd.toCharArray();
		char[] code5 = messageno.toCharArray();
		char[] code6 = picturename.toCharArray();
		char[] code7 = checkcode.toCharArray();

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
		msg = ArrayUtils.addAll(msg, code7);
		msg = ArrayUtils.add(msg, endSign);

		SysLog.info("send :" + new String((msg)));
		// System.out.println("send :"+new String((msg)));

		return charsToBytes(msg);
	}

}
