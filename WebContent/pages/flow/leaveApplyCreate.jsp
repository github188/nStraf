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
      out.print("��������Ѿ��ɹ��ύ����ȴ�������");
    } catch (Exception ex){
      ex.printStackTrace();
      out.print(ex.getStackTrace());
      out.print("�ύ�������ʱ�����쳣������û���������ύ�����Ȩ�ޣ�");
    }

%>
<%@ include file="nav.jsp" %>
