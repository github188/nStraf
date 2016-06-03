function capMoney(xxje) {
	var low					// float
	var i,k,j, l_xx1; 			// int
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	// string
	
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟"; 
		
	xxje=xxje.replace(",","");
        low = parseFloat(xxje);	
	if (isNaN(low)) return "输入无效"; 
	
	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	if (xxje >=2100000000) {
		return "金额过大";
	}
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			// 生成大写单位，即'分角元拾...' 
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			// 生成大写数字, 即'零壹贰叁...'
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	
	if (cap == "整") cap = "零元整";
	
	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}	