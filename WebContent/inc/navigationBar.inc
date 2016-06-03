<table width="100%" height="38" align="center" border="0"  background="<%=request.getContextPath()%>/images_new/main/navigation_bg.jpg"  cellpadding="0" cellspacing="0">    	
     <tr align="center">
          <td width="10" valign="middle"></td>
          <td width="*" align="left" style="color:#336699" valign="middle"><s:property  value="%{getText('index.mainpanel')}"/> 
                
                <%

                   out.write(cn.grgbanking.feeltm.util.NavigationBar.getNavigation(pageContext));
                %>
	  </td>
</table>