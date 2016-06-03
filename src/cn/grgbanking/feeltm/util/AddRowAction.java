package cn.grgbanking.feeltm.util;

import cn.grgbanking.framework.util.Page;

public class AddRowAction {
	public static int getNeedRowNum(Page page){
		int num = page.getPageSize()-page.getQueryResult().size();
		return num;
	};
}
