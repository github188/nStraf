<!--20100107 14:00-->
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
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function update(){
			if(document.getElementById("toolName").value.trim()=="")
			{
				alert("请输入工具名称");
				return;
			}
			if(document.getElementById("visitURL").value.trim()=="")
			{
				alert("请输入访问地址，如http://ip:port或ftp://ip:port");
				return;
			}
			if(! Validate(document.getElementById("visitURL").value, "Url"))
			{
				alert("请输入访问地址，如http://ip:port或ftp://ip:port");
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^(http|ftp|svn):\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		   var Currency = /^\d+(\.\d+)?$/;
		   var Number= /^\d+$/;
		   var Zip = /^[1-9]\d{5}$/;
		   var QQ = /^[1-9]\d{4,8}$/;
		   var Integer = /^[-\+]?\d+$/;
		   var integer = /^[+]?\d+$/;
		   var Double= /^[-\+]?\d+(\.\d+)?$/;
		   var double = /^[+]?\d+(\.\d+)?$/;
		   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		   var Chinese = /^[\u0391-\uFFE5]+$/;
		   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		
			//var itemNameValue=document.getElementsByName(itemName)[0].value
			var itemNameValue=itemName;
			
				var flag;
			switch(pattern){ 
			 case "Require":
				 flag = Require.test(itemNameValue);
				  break;
			 case "Email":
				 flag = Email.test(itemNameValue);
				  break;
			 case "Phone":
				 flag = Phone.test(itemNameValue);
				  break;
			 case "Mobile":
				 flag = Mobile.test(itemNameValue);
				  break;
			 case "Url":
				 flag = Url.test(itemNameValue);
				  break;
			 case "IdCard":
				 flag = IdCard.test(itemNameValue);
				  break;
			 case "Currency":
				 flag = Currency.test(itemNameValue);
				  break;
			 case "Number":
				 flag = Number.test(itemNameValue);
					  break;
			 case "Zip":
				 flag = Zip.test(itemNameValue);
				  break;
			 case "QQ":
				 flag = QQ.test(itemNameValue);
				  break;
			 case "integer":
				 flag = integer.test(itemNameValue);
				  break;		  
			 case "Integer":
			// if (itemNameValue.length>0)
				 flag = Integer.test(itemNameValue);
			//else 
			//	flag=true
				  break;
			 case "Double":
				 flag = Double.test(itemNameValue);
				  break;
			 case "double":
					 flag = double.test(itemNameValue);
					  break;
			 case "English":
				 flag = English.test(itemNameValue);
				  break;
			 case "Chinese":
				 flag = Chinese.test(itemNameValue);
				  break;
			 case "BankCard":
				 flag = BankCard.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Url"))
			{
				alert("请输入访问地址，如http://ip:port或ftp://ip:port");
			}
		}
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/tool/toolinfo!update.action"   method="post">
		<input type="hidden" name="tool.id" value='<s:property value="tool.id"/>'/>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="工作报告信息" /></legend>
                    <table width="100%" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">名称：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><input name="tool.toolName" type="text" id="toolName" size="30" maxlength="30" value='<s:property value="tool.toolName"/>'>&nbsp;</td> 
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">版本：&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="tool.versionNO" type="text" id="version" size="30" maxlength="30" value='<s:property value="tool.versionNO"/>'>&nbsp;</td> 
                      </tr> 
                      <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">分类：&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF">
                          	<select name="tool.assort" id="catalog" style="width: 199px">
                                 <option value="0" selected="true">硬件检测</option>
                                 <option value="1">硬件检测(XFS)</option>
                                 <option value="2">介质升级及硬件检测</option>
                                 <option value="3">介质升级</option>
                                 <option value="4">联网测试</option>
                                 <option value="5">自动化测试</option>
                                 <option value="6">白盒测试</option>
                                 <option value="7">辅助测试</option>
                            </select>
                           </td> 
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">属性：&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF">
                          <select name="tool.property" id="attribution" style="width: 199px">
                                 <option value="0" selected="true">自研</option>
                                 <option value="1">共享</option>
                                 <option value="2">破解</option>
                                 <option value="3">正版</option>
                                 <option value="4">免费</option>
                           </select>
                           </td> 
                      </tr> 
                      <tr>
                            <td align="center" bgcolor="#999999"><div align="center">来源：&nbsp;&nbsp;</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="tool.source" type="text" id="provider" size="82" maxlength="80" value='<s:property value="tool.source"/>'></td> 
                      </tr>
                        <tr>
                            <td align="center" bgcolor="#999999"><div align="center">访问地址<font color="#FF0000">*</font></div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="tool.visitURL" type="text" id="visitURL" size="82" value='<s:property value="tool.visitURL"/>'></td> 
                      </tr>
                        <tr>
                            <td align="center" bgcolor="#999999"><div align="center">工具用途</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><textarea name="tool.toolDesc" type="text" id="toolDesc" cols="61" rows="4" ><s:property value="tool.toolDesc"/></textarea></td> 
                      </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 