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
		 	window.close();
		}
		function save(){
			var curCount = 1;
			var bSubmit = false;
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=1; ind<=iTaskIndex; ind++){
				var taskDivPtr = document.getElementById("task"+ind);
				if(taskDivPtr!=null){
					if(taskDivPtr.getElementsByTagName("textarea")[2].value.trim() == ""){
						alert("请输入第" + curCount + "项的任务描述");
						return;
					}
					bSubmit = true;
				}
				if(taskDivPtr!=null)
				{
					curCount++;
				}
			}
			/*if(!bSubmit)
			{
				alert("没有选中任一条记录，数据不进行提交");
				return;
			}
			// 删除未打勾的记录
			for(var ind=1; ind<=iTaskIndex; ind++){
				var taskDivPtr = document.getElementById("task"+ind);
				if((taskDivPtr!=null) && (!taskDivPtr.getElementsByTagName("input")[0].checked)){
					taskDivPPtr.removeChild(taskDivPtr);
				}
			}*/
			/*for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				alert(document.getElementsByName("subtotal")[ind].value);
			}*/
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
		
			var itemNameValue=document.getElementsByName(itemName)[0].value
			
				var flag
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
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.name, "Number"))
			{
				document.getElementsByName(itemName.name)[0].value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseInt(itemName.value);
				if(tmp>20){
					itemName.value = 0;
					alert("请输入20以内的数字");
				}			
			}
			var itemPPtr = itemName.parentNode.parentNode.parentNode;
			for(var ind=0; ind<itemPPtr.getElementsByTagName("input").length; ind++){
				if(itemPPtr.getElementsByTagName("input")[ind].name != "subtotal"){
					var tmpInt = parseInt(itemPPtr.getElementsByTagName("input")[ind].value);
					if(isNaN(tmpInt)){
						tmpInt = 0;
					}
					ret += tmpInt;
				}
			}
			for(var ind=0; ind<itemPPtr.getElementsByTagName("input").length; ind++){
				if(itemPPtr.getElementsByTagName("input")[ind].name == "subtotal"){
					itemPPtr.getElementsByTagName("input")[ind].value = ret;
				}
			}
			ret = 0;
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				ret += parseInt(document.getElementsByName("subtotal")[ind].value);
			}
			document.getElementById("sumShow").innerHTML = ret;
		}
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
				val.value = "今天没工作任务";
			}
		}
		
		function addAgain(){
			if(document.getElementsByName("subtotal").length >= iTaskTotalpd)
			{
				alert("哇，一天这么多任务，可惜本页面限制最多新增" + iTaskTotalpd + "项任务");
				return;
			}
			iTaskIndex++;
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			var taskDivPtr = taskDivPPtr.childNodes[tableTaskIndex];
			var taskDivPtr2 = taskDivPtr.cloneNode(true);
			taskDivPtr2.id = "task" + iTaskIndex;
			for(var ind=0; ind<taskDivPtr2.getElementsByTagName("textarea").length; ind++){
				taskDivPtr2.getElementsByTagName("textarea")[ind].value="";
			}
			taskDivPtr2.getElementsByTagName("select")[0].value = "0";
			taskDivPtr2.getElementsByTagName("select")[1].value = "5";
			for(var ind=0; ind<taskDivPtr2.getElementsByTagName("input").length; ind++){
				if(taskDivPtr2.getElementsByTagName("input")[ind].type=="text"){
					if(taskDivPtr2.getElementsByTagName("input")[ind].name != "prjName")
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
		}
		
		function delAgain(){
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=1; ind<=iTaskIndex; ind++){
				var taskDivPtr = document.getElementById("task"+ind);
				if(taskDivPtr!=null && taskDivPtr.getElementsByTagName("input")[0].checked){
					if(taskDivPPtr.childNodes.length==tableTaskIndex+1)
					{
						alert("必须至少一项任务");
						taskDivPtr.getElementsByTagName("input")[0].checked = false;
						for(var ind=0; ind<taskDivPtr.getElementsByTagName("textarea").length; ind++){
							taskDivPtr.getElementsByTagName("textarea")[ind].value="";
						}
						taskDivPtr.getElementsByTagName("select")[0].value = "0";
						taskDivPtr.getElementsByTagName("select")[1].value = "5";
						for(var ind=0; ind<taskDivPtr.getElementsByTagName("input").length; ind++){
							if(taskDivPtr.getElementsByTagName("input")[ind].type=="text"){
								if(taskDivPtr.getElementsByTagName("input")[ind].name != "prjName")
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
					}
				}
			}
			ret = 0;
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				ret += (isNaN(parseInt(document.getElementsByName("subtotal")[0].value))?0:(parseInt(document.getElementsByName("subtotal")[0].value)));
			}
			document.getElementById("sumShow").innerHTML = ret;
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/report/reportinfo!save.action" method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
<tr>
<td>
	<fieldset class="jui_fieldset" width="100%">
		<legend><s:text name="label.newTermInfo" /></legend>
        <table width="100%" align="center">
        	<tr>
            	<td align="center" width="10%"><div align="center">日期</div></td>  
                <td><div align="left">
                	<input name="createDate"  type="text" id="begin" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>' readonly="true"> 
                </div></td>
            	<td>
	            	<div align="right">工时总计 <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
        </table>
		<table width="100%" align="center" border="1"  id="task1">
        	<tr>
				<td><input type="checkbox" name="taskCkd1"/></td>
                <td>
        		<table width="100%" align="center">
						<tr>
                          <td align="center" width="6%"><div align="center">项目名称</div></td>  
                          <td colspan="4"><textarea name="prjName" type="text" id="prjName" rows="2" cols="42"></textarea></td> 
                          <td align="center" width="6%"><div align="center">交付件</div></td>  
                          <td colspan="4"><textarea name="attachment" type="text" id="attachment" rows="2" cols="42"></textarea>  </td> 
   				      </tr> 
                        <tr>
                            <td align="center" width="6%"><div align="center">任务描述</div></td>
                            <td colspan="4"><textarea name="taskDesc" type="text" id="taskDesc" rows="3" cols="42" value="" onBlur="checkHdl(this)" ></textarea><font color="#FF0000">*</font>  </td> 
                            <td align="center" width="6%"><div align="center">任务偏差原因</div></td> 
                          <td colspan="4"><textarea name="taskReason" type="text" id="taskReason"  rows="3" cols="42" value=""></textarea> </td> 
                      </tr>
                        <tr>
                            <td align="center" width="6%"><div align="center">完成%</div></td>
                            <td colspan="4" align="center"><div align="left">
                            <select name="finishRate"  id="finishRate" style="width: 315px" >
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
                            </select><font color="#FF0000">*</font>
                            </div></td> 
                            <td align="center" width="6%"><div align="center">状态</div></td> 
                          <td colspan="4" align="center"><div align="left">
                            <select name="status"  id="status" style="width: 315px">
                            <option value="按时" selected="true">按时</option>
                            <option value="延迟">延迟</option>
                            <option value="新增">新增</option>
                            <option value="取消">取消</option> 
                            <option value="暂停">暂停</option>         
                            </select><font color="#FF0000">*</font>
                            </div></td> 
                      </tr>
                        <tr><td colspan="15">&nbsp;</td></tr>
                        <tr>
                          <td width="6%">&nbsp;</td>
                          <td width="6%"><div align="center">管理</div></td>
                          <td width="6%"><div align="center">需求</div></td>
                          <td width="6%"><div align="center">设计</div></td>
                          <td width="6%"><div align="center">编码</div></td>
                          <td width="6%"><div align="center">测试</div></td>
                          <td width="6%"><div align="center">其他</div></td>
                          <td width="6%"><div align="center">工程</div></td>
                          <td width="6%"><div align="center">小计</div></td>
                          <td width="6%">&nbsp;</td>
                      </tr>
                        <tr>
                        	<td><div align="center">&nbsp;</div></td>
                            <td><div align="center"><input name="managerment" type="text" id="managerment"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td> 
                          <td><div align="center"><input name="requirement" type="text" id="requirement"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td> 
                          <td><div align="center"><input name="design" type="text" id="design"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td>
                          <td><div align="center"><input name="code" type="text" id="code"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td>
                          <td><div align="center"><input name="test" type="text" id="test"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td>  
                          <td><div align="center"><input name="other" type="text" id="other"  size="8" maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td>
                          <td><div align="center"><input name="project" type="text" id="project"  size="8"  maxlength="2" onChange="numVali(this)" onBlur="numVali(this)" value='0'></div></td>
                          <td><div align="center"><input name="subtotal" type="text" id="subtotal"  size="8" maxlength="2" readonly value='0'></div></td>
					      <td>&nbsp;</td>
                      </tr>
                  </table>
                </td>
              </tr>
			</table>
	</fieldset>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
 <tr>  
       <td>
             <fieldset  width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%" >
                      <tr>
                        <td><s:text name="label.admin.content"/></td>
                        <td width="6%"><input type="button" value="新增" onClick="addAgain()"/></td>
                        <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
                        <td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>
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
  
</body>  
</html> 

