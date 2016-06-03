<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title></title>

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<!-- 行内文字过长自动省略显示 -->
<style type="text/css">
	#downloadList{table-layout:fixed;}
	#downloadList td {
		/* word-wrap: break-word; 
		word-break: break-all; */
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
	.area_title {
		font-size:18px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		font-weight:bold;
		color: #828584
	}
	.tabletitle {
		font-size:15px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		font-weight:bold;
		color: #6E6E6E
	}
	.divtitle {
		font-size:12px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		font-weight:bold;
		margin-left: 10px;
	}
</style>

<script type="text/javascript">
var estimateDetail =<%=(String)request.getAttribute("estimateDetail")%>
//数据初始化
$(function () {
	fillFormlist()
});



function fillFormlist(){
	$("#formlist").html("");
	if(estimateDetail){
		var resultArr = estimateDetail.result;
		for(var i=0;i<resultArr.length;i++){
			//填充人日统计表数据
			var user  = resultArr[i];
			html ='<tr><td align="center">'+(i+1)+'</td>';
			html +='<td align="center">'+user.noLogDate+'</td>';
			html +='<td align="left">'+user.userName+'('+user.userId+')'+'</td></tr>';
		$("#formlist").append(html);
		}
	}
}

function update(){
	var params = {};
	if($("#personDayEdit").val()==""){
		alert("修改人日不能为空！");
	}
	else if($("#note").val()==""){
		alert("备注不能为空！");
	}
	else{
	params.id=$("#id").val();
	params.personDayEdit=$("#personDayEdit").val();
	params.note=$("#note").val();
	if (!confirm('您确定人日修改吗？')) {
		return;
	}
	var actionUrl = "/pages/personDay/listData!update.action?";
	actionUrl += "id="+params.id;
    actionUrl += "&personDayEdit="+params.personDayEdit;
    actionUrl += "&note="+encodeURI(params.note);
    var features="500,400,tmlInfo.updateTitle,tmlInfo";
	var resultvalue = OpenModal(actionUrl,features);
	}
}
</script>


</head>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="reportInfoForm" namespace=""
		 action="">
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td width="8%" align="center" class="input_label">
							<div style="float: left;" class="area_title"><s:property value="personDay.projectName" /><s:property value="personDay.year" />年<s:property value="personDay.month" />月人日详情</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
			<!-- 刷新按钮 <td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
				-->
			</tr>
		</table>
		<table>
		  <tr>
		  <td width = 60%; valign="top">
		  <table width="100%" border="0" cellpadding="5" cellspacing="5"  class="tabletitle"
					bgcolor="">
					<!-- 修改返回参数 -->
					<input type="hidden" name="personDay.id" id ="id" value="<s:property value="personDay.id" />"/>
					<!--  <input type="hidden" name="personDay.year" value="<s:property value="personDay.year" />"/>
					<input type="hidden" name="personDay.month" value="<s:property value="personDay.month" />"/>
					<input type="hidden" name="personDay.projectId" value="<s:property value="personDay.projectId" />"/>
					<input type="hidden" name="personDay.projectName" value="<s:property value="personDay.projectName" />"/>
					<input type="hidden" name="personDay.personDayConfirm" value="<s:property value="personDay.personDayConfirm" />"/>
					<input type="hidden" name="personDay.error" value="<s:property value="personDay.error" />"/>
					<input type="hidden" name="personDay.estimateDetail" value="<s:property value="personDay.estimateDetail" />"/>
					-->
					<tr>
						<td style="width:80px;">
						确认人日：
						</td>
						<td style = "color: #252A2A;">
						<s:if test="personDay.personDayConfirm==null">
					    0
					    </s:if>
					    <s:else>
					    <s:property value="personDay.personDayConfirm" />
					    </s:else>
						</td>
					</tr>
					<tr>
						<td>
						误差人日：
						</td>
						<td style = "color: #252A2A;">
					    <s:if test="personDay.error==null">
					    0
					    </s:if>
					    <s:else>
					    <s:property value="personDay.error" />
					    </s:else>
						</td>
					</tr>
					<tr>
						<td>
						总体核算：
						</td>
						<td style = "color: #252A2A;">
						<s:property value="#request.sum" />
						</td>
					</tr>
					<tr>
						<td>
						人日修改为：
						</td>
						<td>
						<input type="text" style = "color: #252A2A;width: 65px;" name="personDay.personDayEdit" 
						onkeyup="this.value=this.value.replace(/[^0-9]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9]+/,'')" 
										id="personDayEdit"  value="<s:property value="personDay.personDayEdit" />"/>
						&nbsp;&nbsp;<input type="button" name="ok" id="ok"
					value="修改" class="MyButton"
					onClick="update();" image="../../images/share/yes1.gif"> 
						</td>
					</tr>
					<tr>
						<td>
						备注：
						</td>
						<td style = "color: #252A2A;">
						<textarea cols="76" rows="6" name="personDay.note" type="text"
										id="note" style="width: 331.6px;"><s:property value="personDay.note" /></textarea>
						</td>
					</tr>
				</table>
				<s:if test="personDay.isEdit!='false'">
				<div class="divtitle">更新人：<s:property value="personDay.updateUsername" />&nbsp;&nbsp; 更新时间：<s:date name="personDay.updateTime" format="yyyy-MM-dd HH:mm:ss"/></div>
		  		</s:if>
		  </td>
		    <td width=100% style="float:left;"valign="top" > 
		    <div style="overflow-y:scroll;overflow-x: hidden;height:410px;">
				<table width="100%" border="0" cellpadding="1" cellspacing="1" style="float:left;"
					bgcolor="" id=downloadList>
					<tr >
					<td nowrap width="20%" class="oracolumncenterheader"><div
								align="center">编号</div></td>
						<!--  <td nowrap class="oracolumncenterheader" width="2%"></td> 
						-->
						<td nowrap width="40%" class="oracolumncenterheader"><div
								align="center">缺交日期</div></td>
						<td nowrap width="40%" class="oracolumncenterheader"><div
								align="center">人员姓名</div></td>
						</tr>
					<tbody name="formlist" id="formlist" align="center">
					<tr>
							<td align="center">
								<div align="center">
								</div>
							</td>
							<td align="center"></td>
							<td align="center"></td>
					 </tr>
					</tbody>
				</table>
				</div>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>

