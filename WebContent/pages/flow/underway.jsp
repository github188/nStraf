<%@page import="java.util.*,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 com.opensymphony.workflow.query.Expression,
                 com.opensymphony.workflow.query.FieldExpression,
                 com.opensymphony.workflow.query.NestedExpression,
                 com.opensymphony.workflow.query.WorkflowExpressionQuery,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor"%>
<%@page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<%

  Workflow wf = new BasicWorkflow((String) session.getAttribute("username"));

  NestedExpression nestedExpression = new NestedExpression(
  new Expression[]{new FieldExpression(FieldExpression.OWNER, FieldExpression.CURRENT_STEPS, FieldExpression.EQUALS, session.getAttribute("username")),
                   new FieldExpression(FieldExpression.STATUS, FieldExpression.CURRENT_STEPS, FieldExpression.EQUALS, "Underway")}, NestedExpression.AND) ;

  WorkflowExpressionQuery query = new WorkflowExpressionQuery(nestedExpression);

  try {
	
    List workflows = wf.query(query);
   
    for (Iterator iterator = workflows.iterator(); iterator.hasNext(); ) {
    	
      long wfid = ((Long) iterator.next()).longValue();
     
      WorkflowDescriptor wd = wf.getWorkflowDescriptor(wf.getWorkflowName(wfid));
     
      int[] actions = wf.getAvailableActions(wfid, null);
      for (int i = 0; i < actions.length; i++) {
        String name = wd.getAction(actions[i]).getName();
        System.out.println("name:"+name);
        %>
        <li> <a href="leaveApprove.jsp?wfid=<%=wfid%>&action=<%= actions[i] %>"><%= name %></a>
        <%
      }
    }
  }catch(Exception ex){
    System.out.println("取得工作列表时出现异常");
    ex.printStackTrace();
  } %>
  <%@include file="nav.jsp"%>
