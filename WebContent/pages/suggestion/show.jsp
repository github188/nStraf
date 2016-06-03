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
                      
			if(document.getElementById("raise_date").value.trim()=="")
			{
				alert("请输入提出日期");
				return;
			}
			if(document.getElementById("raise_man").value.trim()=="")
			{
				alert("请输入提交者");
				return;
			}
			if(document.getElementById("summary").value.trim()=="")
			{
				alert("请输入概要");
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
			action="<%=request.getContextPath()%>/pages/suggestion/suggestioninfo!update.action"
			method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="90%">
							<legend>
								问题/建议[编号：<s:property value='suggestion.pno'/>]
							</legend>
						  <table width="89%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			      <br />
								
                                
								<tr>
									<td align="center" width="13%" bgcolor="#999999">
		  <div align="center">
									  提出日期</div>									</td>
									<td bgcolor="#FFFFFF" width="27%">
								  <input name="suggestion.raise_date" type="text"
											id="raise_date" value='<s:date name="suggestion.raise_date" format="yyyy-MM-dd"/>' readonly>									</td>
							  <td width="9%" align="center" bgcolor="#999999">
<div align="center">
								  提出者</div>									</td>
								  <td  bgcolor="#FFFFFF" width="26%">
								  <input name="suggestion.raise_man" type="text" id="raise_man"
											maxlength="20" size="20" value='<s:property value="suggestion.raise_man"/>' readonly>									</td>
								  <td  bgcolor="#999999"  width="11%"><div align="center"> 状态</div></td>
								  <td width="14%" bgcolor="#FFFFFF"><input type="text" value="<s:property value='suggestion.status'/>" size="12" maxlength="12"readonly/></td>
                                  </tr>
								
								<tr>
									<td align="center" bgcolor="#999999">
										<div align="center">
											概要</div>									</td>
									<td colspan="3" bgcolor="#FFFFFF">
										<input name="suggestion.summary" type="text" id="summary"
											maxlength="62" size="62" value='<s:property value="suggestion.summary"/>' readonly>									</td>
								    <td bgcolor="#999999"><div align="center"> 类别</div></td>
								    <td bgcolor="#FFFFFF"><input type="text" value="<s:property value='suggestion.category'/>" size="12" maxlength="12"readonly/>
                                   
								                                    </td>
                                                                        
							    </tr>
							
								<tr>
									<td align="center" bgcolor="#999999">
										<div align="center">
											描述</div>									</td>
									<td bgcolor="#FFFFFF" colspan="5">
										<div >
											<textarea cols="74" rows="3" name="suggestion.description"
												id="description"   readonly><s:property value="suggestion.description"/></textarea>
										</div>									</td>
								</tr>
								
								<tr>
									<td align="center" bgcolor="#999999">
										建议措施</td>
									<td bgcolor="#FFFFFF" colspan="5">
										<textarea cols="74" rows="3" name="suggestion.giving_solution" 
											id="solution" readonly><s:property value="suggestion.giving_solution" /></textarea>									</td>
								</tr>
							<tr>
								<td width="13%" align="center" bgcolor="#999999">
								
									评估意见							</td>
						      <td  bgcolor="#FFFFFF" colspan="5">
							<textarea cols="74" rows="3" name="suggestion.mamager_sugggestion"
								id="mamager_sugggestion"  readonly><s:property value="suggestion.mamager_sugggestion"/></textarea>							</td>
							</tr>

                                
								<tr>
                                    <td width="13%" align="center" bgcolor="#999999">
                                  <div align="center">
                                  计划完成日期                                        </div>                           	        </td>
							<td width="27%" bgcolor="#FFFFFF">
							  <input name="suggestion.finishing_date" type="text"
									id="finishing_date"  value='<s:date name="suggestion.finishing_date" format="yyyy-MM-dd"/>' readonly>							</td>
							  <td align="center" width="9%" bgcolor="#999999">
<div align="center" >
								  处理者</div>									</td>
							  <td width="26%" bgcolor="#FFFFFF">
								
											<input name="suggestion.resolve_man" type="text"
												id="resolve_man" maxlength="20" size="20" value='<s:property value="suggestion.resolve_man"/>' readonly>
														</td>
									
								  <td width="11%" bgcolor="#999999"><div align="center"> 价值分</div></td>
								  <td bgcolor="#FFFFFF">
                                      
                                              
                                          
                                               <input type="text" value="<s:property value='suggestion.price_score'/>" size="12" maxlength="12"readonly/></td>
</tr>
					
								<tr>
									<td align="center" bgcolor="#999999">
										解决措施									</td>
									<td bgcolor="#FFFFFF" colspan="5">
										<textarea cols="74" rows="3" name="suggestion.solution"
											id="solution"  readonly><s:property value="suggestion.solution"/></textarea>									</td>
								</tr>
								
								<tr>
									<td width="13%" align="center" bgcolor="#999999">
						  <div align="center">
											实际完成日期										
                                      </div>									
                                  </td>
				   	  <td bgcolor="#FFFFFF"  colspan="3">
								  		<input name="suggestion.pratical_date" type="text"
											id="pratical_date" 
											value='<s:date name="suggestion.pratical_date" format="yyyy-MM-dd"/>' readonly >									
                                  </td>
									
							  <td align="center" bgcolor="#999999">
  									<div align="center">
											效果分
                                    </div>									
                              </td>
							  <td  bgcolor="#FFFFFF"   >
                                     
                                      <input type="text" value="<s:property value='suggestion.effect_score'/>" size="12" maxlength="12"readonly/>						
                               </td>
					 </tr>
								
							
								<tr>
									<td width="13%" height="67" align="center" bgcolor="#999999">
	    <div align="center">
								  效果评价</div>									</td>
<td bgcolor="#FFFFFF" colspan="5" >
										<textarea name="suggestion.public_evaluation" cols="74" rows="5" id="public_evaluation"  readonly><s:property value="suggestion.public_evaluation" /></textarea>									</td>
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
