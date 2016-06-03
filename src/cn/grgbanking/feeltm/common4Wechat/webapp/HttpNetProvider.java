package cn.grgbanking.feeltm.common4Wechat.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class HttpNetProvider {
	
	private static Logger logger = Logger.getLogger(HttpNetProvider.class);

	public static byte[] doGetBytes(String url){
		InputStream is=null;
		ByteArrayOutputStream os=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
			conn.setDoInput(true);
			is=conn.getInputStream();
			os = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				os.write(b,0,length);
			}
			conn.disconnect();
			return os.toByteArray();
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
		}
		return null;
	}
	
	public static byte[] doPostBytes(String url,byte[] data){
		InputStream is=null;
		OutputStream os=null;
		ByteArrayOutputStream bos=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();		
			conn.setDoInput(true);
			conn.setDoOutput(true);
			os=conn.getOutputStream();
			os.write(data);
			os.flush();
			is=conn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				bos.write(b,0,length);
			}
			conn.disconnect();
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			}
			if(bos!=null)
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
		}
		return null;
	}
	
	public static byte[] doPostBytes(String url,byte[] data,String requestContentType){
		InputStream is=null;
		OutputStream os=null;
		ByteArrayOutputStream bos=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();		
			conn.setRequestProperty("content-type", requestContentType);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			os=conn.getOutputStream();
			os.write(data);
			os.flush();
			is=conn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				bos.write(b,0,length);
			}
			conn.disconnect();
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			}
			if(bos!=null)
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
		}
		return null;
	}
	
	public static byte[] doPostBytes(String url,String data){
		try {
			return doPostBytes(url,data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException",e);
		}
		return null;
	}
	
	public static byte[] doPostBytes(String url,String data,String requestContentType){
		try {
			return doPostBytes(url,data.getBytes("utf-8"),requestContentType);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException",e);
		}
		return null;
	}
	
	
	public static JSONObject sendByHttpClient(String url,String paramJsonStr) throws Exception {
		HttpClient client = new HttpClient();
		// 使用POST方法
		PostMethod method = new PostMethod(url);
		JSONObject parms=new JSONObject(paramJsonStr);
		Iterator<String> keys=parms.keys();
		while(keys.hasNext()){
			String key=keys.next();
			String val=(String)parms.get(key);
			((PostMethod) method).addParameter(key, val);
		}
		HttpMethodParams param = method.getParams();
		param.setContentCharset("UTF-8");

		int statusCode=client.executeMethod(method);
		// 打印返回的信息
 		StringBuffer stringBuffer = new StringBuffer();  
		
		 if (statusCode == HttpStatus.SC_OK) {  
             InputStream ins = method.getResponseBodyAsStream();  
             byte[] b = new byte[1024];  
             int r_len = 0;  
             while ((r_len = ins.read(b)) > 0) {  
            	 stringBuffer.append(new String(b, 0, r_len, method.getResponseCharSet()));  
             }  
         } else {  
             System.err.println("Response Code: " + statusCode);  
         }  
        
		// 释放连接
		method.releaseConnection();
		String jsonStr =  URLDecoder.decode(stringBuffer.toString(),"UTF-8");
		JSONObject jsonObject = new JSONObject(jsonStr);
		return jsonObject;
	}  
	
	public static void main(String[] args) throws Exception {}
}
