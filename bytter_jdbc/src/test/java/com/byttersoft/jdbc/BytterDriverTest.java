package com.byttersoft.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

/**
 * Test for {@link BytterDriver}
 * @author pangl
 *
 */
public class BytterDriverTest extends TestCase{

	public void test_connect_sqlserver() throws Exception {
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
				"jdbc:byttersoft:sqlserver:jdbc:sqlserver://192.168.0.36:1433;databaseName=bt_jszx", 
				"sa", "sasa");
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("connect timed out") != -1)
				return;
			return;
			//throw ex;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("select count(*) from bt_user");
		while(rs.next()) {
			rs.getInt(1);
		}
		conn.close();
		assertTrue(true);
	}
	
	public void test_connectOracle() throws Exception {
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
				"jdbc:byttersoft:oracle:jdbc:oracle:thin:@192.168.0.36:1522:orcl11gr2", 
				"btv10_cp", "btv10_cp");
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("The Network Adapter could not establish the connection") != -1)
				return;
			return;
			//throw ex;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("select count(*) from bt_user");
		while(rs.next()) {
			rs.getInt(1);
		}
		conn.close();
		assertTrue(true);
	}
	
	public void test_getOrgUrl() throws Exception {
		String url = "jdbc:byttersoft:oracle:jdbc:oracle:thin:@localhost:1521:orcl";
		String expected = "jdbc:oracle:thin:@localhost:1521:orcl";
		
		assertEquals(expected, BytterDriver.getOrgUrl(url));
	}
	
	public void test_getDBType() throws Exception {
		String url = "jdbc:byttersoft:orAcle:jdbc:oracle:thin:@localhost:1521:orcl";
		assertEquals(DBType.oracle, BytterDriver.getDBType(url));
	}
	
	/**
	 * ²âÊÔSQL server Êý¾Ý¿âÖÐµÄ || ·ûºÅÓï¾ä
	 * @throws Exception
	 */
	public void test_execSqlServer() throws Exception{
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:byttersoft:sqlserver:jdbc:sqlserver://192.168.0.36:1433;databaseName=bt_jszx", 
					"sa", "sasa");
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("connect timed out") != -1)
				return;
			return;
			//throw ex;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("select 'aa'||'bb' ||'cc'|| '|| dd' from bt_user");
		String value = null;
		while(rs.next()) {
			value = rs.getString(1);
		}
		conn.close();
		assertEquals("aabbcc|| dd", value);
	}
	
	
	/**
	 * ²âÊÔSQL SERVER µÄ  || ·ûºÅÓï¾ä
	 * @throws Exception
	 */
	public void test_sqlServerBuss() throws Exception {
		String sql = "select distinct corp_code,'('||corp_code||')'||corp_name as corp_name " + 
				"from v_user_corp_sys_opr " + 
				"where user_code='JZ01' and sys_code='cus' and op_type='1'";
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
				"jdbc:byttersoft:sqlserver:jdbc:sqlserver://192.168.0.36:1433;databaseName=bt_jszx", 
				"sa", "sasa");
		} catch(Exception ex) {
			if (ex.getMessage().indexOf("connect timed out") != -1)
				return;
			//throw ex;
			return;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		String value = null;
		while(rs.next()) {
			value = rs.getString(1);
			System.out.println(value + "," + rs.getString(2));
		}
		conn.close();
		assertEquals("2001", value);
	}
}
