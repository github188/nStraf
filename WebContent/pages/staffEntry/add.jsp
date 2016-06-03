<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%String ctxPath= "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<html>
<head>
<title>certification query</title>
</head>

<script type="text/javascript">
	var checkConditionListIndex=0;//用来标记那些类型、项目、状态、所属部门是同一组的（同一组下标值相同）

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function save() {
		if(validateForm()){
			document.getElementById("ok").disabled = true;
			window.returnValue = true;
			entryInfoForm.submit();
		}
	}
	
	//提交时验证数据是否填写完整
	function validateForm(){
		if($("#entryInfoDetName").find("option:selected").val()=='全选'){
			alert('请选择部门!');
			return false;
		}
		if($("#identryInfoUserName").find("option:selected").val()=='全选'){
			alert('请选择用户!');
			return false;
		}
		if($("#entryInfoGrgBeginDate").val()==''){
			alert('请选择入职日期');
			return false;
		}
		
		if($("#entryInfoRegularDate").val()==''){
			alert('请选择转正日期');
			return false;
		} 
		
		//if($("#entryInfoNote").html().length>500){
		//	alert('备注限制在500字之内!');
	//		return false;
	//	}
		//alert();
		return true;
	}

	//页面加载完毕执行
	$(function(){
		//插入一空行
		insertLastRow();
		//加载并解析模版中的行，然后插入到空行前面
		var encodeCheckListStr='<s:property value="#encodeCheckListStr"/>';
		analysisCheckCondtion(UrlDecode(encodeCheckListStr));
		
		
		//用户选择改变后，自动赋值给隐藏控件
		$("#identryInfoUserName").change(function(){
			var selectedOption=$(this).find("option:selected");
			if(selectedOption.val()!='全选'){
				$("#entryInfoUserIdHidden").val(selectedOption.val());
				$("#entryInfoUserNameHidden").val(selectedOption.text());
			}else{
				$("#entryInfoUserIdHidden").val('');
				$("#entryInfoUserNameHidden").val('');
				
			}
		});
		//部门选择改变后，自动复制给隐藏控件
		$("#entryInfoDetName").change(function(){
			var selectedOption=$(this).find("option:selected");
			if(selectedOption.val()!='全选'){
				$("#entryInfoDetNameHidden").val(selectedOption.text());
			}else{
				$("#entryInfoDetNameHidden").val('');
				
			}
		});
	});
	
	
	//解析检查条件，然后放到extendStatusTable中
	function analysisCheckCondtion(jsonStr){
		//转为json对象
		var jsonConditions = eval("(" + jsonStr + ")");
		for(var i=0;i<jsonConditions.length;i++){
			var con=jsonConditions[i];//获取每一个检查条件对象
			//获取最后一行
			var rowsCount=document.getElementById("extendStatusTable").rows.length;
			var lastRow=$(document.getElementById ("extendStatusTable").rows [rowsCount-1]);
			//在最后一行前插入一行(插入的为倒数第二行)
			var boobyRowHtml=generateBoobyRowHtml(con);
			lastRow.before(boobyRowHtml);
		}
	}
	
	//增加一行
	function addAgain(){
		if(checkConditionHasInput()){//检查是否有输入框没有填入值
			insertLastRow();
		}
	}
	//删除行
	function delAgain(){
		var selectCount=0;
		//找到table中所有选中的checkBox
		$("#extendStatusTable").find("input[type='checkbox']").each(function(){
			//通过判断<input type='checkbox'>这个控件中是否包含CHECKED字段来判定其是否被选中
			if($(this).parent().html().indexOf('CHECKED')>0){
				selectCount++;
				//获取该checkBox对应的行
				var trObj=$(this).parent().parent().parent();
				//删除这行
				trObj.remove();
			}
		});
		if(selectCount==0){
			alert("请至少选择一行!");
		}
	}
	
	//在最后新建一空行
	function insertLastRow(){
		//获取最后一行
		var rowsCount=document.getElementById("extendStatusTable").rows.length;
		var lastRow=$(document.getElementById ("extendStatusTable").rows [rowsCount-1]);
		//在最后一行后插入一行(插入后新行为最后一行)
		var lastRowHtml=generateLastRowHtml();	
		lastRow.after(lastRowHtml);
	}
	
	//先检查当前页面是否都已经填入值
	function checkConditionHasInput(){
		var rowsCount=document.getElementById("extendStatusTable").rows.length;
		for(var i=0;i<rowsCount;i++){
			var lastRow=document.getElementById ("extendStatusTable").rows[i];
			var typeCol=$(lastRow.cells[1]);//类型
			var contentCol=$(lastRow.cells[2]);//项目
			var jointDeptCol=$(lastRow.cells[3]);//相关部门
			var noteCol=$(lastRow.cells[4]);//状态
			
			//说明：由于每行的name都不一样，这里是查找name属性以checkConditionList开头，以.type(或.content或.jointDept或.note)结尾的input
			if(typeCol.find("input[name^='checkConditionList'][name$='.type']").val()==''){
				alert("请输入类型!");
				return false; 
			}
			if(contentCol.find("input[name^='checkConditionList'][name$='.content']").val()==''){
				alert("请输入项目!");
				return false;
			}
			if(jointDeptCol.find("input[name^='checkConditionList'][name$='.jointDept']").val()==''){
				alert("请输入相关部门!");
				return false;
			}
			if(noteCol.find("input[name^='checkConditionList'][name$='.note']").val()==''){
				alert("请输入状态!");
				return false;
			}
		}
		return true;
	}
	
	
	
	//产生倒数第二行的html
	function generateBoobyRowHtml(con){
		var html="<tr>";
		html+="<td width='5%' bgcolor='#FFFFFF'><div align='center'><input type='checkBox' name='checkCondition_"+con.id+"' value='checkCondition_"+con.id+"'/></div></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].type' type='text' id='' size='21' class='MyInput' value='"+con.type+"'/></td>";
		html+="<td width='25%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].content' type='text' id='' size='27' class='MyInput' value='"+con.content+"'/></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].jointDept' type='text' id='' size='21' class='MyInput' value='"+con.jointDept+"'/></td>";
		html+="<td width='30%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].note' type='text' id='' size='33' class='MyInput' value='"+con.note+"'/></td>";
		html+="</tr>";
		//增加一行后，下标自增
		checkConditionListIndex++;
		return html;
	}
	
	//在最后一行产生一新行
	function generateLastRowHtml(){
		var id=guidGenerator();
		var html="<tr>";
		html+="<td width='5%' bgcolor='#FFFFFF'><div align='center'><input type='checkBox' name='checkCondition_"+id+"' value='checkCondition_"+id+"'/></div></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].type' type='text' id='' size='21' class='MyInput' value=''/></td>";
		html+="<td width='25%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].content' type='text' id='' size='27' class='MyInput' value=''/></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].jointDept' type='text' id='' size='21' class='MyInput' value=''/></td>";
		html+="<td width='30%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].note' type='text' id='' size='33' class='MyInput' value=''/></td>";
		html+="</tr>";
		//增加一行后，下标自增
		checkConditionListIndex++;
		return html;
	}
	
	//解码处理
	function UrlDecode(zipStr){  
	    var uzipStr="";  
	    for(var i=0;i<zipStr.length;i++){  
	        var chr = zipStr.charAt(i);  
	        if(chr == "+"){  
	            uzipStr+=" ";  
	        }else if(chr=="%"){  
	            var asc = zipStr.substring(i+1,i+3);  
	            if(parseInt("0x"+asc)>0x7f){  
	                uzipStr+=decodeURI("%"+asc.toString()+zipStr.substring(i+3,i+9).toString());  
	                i+=8;  
	            }else{  
	                uzipStr+=AsciiToString(parseInt("0x"+asc));  
	                i+=2;  
	            }  
	        }else{  
	            uzipStr+= chr;  
	        }  
	    }  
	  
	    return uzipStr;  
	} 
	function StringToAscii(str){  
	    return str.charCodeAt(0).toString(16);  
	}  
	function AsciiToString(asccode){  
	    return String.fromCharCode(asccode);  
	}
	
	//产生GUID
	function guidGenerator() {
	    var S4 = function() {
	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	    };
	    return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
	}

</script>
<script type="text/javascript">

<%-- 检查条件模版的操作，暂时封存 

//跳转到增加检查条件页面
function addCheckCondition(){
	var result=OpenModal('/pages/staffEntry/entryInfo!addCheckCondition.action','400,200');
	if(result==true){
		alert("增加检查条件成功!");
		refreshCheckCondition();//刷新检查条件
	}
}
//跳转到修改检查条件页面
function editCheckCondition(){
	//查找被勾选的检查条件
	var selectCheckBoxArray=new Array();
	$("#extendStatusTable input[type:chekBox][checked]").each(function (){
		selectCheckBoxArray[selectCheckBoxArray.length]=$(this).attr("name");
	});
	if(selectCheckBoxArray.length>1){
		alert("修改时只能选择一个项!");
		return false;
	}else if(selectCheckBoxArray.length<1){
		alert("您没有选择检查项!");
		return false;
	}
	
	//从勾选的checkbox中分离出id
	var selectConditionId=selectCheckBoxArray[0].substring(selectCheckBoxArray[0].indexOf('_')+1);
	var result=OpenModal('/pages/staffEntry/entryInfo!editCheckCondition.action?checkCondition.id='+selectConditionId,'400,200');
	if(result==true){
		alert("修改检查条件成功!");
		refreshCheckCondition();//刷新检查条件
	}
}

//刷新检查条件
function refreshCheckCondition(){
	$.ajax({
		url: '<%=ctxPath%>/pages/staffEntry/entryInfo!queryCheckCondtion.action',
		data:{},
		type: 'POST',
		dataType: 'JSON',
		timeout: 10000,
		error: function(data,error){
			alert("刷新检查条件出错!");
			return false;
		},
		success: function(result){
			analysisCheckCondtion(result);//解析检查条件，然后放到extendStatusDiv中
		}
	});
}

//删除检查条件
function delCheckCondition(){
	//查找被勾选的检查条件
	var selectConditionIdArray=new Array();
	$("#extendStatusTable input[type:chekBox][checked]").each(function (){
		selectConditionIdArray[selectConditionIdArray.length]=$(this).attr("name").substring($(this).attr("name").indexOf('_')+1);
	});
	if(selectConditionIdArray.length<1){
		alert("您没有选择检查项!");
		return false;
	}
	
	//从勾选的checkbox中分离出id
	var conId="";
	for(var i=0;i<selectConditionIdArray.length;i++){
		conId+=selectConditionIdArray[i]+",";
	}
	if(selectConditionIdArray.length>1){
		conId=conId.substring(0, conId.length-1);
	}
	$.ajax({
		url: '<%=ctxPath%>/pages/staffEntry/entryInfo!delCheckCondition.action',
		data:{'checkCondition.id':conId},
		type: 'POST',
		dataType: 'JSON',
		timeout: 10000,
		error: function(data,error){
			alert("删除出错!");
			return false;
		},
		success: function(result){
			alert("修改检查条件成功!");
			refreshCheckCondition();//刷新检查条件
		}
	});
} --%>

</script>

<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="entryInfoForm" action="<%=request.getContextPath()%>/pages/staffEntry/entryInfo!save.action" method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table  width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  style="">
							<tr>
								
							</tr>
						</table>
						<br/>
						<!-- 用户id -->
						<input type="hidden" name="entryInfo.userId" id="entryInfoUserIdHidden" value=''>
						<input type="hidden" name="entryInfo.userName" id="entryInfoUserNameHidden" value=''>
						<input type="hidden" name="entryInfo.detName" id="entryInfoDetNameHidden" value=''>
								<table class="input_table">
							<tr >
								<td colspan="5" class="input_tablehead">入职信息</td>
							<tr>
								<tm:deptSelect 
									deptId="entryInfoDetName" 
									groupId="entryInfoGroupName"
									groupName="entryInfo.groupName"
									userId="entryInfoUserName" 
									deptHeadKey="---请选择部门---" 
									deptHeadValue="全选" 
									userHeadKey="---请选择人员---" 
									userHeadValue="全选"  
									groupHeadKey="--请选择组别--"
									groupHeadValue="全选"
									type="add" 
									isloadName="false"
									labelDept="选择部门 <font color='#FF0000\'>*</font>" 
									labelGroup="项目组" 
									labelUser="姓名 <font color='#FF0000\'>*</font>" 
									tableClass="width:100%;align:center;border:0;" 
									deptLabelClass="width:15%;class:input_label2"
									deptClass="bgcolor:#FFFFFF;width:20%;" 
									groupLabelClass=" width:15%;class:input_label2"
									groupClass="bgcolor:#FFFFFF;width:20%;" 
									userLabelClass="width:15%;class:input_label2"
									userClass="bgcolor:#FFFFFF;width:20%;" 
									>
								</tm:deptSelect>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										入职日期 <font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF">
									<input name="entryInfo.grgBeginDate" type="text" id="entryInfoGrgBeginDate" class="MyInput" value='<s:date name="entryInfo.regularDate" format="yyyy-MM-dd"/>'> 
								</td>
								<td  class="input_label2">
									<div align="center">
										转正日期 <font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="3" bgcolor="#FFFFFF">
									<input name="entryInfo.regularDate" type="text" id="entryInfoRegularDate" class="MyInput" value='<s:date name="entryInfo.regularDate" format="yyyy-MM-dd"/>' >
								</td>
								
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										备注
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea style="border: 0" onkeyup="limitLen(500,this)" cols="90" rows="3" name="entryInfo.note" id="entryInfoNote"></textarea>
									</div>
								</td>
							</tr>
							<tr>
								<td  height="67" class="input_label2">
									<div align="center">其他检查字段</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5" style="vertical-align: top;">
									<table id="extendStatusTable" width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#CCCCCC">
										<tr>
											<td width="7%" class="column_label" ><span>序号</span></td>
											<td width="20%"  class="column_label">
												<div align="center">
													<span>类型</span><font color="#FF0000">*</font>
												</div>
											</td>
											<td width="25%" class="column_label">
												<div align="center">
													<span>项目</span>
													<font color="#FF0000">*</font>
												</div>
											</td>
											<td width="20%" class="column_label">
												<div align="center">
												
													<span>关联部门</span><font color="#FF0000">*</font>
												</div>
											</td>
											<td width="30%" class="column_label">
												<div align="center">
													<span>状态</span><font color="#FF0000">*</font>
												</div>
											</td>
										</tr>
									</table>
									<table width="100%">
										<tr style="vertical-align: top;">
											<td><s:text name="label.admin.content" /></td>
											<td width="6%"><input type="button" value="新增" onClick="addAgain()" /></td>
											<td width="6%"><input type="button" value="删除" onClick="delAgain()" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						
				</td>
			</tr>
		</table>
		<br /> 
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
				<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
