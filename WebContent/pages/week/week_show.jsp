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
 <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE6" >
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
	 		window.close();
		}
		function save(){
			if(!validateInputInfo())
			{
				return ;
			}
			if(document.getElementById("taskInfo").value.trim()=="")
			{
				alert("请输入任务描述");
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
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = reportInfoForm.start.value.trim();
	var endDate = reportInfoForm.end.value.trim();
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

    var begin=document.getElementsByName(beginDate)[0].value.trim();
    var end=document.getElementsByName(endDate)[0].value.trim();

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
		
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				{
					/*var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
					if(document.getElementsByTagName("textarea")[i].id=="taskInfo" && tmpStr.length > 500)
					{
						alert("你输入的字数超过500个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,500);
					}
					if(document.getElementsByTagName("textarea")[i].id=="note" && tmpStr.length > 250)
					{
						alert("你输入的字数超过250个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,250);
					}*/
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
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/server/serverinfo!save.action" method="post">
    	<input type="hidden" name="finishInfoHidden" id="finishInfoHidden" value='<s:property value="weekReport.finishInfo"/>' />
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
    <fieldset class="jui_fieldset" width="100%" style="margin-left:10px">
                    <legend><s:text name="" />小组周报消息</legend>
                    <table width="79%" align="center">
                    <tr>
                    	<input type="hidden" name="weekReport.id" value='<s:property value="weekReport.id"/>'/>
                        <td align="center" width="10%" nowrap ><div align="center" style="padding-top:3">月份：</div></td>  
                        <td width="40%" bgcolor="#FFFFFF"><input name="monthDay" type="text" id="monthDay" maxlength="100" size="22" value='<s:date name="weekReport.monthDay" format="yyyy-MM"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#336699"></td>
                        <td width="50%" align="right" bgcolor="#FFFFFF"><div style="padding-top:2">组别：<font color="#336699"><s:property value="weekReport.groupName"/></font></div></td>  
                    </tr>
                </table>
                    <table width="98%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" style="border-bottom:none">
                  <br/>
                        
                        <tr>
                            <td width="90" rowspan="2" align="center" bgcolor="#A5A5A5"><font color="#000000">上周总结</font></td>
                          <td width="270" bgcolor="#A5A5A5"><div align="center"><font color="#000000">开始日期：</font></div></td>
                        <td width="314" bgcolor="#FFFFFF"><font color="#000000">
<s:date name="weekReport.startDate" format="yyyy-MM-dd"/>
                          </font></td>
                          <td width="196" align="center" bgcolor="#A5A5A5"><font color="#000000">结束日期：</font></td>
                          <td width="310" bgcolor="#FFFFFF"><font color="#000000">
                          <s:date name="weekReport.endDate" format="yyyy-MM-dd"/></font></td> 
                      </tr>
                      <!--<tr>
                            <td align="center" bgcolor="#336699" >任务概述：</td>
                            <td bgcolor="#FFFFFF" colspan="5" nowrap><textarea name="taskOverview" type="text" id="taskDesc" readonly rows="2" cols="80" style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden " ><s:property value="weekReport.taskOverview"/></textarea></td> 
                      </tr>-->
                      <tr>
                      	<td height="224" colspan="5" bgcolor="#FFFFFF" align="justify"><textarea name="taskDesc2" type="text" id="taskDesc" rows="15" cols="120"  style= "height:230px;BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-y:visible;overflow-y:scroll"  wrap="hard" ></textarea>
                      	</td>
                      	<div id="d1"><s:property value="weekReport.taskDesc" escape="false"/></div>
                      	<script language="javascript">document.getElementById('taskDesc2').appendChild(document.getElementById('d1'))</script>
                      </tr>
                      <tr>
                        <td width="90" rowspan="2" align="center" bgcolor="#A5A5A5"><font color="#000000">本周计划</font></td>
                        <td width="270" bgcolor="#A5A5A5"><div align="center"><font color="#000000">开始日期：</font></div></td>
                        <td width="314" bgcolor="#FFFFFF"><font color="#000000">
<s:date name="weekReport.nextStartDate" format="yyyy-MM-dd"/>
                        </font></td>
                          <td width="196" align="center" bgcolor="#A5A5A5"><font color="#000000">结束日期：</font></td>
                          <td width="310" bgcolor="#FFFFFF"><font color="#000000">
                        <s:date name="weekReport.nextEndDate" format="yyyy-MM-dd"/></font></td> 
                      </tr>
                      <!--<tr>
                            <td align="center" bgcolor="#336699" >任务概述：</td>
                            <td bgcolor="#FFFFFF" colspan="5" nowrap><textarea name="taskOverview" type="text" id="taskDesc" readonly rows="2" cols="80" style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden " ><s:property value="weekReport.taskOverview"/></textarea></td> 
                      </tr>-->
                      <tr>
                      	<td height="224" colspan="5" bgcolor="#FFFFFF" ><textarea name="taskDesc" type="text" id="taskInfo" rows="15" cols="120" readonly style= "height:230px;BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-y:visible overflow-y:scroll" wrap="hard"></textarea></td>
                      	<div id="d2"><s:property value="weekReport.note" escape="false"/></div>
                      	<script language="javascript">document.getElementById('taskInfo').appendChild(document.getElementById('d2'))</script>
                      	
                      </tr>
                      <input type="hidden" name="weekReport.responsor" value='<s:property value="weekReport.responsor"/>'/>
                </table>
                </fieldset>                </td> 
            </tr> 
    </table>
	  <!-- 
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
			document.getElementsByTagName("textarea")[i].style.overflowY = "visible";
			//calcRows = 3;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			//document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1 ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script> -->
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 