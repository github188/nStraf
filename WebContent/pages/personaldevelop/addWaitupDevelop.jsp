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
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
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
				var taskDesc = document.getElementsByName("taskDesc")[ind];   
				if(taskDesc!=null)
				{
					if(taskDesc.value.trim() == "")
					{
					alert("请输入能力名称");
					return;
					}
				}
			}		
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
	      
		  function addAgain(){
			var iFuskTotal = document.getElementsByName("taskDesc").length;       //获取当前的新增的记录数
			var curCount = 1;
			for(var ind=0; ind<iFuskTotal; ind++){
				var taskDesc = document.getElementsByName("taskDesc")[ind];
				var capabilityexplain = document.getElementsByName("capabilityExplain")[ind];   
				if(taskDesc!=null)
				{
					if(taskDesc.value.trim() == "")
					{
						alert("请输入能力名称");
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
		
		function selAgain(){
			var taskDivPPtr = document.getElementsByTagName("table")[0];
			for(var ind=0; ind<taskDivPPtr.getElementsByTagName("input").length; ind++){
				if(taskDivPPtr.getElementsByTagName("input")[ind].type.toLowerCase()=="checkbox"){
					taskDivPPtr.getElementsByTagName("input")[ind].checked=selAllFlag;
				}
			}
			selAllFlag = !selAllFlag;
		}
		  
	       function selDate(id)
	      {
	      	var obj=document.getElementById(id);
	      	ShowDate(obj);
	      }  
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init();">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/personaldevelop/personaldevelopinfo!saveWaitupDevelop.action"   method="post">
			<input type="hidden" id="id" name="id"
				value='<s:property value="id"/>'>
              <input type="hidden" id="capabilityexplain" name="waitupdevelop.capabilityexplain"
				value='<s:property value="waitupdevelop.capabilityexplain"/>'>  
		<table width="80%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend>待提升能力</legend>
                    <table width="652" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" id="record">
                    <br/>
                    <tr>
                    	<td align="center" width="5%" bgcolor="#999999">序号</td>
                      <td align="center" width="30%" bgcolor="#999999">能力名称<font color="#FF0000">*</font></td>
                      <td align="center" width="50%" bgcolor="#999999">说明</td>
                    </tr>   
					<tr id="task0">
                       <td  bgcolor="#FFFFFF"><input type="checkbox" name="taskCkd1"/>1</td>
                      <td nowrap  bgcolor="#FFFFFF">
                      <input name="taskDesc" type="text" id="taskDesc"  size="40" maxlength="40">
									</td>
                                                          <td nowrap  bgcolor="#FFFFFF">
                      <input name="capabilityExplain" type="text"  id="capabilityExplain"  size="73" maxlength="53" >
									</td>
                     </tr>
                         
                    </table>
             </fieldset>
                </td> 
            </tr> 

    </table>
<br/>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
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
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
    
</body>  
</html> 