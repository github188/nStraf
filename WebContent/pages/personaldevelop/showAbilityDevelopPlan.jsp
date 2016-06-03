<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
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



			
			window.returnValue=true;
			reportInfoForm.submit();
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

		  function init()
	      {
	      	var id=$("#id").val();
	      	if(""!=id&&null!=id)
	      	{   
	      		parent.document.getElementById("id").value=id;
	      		if(parent.showLi)
	      			parent.showLi();
	      	}
	      	return false;
	      }   
	      
	       function selDate(id)
	      {
	      	var obj=document.getElementById(id);
	      	ShowDate(obj);
	      }  
		  
		    function showLog(value,value2)
		  {
	
		  	var aa = document.getElementById(value).value;
		  	 var id=document.getElementById("id").value;
			 var strUrl="/pages/atm/atminfo!showLogs1.action?mediaValue="+aa+"&id="+id;
		  	 //alert(strUrl);
             var features="800,700,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
			if(resultvalue!=null){
				
				resultvalue=resultvalue.replace(/<BR>/g,"\r\n");
				document.getElementById(value2).value=resultvalue;

				//	$("#clientmgrId").val(returnValue[0]);
			}else{
				
			}
			
		  } 
		  function addAgain(){
			var iFuskTotal = document.getElementsByName("taskDesc").length;       //获取当前的新增的记录数
			var curCount = 1;
			for(var ind=0; ind<iFuskTotal; ind++){
				var taskDesc = document.getElementsByName("taskDesc")[ind];
				var plandate = document.getElementsByName("planDate")[ind];   
				var suggestmode = document.getElementsByName("suggestMode")[ind];
				if(taskDesc!=null)
				{
					if(taskDesc.value.trim() == "")
					{
						alert("请输入期望学习内容");
						return;
					}
				}
				if(plandate!=null)
				{
					if(plandate.value.trim() == "")
					{
						alert("请输入时间计划");
						return;
					}
				}
				if(suggestmode!=null)
				{
					if(suggestmode.value.trim() == "")
					{
						alert("请输入建议方式");
						return;
					}
				}
				curCount++;
			}
		
			
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];   //获取页面的fieldset的元素
			var taskDivPtr = taskDivPPtr.getElementsByTagName("table")[0];     //获取填写记录的表格，包括标题头以及具体的填写内容
			var taskTr=taskDivPtr.getElementsByTagName("tr")[iTaskIndex];
		//	var taskTr=taskDivPtr.childNodes[tableTaskIndex];  //获取填写的当前行
			var taskTr1 = taskTr.cloneNode(true);  //复制当前的记录行
		//	taskTr1.id = "task" + iTaskIndex;  //给复制的行的id赋值，'task1','task2'等，其中的1,2为任务的序列数
			iTaskIndex++;    //将记录的序号数加1
			//给复制的表格中的每个元素赋值
			taskTr1.getElementsByTagName("input")[0].checked = false;
			
			for(var ind=0; ind<taskTr1.getElementsByTagName("input").length; ind++){
				if(taskTr1.getElementsByTagName("input")[ind].type=="text"){
						taskTr1.getElementsByTagName("input")[ind].value="";
				}
			}
			taskTr.parentElement.appendChild(taskTr1);  //将复制的表格追加到后面
			// 将其序号数加1
			//document.getElementsByName("prjName")[iTaskIndex-1].value="";
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
		   iTaskIndex = document.getElementsByName("taskDesc").length;
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
		
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10" onLoad="init();">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/personaldevelop/personaldevelopinfo!updateAbilityDevelopPlan.action"
			method="post">
			<input type="hidden" id="id" name="id"
				value='<s:property value="id"/>'>
                                <input type="hidden" id="finishDate11" name="personaldevelop.finishDate"
				value='<s:property value="personaldevelop.finishDate"/>'>
			<table width="90%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
        <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend>能力发展计划</legend>
                    <table width="252" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70"  id="record">
                    <br/>
                    <tr>
                    	<td align="center" width="5%" bgcolor="#999999">序号</td>
                      <td align="center" width="10%" bgcolor="#999999">期望学习内容<font color="#FF0000">*</font></td>
                      <td align="center" width="10%" bgcolor="#999999">时间计划<font color="#FF0000">*</font></td>
                      <td align="center" width="8%" bgcolor="#999999">建议方式<font color="#FF0000">*</font></td>
                      <td align="center" width="15%" bgcolor="#999999">预期效果<font color="#FF0000">*</font></td>
                      <td align="center" width="20%" bgcolor="#999999">完成效果描述</td>
                      <td align="center" width="10%" bgcolor="#999999">完成日期</td>
                      <td align="center" width="10%" bgcolor="#999999">上级评价</td>
                      <td align="center" width="10%" bgcolor="#999999">上上级评价</td>
                    </tr>   
                    <s:iterator value="adpRecords" id="record" status="st"> 
					<tr id='task<s:property value="#st.index"/>'>
                       <td bgcolor="#FFFFFF"><input type="checkbox" name="taskCkd1"/><s:property value="#st.index+1"/></td>
                      <td  bgcolor="#FFFFFF"><textarea cols="15" rows="3" name="taskDesc"
												id="taskDesc" readonly><s:property value="taskDesc"/></textarea></td>
                      <td   bgcolor="#FFFFFF"><input name="planDate" type="text" id="planDate" size="15" maxlength="100"  value='<s:property value="planDate"/>' readonly="readonly"></td>
                      <td  bgcolor="#FFFFFF">
                      <input name="suggestMode" type="text" id="suggestMode"  size="10" maxlength="30" value='<s:property value="suggestMode"/>' readonly="readonly"></td>
                      <td  bgcolor="#FFFFFF"><textarea cols="20" rows="3" name="wishInstance" id="wishInstance" readonly><s:property value="wishInstance"/></textarea></td>
                      <td  bgcolor="#FFFFFF">
                      <textarea cols="20" rows="3" name="finishInstance"
												id="finishInstance" readonly><s:property value="finishInstance"/></textarea></td>
                    <td  bgcolor="#FFFFFF"><label><input name="finishDate" type="text" id="finishDate" size="15" maxlength="100"   issel="true" value='<s:property value="finishDate"/>' readonly="readonly"></label></td>
                    <td  bgcolor="#FFFFFF"><textarea cols="20" rows="3" name="headmanScore" 
												id="headmanScore" readonly><s:property value="headmanScore"/>
                      </textarea></td>
                      <td  bgcolor="#FFFFFF"><textarea cols="20" rows="3" name="manageScore" 
												id="manageScore" readonly><s:property value="manageScore"/>
                      </textarea></td>
                     </tr>
                     </s:iterator>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
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