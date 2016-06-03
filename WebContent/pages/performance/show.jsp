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
        	    <style type="text/css">
<!--
.STYLE1 {
	color: #0000FF;
	font-weight: bold;
}
.STYLE2 {
	color: #FF0000;
	font-weight: bold;
}
-->
        </style>
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
			action="<%=request.getContextPath()%>/pages/performance/performanceinfo!update.action"
			method="post">
           
              <input type="hidden" name="performance.id" value='<s:property value="performance.id"/>' readonly="readonly">
                 <table width="99%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="99%">
							<legend>
								测试人员考核明细表
							</legend>
        <table width="98%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
          <br/>
              <tr>
                <td  width="7%" align="center"  bgcolor="#999999"><div align="center">月份</div></td>
                <td  colspan="2" bgcolor="#FFFFFF" ><input name="month_date" type="text"  id="month_date" value='<s:property value="performance.month_date"/>' style="border:0" size="10" readonly="readonly"></td>
                <td width="10%" align="center" bgcolor="#999999"><div align="center"> 考核人 </div></td>
                <td  colspan="5"  bgcolor="#FFFFFF"><input name="user_id" type="text"   id="user_id" value='<s:property value="performance.user_id"/>' size="10" style="border:0" maxlength="20" readonly>                </td>
              </tr>
              <tr>
                <td width="7%" bgcolor="#999999"><div align="center">序号</div></td>
                <td width="7%"  bgcolor="#999999"><div align="center">分类</div></td>
                <td width="30%" bgcolor="#999999"><div align="center">考核项</div></td>
                <td width="10%" bgcolor="#999999"><div align="center">分值</div></td>
                <td width="7%"  bgcolor="#999999"><div align="center">自评</div></td>
                <td width="7%"  bgcolor="#999999"><div align="center">主任评分</div></td>
                <td width="7%"  bgcolor="#999999"><div align="center">经理评分</div></td>
                <td width="24%" colspan="2"  bgcolor="#999999"><div align="center">备注</div></td>
          </tr>
              
              <tr>
                <td  bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td  rowspan="3"  bgcolor="#FFFFFF"><div align="center">任务（30）</div>
                    <div align="center"></div></td>
                <td bgcolor="#FFFFFF"><div align="left"> 较易:5~14； &nbsp;&nbsp;正常:15； &nbsp;&nbsp;较难:16~20； &nbsp;&nbsp;很难:21~30 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5~30 (正常:15)</div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_1" type="text" id="subtotal_z_1" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_1"/>' readonly="readonly">
                      </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_1" type="text" id="subtotal_d_1" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_1"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_1" type="text" id="subtotal_m_1" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_1"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_1" type="text" id="remark_1" size="44" maxlength="44" value='<s:property value="performance.remark_1"/>' >
                      </div></td>
              </tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="left">较缓:6~9； &nbsp;&nbsp;&nbsp;&nbsp;正常:10； &nbsp;&nbsp;较急:11~15； &nbsp;&nbsp;紧急:16~20</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5~20 (正常:10)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_2" type="text" id="subtotal_z_2" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_2"/>' readonly="readonly">
                      </div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_2" type="text" id="subtotal_d_2" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_2"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_2" type="text" id="subtotal_m_2" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_2"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_2" type="text" id="remark_2" size="44" maxlength="44" value='<s:property value="performance.remark_2"/>' >
                      </div></td>
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="left">任务量较少:0~4； &nbsp;&nbsp;正常:5； &nbsp;&nbsp;饱满（需加班完成）:6~10</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0~10 (正常:5)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_3" type="text" id="subtotal_z_3" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_3"/>' readonly="readonly">
                      </div></td>
                                                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_3" type="text" id="subtotal_d_3" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_3"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_3" type="text" id="subtotal_m_3" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_3"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_3" type="text" id="remark_3" size="44" maxlength="44" value='<s:property value="performance.remark_3"/>' >
                      </div></td>
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">4</div></td>
                <td  rowspan="4"  bgcolor="#FFFFFF"><div align="center">时效（20）</div>
                    <div align="center"></div></td>
              <td bgcolor="#FFFFFF"><div align="left">
                    如无特殊或合理原因，却未能按时完成计划任务，并对项目或团队造成重大影响的：0~10</div>
                 <div align="left"> 如无特殊或合理原因，却未能按时完成计划任务，但对项目或团队影响不大的：11~15</div>
                <div align="left">按计划完成安排的任务或由于突发任务而未能按时完成计划任务的，但对项目或团队没有造成影响的：15~20 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0~20 (正常:20)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_4" type="text" id="subtotal_z_4" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_4"/>' readonly="readonly">
                      </div></td>
                                                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_4" type="text" id="subtotal_d_4" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_4"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_4" type="text" id="subtotal_m_4" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_4"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_4" type="text" id="remark_4" size="44" maxlength="44" value='<s:property value="performance.remark_4"/>' >
                      </div></td>
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">5</div></td>
                <td bgcolor="#FFFFFF"><div align="left">有突发任务的情况下还能按时完成计划任务 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">2~8</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_5" type="text" id="subtotal_z_5" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_5"/>' readonly="readonly">
                      </div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_5" type="text" id="subtotal_d_5" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_5"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_5" type="text" id="subtotal_m_5" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_5"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_5" type="text" id="remark_5" size="44" maxlength="44" value='<s:property value="performance.remark_5"/>' >
                      </div></td>                  
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">6</div></td>
                <td bgcolor="#FFFFFF"><div align="left">积极主动、提前高效地完成项目，比计划提前20% </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_6" type="text" id="subtotal_z_6" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_6"/>' readonly="readonly">
                      </div></td>
                                             <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_6" type="text" id="subtotal_d_6" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_6"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_6" type="text" id="subtotal_m_6" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_6"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_6" type="text" id="remark_6" size="44" maxlength="44" value='<s:property value="performance.remark_6"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">7</div></td>
                <td bgcolor="#FFFFFF"><div align="left">如果人为原因的消极怠工，沟通不及时、不协调各方面关系等因素造成项目推进比较缓慢的，比计划拖延20%</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_7" type="text" id="subtotal_z_7" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_7"/>' readonly="readonly">
                      </div></td>
                                                                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_7" type="text" id="subtotal_d_7" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_7"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_7" type="text" id="subtotal_m_7" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_7"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_7" type="text" id="remark_7" size="44" maxlength="44" value='<s:property value="performance.remark_7"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">8</div></td>
                <td width="6%" rowspan="5"  bgcolor="#FFFFFF"><div align="center">质量（20）</div>
                    <div align="center"></div></td>
                <td bgcolor="#FFFFFF"><div align="left">质量正常，无严重缺陷或客户投诉</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">20</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_8" type="text" id="subtotal_z_8" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_8"/>' readonly="readonly">
                      </div></td>
                                                                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_8" type="text" id="subtotal_d_8" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_8"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_8" type="text" id="subtotal_m_8" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_8"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_8" type="text" id="remark_8" size="44" maxlength="44" value='<s:property value="performance.remark_8"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">9</div></td>
                <td bgcolor="#FFFFFF"><div align="left">工作质量完成较好，并得到主任的提名表扬</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5</div></td>
                                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_9" type="text" id="subtotal_z_9" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_9"/>' readonly="readonly">
                      </div></td> 
                                                                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_9" type="text" id="subtotal_d_9" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_9"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_9" type="text" id="subtotal_m_9" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_9"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_9" type="text" id="remark_9" size="44" maxlength="44" value='<s:property value="performance.remark_9"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">10</div></td>
                <td bgcolor="#FFFFFF"><div align="left">未按照测试流程，出现漏测</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_10" type="text" id="subtotal_z_10" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_10"/>' readonly="readonly">
                      </div></td>
                                                                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_10" type="text" id="subtotal_d_10" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_10"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_10" type="text" id="subtotal_m_10" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_10"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_10" type="text" id="remark_10" size="44" maxlength="44" value='<s:property value="performance.remark_10"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">11</div></td>
                <td bgcolor="#FFFFFF"><div align="left">在测试过程中不是通过用例且用例没有覆盖而发现的严重或致命缺陷，如账务问题，死机，程序异常等</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~10</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_11" type="text" id="subtotal_z_11" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_11"/>' readonly="readonly">
                      </div></td>
                                                        <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_11" type="text" id="subtotal_d_11" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_11"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_11" type="text" id="subtotal_m_11" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_11"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_11" type="text" id="remark_11" size="44" maxlength="44" value='<s:property value="performance.remark_11"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">12</div></td>
                <td bgcolor="#FFFFFF"><div align="left">提交的报告或其他文档被要求返工2次（含）以上</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_12" type="text" id="subtotal_z_12" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_12"/>' readonly="readonly">
                      </div></td>
                                                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_12" type="text" id="subtotal_d_12" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_12"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_12" type="text" id="subtotal_m_12" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_12"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_12" type="text" id="remark_12" size="44" maxlength="44" value='<s:property value="performance.remark_12"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">13</div></td>
                <td  rowspan="19"  bgcolor="#FFFFFF"><div align="center">过程（20）</div>
                    <div align="center"></div></td>
                <td bgcolor="#FFFFFF"><div align="left">不能与团队和谐相处:0~4； &nbsp;&nbsp;沟通协调正常:5； &nbsp;&nbsp;能主动帮助别人:6~10 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0~10 (正常:5)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_13" type="text" id="subtotal_z_13" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_13"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_13" type="text" id="subtotal_d_13" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_13"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_13" type="text" id="subtotal_m_13" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_13"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_13" type="text" id="remark_13" size="44" maxlength="44" value='<s:property value="performance.remark_13"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">14</div></td>
                <td bgcolor="#FFFFFF"><div align="left">工作消极，需督促:0~9； &nbsp;&nbsp;工作积极主动:10； &nbsp;&nbsp;工作积极主动，能调动团队气氛:11~20 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0~20 (正常:10)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_14" type="text" id="subtotal_z_14" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_14"/>' readonly="readonly">
                      </div></td>
                                               <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_14" type="text" id="subtotal_d_14" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_14"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_14" type="text" id="subtotal_m_14" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_14"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_14" type="text" id="remark_14" size="44" maxlength="44" value='<s:property value="performance.remark_14"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">15</div></td>
                <td bgcolor="#FFFFFF"><div align="left">不适应岗位职责、技能和综合素质要求:0~4； &nbsp;&nbsp;适应岗位职责、技能和综合素质要求:5； &nbsp;&nbsp;非常适应岗位职责、技能和综合素质要求:6~10 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0~10 (正常:5)</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_15" type="text" id="subtotal_z_15" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_15"/>' readonly="readonly">
                      </div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_15" type="text" id="subtotal_d_15" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_15"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_15" type="text" id="subtotal_m_15" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_15"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_15" type="text" id="remark_15" size="44" maxlength="44" value='<s:property value="performance.remark_15"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">16</div></td>
                <td bgcolor="#FFFFFF"><div align="left">测试管理平台中日常行为的奖惩分值，如与其他项有重复的，则其他项为0分 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">测试管理平台中分值</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_16" type="text" id="subtotal_z_16" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_16"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_16" type="text" id="subtotal_d_16" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_16"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_16" type="text" id="subtotal_m_16" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_16"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_16" type="text" id="remark_16" size="44" maxlength="44" value='<s:property value="performance.remark_16"/>' >
                      </div></td>    
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">17</div></td>
                <td bgcolor="#FFFFFF"><div align="left">临时安排的任务能够按时完成，且效果良好一次 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_51" type="text" id="subtotal_z_51" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_51"/>' readonly="readonly">
                      </div></td>
                                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_51" type="text" id="subtotal_d_51" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_51"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_51" type="text" id="subtotal_m_51" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_51"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_51" type="text" id="remark_51" size="44" maxlength="44" value='<s:property value="performance.remark_51"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">18</div></td>
                <td bgcolor="#FFFFFF"><div align="left">工作态度严谨，对发现的严重问题进行跟踪，与开发存在严重分歧的情况下，能促进问题的解决 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_18" type="text" id="subtotal_z_18" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_18"/>' readonly="readonly">
                      </div></td>
                                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_18" type="text" id="subtotal_d_18" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_18"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_18" type="text" id="subtotal_m_18" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_18"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_18" type="text" id="remark_18" size="44" maxlength="44" value='<s:property value="performance.remark_18"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">19</div></td>
                <td bgcolor="#FFFFFF"><div align="left">工作态度不严谨，对严重问题没有持续跟踪，在与开发存在严重分歧的情况下，未能及时反馈问题，导致问题带到现场，但未引起严重后果 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_19" type="text" id="subtotal_z_19" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_19"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_19" type="text" id="subtotal_d_19" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_19"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_19" type="text" id="subtotal_m_19" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_19"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_19" type="text" id="remark_19" size="44" maxlength="44" value='<s:property value="performance.remark_19"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">20</div></td>
                <td bgcolor="#FFFFFF"><div align="left">发现问题后，未坚持跟踪问题或未反馈到上级导致被客户投诉或引起严重后果</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-10</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_20" type="text" id="subtotal_z_20" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_20"/>' readonly="readonly">
                      </div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_20" type="text" id="subtotal_d_20" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_20"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_20" type="text" id="subtotal_m_20" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_20"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_20" type="text" id="remark_20" size="44" maxlength="44" value='<s:property value="performance.remark_20"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">21</div></td>
                <td bgcolor="#FFFFFF"><div align="left">主动以邮件形式反馈工作过程中存在或发现的问题，并推动改进</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_21" type="text" id="subtotal_z_21" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_21"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_21" type="text" id="subtotal_d_21" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_21"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_21" type="text" id="subtotal_m_21" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_21"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_21" type="text" id="remark_21" size="44" maxlength="44" value='<s:property value="performance.remark_21"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">22</div></td>
                <td bgcolor="#FFFFFF"><div align="left">交代任务后不认真评估完成的难度，不及时提出困难，执行过程中不做沟通，出问题后找借口推脱</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_22" type="text" id="subtotal_z_22" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_22"/>' readonly="readonly">
                      </div></td>
                                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_22" type="text" id="subtotal_d_22" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_22"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_22" type="text" id="subtotal_m_22" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_22"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_22" type="text" id="remark_22" size="44" maxlength="44" value='<s:property value="performance.remark_22"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">23</div></td>
                <td bgcolor="#FFFFFF"><div align="left">临时安排的任务因为个人原因没有在规定的时间内完成</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_23" type="text" id="subtotal_z_23" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_23"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_23" type="text" id="subtotal_d_23" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_23"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_23" type="text" id="subtotal_m_23" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_23"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_23" type="text" id="remark_23" size="44" maxlength="44" value='<s:property value="performance.remark_23"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">24</div></td>
                <td bgcolor="#FFFFFF"><div align="left">项目资源紧张时，积极主动地接受任务，并通过加班加点按时完成工作</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5~10</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_24" type="text" id="subtotal_z_24" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_24"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_24" type="text" id="subtotal_d_24" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_24"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_24" type="text" id="subtotal_m_24" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_24"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_24" type="text" id="remark_24" size="44" maxlength="44" value='<s:property value="performance.remark_24"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">25</div></td>
                <td bgcolor="#FFFFFF"><div align="left">态度消极，不服从工作安排，以诸多理由推辞，甚至影响团队其它成员</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5~-10</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_25" type="text" id="subtotal_z_25" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_25"/>' readonly="readonly">
                      </div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_25" type="text" id="subtotal_d_25" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_25"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_25" type="text" id="subtotal_m_25" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_25"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_25" type="text" id="remark_25" size="44" maxlength="44" value='<s:property value="performance.remark_25"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">26</div></td>
                <td bgcolor="#FFFFFF"><div align="left">未按照测试流程严格执行，在部门内部审计/在客户现场发现/被客户投诉</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_26" type="text" id="subtotal_z_26" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_26"/>' readonly="readonly">
                      </div></td> 
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_26" type="text" id="subtotal_d_26" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_26"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_26" type="text" id="subtotal_m_26" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_26"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_26" type="text" id="remark_26" size="44" maxlength="44" value='<s:property value="performance.remark_26"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">27</div></td>
                <td bgcolor="#FFFFFF"><div align="left">工作过程中引入新的技术、方法或工具，提高了工作效率或质量</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">2~5</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_27" type="text" id="subtotal_z_27" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_27"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_27" type="text" id="subtotal_d_27" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_27"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_27" type="text" id="subtotal_m_27" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_27"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_27" type="text" id="remark_27" size="44" maxlength="44" value='<s:property value="performance.remark_27"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">28</div></td>
                <td bgcolor="#FFFFFF"><div align="left">被相关部门投诉合作能力，且属事实的</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_28" type="text" id="subtotal_z_28" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_28"/>' readonly="readonly">
                      </div></td>
                                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_28" type="text" id="subtotal_d_28" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_28"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_28" type="text" id="subtotal_m_28" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_28"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_28" type="text" id="remark_28" size="44" maxlength="44" value='<s:property value="performance.remark_28"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">29</div></td>
                <td bgcolor="#FFFFFF"><div align="left">被客户投诉合作能力</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-5</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_29" type="text" id="subtotal_z_29" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_29"/>' readonly="readonly">
                      </div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_29" type="text" id="subtotal_d_29" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_29"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_29" type="text" id="subtotal_m_29" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_29"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_29" type="text" id="remark_29" size="44" maxlength="44" value='<s:property value="performance.remark_29"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">30</div></td>
                <td bgcolor="#FFFFFF"><div align="left">公司相关部门口头/邮件表扬</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">2~5</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_30" type="text" id="subtotal_z_30" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_30"/>' readonly="readonly">
                      </div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_30" type="text" id="subtotal_d_30" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_30"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_30" type="text" id="subtotal_m_30" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_30"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_30" type="text" id="remark_30" size="44" maxlength="44" value='<s:property value="performance.remark_30"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">31</div></td>
                <td bgcolor="#FFFFFF"><div align="left">客户口头/邮件/信件表扬</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">5~10</div></td>
                                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_31" type="text" id="subtotal_z_31" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_31"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_31" type="text" id="subtotal_d_31" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_31"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_31" type="text" id="subtotal_m_31" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_31"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_31" type="text" id="remark_31" size="44" maxlength="44" value='<s:property value="performance.remark_31"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">32</div></td>
                <td  rowspan="23"  bgcolor="#FFFFFF"><div align="center">其他（10）</div>
                    <div align="center"></div></td>
                <td bgcolor="#FFFFFF"><div align="left">按计划完成交付件的提交，并及时归档 </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">10</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_32" type="text" id="subtotal_z_32" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_32"/>' readonly="readonly">
                      </div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_32" type="text" id="subtotal_d_32" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_32"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_32" type="text" id="subtotal_m_32" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_32"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_32" type="text" id="remark_32" size="44" maxlength="44" value='<s:property value="performance.remark_32"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">33</div></td>
                <td bgcolor="#FFFFFF"><div align="left">测试管理平台中每月提出问题建议数</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">价值分合计/3</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_33" type="text" id="subtotal_z_33" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_33"/>' readonly="readonly">
                      </div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_33" type="text" id="subtotal_d_33" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_33"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_33" type="text" id="subtotal_m_33" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_33"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_33" type="text" id="remark_33" size="44" maxlength="44" value='<s:property value="performance.remark_33"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">34</div></td>
                <td bgcolor="#FFFFFF"><div align="left">测试管理平台中每月处理问题建议数</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">效果分合计/3</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_34" type="text" id="subtotal_z_34" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_34"/>' readonly="readonly">
                      </div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_34" type="text" id="subtotal_d_34" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_34"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_34" type="text" id="subtotal_m_34" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_34"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_34" type="text" id="remark_34" size="44" maxlength="44" value='<s:property value="performance.remark_34"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">35</div></td>
                <td bgcolor="#FFFFFF"><div align="left">不写工作日志超过3天及以上</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-(不写日志天数合计/3)</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_35" type="text" id="subtotal_z_35" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_35"/>' readonly="readonly">
                      </div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_35" type="text" id="subtotal_d_35" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_35"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_35" type="text" id="subtotal_m_35" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_35"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_35" type="text" id="remark_35" size="44" maxlength="44" value='<s:property value="performance.remark_35"/>' >
                      </div></td> 
</tr>
  <tr>
                <td bgcolor="#FFFFFF"><div align="center">36</div></td>
                <td bgcolor="#FFFFFF"><div align="left">按时填写工作日志，且日志填写认真，有总结性描述等</div></td>
      <td  bgcolor="#FFFFFF"><div align="center">1~3</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_36" type="text" id="subtotal_z_36" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_36"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_36" type="text" id="subtotal_d_36" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_36"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_36" type="text" id="subtotal_m_36" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_36"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_36" type="text" id="remark_36" size="44" maxlength="44" value='<s:property value="performance.remark_36"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">37</div></td>
                <td bgcolor="#FFFFFF"><div align="left">填写周报时交付件未及时归档</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-1/次</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_37" type="text" id="subtotal_z_37" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_37"/>' readonly="readonly">
                      </div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_37" type="text" id="subtotal_d_37" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_37"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_37" type="text" id="subtotal_m_37" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_37"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_37" type="text" id="remark_37" size="44" maxlength="44" value='<s:property value="performance.remark_37"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">38</div></td>
                <td bgcolor="#FFFFFF"><div align="left">发现有组员未按要求归档但是周报显示是“已归档”且“已审核”状态时，则对该组主任扣3分处理</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_38" type="text" id="subtotal_z_38" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_38"/>' readonly="readonly">
                      </div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_38" type="text" id="subtotal_d_38" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_38"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_38" type="text" id="subtotal_m_38" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_38"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_38" type="text" id="remark_38" size="44" maxlength="44" value='<s:property value="performance.remark_38"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">39</div></td>
                <td bgcolor="#FFFFFF"><div align="left">部门例会时如果个人原因周报中出现有“未审核”或“未归档”字样，则每发现一个扣该组主任1分</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-1/个</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_39" type="text" id="subtotal_z_39" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_39"/>' readonly="readonly">
                      </div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_39" type="text" id="subtotal_d_39" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_39"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_39" type="text" id="subtotal_m_39" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_39"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_39" type="text" id="remark_39" size="44" maxlength="44" value='<s:property value="performance.remark_39"/>' >
                      </div></td> 
</tr>
 <tr>
                <td bgcolor="#FFFFFF"><div align="center">40</div></td>
                <td bgcolor="#FFFFFF"><div align="left">对于每次周报交付件都能及时归档，且质量良好，经主任提名表扬的，可以加3分奖励</div></td>
      <td  bgcolor="#FFFFFF"><div align="center">3</div></td>
                     <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_40" type="text" id="subtotal_z_40" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_40"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_40" type="text" id="subtotal_d_40" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_40"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_40" type="text" id="subtotal_m_40" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_40"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_40" type="text" id="remark_40" size="44" maxlength="44" value='<s:property value="performance.remark_40"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">41</div></td>
                <td bgcolor="#FFFFFF"><div align="left">测试完成未编写测试(状态)报告</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-2</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_41" type="text" id="subtotal_z_41" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_41"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_41" type="text" id="subtotal_d_41" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_41"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_41" type="text" id="subtotal_m_41" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_41"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_41" type="text" id="remark_41" size="44" maxlength="44" value='<s:property value="performance.remark_41"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">42</div></td>
                <td bgcolor="#FFFFFF"><div align="left">未按要求完成测试用例的完善</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-2</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_42" type="text" id="subtotal_z_42" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_42"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_42" type="text" id="subtotal_d_42" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_42"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_42" type="text" id="subtotal_m_42" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_42"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_42" type="text" id="remark_42" size="44" maxlength="44" value='<s:property value="performance.remark_42"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">43</div></td>
                <td bgcolor="#FFFFFF"><div align="left">未按要求进行测试用例的评审</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_43" type="text" id="subtotal_z_43" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_43"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_43" type="text" id="subtotal_d_43" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_43"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_43" type="text" id="subtotal_m_43" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_43"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_43" type="text" id="remark_43" size="44" maxlength="44" value='<s:property value="performance.remark_43"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">44</div></td>
                <td bgcolor="#FFFFFF"><div align="left">未按要求提交每月工作计划并被要求返工2次（含）以上或未按要求填写个人考核或自我考评返工2次以上</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_44" type="text" id="subtotal_z_44" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_44"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_44" type="text" id="subtotal_d_44" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_44"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_44" type="text" id="subtotal_m_44" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_44"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_44" type="text" id="remark_44" size="44" maxlength="44" value='<s:property value="performance.remark_44"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">45</div></td>
                <td bgcolor="#FFFFFF"><div align="left">提供有价值的基础技术资料、竞争对手资料或银行客户资料，并被录用</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">0.5~3分/资料</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_45" type="text" id="subtotal_z_45" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_45"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_45" type="text" id="subtotal_d_45" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_45"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_45" type="text" id="subtotal_m_45" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_45"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_45" type="text" id="remark_45" size="44" maxlength="44" value='<s:property value="performance.remark_45"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">46</div></td>
                <td bgcolor="#FFFFFF"><div align="left">资料外传，如ATM程序未删导致发机给客户，根据情节轻重进行扣分</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">－5～-2分/资料</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_46" type="text" id="subtotal_z_46" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_46"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_46" type="text" id="subtotal_d_46" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_46"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_46" type="text" id="subtotal_m_46" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_46"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_46" type="text" id="remark_46" size="44" maxlength="44" value='<s:property value="performance.remark_46"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">47</div></td>
                <td bgcolor="#FFFFFF"><div align="left">无故旷工或迟到，并未通知相关人员</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3/次</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_47" type="text" id="subtotal_z_47" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_47"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_47" type="text" id="subtotal_d_47" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_47"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_47" type="text" id="subtotal_m_47" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_47"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_47" type="text" id="remark_47" size="44" maxlength="44" value='<s:property value="performance.remark_47"/>' >
                      </div></td> 
</tr>
              <tr>
                <td bgcolor="#FFFFFF"><div align="center">48</div></td>
                <td bgcolor="#FFFFFF"><div align="left">出差应至少每周汇报一次工作状态，未及时汇报</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-1~-5</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_48" type="text" id="subtotal_z_48" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_48"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_48" type="text" id="subtotal_d_48" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_48"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_48" type="text" id="subtotal_m_48" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_48"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_48" type="text" id="remark_48" size="44" maxlength="44" value='<s:property value="performance.remark_48"/>' >
                      </div></td> 
      </tr>
      
                    <tr>
                <td bgcolor="#FFFFFF"><div align="center">49</div></td>
                <td bgcolor="#FFFFFF"><div align="left">主动组织部门或小组活动，且效果良好</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_49" type="text" id="subtotal_z_49" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_49"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_49" type="text" id="subtotal_d_49" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_49"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_49" type="text" id="subtotal_m_49" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_49"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_49" type="text" id="remark_49" size="44" maxlength="44" value='<s:property value="performance.remark_49"/>' >
                      </div></td> 
      </tr>
      
                    <tr>
                <td bgcolor="#FFFFFF"><div align="center">50</div></td>
                <td bgcolor="#FFFFFF"><div align="left">参加部门或小组活动不积极，或已诸多借口为由不参加</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-3</div></td>
                  <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_50" type="text" id="subtotal_z_50" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_50"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_50" type="text" id="subtotal_d_50" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_50"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_50" type="text" id="subtotal_m_50" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_50"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_50" type="text" id="remark_50" size="44" maxlength="44" value='<s:property value="performance.remark_50"/>' >
                      </div></td> 
      </tr>
      
       <tr>
                <td bgcolor="#FFFFFF"><div align="center">51</div></td>
                <td bgcolor="#FFFFFF"><div align="left">主动组织部门或软件体系培训一次</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">3</div></td>
                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_17" type="text" id="subtotal_z_17" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_17"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_17" type="text" id="subtotal_d_17" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_17"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_17" type="text" id="subtotal_m_17" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_17"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_17" type="text" id="remark_17" size="44" maxlength="44" value='<s:property value="performance.remark_17"/>' >
                      </div></td> 
      </tr>
      
             <tr>
                <td bgcolor="#FFFFFF"><div align="center">52</div></td>
                <td bgcolor="#FFFFFF"><div align="left">部门组间积极配合，互相帮助，并受到同事的表扬</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_52" type="text" id="subtotal_z_52" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_52"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_52" type="text" id="subtotal_d_52" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_52"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_52" type="text" id="subtotal_m_52" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_52"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_52" type="text" id="remark_52" size="44" maxlength="44" value='<s:property value="performance.remark_52"/>' >
                      </div></td> 
      </tr>
      
                   <tr>
                <td bgcolor="#FFFFFF"><div align="center">53</div></td>
                <td bgcolor="#FFFFFF"><div align="left">部门例会上发言积极，且主动分享自己的经验</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~5</div></td>
                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_53" type="text" id="subtotal_z_53" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_53"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_53" type="text" id="subtotal_d_53" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_53"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_53" type="text" id="subtotal_m_53" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_53"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_53" type="text" id="remark_53" size="44" maxlength="44" value='<s:property value="performance.remark_53"/>' readonly="readonly">
                      </div></td> 
      </tr>
                         <tr>
                <td bgcolor="#FFFFFF"><div align="center">54</div></td>
                <td bgcolor="#FFFFFF"><div align="left">积极参加部门或小组活动</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~3</div></td>
                    <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_54" type="text" id="subtotal_z_54" size="10" maxlength="10" value='<s:property value="performance.subtotal_z_54"/>' readonly="readonly">
                      </div></td>
                   <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_54" type="text" id="subtotal_d_54" size="10" maxlength="10" value='<s:property value="performance.subtotal_d_54"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_54" type="text" id="subtotal_m_54" size="10" maxlength="10" value='<s:property value="performance.subtotal_m_54"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_54" type="text" id="remark_54" size="44" maxlength="44" value='<s:property value="performance.remark_54"/>' readonly="readonly">
                      </div></td> 
      </tr>
        <tr>
                <td bgcolor="#FFFFFF"><div align="center">55</div></td>
                <td  bgcolor="#FFFFFF"><div align="center">特别<span class="STYLE1">加分</span>项</div>
          <div align="center"></div></td>
          <td   bgcolor="#FFFFFF"><div align="left"><input name="performance.add_project" type="text" id="add_project"
											maxlength="55" size="55" value='<s:property value="performance.add_project"/>' readonly="readonly"></div></td>
                <td  bgcolor="#FFFFFF"><div align="center">1~10</div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_55" type="text" id="subtotal_z_55" size="10" maxlength="10"  value='<s:property value="performance.subtotal_z_55"/>' readonly="readonly">
                      </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_55" type="text" id="subtotal_d_55"  size="10" maxlength="10" value='<s:property value="performance.subtotal_d_55"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_55" type="text" id="subtotal_m_55"  size="10" maxlength="10" value='<s:property value="performance.subtotal_m_55"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_55" type="text" id="remark_55" size="44" maxlength="44" value='<s:property value="performance.remark_55"/>' readonly="readonly">
                      </div></td> 
</tr>
  <tr>
                <td bgcolor="#FFFFFF"><div align="center">56</div></td>
                <td bgcolor="#FFFFFF"><div align="center">特别<span class="STYLE2">扣分</span>项</div>
      <div align="center"></div></td>
      <td   bgcolor="#FFFFFF"><div align="left"><input name="performance.sub_project" type="text" id="sub_project"
											maxlength="55" size="55" value='<s:property value="performance.sub_project"/>' readonly="readonly"></div></td>
                <td  bgcolor="#FFFFFF"><div align="center">-1~-10</div></td>
                 <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_z_56" type="text" id="subtotal_z_56" size="10" maxlength="10"  value='<s:property value="performance.subtotal_z_56"/>' readonly="readonly">
                      </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_d_56" type="text" id="subtotal_d_56"  size="10" maxlength="10" value='<s:property value="performance.subtotal_d_56"/>' readonly="readonly">
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.subtotal_m_56" type="text" id="subtotal_m_56"  size="10" maxlength="10" value='<s:property value="performance.subtotal_m_56"/>' readonly="readonly">
                      </div></td>
                      <td colspan="2"  bgcolor="#FFFFFF"><div align="center">
                        <input name="performance.remark_56" type="text" id="remark_56" size="44" maxlength="44" value='<s:property value="performance.remark_56"/>' readonly="readonly">
                      </div></td> 
</tr>
      
            <tr>
                <td bgcolor="#CCFFFF"><div align="center">合计</div></td>
                <td colspan="3"  bgcolor="#CCFFFF"><div align="left"></div>                  
              <div align="center"></div>                  <div align="center"></div></td>
                 <td  bgcolor="#CCFFFF"><div align="center">
                     <input name="performance.subtotal_z" type="text" id="subtotal_z" size="10" maxlength="10" value='<s:property value="performance.subtotal_z"/>' readonly="readonly">
                   </div></td>
         <td  bgcolor="#CCFFFF"><div align="center">
                     <input name="performance.subtotal_d" type="text" id="subtotal_d" size="10" maxlength="10" value='<s:property value="performance.subtotal_d"/>' readonly="readonly">
                   </div></td>
      <td  bgcolor="#CCFFFF"><div align="center">
                        <input name="performance.subtotal_m" type="text" id="subtotal_m" size="10" maxlength="10" value='<s:property value="performance.subtotal_m"/>' readonly="readonly">
                      </div></td>
              <td width="4%"   align="center"  bgcolor="#CCFFFF">总分：</td>
<td width="20%"   bgcolor="#CCFFFF"><div align="left">
                        <input name="performance.subtotal_s" type="text" id="subtotal_s" size="10" maxlength="10" value='<s:property value="performance.subtotal_s"/>' readonly="readonly">
                      </div></td> 
          </tr>
           <tr>
           <td  bgcolor="#FFFFFF"><div align="center">评语</div></td>
           	<td  bgcolor="#FFFFFF" colspan="8"><div >
           	  <textarea cols="133" rows="3" name="performance.remark"
												id="remark" readonly><s:property value="performance.remark"/>
           	  </textarea>
           	</div></td>
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
