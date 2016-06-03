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
		var oldBorderStyle, oldBgColor;
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
			if(document.getElementById("deviceName").value.trim()=="")
			{
				alert("请输入服务器名称");
				return;
			}
			if(document.getElementById("netIP").value.trim()=="")
			{
				alert("请输入IP地址");
				return;
			}
			else if(! Validate(document.getElementById("netIP").value, "ip"))
			{
				alert("请输入正确的IP地址");
				return;
			}
			/*var inputAllCount = document.getElementsByTagName("input").length;
			for(var i=0; i<inputAllCount; i++)
			{
				if(document.getElementsByTagName("input")[i].type=="text" && document.getElementsByTagName("input")[i].value.trim()!="")
				{
					if(document.getElementsByTagName("input")[i].value.trim()>200)
					{
						alert("你在 "+ document.getElementsByTagName("input")[i].parentNode.previousSibling.firstChild.innerHTML + " 输入的内容超过最大长度");
						return;
					}
				}
			}*/
			if(document.getElementById("visitURL").value=="远程桌面")
			{
				document.getElementById("pageURL").value = "";
			}
			else if(document.getElementById("visitURL").value=="页面访问")
			{
				if(document.getElementById("pageURL").value.trim()=="")
				{
					alert("请输入链接地址");
					return;
				}
				else if(! Validate(document.getElementById("pageURL").value, "Url"))
				{
					alert("请输入访问的链接地址，如http://127.0.0.1:8080/controller");
					document.getElementById("pageURL").focus();
					return;
				}
			}
			else if(document.getElementById("visitURL").value=="共享访问")
			{
				document.getElementById("pageURL").value = "\\\\" + document.getElementById("netIP").value;
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
		//var ip = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)((d|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
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
			case "ShareUrl":
				flag = ShareUrl.test(itemNameValue);	  
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
			if(document.getElementById("visitURL").value=="远程桌面")
			{
				if(! Validate(itemName.value, "ip"))
				{
					alert("请输入访问的IP地址，如127.0.0.1");
				}
			}
		}
		
		function cheshit(item){
			if(item.value=="远程桌面")
			{
				document.getElementById("pageURL").disabled = true;
				document.getElementById("pageURL").style.borderStyle = "none";
				document.getElementById("pageURL").style.backgroundColor = "#CCCCCC";
				document.getElementById("ipNameLable").innerHTML = '链接地址';
			}else if(item.value=="页面访问")
			{
				document.getElementById("pageURL").disabled = false;
				document.getElementById("pageURL").style.borderStyle = oldBorderStyle;
				document.getElementById("pageURL").style.backgroundColor = oldBgColor;
				document.getElementById("pageURL").focus();
				document.getElementById("ipNameLable").innerHTML = '链接地址<font color="#FF0000">*</font>';
			}else if(item.value=="共享访问")
			{
				document.getElementById("pageURL").disabled = false;
				document.getElementById("pageURL").readOnly = true;
				document.getElementById("pageURL").style.borderStyle = "none";
				document.getElementById("pageURL").style.backgroundColor = "#CCCCCC";
				document.getElementById("ipNameLable").innerHTML = '链接地址';
				if(document.getElementById("netIP").value.trim()!="")
				{
					document.getElementById("pageURL").value = "\\\\" + document.getElementById("netIP").value;
				}
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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/server/serverinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="服务器信息" /></legend>
                    <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">编号</div></td>  
                          <td bgcolor="#FFFFFF"><input name="server.deviceNo" type="text" id="deviceNumber" maxlength="20" size="20" value=''>&nbsp;</td>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">名称<font color="#FF0000">*</font></div></td>
                          <td bgcolor="#FFFFFF"><input name="server.deviceName" type="text" id="deviceName" maxlength="20" size="20"  value=''>&nbsp;</td>  
                          <td align="center" bgcolor="#999999" width="20%"><div align="center" style="width:63px">IP地址<font color="#FF0000">*</font></div></td>
                          <td bgcolor="#FFFFFF"><input name="server.netIP" type="text" id="netIP" maxlength="20" size="20" value=''>&nbsp;</td> 
                      </tr> 
                      <tr>
                            <td align="center" bgcolor="#999999"><div align="center">负责人</div></td>
                            <td bgcolor="#FFFFFF"><input name="server.responsor" type="text" id="responsor" maxlength="20" size="20" value=''>&nbsp;</td> 
                            <td align="center" bgcolor="#999999"><div align="center">用途&nbsp;&nbsp;</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="server.deviceDesc" type="text" id="deviceDesc" maxlength="100" size="57" value=''>&nbsp;</td> 
                      </tr>
                      <tr>
                      		<td align="center" bgcolor="#999999"><div align="center">访问方式</div></td>
                            <td bgcolor="#FFFFFF"><div align="left"> 
                            <select name="server.visitURL" id="visitURL" style="width: 135px" onChange="cheshit(this)">
                                 <option value="页面访问" selected="true">页面访问</option>
                                 <option value="远程桌面">远程桌面</option>
                                 <option value="共享访问">共享访问</option>
                            </select></div>	
                            </td> 
                      		<td align="center" bgcolor="#999999"><div align="center" id="ipNameLable">链接地址<font color="#FF0000">*</font></div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="server.pageURL" type="text" id="pageURL" maxlength="200" size="57" value='' >&nbsp;</td> 
                      </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
    <script type="text/javascript">
	oldBorderStyle = document.getElementById("pageURL").style.borderStyle;
	oldBgColor = document.getElementById("pageURL").style.backgroundColor;
	</script>
</body>  
</html> 