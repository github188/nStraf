package cn.grgbanking.feeltm.common.bean;

import java.util.Date;



/** 动态周
 * @author wtjiao 2014年5月13日 上午10:33:46
 */
public class DynamicWeek {
	/** key[2014年06月30日-2014年07月05日]*/
	private String key;
	/** 时间段[06月30日-07月05日] */
	private String timePeriod;
	/** 描述[2014年6月第5周]*/
	private String desc;
	/** 备注信息 [06月30日-07月05日(2014年6月第5周)]*/
	private String info;
	/** 起始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 是否被选中 */
	private boolean selected;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
}
