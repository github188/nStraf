package cn.grgbanking.feeltm.datadir.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.datadir.dao.DataDirDao;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class DataDirService extends BaseService 
{
	@Autowired
	private DataDirDao dao;
	
	public String TOP_PARENTID="0";
    /**
     * Set the DAO for communication with the data layer.
     * 
     * @param dao
     */
	
	//-------------------------------------------------------------------------
	public SysDatadir findById(String id) {
		SysDatadir dir=(SysDatadir)dao.findById(id);
		return dir;
	}
	//-------------------------------------------------------------------------
	public String getAllPath(String id){
		SysDatadir dir = findById(id);
		ArrayList<String> list = new ArrayList<String>();
		list.add(dir.getKey());
		SysDatadir temp = findById(dir.getParentid());
		boolean flag = true;
		while(flag){
			if(temp!=null){
				list.add(temp.getKey());
				temp = findById(temp.getParentid());
			}else{
				flag = false;
			}
		}
		String path = "";
		for(int i=list.size()-1;i>=0;i--){
			path += list.get(i)+".";
		}
		return path.substring(0,path.length()-1);
	}
	//-------------------------------------------------------------------------	
	public String getParentId(String id) {
		SysDatadir dir=(SysDatadir)dao.findById(id);
		if(dir!=null)
			return dir.getParentid();
		return null;
	}
	
	//-------------------------------------------------------------------------	
	public List queryChildList(String parentId) 
	{
		return dao.queryChildList(parentId);
	}
	
	 public String getNavigation(String itemId){
		 return dao.getNavigation(itemId);
	 }
	 
	 public String getPath(String itemId){
		 return dao.getPath(itemId);
	 }
	 
	//-------------------------------------------------------------------------	
	 public  int  addItem(SysDatadir dataDir){
		 return dao.addItem(dataDir);
	 }
	 
	//-------------------------------------------------------------------------	 

	 public int delAll(String idList){
		 return dao.delAll(idList);
	 }
	 
//-------------------------------------------------------------------------	
	 
	 public int reorderItems(String idList)
	 {
		 return dao.reorderItems(idList);
	 }
	 
	//-------------------------------------------------------------------------
		public int updateItem(SysDatadir dataDir){
	         return dao.updateItem(dataDir);
		}	
}
