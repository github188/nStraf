package cn.grgbanking.feeltm.costControl.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.costControl.dao.DateIncomeManageDao;
import cn.grgbanking.feeltm.costControl.domain.DateIncomeManage;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;
@Service
public class DateIncomeManageService  extends BaseService {
		@Autowired
		private DateIncomeManageDao dateIncomeManageDao;
		/**
		 * 获取人日收入的数据列表
		 * @return
		 */
		public List<DateIncomeManage> getDateIncomeList(){
			List<DateIncomeManage> list = null;
			try {
				list = dateIncomeManageDao.listDateIncomeInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		/**
		 * 
		 * @param queryPrj  要查询的项目名称
		 * @param pageNum   
		 * @param pageSize  
		 * @return
		 */
		public Page getDateIncomePage(String queryPrj ,int pageNum,int pageSize ) {
			String hql="";
			try {
				hql = "  from DateIncomeManage   income  where 1=1 ";
				if("请选择要查询的项目名称".equals(queryPrj)){
					queryPrj=null;
				}
				if(queryPrj!=null&&!"".equals(queryPrj)){
					hql+="  and income.prjGroup='"+queryPrj+"'";
				}
				hql+="  order by income.entryTime  desc";
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dateIncomeManageDao.getPage(hql, pageNum,pageSize);
		}
		/**
		 * 根据id获取人日收入的数据
		 * @param ids  
		 * @return
		 */
		public List<DateIncomeManage> getDateIncomeById(String ids) {
;			List<DateIncomeManage> list=null;
			if(ids!=null){
				try {
					list=dateIncomeManageDao.getDateIncomeInfoById(ids);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return list;
		}
		/**
		 * 根据id删除人日收入的数据
		 * @param ids
		 * @return
		 */
		public boolean delDateIncomeById(String ids) {
			boolean flag = false;
			try {
				dateIncomeManageDao.removeObject(DateIncomeManage.class, ids);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				SysLog.error(e);
			}
			return flag;
		}
		
		

		/**
		 * 添加保存人日收入
		 * @param dateincomeManage  人日收入对象
		 * @return
		 */
		public boolean save(DateIncomeManage dateincomeManage) {
			boolean flag=false;
			try {
				dateIncomeManageDao.addObject(dateincomeManage);
				flag=true;
				return flag;
			} catch (DataAccessException e) {
				e.printStackTrace();
				return flag;
			}
			
		}
		
		/**
		 * 修改人日收入数据
		 * @param dateincomeManage
		 * @return
		 */
		public boolean update(DateIncomeManage dateincomeManage){
			boolean flag=false;
			try{
				dateIncomeManageDao.updateObject(dateincomeManage);
				flag=true;
			}catch(Exception e){
				flag=false;
				e.printStackTrace();
				SysLog.error(e);
			}
			return flag;
		}
}
