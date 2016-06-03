<%@ page import="com.opensymphony.util.TextUtils"%>
<%@ page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
 <html>
    <head>
        <title>OSWorkflow Example App</title>
    </head>

    <body>

    <p>
    请假申请,请填写下面请假申请单
    <form method="POST" action="leaveApplyCreate.jsp">
    <table border="0">
        <tr><td>请假原因:</td><td><input type="text" name="reason"></td></tr>
        <tr><td>请假天数:</td><td><input type="text" name="dayCount"> 整数</td></tr>
        <tr><td>请假类型:</td><td><input type="text" name="applyType"> 整数</td></tr>
        <tr><td colspan="2"><input type="submit" value="提交申请"></td></tr>
    </table>
    </form>
    <hr>
    </body>
</html>
