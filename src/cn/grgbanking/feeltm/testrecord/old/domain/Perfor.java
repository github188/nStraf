package cn.grgbanking.feeltm.testrecord.old.domain;

import java.io.Serializable;


public class Perfor implements Serializable{
	private static final long serialVersionUID = 1L;
	private long bugSum;
	private long testerSum;
	private long workTimeSum;
	private long WorkValueSum;


	public Perfor(){
		super();
	}
	
	public Perfor(long bugSum,long testerSum,long workTimeSum,long WorkValueSum) {
		//super();
		this.bugSum = bugSum;
		this.testerSum = testerSum;
		this.workTimeSum = workTimeSum;
		this.WorkValueSum = WorkValueSum;
	}


	public long getBugSum() {
		return bugSum;
	}

	public void setBugSum(long bugSum) {
		this.bugSum = bugSum;
	}

	public long getTesterSum() {
		return testerSum;
	}

	public void setTesterSum(long testerSum) {
		this.testerSum = testerSum;
	}

	public long getWorkTimeSum() {
		return workTimeSum;
	}

	public void setWorkTimeSum(long workTimeSum) {
		this.workTimeSum = workTimeSum;
	}

	public long getWorkValueSum() {
		return WorkValueSum;
	}

	public void setWorkValueSum(long workValueSum) {
		WorkValueSum = workValueSum;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}