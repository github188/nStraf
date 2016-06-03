<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%-- <%@ include file="/inc/calendar.inc"%> --%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>积分中心</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript">
function query(){
    
	var resultvalue = OpenModal('/pages/integralCenter/integralCenter!detailList.action?menuid=<%=request.getParameter("menuid")%>','900,700,transmgr.traninfo,watch');
}
</script>
<style type="text/css">
	.top_img{
		width: 38px;
		height: 20px
	}
	.img_up{
		width: 13px;
		height: 10px;
		margin: 0 0 5px 5px;
	}
	.change_header{
		font-size: 13px;
		margin:0 0 0 13px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		line-height: 13px;
	}
	.change_rate{
		font-size: 13px;
	}
	.change_prj{
		font-size: 16px;
	}
	.change_center{
		width: 15%;
		color: #24A9E1;
		font-weight: bold;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		line-height: 13px
	}
	.change_dept{
		width: 30%;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		line-height: 20px
	}
	/* .change_right{
		float:center;
		margin:15 16px 0 0;
		line-height: 13px
	} */
	.change_num{
		font-size: 18px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
	}
	.change_table{
		width:300px;
		height:240px;
		margin:0 30px 0 0;
		background-image:url('../../images_new/chart/renri/integralTop5_bg.png');
		background-size:100% 100%
	}

	.total_tb_header{
		width:6%;
		font-size:12px;
		line-height: 12px;
	}
	
	.total_tb_td{
		width: 6%;
		font-size:20px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
	}
	.total_tb_prjName{
		width:11%;
		font-size:13px;
		color: #24A9E1;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
	}
	.total_tb_tr{
		line-height: 35px
	}
	.total_tb_tr:hover{
		background: #25AAE2
	}
	.total_tb td {
		border-top:#FFFFFF;
		border-bottom:1px solid #E0E0D1;
		border-right:1px solid #E0E0D1;
		border-collapse: collapse;
	}
	.area_title {
		font-size:18px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		font-weight:bold;
		color: #828584
	}
	.sortTb{
	height:20%
	}
	/*圆角边框开始*/
.b1,.b2,.b3,.b4,.b1b,.b2b,.b3b,.b4b,.b{display:block;overflow:hidden;}
.b1,.b2,.b3,.b1b,.b2b,.b3b{height:1px;}
.b2,.b3,.b4,.b2b,.b3b,.b4b,.b{border-left:1px solid #77cce7;border-right:1px solid #77cce7;}
.b1,.b1b{margin:0 5px;background:#77cce7;}
.b2,.b2b{margin:0 3px;border-width:2px;}
.b3,.b3b{margin:0 2px;}
.b4,.b4b{height:2px;margin:0 1px;}
.d1{background:#F7F8F9;}
.k {height:100%;}
/*圆角边框结束*/
</style>



<body id="bodyid" leftmargin="10px"  topmargin="0">
<table style="width:80%" align="center">

	<tr>
	<td colspan="1">
		<div style="float: left;" class="area_title">积分规则</div>
	</td>
	</tr>
	<tr height="200px">
		<td >
			<div style="position: relative;margin: 0 0 0 0;">
				<table style="width: 100%;height: 10px;">
				<tr id="total_tb_header">
               <div>
               <b class="b1"></b><b class="b2 d1"></b><b class="b3 d1"></b><b class="b4 d1"></b>
               <div class="b d1 k" style="font-family: Microsoft YaHei,Microsoft JhengHei;">
				操作获得积分数说明：</br>
            	 日常操作：</br>
				1、每日准时（每日23:55前）填写日志获得<s:property value="integralSort.dayLogGrade"/>积分；7日内补填日志获得<s:property value="integralSort.dayLogNotOnTimeGrade"/>积分；超过7天或不填写日志不能获得积分。</br>
				2、每周准时（每周结束之前）填写个人周报获得<s:property value="integralSort.weekLogGrade"/>积分；超时补填个人周报获得<s:property value="integralSort.weekLogTimeout"/>积分；不填写个人周报不能获得积分。</br>
				积分奖励：</br>
				1、在爱心小鱼发表帖子能获得<s:property value="integralSort.praiseOrPraised"/>积分，每日上限<s:property value="integralSort.sendPraiseLimit"/>积分。</br>
				2、在爱心小鱼受到他人的感谢或赞扬获得<s:property value="integralSort.praiseOrPraised"/>积分，每日不设上限。</br>
				3、在爱心小鱼受到忠告或建议，不获得积分也不扣分。</br>
				
				</div>
				<b class="b4b d1"></b><b class="b3b d1"></b><b class="b2b d1"></b><b class="b1b"></b>
				</div>
				</tr>
				</table>
			</div>
			
	    </td>
	</tr>
	
	
	<tr>
		<td>
			<table>
			<tr>
				<td><div class="area_title">月度积分风云榜</div></td>
			</tr>
				<tr>
					<s:iterator value="resultList" var="staffList"  status="st"> 
					<td>
							<s:if test="#st.index==0">
								
								<table id="change_uptable" class="change_table" >
								<tr style="float: left;">
								<td style="height: 12px;"><div  class="change_header">全员排行</div></td>
								</tr>
							</s:if>
							<s:if test="#st.index==1">
								<table id="change_downtable" class="change_table" >
								<tr style="float: left;">
								<td style="height: 12px;"><div  class="change_header">部门排行</div></td>
							</tr>
							</s:if>
							<s:if test="#st.index==2">
							<table id="change_totaltable" class="change_table" >
								<tr>
								<td style="height: 12px;float: left;"><div  class="change_header" >项目排行</div></td>
							</tr>
							</s:if>
									<s:iterator value="#staffList" id="IntegralInfoVo" var="integralInfoVo" status="sta"> 
										<tr class="sortTb" >
										<td style="width: 25%;"><img class="top_img" src="../../images_new/chart/renri/top<s:property value="#sta.index+1"/>.png"/>
										</td>
										<td class="change_center"><div class="change_prj" title=''><s:property value="#integralInfoVo.integralSum"/></div>
										</td>
										<td class="change_dept">
											   <div class="change_rate"><s:property value="#integralInfoVo.detName"/></div>
										</td>
										<td class="change_dept">
										<div class="change_num"style="color：#323E4F;"><s:property value="#integralInfoVo.userName"/></div>
										</td>  
										</tr>
									</s:iterator>	
									<!-- 当排行<tr>不够填充table时,补填撑开table使位置对齐 -->
									<s:if test="#staffList.size()==0">
									<tr style="height:100%;" ></tr>
									</s:if>
									<s:elseif test="#staffList.size()==1">
									<tr style="height:80%;"  ></tr>
									</s:elseif>
									<s:elseif test="#staffList.size()==2">
									<tr style="height:60%;"  ></tr>
									</s:elseif>
									<s:elseif test="#staffList.size()==3">
									<tr style="height:40%;"  ></tr>
									</s:elseif>
									<s:elseif test="#staffList.size()==4">
									<tr style="height:20%;"  ></tr>
									</s:elseif>
									
									
						</table>
						</td>
						</s:iterator>
					
				</tr>
			</table>
		</td>
	</tr>
	</br>
	<tr>
		<td  style="padding-top: 18px;">
			<div class="area_title">个人积分</div>
			<div id="hightChartContainer" style="height: 200px;">
			
			<table style="font-family: Microsoft YaHei,Microsoft JhengHei;background-color:#E4F3F8">
			<tr>
			<td width="200">
			个人积分：<s:property value="integralSort.integralSum"/>
			</td>
			<td width="200">
			公司排名：<s:property value="integralSort.companySort"/>
			</td>
			</tr>
			<tr>
			<td>
			部门排名：<s:property value="integralSort.deptSort"/>
			</td>
			<td>
			项目排名：<s:property value="integralSort.groupSort"/>
			</td>
			</tr>
			<tr>
			<td>
			  积分详情：&nbsp;&nbsp;<input type="button" name="ok" id="ok"
					value="查询详情" class="MyButton"
					onClick="query()" image="../../images/share/yes1.gif"> 
			  </td>
			  
			</tr>
			
			<table>
			</div>
		</td>
	</tr>
</table>
</body>
</html>