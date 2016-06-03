/**
 * 
 */
package cn.grgbanking.feeltm.personDay.bean;

import java.util.List;


/**
 * 每个项目某一年人日数据
 * @author ping
 */
public class SimplePersonDayBean {
		private String prjId;//项目ID
	    private String prjName;//项目名称
     	private List<Integer> ConfirmCountsArr;//1-12月确认人日数
     	//private List<Integer> PersonDayEditCountsArr;//1-12月修改人日数
     	//private List<String> IsEditCountsArr;//1-12月判断修改
     	private int curTotalNum;//今年总人日数
     	private int hisTotalNum;//历年总人日数
    	private String isEnd;//是否完结	0：否 1：是
		public SimplePersonDayBean() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	/*	public SimplePersonDayBean(String prjId, String prjName,
				List<Integer> monthCounts, int curTotalNum, int hisTotalNum) {
			super();
			this.prjId = prjId;
			this.prjName = prjName;
			this.monthCounts = monthCounts;
			this.curTotalNum = curTotalNum;
			this.hisTotalNum = hisTotalNum;
		}
*/
		/*-----------getter&setter------------*/
		public String getPrjId() {
			return prjId;
		}

		public void setPrjId(String prjId) {
			this.prjId = prjId;
		}

		public String getPrjName() {
			return prjName;
		}

		public void setPrjName(String prjName) {
			this.prjName = prjName;
		}


		

		public List<Integer> getConfirmCountsArr() {
			return ConfirmCountsArr;
		}

		public void setConfirmCountsArr(List<Integer> confirmCountsArr) {
			ConfirmCountsArr = confirmCountsArr;
		}

//		public List<Integer> getPersonDayEditCountsArr() {
//			return PersonDayEditCountsArr;
//		}
//
//		public void setPersonDayEditCountsArr(List<Integer> personDayEditCountsArr) {
//			PersonDayEditCountsArr = personDayEditCountsArr;
//		}
//
//		public List<String> getIsEditCountsArr() {
//			return IsEditCountsArr;
//		}
//
//		public void setIsEditCountsArr(List<String> isEditCountsArr) {
//			IsEditCountsArr = isEditCountsArr;
//		}

		public int getCurTotalNum() {
			return curTotalNum;
		}

		public void setCurTotalNum(int curTotalNum) {
			this.curTotalNum = curTotalNum;
		}

		public int getHisTotalNum() {
			return hisTotalNum;
		}

		public void setHisTotalNum(int hisTotalNum) {
			this.hisTotalNum = hisTotalNum;
		}

		public String getIsEnd() {
			return isEnd;
		}

		public void setIsEnd(String isEnd) {
			this.isEnd = isEnd;
		}


		
		

}
