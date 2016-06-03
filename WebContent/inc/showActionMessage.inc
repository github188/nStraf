<%
	
	Object o = request.getAttribute("hasActionMessage");
	String hasActionMessage = "";
	if(o != null)
		hasActionMessage = o.toString();
	if(hasActionMessage != "" && hasActionMessage.equals("true"))
	{
 %>
  <table width="90%" align="center" cellPadding="0" cellSpacing="0">
    <tr>
  		<td> 
     		 <fieldset class="jui_fieldset" width="100%">
           		 <legend><s:text name="operator.backinfo"/></legend>
        		 <table width="100%">
         		 	<tr> 
           				 <td width="10%" align="left">&nbsp;</td>
          			 	 <td width="90%" align="left">
		    				<font color="#FF0000">
							 <s:actionmessage/>
							</font>
			 		 	</td>
         			 </tr>
       		 	</table>
		    </fieldset>
      </td>    
  </tr>
 </table>
 <%} %>