/*
 * Copyright (c) 2010-2011 by GRGBanking
 * All rights reserved.
 */
package cn.grgbanking.feeltm.audit.webapp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.user.User;
import com.opensymphony.user.UserManager;
import com.opensymphony.util.TextUtils;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;


/**
 * Simple utility class that uses stepId to determine that if the current caller is
 *  the owner of last step
 * 
 * @author <a href="mailto:xtao@grgbanking.com">Tiger</a>
 */
@SuppressWarnings("unchecked")
public class IsLastStepOwnerCondition implements Condition {
    //~ Methods ////////////////////////////////////////////////////////////////

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        try {
            // expects a stepId name/value pair
            String stepIdString = (String) args.get("stepId");
            WorkflowEntry entry = (WorkflowEntry) transientVars.get("entry");

            if (stepIdString == null) {
                throw new WorkflowException("This function expects a stepId!");
            }

            StringTokenizer st = new StringTokenizer(stepIdString, ",");
            List stepIds = new LinkedList();

            while (st.hasMoreTokens()) {
                stepIds.add(st.nextToken().trim());
            }

            WorkflowStore store = (WorkflowStore) transientVars.get("store");
            List historySteps = store.findHistorySteps(entry.getId());

            String mostRecentOwner="";
            for (Iterator iterator = historySteps.iterator(); iterator.hasNext();) {
                Step step = (Step) iterator.next();

                if (stepIds.contains(String.valueOf(step.getStepId())) && TextUtils.stringSet(step.getOwner())) {
                	mostRecentOwner=step.getOwner();
                	transientVars.put("mostRecentOwner", step.getOwner());
                    break;
                }
            }      	        	        	        	
            WorkflowContext context = (WorkflowContext) transientVars.get("context");
            User user = UserManager.getInstance().getUser(context.getCaller());
            if(user.getName()!=null&&user.getName().equals(mostRecentOwner)) return true;
            else
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
