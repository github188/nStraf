package cn.grgbanking.feeltm.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class ObjectListToJSON {
	private List<Object> objList;

	public ObjectListToJSON(List<Object> o) {
		this.objList = o;
	}

	public static JSONObject toJSON2(List<Object> objlist, String classname) {
		try {
			Field[] fds = Class.forName(classname).getDeclaredFields();// 获取属性数组
			Method metd = null;
			String fdname = null;
			String fdvalue = null;

			JSONObject jsonObj = new JSONObject();

			for (int i = 0; i < objlist.size(); i++) {
				Class clazz = objlist.get(i).getClass();// 获取集合中的对象类型
				Map data = new HashMap();
				for (Field field : fds) {// 遍历属性数组
					fdname = field.getName();// 得到字段名，
					metd = clazz.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数
					fdvalue = metd.invoke(objlist.get(i), null).toString();
					data.put(fdname, fdvalue);
				}
				jsonObj.put(classname + i, data);
			}// end for
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
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
		List<Object> l = new ArrayList<Object>();
		for (int i = 0; i < 20; i++) {
			Object v = (Object) new VO("weiling" + i, "weiling" + i, "weiling"
					+ i, "weiling" + i, "weiling" + i, i);

			l.add(v);
		}
		ObjectListToJSON t = new ObjectListToJSON(l);
		// System.out.println( t.toJSON(l, "VO"));
		System.out.println(t.toJSON2(l, "VO").toString());

	}

}
