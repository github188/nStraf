<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<html>
<head>
	<title>add date income</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>

<script language="javascript">


 
 
/*新增人日收入的记录
 * 
 */
 function addagain(){	
		var task1=$("#tr1");
		var task2 = task1.clone(true); //复制当前的记录行
		task2.find("input:eq(1)").val("");//将task2中的输入框中数据清掉
		//将复制的tr 添加到最后一个tr的后面
		var checkbox=$(".checkbox");
		var length=checkbox.length;
		for(var i=1;i<checkbox.length+1;i++){
			task2.find("select").attr("name","dateincomelist["+i+"].prjGroup");
			task2.find("input:eq(1)").attr("name","dateincomelist["+i+"].dateIncome");//将task2中的输入框中数据清掉
		}
		var lasttr=checkbox[length-1];
		task2.insertAfter(lasttr);

	}
	
  function delagain(){ 
		var aa = document.getElementsByName("chkList");
		var checkboxlength=aa.length;
		var checkedlength=0;
		for ( var i = 0; i < aa.length; i++) {
			if (aa[i].checked) {
				itemId = aa[i].value;
				checkedlength++;
			}
			
		}
		if(checkboxlength-checkedlength==0){
			alert("请至少填写一条记录");
		}else{
			$("input:checked").each(function (){
				
					$(this).parents().eq(1).remove();
			});
		}
 }
 

 
//确认添加数据
	function save(){
		var isIncome=/^[1-9]\d*$/;
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var  dateIncome=$("#dateIncome").val();
		var  prjGroup=$("#prjGroup").val();
		if(startTime==""){
			alert("请填写项目起始时间！");
			$("#startTime").focus();
			return;
		}
		if(endTime==""){
			alert("请填写项目结束时间！");
			$("#endTime").focus();
			return;
		}
		if(startTime>endTime){
			alert("起始时间不能大于结束时间");
			return;
		}
		if(dateIncome==""){
			alert("请填写人日收入！");
			$("#dateIncome").focus();
			return;
		}
		if(!isIncome.test(dateIncome)){
			alert("您输入的不是正整数！");
			$("#dateIncome").focus();
			return;
		}
		if(prjGroup==""||prjGroup==="请选择项目组"){
			alert("请填写项目名称！");
			$("#prjGroup").focus();
			return;
		}
	    document.getElementById("ok").disabled=true;
		window.returnValue=true;
		adddateincomeForm.submit();
	}	

</script>

<body id="bodyid"  leftmargin="0" topmargin="0">
<form name="adddateincomeForm" action="dateincome!save.action";
		namespace="/pages/costControl" method="post">
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
					<table class="input_table">
			      		<tr>
							<td class="input_tablehead">
								人日收入确认管理
							</td>
			      		</tr>
			      		<tr >
			      			
			      			<td  bgcolor="#FFFFFF"  width="2%"><div></div></td>
			      			<td   bgcolor="#FFFFFF"  width="15%"><div>起始时间：<font color="#FF0000">*</font></div></td>
			      			
			      			<td colspan="3" bgcolor="#FFFFFF">
			      				<input   id="startTime"  name="dateincomeManage.startTime" type="text" class="MyInput">至<input class="MyInput"  type="text" id="endTime"   name="dateincomeManage.endTime">
			      			</td>
			      		</tr>
			      		<tr  id="tr1" class="checkbox">
				      		<td bgcolor="#FFFFFF"  width="2%" nowrap align="center"><input type="checkbox" name="chkList"  />
							</td>
			      			<td bgcolor="#FFFFFF"  width="10%">项目组：<font color="#FF0000">*</font></td>
			      			<td bgcolor="#FFFFFF" width="25%">
			      			<select name="dateincomelist[0].prjGroup" id="prjGroup" name="status" style="width: 90%">
								<option value="" selected="selected">请选择项目组</option>
									<s:iterator value="#request.pro" id="ele">
									<option value="<s:property value='name'/>"><s:property value='name'/></option>
							</s:iterator>
							</select>
							</td>
			      			<td bgcolor="#FFFFFF"  width="10%">人日收入：<font color="#FF0000">*</font></td>
			      			<td bgcolor="#FFFFFF"  width="25%"><input id="dateIncome" name="dateincomelist[0] .dateIncome">人日</td>
			      		</tr>
			      		
			      		<tr>
			      		
			      		<td colspan="5" align="right">
			      			<input type="button" size="11" value="新增" onclick='addagain()'>
							<input type="button" size="11" value="删除" onclick='delagain()'></td>
						</tr>
					</table>
				</td>
				</tr>
					
				</table>
			<br>
			<br>
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
