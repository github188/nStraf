<%@ page import="java.util.*,
                 com.opensymphony.user.User,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor"%>
<%@ page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<%
    String user = (String)session.getAttribute("username");
    Workflow wf = new BasicWorkflow((String) session.getAttribute("username"));
    long wfid = Long.parseLong(request.getParameter("wfid"));
    int action = Integer.parseInt(request.getParameter("action"));
    int applyID = Integer.parseInt((String)request.getParameter("applyID"));
    int opinion = Integer.parseInt((String)request.getParameter("opinion"));
    Map map = new HashMap();
    map.put("approver", user);
    map.put("applyID", new Integer(applyID));
    map.put("opinion", new Integer(opinion));
    try {
      wf.doAction(wfid, action, map);
      out.print("请假审批已经成功提交！");
    }catch(Exception ex){
      out.print("请假审批时出现异常！");
    }

%>
<%@ include file="nav.jsp" %>
