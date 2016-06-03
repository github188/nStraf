<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.grgbanking.feeltm.dayLog.domain.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
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
		
		function checkVote(ele){
			cacelVotePrompt(false);
			if($(ele).html().length>500){
				alert("评论内容不能超过500个字!");
				ele.focus();
			}
		}
		
		function save() {
			//去除提示信息
			cacelVotePrompt(true);
			
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

			//计算总工时
			calculateSumShow();
			
		});
		
	
		//计算总工时
		function calculateSumShow(){
			var sum=0;
			$("div[name='subtotal']").find("input").each(function(){
				sum+=$(this).val()*1;
			});
			$("#sumShow").html(sum);
		}
		
		//除去提示信息(false:添加提示信息  true：除去提示信息)
		function cacelVotePrompt(cancel){
			var votePrompt='请在此输入您的点评信息';
			if(!cancel && $("#myVote").val().trim()==''){
				$("#myVote").val(votePrompt);
			}else if(cancel && $("#myVote").val().trim()==votePrompt){
				$("#myVote").val('');
			}
		}
	
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/daylog/logInfo!saveVote.action" method="post">
<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1" bgcolor="#FFFFFF">
<tr>
<td bgcolor="#FFFFFF">
        <table width="100%" class="input_table">
        	<tr>
				<td class="input_tablehead"><a style="font-weight:bold;">工作日志</a></td>
        	</tr>
        	<tr>
        		<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">姓名：<s:property value='#request.userNameStr'/></div></b></td> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
        	 	<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">部门：<s:property value='#request.deptNameStr'/></div></b></td> 
        	 	<!-- 
        	 	<td align="left" width="3%" nowrap="nowrap"></td> 
        	 	<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">级别：<s:property value='#request.userLevel'/></div></b></td> 
            	 -->
        	 	<td align="left" width="3%" nowrap="nowrap"></td> 
            	<td align="right" nowrap="nowrap"></td>  
            	<td align="right" width="10%" nowrap="nowrap"><b><div align="right'" style="padding-top:4">日期：</div></td>  
                <td align="left" width="20%"><div align="left">
                	<s:property value='#request.logDate'/>
                </div></b></td>
            	<td align="right" width="10%" nowrap="nowrap">
	            	<div align="right">工时总计： <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
            <s:if test="#request.showAudit">
            <tr>
				<td colspan="8" style="text-align: left" colspan="8" style="text-align: left"><b>审核状态:<s:if test="'#request.auditResult'=='审核通过'"><font style="color:blue"><s:property value='#request.auditResult'/></font></s:if><s:else><font style="color:red"><s:property value='#request.auditResult'/></font></s:else></b></td>
            </tr>
            <s:if test="#request.showAuditContent">
			<tr>
            	<td colspan="8" style="text-align: left">
            		<b><s:property value='#request.auditMan'/>(<s:property value='#request.auditTime'/>)</b>:<s:property value='#request.auditLog'/>
            	</td>
            </tr>        
            </s:if>    
            </s:if>
        </table>
        <s:iterator value="daylogList" id="daylog" status="row">
		<table name='taskTable' width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
        	<tr>
				<td bgcolor="#FFFFFF" align="center"><s:property value='#row.index+1'/></td>
                <td bgcolor="#FFFFFF" width="97%">
        		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
   		    		<tr >
                          <td colspan="2" class="column_label" width="30%"><div ><font color="#000000">项目名称</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">类别</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">状态</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">完成%</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">计划/新增</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">工时</font></div></td>
                    	  <td class="column_label" width="10%"><div align="center"><font color="#000000">确认工时</font>	</div></td>
                    </tr>
                    <tr>
                       	<td colspan="2" nowrap bgcolor="#FFFFFF">
                       		<div align="left">
                       			<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="43" maxlength="43" value="<s:property value='prjName'/>">
                       		</div>
                       	</td>
				        <td nowrap bgcolor="#FFFFFF">
				        	<div align="left">
				        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="7" maxlength="10" value="<s:property value='type'/>">
				        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="7" maxlength="10" value="<s:property value='statu'/>">
                            </div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"   rows="6" size="7" maxlength="10" value="<s:property value='finishRate'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"   rows="6" size="7" maxlength="10" value="<s:property value='planOrAdd'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF" align="center">
                        	<div align="left" > 
                        		<input type="text" style="border-color:transparent;" class="input_readonly"  readonly="readonly"  rows="6" size="7" maxlength="10" value="<s:property value='subTotal'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF" align="center">
                        	<div align="left" name="subtotal"> 
                        		<input type="text" style="border-color:transparent;" class="input_readonly"  readonly="readonly"  rows="6" size="7" maxlength="10" value="<s:property value='confirmHour'/>">
                        	</div>
                        </td>
                  </tr>
                  <tr>
                      <td class="input_label2"><div ><font color="#000000">计划任务：</font></div></td>
                      <td colspan="7" nowrap bgcolor="#FFFFFF" style="width:100%" >
                      	<div align="left" style="width:100%;">
                      		<textarea style="border-color:transparent;text-align: left;width:100%;"  readonly="readonly" type="text"  rows="6"  style="width:99%;" ><s:property value='desc'/></textarea>
                      	</div>
                      </td>
                  </tr>
                  <tr>
                  	<td class="input_label2"><div  style="width:80px;"><font color="#000000">任务描述：</font></div></td>
                      <td colspan="7" nowrap bgcolor="#FFFFFF" >
                      		<div align="left" style="width:100%;">
                      			<textarea type="text" style="border-color:transparent;width:100%;"  readonly="readonly"  rows="6" style="width:99%;" ><s:property value='reason'/></textarea>
                      		</div>
                      </td>
                  </tr>
                  <tr>
                  	<td class="input_label2"><div  style="width:80px;"><font color="#000000">延迟原因：</font></div></td>
                      <td colspan="7" nowrap bgcolor="#FFFFFF"  >
                      		<div align="left" style="width:100%;">
                      			<textarea type="text" style="border-color:transparent;width:100%;"  readonly="readonly"  rows="7"  ><s:property value='delay_reason'/></textarea>
                      		</div>
                      </td>
                  </tr>
                  <tr>
                  	 <td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">确认工时说明</font></div></td>
                     	<td colspan="7" nowrap bgcolor="#FFFFFF" >
		                     <div align="left" style="width:100%;">
					             <textarea style="background-color: #E3E3E3;width:100%;" readonly="readonly" name="daylogList_confirmReason" type="text"  rows="6" style="width:99%"><s:property value='confirmDesc'/></textarea>
		                     </div>
                       </td>
	             </tr>
              </table>
              </td>
              </tr>
			</table>
		</s:iterator>
	<br>
</td> 
</tr>
</table>
<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">点评内容</a></legend>
		<br>
		<!-- 用div实现竖向滚动条 -->
		<div style="width:100%;overflow-y:auto;" id="voteListContentDiv">
		<s:iterator value="#request.voteHistoryList" id="vote" status="row">
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
        	<tr>
				<!-- <td bgcolor="#FFFFFF" align="center"><s:property value='#request.voteHistoryList.size() - #row.index'/></td> -->
				<td bgcolor="#FFFFFF" align="center"><s:property value='#row.index+1'/></td>
				<td bgcolor="#FFFFFF" width="97%">
					<!-- 点评信息列表 -->
					<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
						<tr >
			                <td bgcolor="#FFFFFF" colspan="2">
			                	<table width="100%" align="center">
						        	<tr>
						        		<td align="left" width="12%" nowrap="nowrap"><div align="left" style="padding-top:4">点评人：<s:property value='voter'/></div></td> 
						            	<td align="left" width="12%" nowrap="nowrap"></td> 
						        	 	<td align="left" width="12%" nowrap="nowrap"><div align="left" style="padding-top:4">所属部门：<s:property value='voterDept'/></div></td> 
						        	 	<td align="left" width="12%" nowrap="nowrap"></td> 
						            	<td align="right" width="12%" nowrap="nowrap"><div align="left'" style="padding-top:4">点评日期：</div></td>  
						                <td align="left" width="20%">
					                	<div align="left">
					                		<s:date name='voteTime'/>
						                </div>
						                </td>
						                <td width="12%"></td>
					                </tr>
					            </table>
							</td>
			            </tr>
			            <tr>
			            	<td nowrap bgcolor="#FFFFFF" width="12%"><div align="center" ><font color="#000000">内容</font></div></td>
			            	<td nowrap bgcolor="#FFFFFF" >
				               	<div align="left">
				               		<textarea style="border-color:transparent;" readonly="readonly" type="text"  rows="2" cols="80" ><s:property value='content'/></textarea>
				               	</div>
			           		</td>
			           </tr>
					</table>
				</td>
			</tr>
		</table>
		</s:iterator>
		</div>
	</fieldset>
<br/>
<table width="100%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
	<td align="center"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

