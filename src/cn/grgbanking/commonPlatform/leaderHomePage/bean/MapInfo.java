package cn.grgbanking.commonPlatform.leaderHomePage.bean;

/**
 * 领导首页中地图
 * @author xing
 *
 */
public class MapInfo {
	
	private String hckey;//省份对应的key
	private int value;//项目数
	private String projects;//项目列表 {"hc-key": "cn-sh","value": 26,"project":"qweqwe--吴浩星"+'</br>'+"qweqwe--吴浩星"}
	public String getHckey() {
		return hckey;
	}
	public void setHckey(String hckey) {
		this.hckey = hckey;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getProjects() {
		return projects;
	}
	public void setProjects(String projects) {
		this.projects = projects;
	}

}
