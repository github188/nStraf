package cn.grgbanking.feeltm.dayLog.bean;

import java.util.List;

import cn.grgbanking.feeltm.dayLog.domain.DayLogVote;

public class DateVote {
	private String date;
	private List<DayLogVote> voteList;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<DayLogVote> getVoteList() {
		return voteList;
	}
	public void setVoteList(List<DayLogVote> voteList) {
		this.voteList = voteList;
	}
	
	
}
