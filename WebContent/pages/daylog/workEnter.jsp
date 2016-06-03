<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<%@page import="cn.grgbanking.feeltm.costControl.bean.*"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-timepicker-addon.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<html> 
	<head></head>
	<script type="text/javascript">
		function save(){
			//检测确认工时是否有为空的
			var flag = "";
			$("#downloadList").find("tr").each(function(i){
				if(i==0){
					return;
				}
				//该行是否被选择了，未选择的行不进行检查
				var isSelect=$(this).find("input[name='isRowSelect']").val();
				if(isSelect=='true'){
					var val = $(this).find("input[name='enterlogList_confirmHour']").val();
					if(val==""){
						flag="true";
					}
				}
			});
			if(flag!=""){
				alert("未确认框中确认工时不能为空");
				return;
			}
			
			$("#confirmdownloadList").find("tr").each(function(i){
				if(i==0){
					return;
				}
				//该行是否被选择了，未选择的行不进行检查
				var isSelect=$(this).find("input[name='isRowSelect']").val();
				if(isSelect=='true'){
					var val = $(this).find("input[name='enterlogList_confirmHour']").val();
					if(val==""){
						flag="true";
					}
				}
			});
			if(flag!=""){
				alert("已确认框中确认工时不能为空");
				return;
			}
			//获取项目已消耗人日
			var actionUrl ="<%=request.getContextPath() %>/pages/daylog/logInfo!getConfirmInfoString.action?role="+$("#role").val()+"&startdate="+$("#startdate").val();
			actionUrl=encodeURI(actionUrl);
			$.ajax({
				url : actionUrl,
				data : {},
				type : 'POST',
				dataType : 'json',
				timeout : 30000,
				success : function(data) {
					alert(data.info);
					reportInfoForm.submit();
				},
				error : function(data, data1) {
					alert("获取提示信息失败!"+data+","+data1)
					reportInfoForm.submit();
				}
			});
			
		}
		
		 function showInfo(id){
			var strUrl="/pages/daylog/logInfo!show.action?ids="+id+"&operate=show";
			var features="900,600,signrecord.title_view";	
			var resultvalue =OpenModal(strUrl,features);	
		 }
		 
		 
		//修改确认工时时，修改列表上对应的人日数据
		function modifyListData(_this){
			var reg =  /^\d+(\.\d{1,2})?$/;
			var val = $(_this).val();
			if(!reg.test(val)){
				$(_this).val("");
				return;
			}
			//确认人日=项目人日*(项目确认总工时/项目总工时)
			var projectHour = $(_this).parent().prev().prev().html();
			var projectDay = $(_this).parent().prev().html();
			var newConfireHour = $(_this).val();
			//var allConfirmHour = $(_this).parent().find("input[name='allConfirmHour']").val();
			//var oldConfirmHour = $(_this).parent().find("input[name='oldConfirmHour']").val();
			//var newAllConfirmHour = parseFloat(allConfirmHour)-parseFloat(oldConfirmHour)+parseFloat(newConfireHour);
			//var enterDay = roundFun(parseFloat(newConfireHour)/parseFloat(newAllConfirmHour),2);
			//var enterDay = roundFun(parseFloat(projectDay)*(parseFloat(newConfireHour)/parseFloat(projectHour)),2);
			var enterDay = ''; //更改确认工时人日错误 bug
			if (parseFloat(newConfireHour) > 8) {
				enterDay = 1.0;
			} else{
				enterDay = roundFun(parseFloat(newConfireHour) / 8,2);
			} 
			
			$(_this).parent().next().html(enterDay);
		}	
		
		function roundFun(numberRound,roundDigit){   
			if(numberRound>=0){   
			  var tempNumber = parseInt((numberRound * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
			  return tempNumber;   
			}else{   
			  numberRound1=-numberRound;
			  var tempNumber = parseInt((numberRound1 * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
			  return -tempNumber;   
			}   
		}
		
		$(function(){
			//在项目栏勾选项目时，自动勾选“待确认”列表中对应项目的checkbox
			$("input[name='projectSelect']").change(function(){
			    var checked=($(this).attr('checked')==true)||($(this).prop('checked')==true);
				var cpId=$(this).val();
				$("input[name='allRowsProjectId']").each(function(){
					if($(this).val()==cpId){
						if(checked){
							$(this).prev().val('true');//被选中了
							$(this).parent().parent().find("input[group='selectOrQuitRow']").attr('checked','checked');//设置对应行的chekbox
							$(this).parent().parent().find("input[group='selectOrQuitRow']").prop('checked','checked');
						}else{
							$(this).prev().val('false');//没有选择
							$(this).parent().parent().find("input[group='selectOrQuitRow']").removeAttr('checked');//设置对应行的chekbox
						}
					}
				})
			});
			
			//“待确认”和“已确认”列表中全选按钮被勾选后，自动勾选对应列表中所有的行
			$("input[group='selectOrQuitAll']").change(function(){
				var checked=$(this).attr('checked')==true||$(this).prop('checked')==true;
				if(checked){
					$(this).parent().parent().parent().parent().find("input[group='selectOrQuitRow']").attr('checked','checked');
					$(this).parent().parent().parent().parent().find("input[group='selectOrQuitRow']").prop('checked','checked');
					$(this).parent().parent().parent().parent().find("input[name='isRowSelect']").val('true');
				}else{
					$(this).parent().parent().parent().parent().find("input[group='selectOrQuitRow']").removeAttr('checked');
					$(this).parent().parent().parent().parent().find("input[name='isRowSelect']").val('false');
				}
			});
			
			//某行的checkbox被勾选后，自动修改该行隐藏控件的值
			$("input[group='selectOrQuitRow']").change(function(){
				var checked=$(this).attr('checked')==true||$(this).prop('checked')==true;
				if(checked){
					$(this).parent().parent().find("input[name='isRowSelect']").val('true');
				}else{
					$(this).parent().parent().find("input[name='isRowSelect']").val('false');
				}
			});
		});
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/daylog/logInfo!saveMoreConfirmInfo.action" method="post">
<input type="hidden" id="role" name="role" value="<%=request.getAttribute("role").toString() %>" />
<input type="hidden" id="startdate" name="startdate" value="<%=request.getAttribute("startdate").toString() %>"/>
<table width="100%" cellpadding="0" cellspacing="0" align="center"> 
	<tr>
		<td align="center"> 
	 		<input type="button" name="ok"  value='确认工时' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
	 		&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" name="return" value='关闭窗口' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif">
			&nbsp;&nbsp;&nbsp;&nbsp;
		</td> 
	</tr>  
</table> 
<div style="height:130px;overflow-y:auto;">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
	<tr height="95%">
		<td>
            <table class="input_table" height="90%"  style="word-break:break-all;word-wrap:break-word;">
            <tr>
            	<td class="input_label2" colspan="2">
					<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
						<tr>
							<td class="orarowhead">选择确认项目:</td>
						</tr>
					</table>
					<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
						<img src="../../images/loading.gif">
					</div>
					<table width="100%" border="1" cellpadding="1" cellspacing="1">
					<%
						List<EnterProject> plist=(List<EnterProject>)request.getAttribute("enterProjectList");
						int cols=5;
						int rows=(plist.size()%cols==0)?(plist.size()/cols):(plist.size()/cols +1);
						for(int row=0;row<rows;row++){
					%>
						<tr>
					<%
							for(int col=0;col<cols;col++){
								int index=row*cols+col;//下标
								if(index+1<=plist.size()){
									String projectId=plist.get(index).getProjectId();
									String projectName=plist.get(index).getProjectName();
					%>
									<td width="20%" nowrap style="overflow:hidden; padding-left:3px;padding-right:3px" ><input type="checkbox" checked name="projectSelect" value="<%=projectId%>"><%=projectName%></td>
					<%				
								}else{
					%>
									<td width="20%" nowrap style="overflow:hidden; padding-left:3px;padding-right:3px" ></td>
					<%				
								}
							}
					%>	
						</tr>
					<%
						}
					%>
					</table>
               	</td>
			</tr>
        </table>
	</td> 
</tr>
</table>
</div>
<div style="height:300px;overflow-y:auto;">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
	<tr height="95%">
		<td>
            <table class="input_table" height="90%"  style="word-break:break-all;word-wrap:break-word;">
            <tr>
            	<td class="input_label2" colspan="2">
            		<input type="hidden" name="noDealId" id="noDealId" value="" />
					<input type="hidden" name="pageNum" id="pageNum" value="1" />
					<input type="hidden" name="pageSize" id="pageSize" value="10" />
					<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
					<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
						<tr>
							<td class="orarowhead">批量工时确认(未确认)</td>
							<td align="right" width="75%"><font color="red">确认工时只允许输入2位小数</font></td>
						</tr>
					</table>
					<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
						<img src="../../images/loading.gif">
					</div>
					<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
						<tr>
							<td nowrap width="3%" class="oracolumncenterheader"><input type="checkbox" checked group="selectOrQuitAll"></td>
							<td nowrap width="10%" class="oracolumncenterheader"><div align="center">日期</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">姓名</div></td>
							<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">项目工时</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">项目人日</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">确认工时</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">确认人日</div></td>
							<td nowrap width="7%" class="oracolumncenterheader"><div align="center">状态</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">查看详情</div></td>
							<td nowrap width="17%" class="oracolumncenterheader"><div align="center">确认备注</div></td>
						</tr>
						<tbody name="formlist" id="formlist" align="center">
							<s:iterator value="worklist" id="tranInfo" status="row">
								<s:if test="#row.odd == true">
									<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:if>
								<s:else>
									<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:else>
									<td><input type="checkbox" checked group="selectOrQuitRow"></td>
									<td><s:date name="logDate" format="yyyy-MM-dd" /></td>
									<td><s:property value="userName" /></td>
									<td title='<s:property value="groupName" />'><s:property value="groupName" /></td>
									<td><s:property value="projectHour" /></td>
									<td><s:property value="projectDay" /></td>
									<td>
										<input type="text" value='<s:property value="confirmHour" />' style="width:100%;" name="enterlogList_confirmHour" onblur="modifyListData(this)">
										<input type="hidden" value='<s:property value="userId" />,<s:date name="logDate" format="yyyy-MM-dd" />,<s:property value="groupName" />,<s:property value="confirmStatus" />' name="enterlogList_id">
										<input type="hidden" value='<s:property value="allProjectHour" />' name="enterlogList_projectHour">
										<input type="hidden" value='<s:property value="allConfirmHour" />' name="allConfirmHour">
										<input type="hidden" value='<s:property value="confirmHour" />' name="oldConfirmHour">
										<input type="hidden" name="isRowSelect"  value='true'><!-- 该行是否被选择了 -->
										<input type="hidden" name="allRowsProjectId"  value='<s:property value="prjName" />'>
									</td>
									<td><s:property value="enterDay" /></td>
									<td>
										<s:if test="confirmStatus==0">未确认</s:if>
						            	<s:else>已确认</s:else>
									</td>
									<td>
										<a href='javascript:showInfo("<s:property value="id"/>")'><font color="blue">查看详情</font></a>
									</td>
									<td><input type="text" style="width:100%;" name="enterlogList_confirmDesc"></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
               	</td>
			</tr>
        </table>
	</td> 
</tr>
</table>
</div>
<div style="height:300px;overflow-y:auto;">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
	<tr height="95%">
		<td>
            <table class="input_table" height="90%"  style="word-break:break-all;word-wrap:break-word;">
            <tr>
            	<td class="input_label2" colspan="2">
            		<input type="hidden" name="noDealId" id="noDealId" value="" />
					<input type="hidden" name="pageNum" id="pageNum" value="1" />
					<input type="hidden" name="pageSize" id="pageSize" value="10" />
					<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
					<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
						<tr>
							<td class="orarowhead">批量工时确认(已确认)</td>
							<td align="right" width="75%"><font color="red">确认工时只允许输入2位小数</font></td>
						</tr>
					</table>
					<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
						<img src="../../images/loading.gif">
					</div>
					<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=confirmdownloadList>
						<tr>
							<td nowrap width="3%" class="oracolumncenterheader"><input type="checkbox" group="selectOrQuitAll"></td>
							<td nowrap width="10%" class="oracolumncenterheader"><div align="center">日期</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">姓名</div></td>
							<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">项目工时</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">项目人日</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">确认工时</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">确认人日</div></td>
							<td nowrap width="7%" class="oracolumncenterheader"><div align="center">状态</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">查看详情</div></td>
							<td nowrap width="17%" class="oracolumncenterheader"><div align="center">确认备注</div></td>
						</tr>
						<tbody name="formlist" id="formlist" align="center">
							<s:iterator value="confirmworklist" id="tranInfo" status="row">
								<s:if test="#row.odd == true">
									<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:if>
								<s:else>
									<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:else>
									<td><input type="checkbox" group="selectOrQuitRow"></td>
									<td><s:date name="logDate" format="yyyy-MM-dd" /></td>
									<td><s:property value="userName" /></td>
									<td title='<s:property value="groupName" />'><s:property value="groupName" /></td>
									<td><s:property value="projectHour" /></td>
									<td><s:property value="projectDay" /></td>
									<td>
										<input type="text" value='<s:property value="confirmHour" />' style="width:100%;" name="enterlogList_confirmHour" onblur="modifyListData(this)">
										<input type="hidden" value='<s:property value="userId" />,<s:date name="logDate" format="yyyy-MM-dd" />,<s:property value="groupName" />,<s:property value="confirmStatus" />' name="enterlogList_id">
										<input type="hidden" value='<s:property value="allProjectHour" />' name="enterlogList_projectHour">
										<input type="hidden" value='<s:property value="allConfirmHour" />' name="allConfirmHour">
										<input type="hidden" value='<s:property value="confirmHour" />' name="oldConfirmHour">
										<input type="hidden" name="isRowSelect"  value='false'><!-- 该行是否被选择了 -->
									</td>
									<td><s:property value="enterDay" /></td>
									<td>
										<s:if test="confirmStatus==0">未确认</s:if>
						            	<s:else>已确认</s:else>
									</td>
									<td>
										<a href='javascript:showInfo("<s:property value="id"/>")'><font color="blue">查看详情</font></a>
									</td>
									<td><input type="text" style="width:100%;" name="enterlogList_confirmDesc"></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
               	</td>
			</tr>
        </table>
	</td> 
</tr>
</table>
</div>
</form>

</body>  
</html> 

