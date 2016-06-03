package httpcall;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import cn.grgbanking.feeltm.log.SysLog;

/**
 * 
 * Module: PostHttpClient.java Description:
 * 采用httpclient插件的post方式发送流二进制流数据到HTTP服务端 Company: Author: pantp Date: May 5,
 * 2012
 */
public class HttpClentUtil {

	/**
	 * 发送post请求,客户端采用二进制流发送,服务端采用二进制流介绍
	 * 
	 * @param json
	 *            入参的json格式的报文
	 * @param url
	 *            http服务器的地址
	 * @return 返回响应信息
	 */
	public static String postHttpReq(String json, String url) {
		HttpClient httpClient = new HttpClient();

		byte b[] = json.getBytes();// 把字符串转换为二进制数据
		RequestEntity requestEntity = new ByteArrayRequestEntity(b);

		EntityEnclosingMethod postMethod = new PostMethod();
		postMethod.setRequestEntity(requestEntity);// 设置数据
		postMethod.setPath(url);// 设置服务的url
		postMethod.setRequestHeader("Content-Type", "text/html;charset=GBK");// 设置请求头编码

		// 设置连接超时
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5 * 1000);
		// 设置读取超时
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20 * 1000);

		String responseMsg = "";
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);// 发送请求
			responseMsg = postMethod.getResponseBodyAsString();// 获取返回值
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();// 释放连接
		}

		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("HTTP服务异常" + statusCode);
		}
		return responseMsg;
	}

	/**get方式
	 * wtjiao 2014年11月11日 上午10:44:45
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String getHttpReq(String url, String charset) throws Exception{
		try {
			HttpClient client = new HttpClient();
			GetMethod method1 = new GetMethod(url);

			// 设置请求的编码方式
			if (null != charset) {
				method1.addRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=" + charset);
			} else {
				method1.addRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=" + "utf-8");
			}
			int statusCode = client.executeMethod(method1);

			if (statusCode != HttpStatus.SC_OK) { // 打印服务器返回的状态
				System.out.println("Method failed: " + method1.getStatusLine());
			}
			// 返回响应消息
			String response = method1.getResponseBodyAsString();

			// 释放连接
			method1.releaseConnection();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+e.toString());
			throw e;
		}
	}
	
	// POST方式发送HTTP请求
	/*	public static void main(String[] args) {
		String json = "{\"PubInfo\": {\"clinet\": \"10.70.128.132\",\"company\": \"月月鸟0820\"},\"Request\": {\"strBillId\": \"18221075148\",\"strCcsOpId\": \"1234\"}}";
		String url = "http://localhost:8080/nStraf/httpServer.servlet";
		String outPackage = null;
		outPackage = postHttpReq(json, url);
		System.out.println("客户端日志----POST方式调用HTTP,请求报文为:" + json);
		System.out.println("\nauthor<pantp>===========客户端日志----POST方式调用HTTP服务,HTTP服务端响应报文如下:=============\n");
		System.out.println(outPackage);
		System.out.println("\nauthor<pantp>================================================================\n");
	}*/
	
	 public static void main(String[] args) throws UnsupportedEncodingException {
		  //构造HttpClient的实例
		  HttpClient httpClient = new HttpClient();
		  //创建GET方法的实例
		  String addString = "广东省广州市天河区科林路11号";
		  addString =  java.net.URLEncoder.encode(addString,"utf-8");
		  GetMethod getMethod = new GetMethod("http://api.map.baidu.com/geocoder/v2/?address="+addString+"&output=json&ak=0qxbt4Q5TmqGP2RniHs6TMx7");
		  //使用系统提供的默认的恢复策略
		  try {
		   //执行getMethod
		   int statusCode = httpClient.executeMethod(getMethod);
		   if (statusCode != HttpStatus.SC_OK) {
		    System.err.println("Method failed: "
		      + getMethod.getStatusLine());
		   }
		   String response = getMethod.getResponseBodyAsString();
		 System.out.println(response.toString());
					// 释放连接
		  /* //读取内容 
		   byte[] responseBody = getMethod.getResponseBody();
		   //处理内容
		   System.out.println(new String(responseBody));*/
		  } catch (HttpException e) {
		   //发生致命的异常，可能是协议不对或者返回的内容有问题
		   System.out.println("Please check your provided http address!");
		   e.printStackTrace();
		  } catch (IOException e) {
		   //发生网络异常
		   e.printStackTrace();
		  } finally {
		   //释放连接
		   getMethod.releaseConnection();
		  }
		 }

}