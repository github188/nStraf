<%@ page import="com.opensymphony.util.TextUtils"%>
<%@ page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
 <html>
    <head>
        <title>OSWorkflow Example App</title>
    </head>

    <body>

    <p>
    �������,����д����������뵥
    <form method="POST" action="leaveApplyCreate.jsp">
    <table border="0">
        <tr><td>���ԭ��:</td><td><input type="text" name="reason"></td></tr>
        <tr><td>�������:</td><td><input type="text" name="dayCount"> ����</td></tr>
        <tr><td>�������:</td><td><input type="text" name="applyType"> ����</td></tr>
        <tr><td colspan="2"><input type="submit" value="�ύ����"></td></tr>
    </table>
    </form>
    <hr>
    </body>
</html>
