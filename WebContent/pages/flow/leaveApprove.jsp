<%@ page import="com.opensymphony.user.User,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 java.util.*,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor,
                 com.qiny.dao.LeaveDAO,
                 com.qiny.leave.LeaveApply"%>
<%@ page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<%
    String username = (String) session.getAttribute("username");
    Workflow wf = new BasicWorkflow(username);

    long wfid = Long.parseLong(request.getParameter("wfid"));
    int action = Integer.parseInt(request.getParameter("action"));
    WorkflowDescriptor wd =  wf.getWorkflowDescriptor(wf.getWorkflowName(wfid));
    String name = wd.getAction(action).getName();
    LeaveDAO leaveDao = new LeaveDAO();
    LeaveApply leaveApply = leaveDao.getLeaveApply(wfid);
%>
请假审批处理:<br>
<hr><br>

请假人: <%=leaveApply.getApplicant()%><br>
请假原因:<%=new String(leaveApply.getReason().getBytes("ISO-8859-1"),"GBK")%><br>
请假时间:<%=leaveApply.getApplyTime()%><br>
请假天数:<%=leaveApply.getDayCount()%><br><br>
<hr><br>
<form name="approveForm" method="post" action="leaveApproveProcess.jsp?wfid=<%=wfid%>&action=<%=action%>">
<input type="hidden" name="applyID" value="<%=leaveApply.getApplyID()%>"/>
<input type="hidden" name="opinion" value="1"/>
    <table border="0">
        <tr><td>
          <button name="btnPass" onClick="approve(1)">审核通过</button>
          <button name="btnReject" onClick="approve(2)">拒绝通过</button></td></tr>
    </table>
</form>
<hr>
<script language="javascript" type="text/javascript">

function approve(opinion) {
     document.approveForm.opinion.value = opinion;
     document.approveForm.submit();
}
</script>
