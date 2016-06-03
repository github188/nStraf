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
		var templateTable='<table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;">'+
		'	<tr height="40px;">'+
		'		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;"><input type="text" style="width:95%;margin-left:5px;" id="question_type" name="questionRecord.question_type" value="在此输入打分项分类..." onfocus="if (value ==\'在此输入打分项分类...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项分类...\'}"></td>'+
		'		<td style="width:15%;text-align:left;background-color:#eeeeee;">'+
		'			<select id="question_select" name="questionRecord.question_select">'+
		'                 <option value="选填" selected="true">选填</option>'+
		'                 <option value="必填">必填</option>'+
		'            </select>'+
		'		</td>'+
		'		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;"><input type="button" id="del" name="del" onclick="delType(this)" value="删除"></td>'+
		'    </tr>'+
		'	<tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="15" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="15" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="15" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'</table><br/>';
		
		var addQuestionHtml='    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="15" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="question_title" name="questionRecord.question_title" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="question_weight" name="questionRecord.question_weight"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>';
		
		var otherTable='<tr>'+
		'	<td colspan="7" style="width:75%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="在此输入标题..." onfocus="if (value ==\'在此输入标题...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入标题...\'}"></td>'+
		'	<td style="width:15%;text-align:left;">'+
		'		<select id="questionOtherSelect_list" name="questionOtherSelect_list">'+
		'			 <option value="选填" selected="true">选填</option>'+
		'			 <option value="必填">必填</option>'+
		'		</select>'+
		'	</td>'+
		'	<td style="width:10%;text-align:right;"><input type="button" value="删除" onclick="delOther(this)"/></td>'+
		'</tr>'+
		'<tr>'+
		'	<td colspan="9">'+
		'		<textarea name="content" id="content" style="width:100%;font-size:13px;margin-left:5px;" rows="3" readonly></textarea>'+
		'	</td>'+
		'</tr>';
		
		//新增打分项分类
		function addType(_this){
			$(_this).parent().before(templateTable);
		}
		//删除打分项分类
		function delType(_this){
			$(_this).parent().parent().parent().parent().next().remove();
			$(_this).parent().parent().parent().parent().remove();
		}
		//新增打分项
		function addQuestion(_this){
			$(_this).parent().parent().before(addQuestionHtml);
			$(_this).parent().parent().next().remove();
			$(_this).parent().parent().remove();
			/*var trHtml = $(_this).parent().parent().clone(true);
			$(_this).attr("value", "删除");
			$(_this).attr("onclick", "delQuestion(this)");//动态更改onclick事件在ie7下无效
			$(_this).parent().parent().after(trHtml);*/
		}
		//删除打分项
		function delQuestion(_this){
			$(_this).parent().parent().remove();
		}
		//新增其他项
		function addOther(){
			$("#othertableinfo tbody tr:last").after(otherTable);
		}
		//删除其他项
		function delOther(_this){
			$(_this).parent().parent().next().remove();
			$(_this).parent().parent().remove();
		}
		
		//保存模板信息
		function saveModule(){
			var count = "";
			$("table[id='questionTable']").each(function(){
				var len = $(this).find("input[id='questionTitle_list']").length;
				count+=","+len;
			});
			if(count.length>0)
				count=count.substring(1);
			$("#questionCount").val(count);
			window.returnValue = true;
			reportInfoForm.submit();
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
        		<td colspan="2" style="text-align:center;">
        			<input type="text" id="module_title" name="moduleRecord.module_title" value="学习效果现场评估" style="height:30px;width:30%;font-size:14px;text-align:center;"/>
        			<input type="hidden" id="train_id" name="moduleRecord.train_id" value="<%=request.getAttribute("trainId") %>" />
        		</td>
            </tr>
            <tr>
            	<td colspan="2" style="text-align:left;font-size:13px;">课程名称：<%=request.getAttribute("courseName") %>
            		<input type="hidden" id="train_name" name="moduleRecord.train_name" value="<%=request.getAttribute("courseName") %>" />
            	</td>
            </tr>
            <tr>
            	<td style="text-align:left;font-size:13px;">
            		<span style="float:left;">主讲嘉宾：<%=request.getAttribute("teacher") %></span>
            		<span style="float:right;">学习日期：<%=request.getAttribute("updateDate").toString().substring(0, 10) %></span>
            		<input type="hidden" id="train_people" name="moduleRecord.train_people" value="<%=request.getAttribute("teacher") %>" />
            		<input type="hidden" id="train_date" name="moduleRecord.train_date" value="<%=request.getAttribute("updateDate").toString().substring(0, 10) %>" />
            	</td>
            </tr>
            <tr>
            	<td>
            		<textarea name="moduleRecord.module_desc" id="module_desc" style="width:100%;font-size:13px;" rows="3" ></textarea>
            	</td>
            </tr>
        </table>
        <br/>
        <table class="input_table" style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;">
        	<tr height="30px;">
        		<td colspan="2" rowspan="2" style="width:50%;text-align:left;">注：以下打分项为单选</td>
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
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable">
        	<tr height="40px;">
        		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="内容" onfocus="if (value =='在此输入打分项分类...'){value =''}" onblur="if (value ==''){value='在此输入打分项分类...'}" />
        		</td>
        		<td style="width:15%;text-align:left;background-color:#eeeeee;">
        			<select id="questionSelect_list" name="questionSelect_list">
                         <option value="选填" selected="true">选填</option>
                         <option value="必填">必填</option>
                    </select>
        		</td>
        		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">
        			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">
        		</td>
            </tr>
        	<tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="课程框架清晰合理、层次分明？" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="15" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="课程内容有理论基础、有工具方法？" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="15" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="课程案例丰富、生动、贴切？" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="20" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
        </table>
        <br/>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable">
        	<tr height="40px;">
        		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="讲师 " onfocus="if (value =='在此输入打分项分类...'){value =''}" onblur="if (value ==''){value='在此输入打分项分类...'}" />
        		</td>
        		<td style="width:15%;text-align:left;background-color:#eeeeee;">
        			<select id="questionSelect_list" name="questionSelect_list">
                         <option value="选填" selected="true">选填</option>
                         <option value="必填">必填</option>
                    </select>
        		</td>
        		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">
        			<input type="button" id="del" name="del" onclick="delType(this)" value="删除" />
        		</td>
            </tr>
        	<tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="讲师的知识底蕴和实战经验？" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="15" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="讲师的授课表达、气氛调动、进度控制能力？" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="20" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
        </table>
        <br/>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable">
        	<tr height="40px;">
        		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="后勤 " onfocus="if (value =='在此输入打分项分类...'){value =''}" onblur="if (value ==''){value='在此输入打分项分类...'}" />
        		</td>
        		<td style="width:15%;text-align:left;background-color:#eeeeee;">
        			<select id="questionSelect_list" name="questionSelect_list">
                         <option value="选填" selected="true">选填</option>
                         <option value="必填">必填</option>
                    </select>
        		</td>
        		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">
        			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">
        		</td>
            </tr>
        	<tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="组织：培训的通知、接待、主持、讲义、流程等" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="10" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="硬件：培训场地和培训设施" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="5" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
            <tr>
        		<td colspan="2" style="width:50%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
        		</td>
        		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>
        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" /></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
        		<td style="width:5%;text-align:center;"><input type="radio"></td>
            </tr>
        </table>
        <br/>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;"><input type="button" value="新增" onclick="addType(this)"/></div>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="othertableinfo" name="othertalbeinfo">
        	<tr height="40px;">
        		<td colspan="9" style="text-align:left;background-color:#eeeeee;">
        			<span style="margin-left:5px;">其他</span>
        		</td>
            </tr>
        	<tr>
        		<td colspan="7" style="width:75%;text-align:left;">
        			<input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="在本次学习中，您的最大收获是什么？有什么建议？ " onfocus="if (value =='在此输入标题...'){value =''}" onblur="if (value ==''){value='在此输入标题...'}" />
        		</td>
        		<td style="width:15%;text-align:left;">
        			<select id="questionOtherSelect_list" name="questionOtherSelect_list">
                         <option value="选填" selected="true">选填</option>
                         <option value="必填">必填</option>
                    </select>
        		</td>
        		<td style="width:10%;text-align:right;"><input type="button" value="删除" onclick="delOther(this)"/></td>
            </tr>
            <tr>
        		<td colspan="9">
        			<textarea name="content" id="content" style="width:100%;font-size:13px;margin-left:5px;" rows="3" readonly></textarea>
        		</td>
            </tr>
        </table>
        <br/>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;"><input type="button" value="新增" onclick="addOther()"/></div>
        <div style="width:95%;text-align:center;margin-bottom:10px;margin-left:2px;">
        	<input type="button" value="保存" onclick="saveModule()"/>
        	<input type="button" value="关闭" onclick="closeModal();"/>
        	<input type="hidden" id="questionCount" name="questionCount" />
        </div>
	</td>
</tr>
</table>
</form>
</body>  
</html> 

