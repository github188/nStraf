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
				alert("请输入提出者");
				return;
			}
			if(document.getElementById("summary").value.trim()=="")
			{
				alert("请输入概要");
				return;
			}
			if(document.getElementById("description").value.trim()=="")
			{
				alert("请输入描述");
				return;
			}
			if(document.getElementById("solution").value.trim()=="")
			{
				alert("请输入建议措施");
				return;
			}
			if(document.getElementById("category").value.trim()=="")
			{
				alert("请选择类别");
				return;
			}
			document.getElementById("ok").disabled=true;
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
            <input type="hidden" name="suggestion.id" value='<s:property value="suggestion.id"/>'>
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="90%">
							<legend>
								问题/建议
							</legend>
						  <table width="89%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			      <br />
								
                                
								<tr>
									<td align="center" width="13%" bgcolor="#999999">
		  <div align="center">
											提出日期
								  <font color="#FF0000">*</font>										</div>									</td>
								  <td bgcolor="#FFFFFF" width="27%"><input name="suggestion.raise_date" type="text" class="bgg"
											id="raise_date" 
											value='<s:date name="suggestion.raise_date" format="yyyy-MM-dd"/>' readonly /></td>
							  <td width="9%" align="center" bgcolor="#999999">
<div align="center">
											提出者
								  <font color="#FF0000">*</font>										</div>									</td>
								  <td  bgcolor="#FFFFFF" width="26%">
								  <input name="suggestion.raise_man" type="text" class="bgg" id="raise_man" value='<s:property value="suggestion.raise_man"/>' size="20"
											maxlength="20" readonly>									</td>
								  <td  bgcolor="#999999"  width="11%"><div align="center"> 状态 <font color="#FF0000">*</font> </div></td>
								  <td width="14%" bgcolor="#FFFFFF"><select name="suggestion.status" id="status" align="left" style="width:77px"
											>
                                    <option value='新建' style="width:105px"> 新建 </option>
                                    <option value='打开'> 打开 </option>
                                    <option value='已解决'> 已解决 </option>
                                    <option value="不解决">不解决</option>
                                  </select></td>
                                  <input type='hidden' id="st" name="st" value="<s:property value='suggestion.status'/>"/>
                                  <s:if test="suggestion.status!=null&&!suggestion.status.equals('')">
									<script language="javascript">
												var status = document.getElementById("st").value;
						            		    status = decodeURI(status);
												document.getElementsByName("status")[0].value = status;
						            		</script>
								  </s:if>
</tr>
								
								<tr>
									<td align="center" bgcolor="#999999">
										<div align="center">
											概要
											<font color="#FF0000">*</font>										</div>									</td>
									<td colspan="3" bgcolor="#FFFFFF">
										<input name="suggestion.summary" type="text" id="summary"
											maxlength="62" size="62" value='<s:property value="suggestion.summary"/>'>									</td>
								    <td bgcolor="#999999"><div align="center"> 类别 <font color="#FF0000">*</font> </div></td>
								    <td bgcolor="#FFFFFF"><select name="suggestion.category" id="category"
											align="left" style="width:77px">
                                      <option value='' selected="true"></option>
                                      <option value='制度'> 制度 </option>
                                      <option value='标准'> 标准 </option>
                                      <option value='流程'> 流程 </option>
                                      <option value='报告'> 报告 </option>
                                      <option value='方案'> 方案 </option>
                                      <option value='文档'> 文档 </option>
                                      <option value='用例'> 用例 </option>
                                      <option value='环境'> 环境 </option>
                                      <option value='工具'> 工具 </option>
                                      <option value='QC'> QC </option>
                                      <option value='平台'> 平台 </option>
                                      <option value='技术'> 技术 </option>
                                      <option value='管理'> 管理 </option>
                                      <option value='培训'> 培训 </option>
                                      <option value='其他'> 其他 </option>
                                                                        </select>
                                    <input type='hidden' id="st1" name="st1" value="<s:property value='suggestion.category'/>"/>
                                  <s:if test="suggestion.category!=null&&!suggestion.category.equals('')">
											<script language="javascript">
												var category = document.getElementById("st1").value;
						            		    category = decodeURI(category);
												document.getElementsByName("category")[0].value = category;
						            		</script>
                                    </s:if>								                                    </td>
							    </tr>
							
								<tr>
									<td align="center" bgcolor="#999999">
										<div align="center">
											描述
											<font color="#FF0000">*</font>										</div>									</td>
									<td bgcolor="#FFFFFF" colspan="5">
										<div >
											<textarea cols="74" rows="3" name="suggestion.description"
												id="description"  ><s:property value="suggestion.description"/></textarea>
										</div>									</td>
								</tr>
								
								<tr>
									<td align="center" bgcolor="#999999">
										建议措施
										<font color="#FF0000">*</font> </td>
									<td bgcolor="#FFFFFF" colspan="5">
										<textarea cols="74" rows="3" name="suggestion.giving_solution"
											id="solution"><s:property value="suggestion.giving_solution"/></textarea>									</td>
								</tr>
							<tr>
								<td width="13%" align="center" bgcolor="#999999">
								
									评估意见							</td>
						      <td  bgcolor="#FFFFFF" colspan="5">
							<textarea cols="74" rows="3" name="suggestion.mamager_sugggestion"
								id="mamager_sugggestion"  ><s:property value="suggestion.mamager_sugggestion"/></textarea>							</td>
							</tr>

                                
								<tr>
                                    <td width="13%" align="center" bgcolor="#999999">
                                  <div align="center">
                                  计划完成日期                                        </div>                           	        </td>
							<td width="27%" bgcolor="#FFFFFF">
							  <input name="suggestion.finishing_date" type="text"
									id="finishing_date" class="MyInput" isSel="true" isDate="true"
									onFocus="ShowDate(this)" dofun="ShowDate(this)"
									value='<s:date name="suggestion.finishing_date" format="yyyy-MM-dd"/>' >							</td>
							  <td align="center" width="9%" bgcolor="#999999">
<div align="center" >
								  处理者</div>									</td>
							  <td width="26%" bgcolor="#FFFFFF">
								<s:if test="flag.equals('0')"></s:if>
								<s:else>
											<s:select name="suggestion.resolve_man" id="resolve_man" theme="simple" list="map" cssStyle="width:100px;" value="suggestion.resolve_man" emptyOption="true"></s:select>
								  </s:else>									</td>
									
								  <td width="11%" bgcolor="#999999"><div align="center"> 价值分</div></td>
								  <td bgcolor="#FFFFFF"><select name="suggestion.price_score" id="price_score"
											align="left" style="width:78px" >
                                    <option value='' selected="true"></option>
                                    <option value='5'> 5 </option>
                                    <option value='4'> 4 </option>
                                    <option value='3'> 3 </option>
                                    <option value='2'> 2 </option>
                                    <option value='1'> 1 </option>
                                    <option value='0'> 0 </option>
                                  </select>
                                      <s:if test="suggestion.price_score!=null&&!suggestion.price_score.equals('')">
												<script language="javascript">
                                                $("#price_score").val('<s:property value='suggestion.price_score'/>');
                                                </script>
											</s:if>                                  </td>
</tr>
					
								<tr>
									<td align="center" bgcolor="#999999">
										解决措施									</td>
									<td bgcolor="#FFFFFF" colspan="5">
										<textarea cols="74" rows="3" name="suggestion.solution"
											id="solution" style="overflow-y:scroll" wrap="hard"><s:property value="suggestion.solution"/></textarea>									</td>
								</tr>
								
								<tr>
									<td width="13%" align="center" bgcolor="#999999">
						  <div align="center">
											实际完成日期                                      </div>                                  </td>
				   	  <td bgcolor="#FFFFFF"  colspan="3">
								  		<input name="suggestion.pratical_date" type="text"
											id="pratical_date" class="MyInput" isSel="true" isDate="true"
											onFocus="ShowDate(this)" dofun="ShowDate(this)"
											value='<s:date name="suggestion.pratical_date" format="yyyy-MM-dd"/>' >                                  </td>
									
							  <td align="center" bgcolor="#999999">
  									<div align="center">
											效果分                                    </div>                              </td>
							  <td  bgcolor="#FFFFFF"   >
                                    <select name="suggestion.effect_score" id="effect_score"
                                    align="left"  style="width:78px" > 
                                    <option value='' selected="true"></option>
                                    <option value='5'>
                                        5											</option>
                                    <option value='4'>
                                        4											</option>
                                    <option value='3'>
                                        3											</option>
                                    <option value='2'>
                                        2											</option>
                                    <option value='1'>
                                        1											</option>
                        			</select>			
                                      <s:if test="suggestion.effect_score!=null&&!suggestion.effect_score.equals('')">
												<script language="javascript">
                                                $("#effect_score").val('<s:property value='suggestion.effect_score'/>');
                                                </script>
									</s:if>                               </td>
					 </tr>
								
							
								<tr>
									<td width="13%" height="67" align="center" bgcolor="#999999">
	    <div align="center">
								  效果评价</div>									</td>
<td bgcolor="#FFFFFF" colspan="5" >
										<textarea name="suggestion.public_evaluation" cols="74" rows="5" id="public_evaluation"  ><s:property value="suggestion.public_evaluation"/></textarea>									</td>
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
					<td align="center">
						<input type="button" name="ok" id="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onClick="save()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
	</form>
	</body>
</html>
