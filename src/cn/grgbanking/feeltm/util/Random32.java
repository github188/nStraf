package cn.grgbanking.feeltm.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Random32 {
	public final static String MD5(String s) {

		char hexDigits[] = {

		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };

		try {

			byte[] strTemp = s.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");

			mdTemp.update(strTemp);

			byte[] md = mdTemp.digest();

			int j = md.length;

			char str[] = new char[j * 2];

			int k = 0;

			for (int i = 0; i < j; i++) {

				byte byte0 = md[i];

				str[k++] = hexDigits[byte0 >>> 4 & 0x0f];

				str[k++] = hexDigits[byte0 & 0x0f];

			}

			return new String(str);

		} catch (Exception e) {

			return null;

		}

	}

	// 测试

	public static String mainMethod() {
		Date dd = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String tt = sdf.format(dd);
		tt = tt + Math.random();// 当前时间加一随机数
		// 产生不能永远不会重复的32位字符
		// System.out.print(MD5(tt));
		String random32 = MD5(tt);
		return random32;
	}

}