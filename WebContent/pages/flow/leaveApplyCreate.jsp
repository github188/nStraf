<%@ page import="java.util.*,
                 com.opensymphony.user.UserManager,
                 com.opensymphony.user.EntityNotFoundException,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
    String reason = request.getParameter("reason");
    int dayCount = Integer.parseInt((String)request.getParameter("dayCount"));
    String applicant = (String)session.getAttribute("username");
    String applyType=(String)request.getParameter("applyType");
     System.out.println(applicant+"Begin to Initialize workflow 100 1!");
    Workflow wf = new BasicWorkflow((String) session.getAttribute("username"));
    
    Map map = new HashMap();
    map.put("applicant", applicant);
    map.put("reason", reason);
    map.put("dayCount", new Integer(dayCount)); 
    map.put("applyType",applyType);
    try {
      System.out.println(applicant+"Begin to Initialize workflow 100 2!");
      long wfid = wf.initialize("leave_apply", 100, null);
      
      System.out.println(applicant+"Initialize workflow 100 OK!");  
      wf.doAction(wfid, 1, map);
      out.print("请假申请已经成功提交，请等待审批！");
    } catch (Exception ex){
      ex.printStackTrace();
      out.print(ex.getStackTrace());
      out.print("提交请假申请时出现异常，可能没有增加您提交需求的权限！");
    }

%>
<%@ include file="nav.jsp" %>
