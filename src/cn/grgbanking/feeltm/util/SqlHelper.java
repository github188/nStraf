package cn.grgbanking.feeltm.util;

import java.util.List;

/**
 * ��ݲ�����֯Ҫִ�е�sql���
 * 
 * @author ljming
 * 
 */
@SuppressWarnings("unchecked")
public class SqlHelper {

	private static final int MAX_IN_NUMBERS = 255;

	// ------------------------------------------------------------------------------

	public static final String fitStrInCondition(String fieldName,
			String[] values) {
		StringBuffer sbf = new StringBuffer();
		int row = values.length / MAX_IN_NUMBERS;
		int remain = values.length % MAX_IN_NUMBERS;
		int index = 0;
		for (int i = 1; i <= row; i++) {
			sbf.append(fieldName).append(" in(");
			for (int j = 1; j < MAX_IN_NUMBERS; j++) {
				sbf.append("'").append(values[index++]).append("',");
			}
			sbf.append("'").append(values[index++]).append("')");
			if (row > 1 && i < row)
				sbf.append(" or ");
		}
		if (remain > 0) {
			if (row > 0) {
				sbf.append(" or ");
			}
			sbf.append(fieldName).append(" in(");
			for (int i = 1; i < remain; i++) {
				sbf.append("'").append(values[index++]).append("',");
			}
			sbf.append("'").append(values[index++]).append("')");
		}
		if (values.length < MAX_IN_NUMBERS)
			return sbf.toString();
		return "(" + sbf.toString() + ")";
	}

	// ------------------------------------------------------------------------------

	public static final String fitNumInCondition(String fieldName,
			String[] values) {
		StringBuffer sbf = new StringBuffer();
		int row = values.length / MAX_IN_NUMBERS;
		int remain = values.length % MAX_IN_NUMBERS;
		int index = 0;
		for (int i = 1; i <= row; i++) {
			sbf.append(fieldName).append(" in(");
			for (int j = 1; j < MAX_IN_NUMBERS; j++) {
				sbf.append(values[index++]).append(",");
			}
			sbf.append(values[index++]).append(")");
			if (row > 1 && i < row)
				sbf.append(" or ");
		}
		if (remain > 0) {
			if (row > 0) {
				sbf.append(" or ");
			}
			sbf.append(fieldName).append(" in(");
			for (int i = 1; i < remain; i++) {
				sbf.append(values[index++]).append(",");
			}
			sbf.append(values[index++]).append(")");
		}
		if (values.length < MAX_IN_NUMBERS)
			return sbf.toString();
		return "(" + sbf.toString() + ")";
	}

	// ------------------------------------------------------------------------------

	public static final String fitInCondition(String fieldName,
			Object[] values, List parameters) {
		StringBuffer sbf = new StringBuffer();
		int row = values.length / MAX_IN_NUMBERS;
		int remain = values.length % MAX_IN_NUMBERS;
		int index = 0;
		for (int i = 1; i <= row; i++) {
			sbf.append(fieldName).append(" in(");
			for (int j = 1; j < MAX_IN_NUMBERS; j++) {
				sbf.append("?,");
				parameters.add(values[index++]);
			}
			parameters.add(values[index++]);
			sbf.append("?)");

			if (row > 1 && i < row)
				sbf.append(" or ");
		}
		if (remain > 0) {
			if (row > 0) {
				sbf.append(" or ");
			}
			sbf.append(fieldName).append(" in(");
			for (int i = 1; i < remain; i++) {
				sbf.append("?,");
				parameters.add(values[index++]);
			}
			parameters.add(values[index++]);
			sbf.append("?)");
		}
		if (values.length < MAX_IN_NUMBERS)
			return sbf.toString();
		return "(" + sbf.toString() + ")";
	}
	// ------------------------------------------------------------------------------

}
