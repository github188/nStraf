<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<html>
  <html:errors />  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><bean:message  key="orginfo.addinfo" bundle="org"/></title>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>

</head>
  <script language="JavaScript" type="text/JavaScript"></script>
 <script language="javascript" src="../../js/tablesort.js"></script>
  <script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="js/geogPages.js"></script>
<script language="JavaScript" type="text/JavaScript">
function validateOrgInfo(){
     geogAreaForm.areaid.value=geogAreaForm.areaid.value.trim();
     if (!Validate('areaid','Require')){  
       alert('<bean:message  key="check.areaid" bundle="geog"/>')
	   geogAreaForm.areaid.focus(); 
	   return false;
	   }
      if (!Validate('areaid','English')){  
       if (!Validate('areaid','Number')){  
       alert('<bean:message  key="geog.validate.areaid" bundle="geog"/>')
	   geogAreaForm.areaid.focus(); 
	   return false;
	   }}
     if (!Validate('name','Require'))  {
       alert('<bean:message  key="check.areaname" bundle="geog"/>')
	   geogAreaForm.name.focus(); 
	   return false;
	   }
	   
	  window.returnValue=true;
	  return true;
	   
	 }

function setFlag()
{
	var flag = geogAreaForm.flag.value;
	if( flag == "add")
		geogAreaForm.newAnOther.checked = true;
}
</script>

   <body topmargin="0" onLoad="setFlag()">
      <br>

<html:form action="/pages/geog/areaMgr.do" method="post" >
  <table width="100%" cellSpacing="0" cellPadding="0">
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><bean:message  key="geogarea.postiton" bundle="geog"/></legend>
                  
        <table width="100%">
          <tr> 
            <td width="47" align="right">&nbsp;</td>
            <td width="708"><bean:write name="parentName"/></td>
          </tr>
        </table>
              </fieldset>
          </td>
      </tr>
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  
        <legend><bean:message  key="geogarea.addinfo" bundle="geog"/></legend>
                  
        <table width="100%">
          
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td width="174"><div align="right"><bean:message  key="geogarea.areaid" bundle="geog"/>：</div></td>
            <td align="right" width="550"><div align="left"> 
			<input type="text" name="areaid" maxlength="32"  onblur="this.value=this.value.trim()"/>
                 <font color="#FF0000">*</font> </div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><bean:message  key="geogarea.areaname" bundle="geog"/>：</div></td>
            <td align="right" width="550"><div align="left"> 
			<input type="text" name="name" maxlength="50"/>
                <font color="#FF0000">*</font> </div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><bean:message  key="geogarea.note" bundle="geog"/>：</div></td>
            <td align="right" width="550"><div align="left"> 
			<input type="text" name="note" maxlength="100"/>
                 </div></td>
          </tr>
       
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"></div></td>
            <td align="right"><div align="left"> 
			<html:hidden property="parentid" name="geogAreaForms"/>
			<html:hidden property="action" value="insert"/>
			<html:hidden property="flag" />
                 </div></td>
          </tr>
        </table>
              </fieldset>
          </td>
      </tr>
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><bean:message  key="geogarea.show" bundle="geog"/></legend>
                  
        <table width="100%">
          <tr> 
            <td width="9%" >&nbsp;</td>
            <td width="91%" >
              1、<bean:message  key="geogarea.desc1" bundle="geog"/>“<font color="#FF0000">*</font>”<bean:message  key="geogarea.desc2" bundle="geog"/><br>
           </td>
          </tr>
        </table>
          </fieldset>   
          </td>
      </tr>
      <tr>
      	
    <td height="69" align="center">
	 <fieldset class="jui_fieldset" width="100%">
     <legend><bean:message key="label.operator" bundle="geog"/></legend>
	  <table width="100%" border="0">
        <tr> 
          <td align="center">    
          <input type="button" name="btnModal2" value='<bean:message  key="button.ok"/>' class="MyButton" id="btnModal"    onclick="if(validateOrgInfo()){ formSubmit();}" image="../../images/share/yes1.gif">     
          <input type="button" name="btnModal" value='<bean:message  key="button.close"/>' class="MyButton" id="btnModal3"    onclick="closeModal();"  image="../../images/share/f_closed.gif">
 		  <input type="checkbox" name="newAnOther" id="newAnOther" value="1">
          <label for=newAnOther><bean:message key="label.addAnOther"/></label>
          </td>
        </tr>
      </table>
	    </fieldset>
      <br>
    </td>
  </tr>
  </table>
 		  <!-- messages -->
		  <logic:messagesPresent message="true" >
        <fieldset class="jui_fieldset" width="100%">
            <legend><bean:message  key="operator.backinfo" bundle="org"/></legend>
         <table width="100%">
          <tr> 
            <td width="10%" align="left">&nbsp;</td>
          <td width="90%" align="left">
		    <font color="#FF0000">
				<html:messages id="msg" message="true" >
				   <bean:write name="msg"  filter="false"/>
				</html:messages>
			</font>
			  </td>
          </tr>
        </table>
		    </fieldset>
	  </logic:messagesPresent>	
</html:form>
          
    </body>
    
</html>