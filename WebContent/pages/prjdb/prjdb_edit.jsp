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
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function update(){
			if(document.getElementById("prjName").value.trim()=="0")
			{
				alert("请选择项目名称");
				return;
			}
			if(document.getElementById("dbIp").value.trim()=="")
			{
				alert("请输入数据库路径");
				return;
			}
			if(document.getElementById("dbName").value.trim()=="")
			{
				alert("请输入数据库名称");
				return;
			}
			if(document.getElementById("dbUsername").value.trim()=="")
			{
				alert("请输入数据库用户名");
				return;
			}
			if(! Validate(document.getElementById("dbIp").value, "ip"))
			{
				alert("请输入正确的IP地址");
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
		   var ip=/^(((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))$/;
		
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
			case "ip":
				 flag = ip.test(itemNameValue);
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
			var tmp = itemName.value;
			itemName.value = tmp.trim();
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/prjdb/prjdbinfo!update.action"   method="post">
		<input type="hidden" name="prjdb.id" value='<s:property value="prjdb.id"/>'/>
        <input name="prjdb.prjNameHidden" type="hidden" id="prjNameHidden" value='<s:property value="prjdb.prjName"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="数据库配置信息" /></legend>
                    <table width="60%" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                          <td align="center" width="30%" bgcolor="#999999"><div align="center">项目名称：<font color="#FF0000">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF" width="20%"><div align="left">
                          <input name="prjdb.prjName" id="prjName" style="width: 197px"/> 
                        <!--  <select name="prjdb.prjName" id="prjName" style="width: 197px">
                            <option value="0">&nbsp;</option>
                                <option value="ATMC">ATMC</option>
                                <option value="DevDll">DevDll</option>
                                <option value="SP">SP</option>
                <option value="View">FEEL View</option>
                <option value="Sith">FEEL Switch</option>
                <option value="SECOne">SECOne</option>
                <option value="LiquidDate">F@ST LiquidDate</option>
                <option value="TellerMaster">TellerMaster</option>
                <option value="dongbao">东保押运综合业务系统</option>
                            </select>-->
                            </div></td> 
                           </tr>
                           <tr>
                          <td align="center" bgcolor="#999999"><div align="center">数据库路径：<font color="#FF0000">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjdb.dbIp" type="text" id="dbIp" size="30" maxlength="15" onChange="numVali(this)" onBlur="numVali(this)" value='<s:property value="prjdb.dbIp"/>'>&nbsp;</td> 
                      </tr> 
                      <tr>
                          <td align="center" bgcolor="#999999"><div align="center">数据库名称：<font color="#FF0000">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjdb.dbName" type="text" id="dbName" size="30" maxlength="30" value='<s:property value="prjdb.dbName"/>' onChange="numVali(this)" onBlur="numVali(this)">&nbsp;</td> 
                      </tr>
                      <tr>
                          <td align="center" bgcolor="#999999"><div align="center">数据库用户名：<font color="#FF0000">*</font>&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjdb.dbUsername" type="text" id="dbUsername" size="30" maxlength="9" value='<s:property value="prjdb.dbUsername"/>' onChange="numVali(this)" onBlur="numVali(this)">&nbsp;</td> 
                      </tr> 
                      <tr>
                          <td align="center" bgcolor="#999999"><div align="center">数据库用户密码：&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjdb.dbPassword" type="password" id="dbPassword" size="30" maxlength="15" value='<s:property value="prjdb.dbPassword"/>'>&nbsp;</td> 
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
    <script type="text/javascript">
		document.getElementById("prjName").value = document.getElementById("prjNameHidden").value;
	</script>
</body>  
</html> 