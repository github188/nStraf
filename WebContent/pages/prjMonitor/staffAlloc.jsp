<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*,java.util.HashMap,java.util.Map,java.util.Map.Entry"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0">
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<link href="../../css/chart.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">
	$(function(){
		var totalNum = 0;
		var restNum = 0;
		var addNum = 0;
		$("span[name='data2']").each(function(){
			//alert($(this).text());
			totalNum += Number($(this).text());
		})
		$("span[name='data3']").each(function(){
			restNum +=  Number($(this).text());
		})
		$("span[name='data4']").each(function(){
			addNum +=  Number($(this).text());
		})
		//alert(restNum);
		$("#totalNum").html(totalNum);
		$("#restPercent").html((100*Number(restNum)/totalNum).toFixed(1)+"%");
		$("#addPercent").html((100*Number(addNum)/totalNum).toFixed(1)+"%");
		
		$("span[name='percent2']").each(function(){
			var deptStaffNum = $(this).parent().find("span[name='data2']").text();
			$(this).html((100*Number(deptStaffNum)/totalNum).toFixed(1)+"%");
		})
	})
</script>
<body id="bodyid" leftmargin="10px" topmargin="0">
	<!-- ===========================================实时统计================================================ -->
	<table cellpadding="0" cellspacing="0"  style="margin: 10px 0 0 10px;width:95%">
		<tr>
			<!-- 大框左-->
			<td valign="top" class="bigbox_l"></td>
			<!-- 大框中-->		
			<td class="bigbox_mid" 	width="14%">
				<table style="margin-top: 45px;">
					<tr style="height: 40px;" >
						<td>
						<img class="counts_head" src="../../images_new/chart/dataMonitor/counts/staffNum.PNG">
						<span class="staffType">
							&nbsp;员工人数(<span id="totalNum" class="staffPercent"></span>)
						</span>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>
						<img class="counts_head"  src="../../images_new/chart/dataMonitor/counts/restStaff.PNG">
						<span class="staffType">
							&nbsp;空闲人数(<span id="restPercent" class="staffPercent"></span>)
						</span>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>
						<img class="counts_head"  src="../../images_new/chart/dataMonitor/counts/enterStaff.PNG">
						<span class="staffType">
							&nbsp;本月增加(<span id="addPercent"  class="staffPercent"></span>)
						</span>
						</td>
					</tr>
				</table>
			</td>
			<!-- 统计数据 -->
			<s:iterator value="countList" var="dataList">
				<td class="bigbox_mid">
					<table width="100%;" height="220px">
						<tr><td><br /></td></tr>
						<s:iterator value="#dataList" var="data" status="st">
							<tr>
								<td>
									<s:if test="#st.index==0">
										<span class="deptName"><s:property value="#data" /></span>
									</s:if>
									<s:elseif test="#st.index==1">
 										<img style="width:80px;height:5px;" src='../../images_new/chart/dataMonitor/colorsign/<s:property value="#data"/>.PNG' />
 									</s:elseif>
									<s:else>
										<span class="staffNum" name='data<s:property value="#st.index"/>'><s:property value="#data"/></span>
										<span class="staffPercent" name='percent<s:property value="#st.index"/>'></span>
									</s:else>
								</td>
							</tr>
						</s:iterator>							
						<tr"><td><br /></td></tr>
					</table>
				</td>
			</s:iterator>
			<!-- 大框右-->
			<td valign="top" class="bigbox_r"></td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" style="margin: 10px 0 0 10px;width:95%">
		<tr>
			<!-- 中框左-->
			<td valign="top" width="14px;">
				<img src="../../images_new/chart/dataMonitor/counts/midbox_l.PNG" style="margin: 0px 0 0 ">
			</td>
			<td class="midbox_mid" width="12%">
				<img class="counts_head" src="../../images_new/chart/dataMonitor/counts/restStaff.PNG">
				<span class="staffType">
					&nbsp;资源池
				</span>
			</td>	
			<td class="midbox_mid">
				<div style=" overflow:auto;overflow-x: hidden;height: 60px;">
					<s:iterator value="restStaffList" var="restStaffBean">
						<span class="restStaff_bg" style="background-image:url('../../images_new/chart/dataMonitor/hpannel_empty/<s:property value="#restStaffBean.deptColor" />.PNG');">
								<span class="restStaff" style='color:#<s:property value="#restStaffBean.deptColor" />;'>
									<s:set name="userName" value="#restStaffBean.userName"></s:set>
									<s:if test="#userName.length()==2">
										<s:property value="#userName.substring(0,1)"/>　<s:property value="#userName.substring(1,2)"/>
									</s:if>
									<s:else>
									<s:property value="#userName"/>
									</s:else>
								</span>
						</span> 
					</s:iterator>
				</div>
			</td>
			<!-- 中框右-->
			<td valign="top" width="14px;">
				<img src="../../images_new/chart/dataMonitor/counts/midbox_r.PNG" style="margin: 0px 0 0 ">
			</td>		
		</tr>
	</table>
	<!-- ===========================================图表区域================================================ -->
	<table id="allocTable" width="100%" style="margin-left: 0px;margin-top: 10px;cellspacing:0;cellpadding:0">
	<!-- staffAllocMap数据结构：staffAllocMap<String,Map<String,List<map<String,String>>>> -->
		<s:iterator value="staffAllocMap" id="typeMap">	
			<!-- 无项目时不显示 -->
			<s:if test="#typeMap.value.size!=0">	
				<tr>
					<!-- 项目类型 -->
					<td style="width:110px">
						<div style="position: relative;">
							<img class="prjTypeName_bg" src="../../images_new/chart/dataMonitor/font_big.png" />
						<!-- 字符串过长时缩略显示 -->
							<s:if test="#typeMap.key.length()>4">
								<div class="prjTypeName"  align="right" title='<s:property value="#typeMap.key"/>'>
									<s:property value="#typeMap.key.substring(0,4)"/><br />
								</div>
							</s:if>
							<s:else>
								<div class="prjTypeName"  align="right" title='<s:property value="#typeMap.key"/>'>
									<s:property value="#typeMap.key"/>
								</div>
							</s:else>
							</div>
						</div>
					</td>
					<!-- 连接线-->
					<td style="width:25px">
						<table align="left" cellSpacing="0"   cellpadding="0" class="linkArea" >
							<s:set name="prjNum" value="#typeMap.value.size()" />
							<s:if test="#prjNum == 1" >
								<tr >
									<td >
										<img src="../../images_new/chart/dataMonitor/point1.png" style="width:50px;height:5px;vertical-align: middle;" />
									</td>
								</tr>
							</s:if>
							<s:elseif test="#prjNum == 2">
								<tr>
									<td >
										<img src="../../images_new/chart/dataMonitor/point2.png" style="width:50px;height:70px;vertical-align: middle;" />
									</td>
								</tr>
							</s:elseif>
	
							<s:elseif test="#prjNum >= 3" >
								<!-- 判断上下垂直延长线个数 -->  
								<s:if test="#prjNum%2==0">
								  	<s:set name="expendNum" value="(#prjNum/2-1)*2-1" />
								</s:if>
								<s:else>
								  	<s:set name="expendNum" value="(#prjNum/2)*2-2" />
								</s:else>
								<!-- 连接线上部 -->
								<tr>
									<td >
										<img class="link_up_down" src="../../images_new/chart/dataMonitor/brace_up.png"/>
									</td>
								</tr>
								<!-- 上延长线 -->
								<s:bean name= "org.apache.struts2.util.Counter"  id= "counter" >     
								  <s:param name="first"  value= "1"  />   
								  <s:param name="last"  value= "#expendNum"  />     
								  <s:iterator>     
								  	<tr>
										<td >
											<img class="link_exp"  src="../../images_new/chart/dataMonitor/v_exp.png"/>
								 		</td>
									</tr> 
								  </s:iterator>     
								</s:bean>
								<!-- 连接线中部 -->
								<tr>
									<td >
										<img class="link_middle" src="../../images_new/chart/dataMonitor/brace_mid.png" />							 		 
									</td>
								</tr>
								<!-- 下延长线 -->
								<s:bean name= "org.apache.struts2.util.Counter"  id= "counter" >     
								  <s:param name="first"  value= "1"  />     
								  <s:param name="last"  value= "#expendNum"  />     
								  <s:iterator>     
								  	<tr>
										<td >
											<img class="link_exp"  src="../../images_new/chart/dataMonitor/v_exp.png"/>
								 		 </td>
									</tr> 
								  </s:iterator>     
								</s:bean>  
								<!-- 连接线下部 -->
								<tr>
									<td >
										<img class="link_up_down"  src="../../images_new/chart/dataMonitor/brace_down.png" />
									</td>
								</tr>
							</s:elseif>
						</table>
					</td>
					<!-- 项目名称及成员 -->
					<td>
						<s:iterator value="#typeMap.value" id="prjMap">
							<div class="prjArea">
								<!-- 项目名称 -->
								<img class="prjName_bg" src="../../images_new/chart/dataMonitor/font_big.png" />
								<!-- 字符串过长时换行显示 -->
								<div class="prjName" title='<s:property value="#prjMap.key"/>'>
									<s:if test="#prjMap.key.length()>7">
										<s:property value="#prjMap.key.substring(0,7)"/><br />
									</s:if>
									<s:else>
										<s:property value="#prjMap.key"/>
									</s:else>
								</div>							
								<!--带节点连接线-->
								<img class="link_point" src="../../images_new/chart/dataMonitor/point3.png"/>
								<!-- 遍历List -->
								<s:iterator value="#prjMap.value" id="staffBeanList" var="staffBean" status="st"> 
										<s:set name="userName" value="#staffBean.userName"></s:set>
										<s:set name="prjNum" value="#staffBean.prjList.size()"></s:set>
										<s:set name="numSignArr" value="{'①','②','③','④','⑤','⑥','⑦','⑧','⑨','⑩'}" />
										<!-- 项目经理 -->
										<span class="menberArea">
											<s:if test="#st.index==0">
												<img class="prjMgr_bg" onerror="this.src='../../images_new/chart/dataMonitor/hpannel_other.png'" src="../../images_new/chart/dataMonitor/hpannel/<s:property value="#staffBean.deptColor"/>.PNG" />
												<div class="prjMgr">
													<s:if test="userName.length()==2">
														<s:if test="#prjNum>1">														
															<s:property value="#userName"/> <s:property value="#numSignArr[#prjNum-1]"/>													
														</s:if>
														<s:else>
															<s:property value="#userName.substring(0,1)+'　'+#userName.substring(1,2)"/>													
														</s:else>
													</s:if>
													<s:else>
														<s:if test="#prjNum>1">														
															<s:property value="#userName"/><s:property value="#numSignArr[#prjNum-1]"/>													
														</s:if>
														<s:else>
															<s:property value="#userName"/>
														</s:else>														
													</s:else>
												</div>
											</s:if>
											<!-- 项目组员 -->
											<s:else>
												<img class="prjMenber_bg" style="position:absolute;top:0px;left:<s:property value="#st.index*30+60" />;"onerror="this.src='../../images_new/chart/dataMonitor/vpannel_other.png'" src="../../images_new/chart/dataMonitor/vpannel/<s:property value="#staffBean.deptColor"/>.PNG" />
												<div class="prjMenber" style="position:absolute;left:<s:property value="#st.index*30+60" />;">
													<s:if test="userName.length()==2">
														<s:if test="#prjNum>1">														
															<s:property value="#userName.substring(0,1)+' '+#userName.substring(1,2)"/><s:property value="#numSignArr[#prjNum-1]"/>													
														</s:if>
														<s:else>
															<s:property value="#userName.substring(0,1)+'　'+#userName.substring(1,2)"/>													
														</s:else>
													</s:if>
													<s:else>
														<s:if test="#prjNum>1">														
															<font style="font-size: 11px"><s:property value="#userName"/><s:property value="#numSignArr[#prjNum-1]"/></font>													
														</s:if>
														<s:else>
															<s:property value="#userName"/>
														</s:else>														
													</s:else>
												</div>
											</s:else>
										</span>
								</s:iterator>
							</div>
						</s:iterator>
					</td>
			   </tr>
			   <!-- 行间距 -->
			   <tr>
			   	<td><br/></td>
			   </tr>
			</s:if>
		</s:iterator>

	</table>
<%-- <table>
		<tr>
			<s:iterator value="restStaffList" id="restStaffList" var="restUser" status="st1">
				<s:iterator value="#restUser">	
			<td >
				<div style="position: relative;"></div>
					<img class="prjMenber_bg" style="position: absolute;left:<s:property value="#st1.index*30+60" />;" onerror="this.src='../../images_new/chart/dataMonitor/vpannel_other.png'" src="../../images_new/chart/dataMonitor/vpannel_<s:property value="value"/>.png" />
					<div class="prjMenber" style="position: absolute;left:<s:property value="#st1.index*30+60" />;" >
						<s:if test="key.length()==2">
							<s:property value="key.substring(0,1)+'　'+key.substring(1,2)"/>													
						</s:if>
						<s:else>
							<s:property value="key"/>
						</s:else>
					</div> 
			</td>
				</s:iterator>
			</s:iterator>
		</tr>
</table> --%>
</body>
</html>