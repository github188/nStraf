package cn.grgbanking.feeltm.util;
import java.text.DecimalFormat;

public class Currency
{
//------------------------------------------------------------------------------
  /**
   * 
   */
  public static final String LOCAL_FORMAT_STRING="##,###.##";
//------------------------------------------------------------------------------
  /**
   * ����ʽ
   * @param strDecimal �����ַ�
   * @return����ĸ�ʽ
   */
  public static String format(String strDecimal)
  {
    DecimalFormat nf = new DecimalFormat(LOCAL_FORMAT_STRING);
    return nf.format(Double.parseDouble(strDecimal));
  }
//------------------------------------------------------------------------------
  /**
   *�ʽ
   * @param strDecimal
   * @param formate ���� ###.### or ###,###.## or $###,###.##
   * @return
   */
  public static String format(String strDecimal,String formate)
  {
    DecimalFormat nf = new DecimalFormat(formate);
    return nf.format(Double.parseDouble(strDecimal));
  }
//------------------------------------------------------------------------------
  /**
    * @param decimalValue
   * @return���ĸ�ʽ
   */
  public static String format(double decimalValue)
  {
    DecimalFormat nf = new DecimalFormat(LOCAL_FORMAT_STRING);
    return nf.format(decimalValue);
  }
//------------------------------------------------------------------------------
  public static String format(double decimalValue,String formate)
  {
    DecimalFormat nf = new DecimalFormat(formate);
    return nf.format(decimalValue);
  }
}