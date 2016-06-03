package cn.grgbanking.feeltm.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import cn.grgbanking.feeltm.log.SysLog;

@SuppressWarnings("unchecked")
public class ReportBean {

	public final static int getConditionNum(HashMap map) {
		int i = 0;
		if (map == null || map.size() == 0)
			return 0;
		Set keys = map.keySet();
		Iterator keyIter = keys.iterator();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			String value = (String) map.get(key);
			if (key.equals("orgid") && value != null && !value.equals(""))// ��Action������ȡ�����л�ID��ֵ
				i += StringUtils.split(value, ",").length;
			else if (key.equals("txnid") && value != null && !value.equals(""))// ��Action������ȡ�������͵�ֵ
				i += StringUtils.split(value, ",").length;
			else if (key.equals("respcode") && value != null
					&& !value.equals(""))// ��Action������ȡ������Ӧ���ֵ
				i += StringUtils.split(value, ",").length;
			else if (key.equals("devcode") && value != null
					&& !value.equals(""))// ��Action������ȡ������Ӧ���ֵ
				i += StringUtils.split(value, ",").length;
			else if (value != null && !value.equals(""))
				i++;
		}
		return i;

	}

	public final static String findConditionLike(HashMap map, String name)
			throws Exception {
		String sql = "where 1=1 ";
		Set keys = map.keySet();
		Iterator keyIter = keys.iterator();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			String value = (String) map.get(key);
			if (value != null && !value.equals(""))
				sql += " and " + name + "." + key + " like '%" + value + "%' ";
		}
		return sql;
	}

	public final static String findConditionLike(HashMap map, String name,
			Object[] obj, Type[] types) {
		int i = 0;
		int j = 0;
		String sql = "where 1=1 ";
		try {
			if (map != null || map.size() > 0) {
				obj = new Object[ReportBean.getConditionNum(map)];
				types = new Type[ReportBean.getConditionNum(map)];

				System.out.println("map.size()=" + map.size());
				Set keys = map.keySet();
				Iterator keyIter = keys.iterator();
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					String value = (String) map.get(key);
					System.out.println(key + ":" + value);
					if (key.equals("beginDate") && value != null
							&& !value.equals("")) {
						sql += " and " + name + ".date>=? ";
						obj[i++] = Timestamp.valueOf(value);
						types[j++] = Hibernate.DATE;
					} else if (key.equals("endDate") && value != null
							&& !value.equals("")) {
						sql += " and " + name + ".date<=? ";
						obj[i++] = Timestamp.valueOf(value);
						types[j++] = Hibernate.DATE;
					} else if (key.equals("minamount") && value != null
							&& !value.equals("")) {
						sql += " and " + name + ".txnamount>=? ";
						obj[i++] = Integer.valueOf(value);
						types[j++] = Hibernate.INTEGER;

					} else if (key.equals("maxamount") && value != null
							&& !value.equals("")) {
						sql += " and " + name + ".txnamount<=? ";
						obj[i++] = Integer.valueOf(value);
						types[j++] = Hibernate.INTEGER;

					} else if (key.equals("orgid") && value != null
							&& !value.equals("")) {
						String[] str = StringUtils.split(value, ",");
						sql += " and " + name + ".orgid in (";
						for (int k = 0; k < StringUtils.split(value, ",").length; k++) {
							sql += "?";
							if (k < str.length - 1)
								sql += ",";
							obj[i++] = str[k];
							types[j++] = Hibernate.STRING;
						}
						sql += ") ";
					} else if (value != null && !value.equals("")) {
						sql += " and " + name + "." + key + " like ? ";
						obj[i++] = "%" + value + "%";
						types[j++] = Hibernate.STRING;

					}
				}// end while
			}// end if(map!=null)

		} catch (Exception e) {
			SysLog.error("error in (ReportBean.java-findConditionLike())");
			e.printStackTrace();
		}
		return sql;
	}

}
