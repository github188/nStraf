<%@ page import="com.opensymphony.util.TextUtils"%>
<%@ page contentType="text/html;charset=GBK"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

 <html>
    <head>
        <title>OSWorkflow Example App</title>
    </head>

    <body>

    <p>
    ����������ϵͳ
    <form method="POST" action="login.jsp">
    <table border="0">
        <tr><td>�û���:</td><td><input type="text" name="username"></td></tr>
        <tr><td>����:</td><td><input type="password" name="password"></td></tr>
        <tr><td colspan="2"><input type="submit" value=" ��½ "></td></tr>
    </table>
    </form>

    <% if (TextUtils.parseBoolean(request.getParameter("auth_failed"))) { %>
        <font color="red">Invalid login! Have you created an account yet?</font>
    <% } %>
    <hr>

    </body>
</html>
