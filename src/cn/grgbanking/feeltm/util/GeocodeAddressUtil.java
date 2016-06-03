package cn.grgbanking.feeltm.util;

import httpcall.HttpClentUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 地址编码和逆编码工具类
 * 
 * @author lhyan3 2014年7月16日
 */
public class GeocodeAddressUtil {

	private static double EARTH_RADIUS = 6378137;

	private static double distance = 2000.0;

	/**
	 * 根据地址名称获取经纬度
	 * 
	 * @param address
	 *            地址
	 * @return 经纬度 lhyan3 2014年7月16日
	 * @throws Exception
	 */
	public static double[] getLanLonByAddress(String address) {
		try{
			//中文地址转码
			address = java.net.URLEncoder.encode(address,"utf-8");
			String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address
					+ "&output=json&ak=0qxbt4Q5TmqGP2RniHs6TMx7";
			//String back = HttpRequest.sendPost(url, "");
			//百度新版不能用Post请求，否则会报返回值101错误
			String back = HttpClentUtil.getHttpReq(url, "utf-8");
			double lanlng[] = new double[2];
			JSONObject backjson;
			backjson = new JSONObject(back);
			int status = (Integer) backjson.get("status");
			if (status == 0) {
				// 获取成功
				JSONObject result = backjson.getJSONObject("result");
				JSONObject llJson2 = (JSONObject) result.get("location");
				double lng = (Double) llJson2.getDouble("lng");
				double lat = llJson2.getDouble("lat");
				lanlng[0] = lat;
				lanlng[1] = lng;
			}
			return lanlng;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 根据经纬度逆编码地址
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 地址 lhyan3 2014年7月16日
	 */
	public static String getAddressBylanlng(double lng, double lat)
			throws Exception {
		String url = "http://api.map.baidu.com/geocoder/v2/?ak=0qxbt4Q5TmqGP2RniHs6TMx7&output=json&pois=0&";
		url += "location=" + lat + "," + lng;
		// 逆编码
		//String back = HttpRequest.sendPost(url, "");
		//百度新版不能用Post请求，否则会报返回值101错误
		String back = HttpClentUtil.getHttpReq(url, "utf-8");
		 
		String address = "";
		JSONObject json;
		json = new JSONObject(back);
		 
		JSONObject json2 = (JSONObject) json.get("result");
		 
		address = json2.get("formatted_address").toString();
		return address;
	}

	/**
	 * 坐标转换 非百度经纬度转换成百度经纬度
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return lhyan3 2014年7月16日
	 */
	public static String[] changeToBaidu(double lng, double lat)
			throws Exception {
		String[] latlng = new String[2];
		String url1 = "http://api.map.baidu.com/geoconv/v1/?from=1&to=5&ak=0qxbt4Q5TmqGP2RniHs6TMx7&";
		// 矫正偏差位置
		url1 += "coords=" + lng + "," + lat;
		//String llback = HttpRequest.sendPost(url1, "");
		//百度新版不能用Post请求，否则会报返回值101错误
		String llback = HttpClentUtil.getHttpReq(url1, "utf-8");
		JSONObject lljson;
		lljson = new JSONObject(llback);
		JSONArray llJson2 = (JSONArray) lljson.get("result");
		if (llJson2.length() > 0) {
			JSONObject lljson3 = llJson2.getJSONObject(0);
			String lognitude = lljson3.getString("x");
			String latitude = lljson3.getString("y");
			latlng[0] = latitude;
			latlng[1] = lognitude;
		}
		return latlng;
	}

	/**
	 * 返回经纬度1与经纬度2的距离
	 * 
	 * @param lon1
	 *            经度1
	 * @param lat1
	 *            纬度1
	 * @param lon2
	 *            经度2
	 * @param lat2
	 *            纬度2
	 * @return lhyan3 2014年7月16日
	 */
	public static boolean getDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double dis = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		dis = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * EARTH_RADIUS;
		boolean flag = false;
		if (dis <= distance) {
			flag = true;
		}
		return flag;
	}

	public static void main(String[] args) throws Exception {
		// getLanLonByAddress("广州市天河区科林路11号");
		boolean flag = getDistance(113.36496084803, 22.955234896666,
				113.37663325959, 22.958245015802);
		System.out.println(flag);
	}

}
