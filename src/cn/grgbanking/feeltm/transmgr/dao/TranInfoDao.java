package cn.grgbanking.feeltm.transmgr.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.SpecialRegulation;
import cn.grgbanking.feeltm.domain.TranAbnoinfo;
import cn.grgbanking.feeltm.domain.TranCallbackAbnoinfo;
import cn.grgbanking.feeltm.domain.TranEspeciinfo;
import cn.grgbanking.feeltm.domain.TranInfo;
import cn.grgbanking.feeltm.domain.TransBanknoteSeq;
import cn.grgbanking.feeltm.domain.TransHourInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.dao.BaseDao;

@SuppressWarnings( { "deprecation", "unchecked" })
@Repository
public class TranInfoDao extends BaseDao<TranInfo> {

	@SuppressWarnings("finally")
	public boolean unatureAny(String date) {
		// ////////////////////////
		boolean flag = false;
		Connection cnn = null;
		Session session = this.getSessionFactory().openSession();
		try {
			cnn = session.connection();
			CallableStatement cstmt = cnn
					.prepareCall("{call unatureAnyAndClearDate(?,?) }");
			cstmt.setString(1, date);
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();

			int tempFlag = 0;
			tempFlag = cstmt.getInt(2);
			cnn.commit();
			if (tempFlag == 1)
				flag = true;

		} catch (Exception ee) {
			ee.printStackTrace();
			try {
				cnn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} finally {
			try {
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
		}
	}

	public List analyseAbnormitRepeat(String date) {
		List addTransInfoRepList = new ArrayList();
		try {

			String strSql = " from TransHourInfo th   where th.repeatNum>0 and th.blNum=0 and th.transCode='DEP' and reserve1='1' and th.createDate<='"
					+ date + "'";

			List list = this.getHibernateTemplate().find(strSql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String transId = "";
					String seq = "";
					TransHourInfo transHourInfo = (TransHourInfo) list.get(i);
					transId = transHourInfo.getId();
					String strSubSql = "select distinct seriaNo from TransBanknoteSeq t where tranId='"
							+ transId
							+ "'  and noteFlag  ='0' and noteType ='3'";
					List subList = this.getHibernateTemplate().find(strSubSql);
					if (subList != null && subList.size() > 0) {

						Iterator iterSub = subList.iterator();
						int k = 0;
						while (iterSub.hasNext() && k < 5) {
							Object obj = iterSub.next();
							if (k == 0)
								seq += obj.toString();
							else
								seq += "," + obj.toString();
							k++;
						}
					}
					TranAbnoinfo tranAbnoinfo = new TranAbnoinfo();
					tranAbnoinfo.setAccountNo(transHourInfo.getAccountNo());
					tranAbnoinfo.setAnyDate(new Date());
					tranAbnoinfo.setTransNotenum(transHourInfo.getNoteNum());
					tranAbnoinfo.setBlackNotenum(transHourInfo.getBlNum());
					tranAbnoinfo.setCallbackNotenum(transHourInfo
							.getCallBackNum());
					tranAbnoinfo.setCreateDate(transHourInfo.getCreateDate());
					tranAbnoinfo.setJournalNo(transHourInfo.getJournalNo());
					tranAbnoinfo.setNote(seq);
					tranAbnoinfo.setRegulation("");
					tranAbnoinfo.setRepeatNotenum(transHourInfo.getRepeatNum());
					tranAbnoinfo.setReserve1(transHourInfo.getReserve1());
					tranAbnoinfo.setReserve2(transHourInfo.getReserve2());
					tranAbnoinfo.setReserve3(transHourInfo.getReserve3());
					tranAbnoinfo.setTermid(transHourInfo.getTermid());
					tranAbnoinfo.setTranId(transHourInfo.getId());
					tranAbnoinfo.setTransAmt(transHourInfo.getTransAmt());
					tranAbnoinfo.setTransCode(transHourInfo.getTransCode());
					tranAbnoinfo.setTransDate(transHourInfo.getTransDate());
					tranAbnoinfo.setTransOrgid(transHourInfo.getTransOrgid());
					tranAbnoinfo.setTransResult(transHourInfo.getTransResult());
					tranAbnoinfo.setTransTime(transHourInfo.getTransTime());
					addTransInfoRepList.add(tranAbnoinfo);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return addTransInfoRepList;

	}

	public List analyseAbnormitBlack(String date) {
		List addTransInfoBlackList = new ArrayList();
		try {
			String strSql = "  from TransHourInfo  t1 where blNum >0  and  transCode ='DEP' and reserve1='1' and  createDate <='"
					+ date + "'";
			String strBlackSql = "   from BlackRegulation   where regulationStatus ='9' ";
			List blackList = this.getHibernateTemplate().find(strBlackSql);
			if (blackList != null && blackList.size() > 0) {
				List list = this.getHibernateTemplate().find(strSql);

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String transId = "";
						String seq = "";
						TransHourInfo transHourInfo = (TransHourInfo) list
								.get(i);
						transId = transHourInfo.getId();
						boolean tempFlag = false;
						String blackSeqNote = "";
						for (int k = 0; k < blackList.size() && !tempFlag; k++) {
							if ((BlackRegulation) blackList.get(k) != null) {
								String blackSeq = ((BlackRegulation) blackList
										.get(k)).getRegulation().toString()
										.replace("*", "%");
								String strSubSql = " select count(*)  from  TransBanknoteSeqHour    where tranId ='"
										+ transId
										+ "' and noteFlag  ='0' and noteType ='201' and seriaNo  like '"
										+ blackSeq + "'";
								List subSeqBlack = this.getHibernateTemplate()
										.find(strSubSql);
								if (subSeqBlack != null
										&& subSeqBlack.size() > 0
										&& subSeqBlack.get(0) != null
										&& (Integer.parseInt(String
												.valueOf(subSeqBlack.get(0))) > 0)) {

									tempFlag = true;
									blackSeqNote = blackSeq.replace("%", "*");
								}
							}

						}

						TranAbnoinfo tranAbnoinfo = new TranAbnoinfo();
						tranAbnoinfo.setAccountNo(transHourInfo.getAccountNo());
						tranAbnoinfo.setAnyDate(new Date());
						tranAbnoinfo.setTransNotenum(transHourInfo.getNoteNum());
						tranAbnoinfo.setBlackNotenum(transHourInfo.getBlNum());
						tranAbnoinfo.setCallbackNotenum(transHourInfo
								.getCallBackNum());
						tranAbnoinfo.setCreateDate(transHourInfo
								.getCreateDate());
						tranAbnoinfo.setJournalNo(transHourInfo.getJournalNo());
						tranAbnoinfo.setNote(seq);
						tranAbnoinfo.setRegulation(blackSeqNote);
						tranAbnoinfo.setRepeatNotenum(transHourInfo
								.getRepeatNum());
						tranAbnoinfo.setReserve1(transHourInfo.getReserve1());
						tranAbnoinfo.setReserve2(transHourInfo.getReserve2());
						tranAbnoinfo.setReserve3(transHourInfo.getReserve3());
						tranAbnoinfo.setTermid(transHourInfo.getTermid());
						tranAbnoinfo.setTranId(transHourInfo.getId());
						tranAbnoinfo.setTransAmt(transHourInfo.getTransAmt());
						tranAbnoinfo.setTransCode(transHourInfo.getTransCode());
						tranAbnoinfo.setTransDate(transHourInfo.getTransDate());
						tranAbnoinfo.setTransOrgid(transHourInfo
								.getTransOrgid());
						tranAbnoinfo.setTransResult(transHourInfo
								.getTransResult());
						tranAbnoinfo.setTransTime(transHourInfo.getTransTime());
						addTransInfoBlackList.add(tranAbnoinfo);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return addTransInfoBlackList;

	}

	public List analyseAbnormitCallback(String date) {
		List addTransInfoCallList = new ArrayList();
		try {
			String strSql = "  from TransHourInfo  t where  callBackNum >0 and transCode ='CWD' and reserve1='1'  and createDate  <='"
					+ date + "'";

			List list = this.getHibernateTemplate().find(strSql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String tranId = "";
					String callSeq = "";
					TransHourInfo transHourInfo = (TransHourInfo) list.get(i);
					tranId = transHourInfo.getId();
					boolean tempFlag = false;
					String strSubSql = " select seriaNo  from  TransBanknoteSeqHour  where tranId='"
							+ tranId
							+ "' and noteFlag ='1' and ( seriaNo   not in  (  select  seriaNo   from TransBanknoteSeqHour  where  tranId ='"
							+ tranId
							+ "' and noteFlag ='0' ) or seriaNo  is null )";
					List subListCall = this.getHibernateTemplate().find(
							strSubSql);			
			
					// 张数一致，分析是否序列号一致
					/*
					 * transHourInfo.getNoteNum()与transHourInfo.getCallBackNum()是对象。
					 * transHourInfo.getNoteNum()=transHourInfo.getCallBackNum()永远是false
					 */
					String noteNum=String.valueOf(transHourInfo.getNoteNum());
					String callBackNum=String.valueOf(transHourInfo.getCallBackNum());
					if (noteNum.equals(callBackNum)) {
						
						// 张数一致，如果序列号一致不要监控，这里只监控张数一致，序号不一致的数据
						// 1：张数不一致
						// 2：张数一致，序列号不一致
						// 3：张数和序号都不一致
						callSeq = "";
						if (subListCall != null && subListCall.size() > 0) {
							for (int k = 0; k < subListCall.size(); k++) {
								Object obj = subListCall.get(k);
								if (obj == null || obj.toString().length() == 0)
									callSeq += "-1,";
								else
									callSeq += obj.toString() + ",";
							}
							TranCallbackAbnoinfo tranCallbackAbnoinfo = new TranCallbackAbnoinfo();
							tranCallbackAbnoinfo.setAbnoCallbackType("2");
							tranCallbackAbnoinfo.setAccountNo(transHourInfo
									.getAccountNo());
							tranCallbackAbnoinfo.setAnyDate(new Date());
							//tranCallbackAbnoinfo.setTransNotenum(transHourInfo.getNoteNum());
							tranCallbackAbnoinfo.setBlackNotenum(transHourInfo
									.getBlNum());
							tranCallbackAbnoinfo
									.setCallbackNotenum(transHourInfo
											.getCallBackNum());
							tranCallbackAbnoinfo.setCreateDate(transHourInfo
									.getCreateDate());
							tranCallbackAbnoinfo.setJournalNo(transHourInfo
									.getJournalNo());
							tranCallbackAbnoinfo.setNote(callSeq);
							tranCallbackAbnoinfo.setRepeatNotenum(transHourInfo
									.getRepeatNum());
							tranCallbackAbnoinfo.setReserve1(transHourInfo
									.getReserve1());
							tranCallbackAbnoinfo.setReserve2(transHourInfo
									.getReserve2());
							tranCallbackAbnoinfo.setReserve3(transHourInfo
									.getReserve3());
							tranCallbackAbnoinfo.setTermid(transHourInfo
									.getTermid());
							tranCallbackAbnoinfo.setTranId(transHourInfo
									.getId());
							tranCallbackAbnoinfo.setTransAmt(transHourInfo
									.getTransAmt());
							tranCallbackAbnoinfo.setTransCode(transHourInfo
									.getTransCode());
							tranCallbackAbnoinfo.setTransDate(transHourInfo
									.getTransDate());
							tranCallbackAbnoinfo.setTransNotenum(transHourInfo
									.getNoteNum());
							tranCallbackAbnoinfo.setTransOrgid(transHourInfo
									.getTransOrgid());
							tranCallbackAbnoinfo.setTransResult(transHourInfo
									.getTransResult());
							tranCallbackAbnoinfo.setTransTime(transHourInfo
									.getTransTime());
							addTransInfoCallList.add(tranCallbackAbnoinfo);
						}
					} else {// 回收钞票张数不一致
						callSeq = "";
						String callBackType = "";
						// 张数不一致，而且回收的序号也不一致
						if (subListCall != null && subListCall.size() > 0) {
							for (int k = 0; k < subListCall.size(); k++) {
								Object obj = subListCall.get(k);
								if (obj == null || obj.toString().length() == 0)
									callSeq += "-1,";
								else
									callSeq += obj.toString() + ",";
							}
							callBackType = "3";
						} else {// 回收张数不一致，但是回收的序列号在交易的序列号中可以找到，这种情况基本上不会出现
							callBackType = "1";
						}
						TranCallbackAbnoinfo tranCallbackAbnoinfo = new TranCallbackAbnoinfo();
						tranCallbackAbnoinfo.setAbnoCallbackType(callBackType);
						tranCallbackAbnoinfo.setAccountNo(transHourInfo
								.getAccountNo());
						tranCallbackAbnoinfo.setAnyDate(new Date());
						//tranCallbackAbnoinfo.setTransNotenum(transHourInfo.getNoteNum());
						tranCallbackAbnoinfo.setBlackNotenum(transHourInfo
								.getBlNum());
						tranCallbackAbnoinfo.setCallbackNotenum(transHourInfo
								.getCallBackNum());
						tranCallbackAbnoinfo.setCreateDate(transHourInfo
								.getCreateDate());
						tranCallbackAbnoinfo.setJournalNo(transHourInfo
								.getJournalNo());
						tranCallbackAbnoinfo.setNote(callSeq);
						tranCallbackAbnoinfo.setRepeatNotenum(transHourInfo
								.getRepeatNum());
						tranCallbackAbnoinfo.setReserve1(transHourInfo
								.getReserve1());
						tranCallbackAbnoinfo.setReserve2(transHourInfo
								.getReserve2());
						tranCallbackAbnoinfo.setReserve3(transHourInfo
								.getReserve3());
						tranCallbackAbnoinfo.setTermid(transHourInfo
								.getTermid());
						tranCallbackAbnoinfo.setTranId(transHourInfo.getId());
						tranCallbackAbnoinfo.setTransAmt(transHourInfo
								.getTransAmt());
						tranCallbackAbnoinfo.setTransCode(transHourInfo
								.getTransCode());
						tranCallbackAbnoinfo.setTransDate(transHourInfo
								.getTransDate());
						tranCallbackAbnoinfo.setTransNotenum(transHourInfo
								.getNoteNum());
						tranCallbackAbnoinfo.setTransOrgid(transHourInfo
								.getTransOrgid());
						tranCallbackAbnoinfo.setTransResult(transHourInfo
								.getTransResult());
						tranCallbackAbnoinfo.setTransTime(transHourInfo
								.getTransTime());
						addTransInfoCallList.add(tranCallbackAbnoinfo);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return addTransInfoCallList;
	}

	public List analyseAbnormitEspecial(String date) {
		List addTransInfoEspecialList = new ArrayList();
		try {

			String strEspecialRuleSql = "   from  SpecialRegulation ";
			String especialOneSeq = "";
			String especialTwoSeq = "";
			List especialRuleList = this.getHibernateTemplate().find(
					strEspecialRuleSql);
			if (especialRuleList != null && especialRuleList.size() > 0) {
				for (int i = 0; i < especialRuleList.size(); i++) {
					SpecialRegulation specialRegulation = (SpecialRegulation) especialRuleList
							.get(i);
					String strEspecialSql = "";
					// 单规则
					if (specialRegulation.getSpecialType() != null
							&& specialRegulation.getSpecialType().equals("0")) {
						if (specialRegulation.getRegulation() != null) {
							especialOneSeq = specialRegulation.getRegulation()
									.replace("*", "%");
						}
						strEspecialSql = "  from TransHourInfo  t  where   t.reserve1='1' and t.id in(  select distinct tbs.tranId   from TransBanknoteSeqHour  tbs    where tbs.seriaNo  like '"
								+ especialOneSeq
								+ "' and  tbs.createDate <='"
								+ date + "')";
					} else if (specialRegulation.getSpecialType() != null
							&& specialRegulation.getSpecialType().equals("1")) {
						// 范围规则
						if (specialRegulation.getRegulation() != null
								&& specialRegulation.getRegulation().length() >= 21) {
							String temp[] = specialRegulation.getRegulation()
									.trim().split("-");
							if (temp.length == 2) {
								especialOneSeq = temp[0];
								especialTwoSeq = temp[1];
								especialOneSeq = especialOneSeq.replace("*",
										"%");
								especialTwoSeq = especialTwoSeq.replace("*",
										"%");
							}

						}
						strEspecialSql = "  from TransHourInfo  t  where  t.reserve1='1' and  t.id in(  select distinct tbs.tranId   from TransBanknoteSeqHour  tbs    where tbs.seriaNo <='"
								+ especialTwoSeq
								+ "' and tbs.seriaNo >='"
								+ especialOneSeq
								+ "'  and tbs.createDate <='"
								+ date + "')";
					}

					if (strEspecialSql.trim().length() > 0) {
						List specialList = this.getHibernateTemplate().find(
								strEspecialSql);
						for (int k = 0; k < specialList.size(); k++) {
							TransHourInfo transHourInfo = (TransHourInfo) specialList
									.get(k);
							TranEspeciinfo tranEspeciinfo = new TranEspeciinfo();
							tranEspeciinfo.setAccountNo(transHourInfo
									.getAccountNo());
							tranEspeciinfo.setAnyDate(new Date());
							tranEspeciinfo.setBlackNotenum(transHourInfo
									.getBlNum());
							tranEspeciinfo.setCallbackNotenum(transHourInfo
									.getCallBackNum());
							tranEspeciinfo.setCometype(specialRegulation
									.getSource());
							tranEspeciinfo.setCreateDate(transHourInfo
									.getCreateDate());
							tranEspeciinfo.setCurrency(specialRegulation
									.getMoneyType());
							tranEspeciinfo.setDenomination(specialRegulation
									.getMoneyDenomination());
							tranEspeciinfo.setEspeciseq(specialRegulation
									.getRegulation());
							tranEspeciinfo.setJournalNo(transHourInfo
									.getJournalNo());
							tranEspeciinfo.setRepeatNotenum(transHourInfo
									.getRepeatNum());
							tranEspeciinfo.setReserve1(transHourInfo
									.getReserve1());
							tranEspeciinfo.setReserve2(transHourInfo
									.getReserve2());
							tranEspeciinfo.setReserve3(transHourInfo
									.getReserve3());
							tranEspeciinfo.setResult(specialRegulation
									.getResult());
							tranEspeciinfo.setSequence("");
							tranEspeciinfo.setTermid(transHourInfo.getTermid());
							tranEspeciinfo.setTranId(transHourInfo.getId());
							tranEspeciinfo.setTransAmt(transHourInfo
									.getTransAmt());
							tranEspeciinfo.setTransCode(transHourInfo
									.getTransCode());
							tranEspeciinfo.setTransDate(transHourInfo
									.getTransDate());
							tranEspeciinfo.setTransNotenum(transHourInfo
									.getNoteNum());
							tranEspeciinfo.setTransOrgid(transHourInfo
									.getTransOrgid());
							tranEspeciinfo.setTransResult(transHourInfo
									.getTransResult());
							tranEspeciinfo.setTransTime(transHourInfo
									.getTransTime());
							tranEspeciinfo.setSpecialId(specialRegulation
									.getId());
							tranEspeciinfo.setNote(specialRegulation.getResult());

							addTransInfoEspecialList.add(tranEspeciinfo);
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return addTransInfoEspecialList;
	}

	public boolean removeTranHourDate(String date) {
		boolean flag = false;
		System.out.println("date start remove:" + new Date());
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();

		Connection cnn = session.connection();
		try {
			String strInsertTranInfo = " insert into Tran_Info  select * from Trans_Hour_Info  where   reserve1='1' and c_create_Date <='"
					+ date + "'";
			String strInsertTranSeq = "insert into Trans_Banknote_Seq   select * from Trans_Banknote_Seq_Hour   where   c_create_Date <='"
					+ date
					+ "' and c_tran_Id  in(select c_id  from Trans_Hour_Info  where   reserve1='1' and c_create_Date <='"
					+ date + "')";

			String strDelTranSeq = "delete Trans_Banknote_Seq_Hour  where  c_create_Date <='"
					+ date
					+ "' and c_tran_Id  in(select c_id  from  Trans_Hour_Info  where   reserve1='1' and  c_create_Date  <='"
					+ date + "')";
			String strDelInfo = "  delete Trans_Hour_Info   where   reserve1='1' and c_create_Date  <='"
					+ date + "'";

			Statement stmt = cnn.createStatement();
			stmt.execute(strInsertTranInfo);
			stmt.execute(strInsertTranSeq);
			stmt.execute(strDelTranSeq);
			stmt.execute(strDelInfo);

			cnn.commit();

			session.close();
			// this.getHibernateTemplate().execute(strInsertTranInfo);
			// this.getHibernateTemplate().bulkUpdate(strInsertTranSeq);
			// this.getHibernateTemplate().bulkUpdate(strDelTranSeq);
			// this.getHibernateTemplate().bulkUpdate(strDelInfo);
			flag = true;
			System.out.println("end remove date:" + new Date());
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			try {
				cnn.rollback();
				cnn.close();
				session.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		}
		return flag;
	}
	public boolean removeTranHourDateList(String date) {
		boolean flag = false;
		
		System.out.println("date start remove:" + new Date());
		//插入交易信息中的数据
		List insertTranInfoList=null;
		//插入钞票序列号的信息
		List insertTransBanknoteList=null;
		/*
		 * 移出数据的时候，查询已经上传成功的信息。
		 * 插入数据的时候，先插入主交易信息，然后插入序列号信息
		 * 删除数据的时候，先删除序列号信息，然后在删除主交易信息
		 * 
		 */
		String insertTranInfoStr= "  from TransHourInfo  where   reserve1='1' and createDate <='"+ date + "'";
		String insertTransBanknoteStr=" from TransBanknoteSeqHour   where   createDate <='"+ date
			+ "' and tranId  in(select id  from TransHourInfo  where   reserve1='1' and createDate <='"+ date + "')";
		String deleteTranInfoHour= "  delete TransHourInfo   where   reserve1='1' and createDate  <='"
			+ date + "'";
		String deleteTranBanknoteStr="delete TransBanknoteSeqHour  where  createDate <='"+ date
			+ "' and tranId  in(select id  from  TransHourInfo  where   reserve1='1' and  createDate  <='"
			+ date + "')";
		
		insertTranInfoList=this.getHibernateTemplate().find(insertTranInfoStr);
		insertTransBanknoteList=this.getHibernateTemplate().find(insertTransBanknoteStr);
		
		if(insertTranInfoList!=null&&insertTranInfoList.size()>0){
			for(int i=0;i<insertTranInfoList.size();i++){
				TranInfo  tranInfo=(TranInfo)insertTranInfoList.get(i);
				
					this.getHibernateTemplate().save(tranInfo);
				
				
			}
		}
		if(insertTransBanknoteList!=null&&insertTransBanknoteList.size()>0){
			for(int i=0;i<insertTransBanknoteList.size();i++){
				TransBanknoteSeq transBanknoteSeq=(TransBanknoteSeq)insertTransBanknoteList.get(i);
				this.getHibernateTemplate().save(transBanknoteSeq);
			}
		}
		//一定要先删除序列号的信息，在删除交易信息
		this.getHibernateTemplate().bulkUpdate(deleteTranBanknoteStr);
		this.getHibernateTemplate().bulkUpdate(deleteTranInfoHour);
		System.out.println("end remove date:" + new Date());
		
		/*
		System.out.println("date start remove:" + new Date());
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();

		Connection cnn = session.connection();
		try {
			String strInsertTranInfo = " insert into Tran_Info  select * from Trans_Hour_Info  where   reserve1='1' and c_create_Date <='"
					+ date + "'";
			String strInsertTranSeq = "insert into Trans_Banknote_Seq   select * from Trans_Banknote_Seq_Hour   where   c_create_Date <='"
					+ date
					+ "' and c_tran_Id  in(select c_id  from Trans_Hour_Info  where   reserve1='1' and c_create_Date <='"
					+ date + "')";

			String strDelTranSeq = "delete Trans_Banknote_Seq_Hour  where  c_create_Date <='"
					+ date
					+ "' and c_tran_Id  in(select c_id  from  Trans_Hour_Info  where   reserve1='1' and  c_create_Date  <='"
					+ date + "')";
			String strDelInfo = "  delete Trans_Hour_Info   where   reserve1='1' and c_create_Date  <='"
					+ date + "'";

			Statement stmt = cnn.createStatement();
			stmt.execute(strInsertTranInfo);
			stmt.execute(strInsertTranSeq);
			stmt.execute(strDelTranSeq);
			stmt.execute(strDelInfo);

			cnn.commit();

			session.close();
			// this.getHibernateTemplate().execute(strInsertTranInfo);
			// this.getHibernateTemplate().bulkUpdate(strInsertTranSeq);
			// this.getHibernateTemplate().bulkUpdate(strDelTranSeq);
			// this.getHibernateTemplate().bulkUpdate(strDelInfo);
			flag = true;
			System.out.println("end remove date:" + new Date());
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			try {
				cnn.rollback();
				cnn.close();
				session.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}

		}
		*/
		return flag;
	}

	public boolean analyseTranDataAdd(List<TranAbnoinfo> abnoBlackList,
			List<TranAbnoinfo> abnoRepeatList,
			List<TranCallbackAbnoinfo> tranCallbackAbnoinfo,
			List<TranEspeciinfo> tranEspeciinfo) {
		boolean flag = false;
		for (int i = 0; i < abnoBlackList.size(); ++i) {

			this.getHibernateTemplate().save(
					(TranAbnoinfo) abnoBlackList.get(i));
			if (i % 50 == 0)
				this.getHibernateTemplate().flush();
		}
		for (int i = 0; i < abnoRepeatList.size(); i++) {
			this.getHibernateTemplate().save(
					(TranAbnoinfo) abnoRepeatList.get(i));
			if (i % 50 == 0)
				this.getHibernateTemplate().flush();
		}
		for (int i = 0; i < tranCallbackAbnoinfo.size(); i++) {
			this.getHibernateTemplate().save(
					(TranCallbackAbnoinfo) tranCallbackAbnoinfo.get(i));
			if (i % 50 == 0)
				this.getHibernateTemplate().flush();
		}
		for (int i = 0; i < tranEspeciinfo.size(); i++) {
			this.getHibernateTemplate().save(
					(TranEspeciinfo) tranEspeciinfo.get(i));
			if (i % 50 == 0)
				this.getHibernateTemplate().flush();
		}
		flag = true;
		return flag;
	}

}
