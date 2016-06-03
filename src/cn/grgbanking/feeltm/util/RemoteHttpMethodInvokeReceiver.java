package cn.grgbanking.feeltm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.signrecord.webapp.SignRecordTimeQuartz;


/**远程方法调用接受类，原来的HttpUserDataRoleConfigService
 * wtjiao 2014年12月12日 上午9:49:15
 */
public class RemoteHttpMethodInvokeReceiver {
	
    /**获取用户角色配置信息
     * @param request
     * @param response
     */
    public static void getUserDataRoleConfig(HttpServletRequest request,HttpServletResponse response){
        String result =getRequestParam(request);
		String[] param = result.split("\\|");
		String user_id=param[0];
		String key = param[1];
		boolean getInfo = UserDataRoleConfig.viewAllDataByUserid(result.split("\\|")[0], result.split("\\|")[1]);
		setResponseValue(response,getInfo+"");
    }
    
    /**调用签到数据解析器
     * @param request
     * @param response
     */
    public static void invokeRegularlySignAnalyser(HttpServletRequest request,HttpServletResponse response){
    	try{
    		String result =getRequestParam(request);
    		String recordId=result.split(":")[1];
        	SignRecordTimeQuartz signRecordService=(SignRecordTimeQuartz)BaseApplicationContext.getAppContext().getBean("signRecordTimeQuartz");
        	signRecordService.attendanceAnalyser(recordId);
        	setResponseValue(response,"");
    	}catch(Exception e){
    		String str="手机服务器调用WEB服务器端解析签到数据请求错误，有可能是传入的参数有误";
    		SysLog.info(str);
    		System.out.println(str);
    	}
    	
    }
    
    
    /**获取参数
     * wtjiao 2014年12月12日 上午10:20:36
     * @param request
     * @return
     */
    private static String getRequestParam(HttpServletRequest request){
    	try{
	    	request.setCharacterEncoding("UTF-8");
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String line = "";
	        StringBuffer buf = new StringBuffer();
	        while ( (line = br.readLine()) != null ) {
	            buf.append(line);
	        }
	        return buf.toString();
    	}catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return "";
    }
    
    /**设置返回值
     * wtjiao 2014年12月12日 上午10:20:28
     * @param response
     * @param str
     */
    private static void setResponseValue(HttpServletResponse response, String str){
    	try{
	    	 response.setHeader("cache-control", "no-cache");
	         response.setCharacterEncoding("UTF-8");
	         response.setContentType("text/html");
	         OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());    
	         out.write(str);
	         out.flush();
	         out.close();
    	} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

