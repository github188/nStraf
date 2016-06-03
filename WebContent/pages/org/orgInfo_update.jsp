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
<title><s:text  name="orginfo.modifyinfo"/></title>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="js/orgPages.js"></script>
  <script language="javascript" src="../../js/Validator.js"></script>
<script language="JavaScript" type="text/JavaScript">
function validateOrgInfo(){
	 if(document.getElementById("orgid")!=null){
	 
     if (!Validate('orgid','Require')){  
       alert('<s:text name="check.orgid"/>')
	   orgInfoForm.orgid.focus(); 
	   return false;
	   }
	   }
	 if(document.getElementById("orgname")!=null){ 
     if (!Validate('orgname','Require'))  {
       alert('<s:text name="check.orgname"/>')
	   orgInfoForm.orgname.focus(); 
	   return false;
	   }
	   }
	  if(document.getElementById("orgfullname")!=null){  
     	if (!Validate('orgfullname','Require'))  {
       alert('<s:text name="check.orgfullname"/>')
	   orgInfoForm.orgfullname.focus(); 
	     return false;
	   }
	   }
	  window.returnValue = true;
	  return true;
	   
	 }


function fitLook(){
   if(orgInfoForm.nowstatus.options[orgInfoForm.nowstatus.selectedIndex].value==3)
	 {
		  document.all.divComplaintDate.style.display="block"
		  document.all.divComplaint.style.display="none"
		  document.all.divOrgNameDate.style.display="block"
		  document.all.divOrgName.style.display="none"
		  document.all.divOrgFullNameDate.style.display="block"
		  document.all.divOrgFullName.style.display="none"
		  document.all.divOrgAddress.style.display="block"
	 }
	 else {
		  document.all.divComplaintDate.style.display="none"
		  document.all.divComplaint.style.display="block"
		  document.all.divOrgNameDate.style.display="none"
		  document.all.divOrgName.style.display="block"
		  document.all.divOrgFullNameDate.style.display="none"
		  document.all.divOrgFullName.style.display="block"
		  document.all.divOrgAddress.style.display="none"
   }
 }
 
 function formsubmit(){
 	var oldorgid=document.getElementById("oldorgid").value;
    orgInfoForm.action="<%=request.getContextPath() %>/pages/org/orgMgr!update.action?oldorgid="+oldorgid;
    //alert(orgInfoForm.action);
	 orgInfoForm.submit();
}
 
</script>

</head>
    <body topmargin="0">
      <br>

<form action="/pages/org/orgMgr.do" method="post" name="orgInfoForm" id="orgInfoForm">
  <table width="100%" cellSpacing="0" cellPadding="0">
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="orginfo.postiton"/></legend>
                  
        <table width="100%">
          <tr> 
            <td width="47" align="right">&nbsp;</td>
            
          <td width="708"><s:property value="parentName"/></td>
          </tr>
        </table>
              </fieldset>
          </td>
      </tr>
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  
        <legend><s:text name="orginfo.modifyinfo"/></legend>
                  
        <table width="100%">
          
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td width="174"><div align="right"><s:text name="orginfo.orgid"/><s:text name="label.colon"/></div></td>
            <td align="right" width="550"><div align="left"> 
			<div id="divComplaintDate" style="display:block">
			<%
					OrgInfo orgInfo =((OrgInfo) request.getSession().getAttribute("orgInfoForms"));
					int childnum = orgInfo.getChildnum();
					String parentid = orgInfo.getParentid();	
					String orgid= orgInfo.getOrgid();
					String id = orgInfo.getId();
					String orgname=orgInfo.getOrgname();
					String orgfullname=orgInfo.getOrgfullname();
					String contact = orgInfo.getContact();
					String tel=orgInfo.getTel();
					String address = orgInfo.getAddress();
					String level = orgInfo.getOrgLevel();
					int order = orgInfo.getOrder();
					
					if(childnum>0){		
		    %>
			<%=orgid %><font color="#FF0000">*</font>
           <%
           }
                if(childnum==0){
           %>
			 <input type="text" name="orgid" id="orgid" maxlength="60" readonly="true" value='<%=orgid %>'/><font color="#FF0000">*</font>
			<%} %>
			</div>
			<div id="divComplaint" style="display:none">
			<%=orgid %><font color="#FF0000">*</font>
			</div>
		  <input type="hidden" name="childnum" id="childnum" value='<%=childnum%>'/>
		  <input type="hidden" name="oldorgid" id="oldorgid" value='<%=orgid %>'/>
		<input type="hidden" name="id"  value="<%=id %>"/>
		
			</div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><s:text  name="orginfo.orgname"/><s:text name="label.colon"/></div></td>
            <td align="right" width="550">
			<div id="divOrgNameDate" style="display:block" align="left"> 
			<%
			if(childnum>0)
			{
			 %>
			<input type="text" name="orgname" id="orgname" maxlength="60"  value='<%=orgname %>'/><font color="#FF0000">*</font>			
		 <%}else{ %>
			 <input type="text" name="orgname" id="orgname" maxlength="60" readonly="true" value='<%=orgname %>'/><font color="#FF0000">*</font>			
			<%} %>
				
			<div id="divOrgName" style="display:none" align="left">
			<%=orgname %><font color="#FF0000">*</font>
			</div></td>
          </tr>
          <tr> 
            <td width="27" align="right">&nbsp;</td>
            <td><div align="right"><s:text  name="orginfo.orgfullname"/><s:text name="label.colon"/></div></td>
            <td align="right" width="550">
			<div id="divOrgFullNameDate" style="display:block" align="left"> 
			<input type="text" value="<%=orgfullname %>" name="orgfullname" id="orgfullname" maxlength="100"/>
                 <font color="#FF0000">*</font> </div>
				 
			<div id="divOrgFullName" style="display:none" align="left">
			<%=orgfullname%><font color="#FF0000">*</font>
			</div>
			</td>
          </tr>
	   
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"><s:text name="orginfo.contact"/><s:text name="label.colon"/></div></td>
            <td align="right"><div align="left"> 
			<input type="text" name="contact" maxlength="60" value="<%=contact %>" />
              </div></td>
          </tr>
          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"><s:text  name="orginfo.tel"/><s:text name="label.colon"/></div></td>
            <td align="right"><div align="left"> 
			<input type="text" name="tel" maxlength="50" value="<%=tel %>"
			onkeyup="this.value=this.value.replace(/\D/g,'')" onkeydown="this.value=this.value.replace(/\D/g,'')"/>
                </div></td>
          </tr>
          <tr>
            <td align="right">&nbsp;</td>
            <td width="30%"><div align="right"><s:text name="orginfo.address"/><s:text name="label.colon"/></div></td>
            <td align="right" width="60%" ><div align="left">
			<input type="text" name="address" maxlength="200"  value="<%=address %>"/>
               </div></td>
          </tr>

          <tr> 
            <td align="right">&nbsp;</td>
            <td><div align="right"></div></td>
            <td align="right"><div align="left"> 
			<input type="hidden" name="parentid" value="<%=parentid %>"/>
				<input type="hidden" name="level" value="<%=level %>"/>
					<input type="hidden" name="order" value="<%=order %>"/>

			
                 </div></td>
          </tr>
        </table>
              </fieldset>
          </td>
      </tr>
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="orginfo.show"/></legend>
                  
        <table width="100%">
          <tr> 
            <td width="9%" >&nbsp;</td>
            <td width="91%" >
          
              1、<s:text name="orginfo.desc2" />“<font color="#FF0000">*</font>”<s:text name="orginfo.desc3"/><br>
              2、<s:text name="orginfo.desc4" /></td>
          </tr>
        </table>
          </fieldset>   
          </td>
      </tr>
      <tr>
      	
    <td height="69" align="center">
	 <fieldset class="jui_fieldset" width="100%">
     <legend><s:text name="label.operator"/></legend>
	  <table width="100%" border="0">
        <tr> 
          <td></td>
          <td width="15%">
<div align="center">
              <input type="button" name="btnModal2" value='<s:text name="button.ok"/>' class="MyButton" id="btnModal"   onclick="if(validateOrgInfo()){ formsubmit();}" image="../../images/share/yes1.gif">
            </div></td>
          <td width="25%"><input type="button" name="btnModal" value='<s:text name="button.close"/>' class="MyButton" id="btnModal3"    onclick="closeModal();" image="../../images/share/f_closed.gif" ></td>
          <td width="25%">&nbsp;</td>
        </tr>
      </table>
	    </fieldset>
      <br>	  
    </td>
  </tr>
  </table>	
</form>   
</body>   
</html>