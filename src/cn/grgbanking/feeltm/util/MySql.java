package cn.grgbanking.feeltm.util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.dao.BaseDao;

@SuppressWarnings({"unchecked", "deprecation"})
public class MySql extends BaseDao {
	protected Class getDomainClass() {
		// TODO Auto-generated method stub
		return MySql.class;
	}

	/**
	 * 执行Statement查询语句,动态生成数据 ，List中嵌套Map
	 */

	public List executeQuery(String sql, String pro) {
		SessionFactory sessionFactory = null;
		Session session = null;
		Statement stmt = null;
		ResultSetMetaData meta = null;
		ResultSet rst = null;
		try {
			sessionFactory = (SessionFactory) BaseApplicationContext
					.getAppContext().getBean("sessionFactory");
			session = sessionFactory.openSession();

			List list = new ArrayList();
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (stmt == null)
				stmt = session.connection().createStatement();
			if (rst != null) {
				rst.close();
				rst = null;
			}
			rst = stmt.executeQuery(sql);
			if (rst != null)
				meta = rst.getMetaData();
			int count = 0;
			count = meta.getColumnCount();

			String[] columnName = new String[count];
			for (int i = 0; i < count; i++) {
				columnName[i] = meta.getColumnName(i + 1);

			}
			while ((rst.next())) {
				HashMap rsMap = new HashMap();
				for (int i = 0; i < count; i++) {

					String temp = "";
					if (columnName[i] != null)
						temp = columnName[i];
					if (temp.startsWith("I_"))

					{
						String a = String.valueOf(rst.getDouble(columnName[i]));
						if (a != null && !a.equals("null") && a.endsWith(".0"))
							a = a.substring(0, a.length() - 2);

						rsMap.put(columnName[i], a);
					} else
						rsMap.put(columnName[i], rst.getString(columnName[i]));
				}

				list.add(rsMap);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error("Mysql excute query error:" + e);
			return null;
		} finally {
			try {
				if (meta != null) {
					// meta.close();
					meta = null;
				}
				if (rst != null) {
					rst.close();
					rst = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception ee) {
				SysLog.error(ee);
			}
		}

	}

	/**
	 * 执行Statement查询语句,动态生成数据,List中嵌套List
	 */

	public List executeQueryList(String sql, String pro) {
		SessionFactory sessionFactory = null;
		Session session = null;
		Statement stmt = null;
		ResultSetMetaData meta = null;
		ResultSet rst = null;
		try {
			sessionFactory = (SessionFactory) BaseApplicationContext
					.getAppContext().getBean("sessionFactory");
			session = sessionFactory.openSession();
			List list = new ArrayList();
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (stmt == null)
				stmt = session.connection().createStatement();
			if (rst != null) {
				rst.close();
				rst = null;
			}
			rst = stmt.executeQuery(sql);
			if (rst != null)
				meta = rst.getMetaData();
			int count = 0;
			count = meta.getColumnCount();

			String[] columnName = new String[count];
			for (int i = 0; i < count; i++) {
				columnName[i] = meta.getColumnName(i + 1);

			}
			while ((rst.next())) {
				List rsList = new ArrayList();
				for (int i = 0; i < count; i++) {
					String temp = "";
					if (columnName[i] != null)
						temp = columnName[i];
					if (temp.startsWith("I_"))

					{
						String a = String.valueOf(rst.getDouble(columnName[i]));
						if (a != null && !a.equals("null") && a.endsWith(".0"))
							a = a.substring(0, a.length() - 2);

						rsList.add(a);
					} else
						rsList.add(rst.getString(columnName[i]));
				}

				list.add(rsList);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error("Mysql excute query error:" + e);
			return null;
		} finally {
			try {

				if (meta != null) {
					// meta.close();
					meta = null;
				}
				if (rst != null) {
					rst.close();
					rst = null;
				}

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception ee) {
				SysLog.error(ee);
			}
		}

	}

	/**
	 * 执行Statement更改语句
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) throws SQLException {
		SessionFactory sessionFactory = null;
		Session session = null;

		try {
			sessionFactory = (SessionFactory) BaseApplicationContext
					.getAppContext().getBean("sessionFactory");
			session = sessionFactory.openSession();

			if (session.connection().createStatement().executeUpdate(sql) <= 0)
				return false;
			return true;
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			System.out.println("the SQLState is " + e.getSQLState());
			System.out.println("error,sql=" + sql);
			e.printStackTrace();
			SysLog.error("Mysql excute update error sql=" + sql);
			// CLog.writeLog(new
			// Exception().getStackTrace()[0].getClassName()+":"+new
			// Exception().getStackTrace()[0].getMethodName()+":"+new
			// Exception().getStackTrace()[0].getLineNumber());
			return false;
		} finally {
			try {

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception e) {
				SysLog.error(e);
			}
		}
	}

	/**
	 * 删除
	 */
	public boolean delete(String sql) {

		try {

			return executeUpdate(sql);
		} catch (Exception e6) {
			e6.printStackTrace();
			SysLog.error(e6);
			return false;
		}

	}

	/*
	 * 事务批处理
	 */
	public int dbPatchDeal(String var[]) {
		String strSql = "";
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = (SessionFactory) BaseApplicationContext
					.getAppContext().getBean("sessionFactory");
			session = sessionFactory.openSession();
			session.connection().setAutoCommit(false);

			if (var != null) {
				for (int i = 0; i < var.length; i++) {
					strSql = var[i];
					if (strSql != null && strSql.length() > 0)
						session.connection().createStatement().executeUpdate(
								strSql);
				}
			}

			session.connection().commit(); // 统一提交。

		} catch (Exception e) {
			try {
				session.connection().rollback();

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception ee) {
				SysLog.error(ee);
				System.out.println("sql" + strSql);

			}
			System.out.println("sql" + strSql);

			return -1;
		} finally {
			try {

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception ee) {
				SysLog.error(ee);
				ee.printStackTrace();
			}
		}

		return 1;
	}

	/*
	 * 存储过程
	 */
	public int execPre(String porName) {
		SessionFactory sessionFactory = null;
		Session session = null;
		CallableStatement stmt = null;
		try {

			sessionFactory = (SessionFactory) BaseApplicationContext
					.getAppContext().getBean("sessionFactory");
			session = sessionFactory.openSession();
			stmt = session.connection().prepareCall("{call   " + porName + "}");
			stmt.execute();

			return 1;
		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			return -1;
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (session != null) {
					session.close();
					session = null;
				}
				if (sessionFactory != null) {
					sessionFactory.close();
					sessionFactory = null;
				}

			} catch (Exception ee) {
				SysLog.error(ee);

				return -1;
			}
		}
	}

}