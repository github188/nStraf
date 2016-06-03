package cn.grgbanking.feeltm.expense.webapp;


public class Test {
//	public static void main(String[] args) {
//		String e = "E1000";
//		//String e = "";
//		System.out.println(e.contains("E"));
//		if(e.contains("E")) {
//			e = e.substring(1);
//			System.out.println("substring后...."+e);
//			long d=Long.parseLong(e)+1;
//			System.out.println("d..."+d);
//			DecimalFormat format=new DecimalFormat("e0000");
//			System.out.println("最后存入数据库的...."+format.format(d));
//		}
//	}
	public static void main(String[] args) {
		String udpateCostData = "12.0 ,10.0 ,0.0 ,199.0 ,5.0 ,5,5,60.0 ,296.00,12.0 ,10.0 ,0.0 ,199.0 ,5.0 ,5,5,60.0 ,296.00";
		String[] costDataArray = udpateCostData.split(",");
		int count = (costDataArray.length)/9;
		String[] dataArray = new String[count];
		int len = 0;
		for(int i=0;i<costDataArray.length;i=i+9){
			String str = "";
			for(int j=i;j<i+9;j++){
				str+=","+costDataArray[j];
			}
			if(!"".equals(str)){
				str=str.substring(1);
			}
			dataArray[len++]=str;
		}
	}
}

