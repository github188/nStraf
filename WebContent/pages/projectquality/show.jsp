<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
	<head>
		<title>certification query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
                      
			if(document.getElementById("create_date").value.trim()=="")
			{
				alert("请输入提交日期");
				return;
			}
			if(document.getElementById("create_man").value.trim()=="")
			{
				alert("请输入提交者");
				return;
			}
			if(document.getElementById("summary").value.trim()=="")
			{
				alert("请输入案例名称");
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
    function selDate(id)
    {
    	var obj=document.getElementById(id);
    	ShowDate(obj);
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
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/projectquality/projectqualityinfo!update.action"
			method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="90%">
							<legend>
								项目质量[编号：<s:property value='projectQuality.prjQno'/>]
							</legend>
						  <table width="89%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			      <br />
								
                                
								<tr>
									<td align="center" width="9%" bgcolor="#999999">
		  <div align="center">
									  项目名称</div>									</td>
									<td bgcolor="#FFFFFF" width="10%">
                                    	  <input name="projectQuality.prjName" type="text" id="prjName"
											 size="15" value='<s:property value="projectQuality.prjName"/>' readonly>	
																</td>
							  <td width="9%" align="center" bgcolor="#999999">
<div align="center">
								  开始日期</div>									</td>
								  <td  bgcolor="#FFFFFF" width="7%">
								    <input name="projectQuality.prjStart" type="text"
										size="11"	id="prjStart" value='<s:date name="projectQuality.prjStart" format="yyyy-MM-dd"/>' readonly>	</td>
								  <td  bgcolor="#999999"  width="9%"><div align="center">结束日期</div></td>
								  <td width="7%" bgcolor="#FFFFFF">   <input name="projectQuality.prjEnd" type="text"
										size="11"	id="prjEnd" value='<s:date name="projectQuality.prjEnd" format="yyyy-MM-dd"/>' readonly>	</td>
                                            <td align="center"  bgcolor="#999999" width="9%"><div align="center">质量得分</div></td>
									<td width="26%" colspan="3"  bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.qualityMark" type="text" id="qualityMark"
											 size="11" value='<s:property value="projectQuality.qualityMark"/>' readonly>	
									</td>
                                  </tr>
                                  
                                  <tr>
									<td align="center"  bgcolor="#999999"><div align="center">缺陷总数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugSum" type="text" id="bugSum"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugSum"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">缺陷总值</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugZongzhi" type="text" id="bugZongzhi"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugZongzhi"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">代码行数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.codeNum" type="text" id="codeNum"
											maxlength="8" size="11" value='<s:property value="projectQuality.codeNum"/>' readonly>	
									</td>
							     	<td align="center"  bgcolor="#999999"><div align="center">缺陷密度</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugDensity" type="text" id="bugDensity"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugDensity"/>' readonly>	
									</td>
                                
									<td align="center"  bgcolor="#999999"><div align="center">缺陷密度得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugDensityMark" type="text" id="bugDensityMark"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugDensityMark"/>' readonly>	
									</td>
                                      </tr>
                                  
                                                                    <tr>
                                    	<td align="center"  bgcolor="#999999"><div align="center">关闭缺陷数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugClosednum" type="text" id="bugClosednum"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugClosednum"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">缺陷解决率</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugResolverate" type="text" id="bugResolverate"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugResolverate"/>' readonly>	
									</td>
							     	<td align="center"  bgcolor="#999999"><div align="center">缺陷解决率得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugResolvemark" type="text" id="bugResolvemark"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugResolvemark"/>' readonly>	
									</td>
                                 
									<td align="center"  bgcolor="#999999"><div align="center">重新打开缺陷数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugReopennum" type="text" id="bugReopennum"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugReopennum"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">缺陷Reopen率</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugReopenrate" type="text" id="bugReopenrate"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugReopenrate"/>' readonly>	
									</td>
                                     </tr>
								                                         <tr>
                                    	<td align="center"  bgcolor="#999999"><div align="center">缺陷Reopen率得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.bugReopenmark" type="text" id="bugReopenmark"
											maxlength="8" size="11" value='<s:property value="projectQuality.bugReopenmark"/>' readonly>	
									</td>
							     	<td align="center"  bgcolor="#999999"><div align="center">文档质量得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.documentQualitymark" type="text" id="documentQualitymark"
											maxlength="8" size="11" value='<s:property value="projectQuality.documentQualitymark"/>' readonly>	
									</td>
                                 
									<td align="center"  bgcolor="#999999"><div align="center">基础质量得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.baseQualitymark" type="text" id="baseQualitymark"
											maxlength="8" size="11" value='<s:property value="projectQuality.baseQualitymark"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">中止测试数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.teststopNum" type="text" id="teststopNum"
											maxlength="8" size="11" value='<s:property value="projectQuality.teststopNum"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">中止测试得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.teststopMark" type="text" id="teststopMark"
											maxlength="8" size="11" value='<s:property value="projectQuality.teststopMark"/>' readonly>	
									</td>
                                     </tr>
								                                        <tr>
							     	<td align="center"  bgcolor="#999999"><div align="center">版本提交频次</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.testupNum" type="text" id="testupNum"
											maxlength="8" size="11" value='<s:property value="projectQuality.testupNum"/>' readonly>	
									</td>
                                
									<td align="center"  bgcolor="#999999"><div align="center">版本提交得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.testupMark" type="text" id="testupMark"
											maxlength="8" size="11" value='<s:property value="projectQuality.testupMark"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">工程投诉数</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.projectcomplainNum" type="text" id="projectcomplainNum"
											maxlength="8" size="11" value='<s:property value="projectQuality.projectcomplainNum"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">工程投诉扣分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.projectcomplainMark" type="text" id="projectcomplainMark"
											maxlength="8" size="11" value='<s:property value="projectQuality.projectcomplainMark"/>' readonly>	
									</td>
							     	<td align="center"  bgcolor="#999999"><div align="center">代码质量</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.codeQuality" type="text" id="codeQuality"
											maxlength="8" size="11" value='<s:property value="projectQuality.codeQuality"/>' readonly>	
									</td>
                                  </tr>
                                  
                                  						                                        <tr>
									<td align="center"  bgcolor="#999999"><div align="center">代码质量得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.codeQualitymark" type="text" id="codeQualitymark"
											maxlength="8" size="11" value='<s:property value="projectQuality.codeQualitymark"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">性能质量</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.performanceQuality" type="text" id="performanceQuality"
											maxlength="8" size="11" value='<s:property value="projectQuality.performanceQuality"/>' readonly>	
									</td>
                                    	<td align="center"  bgcolor="#999999"><div align="center">性能质量得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.performanceQualitymark" type="text" id="performanceQualitymark"
											maxlength="8" size="11" value='<s:property value="projectQuality.performanceQualitymark"/>' readonly>	
									</td>
							     	<td align="center"  bgcolor="#999999"><div align="center">组件复用</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.moduleUsed" type="text" id="moduleUsed"
											maxlength="8" size="11" value='<s:property value="projectQuality.moduleUsed"/>' readonly>	
									</td>
                                    	     	<td align="center"  bgcolor="#999999"><div align="center">组件复用得分</div></td>
									<td bgcolor="#FFFFFF">
                                    	  <input name="projectQuality.moduleUsedmark" type="text" id="moduleUsedmark"
											maxlength="8" size="11" value='<s:property value="projectQuality.moduleUsedmark"/>' readonly>	
									</td>
                                  </tr>

							</table>
					  </fieldset>
					</td>
				</tr>
		
			</table>
			<br />
			<br />
			<table width="80%" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="center"><input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
	</form>
	</body>
</html>
