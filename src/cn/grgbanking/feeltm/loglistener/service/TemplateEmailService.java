package cn.grgbanking.feeltm.loglistener.service;

import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.grgbanking.feeltm.config.SendEmailUtil;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import freemarker.template.Template;

public class TemplateEmailService {
    private FreeMarkerConfigurer freeMarkerConfigurer=null;//FreeMarker的技术类  
      
    public TemplateEmailService(){
    	this.freeMarkerConfigurer=(FreeMarkerConfigurer)BaseApplicationContext.getAppContext().getBean("freeMarker");
    } 
  
    //通过模板构造邮件内容，参数username将替换模板文件中的${username}标签。  
    private String getMailText(String templateFile, Map<String, String> templateTagMap){  
        String htmlText="";  
        try {  
            //通过指定模板名获取FreeMarker模板实例  
            Template tpl=freeMarkerConfigurer.getConfiguration().getTemplate(templateFile);  
            //解析模板并替换动态数据，最终username将替换模板文件中的${username}标签。  
            htmlText=FreeMarkerTemplateUtils.processTemplateIntoString(tpl,templateTagMap);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return htmlText;  
    }  
      
    //发送模板邮件 ,返回发送的内容
    /**
     * wtjiao 2014年10月28日 上午8:22:41
     * @param toEmail 收件人地址
     * @param title 邮件标题
     * @param templateFile 采用的模版文件，在src/mailTemplate下
     * @param templateTagMap 需要替换的模版文件标识
     * @return 邮件发送内容  0表示邮件发送失败
     */
    public String sendTemplateMail(String toEmail,String title,String templateFile,Map<String,String> templateTagMap) {  
    	String htmlText=getMailText(templateFile,templateTagMap);//使用模板生成html邮件内容  
    	try{
			boolean sucess=SendEmailUtil.sendEmail(toEmail, null, SendEmailUtil.EMAIL_USERAME, title, htmlText);
			if(sucess){
				return htmlText;
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
    	return "0";
    } 
    
   /**
    * 
    * @param toEmail
    * @param copys 抄送人
    * @param title
    * @param templateFile
    * @param templateTagMap
    * @return
    */
   
    public String sendTemplateMailCopys(String toEmail,String copys,String title,String templateFile,Map<String,String> templateTagMap) {  
    	String htmlText=getMailText(templateFile,templateTagMap);//使用模板生成html邮件内容  
    	try{
			boolean sucess=SendEmailUtil.sendEmailtoCopy(toEmail, copys, SendEmailUtil.EMAIL_USERAME, title, htmlText);
			if(sucess){
				return htmlText;
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
    	return "0";
    }  
   
    
    
}
