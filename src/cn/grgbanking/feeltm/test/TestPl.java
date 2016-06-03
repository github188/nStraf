package cn.grgbanking.feeltm.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.grgbanking.feeltm.domain.testsys.StandardDic;

public class TestPl {

	private static String[] getEmails() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","feelstraf","feelstraf");
		PreparedStatement stat=conn.prepareStatement("SELECT c_email FROM sys_user");
		ResultSet rs=stat.executeQuery();
		List<String> es=new ArrayList<String>();
		while(rs.next()){
			String email=rs.getString(1);
			if(email!=null&&!"".equals(email.trim())){
				es.add(email);
			}
		}
		conn.close();
		//System.out.println(es);
		String[] ss=new String[es.size()];
		es.toArray(ss);
		return ss;
	}
	
	public static void main(String[] args) {
//		List<StandardDic> ts=new ArrayList<StandardDic>();
//		ts.add(new StandardDic("<=1", 5));
//		ts.add(new StandardDic("<=2", 4));
//		ts.add(new StandardDic("<=5", 3));
//		ts.add(new StandardDic("<=10", 2));
//		ts.add(new StandardDic(">10", 1));
//		for(int i=0;i<20;i++){
//		System.out.println("传入的天数"+i+":"+getFixTimePoint(i, ts));
//		}
		//System.out.println(roundUp(23.5428, 3));
		
		System.out.println(getDevQuality(20, 17, 40, 19));
	}
	
	/**
	 * 
	 * @param day 传入的修复天数
	 * @param standard  判断其得分的范围值   例如:  >10 对应1分 ,得分最高的排最前面
	 * @return  得到的分数
	 */
	public static int getFixTimePoint(int day,List<StandardDic> standard){
	   int i = 0;
		for(StandardDic d:standard){
			if(d.getStandard().contains(">=")){  //从标准最高的开始比较
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));  
				if(day>=tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains("<=")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));
				if(day<=tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains(">")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1));
				if(day>tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains("<")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1)); 
				if(day<tmpDay){
					i= d.getPoint();
					break;
				}
			}	
		}
	  return i;
	}
	
	/**
	 * 开发人员质量 = ( 缺陷修复率得分 * 40 + 缺陷Reopen率得分 * 40 + 缺陷平均修复时间得分 * 20 ) / 5+缺陷总值得分；
	 * @param fixPoint
	 * @param reopenPoint
	 * @param avgFixTimePoint
	 * @param bugTotalValuePoint
	 * @return
	 */
	public static int  getDevQuality(int fixPoint,int reopenPoint,int avgFixTimePoint,int bugTotalValuePoint){
		return (fixPoint*8+reopenPoint*8+avgFixTimePoint*4)+bugTotalValuePoint;	
	}
	
	/**
	 * 四舍五入
	 * @param a
	 * @param keep
	 * @return
	 */
	private static double roundUp(double a,int keep) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), keep,
				BigDecimal.ROUND_HALF_UP);
		return re.doubleValue();
	}

	
	
}
	//}


