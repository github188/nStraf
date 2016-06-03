package cn.grgbanking.feeltm.service;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import cn.grgbanking.feeltm.login.webapp.UserOnline;

public class SessionListener implements HttpSessionListener {   
    public void sessionCreated(HttpSessionEvent event) { 
    }   
    
    public void sessionDestroyed(HttpSessionEvent event)   {   
    	UserOnline.logonOffUser(event.getSession().getId());
    }   


}   

