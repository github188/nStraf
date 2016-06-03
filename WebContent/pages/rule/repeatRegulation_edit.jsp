<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
 <link href="../../css/css_v2.css" type=text/css rel=stylesheet>
 <script type="text/javascript" src="../../js/tablesort.js"></script>
 <script type="text/javascript" src="../../js/Validator.js"></script>
 <script type="text/javascript" src="../../js/DateValidator.js"></script>
 <script type="text/javascript">
	function closeModal(){
	 	window.close();
	}
	function update(){
		repeatRegulationForm.submit();
	}
 </script>
 
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name=repeatRegulationForm  action="<%=request.getContextPath()%>/pages/rule/repeatRegulation!update.action"   method="post"    >
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<fieldset  class="popnewdialog1" style="padding-bottom: 5px;" width="100%">
					<legend><s:text name="repeat.modify" /></legend>
		 			<s:iterator value="repeatRegulation" id="repeatRegulation">
		 			<input type="hidden" name="id" value="<s:property value='id' />" /> 
					<table width="100%" align="center">
						<tr>
							<td width="30%" align="right"> 
           						<div align="right"><s:text name="repeat.info.num"/></div>
           					</td>
           					<td width="5%"  align="left"><s:text name="label.colon"/></td>
           					<td width="65%"  align="left"> <input name="repeatnum" type="text" id="repeatnum" size="30" maxlength="2" value="<s:property value='repeatnum' />"> 
             					<font color="#FF0000">*</font> 
            				</td>
        				</tr> 
						<tr>
							<td width="30%" align="right"> 
				           		<div align="right"><s:text name="repeat.info.dealMode"/></div>
				           	</td>
				           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
				           	<td width="65%" align="left">
				           		<tm:tmSelect name="dealwithMode" id="dealwithMode" selType="dataDir" beanName="repeatRegulation"  path="ruleMgr.processModel" /> 
				             	<font color="#FF0000">*</font> 
				            </td>
				        </tr> 
						<tr>
							<td width="30%" align="right"> 
				           		<div align="right"><s:text name="repeat.info.screenMode"/></div>
				           	</td>
				           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
				           	<td width="65%"  align="left"> 
				           	
				           		<tm:tmSelect name="creenMode" id="creenMode" selType="dataDir" beanName="repeatRegulation"  path="ruleMgr.greenModel" /> 
				             	<font color="#FF0000">*</font> 
				            </td>
				        </tr> 
						<tr>
							<td width="30%" align="right"> 
				           		<div align="right"><s:text name="repeat.info.accountMode"/></div>
				           	</td>
				           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
				           	<td width="65%"  align="left"> 
								<tm:tmSelect name="enterAccountMode" id="enterAccountMode" selType="dataDir" beanName="repeatRegulation"  path="ruleMgr.accountModel" /> 
				             	<font color="#FF0000">*</font> 
				            </td>
				        </tr> 
						<tr>
							<td width="30%" align="right"> 
				           		<div align="right"><s:text name="repeat.info.logMode"/></div>
				           	</td>
				           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
				           	<td width="65%"  align="left">
								<tm:tmSelect name="logMode" id="logMode" selType="dataDir" beanName="repeatRegulation"  path="ruleMgr.logModel" /> 
				            </td>
				        </tr>
				        
				        <tr>
							<td width="30%" align="right"> 
				           		<div align="right"><s:text name="rule.list.audit"/></div>
				           	</td>
				           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
				           	<td width="65%"  align="left">
				           		<SELECT name="reversionName" id="reversionName">
				           		<s:iterator value="usrList" id="usr">
				           			<option value='<s:property value="userid" />'><s:property value="userid" /></option>
				           		</s:iterator>
				           		</SELECT> 
				             	<font color="#FF0000">*</font> 
				            </td>
				        </tr>
					</table>
		   			</s:iterator>
					</fieldset>
				</td> 
			</tr>
			<tr>
		      	<td>
		              <fieldset class="jui_fieldset" width="100%">
		                  <legend><s:text name="label.title"/></legend>
		                  <table width="100%" >
		                      <tr>
		                          <td><s:text name="label.content"/></td>
		                      </tr>
		                  </table>
		              </fieldset>
		          </td>
		      </tr>
			 
			<tr style="padding-top: 5px;">
				<td align="center"> 
		 		<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
		  	</tr>  
		 </table> 
 </form>
  
</body>  
</html> 

