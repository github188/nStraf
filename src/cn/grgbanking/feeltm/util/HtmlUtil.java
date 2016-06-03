package cn.grgbanking.feeltm.util;

import java.util.*;

@SuppressWarnings("unchecked")
public class HtmlUtil {
	// ------------------------------------------------------------------------------

	public static String htmlEncode(String strInput) {
		if (strInput == null)
			return "";

		strInput = strInput.trim();

		StringBuffer output = new StringBuffer();
		int len = strInput.length();
		char ch;
		for (int i = 0; i < len; i++) {
			ch = strInput.charAt(i);
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
			if (ch == '"')
				output.append("&quot;");
			else
				output.append(ch);
		}
		return output.toString();
	}

	// ------------------------------------------------------------------------------
	public static String htmlEncode(Object strObj) {
		if (strObj == null)
			return "";
		return htmlEncode(strObj.toString());
	}

	// ------------------------------------------------------------------------------

	public static final String escapeScript(String strInput) {
		if (strInput == null)
			return "";

		strInput = strInput.trim();

		StringBuffer output = new StringBuffer();
		int len = strInput.length();
		char ch;
		for (int i = 0; i < len; i++) {
			ch = strInput.charAt(i);
			if (ch == '"') {
				output.append("\\");
			}
			output.append(ch);
		}
		return output.toString();
	}

	// ------------------------------------------------------------------------------

	public static final String createSelectOptions(HashMap strMap,
			String selectKey) {
		StringBuffer sbf = new StringBuffer();
		Iterator itr = strMap.keySet().iterator();
		String key;
		while (itr.hasNext()) {
			key = itr.next().toString();
			sbf.append("<option value=\"").append(key);
			sbf.append("\"").append(key.equals(selectKey) ? "selected >" : ">");
			sbf.append(htmlEncode(strMap.get(key))).append("</option>\r\n");
		}
		return sbf.toString();
	}

	// ------------------------------------------------------------------------------
	public static final String splitRow(String strContent, String symbol) {
		if (strContent == null || strContent.length() == 0)
			return "";
		StringBuffer sbf = new StringBuffer();
		String[] aryStr = StringUtil.toArray(strContent, symbol);
		for (int i = 0; i < aryStr.length; i++) {
			sbf.append(aryStr[i]).append(symbol).append("<br>");
		}
		return sbf.toString();
	}
}
