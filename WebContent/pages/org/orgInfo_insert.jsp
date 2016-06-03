<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.domain.OrgInfo" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
  <html:errors />  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text  name="orginfo.addinfo"/></title>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
</head>
 <script language="javascript" src="../../js/tablesort.js"></script>
  <script language="javascript" src="../../js/Validator.js"></script>
<script language="JavaScript" type="text/JavaScript">
function validateOrgInfo(){
     orgInfoForm.orgid.value=orgInfoForm.orgid.value.trim();
     if (!Validate('orgname','Require'))  {
       alert('<s:text name="check.orgname"/>')
	   orgInfoForm.orgname.focus(); 
	   return false;
	   }
     if (!Validate('orgfullname','Require'))  {
       alert('<s:text name="check.orgfullname"/>')
	   orgInfoForm.orgfullname.focus(); 
	     return false;
	   }
	  window.returnValue = true;
	  return true;
	   
	 }
function setFlag()
{
	var flag = orgInfoForm.flag.value;
	if( flag == "add")
		orgInfoForm.newAnOther.checked = true;
}
function closeModal(){
	window.close();
}
function fitLook(){
   if(orgInfoForm.bankChangeType.options[orgInfoForm.bankChangeType.selectedIndex].value==0)
	 {
		  document.all.divComplaintDate.style.display="none"
	 }
	 else if(orgInfoForm.bankChangeType.options[orgInfoForm.bankChangeType.selectedIndex].value==1)
	 {
		  document.all.divComplaintDate.style.display="none"
	 }
	 else if(orgInfoForm.bankChangeType.options[orgInfoForm.bankChangeType.selectedIndex].value==2)
	 {
		  document.all.divComplaintDate.style.display="none"
	 }
	 else if(orgInfoForm.bankChangeType.options[orgInfoForm.bankChangeType.selectedIndex].value==3)
	 {
		  document.all.divComplaintDate.style.display="block"
	 }
	 else {
		  document.all.divComplaintDate.style.display="none"
   	}
 }
 
	function formsubmit(){
	  if(validateOrgInfo()){
		   var if_newAnOther=orgInfoForm.newAnOther.checked;
           orgInfoForm.action="<%=request.getContextPath() %>/pages/org/orgMgr!insert.action?newAnOther="+if_newAnOther;
	       orgInfoForm.submit();
		}
	}

</script>

   <body topmargin="0" onLoad="setFlag()">
      <br>

<form action="/pages/org/orgMgr" method="post" name="orgInfoForm"  id="orgInfoForm">
  <table width="90%" align="center" cellSpacing="0" cellPadding="0" class="popnewdialog1">
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="orginfo.postiton"/></legend>    
        <table width="100%">
          <tr> 
            <td width="47" align="right">&nbsp;</td>
            
          <td width="708"><%=request.getSession().getAttribute("parentName")%></td>
          </tr>
        </table>
              </fieldset>
              <br/>
          </td>
      </tr>
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  
        <legend><s:text name="orginfo.addinfo"/></legend>
                  
        <table width="100%" align="center">
          
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right">机构等级</div></td>
            <td align="center" ><div align="left"> 
			<select name="orgLevel"  id="orgLevel"  >
			 <option value="">===请选择==</option>
			 <option value="4">省行</option>
             <option value="3">分行</option>
   			 <option value="2">支行</option>
             <option value="1">网点</option>      
			</select>
              </div></td>
          </tr>
          
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td width="174"><div align="right"><s:text name="orginfo.orgid"/>：</div></td>
            <td align="right" width="550"><div  align="left"> 
			<input type="text" name="orgid" maxlength="6"  />
                 <font color="#FF0000">*</font> </div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.orgname"/>：</div></td>
            <td align="right" width="550"><div align="left"> 
			<input type="text" name="orgname" maxlength="60"/>
                <font color="#FF0000">*</font> </div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.orgfullname"/>：</div></td>
            <td align="right" width="550"><div align="left"> 
			<input type="text" name="orgfullname" maxlength="100"/>
                 <font color="#FF0000">*</font> </div></td>
          </tr>
          
          
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.contact"/>：</div></td>
            <td align="right"><div align="left"> 
			<input type="text" name="contact" maxlength="60"/>
              </div></td>
          </tr>
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.tel"/>：</div></td>
            <td align="right"><div align="left"> 
			<input type="text" name="tel" maxlength="50"
			onkeyup="this.value=this.value.replace(/\D/g,'')" onkeydown="this.value=this.value.replace(/\D/g,'')"/>
                </div></td>
          </tr>
          <tr>
            <td align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.address"/>：</div></td>
            <td align="right"><div align="left">
			<input type="text" name="address" maxlength="200"/>
               </div></td>
          </tr>
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"></div></td>
            <td align="right">
            	<div align="left"> 							
				<%
					String parentid =((OrgInfo) request.getSession().getAttribute("orgInfoForms")).getParentid();
				%>
					<input type="hidden" name="parentid" value="<%=parentid %>"/>
					<input type="hidden" name="flag" value="flag" id="flag"/>
                </div>
            </td>
          </tr>
        </table>
       </fieldset>
      </td>
    </tr>
    </table>
    <br/>
    <table width="90%" align="center" cellPadding="0" cellSpacing="0"  >
    <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="orginfo.show"/></legend>
                  
        <table width="100%">
          <tr> 
            <td><s:text name="label.admin.content"/></td>
          </tr>
        </table>
          </fieldset>   
          </td>
      </tr>
      <tr>
    <td height="69" align="center">
	 <!--  <fieldset class="jui_fieldset" width="100%">
     <legend><s:text name="label.operator"/></legend>-->
	  <table width="100%" border="0">
        <tr>           
          <td align="center">
          <br/>
          <input type="button" name="btnModal2" value='<s:text name="button.ok"/>' class="MyButton" id="btnModal"   onclick="formsubmit()" image="../../images/share/yes1.gif">            
          <input type="button" name="btnModal" value='<s:text name="button.close"/>' class="MyButton" id="btnModal3"    onclick="closeModal();"  image="../../images/share/f_closed.gif">
          <input type="checkbox" name="newAnOther" id="newAnOther" value="1">
          <label for="newAnOther"><s:text name="label.addAnOther"/></label>
          </td>
        </tr>
      </table>
	   <!--   </fieldset>-->
      <br>
    </td>
  </tr>
  </table>
  </form>
<%@ include file="/inc/showActionMessage.inc"%>
          
    </body>
    
</html>