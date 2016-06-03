package cn.grgbanking.feeltm.log;
/**
 * ��װlog4j
 * �ṩ��̬��������¼ϵͳ�ĵ�����Ϣ��һ����Ϣ�ʹ�����Ϣ�����浽ָ���ļ�
 * @author ljming
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;

public class SysLog {
	
	  /**
	   * ���Լ�¼��
	   */
	  private static Logger debugLogger=Logger.getLogger("feeltm.debug");
	  
	  /**
	   * һ����Ϣ��¼��
	   */
	  private static Logger infoLogger=Logger.getLogger("feeltm.info");
	  
	  /**
	   * ������Ϣ��¼��
	   */
	  private static Logger errorLogger=Logger.getLogger("feeltm.error");

	  /**
	   * ������Ϣ��¼
	   */
	  private static Logger tranLogger=Logger.getLogger("feeltm.tran");
	  
	  private static Logger flowLogger=Logger.getLogger("feeltm.flow");
	  
	  static
	  {
		String webappHome=Configure.ROOT_PATH+File.separator;
		System.setProperty("webappHome",webappHome);  
	    PropertyConfigurator.configure(new SysLog().getClass().getClassLoader().getResource("feelTMLog.properties"));
	  }
	  
	  
	  /**自定义 记录数据的log
	 * wtjiao 2014年12月8日 下午1:53:39
	 * @param msg
	 */
	public static final void record(String msg){
		try{
			String path=System.getProperty("webappHome", File.separator.equals("/")?"/":"C:\\");
			path+="mylogs"+File.separator;
			FileUtils.forceMkdir(new File(path));
			String filename="nStarfRecord.log";
			File file=new File(path+filename);
			if(!file.exists()){
				FileUtils.touch(file);
			}
			appendFile(path+filename, msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	  }
	
	private static void appendFile(String fileName,String content){
		FileWriter writer = null;  
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(fileName, true);     
            writer.write(content+"\n");       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }  
	}
	  /**
	   * ��¼������Ϣ
	   * @param msg  ��Ϣ
	   */
	  public static final void debug(String msg)
	  {
	     debugLogger.debug(msg);
	  }

	  /**
	   * ��¼һ����Ϣ
	   * @param msg
	   */
	  //一般日志信息
	  public static final void info(String msg)
	  {
	     infoLogger.info(msg);
	  }
	  //用于有Request,可以取出当前用户的一般日志记录
	  public static final void info(HttpServletRequest request,String msg)
	  {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);		  
	     infoLogger.info(userModel.getUserid()+":"+msg);
	  }

	  public static final void flow(String msg)
	  {
		  flowLogger.info(msg);
	  }
	  
	  /**
	   * ��¼������Ϣ
	   * @param msg
	   */
	  public static final void tran(String msg)
	  {
		  tranLogger.info(msg);
	  }

	  /**
	   * ��¼������Ϣ
	   * @param msg
	   */
	  //一般错误信息记录
	  public static final void error(String msg)
	  {
	     errorLogger.error(msg);
	  }
	  //用于有Request,可以取出当前用户的错误日志记录
	  public static final void error(HttpServletRequest request,String msg)
	  {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);		  
	     infoLogger.error(userModel.getUserid()+":"+msg);
	  }
	  

      /**
      * ��¼�쳣������Ϣ
      */
	  public static final void error(Exception exp)
	  {
	     errorLogger.error(stackToString(exp));
	  }

      public static String stackToString(Exception exp)
      {
	      try
	      {
	          StringWriter sw = new StringWriter();
	          PrintWriter pw = new PrintWriter(sw);
	          exp.printStackTrace(pw);
	          return sw.toString();
	      }
	      catch(Exception e2)
	      {
	          return "(bad stack2string)".concat(exp.getMessage());
	      }
      }
      
      /*记录操作日志*/
      
   public static void operLog(HttpServletRequest request, String operid, String others)
		throws ParseException {
	
}

}
