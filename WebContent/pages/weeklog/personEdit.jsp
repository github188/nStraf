<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@ page contentType="text/html; charset=UTF-8" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
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

		/* function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
			if(window==window.parent){
	           window.close();
			}else{
			   parent.close();  
			}
		} */
		
		function checkSumary(ele){
			if($(ele).html().length>500){
				var str = $(ele).val();
				str = str.substr(0,499);
				$(ele).val(str);
				alert("最多能输入500字!");
				$(ele).focus();
			}
			cacelSumaryPrompt(false);
		}
		
		function checkPlan(ele){
			if($(ele).html().length>500){
				var str = $(ele).val();
				str = str.substr(0,499);
				$(ele).val(str);
				alert("最多能输入500字!");
				$(ele).focus();
			}
			cacelPlanPrompt(false);
		}
		
		function save(){
			//先去掉提示信息
			cacelSumaryPrompt(true);
			cacelPlanPrompt(true);
			
			//验证总结和计划是否填写
			if($("#mySumary").val().trim()==''){
				alert("请先填写总结！");
				$("#mySumary").focus();
				return;
			}else if($("#myPlan").val().trim()==''){
				alert("请先填写计划！");
				$("#myPlan").focus();
				return;
			}
			
			if($("#mySumary").val().length>500){
				alert("总结至多填写500字！");
				$("#mySumary").focus();
				return;
			}
			if($("#myPlan").val().length>500){
				alert("计划至多填写500字！");
				$("#myPlan").focus();
				return;
			}
			
			window.returnValue=true;
			reportInfoForm.submit();
		}
	
		$(function(){
			//当所属组别字段过长时，简化显示
			$("#groupStrDiv").each(function(){
				//首先去掉最后的逗号
				if($(this).html().charAt($(this).html().length-1)==','){
					$(this).html($(this).html().substring(0,$(this).html().length-1));
				}
				var groupLabel=$(this).html().substring(0,$(this).html().indexOf(':')+1);
				$(this).html($(this).html().substring($(this).html().indexOf(':')+1,$(this).html().length));
				//如果还有逗号的话，说明是几个组，这里只展示一个组，其他组在鼠标移上去后显示
				if($(this).html().indexOf(',')>0){
					var shortShow=$(this).html().substring(0,$(this).html().indexOf(','))+'...';
					shortShow=groupLabel+'<a style="font-weight:bold;" title="'+$(this).html()+'">'+shortShow+'</a>';
					$(this).html(shortShow);
				}else{
					shortShow=groupLabel+$(this).html();
					$(this).html(shortShow);
				}
			});
			
			//设置日志列表div和评论列表div高度
			if($("#daylogListContentDiv").height()<20){//高度小于20，表示没有数据，则把高度设为10
				$("#daylogListContentDiv").height(10);
			}else{//高度大于400，强制设置高度为400，同时出现滚动条
				$("#daylogListContentDiv").height(400);
			}
			
			if($("#voteListContentDiv").height()<20){//高度小于10，表示没有数据，则把高度设为10
				$("#voteListContentDiv").height(10);
			}else if($("#voteListContentDiv").height()<150){//高度在10至150之间，根据内容多少自动调节div高度
				$("#voteListContentDiv").height($("#voteListContentDiv").height()+10);
			}else{//高度大于150，强制设置高度为150，同时出现滚动条
				$("#voteListContentDiv").height(150);
			}
			
			//页面加载后，给隐藏的周选择列表选择值控件赋值
			$("#selectedWeekParam").val($("#weekSelect").find("option:selected").val());
			
			//页面加载后，显示提示信息
			cacelSumaryPrompt(false);
			cacelPlanPrompt(false);
		});
		
		
		$(function(){
			//选择不同的周，则刷新本页面，获取选定周的数据
			$("#weekSelect").change(function(){
				var weeklogId=$("#personWeekLogId").val();
				var submitoldDate=$("input[name='oldWeekendDate']").val();
				location="<%=request.getContextPath()%>/pages/weeklog/logInfo!edit.action?selectedWeek="+$(this).val()+"&oldWeekendDate="+submitoldDate;
				});
		});
		
		function cacelSumaryPrompt(cancel){
			var votePrompt='请在此输入您的总结信息';
			if(!cancel && $("#mySumary").val().trim()==''){
				$("#mySumary").val(votePrompt);
			}else if(cancel && $("#mySumary").val().trim()==votePrompt){
				$("#mySumary").val('');
			}
		}
		
		function cacelPlanPrompt(cancel){
			var votePrompt='请在此输入您的下周计划';
			if(!cancel && $("#myPlan").val().trim()==''){
				$("#myPlan").val(votePrompt);
			}else if(cancel && $("#myPlan").val().trim()==votePrompt){
				$("#myPlan").val('');
			}
		}
	
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/weeklog/logInfo!update.action" method="post">
<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td bgcolor="#FFFFFF">
	<!-- ********************************************* 基本信息展示 ******************************************** -->
	<table width="100%" align="center">
     	<tr>
     		<td align="left" width="10%" class="input_label2"><b><div align="left" style="padding-top:4">姓名：<s:property value='#request.usr.username'/></div></td> 
         	<td align="left" width="3%"></td> 
     	 	<td align="left" width="10%" class="input_label2"><b><div align="left" style="padding-top:4">部门：<s:property value='#request.deptNameStr'/></div></td> 
     	 	<td align="left"></td> 
         	<td align="right" class="input_label2">
         		<b>动态周：</b>
         	</td>
             <td align="left" width="20%"><div align="right">
           		<select id="weekSelect" >
	              	<s:iterator  value="#request.weeks" status="row">
	            				<option value='<s:property value="key"/>' <s:if test="selected">selected</s:if>><s:property value="key"/></option>
	              	</s:iterator>
           		</select>
           		<!-- 页面加载或者更换周导致页面重新加载时，记录选定的周，用于表单提交 -->
           		<input type="hidden" name="selectedWeekParam" id="selectedWeekParam" value=''>
           		<input type="hidden" name="oldWeekendDate" id="oldWeekendDate" value='<s:property value='#request.oldWeekendDate'/>' >
             </div></td>
         </tr>
    </table>
    <br/>
    <!-- ********************************************* 工作日志展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;"><s:text name="label.newTermInfo" /></a></legend>
		<br>
		<!-- 用div实现竖向滚动条 -->
		<div class="scrollFeild-loginfo" id="daylogListContentDiv">
			<s:iterator value="dateDayLogList" id="dateDayLog" status="st">
			<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
			<tr>
				<td width="10%" bgcolor="#FFFFFF" style="text-align: center">
					<div ><s:property value='logDate'/></div> 
					
				</td>
				<td bgcolor="#FFFFFF">
					<s:iterator value="daylogList" id="daylog" status="row">
					<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
	   		    		<tr >
	                          <td class="column_label" style="width:25%"><font color="#000000">项目名称</font></td>
	                          <td class="column_label" style="width:15%"><font color="#000000">类别</font></div></td>
	                          <td class="column_label" style="width:15%"><font color="#000000">状态</font></td>
	                          <td class="column_label" style="width:15%"><font color="#000000">完成%</font></td>
	                          <td class="column_label" style="width:15%"><font color="#000000">计划/新增</font></td>
	                          <td class="column_label" style="width:15%"><font color="#000000">工时</font></td>
	                    </tr>
	                    <tr>
	                       	<td bgcolor="#FFFFFF" style="width:25%;">
	                       		<s:property value='#daylog.prjName'/>
	                       	</td>
					        <td  bgcolor="#FFFFFF">
					        	<s:property value='#daylog.type'/>
	                        </td>
	                        <td  bgcolor="#FFFFFF">
	                        	<s:property value='#daylog.statu'/>
	                        </td>
	                        <td  bgcolor="#FFFFFF">
	                        	<s:property value='#daylog.finishRate'/>
	                        </td>
	                        <td  bgcolor="#FFFFFF">
	                        	<s:property value='#daylog.planOrAdd'/>
	                        </td>
	                        <td  bgcolor="#FFFFFF" align="center">
	                        	<s:property value='#daylog.subTotal'/>
	                        </td>
	                  </tr>
	                  <tr>
	                      <td width="25%" class="input_label2"><div align="center"><font color="#000000">计划任务</font></div></td>
	                      <td colspan="5"  bgcolor="#FFFFFF" >
	                      		<div align="left">
	                      			<textarea style="border-color:transparent;"  readonly="readonly" type="text"  rows="5" cols="80" ><s:property value='desc'/></textarea>
	                      		</div>
	                      </td>
	                 </tr>
	                 <tr>
	                  <td width="25%" class="input_label2"><div align="center"><font color="#000000">任务描述</font></div></td>
	                     <td colspan="5"  bgcolor="#FFFFFF" >
	                     		<div align="left">
	                     			<textarea style="border-color:transparent;"  readonly="readonly" type="text"  rows="5" cols="80" ><s:property value='reason'/></textarea>
	                     		</div>
	                     </td>
	              </table>
			    </s:iterator>
				</td>
			</tr>
			</table>
			</s:iterator>
		</div>
	</fieldset>
	<br>
	
	<!-- ********************************************* 点评内容展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">点评内容</a></legend>
		<br>
		<!-- 用div实现竖向滚动条 -->
		<div class="scrollFeild-loginfo" id="voteListContentDiv">
			<s:iterator value="#request.dateVoteList" id="dateVote" status="st">
				<table width="100%" align="center" style="table-layout:fixed" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
				<tr>
					<td width="10%" bgcolor="#FFFFFF" style="text-align: center">
						<div ><s:property value='date'/></div> 
						
					</td>
					<td width="80%" bgcolor="#FFFFFF">
						<!-- 点评信息列表 -->
	                	<table width="100%" align="center">
	                		<s:iterator value="#dateVote.voteList" id="vote" status="row">
					        	<tr>
					        		<td align="left" style="word-break:break-all;word-wrap:break-word;">
						        		<div align="left" style="padding-top:4" >
						        			<b><s:property value='voter'/>(<s:property value='voterDept'/>) <s:date name='voteTime' format="yyyy-MM-dd"/>:</b><s:property value='content'/>
						                </div>
					                </td>
				                </tr>
			                </s:iterator>
			            </table>
					</td>
				</tr>
				</table>
			</s:iterator>
		</div>
	</fieldset>
	<br>
	
	<!-- ********************************************* 总结计划展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">总结计划</a></legend>
		<br>
		<!-- 总结计划 -->
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
			<tr>
				<td  class="input_label2" width="10%"><div align="center"><font color="red">本周总结</font></div></td>
	           	<td  bgcolor="#FFFFFF" >
	              	<div align="left">
	              		<textarea onfocus="cacelSumaryPrompt(true)" onkeyup="limitLen(500,this)"  type="text"  rows="4" cols="80" id="mySumary" name='personWeekLog.desc'><s:property value='personWeekLog.desc'/></textarea>
	              	</div>
	          	</td>
			</tr>
			<tr>
	           	<td  class="input_label2" width="10%"><div align="center"><font color="red">下周计划</font></div></td>
	           	<td  bgcolor="#FFFFFF" >
	              	<div align="left">
	              		<textarea onfocus="cacelPlanPrompt(true)" onkeyup="limitLen(500,this)"  type="text"  rows="4" cols="80" id="myPlan" name='personWeekLog.plan'><s:property value='personWeekLog.plan'/></textarea>
	              	</div>
          		</td>
			</tr>
		</table>
	</fieldset>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
 <tr>  
	<td align="center"> 
	<br/>
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

