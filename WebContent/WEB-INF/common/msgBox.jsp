<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="cn.grgbanking.framework.webapp.WebConstants"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<title>msgBox</title>
<script language="JavaScript">
 var backUrl="/"
 var target="SELF"
 var buttonType="RETURN"

 <logic:present name="MSGBOX.KEY">
     backUrl='<bean:write name="MSGBOX.KEY" property="backUrl" />'
	 target='<bean:write name="MSGBOX.KEY" property="target"/>'
	 buttonType='<bean:write name="MSGBOX.KEY" property="buttonType"/>'
 </logic:present>
 
 <logic:present name="<%=WebConstants.LINK_URL%>">
     backUrl='<%=request.getAttribute(WebConstants.LINK_URL).toString()%>'
 </logic:present>
     

 function BackTo()
 {
    if(buttonType=="CLOSE")
    {
	    if(window==window.parent){
           window.close();
		}else{
			if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
				parent.close();  
			}else{
				parent.refreshParent();
		   		parent.close();  
			}
		}    
        return;
    }
    if(buttonType=="RETURN")
    {
    	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
    		if(document.parentWindow.history.length==0){
   	         	window.close();
    		}else{
   	         	window.history.back();
    		}
    	}else{
    		if(window==window.parent){
               window.close();
    		}else{
    		   var hrefUrl = parent.location.href;
    		   if(hrefUrl.indexOf("modalDialog")!=-1){//判断是否有打开弹出页面，如果有，则刷新*list页面
    			   parent.refreshParent();
        		   parent.close();
    		   }else{//如果在当前页面进行操作，返回上一层即可
    			   window.history.back();
    		   }
    		}
    	}
       return;
    }
    if(buttonType=="OK"){
    	parent.close();
    }
    	
	if(backUrl=="<%=request.getContextPath()%>/")
	{
	   top.location=backUrl
	   return
	}
	
    if(target=="PARENT")
    {
        parent.location.href=backUrl;
    }else
    {
	   if(target=="TOP")
	   {
	       top.location.href=backUrl;
	   } else
	   {
          window.location.href=backUrl;
		}
    }
 }
 function ShowErrDetail()
 {
    var  divDetail=document.getElementById('divDetail')
	if(divDetail==null)
	   return;
	if(divDetail.style.visibility=='hidden')
	{
    	divDetail.style.visibility = 'visible'  
	}	
	else
	{
	    divDetail.style.visibility = 'hidden'
	}	
 } 
</script>
</head>
<body   bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table WIDTH=100% height="100%">
  <tr>
    <td >
      <table width="424" border=0 cellpadding=0 cellspacing=0 align="center" style="background-repeat: no-repeat;" height="258" background="<%=request.getContextPath()%>/images_new/msg/message.png" >
        <tr>
          <td colspan="4" height="52">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" align="left" valign="top">&nbsp; </td>
          <td colspan=2 rowspan=2>
            <p align="center">
			  <logic:messagesPresent message="true" >
				<html:messages id="msg" message="true" >
				   <bean:write name="msg"  filter="false"/>
				</html:messages>
			  </logic:messagesPresent>
			  
			  <logic:present name="MSGBOX.KEY">
                   <logic:notEmpty name="MSGBOX.KEY"  property="msgInfo">
					      <bean:write name="MSGBOX.KEY"  property="msgInfo"/>   
				   </logic:notEmpty>				  				  
		      </logic:present>
            </p>
            <logic:present name="MSGBOX.KEY"> 
               <logic:notEmpty name="MSGBOX.KEY"  property="errorStack">
                   <div id = "divDetail"  style="width=330;height:80;visibility:hidden;overflow:scroll">
				     <bean:write name="MSGBOX.KEY"  property="errorStack"/>
			       </div>
			   </logic:notEmpty>				  
			 </logic:present> 
			 <br/>
               <div align="center">
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="OK" >
			       <input type="button" name="btnOk" value='<s:text name="button.ok"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/ok.gif">
			     </logic:equal>
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="RETURN" >
			       <input type="button" name="btnOk" value='<s:text name="button.return"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/return.gif">
			     </logic:equal>
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="CLOSE" >
			       <input type="button" name="btnOk" value='<s:text name="button.close"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/close.gif">
			     </logic:equal>
              </div>
          </td>
          <td width="9">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" height="190" valign="top"> 
            <table width="63" border="0" cellspacing="0" cellpadding="0" height="60">
              <tr> 
              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td > 
                  <div align="center"><img src="<%=request.getContextPath()%>/images_new/msg/warn.png" onclick="ShowErrDetail()" style="cursor:hand" width="63" height="60"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="34" height="190">&nbsp; </td>
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