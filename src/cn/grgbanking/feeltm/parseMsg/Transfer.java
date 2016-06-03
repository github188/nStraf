package cn.grgbanking.feeltm.parseMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.lang.ArrayUtils;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.parseMsg.baseMessage.BaseMessage;
import cn.grgbanking.feeltm.util.Des3Safe;

public class Transfer {
	public String ip = (String) BusnDataDir.getMap(
			"systemConfig.systemParamConfig").get("ip");
	public int port = Integer.parseInt(String.valueOf(BusnDataDir.getMap(
			"systemConfig.systemParamConfig").get("port")));
	public int timeOut = Integer.parseInt(String.valueOf(BusnDataDir.getMap(
			"systemConfig.systemParamConfig").get("timeout")));

	public String deskey = (String) BusnDataDir.getMap(
			"systemConfig.systemParamConfig").get("deskey");
	public String deskeyExist = (String) BusnDataDir.getMap(
			"systemConfig.systemParamConfig").get("deskeyExist");

	public byte[] sendPack(byte[] sendpack) throws UnknownHostException,
			IOException {
		return this.sendPack3DesSafeContent(sendpack);

	}

	public byte[] sendPack3DesSafeContent(byte[] sendpack)
			throws UnknownHostException, IOException {

		char tempKey[] = new char[16];
		tempKey = vStrToBin(deskey.toCharArray(),
				deskey.toCharArray().length / 2);
		Des3Safe des3Safe = new Des3Safe();
		char desChar[] = tempKey;
		byte tempsendpack[];
		if (deskeyExist != null
				&& (deskeyExist.equals("y") || deskeyExist.equals("Y"))) {
			byte[] sendSrclength = new byte[4];
			char sendpackchar[] = new char[sendpack.length];
			for (int i = 0; i < sendpack.length; i++) {
				sendpackchar[i] = (char) sendpack[i];
			}
			// 提取交易码，和原始报文长度
			for (int i = 0; i < 4; i++) {
				sendSrclength[i] = sendpack[i + 4];
			}

			int sendlength = 0;
			sendlength = Integer.parseInt(new String(BaseMessage
					.bytesToChars(sendSrclength)));
			if (sendlength % 8 != 0) {
				int templength = sendlength % 8;
				sendlength = sendlength + 8 - templength + 8;
			} else {
				sendlength = sendlength + 8;
			}
			tempsendpack = new byte[sendlength];
			char[] desdatesendpack = new char[sendlength];
			for (int i = 0; i < sendpack.length; i++) {
				desdatesendpack[i] = sendpackchar[i];
			}
			int count = 0;
			count = sendlength / 8;
			for (int jj = 1; jj < count; jj++) {
				char tempSrc[] = new char[8];

				char tempRet[] = new char[8];
				for (int i = 0; i < 8; i++) {
					tempSrc[i] = desdatesendpack[jj * 8 + i];
				}
				tempRet = des3Safe.v3DES('E', desChar, tempSrc);
				for (int i = 0; i < 8; i++) {
					desdatesendpack[jj * 8 + i] = tempRet[i];
				}
			}
			for (int i = 0; i < desdatesendpack.length; i++) {
				tempsendpack[i] = (byte) desdatesendpack[i];
			}
		} else {
			tempsendpack = sendpack;
		}

		Socket theSocket = new Socket(ip, port);
		theSocket.setSoTimeout(timeOut);
		InputStream serverReturn = theSocket.getInputStream();
		OutputStream sendToServer = theSocket.getOutputStream();
		sendToServer.write(tempsendpack);
		sendToServer.flush();
		int length = 0;
		byte[] returnpack = new byte[1024];
		try {
			length = serverReturn.read(returnpack);
		} catch (InterruptedIOException eee) {// 捕捉超时异常
			sendToServer.close();
			serverReturn.close();
			theSocket.close();
			return null;

		} catch (Exception ee) {
			sendToServer.close();
			serverReturn.close();
			theSocket.close();
			return null;
		}

		byte[] packlengths = new byte[4];
		try {
			returnpack = ArrayUtils.subarray(returnpack, 0, length);
			for (int i = 0; i < 4; i++) {
				packlengths[i] = returnpack[i + 4];
			}
		} catch (Exception e) {
			sendToServer.close();
			serverReturn.close();
			theSocket.close();
			return null;
		}
		// 解密
		if (deskeyExist != null
				&& (deskeyExist.equals("y") || deskeyExist.equals("Y"))) {
			int packlength = Integer.parseInt(new String(BaseMessage
					.bytesToChars(packlengths)));
			int templength = 0;// 当位数与8不为0时，要补齐8的整数倍。要解密的位数

			if (packlength % 8 != 0) {
				int temp = packlength % 8;
				templength = packlength + 8 - temp + 8;

			} else {
				templength = packlength;
			}
			int count = 0;
			count = templength / 8;

			for (int jj = 1; jj < count; jj++) {
				char tempSrc[] = new char[8];

				char tempRet1[] = new char[8];
				byte tempRetbyte2[] = new byte[8];
				for (int i = 0; i < 8; i++) {

					tempSrc[i] = (char) returnpack[jj * 8 + i];

				}
				tempRet1 = des3Safe.v3DES('D', desChar, tempSrc);
				for (int i = 0; i < tempRet1.length; i++) {
					tempRetbyte2[i] = (byte) tempRet1[i];
				}

				for (int i = 0; i < 8; i++) {
					returnpack[jj * 8 + i] = tempRetbyte2[i];
				}
			}

		}
		try {
			if ((BaseMessage.bytesToChars(returnpack)).length > 8) {

				length = Integer.parseInt(new String(BaseMessage
						.bytesToChars(returnpack)).substring(4, 8)) + 8;
				if (returnpack.length >= length) {
					byte[] temppack = new byte[length];

					System.arraycopy(returnpack, 0, temppack, 0, (length - 1));
					returnpack = temppack;

				}
			}
			sendToServer.close();
			serverReturn.close();
			theSocket.close();
			return returnpack;
		} catch (NumberFormatException e) {

			sendToServer.close();
			serverReturn.close();
			theSocket.close();
			e.printStackTrace();
			return null;
		}

	}

	public String byte2hex(char[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs;
		}
		return hs.toUpperCase();
	}

	public char AtoB(byte c) {
		if ((c >= '0') && (c <= '9'))
			return ((char) (c - '0'));
		if ((c >= 'A') && (c <= 'F'))
			return ((char) (c - 'A' + 10));
		if ((c >= 'a') && (c <= 'f'))
			return ((char) (c - 'a' + 10));

		return (0);
	} // char AtoB(char c)

	public char[] vStrToBin(char p_pcSourceStr[], int p_iBinLen) {
		char p_pcDestBin[] = new char[p_iBinLen];
		int i = 0;
		int j = 0;
		while (p_iBinLen > 0) {
			if ('\0' == p_pcSourceStr[i])
				return p_pcDestBin;
			if (p_pcSourceStr[i + 1] == '\0') {
				p_pcDestBin[j] = (char) ((byte) AtoB((byte) ((byte) (p_pcSourceStr[i]) * 16)));
				return p_pcDestBin;
			}
			;

			p_pcDestBin[j] = (char) ((byte) AtoB((byte) p_pcSourceStr[i]) * 16 + (byte) AtoB((byte) (p_pcSourceStr[i + 1])));
			j++;
			i += 2;
			p_iBinLen--;

		} // while(p_iLen>0)
		return p_pcDestBin;
	}

}
