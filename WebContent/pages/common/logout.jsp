<%
    session.removeAttribute(cn.grgbanking.feeltm.util.Constants.LOGIN_USER_KEY);
    session.invalidate();
    response.sendRedirect(request.getContextPath()+"/index.jsp");

%>