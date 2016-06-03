package cn.grgbanking.feeltm.costControl.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.costControl.bean.DeptDetailCostBean;
import cn.grgbanking.feeltm.costControl.bean.DeptGeneralCostBean;
import cn.grgbanking.feeltm.costControl.bean.ProjectProfitBean;
import cn.grgbanking.feeltm.costControl.dao.CostControlDao;
import cn.grgbanking.feeltm.costControl.dao.DateIncomeManageDao;
import cn.grgbanking.feeltm.costControl.domain.DateIncomeManage;
import cn.grgbanking.feeltm.costControl.domain.DeptDetailCost;
import cn.grgbanking.feeltm.costControl.domain.DeptGeneralCost;
import cn.grgbanking.feeltm.dayLog.bean.DateDayLog;
import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.personDay.dao.PersonDayDao;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service
public class CostControlService extends BaseService{
	@Autowired
	private CostControlDao costControlDao;
	@Autowired
	private PersonDayDao personDayDao;
	@Autowired
	private DateIncomeManageDao incomeManageDao;
	@Autowired
	private DayLogDao dayLogDao;
	
	/** 保存部门人日详情
	 * @param detailCostList
	 */
	public void saveDetailCost(List<DeptDetailCost> detailCostList) {
		if(detailCostList!=null){
			for(int i=0;i<detailCostList.size();i++){
				costControlDao.addObject(detailCostList.get(i));
			}
		}
	}

	/**保存部门人日概况
	 * @param generalCostList
	 */
	public void saveGeneralCost(List<DeptGeneralCost> generalCostList) {
		if(generalCostList!=null){
			for(int i=0;i<generalCostList.size();i++){
				costControlDao.addObject(generalCostList.get(i));
			}
		}
	}


	/** 获取部门人日概况列表
	 */
	public List<DeptGeneralCostBean> getDeptGeneralCostBeanList(String queryDept, String queryStartTime,String queryEndTime) {
		List<DeptGeneralCost> generalCostList=costControlDao.getGeneralCostList(queryDept,queryStartTime,queryEndTime);
		//对获取到的列表按照指定日期进行封装
		List<DeptGeneralCostBean> generalCostBeanList=new ArrayList<DeptGeneralCostBean>();
		
		//原始的列表已经按照部门进行排序，同一个部门在一起，这里直接迭代即可
		String preDept="";//上一个元素的部门
		if(generalCostList!=null){
			for(int i=0;i<generalCostList.size();i++){
				DeptGeneralCost cost=generalCostList.get(i);
				//本次遍历得到的元素的部门和上一个元素部门不同，说明已经遍历到了新的部门
				if(!preDept.equals(cost.getDeptName())){
					DeptGeneralCostBean costBean=new DeptGeneralCostBean(cost.getDeptName(),cost.getDeptMembersNo(),cost.getDeptMembersNoStatistic(),queryStartTime,queryEndTime);
					generalCostBeanList.add(costBean);//加入到list中
				}
				//从benalist中获取最后的部门，然后把当前遍历的对象内容加入到该部门中
				DeptGeneralCostBean lastCostBean=generalCostBeanList.get(generalCostBeanList.size()-1);//列表中最后一个costBean，即本部门的costBean
				addToGeneralCostBean(cost,lastCostBean);//加入进去
				
				//最后把上一个元素的部门置为当前元素的部门，为下一次循环做准备
				preDept=cost.getDeptName();
			}
		}
		
		return generalCostBeanList;
	}
	
	/**加入到本部门的costBean中
	 * @param cost
	 * @param lastCostBean
	 */
	private void addToGeneralCostBean(DeptGeneralCost cost,DeptGeneralCostBean costBean) {
		//将当前的元素4个值累加到costBean中
		costBean.setDeptManagerConfirm(costBean.getDeptManagerConfirm()+cost.getDeptManagerConfirm());
		costBean.setProjectManagerConfirm(costBean.getProjectManagerConfirm()+cost.getProjectManagerConfirm());
		costBean.setNotConfirm(costBean.getNotConfirm()+cost.getNotConfirm());
		costBean.setNotRegist(costBean.getNotRegist()+cost.getNotRegist());
		Integer traineeCost=costBean.getTraineeCost()==null?0:costBean.getTraineeCost();
		Integer addTraineeCost=cost.getTraineeCost()==null?0:cost.getTraineeCost();
		costBean.setTraineeCost(traineeCost+addTraineeCost);
		//重新计算百分比
		int part1=costBean.getDeptManagerConfirm()+costBean.getNotConfirm()+costBean.getNotRegist();
		int part2=costBean.getDeptManagerConfirm()+costBean.getNotConfirm()+costBean.getNotRegist()+costBean.getProjectManagerConfirm();
		double val=(part1*1.0)/(part2*1.0);
		costBean.setNotProjectPercent(Math.round(val*10000)/100.0);
	}

	/**获取部门人日详情对象,注：这里返回的是Page<DeptDetailCostBean>,不是 Page<DeptDetailCost>
	 * @param queryDept
	 * @param queryStartTime
	 * @param queryEndTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page getDeptDetailCostBeanPage(String queryDept,String queryStartTime, String queryEndTime, int pageNum,int pageSize) {
		
		List costBeanList=getDeptDetailCostBeanList(queryDept,queryStartTime,queryEndTime);
		
		//对得到的beanList按照部门、姓名重新排序
		Collections.sort(costBeanList,new Comparator<DeptDetailCostBean>() {
			@Override
			public int compare(DeptDetailCostBean o1, DeptDetailCostBean o2) {
				if(o1.getDeptName().compareTo(o2.getDeptName())==0){
					return o1.getUserName().compareTo(o2.getUserName());
				}else{
					return o1.getDeptName().compareTo(o2.getDeptName());
				}
			}
		});
		return getMyPage(costBeanList, pageNum, pageSize);
       
	}
	
	/**获取DetailCostBean的列表
	 * @return
	 */
	public List getDeptDetailCostBeanList(String queryDept,String queryStartTime, String queryEndTime) {
		List costList=costControlDao.getDetailCostList(queryDept, queryStartTime, queryEndTime);
		List costBeanList=toDetailCostBeanList(costList, queryStartTime, queryEndTime);
		return costBeanList;
	}
	
	/**将DetailCost列表转换为DetailCostBean列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List toDetailCostBeanList(List costList, String queryStartTime, String queryEndTime) {
		List<DeptDetailCostBean> beanList=new ArrayList<DeptDetailCostBean>();
		
		//对列表先按照userid进行排序 
		Collections.sort(costList,new Comparator<DeptDetailCost>() {
			@Override
			public int compare(DeptDetailCost o1, DeptDetailCost o2) {
				return o1.getUserId().compareTo(o2.getUserId()); 
			}
		});
		
		String preUserId="";
		if(costList!=null){
			//遍历
			for(int i=0;i<costList.size();i++){
				DeptDetailCost cost=(DeptDetailCost)costList.get(i);
				if(!cost.getUserId().equals(preUserId)){
					//和之前的userid不同，说明已经遍历到新用户了
					DeptDetailCostBean costBean=new DeptDetailCostBean(cost.getDeptName(),cost.getUserId(),cost.getUserName(),queryStartTime,queryEndTime);
					beanList.add(costBean);
				}
				//获取最后一个CostBean
				DeptDetailCostBean costBean=beanList.get(beanList.size()-1);
				addToDetailCostBean(cost,costBean);
				
				//最后把上一个元素的部门置为当前元素的员工id，为下一次循环做准备
				preUserId=cost.getUserId();
			}
		}
		
		return beanList;
	}
	
	
	private void addToDetailCostBean(DeptDetailCost cost,DeptDetailCostBean costBean) {
		//将当前的元素4个值累加到costBean中
		costBean.setDeptManagerConfrim(costBean.getDeptManagerConfrim()+cost.getDeptManagerConfrim());
		costBean.setProjectManagerConfirm(costBean.getProjectManagerConfirm()+cost.getProjectManagerConfirm());
		costBean.setNotConfirm(costBean.getNotConfirm()+cost.getNotConfirm());
		costBean.setNotRegist(costBean.getNotRegist()+cost.getNotRegist());
		double traineeCost=costBean.getTraineeCost()==null?0D:costBean.getTraineeCost();
		double addTrainCost=cost.getTraineeCost()==null?0D:cost.getTraineeCost();
		costBean.setTraineeCost(traineeCost+addTrainCost);
		//重新计算百分比
		double part1=costBean.getDeptManagerConfrim()+costBean.getNotConfirm()+costBean.getNotRegist();
		double part2=costBean.getDeptManagerConfrim()+costBean.getNotConfirm()+costBean.getNotRegist()+costBean.getProjectManagerConfirm();
		double val=(part1*1.0)/(part2*1.0);
		costBean.setNotProjectPercent(Math.round(val*10000)/100.0);
	}

	private static int computedPageCount(int recordCount, int pageSize) {
        int div = recordCount / pageSize;
        int mod = recordCount % pageSize;

        //如果剩下的记录数不够页面尺寸，页将它作为一页对待
        int pageCount = (mod == 0) ? div : div + 1;
        return pageCount;
    }
	
	/**把list组装为page
	 * @param list
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getMyPage(List<Object> list, int pageNum,int pageSize){
		 Page page = new Page();
	        pageNum = (pageNum < 1) ? 1 : pageNum;
	        pageSize = (pageSize < 1) ? 15 : pageSize;

	        int recordCount = list!=null?list.size():0;
	        //设置当前页和记录总数
	        page.setCurrentPageNo(pageNum);
	        page.setRecordCount(recordCount);

	        //设置总页数和页面尺寸
	        page.setPageCount(computedPageCount(recordCount, pageSize));
	        page.setPageSize(pageSize);
	        if (recordCount > 0) {
	        	//获取page列表
	            page.setQueryResult(getPageData(list,pageNum,pageSize));
	        }
	        return page;
	}
	
	/**截取list中的部分数据，组成page列表
	 */
	private List getPageData(List resultList, int pageNum, int pageSize) {
		int startIndex=(pageNum-1)*pageSize;
		int endIndex=pageNum*pageSize-1;
		if(endIndex+1>resultList.size()){
			endIndex=resultList.size()-1;
		}
		List list=new ArrayList();
		for(int i=0;i<resultList.size();i++){
			if(i>=startIndex && i<=endIndex){
				list.add(resultList.get(i));
			}
		}
		return list;
	}
	
	/**
	 * 获取昨天的部门统计信息
	 * @param yesterday
	 * @return
	 */
	public List<DeptGeneralCost> getDeptCostInfo(String yesterday){
		return costControlDao.getDeptCostInfo(yesterday);
	}
	
	/**
	 * 获取昨天的部门人员统计信息
	 * @param yesterday
	 * @return
	 */
	public List<DeptDetailCost> getDeptUserCostInfo(String yesterday){
		return costControlDao.getDeptUserCostInfo(yesterday);
	}

	/**获取项目利润统计分页
	 * @param queryDept
	 * @param queryStartTime
	 * @param queryEndTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getProjectProfitPage(String projectName, String queryStartTime,String queryEndTime, int pageNum, int pageSize) {
		List profitList=getProjectProfitList(projectName,queryStartTime,queryEndTime);
		//按项目组名称排序
		return getMyPage(profitList, pageNum, pageSize);
	}

	/**查询项目收益列表
	 * @param projectName
	 * @param queryStartTime
	 * @param queryEndTime
	 * @return
	 */
	public List<ProjectProfitBean> getProjectProfitList(String projectName,String queryStartTime, String queryEndTime) {
		List<PersonDay> pdList= personDayDao.queryProjectListByMonthBucket(projectName, queryStartTime, queryEndTime);
		List<DateIncomeManage> incomeList=incomeManageDao.listDateIncomeByTimeBukcet(projectName, queryEndTime, queryEndTime);
		List<ProjectProfitBean> profitBeanList=new ArrayList<ProjectProfitBean>();
		if(pdList!=null){
			for(PersonDay pd:pdList){
				ProjectProfitBean profit=new ProjectProfitBean();
				profit.setProjectId(pd.getProjectId());
				profit.setProjectName(pd.getProjectName());
				profit.setMonth(pd.getExt1());
				int income=getIncome(pd.getProjectId(),pd.getExt1(),incomeList);
				int cost=((Double)Double.parseDouble(pd.getPersonDayConfirm())).intValue();
				profit.setIncome(income);
				profit.setCost(cost);
				profit.setProfit(income-cost);
				profitBeanList.add(profit);
			}
		}
		return profitBeanList;
	}

	/**获取某个项目的收益
	 * @param projectId
	 * @param month 
	 * @param incomeList
	 * @return
	 */
	private int getIncome(String projectId, String month, List<DateIncomeManage> incomeList) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		if(incomeList!=null){
			int totalIncome=0;
			for(DateIncomeManage income:incomeList){
				//如果成本中的时间跨月，则算到最后一个月上
				if(projectId.equals(income.getProjectId())&& month.equals(sdf.format(income.getEndTime()))){
					totalIncome+=income.getDateIncome().intValue();
				}
			}
			return totalIncome;
		}
		return 0;
	}

	/**获取项目指定时间的人日
	 * @param projectId 项目id
	 * @param startDate 开始时间 
	 * @param endDate 结束时间
	 * @return
	 */
	public List getProjectPersonDay(String projectId, String startDate,String endDate) {
		List<DateDayLog> logList=dayLogDao.queryDayLogByProjectAndTimePierod(projectId, startDate, endDate);
		
		return logList;
	}
}
