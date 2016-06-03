﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html> 
	<head></head>
	<script type="text/javascript">
		var delQuestionId = "";
		var templateTable='<table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable" onclick="setTableBorder(this)" >'+
		'	<tr height="40px;">'+
		'		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;"><input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="在此输入打分项分类..." onblur="if (value ==\'\'){value=\'在此输入打分项分类...\'}" onfocus="setTypeBorderColor(this)" /></td>'+
		'		<td style="width:15%;text-align:left;background-color:#eeeeee;">'+
		'			<select id="questionSelect_list" name="questionSelect_list">'+
		'                 <option value="选填" selected="true">选填</option>'+
		'                 <option value="必填">必填</option>'+
		'            </select>'+
		'		</td>'+
		'		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">'+
		'			<input type="hidden" id="questionTypeId" name="questionTypeId" value="" />'+
		'			<input type="hidden" id="questionType_list" name="questionType_list" value="" />'+
		'			<input type="button" id="del" name="del" onclick="modifyType(this)" value="保存">'+
		'			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">'+
		'		</td>'+
		'    </tr>'+
		'	<tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="text-align:left;width:15%;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="" />'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="">'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="">'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="">'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="">'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>'+
		'</table><br/>';
		
		var addQuestionHtml='    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<input type="hidden" id="questionId" name="questionId" value="">'+
		'		<input type="hidden" id="questionTitle" name="questionTitle" value="" />'+
		'		<input type="hidden" id="questionWeight" name="questionWeight" value="" />'+
		'    </tr>';
		
		var otherTable='<tr>'+
		'	<td colspan="7" style="width:75%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="在此输入标题..." onblur="if (value ==\'\'){value=\'在此输入标题...\'}" onfocus="setOtherBorderColor(this)" /></td>'+
		'	<td style="width:10%;text-align:left;">'+
		'		<select id="questionOtherSelect_list" name="questionOtherSelect_list">'+
		'			 <option value="选填" selected="true">选填</option>'+
		'			 <option value="必填">必填</option>'+
		'		</select>'+
		'	</td>'+
		'	<td style="width:15%;text-align:right;">'+
		'		<input type="button" value="保存" onclick="modifyOther(this)"/>'+
		'		<input type="button" value="删除" onclick="delOther(this)"/>'+
		'		<input type="hidden" id="questionOtherId" name="questionOtherId" value="" />'+
		'		<input type="hidden" id="questionOtherTitle_list" name="questionOtherTitle_list" value="" />'+
		'	</td>'+
		'</tr>'+
		'<tr>'+
		'	<td colspan="9">'+
		'		<textarea name="content" id="content" style="width:100%;font-size:13px;margin-left:5px;" rows="3" ></textarea>'+
		'	</td>'+
		'</tr>';
		
		//新增打分项分类
		function addType(_this){
			$(_this).parent().before(templateTable);
		}
		//删除打分项分类
		function delType(_this){
			var id=$(_this).parent().find("input[id='questionTypeId']").val();
			if(id!="undefined" && id!=undefined){
				//请求后台处理，保存数据
				var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!deleteQuestionTypeInfo.action";
				$.ajax({
					url:url,
					data:{
						questionId:id
					},
					success:function(){}
				});
			}
			$(_this).parent().parent().parent().parent().prev().remove();
			$(_this).parent().parent().parent().parent().remove();
		}
		//新增打分项
		function addQuestion(_this){
			$(_this).parent().parent().after(addQuestionHtml);
			var html='<input type="button" value="删除" onclick="delQuestion(this)"/>';
			$(_this).parent().html('').html(html);
		}
		//删除打分项
		function delQuestion(_this){
			$(_this).parent().parent().remove();
			delQuestionId+=","+$(_this).parent().parent().find("input[id='questionId']").val();
		}
		//新增其他项
		function addOther(){
			$("#othertableinfo tbody tr:last").after(otherTable);
		}
		//删除其他项
		function delOther(_this){
			var id=$(_this).parent().find("input[id='questionOtherId']").val();
			if(id!="undefined" && id!=undefined){
				//请求后台处理，保存数据
				var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!deleteOtherInfo.action";
				$.ajax({
					url:url,
					data:{
						questionId:id
					},
					success:function(){}
				});
			}
			$(_this).parent().parent().next().remove();
			$(_this).parent().parent().remove();
			
		}
		
		//修改模板信息
		function modifyModule(_this){
			if($(_this).attr("value")=="修改"){
				var title = $(_this).parent().find("input[id='moduleTitle']").val();
				var titleHtml='<input type="text" id="module_title" name="moduleRecord.module_title" value="'+title+'" style="height:30px;width:30%;font-size:14px;text-align:center;" onfocus="setModuleTitleColor(this)" onblur="if (value ==\'\'){value=\'在此输入评估名称...\'}"/>';
				$(_this).parent().prev().html('').html(titleHtml);
				var desc = $(_this).parent().find("input[id='moduleDesc']").val();
				var descHtml = ""; 
				if(desc==""){
					desc = "在此输入评估描述...";
					descHtml='<tr><td colspan="2"><textarea name="moduleRecord.module_desc" id="module_desc" style="width:100%;font-size:13px;" rows="3" onfocus="if (value ==\'在此输入评估描述...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入评估描述...\'}">'+desc+'</textarea></td></tr>';
					$(_this).parent().parent().next().next().after(descHtml);
				}else{
					descHtml='<textarea name="moduleRecord.module_desc" id="module_desc" style="width:100%;font-size:13px;" rows="3" onfocus="if (value ==\'在此输入评估描述...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入评估描述...\'}">'+desc+'</textarea>';
					$(_this).parent().parent().next().next().next().children().html('').html(descHtml);
				}
				$(_this).attr("value","保存");
			}else{
				//判断模板抬头信息是否完整
				var moduleTitle = $("#module_title").val();
				if(moduleTitle=="在此输入评估名称..."){
					$("#module_title").css("border-color","red");
					alert("请输入评估名称");
					return;
				}
				var title=$("#module_title").val();
				var desc=$("#module_desc").val();
				if(desc=="在此输入评估描述...")
					desc = "";
				$("#moduleDesc").val(desc);
				$(_this).parent().prev().html('').html(title);
				if(desc==""){
					$(_this).parent().parent().parent().find("tr:last").remove();
				}else{
					$(_this).parent().parent().next().next().next().children().html('').html(desc);
				}
				$(_this).attr("value","修改");
				//请求后台处理
				var moduleId=$("#module_id").val();
				var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!updateModuleTitle.action";
				$.ajax({
					url:url,
					data:{
						moduleId:moduleId,
						module_title:title,
						module_desc:desc
					},
					success:function(data){
						
					}
				});
			}
		}
		
		//修改其他分类模块
		function modifyOther(_this){
			if($(_this).attr("value")=="修改"){
				$(_this).parent().prev().find("select[id='questionOtherSelect_list']").css("display","block");
				var val = $(_this).parent().find("input[id='questionOtherTitle_list']").val();
				var html='<input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="'+val+'" onblur="if (value ==\'\'){value=\'在此输入标题...\'}" onfocus="setOtherBorderColor(this)" />';
				$(_this).parent().prev().prev().html('').html(html);
				$(_this).attr("value","保存");
			}else{
				if(!validateOther(_this)){
					return;
				}
				var select = $(_this).parent().prev().find("select[id='questionOtherSelect_list']").val();
				$(_this).parent().prev().find("select[id='questionOtherSelect_list']").css("display","none");
				var val = $(_this).parent().prev().prev().find("input[id='questionOtherTitle_list']").val();
				$(_this).parent().find("input[id='questionOtherTitle_list']").val(val);
				if(val=="在此输入标题..."){
					$(_this).parent().parent().next().remove();
					$(_this).parent().parent().remove();
					return;
				}
				var html="";
				if(select=="必填"){
					html='<span style="color:red;margin-left:5px;">*</span>';
				}else{
					html='<span style="margin-left:5px;"></span>';
				}
				var html=html+val;
				$(_this).parent().prev().prev().html('').html(html);
				$(_this).attr("value","修改");
				if(val!="在此输入标题..."){
					var id=$(_this).parent().find("input[id='questionOtherId']").val();
					if(id=="undefined" || id==undefined || id==""){//新增
						var moduleId = $("#module_id").val();
						//请求后台处理，保存数据
						var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!saveOtherAddInfo.action";
						$.ajax({
							url:url,
							data:{
								module_id:moduleId,
								questionSelect:select,
								questionTitle:val
							},
							success:function(data){
								$(_this).parent().find("input[id='questionOtherId']").val(data.questionId);
							}
						});
					}else{//修改
						//请求后台处理，保存数据
						var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!updateOtherInfo.action";
						$.ajax({
							url:url,
							data:{
								questionId:id,
								questionSelect:select,
								questionTitle:val
							},
							success:function(){}
						});
					}
				}
			}
		}
		//修改打分项分类
		function modifyType(_this){
			if($(_this).attr("value")=="修改"){
				delQuestionId="";
				//显示打分项分类
				$(_this).parent().prev().find("select[id='questionSelect_list']").css("display","block");
				var val = $(_this).parent().find("input[id='questionType_list']").val();
				var html='<input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="'+val+'" onblur="if (value ==\'\'){value=\'在此输入打分项分类...\'}" onfocus="setTypeBorderColor(this)" />';
				$(_this).parent().prev().prev().html('').html(html);
				$(_this).attr("value","保存");
				
				//显示打分项
				var len = $(_this).parent().parent().parent().children().length;
				$(_this).parent().parent().parent().children().each(function(i){
					if(i==0) return;
					if(i==len){$(this).css("display","block");return;}
					$(this).find("input[id='delQuestionBut']").css("display","block");
					var title = $(this).find("input[id='questionTitle']").val();
					var titleHtml='<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="'+title+'" onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}" />';
					$(this).find("td:first").html('').html(titleHtml);
					var weight = $(this).find("input[id='questionWeight']").val();
					var weightHtml='<input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="'+weight+'" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)" onblur="modifyWeightVal(this)"/>';
					$(this).find("td:first").next().next().html('').html(weightHtml);
				});
				$(_this).parent().parent().parent().find("tr:last").after(addQuestionHtml);
			}else{
				//校验数据是否填写完整
				if(!validate(_this)){
					return;
				}
				//显示打分项分类
				var val = $(_this).parent().prev().prev().find("input[id='questionType_list']").val();
				$(_this).parent().find("input[id='questionType_list']").val(val);
				if(val=="在此输入打分项分类..."){
					alert("请输入打分项分类");
					return;
				}
				var select = $(_this).parent().prev().find("select[id='questionSelect_list']").val();
				$(_this).parent().prev().find("select[id='questionSelect_list']").css("display","none");
				var html="";
				if(select=="必填"){
					html='<span style="color:red;margin-left:5px;">*</span>';
				}else{
					html='<span style="margin-left:5px;"></span>';
				}
				var html=html+val;
				$(_this).parent().prev().prev().html('').html(html);
				$(_this).attr("value","修改");
				
				//显示打分项
				var len = $(_this).parent().parent().parent().children().length;
				$(_this).parent().parent().parent().children().each(function(i){
					if(i==0) return;
					var title = $(this).find("input[id='questionTitle_list']").val();
					if(title=="在此输入打分项..."){
						$(this).remove();
						return;
					}
					$(this).find("td:first").html('').html(title);
					var weight = $(this).find("input[id='questionWeight_list']").val();
					$(this).find("td:first").next().next().html('').html(weight);
					$(this).find("input[id='delQuestionBut']").css("display","none");
					$(this).find("input[id='questionTitle']").val(title);
					$(this).find("input[id='questionWeight']").val(weight);
				});
				var lastVal = $(_this).parent().parent().parent().find("tr:last").find("td:first").html();
				if(lastVal=="在此输入打分项..."){
					$(_this).parent().parent().parent().find("tr:last").remove();
				}else{
					var html='<input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/>';
					$(_this).parent().parent().parent().find("tr:last").find("td:first").next().html('').html(html);
					$(_this).parent().parent().parent().find("tr:last").find("input[id='delQuestionBut']").css("display","none");
				}
				
				//请求后台处理，保存数据
				var id=$(_this).parent().find("input[id='questionTypeId']").val();
				if(id=="undefined" || id==undefined || id==""){//新增
					var moduleId = $("#module_id").val();
					var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!saveQuestionTypeAddInfo.action";
					$.ajax({
						url:url,
						data:{
							module_id:moduleId,
							questionSelect:select,
							questionType:val
						},
						success:function(data){
							$(_this).parent().find("input[id='questionType_list']").val(val);
							$(_this).parent().find("input[id='questionTypeId']").val(data.questionId);
							saveQuestion(_this,data.questionId,delQuestionId);
						}
					});
				}else{//修改
					//请求后台处理，保存数据
					var moduleId = $("#module_id").val();
					var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!updateQuestionTypeInfo.action";
					$.ajax({
						url:url,
						data:{
							questionId:id,
							questionSelect:select,
							questionType:val
						},
						success:function(){
							var typeid=$(_this).parent().find("input[id='questionTypeId']").val();
							saveQuestion(_this,typeid,delQuestionId);
						}
					});
				}
			}
		}
		
		function saveQuestion(_this,typeid,delQuestionId){
			//保存打分项数据
			var jsonData = "";
			var len = $(_this).parent().parent().parent().children().length;
			$(_this).parent().parent().parent().children().each(function(i){
				if(i==0)return;
				var title = $(this).find("td:first").html();
				var weight = $(this).find("td:first").next().next().html();
				var questionId = $(this).find("input[id='questionId']").val();
				if(delQuestionId!="") delQuestionId = delQuestionId.substring(1);
				jsonData+=","+'{"typeid":"'+typeid+'","questionTitle":"'+title+'","questionWeight":"'+weight+'","questionId":"'+questionId+'","delQuestionId":"'+delQuestionId+'"}';
				//如果是新增
			});
			if(jsonData!="") jsonData="["+jsonData.substring(1)+"]";
		    var url = "<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!dealQuestionInfo.action";
			$.ajax({
				url:url,
				data:{
					jsonData:jsonData
				},
				success:function(data){
					$(_this).parent().find("input[id='questionTypeId']").val(data.questionId.split(",")[0]);
					$(_this).parent().parent().parent().children().each(function(i){
						if(i==0)return;
						$(this).find("input[id='questionId']").val(data.questionId.split(",")[i-1]);
					});
				}
			});
		}
		//保存打分项分类数据时校验相关数据
		var isSaveBut = false;
		function validate(_this){
			//保存数据时，判断是否有填写打分项分类，如果有，则判断是否有打分项
			var val = $(_this).parent().prev().prev().find("input[id='questionType_list']").val();
			if(val=="在此输入打分项分类..."){
				$(_this).parent().prev().prev().find("input[id='questionType_list']").css("border-color","red");
				alert("请输入打分项分类");
				return false;
			}
			var hasChildCount = 0;
			$(_this).parent().parent().parent().find("input[id='questionTitle_list']").each(function(i){
				var val = $(this).val();
				if(val!="在此输入打分项..."){
					hasChildCount++;
				}
			});
			if(hasChildCount==0){
				$(_this).parent().parent().parent().parent().css("border","1px solid red");
				isSaveBut = true;
				alert("请为打分项分类添加打分项");
				return false;
			}
			//判断打分项是否填写了对应的权重值
			var hasNoWrite = 0;
			$(_this).parent().parent().parent().find("input[id='questionTitle_list']").each(function(i){
				var val = $(this).val();
				if(val!="在此输入打分项..."){
					var weight_val = $(this).parent().next().next().find("input[id='questionWeight_list']").val();
					if(weight_val==""){
						$(this).parent().next().next().find("input[id='questionWeight_list']").css("border-color","red");
						alert("请填写权重值");
						hasNoWrite++;
						return false;
					}
				}
			});
			if(hasNoWrite>0)
				return false;
			
			var weightTotle = 0;
			$("input[id='questionWeight']").each(function(){
				var val = $(this).val();
				if(isNaN(val)) return;
				weightTotle = Number(weightTotle) + Number(val);
			});
			//判断权重值是否等于100
			if(weightTotle!=100){
				alert("权重值之和不等于100,请调整权重值");
				return false;
			}
			return true;
		}
		
		//保存其他标题时，校验相关数据
		function validateOther(_this){
			var otherval = $(_this).parent().prev().prev().find("input[id='questionOtherTitle_list']").val();
			if(otherval=="在此输入标题..."){
				$(_this).parent().prev().prev().find("input[id='questionOtherTitle_list']").css("border-color","red");
				alert("请输入标题");
				return false;
			}
			return true;
		}
		
		//判断评估名称边框颜色
		function setModuleTitleColor(_this){
			if($(_this).val()=="在此输入评估名称...")
				$(_this).val('');
			var css = $(_this).css("border-color");
			if(css=="rgb(255, 0, 0)" || css=="red"){
				$(_this).css("border-color","");
			}
		}
		//判断table的边框颜色
		function setTableBorder(_this){
			if(!isSaveBut){
				var css = $(_this).css("border-color");
				if(css=="rgb(255, 0, 0)" || css=="red"){
					$(_this).css("border","0");
				}
			}
			isSaveBut = false;
		}
		//判断打分项分类边框颜色
		function setTypeBorderColor(_this){
			if($(_this).val()=="在此输入打分项分类...")
				$(_this).val('');
			var css = $(_this).css("border-color");
			if(css=="rgb(255, 0, 0)" || css=="red"){
				$(_this).css("border-color","");
			}
		}
		//判断权重值边框颜色
		function setBorderColor(_this){
			var css = $(_this).css("border-color");
			if(css=="rgb(255, 0, 0)" || css=="red"){
				$(_this).css("border-color","");
			}
		}
		//判断其他边框的颜色
		function setOtherBorderColor(_this){
			if($(_this).val()=="在此输入标题...")
				$(_this).val('');
			var css = $(_this).css("border-color");
			if(css=="rgb(255, 0, 0)" || css=="red"){
				$(_this).css("border-color","");
			}
		}
		//权重值更改时，修改隐藏域questionWeight的值
		function modifyWeightVal(_this){
			var weight = $(_this).val();
			$(_this).parent().parent().find("input[id='questionWeight']").val(weight);
			
			var weightTotle = 0;
			$("input[id='questionWeight']").each(function(){
				var val = $(this).val();
				if(isNaN(val)) return;
				weightTotle = Number(weightTotle) + Number(val);
			});
			$("#totalWeight").html("总权重值:"+weightTotle);
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10">
<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/moduleSet/trainModuleSetInfo!save.action" method="post">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" style="border:solid 1px #cccccc;">
<tr>
	<td align="center">
        <table style="width:95%;" border="0" cellPadding="1" cellSpacing="1">
        	<br/>
        	<tr>
        		<td style="text-align:center;width:95%">
        			<s:property value='#request.moduleRecord.module_title'/>
        		</td>
        		<td align="right" width="5%">
        			<input type="hidden" id="moduleTitle" name="moduleTitle" value="<s:property value='#request.moduleRecord.module_title'/>"/>
        			<input type="hidden" id="moduleDesc" name="moduleDesc" value="<s:property value="#request.moduleRecord.module_desc"/>"/>
        			<input type="button" id="modifybut" name="modifybut" onclick="modifyModule(this)" value="修改">
        		</td>
            </tr>
            <tr>
            	<td colspan="2" style="text-align:left;font-size:13px;">课程名称：<s:property value="#request.moduleRecord.train_name"/>
            		<input type="hidden" id="train_name" name="moduleRecord.train_name" value="<s:property value='#request.moduleRecord.train_name'/>" />
            		<input type="hidden" id="train_id" name="moduleRecord.train_id" value="<s:property value='#request.moduleRecord.train_id'/>" />
        			<input type="hidden" id="module_id" name="module_id" value="<s:property value='#request.moduleRecord.id'/>" />
            	</td>
            </tr>
            <tr>
            	<td colspan="2" style="text-align:left;font-size:13px;">
            		<span style="float:left;">主讲嘉宾：<s:property value="#request.moduleRecord.train_people"/></span>
            		<span style="float:right;">学习日期：<s:date name="moduleRecord.train_date" format="yyyy-MM-dd"/></span>
            		<input type="hidden" id="train_people" name="moduleRecord.train_people" value="<s:property value='#request.moduleRecord.train_people'/>" />
            		<input type="hidden" id="train_date" name="moduleRecord.train_date" value="<s:property value='#request.moduleRecord.train_date'/>" />
            	</td>
            </tr>
            <s:if test='#request.moduleRecord.module_desc!=null'>
	            <tr>
	            	<td colspan="2">
	            		<s:property value="#request.moduleRecord.module_desc"/>
	            	</td>
	            </tr>
            </s:if>
        </table>
        <br/>
        <table class="input_table" style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;">
        	<tr height="30px;">
        		<td colspan="2" rowspan="2" style="width:50%;text-align:left;">注：以下打分项为单选&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red"><br/>1.【在此输入打分项...】的权重值不列入计算范围<br/>2.【在此输入打分项...】不进行保存</span></td>
        		<td style="text-align:left;width:15%;">&nbsp;</td>
        		<td style="width:10%;text-align:center;">权重值</td>
        		<td style="width:5%;text-align:center;">很好</td>
        		<td style="width:5%;text-align:center;">好</td>
        		<td style="width:5%;text-align:center;">一般</td>
        		<td style="width:5%;text-align:center;">较差</td>
        		<td style="width:5%;text-align:center;">差</td>
            </tr>
            <tr height="30px;">
        		<td style="text-align:center;">&nbsp;</td>
        		<td style="text-align:center;">%</td>
        		<td style="text-align:center;">5</td>
        		<td style="text-align:center;">4</td>
        		<td style="text-align:center;">3</td>
        		<td style="text-align:center;">2</td>
        		<td style="text-align:center;">1</td>
            </tr>
        </table>
        <br/>
        <s:iterator value="#request.domainList" id="domainList" status="row">
        	<table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable" onclick="setTableBorder(this)">
	        	<tr height="40px;">
	        		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;">
	        			<s:if test="%{#domainList.question_select=='必填'}"><span style="color:red;margin-left:5px;">*</span></s:if>
	        			<s:else><span style="margin-left:5px;"></span></s:else>
	        			<s:property value='question_type' />
	        		</td>
	        		<td style="width:15%;text-align:left;background-color:#eeeeee;">
	        			<select id="questionSelect_list" name="questionSelect_list" style="display:none;">
	                         <option value="选填" <s:if test="%{#domainList.question_select=='选填'}">selected</s:if> >选填</option>
	                         <option value="必填" <s:if test="%{#domainList.question_select=='必填'}">selected</s:if> >必填</option>
	                    </select>
	        		</td>
	        		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">
	        			<input type="hidden" id="questionType_list" name="questionType_list" value="<s:property value='question_type' />" />
	        			<input type="hidden" id="questionTypeId" name="questionTypeId" value="<s:property value='id' />" />
	        			<input type="button" id="del" name="del" onclick="modifyType(this)" value="修改">
	        			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">
	        		</td>
	            </tr>
	           	<s:iterator value="#domainList.quesionList" id="quesionList">
	           		<s:if test='#quesionList.question_title!="在此输入打分项..."'>
						<tr>
			        		<td colspan="2" style="width:50%;text-align:left;">
			        			<s:property value='question_title' />
			        		</td>
			        		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)" id="delQuestionBut" name="delQuestionBut" style="display:none;"/></td>
			        		<td style="width:10%;text-align:center;"><s:property value='question_weight' /></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<input type="hidden" id="questionId" name="questionId" value="<s:property value='id' />">
			        		<input type="hidden" id="questionTitle" name="questionTitle" value="<s:property value='question_title' />" />
			        		<input type="hidden" id="questionWeight" name="questionWeight" value="<s:property value='question_weight' />" />
			            </tr>
					</s:if>					           
				</s:iterator>
        	</table><br/>
        </s:iterator>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;">
        	<input type="button" value="新增" onclick="addType(this)" style="float:left;"/>
        	<span id="totalWeight" style="float:right;margin-right:220px;color:red;">总权重值:100</span>
        </div>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="othertableinfo" name="othertalbeinfo">
        	<tr height="40px;">
        		<td colspan="9" style="text-align:left;background-color:#eeeeee;">
        			<span style="margin-left:5px;">其他</span>
        		</td>
            </tr>
            <s:iterator value="#request.otherQuestionList" id="otherQuestionList" status="row">
	        	<tr>
	        		<td colspan="7" style="width:75%;text-align:left;">
	        			<s:if test="%{#otherQuestionList.question_select=='必填'}"><span style="color:red;margin-left:5px;">*</span></s:if>
	        			<s:else><span style="margin-left:5px;"></span></s:else>
	        			<s:property value='question_title' />
	        		</td>
	        		<td style="width:10%;text-align:left;">
	        			<select id="questionOtherSelect_list" name="questionOtherSelect_list" style="display:none;">
	                         <option value="选填" <s:if test="%{#otherQuestionList.question_select=='选填'}">selected</s:if> >选填</option>
	                         <option value="必填" <s:if test="%{#otherQuestionList.question_select=='必填'}">selected</s:if> >必填</option>
	                    </select>
	        		</td>
	        		<td style="width:15%;text-align:right;">
	        			<input type="hidden" id="questionOtherId" name="questionOtherId" value="<s:property value='id' />" />
	        			<input type="hidden" id="questionOtherTitle_list" name="questionOtherTitle_list" value="<s:property value='question_title' />" />
	        			<input type="button" value="修改" onclick="modifyOther(this)"/>
	        			<input type="button" value="删除" onclick="delOther(this)"/>
	        		</td>
	            </tr>
	            <tr>
	        		<td colspan="9">
	        			<textarea name="content" id="content" style="width:100%;font-size:13px;margin-left:5px;" rows="3" readonly></textarea>
	        		</td>
	            </tr>
	        </s:iterator>
        </table>
        <br/>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;"><input type="button" value="新增" onclick="addOther()"/></div>
        <div style="width:95%;text-align:center;margin-bottom:10px;margin-left:2px;">
        	<!-- <input type="button" value="保存" onclick="saveModule()"/> -->
        	<input type="button" value="关闭" onclick="closeModal();"/>
        	<input type="hidden" id="questionCount" name="questionCount" />
        </div>
	</td>
</tr>
</table>
</form>
</body>  
</html> 

