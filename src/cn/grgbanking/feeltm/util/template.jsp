<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议详情</title>
<style type="text/css">
<!--
.STYLE1 {
	font-family: "新宋体";
	font-size: 24px;
	font-weight: bold;
}
.STYLE2 {
	font-family: "新宋体";
	font-weight: bold;
}
.STYLE3 {
	font-family: "新宋体";
}
-->
</style>
</head>
<body>
<table  height="232" border="1"   cellpadding="0" cellspacing="0" bordercolorlight="#000000" >
 <tr>
    <td colspan="6"  border-bottom-style:none style="border-left:1px black solid;border-right:1px black solid;border-top:1px black solid;border-bottom:0px white solid;">
    <img src="cid:IMG1" border="0">
    </td>
       </tr>
        <tr>
        
            <td colspan="6"  border-top-style:none style="border-left:1px black solid;border-right:1px black solid;border-top:0px white solid;border-bottom:1px black solid;"  align="center" border="0" >
            <span class="STYLE1">会议纪要 </span></td>       
  </tr>
  <tr bordercolorlight="#000000" >
    <td width="103"  bgcolor="#999999"><div align="right" class="STYLE2">主题</div></td>
    <td colspan="5" class="STYLE3">
      <label>
     ${subject}
      </label>   </td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">时间</div></td>
    <td width="334" >
<label>
      ${currentDateTime}(${hour})
        </label>    </td>
    <td width="58"  bgcolor="#999999"><div align="right" class="STYLE2">地点</div></td>
    <td width="144" class="STYLE3">
<label>
        ${addr}
        </label>    </td>
    <td width="86"  bgcolor="#999999"><div align="right" class="STYLE2">主持人</div></td>
    <td width="159" class="STYLE3">
<label>
        ${compere}
        </label>    </td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">出席人员</div></td>
    <td colspan="5" class="STYLE3">
      ${attendPersons}</td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">缺席人员</div></td>
    <td colspan="5" class="STYLE3">${absentPersons}</td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">会议内容</div></td>
    <td colspan="5" class="STYLE3"><textarea cols="125" rows="120"  border-top-style:none style= "overflow-y:hidden;">${content}</textarea></td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">主送</div></td>
    <td colspan="5" class="STYLE3">${main}</td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">抄送</div></td>
    <td colspan="5" class="STYLE3">${copy}</td>
  </tr>
  <tr bordercolorlight="#000000">
    <td bgcolor="#999999"><div align="right" class="STYLE2">记录</div></td>
    <td class="STYLE3"><label>
      ${writer}
      </label>    </td>
    <td bgcolor="#999999"><div align="right">复核</div></td>
    <td class="STYLE3"><label>
      ${reAudit}
      </label>    </td>
    <td bgcolor="#999999"><div align="right">签发</div></td>
    <td class="STYLE3"><label>
      ${sign}
      </label>    </td>
  </tr>
</table>
</body>
</html>
