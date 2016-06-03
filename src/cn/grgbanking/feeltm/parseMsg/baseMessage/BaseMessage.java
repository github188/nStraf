package cn.grgbanking.feeltm.parseMsg.baseMessage;

import org.apache.commons.lang.ArrayUtils;

public class BaseMessage {
	protected char endSign = '\0';

	protected String telno = "\0\0\0\0\0\0\0\0\0\0\0\0"; // 12

	protected String ttyp = "\0\0\0\0\0\0\0\0";// 8

	protected String devid = "\0\0\0\0\0\0\0\0";// 8

	protected String ogcd = "\0\0\0\0\0\0\0\0\0\0\0\0";// 12

	protected String messageno = "\0\0\0\0\0\0\0\0\0\0\0\0";// 12

	protected String retcode = "\0\0\0\0\0\0\0\0\0\0\0\0";// 12

	public void setTelno(String telno) {
		telno += "\0\0\0\0\0\0\0\0\0\0\0\0";
		this.telno = telno.substring(0, 12);
	}

	public void setTtyp(String ttyp) {
		ttyp += "\0\0\0\0\0\0\0\0";
		this.ttyp = ttyp.substring(0, 8);
	}

	public void setDevid(String devid) {
		devid += "\0\0\0\0\0\0\0\0";
		this.devid = devid.substring(0, 8);
	}

	public void setOgcd(String ogcd) {
		ogcd += "\0\0\0\0\0\0\0\0\0\0\0\0";
		this.ogcd = ogcd.substring(0, 12);
	}

	public void setMessageno(String messageno) {
		messageno += "\0\0\0\0\0\0\0\0\0\0\0\0";
		this.messageno = messageno.substring(0, 12);
	}

	public void setRetcode(String retcode) {
		retcode += "\0\0\0\0\0\0\0\0\0\0\0\0";
		this.retcode = retcode.substring(0, 12);
	}

	public String getTelno() {
		return getValue(telno);
	}

	public String getTtyp() {
		return getValue(ttyp);
	}

	public String getDevid() {
		return getValue(devid);
	}

	public String getOgcd() {
		return getValue(ogcd);
	}

	public String getMessageno() {
		return getValue(messageno);
	}

	public String getRetcode() {
		return getValue(retcode);
	}

	// ------------------------------------------------------------------------------
	public static final char[] bytesToChars(byte[] p_abyData) {
		return bytesToChars(p_abyData, p_abyData.length);
	}

	// ------------------------------------------------------------------------------
	protected static final char[] bytesToChars(byte[] p_abyData, int p_iLength) {
		char[] l_acMsgChars = new char[p_iLength];
		for (int j = 0; j < p_iLength; j++) {
			if (p_abyData[j] < 0) {
				l_acMsgChars[j] = (char) (256 + p_abyData[j]);
			} else {
				l_acMsgChars[j] = (char) p_abyData[j];
			}
		}
		return l_acMsgChars;
	}

	// ------------------------------------------------------------------------------
	public static byte[] charsToBytes(char[] p_acData) {
		return charsToBytes(p_acData, p_acData.length);
	}

	// ------------------------------------------------------------------------------
	protected static byte[] charsToBytes(char[] p_acData, int p_iLength) {
		byte[] l_abyMsgBytes = new byte[p_iLength];
		for (int j = 0; j < p_iLength; j++) {
			l_abyMsgBytes[j] = (byte) p_acData[j];
		}
		return l_abyMsgBytes;
	}

	// --------------------------------------------------------------------------------
	protected String getValue(String str) {
		int i;
		char[] s = str.toCharArray();
		for (i = 0; i < s.length; i++) {
			if (s[i] == '\0') {
				break;
			}
		}
		if (i == 0) {
			str = "";
		} else {
			s = ArrayUtils.subarray(s, 0, i);
			str = String.valueOf(s);
		}
		return str;
	}

	protected String getBlank(int i) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < i; j++) {
			sb.append("\0");
		}
		return sb.toString();
	}

}