package cn.grgbanking.feeltm.notify.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.notify.dao.NotifyDao;
import cn.grgbanking.feeltm.notify.domain.Notify;
import cn.grgbanking.feeltm.notify.domain.NotifyUser;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

/**
 * 2014-4-29 通知
 * @author lhyan3
 *
 */
@Service
public class NotifyService extends BaseService{
	
	@Autowired
	private NotifyDao dao;
	
	@Autowired
	private StaffInfoDao staffInfoDao;
	
	public List<Notify> getAllNotifies(){
		List<Notify> list = dao.getAllNotifies();
		return list;
	}
	/**
	 * lhy 2014-4-30
	 * @param notify
	 * @param userModel
	 * @param endTime 
	 * @param startTime 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page findNotifyPage(Notify notify, UserModel userModel,
			String startTime, String endTime, int pageNum, int pageSize) {
		String hql = "from Notify n where 1=1";
		if(null != notify.getNotifyNum() && !"".equals(notify.getNotifyNum())){
			hql += " and n.notifyNum='"+notify.getNotifyNum()+"'";
		}
		if(null != notify.getType() && !"".equals(notify.getType())){
			hql += " and n.type='"+notify.getType()+"'";
		}
		if(null != notify.getTitle() && !"".equals(notify.getTitle())){
			hql += " and n.title like '"+'%'+notify.getTitle()+'%'+"' ";
		}
		if(null != startTime && !"".equals(startTime)){
			startTime += " 00:00:00";
			hql += " and n.writeTime>=to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if(null != endTime && !"".equals(endTime)){
			endTime += " 23:59:59";
			hql += " and n.writeTime <= to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if(null != notify.getStatus() && !"".equals(notify.getStatus())){
			hql += " and n.status = '"+notify.getStatus()+"'";
		}
		hql += " order by n.writeTime desc";
		return dao.findNotifyPage(hql,pageNum,pageSize);
	}
	
	/**
	 * lhy 2014-5-4
	 * 获取下一个流水号
	 * @return
	 */
	@Transactional(readOnly=true)
	public String getNextNum() {
		return dao.getNextNum();
	}
	/**
	 * lhy 2014-5-4
	 * @param notify
	 */
	public void save(Notify notify) {
		dao.addObject(notify);
	}
	/**
	 * lhy 2014-5-4 
	 * 保存通知，并和人员建立关联
	 * @param notify
	 * @param mainids
	 * @param extrasids
	 */
	public void addNotify(Notify notify, String mainids, String extrasids) {
		if(null != mainids && !"".equals(mainids)){
			String []mains = mainids.split(",");
			for(int i=0;i<mains.length;i++){
				NotifyUser notifyUser = new NotifyUser();
				notifyUser.setFlag(1);
				notifyUser.setNotifyNum(notify.getNotifyNum());
				notifyUser.setUserid(mains[i]);
				dao.addObject(notifyUser);
			}
		}
		this.save(notify);
		if(null != extrasids && !"".equals(extrasids)){
			String []extra = extrasids.split(",");
			for(int i=0;i<extra.length;i++){
				NotifyUser notifyUser = new NotifyUser();
				notifyUser.setFlag(0);
				notifyUser.setNotifyNum(notify.getNotifyNum());
				notifyUser.setUserid(extra[i]);
				dao.addObject(notifyUser);
			}
		}
	}
	/**
	 * lhy 2014-5-5
	 * @param id
	 * @return
	 */
	public Notify findByNotifyNum(String id) {
		return dao.findByNotifyNum(id);
	}
	/**
	 * lhy 2014-5-5
	 * 提交审核
	 * @param string
	 * @return
	 */
	public int upAuditing(String string) {
		String str[]= string.split(",");
		int j = 0;
		for(int i=0;i<str.length;i++){
			Notify notify = dao.findByNotifyNum(str[i]);
			if(null != notify){
				notify.setStatus("2");
				dao.updateObject(notify);
				j++;
			}
		}
		return j;
	}
	
	
	/**
	 * lhy 2014-5-5
	 * @param notify
	 */
	public void updateNotify(Notify notify) {
		dao.updateObject(notify);
	}
	
	/**
	 * lhy 2014-5-5
	 * @param notifyNum
	 * @return
	 */
	public String[] findSendersByNum(String notifyNum) {
		List<NotifyUser> list = dao.findSendersByNum(notifyNum);
		String[] idnames = new String[4];
		String mainids = "";
		String mainnames = "";
		String extraids = "";
		String extranames = "";
		if(null!=list && list.size()>0){
			for(NotifyUser user :list){
				String name = staffInfoDao.getUsernameById(user.getUserid());
				if(user.getFlag()==1){
					mainids += ","+user.getUserid();
					mainnames += ","+name;
				}else{
					extraids += ","+user.getUserid();
					extranames += ","+name;
				}
			}
		}
		if(!"".equals(mainids)){
			mainids= mainids.substring(1);
		}
		if(!"".equals(mainnames)){
			mainnames = mainnames.substring(1);
		}
		if(!"".equals(extraids)){
			extraids = extraids.substring(1);
		}
		if(!"".equals(extranames)){
			extranames = extranames.substring(1);
		}
		idnames[0] = mainids;
		idnames[1] = mainnames;
		idnames[2] = extraids;
		idnames[3] = extranames;
		return idnames;
	}
	
	/**
	 * lhy 2014-5-5
	 * @param string
	 * @return
	 */
	public int deleteNotifys(String string) {
		String str[]= string.split(",");
		int j = dao.deleteNotifyByNum(str);
		return j;
	}
	
	/**
	 * lhy 2014-5-9
	 * @param notifyNum
	 * @param flag
	 * @return
	 */
	public String findEmailByNum(String notifyNum,int flag) {
		List<String> list = dao.findEmailByNum(notifyNum,flag);
		String email = ",";
		if(list!=null && list.size()>0){
			for(String s:list){
				if(null!=s && !"".equals(s)){
					email += ","+s;
				}
			}
		}
		if(!"".equals(email)){
			email = email.substring(1);
		}
		return email;
	}
	
	/**
	 * 
	 */
	public String findSenderEmailByNum(String notifyNum) {
		String email = "";
		email = dao.findSenderEmailByNum(notifyNum);
		return email;
	}
	
	/**
	 * 
	 * @param n
	 * @param mainids
	 * @param extrasids
	 */
	public void updateNotify(Notify n, String mainids, String extrasids) {
		int j = dao.deleteNotifyUser(n.getNotifyNum());
		if(null != mainids && !"".equals(mainids)){
			String []mains = mainids.split(",");
			System.out.println(mains.length);
			for(int i=0;i<mains.length;i++){
				System.out.println("userid:"+mains[i]);
				NotifyUser notifyUser = new NotifyUser();
				notifyUser.setFlag(1);
				notifyUser.setNotifyNum(n.getNotifyNum());
				notifyUser.setUserid(mains[i]);
				dao.addObject(notifyUser);
			}
		}
		if(null != extrasids && !"".equals(extrasids)){
			String []extra = extrasids.split(",");
			for(int i=0;i<extra.length;i++){
				NotifyUser notifyUser = new NotifyUser();
				notifyUser.setFlag(0);
				notifyUser.setNotifyNum(n.getNotifyNum());
				notifyUser.setUserid(extra[i]);
				dao.addObject(notifyUser);
			}
		}
		this.updateNotify(n);
	}
	
	/**
	 * @param notifyNum
	 * @return
	 */
	public String[] findUseridByNum(String notifyNum) {
		List<String> list = dao.findUseridByNum(notifyNum);
		if(list!=null && list.size()>0){
			String[] userids = new String[list.size()];
			for(int i = 0;i<list.size();i++){
				userids[i] = list.get(i);
			}
			return userids;
		}
		return null;
	}
	
	/**
	 * @param notifyNum
	 * @return
	 */
	public String findUseridByNum1(String notifyNum) {
		List<String> list = dao.findUseridByNum(notifyNum);
		if(list!=null && list.size()>0){
			String userids = "";
			for(int i = 0;i<list.size();i++){
				userids = ","+list.get(i);
			}
			return userids.substring(1);
		}
		return null;
	}

}
