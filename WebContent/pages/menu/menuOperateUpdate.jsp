<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title><s:text name="menuOperate.title.modify" /></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>

<script type="text/javascript">
 function  SubmitForm(formInfo)
 {
   var  returnValue=validateMenuOperateForm(formInfo)
   if(returnValue)
   { 
      window.returnValue=true
	 }  
   return returnValue
 }
</script>
<html:errors />
<body id="bodyid"  leftmargin="10" topmargin="10" style="overflow:hidden">
<s:iterator value="menuOperate">
<form name="menuOperateForm" action="<%=request.getContextPath() %>/pages/menu/MenuOperate!saveupdate.action?operid=<s:property value='operid'/>" method="post" >
 
  <table width="100%" cellSpacing="0" cellPadding="0">
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="menuOperateForm.title" /></legend>
                  <table>
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.id" /><s:text name="label.colon"/></td>
                          <td width="480" height="20"><s:property value="operid"/>
                          </td>
                      </tr>
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateForm.opername" /></td>
                          <td width="480">
						  <input type="text"  name="opername" value="<s:property value='opername'/>" size="30" maxlength="50"/>
                              <font color="#FF0000">*</font>
                          </td>
                      </tr>
                    <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.clickname" /><s:text name="label.colon"/></td>
                          <td>
						   <input type="text" name="clickname" value='<s:property value="clickname"/>'  size="30" maxlength="200"/>
                                <font color="#FF0000">*</font>
                          </td>
                      </tr>
                     <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.picpath" /><s:text name="label.colon"/></td>
                          <td>
						   <input type="text" name="picpath" value="<s:property value='picpath'/>" size="30" maxlength="200"/>
                          
                          </td>
                      </tr>
                     <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.types" /><s:text name="label.colon"/></td>
                          <td>
				<tm:tmSelect name="types" id="types"  selType="dataDir" path="menuMgr.buttonTypes" />
				<s:if test="types!=null"> 
            <script language="javascript">
                            menuOperateForm.types.value='<s:property value="types"/>';
                            </script>
                            </s:if>
				                       </td>
                      </tr>					  					  					  
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.keys" /><s:text name="label.colon"/></td>
                          <td>
						   <input type="text" name="keys" value="<s:property value='keys'/>" size="30"/>
                      
                          </td>
                      </tr>
                      <tr>
                          <td width="110" align="right">
                               <s:text name="menuInfo.site" /><s:text name="label.colon"/></td>
                          <td>
				<tm:tmSelect name="site" id="site"  selType="dataDir" path="menuMgr.buttonSite"/>
				<s:if  test="site != null"> 
            <script language="javascript">
                            menuOperateForm.site.value='<s:property value="site"/>';
                            </script>
                            </s:if>
				                       </td>
                      </tr>					  					  
 					  
                  </table>
              </fieldset>
          </td>
      </tr>
  </table>
<br>
  <table width="100%" cellSpacing="0" cellPadding="0">
      <tr>
      	<td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="label.title"/></legend>
                  <table width="100%">
                      <tr>
                          <td><s:text name="label.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>
      </tr>
      <tr>
      	<td align="center">
      		<br>
      		<input type="submit" name="Submit1" value="<s:text name="button.ok"/>" class="MyButton"  image="../../images/share/yes1.gif">
          <input type="button" name="Submit2" value="<s:text name="button.close"/>" class="MyButton"  onclick="window.close();" image="../../images/share/f_closed.gif">
      	</td></tr>
  </table>
<form>
</s:iterator>
</body>
</html>