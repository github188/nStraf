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
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function update(){
			if(document.getElementById("prjName").value.trim()=="")
			{
				alert("请选择项目名称");
				return;
			}
			if(document.getElementById("versionNO").value.trim()=="")
			{
				alert("请输入项目版本");
				return;
			}
			if(document.getElementById("prjDefect").value.trim()=="")
			{
				alert("请输入工程反馈缺陷数");
				return;
			}
			if(document.getElementById("codeLine").value.trim()=="0" || document.getElementById("codeLine").value.trim()=="")
			{
				alert("请输入代码行数且不能为0");
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
			if(! Validate(itemName.value, "Number"))
			{
				itemName.value = 0;
				//alert("请输入数字");
				return;
			}
		}
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
		
		function search()
		{
			var url="prjparamsinfo!queryNames.action";
			var params={prjName:$("#prjName").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
			document.getElementById("versionNO").disabled = true;
		}
		$.fn.callbackFun=function (json)
		 {	
		 	document.getElementById("versionNO").disabled = false;	
		  	$("#versionNO option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=1;i<json.length;i++)
				{
					if(json[i].trim() == "fqst")
					{
						nval = "";
					}
					else{
						nval = json[i];
					}
					 $("#versionNO").append("<option value='"+json[i]+"'>"+nval+"</option>");
				 }
				 document.getElementById("versionNO").focus();
				// document.getElementById("prjName").value = document.getElementById("prjNameHidden").value;
				//document.getElementById("versionNO").value = document.getElementById("versionNOHidden").value;
				 document.getElementById("versionNO").value = document.getElementById("versionNOHidden").value;
				 document.getElementById("bodyid").focus();
			}	
		 }
		 
		 function searchPrj()
		{
			var url="prjparamsinfo!queryPrjs.action";
			var params="";
			jQuery.post(url, params, $(document).callbackFun1, 'json');
			document.getElementById("prjName").disabled = true;
		}
		$.fn.callbackFun1=function (json)
		 {
		 	document.getElementById("prjName").disabled = false;	
		  	$("#prjName option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=1;i<json.length;i++)
				{
					 $("#prjName").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
				 document.getElementById("prjName").focus();
				 document.getElementById("prjName").value = document.getElementById("prjNameHidden").value;
				 document.getElementById("bodyid").focus();
				 search();
			}	
		 }
		 function searchType()
		{
			var url="../prjdetail/prjdetailinfo!selectDB.action";
			var params={prjType1:$("#prjType1").val()};
			jQuery.post(url, params, $(document).callbackFun3, 'json');
			document.getElementById("prjName").disabled = true;
			document.getElementById("versionNO").disabled = true;
		}
		$.fn.callbackFun3=function (json)
		 {
		 	document.getElementById("prjName").disabled = false;	
			document.getElementById("versionNO").disabled = false;
		  	$("#prjName option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=1;i<json.length;i++)
				{
					 $("#prjName").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
				 document.getElementById("prjName").focus();
				 document.getElementById("prjName").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
		 
		 function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				{
					var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
					if(tmpStr.length > 1000)
					{
						alert("你输入的字数超过1000个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,1000);
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="searchPrj()">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/prjparams/prjparamsinfo!update.action"   method="post">
		<input type="hidden" name="prjparams.id" value='<s:property value="prjparams.id"/>'/>
        <input name="prjparams.prjType1Hidden" type="hidden" id="prjType1Hidden" value='<s:property value="prjparams.prjType1"/>'>
        <input name="prjparams.prjNameHidden" type="hidden" id="prjNameHidden" value='<s:property value="prjparams.prjName"/>'>
        <input name="prjparams.versionNOHidden" type="hidden" id="versionNOHidden" value='<s:property value="prjparams.versionNO"/>'>
		<table width="70%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="项目附加信息" /></legend>
                    <table width="100%" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    	<tr>
                            <td align="center" bgcolor="#999999" width="35%">项目类别：&nbsp;&nbsp;</td>
                            <td bgcolor="#FFFFFF"> <s:select name="prjparams.prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:360px;" onchange="searchType();"></s:select>
                        <!--<select name="prjparams.prjType1" id="prjType1" onChange="searchType()" style="width:360px">
                            <option value="ATMC" selected>ATMC</option>
                            <option value="DevDll">DevDll</option>
                <option value="SP">SP</option>           
                <option value="View">FEEL View</option>
                <option value="Sith">FEEL Switch</option>
                <option value="SECOne">SECOne</option>
                <option value="LiquidDate">F@ST LiquidDate</option>
                <option value="TellerMaster">TellerMaster</option>
                <option value="dongbao">东保押运综合业务系统</option>
                            </select>--></td>
                          </tr>
                        <tr>
                          <td align="center" bgcolor="#999999"><div align="center">项目名称：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><div align="left">
                          <select name="prjparams.prjName" id="prjName" style="width: 360px" onChange="search();">
                            </select>
                            </div></td> 
                          </tr>
                         <tr>
                          <td align="center" bgcolor="#999999"><div align="center">项目版本：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><select name="prjparams.versionNO" id="versionNO" style="width: 360px"></select></td> 
                      </tr> 
                      <tr>
                          <td align="center" bgcolor="#999999"><div align="center">工程反馈缺陷数：<font color="#FF0000">*</font></td>  
                          <td bgcolor="#FFFFFF"><input name="prjparams.prjDefect" type="text" id="prjDefect" size="57" maxlength="6" value='<s:property value="prjparams.prjDefect"/>' onChange="numVali(this)" onBlur="numVali(this)">&nbsp;</td>
                        </tr>
                        <tr> 
                          <td align="center" bgcolor="#999999"><div align="center">代码行数：</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjparams.codeLine" type="text" id="codeLine" size="57" maxlength="9" value='<s:property value="prjparams.codeLine"/>' onChange="numVali(this)" onBlur="numVali(this)">&nbsp;</td> 
                      </tr> 
                       <tr>
                      	<td align="center" bgcolor="#999999"><div align="center">备注信息：</div></td>
                      	<td bgcolor="#FFFFFF"><textarea name="prjparams.note" rows="4" cols="42"><s:property value="prjparams.note"/></textarea></td>
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
		 	document.getElementById("prjType1").value = document.getElementById("prjType1Hidden").value;
			//alert(document.getElementById("prjType1").value);
			searchType();
	</script>
</body>  
</html> 