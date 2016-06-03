<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时小计span改为input-->
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
		function save(){
			var curCount = 1;
			for(var ind=0; ind<iTaskIndex; ind++){
				var prjName = document.getElementsByName("prjName")[ind];
				var taskDesc = document.getElementsByName("taskDesc")[ind];   
				var responsible = document.getElementsByName("responsible")[ind];
				var finishRate = document.getElementsByName("finishRate")[ind];
				var delayReason = document.getElementsByName("delayReason")[ind];  
				var audit = document.getElementsByName("audit")[ind]; 
				if(prjName!=null)
				{
					if(prjName.value == "")
					{
						alert("请输入项目名称");
						return;
					}
					if(prjName.value.trim() == "--------应用软件测试组项目列表--------" || prjName.value.trim() == "------技术支持组项目列表------" || prjName.value.trim() == "--------基础软件测试组项目列表--------" || prjName.value.trim() == "--------质量管理组项目列表--------" || prjName.value.trim() == "------------其他事项列表------------")
					{
						alert("第"+ curCount +"项任务的项目名称请确认是否正确");
						prjName.value = "";
						return;
					}
				}
				if(taskDesc!=null)
				{
					if(taskDesc.value.trim() == "")
					{
						alert("请输入任务描述");
						return;
					}
				}
				if(responsible!=null)
				{
					if(responsible.value.trim() == "")
					{
						alert("请输入负责人");
						return;
					}
				}	
			
				if(finishRate.value!= "0%" &&finishRate.value!= "100%" && delayReason.value == "")
				{
						alert("请填写延迟原因");
						return;
				}	
				curCount++;
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
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numClear(itemName){
			itemName.select();
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			//if(! Validate(itemName.value, "Number"))
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
			/*if(val.value.trim()!="")
			{
				if(val.value.trim().length > 250)
				{
					alert("你输入的字数超过250个了");
				}
			}*/
		}
		
		function addAgain(){
			var iFuskTotal = document.getElementsByName("taskDesc").length;       //获取当前的新增的记录数
			var curCount = 1;
			for(var ind=0; ind<iFuskTotal; ind++){
				var prjName = document.getElementsByName("prjName")[ind];
				var taskDesc = document.getElementsByName("taskDesc")[ind];   
				var responsible = document.getElementsByName("responsible")[ind];
				if(prjName!=null)
				{
					if(prjName.value.trim() == "")
					{
						alert("请输入项目名称");
						return;
					}
					if(prjName.value.trim() == "--------应用软件测试组项目列表--------" || prjName.value.trim() == "------技术支持组项目列表------" || prjName.value.trim() == "--------基础软件测试组项目列表--------" || prjName.value.trim() == "--------质量管理组项目列表--------" || prjName.value.trim() == "------------其他事项列表------------")
					{
						alert("第"+ curCount +"项任务的项目名称请确认是否正确");
						prjName.value = "";
						return;
					}
				}
				if(taskDesc!=null)
				{
					if(taskDesc.value.trim() == "")
					{
						alert("请输入任务描述");
						return;
					}
				}
				if(responsible!=null)
				{
					if(responsible.value.trim() == "")
					{
						alert("请输入负责人");
						return;
					}
				}	
				curCount++;
			}
		
			
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];   //获取页面的fieldset的元素
			var taskDivPtr = taskDivPPtr.getElementsByTagName("table")[1];     //获取填写记录的表格，包括标题头以及具体的填写内容
			var taskTr=taskDivPtr.getElementsByTagName("tr")[iTaskIndex];
		//	var taskTr=taskDivPtr.childNodes[tableTaskIndex];  //获取填写的当前行
			var taskTr1 = taskTr.cloneNode(true);  //复制当前的记录行
			taskTr1.id = "task" + iTaskIndex;  //给复制的行的id赋值，'task1','task2'等，其中的1,2为任务的序列数
			iTaskIndex++;    //将记录的序号数加1
			//给复制的表格中的每个元素赋值
			taskTr1.getElementsByTagName("input")[0].checked = false;
			taskTr1.getElementsByTagName("input")[0].disabled = false;
			taskTr1.getElementsByTagName("select")[0].value = "0";   //给项目名称置为初始值
			taskTr1.getElementsByTagName("select")[1].value = "1";   //给项目类型
			taskTr1.getElementsByTagName("select")[1].onchange=null;
			//document.formName.selectName.detachEvent( "onchange ");
			taskTr1.getElementsByTagName("select")[2].value = "0%";   //完成情况
			taskTr1.getElementsByTagName("select")[3].value = "0";  //是否归档
			taskTr1.getElementsByTagName("select")[4].value = "0";   //主任审核
			taskTr1.getElementsByTagName("select")[4].DefaultValue="0";
		//	taskTr1.getElementsByTagName("select")[4].onchange=null;
			
			for(var ind=0; ind<taskTr1.getElementsByTagName("input").length; ind++){
				if(taskTr1.getElementsByTagName("input")[ind].type=="text"){
						taskTr1.getElementsByTagName("input")[ind].value="";
						taskTr1.getElementsByTagName("input")[ind].setAttribute("readOnly",false);
				}
			}
			taskTr.parentElement.appendChild(taskTr1);  //将复制的表格追加到后面
			// 将其序号数加1
			document.getElementsByName("prjName")[iTaskIndex-1].value="";
			for(var ind=0; ind<document.getElementsByName("taskDesc").length; ind++){
				document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind+1) ;
				taskDivPtr.getElementsByTagName("tr")[ind+1].id="task"+ind;				
			}
		}
		
		function delAgain(){
			var tipFlag = false;
			var ss=0;
			var taskDivPPtr = document.getElementById("record");  //获取table元素
		   // alert("delAgain iTaskIndex="+iTaskIndex);
			for(var ind=0; ind<iTaskIndex; ind++){   //遍历任务序列号
				//var taskDivPtr = document.getElementById("task"+ind);  //获得每个数据输入行
				var taskDivPtr =taskDivPPtr.getElementsByTagName("tr")[ind+1];
				if(taskDivPtr!=null && taskDivPtr.getElementsByTagName("input")[0].checked)
				{
					tipFlag = true;
					break;
				}
			}
			if(tipFlag) {
				if(!confirm('您确认删除记录吗？'))
				{
					return;				
				}
			}
			
			var recordNum=iTaskIndex;
			//alert("recordNum:"+iTaskIndex);
			//var selectedRows=0;
			for(var ind1=0; ind1<recordNum; ind1++){   ////遍历任务序列号
				//var taskDivPtr = document.getElementById("task"+ind1);  //获得任务序列号
			var taskDivPtr=	taskDivPPtr.getElementsByTagName("tr")[ind1+1];
			
				if(taskDivPtr==null){
					taskDivPtr=taskDivPPtr.getElementsByTagName("tr")[ind1+1-ss];
				}
				//alert("delAgain checked="+taskDivPtr.getElementsByTagName("input")[0].checked);
				//alert("delAgain iTaskIndex2="+taskDivPtr);
				if(taskDivPtr!=null && taskDivPtr.getElementsByTagName("input")[0].checked){  //如果该行被选定
						//alert("selectRow:"+ind)

						if(iTaskIndex==1){
							taskDivPtr.getElementsByTagName("input")[0].checked = false;
							taskDivPtr.getElementsByTagName("select")[0].value = "0";   //给项目名称置为初始值
							taskDivPtr.getElementsByTagName("select")[1].value = "0";   //给项目类型
							taskDivPtr.getElementsByTagName("select")[2].value = "0%";   //完成情况
							taskDivPtr.getElementsByTagName("select")[3].value = "1";  //是否归档
							taskDivPtr.getElementsByTagName("select")[4].value = "0";   //主任审核
							for(var ind2=0; ind2<taskDivPtr.getElementsByTagName("input").length; ind2++){
								if(taskDivPtr.getElementsByTagName("input")[ind2].type=="text"){
										taskDivPtr.getElementsByTagName("input")[ind2].value="";
								}
							}
							document.getElementsByName("taskCkd1")[0].nextSibling.nodeValue = 1 ;	
							return;
						}else{
							taskDivPtr.parentElement.removeChild(taskDivPtr);
							iTaskIndex--;
							ss++;
						}
					//}
				}
			}
			
			// add index
			for(var ind=0; ind<document.getElementsByName("taskDesc").length; ind++){
				document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind+1) ;				
			}
		}
		
		function selAgain(){
			var taskDivPPtr = document.getElementsByTagName("table")[1];
			for(var ind=0; ind<taskDivPPtr.getElementsByTagName("input").length; ind++){
				if(taskDivPPtr.getElementsByTagName("input")[ind].type.toLowerCase()=="checkbox"){
					taskDivPPtr.getElementsByTagName("input")[ind].checked=selAllFlag;
				}
			}
			selAllFlag = !selAllFlag;
		}
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = reportInfoForm.begin.value.trim();
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
		function tihs()
		{
			var url="weekinfo!close.action";
			var params={idFlag:$("#riid").val()};
			//jQuery.post(url, params, $(document).callbackFun{return;}, 'json');
			jQuery.post(url, params, '', 'json');
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
<body id="bodyid"  leftmargin="0" topmargin="10" onUnload="tihs()">
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/week/weekinfo!update.action" method="post">
    <input name="weekReport.taskOverview" type="hidden" id="taskDesc123" maxlength="200" size="107" value='<s:property value="weekReport.taskOverview"/>'>
    <input name="weekReport.id" type="hidden" id="riid"  value='<s:property value="weekReport.id"/>'>
       <input type="hidden" name="weekReport.responsor" value='<s:property value="weekReport.responsor"/>'/>
<table width="80%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td>
	<fieldset class="jui_fieldset" width="100%">
		 <legend><s:text name="" />小组周报</legend>
         <input type="hidden" name="curDateHidden" id="curDateHidden" value='<s:date name="new java.util.Date()" format="yyyy-MM"/>' />
      <table width="90%" align="center">
        	 <tr>
                        <td align="center" width="10%" nowrap ><div align="center" style="padding-top:4">月份：</div></td>  
                        <td bgcolor="#FFFFFF"><input  name="monthDay" type="text" id="monthDay" maxlength="100" size="22" value='<s:date name="new java.util.Date()" format="yyyy-MM"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#336699"></td>
               <td></td>
               <td></td>
               <td bgcolor="#FFFFFF" align="right"><div>组别：</td>
               <td bgcolor="#FFFFFF"><input name="weekReport.groupName" type="text" id="groupName" maxlength="100" size="22" value='<s:property value="weekReport.groupName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#336699; padding-top:2"></td>  
          </tr>
                 <tr>
                            <td align="center" bgcolor="#A5A5A5" width="15%"><font color="#000000">开始日期：</font></td>
                   <td width="20%" bgcolor="#FFFFFF"><div align="left">
                     <input name="weekReport.startDate" type="text" id="start" size="22" readonly value='<s:date name="weekReport.startDate" format="yyyy-MM-dd"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000">
                   </div></td> 
                            <td></td>
              				 <td></td>
                   <td align="center" bgcolor="#A5A5A5" width="10%"><font color="#000000">结束日期：</font></td>
                   <td width="20%" bgcolor="#FFFFFF"><input name="weekReport.endDate" type="text" id="end" size="22" readonly value='<s:date name="weekReport.endDate" format="yyyy-MM-dd"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000"></td> 
                                                           
          </tr>    
      </table>
        
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70" id="record">
        
        	<tr >
	      <td width="6%" bgcolor="#FFFFFF"><div align="center">序号</div></td>
              <td width="186px" nowrap="nowrap" bgcolor="#A5A5A5"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
               <td width="30%" nowrap="nowrap" bgcolor="#A5A5A5"><div align="center"><font color="#000000">任务描述</font><font color="#FF0000">*</font></div>															                          </td>
                  <td width="6%" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">项目类型</font><font color="#FF0000">*</font></div></td>
              <td width="8%" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">负责人</font><font color="#FF0000">*</font></div></td>
                  <td width="8%" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">完成情况</font></div></td>
		<td width="18%" nowrap="nowrap" bgcolor="#A5A5A5"><div align="center"><font color="#000000">延迟原因/建议</font></div>
              <td width="6%" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">是否归档</font></div></td>
                  <td width="8%" nowrap bgcolor="#A5A5A5"><div align="center"><font color="#000000">主任审核</font></div></td>
          </tr>
          
           <s:iterator value="records" id="record" status="st"> 
              <tr id='task<s:property value="#st.index"/>'>
               		<td bgcolor="#FFFFFF" align="center"><input type="checkbox" name="taskCkd1" disabled/><s:property value="#st.index+1"/></td>
                	<td colspan="" nowrap bgcolor="#FFFFFF"><tm:tmSelect name="prjName" id="prjName" selType="dataDir" path="systemConfig.projectname"  style="width:186px" /></td>
                     <td nowrap bgcolor="#FFFFFF" align="left"><input name="taskDesc" id="taskDesc"size="70" maxlength="70" value='<s:property value="taskDesc"/>'readonly/></td>
                     <td nowrap bgcolor="#FFFFFF"><div align="center">
                            <select name="prjType"  id="prjType" DefaultValue= '<s:property value="prjType"/>'   onchange= "this.value=this.DefaultValue">
                              <option value="0">计划</option>
                              <option value="1">新增</option>
                            </select>
                          </div>                       </td>
                       <td nowrap bgcolor="#FFFFFF"><div align="center"> <input name="responsible" type="text" id="responsible"  size="8" maxlength="100" value='<s:property value="responsible"/>'readonly/></div></td>
                       <td nowrap bgcolor="#FFFFFF"><div align="center">
                           <select name="finishRate"  id="finishRate" readonly>
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
                                <option value="0%" >0%</option>
                              </select></div>                          </td> 
			   <td nowrap bgcolor="#FFFFFF" align="left"><input name="delayReason" id="delayReason" size="30" maxlength="100" value='<s:property value="delayReason"/>'/></td>
                          <td>
                          	<select name="file"  id="file" >
                              <option value="1">未归档</option>
                             <option value="2">已归档</option>
                             	<option value="0">无需归档</option>
                            </select>                           </td>
                          <td> <select name="audit"  id="audit" DefaultValue= '<s:property value="audit"/>'   onchange= "this.value=this.DefaultValue">
                              <option value="0">未审核</option>
                              <option value="1">已审核</option>
                            </select>                           </td>
                            
                         <input name="prjNameCpy" type="hidden" id="stat0"  value='<s:property value="prjName"/>' />  
                        <input name="prjTypeCpy" type="hidden" id="stat1"  value='<s:property value="prjType"/>' />
                         <input name="finishRateCpy" type="hidden" id="stat2"  value='<s:property value="finishRate"/>' />
        				 <input name="fileCpy" type="hidden" id="stat3"  value='<s:property value="file"/>' />
                        <input name="auditCpy" type="hidden" id="stat4"  value='<s:property value="audit"/>' />                 
                  </tr>
     
                   </s:iterator>
                  
                </table>        
        <!--   </td>
          </tr>
          
        	<tr>
        	  <td bgcolor="#FFFFFF" align="center"><input type="checkbox" name="taskCkd1"/>1</td>
      	  </tr>
          <hr color="#0000FF" size="3"/>
          </table>
          -->		
	</fieldset>
    <!--
</td> 
</tr>-->
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
 <tr>  
       <td>
             <fieldset width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
				<table width="100%" >
                      <tr>
                        <td><s:text name="label.admin.content"/></td>
                        <td width="6%"><input type="button" value="新增" onClick="addAgain()"/></td>
                           <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
                        <!--<td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>-->
                      </tr>
                  </table>
              </fieldset>
      </td>  
  </tr>
	<td align="center"> 
	<br/>
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  <script type="text/javascript">
	iTaskIndex = document.getElementsByName("taskDesc").length;
	if(document.getElementsByName("taskDesc")[0].value == "")
	{
		document.getElementsByName("taskDesc")[0].readOnly = false;
	}
	if(document.getElementsByName("responsible")[0].value == "")
	{
		document.getElementsByName("responsible")[0].readOnly = false;
	}


	for(var ind=0; ind<document.getElementsByName("taskDesc").length; ind++){
		var prjNameCpy = document.getElementsByName("prjNameCpy")[ind].value;
		prjNameCpy = decodeURI(prjNameCpy);
		var prjTypeCpy = document.getElementsByName("prjTypeCpy")[ind].value;
		prjTypeCpy = decodeURI(prjTypeCpy);
		var finishRateCpy = document.getElementsByName("finishRateCpy")[ind].value;
		//finishRateCpy = decodeURI(finishRateCpy);
		var fileCpy = document.getElementsByName("fileCpy")[ind].value;
		fileCpy = decodeURI(fileCpy);
		var auditCpy = document.getElementsByName("auditCpy")[ind].value;
		auditCpy = decodeURI(auditCpy);
		
		
		document.getElementsByName("prjName")[ind].value =prjNameCpy ;
		document.getElementsByName("prjType")[ind].value=prjTypeCpy;
		document.getElementsByName("finishRate")[ind].value=finishRateCpy;
		document.getElementsByName("file")[ind].value =fileCpy ;
		document.getElementsByName("audit")[ind].value = auditCpy;
	}
	if(iTaskIndex == 1){
	
		if(document.getElementsByName("prjName")[0].value == "1")
		{
			document.getElementsByName("prjName")[0].value = "0";
		}
	}
</script>
</body>  
</html> 

