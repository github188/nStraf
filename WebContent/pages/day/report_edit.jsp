<!--本页面为edit新版页面，修改日期2010-1-5，后台字段未改动，加入了strut迭代器-->
<!--20100106 18:23-->
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
		var allClearFlag = false;
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
	 		window.close();
		}
		function update(){
		
			if(!validateInputInfo())
			{
				return ;
			}
			var iFuskTotal = document.getElementsByName("taskDesc").length;   
			var Count = 1;
			for(var ind=0; ind<iFuskTotal; ind++){
				var prjName = document.getElementsByName("prjName")[ind];

				if(prjName!=null)
				{
					if(prjName.value.trim() == "")
					{
						alert("请输入项目名称");
						return;
					}
					if(prjName.value.trim() == "--------黑盒测试组项目列表--------" || prjName.value.trim() == "------自动化测试组项目列表------" || prjName.value.trim() == "--------白盒测试组项目列表--------" || prjName.value.trim() == "--------质量管理组项目列表--------" || prjName.value.trim() == "------------其他事项列表------------")
					{
						alert("第"+ Count +"项任务的项目名称请确认是否正确");
						prjName.value = "";
						return;
					}
				}
				Count++;
			}
			var todayAllTime = Math.round(parseFloat(document.getElementById("sumShow").innerHTML)*100)/100;
			if(todayAllTime>24 || isNaN(todayAllTime))
			{
				alert("一天的工作时间大于24小时，请重新检查下工作时间");
				return;
			}
			var curCount = 1;
			var bSubmit = false;
			var iFuskIndex = document.getElementsByName("subtotal").length;
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=0; ind<iFuskIndex; ind++){
				var taskDivPtr = document.getElementsByName("prjName")[ind].parentNode.parentNode.parentNode.parentNode;
				if(taskDivPtr!=null){
					if(taskDivPtr.getElementsByTagName("select")[0].value.trim() == "0")
					{
						alert("请选择第" + curCount + "项的项目名称");
						return;
					}
					if(taskDivPtr.getElementsByTagName("select")[1].value.trim() == "0%")
					{
						alert("请选择第" + curCount + "项的完成情况");
						return;
					}
					var input7 = parseFloat(taskDivPtr.getElementsByTagName("input")[7].value.trim(),10);
					if(Math.round(input7*100)/100 == 0 && taskDivPtr.getElementsByTagName("select")[2].value.trim() != "延迟"){
						alert("请输入第" + curCount + "项的任务工时");
						return;
					}
					if(taskDivPtr.getElementsByTagName("textarea")[0].value.trim() == ""){
						alert("请输入第" + curCount + "项的任务描述");
						return;
					}
					if(taskDivPtr.getElementsByTagName("textarea")[0].value.trim().length > 250)
					{
						alert("第" + curCount + "项的任务描述内容超过250个字了");
						return; 
					}
					if(taskDivPtr.getElementsByTagName("input")[8].value.trim()!="" && taskDivPtr.getElementsByTagName("input")[8].value.trim().length > 250)
					{
						alert("第" + curCount + "项的交付件描述内容超过250个字了");
						return;
					}
					if(taskDivPtr.getElementsByTagName("input")[9].value.trim()!="" && taskDivPtr.getElementsByTagName("input")[9].value.trim().length > 250)
					{
						alert("第" + curCount + "项的偏差原因内容超过250个字了");
						return;
					}
					bSubmit = true;
				}
				if(taskDivPtr!=null)
				{
					curCount++;
				}
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
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
		   return flag
		
		}
				
		function numClear(itemName){
			itemName.select();
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Currency"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>20){
					itemName.value = 0;
					alert("请输入20以内的数字");
				}			
			}
			var itemPPtr = itemName.parentNode.parentNode.parentNode;
			for(var ind=0; ind<itemPPtr.getElementsByTagName("input").length; ind++){
				if(itemPPtr.getElementsByTagName("input")[ind].name != "subtotal"){
					var tmpInt = parseFloat(itemPPtr.getElementsByTagName("input")[ind].value);
					if(isNaN(tmpInt)){
						tmpInt = 0;
					}
					ret += tmpInt;
				}
			}
			for(var ind=0; ind<itemPPtr.parentNode.getElementsByTagName("input").length; ind++){
				if(itemPPtr.parentNode.getElementsByTagName("input")[ind].name == "subtotal"){
					itemPPtr.parentNode.getElementsByTagName("input")[ind].value = Math.round(ret*100)/100;
				}
			}
			ret = 0;
			var tmpRet = 0;
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				tmpRet = parseFloat(document.getElementsByName("subtotal")[ind].value);
				if(isNaN(tmpRet))
				{
					tmpRet = 0;
				}
				ret += tmpRet;
			}
			document.getElementById("sumShow").innerHTML = Math.round(ret*100)/100;
		}
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				//alert("必填项不能为空");
				//val.value = "今天没工作任务";
			}
		}
		
		function addAgain(){
			var iFuskTotal = document.getElementsByName("taskDesc").length;
			var curCount = 1;
			for(var ind=0; ind<iFuskTotal; ind++){
				var taskDivPtrS = document.getElementsByName("taskDesc")[ind];
				var taskDivPtrJ = document.getElementsByName("prjName")[ind];
				var taskDivPtrPgr = document.getElementsByName("finishRate")[ind];
				var taskDivPtrST = document.getElementsByName("subtotal")[ind].value.trim();
				if(taskDivPtrJ!=null)
				{
					if(taskDivPtrJ.value == "")
					{
						alert("请输入项目名称");
						return;
					}
					if(taskDivPtrJ.value.trim() == "--------黑盒测试组项目列表--------" || taskDivPtrJ.value.trim() == "------自动化测试组项目列表------" || taskDivPtrJ.value.trim() == "--------白盒测试组项目列表--------" || taskDivPtrJ.value.trim() == "--------质量管理组项目列表--------" || taskDivPtrJ.value.trim() == "------------其他事项列表------------")
					{
						alert("第"+ curCount +"项任务的项目名称请确认是否正确");
						taskDivPtrJ.value = "";
						return;
					}
				}
				if(taskDivPtrPgr!=null)
				{
					if(taskDivPtrPgr.value.trim() == "0%")
					{
						alert("请选择完成情况");
						return;
					}
				}
				if(taskDivPtrS!=null)
				{
					if(taskDivPtrS.value.trim() == "")
					{
						alert("请输入任务描述");
						return;
					}
				}
				var input7 = parseFloat(taskDivPtrST);
				if(Math.round(input7*100)/100==0)
				{
					alert("请输入任务工时");
					return;
				}
				curCount++;
			}

			var taskDivPtr, iFuskIndex;
			if(document.getElementsByName("subtotal").length >= iTaskTotalpd)
			{
				alert("哇，一天打了这么多酱油。可惜本页面限制最多新增" + iTaskTotalpd + "项任务");
				return;
			}
			iTaskIndex++;
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			//var taskDivPtr = taskDivPPtr.childNodes[tableTaskIndex];
			var taskDivPtr = document.getElementById("dateTable").nextSibling;
			iFuskIndex = document.getElementsByTagName("table").length;
			for(var ind=0; ind<iFuskIndex; ind++){
				taskDivPtr = document.getElementsByTagName("table")[ind];
				if(taskDivPtr!=null && taskDivPtr.id!="")
				{
					if(taskDivPtr.id.indexOf("task")>=0)
					{break;}
				}
			}			
			var taskDivPtr2 = taskDivPtr.cloneNode(true);
			taskDivPtr2.id = "task" + iTaskIndex;
			for(var ind=0; ind<taskDivPtr2.getElementsByTagName("textarea").length; ind++){
				taskDivPtr2.getElementsByTagName("textarea")[ind].value="";
			}
			taskDivPtr2.getElementsByTagName("select")[0].value = "0";
			taskDivPtr2.getElementsByTagName("select")[1].value = "0%";
			taskDivPtr2.getElementsByTagName("select")[2].value = "按时";
			for(var ind=0; ind<taskDivPtr2.getElementsByTagName("input").length; ind++){
				if(taskDivPtr2.getElementsByTagName("input")[ind].type=="text"){
					if(taskDivPtr2.getElementsByTagName("input")[ind].name != "attachment" && taskDivPtr2.getElementsByTagName("input")[ind].name != "taskReason")
					{
						taskDivPtr2.getElementsByTagName("input")[ind].value="0";
					}
					else
					{
						taskDivPtr2.getElementsByTagName("input")[ind].value="";
					}
				}
			}
			taskDivPPtr.appendChild(taskDivPtr2);
			// add index
			document.getElementsByName("prjName")[iTaskIndex-1].value="";
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind+1) ;				
			}
		}
		
		function delAgain(){
			//var iFuskIndex = document.getElementsByName("subtotal").length;
			var iFuskIndex = 50;
			var tipFlag = false;
			for(var ind=0; ind<iFuskIndex; ind++){
				var taskDivPtr = document.getElementById("task"+ind);
				if(taskDivPtr!=null && taskDivPtr.id!="")
				{
					if(taskDivPtr.id.indexOf("task")>=0)
					{
						if(taskDivPtr!=null && taskDivPtr.getElementsByTagName("input")[0].checked)
						{
							tipFlag = true;
							break;
						}
					}
				}
			}
			if(tipFlag) {
				if(!confirm('您确认删除记录吗？'))
				{
					return;				
				}
			}
			
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=0; ind<iFuskIndex; ind++){
				//var taskDivPtr = document.getElementsByTagName("table")[ind];
				var taskDivPtr = document.getElementById("task"+ind);
				if(taskDivPtr!=null && taskDivPtr.id!="")
				{
					if(taskDivPtr.id.indexOf("task")>=0)
					{
						if(taskDivPtr!=null && taskDivPtr.getElementsByTagName("input")[0].checked){
							//if(taskDivPPtr.childNodes.length==9)
							if(document.getElementsByName("subtotal").length == 1)
							{
								//alert("必须至少一项任务");
								allClearFlag = true;
								taskDivPtr.getElementsByTagName("input")[0].checked = false;
								for(var ind=0; ind<taskDivPtr.getElementsByTagName("textarea").length; ind++){
									taskDivPtr.getElementsByTagName("textarea")[ind].value="";
								}
								taskDivPtr.getElementsByTagName("select")[0].value = "0";
								taskDivPtr.getElementsByTagName("select")[1].value = "0%";
								taskDivPtr.getElementsByTagName("select")[2].value = "按时";
								//taskDivPtr.getElementsByTagName("span")[0].innerHTML = "0";
								for(var ind=0; ind<taskDivPtr.getElementsByTagName("input").length; ind++){
									if(taskDivPtr.getElementsByTagName("input")[ind].type=="text"){
										if(taskDivPtr.getElementsByTagName("input")[ind].name != "attachment" && taskDivPtr.getElementsByTagName("input")[ind].name != "taskReason")
										{
											taskDivPtr.getElementsByTagName("input")[ind].value="0";
										}
										else
										{
											taskDivPtr.getElementsByTagName("input")[ind].value="";
										}
									}
								}
								selAllFlag = true;
							}else{
								taskDivPPtr.removeChild(taskDivPtr);
								allClearFlag = false;
								iTaskIndex--;
							}//if(taskDivPPtr.childNodes.length==tableTaskIndex+13)
						}
					}
				}
			}
			ret = 0;
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				ret += (isNaN(parseFloat(document.getElementsByName("subtotal")[ind].value))?0:(parseFloat(document.getElementsByName("subtotal")[ind].value)));
			}
			document.getElementById("sumShow").innerHTML = Math.round(ret*100)/100;
			// add index
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind+1) ;				
			}
		}
		
		function selAgain(){
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=0; ind<taskDivPPtr.getElementsByTagName("input").length; ind++){
				if(taskDivPPtr.getElementsByTagName("input")[ind].type.toLowerCase()
=="checkbox"){
					taskDivPPtr.getElementsByTagName("input")[ind].checked=selAllFlag;
				}
			}
			selAllFlag = !selAllFlag;
		}
		
		function loadHdl(){
			var iFuskIndex = document.getElementsByTagName("table").length;
			for(var ind=0; ind<=iFuskIndex; ind++){
				var taskDivPtr = document.getElementsByTagName("table")[ind];
				if(taskDivPtr!=null && taskDivPtr.id!="")
				{
					if(taskDivPtr.id.indexOf("task")>=0)
					{
						taskDivPtr.id = "task"+(ind+1);
					}
				}
			}
		}
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = reportInfoForm.begin.value;
		if(thisDate.length>0){
		var a = re1.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
	 }
	 
	 function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementsByName(beginDate)[0].value
    var end=document.getElementsByName(endDate)[0].value

	var flag=true;

    if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;
   return flag;

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
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="loadHdl()">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/day/reportinfo!update.action"  method="post">
		<!--<input type="hidden" name="report.id" value='<s:property value="report.id"/>'/>	-->
		<input type="hidden" name="groupName" value='<s:property value="groupName"/>'/>
        <!--<input type="hidden" name="createDate" id="begin" value='<s:date name="createDate" format="yyyy-MM-dd"/>'/>-->
		<table width="80%" align="center" cellPadding="1" cellSpacing="1">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="工作报告信息" /></legend>
                <table width="90%" align="center" id="dateTable">
                    <tr>
                        <td align="center" width="10%" nowrap><div align="center" style="padding-top:4">日期：</div></td>  
                        <td><div align="left">
                        <!--<input name="createDate"  type="text" id="begin" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="createDate" format="yyyy-MM-dd"/>'>-->
                        <span><font color="#336699">
                        <!--createDate这个时间可以修改，createDateHidden用于删除过去的日报信息  -->
                        <input name="createDate"  type="text" id="begin" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="createDate" format="yyyy-MM-dd"/>'>
                        <input name="createDateHidden" type="hidden"   value='<s:date name="createDate" format="yyyy-MM-dd"/>'/>
                        </font></span>
                        </div></td>
                        <td>
                            <div align="right">工时总计 <font color="#FF0000"><span id="sumShow"><s:property value="subsum"/></span></font> 小时</div>
                        </td>
                    </tr>
                </table>
            <s:iterator value="dayReports" id="report" status="st"> 
				<table width="80%" align="center" border="0" cellspacing="1" cellpadding="1" id="task1" bgcolor="#583F70">
                    <tr>
                        <td bgcolor="#FFFFFF"><div align="center"><input type="checkbox" name="taskCkd1"/><s:property value="#st.index+1"/></div></td>
                        <td bgcolor="#FFFFFF">
                        <table width="80%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" >
                                <tr>
                                  <td colspan="2" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
                                  <td nowrap width="10%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">完成%</font><font color="#FF0000">*</font></div></td>
                                  <td nowrap width="10%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">状态</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">管理</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">需求</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">设计</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">编码</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">测试</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">其他</font></div></td>
                                  <td nowrap width="6%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">工程</font></div></td>
                                  <td nowrap bgcolor="#A5A5A5" width="6%"><div align="center"><font color="#000000">小计</font></div></td>
                              </tr>
                                <tr>
                                    <td colspan="2" nowrap bgcolor="#FFFFFF"><tm:tmSelect name="prjName" id="prjName" selType="dataDir" path="systemConfig.projectname"  style="width:186px" /></td>
                                    <td nowrap bgcolor="#FFFFFF"><div align="center">
                                    <select name="finishRate"  id="finishRate" >
                                    <option value="100%">100%</option>
                                    <option value="95%">95%</option>
                                    <option value="90%">90%</option>
                                    <option value="85%">85%</option>
                                    <option value="80%">80%</option>
                                    <option value="75%">75%</option>
                                    <option value="70%">70%</option>
                                    <option value="65%">65%</option>
                                    <option value="60%">60%</option>
                                    <option value="55%">55%</option>
                                    <option value="50%">50%</option>
                                    <option value="45%">45%</option>
                                    <option value="40%">40%</option>
                                    <option value="35%">35%</option>
                                    <option value="30%">30%</option>
                                    <option value="25%">25%</option>
                                    <option value="20%">20%</option>
                                    <option value="15%">15%</option>
                                    <option value="10%">10%</option>
                                    <option value="5%">5%</option>
                                    <option value="0%" selected="true">0%</option>
                                    </select></div></td>
                                    <td nowrap bgcolor="#FFFFFF"><div align="center">
                                    <select name="status"  id="status">
                                    <option value="按时" selected="true">按时</option>
                                    <option value="延迟">延迟</option>
                                    <option value="提前">提前</option>
                                    <option value="新增">新增</option>
                                    <option value="取消">取消</option> 
                                    <option value="暂停">暂停</option>         
                                    </select>
                                    </div></td>
                                    <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="managerment" type="text" id="managerment"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="managerment"/>'></div></td> 
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="requirement" type="text" id="requirement"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="requirement"/>'></div></td> 
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="design" type="text" id="design"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="design"/>'></div></td>
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="code" type="text" id="code"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="code"/>'></div></td>
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="test" type="text" id="test"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="test"/>'></div></td>  
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="other" type="text" id="other"  size="4" maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="other"/>'></div></td>
                                  <td nowrap bgcolor="#FFFFFF"><div align="center"><input name="project" type="text" id="project"  size="4"  maxlength="4" onChange="numVali(this)" onBlur="numVali(this)" onFocus="numClear(this)" value='<s:property value="project"/>'></div></td>
                                  <td nowrap bgcolor="#FFFFFF" align="center" ><div align="center"><input name="subtotal" type="text" id="subtotal" readonly size="4" value='<s:property value="subtotal"/>' style="border:none; color:#FF0000; text-align:center" /></div></td>
                              </tr>
                              <tr>
                                  <td nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">任务描述</font><font color="#FF0000">*</font></div></td>
                                  <td colspan="12" nowrap bgcolor="#FFFFFF"><div align="left"><textarea name="taskDesc" type="text" id="taskDesc" rows="2" cols="70" onBlur="checkHdl(this)" ><s:property value="taskDesc"/></textarea>
                                  </div></td>
                              </tr>
                              <tr>
                                  <td nowrap bgcolor="#A5A5A5" width="40%"><div align="center"><font color="#000000">交付件</font></div></td>
                                  <td colspan="4" nowrap bgcolor="#FFFFFF" ><div align="left"><input name="attachment" type="text" id="attachment" size="42" maxlength="250" value='<s:property value="attachment"/>'>
                                  </div></td>
                                  <td colspan="2" bgcolor="#A5A5A5" nowrap><div align="center" style="width:80px;"><font color="#000000">&nbsp;偏差原因&nbsp;</font></div></td>
                                  <td colspan="6" nowrap bgcolor="#FFFFFF" ><div align="left"><input name="taskReason" type="text" id="taskReason" size="33" maxlength="250" value='<s:property value="taskReason"/>' style="color:#FF0000"></div></td>
                              </tr>
                          </table>
                      </td>
                      </tr>
                      <hr color="#0000FF" size="4"/>
                    </table>
				<input name="stat" type="hidden" id="stat"  value='<s:property value="status"/>' />
                <input name="stat1" type="hidden" id="stat1"  value='<s:property value="prjName"/>' />
				<input name="stat2" type="hidden" id="stat2"  value='<s:property value="finishRate"/>' />
              </s:iterator>
			</fieldset>
			</td> 
		</tr> 
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
    <tr>  
           <td>
                 <fieldset  width="100%" >
                      <legend><s:text name="label.tips.title"/></legend>
                      <table width="100%" >
                          <tr>
                            <td><s:text name="label.admin.content"/></td>
                            <td width="6%"><input type="button" value="新增" onClick="addAgain()"/></td>
                            <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
                            <!--<td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>-->`
                          </tr>
                      </table>
                  </fieldset>
              </td>  
      </tr>
      <br/>
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
<script type="text/javascript">
	iTaskIndex = document.getElementsByName("subtotal").length;
	for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
		var szStatus = document.getElementsByName("stat")[ind].value;
		szStatus = decodeURI(szStatus);
		var szPrjName = document.getElementsByName("stat1")[ind].value;
		szPrjName = decodeURI(szPrjName);
		var szFinishRate = document.getElementsByName("stat2")[ind].value;
		document.getElementsByName("finishRate")[ind].value = (szFinishRate.trim() == "" ? "0%" : szFinishRate.trim());
		
		document.getElementsByName("status")[ind].value = (szStatus.trim() == "" ? "按时" : szStatus.trim());
		document.getElementsByName("prjName")[ind].value = (szPrjName.trim() == "" ? "0" : szPrjName.trim());
	}
</script>
</body>  
</html> 