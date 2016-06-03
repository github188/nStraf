package cn.grgbanking.commonPlatform.leaderHomePage.bean;

import java.math.BigDecimal;
/**
 * 领导首页合同速度计模块
 * @author xing
 *
 */
public class ContractInfo {
	/**
	 * 年度合同计划金额
	 */
	private BigDecimal contractPlan;
	/**
	 * 合同总金额
	 */
	private BigDecimal sumContract;
	/**
	 * 从数字字典获取参数,合同速度计刻度颜色分布比例
	 * 参数设置-领导首页-合同参数
	 */
	private float fristPercent;
	private float secondPercent;
	//参数设置-领导首页-合同参数  --合同金额单位
	private String unit;
	public BigDecimal getContractPlan() {
		return contractPlan;
	}
	public void setContractPlan(BigDecimal contractPlan) {
		this.contractPlan = contractPlan;
	}
	public BigDecimal getSumContract() {
		return sumContract;
	}
	public void setSumContract(BigDecimal sumContract) {
		this.sumContract = sumContract;
	}
	public float getFristPercent() {
		return fristPercent;
	}
	public void setFristPercent(float fristPercent) {
		this.fristPercent = fristPercent;
	}
	public float getSecondPercent() {
		return secondPercent;
	}
	public void setSecondPercent(float secondPercent) {
		this.secondPercent = secondPercent;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
