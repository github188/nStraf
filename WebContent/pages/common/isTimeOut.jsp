<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file="/inc/htc.inc"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:property value="%{getText('checkright.timeout')}"/></title>
<script language="JavaScript">
 function BackTo()
 {
  
 	var aa=window;
 	while(aa!=aa.parent){
 		aa=aa.parent;
 	}
 	
 	if(aa.dialogArguments){
 		var bb=window.dialogArguments;
 		while(bb!=bb.parent){
 		  bb=bb.parent;
 	  }
 	 
 	  bb.location="<%=request.getContextPath()%>/login!timeout.action"; 	 
 	  window.close();
  }
 	if(aa.opener){
 		var bb=aa.opener;
 		while(bb!=bb.parent){
 		  bb=bb.parent;
 	  }
 	 
 		bb.location="<%=request.getContextPath()%>/login!timeout.action";
 	  window.close();
 	}
 	
  top.location="<%=request.getContextPath()%>/login!timeout.action";
 }
 function timeoutPage()
 {

 	var aa=window;
 	var pageDiv = 0;
 	while(aa!=aa.parent && !aa.dialogArguments){
 		aa=aa.parent;
 		pageDiv++;
 	}
 	if(pageDiv>0){
 	
 	  aa.location="<%=request.getContextPath()%>/pages/common/isTimeOut.jsp"; 
 	  oTimer=setInterval('later.innerText=parseInt(later.innerText)-1;a();',6000);
  }
 }
</script>
</head>
<body   bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onload="timeoutPage()">
<table WIDTH=100% height="100%">

  <tr>
 <td >
      <table width=424 border=0 cellpadding=0 cellspacing=0 align="center" height="258" style="background-repeat: no-repeat;" background="<%=request.getContextPath()%>/images_new/msg/message.png" >
        <tr>
          <td colspan="4" height="52">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" align="left" valign="top">&nbsp; </td>
          <td colspan=2 rowspan=2>
            <p align="center">
            <s:property value="%{getText('checkright.timeout')}"/>
			      
			   <br>
			   <br>
               <div align="center">
			       <input type="button" name="btnOk" value='<s:property value="%{getText('button.ok')}"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/ok.gif">
              </div>
          </td>
          <td width="9">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" height="190" valign="top"> 
            <table width="91" border="0" cellspacing="0" cellpadding="0" height="60">
              <tr> 
              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td > 
                  <div align="center"><img src="<%=request.getContextPath()%>/images_new/msg/warn.png"  style="cursor:hand" width="63" height="60"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="9" height="190">&nbsp; </td>
        </tr>
       
      </table>
    </td>
</tr>
</table>
</body>
</html>
<script language="javascript">
try{
   btnOk.focus()
}catch(e){
  //alert(e.description)
}
</script>