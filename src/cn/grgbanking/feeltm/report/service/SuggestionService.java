package cn.grgbanking.feeltm.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.SuggestionDao;
import cn.grgbanking.framework.util.Page;

@Service("suggestionService")
@Transactional
public class SuggestionService {
	@Autowired
	private SuggestionDao suggestionDao;
	public boolean add(ProblemOrSuggestion suggestion){
		boolean flag=false;
		try{
			suggestionDao.addObject(suggestion);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean delete(ProblemOrSuggestion suggestion){
		boolean flag = false;
		try {
			suggestionDao.removeObject(ProblemOrSuggestion.class, suggestion.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(ProblemOrSuggestion suggestion){
		boolean flag=false;
		try{
			suggestionDao.updateObject(suggestion);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public ProblemOrSuggestion getSuggestionById(String id){
		return (ProblemOrSuggestion)suggestionDao.getObject(ProblemOrSuggestion.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String raiseDate, String raiseMan, String resloveMan,String status, String category,String summary, String description,String pno,String planFinishDate ,
			int pageNum, int pageSize,String raiseEndDate) {
		return suggestionDao.getPage(raiseDate, raiseMan, resloveMan, status, category,summary,description, pno, planFinishDate, pageNum, pageSize,raiseEndDate);
	}
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return suggestionDao.getNextNo();
	}
	
}
