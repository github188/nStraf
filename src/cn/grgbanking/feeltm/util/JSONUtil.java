package cn.grgbanking.feeltm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.json.JSONArray;

@SuppressWarnings("unchecked")
public class JSONUtil {
	private JSONArray jsonObj;
	private Method metd = null;
	private Field[] fds;
	private String classname;
	private String fdname = null;
	private String fdvalue = null;

	public JSONUtil(String classname) {
		jsonObj = new JSONArray();
		this.classname = classname;
		try {
			fds = Class.forName(classname).getDeclaredFields();
		} catch (ClassNotFoundException cnfex) {
			System.err.println("没有找到类：" + classname);
			cnfex.printStackTrace();
		}

	}

	// 将一个包含Object对象的list，以及一个map中的数据合并，生成一个JSONObject
	public JSONArray toJSON(List<Object> objlist, Map map) {
		try {
			for (int i = 0; i < objlist.size(); i++) {
				Class clazz = objlist.get(i).getClass();// 获取集合中的对象类型
				Map data = new LinkedHashMap();
				for (Field field : fds) {// 遍历属性数组
					fdname = field.getName();// 得到字段名，
					
					metd = clazz.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数

					Object tempObj = metd.invoke(objlist.get(i));
					if (tempObj != null) {
						//date类型，需要格式化
						fdvalue = metd.invoke(objlist.get(i), null).toString();
					} else {
						fdvalue = "";
					}
					data.put(fdname, fdvalue);

					if (map != null) {
						data.putAll(map);
					}
				}
				jsonObj.put( data);

			}// end for
			// System.out.println(jsonObj);
			return jsonObj;
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
		} catch (Exception jsonex) {
			System.out.println("调用JSONObject的put()方法出错");
			jsonex.printStackTrace();
			return null;
		}
	}
	
	/**将一个包含Object对象的list，以及一个map中的数据合并，生成一个JSONObject
	 * @param objlist
	 * @param map
	 * @param sdf 对其中的Date类型格式化
	 * @return
	 */
	public JSONArray toJSON(List<Object> objlist, Map map,SimpleDateFormat sdf){
		try {
			if(sdf==null){
				sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			
			for (int i = 0; i < objlist.size(); i++) {
				Class clazz = objlist.get(i).getClass();// 获取集合中的对象类型
				Map data = new LinkedHashMap();
				for (Field field : fds) {// 遍历属性数组
					fdname = field.getName();// 得到字段名，
					
					metd = clazz.getMethod("get" + change(fdname), null);// 根据字段名找到对应的get方法，null表示无参数

					Object tempObj = metd.invoke(objlist.get(i));
					if (tempObj != null) {
						//date类型，需要格式化
						if(field.getType().toString().equals(java.util.Date.class.toString())){
							
							fdvalue=sdf.format((java.util.Date)metd.invoke(objlist.get(i), null));
						}else{
							fdvalue = metd.invoke(objlist.get(i), null).toString();
						}
					} else {
						fdvalue = "";
					}
					data.put(fdname, fdvalue);

					if (map != null) {
						data.putAll(map);
					}
				}
				jsonObj.put( data);

			}// end for
			// System.out.println(jsonObj);
			return jsonObj;
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
		} catch (Exception jsonex) {
			System.out.println("调用JSONObject的put()方法出错");
			jsonex.printStackTrace();
			return null;
		}
	}
	
	//通过jsonlib的方式， 将一个包含Object对象的list，以及一个map中的数据合并，生成一个JSONObject（优点，能递归）
	public net.sf.json.JSONArray toJSONByJsonlib(List<Object> objlist, Map map) {
		return toJSONByJsonlib(objlist, map,null);
	}
	
	public net.sf.json.JSONArray toJSONByJsonlib(List<Object> objlist, Map map,final String formatStr) {
		
		//定义json转换时date的处理方式
		JsonConfig jsonConfig = new JsonConfig();
		final String format=formatStr==null?"yyyy-MM-dd HH:mm:ss":formatStr;
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if(value==null){ 
				      return ""; 
				 }
				 if (value instanceof Date) {
				      String str = new SimpleDateFormat(format).format((Date) value); 
				      return str; 
				 } 
				 return value.toString(); 
			}
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});
		
		//list转为jsonArray
		net.sf.json.JSONArray jsonArray=net.sf.json.JSONArray.fromObject(objlist,jsonConfig);
		Collection jsonCollection=jsonArray.toCollection(jsonArray);
		net.sf.json.JSONArray newJsonArray=new net.sf.json.JSONArray();
		if(jsonCollection!=null && !jsonCollection.isEmpty()){  
            Iterator it=jsonCollection.iterator();
            while(it.hasNext()){  
            	net.sf.json.JSONObject jsonObj=net.sf.json.JSONObject.fromObject(it.next());  
            	if(map!=null){
            		Iterator it2=map.keySet().iterator();
            		while(it2.hasNext()){
            			String key=(String)it2.next();
            			Object value=map.get(key);
            			jsonObj.put(key, value);
            		}
            	}
                newJsonArray.add(jsonObj);
            }  
        } 
		return newJsonArray;
	}

	/**
	 * 将一个包含Object对象的list，一个含string对象的list，以及一个map中的数据合并，生成一个JSONObject，
	 * 同时必须为第二个list中的对象取个名字。 该方法是特别正对于SysUserInfoAction.java中的特殊应用而编写的 param
	 * objlist:List<Object> 存放Object对象的list param strlist：List
	 * 存放需要跟Object中的属性一起添加到JSONObject中的值。这个list的size应该与objlist一样 param
	 * strListName: String 第二个list中的值存放在map中的key值 param map：Map
	 * 其他的需要存放到JSONObject中的值
	 */
	public JSONArray twoList2JSON(List<Object> objlist, List strlist,
			String strListName, Map map) {
		try {

			for (int i = 0; i < objlist.size(); i++) {
				System.out.println(i+":"+strlist.get(i));
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
					data.put(strListName, strlist.get(i));

					if (map != null) {
						data.putAll(map);
					}
				}
				jsonObj.put( data);
			}// end for
			return jsonObj;
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
		} catch (Exception jsonex) {
			System.out.println("调用JSONObject的put()方法出错");
			jsonex.printStackTrace();
			return null;
		}
	}
	public JSONArray twoList2JSON2(List<Object> objlist, List strlist,
			String strListName, Map map,String dept,Map deptMap) {
		try {
			
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
					data.put(strListName, strlist.get(i));
					if(dept.equals(fdname)){
						if( "".equals(fdvalue) || null == deptMap.get(fdvalue) || "".equals(deptMap.get(fdvalue))){
							data.put(dept, "");
						}else{
							data.put(dept, deptMap.get(fdvalue));
						}
					}
					if (map != null) {
						data.putAll(map);
					}
					
				}
				jsonObj.put( data);
			}// end for
			return jsonObj;
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
		} catch (Exception jsonex) {
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
