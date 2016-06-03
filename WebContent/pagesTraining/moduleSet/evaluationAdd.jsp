<%@ page contentType="text/html; charset=UTF-8" %>
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
		var templateTable='<table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="questionTable" onclick="setTableBorder(this)">'+
		'	<tr height="40px;">'+
		'		<td colspan="2" style="width:50%;text-align:left;background-color:#eeeeee;"><input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="在此输入打分项分类..." onblur="if (value ==\'\'){value=\'在此输入打分项分类...\'}" onfocus="setTypeBorderColor(this)"></td>'+
		'		<td style="width:15%;text-align:left;background-color:#eeeeee;">'+
		'			<select id="questionSelect_list" name="questionSelect_list">'+
		'                 <option value="选填">选填</option>'+
		'                 <option value="必填" selected="true">必填</option>'+
		'            </select>'+
		'		</td>'+
		'		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">'+
		'			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">'+
		'		</td>'+
		'    </tr>'+
		'	<tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="text-align:left;width:15%;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="删除" id="delQuestionBut" name="delQuestionBut" onclick="delQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" value="" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>'+
		'</table><br/>';
		
		var addQuestionHtml='    <tr>'+
		'		<td colspan="2" style="width:50%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value ==\'在此输入打分项...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入打分项...\'}"></td>'+
		'		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>'+
		'		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,\'\')" onfocus="setBorderColor(this)"/></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'		<td style="width:5%;text-align:center;"><input type="radio"></td>'+
		'    </tr>';
		
		var otherTable=otherTable='<tr>'+
		'	<td colspan="7" style="width:75%;text-align:left;"><input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="在此输入标题..." onfocus="if (value ==\'在此输入标题...\'){value =\'\'}" onblur="if (value ==\'\'){value=\'在此输入标题...\'}"></td>'+
		'	<td style="width:10%;text-align:left;">'+
		'		<select id="questionOtherSelect_list" name="questionOtherSelect_list">'+
		'			 <option value="选填" selected="true">选填</option>'+
		'			 <option value="必填">必填</option>'+
		'		</select>'+
		'	</td>'+
		'	<td style="width:15%;text-align:right;">'+
		'		<input type="button" value="删除" onclick="delOther(this)"/>'+
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
			$(_this).parent().parent().parent().parent().next().remove();
			$(_this).parent().parent().parent().parent().remove();
		}
		//新增打分项
		function addQuestion(_this){
			$(_this).parent().parent().after(addQuestionHtml);
			var html='<input type="button" value="删除" onclick="delQuestion(this)"/>';
			$(_this).parent().html('').html(html);
			/*$(_this).parent().parent().before(addQuestionHtml);//使用上面的方法更加方便
			$(_this).parent().parent().next().remove();
			$(_this).parent().parent().remove();*/
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
			if(!validate()){
				return;	
			}
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
		
		//保存前校验权重值是否为100
		function validate(){
			//判断模板抬头信息是否完整
			var moduleTitle = $("#module_title").val();
			if(moduleTitle=="在此输入评估名称..."){
				$("#module_title").css("border-color","red");
				alert("请输入评估名称");
				return;
			}
			//判断哪些打分项分类下无打分项的
			var isReturn = false;
			$("table[id='questionTable']").each(function(){
				var typeval = $(this).find("input[id='questionType_list']").val();
				if(typeval=="在此输入打分项分类..."){
					var hasChildCount = 0;
					$(this).find("input[id='questionTitle_list']").each(function(i){
						var val = $(this).val();
						if(val!="在此输入打分项..."){
							hasChildCount++;
						}
					});
					if(hasChildCount>0){
						$(this).find("input[id='questionType_list']").css("border-color","red");
						alert("请输入打分项分类");
						isReturn = true;
						return;
					}
				}else{
					var hasChildCount = 0;
					$(this).find("input[id='questionTitle_list']").each(function(i){
						var val = $(this).val();
						if(val!="在此输入打分项..."){
							hasChildCount++;
						}
					});
					if(hasChildCount==0){
						$(this).css("border","1px solid red");
						alert("请为打分项分类添加打分项");
						isReturn = true;
						return;
					}
				}
			});
			if(isReturn)
				return false;
			//判断哪些打分想没有权重值的，除“在此输入打分项...”
			var hasNoWrite = 0;
			var weight=0;
			$("table[id='questionTable']").each(function(){
				$(this).find("input[id='questionTitle_list']").each(function(i){
					var val = $(this).val();
					if(val!="在此输入打分项..."){
						var weight_val = $(this).parent().next().next().find("input[id='questionWeight_list']").val();
						if(weight_val==""){
							$(this).parent().next().next().find("input[id='questionWeight_list']").css("border-color","red");
							alert("请填写权重值");
							hasNoWrite++;
							return false;
						}else{
							weight = Number(weight)+Number(weight_val);
						}
					}
				});
			});
			if(hasNoWrite!=0)
				return false;
			if(weight!=100){
				alert("权重值之和必须等于100");
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
			var css = $(_this).css("border-color");
			if(css=="rgb(255, 0, 0)" || css=="red"){
				$(_this).css("border","0");
			}
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
		//判断边框的颜色 如果为红色，当文本框取得焦点时，去掉红色边框
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
        			<input type="text" id="module_title" name="moduleRecord.module_title" value="<s:property value='#request.moduleRecord.module_title'/>" style="height:30px;width:30%;font-size:14px;text-align:center;" onfocus="setModuleTitleColor(this)" onblur="if (value ==''){value='在此输入评估名称...'}"/>
        			<input type="hidden" id="train_id" name="moduleRecord.train_id" value="<s:property value='#request.trainId'/>" />
        		</td>
            </tr>
            <tr>
            	<td colspan="2" style="text-align:left;font-size:13px;">课程名称：<s:property value="#request.courseName"/>
            		<input type="hidden" id="train_name" name="moduleRecord.train_name" value="<s:property value='#request.courseName'/>" />
            	</td>
            </tr>
            <tr>
            	<td style="text-align:left;font-size:13px;">
            		<span style="float:left;">主讲嘉宾：<s:property value="#request.teacher"/></span>
            		<span style="float:right;">学习日期：<s:property value='#request.updateDate'/></span>
            		<input type="hidden" id="train_people" name="moduleRecord.train_people" value="<s:property value='#request.teacher'/>" />
            		<input type="hidden" id="train_date" name="moduleRecord.train_date" value="<s:property value='#request.updateDate'/>" />
            	</td>
            </tr>
            <tr>
            	<td>
            		<textarea name="moduleRecord.module_desc" id="module_desc" style="width:100%;font-size:13px;" rows="3" onfocus="if (value =='在此输入评估描述...'){value =''}" onblur="if (value ==''){value='在此输入评估描述...'}"><s:property value="#request.moduleRecord.module_desc"/></textarea>
            	</td>
            </tr>
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
	        			<input type="text" style="width:95%;margin-left:5px;" id="questionType_list" name="questionType_list" value="<s:property value='question_type' />" onblur="if (value ==''){value='在此输入打分项分类...'}" onfocus="setTypeBorderColor(this)" />
	        		</td>
	        		<td style="width:15%;text-align:left;background-color:#eeeeee;">
	        			<select id="questionSelect_list" name="questionSelect_list">
	                         <option value="选填" <s:if test="%{#domainList.question_select=='选填'}">selected</s:if> >选填</option>
	                         <option value="必填" <s:if test="%{#domainList.question_select=='必填'}">selected</s:if> >必填</option>
	                    </select>
	        		</td>
	        		<td colspan="6" style="width:25%;text-align:right;background-color:#eeeeee;">
	        			<input type="button" id="del" name="del" onclick="delType(this)" value="删除">
	        		</td>
	            </tr>
	           	<s:iterator value="#domainList.quesionList" id="quesionList"> 
	           		<s:if test='#quesionList.question_title=="在此输入打分项..."'>
	           			<tr>
			        		<td colspan="2" style="width:50%;text-align:left;">
			        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="在此输入打分项..." onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
			        		</td>
			        		<td style="width:15%;text-align:left;"><input type="button" value="新增" onclick="addQuestion(this)"/></td>
			        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" onkeyup="value=value.replace(/[^0-9]/g,'')" onfocus="setBorderColor(this)"/></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			            </tr>
	           		</s:if>
					<s:else>
						<tr>
			        		<td colspan="2" style="width:50%;text-align:left;">
			        			<input type="text" style="width:95%;margin-left:5px;" id="questionTitle_list" name="questionTitle_list" value="<s:property value='question_title' />" onfocus="if (value =='在此输入打分项...'){value =''}" onblur="if (value ==''){value='在此输入打分项...'}" />
			        		</td>
			        		<td style="text-align:left;width:15%;"><input type="button" value="删除" onclick="delQuestion(this)"/></td>
			        		<td style="width:10%;text-align:center;"><input type="text" style="width:50px;" id="questionWeight_list" name="questionWeight_list" value="<s:property value='question_weight' />" onkeyup="value=value.replace(/[^0-9]/g,'')" onfocus="setBorderColor(this)"/></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			        		<td style="width:5%;text-align:center;"><input type="radio"></td>
			            </tr>
					</s:else>
	            </s:iterator>
        	</table>
        </s:iterator>
        <br/>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;"><input type="button" value="新增" onclick="addType(this)"/></div>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="othertableinfo" name="othertalbeinfo">
        	<tr height="40px;">
        		<td colspan="9" style="text-align:left;background-color:#eeeeee;">
        			<span style="margin-left:5px;">其他</span>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">【在此输入标题...】不进行保存</span>
        		</td>
            </tr>
            <s:iterator value="#request.otherQuestionList" id="otherQuestionList" status="row">
	        	<tr>
	        		<td colspan="7" style="width:75%;text-align:left;">
	        			<input type="text" style="width:95%;margin-left:5px;" id="questionOtherTitle_list" name="questionOtherTitle_list" value="<s:property value='question_title' />" onfocus="if (value =='在此输入标题...'){value =''}" onblur="if (value ==''){value='在此输入标题...'}" />
	        		</td>
	        		<td style="width:15%;text-align:left;">
	        			<select id="questionOtherSelect_list" name="questionOtherSelect_list">
	                         <option value="选填" <s:if test="%{#otherQuestionList.question_select=='选填'}">selected</s:if> >选填</option>
	                         <option value="必填" <s:if test="%{#otherQuestionList.question_select=='必填'}">selected</s:if> >必填</option>
	                    </select>
	        		</td>
	        		<td style="width:10%;text-align:right;"><input type="button" value="删除" onclick="delOther(this)"/></td>
	            </tr>
	        </s:iterator>
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

