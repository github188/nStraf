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
		 	window.close();
		} */
		
		function checkVote(ele){
			if($(ele).html().length>500){
				alert("评论内容不能超过500个字!");
				ele.focus();
			}
		}
		
		function save() {
			//去除提示信息
			cacelVotePrompt(true);
			if($("#myVote").val().trim()!=''){
				window.returnValue=true;
				reportInfoForm.submit();
			}else{
				window.close();
			}
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
			
			//点评框添加提示信息
			cacelVotePrompt(false);
			
			//评论div的高度
			if($("#voteListContentDiv").height()<20){//高度小于10，表示没有数据，则把高度设为10
				$("#voteListContentDiv").height(10);
			}else if($("#voteListContentDiv").height()<350){//高度在10至350之间，根据内容多少自动调节div高度
				$("#voteListContentDiv").height($("#voteListContentDiv").height()+10);
			}else{//高度大于350，强制设置高度为350，同时出现滚动条
				$("#voteListContentDiv").height(350);
			}
			
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
	
		function limitLen(maxlen,element){
			var message = $(element).val();
		    var byteCount=0;  
		    var strValue=message;  
		    var strLength=message.length;  
		    var maxValue=maxlen;  
		        for(var i=0;i<strLength;i++){  
		            byteCount=(strValue.charCodeAt(i)<=256)?byteCount+1:byteCount+2;  
		            if(byteCount>(maxValue*2)){  
		                message=strValue.substring(0,i);  
		                $(element).val(message);
		                alert("内容最多不能超过"+maxValue+"个字！");  
		                byteCount=maxValue;  
		                $(element).focus();
		                break;  
		            }  
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
	            	<div align="right">工时总计 <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
        </table>
        <s:iterator value="daylogList" id="daylog" status="row">
		<table name='taskTable' width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
        	<tr>
				<td bgcolor="#FFFFFF" align="center"><s:property value='#row.index+1'/></td>
                <td bgcolor="#FFFFFF" width="97%">
        		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
   		    		<tr >
                          <td colspan="2" class="column_label" width="40%"><div align="center"><font color="#000000">项目名称</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">类别</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">状态</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">完成%</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">计划/新增</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">工时</font></div></td>
                    </tr>
                    <tr>
                       	<td colspan="2" nowrap bgcolor="#FFFFFF">
                       		<div align="left">
                       			<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="43" maxlength="43" value="<s:property value='prjName'/>">
                       		</div>
                       	</td>
				        <td nowrap bgcolor="#FFFFFF">
				        	<div align="left">
				        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='type'/>">
				        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='statu'/>">
                            </div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='finishRate'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='planOrAdd'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF" align="center">
                        	<div align="left" name="subtotal"> 
                        		<input type="text" style="border-color:transparent;" class="input_readonly"  readonly="readonly"  size="10" maxlength="10" value="<s:property value='subTotal'/>">
                        	</div>
                        </td>
                  </tr>
                  <tr>
                      <td class="input_label2"><div align="center"><font color="#000000">计划任务</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" >
                      	<div align="left">
                      		<textarea style="border-color:transparent;text-align: left;"  readonly="readonly" type="text"  rows="3" cols="62" ><s:property value='desc'/></textarea>
                      	</div>
                      </td>
                  </tr>
                  <tr>
                  	<td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">任务描述</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" >
                      		<div align="left">
                      			<textarea type="text" style="border-color:transparent;"  readonly="readonly"  rows="3" cols="62" ><s:property value='reason'/></textarea>
                      		</div>
                      </td>
                  </tr>
              </table>
              </td>
              </tr>
			</table>
		</s:iterator>
	<br>
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
						<tr>
			                <td bgcolor="#FFFFFF" colspan="2">
			                	<table width="100%" align="center">
						        	<tr>
						        		<td align="left" width="10%" nowrap="nowrap"><div align="left" style="padding-top:4">点评人：<s:property value='voter'/></div></td> 
						            	<td align="left" width="7%" nowrap="nowrap"></td> 
						        	 	<td align="left" width="10%" nowrap="nowrap"><div align="left" style="padding-top:4">所属部门：<s:property value='voterDept'/></div></td> 
						        	 	<td align="left" width="7%" nowrap="nowrap"></td> 
						            	<td align="right" width="10%" nowrap="nowrap"><div align="left'" style="padding-top:4">点评日期：</div></td>  
						                <td align="left" width="20%">
					                	<div align="left">
					                		<s:date name='voteTime'/>
						                </div>
						                </td>
						                <td></td>
					                </tr>
					            </table>
							</td>
			            </tr>
			            <tr>
			            	<td nowrap bgcolor="#FFFFFF" width="12%"><div align="center"><font color="#000000">内容</font></div></td>
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
	<br>
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">我有话说</a></legend>
		<br>
		<!-- 点评 -->
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
			<tr>
	               <td bgcolor="#FFFFFF" colspan="2">
	               	<table width="100%" align="center">
			        	<tr>
			        		<input type="hidden" name='vote.voterId' value='<s:property value="#request.votePersonId"/>' />
			        		<input type="hidden" name='vote.daylogId' value='<s:property value="#request.dateDaylogId"/>' />
			        		<!-- 关联的父评论id，当该评论是回复评论列表中的某个评论时起作用，该功能暂时没有实现 -->
			        		<input type="hidden" name='vote.parentId' value='<s:property value=""/>' />
			        		<td align="left" width="10%" nowrap="nowrap"><div align="left" style="padding-top:4">点评人：<input name="vote.voter" type="text" style="border-color:transparent;"  readonly="readonly"  size="10" maxlength="10" value='<s:property value="#request.votePersonUserName"/>'></div></td> 
			            	<td align="left" width="3%" nowrap="nowrap"></td> 
			        	 	<td align="left" width="10%" nowrap="nowrap"><div align="left" style="padding-top:4">所属部门：<input name="vote.voterDept" type="text" style="border-color:transparent;"  readonly="readonly"  size="10" maxlength="10" value='<s:property value="#request.voatePersonDeptName"/>'></div></td> 
			        	 	<td align="left" width="3%" nowrap="nowrap"></td> 
			            	<td align="right" width="10%" nowrap="nowrap"><div align="left'" style="padding-top:4">点评时间:</div></td>  
			                <td align="left" width="20%">
			                	<div align="left">
				                	<input name="vote.voteTime" type="text" style="border-color:transparent;"  readonly="readonly"  size="25" maxlength="25" value='<s:property value="#request.voteTime"/>'>
				                </div>
			                </td>
			                <td></td>
			            </tr>
			        </table>
	               </td>
	           </tr>
	           <tr>
	           	<td nowrap bgcolor="#A5A5A5" width="16%"><div align="center"><font color="red">点评内容</font></div></td>
	           	<td nowrap bgcolor="#FFFFFF" >
	              	<div align="left">
	              		<textarea onclick="cacelVotePrompt(true)"  type="text"  rows="2" cols="80" id="myVote" name='vote.content' onkeyup="limitLen(500,this);"></textarea>
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
	<td align="center"> 
		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

