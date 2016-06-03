<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
	function closeModal(){
	 window.close();
	}

	function  GetSelIds(){
		 var idList="";
		var  em= document.all.tags("input");
		for(var i=0;i<em.length;i++){
		  if(em[i].type=="checkbox"&&(em[i].name=="id1"||em[i].name=="id2")){
		      if(em[i].checked){
		        idList+=","+em[i].value.split(",")[0];
		  }
		 } 
		 } 
		if(idList=="") 
		   	return "";
		//if(document.getElementById("chkAll").checked)
	 	//	return idList.substring(5);//去掉"全选"
		return idList.substring(1);
	}

	function SelAll1(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox"&&em[i].name=="id1")
	    		em[i].checked=chkAll.checked;
		}
	}

	function SelAll2(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox"&&em[i].name=="id2")
	    		em[i].checked=chkAll.checked;
		}
	}
	
	function checkPic()
	{
		 var idList=GetSelIds();
		 if(idList==""){
		 	alert("请选择要查看的交易项！");
		 }
		 else{
			 var strUrl="/pages/watch/transBanknoteSeqHour!checkPic.action?ids="+idList;
			 var returnValue=OpenModal(strUrl,"600,600,watch.viewSeqPic")
		 }
	}
</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form action="/pages/transHourInfo/transHourInfo!show.action"
			method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="wacth.monitor" />
							</legend>
							<s:iterator value="transHourInfo" id="transHourInfo">
								<table width="100%" align="center" class="popnewdialog1">
									<tr height="5px">
										<td></td>
									</tr>
									<tr>
										<td>
											<s:text name="watch.trans_code" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="transCode" />
										</td>

										<td>
											<s:text name="watch.trans_orgid" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="transOrgid" />
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="watch.account_no" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="accountNo" />
										</td>

										<td>
											<s:text name="watch.termID" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="termid" />
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="watch.trans_time" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="transTime" />
										</td>

										<td>
											<s:text name="watch.journal_no" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="journalNo" />
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="watch.trans_amt" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="transAmt" />
										</td>
										<td>
											<s:text name="watch.trans_result" />
											<s:text name="label.colon" />
										</td>
										<td>
											<tm:dataDir beanName="transHourInfo" property="transResult"
												path="transMgr.transResult" scope="request" />
										</td>
									</tr>
									<tr>
										<td>
											<s:text name="watch.note_count" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="noteNum" />
										</td>
										<td>
											<s:text name="watch.blacklist_count" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="blNum" />
										</td>
									</tr>

									<tr>
										<td>
											<s:text name="watch.repeat_count" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="repeatNum" />
										</td>
										<td>
											<s:text name="watch.callback_count" />
											<s:text name="label.colon" />
										</td>
										<td>
											<s:property value="callBackNum" />
										</td>
									</tr>
								</table>
							</s:iterator>
						</fieldset>
					</td>
				</tr>
				<!-- <tr>
					<td>
						&nbsp;
					</td>
				</tr> -->
				<tr>
					<td height="30">
						<div align="left"><img src="../../images/noteType.gif" /></div>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<LEGEND>
								<s:text name="trans.transaction" />
							</LEGEND>
							<table width="100%" border="0" cellpadding="1" cellspacing="1"
								bgcolor="#000066">
								<tr bgcolor="#FFFFFF">
									<td>
										<table width="100%" cellSpacing="0" cellPadding="0">
											<tr>
												<td>
													<div align="left">
														<input type="checkbox" name="all" id="chkAll1" value="all"
															onClick="SelAll1(this)">
														<label for=chkAll1>
															<s:text name="operInfo.checkall" />
														</label>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="1" cellspacing="1"
								bgcolor="#000066">
								<tr class="oracolumncenterheader">
									<td nowrap width="4%">
										<s:text name="label.select" />
									</td>
									<td nowrap width="12%">
										<div align="center">
											<s:text name="watch.seriaNo" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="watch.note_type" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.moneyType" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.denomination" />
										</div>
									</td>
									
									<td nowrap width="4%">
										<s:text name="label.select" />
									</td>
									<td nowrap width="12%">
										<div align="center">
											<s:text name="watch.seriaNo" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="watch.note_type" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.moneyType" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.denomination" />
										</div>
									</td>
								</tr>
								<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									<s:iterator value="transBanknoteSeqHourList1"
										id="transBanknoteSeqHour1" status="status">
										<td nowrap align="center">
											<input type="checkbox" name="id1"
												value='<s:property value="id"/>' />
										</td>
										<td nowrap align="center">
											<s:if test='noteType=="0"'>
												<font color="#00AA00">
											</s:if>
											<s:elseif test='noteType=="1"'>
												<font color="#FF0000">
											</s:elseif>
											<s:elseif test='noteType=="3"'>
												<font color="#FF9900">
											</s:elseif>
											<s:else>
												<font color="#0000AA">
											</s:else>
											<s:property value="seriaNo" />
											</font>
										</td>
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour1" property="noteType" path="transMgr.noteType" />
										</td>
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour1" property="currency" path="ruleMgr.moneyType" />
										</td>
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour1" property="denomination" path="ruleMgr.moneyDenomination" />
										</td>
										<s:if test="#status.count%2 == 0">
											<s:if test="(#status.count/2)%2 == 0">
								</tr>
								<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									</s:if>
									<s:else>
								</tr>
								<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									</s:else>
									</s:if>
									</s:iterator>
									<s:if test="#listSize1%2 == 1">
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</s:if>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<LEGEND>
								<s:text name="trans.callback" />
							</LEGEND>
							<table width="100%" border="0" cellpadding="1" cellspacing="1"
								bgcolor="#000066">
								<tr bgcolor="#FFFFFF">
									<td>
										<table width="100%" cellSpacing="0" cellPadding="0">
											<tr>
												<td>
													<div align="left">
														<input type="checkbox" name="all" id="chkAll2" value="all"
															onClick="SelAll2(this)">
														<label for=chkAll2>
															<s:text name="operInfo.checkall" />
														</label>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="1" cellspacing="1"
								bgcolor="#000066">
								<tr class="oracolumncenterheader">
									<td nowrap width="4%">
										<s:text name="label.select" />
									</td>
									<td nowrap width="12%">
										<div align="center">
											<s:text name="watch.seriaNo" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="watch.note_type" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.moneyType" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.denomination" />
										</div>
									</td>
									
									<td nowrap width="4%">
										<s:text name="label.select" />
									</td>
									<td nowrap width="12%">
										<div align="center">
											<s:text name="watch.seriaNo" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="watch.note_type" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.moneyType" />
										</div>
									</td>
									<td nowrap width="8%">
										<div align="center">
											<s:text name="rule.list.denomination" />
										</div>
									</td>
								</tr>
								<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									<s:iterator value="transBanknoteSeqHourList2"
										id="transBanknoteSeqHour2" status="status">
										<td nowrap align="center">
											<input type="checkbox" name="id2"
												value='<s:property value="id"/>' />
										</td>
										<td nowrap align="center">
											<s:if test='noteType=="0"'>
												<font color="#00AA00">
											</s:if>
											<s:elseif test='noteType=="1"'>
												<font color="#FF0000">
											</s:elseif>
											<s:elseif test='noteType=="3"'>
												<font color="#FF9900">
											</s:elseif>
											<s:else>
												<font color="#0000AA">
											</s:else>
											<s:property value="seriaNo" />
											</font>
										</td>
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour2" property="noteType" path="transMgr.noteType" />
										</td>
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour2" property="currency" path="ruleMgr.moneyType" />
										</td nowrap align="center">
										<td nowrap align="center">
											<tm:dataDir beanName="transBanknoteSeqHour2" property="denomination" path="ruleMgr.moneyDenomination" />
										</td>
										<s:if test="#status.count%2 == 0">
											<s:if test="(#status.count/2)%2 == 0">
								</tr>
								<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									</s:if>
									<s:else>
								</tr>
								<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
									</s:else>
									</s:if>
									</s:iterator>
									<s:if test="#listSize2%2 == 1">
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</s:if>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				

				<tr>
					<td align="center">
						<input type="button" name="return" value='查看图片' class="MyButton"
							onclick="checkPic();" image="../../images/share/view.gif">
						&nbsp;
						<input type="button" name="return"
							value='<s:text name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
		</form>

	</body>
</html>

