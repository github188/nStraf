package cn.grgbanking.feeltm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import cn.grgbanking.feeltm.log.SysLog;

@SuppressWarnings("unchecked")
public class StringUtil {
    //用来转化过程质量，项目质量
	public static Map<Integer,String> point2Level;
	
	//用来转化进度
	public static Map<Integer,String> point2Progress;
	
	static{
		point2Level=new HashMap<Integer, String>();
		point2Level.put(5, "优");
		point2Level.put(4, "良");
		point2Level.put(3, "中");
		point2Level.put(2, "较差");
		point2Level.put(1, "差");
		
		point2Progress=new HashMap<Integer,String>();
		point2Progress.put(5, "提前>=2周");
		point2Progress.put(4, "提前>=1周");
		point2Progress.put(3, "正常");
		point2Progress.put(2, "延迟>=1周");
		point2Progress.put(1, "延迟>=2周");
		
	}
	
	public static List<String> getAllMonth(String start,String end){
		List<String> allMonth=new ArrayList<String>();
		SimpleDateFormat mat=new SimpleDateFormat("yyyy-MM");
		Calendar startCal=Calendar.getInstance();	
		try {
			Date startMonth=mat.parse(start);
			Date endMonth=mat.parse(end);
			startCal.setTime(startMonth);
			while(startMonth.compareTo(endMonth)<=0){
				allMonth.add(mat.format(startMonth));
				startMonth.setMonth(startMonth.getMonth()+1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return allMonth;
	}
	
	public static int toInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			SysLog.error("error in (StringUtil.java-toInt())");
			return def;
		}
	}

	public static long toLong(String s, long def) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return def;
		}
	}

	public static final String replace(String line, String oldString,
			String newString) {
		if (line == null || oldString == null || newString == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	// ------------------------------------------------------------------------------

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null || oldString == null || newString == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	// ------------------------------------------------------------------------------

	public static final String[] toArray(String sData, String symbol) {
		if (sData == null || symbol == null) {
			return null;
		}
		if (sData.length() == 0)
			return new String[] { "" };

		int sbLen = symbol.length();

		if (sData.length() < sbLen
				|| !sData.substring(sData.length() - sbLen).equals(symbol))
			sData = sData + symbol;

		String sRst[] = null;

		int j = 0;
		int i = sData.indexOf(symbol);
		while (i > -1) {
			j++;
			i += sbLen;
			i = sData.indexOf(symbol, i);
		}
		sRst = new String[j];
		j = 0;
		int k = 0;
		i = sData.indexOf(symbol);
		while (i > -1) {
			sRst[j++] = sData.substring(k, i);
			k = i + sbLen;
			i = sData.indexOf(symbol, k);
		}
		return sRst;
	}

	// ------------------------------------------------------------------------------

	public static String repeat(String s, int times) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < times; i++)
			sb.append(s);
		return sb.toString();
	}

	public static String getAtmForReport(BigDecimal amt) {
		if (amt == null) {
			amt = new BigDecimal(0);
		}
		StringBuffer sb = new StringBuffer("");
		String s = amt.toString();
		String s1 = null;
		int a = 0;
		int len = amt.toString().length();
		if (s.substring(0, 1).equals("-")) {
			s1 = s.substring(1, len);

			int len1 = len - 1;
			if (len1 % 3 == 0) {
				a = len1 / 3 - 1;
			} else {
				a = len1 / 3;
			}
			sb.append("-");
			for (int i = 0; i <= a; i++) {
				int j = 0;
				j = len1 - (a + 1 - i) * 3;
				if (j < 0) {
					j = 0;
				}
				sb.append(s1.substring(j, len1 - (a - i) * 3));
				if (i != a) {
					sb.append(",");
				}
			}
		} else {
			if (len % 3 == 0) {
				a = len / 3 - 1;
			} else {
				a = len / 3;
			}

			for (int i = 0; i <= a; i++) {
				int j = 0;
				j = len - (a + 1 - i) * 3;
				if (j < 0) {
					j = 0;
				}

				sb.append(s.substring(j, len - (a - i) * 3));
				if (i != a) {
					sb.append(",");
				}

			}
		}
		sb.append(".00");
		return sb.toString();
	}

	// ------------------------------------------------------------------------------

	public static String appendLeft(String src, char ch, int byteLen) {
		StringBuffer sb = new StringBuffer();
		byteLen = byteLen - src.getBytes().length;
		for (int i = 0; i < byteLen; i++)
			sb.append(ch);
		sb.append(src);
		return sb.toString();
	}

	// ------------------------------------------------------------------------------
	public static String appendRight(String src, char ch, int byteLen) {
		StringBuffer sb = new StringBuffer();
		sb.append(src);
		byteLen = byteLen - src.getBytes().length;
		for (int i = 0; i < byteLen; i++)
			sb.append(ch);
		return sb.toString();
	}

	// ------------------------------------------------------------------------------

	public static String truncate(String src, int maxLen) {
		if (src == null)
			return "";
		if (src.length() >= maxLen) {
			return src.substring(0, maxLen);
		}
		return src;
	}

	// ------------------------------------------------------------------------------

	public static String fitLabel(String label, int maxLen) {
		if (label.length() >= maxLen)
			return label.substring(0, maxLen) + "..";

		return label;
	}

	// ------------------------------------------------------------------------------

	public static final boolean isNumber(String s) {
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] < '0' || ch[i] > '9')
				return false;
		}
		return true;
	}

	// ------------------------------------------------------------------------------

	public static final String formatDecimal(String strDecimal, String format) {
		java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
		return nf.format(Double.parseDouble(strDecimal));
	}

	public static final String formatDecimal(double decimal, String format) {
		java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
		return nf.format(decimal);
	}

	// ------------------------------------------------------------------------------

	public static final String trim(Object strObj) {
		if (strObj == null)
			return "";
		return strObj.toString().trim();
	}

	// ------------------------------------------------------------------------------

	public static String stackToString(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "(bad stack2string)".concat(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------

	public static String escapeXMLstr(String input) {
		if (input == null)
			return null;

		StringBuffer output = new StringBuffer();
		int len = input.length();
		for (int i = 0; i < len; i++) {
			char ch = input.charAt(i);
			if (ch == '&') {
				output.append("&amp;");
				continue;
			}
			if (ch == '<') {
				output.append("&lt;");
				continue;
			}
			if (ch == '>') {
				output.append("&gt;");
				continue;
			}
			if (ch == '\'') {
				output.append("&apos;");
				continue;
			}
			if (ch == '"')
				output.append("&quot;");
			else
				output.append(ch);
		}

		return output.toString();
	}

	public static String padTrailing(String padString, int minLength,
			String padChar) {
		String tempPad = "";
		int lCnt = 0;

		String tempString = checkNull(padString);

		if (tempString.length() >= minLength)
			return tempString;
		else {
			for (lCnt = 1; lCnt <= minLength - tempString.length(); lCnt++) {
				tempPad = padChar + tempPad;
			}
		}
		return tempString + tempPad;
	}

	public static String padLeading(String padString, int minLength,
			String padChar) {
		String tempPad = "";
		int lCnt = 0;

		String tempString = checkNull(padString);

		if (tempString.length() >= minLength)
			return tempString;
		else {
			for (lCnt = 1; lCnt <= minLength - tempString.length(); lCnt++) {
				tempPad = tempPad + padChar;
			}
		}
		return tempPad + tempString;
	}

	public static String subString(String inputString, int length) {

		String tempString = checkNull(inputString);

		if (tempString.length() >= length)
			return tempString.substring(0, length);
		else {
			return tempString;
		}
	}

	/**
	 * Check if the string contains space
	 * 
	 * @param inputString
	 * @return
	 */
	public static boolean hasSpace(String inputString) {
		boolean returnFlag = true;

		if (inputString.indexOf(" ") < 0)
			returnFlag = false;

		return returnFlag;
	}

	/**
	 * Check if the string consist of alpha
	 * 
	 * @param inputString
	 * @return
	 */
	public static boolean isAlpha(String inputString) {
		int counter;

		for (counter = 0; counter < inputString.length(); counter++) {
			if (inputString.charAt(counter) >= 'a'
					&& inputString.charAt(counter) <= 'z')
				continue;
			if (inputString.charAt(counter) >= 'A'
					&& inputString.charAt(counter) <= 'Z')
				continue;
			return false;
		}
		return true;
	}

	/**
	 * Check if the charater is alpha
	 * 
	 * @param inputChar
	 * @return
	 */
	public static boolean isAlpha(char inputChar) {
		if (inputChar >= 'a' && inputChar <= 'z') {
			return true;
		}
		if (inputChar >= 'A' && inputChar <= 'Z') {
			return true;
		}

		return false;
	}

	/**
	 * Conver the string from one encoding to another
	 * 
	 * @param inputString
	 * @param oldEncode
	 * @param newEncode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String converEncode(String inputString, String oldEncode,
			String newEncode) throws UnsupportedEncodingException {

		String returnString = "";

		if (inputString == null)
			return returnString;
		try {
			returnString = new String(inputString.getBytes(oldEncode),
					newEncode);
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return returnString;
	}

	/**
	 * Conver bytes to hex string
	 * 
	 * @param inputByte
	 * @return
	 */
	public static String byteToHex(byte inputByte) {

		char hexDigitArray[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };

		char[] tempArray = { hexDigitArray[(inputByte >> 4) & 0x0f],
				hexDigitArray[inputByte & 0x0f] };

		return new String(tempArray);
	}

	/**
	 * Get binary length of string
	 * 
	 * @param inputString
	 * @return
	 */
	public static int bin2int(String inputString) {
		int index;
		int tempLength;
		int returnLength = 0;
		byte[] tempArray;

		if (inputString == null)
			return returnLength;

		tempArray = inputString.getBytes();
		tempLength = tempArray.length;
		for (index = 0; index < tempLength; returnLength = (returnLength * 2)
				+ tempArray[index] - '0', index++)
			;

		return returnLength;
	}

	/**
	 * Conver byte to binary
	 * 
	 * @param inputByte
	 * @return
	 */
	public static String byte2bin(byte inputByte) {
		char lbinDigit[] = { '0', '1' };
		char[] lArray = { lbinDigit[(inputByte >> 7) & 0x01],

		lbinDigit[(inputByte >> 6) & 0x01], lbinDigit[(inputByte >> 5) & 0x01],
				lbinDigit[(inputByte >> 4) & 0x01],
				lbinDigit[(inputByte >> 3) & 0x01],
				lbinDigit[(inputByte >> 2) & 0x01],
				lbinDigit[(inputByte >> 1) & 0x01], lbinDigit[inputByte & 0x01] };

		return new String(lArray);
	}

	/**
	 * Check if the string contains chinese
	 * 
	 * @param inputString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean hasChinese(String inputString)
			throws UnsupportedEncodingException {
		byte[] tempArray;
		String tempCode = new String();
		boolean returnFlag = false;

		try {
			// lUniStr = new String(inputString.getBytes(), "big5");
			tempArray = inputString.getBytes("UnicodeBig");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		for (int lk = 0; lk < tempArray.length; lk++) {
			if (tempArray[lk] == 0) {
				lk++;
			} else {
				tempCode = byteToHex(tempArray[lk++])
						+ byteToHex(tempArray[lk]);

				if (!tempCode.equals("feff")) {
					returnFlag = true;
					break;
				}
			}
		}
		return returnFlag;
	}

	/**
	 * Replace the special char in html
	 * 
	 * @param inputString
	 * @return
	 */
	public static String fixHTML(String inputString) {

		int index = 0;
		char tempChar;
		StringBuffer stringBuffer;

		if (inputString == null)
			return "";
		if ((inputString.trim()).equals(""))
			return inputString;

		stringBuffer = new StringBuffer(inputString);

		while (index < stringBuffer.length()) {
			if ((tempChar = stringBuffer.charAt(index)) == '"') {
				stringBuffer.replace(index, index + 1, "&#34;");
				index += 5;
				continue;
			}
			if (tempChar == '\'') { // "'"
				stringBuffer.replace(index, index + 1, "&#39;");
				index += 5;
				continue;
			}
			if (tempChar == '&') {
				try {
					if ((tempChar = stringBuffer.charAt(index + 1)) == '#') {
						index += 2;
						continue;
					}
				} catch (StringIndexOutOfBoundsException ex) {
					// needn't to do anything
				}

				stringBuffer.replace(index, index + 1, "&amp;");
				index += 5;
				continue;
			}
			if (tempChar == '<') {
				stringBuffer.replace(index, index + 1, "&lt;");
				index += 4;
				continue;
			}
			if (tempChar == '>') {
				stringBuffer.replace(index, index + 1, "&gt;");
				index += 4;
				continue;
			}
			index++;
		}
		return stringBuffer.toString();
	}

	/**
	 * Conver "'" to "''"
	 * 
	 * @param inputString
	 * @return
	 */
	public static String fixSQL(String inputString) {

		int index = 0;
		StringBuffer stringBuffer;

		if (inputString == null)
			return "";
		if ((inputString.trim()).equals(""))
			return inputString;

		stringBuffer = new StringBuffer(inputString);

		while (index < stringBuffer.length()) {
			if (stringBuffer.charAt(index) == '\'') {
				stringBuffer.replace(index, index + 1, "''");
				index += 2;
				continue;
			}
			index++;
		}
		return stringBuffer.toString();
	}

	/**
	 * Check if the string is null, if yes return ""
	 * 
	 * @param rStr
	 * @return
	 */
	public static String checkNull(String rStr) {
		if (rStr == null)
			return "";
		else
			return rStr.trim();
	}

	/**
	 * Check if the string is null, if yes return ""
	 * 
	 * @param rStr
	 * @return
	 */
	public static String checkNull(String rStr, String defaultValue) {
		if (rStr == null || rStr.equals(""))
			return defaultValue;
		else
			return rStr.trim();
	}

	/**
	 * Trim the string if it 's not null
	 * 
	 * @param rStr
	 * @return
	 */
	public static String trim(String rStr) {
		if (rStr == null) {
			return rStr;
		} else {
			return rStr.trim();
		}
	}

	/**
	 * Replace substring in the input string
	 * 
	 * @param inputString
	 * @param beReplaced
	 * @param replaceTo
	 * @return
	 */
	public static String replaceString(String inputString, String beReplaced,
			String replaceTo) {
		int index = 0;
		String returnString = "";

		returnString = inputString;

		do {
			index = inputString.indexOf(beReplaced, index);

			if (index == -1)
				break;

			returnString = inputString.substring(0, index) + replaceTo
					+ inputString.substring(index + beReplaced.length());
			index += replaceTo.length();
			inputString = returnString;

		} while (true);

		return returnString.substring(0, returnString.length());
	}

	/**
	 * Check if the input string is consist of numeric
	 * 
	 * @param inputString
	 * @return
	 */
	public static boolean isNumeric(String inputString) {
		int counter;

		if (inputString == null || inputString.trim().length() == 0) {
			return false;
		}

		for (counter = 0; counter < inputString.length(); counter++) {
			if (!(inputString.charAt(counter) >= '0' && inputString
					.charAt(counter) <= '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Split the string according to the delimetr to a vector
	 * 
	 * @param inputString
	 * @param delimiter
	 * @return
	 */
	public static Vector split(String inputString, String delimiter) {
		Vector returnVector = new Vector();
		int position = -1;

		position = inputString.indexOf(delimiter);
		while (position >= 0) {
			if (position > 0)
				returnVector.add(inputString.substring(0, position));

			inputString = inputString.substring(position + delimiter.length());
			position = inputString.indexOf(delimiter);
		}

		if (!inputString.equals(""))
			returnVector.add(inputString);

		return returnVector;

	}

	public static String[] splitToArray(String inputString, String delimiter) {
		List arrayList = new ArrayList();
		String[] result;
		int position = -1;

		position = inputString.indexOf(delimiter);
		while (position >= 0) {
			// if (position > 0)
			arrayList.add(inputString.substring(0, position));

			inputString = inputString.substring(position + delimiter.length());
			position = inputString.indexOf(delimiter);
		}

		if (!inputString.equals(""))
			arrayList.add(inputString);

		result = new String[arrayList.size()];
		arrayList.toArray(result);
		return result;

	}

	/**
	 * Replace some special character in the input string
	 * 
	 * @param inputString
	 * @return
	 */
	public static String fixWML(String inputString) {
		int index = 0;
		char ch;

		if (inputString == null)
			return "";
		StringBuffer strbuff = new StringBuffer(inputString);

		while (index < strbuff.length()) {
			if ((ch = strbuff.charAt(index)) == '"') {
				strbuff.replace(index, index + 1, "&#34;");
				index += 5;
				continue;
			}

			if (ch == '\'') {
				strbuff.replace(index, index + 1, "&#39;");
				index += 5;
				continue;
			}

			if (ch == '$') {
				strbuff.replace(index, index + 1, "$$");
				index += 2;
				continue;
			}

			if (ch == '&') {
				strbuff.replace(index, index + 1, "&amp;");
				index += 5;
				continue;
			}

			if (ch == '<') {
				strbuff.replace(index, index + 1, "&lt;");
				index += 4;
				continue;
			}

			if (ch == '>') {
				strbuff.replace(index, index + 1, "&gt;");
				index += 4;
				continue;
			}

			index++;
		}

		return strbuff.toString();
	}

	public static String removeString(String tmp, String start, String end) {
		int startPosi = -1;
		int endPosi = -1;
		startPosi = tmp.indexOf(start);
		if (startPosi >= 0) {
			endPosi = tmp.indexOf(end, startPosi);
			if (endPosi >= startPosi) {
				tmp = tmp.substring(0, startPosi) + tmp.substring(endPosi + 2);
			}
		}
		return tmp;
	}

	// ------------------------------------------------------------------------------
}