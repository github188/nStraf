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
		 	window.close();
		}
		function save(){
			if(!validateInputStart())
			{
				return ;
			}
			if(document.getElementById("prise").value.trim()=="")
			{
				alert("请输入任务概述");
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
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = document.getElementById("startDate").value.trim();
	var endDate = document.getElementById("endDate").value.trim();
	    if(thisDate.length==0)
		{
			alert('请输入开始日期!');
			return false;
		}
		if(endDate.length==0)
		{
			alert('请输入结束日期!');
			return false;
		}
		if(thisDate.length>0&&endDate.length>0){
		var v = re.test(endDate);
		var a = re1.test(thisDate);
		if(!v||!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 
		 if(!DateValidate('start','end'))
		 {
		 	alert('开始日期大于结束日期，请重新输入！');
		 	return false;
		 }
	 }else if(thisDate.length>0&&endDate.length==0){
	     
		  var a = re1.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }else if(thisDate.length==0&&endDate.length>0){
	      var v = re.test(endDate);
		if(!v){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }
	  return true;
	 }
	 
	 function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementById("startDate").value.trim();
    var end=document.getElementById("endDate").value.trim();
	var flag=true;

    /*if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;*/
	if(Require.test(begin) && Require.test(end))
	{
		var beginStr = begin.split("-");
		var endStr = end.split("-");
		if(parseInt(beginStr[0]) > parseInt(endStr[0]))
		{
			flag = false;
		}
		else if(parseInt(beginStr[0]) == parseInt(endStr[0]))
		{
			if(parseInt(beginStr[1]) > parseInt(endStr[1]))
			{
				flag = false;
			}
			else if(parseInt(beginStr[1]) == parseInt(endStr[1]))
			{
				if(parseInt(beginStr[2]) > parseInt(endStr[2]))
				{
					flag = false;
				}
			}
		}
	}
   return flag;

}
		
		function validateInputStart(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;    
  	var thisDate = document.getElementById("start").value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 
		 function validateInputStartMonth(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;    
  	var thisDate = document.getElementById("startDate").value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
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
			var url="behaviorinfo!checkExist.action";
			var params={startDate:$("#startDate").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {	
		 	$("#username option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				//for(var i=json.length-1;i>=0;i--)
				{
					    $("#username").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
			}
		 }
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/behavior/behaviorinfo!save.action" method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="90%" style="margin-left:10px">
                    <legend>
                    月度任务
                    </legend>
                <table width="80%" align="center">
                    <tr>
                        <td align="left" bgcolor="#FFFFFF">姓名：
                        	<input name="monthMission.currentName" type="text" id="currentName" maxlength="100" size="22" value='<s:property value="monthMission.currentName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#336699; padding-top:2"></td>
                        </td> 
                        <td align="right" bgcolor="#FFFFFF">组别：
                        <input name="monthMission.groupName" type="text" id="groupName" maxlength="100" size="22" value='<s:property value="monthMission.groupName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#336699; padding-top:2"></td>
                    </tr>
                </table>
  <table width="90%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" style="border-bottom:none">
                    <br/>
                        <tr>
                            <td align="center" bgcolor="#A5A5A5" width="20%">月份</td>
                            <td bgcolor="#FFFFFF"><div align="left"><input name="monthMission.monthDate" type="text" id="start" size="22" readonly value='<s:property value="monthMission.monthDate"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000"></div></td> 
                      </tr>
                      	<td align="center" bgcolor="#A5A5A5">月度考核分</td>
                        <td bgcolor="#FFFFFF"><div align="left"><input name="monthMission.examScore" type="text" id="start" size="22" readonly value='<s:property value="monthMission.examScore"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000"></div></td>
                      </tr>
                      <tr>
                      	<td align="center" bgcolor="#A5A5A5">考核等级</td>
                        <td bgcolor="#FFFFFF"><div align="left"><input name="monthMission.examRank" type="text" id="start" size="22" readonly value='<s:property value="monthMission.examRank"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000"></div></td>
                      </tr>
                      <tr>
                      	<td height="44" align="center" bgcolor="#A5A5A5">备注</td>
                        <td bgcolor="#FFFFFF" colspan="2"><textarea name="monthMission.note" type="text" id="note" rows="3" cols="56" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "><s:property value="monthMission.note"/></textarea></td>
                      </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<div style="color:#FF0000; margin-left:20px" id="showInfo"></div>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 	
 	</form>
    <script type="text/javascript">
	for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
	{
		var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
		var tmpStrlen = tmpStr.length;
		var tmpLfCount = tmpStr.split("\n").length;
		var tmpCols = parseInt(document.getElementsByTagName("textarea")[i].cols);
		var calcRows = Math.ceil(tmpStrlen/tmpCols);
		var nosetFlag = false;
		if(calcRows == 0)
		{
			calcRows = 1;
		}
		if(calcRows>=4)
		{
			document.getElementsByTagName("textarea")[i].style.overflowY = "auto";
			calcRows = 3;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1 ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script>
</body>  
</html> 