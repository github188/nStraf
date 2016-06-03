package cn.grgbanking.feeltm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"unused", "unchecked"})
public class ObjectListToJSON {
	private List<Object> objList;

	public ObjectListToJSON(List<Object> o) {
		this.objList = o;
	}

	// 将两个list组合，生成一个JSONObject
	public static JSONObject TwoList2JSON(List<Object> objlist,
			String classname, String attrName, List<String> list2,
			String pageCount, String recordCount) {
		org.json.JSONObject jsonObj = new org.json.JSONObject();
		Method metd = null;
		String fdname = null;
		String fdvalue = null;

		try {
			Field[] fds = Class.forName(classname).getDeclaredFields();// 获取属性数组

			for (int i = 0; i < objlist.size(); i++) {
				Class clazz = objlist.get(i).getClass();// 获取集合中的对象类型
				Map data = new LinkedHashMap();

				for (Field field : fds) {// 遍历属性数组
					fdname = field.getName();// 得到字段名，
					metd = clazz.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数

					Object tempObj = metd.invoke(objlist.get(i));
					if (tempObj != null) {
						fdvalue = metd.invoke(objlist.get(i), null).toString();
					} else {
						fdvalue = "";
					}

					data.put(fdname, fdvalue);
					data.put(attrName, list2.get(i));
					data.put("pageCount", pageCount);
					data.put("recordCount", recordCount);

				}
				jsonObj.put(classname + i, data);
			}// end for

			return jsonObj;
		} catch (ClassNotFoundException cnfex) {
			System.out.println("没有找到类：" + classname);
			cnfex.printStackTrace();
			return null;
		} catch (NoSuchMethodException nsmex) {
			System.out.println("类：" + classname + " 没有方法："
					+ ("get" + change(fdname)));
			nsmex.printStackTrace();
			return null;
		} catch (IllegalAccessException iaex) {
			System.out.println("metd.invoke()调用出错");
			iaex.printStackTrace();
			return null;
		} catch (InvocationTargetException itex) {
			System.out.println("metd.invoke()调用出错");
			itex.printStackTrace();
			return null;
		} catch (JSONException jsonex) {
			System.out.println("调用JSONObject的put()方法出错");
			jsonex.printStackTrace();
			return null;
		}
	}

	public static JSONObject toJSON(List<Object> objlist, String classname) {

		org.json.JSONObject jsonObj = new org.json.JSONObject();
		Method metd = null;
		String fdname = null;
		String fdvalue = null;

		try {
			Field[] fds = Class.forName(classname).getDeclaredFields();// 获取属性数组

			for (int i = 0; i < objlist.size(); i++) {
				Class clazz = objlist.get(i).getClass();// 获取集合中的对象类型
				Map data = new LinkedHashMap();
				for (Field field : fds) {// 遍历属性数组
					fdname = field.getName();// 得到字段名，
					metd = clazz.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数

					Object tempObj = metd.invoke(objlist.get(i));
					if (tempObj != null) {
						fdvalue = metd.invoke(objlist.get(i), null).toString();
					} else {
						fdvalue = "";
					}
					data.put(fdname, fdvalue);
				}
				jsonObj.put(classname + i, data);
			}// end for
			return jsonObj;
		} catch (ClassNotFoundException cnfex) {
			System.out.println("没有找到类：" + classname);
			cnfex.printStackTrace();
			return null;
		} catch (NoSuchMethodException nsmex) {
			System.out.println("类：" + classname + " 没有方法："
					+ ("get" + change(fdname)));
			nsmex.printStackTrace();
			return null;
		} catch (IllegalAccessException iaex) {
			System.out.println("metd.invoke()调用出错");
			iaex.printStackTrace();
			return null;
		} catch (InvocationTargetException itex) {
			System.out.println("metd.invoke()调用出错");
			itex.printStackTrace();
			return null;
		} catch (JSONException jsonex) {
			System.out.println("调用JSONObject的put()方法出错");
			jsonex.printStackTrace();
			return null;
		}
	}

	public static String change(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}

	public static void main(String args[]) {
		/*
		 * List<Object> l = new ArrayList<Object>(); for(int i = 0 ; i < 20 ;
		 * i++) { Object v =(Object) new
		 * VO("weiling"+i,"weiling"+i,"weiling"+i,"weiling"+i,"weiling"+i,i);
		 * 
		 * l.add(v); } ObjectListToJSON t= new ObjectListToJSON(l);
		 * 
		 * System.out.println(t.toJSON2(l, "VO").toString());
		 */

	}

}
