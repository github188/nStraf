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
    <script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var clickFlag = false;
		var oldStartTime, oldEndTime;
		var submitOK = true;
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
			if(!numVali(document.getElementById("TotalCase")))
			{
				return ;
			}
			if(!numVali(document.getElementById("PassCase")))
			{
				return ;
			}
			if(!numVali(document.getElementById("FailCase")))
			{
				return ;
			}
			if($.trim($("#PrjName").val())=='')
			{
				alert("项目名称不能为空");
				return ;
			}
			if($.trim($("#VersionNo").val())=='')
			{
				alert("版本号不能为空");
				return ;
			}
			if($.trim($("#ExecMan").val())=='')
			{
				alert("执行人不能为空");
				return ;
			}
			if($.trim($("#AllTime").val())=='')
			{
				$("#AllTime").val("0");
				return ;
			}
			var reason = document.getElementById("note").value.trim();
			document.getElementById("note").value = reason;
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
		   var Number2= /^(-)?\d+(\.\d+)?$/;
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
			 case "Number2":
				 flag = Number2.test(itemNameValue);
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
				 
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				{
					var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
					if(tmpStr.length > 250)
					{
						alert("你输入的字数超过250个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,250);
					}
				}
			} 
		} 
		
		function listenKey(){ 
			if (document.addEventListener) { 
				document.addEventListener("keyup",getKey,false); 
			} else if (document.attachEvent) { 
				document.attachEvent("onkeyup",getKey); 
			} else { 
				document.onkeyup = getKey; 
			} 
		} 
		listenKey();
		
		function search()
		{	
			var url="autotestrecordinfo!queryNames.action";
			var params={groupName:$("#groupName").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {	
		 	$("#currentName option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					if(json[i].trim()!="")
					{
						$("#currentName").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
					}
				 }
			}
		 }
		 
		 function numClear(itemName){
			itemName.select();
		}
		
		function numVali(itemName) {
			if(! Validate(itemName.value, "Currency"))
			{
				itemName.value = 0;
				alert("用例数请输入数字");
				return false;
			}
			itemName.value = parseFloat(itemName.value);
			return true;
		}
		
		function numClear2(itemName){
			var val = Math.round(parseFloat(itemName.value)*100)/100;
			if(val<0)
			{
				val = -val;
			}
			itemName.value = val;
			itemName.select();
		}
		
		function numVali2(itemName) {
			if(! Validate(itemName.value, "Currency"))
			{
				itemName.value = 0;
				alert("请输入数字");
				return;
			}
		}
		
		function numVali2Blur(itemName)
		{
			var val = Math.round(parseFloat(itemName.value)*100)/100;
			if(val>0)
			{
				val = -val;
			}
			itemName.value = val;
			if(! Validate(itemName.value, "Number2"))
			{
				itemName.value = 0;
				alert("请输入数字");
				return;
			}
		}
		
		function search_1()
		{	
			var url="autotestrecordinfo!queryNames.action";
			var params={groupName:$("#groupName").val()};
			jQuery.post(url, params, $(document).callbackFun_1, 'json');
		}
		$.fn.callbackFun_1=function (json)
		 {	
		 	$("#currentName option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					if(json[i].trim()!="")
					{
						$("#currentName").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
					}
				 }
				 document.getElementById("currentName").value = decodeURI(document.getElementById("currentNameHidden").value);
			}
		 }
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/autotestrecord/autotestrecordinfo!save.action" method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="90%" style="margin-left:10px">
                <table width="80%" align="center">
                    
                </table>
  <table width="90%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" style="border-bottom:none">
                    <br/>
                    <tr>
                    	<td align="center" bgcolor="#A5A5A5">项目名称<font color="#FF0000">*</font></td>
                        <td bgcolor="#FFFFFF"><input name="autoTestRecord.PrjName" type="text" id="PrjName" size="50" style="width:130px;" />
                        <td align="center" bgcolor="#A5A5A5">版本号<font color="#FF0000">*</font></td>
                        <td bgcolor="#FFFFFF"><input name="autoTestRecord.VersionNo" type="text" id="VersionNo" size="50" style="width:130px;" />
                    </tr>
                    <tr>
                    	<td align="center" bgcolor="#A5A5A5">执行状态</td>
                        <td bgcolor="#FFFFFF"><select name="autoTestRecord.Status" style="width:130px;" id="Status">
                				<option value="通过">通过</option>
                				<option value="失败">失败</option>
                				<option value="执行未完成">执行未完成</option>
              			</select> </td>
                        <td align="center" bgcolor="#A5A5A5">执行人<font color="#FF0000">*</font></td>
                        <td bgcolor="#FFFFFF"><input name="autoTestRecord.ExecMan" type="text" id="ExecMan" size="50" style="width:130px;" />
                    </tr>
                    <tr>
                    	<td align="center" bgcolor="#A5A5A5">执行时间</td>
                        <td bgcolor="#FFFFFF"><div align="left"><input name="autoTestRecord.ExecTime" type="text" id="ExecTime" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" value='<s:date name="new java.util.Date()" format="yyyy-MM-dd hh:mm:ss"/>'></div></td> 
                        <td align="center" bgcolor="#A5A5A5">执行总时间</td>
                        <td bgcolor="#FFFFFF"><input name="autoTestRecord.AllTime" type="text" id="AllTime" size="50" style="width:130px;" />
                    </tr>
                        <tr>
                            <td align="center" bgcolor="#A5A5A5">总用例数</td>
                            <td bgcolor="#FFFFFF" colspan="3"><input name="autoTestRecord.TotalCase" type="text" id="TotalCase" size="50" style="width:130px;" />
                      </tr>
                      <tr>
                            <td align="center" bgcolor="#A5A5A5">通过用例数</td>
                            <td bgcolor="#FFFFFF"><input name="autoTestRecord.PassCase" type="text" id="PassCase" size="50" style="width:130px;" />
                            <td align="center" bgcolor="#A5A5A5">失败用例数</td>
                            <td bgcolor="#FFFFFF"><input name="autoTestRecord.FailCase" type="text" id="FailCase" size="50" style="width:130px;" />
                      </tr>
                      <tr>
                      	<td height="44" align="center" bgcolor="#A5A5A5">备注</font></td>
                        <td bgcolor="#FFFFFF" colspan="3"><textarea name="autoTestRecord.note" type="text" id="note" rows="3" cols="50" ></textarea></td>
                      </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<div style="color:#FF0000; margin-left:20px" id="showInfo"></div>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
    <tr>  
           <td>
                 <fieldset width="100%" >
                      <legend><s:text name="label.tips.title"/></legend>
                      <table width="100%" >
                          <tr>
                            <td><s:text name="label.admin.content"/></td>
                          </tr>
                      </table>
                  </fieldset>
      		<br/>
              </td>  
      </tr>
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="okButton"  value='<s:text name="grpInfo.ok" />' class="MyButton"  onclick="save()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
</body>  
</html> 