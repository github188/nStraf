package cn.grgbanking.feeltm.project.webapp;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.loglistener.util.LogListenerSyschronizeUtil;
import cn.grgbanking.feeltm.project.util.UserProjectUpdateUtil;
import cn.grgbanking.framework.webapp.BaseAction;

public class UserProjectUpdateListener extends BaseAction {
    @Autowired
    private UserProjectUpdateUtil userProjectUpdateUtil;

    @Autowired
    private LogListenerSyschronizeUtil listenerSyschronizeUtil;

    /**
     * 每晚同步一次用户数据 wtjiao 2014年9月5日 上午9:40:41
     */
    public void synchronizeUserProject() {
//        synchronized (this) {

            userProjectUpdateUtil.registerRequest(request).synchronizeUserProject();

            listenerSyschronizeUtil.sychronizeLogListener();
//        }

    }
}
