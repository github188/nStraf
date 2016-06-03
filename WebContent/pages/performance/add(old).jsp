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
			if(document.getElementById("raise_man").value.trim()=="")
			{
				alert("请输入提交者");
				return;
			}
			if(document.getElementById("raise_date").value.trim()=="")
			{
				alert("请输入提交日期");
				return;
			}
			if(document.getElementById("summary").value.trim()=="")
			{
				alert("请输入概要");
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
		
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/suggestion/suggestioninfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="问题/建议" /></legend>
              <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" >提出日期<font color="#FF0000">*</font></div></td>
                          <td width="52%" bgcolor="#FFFFFF">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input name="suggestion.raise_date" type="text" id="raise_date" maxlength="20" size="20" issel="true" isdate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)"></td>
                          <td width="8%" align="center" bgcolor="#999999"><div align="center" >提出者<font color="#FF0000">*</font></div></td>
                          <td width="20%" bgcolor="#FFFFFF"><input name="suggestion.raise_man" type="text" id="raise_man" maxlength="20" size="18"  value=''></td>  
                      </tr> 
                      
                      <tr>
                        <td align="center" bgcolor="#999999"><div align="center">概要<font color="#FF0000">*</font></div></td>
                            <td bgcolor="#FFFFFF" align="center"><input name="suggestion.summary" type="text" id="summary" maxlength="32" size="61" value=''>&nbsp;</td> 
                            <td align="center" bgcolor="#999999"><div align="center">状态<font color="#FF0000">*</font>&nbsp;&nbsp;</div></td>
                    <td colspan="3" bgcolor="#FFFFFF">	<select name="suggestion.status" id="status" align="center" style="width:70%">
                    	<option value='未解决' selected="true">未解决</option>
                        <option value='已解决'>已解决</option>
                        <option value='跟进中'>跟进中</option>
                    </select> &nbsp;</td> 
                      </tr>
                      
                      <tr>
                      		<td align="center" bgcolor="#999999"><div align="center">描述<font color="#FF0000">*</font></div></td>
                            <td bgcolor="#FFFFFF"><div align="center">
                              <textarea cols="50" rows="5" name="suggestion.desc" id="desc"></textarea>
                            </div>                        </td> 
                      		<td align="center" bgcolor="#999999"><div align="center" id="ipNameLable">优先级<font color="#FF0000">*</font></div></td>
                        <td colspan="3" bgcolor="#FFFFFF">
                        <select name="suggestion.priority" id="priority" align="center" style="width:70%">
                    	<option value='高'>高</option>
                        <option value='中'>中</option>
                        <option value='低'>低</option>
                    </select> 
                        &nbsp;</td> 
                      </tr>
                      <tr>
                      	<td align="center" >解决措施</td>
                        <td align="center" > <textarea cols="50" rows="5" name="suggestion.solution" id="solution" ></textarea></td>
                        <td align="center" >解决人</td>
                        <td><input name="suggestion.solution_man" type="text" id="solution_man" maxlength="20" size="18" value=''>&nbsp;</td>
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
			<td align="center"><input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onClick="save()"  image="../../images/share/yes1.gif">
			  <input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif">			</td> 
  		</tr>  
 	</table> 
			
</form>
    <script type="text/javascript">
	oldBorderStyle = document.getElementById("pageURL").style.borderStyle;
	oldBgColor = document.getElementById("pageURL").style.backgroundColor;
	</script>
</body>  
</html> 