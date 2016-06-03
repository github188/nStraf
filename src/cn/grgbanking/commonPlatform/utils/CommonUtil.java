package cn.grgbanking.commonPlatform.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;


public class CommonUtil {
	/**
	 * 网页端输出json字符串
	 * 
	 * @param str
	 *            json字符串
	 */
	public static void ajaxPrint(String str) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向手机端输出jsonp数据
	 * @param jsonpObj
	 * @param jsonpCallback
	 */
	public static String outputJson(Object jsonpObj, String jsonpCallback){
		if (StringUtils.isNotBlank(jsonpCallback)) {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=utf-8");
			JSONArray jsonArray = JSONArray.fromObject(jsonpObj);
			String json = jsonpCallback+"("+jsonArray.toString()+")"; 
			try {
				response.getWriter().print(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
