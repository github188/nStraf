package cn.grgbanking.feeltm.prj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.StandardDic;
import cn.grgbanking.feeltm.prj.dao.PointParamDao;

@Service("pointParamsService")
@Transactional
public class PointParamService {
	@Autowired
	private PointParamDao dao;
	
	/**
	 * 
	 * @param category  类别
	 * @param paramValue   参数值，例如缺陷reopen率为1%,即此时该参数应输入为0.01
	 * @return  各个类别的相应得分情况
	 */
	@Transactional(readOnly = true)
	public int getPoint(String category,double paramValue){
		int i=0;
		List<StandardDic> dics=dao.getPointParamsByCategory(category);
		for(StandardDic d:dics){
			if(d.getStandard().contains(">=")){  //从标准最高的开始比较
				double  tmpDay=Double.parseDouble(d.getStandard().substring(2));  
				if(paramValue>=tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains("<=")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(2));
				if(paramValue<=tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains(">")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(1));
				if(paramValue>tmpDay){
					i= d.getPoint();
					break;
				}
			}else if(d.getStandard().contains("<")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(1)); 
				if(paramValue<tmpDay){
					i= d.getPoint();
					break;
				}
			}	
		}
		return i;
	}
	
	
	@Transactional(readOnly = true)
	public StandardDic getDateDic(String category,double paramValue){
		List<StandardDic> dics=dao.getPointParamsByCategory(category);
		StandardDic dic=null;
		for(StandardDic d:dics){
			if(d.getStandard().contains(">=")){  //从标准最高的开始比较
				double  tmpDay=Double.parseDouble(d.getStandard().substring(2));  
				if(paramValue>=tmpDay){
					dic=d;
					break;
				}
			}else if(d.getStandard().contains("<=")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(2));
				if(paramValue<=tmpDay){
					dic=d;
					break;
				}
			}else if(d.getStandard().contains(">")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(1));
				if(paramValue>tmpDay){
					dic=d;
					break;
				}
			}else if(d.getStandard().contains("<")){
				double tmpDay=Double.parseDouble(d.getStandard().substring(1)); 
				if(paramValue<tmpDay){
					dic=d;
					break;
				}
			}	
		}
		return dic;
	}
	
	
	public String getLevel(String category,int totalPoint){
		int i=0;
		List<StandardDic> dics=dao.getPointParamsByCategory(category);
		for(StandardDic d:dics){
			if(d.getStandard().contains(">=")){  //从标准最高的开始比较
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));  
				if(totalPoint>=tmpDay){
					i= d.getLever();
					break;
				}
			}else if(d.getStandard().contains("<=")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));
				if(totalPoint<=tmpDay){
					i= d.getLever();
					break;
				}
			}else if(d.getStandard().contains(">")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1));
				if(totalPoint>tmpDay){
					i= d.getLever();
					break;
				}
			}else if(d.getStandard().contains("<")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1)); 
				if(totalPoint<tmpDay){
					i= d.getLever();
					break;
				}
			}	
		}
		
		return getLevel(i);
	}
	
	public String getTotalPointLevel(String category,int totalPoint){
		String i="";
		List<StandardDic> dics=dao.getPointParamsByCategory(category);
		for(StandardDic d:dics){
			if(d.getStandard().contains(">=")){  //从标准最高的开始比较
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));  
				if(totalPoint>=tmpDay){
					i= d.getMin();
					break;
				}
			}else if(d.getStandard().contains("<=")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(2));
				if(totalPoint<=tmpDay){
					i= d.getMin();
					break;
				}
			}else if(d.getStandard().contains(">")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1));
				if(totalPoint>tmpDay){
					i= d.getMin();
					break;
				}
			}else if(d.getStandard().contains("<")){
				int tmpDay=Integer.parseInt(d.getStandard().substring(1)); 
				if(totalPoint<tmpDay){
					i= d.getMin();
					break;
				}
			}	
		}
		
		return i;
	}
	
	
	
	public String getLevel(int i){
		String level=null;
		switch(i){
		case 1:  level="优";break;
		case 2:  level="良";break;
		case 3:  level="中";break;
		case 4:  level="较差";break;
		case 5:  level="差";break;
		}
		return level;
	}
	
	
	
	
	
}
